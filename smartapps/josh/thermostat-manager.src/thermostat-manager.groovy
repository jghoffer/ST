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
    subscribe(heatButton, "switch.on", goButton)
    subscribe(coolButton, "switch.on", goButton)
    subscribe(heatButton, "switch.off", off)
    subscribe(coolButton, "switch.off", off)    
}

def goButton(evt) {
	def v
    switch(evt.device.label) {
    	case coolButton.label: v=0; break
        case heatButton.label: v=1; break
        default: return
	}
	settings["${(["cool","heat"])[v]}Button"].on(); settings["${(["heat","cool"])[v]}Button"].off(); 
    thermostat."${(["cool","heat"])[v]}"()
}

def mode(evt) {	
	log.debug "sync Mode"
    if (evt.value == "off") [heatButton,coolButton]*.off()
        		else settings["${evt.value}Button"].on()
}

def off(evt) {
    if ((heatButton.currentValue('switch') == "off") && (coolButton.currentValue('switch') == "off")) {
    	thermostat.off()
       	thermostat.poll()
    }    
}

def doPoll() {
	thermostat.poll()
	runIn( (pollingInterval * 60), "doPoll")
}
