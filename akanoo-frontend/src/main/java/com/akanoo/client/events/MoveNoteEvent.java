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
