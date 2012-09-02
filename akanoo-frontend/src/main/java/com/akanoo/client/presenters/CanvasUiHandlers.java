package com.akanoo.client.presenters;

import com.akanoo.client.presenters.CanvasPresenter.Point;
import com.gwtplatform.mvp.client.UiHandlers;

public interface CanvasUiHandlers extends UiHandlers {

	void createNote(Point position, String body);

	void updateNoteBody(Note note, String text);

	void moveNote(Note note, Point point);

	void removeNote(Note note);

}
