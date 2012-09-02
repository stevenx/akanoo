package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class CometHeartbeatEvent extends
		GwtEvent<CometHeartbeatEvent.CometHeartbeatHandler> {

	public static Type<CometHeartbeatHandler> TYPE = new Type<CometHeartbeatHandler>();

	public interface CometHeartbeatHandler extends EventHandler {
		void onCometHeartbeat(CometHeartbeatEvent event);
	}

	public CometHeartbeatEvent() {
	}

	@Override
	protected void dispatch(CometHeartbeatHandler handler) {
		handler.onCometHeartbeat(this);
	}

	@Override
	public Type<CometHeartbeatHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometHeartbeatHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CometHeartbeatEvent());
	}
}
