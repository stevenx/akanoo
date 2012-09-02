package com.akanoo.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * This Event will be thrown when a canvas is selected.
 */
public class ShareButtonClickedEvent extends
		GwtEvent<ShareButtonClickedEvent.ShareButtonClickedHandler> {

	public static Type<ShareButtonClickedHandler> TYPE = new Type<ShareButtonClickedHandler>();
	
	public interface ShareButtonClickedHandler extends EventHandler {
		void onShareButtonClicked(ShareButtonClickedEvent event);
	}

	public ShareButtonClickedEvent() {
	}

	@Override
	protected void dispatch(ShareButtonClickedHandler handler) {
		handler.onShareButtonClicked(this);
	}

	@Override
	public Type<ShareButtonClickedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ShareButtonClickedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ShareButtonClickedEvent());
	}
}
