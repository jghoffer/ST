    preferences {
    
		//input "doubleAction", "bool", title: "Double Action?", description: "", displayDuringSetup: false
        input "actionType", "enum", title:"Manual Action", options: ["Single","Double","None"]
        input "color", "enum", title:"Color", options: ["Red","Green","Yellow"]
	}

metadata {
	
    definition (name: "State Switch", namespace: "Room", author: "Josh") {
		capability "Switch"
        capability "Relay Switch"
        
        attribute "switch", "STRING"
		attribute "action", "STRING"
        attribute "colorDisp", "STRING"
        attribute "status", "STRING"
	}
    


	tiles(scale: 2) {
		standardTile("switch", "switch", width: 2, height: 2, decoration: "flat") {
			state "on", label: 'Switch On', action: "switch.off"
			state "off", label: 'Switch Off', action: "switch.on"
        }
        
        standardTile("status", "device.status", width: 2, height: 2, decoration: "flat", canChangeIcon: true, canChangeBackground: true) {
       		state "GreenDouble", label: 'Set', action: "switch.off", icon: " ", backgroundColor: "#00b404"
			state "GreenSingle", label: 'Set', action: "", icon: " ", backgroundColor: "#00b404"
       		state "RedDouble", label: 'Set', action: "switch.off", icon: " ", backgroundColor: "#f00"
			state "RedSingle", label: 'Set', action: "", icon: " ", backgroundColor: "#f00"
       		state "YellowDouble", label: 'Set', action: "switch.off", icon: " ", backgroundColor: "#ffd700"
			state "YellowSingle", label: 'Set', action: "", icon: " ", backgroundColor: "#ffd700"


			state "Off", label: '', action: "switch.on", icon:" ", backgroundColor: "#fffffe", nextState: "Off"
            state "None", label: '', action: "", icon: " ", backgroundColor: "#fffffe"
        }
        
        standardTile("color","device.colorDisp",width: 1, height: 1) {
        	state "Green", backgroundColor: "#00b404"
            state "Red", backgroundColor: "#f00"
            state "Yellow", backgroundColor: "#ffd700"
        }
        
        standardTile("action", "action", width: 3, height: 1) {
       		state "Double", label: "Double Action!"
			state "Single", label: "Single Action"
            state "None", label: "No Action"
        }
        standardTile("dummy","dummy",width:6, height:1) { 
        	state "dummy",label:"dummy",backgroundColor: "#f00"}
        
        main "status"
		details(["switch", "action", "color", "status"])
	}
}

def parse(String description) {
	def pair = description.split(":")
    if (pair.length>1)
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

def on() {
	log.debug "on()"
	sendEvent(name: "switch", value: "on", isStateChange: true)
   // sendEvent(name: "status", value: "${device.currentValue("action")}")
   sendEvent(name: "status", value: "${color}${state.onAction}", isStateChange: true, displayed: false)
}

def off() {
	log.debug "off()"
	sendEvent(name: "switch", value: "off", isStateChange: true)
  //  sendEvent(name: "status", value: "off")
    sendEvent(name: "status", value: "${state.offAction}", isStateChange: true, displayed: false)
}

def updated()
{
	log.debug "updated"
	//def action = doubleAction ? "double" : "single" 
    state.onAction = (actionType == "Double") ? "Double" : "Single" 
    state.offAction = (actionType == "None") ? "None" : "Off" 
    
    sendEvent(name: "colorDisp", value: "$color", isStateChange: true, displayed:false)
    sendEvent(name: "action", value: "$actionType", isStateChange: true, displayed:false)
    if (device.currentValue("switch") == "on")
    	sendEvent(name: "status", value: "${color}${state.onAction}", isStateChange: true, displayed:false)
    else
		sendEvent(name: "status", value: "${state.offAction}", isStateChange: true, displayed:false)
}