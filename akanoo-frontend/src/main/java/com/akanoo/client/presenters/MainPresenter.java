package com.akanoo.client.presenters;

import com.akanoo.client.GoogleAnalyticsConstants;
import com.akanoo.client.atmosphere.CometListener;
import com.akanoo.client.place.NameTokens;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;

public class MainPresenter extends
		Presenter<MainPresenter.MyView, MainPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.home)
	public interface MyProxy extends ProxyPlace<MainPresenter> {
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> CONTENT_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> HEADER_SLOT = new Type<RevealContentHandler<?>>();

	@Inject
	private HeaderPresenter header;

	@Inject
	private PlaceManager placeManager;

	@Inject
	private DummyBackgroundPresenter dummyBackground;

	@Inject
	private CometListener cometListener;

	@Inject
	private GoogleAnalytics googleAnalytics;

	@Inject
	public MainPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();

		cometListener.checkConnection();

		setInSlot(HEADER_SLOT, header);
	}

	@Override
	protected void onReset() {
		super.onReset();

		if (placeManager.getCurrentPlaceRequest().getNameToken()
				.equals(NameTokens.home)) {
			setInSlot(CONTENT_SLOT, dummyBackground);

			// track "before" load event
			if (GoogleAnalyticsConstants.ENABLE_ANALYTICS)
				googleAnalytics.trackEvent(
						GoogleAnalyticsConstants.categoryhome,
						GoogleAnalyticsConstants.actionopen);

		}
	}
}
