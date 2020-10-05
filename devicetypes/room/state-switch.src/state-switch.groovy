    preferences {
    
		//input "doubleAction", "bool", title: "Double Action?", description: "", displayDuringSetup: false
        input "actionType", "enum", title:"Manual Action", options: ["Single","Double","None"]
        input "color", "enum", title:"Color", options: ["Red","Green","Yellow"]
        input "textOn", "bool", title:"Text when On"
        input "textOff", "bool", title:"Text when Off"
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
            state "GreenDoubleYes", label: "Set", action: "switch.off", icon: " ", backgroundColor: "#00b404"
			state "GreenSingleYes", label: "Set", action: "", icon: " ", backgroundColor: "#00b404"
       		state "RedDoubleYes", label: "Set", action: "switch.off", icon: " ", backgroundColor: "#f00"
			state "RedSingleYes", label: "Set", action: "", icon: " ", backgroundColor: "#f00"
       		state "YellowDoubleYes", label: "Set", action: "switch.off", icon: " ", backgroundColor: "#ffd700"
			state "YellowSingleYes", label: "Set", action: "", icon: " ", backgroundColor: "#ffd700"
       		
            state "GreenDoubleNo", label: "", action: "switch.off", icon: " ", backgroundColor: "#00b404"
			state "GreenSingleNo", label: "", action: "", icon: " ", backgroundColor: "#00b404"
       		state "RedDoubleNo", label: "", action: "switch.off", icon: " ", backgroundColor: "#f00"
			state "RedSingleNo", label: "", action: "", icon: " ", backgroundColor: "#f00"
       		state "YellowDoubleNo", label: "", action: "switch.off", icon: " ", backgroundColor: "#ffd700"
			state "YellowSingleNo", label: "", action: "", icon: " ", backgroundColor: "#ffd700"
	
            state "OffYes", label: "Off", action: "switch.on", icon:" ", backgroundColor: "#ffffff", nextState: "Transition"
            state "OffNo", label: "", action: "switch.on", icon:" ", backgroundColor: "#fffffe", nextState: "Transition"
            state "None", label: "", action: "", icon: " ", backgroundColor: "#fffffe"
            state "Transition", label: "Set", action:"", icon:"", backgroundColor: "#000000"
      
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
    sendEvent(name: "status", value: "${color}${state.onAction}${state.onText}", isStateChange: true, displayed: false)
}

def off() {
	log.debug "off()"
	sendEvent(name: "switch", value: "off", isStateChange: true)
    sendEvent(name: "status", value: "${state.offAction}${state.offText}", isStateChange: true, displayed: false)
}

def updated()
{
	log.debug "updated"
	state.onAction = (actionType == "Double") ? "Double" : "Single" 
    state.offAction = (actionType == "None") ? "None" : "Off" 
    state.onText = textOn ? "Yes" : "No"
    state.offText = textOff ? "Yes" : "No"
    
    
    sendEvent(name: "colorDisp", value: "$color", isStateChange: true, displayed:false)
    sendEvent(name: "action", value: "$actionType", isStateChange: true, displayed:false)
    if (device.currentValue("switch") == "on")
    	sendEvent(name: "status", value: "${color}${state.onAction}${state.onText}", isStateChange: true, displayed:false)
    else
		sendEvent(name: "status", value: "${state.offAction}${state.offText}", isStateChange: true, displayed:false)
}