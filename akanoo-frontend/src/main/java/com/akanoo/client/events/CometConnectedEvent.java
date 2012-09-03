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

public class CometConnectedEvent extends
		GwtEvent<CometConnectedEvent.CometConnectedHandler> {

	public static Type<CometConnectedHandler> TYPE = new Type<CometConnectedHandler>();

	public interface CometConnectedHandler extends EventHandler {
		void onCometConnected(CometConnectedEvent event);
	}

	public CometConnectedEvent() {
	}

	@Override
	protected void dispatch(CometConnectedHandler handler) {
		handler.onCometConnected(this);
	}

	@Override
	public Type<CometConnectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometConnectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CometConnectedEvent());
	}
}
