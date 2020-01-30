/**
 *  LG Smart TV Device Type
 *
 *  Copyright 2015 ME ME ME
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
	definition (name: "Josh's LG Smart TV", namespace: "tv", author: "Josh") 
    {
		capability "TV"
        capability "Switch Level"
        capability "Switch"
        capability "Music Player"
        capability "Refresh"
        
        attribute "status", "STRING"        
        attribute "sessionId", "string"
        attribute "muted", "string"
        attribute "tvMode", "string"        
        attribute "dispMain", "string"
        attribute "dispDetails", "string"
        attribute "working", "string"
        attribute "changed", "string"
        attribute "volShow", "string"
        attribute "chShow", "string"
        attribute "showMode", "number"
        attribute "shift", "string"
        attribute "playing", "string"
        attribute "debug", "string"
        
        command "setStatus", ["string"] 
        command "valueInc", ["number"]
        command "toggleMode", ["number"]
        command "update" 
        command "refresh"
        command "chMode"
        command "volMode"
        command "main"
        command "lock"
        command "unlock"
        command "mute"
        command "getVolume"
        command "getChannel"
        command "getMode"
        command "getDisplay"
        
        command "goHome"
        command "goBack"
        command "goExit"
        command "goOK"
        command "goSettings"
        command "play"
        command "pause"
        command "playPause"
        command "goUp"
        command "goDown"
        command "goRight"
        command "goLeft"
        command "channelDown"
        command "channelUp"
        command "goInput"
        command "reqAuthCommand"
        command "goInfo"
        command "goShortcut"
        command "goTV"
        command "goNetflix"
        command "goHulu"        
        command "goCast"
        command "goChList"
        command "goAdvSettings"
        command "goRecent"
        command "goMenu"
        command "goChList"
        command "toggleShift"
        command "goReset"
        
        command "go1"
        command "go2"
        command "go3"
        command "go4"
        command "go5"
        command "go6"
        command "go7"
        command "go8"
        command "go9"
        command "go0"
        command "goDash"
                
        command "debug"
        command "rwd"
        command "fwd"
        command "resetPointer"
        command "setLevelWorker"
	}
    
    mappings {
  path("/event") {
    action: [
      GET: "listMethod",
      PUT: "updateMethod",
      POST: "updateMethod"
    ]
  }
  path("/udap/api/event/") {
    action: [
      GET: "listMethod",
      PUT: "updateMethod",
      POST: "updateMethod"
    ]
  }  
  path("/udap/api/:event") {
    action: [
      GET: "listMethod",
      PUT: "updateMethod",
      POST: "updateMethod"
    ]
  }   
}
    
    preferences {
        input("televisionIp", "string", title:"Television IP Address", description: "Television's IP address", required: true, displayDuringSetup: false)
        input("pairingKey", "string", title:"Pairing Key", description: "Pairing key", required: true, displayDuringSetup: false)
 		input("scVolume", "number", title:"Shortcut Volume", description: "", required: false, default: "7", displayDuringSetup: false) 
 		input("scMajor", "number", title:"Shortcut Major", description: "", required: false, default: "7", displayDuringSetup: false)
        input("scMinor", "number", title:"Shortcut Minor", description: "", required: false, default: "1", displayDuringSetup: false)
        input("scPnys", "number", title:"Shortcut Pysical", description: "", required: false, default: "7", displayDuringSetup: false)

	}

	simulator 
    {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
        multiAttributeTile(name: "preview", type: "generic", width: 6, height: 4) {//generic
        
      		
            //tileAttribute("device.volShow", key: "SECONDARY_CONTROL", wordWrap: true) { 
           	//	attributeState "default", icon:"st.custom.sonos.unmuted", label:'${currentValue} \n'
            //    attributeState "hidden", icon:" ", label:''
            //    attributeState "true", icon:"st.custom.sonos.muted", label:''
           // }
           	tileAttribute("device.chShow", key: "MARQUEE") { 
            	attributeState "default", label:'${currentValue}'//, action: "volMode"
                attributeState "hidden", icon:" ", label:''
            }                        
            
           	//tileAttribute("device.dispDetails", key: "SECONDARY_CONTROL") { 
           // 	attributeState "default", icon:" ",label:'\n${currentValue} '
           //     attributeState "hidden", icon:" ", label:''
		//	}
           	//tileAttribute("dummy", key: "SECONDARY_CONTROL", wordWrap: true) { 
            //	attributeState "default", icon:" ", label:'                                                          '//, action: "on"
            //}            
          

            tileAttribute ("device.changed", key: "PRIMARY_CONTROL", wordWrap: true) {
			//	attributeState "off", label:'${name}', backgroundColor:"#ffffff",
           	//		nextState:"off"
           		attributeState("chsmart", icon: "st.Electronics.electronics18",label: '', action: "playPause", nextState: "pause", backgroundColor: "#41F073")
           		attributeState("chcast", icon:"st.Electronics.electronics8", label: 'Cast', action: "playPause", nextState: "pause", backgroundColor: "#41F073")
           		attributeState("pause", icon:"st.custom.buttons.play-pause", label: '', action: "", nextState: "updating", backgroundColor: "#41F073")
//           		attributeState("chcast", icon:"st.Electronics.electronics8", label: 'Cast', action: "pause", nextState: "updating", backgroundColor: "#41F073")


            	attributeState("off", icon:"st.Electronics.electronics15", label: 'Off', action: "on", nextState: "on", backgroundColor: "#ffffff")
            	attributeState("updating", icon:"Refresh.refresh", label: ' ',  backgroundColor: "#41F073")

				attributeState("ch2", icon:"st.Electronics.electronics15", label: '2', action: "update", nextState: "updating", backgroundColor: "#41F073")
                attributeState("ch4", icon:"st.Electronics.electronics15", label: '4', action: "update", nextState: "updating",backgroundColor: "#41F073")
                attributeState("ch5", icon:"st.Electronics.electronics15", label: '5', action: "update", nextState: "updating",backgroundColor: "#41F073")
                attributeState("ch7", icon:"st.Electronics.electronics15", label: '7', action: "update", nextState: "updating",backgroundColor: "#41F073")
                attributeState("ch9", icon:"st.Electronics.electronics15", label: '9', action: "update", nextState: "updating",backgroundColor: "#41F073")
                attributeState("ch11", icon:"st.Electronics.electronics15", label: '11', action: "update", nextState: "updating",backgroundColor: "#41F073")
                attributeState("ch16", icon:"st.Electronics.electronics15", label: '16', action: "update", nextState: "updating",backgroundColor: "#41F073")
                attributeState("ch20", icon:"st.Electronics.electronics15", label: '20',action: "update", nextState: "updating", backgroundColor: "#41F073")
                attributeState("ch22", icon:"st.Electronics.electronics15", label: '22',action: "update", nextState: "updating", backgroundColor: "#41F073")
                attributeState("ch24", icon:"st.Electronics.electronics15", label: '24',action: "update", nextState: "updating", backgroundColor: "#41F073")
                attributeState("ch25", icon:"st.Electronics.electronics15", label: '25',action: "update", nextState: "updating", backgroundColor: "#41F073")
                attributeState("ch26", icon:"st.Electronics.electronics15", label: '26',action: "update", nextState: "updating", backgroundColor: "#41F073")
                attributeState("ch30", icon:"st.Electronics.electronics15", label: '30',action: "update", nextState: "updating", backgroundColor: "#41F073")
                attributeState("ch32", icon:"st.Electronics.electronics15", label: '32', action: "update", nextState: "updating",backgroundColor: "#41F073")
                attributeState("ch33", icon:"st.Electronics.electronics15", label: '33',action: "update", nextState: "updating", backgroundColor: "#41F073")
                attributeState("ch36", icon:"st.Electronics.electronics15", label: '36', action: "update", nextState: "updating",backgroundColor: "#41F073")
                attributeState("ch40", icon:"st.Electronics.electronics15", label: '40', action: "update", nextState: "updating",backgroundColor: "#41F073")
                attributeState("ch48", icon:"st.Electronics.electronics15", label: '48', action: "update", nextState: "updating",backgroundColor: "#41F073")
				attributeState("ch50", icon:"st.Electronics.electronics15", label: '50',action: "update", nextState: "updating", backgroundColor: "#41F073")               
				attributeState("chChange", icon:"st.Electronics.electronics15", label: '', nextState: "volChange", backgroundColor: "#41F073")
				attributeState("chDown", icon:"st.Electronics.electronics15", label: '-', nextState: "volChange", backgroundColor: "#41F073")
				attributeState("chUp", icon:"st.Electronics.electronics15", label:"+", nextState: "volChange", backgroundColor: "#41F073")
				attributeState "volChange", icon:'st.custom.sonos.unmuted', label:'', backgroundColor:"#90EBE9", nextState:"chChange"		
				attributeState "volUp", icon:"st.custom.sonos.unmuted", label:'+', backgroundColor:"#90EBE9", nextState:"chChange"		
				attributeState "volDown", icon:"st.custom.sonos.unmuted", label:'-', backgroundColor:"#90EBE9", nextState:"chChange"		
                attributeState("volNum", icon:"st.custom.sonos.unmuted", label: '${currentValue}', action: "mute", nextState: "volChange", backgroundColor: "#90EBE9", defaultState: true)
                attributeState("true", icon:"st.custom.sonos.muted", label: '', action: "mute", nextState: "volChange", backgroundColor: "#90EBE9", defaultState: true)
                
			}
                
            tileAttribute("control", key: "VALUE_CONTROL") {
                attributeState "default", action:"valueInc"
         	}       
                    tileAttribute ("device.showMode", key: "SLIDER_CONTROL") {
            attributeState("showMode", action:"toggleMode")
        }
 
        }  
        standardTile("power", "device.switch", height: 2, width: 2, inactiveLabel:false, icon:"st.switches.switch.on") {
    		state "on", label: 'On',backgroundColor: "#41F073", action: "off"
    		state "off", label: ' ', action: "on"
  		}        
        valueTile("back", "device.status", height: 1, width: 2, inactiveLabel:false, decoration:"flat") {
            state "default", label:'Back', icon:"", action:"goBack"
        }     
        standardTile("home", "device.dispMain", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {          
            state "Home", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit"
            state "Settings", label:' ', icon:"st.nest.empty", action:""
            state "default", label:'Home', icon:"st.nest.nest-home", action:"goHome"
        }     
        //valueTile("exit", "device.status", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
        //    state "default", label:'Exit', icon: "", action:"goExit"
        //}             
        standardTile("ok", "device.status", height: 2, width: 2, inactiveLabel:false, decoration:"flat") {
            state "default", label:'OK', icon: "st.samsung.da.REF_2line_freezer", action:"goOK"
        }        
        
        standardTile("input", "device.dispMain", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
           // state "Settings", label:' ', icon:"st.nest.empty", action:""
            state "Input", label:' ', icon:"st.nest.empty", action:""
            state "Netflix", label:' ', icon:"st.nest.empty", action:""
           // state "Hulu", label:' ', icon:"st.nest.empty", action:""
           // state "Smart", label:' ', icon:"st.nest.empty", action:""
            
            //state "Input", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart" 
            state "default", label:'Input', icon: "st.Electronics.electronics4", action:"goInput", nextState: "Input"
            //state "default", label:'', icon: "st.quirky.egg-minder.quirky-egg-report", action:"goChList", nextState: "true"
        }     
        
        standardTile("simp", "device.dispMain", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "Smart", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart"
            state "Netflix", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart"
          //  state "Hulu", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart"
            state "Input", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart" 
            state "Settings", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart"
            state "Home", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart" 
            
           // state "GoogleCast", label:'TV', icon: "st.Electronics.electronics15", action:"goTV", nextState: "default"
			state "default", label:'Cast', icon: "st.Electronics.electronics8", action:"goCast", nextState: "GoogleCast"
        }                                
        
        standardTile("netflix", "device.dispMain", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "Netflix", label:' ', icon: "st.nest.empty", action:""
            //state "Settings", label:' ', icon:"st.nest.empty", action:""
			state "default", label:'Netflix', icon: "st.Electronics.electronics7", action:"goNetflix", nextState: "Netflix"
        }     
        
        standardTile("rwd", "rwd", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
          state "default", label:'', icon: "st.sonos.previous-btn", action:"rwd"
        }      
        
        standardTile("fwd", "fwd", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
          state "default", label:'', icon: "st.sonos.next-btn", action:"fwd"
        }      
        
        standardTile("shift", "device.shift", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "10", label:'${currentValue}', icon: "st.illuminance.illuminance.bright", action:"toggleShift", nextState:"0"
            state "5", label:'${currentValue}', icon: "st.illuminance.illuminance.light", action:"toggleShift", nextState:"10"
            state "0", label:'', icon: "st.illuminance.illuminance.dark", action:"toggleShift", nextState:"5"
            //state "20", label:'', icon: "st.Weather.weather14", action:"toggleShift", nextState:"5"
        }      
                       
        standardTile("play", "device.playing", height: 1, width: 2, inactiveLabel:false, decoration:"flat") {
            state "pause", label:'', icon: "st.sonos.play-btn", action:"play", nextState: "play"  
            state "play", label:'', icon: "st.sonos.pause-btn", action:"pause", nextState: "pause"
            state "default", label:' ', icon:"st.nest.empty", action:""
          //  state "false", label:'!', icon:"st.unknown.zwave.remote-controller", action:"goShortcut", nextState: "false"
        }      
        
        standardTile("settings", "device.dispMain", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "settings", label:'Advanced', icon: "st.secondary.configure", action:"goAdvSettings",nextState:"settings"
           
            state "Smart", label:'Options', icon: "st.secondary.tools", action:"goMenu", nextState: "smart" 
            state "Netflix", label:'Options', icon: "st.secondary.tools", action:"goMenu", nextState: "smart" 
           // state "Hulu", label:'Options', icon: "st.secondary.tools", action:"goMenu", nextState: "smart" 
            state "default", label:'Settings', icon: "st.secondary.tools", action:"goSettings",nextState:"settings"
            
            //st.unknown.zwave.remote-controller to
        }             
        standardTile("up", "device.tvMode", height: 1, width:2, inactiveLabel:false, decoration:"flat") {
            state "true", label:'+', icon:"st.thermostat.thermostat-up-inactive", action:"channelUp", nextState: "true"
            state "default", label:'', icon:"st.thermostat.thermostat-up", action:"goUp", nextState: "default"
        }     
        
        standardTile("down", "device.tvMode", height: 1, width: 2, inactiveLabel:false, decoration:"flat") {
            state "true", label:'-', icon:"st.thermostat.thermostat-down-inactive", action:"channelDown", nextState: "true"
            state "default", label:'', icon:"st.thermostat.thermostat-down", action:"goDown", nextState: "default"
        }  
        standardTile("right", "device.shift", height: 2, width: 1, inactiveLabel:false, decoration:"flat") {
            state "0", label:'', icon:"st.thermostat.thermostat-right", action:"goRight",nextState:"0"
            state "5", label:'*', icon:"st.thermostat.thermostat-right", action:"goRight",nextState:"5"
            state "10", label:'* * *', icon:"st.thermostat.thermostat-right", action:"goRight",nextState:"10"

		}  
        standardTile("left", "device.shift", height: 2, width: 1, inactiveLabel:false, decoration:"flat") {
            state "0", label:'', icon:"st.thermostat.thermostat-left", action:"goLeft",nextState:"0"
            state "5", label:'*', icon:"st.thermostat.thermostat-left", action:"goLeft",nextState:"5"
            state "10", label:'* * *', icon:"st.thermostat.thermostat-left", action:"goLeft",nextState:"10"
        }  
        
        standardTile("mute", "device.muted", height: 2, width: 2, inactiveLabel:false, decoration:"flat") {
            state "true", label:'Muted', icon:"st.custom.sonos.muted", action:"mute", nextState: "false"
            state "false", label:'', icon:"st.custom.sonos.unmuted", action:"mute", nextState: "true"
            state "default", label:'${currentValue}', icon:"st.custom.sonos.unmuted", action:"mute", nextState: "true"
        }
        standardTile("volumeUp", "device.shift", height: 2, width: 2, inactiveLabel:false, decoration:"flat") {
            state "0", label:'', icon:"st.custom.buttons.add-icon", action:"TV.volumeUp",nextState:"0"  
            state "5", label:'*  *', icon:"st.custom.buttons.add-icon", action:"TV.volumeUp",nextState:"5"
            state "10", label:'*   *   *   *', icon:"st.custom.buttons.add-icon", action:"TV.volumeUp",nextState:"10"            
        }
        standardTile("volumeDown", "device.shift", height: 2, width: 2, inactiveLabel:false, decoration:"flat") {
            state "0", label:'', icon:"st.custom.buttons.subtract-icon", action:"TV.volumeDown",nextState:"0"  
            state "5", label:'*  *', icon:"st.custom.buttons.subtract-icon", action:"TV.volumeDown",nextState:"5"
            state "10", label:'*   *   *   *', icon:"st.custom.buttons.subtract-icon", action:"TV.volumeDown",nextState:"10" 
        }
        standardTile("refresh", "device.status", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "default", label:'', icon:"st.secondary.refresh", action:"Refresh.refresh"
        }

        valueTile("dummy", "dummy", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "default", label:"", icon:" "
        }
        
        valueTile("dispMain", "device.dispMain", height: 1, width: 4, inactiveLabel:false, decoration:"flat") {
            state "default", label:'${currentValue}', icon:" ", action:"getDisplay"
        } 
        
        valueTile("dispDetails", "device.dispDetails", height: 1, width: 3, inactiveLabel:false, decoration:"flat") {
            state "default", label:'${currentValue}', icon:" ", action:"goInfo"
        }             

		standardTile("reqAuth", "reqAuth", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "default", label:"Pairing Key", icon:" ", action:"reqAuthCommand"
        }
         
        valueTile("debug", "debug", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "default", label:'!', action:"goTV"
        }
         
        valueTile("n1", "temporary", height: 1, width: 1) {state "default", label:'1', action:"go1"}          
        valueTile("n2", "temporary", height: 1, width: 1) {state "default", label:'2', action:"go2"}          
        valueTile("n3", "temporary", height: 1, width: 1) {state "default", label:'3', action:"go3"}          
        valueTile("n4", "temporary", height: 1, width: 1) {state "default", label:'4', action:"go4"}          
        valueTile("n5", "temporary", height: 1, width: 1) {state "default", label:'5', action:"go5"}          
        valueTile("n6", "temporary", height: 1, width: 1) {state "default", label:'6', action:"go6"}          
        valueTile("n7", "temporary", height: 1, width: 1) {state "default", label:'7', action:"go7"}          
        valueTile("n8", "temporary", height: 1, width: 1) {state "default", label:'8', action:"go8"}          
        valueTile("n9", "temporary", height: 1, width: 1) {state "default", label:'9', action:"go9"}          
        valueTile("n0", "temporary", height: 1, width: 1) {state "default", label:'0', action:"go0"}          
                 
        valueTile("nDash", "temporary", height: 1, width: 1) {state "default", label:'-', action:"goDash"} 
        
        standardTile("cursorState", "temporary", height: 1, width: 2, inactiveLabel:false, decoration:"flat") {
            state "default", label:"", icon: "st.motion.acceleration.active", action:"goInfo", nextState: "changed"
        }
        
        
        main (["preview"])
        
		details(["power","dispMain","home","dispDetails", 
        		 "input","netflix","simp","up","shift",
                 "back", "left", "ok","right",
                 "play",
                 "rwd","fwd","debug",
                 "down", "settings",
                 "volumeDown", "mute", "volumeUp",
                 //"temp1","temp2",
                 "n1","n2","n3","n4",
                   "refresh","reqAuth",
                 "n5","n6","n7","n8",
                 "n9","nDash","n0"
                 ])
	}
}

def listMethod(){	log.debug "list method"	}
def updateMethod(){	log.debug "update method"	}

def debug()
{

 appClose("0", "Settings")
// appTerm("102", "Settings")
    //def inc = 1000
//    def newPos = (state.curPos as Integer) + inc
    
 //   log.trace "curpos = $state.curPos"
 //   goPoint(6,0)
 //   sendEvent(name:'debug', value:state.curPos, displayed: false)
    
	//tvMove("$newPos","0")
   // updateDataValue("curPos", "$newPos")
    //
}

def goPoint(x,y) {
	unschedule("resetPointer")
    tvMove("1","1")
    tvMove("-1","-1")
    
	x = x + (state.curPos.split(":")[0] as Integer)
    y = y + (state.curPos.split(":")[1] as Integer)
    updateDataValue("curPos", "${x}:${y}")
    
	def xDec = Math.floor(x/15); def yDec = Math.floor(y/15)
	x = x-(xDec*15) as Integer; y = y-(yDec*15) as Integer 

	for (int i = 0; i < xDec; i++) tvMove("15","0")
	for (int i = 0; i < yDec; i++) tvMove("0","15")
        log.debug "$x, $y"
    tvMove("$x","$y")
    
    runIn(10,"resetPointer")
}

def resetPointer() { updateDataValue("curPos", "0:0"); sendEvent(name:'debug', value:"", displayed: false) }


def go1() {	tvCommand(3, "channelChange")}
def go2() {	tvCommand(4, "channelChange")}
def go3() {	tvCommand(5, "channelChange")}
def go4() {	tvCommand(6, "channelChange")}
def go5() {	tvCommand(7, "channelChange")}
def go6() {	tvCommand(8, "channelChange")}
def go7() {	tvCommand(9, "channelChange")}
def go8() {	tvCommand(10, "channelChange")}
def go9() {	tvCommand(11, "channelChange")}
def go0() {	tvCommand(2, "channelChange")}

def goDash() {	tvCommand(402, "channelChange")}

def updated() {
	log.debug "executing updated()"
    state.remove("oldMode")
    state.remove("appList")
    
	updateDataValue("preOffing","false")
    refresh()
}


def setStatus(value) {
	log.debug "executing setStatus $value"
    switch(value) {
    	case "volUp": 
        	volumeUp()
            break
        case "volDown": 
        	volumeDown()
            break
     }
}

def setLevel(value) {
    value = value as Integer
    log.trace "setlevel $value"
	updateDataValue("newLevel","$value")
	//state.newLevel = value
   // runIn(1, "setLevelWorker")
    setLevelWorker()
}

def setLevelWorker() {
	def value = state.newLevel as Integer
	log.debug "value is $value"
	if ((value < 45)) {
    	if (value == 7) goShortcut()
        
        if (value == 4) go4()
        if (value == 5) go5()
        if (value == 9) go9()
        if (value == 26) go26()
        if (value == 32) go32()
        
        log.debug "it's ${(value == 1) && (state.curMute == 'false')}"
        if ((value == 11) && (state.curMute == 'false')) mute()
        if ((value == 10) && (state.curMute == 'true')) mute()
    } else if (value > 89) {   
        if (value > 95) delayTvCommand(27, value-95, "channelChange")
        if (value < 95) delayTvCommand(28, 95-value, "channelChange") 
    } else {
    	if (value > 70) delayTvCommand(24, value-70,"volumeChange")
    	//else if (value > 71) delayTvCommand(24, 5,"volumeChange")
        if (value < 70) delayTvCommand(25, 70-value,"volumeChange")
        //else if (value < 69) delayTvCommand(25, 5,"volumeChange")
    }
	sendEvent(name: "level", value: "70", isStateChange: true, displayed: false)
}


def on() {
	log.debug "Setting 'on'"
	sendEvent(name: 'switch', value:"on")
    sendEvent(name:'working', value:"true", displayed: false)
    log.trace "preOffing = $state.preOffing"
    updateDataValue("preOffing","false")
	refresh()
	//chMode()
}

def preOff() {
	log.debug "Executing 'off'"
    updateDataValue("preOffing","false")
    //sendEvent(name: "level", value: "1", isStateChange: true)
	sendEvent(name: "switch", value:"off", isStateChange: true)
    sendEvent(name: 'sessionId', value:"", displayed: false)
	sendEvent(name:'working', value:"off", displayed: false)
    
	sendEvent(name:'dispMain', value:"", displayed: false)
	sendEvent(name:'dispDetails', value:"", displayed: false)    
	sendEvent(name:'chShow', value:"", displayed: false)
	sendEvent(name:'volShow', value:"", displayed: false)    
	sendEvent(name:'muted', value:"", displayed: false)
	sendEvent(name:'changed', value:"off", displayed: false)
    unschedule()
}

def off() {
	preOff()
    return tvCommand(1)
}

// parse events into attributes
def parse(String description) {
	//log.debug "Parsing '${description}'"    
    if (description == "updated") sendEvent(name:'refresh', displayed:false)
   
   	def headers = ""
	def parsedHeaders = ""
    
    def msg = parseLanMessage(description)
   // log.debug "Parsed: $msg"

    def headerMap = msg.headers      // => headers as a Map
    def headerString = msg.header      // => headers as a String
    def body = msg.body              // => request body as a string
    def status = msg.status          // => http status code of the response
    def caller = msg.requestId
    if (!caller) caller = ""
    log.debug "caller is $caller"
  
    def contentType = (headerString =~ /Content-Type:.*/) ? (headerString =~ /Content-Type:.*/)[0] : null
    log.debug "Status: $status, $contentType, index is $msg.index"
    //   	  if (msg.body.contains("auid")) updateDataValue("appList", msg.body)
    
    if (status == 200) {   	  
		if (contentType?.contains("xml")) {
    		body = new XmlSlurper().parseText(body)
			def sessionId = body.session.text() 
			if (valid(sessionId)) sendEvent(name:'sessionId', value:sessionId, displayed:false)    	
		
	        def data = body.data  
			if (valid(data.text())) { data.children().each{ if (valid(it.text())) {
 		       	log.debug "| ${it.name()} = ${it.text()}"	
            	if (it.name() == "physicalNum") updateDataValue("chPhys", it.text())
            	if (it.name() == "chtype") updateDataValue("chType", it.text())
           	 	if (it.name() == "major") updateDataValue("chMajor", it.text())
            	if (it.name() == "minor") updateDataValue("chMinor", it.text())
            	if (it.name() == "chname") updateDataValue("chName", it.text())
            	if (it.name() == "progName") updateDataValue("chProg", it.text())           
            	if (it.name() == "mode") {
                	updateDataValue("oldMode", state.curMode)
                    updateDataValue("curMode", it.text())
                }    
				if (it.name() == "mute") updateDataValue("curMute", it.text())
            	if (it.name() == "level") updateDataValue("curLevel", it.text())
        	}	}	}	
		}
            
		if (caller=="volumeChange") getVolume()//runIn(1,"getVolume")  
        if (caller=="channelChange") {
            runIn(1,"getChannel")
            runIn(3,"getChannelDup")
        }    
        if (caller=="modeChange") {
                sendEvent(name:'working', value:"true", displayed: false)
        	    runIn(2,"getMode") 
        		runIn(4,"getModeDup") 
                
        }
        
        if (caller.contains("launch:")) {
            def launchedApp = caller.split(":")[1]
            if (launchedApp == "Netflix" || launchedApp == "Hulu" || launchedApp == "Amazon"  || launchedApp == "Settings"  || launchedApp == "YouTube") {
            	log.debug "launched app is: $launchedApp"
				updateDataValue("curApp","$launchedApp")
                storeDisplay("smart")
            } //else runIn(3, "getMode")  
            runIn(3, "getMode")             
            runIn(6, "getModeDup") 	
        }   
        
        if (caller.contains("goTVmenu")) {
        	delay(75)
            tvCommand(12)
            delay(325)
            tvCommand(20)
            runIn(2, "getMode")
            runIn(4, "getModeDup") 
        }
        
        if (caller.contains("appname:")) {
        	def temp = caller.split(":")[1]
			log.debug "APPID: $temp = $body"
            updateDataValue("id$temp", "$body")
        }
                
		if (caller=="getVolume") { 
        	if (valid(state.curMute) && valid(state.curLevel)) {	
        		def muted       
            	log.debug "curLevel $state.curLevel | curMute $state.curMute"
	        	if (state.curMute == "true") muted = "true"//sendEvent(name:'muted', value:"true")
            		else muted = state.curLevel//sendEvent(name:'muted', value:state.curLevel)
            	sendEvent(name:'muted', value:"$muted", displayed: false)      
                sendEvent(name:'lock', value:"$muted", displayed: false)  

             //	if (device.currentValue('showMode') == 0) {
             		if (!device.currentValue("changed").contains("ch")) sendEvent(name: 'changed', value:"$muted", displayed: false)
             //	}	 
               // else	if (device.currentValue("showMode") == 100) sendEvent(name: 'volShow', value:"$muted", displayed: false)
			}	else getVolume()
       	}
        
		if (caller=="getMode") { 
        	if (state.curMode == "TouchPad") {
				//if (state.oldMode != "Touch
				getAppStates()  
            }	else
    	    if (state.curMode == "VolCh") getChannel()
        }
        if (caller=="getChannel") { if (valid(state.curMode)) {
        	if (state.curMode == "VolCh") { 
                if (valid(state.chType)) {
					if (state.chType == "satellite") storeDisplay("cast")
            		if (state.chType == "terrestrial") storeDisplay("true")
            	}	else getChannel()
        	}   
        }	else getMode()	}
        
		if (caller.contains("status:")) {
        	def temp = caller.split(":")[1]
			log.debug "$temp = $body"
			if (body.contains("RUN") || body.contains("LOAD")) {
                updateDataValue("curApp","$temp")
                log.debug "setting curApp to $temp"
                storeDisplay("smart")
                //runIn(30, "getMode") 
             } 
		}
    }

    def working = device.currentValue('working')  

    if (working == "true") {
    	if (status == 401) {
    		log.debug "Unauthorized - clearing session value"
           	refresh()
        }
    }
    
    if (working != "off") {
   		sendEvent(name:'working', value:"false", displayed: false)
        if (device.currentValue("switch") == "off") {
       	  sendEvent(name: 'switch', value:"on")
          sendEvent(name: "level", value: "70", isStateChange: true, displayed: false)
          update()
        }
        unschedule("preOff")
        log.debug "unscheduling preOff"
    }
        
}

