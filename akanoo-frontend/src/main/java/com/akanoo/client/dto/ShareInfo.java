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