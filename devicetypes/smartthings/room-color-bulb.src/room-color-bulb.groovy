
	preferences {
//		input description: "This feature allows you to correct any temperature variations by selecting an offset. Ex: If your sensor consistently reports a temp that's 5 degrees too warm, you'd enter \"-5\". If 3 degrees too cold, enter \"+3\".", displayDuringSetup: false, type: "paragraph", element: "paragraph"
		input "noDimmer", "bool", title: "No dimmer?", description: "just on off"
	}

metadata {
	definition (name: "Room Color Bulb", namespace: "smartthings", author: "Josh") {
	//definition (name: "Room Control (Color)", namespace: "Room", author: "Josh") {
		capability "Actuator"
        capability "Indicator"
		capability "Color Control"
		capability "Color Temperature"
		capability "Switch"
		capability "Switch Level" // brightness
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
        attribute "DeviceWatch-DeviceStatus","string"
        attribute "colored","string"
    	command "goClear"
    	command "setStatus", ["string"] 
        command "boom"
        
    	attribute "status", "STRING"
        
        
		fingerprint inClusters: "0x26"
    }

	simulator {
		status "on":  "command: 2003, payload: FF"
		status "off": "command: 2003, payload: 00"
		status "09%": "command: 2003, payload: 09"
		status "10%": "command: 2003, payload: 0A"
		status "33%": "command: 2003, payload: 21"
		status "66%": "command: 2003, payload: 42"
		status "99%": "command: 2003, payload: 63"

		// reply messages
		reply "2001FF,delay 5000,2602": "command: 2603, payload: FF"
		reply "200100,delay 5000,2602": "command: 2603, payload: 00"
		reply "200119,delay 5000,2602": "command: 2603, payload: 19"
		reply "200132,delay 5000,2602": "command: 2603, payload: 32"
		reply "20014B,delay 5000,2602": "command: 2603, payload: 4B"
		reply "200163,delay 5000,2602": "command: 2603, payload: 63"
	}


	tiles(scale: 2) {

        multiAttributeTile(name: "button", type: "lighting", width: 6, height: 4, canChangeIcon: true) {

			tileAttribute ("device.status", key: "PRIMARY_CONTROL", wordWrap: true) {
		
 				attributeState "cinema", label: ' ', icon:"st.Electronics.electronics18", action:"goClear", backgroundColor: "#FF3300", nextState:"clear"
 				attributeState "piano", label: ' ', icon:"st.Electronics.electronics11", action:"goClear", backgroundColor: "#FF3300", nextState:"clear"
 				attributeState "nap", label: ' ', icon:"st.Bedroom.bedroom6", action:"goClear", backgroundColor: "#FF3300", nextState:"clear"
                attributeState "clear", label: 'On', icon:" ", action:" ", backgroundColor: "#FF3300"

				attributeState "default", label: 'On', icon:"st.switches.switch.off", 
                	inactiveLabel: false, decoration: "flat",  
                	action:"off", backgroundColor:"#79b821"

            	attributeState "off", label: 'Off', action: "on", /*nextState: "level",*/ icon: " ", backgroundColor: "#ffffff" 			
			}
			if (!noDimmer) {
				tileAttribute ("device.level", key: "SLIDER_CONTROL") {
					attributeState "level", action:"setLevel", range:"(0..100)"
				}
                tileAttribute ("device.color", key: "COLOR_CONTROL") {
					attributeState "color", action:"color control.setColor"
				}
                tileAttribute("colored", key: "SECONDARY_CONTROL") {
    				attributeState("default", label:'${currentValue}', icon:"st.alarm.water.wet")
                }
            }
            
		}
        
		standardTile("refresh", "device.switch", height: 1, width: 1, inactiveLabel: false, decoration: "flat") {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        		standardTile("boom", "boom", height: 1, width: 1, inactiveLabel: false, decoration: "flat") {
			state "default", label:"BOOM!", action:"boom", icon:""
		}
        
		controlTile("colorTempSliderControl", "device.colorTemperature", "slider", height: 2, width: 4, inactiveLabel: false, range:"(2700..9000)") {
			state "colorTemp", action:"color temperature.setColorTemperature"
		}

		main(["button"])
		details(["button",  "refresh", "colorTemp","colorTempSliderControl","boom"])
	}
}

def boom(){

}

def setStatus(value) {
	log.debug "executing setStatus $value"
    if (value == "clear") goClear()
    else sendEvent(name: "status", value: "$value",isStateChange: true)
}

def goClear() {
	log.debug "going clear"
    def level = device.currentValue("level")
    def stat = device.currentValue("status")
	if (["piano","cinema","nap"].contains(stat)) {
    	log.debug "stat is $stat"
 	  	sendEvent(name: "status", value: "clear",isStateChange: true) 
    	sendEvent(name: "status", value: level,isStateChange: true)  
    }    
}

def parse(String description) {
	sendEvent(name: "colorTemperature", value: 0)
}

def on(type=null) {
	log.debug "on() type = $type"
    sendEvent(name: "switch", value: "on", type:"$type")
    
    def stat = device.currentValue("level")
    if (noDimmer) stat = "on"
    sendEvent(name: "status", value: "$stat")
}

def off(type=null) {
	log.debug "off() type = $type"
    sendEvent(name: "switch", value: "off", type:"$type")
	sendEvent(name: "status", value: "off")
}

def setLevel(value){
	log.trace "setLevel( $value )"
    
    goClear()
    def valueaux = value as Integer
    if (valueaux == 0) {off()} else {on()}
    def level = Math.min(valueaux, 99)
    sendEvent(name: "level", value: "$level", isStateChange: true)
    sendEvent(name: "status", value: "$level", isStateChange: true)
}

def setColor(value) {
	log.debug "setColor ${value}"

	if(value.hue) {
    	sendEvent(name: "hue", value: value.hue)
        if(!value.hex) sendEvent(name: "color", value: colorUtil.hslToHex((value.hue) as int, (value.saturation) as int))
    }
	if(value.hex) sendEvent(name: "color", value: value.hex)
	if(value.saturation) sendEvent(name: "saturation", value: value.saturation)
    
    def colored = (device.currentValue("saturation") < 20) 
    sendEvent(name: "colored", value: colored ? "White":"Color")
    sendEvent(name: "status", value: colored ? "White":"Color")
}


def setColorTemperature(value) {
	log.debug "Executing setColorTemperature ${value}"
	sendEvent(name: "colorTemperature", value: value)
}

def refresh() {
}