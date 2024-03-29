/** LG Smart TV Device Type  */
 
metadata {
	definition (name: "Josh's TV", namespace: "tv", author: "Josh") 
    {
		
        capability "Switch Level"
        capability "Switch"
        capability "Music Player"
        capability "Refresh"
        capability "Door Control"
        capability "Lock"
        
        attribute "sessionId", "string"   
       // attribute "working", "string"
        
        command "volumeUp", ["string"]
        command "volumeDown", ["string"]
        command "update" 
        
        command "goHome"
        command "goBack"
        command "goExit"
        command "goOK"
        command "goSettings"
        command "playPause"
        command "goUp"
        command "goDown"
        command "goRight"
        command "goLeft"
        command "goInput"
        command "reqAuthCommand"
        command "goMenu"  
        command "rwd"
        command "fwd"
	}
 
    
    preferences {
        input("televisionIp", "string", title:"Television IP Address", description: "Television's IP address", required: true, displayDuringSetup: false)
        input("pairingKey", "string", title:"Pairing Key", description: "Pairing key", required: true, displayDuringSetup: false)
        input("multi", "number", title:"Multiplier", description: "Multiplier", required: true, displayDuringSetup: false)
    }


}

def setTrack(str) {
log.debug "go $str" 
	sendEvent(name: "trackDescription", value: "$str")
}


def open() {
	sendEvent(name: "door", value: "closing")
    volumeUp(multi)
}

def close() {
    sendEvent(name: "door", value: "closing")
    volumeDown(multi)
}

def updated() {    
	updateDataValue("preOffing","false")
    refresh()
}

def nextTrack() { volumeUp(1) }
def previousTrack() { volumeDown(1) }

def setLevel(value) {
   log.trace "setlevel $value"
   value = value as Integer
   if (value == 0) mute() else if (value == 100) unmute()
   else {
 		def vol = state.curLevel as Integer
		if (value > vol) volumeUp(value-vol)
		if (value < vol) volumeDown(vol-value)
		sendEvent(name: "level", value: value, isStateChange: true, displayed: false)
   }
}


def on() {
	log.debug "Setting 'on'; preOffing = $state.preOffing"
	sendEvent(name: 'switch', value:"on")
    //sendEvent(name:'working', value:"true", displayed: false) 
    updateDataValue("preOffing","false")
	refresh()
}

def preOff() {
	log.debug "Executing 'off'"
    updateDataValue("preOffing","false")
	sendEvent(name: "switch", value:"off", isStateChange: true)
    sendEvent(name: 'sessionId', value:"", displayed: false)
	//sendEvent(name:'working', value:"off", displayed: false)
    updateDataValue("preoffing", "off")
    sendEvent(name:'mute', value:"unmuted", displayed: false)
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
				if (it.name() == "mute") updateDataValue("curMute", it.text())
            	if (it.name() == "level") {updateDataValue("curLevel", it.text())}    
        	}	}	}	
		}
            
		if (caller=="volumeChange") runIn(5,getVolume)
                
		if (caller=="getVolume") { 
        	if (valid(state.curMute) && valid(state.curLevel)) {	   
            	log.debug "curLevel $state.curLevel | curMute $state.curMute"
	        	//if (state.curMute == "true") setMute(true)
                //else {            
                  //setMute(false) // { sendEvent(name:'mute', value:"unmuted", displayed: false) }  
                  //setMute((state.curMute == "true") ? "muted":"unmuted")
                  sendEvent(name: 'mute', value:"${(state.curMute == "true") ? "muted":"unmuted"}", displayed: false)
                  setLevel(state.curLevel)
                //}
			}	
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

    //def working = device.currentValue('working')  
    if (status == 401) {
    	log.debug "Unauthorized - clearing session value"
    	refresh()
    }
    
    if (state.preOffing != "off") {
   		//sendEvent(name:'working', value:"false", displayed: false)
        if (device.currentValue("switch") == "off") {
       	  sendEvent(name: 'switch', value:"on")
          update()
        }
        unschedule("preOff")
        updateDataValue("preOffing","false")
    }
        
}

def playPause() { if (device.currentValue('status') == "playing") pause()
				  else if (device.currentValue('status') == "paused") play()
                 }

def pause() {	log.debug "going pause"; 
                sendEvent(name:'status', value:"paused", displayed: false)
				tvCommand(34); 	  		    	
            }
def play() {	log.debug "going play";
                sendEvent(name:'status', value:"playing", displayed: false)
				tvCommand(33) 	  		   
        
        }
def fwd() {log.debug "fwd"; tvCommand(36)}
def rwd() {log.debug "rwd"; tvCommand(37)}

def volumeUp(value) { log.trace "volume up by $value"
    delayTvCommand(24, value as Integer,"volumeChange")
    unmute()
    //sendEvent(name: "door", value: "open")
}
def volumeDown(value) {log.trace "volume down by $value"
    delayTvCommand(25, value as Integer,"volumeChange")
    unmute()
   // sendEvent(name: "door", value: "closed")  
}

