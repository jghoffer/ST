definition(
    name: "Manager",
    namespace: "linkerPro",
    author: "Josh",
    description: "Woo!",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    // The parent app preferences are pretty simple: just use the app input for the child app.
    page(name: "mainPage", title: "Links:", install: true, uninstall: true,submitOnChange: true) {
        	section {
            app(name: "deviceApp", appName: "Routine", namespace: "linkerPro", title: "+ Add New...", multiple: true)

		}
        section(mobileOnly:true,"Admin") { label title: "Name", required: false }
	}
}


def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}

def initialize() {

    // nothing needed here, since the child apps will handle preferences/subscriptions
    // this just logs some messages for demo/information purposes
    log.debug "there are ${childApps.size()} child smartapps"
    childApps.each {child ->
        log.debug "child app: ${child.label}"
        child.initialize()
    }
}