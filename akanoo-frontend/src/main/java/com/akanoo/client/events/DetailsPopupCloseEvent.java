package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class DetailsPopupCloseEvent extends
		GwtEvent<DetailsPopupCloseEvent.DetailsPopupCloseHandler> {

	public static Type<DetailsPopupCloseHandler> TYPE = new Type<DetailsPopupCloseHandler>();

	public interface DetailsPopupCloseHandler extends EventHandler {
		void onDetailsPopupClose(DetailsPopupCloseEvent event);
	}

	public DetailsPopupCloseEvent() {
	}

	@Override
	protected void dispatch(DetailsPopupCloseHandler handler) {
		handler.onDetailsPopupClose(this);
	}

	@Override
	public Type<DetailsPopupCloseHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DetailsPopupCloseHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new DetailsPopupCloseEvent());
	}
}