def mute(){ setMute("muted") }// if (device.currentValue('mute')=="unmuted") toggleMute() }
def unmute(){ setMute("unmuted") }//if (device.currentValue('mute')=="muted") toggleMute() }


def setMute(muted) { 
	log.debug "setMute $muted"
    if (device.currentValue('mute') != muted) tvCommand(26,"volumeChange")
    sendEvent(name: 'mute', value:muted, displayed: false)

/* (bool) {
    	sendEvent(name: 'mute', value:"muted", displayed: false)
        sendEvent(name: "lock", value: "locked")
    } else {
    	sendEvent(name: 'mute', value:"unmuted", displayed: false)
        sendEvent(name: "lock", value: "unlocked")
	} */
}

/*def toggleMute() {    
    def muted = (device.currentValue('mute') == "unmuted")
    log.debug "Current mute: ${device.currentValue('mute')} ...and mute is $muted"
    setMute(muted)
    tvCommand(26,"volumeChange")
}  
*/

def lock() {
log.debug "locked"
	sendEvent(name: "lock", value: "locked")
//	mute()    
}
def unlock() {
log.debug "unlocked"
	sendEvent(name: "lock", value: "unlocked")
//    unmute()    
}

def getVolume() { updateDataValue("curMute", "");
					updateDataValue("curLevel", "")
					query("data?target=volume_info", "getVolume")  
                }
def getHome() { appStatus("9", "Home") }
def getSettings() { appStatus("102", "Settings") }
def getAdvSettings() { appStatus("120", "Settings") }
def getVideo() { appStatus("123", "Video") }
def getInput() { appStatus("101", "Input") }
def getMenu() { appStatus("108", "Menu") }

def goBack() { tvCommand(23,"modeChange") }
def goHome() { 	tvCommand(21,"modeChange") }
def goExit() { tvCommand(412,"modeChange") }
def goOK() { tvCommand(20,"modeChange") }
def goInput() { tvCommand(47,"modeChange") }
def goMenu() { tvCommand(417,"modeChange") }
def goSettings() { appCommand("102", "Settings") }


def delay(duration)
{
	def dTotalSleep = 0
	def dStart = new Date().getTime()
    
    while (dTotalSleep <= duration)
    {            
        dTotalSleep = (new Date().getTime() - dStart)
    }
}

def delayTvCommand(key, loop, requestId="") {
	log.debug "delaying... $loop"
	for (int c = 0; c < loop; c++) { 
        if (c == loop-1)	tvCommand(key,requestId) 
        else {
        	tvCommand(key,"loop $c") 
        	delay(250)
       }     
     }
}
    
def goUp() { tvCommand(12) }
def goDown() { tvCommand(13) }
        
def goRight() {	tvCommand(15) }
def goLeft() { tvCommand(14) }

def update()
{
    log.debug "executing Update"
    getVolume() 
}

def refresh() {
    log.debug "Executing refresh"// and ${device.currentValue('switch')}"
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
    if (device.currentValue('switch') == "off") updateDataValue("preOffing","false")//sendEvent(name:'working', value:"false", displayed: false)
}


def tvClick() {	goCommand("HandleTouchClick"); 	goCommand("HandleTouchClick")}    
def tvMove(x, y) {	goCommand("HandleTouchMove","<x>$x</x><y>$y</y>")	}
def cursorVisible(value) {	goEvent("cursorVisible","<value>$value</value>")}

def reqAuthCommand() {	goAuth("AuthKeyReq","") }
def sessionIdCommand() {	log.debug "sessionIdCommand"
							goAuth("AuthReq","<value>$pairingKey</value>","Pairing Key")  
                            
                       }
def appCommand(auid,requestId="") {	goCommand("AppExecute","<auid>${Long.toHexString(auid.toLong())}</auid>","launch:$requestId") }
def appClose(auid,requestId="") {	goCommand("AppTerminate","<auid>${Long.toHexString(auid.toLong())}</auid>","close:$requestId") }
def appStatus(auid, requestId="") { query("apptoapp/data/$auid/status", "status:$requestId") }
def appTerm(auid, requestId="") { sendHttp("POST", "/roap/api/apptoapp/command/$auid/term", "", requestId)}

def tvCommand(cmd,requestId="") { goCommand("HandleKeyInput","<value>${cmd}</value>",requestId) 
log.debug "sending..."}
    
def goCommand(commandName, values,requestId="") {log.debug "gocommand"; goService(commandName, "command","udap",values,requestId) }
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
                    //CONNECTION:	"Keep-Alive",  
                   "Content-Type":	"text/xml; charset=utf-8",// "Content-Type":	"application/atom+xml",                       //
                   // "User-Agent":"Linux UPnP/1.0 SmartThings"                     
                    "User-Agent": "UDAP/1.0"
                    ]
	] 
    def hubAction = new physicalgraph.device.HubAction(httpRequest)
    if (valid(requestId)) hubAction.requestId=requestId
    log.debug "$hubAction"
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