def storeDisplay(tvMode) {    log.debug "storing Display (tvMode = $tvMode) (curApp = $state.curApp) "
    sendEvent(name:'tvMode', value:tvMode, linkText: "Mode Change", descriptionText: "$tvMode")
    if (tvMode != "true") updateDataValue("chMajor", "$tvMode") 
	def curMain; def curProg=""
    switch(tvMode) {
		case "smart": 
        	curMain = "Smart"
            if (state.curApp == "") curProg = "Reading..." else
            if (state.curApp == "none") {
            	curProg = "Unknown"
                sendEvent(name:'playing', value:"play")
            } else
            {
            	log.debug "special case!"
            	curMain = "$state.curApp" 
            	curProg = "Smart App"
                
                if (curMain == "Settings") sendEvent(name:'playing', value:"settings", displayed: false) else
                if (curMain == "Home") sendEvent(name:'playing', value:"none", displayed: false) else
                if (curMain == "Input") sendEvent(name:'playing', value:"none", displayed: false)
                else sendEvent(name:'playing', value:"play", displayed: false)
                }    
            //sendEvent(name:'playing', value:"play") 
		break    
    	case "true": 
        	curMain = state.chMajor +'.'+ state.chMinor +' - '+ state.chName + ' ('+state.chPhys+')'; 
            curProg = state.chProg
            sendEvent(name:'playing', value:"false", displayed: false)
            //updateDataValue("curApp","")
		break
		case "cast": 
         	curMain = "GoogleCast"
            sendEvent(name:'playing', value:"play", displayed: false)
            //updateDataValue("curApp","")
		break
	}
	sendEvent(name:'dispMain', value:curMain)
	sendEvent(name:'dispDetails', value:curProg)
    
    //if (device.currentValue('showMode') == 100) sendEvent(name: 'changed', value:"ch${state.chMajor}", displayed: false)
}

