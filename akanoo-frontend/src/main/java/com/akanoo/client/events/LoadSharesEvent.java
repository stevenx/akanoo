package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.CanvasInfo;
import com.google.gwt.event.shared.HasHandlers;

public class LoadSharesEvent extends
		GwtEvent<LoadSharesEvent.LoadSharesHandler> {

	public static Type<LoadSharesHandler> TYPE = new Type<LoadSharesHandler>();
	private CanvasInfo canvasInfo;

	public interface LoadSharesHandler extends EventHandler {
		void onLoadShares(LoadSharesEvent event);
	}

	public LoadSharesEvent(CanvasInfo canvasInfo) {
		this.canvasInfo = canvasInfo;
	}

	public CanvasInfo getCanvasInfo() {
		return canvasInfo;
	}

	@Override
	protected void dispatch(LoadSharesHandler handler) {
		handler.onLoadShares(this);
	}

	@Override
	public Type<LoadSharesHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadSharesHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, CanvasInfo canvasInfo) {
		source.fireEvent(new LoadSharesEvent(canvasInfo));
	}
}
