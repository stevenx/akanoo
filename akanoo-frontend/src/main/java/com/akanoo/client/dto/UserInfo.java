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
