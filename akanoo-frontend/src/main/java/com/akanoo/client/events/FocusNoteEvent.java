package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.NoteInfo;
import com.google.gwt.event.shared.HasHandlers;

public class FocusNoteEvent extends GwtEvent<FocusNoteEvent.FocusNoteHandler> {

	public static Type<FocusNoteHandler> TYPE = new Type<FocusNoteHandler>();
	private NoteInfo noteInfo;

	public interface FocusNoteHandler extends EventHandler {
		void onFocusNote(FocusNoteEvent event);
	}

	public FocusNoteEvent(NoteInfo noteInfo) {
		this.noteInfo = noteInfo;
	}

	public NoteInfo getNoteInfo() {
		return noteInfo;
	}

	@Override
	protected void dispatch(FocusNoteHandler handler) {
		handler.onFocusNote(this);
	}

	@Override
	public Type<FocusNoteHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<FocusNoteHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, NoteInfo noteInfo) {
		source.fireEvent(new FocusNoteEvent(noteInfo));
	}
}
