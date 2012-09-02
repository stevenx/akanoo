package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

public class NewCanvasTitleEnteredEvent extends
		GwtEvent<NewCanvasTitleEnteredEvent.NewCanvasTitleEnteredHandler> {

	public static Type<NewCanvasTitleEnteredHandler> TYPE = new Type<NewCanvasTitleEnteredHandler>();
	private String title;

	public interface NewCanvasTitleEnteredHandler extends EventHandler {
		void onNewCanvasTitleEntered(NewCanvasTitleEnteredEvent event);
	}

	public NewCanvasTitleEnteredEvent(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	@Override
	protected void dispatch(NewCanvasTitleEnteredHandler handler) {
		handler.onNewCanvasTitleEntered(this);
	}

	@Override
	public Type<NewCanvasTitleEnteredHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NewCanvasTitleEnteredHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String title) {
		source.fireEvent(new NewCanvasTitleEnteredEvent(title));
	}
}
