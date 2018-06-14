/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *	Button Controller
 *
 *	Author: SmartThings
 *	Date: 2014-5-21
 */
definition(
    name: "Button Controller",
    namespace: "smartthings",
    author: "SmartThings",
    description: "Control devices with buttons.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/MyApps/Cat-MyApps.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/MyApps/Cat-MyApps@2x.png"
)

preferences {
	page(name: "selectButton")
	page(name: "configureButton")
/**
	page(name: "timeIntervalInput", title: "Only during a certain time") {
		section {
			input "starting", "time", title: "Starting", required: false
			input "ending", "time", title: "Ending", required: false
		}
	}
  */
}

def selectButton() {
	dynamicPage(name: "selectButton", title: "First, select your  device", nextPage: "configureButton", uninstall: configured()) {
		section {
			input "buttonDevice", "capability.button", title: "Button", multiple: false, required: true
		}
section(mobileOnly:true,"General") { label title: "Name", required: false }
		section(title: "More options", hidden: hideOptionsSection(), hideable: true) {

			def timeLabel = timeIntervalLabel()

			href "timeIntervalInput", title: "Only during a certain time", description: timeLabel ?: "Tap to set", state: timeLabel ? "complete" : null

			input "days", "enum", title: "Only on certain days of the week", multiple: true, required: false,
				options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]

			input "modes", "mode", title: "Only when mode is", multiple: true, required: false
		}

	}
}

def configureButton() {
	dynamicPage(name: "configureButton", title: "Now... configure!", nextPage: null, install:true, uninstall: true) {
      for (buttonNumber in 1 .. 4) {     
        section(title: "${getTitle(buttonNumber)}", hideable: true, hidden: true, getButtonSections(buttonNumber))
        }
        //section(title: "2", hideable: true, hidden: false, getButtonSections(2)) 
        //section(title: "3", hideable: true, hidden: false, getButtonSections(3)) 
        //section(title: "4", hideable: true, hidden: false, getButtonSections(4)) 
     }
}

def getButtonSections(buttonNumber) {
	return {
		input "lights_${buttonNumber}_pushed", "capability.switch", title: "Toggle:", multiple: true, required: false
        input "onSwitches_${buttonNumber}_pushed", "capability.switch", title: "Turn on:", multiple: true, required: false
        input "offSwitches_${buttonNumber}_pushed", "capability.switch", title: "Turn off:", multiple: true, required: false
		input "mode_${buttonNumber}_pushed", "mode", title: "Change mode to:", required: false
		def phrases = location.helloHome?.getPhrases()*.label
		log.trace phrases
		input "phrase_${buttonNumber}_pushed", "enum", title: "Hello Home Actions", required: false, options: phrases
		input "message_${buttonNumber}_pushed", "text", title: "Display Message:", required: false
        //input "notifications_${buttonNumber}_pushed","bool" ,title: "Push Notifications", required: false, defaultValue: false
        input "phone_${buttonNumber}_pushed","phone" ,title: "Sms Notifications", required: false
        input "refresh_${buttonNumber}_pushed", "bool", title: "Force refresh?", required: false 
        input "onModes_${buttonNumber}_pushed", "mode", title: "If mode is:", multiple: true, required: false
        input "title_${buttonNumber}_pushed", "text", title: "Custom title:", required: false
	}
}

def installed() {
	initialize()
}

def updated() {
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(buttonDevice, "button", buttonEvent)
}

def configured() {
	return buttonDevice || buttonConfigured(1) || buttonConfigured(2) || buttonConfigured(3) || buttonConfigured(4)
}

def buttonConfigured(idx) {
	return settings["lights_$idx_pushed"] ||
    settings["onSwitches_$idx_pushed"] ||
    settings["offSwitches_$idx_pushed"] ||
		settings["mode_$idx_pushed"] ||
        settings["notifications_$idx_pushed"] ||
        settings["phone_$idx_pushed"]
}

def buttonEvent(evt){
	if(allOk) {
		def buttonNumber = evt.value//data // why doesn't jsonData work? always returning [:]
		def value = evt.value
		log.debug "buttonEvent: $evt.name = $evt.value ($evt.data)"
		log.debug "button: $buttonNumber, value: $value"

		def recentEvents = buttonDevice.eventsSince(new Date(now() - 3000)).findAll{it.value == evt.value && it.data == evt.data}
		log.debug "Found ${recentEvents.size()?:0} events in past 3 seconds"

		if(recentEvents.size <= 1){
			switch(buttonNumber) {
				case ~/.*1.*/:
					executeHandlers(1, "pushed")
					break
				case ~/.*2.*/:
					executeHandlers(2, "pushed")
					break
				case ~/.*3.*/:
					executeHandlers(3, "pushed")
					break
				case ~/.*4.*/:
					executeHandlers(4, "pushed")
					break
			}
		} else {
			log.debug "Found recent button press events for $buttonNumber with value $value"
		}
	}
}

def executeHandlers(buttonNumber, value) {
	log.debug "executeHandlers: $buttonNumber - $value"

	def lights = find('lights', buttonNumber, value)
	if (lights != null) toggle(lights)
    
	def onSwitches = find('onSwitches', buttonNumber, value)
	onSwitches?.on()
    
	def offSwitches = find('offSwitches', buttonNumber, value)
	offSwitches?.off()    

	def mode = find('mode', buttonNumber, value)
	if (mode != null) changeMode(mode)

	def phrase = find('phrase', buttonNumber, value)
	if (phrase != null) location.helloHome.execute(phrase)

	def message = find('message', buttonNumber, value)

//	def notifications = find('notifications', buttonNumber, value)
	if (message) sendPush(message ?: "Button $buttonNumber was pressed" )

	def phone = find('phone', buttonNumber, value)
	if (phone) sendSms(phone, phone ?:"Button $buttonNumber was pressed")

}

def find(type, buttonNumber, value) {
	def preferenceName = type + "_" + buttonNumber + "_" + value
	def pref = settings[preferenceName]
	if(pref != null) {
		log.debug "Found: $pref for $preferenceName"
	}

	return pref
}

def getTitle(buttonNumber) {
  def custom = find("title", buttonNumber, "pushed")
  return custom ?: "Button $buttonNumber"
}


def toggle(devices) {
	log.debug "toggle: $devices = ${devices*.currentValue('switch')}"

	if (devices*.currentValue('switch').contains('on')) {
		devices.off()
	}
	else if (devices*.currentValue('switch').contains('off')) {
		devices.on()
	}
	else {
		devices.on()
	}
}

def changeMode(mode) {
	log.debug "changeMode: $mode, location.mode = $location.mode, location.modes = $location.modes"

	if (location.mode != mode && location.modes?.find { it.name == mode }) {
		setLocationMode(mode)
	}
}

// execution filter methods
private getAllOk() {
	modeOk && daysOk && timeOk
}

private getModeOk() {
	def result = !modes || modes.contains(location.mode)
	log.trace "modeOk = $result"
	result
}

private getDaysOk() {
	def result = true
	if (days) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = days.contains(day)
	}
	log.trace "daysOk = $result"
	result
}

private getTimeOk() {
	def result = true
	if (starting && ending) {
		def currTime = now()
		def start = timeToday(starting).time
		def stop = timeToday(ending).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
	log.trace "timeOk = $result"
	result
}

private hhmm(time, fmt = "h:mm a")
{
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}

private hideOptionsSection() {
	(starting || ending || days || modes) ? false : true
}

private timeIntervalLabel() {
	(starting && ending) ? hhmm(starting) + "-" + hhmm(ending, "h:mm a z") : ""
}