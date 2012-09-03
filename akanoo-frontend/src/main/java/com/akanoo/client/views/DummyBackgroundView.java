/* Copyright 2012 Fabian Gebert and Michael Gorski.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
