/**
 *  Virtual Factory
 *
 *  Copyright 2016 Josh H
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
definition(
    name: "Virtual Factory",
    namespace: "Josh",
    author: "Josh H",
    description: "no",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	page(name:"firstPage", title:"Set-Up", content:"firstPage", refreshTimeout:5)

}

def firstPage() {
	dynamicPage(name: "firstPage",uninstall: true, install:true,nextPage: null) {
		section("Room:"){
        	input "childName", "text", title: "Name:", description:"", required: true
    	}
	}
}    

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	//unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    addDevice()
}


def addDevice()
{ 
  getChildDevices().each { device ->
  	log.debug "device found: $device.id || $device.deviceNetworkId || $device.label"
 //   deleteChildDevice(device.deviceNetworkId) 
  }
  
  def dni = "${new Random().nextInt(999) + 1000}"
  def d = getChildDevice(dni)
  if (!d) 
  {
  	log.debug "Hub: " + location.hubs[0].id
    addChildDevice("Room", "State Switch", dni, null, 
    	[
        	name: "Virtual$dni", 
            label: childName, 
            completedSetup: true,
            preferences: [
            ]
        ])
    d = getChildDevice(dni)
    log.debug "created ${d.displayName} with id $dni"
  } 
  else 
  {
    log.debug "Device with id $dni already created"
  }
}

def uninstalled() {
	log.debug "uninstalled"
    removeChildDevices(getChildDevices())
}

private removeChildDevices(devices) {
	devices.each {
		deleteChildDevice(it.deviceNetworkId) // 'it' is default
	}
}

// TODO: implement event handlers