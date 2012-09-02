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
import com.akanoo.client.events.LoadCanvasEvent;
import com.akanoo.client.events.LoadSharesEvent;
import com.akanoo.client.events.MoveNoteEvent;
import com.akanoo.client.events.RenameCanvasEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class CometService implements CometMessageHandler {
	private EventBus eventBus;
	private List<UserInfo> friends;

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

	@Override
	public void onCometMessage(CometMessageEvent event) {
		if (MessageConstants.initialize.toString().equals(event.getCode())) {
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
