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

import name.pehl.piriti.converter.client.Converter;

import com.akanoo.client.dto.AccountInfo.AccountInfoWriter;
import com.akanoo.client.dto.CanvasInfo.CanvasInfoWriter;
import com.akanoo.client.dto.NoteInfo.NoteInfoWriter;
import com.akanoo.client.dto.ShareInfo.ShareInfoWriter;
import com.akanoo.client.dto.UserInfo.UserInfoWriter;
import com.google.inject.Inject;

public class SendableConverter implements Converter<Sendable> {

	@Inject
	private static AccountInfoWriter accountInfoWriter;
	
	@Inject
	private static CanvasInfoWriter canvasInfoWriter;

	@Inject
	private static NoteInfoWriter noteInfoWriter;

	@Inject
	private static ShareInfoWriter shareInfoWriter;

	@Inject
	private static UserInfoWriter userInfoWriter;

	@Override
	public Sendable convert(String value) {
		//this converter is incapable, so return null
		return null;
	}

	
	
	@Override
	public String serialize(Sendable value) {
		if (value instanceof AccountInfo) {
			return accountInfoWriter.toJson((AccountInfo) value);
		} else if (value instanceof CanvasInfo) {
			return canvasInfoWriter.toJson((CanvasInfo) value);
		} else if (value instanceof NoteInfo) {
			return noteInfoWriter.toJson((NoteInfo) value);
		} else if (value instanceof ShareInfo) {
			return shareInfoWriter.toJson((ShareInfo) value);
		} else if (value instanceof UserInfo) {
			return userInfoWriter.toJson((UserInfo) value);
		}
		return "null";
	}

}
