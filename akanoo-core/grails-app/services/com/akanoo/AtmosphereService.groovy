package com.akanoo


import groovy.json.JsonSlurper

import java.io.IOException
import java.io.Serializable
import java.util.List

import javax.servlet.ServletConfig
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.AtmosphereResource
import org.atmosphere.cpr.BroadcasterFactory
import org.atmosphere.gwt.server.AtmosphereGwtHandler
import org.atmosphere.gwt.server.GwtAtmosphereResource
import org.atmosphere.gwt.server.impl.WebsocketResponseWriter
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.transaction.annotation.Transactional;

import com.akanoo.UserServiceInterface


class AtmosphereService extends AtmosphereGwtHandler {

	static transactional = true

	// session key under which the HomeController will store the locale since the websocket
	// requests do not contain a locale property but we need the locale for emailing	
	public static final String LOCALE_KEY = "locale"
	
	// comet timeout, not sure when this value will actually have an effect
	private static final int DEFAULT_TIMEOUT = 60000 //one minute
	
	// url mapping
	static atmosphere = [mapping: '/atmosphere/gwt']

	def clientService
	
	UserServiceInterface userService
	
	@Override
	public void doPost(HttpServletRequest postRequest, HttpServletResponse postResponse, List<Serializable> messages, GwtAtmosphereResource cometResource) {
		def authenticationChanged = false
		
		if(cometResource.writer instanceof WebsocketResponseWriter) {
			AtmosphereResource resource = cometResource.atmosphereResource
			
			userService.restoreContextFromSession resource.session
						
			if(userService.hasRole("ROLE_USER")) {
				authenticationChanged = true
			}
			else {
				log.error "Failed to find authentication in comet resource"
				return
			}
			
			// set up locale
			LocaleContextHolder.locale = resource.session.getAttribute(LOCALE_KEY)
		}
		
	
		// process incoming message	
		for(Serializable messageValue : messages) {
			if(messageValue instanceof String) {
				try {
					def jsonReader = new JsonSlurper()
					def message = jsonReader.parseText messageValue
					
					//convert Integer to Long for the permission control to work
					if(message?.body?.canvasId instanceof Integer) {
						message.body.canvasId = new Long(message.body.canvasId)
					}
					if(message?.body?.id instanceof Integer) {
						message.body.id = new Long(message.body.id)
					}
					
					if(message.code) {
						//message.body corresponds to the DTOs sent by the client and will be a map
						//created by the JsonSlurper
						clientService."$message.code" message.body, cometResource
					}
				}
				catch(Exception ex) {
					log.error "Exception in atmosphere handler", ex
				}
			}
		}
		
		
		if(authenticationChanged) {
			// reset locale
			LocaleContextHolder.resetLocaleContext()
	
			// reset security (important since this thread might be used in another security context) 		
			userService.resetContext()
		}
	}
	
	@Override
	public int doComet(GwtAtmosphereResource resource) throws ServletException,
			IOException {
				
		def connectionBroadcaster = BroadcasterFactory.getDefault().lookup("connection$resource.connectionID",true);
		
		resource.getAtmosphereResource().setBroadcaster(connectionBroadcaster);
		
		DEFAULT_TIMEOUT
	}
}
