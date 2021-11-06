/**
 *  WeMo Direct LED Bulbs
 *
 *  Copyright 2014 SmartThings
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
 *  Thanks to Chad Monroe @cmonroe and Patrick Stuart @pstuart
 *
 */
metadata {
	definition (name: "Custom WeMo Bulb", namespace: "smartthings", author: "SmartThings") {
	
    	capability "Actuator"
        capability "Configuration"
        capability "Refresh"
		
        capability "Switch"
		capability "Switch Level"
        capability "Light"
        attribute "onSpeed", "number"
        attribute "offSpeed", "number"
        attribute "oldLevel", "number"
        command "setOnSpeed", ["number"]
        command "setOffSpeed", ["number"]

		fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0006,0008,FF00", outClusters: "0019"
	}
    
  preferences {
        input("onSpeedDef", "number", title:"On Speed", required: true)
        input("offSpeedDef", "number", title:"Off Speed", required: true) 
 }

	// simulator metadata
	simulator {
		// status messages
		status "on": "on/off: 1"
		status "off": "on/off: 0"

		// reply messages
		reply "zcl on-off on": "on/off: 1"
		reply "zcl on-off off": "on/off: 0"
	}

	// UI tile definitions
	tiles(scale: 2) {
        standardTile("switch",  "device.switch", width: 6, height: 6, canChangeIcon: false, decoration: "flat") {
			state "on", label:'${name}', action:"switch.off", icon:" ", backgroundColor:"#79b821", nextState:"turningOff"
			state "off", label:'${name}', action:"switch.on", icon:" ", backgroundColor:"#ffffff", nextState:"turningOn"
			state "turningOn", label:'${name}', action:"switch.off", icon:" ", backgroundColor:"#79b821", nextState:"turningOff"
			state "turningOff", label:'${name}', action:"switch.on", icon:" ", backgroundColor:"#ffffff", nextState:"turningOn"
		}
        
		standardTile("refresh", "device.switch", height: 1, width: 1, inactiveLabel: false, decoration: "flat") {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 4, inactiveLabel: false) {
			state "level", action:"switch level.setLevel"
		}
		valueTile("level", "device.level", height: 1, width: 1,  inactiveLabel: false, decoration: "flat") {
			state "level", label: '${currentValue}%',
            				backgroundColors:[
					[value: 31, color: "#153591"],
					[value: 44, color: "#1e9cbb"],
					[value: 59, color: "#90d2a7"],
					[value: 74, color: "#44b621"],
					[value: 84, color: "#f1d801"],
					[value: 95, color: "#d04e00"],
					[value: 96, color: "#bc2323"]
				]
		}	

		main(["switch"])
		details(["switch","level", "levelSliderControl", "refresh"])
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
//	log.trace description
	if (description?.startsWith("catchall:")) {
		//def msg = zigbee.pa is rse(description)
		//log.trace msg
		//log.trace "data: $msg.data"
        if(description?.endsWith("0100"))
        {
        	def result = createEvent(name: "switch", value: "on")
            log.debug "Parse returned ${result?.descriptionText}"
            return result
        }
        if(description?.endsWith("0000"))
        {
        	def result = createEvent(name: "switch", value: "off")
            log.debug "Parse returned ${result?.descriptionText}"
            return result
        }
	}
    if (description?.startsWith("read attr")) {
    	log.debug description[-2..-1]
        def i = Math.round(convertHexToInt(description[-2..-1]) / 256 * 100 )
        
		sendEvent( name: "level", value: i )
    }	
}

def setOnSpeed(value) { 	sendEvent(name: "onSpeed", value: value) 
log.trace "set $value"
}
def setOffSpeed(value) { 	sendEvent(name: "offSpeed", value: value) }


def on() {
	log.debug "on()"
	sendEvent(name: "switch", value: "on")
	//"st cmd 0x${device.deviceNetworkId} 1 6 1 {}"

	def temp = ((device.currentValue("oldLevel") as long) * (255 * 0.01))
    def level = hexString(Math.round(temp))
    
    def speed = swapEndianHex(device.currentValue("onSpeed").toString().padLeft(4, '0'))
    log.debug "on speed ${device.currentValue("onSpeed").toString()/*.padLeft(4, '0')*/} -> $speed"
    "st cmd 0x${device.deviceNetworkId} 1 8 4 {${level} $speed}" 
}

def off() {
	log.debug "off()"
    sendEvent(name: "oldLevel", value: device.currentValue("level"))
	sendEvent(name: "switch", value: "off")
//  "st cmd 0x${device.deviceNetworkId} 1 6 0 {6000}"

    def speed = swapEndianHex(device.currentValue("offSpeed").toString().padLeft(4, '0'))
	log.debug "speed $speed"
    "st cmd 0x${device.deviceNetworkId} 1 8 4 {00 ${speed}}"
}

def refresh() {

log.trace "${device.currentValue("onSpeed")} onSpeedDef = $onSpeedDef"
setOnSpeed(device.currentValue("onSpeed") ?: onSpeedDef)
setOffSpeed(device.currentValue("offSpeed") ?: offSpeedDef)

//if (device.currentValue("offSpeed")==null) setOffSpeed(1)

	[
	"st rattr 0x${device.deviceNetworkId} 1 6 0", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 1 8 0"
    ]
}

def setLevel(value) {
	value = value as Integer
    log.trace "setLevel($value)"
	def cmds = []

	if (value == 0) {
		sendEvent(name: "switch", value: "off")
		cmds << "st cmd 0x${device.deviceNetworkId} 1 8 0 {0000 0000}"
	}
	else if (device.latestValue("switch") == "off") {
		sendEvent(name: "switch", value: "on")
	}

	sendEvent(name: "level", value: value)
    
    def temp = ((value as long) * (255 * 0.01))
    def level = hexString(Math.round(temp))
    
    def speed = swapEndianHex(device.currentValue("onSpeed").toString().padLeft(4, '0'))
	cmds << "st cmd 0x${device.deviceNetworkId} 1 8 4 {${level} $speed}"
    

	//log.debug cmds
	cmds
}

def configure() {

	String zigbeeId = swapEndianHex(device.hub.zigbeeId)
	log.debug "Confuguring Reporting and Bindings."
	def configCmds = [	
  
        //Switch Reporting
        "zcl global send-me-a-report 6 0 0x10 0 3600 {01}", "delay 500",
        "send 0x${device.deviceNetworkId} 1 1", "delay 1000",
        
        //Level Control Reporting
        "zcl global send-me-a-report 8 0 0x20 5 3600 {0010}", "delay 200",
        "send 0x${device.deviceNetworkId} 1 1", "delay 1500",
        
        "zdo bind 0x${device.deviceNetworkId} 1 1 6 {${device.zigbeeId}} {}", "delay 1000",
		"zdo bind 0x${device.deviceNetworkId} 1 1 8 {${device.zigbeeId}} {}", "delay 500",
	]
    return configCmds + refresh() // send refresh cmds as part of config
}


private hex(value, width=2) {
	def s = new BigInteger(Math.round(value).toString()).toString(16)
	while (s.size() < width) {
		s = "0" + s
	}
	s
}

private Integer convertHexToInt(hex) {
	Integer.parseInt(hex,16)
}

private String swapEndianHex(String hex) {
    reverseArray(hex.decodeHex()).encodeHex()
}

private byte[] reverseArray(byte[] array) {
    int i = 0;
    int j = array.length - 1;
    byte tmp;
    while (j > i) {
        tmp = array[j];
        array[j] = array[i];
        array[i] = tmp;
        j--;
        i++;
    }
    return array
}

def updated() {

setOnSpeed(onSpeedDef)
setOffSpeed(offSpeedDef)
log.debug "so... $onSpeedDef"
}