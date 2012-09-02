package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class CometErrorEvent extends
		GwtEvent<CometErrorEvent.CometErrorHandler> {

	public static Type<CometErrorHandler> TYPE = new Type<CometErrorHandler>();
	private boolean connected;

	public interface CometErrorHandler extends EventHandler {
		void onCometError(CometErrorEvent event);
	}

	public CometErrorEvent(boolean connected) {
		this.connected = connected;
	}

	public boolean isConnected() {
		return connected;
	}

	@Override
	protected void dispatch(CometErrorHandler handler) {
		handler.onCometError(this);
	}

	@Override
	public Type<CometErrorHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometErrorHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, boolean connected) {
		source.fireEvent(new CometErrorEvent(connected));
	}
}
