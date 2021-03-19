/** LG Smart TV Device Type  */
 
metadata {
	definition (name: "Josh's LG Smart TV", namespace: "tv", author: "Josh") 
    {
		capability "TV"
        capability "Switch Level"
        capability "Switch"
        capability "Music Player"
        capability "Refresh"
        
      //  attribute "status", "STRING"        
        attribute "sessionId", "string"
        attribute "muted", "string"
        attribute "tvMode", "string"        
        attribute "dispMain", "string"
        attribute "dispDetails", "string"
        attribute "working", "string"
        attribute "changed", "string"
        attribute "shift", "string"
        attribute "playing", "string"
        
        command "volUp", ["string"]
        command "volDown", ["string"]
        command "toggleMode", ["number"]
        command "update" 
        command "refresh"
        command "chMode"
        command "volMode"
        command "mute"
        command "setMute", ["string"]
        command "getVolume"
        command "getChannel"
        command "getMode"
        command "getDisplay"
        
        command "goHome"
        command "goBack"
        command "goExit"
        command "goOK"
        command "goSettings"
        command "goPlay"
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
                
        command "goDebug"
        command "rwd"
        command "fwd"
        command "resetPointer"
	}
 
 /*
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
*/
    
    preferences {
        input("televisionIp", "string", title:"Television IP Address", description: "Television's IP address", required: true, displayDuringSetup: false)
        input("pairingKey", "string", title:"Pairing Key", description: "Pairing key", required: true, displayDuringSetup: false)
	}

	simulator 
    {
		// TODO: define status and reply messages here
	}

/*	tiles(scale: 2) {
        multiAttributeTile(name: "preview", type: "generic", width: 6, height: 4) {
           	tileAttribute("device.chShow", key: "MARQUEE") { 
            	attributeState "default", label:'${currentValue}'//, action: "volMode"
                attributeState "hidden", icon:" ", label:''
            }                        
            
            tileAttribute ("device.changed", key: "PRIMARY_CONTROL", wordWrap: true) {
           	    attributeState("off", icon:"st.Electronics.electronics15", label: 'Off',nextState: "on", backgroundColor: "#ffffff")
				attributeState "volUp", icon:"st.custom.sonos.unmuted", label:'+', backgroundColor:"#90EBE9", nextState:"chChange"		
				attributeState "volDown", icon:"st.custom.sonos.unmuted", label:'-', backgroundColor:"#90EBE9", nextState:"chChange"	
                attributeState "volChange", icon:'st.custom.sonos.unmuted', label:'', backgroundColor:"#90EBE9", nextState:"volNum"
                attributeState("true", icon:"st.custom.sonos.muted", label: '', nextState: "volChange", backgroundColor: "#90EBE9")
                attributeState("volNum", icon:"st.custom.sonos.unmuted", label: '${currentValue}', nextState: "volChange", backgroundColor: "#90EBE9", defaultState: true)                
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
            
        standardTile("ok", "device.status", height: 2, width: 2, inactiveLabel:false, decoration:"flat") {
            state "default", label:'OK', icon: "st.samsung.da.REF_2line_freezer", action:"goOK"
        }        
        
        standardTile("input", "device.dispMain", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "Input", label:' ', icon:"st.nest.empty", action:""
            state "Netflix", label:' ', icon:"st.nest.empty", action:""           
            state "default", label:'Input', icon: "st.Electronics.electronics4", action:"goInput", nextState: "Input"
        }     
        
        standardTile("simp", "device.dispMain", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "Smart", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart"
            state "Netflix", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart"
            state "Input", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart" 
            state "Settings", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart"
            state "Home", label:'Exit', icon: "st.unknown.zwave.static-controller", action:"goExit", nextState: "Smart" 
			state "default", label:'Cast', icon: "st.Electronics.electronics8", action:"goCast", nextState: "Apple / Google"
        }                                
        
        standardTile("netflix", "device.dispMain", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "Netflix", label:' ', icon: "st.nest.empty", action:""
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
            state "default", label:'!', action:"goDebug"
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
	} */
}

//def listMethod(){	log.debug "list method"	}
//def updateMethod(){	log.debug "update method"	}

def updated() {    
	updateDataValue("preOffing","false")
    refresh()
}

def volUp(value) { volumeUp(value as Integer) }
def volDown(value) { volumeDown(value as Integer) }

def nextTrack() { volumeUp(1) }
def previousTrack() { volumeDown(1) }
def play() { mute() }

def setLevel(value) {
    value = value as Integer
    log.trace "setlevel $value"
    
   def vol = state.curLevel as Integer
   if (value > vol) volumeUp(value-vol)//delayTvCommand(24, value-vol,"volumeChange")
   if (value < vol) volumeDown(vol-value)//delayTvCommand(25, vol-value,"volumeChange")
   sendEvent(name: "level", value: value, isStateChange: true, displayed: false)
}


