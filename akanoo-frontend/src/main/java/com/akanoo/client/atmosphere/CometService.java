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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.akanoo.client.dto.AccountInfo;
import com.akanoo.client.dto.CanvasInfo;
import com.akanoo.client.dto.MessageConstants;
import com.akanoo.client.dto.NoteInfo;
import com.akanoo.client.dto.UserInfo;
import com.akanoo.client.events.CometMessageEvent;
import com.akanoo.client.events.CometMessageEvent.CometMessageHandler;
import com.akanoo.client.events.CreateCanvasEvent;
import com.akanoo.client.events.CreateNoteEvent;
import com.akanoo.client.events.FocusNoteEvent;
import com.akanoo.client.events.LoadActiveUsersEvent;
import com.akanoo.client.events.LoadCanvasEvent;
import com.akanoo.client.events.LoadSharesEvent;
import com.akanoo.client.events.MoveNoteEvent;
import com.akanoo.client.events.RenameCanvasEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class CometService implements CometMessageHandler {
	private EventBus eventBus;
	private List<UserInfo> friends;
	private boolean initialized;

	@Inject
	public CometService(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void initialize() {
		eventBus.addHandler(CometMessageEvent.getType(), this);
	}

	public List<UserInfo> getFriends() {
		return friends != null ? Collections.unmodifiableList(friends)
				: new ArrayList<UserInfo>();
	}

	public boolean isInitialized() {
		return initialized;
	}
	
	@Override
	public void onCometMessage(CometMessageEvent event) {
		if (MessageConstants.initialize.toString().equals(event.getCode())) {
			initialized = true;
			
			eventBus.fireEvent(new InitializeEvent((AccountInfo) event
					.getObject()));
		} else if (MessageConstants.loadfriends.toString().equals(
				event.getCode())) {
			AccountInfo accountInfo = (AccountInfo) event.getObject();
			friends = accountInfo.friends;
			eventBus.fireEvent(new LoadFriendsEvent(accountInfo));
		} else if (MessageConstants.createcanvas.toString().equals(
				event.getCode())) {
			eventBus.fireEvent(new CreateCanvasEvent((CanvasInfo) event
					.getObject()));
		} else if (MessageConstants.renamecanvas.toString().equals(
				event.getCode())) {
			eventBus.fireEvent(new RenameCanvasEvent((CanvasInfo) event
					.getObject()));
		} else if (MessageConstants.loadcanvas.toString().equals(
				event.getCode())) {
			eventBus.fireEvent(new LoadCanvasEvent((CanvasInfo) event
					.getObject()));
		} else if (MessageConstants.loadshares.toString().equals(
				event.getCode())) {
			eventBus.fireEvent(new LoadSharesEvent((CanvasInfo) event
					.getObject()));
		} else if (MessageConstants.loadactiveusers.toString().equals(
				event.getCode())) {
			eventBus.fireEvent(new LoadActiveUsersEvent((CanvasInfo) event
					.getObject()));
		} else if (MessageConstants.createnote.toString().equals(
				event.getCode())) {
			eventBus.fireEvent(new CreateNoteEvent((NoteInfo) event.getObject()));
		} else if (MessageConstants.focusnote.toString()
				.equals(event.getCode())) {
			eventBus.fireEvent(new FocusNoteEvent((NoteInfo) event.getObject()));
		} else if (MessageConstants.movenote.toString().equals(event.getCode())) {
			eventBus.fireEvent(new MoveNoteEvent((NoteInfo) event.getObject()));
		} else if (MessageConstants.updatenote.toString().equals(
				event.getCode())) {
			eventBus.fireEvent(new UpdateNoteEvent((NoteInfo) event.getObject()));
		} else if (MessageConstants.removenote.toString().equals(
				event.getCode())) {
			eventBus.fireEvent(new RemoveNoteEvent((NoteInfo) event.getObject()));
		}
	}
}
