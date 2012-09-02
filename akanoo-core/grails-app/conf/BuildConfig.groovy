grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
	// inherit Grails' default dependencies
	inherits("global") {
		// uncomment to disable ehcache
		// excludes 'ehcache'
	}
	log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

	pom true

	repositories {
		mavenLocal()
		grailsCentral()
		// uncomment the below to enable remote dependency resolution
		// from public Maven repositories
		mavenCentral()
	}
	dependencies {

	}

	plugins {

	}
}