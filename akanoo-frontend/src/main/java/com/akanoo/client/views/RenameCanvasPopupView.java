package com.akanoo.client.views;

import com.akanoo.client.messages.Languages;
import com.akanoo.client.presenters.RenameCanvasPopupPresenter;
import com.akanoo.client.presenters.RenameCanvasPopupUiHandlers;
import com.akanoo.client.views.SharingPopupView.Resources;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

public class RenameCanvasPopupView extends 
		PopupViewWithUiHandlers<RenameCanvasPopupUiHandlers> implements
		RenameCanvasPopupPresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	Resources resources;
	
	@UiField(provided = true)
	Languages messages;
	
	@UiField
	TextBox titleField;
	
	@UiField
	PopupPanel popupPanel;
	
	public interface Binder extends UiBinder<Widget, RenameCanvasPopupView> {
	}

	@Inject
	public RenameCanvasPopupView(final EventBus eventBus, final Binder binder,
			Resources resources, Languages messages) {
		super(eventBus);
		
		this.resources = resources;
		resources.sharingPopupStyle().ensureInjected();
		
		this.messages = messages;
		
		widget = binder.createAndBindUi(this);
		
		popupPanel.setGlassEnabled(true);
		popupPanel.setModal(true);
		popupPanel.setAutoHideEnabled(true);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public String getTitle() {
		return titleField.getText();
	}
	
	@Override
	public void setTitle(String title) {
		titleField.setText(title);
		
	}
	
	@UiHandler("saveButton")
	void onSaveClicked(ClickEvent clickEvent) {
		if (!titleField.getText().isEmpty())
			getUiHandlers().onSaveClicked();
	}
	
	@UiHandler("closeButton")
	void onCloseClicked(ClickEvent clickEvent) {
		getUiHandlers().onCloseClicked();
	}	

}
