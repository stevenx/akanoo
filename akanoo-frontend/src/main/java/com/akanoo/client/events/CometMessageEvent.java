package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;

import com.akanoo.client.dto.Sendable;
import com.google.gwt.event.shared.HasHandlers;

public class CometMessageEvent extends
		GwtEvent<CometMessageEvent.CometMessageHandler> {

	public static Type<CometMessageHandler> TYPE = new Type<CometMessageHandler>();
	private String code;
	private Sendable object;

	public interface CometMessageHandler extends EventHandler {
		void onCometMessage(CometMessageEvent event);
	}

	public CometMessageEvent(String code, Sendable object) {
		this.code = code;
		this.object = object;
	}

	public String getCode() {
		return code;
	}

	public Sendable getObject() {
		return object;
	}

	@Override
	protected void dispatch(CometMessageHandler handler) {
		handler.onCometMessage(this);
	}

	@Override
	public Type<CometMessageHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometMessageHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String code, Sendable object) {
		source.fireEvent(new CometMessageEvent(code, object));
	}
}
