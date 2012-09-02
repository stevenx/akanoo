package com.akanoo.client.presenters;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class DummyBackgroundPresenter extends
		PresenterWidget<DummyBackgroundPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	public DummyBackgroundPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
