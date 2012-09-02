package com.akanoo.client.dto;

import name.pehl.piriti.commons.client.CreateWith;
import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;

@CreateWith(MessageCreator.class)
public class Message {
	public interface MessageReader extends JsonReader<Message> {
	}

	public interface MessageWriter extends JsonWriter<Message> {
	}

	public String code;
	
	public Sendable body;
}
