package com.akanoo.client.presenters;

import com.gwtplatform.mvp.client.UiHandlers;

public interface SharingPopupUiHandlers extends UiHandlers {

	void onCloseClicked();

	void onShareClicked();

	void onSelectionChange();

	void onAddEmailClicked();

}
