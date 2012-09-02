package com.akanoo.client.views;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.ui.Widget;

public interface HasDragAndResizeHandles extends HasDragHandle {
	Widget getResizeHandle();
}
