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
import java.util.List;

import com.akanoo.client.dto.UserInfo;
import com.akanoo.client.messages.Languages;
import com.akanoo.client.presenters.CanvasPresenter;
import com.akanoo.client.presenters.CanvasUiHandlers;
import com.akanoo.client.presenters.Note;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class CanvasView extends ViewWithUiHandlers<CanvasUiHandlers> implements
		CanvasPresenter.MyView {

	private final class TextBlurHandler implements BlurHandler {
		private final TextBoxBase textBox;
		private final Label bodyLabel;
		private final Note note;

		private TextBlurHandler(TextBoxBase textBox, Label bodyLabel, Note note) {
			this.textBox = textBox;
			this.bodyLabel = bodyLabel;
			this.note = note;
		}

		@Override
		public void onBlur(BlurEvent event) {
			GWT.log("Textbox blurred");

			textBox.removeFromParent();

			String text = textBox.getText();
			if (!text.isEmpty()) {
				bodyLabel.setVisible(true);
				getUiHandlers().updateNoteBody(note, text);

			} else {
				getUiHandlers().removeNote(note);
			}

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					GWT.log("Editing finished");
					editing = false;
				}
			});
		}
	}

	public class NoteRepresentation {

		public Note note;
		public Label bodyLabel;
		public Label backBodyLabel;
		public FlowPanel notePanel;

	}

	private static class NoteClickHandler implements ClickHandler {
		private Note note;

		public NoteClickHandler(Note note) {
			this.note = note;
		}

		@Override
		public void onClick(ClickEvent event) {
			event.stopPropagation();
			this.noteClicked(note);
		}

		protected void noteClicked(Note note) {

		}
	}

	/**
	 * Resources that match the GWT standard style theme.
	 */
	public interface Resources extends ClientBundle {

		@Source("background.jpg")
		@ImageOptions(repeatStyle = RepeatStyle.Both)
		ImageResource lightbackground();

		@Source("tour-create-note.png")
		ImageResource tourCreateNote();

		@Source("trash3.png")
		ImageResource trash();

		@Source("trash3-hover.png")
		ImageResource trashHover();

		@Source("resizeImage.png")
		ImageResource resize();

		/**
		 * The styles used in this widget.
		 */
		@Source(Style.DEFAULT_CSS)
		Style canvasStyle();
	}

	public interface Style extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "com/akanoo/client/views/CanvasView.css";

		int pixelSensitivity();

		int sizePadding();

		int activeUserPanelHeight();

		String boundaryPanel();

		String createNotePanel();

		String focusPanel();

		String canvas();

		String note();

		String noteFocusPanel();

		String deleteNoteButton();

		String resizeNoteButton();

		String bodyLabelPosition();

		String bodyLabel();

		String bodyBox();

		String activeUserLabel();

		String activeUserPanel();

	}

	@UiField(provided = true)
	Resources resources;

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CanvasView> {
	}

	@UiField
	AbsolutePanel boundaryPanel;

	@UiField
	AbsolutePanel canvas;

	@UiField
	FocusPanel canvasFocus;

	private PickupDragController dragController;

	private Point startPosition;

	private AbsolutePositionDropController dropController;

	@Inject
	private Languages lang;

	private boolean ignoreClick;

	protected boolean editing;

	protected boolean ignoreNext;

	private boolean enabled;

	@UiField
	Widget createNotePanel;

	@UiField(provided = true)
	Languages messages;

	private List<NoteRepresentation> representations;

	@UiField
	ScrollPanel scrollPanel;

	@UiField
	Panel activeUserPanel;

	private static class Point implements CanvasPresenter.Point {
		private int x;
		private int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int getX() {
			return x;
		}

		@Override
		public int getY() {
			return y;
		}

		@Override
		public String toString() {
			return "[x=" + x + ",y=" + y + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof CanvasPresenter.Point))
				return false;

			return getX() == ((CanvasPresenter.Point) obj).getX()
					&& getY() == ((CanvasPresenter.Point) obj).getY();
		}
	}

	@Inject
	public CanvasView(final Binder binder, Resources resources,
			Languages messages) {
		this.resources = resources;
		resources.canvasStyle().ensureInjected();

		this.messages = messages;

		representations = new ArrayList<CanvasView.NoteRepresentation>();

		widget = binder.createAndBindUi(this);

		scrollPanel.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if (event.isAttached()) {
					updateCanvasSize();
				}
			}
		});

		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				updateCanvasSize();
			}
		});

		// create drag controller
		dragController = new PickupDragController(boundaryPanel, true);
		dragController.setBehaviorDragStartSensitivity(resources.canvasStyle()
				.pixelSensitivity());
		dragController.setBehaviorBoundaryPanelDrop(false);

		dragController.addDragHandler(new DragHandlerAdapter() {
			@Override
			public void onDragStart(DragStartEvent event) {
				Widget note = event.getContext().draggable;
				startPosition = new Point(canvas.getWidgetLeft(note), canvas
						.getWidgetTop(note));

				GWT.log("Starting at: " + startPosition);

				ignoreClick = true;
			}
		});

		dropController = new AbsolutePositionDropController(canvas) {

			@Override
			public void onDrop(DragContext context) {
				super.onDrop(context);

				final Widget notePanel = context.draggable;

				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						Point newPosition = new Point(canvas
								.getWidgetLeft(notePanel), canvas
								.getWidgetTop(notePanel));

						if (!newPosition.equals(startPosition)) {
							GWT.log("Now got: " + newPosition);
							Note note = findByNotePanel(notePanel).note;
							getUiHandlers().moveNote(note, newPosition);
						}

						ignoreClick = false;

					}
				});
			}
		};

		dragController.registerDropController(dropController);

		canvasFocus.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				GWT.log("Canvas focussed!");

			}
		});

		canvasFocus.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				GWT.log("Canvas mouse down!");
				if (editing)
					ignoreNext = true;
			}
		});

		canvasFocus.addTouchStartHandler(new TouchStartHandler() {

			@Override
			public void onTouchStart(TouchStartEvent event) {
				GWT.log("Canvas touch down!");
				if (editing)
					ignoreNext = true;
			}
		});

		canvasFocus.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				canvasClick(event);
			}
		});
	}

	private NoteRepresentation findByNotePanel(Widget notePanel) {
		for (NoteRepresentation noteRepresentation : representations) {
			if (noteRepresentation.notePanel == notePanel) {
				return noteRepresentation;
			}
		}
		return null;
	}

	private NoteRepresentation findByNote(Note note) {
		for (NoteRepresentation noteRepresentation : representations) {
			if (noteRepresentation.note == note) {
				return noteRepresentation;
			}
		}
		return null;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void clearNotes() {
		canvas.clear();

		canvas.add(createNotePanel);
		createNotePanel.setVisible(enabled);

		representations.clear();
	}

	private static ClickHandler clickStopPropagationHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			event.stopPropagation();
		}
	};

	@Override
	public void addNote(Note note) {
		// hide tour panel
		createNotePanel.setVisible(false);

		FlowPanel notePanel = new FlowPanel();
		notePanel.addStyleName(resources.canvasStyle().note());

		// note drag and drop
		FocusPanel noteFocusPanel = new FocusPanel();
		noteFocusPanel.addStyleName(resources.canvasStyle().noteFocusPanel());
		notePanel.add(noteFocusPanel);
		// don't pass click events to the canvas
		noteFocusPanel.addClickHandler(clickStopPropagationHandler);

		// flow panel for all note elements
		Panel noteFlowPanel = new FlowPanel();
		noteFocusPanel.add(noteFlowPanel);

		// note content area
		Label bodyLabel = new Label(note.getBody());
		bodyLabel.addStyleName(resources.canvasStyle().bodyLabelPosition());
		bodyLabel.addStyleName(resources.canvasStyle().bodyLabel());
		bodyLabel.addClickHandler(new NoteClickHandler(note) {
			@Override
			protected void noteClicked(Note note) {
				CanvasView.this.noteClicked(note);
			}
		});
		noteFlowPanel.add(bodyLabel);

		// note content area
		Label backBodyLabel = new Label(note.getBody());
		backBodyLabel.addStyleName(resources.canvasStyle().bodyLabelPosition());
		backBodyLabel.addStyleName(resources.canvasStyle().bodyLabel());
		backBodyLabel.addClickHandler(new NoteClickHandler(note) {
			@Override
			protected void noteClicked(Note note) {
				CanvasView.this.noteClicked(note);
			}
		});
		backBodyLabel.setVisible(false);
		noteFlowPanel.add(backBodyLabel);

		// delete note button
		final PushButton deleteNoteButton = new PushButton();
		deleteNoteButton.setStyleName(resources.canvasStyle()
				.deleteNoteButton());
		deleteNoteButton.getUpFace().setImage(new Image(resources.trash()));
		deleteNoteButton.getUpHoveringFace().setImage(
				new Image(resources.trashHover()));
		deleteNoteButton.setTitle(messages.deleteNoteVerb());
		deleteNoteButton.addClickHandler(new NoteClickHandler(note) {

			@Override
			protected void noteClicked(Note note) {
				getUiHandlers().removeNote(note);
			}
		});
		notePanel.add(deleteNoteButton);

		/*
		 * // add resize button PushButton resizeNoteButton = new PushButton();
		 * resizeNoteButton.setStyleName(resources.canvasStyle()
		 * .resizeNoteButton());
		 * resizeNoteButton.setTitle(messages.resizeNoteVerb());
		 * notePanel.add(resizeNoteButton);
		 */

		canvas.add(notePanel, note.getX(), note.getY());

		// drag and drop notes enabled
		dragController.makeDraggable(notePanel, noteFocusPanel);

		NoteRepresentation representation = new NoteRepresentation();
		representation.note = note;
		representation.notePanel = notePanel;
		representation.bodyLabel = backBodyLabel;
		representation.backBodyLabel = backBodyLabel;
		representations.add(representation);

		updateCanvasSize();
	}

	private void updateCanvasSize() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				int maxX = scrollPanel.getOffsetWidth();
				int maxY = scrollPanel.getOffsetHeight();
				for (NoteRepresentation noteRepresentation : representations) {
					maxX = Math.max(noteRepresentation.note.getX() + 180
							+ resources.canvasStyle().sizePadding(), maxX);
					maxY = Math.max(noteRepresentation.note.getY() + 260
							+ resources.canvasStyle().sizePadding(), maxY);
				}

				boundaryPanel.setPixelSize(maxX, maxY);
			}
		});

	}

	// create new note on click
	void canvasClick(ClickEvent event) {
		if (ignoreClick || editing || !enabled)
			return;

		if (ignoreNext) {
			ignoreNext = false;
			return;
		}

		GWT.log("Creating new note");

		Element canvasElement = canvas.getElement();
		Point position = new Point(event.getRelativeX(canvasElement),
				event.getRelativeY(canvasElement));
		getUiHandlers().createNote(position, "");
	}

	// edit note
	public void noteClicked(final Note note) {
		NoteRepresentation representation = findByNote(note);

		final FlowPanel notePanel = representation.notePanel;
		final Label bodyLabel = representation.bodyLabel;

		if (note != null) {
			final TextBoxBase textBox = new TextArea();

			textBox.setText(note.getBody());
			textBox.addStyleName(resources.canvasStyle().bodyLabelPosition());
			textBox.addStyleName(resources.canvasStyle().bodyBox());

			notePanel.insert(textBox, 0);
			bodyLabel.setVisible(false);

			editing = true;
			GWT.log("Editing started");

			textBox.addBlurHandler(new TextBlurHandler(textBox, bodyLabel, note));
			textBox.setFocus(true);
		}
	}

	@Override
	public void updateNoteBody(Note note) {
		NoteRepresentation representation = findByNote(note);
		if (representation != null) {
			representation.bodyLabel.setText(note.getBody());
			GWT.log("updated label to " + note.getBody());
		}
	}

	@Override
	public void updateNotePosition(Note note) {
		NoteRepresentation representation = findByNote(note);
		if (representation != null) {
			canvas.setWidgetPosition(representation.notePanel, note.getX(),
					note.getY());
			GWT.log("updated note position to " + note.getX() + ", "
					+ note.getY());
		}

		updateCanvasSize();
	}

	@Override
	public void focusNote(final Note note) {
		// do not focus note if we're currently in edit mode
		if (editing)
			return;

		// wait for the current event loop to finish and then focus the note
		// body label
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				// call click handler
				noteClicked(note);

			}
		});
	}

	@Override
	public void removeNote(Note note) {
		NoteRepresentation representation = findByNote(note);
		if (representation != null) {
			representations.remove(representation);
			representation.notePanel.removeFromParent();
		}
	}

	@Override
	public String getSampleText() {
		return lang.sampleBody();
	}

	@Override
	public void setEnabled(boolean b) {
		this.enabled = b;
	}

	@Override
	public void populateActiveUsers(List<UserInfo> collaborators) {
		activeUserPanel.clear();

		activeUserPanel.setVisible(collaborators.size() > 0);

		for (UserInfo userInfo : collaborators) {
			Label activeUserLabel = new Label(userInfo.name);
			activeUserLabel.setTitle(userInfo.email);
			activeUserLabel.addStyleName(resources.canvasStyle()
					.activeUserLabel());

			activeUserPanel.add(activeUserLabel);
		}
	}
}