def chMode() {
	log.debug "executing chMode"
	def chVal = "ch${state.chMajor}"
    //if (chVal == chtrue) chVal = "ch${state.chMajor}"
    
    /*
	sendEvent(name: 'changed', value:chVal, displayed: false)
	sendEvent(name: 'volShow', value:device.currentValue("muted"), displayed: false)     
	//sendEvent(name: 'chShow', value:"hidden")      
    sendEvent(name: 'chShow', value:"                                                        \n${device.currentValue('dispDetails')}", displayed: false) 
    sendEvent(name: "showMode", value: 100, displayed: false)
    */
}

def volMode() {
	log.debug "executing volMode"
	def volVal = device.currentValue('muted')
	sendEvent(name: 'changed', value:volVal, displayed: false)
    def simpMain
    if (device.currentValue('dispMain')=="GoogleCast") simpMain = "GoogleCast"
    else if (device.currentValue('dispMain')=="Smart") simpMain = "     Touch   "
    else simpMain = "        "+ state.chMajor +'.'+ state.chMinor
	sendEvent(name: 'chShow', value:"                                ${simpMain}            \n${device.currentValue('dispDetails')}", displayed: false)    
	sendEvent(name: 'volShow', value:"hidden", displayed: false)        
    sendEvent(name: "showMode", value: 0, displayed: false)
}

