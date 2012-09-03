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
package com.akanoo.client.widgets;

import com.akanoo.client.dto.UserInfo;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.inject.Inject;

public class UserCell extends AbstractCell<UserInfo> {

	/**
	 * Resources that match the GWT standard style theme.
	 */
	public interface Resources extends ClientBundle {
		/**
		 * The styles used in this widget.
		 */
		@Source(Style.DEFAULT_CSS)
		Style userCellStyle();
	}

	public interface Style extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "com/akanoo/client/widgets/UserCell.css";

		String cell();

		String thumb();

		String name();
	}

	@Inject
	private static Resources resources;

	public interface Template extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<div class=\"{0}\"><img class=\"{1}\" src=\"{2}\"/><div class=\"{3}\">{4}</div></div>")
		SafeHtml cell(String outerStyleName, String thumbStyleName,
				SafeUri thumbUrl, String nameStyleName, String nameString);

		@SafeHtmlTemplates.Template("<div class=\"{0}\"><div class=\"{1}\">{2}</div></div>")
		SafeHtml cellSimple(String outerStyleName, String nameStyleName,
				String nameString);
	}

	@Inject
	private static Template template;

	public UserCell() {
		resources.userCellStyle().ensureInjected();
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			UserInfo value, SafeHtmlBuilder sb) {
		Style style = resources.userCellStyle();
		SafeUri thumbUrl = null;
		if (value.thumbUrl != null)
			try {
				thumbUrl = UriUtils.fromString(value.thumbUrl);
			} catch (Exception ex) {
			}

		if (thumbUrl != null)
			sb.append(template.cell(style.cell(), style.thumb(), thumbUrl,
					style.name(), value.name));
		else
			sb.append(template.cellSimple(style.cell(), style.name(),
					value.name));
	}
}
