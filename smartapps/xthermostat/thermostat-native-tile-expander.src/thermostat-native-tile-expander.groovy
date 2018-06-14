definition(
    name: "Thermostat (native) Tile Expander",
    namespace: "xThermostat",
    author: "Josh",
    description: "Boom",
    category: "Convenience",
    iconUrl: "http://icons.iconarchive.com/icons/iconsmind/outline/64/Temperature-2-2-icon.png",
    iconX2Url: "http://icons.iconarchive.com/icons/iconsmind/outline/64/Temperature-2-2-icon.png"
)

preferences {
  section("Thermostat") {
  	input "thermostat", "capability.thermostat", multiple: false, title: "Thermometer", required: false
  }

  section("Tiles") {
    input name: "upButton", type: "capability.switch", title: "Up Button"
    input name: "downButton", type: "capability.switch", title: "Down Button"
    input name: "display", type: "capability.switch", title: "Display"
    input name: "heatButton", type: "capability.switch", title: "Heat Button"
    input name: "coolButton", type: "capability.switch", title: "Cool Button"    
  }
}

def installed() {  updated()	}
def updated() {
	log.debug "updated...?"
    unsubscribe()
	subscribe(thermostat, "coolingSetpoint", tempSet)
	subscribe(thermostat, "heatingSetpoint", tempSet) 
    subscribe(thermostat, "thermostatSetpoint", tempSet)
    subscribe(thermostat, "thermostatMode", mode)
    subscribe(thermostat, "temperature", tempChange)
    subscribe(thermostat, "thermostatOperatingState", opState)    
    
    subscribe(upButton, "switch.on", up)
    subscribe(downButton, "switch.on", down)   
    subscribe(display, "switch.off", off)
    subscribe(heatButton, "switch.on", goHeat)
    subscribe(coolButton, "switch.on", goCool)
}

def tempChange(evt) {
	log.debug "temp change $evt.value"
//    thermostat.currentValue('heatingSetpoint')
//	  thermostat.currentValue('coolingSetpoint')
}

def setStatus() {
	log.debug "setStatus: mode = ${thermostat.currentValue("thermostatMode")} || state = ${thermostat.currentValue('thermostatOperatingState')}"
    if (thermostat.currentValue("thermostatMode") == "cool") {
    	int coolTemp = thermostat.currentValue('coolingSetpoint')
        log.debug "state is ${(thermostat.currentValue('thermostatOperatingState') == "Idle")}"
		if (thermostat.currentValue('thermostatOperatingState') == "Idle") display.setStatus("${coolTemp}w") 
    	else display.setStatus("${coolTemp}")     
    } else 
    if (thermostat.currentValue("thermostatMode") == "heat") display.setStatus("${thermostat.currentValue('heatingSetpoint')}")
}    

def opState(evt) {
	setStatus()
}

def tempSet(evt) {
	log.debug "Temp Set: $evt.name to $evt.value"

    syncMode(thermostat.currentValue("thermostatMode"))
}

def goHeat(evt) {
	thermostat.heat()
}

def goCool(evt) {
	thermostat.cool()
}

def mode(evt) {	
	syncMode(evt.value)
}

def syncMode(value) {
	log.debug "sync Mode"
    setStatus()
	if (value == "heat") {
    	upButton.heatOff()
        downButton.heatOff()
		heatButton.on()
    	coolButton.off()        
    }    
	if (value == "cool") {
    	downButton.coolOff()
        upButton.coolOff()
        coolButton.on()  
		heatButton.off()         
    }    
	if (value == "off") {
    	upButton.idleOff()
        downButton.idleOff()
        heatButton.off()
    	coolButton.off()   
    }     
}

def up(evt) {	
	if (thermostat.currentValue("thermostatMode") == "off") thermostat.heat()//syncMode("heat")
	else thermostat.temperatureUp()//raiseSetpoint()
    display.on()
}
def down(evt) {	
	if (thermostat.currentValue("thermostatMode") == "off") thermostat.cool()//syncMode("cool")
	else thermostat.temperatureDown()//lowerSetpoint()
	display.on()
} 
def off(evt) {
	log.debug "why $evt.value"
    if (evt.value == "off") thermostat.off()
}


def help(evt) {	log.debug "name: $evt.name | value: $evt.value | type: $evt.device" }