def toggleMode(val) {
	log.debug "slider is $val"
    if (val > 50) chMode() else volMode()
    //sendEvent(name: "showMode", value: val)
}

def valueInc(n) {
	switch(device.currentValue("changed")) {
		case ~/.*ch.*/:
			log.debug "chInc($n)"
            if (device.currentValue("tvMode") == "true") {
				if (n==1) channelUp()
  				if (n==0) channelDown()
            } 
            if (device.currentValue("tvMode") == "cast" || device.currentValue("tvMode") == "smart" ) {
				if (n==1) play()
  				if (n==0) next()
            }           
            break
        case ~/.*.*/:     
			log.debug "volumeInc($n)"
			if (n==1) volumeUp()
  			if (n==0) volumeDown()
    		break         
    }
}

////////////////////////////////

def toggleShift(){
	if (device.currentValue('shift') == "0") sendEvent(name:"shift", value:"5", linkText: "Shift: 5", descriptionText: "") else
    if (device.currentValue('shift') == "5") sendEvent(name:"shift", value:"10", linkText: "Shift 10", descriptionText: "") else
    sendEvent(name:"shift", value:"0", linkText: "Shift Off", descriptionText: "") 

}

///////////////////////////////////////////////

def playPause() { if (device.currentValue('playing') == "play") pause()
				  else if (device.currentValue('playing') == "pause") play()
                 }

