package com.sensia.tools.client.swetools.editors.sensorml.renderer.editor.panels.element;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sensia.relaxNG.RNGData;
import com.sensia.relaxNG.RNGElement;
import com.sensia.relaxNG.RNGTag;
import com.sensia.tools.client.swetools.editors.sensorml.listeners.IButtonCallback;
import com.sensia.tools.client.swetools.editors.sensorml.panels.IPanel;
import com.sensia.tools.client.swetools.editors.sensorml.panels.IRefreshHandler;
import com.sensia.tools.client.swetools.editors.sensorml.panels.base.element.DisclosureElementPanel;
import com.sensia.tools.client.swetools.editors.sensorml.renderer.advanced.AdvancedRendererSML;
import com.sensia.tools.client.swetools.editors.sensorml.utils.Utils;

public class EditSectionElementPanel extends DisclosureElementPanel{

	private Label button;
	private Panel labelPanel;
	private Panel definitionPanel;
	
	public EditSectionElementPanel(final RNGElement tag, final IRefreshHandler refreshHander) {
		super(tag);
		
		labelPanel = new HorizontalPanel();
		definitionPanel = new HorizontalPanel();
		
		button= new Label("");
		button.addStyleName("rng-advanced-button");
		
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// create a new Renderer
				final AdvancedRendererSML renderer = new AdvancedRendererSML();
				final Panel rootPanel = new VerticalPanel();
				
				renderer.setRefreshHandler(new IRefreshHandler() {
					
					@Override
					public void refresh() {
						renderer.reset();
						rootPanel.clear();
						renderer.visitChildren(tag.getChildren());
						rootPanel.add(renderer.getRoot().getPanel());
						
						if(refreshHander != null) {
							refreshHander.refresh();
						}
					}
				});

				renderer.visitChildren(tag.getChildren());
				rootPanel.add(renderer.getRoot().getPanel());
				
				DialogBox dialogBox = Utils.createEditDialogBox(rootPanel, "Edit "+tag.getName(), new IButtonCallback(){

					@Override
					public void onClick() {
						if(refreshHander != null) {
							refreshHander.refresh();
						}
					}
					
				});
			}
		});

		Widget currentHeader = sectionPanel.getHeader();
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(currentHeader);
		hPanel.add(labelPanel);
		hPanel.add(definitionPanel);
		hPanel.add(button);
		
		labelPanel.setVisible(false);
		definitionPanel.setVisible(false);
		
		sectionPanel.setHeader(hPanel);
		button.addStyleName("rng-advanced-button-section");
		
		sectionPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				button.removeStyleName("rng-advanced-button-section");
			}
		});
		
		sectionPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {

			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				button.addStyleName("rng-advanced-button-section");
				
			}
		});
		container.addStyleName("section-panel");
	}
	
	@Override
	protected void addInnerElement(IPanel<? extends RNGTag> element) {
		if(element.getTag() instanceof RNGData<?>) {
			//TODO:??
			Label label = new Label(Utils.toNiceLabel(getName()));
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.add(label);
			hPanel.add(element.getPanel());
		} else if(element.getName().equals("label")){
			labelPanel.add(new HTML("("));
			labelPanel.add(element.getPanel());
			labelPanel.add(new HTML(")"));
			
			labelPanel.setVisible(true);
		} else if(element.getName().equals("definition")){
			definitionPanel.add(element.getPanel());
			
			definitionPanel.setVisible(true);
		} else {
			super.addInnerElement(element);
		}
	}
}