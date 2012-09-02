package com.akanoo

import javax.servlet.ServletConfig
import javax.servlet.ServletException

import org.atmosphere.cpr.AtmosphereServlet
import org.springframework.web.context.support.WebApplicationContextUtils;

class StratosphereServlet extends AtmosphereServlet {
	@Override
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);

		def webApplicationContext = WebApplicationContextUtils.getWebApplicationContext sc.servletContext

		def atmosphereService = webApplicationContext.getBean("atmosphereService")

		framework.addAtmosphereHandler("/atmosphere/gwt",atmosphereService)
	}
}