def on() {
	log.debug "Setting 'on'; preOffing = $state.preOffing"
	sendEvent(name: 'switch', value:"on")
    sendEvent(name:'working', value:"true", displayed: false)
    updateDataValue("preOffing","false")
	refresh()
}

def preOff() {
	log.debug "Executing 'off'"
    updateDataValue("preOffing","false")
	sendEvent(name: "switch", value:"off", isStateChange: true)
    sendEvent(name: 'sessionId', value:"", displayed: false)
	sendEvent(name:'working', value:"off", displayed: false)
    
	sendEvent(name:'dispMain', value:"", displayed: false)
	sendEvent(name:'dispDetails', value:"", displayed: false) 
	sendEvent(name:'muted', value:"", displayed: false)
	sendEvent(name:'changed', value:"off", displayed: false)
    unschedule()
}

def off() {
	preOff()
    return tvCommand(1)
}


def parse(String description) {   
    if (description == "updated") sendEvent(name:'refresh', displayed:false)
   
   	def headers = ""
	def parsedHeaders = ""
    
    def msg = parseLanMessage(description)

    def headerMap = msg.headers      // => headers as a Map
    def headerString = msg.header      // => headers as a String
    def body = msg.body              // => request body as a string
    def status = msg.status          // => http status code of the response
    def caller = msg.requestId
    if (!caller) caller = ""
  
    def contentType = (headerString =~ /Content-Type:.*/) ? (headerString =~ /Content-Type:.*/)[0] : null
    log.debug "Caller: $caller, Status: $status, ContentType: $contentType, index: $msg.index"
    
    if (status == 200) {   	  
		if (contentType?.contains("xml")) {
    		body = new XmlSlurper().parseText(body)
			def sessionId = body.session.text() 
			if (valid(sessionId)) sendEvent(name:'sessionId', value:sessionId, displayed:false)    	
		log.debug "body: $body"
	        def data = body.data  
			if (valid(data.text())) { data.children().each{ if (valid(it.text())) {
 		       	log.debug "| ${it.name()} = ${it.text()}"	
            	if (it.name() == "physicalNum") updateDataValue("chPhys", it.text())
            	if (it.name() == "chtype") updateDataValue("chType", it.text())
           	 	if (it.name() == "major") updateDataValue("chMajor", it.text())
            	if (it.name() == "minor") updateDataValue("chMinor", it.text())
            	if (it.name() == "chname") updateDataValue("chName", it.text())
            	if (it.name() == "progName") updateDataValue("chProg", it.text())           
            	if (it.name() == "mode") updateDataValue("curMode", it.text())    
				if (it.name() == "mute") updateDataValue("curMute", it.text())
            	if (it.name() == "level") {
                	updateDataValue("curLevel", it.text())
                    setLevel(it.text())
                }    
        	}	}	}	
		}
            
		if (caller=="volumeChange") getVolume() 
        if (caller=="channelChange") {
            runIn(1,"getChannel")
            runIn(3,"getChannelDup")
        }    
        
        /*
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
            }  
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
        
        if (caller=="getMode") { 
        	if (state.curMode == "TouchPad") {
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
        
        
        */
                
		if (caller=="getVolume") { 
        	if (valid(state.curMute) && valid(state.curLevel)) {	
        		def muted       
            	log.debug "curLevel $state.curLevel | curMute $state.curMute"
	        	if (state.curMute == "true") muted = "true"
            		else muted = state.curLevel
            	sendEvent(name:'muted', value:"$muted", displayed: false)     

             	if (!device.currentValue("changed").contains("ch")) sendEvent(name: 'changed', value:"$muted", displayed: false)
                
			}	//else getVolume()
       	}
        

		if (caller.contains("status:")) {
        	def temp = caller.split(":")[1]
			log.debug "$temp = $body"
			if (body.contains("RUN") || body.contains("LOAD")) {
                updateDataValue("curApp","$temp")
                log.debug "setting curApp to $temp"
                storeDisplay("smart")
             } 
		}
    }

    def working = device.currentValue('working')  

  //  if (working == "true") {
    	if (status == 401) {
    		log.debug "Unauthorized - clearing session value"
           	refresh()
        }
  //  }
    
    if (working != "off") {
   		sendEvent(name:'working', value:"false", displayed: false)
        if (device.currentValue("switch") == "off") {
       	  sendEvent(name: 'switch', value:"on")
          update()
        }
        unschedule("preOff")
    }
        
}
/*
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
		break    
    	case "true": 
        	curMain = state.chMajor +'.'+ state.chMinor +' - '+ state.chName + ' ('+state.chPhys+')'; 
            curProg = state.chProg
            sendEvent(name:'playing', value:"false", displayed: false)
		break
		case "cast": 
         	curMain = "Apple / Google"
            sendEvent(name:'playing', value:"play", displayed: false)
		break
	}
	sendEvent(name:'dispMain', value:curMain)
	sendEvent(name:'dispDetails', value:curProg)
}

def chMode() {
	def chVal = "ch${state.chMajor}"
}

def volMode() {
	def volVal = device.currentValue('muted')
	sendEvent(name: 'changed', value:volVal, displayed: false)
    def simpMain
    if (device.currentValue('dispMain')=="Apple / Google") simpMain = "Apple / Google"
    else if (device.currentValue('dispMain')=="Smart") simpMain = "     Touch   "
    else simpMain = "        "+ state.chMajor +'.'+ state.chMinor
}

def toggleMode(val) {
    if (val > 50) chMode() else volMode()
}


////////////////////////////////

def toggleShift(){
	if (device.currentValue('shift') == "0") sendEvent(name:"shift", value:"5", linkText: "Shift: 5", descriptionText: "") else
    if (device.currentValue('shift') == "5") sendEvent(name:"shift", value:"10", linkText: "Shift 10", descriptionText: "") else
    sendEvent(name:"shift", value:"0", linkText: "Shift Off", descriptionText: "") 

}

///////////////////////////////////////////////
*/
def playPause() { if (device.currentValue('playing') == "play") pause()
				  else if (device.currentValue('playing') == "pause") goPlay()
                 }

