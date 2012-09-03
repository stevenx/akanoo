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

public class CometDisconnectedEvent extends
		GwtEvent<CometDisconnectedEvent.CometDisconnectedHandler> {

	public static Type<CometDisconnectedHandler> TYPE = new Type<CometDisconnectedHandler>();

	public interface CometDisconnectedHandler extends EventHandler {
		void onCometDisconnected(CometDisconnectedEvent event);
	}

	public CometDisconnectedEvent() {
	}

	@Override
	protected void dispatch(CometDisconnectedHandler handler) {
		handler.onCometDisconnected(this);
	}

	@Override
	public Type<CometDisconnectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometDisconnectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CometDisconnectedEvent());
	}
}
