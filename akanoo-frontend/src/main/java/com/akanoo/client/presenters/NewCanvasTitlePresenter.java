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
package com.akanoo.client.presenters;

import com.akanoo.client.events.NewCanvasTitleEnteredEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class NewCanvasTitlePresenter extends
		PresenterWidget<NewCanvasTitlePresenter.MyView> implements
		NewCanvasTitleUiHandlers {

	public interface MyView extends PopupView,
			HasUiHandlers<NewCanvasTitleUiHandlers> {
		String getTitle();
	}

	@Inject
	public NewCanvasTitlePresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);

		getView().setUiHandlers(this);
	}

	@Override
	protected void onHide() {
		// only in popups: unbind this presenter when it's being hidden
		unbind();
	}

	@Override
	public void onCloseClicked() {
		getView().hide();
	}

	@Override
	public void onTitleEntered() {
		fireEvent(new NewCanvasTitleEnteredEvent(getView().getTitle()));
		getView().hide();
	}
}
