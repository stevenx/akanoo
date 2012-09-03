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
package com.akanoo.client.messages;

import com.google.gwt.i18n.client.Messages;

public interface Languages extends Messages {

	@DefaultMessage("New Note")
	String sampleBody();

	@DefaultMessage("Untitled Canvas")
	String defaultCanvasTitle();

	@DefaultMessage("Specify the title of your canvas")
	String specifyCanvasTitle();

	@DefaultMessage("Set a new canvas title")
	String renameCanvasTitle();
	
	@DefaultMessage("Are you sure you want to delete canvas \"{0}\"? This cannot be undone.")
	String confirmDeletionMessage(String title);

	@DefaultMessage("Share Canvas")
	String shareCanvasVerb();

	@DefaultMessage("Rename Canvas")
	String renameCanvasVerb();
	
	@DefaultMessage("Delete Canvas")
	String deleteCanvasVerb();

	@DefaultMessage("{0}...")
	String withDots(String string);

	@DefaultMessage("Close")
	String closeVerb();

	@DefaultMessage("Share")
	String shareVerb();

	@DefaultMessage("Add")
	String addVerb();

	@DefaultMessage("Create")
	String createVerb();

	@DefaultMessage("Save")
	String saveVerb();
	
	@DefaultMessage("Details for canvas \"{0}\"")
	String canvasDetailsTitle(String title);

	@DefaultMessage("The current canvas is no longer available")
	String canvasNoLongerAvailableMessage();

	@DefaultMessage("Sharing of \"{0}\"")
	String canvasSharingTitle(String title);

	@DefaultMessage("Share via email")
	String inviteByEmailTitle();

	@DefaultMessage("Email address already among the collaborators")
	String duplicateEmailMessage();

	@DefaultMessage("That is not a valid email address!")
	String invalidEmailMessage();
	
	@DefaultMessage("Akanoo requires a modern browser. Please install a recent version of Chrome or Firefox. (Other browsers with WebSocket support might work as well.)")
	String webSocketSupportMissingAlert();

	@DefaultMessage("Delete Note")
	String deleteNoteVerb();
	
	@DefaultMessage("Resize Note")
	String resizeNoteVerb();
}
