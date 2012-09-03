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

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;

import com.google.gwt.view.client.ProvidesKey;

public class UserInfo extends Sendable {
	public interface UserInfoReader extends JsonReader<UserInfo> {
	}

	public interface UserInfoWriter extends JsonWriter<UserInfo> {
	}

	public String thumbUrl;
	public String name;
	public String email;
	public String uid;
	public boolean enabled;

	public static ProvidesKey<UserInfo> keyprovider = new ProvidesKey<UserInfo>() {

		@Override
		public Object getKey(UserInfo item) {
			return item.uid != null ? item.uid : item.email;
		}
	};

}
