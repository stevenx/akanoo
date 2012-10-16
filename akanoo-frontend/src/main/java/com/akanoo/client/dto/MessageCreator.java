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
package com.akanoo.client.dto;

import java.util.HashMap;
import java.util.Map;

import name.pehl.piriti.json.client.JsonInstanceCreator;
import name.pehl.piriti.json.client.JsonReader;

import com.akanoo.client.dto.AccountInfo.AccountInfoReader;
import com.akanoo.client.dto.CanvasInfo.CanvasInfoReader;
import com.akanoo.client.dto.NoteInfo.NoteInfoReader;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.inject.Inject;

public class MessageCreator extends JsonInstanceCreator<Message> {

	@Inject
	private static AccountInfoReader accountInfoReader;

	@Inject
	private static NoteInfoReader noteInfoReader;

	@Inject
	private static CanvasInfoReader canvasInfoReader;

	private Map<MessageConstants, JsonReader<? extends Sendable>> readerMap;

	public MessageCreator() {
		readerMap = new HashMap<MessageConstants, JsonReader<? extends Sendable>>();

		// message and reader for reply
		readerMap.put(MessageConstants.initialize, accountInfoReader);
		readerMap.put(MessageConstants.loadfriends, accountInfoReader);
		readerMap.put(MessageConstants.createnote, noteInfoReader);
		readerMap.put(MessageConstants.focusnote, noteInfoReader);
		readerMap.put(MessageConstants.movenote, noteInfoReader);
		readerMap.put(MessageConstants.updatenote, noteInfoReader);
		readerMap.put(MessageConstants.removenote, noteInfoReader);
		readerMap.put(MessageConstants.loadshares, canvasInfoReader);
		readerMap.put(MessageConstants.loadactiveusers, canvasInfoReader);
		readerMap.put(MessageConstants.loadcanvas, canvasInfoReader);
		readerMap.put(MessageConstants.createcanvas, canvasInfoReader);
		readerMap.put(MessageConstants.renamecanvas, canvasInfoReader);
	}

	@Override
	public Message newInstance(JSONValue context) {
		Message message = null;
		JSONObject jsonObject = context.isObject();
		if (jsonObject != null) {
			JSONValue codeValue = jsonObject.get("code");
			JSONValue bodyValue = jsonObject.get("body");
			if (codeValue != null && bodyValue != null) {
				JSONString codeJSONString = codeValue.isString();
				JSONObject bodyJSONValue = bodyValue.isObject();
				if (codeJSONString != null && bodyJSONValue != null) {
					String codeString = codeJSONString.stringValue();

					MessageConstants messageConstants = MessageConstants
							.fromString(codeString);

					if (messageConstants != null) {
						// initialize message
						message = new Message();
						message.code = codeString;

						JsonReader<? extends Sendable> reader = readerMap
								.get(messageConstants);
						message.body = reader.read(bodyJSONValue);
					}
				}
			}
		}
		return message;
	}

}
