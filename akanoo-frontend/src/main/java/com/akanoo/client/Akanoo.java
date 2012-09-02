package com.akanoo.client;

import com.google.gwt.core.client.EntryPoint;
import com.akanoo.client.gin.ClientGinjector;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

public class Akanoo implements EntryPoint {

	private final ClientGinjector ginjector = GWT.create(ClientGinjector.class);

	@Override
	public void onModuleLoad() {
		// This is required for Gwt-Platform proxy's generator
		DelayedBindRegistry.bind(ginjector);
	
		ginjector.getPlaceManager().revealCurrentPlace();
		
		ginjector.getCometService().initialize();
	}
	
	
	public static native String getAtmosphereUrl() /*-{
		return $wnd.gwtAtmosphereUrl;
	}-*/;


	/**
	 * Real name of current user, provided by HomeController in backend through app.gsp
	 */
	public static native String getRealName() /*-{
		return $wnd.gwtRealName;
	}-*/;

	/**
	 * Logout url, provided by HomeController in backend through app.gsp
	 */
	public static native String getLogoutUrl() /*-{
		return $wnd.gwtLogoutUrl;
	}-*/;
}
