package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class CometDisconnectedEvent extends
		GwtEvent<CometDisconnectedEvent.CometDisconnectedHandler> {

	public static Type<CometDisconnectedHandler> TYPE = new Type<CometDisconnectedHandler>();

	public interface CometDisconnectedHandler extends EventHandler {
		void onCometDisconnected(CometDisconnectedEvent event);
	}

	public CometDisconnectedEvent() {
	}

	@Override
	protected void dispatch(CometDisconnectedHandler handler) {
		handler.onCometDisconnected(this);
	}

	@Override
	public Type<CometDisconnectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometDisconnectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CometDisconnectedEvent());
	}
}
