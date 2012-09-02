package com.akanoo.client.atmosphere;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.AccountInfo;
import com.google.gwt.event.shared.HasHandlers;

public class InitializeEvent extends
		GwtEvent<InitializeEvent.InitializeHandler> {

	public static Type<InitializeHandler> TYPE = new Type<InitializeHandler>();
	private AccountInfo accountInfo;

	public interface InitializeHandler extends EventHandler {
		void onInitialize(InitializeEvent event);
	}

	public InitializeEvent(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	@Override
	protected void dispatch(InitializeHandler handler) {
		handler.onInitialize(this);
	}

	@Override
	public Type<InitializeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<InitializeHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, AccountInfo accountInfo) {
		source.fireEvent(new InitializeEvent(accountInfo));
	}
}
