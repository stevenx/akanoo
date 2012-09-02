package com.akanoo.client.views;

import com.akanoo.client.presenters.MainPresenter;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class MainView extends ViewImpl implements MainPresenter.MyView {

	/**
	 * Resources that match the GWT standard style theme.
	 */
	public interface Resources extends ClientBundle {

		@Source("background.jpg")
		@ImageOptions(repeatStyle = RepeatStyle.Both)
		ImageResource background();

		/**
		 * The styles used in this widget.
		 */
		@Source(Style.DEFAULT_CSS)
		Style mainStyle();
	}

	public interface Style extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "com/akanoo/client/views/MainView.css";

		int mainPadding();

		int topPanelHeight();

		int contentPanelTop();

		int contentPanelBottom();

		String mainPanel();
	}

	@UiField
	Resources resources;

	private final Widget widget;

	@UiField
	Panel headerPanel;
	@UiField
	Panel contentPanel;

	public interface Binder extends UiBinder<Widget, MainView> {
	}

	@Inject
	public MainView(final Binder binder, Resources resources) {
		this.resources = resources;
		resources.mainStyle().ensureInjected();

		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == MainPresenter.CONTENT_SLOT) {
			contentPanel.clear();
			if (content != null)
				contentPanel.add(content);
		} else if (slot == MainPresenter.HEADER_SLOT) {
			headerPanel.clear();
			if (content != null)
				headerPanel.add(content);
		}
	}
}
