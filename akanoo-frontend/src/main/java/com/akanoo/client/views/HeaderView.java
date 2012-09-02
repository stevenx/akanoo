package com.akanoo.client.views;

import com.akanoo.client.messages.Languages;
import com.akanoo.client.presenters.HeaderPresenter;
import com.akanoo.client.presenters.HeaderUiHandlers;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class HeaderView extends ViewWithUiHandlers<HeaderUiHandlers> implements
		HeaderPresenter.MyView {

	/**
	 * Resources that match the GWT standard style theme.
	 */
	public interface Resources extends ClientBundle {

		@Source("logo-small-alpha.png")
		ImageResource logoSmall();

		@Source("add.png")
		ImageResource addIcon();

		/**
		 * The styles used in this widget.
		 */
		@Source({ "Buttons.css", Style.DEFAULT_CSS })
		Style headerStyle();
	}

	public interface Style extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "com/akanoo/client/views/HeaderView.css";

		String button();

		String active();

		String header();

		String logo();

		String indicator();

		String indicatorOnline();

		String createCanvasLink();

		String canvasLinkPanel();
		
		String headerShareButton();
	}

	@UiField
	Resources resources;

	@UiField
	Widget onlineIndicator;

	@UiField
	Widget shareButton;
	
	@UiField
	HorizontalPanel canvasLinkPanel;

	@UiField()
	Widget createCanvasLink;

	private final Widget widget;

	@UiField(provided = true)
	Languages messages;

	public interface Binder extends UiBinder<Widget, HeaderView> {
	}

	@Inject
	public HeaderView(final Binder binder, Resources resources, Languages messages) {
		this.resources = resources;
		resources.headerStyle().ensureInjected();
		this.messages = messages;
		
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setOnlineIndicator(boolean b) {
		onlineIndicator.setStyleName(resources.headerStyle().indicatorOnline(),
				b);
	}

	@Override
	public void alertOffline() {

	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == HeaderPresenter.TAB_SLOT) {
			canvasLinkPanel.clear();
			canvasLinkPanel.add(createCanvasLink);
		}
	}

	public void addToSlot(Object slot, Widget content) {
		if (slot == HeaderPresenter.TAB_SLOT) {
			int linkIndex = canvasLinkPanel.getWidgetIndex(createCanvasLink);
			canvasLinkPanel.insert(content, linkIndex);
		}
	}

	@UiHandler("createCanvasLink")
	void onCreateCanvasLink(ClickEvent event) {
		event.preventDefault();
		getUiHandlers().onCreateCanvasClicked();
	}
	
	@UiHandler("shareButton")
	void onShareClicked(ClickEvent event) {
		getUiHandlers().onShareClicked();
	}	

	@Override
	public void alertCanvasDeleted() {
		Window.alert(messages.canvasNoLongerAvailableMessage());
	}
	
	@Override
	public void setShareButtonVisibility(boolean b) {
		shareButton.setVisible(b);
	}
}
