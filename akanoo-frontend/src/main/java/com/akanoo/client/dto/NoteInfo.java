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
}