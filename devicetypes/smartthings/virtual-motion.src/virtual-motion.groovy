metadata {
	definition (name: "Virtual Motion", namespace: "smartthings", author: "SmartThings", mnmn: "SmartThings", vid: "generic-motion-9") {
		capability "Switch"
        capability "Actuator"
		capability "Motion Sensor"
	}
}

def installed() {
	initialize()
	sendEvent(name: "motion", value: "inactive", displayed: false)
}

def updated() {initialize()}
def initialize() {}
def parse(String description) { log.debug "$description" }

def on() {	sendEvent(name: "motion", value: "active", descriptionText: "$device.displayName detected motion") }
def off(){	sendEvent(name: "motion", value: "inactive", descriptionText: "$device.displayName motion has stopped") }