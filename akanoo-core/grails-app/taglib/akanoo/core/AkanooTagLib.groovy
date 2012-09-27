package akanoo.core

import com.akanoo.UserServiceInterface

class AkanooTagLib {
	
	UserServiceInterface userService
	
	def akanooHead = { attrs, body ->
		
		def currentUser = userService.currentUser
		
		//compute params for gwt
		def gwtAtmosphereUrl = createLink(controller:'atmosphere',action:'gwt',absolute: true)
		def gwtLogoutUrl = createLink(controller:'logout',action:'index',absolute: true)
		def gwtRealName = currentUser.realName ?: currentUser.email

		def model = [gwtAtmosphereUrl:gwtAtmosphereUrl,
					gwtRealName: gwtRealName,
					gwtLogoutUrl: gwtLogoutUrl]

		out << render(template:"/akanoo/appHead", plugin: "akanoo-core", model: model)
	}

	def akanooBody = { attrs, body ->
		out << render(template:"/akanoo/appBody", plugin: "akanoo-core")
	}
}
