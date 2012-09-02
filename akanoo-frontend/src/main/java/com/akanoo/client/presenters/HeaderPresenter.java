package com.akanoo.client.presenters;

import java.util.ArrayList;
import java.util.List;

import com.akanoo.client.atmosphere.CometListener;
import com.akanoo.client.atmosphere.InitializeEvent;
import com.akanoo.client.atmosphere.InitializeEvent.InitializeHandler;
import com.akanoo.client.dto.CanvasInfo;
import com.akanoo.client.dto.MessageConstants;
import com.akanoo.client.events.CometConnectedEvent;
import com.akanoo.client.events.CometConnectedEvent.CometConnectedHandler;
import com.akanoo.client.events.CometDisconnectedEvent;
import com.akanoo.client.events.CometDisconnectedEvent.CometDisconnectedHandler;
import com.akanoo.client.events.CreateCanvasEvent;
import com.akanoo.client.events.CreateCanvasEvent.CreateCanvasHandler;
import com.akanoo.client.events.NewCanvasTitleEnteredEvent;
import com.akanoo.client.events.NewCanvasTitleEnteredEvent.NewCanvasTitleEnteredHandler;
import com.akanoo.client.events.ShareButtonClickedEvent;
import com.akanoo.client.place.NameTokens;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class HeaderPresenter extends PresenterWidget<HeaderPresenter.MyView>
		implements CometConnectedHandler, CometDisconnectedHandler,
		HeaderUiHandlers, InitializeHandler, CreateCanvasHandler,
		NewCanvasTitleEnteredHandler {

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TAB_SLOT = new Type<RevealContentHandler<?>>();

	public interface MyView extends View, HasUiHandlers<HeaderUiHandlers> {

		void setOnlineIndicator(boolean b);

		void alertOffline();

		void alertCanvasDeleted();
		
		void setShareButtonVisibility(boolean b);
	}

	@Inject
	private Provider<CanvasLinkPresenter> canvasLinkProvider;
	
	private CometListener cometListener;

	@Inject
	private PlaceManager placeManager;

	@Inject
	private Provider<NewCanvasTitlePresenter> newCanvasTitleProvider;
	
	@Inject
	public HeaderPresenter(final EventBus eventBus, final MyView view,
			CometListener cometListener) {
		super(eventBus, view);

		getView().setUiHandlers(this);
		this.cometListener = cometListener;
	}

	@Override
	protected void onBind() {
		addRegisteredHandler(CometConnectedEvent.getType(), this);
		addRegisteredHandler(CometDisconnectedEvent.getType(), this);

		addRegisteredHandler(InitializeEvent.getType(), this);
		addRegisteredHandler(CreateCanvasEvent.getType(), this);

		addRegisteredHandler(NewCanvasTitleEnteredEvent.getType(), this);
	}

	@Override
	protected void onReset() {
		super.onReset();
		
		boolean isCanvasToken = placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.canvas);
		getView().setShareButtonVisibility(isCanvasToken);
	}
	
	@Override
	protected void onReveal() {
		getView().setOnlineIndicator(cometListener.isConnected());
	}


	@Override
	public void onCometConnected(CometConnectedEvent event) {
		getView().setOnlineIndicator(true);
	}

	@Override
	public void onCometDisconnected(CometDisconnectedEvent event) {
		getView().setOnlineIndicator(false);
		getView().alertOffline();
	}

	@Override
	public void onInitialize(InitializeEvent event) {
		clearSlot(TAB_SLOT);
		List<String> validIdParams = new ArrayList<String>();
		for (CanvasInfo canvasInfo : event.getAccountInfo().canvases) {
			CanvasLinkPresenter canvasLink = canvasLinkProvider.get();
			canvasLink.setCanvasInfo(canvasInfo.title, canvasInfo.id);
			addToSlot(TAB_SLOT, canvasLink);

			validIdParams.add(canvasInfo.id.toString());
		}

		// if canvas was deleted/unshared: close tab
		if (placeManager.getCurrentPlaceRequest().getNameToken()
				.equals(NameTokens.canvas)
				&& !validIdParams.contains(placeManager
						.getCurrentPlaceRequest().getParameter(
								NameTokens.idparam, ""))) {
			getView().alertCanvasDeleted();
			placeManager.revealDefaultPlace();
		}
	}

	@Override
	public void onCreateCanvasClicked() {
		NewCanvasTitlePresenter titlePresenter = newCanvasTitleProvider.get();
		addToPopupSlot(titlePresenter);
	}

	@Override
	public void onNewCanvasTitleEntered(NewCanvasTitleEnteredEvent event) {
		CanvasInfo canvasInfo = new CanvasInfo();
		canvasInfo.title = event.getTitle();

		cometListener
				.send(MessageConstants.createcanvas.toString(), canvasInfo);
	}

	@Override
	public void onCreateCanvas(CreateCanvasEvent event) {
		// switch to a newly created canvas if it is not already opened
		PlaceRequest newCanvasPlace = new PlaceRequest(NameTokens.canvas).with(
				NameTokens.idparam, event.getCanvasInfo().id.toString());
		if (!placeManager.getCurrentPlaceRequest().equals(newCanvasPlace)) {
			placeManager.revealPlace(newCanvasPlace);
		}
	}
	
	@Override
	public void onAvatarClicked() {
		//TODO show popup
	}
	
	@Override
	public void onShareClicked() {
		ShareButtonClickedEvent.fire(this);
	}
	
}
