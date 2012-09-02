package com.akanoo.client.gin;

import com.akanoo.client.atmosphere.CometListener;
import com.akanoo.client.atmosphere.CometService;
import com.akanoo.client.presenters.MainPresenter;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.akanoo.client.presenters.CanvasPresenter;

@GinModules({ ClientModule.class })
public interface ClientGinjector extends Ginjector {

	EventBus getEventBus();

	PlaceManager getPlaceManager();

	CometListener getCometListener();

	CometService getCometService();

	AsyncProvider<MainPresenter> getMainPresenter();

	AsyncProvider<CanvasPresenter> getCanvasPresenter();

}
