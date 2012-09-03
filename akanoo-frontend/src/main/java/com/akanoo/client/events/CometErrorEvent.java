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
package com.akanoo.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class CometErrorEvent extends
		GwtEvent<CometErrorEvent.CometErrorHandler> {

	public static Type<CometErrorHandler> TYPE = new Type<CometErrorHandler>();
	private boolean connected;

	public interface CometErrorHandler extends EventHandler {
		void onCometError(CometErrorEvent event);
	}

	public CometErrorEvent(boolean connected) {
		this.connected = connected;
	}

	public boolean isConnected() {
		return connected;
	}

	@Override
	protected void dispatch(CometErrorHandler handler) {
		handler.onCometError(this);
	}

	@Override
	public Type<CometErrorHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometErrorHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, boolean connected) {
		source.fireEvent(new CometErrorEvent(connected));
	}
}
