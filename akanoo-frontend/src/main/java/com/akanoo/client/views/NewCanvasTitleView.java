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

import com.akanoo.client.messages.Languages;
import com.akanoo.client.presenters.NewCanvasTitlePresenter;
import com.akanoo.client.presenters.NewCanvasTitleUiHandlers;
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

public class NewCanvasTitleView extends
		PopupViewWithUiHandlers<NewCanvasTitleUiHandlers> implements
		NewCanvasTitlePresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	Resources resources;

	@UiField(provided = true)
	Languages messages;

	@UiField
	TextBox titleField;

	@UiField
	PopupPanel popupPanel;

	public interface Binder extends UiBinder<Widget, NewCanvasTitleView> {
	}

	@Inject
	public NewCanvasTitleView(final EventBus eventBus, final Binder binder,
			Resources resources, Languages messages) {
		super(eventBus);

		this.resources = resources;
		resources.sharingPopupStyle().ensureInjected();

		this.messages = messages;

		widget = binder.createAndBindUi(this);

		popupPanel.setGlassEnabled(true);
		popupPanel.setModal(true);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public String getTitle() {
		return titleField.getText();
	}

	@UiHandler("closeButton")
	void onCloseClicked(ClickEvent clickEvent) {
		getUiHandlers().onCloseClicked();
	}

	@UiHandler("createButton")
	void onShareClicked(ClickEvent clickEvent) {
		if (!titleField.getText().isEmpty())
			getUiHandlers().onTitleEntered();
	}
}
