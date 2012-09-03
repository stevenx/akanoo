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

public class ShareInfo extends Sendable {
	public interface ShareInfoReader extends JsonReader<ShareInfo> {
	}

	public interface ShareInfoWriter extends JsonWriter<ShareInfo> {
	}

	public long canvasId;

	public boolean read;
	public boolean write;
	public boolean admin;
	public String username;
	public String email;
}