def pause() {	log.debug "pause"; //sendEvent(name:'changed', value:"pause")
				sendEvent(name:'playing', value:"pause", displayed: false)
				if (device.currentValue('tvMode') == "smart" || device.currentValue('tvMode') == "cast") {
					tvCommand(34); 
	  		    	//sendEvent(name:'changed', value:"ch${device.currentValue('tvMode')}", isStateChange: true, displayed: false)
				}
            }
def play() {	log.debug "play"; //sendEvent(name:'changed', value:"play"); 
				sendEvent(name:'playing', value:"play", displayed: false)
				if (device.currentValue('tvMode') == "smart" || device.currentValue('tvMode') == "cast") {
					tvCommand(33) 
	  		    	//sendEvent(name:'changed', value:"ch${device.currentValue('tvMode')}", isStateChange: true, displayed: false)
				}        
        
        }
def fwd() {log.debug "fwd"; tvCommand(36)}
def rwd() {log.debug "rwd"; tvCommand(37)}

def goShortcut() { 	
	if (device.currentValue('tvMode') == "true") tvChannelChange(7,1,7) else {
    	if (device.currentValue('dispMain') == "GoogleCast")  goTV() 
    	else goExit()
        //delayTvChannelChange(7,1,7, 3)
        //delayTvChannelChange(7,1,7, 5)
        //delayTvChannelChange(7,1,7, 10)
      //  delay(200)  
      //  tvChannelChange(7,1,7)
        delay(3000)  
        tvChannelChange(7,1,7)
    }    
}

