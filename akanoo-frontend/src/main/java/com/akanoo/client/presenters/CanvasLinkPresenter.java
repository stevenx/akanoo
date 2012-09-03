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

import com.akanoo.client.atmosphere.CometListener;
import com.akanoo.client.place.NameTokens;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.RevealRootPopupContentEvent;

public class CanvasLinkPresenter extends
		PresenterWidget<CanvasLinkPresenter.MyView> implements
		CanvasLinkUiHandlers {

	public interface MyView extends View, HasUiHandlers<CanvasLinkUiHandlers> {

		void setTitle(String title);

		void setActive(boolean active);
	}

	private Long canvasId;
	@Inject
	private PlaceManager placeManager;
	private PlaceRequest placeRequest;
	@Inject
	private Provider<CanvasDetailsPopupPresenter> detailsPopupProvider;
	private String canvasTitle;
	
	@Inject
	public CanvasLinkPresenter(final EventBus eventBus, final MyView view,
			CometListener cometListener) {
		super(eventBus, view);

		getView().setUiHandlers(this);
	}

	public void setCanvasInfo(String title, Long id) {
		this.canvasId = id;
		this.canvasTitle = title;
		getView().setTitle(title);

		placeRequest = new PlaceRequest(NameTokens.canvas).with(
				NameTokens.idparam, String.valueOf(canvasId));
	}

	/**
	 * This method will be called when a canvas link is clicked.
	 */
	@Override
	public void onLinkClicked() {
		if (!placeManager.hasPendingNavigation()
				&& !placeRequest.equals(placeManager.getCurrentPlaceRequest()))
			placeManager.revealPlace(placeRequest);

	}

	@Override
	protected void onReveal() {
		// update the view
		updateView();
	}

	@Override
	protected void onReset() {
		// update the view
		updateView();
	}

	private void updateView() {
		getView().setActive(
				placeRequest.equals(placeManager.getCurrentPlaceRequest()));
	}

	@Override
	public void onOptionsButtonClicked() {
		CanvasDetailsPopupPresenter detailsPopup = detailsPopupProvider.get();
		detailsPopup.setCanvasInfo(canvasId, canvasTitle);
		RevealRootPopupContentEvent.fire(detailsPopup, detailsPopup);
	}
}
