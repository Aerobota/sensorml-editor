package com.sensia.tools.client.swetools.editors.sensorml.renderer.editor.panels.element;

import com.google.gwt.user.client.ui.HTML;
import com.sensia.relaxNG.RNGElement;
import com.sensia.relaxNG.RNGTag;
import com.sensia.tools.client.swetools.editors.sensorml.panels.AbstractPanel;
import com.sensia.tools.client.swetools.editors.sensorml.panels.IPanel;
import com.sensia.tools.client.swetools.editors.sensorml.utils.SMLHorizontalPanel;
import com.sensia.tools.client.swetools.editors.sensorml.utils.Utils;

public class EditSimpleElementPanel extends AbstractPanel<RNGElement>{
	
	public EditSimpleElementPanel(RNGElement element) {
		this(element, null);
	}
	
	public EditSimpleElementPanel(RNGElement element, String label) {
		super(element);		
		
		container = new SMLHorizontalPanel();
		container.addStyleName("simple-element-panel");
		
		if (label != null && !label.isEmpty()) {
		    HTML html = new HTML(Utils.toNiceLabel(label)+":");
	        html.addStyleName("label");
		    container.add(html);
		}		
	}
	
	@Override
	protected void addInnerElement(IPanel<? extends RNGTag> element) {
		container.add(element.getPanel());
	}

	@Override
	protected AbstractPanel<RNGElement> newInstance() {
		return null;
	}

	@Override
	public String getName() {
		return getTag().getName();
	}

}
