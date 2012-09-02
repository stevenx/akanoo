package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class CometBeforeDisconnectedEvent extends
		GwtEvent<CometBeforeDisconnectedEvent.CometBeforeDisconnectedHandler> {

	public static Type<CometBeforeDisconnectedHandler> TYPE = new Type<CometBeforeDisconnectedHandler>();

	public interface CometBeforeDisconnectedHandler extends EventHandler {
		void onCometBeforeDisconnected(CometBeforeDisconnectedEvent event);
	}

	public CometBeforeDisconnectedEvent() {
	}

	@Override
	protected void dispatch(CometBeforeDisconnectedHandler handler) {
		handler.onCometBeforeDisconnected(this);
	}

	@Override
	public Type<CometBeforeDisconnectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometBeforeDisconnectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CometBeforeDisconnectedEvent());
	}
}
