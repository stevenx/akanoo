package com.akanoo.client.atmosphere;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.NoteInfo;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateNoteEvent extends
		GwtEvent<UpdateNoteEvent.UpdateNoteHandler> {

	public static Type<UpdateNoteHandler> TYPE = new Type<UpdateNoteHandler>();
	private NoteInfo noteInfo;

	public interface UpdateNoteHandler extends EventHandler {
		void onUpdateNote(UpdateNoteEvent event);
	}

	public UpdateNoteEvent(NoteInfo noteInfo) {
		this.noteInfo = noteInfo;
	}

	public NoteInfo getNoteInfo() {
		return noteInfo;
	}

	@Override
	protected void dispatch(UpdateNoteHandler handler) {
		handler.onUpdateNote(this);
	}

	@Override
	public Type<UpdateNoteHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UpdateNoteHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, NoteInfo noteInfo) {
		source.fireEvent(new UpdateNoteEvent(noteInfo));
	}
}
