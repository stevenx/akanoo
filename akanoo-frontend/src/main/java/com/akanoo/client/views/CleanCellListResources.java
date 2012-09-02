package com.akanoo.client.views;

import com.google.gwt.user.cellview.client.CellList.Resources;
import com.google.gwt.user.cellview.client.CellList.Style;

public interface CleanCellListResources extends Resources {

	public interface CleanStyle extends Style {

		String CLEAN_CSS = "com/akanoo/client/views/CleanCellList.css";

	}

	@Override
	@Source(CleanStyle.CLEAN_CSS)
	public CleanStyle cellListStyle();
}