def channelUp()	{ if (device.currentValue('shift') == "5") delayTvCommand(27, 5, "channelChange") else
				  if (device.currentValue('shift') == "10") delayTvCommand(27, 10, "channelChange") else tvCommand(27,"channelChange")
                 // toggleMode(100); sendEvent(name:'changed', value:"chUp", displayed: false); 
}

def channelDown(){ if (device.currentValue('shift') == "5") delayTvCommand(28, 5, "channelChange") else
				  if (device.currentValue('shift') == "10") delayTvCommand(28, 10, "channelChange") else tvCommand(28, "channelChange")
                 // toggleMode(100); sendEvent(name:'changed', value:"chDown", displayed: false);
}                  

def volumeUp() { if (device.currentValue('shift') == "5") delayTvCommand(24, 5,"volumeChange") else
				 if (device.currentValue('shift') == "10") delayTvCommand(24, 10,"volumeChange") else tvCommand(24,"volumeChange")
				 sendEvent(name:'changed', value:"volDown", displayed: false); //sendEvent(name:'muted', value:"false")
                 //getVolume()
}

def volumeDown() { if (device.currentValue('shift') == "5") delayTvCommand(25, 5,"volumeChange") else
				   if (device.currentValue('shift') == "10") delayTvCommand(25, 10,"volumeChange") else tvCommand(25,"volumeChange")
				   sendEvent(name:'changed', value:"volDown", displayed: false); //sendEvent(name:'muted', value:"false")
}

