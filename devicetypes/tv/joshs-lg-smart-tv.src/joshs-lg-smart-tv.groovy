/** LG Smart TV Device Type  */
 
metadata {
	definition (name: "Josh's LG Smart TV", namespace: "tv", author: "Josh") 
    {
		capability "TV"
        capability "Switch Level"
        capability "Switch"
        capability "Music Player"
        capability "Refresh"
        
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
        
        command "rwd"
        command "fwd"
        command "resetPointer"
	}
 
    
    preferences {
        input("televisionIp", "string", title:"Television IP Address", description: "Television's IP address", required: true, displayDuringSetup: false)
        input("pairingKey", "string", title:"Pairing Key", description: "Pairing key", required: true, displayDuringSetup: false)
	}

	simulator 
    {
		// TODO: define status and reply messages here
	}


}

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