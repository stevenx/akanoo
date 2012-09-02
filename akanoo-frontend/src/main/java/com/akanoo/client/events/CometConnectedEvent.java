package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class CometConnectedEvent extends
		GwtEvent<CometConnectedEvent.CometConnectedHandler> {

	public static Type<CometConnectedHandler> TYPE = new Type<CometConnectedHandler>();

	public interface CometConnectedHandler extends EventHandler {
		void onCometConnected(CometConnectedEvent event);
	}

	public CometConnectedEvent() {
	}

	@Override
	protected void dispatch(CometConnectedHandler handler) {
		handler.onCometConnected(this);
	}

	@Override
	public Type<CometConnectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometConnectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CometConnectedEvent());
	}
}
