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

public class NoteInfo extends Sendable {
	public interface NoteInfoReader extends JsonReader<NoteInfo> {
	}

	public interface NoteInfoWriter extends JsonWriter<NoteInfo> {
	}

	//id of canvas this note belongs
	public long canvasId;

	//id of note
	public long id;
	
	//x coordinate on canvas
	public int x;
	//y
	public int y;
	//body of note
	public String body;
	//back side body of note
	public String backBody;
	//url of note
	public String url;
}
