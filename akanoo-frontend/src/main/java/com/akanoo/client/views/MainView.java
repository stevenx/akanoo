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
