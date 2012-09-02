package com.akanoo.client.gin;

import com.akanoo.client.GoogleAnalyticsConstants;
import com.akanoo.client.atmosphere.CometListener;
import com.akanoo.client.dto.AccountInfo.AccountInfoReader;
import com.akanoo.client.dto.AccountInfo.AccountInfoWriter;
import com.akanoo.client.dto.CanvasInfo.CanvasInfoReader;
import com.akanoo.client.dto.CanvasInfo.CanvasInfoWriter;
import com.akanoo.client.dto.Message.MessageReader;
import com.akanoo.client.dto.Message.MessageWriter;
import com.akanoo.client.dto.MessageCreator;
import com.akanoo.client.dto.NoteInfo.NoteInfoReader;
import com.akanoo.client.dto.NoteInfo.NoteInfoWriter;
import com.akanoo.client.dto.SendableConverter;
import com.akanoo.client.dto.ShareInfo.ShareInfoReader;
import com.akanoo.client.dto.ShareInfo.ShareInfoWriter;
import com.akanoo.client.dto.UserInfo.UserInfoReader;
import com.akanoo.client.dto.UserInfo.UserInfoWriter;
import com.akanoo.client.place.ClientPlaceManager;
import com.akanoo.client.place.DefaultPlace;
import com.akanoo.client.place.NameTokens;
import com.akanoo.client.presenters.CanvasDetailsPopupPresenter;
import com.akanoo.client.presenters.CanvasLinkPresenter;
import com.akanoo.client.presenters.CanvasPresenter;
import com.akanoo.client.presenters.DummyBackgroundPresenter;
import com.akanoo.client.presenters.HeaderPresenter;
import com.akanoo.client.presenters.MainPresenter;
import com.akanoo.client.presenters.NewCanvasTitlePresenter;
import com.akanoo.client.presenters.RenameCanvasPopupPresenter;
import com.akanoo.client.presenters.SharingPopupPresenter;
import com.akanoo.client.views.CanvasDetailsPopupView;
import com.akanoo.client.views.CanvasLinkView;
import com.akanoo.client.views.CanvasView;
import com.akanoo.client.views.DummyBackgroundView;
import com.akanoo.client.views.HeaderView;
import com.akanoo.client.views.MainView;
import com.akanoo.client.views.NewCanvasTitleView;
import com.akanoo.client.views.RenameCanvasPopupView;
import com.akanoo.client.views.SharingPopupView;
import com.akanoo.client.widgets.UserCell;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.annotations.GaAccount;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalyticsNavigationTracker;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(ClientPlaceManager.class));

		requestStaticInjection(UserCell.class);

		if (GoogleAnalyticsConstants.ENABLE_ANALYTICS) {
			bindConstant().annotatedWith(GaAccount.class).to("UA-");
			bind(GoogleAnalyticsNavigationTracker.class).asEagerSingleton();
		}

		bindJsonReaders();

		bind(CometListener.class).in(Singleton.class);

		bindPresenter(MainPresenter.class, MainPresenter.MyView.class,
				MainView.class, MainPresenter.MyProxy.class);

		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);

		bindPresenter(CanvasPresenter.class, CanvasPresenter.MyView.class,
				CanvasView.class, CanvasPresenter.MyProxy.class);

		bindPresenterWidget(HeaderPresenter.class,
				HeaderPresenter.MyView.class, HeaderView.class);

		bindPresenterWidget(CanvasLinkPresenter.class,
				CanvasLinkPresenter.MyView.class, CanvasLinkView.class);

		bindPresenterWidget(DummyBackgroundPresenter.class,
				DummyBackgroundPresenter.MyView.class,
				DummyBackgroundView.class);

		bindPresenterWidget(SharingPopupPresenter.class,
				SharingPopupPresenter.MyView.class, SharingPopupView.class);

		bindPresenterWidget(CanvasDetailsPopupPresenter.class,
				CanvasDetailsPopupPresenter.MyView.class,
				CanvasDetailsPopupView.class);

		bindPresenterWidget(NewCanvasTitlePresenter.class,
				NewCanvasTitlePresenter.MyView.class, NewCanvasTitleView.class);

		bindPresenterWidget(RenameCanvasPopupPresenter.class,
				RenameCanvasPopupPresenter.MyView.class, RenameCanvasPopupView.class);
	}

	private void bindJsonReaders() {
		requestStaticInjection(MessageCreator.class, SendableConverter.class);

		bind(MessageReader.class).asEagerSingleton();
		bind(CanvasInfoReader.class).asEagerSingleton();
		bind(NoteInfoReader.class).asEagerSingleton();
		bind(ShareInfoReader.class).asEagerSingleton();
		bind(UserInfoReader.class).asEagerSingleton();
		bind(AccountInfoReader.class).asEagerSingleton();

		bind(MessageWriter.class).asEagerSingleton();
		bind(CanvasInfoWriter.class).asEagerSingleton();
		bind(NoteInfoWriter.class).asEagerSingleton();
		bind(ShareInfoWriter.class).asEagerSingleton();
		bind(UserInfoWriter.class).asEagerSingleton();
		bind(AccountInfoWriter.class).asEagerSingleton();
	}
}
