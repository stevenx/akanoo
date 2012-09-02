package com.akanoo.client.views;

import com.akanoo.client.presenters.DummyBackgroundPresenter;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DummyBackgroundView extends ViewImpl implements
		DummyBackgroundPresenter.MyView {

	/**
	 * Resources that match the GWT standard style theme.
	 */
	public interface Resources extends ClientBundle {

		@Source("background.jpg")
		@ImageOptions(repeatStyle = RepeatStyle.Both)
		ImageResource lightbackground();

		@Source("tour-create-canvas.png")
		ImageResource tourCreateCanvas();

		/**
		 * The styles used in this widget.
		 */
		@Source(Style.DEFAULT_CSS)
		Style canvasStyle();
	}

	public interface Style extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "com/akanoo/client/views/DummyBackgroundView.css";

		String boundaryPanel();

		String createCanvasPanel();

	}

	@UiField(provided=true)
	Resources resources;

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DummyBackgroundView> {
	}

	@Inject
	public DummyBackgroundView(final Binder binder, Resources resources) {
		this.resources = resources;
		resources.canvasStyle().ensureInjected();
		
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
