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

import java.util.List;

import org.atmosphere.gwt.client.AtmosphereClient;
import org.atmosphere.gwt.client.AtmosphereGWTSerializer;
import org.atmosphere.gwt.client.AtmosphereListener;
import org.atmosphere.gwt.client.impl.WebSocketCometTransport;

import com.akanoo.client.Akanoo;
import com.akanoo.client.dto.Message;
import com.akanoo.client.dto.Message.MessageReader;
import com.akanoo.client.dto.MessageConstants;
import com.akanoo.client.dto.Sendable;
import com.akanoo.client.dto.SendableConverter;
import com.akanoo.client.events.CometBeforeDisconnectedEvent;
import com.akanoo.client.events.CometConnectedEvent;
import com.akanoo.client.events.CometDisconnectedEvent;
import com.akanoo.client.events.CometErrorEvent;
import com.akanoo.client.events.CometHeartbeatEvent;
import com.akanoo.client.events.CometMessageEvent;
import com.akanoo.client.messages.Languages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class CometListener {

	private static final boolean ENFORCE_WEBSOCKET = false;
	private static final boolean SUPPORT_WEBSOCKET = true;

	@Inject
	EventBus eventBus;

	@Inject
	Languages messages;

	@Inject
	private MessageReader messageReader;

	@Inject
	private SendableConverter sendableConverter;

	AtmosphereClient client;

	public boolean connected;

	private boolean initialized;

	private class MyCometListener implements AtmosphereListener {

		@Override
		public void onConnected(int heartbeat, int connectionID) {
			GWT.log("comet.connected [" + heartbeat + ", " + connectionID + "]");

			if (!connected) {
				connected = true;
				eventBus.fireEvent(new CometConnectedEvent());

				sendInitializer();
			}
		}

		@Override
		public void onBeforeDisconnected() {
			GWT.log("comet.beforeDisconnected");

			eventBus.fireEvent(new CometBeforeDisconnectedEvent());
		}

		@Override
		public void onDisconnected() {
			GWT.log("comet.disconnected");

			if (connected) {
				connected = false;
				eventBus.fireEvent(new CometDisconnectedEvent());
			}
		}

		@Override
		public void onError(Throwable exception, boolean connected) {
			int statuscode = -1;
			if (exception instanceof StatusCodeException) {
				statuscode = ((StatusCodeException) exception).getStatusCode();
			}
			GWT.log("comet.error [connected=" + connected + "] (" + statuscode
					+ ")", exception);

			eventBus.fireEvent(new CometErrorEvent(connected));

			if (connected == false && CometListener.this.connected) {
				CometListener.this.connected = false;
				eventBus.fireEvent(new CometDisconnectedEvent());
			}
		}

		@Override
		public void onHeartbeat() {
			GWT.log("comet.heartbeat [" + client.getConnectionID() + "]");

			eventBus.fireEvent(new CometHeartbeatEvent());

		}

		@Override
		public void onRefresh() {
			GWT.log("comet.refresh [" + client.getConnectionID() + "]");
		}

		@Override
		public void onAfterRefresh() {
			GWT.log("comet.afterRefresh [" + client.getConnectionID() + "]");
		}

		@Override
		public void onMessage(List<?> messages) {
		
			StringBuilder result = new StringBuilder();
			for (Object obj : messages) {
				result.append(obj.toString()).append("<br/>");
			}
			GWT.log("comet.message [" + client.getConnectionID() + "] "
					+ result.toString());
			GWT.log("[" + client.getConnectionID() + "] Received "
					+ messages.size() + " messages");

			for (Object message : messages) {
				if (message instanceof String) {
					try {
						String messageString = (String) message;
						messageString = messageString.replaceAll("\\\n", "\\n");
						Message bean = messageReader.read(messageString);

						String code = bean.code;

						Sendable object = bean.body;

						eventBus.fireEvent(new CometMessageEvent(code, object));
					} catch (Exception ex) {
						GWT.log("Couldn't parse incoming data!", ex);
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	public void initialize() {
		if (initialized)
			return;

		if (!WebSocketCometTransport.hasWebSocketSupport() && ENFORCE_WEBSOCKET) {
			Window.alert(messages.webSocketSupportMissingAlert());
			return;
		}

		MyCometListener cometListener = new MyCometListener();

		AtmosphereGWTSerializer serializer = GWT.create(StringSerializer.class);
		// set a small length parameter to force refreshes
		// normally you should remove the length parameter
		String url = Akanoo.getAtmosphereUrl();

		client = new AtmosphereClient(url, serializer, cometListener,
				SUPPORT_WEBSOCKET);
		client.start();

		initialized = true;
	}

	public boolean isConnected() {
		return connected;
	}

	public void sendInitializer() {
		send(MessageConstants.initialize.toString(), null);
	}

	public void send(String code, Sendable sendable) {
		String bodyValueString = sendableConverter.serialize(sendable);

		StringBuilder sb = new StringBuilder();

		sb.append("{");
		sb.append("\"code\":\"");
		sb.append(code);
		sb.append("\"");
		sb.append(",");
		sb.append("\"body\":");
		sb.append(bodyValueString);
		sb.append("}");

		String payload = sb.toString();

		GWT.log("comet.post [" + client.getConnectionID() + "] " + payload);
		client.post(payload);
	}

	public boolean checkConnection() {
		if (isConnected()) {
			return true;
		} else {
			initialize();
			return false;
		}
	}
}
