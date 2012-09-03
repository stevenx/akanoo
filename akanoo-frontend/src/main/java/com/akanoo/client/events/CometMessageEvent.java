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
import java.lang.String;

import com.akanoo.client.dto.Sendable;
import com.google.gwt.event.shared.HasHandlers;

public class CometMessageEvent extends
		GwtEvent<CometMessageEvent.CometMessageHandler> {

	public static Type<CometMessageHandler> TYPE = new Type<CometMessageHandler>();
	private String code;
	private Sendable object;

	public interface CometMessageHandler extends EventHandler {
		void onCometMessage(CometMessageEvent event);
	}

	public CometMessageEvent(String code, Sendable object) {
		this.code = code;
		this.object = object;
	}

	public String getCode() {
		return code;
	}

	public Sendable getObject() {
		return object;
	}

	@Override
	protected void dispatch(CometMessageHandler handler) {
		handler.onCometMessage(this);
	}

	@Override
	public Type<CometMessageHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CometMessageHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String code, Sendable object) {
		source.fireEvent(new CometMessageEvent(code, object));
	}
}
