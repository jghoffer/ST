/**
 *  Z-Wave Garage Door Opener
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
 */
metadata {
	definition (name: "Simulated Garage Door Opener", namespace: "smartthings/testing", author: "SmartThings") {
		capability "Actuator"
        capability "Door Control"
		capability "Contact Sensor"
		capability "Refresh"
		capability "Sensor"
		capability "Health Check"
	}
	preferences {
        input("edit", "bool", title:"Editable", description: "", required: true, displayDuringSetup: false)
    }  
	simulator {
		
	}

	tiles {
		standardTile("toggle", "device.door", width: 2, height: 2) {
			state("closed", label:'${name}', action:"door control.open", icon:"st.doors.garage.garage-closed", backgroundColor:"#00A0DC", nextState:"opening")
			state("open", label:'${name}', action:"door control.close", icon:"st.doors.garage.garage-open", backgroundColor:"#e86d13", nextState:"closing")
			state("opening", label:'${name}', icon:"st.doors.garage.garage-closed", backgroundColor:"#e86d13")
			state("closing", label:'${name}', icon:"st.doors.garage.garage-open", backgroundColor:"#00A0DC")
			
		}
		standardTile("open", "device.door", inactiveLabel: false, decoration: "flat") {
			state "default", label:'open', action:"door control.open", icon:"st.doors.garage.garage-opening"
		}
		standardTile("close", "device.door", inactiveLabel: false, decoration: "flat") {
			state "default", label:'close', action:"door control.close", icon:"st.doors.garage.garage-closing"
		}

		main "toggle"
		details(["toggle", "open", "close"])
	}
}

def parse(String description) {
	log.trace "parse($description)"
}



def open() {
     def d = (edit==true) ? "open" : "closed"
     sendEvent(name: "contact", value: "open")
     sendEvent(name: "door", value: d)
     log.debug d
}

def close() {
    def d = (edit==true) ? "closed" : "open"
    sendEvent(name: "door", value: d)
    sendEvent(name: "contact", value: "closed")
    log.debug d
}

def finishOpening() {
    sendEvent(name: "door", value: "open")
    sendEvent(name: "contact", value: "open")
}

def finishClosing() {
    sendEvent(name: "door", value: "closed")
    sendEvent(name: "contact", value: "closed")
}

def installed() {
	log.trace "Executing 'installed'"
	initialize()
}

def updated() {
	log.trace "Executing 'updated'"
//    def d = ((edit==true) || device.currentValue('contact')="open") ? "open" : "closed"   
	initialize()
}

private initialize() {
	log.trace "Executing 'initialize'"
    if (device.currentValue('contact')=="open") open() else close()

	sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
	sendEvent(name: "healthStatus", value: "online")
	sendEvent(name: "DeviceWatch-Enroll", value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false)
}