grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.release.scm.enabled = false
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        mavenCentral()
    }

    def seleniumVersion = "2.21.0"

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        compile 'com.asual.lesscss:lesscss-engine:1.3.3'

        test("org.gmock:gmock:0.8.2") {
            export = false
        }
        test("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion") {
            export = false
            exclude "xml-apis"
        }
        test("org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion") {
            export = false
        }
        test("org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion") {
            export = false
        }
    }
    plugins {
        test(":spock:0.7") {
            export = false
            exclude "spock-grails-support"
        }
        test(":geb:0.9.0") {
            export = false
        }
        compile(":resources:1.1.6") {
            export = false
        }
        compile(":tomcat:$grailsVersion") {
            export = false
        }
    }
}
