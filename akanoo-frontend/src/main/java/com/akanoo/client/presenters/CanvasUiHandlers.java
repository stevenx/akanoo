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
package com.akanoo.client.presenters;

import com.akanoo.client.presenters.CanvasPresenter.Point;
import com.gwtplatform.mvp.client.UiHandlers;

public interface CanvasUiHandlers extends UiHandlers {

	void createNote(Point position, String body);

	void updateNoteBody(Note note, String text, boolean isBackBody);

	void moveNote(Note note, Point point);

	void removeNote(Note note);

}
