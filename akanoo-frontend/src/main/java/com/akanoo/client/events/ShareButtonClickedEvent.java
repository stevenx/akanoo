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
package com.akanoo.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * This Event will be thrown when a canvas is selected.
 */
public class ShareButtonClickedEvent extends
		GwtEvent<ShareButtonClickedEvent.ShareButtonClickedHandler> {

	public static Type<ShareButtonClickedHandler> TYPE = new Type<ShareButtonClickedHandler>();
	
	public interface ShareButtonClickedHandler extends EventHandler {
		void onShareButtonClicked(ShareButtonClickedEvent event);
	}

	public ShareButtonClickedEvent() {
	}

	@Override
	protected void dispatch(ShareButtonClickedHandler handler) {
		handler.onShareButtonClicked(this);
	}

	@Override
	public Type<ShareButtonClickedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ShareButtonClickedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ShareButtonClickedEvent());
	}
}
