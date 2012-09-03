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
package com.akanoo.client.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.akanoo.client.dto.UserInfo;
import com.akanoo.client.messages.Languages;
import com.akanoo.client.presenters.SharingPopupPresenter;
import com.akanoo.client.presenters.SharingPopupUiHandlers;
import com.akanoo.client.widgets.UserCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

public class SharingPopupView extends
		PopupViewWithUiHandlers<SharingPopupUiHandlers> implements
		SharingPopupPresenter.MyView, SelectionChangeEvent.Handler {

	/**
	 * Resources that match the GWT standard style theme.
	 */
	public interface Resources extends ClientBundle {
		@ImageOptions(repeatStyle = RepeatStyle.Both)
		ImageResource lightbackground();

		/**
		 * The styles used in this widget.
		 */
		@Source({ "Buttons.css", Style.DEFAULT_CSS })
		Style sharingPopupStyle();
	}

	public interface Style extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "com/akanoo/client/views/SharingPopupView.css";

		String button();

		String active();

		String titleLabel();

		String shareLabel();

		String sharesList();
	}

	@UiField(provided = true)
	Resources resources;

	@UiField
	Label titleLabel;

	private final Widget widget;

	@UiField(provided = true)
	Languages messages;

	@UiField(provided = true)
	CellList<UserInfo> shares;

	private ListDataProvider<UserInfo> dataProvider;

	private MultiSelectionModel<UserInfo> selectionModel;

	@UiField
	TextBox emailField;

	@UiField
	PushButton shareButton;

	public interface Binder extends UiBinder<Widget, SharingPopupView> {
	}

	@Inject
	public SharingPopupView(final EventBus eventBus, final Binder binder,
			Resources resources, Languages messages,
			CleanCellListResources cellListResources) {
		super(eventBus);

		this.resources = resources;
		resources.sharingPopupStyle().ensureInjected();

		this.messages = messages;

		setupCellList(cellListResources);

		widget = binder.createAndBindUi(this);
	}

	private void setupCellList(CellList.Resources cellListResources) {
		selectionModel = new MultiSelectionModel<UserInfo>(UserInfo.keyprovider);

		// Construct a composite cell for contacts that includes a checkbox.
		List<HasCell<UserInfo, ?>> hasCells = new ArrayList<HasCell<UserInfo, ?>>();
		hasCells.add(new HasCell<UserInfo, Boolean>() {

			private CheckboxCell cell = new CheckboxCell(true, false);

			public Cell<Boolean> getCell() {
				return cell;
			}

			public FieldUpdater<UserInfo, Boolean> getFieldUpdater() {
				return null;
			}

			public Boolean getValue(UserInfo object) {
				return selectionModel.isSelected(object);
			}
		});
		hasCells.add(new HasCell<UserInfo, UserInfo>() {

			private UserCell cell = new UserCell();

			public Cell<UserInfo> getCell() {
				return cell;
			}

			public FieldUpdater<UserInfo, UserInfo> getFieldUpdater() {
				return null;
			}

			public UserInfo getValue(UserInfo object) {
				return object;
			}
		});
		CompositeCell<UserInfo> friendCell = new CompositeCell<UserInfo>(
				hasCells) {
			@Override
			public void render(Context context, UserInfo value,
					SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table><tbody><tr>");
				super.render(context, value, sb);
				sb.appendHtmlConstant("</tr></tbody></table>");
			}

			@Override
			protected Element getContainerElement(Element parent) {
				// Return the first TR element in the table.
				return parent.getFirstChildElement().getFirstChildElement()
						.getFirstChildElement();
			}

			@Override
			protected <X> void render(Context context, UserInfo value,
					SafeHtmlBuilder sb, HasCell<UserInfo, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</td>");
			}
		};

		shares = new CellList<UserInfo>(friendCell, cellListResources,
				UserInfo.keyprovider);
		shares.setSelectionModel(selectionModel,
				DefaultSelectionEventManager.<UserInfo> createCheckboxManager());

		dataProvider = new ListDataProvider<UserInfo>(UserInfo.keyprovider);
		dataProvider.addDataDisplay(shares);

		selectionModel.addSelectionChangeHandler(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@UiHandler("closeButton")
	void onCloseClicked(ClickEvent clickEvent) {
		getUiHandlers().onCloseClicked();
	}

	@UiHandler("shareButton")
	void onShareClicked(ClickEvent clickEvent) {
		getUiHandlers().onShareClicked();
	}

	@UiHandler("addEmailButton")
	void onAddEmailClicked(ClickEvent clickEvent) {
		getUiHandlers().onAddEmailClicked();
	}

	@Override
	public void setTitle(String title) {
		titleLabel.setText(messages.canvasSharingTitle(title));
	}

	@Override
	public String getEmail() {
		return emailField.getText();
	}

	@Override
	public void clearEmail() {
		emailField.setText("");
		emailField.setFocus(true);
	}

	@Override
	public void alertInvalidEmail() {
		Window.alert(messages.invalidEmailMessage());
	}

	@Override
	public void populateCollaborators(List<UserInfo> collaborators) {
		List<UserInfo> list = dataProvider.getList();
		list.clear();
		list.addAll(collaborators);
		shares.setPageSize(list.size());
	}

	@Override
	public void addInvitee(UserInfo userInfo) {
		List<UserInfo> list = dataProvider.getList();
		list.add(userInfo);
		shares.setPageSize(list.size());
		selectionModel.setSelected(userInfo, true);
	}

	@Override
	public void alertDuplicateEmail() {
		Window.alert(messages.duplicateEmailMessage());
	}

	@Override
	public void onSelectionChange(SelectionChangeEvent event) {
		getUiHandlers().onSelectionChange();
	}

	@Override
	public void setSelectedUsers(Collection<UserInfo> collaborators) {
		selectionModel.clear();
		for (UserInfo collaborator : collaborators) {
			selectionModel.setSelected(collaborator, true);
		}
	}

	@Override
	public Set<UserInfo> getSelectedUsers() {
		return selectionModel.getSelectedSet();
	}

	@Override
	public void setEnabled(boolean enabled) {
		shareButton.setEnabled(enabled);
	}
}
