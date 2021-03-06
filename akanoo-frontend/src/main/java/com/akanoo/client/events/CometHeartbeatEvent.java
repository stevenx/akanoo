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

public class CometHeartbeatEvent extends
		GwtEvent<CometHeartbeatEvent.CometHeartbeatHandler> {

	public static Type<CometHeartbeatHandler> TYPE = new Type<CometHeartbeatHandler>();

	public interface CometHeartbeatHandler extends EventHandler {
		void onCometHeartbeat(CometHeartbeatEvent event);
	}

	public CometHeartbeatEvent() {
	}

	@Override
	protected void dispatch(CometHeartbeatHandler handler) {
		handler.onCometHeartbeat(this);
	}

	@Override
	public Type<CometHeartbeatHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometHeartbeatHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CometHeartbeatEvent());
	}
}
