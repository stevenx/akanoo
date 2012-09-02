package com.akanoo.client.events;

import com.akanoo.client.dto.CanvasInfo;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class RenameCanvasEvent extends
		GwtEvent<RenameCanvasEvent.RenameCanvasHandler> {

	public static Type<RenameCanvasHandler> TYPE = new Type<RenameCanvasHandler>();
	private CanvasInfo canvasInfo;
		
	public interface RenameCanvasHandler extends EventHandler {
		void onRenameCanvas(RenameCanvasEvent event);
	}

	public RenameCanvasEvent(CanvasInfo canvasInfo) {
		this.canvasInfo = canvasInfo;
	}

	public CanvasInfo getCanvasInfo() {
		return canvasInfo;
	}
	
	@Override
	protected void dispatch(RenameCanvasHandler handler) {
		handler.onRenameCanvas(this);
	}

	@Override
	public Type<RenameCanvasHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RenameCanvasHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, CanvasInfo canvasInfo) {
		source.fireEvent(new RenameCanvasEvent(canvasInfo));
	}
}
