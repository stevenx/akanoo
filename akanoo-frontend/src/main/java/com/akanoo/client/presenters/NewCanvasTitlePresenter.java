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
