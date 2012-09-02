package com.akanoo.client.dto;

import java.util.List;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;

public class AccountInfo extends Sendable {
	public interface AccountInfoReader extends JsonReader<AccountInfo> {
	}

	public interface AccountInfoWriter extends JsonWriter<AccountInfo> {
	}

	public UserInfo currentUser;
	public List<CanvasInfo> canvases;
	public List<UserInfo> friends;

}