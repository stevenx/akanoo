package com.akanoo.client.atmosphere;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.NoteInfo;
import com.google.gwt.event.shared.HasHandlers;

public class RemoveNoteEvent extends
		GwtEvent<RemoveNoteEvent.RemoveNoteHandler> {

	public static Type<RemoveNoteHandler> TYPE = new Type<RemoveNoteHandler>();
	private NoteInfo noteInfo;

	public interface RemoveNoteHandler extends EventHandler {
		void onRemoveNote(RemoveNoteEvent event);
	}

	public RemoveNoteEvent(NoteInfo noteInfo) {
		this.noteInfo = noteInfo;
	}

	public NoteInfo getNoteInfo() {
		return noteInfo;
	}

	@Override
	protected void dispatch(RemoveNoteHandler handler) {
		handler.onRemoveNote(this);
	}

	@Override
	public Type<RemoveNoteHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RemoveNoteHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, NoteInfo noteInfo) {
		source.fireEvent(new RemoveNoteEvent(noteInfo));
	}
}
