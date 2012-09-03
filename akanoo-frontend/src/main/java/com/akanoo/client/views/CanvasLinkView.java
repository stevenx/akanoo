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

import com.akanoo.client.presenters.CanvasLinkPresenter;
import com.akanoo.client.presenters.CanvasLinkUiHandlers;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class CanvasLinkView extends ViewWithUiHandlers<CanvasLinkUiHandlers>
		implements CanvasLinkPresenter.MyView {

	/**
	 * Resources that match the GWT standard style theme.
	 */
	public interface Resources extends ClientBundle {

		@Source("settings.png")
		ImageResource settings();

		@Source("settings-hover.png")
		ImageResource settingsHover();
		
		/**
		 * The styles used in this widget.
		 */
		@Source(Style.DEFAULT_CSS)
		Style canvasLinkStyle();
	}

	public interface Style extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "com/akanoo/client/views/CanvasLinkView.css";

		String canvasLink();

		String active();

		String canvasAnchor();
		
		String optionsButton();
	}

	@UiField(provided = true)
	Resources resources;

	private final Widget widget;

	@UiField(provided = true)
	Anchor anchor;

	@UiField
	PushButton optionsButton;

	public interface Binder extends UiBinder<Widget, CanvasLinkView> {
	}

	@Inject
	public CanvasLinkView(final Binder binder, Resources resources) {
		this.resources = resources;
		resources.canvasLinkStyle().ensureInjected();

		anchor = new Anchor(true);

		widget = binder.createAndBindUi(this);
		
		optionsButton.setVisible(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setTitle(String title) {
		anchor.setText(title);
	}

	@UiHandler("anchor")
	public void onLinkClicked(ClickEvent event) {
		event.preventDefault();
		getUiHandlers().onLinkClicked();
	}

	@UiHandler("optionsButton")
	void onOptionsButtonClicked(ClickEvent event) {
		event.preventDefault();
		getUiHandlers().onOptionsButtonClicked();
	}

	@Override
	public void setActive(boolean active) {
		widget.setStyleName(resources.canvasLinkStyle().active(), active);

		optionsButton.setVisible(active);
	}
}
