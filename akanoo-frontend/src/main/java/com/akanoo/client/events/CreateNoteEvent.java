package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.NoteInfo;
import com.google.gwt.event.shared.HasHandlers;

public class CreateNoteEvent extends
		GwtEvent<CreateNoteEvent.CreateNoteHandler> {

	public static Type<CreateNoteHandler> TYPE = new Type<CreateNoteHandler>();
	private NoteInfo noteInfo;

	public interface CreateNoteHandler extends EventHandler {
		void onCreateNote(CreateNoteEvent event);
	}

	public CreateNoteEvent(NoteInfo noteInfo) {
		this.noteInfo = noteInfo;
	}

	public NoteInfo getNoteInfo() {
		return noteInfo;
	}

	@Override
	protected void dispatch(CreateNoteHandler handler) {
		handler.onCreateNote(this);
	}

	@Override
	public Type<CreateNoteHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CreateNoteHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, NoteInfo noteInfo) {
		source.fireEvent(new CreateNoteEvent(noteInfo));
	}
}
