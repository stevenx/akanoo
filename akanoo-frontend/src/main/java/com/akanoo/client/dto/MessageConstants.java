package com.akanoo.client.dto;

public enum MessageConstants {
	/**
	 * Initializes session
	 * client->server and server->client (also sends loadfriends)
	 */
	initialize("initialize"),

	/**
	 * Provides list of friends to the client
	 * client->server and server->client (also triggered by initialize)
	 */
	loadfriends("loadfriends"),

	/**
	 * Creates a new canvas
	 * client->server, will reply with initialize
	 */
	createcanvas("createcanvas"),

	/**
	 * Loads a specific canvas
	 * client->server, server->client (only to the caller)
	 */
	loadcanvas("loadcanvas"),

	/**
	 * Loads collaborators/shares of the given canvas
	 * client->server, server->client
	 */
	loadshares("loadshares"),

	/**
	 * Updates the shares of the given canvas
	 * client->server, will notify all affected users of the changes
	 */
	sharecanvas("sharecanvas"),

	/**
	 * Rename the the given canvas
	 * client->server, will notify all affected users of the changes, will also notify the renaming client when the operation succeeded
	 */
	renamecanvas("renamecanvas"),
	
	/**
	 * Deletes the canvas
	 * client->server, will broadcast with "initialize" to all affected users
	 */
	deletecanvas("deletecanvas"),

	/**
	 * Create note
	 * client->server, server->clients
	 */
	createnote("createnote"),

	/**
	 * Focus note, currently puts that note into edit state
	 * Sent by the server to put a newly created note into edit state (as suggested by Olaf Schneider)
	 * server->(calling) client
	 */
	focusnote("focusnote"),
	
	/**
	 * Update note body
	 * client->server, server->clients
	 */
	updatenote("updatenote"),

	/**
	 * Move note
	 * client->server, server->clients
	 */
	movenote("movenote"),

	/**
	 * Remove note
	 * client->server, server->clients
	 */
	removenote("removenote");

	private String messageCode;

	public String toString() {
		return messageCode;
	}

	public static MessageConstants fromString(String messageCode) {
		for (MessageConstants value : values()) {
			if (value.messageCode.equals(messageCode)) {
				return value;
			}
		}
		return null;
	}

	MessageConstants(String messageCode) {
		this.messageCode = messageCode;
	}

}
