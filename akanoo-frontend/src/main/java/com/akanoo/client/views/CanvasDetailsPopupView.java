package com.akanoo.client.views;

import com.akanoo.client.messages.Languages;
import com.akanoo.client.presenters.CanvasDetailsPopupPresenter;
import com.akanoo.client.presenters.CanvasDetailsUiHandlers;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

public class CanvasDetailsPopupView extends
		PopupViewWithUiHandlers<CanvasDetailsUiHandlers> implements
		CanvasDetailsPopupPresenter.MyView {

	/**
	 * Resources that match the GWT standard style theme.
	 */
	public interface Resources extends ClientBundle {
		/**
		 * The styles used in this widget.
		 */
		@Source({ "Buttons.css", Style.DEFAULT_CSS })
		Style canvasDetailsPopupStyle();
	}

	public interface Style extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "com/akanoo/client/views/CanvasDetailsPopupView.css";

		String button();

		String active();

		String titleLabel();
	}

	@UiField(provided = true)
	Resources resources;

	@UiField
	PopupPanel popupPanel;

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CanvasDetailsPopupView> {
	}

	@UiField
	Label titleLabel;

	private String title;

	private Languages messages;

	@Inject
	public CanvasDetailsPopupView(final EventBus eventBus, final Binder binder,
			Languages messages, Resources resources) {
		super(eventBus);

		this.resources = resources;
		resources.canvasDetailsPopupStyle().ensureInjected();

		widget = binder.createAndBindUi(this);

		popupPanel.setGlassEnabled(true);
		popupPanel.setModal(true);

		this.messages = messages;

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public boolean confirmDelete() {
		return Window.confirm(messages.confirmDeletionMessage(title));
	}

	@Override
	public void setTitle(String title) {
		titleLabel.setText(messages.canvasDetailsTitle(title));
		this.title = title;
	}

	@UiHandler("shareButton")
	void onShareClicked(ClickEvent event) {
		getUiHandlers().onShareClicked();
	}
	
	@UiHandler("renameButton")
	void onRenameClicked(ClickEvent event) {
		getUiHandlers().onRenameClicked();
	}

	@UiHandler("deleteButton")
	void onDeleteClicked(ClickEvent event) {
		getUiHandlers().onDeleteClicked();
	}

	@UiHandler("closeButton")
	void onCloseClicked(ClickEvent event) {
		getUiHandlers().onCloseClicked();
	}
}