def lock() {
	log.debug "locker!"
	sendEvent(name: 'lock', value:"$muted", displayed: false)
	mute()
}

def unlock() {
	sendEvent(name: 'lock', value:"$muted", displayed: false)
	mute()
}

def mute() {  
	log.debug "mute called!"
    tvCommand(26,"volumeChange")
    def muted; if (device.currentValue('muted') == "true") muted = "false" else muted = "true"
    sendEvent(name: 'lock', value:"$muted", displayed: false)
    log.debug "Current muted: ${device.currentValue('muted')} ...and muted is $muted"
    sendEvent(name:'changed', value:"volChange", displayed: false); sendEvent(name: 'muted', value:muted, displayed: false)
}  

def getVolume() { updateDataValue("curMute", ""); 
					updateDataValue("curLevel", "")
					query("data?target=volume_info", "getVolume")  }
def getVolumeDup() {getVolume()}
def getMode() { query("data?target=context_ui","getMode") 
log.trace "GETTING MODE"}
def getModeDup() {getMode()}
def getChannel() { query("data?target=cur_channel","getChannel") }
	def getChannelDup() {getChannel()}

def getDisplay() { 	updateDataValue("chMajor", ""); updateDataValue("chPhys", ""); updateDataValue("chType", ""); 
					updateDataValue("chMinor", ""); updateDataValue("chName", ""); updateDataValue("chProg", "");
					updateDataValue("curMode", "");
                    
                    getMode()
                 }
                 
def getAppStates() {
	log.debug "gettingAppStates"
    updateDataValue("curApp","")
   
	["Netflix","Hulu","Amazon","YouTube","Home","Settings","AdvSettings","ChList"].each { //,"Recent","Input","Simp","Video","SetTop","Menu"].each { 
        log.debug "Getting $it"
        runIn(2, "get$it")
    }
    //runIn(7, "getDone")
    runIn(30, "getDone")
}

def getDone() {
	log.debug "getting done & curApp = $state.curApp"
	if (state.curApp == "") {
    	log.debug "getting HER done"
		updateDataValue("curApp","none")
    	if (state.curMode == "TouchPad")  storeDisplay("smart")
    }
}

def getAppId(appName) { query("apptoapp/data/$appName", "appname:$appName") }
                 
def getNetflix() { appStatus("144115188075855874", "Netflix") }
def getHulu() { appStatus("144115188075855875", "Hulu") }
def getAmazon() { appStatus("144115188075855876", "Amazon") }
def getYouTube() { appStatus("144115188075855877", "YouTube") }

def getHome() { appStatus("9", "Home") }
def getSettings() { appStatus("102", "Settings") }
def getAdvSettings() { appStatus("120", "Settings") }
def getVideo() { appStatus("123", "Video") }
def getChList() { appStatus("117", "ChList") }
def getRecent() { appStatus("105", "Recent") }
def getSimp() { appStatus("119", "Simp") }
def getSetTop() { appStatus("160", "SetTop") }
def getInput() { appStatus("101", "Input") }
def getMenu() { appStatus("108", "Menu") }

def goNetflix() { appCommand("144115188075855874", "Netflix") }
def goHulu() { appCommand("144115188075855875", "Hulu") }

def goBack() { tvCommand(23,"modeChange") }
def goHome() { tvCommand(21,"modeChange") }
def goExit() { tvCommand(412,"modeChange") }
def goOK() { tvCommand(20,"modeChange") }
def goInput() { tvCommand(47,"modeChange") }
def goMenu() { tvCommand(417,"modeChange") }
def	goCast() { appCommand("160", "transfer")}
def goRecent() { appCommand("105", "Recent")}
def goSettings() {   if (device.currentValue('shift') == "0") appCommand("102", "Settings")
				   	 if (device.currentValue('shift') == "5") tvCommand(417,"modeChange")//appCommand("108", "Settings")
                     //if (device.currentValue('shift') == "10") appCommand("120", "Settings")
                     
}

def goReset() {
    def tvMode = device.currentValue('tvMode')
    if (device.currentValue('tvMode') == "smart") {
    	goExit()
    }
    if (device.currentValue('tvMode') == "cast") {
    	goTV()
    }
}

def goAdvSettings() { appCommand("120", "AdvSettings")}
def goTV() { appCommand("119", "goTVmenu")}
def goChList() { appCommand("117", "ChList")}
def goBrowser() { appCommand("12", "Browser")}
def goInfo() {
    def tvMode = device.currentValue('tvMode')
    if (tvMode == "smart") {
    	for (int i = 0; i < 20; i++) {
			tvMove("-1","0")
    		tvMove("1","0")
        }    
    	tvCommand(20)
    }    
    tvCommand(45)
}

