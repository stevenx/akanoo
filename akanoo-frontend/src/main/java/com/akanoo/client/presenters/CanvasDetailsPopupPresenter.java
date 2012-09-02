package com.akanoo.client.presenters;

import com.akanoo.client.GoogleAnalyticsConstants;
import com.akanoo.client.atmosphere.CometListener;
import com.akanoo.client.dto.CanvasInfo;
import com.akanoo.client.dto.MessageConstants;
import com.akanoo.client.events.DetailsPopupCloseEvent;
import com.akanoo.client.events.DetailsPopupCloseEvent.DetailsPopupCloseHandler;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;

public class CanvasDetailsPopupPresenter extends
		PresenterWidget<CanvasDetailsPopupPresenter.MyView> implements
		CanvasDetailsUiHandlers, DetailsPopupCloseHandler {

	public interface MyView extends PopupView,
			HasUiHandlers<CanvasDetailsUiHandlers> {

		void setTitle(String title);

		boolean confirmDelete();

	}

	private CometListener cometListener;
	private long canvasId;

	@Inject
	private Provider<SharingPopupPresenter> sharingProvider;
	private String canvasTitle;

	@Inject
	private Provider<RenameCanvasPopupPresenter> renameProvider;

	@Inject
	private GoogleAnalytics googleAnalytics;

	@Inject
	public CanvasDetailsPopupPresenter(final EventBus eventBus,
			final MyView view, CometListener cometListener) {
		super(eventBus, view);

		getView().setUiHandlers(this);

		this.cometListener = cometListener;
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(DetailsPopupCloseEvent.getType(), this);
	}

	public void setCanvasInfo(Long id, String title) {
		this.canvasId = id;
		this.canvasTitle = title;
		getView().setTitle(title);
	}

	@Override
	public void onShareClicked() {
		SharingPopupPresenter sharingPopup = sharingProvider.get();
		sharingPopup.setCanvasInfo(canvasTitle, canvasId);
		addToPopupSlot(sharingPopup);
	}

	@Override
	public void onRenameClicked() {
		RenameCanvasPopupPresenter renamePopup = renameProvider.get();
		renamePopup.setCanvasInfo(canvasTitle, canvasId);
		addToPopupSlot(renamePopup);
	}

	@Override
	public void onDeleteClicked() {
		if (getView().confirmDelete()) {
			CanvasInfo canvasInfo = new CanvasInfo();
			canvasInfo.id = canvasId;

			this.cometListener.send(MessageConstants.deletecanvas.toString(),
					canvasInfo);

			getView().hide();
			
			// track "before" load event
			if (GoogleAnalyticsConstants.ENABLE_ANALYTICS)
				googleAnalytics.trackEvent(
						GoogleAnalyticsConstants.categorycanvas,
						GoogleAnalyticsConstants.actiondelete,
						GoogleAnalyticsConstants.delete, (int) canvasId);
		}
	}

	@Override
	public void onCloseClicked() {
		getView().hide();
	}

	@Override
	protected void onHide() {
		// only in popups: unbind this presenter when it's being hidden
		unbind();
	}
	
	@Override
	public void onDetailsPopupClose(DetailsPopupCloseEvent event) {
		getView().hide();
	}
}
