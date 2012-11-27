package com.akanoo.client.events;

import com.akanoo.client.dto.CanvasInfo;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class LoadActiveUsersEvent extends
		GwtEvent<LoadActiveUsersEvent.LoadActiveUsersHandler> {

	public static Type<LoadActiveUsersHandler> TYPE = new Type<LoadActiveUsersHandler>();
	private CanvasInfo canvasInfo;

	public interface LoadActiveUsersHandler extends EventHandler {
		void onLoadActiveUsers(LoadActiveUsersEvent event);
	}

	public LoadActiveUsersEvent(CanvasInfo canvasInfo) {
		this.canvasInfo = canvasInfo;
	}

	public CanvasInfo getCanvasInfo() {
		return canvasInfo;
	}

	@Override
	protected void dispatch(LoadActiveUsersHandler handler) {
		handler.onLoadActiveUsers(this);
	}

	@Override
	public Type<LoadActiveUsersHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadActiveUsersHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, CanvasInfo canvasInfo) {
		source.fireEvent(new LoadActiveUsersEvent(canvasInfo));
	}
}