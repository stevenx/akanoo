package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.CanvasInfo;
import com.google.gwt.event.shared.HasHandlers;

public class LoadCanvasEvent extends
		GwtEvent<LoadCanvasEvent.LoadCanvasHandler> {

	public static Type<LoadCanvasHandler> TYPE = new Type<LoadCanvasHandler>();
	private CanvasInfo canvasInfo;

	public interface LoadCanvasHandler extends EventHandler {
		void onLoadCanvas(LoadCanvasEvent event);
	}

	public LoadCanvasEvent(CanvasInfo canvasInfo) {
		this.canvasInfo = canvasInfo;
	}

	public CanvasInfo getCanvasInfo() {
		return canvasInfo;
	}

	@Override
	protected void dispatch(LoadCanvasHandler handler) {
		handler.onLoadCanvas(this);
	}

	@Override
	public Type<LoadCanvasHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadCanvasHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, CanvasInfo canvasInfo) {
		source.fireEvent(new LoadCanvasEvent(canvasInfo));
	}
}
