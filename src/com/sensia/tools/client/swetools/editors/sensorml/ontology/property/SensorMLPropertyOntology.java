/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2016 DHAINAUT.
 All Rights Reserved.
 
 Contributor(s): 
    Mathieu DHAINAUT <mathieu.dhainaut@gmail.com>
 
 ******************************* END LICENSE BLOCK ***************************/

package com.sensia.tools.client.swetools.editors.sensorml.ontology.property;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.sensia.tools.client.swetools.editors.sensorml.listeners.ILoadFileCallback;
import com.sensia.tools.client.swetools.editors.sensorml.utils.Utils;

/**
 * The Class SensorMLPropertyOntology.
 */
public class SensorMLPropertyOntology implements IOntologyPropertyReader{

	/** The original data. */
	private List<Property> originalData;
	
	/**
	 * Instantiates a new sensor ml property ontology.
	 */
	public SensorMLPropertyOntology() {
		originalData = new ArrayList<Property>();
	}
	
	/* (non-Javadoc)
	 * @see com.sensia.tools.client.swetools.editors.sensorml.ontology.property.IOntologyPropertyReader#loadOntology(java.lang.String, com.sensia.tools.client.swetools.editors.sensorml.ontology.property.ILoadOntologyCallback)
	 */
	public void loadOntology(String url,final ILoadOntologyCallback callback) {
		originalData.clear();
		
		//load the ontology
		ILoadFileCallback cb = new ILoadFileCallback() {
			@Override
			public void onLoad(String content) {
				//parses the content
				Document ontologyRoot = XMLParser.parse(content);
				parseOntology(ontologyRoot.getDocumentElement());
				//callback the result
				callback.onLoad(getHeaders(), getValuesFromData());
			}
		};
		
		Utils.getFile(url, cb);
	}
	
	/**
	 * Gets the headers.
	 *
	 * @return the headers
	 */
	private List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		headers.add("Definition URL");
		headers.add("Title");
		headers.add("Definition");
		headers.add("Creator");
		headers.add("PreLabel");
		return headers;
	}
	
	/**
	 * Gets the values from data.
	 *
	 * @return the values from data
	 */
	private Object[][] getValuesFromData() {
		int colNumber = (!originalData.isEmpty())? 5 : 0;
		
		Object [][] values = new Object[originalData.size()] [colNumber];
		
		int i=0;
		for(Property p : originalData) {
			values[i][0] = p.getDefUrl();
			values[i][1] = p.getTitle();
			values[i][2] = p.getDef();
			values[i][3] = p.getCreator();
			values[i][4] = p.getPreLabel();
			i++;
		}
		return values;
	}
	
	/**
	 * Parses the ontology.
	 *
	 * @param element the element
	 */
	private void parseOntology(Element element){
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element elt = (Element) node;
				if(elt.getNodeName().equals("Property")){
					Property property = new Property();
					parseProperty(property,elt);
					originalData.add(property);
					
				} else {
					parseOntology((Element) node);
				}
			}
		}
	}
	
	/**
	 * Parses the property.
	 *
	 * @param property the property
	 * @param propertyNode the property node
	 */
	private void parseProperty(final Property property,Element propertyNode) {
		//add attributes if any
        if (propertyNode.hasAttributes()) {
        	NamedNodeMap attributes = propertyNode.getAttributes();
		
			for (int j = 0; j < attributes.getLength(); j++) {
				Node attr = attributes.item(j);
				if(attr.getNodeName().equals("rdf:about")) {
					property.setDefUrl(attr.getNodeValue());
				}
			}
		}
        
		NodeList children = propertyNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				if(node.getNodeName().equals("skos_definition")) {
					property.setDef(node.getChildNodes().item(0).getNodeValue());
				} else if(node.getNodeName().equals("skos_prefLabel")) {
					property.setPreLabel(node.getChildNodes().item(0).getNodeValue());
				} else if(node.getNodeName().equals("dc_creator")) {
					property.setCreator(node.getChildNodes().item(0).getNodeValue());
				} else if(node.getNodeName().equals("dc_title")) {
					property.setTitle(node.getChildNodes().item(0).getNodeValue());
				}
			} 
		}
	}
	
	/**
	 * The Class Property.
	 */
	private class Property {
		
		/** The def url. */
		private String defUrl;
		
		/** The def. */
		private String def;
		
		/** The pre label. */
		private String preLabel;
		
		/** The creator. */
		private String creator;
		
		/** The title. */
		private String title;
		
		/**
		 * Instantiates a new property.
		 */
		public Property() {
			defUrl = "";
			def = "";
			preLabel = "";
			creator = "";
			title = "";
		}
		
		/**
		 * Gets the def url.
		 *
		 * @return the def url
		 */
		public String getDefUrl() {
			return defUrl;
		}
		
		/**
		 * Sets the def url.
		 *
		 * @param defUrl the new def url
		 */
		public void setDefUrl(String defUrl) {
			this.defUrl = defUrl;
		}
		
		/**
		 * Gets the def.
		 *
		 * @return the def
		 */
		public String getDef() {
			return def;
		}
		
		/**
		 * Sets the def.
		 *
		 * @param def the new def
		 */
		public void setDef(String def) {
			this.def = def;
		}
		
		/**
		 * Gets the pre label.
		 *
		 * @return the pre label
		 */
		public String getPreLabel() {
			return preLabel;
		}
		
		/**
		 * Sets the pre label.
		 *
		 * @param preLabel the new pre label
		 */
		public void setPreLabel(String preLabel) {
			this.preLabel = preLabel;
		}
		
		/**
		 * Gets the creator.
		 *
		 * @return the creator
		 */
		public String getCreator() {
			return creator;
		}
		
		/**
		 * Sets the creator.
		 *
		 * @param creator the new creator
		 */
		public void setCreator(String creator) {
			this.creator = creator;
		}
		
		/**
		 * Gets the title.
		 *
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}
		
		/**
		 * Sets the title.
		 *
		 * @param title the new title
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Property [defUrl=" + defUrl + ", def=" + def + ", preLabel=" + preLabel + ", creator=" + creator + ", title=" + title + "]";
		}
	}
}
