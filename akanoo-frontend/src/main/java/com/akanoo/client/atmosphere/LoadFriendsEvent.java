package com.akanoo.client.atmosphere;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.AccountInfo;
import com.google.gwt.event.shared.HasHandlers;

public class LoadFriendsEvent extends
		GwtEvent<LoadFriendsEvent.LoadFriendsHandler> {

	public static Type<LoadFriendsHandler> TYPE = new Type<LoadFriendsHandler>();
	private AccountInfo accountInfo;

	public interface LoadFriendsHandler extends EventHandler {
		void onLoadFriends(LoadFriendsEvent event);
	}

	public LoadFriendsEvent(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	@Override
	protected void dispatch(LoadFriendsHandler handler) {
		handler.onLoadFriends(this);
	}

	@Override
	public Type<LoadFriendsHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadFriendsHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, AccountInfo accountInfo) {
		source.fireEvent(new LoadFriendsEvent(accountInfo));
	}
}
