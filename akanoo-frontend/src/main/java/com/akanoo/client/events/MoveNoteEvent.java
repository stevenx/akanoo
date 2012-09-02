package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.NoteInfo;
import com.google.gwt.event.shared.HasHandlers;

public class MoveNoteEvent extends GwtEvent<MoveNoteEvent.MoveNoteHandler> {

	public static Type<MoveNoteHandler> TYPE = new Type<MoveNoteHandler>();
	private NoteInfo noteInfo;

	public interface MoveNoteHandler extends EventHandler {
		void onMoveNote(MoveNoteEvent event);
	}

	public MoveNoteEvent(NoteInfo noteInfo) {
		this.noteInfo = noteInfo;
	}

	public NoteInfo getNoteInfo() {
		return noteInfo;
	}

	@Override
	protected void dispatch(MoveNoteHandler handler) {
		handler.onMoveNote(this);
	}

	@Override
	public Type<MoveNoteHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MoveNoteHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, NoteInfo noteInfo) {
		source.fireEvent(new MoveNoteEvent(noteInfo));
	}
}
