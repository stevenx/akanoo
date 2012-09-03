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

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.akanoo.client.dto.CanvasInfo;
import com.google.gwt.event.shared.HasHandlers;

public class LoadCanvasEvent extends
		GwtEvent<LoadCanvasEvent.LoadCanvasHandler> {

	public static Type<LoadCanvasHandler> TYPE = new Type<LoadCanvasHandler>();
	private CanvasInfo canvasInfo;

	public interface LoadCanvasHandler extends EventHandler {
		void onLoadCanvas(LoadCanvasEvent event);
	}

	public LoadCanvasEvent(CanvasInfo canvasInfo) {
		this.canvasInfo = canvasInfo;
	}

	public CanvasInfo getCanvasInfo() {
		return canvasInfo;
	}

	@Override
	protected void dispatch(LoadCanvasHandler handler) {
		handler.onLoadCanvas(this);
	}

	@Override
	public Type<LoadCanvasHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadCanvasHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, CanvasInfo canvasInfo) {
		source.fireEvent(new LoadCanvasEvent(canvasInfo));
	}
}
