/* Copyright 2012 Fabian Gebert and Michael Gorski.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
