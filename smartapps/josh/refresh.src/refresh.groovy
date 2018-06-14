definition(
    name: "Refresh",
    namespace: "Josh",
    author: "Josh",
    description: "Refresh!",
    category: "My Apps",
    iconUrl: "http://cdn.device-icons.smartthings.com/secondary/refresh.png",
    iconX2Url: "http://cdn.device-icons.smartthings.com/secondary/refresh.png"
)

preferences {
    section("Devices") {
		input "pollingDevices", "capability.refresh", multiple: true, title: "Refresh devices"
	}
    section("Interval (defaults = 30)") {
		input "pollingInterval", "decimal", title: "Interval", required: false
        input "pollingBackup", "decimal", title: "Backup Interval", required: false   
	}
    section("Movement") {
    	input "motions", "capability.motionSensor", title: "Motion sensors:", multiple: true, required: false
        input "motionInterval", "decimal", title: "Motion delay", required: false

    }    
    
}

def motionActiveHandler(evt) {
  def curTime = now()
  if (state.lastHit == null) state.lastHit = 0
  def diff = (curTime - state.lastHit)
  
   log.debug "Hit! $diff for ${motionInterval * 1000}"

  if (diff > (motionInterval * 1000)) {
  	log.debug "Motion making them go!"
    unschedule()
    doPoll()
    state.lastHit = curTime
  }
}

def doPoll() {
	def timeOut = (pollingInterval != null && pollingInterval != "") ? pollingInterval : 30
	log.debug "running... next in: $timeOut"
    pollingDevices.refresh()
    state.lastPoll = now()
	runIn( timeOut, doPoll)
}

def backupCheck() {
    if (!state.lastPoll) state.lastPoll = 0
	def diff = (now() - state.lastPoll)
	log.debug "backup watching... deets-> diff:$diff || lastPoll $state.lastPoll || pb1000: ${pollingBackup * 1000}"    
    if (diff > (pollingBackup * 1000)) {
    		unschedule()
   			doPoll()
    }
	runIn( 3000, backupCheck)    
}


def appTouch(evt) {
	unschedule()
	doPoll()
    backupCheck()
}


def initialize() {
    subscribe(app, appTouch)

    if (motions) {
		motions.each() {
    		subscribe(it, "motion.active", motionActiveHandler)
        }
    }   
    doPoll()
    backupCheck()
}

def installed() {
	log.debug "Installed with settings: ${settings}"
    state.lastHit = 0
 	initialize()   
}

def updated() {
	log.debug "Updated with settings: ${settings}"
    unsubscribe()
    unschedule()
	initialize()   
}
