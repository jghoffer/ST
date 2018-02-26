definition(
    name: "Thermostat Manager",
    namespace: "Josh",
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
  
	section("Interval (minutes)") {
		input "pollingInterval", "decimal", title: "Interval", required: true
	}

  section("Tiles") {
    input name: "heatButton", type: "capability.switch", title: "Heat Button"
    input name: "coolButton", type: "capability.switch", title: "Cool Button"    
  }
}

def appTouch(evt) {
	doPoll()
}

def installed() {  updated()	}
def updated() {
	log.debug "updated...?"
    unsubscribe()
    subscribe(app, appTouch)
    subscribe(thermostat, "thermostatMode", mode)      
    subscribe(heatButton, "switch.on", goHeat)
    subscribe(coolButton, "switch.on", goCool)
    subscribe(heatButton, "switch.off", off)
    subscribe(coolButton, "switch.off", off)    
}

def goHeat(evt) {
	thermostat.heat()
    coolButton.off() 
}

def goCool(evt) {
	thermostat.cool()
    heatButton.off()
}

def mode(evt) {	
	log.debug "sync Mode"
    //heatButton.off()
    //coolButton.off()  
	if (evt.value == "heat") heatButton.on()
	if (evt.value == "cool") coolButton.on() 
    if (evt.value == "off") {
    	heatButton.off()
    	coolButton.off()      
    }
}

def off(evt) {
	log.debug "why $evt.value"
    if ((heatButton.currentValue('switch') == "off") && (coolButton.currentValue('switch') == "off")) {
    	thermostat.off()
       	thermostat.poll()
    }    
}

def doPoll() {
	thermostat.poll()
	runIn( (pollingInterval * 60), doPoll)
}
