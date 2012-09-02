package com.akanoo.client.dto;

import java.util.List;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;

public class CanvasInfo extends Sendable {
	public interface CanvasInfoReader extends JsonReader<CanvasInfo> {
	}

	public interface CanvasInfoWriter extends JsonWriter<CanvasInfo> {
	}

	public Long id;
	public String title;
	public List<NoteInfo> notes;
	public List<UserInfo> collaborators;
}