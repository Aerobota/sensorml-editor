package com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.swe;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.AbstractSensorElementWidget;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.ISensorWidget;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.swe.position.SWESensorPositionByDataRecordWidget;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.swe.position.SWESensorPositionByDescriptionWidget;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.swe.position.SWESensorPositionByPointWidget;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.swe.position.SWESensorPositionByTrajectoryWidget;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.swe.position.SWESensorPositionByVectorWidget;

public class SWESensorPositionWidget extends AbstractSensorElementWidget{

	private ISensorWidget sensorPositionPanel;
	
	private Panel container;
	public SWESensorPositionWidget() {
		super("position", TAG_DEF.SWE, TAG_TYPE.ELEMENT);
		container = new HorizontalPanel();
	}

	@Override
	public Panel getPanel() {
		if(sensorPositionPanel != null) {
			return sensorPositionPanel.getPanel();
		} else {
			return container;
		}
	}

	@Override
	protected void addSensorWidget(ISensorWidget widget) {
		//container.add(widget.getPanel());
		//We have to handle many cases:
		//Position by description
		//Position by Point (gml:point)
		//Position by Location (swe:Vector)
		//Position by Position (swe:DataRecord)
		//Position by Trajectory (swe:DataArray)
		//Position by Process (sml:SimpleProcess or sml:AggregateProcess or sml:PhysicalComponent or sml:PhysicalSystem)
		
		if(widget.getName().equals("DataRecord")) {
			sensorPositionPanel = new SWESensorPositionByDataRecordWidget();
		} else if(widget.getName().equals("Vector")) {
			sensorPositionPanel = new SWESensorPositionByVectorWidget();
		} else if(widget.getName().equals("Text")) {
			sensorPositionPanel = new SWESensorPositionByDescriptionWidget();
		} else if(widget.getName().equals("Point")) {
			sensorPositionPanel = new SWESensorPositionByPointWidget();
		} else if(widget.getName().equals("DataArray")) {
			sensorPositionPanel = new SWESensorPositionByTrajectoryWidget();
		}
		
		if(sensorPositionPanel != null) {
			sensorPositionPanel.addElement(widget);
		}
		
		if(sensorPositionPanel == null) {
			GWT.log("unsuported Position, take a default container");
			container.add(widget.getPanel());
		}
	}

	@Override
	public void refresh () {
		if(sensorPositionPanel != null) {
			sensorPositionPanel.refresh();
		}
	}
	
	@Override
	protected AbstractSensorElementWidget newInstance() {
		return new SWESensorPositionWidget();
	}
	
	@Override
	protected void activeMode(MODE mode) {
		if(sensorPositionPanel != null) {
			sensorPositionPanel.switchMode(mode);
		}
	}
}