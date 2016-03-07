package com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.base;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.sensia.relaxNG.RNGAttribute;
import com.sensia.relaxNG.RNGElement;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.AbstractSensorElementWidget;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.ISensorWidget;
import com.sensia.tools.client.swetools.editors.sensorml.panels.widgets.ISensorWidget.APPENDER;

public class SensorGenericXLinkWidget extends AbstractSensorElementWidget{

	private Panel container;
	private Anchor anchorHref;
	
	public SensorGenericXLinkWidget(String name, TAG_DEF def) {
		super(name, def, TAG_TYPE.ELEMENT);
		container = new HorizontalPanel();
		
		//build xlink:href givent an element
		anchorHref = new Anchor();
		container.add(anchorHref);
	}

	@Override
	public Panel getPanel() {
		return container;
	}

	@Override
	protected void activeMode(MODE mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addSensorWidget(ISensorWidget widget) {
		//TODO: handle TITLE/LABEL/NAME
		if(widget.getName().equals("name")) {
			anchorHref.setText(widget.getValue("name"));
		} else if(widget.getName().equals("title")) {
			anchorHref.setText(widget.getValue("title"));
		} else if (widget.getName().equals("href")) {
			anchorHref.setHref(widget.getValue("href"));
			anchorHref.setName("link");
		} else {
			container.add(widget.getPanel());
		}
	}

	@Override
	protected AbstractSensorElementWidget newInstance() {
		return new SensorGenericXLinkWidget(getName(), getDef());
	}
	
	@Override
	public APPENDER appendTo() {
		return APPENDER.HORIZONTAL;
	}

}