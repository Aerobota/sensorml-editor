/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2016 DHAINAUT.
 All Rights Reserved.
 
 Contributor(s): 
    Mathieu DHAINAUT <mathieu.dhainaut@gmail.com>
 
 ******************************* END LICENSE BLOCK ***************************/

package com.sensia.tools.client.swetools.editors.sensorml.panels;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sensia.gwt.relaxNG.RNGParser;
import com.sensia.gwt.relaxNG.RNGParserCallback;
import com.sensia.relaxNG.RNGGrammar;
import com.sensia.relaxNG.RNGTag;
import com.sensia.tools.client.swetools.editors.sensorml.IParsingObserver;
import com.sensia.tools.client.swetools.editors.sensorml.RNGProcessorSML;
import com.sensia.tools.client.swetools.editors.sensorml.controller.IObserver;
import com.sensia.tools.client.swetools.editors.sensorml.controller.Observable;
import com.sensia.tools.client.swetools.editors.sensorml.listeners.ViewAsXMLButtonClickListener;
import com.sensia.tools.client.swetools.editors.sensorml.old.RNGRendererSML;
import com.sensia.tools.client.swetools.editors.sensorml.panels.IPanel.MODE;
import com.sensia.tools.client.swetools.editors.sensorml.panels.source.ISourcePanel;
import com.sensia.tools.client.swetools.editors.sensorml.panels.source.LocalFileSourcePanel;
import com.sensia.tools.client.swetools.editors.sensorml.panels.source.UrlSourcePanel;

/**
 * This class is in charge of creating the main Panel. The top elements will 
 * manage the file loading while the center panel display the content of the 
 * transformed XML file into a HTML pretty view.
 * @author Mathieu Dhainaut
 *
 */
public class ViewerPanel extends Composite implements IParsingObserver, IObserver {
	private static final long serialVersionUID = -7684111574093800909L;

	//the panel in charge of displaying the HTML content
	private VerticalPanel mainPanel;
	
	//the checkbox to switch between view and edit mode
	private CheckBox editCheckbox;
	private IPanel root;
	
	//the processor in charge of parsing and create the RNG profile
	private RNGProcessorSML smlEditorProcessor;
	
	public ViewerPanel(final RNGProcessorSML sgmlEditorProcessor){
		sgmlEditorProcessor.addObserver(this);
		this.smlEditorProcessor = sgmlEditorProcessor;

		final Panel viewXmlPanel = getXMLViewPanel();

		final VerticalPanel verticalPanel = new VerticalPanel();
		
		//add View as XML button
		Button viewAsXML = new Button("View as XML");
		viewAsXML.addClickHandler(new ViewAsXMLButtonClickListener(sgmlEditorProcessor));
		
		//Get the url parameter to load the document where this one is under the form : ?url=DocumentPath
		String passedFile = com.google.gwt.user.client.Window.Location.getParameter("url");
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(viewXmlPanel);
		panel.add(viewAsXML);
		
		verticalPanel.add(panel);
		
		if(passedFile != null) {
			//load the file given the url passed as parameter
			//do not display the edit/view options
			smlEditorProcessor.setMode(MODE.VIEW);
			smlEditorProcessor.parse(passedFile);
			
			editCheckbox.setVisible(true);
			
		}
		
		mainPanel = new VerticalPanel();
		mainPanel.addStyleName("viewer");
		
		verticalPanel.add(mainPanel);
		initWidget(verticalPanel);
		
	}

	// Get the top elements panel for the XML part
	private Panel getXMLViewPanel() {
		final HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(20);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		HTML title = new HTML("<b>SensorML XML/RNG:</b>");
		final Button load = new Button("Load");
		
		//init radio buttons choices
		final RadioButton fromLocalFileSystem = new RadioButton("myRadioGroup", "from local");
		final RadioButton fromUrl = new RadioButton("myRadioGroup", "from url");
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(fromLocalFileSystem);
		hPanel.add(fromUrl);
		
		editCheckbox = new CheckBox("Edit");
		
		//container for either local file system input panel or url valueBox
		final SimplePanel fromPanel = new SimplePanel();
		
		//init file upload panel
		final ISourcePanel fileUploadPanel = new LocalFileSourcePanel(smlEditorProcessor, editCheckbox);
		
		//init url load
		final ISourcePanel urlDownloadPanel = new UrlSourcePanel(smlEditorProcessor, editCheckbox);
		
		//add to xml panel
		panel.add(title);
		panel.add(hPanel);
		panel.add(fromPanel);
		panel.add(load);
		panel.add(editCheckbox);
		
		//set from local file system panel as default choice
		fromLocalFileSystem.setChecked(true);
		fromPanel.add(fileUploadPanel.getPanel());
				
		//add listener to handle event
		load.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(fromLocalFileSystem.getValue()) {
					fileUploadPanel.parseContent();
				} else if(fromUrl.getValue()){
					urlDownloadPanel.parseContent();
				}
			}
		});
		
		editCheckbox.setVisible(true);
		
		//after clicking on the checkbox, the mode is sent to the tree hierarchy starting from the Root element
		editCheckbox.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(root != null){
					MODE mode = (editCheckbox.isChecked()) ? MODE.EDIT : MODE.VIEW;
					root.switchMode(mode);
				}
			}
		});
		
		//add listener to handle from local file system handler
		fromLocalFileSystem.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()) {
					fromPanel.clear();
					fromPanel.add(fileUploadPanel.getPanel());
				}
			}
		});
		
		//add listener to handle from local file system handler
		fromUrl.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()) {
					fromPanel.clear();
					fromPanel.add(urlDownloadPanel.getPanel());
				}
			}
		});
		
		return panel;
	}
	
	public void parse(RNGGrammar grammar) {
		mainPanel.clear();
		IPanel newNode = smlEditorProcessor.parseRNG(grammar);
		mainPanel.add(newNode.getPanel());
		root = newNode;
	}
	
	public void parse(String xmlDoc) {
		// use MVC
		smlEditorProcessor.parseString(xmlDoc);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sensia.swetools.editors.sensorml.client.IParsingObserver#parseDone(com.sensia.swetools.editors.sensorml.client.panels.model.INodeWidget)
	 */
	@Override
	public void parseDone(final IPanel topElement) {
		//One the parsing done, the viewer is reset and displays the new content
		mainPanel.clear();
		mainPanel.add(topElement.getPanel());
		root = topElement;
	}
	
	@Override
	public void update(Observable model, Object hint) {
		//IPanel newNode = smlEditorProcessor.parseRNG(((RNGTag) model).getParent());
		// replace the old corresponding node by the new node
		mainPanel.clear();
		IPanel newNode = smlEditorProcessor.parseRNG(smlEditorProcessor.getLoadedGrammar());
		mainPanel.add(newNode.getPanel());
		root = newNode;
		//smlEditorProcessor.parse(profiles.get(profileListBox.getValue(profileListBox.getSelectedIndex())));
	}
}