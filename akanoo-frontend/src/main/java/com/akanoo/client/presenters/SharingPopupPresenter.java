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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.akanoo.client.GoogleAnalyticsConstants;
import com.akanoo.client.atmosphere.CometListener;
import com.akanoo.client.atmosphere.CometService;
import com.akanoo.client.dto.CanvasInfo;
import com.akanoo.client.dto.MessageConstants;
import com.akanoo.client.dto.ShareInfo;
import com.akanoo.client.dto.UserInfo;
import com.akanoo.client.events.DetailsPopupCloseEvent;
import com.akanoo.client.events.LoadSharesEvent;
import com.akanoo.client.events.LoadSharesEvent.LoadSharesHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.regexp.shared.RegExp;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class SharingPopupPresenter extends
		PresenterWidget<SharingPopupPresenter.MyView> implements
		SharingPopupUiHandlers, LoadSharesHandler {

	public interface MyView extends PopupView,
			HasUiHandlers<SharingPopupUiHandlers> {

		void setTitle(String title);

		void populateCollaborators(List<UserInfo> collaborators);

		Set<UserInfo> getSelectedUsers();

		void setSelectedUsers(Collection<UserInfo> collaborators);

		String getEmail();

		void addInvitee(UserInfo userInfo);

		void alertDuplicateEmail();

		void alertInvalidEmail();

		void clearEmail();

		void setEnabled(boolean enabled);
	}

	private static final String emailpattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SHARE_SLOT = new Type<RevealContentHandler<?>>();
	private List<UserInfo> collaborators;

	@Inject
	private CometListener cometListener;

	private Long canvasId;
	private List<UserInfo> friends;
	@Inject
	private CometService cometService;

	@Inject
	private GoogleAnalytics googleAnalytics;

	@Inject
	public SharingPopupPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);

		getView().setUiHandlers(this);
	}

	public void setCanvasInfo(String title, Long canvasId) {
		this.canvasId = canvasId;
		getView().setTitle(title);
	}

	@Override
	public void onCloseClicked() {
		getView().hide();
	}

	@Override
	protected void onBind() {
		addRegisteredHandler(LoadSharesEvent.getType(), this);
	}

	@Override
	protected void onReveal() {
		friends = new ArrayList<UserInfo>(cometService.getFriends());

		fetchCanvas();
	}

	private void fetchCanvas() {
		// load canvas (including collaborators)
		CanvasInfo canvasInfo = new CanvasInfo();
		canvasInfo.id = canvasId;
		cometListener.send(MessageConstants.loadshares.toString(), canvasInfo);
	}

	@Override
	public void onLoadShares(LoadSharesEvent event) {
		if (!isVisible())
			return;

		if (canvasId.equals(event.getCanvasInfo().id)) {
			collaborators = event.getCanvasInfo().collaborators;
			getView().setEnabled(true);
			populateView();
		}
	}

	private void populateView() {
		List<UserInfo> friendsAndCollaborators = new ArrayList<UserInfo>();

		if (collaborators != null) {
			friendsAndCollaborators.addAll(collaborators);
		}

		if (friends != null) {
			for (UserInfo friend : friends) {
				boolean found = false;
				for (UserInfo otherFriend : friendsAndCollaborators) {
					if (otherFriend.uid.equals(friend.uid)) {
						found = true;
						break;
					}
				}

				if (!found)
					friendsAndCollaborators.add(friend);
			}
		}

		getView().populateCollaborators(friendsAndCollaborators);
		if (collaborators != null) {
			getView().setSelectedUsers(collaborators);
		}
	}

	@Override
	public void onSelectionChange() {
		// ignore for now
	}

	@Override
	public void onAddEmailClicked() {
		String email = getView().getEmail();

		RegExp regex = RegExp.compile(emailpattern);

		if (!regex.test(email)) {
			getView().alertInvalidEmail();
			return;
		}

		List<UserInfo> users = new ArrayList<UserInfo>();
		if (collaborators != null)
			users.addAll(collaborators);
		if (friends != null)
			users.addAll(friends);

		for (UserInfo user : users) {
			if (email.equalsIgnoreCase(user.email)) {
				getView().alertDuplicateEmail();
				return;
			}
		}

		UserInfo userInfo = new UserInfo();
		userInfo.email = email;
		// set name to email address also
		userInfo.name = email;

		// add invitee to list of friends (so duplicates aren't possible)
		friends.add(userInfo);

		getView().addInvitee(userInfo);
		getView().clearEmail();
	}

	@Override
	public void onShareClicked() {
		if (collaborators == null) {
			return;
		}

		Set<UserInfo> updatedCollaborators = getView().getSelectedUsers();

		// compute unshared users
		List<UserInfo> unsharedUsers = new ArrayList<UserInfo>();
		for (UserInfo collaborator : collaborators) {
			if (!updatedCollaborators.contains(collaborator)) {
				unsharedUsers.add(collaborator);
			}
		}

		// compute new users
		List<UserInfo> sharedUsers = new ArrayList<UserInfo>();
		for (UserInfo updatedCollaborator : updatedCollaborators) {
			if (!collaborators.contains(updatedCollaborator)) {
				sharedUsers.add(updatedCollaborator);
			}
		}

		// unshare deleted users
		for (UserInfo unsharedUser : unsharedUsers) {
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.admin = false;
			shareInfo.write = false;
			shareInfo.read = false;
			shareInfo.canvasId = canvasId;
			shareInfo.username = unsharedUser.uid;
			cometListener.send(MessageConstants.sharecanvas.toString(),
					shareInfo);
		}

		// share newusers
		for (UserInfo sharedUser : sharedUsers) {
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.admin = true;
			shareInfo.write = true;
			shareInfo.read = true;
			shareInfo.canvasId = canvasId;
			if (sharedUser.uid != null)
				shareInfo.username = sharedUser.uid;
			if (sharedUser.email != null)
				shareInfo.email = sharedUser.email;
			cometListener.send(MessageConstants.sharecanvas.toString(),
					shareInfo);
		}

		getView().hide();
		// close the option popup view
		fireEvent(new DetailsPopupCloseEvent());

		// track "before" load event
		if (GoogleAnalyticsConstants.ENABLE_ANALYTICS)
			googleAnalytics.trackEvent(
					GoogleAnalyticsConstants.categorysharing,
					GoogleAnalyticsConstants.actionshareunshare);
	}

	@Override
	protected void onHide() {
		// only in popups: unbind this presenter when it's being hidden
		unbind();
	}
}
