package com.akanoo.client.presenters;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CanvasDetailsUiHandlers extends UiHandlers {
	void onCloseClicked();

	void onShareClicked();

	void onRenameClicked();
	
	void onDeleteClicked();
}
