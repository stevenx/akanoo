/* Copyright 2012 Fabian Gebert and Michael Gorski.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
