package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.CanvasInfo;
import com.google.gwt.event.shared.HasHandlers;

public class CreateCanvasEvent extends
		GwtEvent<CreateCanvasEvent.CreateCanvasHandler> {

	public static Type<CreateCanvasHandler> TYPE = new Type<CreateCanvasHandler>();
	private CanvasInfo canvasInfo;

	public interface CreateCanvasHandler extends EventHandler {
		void onCreateCanvas(CreateCanvasEvent event);
	}

	public CreateCanvasEvent(CanvasInfo canvasInfo) {
		this.canvasInfo = canvasInfo;
	}

	public CanvasInfo getCanvasInfo() {
		return canvasInfo;
	}

	@Override
	protected void dispatch(CreateCanvasHandler handler) {
		handler.onCreateCanvas(this);
	}

	@Override
	public Type<CreateCanvasHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CreateCanvasHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, CanvasInfo canvasInfo) {
		source.fireEvent(new CreateCanvasEvent(canvasInfo));
	}
}
