package com.akanoo.client.presenters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.akanoo.client.GoogleAnalyticsConstants;
import com.akanoo.client.atmosphere.CometListener;
import com.akanoo.client.atmosphere.RemoveNoteEvent;
import com.akanoo.client.atmosphere.RemoveNoteEvent.RemoveNoteHandler;
import com.akanoo.client.atmosphere.UpdateNoteEvent;
import com.akanoo.client.atmosphere.UpdateNoteEvent.UpdateNoteHandler;
import com.akanoo.client.dto.CanvasInfo;
import com.akanoo.client.dto.MessageConstants;
import com.akanoo.client.dto.NoteInfo;
import com.akanoo.client.events.CometConnectedEvent;
import com.akanoo.client.events.ShareButtonClickedEvent;
import com.akanoo.client.events.CometConnectedEvent.CometConnectedHandler;
import com.akanoo.client.events.CreateNoteEvent;
import com.akanoo.client.events.CreateNoteEvent.CreateNoteHandler;
import com.akanoo.client.events.FocusNoteEvent;
import com.akanoo.client.events.FocusNoteEvent.FocusNoteHandler;
import com.akanoo.client.events.LoadCanvasEvent;
import com.akanoo.client.events.LoadCanvasEvent.LoadCanvasHandler;
import com.akanoo.client.events.MoveNoteEvent;
import com.akanoo.client.events.MoveNoteEvent.MoveNoteHandler;
import com.akanoo.client.events.ShareButtonClickedEvent.ShareButtonClickedHandler;
import com.akanoo.client.place.NameTokens;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class CanvasPresenter extends
		Presenter<CanvasPresenter.MyView, CanvasPresenter.MyProxy> implements
		CanvasUiHandlers, RemoveNoteHandler, UpdateNoteHandler,
		MoveNoteHandler, CreateNoteHandler, LoadCanvasHandler,
		CometConnectedHandler, FocusNoteHandler, ShareButtonClickedHandler {

	public interface Point {

		int getX();

		int getY();

	}

	public interface MyView extends View, HasUiHandlers<CanvasUiHandlers> {

		void addNote(Note note);

		void clearNotes();

		void updateNoteBody(Note note);

		void updateNotePosition(Note note);

		void focusNote(Note note);

		String getSampleText();

		void removeNote(Note note);

		void setEnabled(boolean b);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.canvas)
	public interface MyProxy extends ProxyPlace<CanvasPresenter> {
	}

	private List<Note> notes;
	private CometListener cometListener;

	@Inject
	public CanvasPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, CometListener cometListener) {
		super(eventBus, view, proxy);

		getView().setUiHandlers(this);

		notes = new ArrayList<Note>();

		this.cometListener = cometListener;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.CONTENT_SLOT, this);
	}

	private HandlerRegistration handler = null;
	private CanvasInfo canvas;
	private long canvasId;

	@Inject
	private GoogleAnalytics googleAnalytics;

	@Inject
	private Provider<SharingPopupPresenter> sharingProvider;
	
	/**
	 * To support URL parameters
	 */
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		long canvasId = Long.parseLong(request.getParameter(NameTokens.idparam,
				"-1"));

		if (canvasId < 0) {
			getProxy().manualRevealFailed();
			return;
		}

		this.canvasId = canvasId;

		if (!cometListener.checkConnection()) {
			// wait for the connection to establish
			handler = getEventBus().addHandler(CometConnectedEvent.getType(),
					new CometConnectedHandler() {

						@Override
						public void onCometConnected(CometConnectedEvent event) {
							getProxy().manualReveal(CanvasPresenter.this);
							GWT.log("Loading canvas after connect, already connected");
							loadCanvas();
							handler.removeHandler();
							handler = null;
						}
					});
		} else {
			getProxy().manualReveal(this);
			GWT.log("Loading canvas, already connected");
			loadCanvas();
		}

		// track "before" load event
		if (GoogleAnalyticsConstants.ENABLE_ANALYTICS)
			googleAnalytics.trackEvent(GoogleAnalyticsConstants.categorycanvas,
					GoogleAnalyticsConstants.actionopen,
					GoogleAnalyticsConstants.before, (int) canvasId);
	}

	@Override
	protected void onBind() {
		addRegisteredHandler(RemoveNoteEvent.getType(), this);
		addRegisteredHandler(UpdateNoteEvent.getType(), this);
		addRegisteredHandler(MoveNoteEvent.getType(), this);
		addRegisteredHandler(CreateNoteEvent.getType(), this);
		addRegisteredHandler(FocusNoteEvent.getType(), this);
		addRegisteredHandler(LoadCanvasEvent.getType(), this);

		addRegisteredHandler(CometConnectedEvent.getType(), this);
		
		addRegisteredHandler(ShareButtonClickedEvent.getType(), this);
	}

	@Override
	public void createNote(Point position, String body) {
		NoteInfo noteInfo = new NoteInfo();

		noteInfo.x = position.getX();
		noteInfo.y = position.getY();
		noteInfo.body = body;
		noteInfo.canvasId = canvas.id;

		cometListener.send(MessageConstants.createnote.toString(), noteInfo);

	}

	@Override
	public void updateNoteBody(Note note, String text) {
		NoteInfo noteInfo = new NoteInfo();
		noteInfo.body = text;
		noteInfo.id = note.getId();
		noteInfo.canvasId = canvas.id;

		cometListener.send(MessageConstants.updatenote.toString(), noteInfo);
	}

	@Override
	public void moveNote(Note note, Point point) {
		NoteInfo noteInfo = new NoteInfo();
		noteInfo.x = point.getX();
		noteInfo.y = point.getY();
		noteInfo.id = note.getId();
		noteInfo.canvasId = canvas.id;

		cometListener.send(MessageConstants.movenote.toString(), noteInfo);
	}

	@Override
	public void removeNote(Note note) {
		NoteInfo noteInfo = new NoteInfo();
		noteInfo.id = note.getId();
		noteInfo.canvasId = canvas.id;

		cometListener.send(MessageConstants.removenote.toString(), noteInfo);
	}

	@Override
	public boolean useManualReveal() {
		return true;
	}

	@Override
	public void onCometConnected(CometConnectedEvent event) {
		GWT.log("Loading canvas due to connection event");
		loadCanvas(false);
	}

	private void loadCanvas() {
		loadCanvas(true);
	}

	private void loadCanvas(boolean clear) {
		CanvasInfo canvasInfo = new CanvasInfo();

		canvasInfo.id = canvasId;

		if (clear) {
			getView().setEnabled(false);
			getView().clearNotes();
		}

		cometListener.send(MessageConstants.loadcanvas.toString(), canvasInfo);
	}

	@Override
	public void onRemoveNote(RemoveNoteEvent event) {
		Note removedNote = null;
		for (Note note : notes) {
			if (note.getId() == event.getNoteInfo().id) {
				removedNote = note;
				break;
			}
		}

		if (removedNote != null) {
			onRemoveNote(removedNote);
		}
	}

	private void onRemoveNote(Note removedNote) {
		getView().removeNote(removedNote);
		notes.remove(removedNote);
	}

	@Override
	public void onUpdateNote(UpdateNoteEvent event) {
		for (Note note : notes) {
			if (note.getId() == event.getNoteInfo().id) {
				String newBody = event.getNoteInfo().body;
				onUpdateNoteIfChanged(note, newBody);
				return;
			}
		}

		GWT.log("failed to update note");
	}

	private void onUpdateNoteIfChanged(Note note, String newBody) {
		if (!note.getBody().equals(newBody)) {
			note.setBody(newBody);
			getView().updateNoteBody(note);
			GWT.log("updated note body");
		}
	}

	@Override
	public void onMoveNote(MoveNoteEvent event) {
		for (Note note : notes) {
			if (note.getId() == event.getNoteInfo().id) {
				int newX = event.getNoteInfo().x;
				int newY = event.getNoteInfo().y;
				onMoveNoteIfChanged(note, newX, newY);
				return;
			}
		}
	}

	private void onMoveNoteIfChanged(Note note, int newX, int newY) {
		if (note.getX() != newX && note.getY() != newY) {
			note.setX(newX);
			note.setY(newY);
			getView().updateNotePosition(note);
			GWT.log("updated note position");
		}
	}

	@Override
	public void onCreateNote(CreateNoteEvent event) {
		if (event.getNoteInfo().canvasId != canvasId)
			return;

		doCreateNote(event.getNoteInfo());
	}

	@Override
	public void onFocusNote(FocusNoteEvent event) {
		for (Note note : notes) {
			if (note.getId() == event.getNoteInfo().id) {
				getView().focusNote(note);
				GWT.log("focussed note");
				return;
			}
		}
	}

	@Override
	public void onLoadCanvas(LoadCanvasEvent event) {
		if (event.getCanvasInfo().id != canvasId)
			return;

		List<NoteInfo> remoteNotes = event.getCanvasInfo().notes;

		Set<Note> orphanedNotes = new HashSet<Note>();

		// identify for orphaned notes
		for (Note note : notes) {
			boolean foundRemote = false;
			for (NoteInfo remoteNote : remoteNotes) {
				if (note.getId() == remoteNote.id) {
					foundRemote = true;
				}
			}

			if (!foundRemote) {
				orphanedNotes.add(note);
			}
		}

		// remove orphaned notes
		for (Note orphanedNote : orphanedNotes) {
			getView().removeNote(orphanedNote);
			notes.remove(orphanedNote);
		}

		// add/update remote notes
		for (NoteInfo remoteNote : remoteNotes) {
			boolean found = false;
			for (Note note : notes) {
				if (remoteNote.id == note.getId()) {
					// update existing notes if changed
					onUpdateNoteIfChanged(note, remoteNote.body);
					onMoveNoteIfChanged(note, remoteNote.x, remoteNote.y);
					found = true;
				}
			}

			// create new note
			if (!found) {
				// create note (will also add the note to the list of notes)
				doCreateNote(remoteNote);
			}
		}

		this.canvas = event.getCanvasInfo();

		// enable editing
		getView().setEnabled(true);

		if (remoteNotes.size() == 0) {
			// show tour image
			getView().clearNotes();
		}

		// track "before" load event
		if (GoogleAnalyticsConstants.ENABLE_ANALYTICS)
			googleAnalytics.trackEvent(GoogleAnalyticsConstants.categorycanvas,
					GoogleAnalyticsConstants.actionopen,
					GoogleAnalyticsConstants.load, (int) canvasId);
	}

	private void doCreateNote(NoteInfo noteInfo) {
		Note note = new Note();
		note.setId(noteInfo.id);
		note.setX(noteInfo.x);
		note.setY(noteInfo.y);
		note.setBody(noteInfo.body);

		notes.add(note);
		getView().addNote(note);
	}
	
	@Override
	public void onShareButtonClicked(ShareButtonClickedEvent event) {
		SharingPopupPresenter sharingPopup = sharingProvider.get();
		sharingPopup.setCanvasInfo(canvas.title, canvasId);
		addToPopupSlot(sharingPopup);
	}
}
