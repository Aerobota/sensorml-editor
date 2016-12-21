package com.sensia.tools.client.swetools.editors.sensorml.renderer.editor.panels.sml.position;

import java.awt.Panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.sensia.relaxNG.RNGAttribute;
import com.sensia.relaxNG.RNGElement;
import com.sensia.relaxNG.RNGTag;
import com.sensia.tools.client.swetools.editors.sensorml.panels.IPanel;
import com.sensia.tools.client.swetools.editors.sensorml.panels.IRefreshHandler;
import com.sensia.tools.client.swetools.editors.sensorml.panels.map.GenericLineMap;
import com.sensia.tools.client.swetools.editors.sensorml.panels.map.GenericPointMap;
import com.sensia.tools.client.swetools.editors.sensorml.renderer.editor.panels.element.EditSubSectionElementPanel;
import com.sensia.tools.client.swetools.editors.sensorml.renderer.editor.panels.swe.SWEEditDataArrayPanel;
import com.sensia.tools.client.swetools.editors.sensorml.utils.CloseDialog;
import com.sensia.tools.client.swetools.editors.sensorml.utils.ModelHelper;
import com.sensia.tools.client.swetools.editors.sensorml.utils.Utils;

public class SMLEditPositionByTrajectoryPanel extends EditSubSectionElementPanel{

	protected String epsg;
	protected Object[][] values;
	
	public SMLEditPositionByTrajectoryPanel(RNGElement tag, IRefreshHandler refreshHandler) {
		super(tag, refreshHandler);
	}

	@Override
	protected void addInnerElement(IPanel<? extends RNGTag> element) {
		if(element.getName().equals("DataArray") && element instanceof SWEEditDataArrayPanel) {
			SWEEditDataArrayPanel panel = (SWEEditDataArrayPanel) element;
			panel.setGraphVisible(false);
			
			// build map icon
			
			// get values
			values = panel.getValues();
			
			// looking for EPSG
			RNGAttribute referenceFrame = ModelHelper.findAttributeValue("referenceFrame",element.getTag());
			if(referenceFrame != null) {
				epsg = referenceFrame.getChildValueText();
			}
			
			Image mapIcon = new Image(GWT.getModuleBaseURL()+"images/maps-icon.png");
			
			mapIcon.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					GenericLineMap lineMap = getLineMapPanel();
					createDialog(lineMap);
				}
			});
			
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.add(mapIcon);
			panel.appendToLine(hPanel);
			
		}
		super.addInnerElement(element);
	}
	
	protected GenericLineMap getLineMapPanel() {
		
		// parse Object into Double
		double [] [] latLonValues = new double[values.length][values[0].length];
		for(int i=0;i < values.length;i++){
			for(int j=0;j < values[0].length;j++){
				GWT.log(values[i][j]+"");
				GWT.log(values[i][j].toString()+"");
				latLonValues[i] [j] = Double.parseDouble(values[i][j].toString().trim());
			}
		}
		return new GenericLineMap(latLonValues, epsg, true);
	}
	
	protected void createDialog(final GenericLineMap lineMap) {
		CloseDialog dialog = Utils.displaySaveDialogBox(lineMap.getPanel(), "Line position");
		dialog.addSaveHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// save the position
				
				//TODO:
				if(refreshHandler != null) {
					refreshHandler.refresh();
				}
			}
		});
	}
}