def pause() {	log.debug "pause"; 
				sendEvent(name:'playing', value:"pause", displayed: false)
				if (device.currentValue('tvMode') == "smart" || device.currentValue('tvMode') == "cast") {
					tvCommand(34); 	  		    	
				}
            }
def goPlay() {	log.debug "play";
				sendEvent(name:'playing', value:"play", displayed: false)
				if (device.currentValue('tvMode') == "smart" || device.currentValue('tvMode') == "cast") {
					tvCommand(33) 	  		    	
				}        
        
        }
def fwd() {log.debug "fwd"; tvCommand(36)}
def rwd() {log.debug "rwd"; tvCommand(37)}

def channelUp()	{ if (device.currentValue('shift') == "5") delayTvCommand(27, 5, "channelChange") else
				  if (device.currentValue('shift') == "10") delayTvCommand(27, 10, "channelChange") else tvCommand(27,"channelChange")
}

def channelDown(){ if (device.currentValue('shift') == "5") delayTvCommand(28, 5, "channelChange") else
				  if (device.currentValue('shift') == "10") delayTvCommand(28, 10, "channelChange") else tvCommand(28, "channelChange")
}                  

def volumeUp(value) {delayTvCommand(24, value,"volumeChange")}


def volumeDown(value) {delayTvCommand(25, value,"volumeChange")}


def setMute(n)
{  log.debug "muting $n"
 if ((n == '1') && (device.currentValue('muted') != 'true')) mute()
 if ((n == '0') && (device.currentValue('muted') == 'true')) mute()
}

def mute() {  
    tvCommand(26,"volumeChange")
    def muted; if (device.currentValue('muted') == "true") muted = "false" else muted = "true"
    log.debug "Current muted: ${device.currentValue('muted')} ...and muted is $muted"
    sendEvent(name:'changed', value:"volChange", displayed: false); sendEvent(name: 'muted', value:muted, displayed: false)
}  

def getVolume() { updateDataValue("curMute", ""); 
					updateDataValue("curLevel", "")
					query("data?target=volume_info", "getVolume")  
                    }
def getVolumeDup() {getVolume()}

/*
def getMode() { query("data?target=context_ui","getMode") }
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
   
	["Netflix","Hulu","Amazon","YouTube","Home","Settings","AdvSettings","ChList","Input"].each { //,"Recent","Simp","Video","SetTop","Menu"].each { 
        log.debug "Getting $it"
        runIn(2, "get$it")
    }
    runIn(30, "getDone")
}

def getDone() {
	log.debug "getting done & curApp = $state.curApp"
	if (state.curApp == "") {
		updateDataValue("curApp","none")
    	if (state.curMode == "TouchPad") storeDisplay("smart")
    }
}
*/

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
	log.debug "delaying... $loop"
	for (int c = 0; c < loop; c++) { 
    	tvCommand(key,requestId) 
        log.trace "delay lopp $c"
        delay(250)  
     }
}
    
def goUp() { tvCommand(12) }
def goDown() { tvCommand(13) }
        
def goRight() {	tvCommand(15) }
def goLeft() { tvCommand(14) }

def update()
{
    log.debug "executing Update()"
    getVolume() 
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
        			HOST: "$televisionIp:8080",
                    CONNECTION:	"Keep-Alive",  
                    "Content-Type":	"application/atom+xml",                       //"Content-Type":	"text/xml; charset=utf-8",//
                    "User-Agent":"Linux UPnP/1.0 SmartThings"                     //"User-Agent": "UDAP/2.0"//
                    ]
	] 
    def hubAction = new physicalgraph.device.HubAction(httpRequest)
    if (valid(requestId)) hubAction.requestId=requestId
   // log.debug "$hubAction"
	sendHubCommand(hubAction)
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