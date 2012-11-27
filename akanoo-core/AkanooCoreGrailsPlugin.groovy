import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.MeteorServlet;
import org.springframework.util.ClassUtils;

import com.akanoo.AtmosphereService;
import com.akanoo.StratosphereServlet;

class AkanooCoreGrailsPlugin {
    // the plugin version
    def version = "0.1.4"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Akanoo Core Plugin" // Headline display name of the plugin
    def author = "Fabian Gebert"
    def authorEmail = "fabian@akanoo.com"
    def description = 'Collaborative note taking app'

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/akanoo-core"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

   def doWithWebDescriptor = { xml ->
        def servlets = xml.'servlet'
        def config = application.config?.akanoo

        servlets[servlets.size() - 1] + {
            'servlet' {
                'description'('StratosphereServlet')
                'servlet-name'('StratosphereServlet')
                'servlet-class'(StratosphereServlet.name)
                /*'init-param' {
                    'param-name'(ApplicationConfig.SERVLET_CLASS)
                    'param-value'(AtmosphereService.name)
                }*/
                if (ClassUtils.isPresent("javax.servlet.AsyncContext", Thread.currentThread().getContextClassLoader())) {
                    'async-supported'(true)
                }
                config?.servlet?.initParams.each { initParam ->
                    'init-param' {
                        'param-name'(initParam.key)
                        'param-value'(initParam.value)
                    }
                }
                'load-on-startup'('1')
            }
        }

        def mappings = xml.'servlet-mapping'
        mappings[mappings.size() - 1] + {
            'servlet-mapping' {
                'servlet-name'('StratosphereServlet')
                'url-pattern'('/atmosphere/*')
            }
        }
    }

}