def delay(duration, callback = {})
{
	def dTotalSleep = 0
	def dStart = new Date().getTime()
    def cmds = []
	cmds << "delay 2"
    
    while (dTotalSleep <= duration)
    {            
		cmds
        dTotalSleep = (new Date().getTime() - dStart)
    }

    log.debug "Slept ${dTotalSleep}ms"

	callback(dTotalSleep)
}

def delayTvCommand(key, loop, requestId="") {
	log.debug "delaying..."
	for (int c = 0; c < loop; c++) { 
    	tvCommand(key,requestId)  
        delay(250)  
     }
}
    
def goUp() {
    if (device.currentValue('shift') == "5") delayTvCommand(12, 3)  else
    if (device.currentValue('shift') == "10") delayTvCommand(12, 6) else tvCommand(12)   
}
      
def goDown() {
    if (device.currentValue('shift') == "5") delayTvCommand(13, 3) else
    if (device.currentValue('shift') == "10") delayTvCommand(13, 6) else tvCommand(13)
}
        
def goRight() {		log.debug "${device.currentValue('shift')}"
					if (device.currentValue('shift') == "5") delayTvCommand(15, 3) else
    				if (device.currentValue('shift') == "10") delayTvCommand(15, 6) else tvCommand(15) }
def goLeft() {      if (device.currentValue('shift') == "5") delayTvCommand(14, 3) else
    				if (device.currentValue('shift') == "10") delayTvCommand(14, 6) else tvCommand(14) }

def update()
{
	//updateDataValue("chList","[[48,4],[36,5],[7,7],[9,9],[35,20],[25,25],[50,50],[50,50],[50,50]]")
    log.debug "executing Update()"
    getDisplay()
    getVolume() 
    cursorVisible("true")
}

def refresh() 
{
    log.debug "Executing 'refresh' and ${device.currentValue('switch')}"
	sessionIdCommand()
    if (device.currentValue('switch') == "on") 
    {
    	log.trace "preoffing = ${state.preOffing}"
    	if (state.preOffing == 'false') {
        	updateDataValue("preOffing","true")
            runIn(90, "preOff")
        	log.debug "scheduling 'preOff()'"
		}
		update()
    }
    if (device.currentValue('switch') == "off") sendEvent(name:'working', value:"false", displayed: false)
}

def tvChannelChange(major, minor, phys) {
    goCommand("HandleChannelChange",
    			"<major>${major}</major><minor>${minor}</minor><sourceIndex>1</sourceIndex><physicalNum>${phys}</physicalNum>",
                "channelChange")
}

def delayTvChannelChange(major, minor, phys, loop) {
	log.debug "delaying..."
	for (int c = 0; c < loop; c++) { 
    	tvChannelChange(major, minor, phys)
        delay(250)  
     }
}

def tvClick() {	goCommand("HandleTouchClick"); 	goCommand("HandleTouchClick")}    
def tvMove(x, y) {	goCommand("HandleTouchMove","<x>$x</x><y>$y</y>")	}
def cursorVisible(value) {	goEvent("cursorVisible","<value>$value</value>")}

def reqAuthCommand() {	goAuth("AuthKeyReq","") }
def sessionIdCommand() {	log.debug "sessionIdCommand"
							goAuth("AuthReq","<value>$pairingKey</value>")  
                            
                       }
def appCommand(auid,requestId="") {	goCommand("AppExecute","<auid>${Long.toHexString(auid.toLong())}</auid>","launch:$requestId") }
def appClose(auid,requestId="") {	goCommand("AppTerminate","<auid>${Long.toHexString(auid.toLong())}</auid>","close:$requestId") }
def appStatus(auid, requestId="") { query("apptoapp/data/$auid/status", "status:$requestId") }
def appTerm(auid, requestId="") { sendHttp("POST", "/roap/api/apptoapp/command/$auid/term", "", requestId)}

def tvCommand(cmd,requestId="") { goCommand("HandleKeyInput","<value>${cmd}</value>",requestId) }
    
def goCommand(commandName, values,requestId="") { goService(commandName, "command","udap",values,requestId) }
def goEvent(commandName, values,requestId="") { goService(commandName, "event","udap",values,requestId) }
def goAuth(commandName, values,requestId="") { goService(commandName, "auth","roap",values,requestId) }

private Integer convertHexToInt(hex) {
    return Integer.parseInt(hex,16)
}

def goService(serviceName, serviceType, protocol, values, requestId="") {
    def method = "POST"
    //def path = "/roap/api/$serviceType"
    def path = "/$protocol/api/$serviceType"  
    def body = "<?xml version=\"1.0\" encoding=\"utf-8\"?><$serviceType><type>$serviceName</type>$values</$serviceType>"
	sendHttp(method, path, body, requestId) 
} 

def query(text, requestId="") {	
	def method = "GET"
	def path = "/udap/api/"+text 
    def body = ""
    sendHttp(method, path, body, requestId)
}

def sendHttp(String method, path, body, requestId = "") {
	def httpRequest = [
      	method:		"$method",
        path: 		"$path",
        body:		"$body",
        headers:	[
        			//"Soapaction": "urn:schemas-upnp-org:service:RenderingControl:1#SetVolume",
        			HOST: "$televisionIp:8080",
                    CONNECTION:	"Keep-Alive",  
                     //CONNECTION:	"Close",   
                    //"Accept":"application/json",
                    "Content-Type":	"application/atom+xml", //"application/x-apple-binary-plist",
                    //"Content-Type":	"text/xml; charset=utf-8",//
                    //"User-Agent": "UDAP/2.0"//
                    "User-Agent":"Linux UPnP/1.0 SmartThings"
                    ]
	] 
    def hubAction = new physicalgraph.device.HubAction(httpRequest)
    if (valid(requestId)) hubAction.requestId=requestId
    log.debug "$hubAction"
	sendHubCommand(hubAction)  
   // return hubAction
}


def valid(value) {
	def v=(value != null && value != "")
    //value.name()
	//log.debug "${value} is $v"
	return (v)
}   

private String convertHexToIP(hex) {
    return [convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}