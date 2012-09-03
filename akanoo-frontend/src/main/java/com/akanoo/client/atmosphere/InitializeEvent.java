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
