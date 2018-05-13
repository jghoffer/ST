
 
definition(
    name: "LG Manager",
    namespace: "Josh",
    author: "Josh",
    description: "LG Smart TV w/ Echo",
    category: "My Apps",
    iconUrl: "http://icons.iconarchive.com/icons/tribalmarkings/colorflow/64/lg-icon.png",
    iconX2Url: "http://icons.iconarchive.com/icons/tribalmarkings/colorflow/64/lg-icon.png",
    iconX3Url: "http://icons.iconarchive.com/icons/tribalmarkings/colorflow/64/lg-icon.png"
)


preferences 
{
	page(name:"firstPage", title:"LG TV Preferences", content:"firstPage", refreshTimeout:5)
	page(name:"televisionDiscovery", title:"LG TV Setup", content:"televisionDiscovery", refreshTimeout:5)
	page(name:"televisionAuthenticate", title:"LG TV Pairing", content:"televisionAuthenticate", refreshTimeout:5)
}

def parse(String description) 
{
	log.debug "Parsing '${description}'"  
}

mappings {
      path("/a") { action: [GET: "processA"] }
      path("/tv") { action: [GET: "processTV"] }
      path("/setup") { action: [GET: "displayData"] }
}

def processA(){
	def persType="Normal"
    return ["personality":persType]
}

def sendJSON(outputTxt){
    log.debug outputTxt
    return ["voiceOutput":outputTxt+"!"]
}

def displayData(){
	render contentType: "text/html", data: """<!DOCTYPE html><html><head><meta charset="UTF-8" /><meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0"/></head><body style="margin: 0;">${setupData()}</body></html>"""
}
def setupData(){
	log.info "Set up web page located at : ${getApiServerUrl()}/api/smartapps/installations/${app.id}/setup?access_token=${state.accessToken}"
    def result ="<div style='padding:10px'><i><b>var STappID = '${app.id}';<br>var STtoken = '${state.accessToken}';<br><br>device list = '${state.devList}';<br><br>TV device = '${state.tv}';<br>"
}

def processTV() {    
    log.debug "-Device command received-"
	def dev = params.Device ?: "undefined" 	//Label of device
	def op = params.Operator ?: "undefined"	//Operation to perform
    def param = params.Param ?: "undefined" 	//Other parameter (color)
    def cmd = params.Cmd ?: "undefined"
    
    if (cmd == "louder" || cmd == "quieter") dev = "volume" 
    if (cmd == "louder") op = "increase"
    if (cmd == "quieter") op = "decrease"
    
    log.debug "Dev: " + dev
    log.debug "Op: " + op
    log.debug "Param: " + param
    log.debug "Cmd: " + cmd
    def num = 0
    try {
    	num = param as int	
    } catch (e){ num = 0 }
    
    
    String outputTxt = ""
    if (state.devList.contains(dev)) {
        if (op == "status" || (op=="undefined" && param=="undefined" && num==0)) 
        	outputTxt = getReply (dev, "status", "", "") 
		else if (op == "events" || op == "event") {	
            def finalCount = num != 0 ? num as int : eventCt ? eventCt as int : 0
            if (finalCount>0 && finalCount < 10) outputTxt = getLastEvent(deviceList.devices?.find{it.label.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase() == dev.toLowerCase()}, finalCount) + "%3%"
            else if (!finalCount) { outputTxt = "You do not have the number of events you wish to hear specified in your Ask Alexa SmartApp, and you didn't specify a number in your request. %1%" }
        	else if (finalCount > 9) { outputTxt = "The maximum number of past events to list is nine. %1%" }
        }
        else outputTxt = getReply (dev, op, num, param)
	} 
    else { outputTxt = "I had some problems finding the attribute $dev . %1%" }
    sendJSON(outputTxt)
}

def getReply(dev, op, num, param){
	String result = ""
    
    try {
  		def STdevice = getChildDevice(tvId())
    	if (op=="status") {
            if (dev=="volume" || dev == "level" || dev=="switch") {
                def onOffStatus = STdevice.currentValue("switch")
                result = "The ${STdevice} is ${onOffStatus}"
                
                def level = STdevice.currentValue("level")
                result += onOffStatus == "on" && level ? ", and it's set to ${level}%" : ""
                log.trace result
            } else result = "The ${STdevice} is currently ${STdevice.currentValue(dev)}. "
        } else if (dev=="volume" || dev == "level" || dev=="switch") {
        	num = num < 0 ? 0 : num >99 ? 100 : num
            if (dev=="volume" || dev == "level" && num > 0) {
            	if (op == "increase" || op=="raise" || op=="up" || op == "decrease" || op=="down" || op=="lower") { 
            		def newValues = upDown(STdevice, dev, op, num)
                	num = newValues.newLevel
                	STdevice.setLevel(num)
                	result = newValues.msg
            	} else if (op == "undefined") {
                	STdevice.setLevel(num)
                	result = "I am setting the ${STdevice} to ${num}%. "
            	}
			}
            if ((dev=="power" || dev == "mute") && num==0 ) {
                if (op=="on" || op=="off"){
                	STdevice."$op"() 
                	result = "I am turning the ${STdevice} ${op}. "
                }
                if (op=="toggle") {
        			def oldstate = STdevice.currentValue("switch")
                    def newstate = oldstate == "off" ? "on" : "off"
        			STdevice."$newstate"()
                    result = "I am toggling the ${STdevice} from '${oldstate}' to '${newstate}'. "
                }
            }              
    	}
	} catch (e){ result = "I could not process your request for the '${dev}'."}// $e %1%" }
//    if (op=="status" && result && !result.endsWith("%")) result += "%2%"
//    if (op!="status" && result && !result.endsWith("%")) result += "%3%"
//    if (!result) result = "Sorry, I had a problem understanding your request. %1%"

    return result
}

def upDown(tv, dev, op, num){
    def numChange,newLevel, defMove, txtRsp = ""
    if (dev == "volume") defMove = 5
    if (dev == "channel") defMove = 1
	if (op == "increase" || op=="raise" || op=="up")  numChange = num == 0 ? defMove : num > 0 ? num : 0
    if (op == "decrease" || op=="down" || op=="lower") numChange = num == 0 ? -defMove : num > 0 ? -num  : 0
    if (dev == "volume") {
    	newLevel = 70 + numChange
        txtRsp = numChange > 0 ? "Volume up by $num. " :  "Volume down by $num. "
    }                         
    return [newLevel: newLevel, msg:txtRsp]
}

def tvId() {
  def deviceSettings = selectedTv.split("!")
  def ipAddressHex = deviceSettings[1]
  def ipAddress = convertHexToIP(ipAddressHex)
  log.debug "ipAddressHex = $ipAddressHex || ipAddress = $ipAddress"
  return "${ipAddressHex}:${convertPortToHex(8080)}"
}

def doPoll() {
	def timeOut = (pollingInterval != null && pollingInterval != "") ? pollingInterval : 30
	log.debug "next in: $timeOut"
    getChildDevice(tvId())?.refresh()
    state.lastPoll = now()
	runIn( timeOut, doPoll)
}

def backupCheck() {
    if (!state.lastPoll) state.lastPoll = 0
	def diff = (now() - state.lastPoll)
	log.debug "backup watching... deets-> diff:$diff || lastPoll $state.lastPoll || pb1000: ${pollingBackup * 1000}"    
    if (diff > (pollingBackup * 1000)) {
    		unschedule()
   			doPoll()
    }
	runIn( 3000, backupCheck)    
}


def appTouch(evt) {
	unschedule()
	doPoll()
    backupCheck()
}

def firstPage() {
    return dynamicPage(name:"firstPage", title:"Preferences", nextPage:"televisionDiscovery", refreshInterval:refreshInterval, uninstall: false){
    	section {
        	paragraph "STappID = '${app.id}'"+"STtoken = '${state.accessToken}'"
        }
	}
}

def televisionDiscovery() 
{
	log.trace "discovering..."
    int tvRefreshCount = !state.bridgeRefreshCount ? 0 : state.bridgeRefreshCount as int
    state.bridgeRefreshCount = tvRefreshCount + 1
    def refreshInterval = 3

    def options = televisionsDiscovered() ?: []
    def numFound = options.size() ?: 0

    if(!state.subscribe) {
    	log.debug "subscribing...?"
        subscribe(location, null, deviceLocationHandler, [filterEvents:false])    
        //subscribe(location, "ssdpTerm.urn:schemas-upnp-org:device:MediaRenderer:", ssdpHandler)
        state.subscribe = true
        log.debug "subscribing..."
    }

    // Television discovery request every 15 seconds
    if((tvRefreshCount % 5) == 0) {
        findTv()
    }

    return dynamicPage(name:"televisionDiscovery", title:"LG TV Search Started!", nextPage:"televisionAuthenticate", refreshInterval:refreshInterval, uninstall: true){
    
        section("Interval (defaults = 30)") {
			input "pollingInterval", "decimal", title: "Interval", required: false
        	input "pollingBackup", "decimal", title: "Backup Interval", required: false   
		}

        section("External Controllers:") {
        	input "muteButton", "capability.switch", multiple: false, title: "Mute Controller:", required: false 
        }
    
    	section("Please wait while we discover your LG TV. Discovery can take five minutes or more, so sit back and relax! Select your device below once discovered."){
            input "selectedTv", "enum", required:false, title:"Select LG TV (${numFound} found)", multiple:false, options:options
        }
    }
}

def televisionAuthenticate() 
{
	tvRequestPairingKey()
    
    return dynamicPage(name:"televisionAuthenticate", title:"LG TV Search Started!", nextPage:"", install:true){
        section("We sent an pairing request to your TV. Please enter the pairing key and click Done."){
        	input "pairingKey", "string", defaultValue:"DDTYGF", required:true, title:"Pairing Key", multiple:false
        }
    }
}

def ssdpHandler(evt) {
    def description = evt.description
    def hub = evt?.hubId

    def parsedEvent = parseEventMessage(description)
    parsedEvent << ["hub":hub]

    def devices = getDevices()
    String ssdpUSN = parsedEvent.ssdpUSN.toString()
    if (!devices."${ssdpUSN}") {
        devices << ["${ssdpUSN}": parsedEvent]
    }
}

Map televisionsDiscovered() 
{
	def vbridges = getLGTvs()
	def map = [:]
	vbridges.each {
    	log.debug "Discovered List: $it"
        def value = "$it"
        def key = it.value
        
        if (key.contains("!")) {
            def settingsInfo = key.split("!")
            def deviceIp = convertHexToIP(settingsInfo[1])
            value = "LG TV (${deviceIp})"
        }
        
        map["${key}"] = value
	}
	map
}

def installed() 
{
	log.debug "Installed with settings: ${settings}"
    
	initialize()
}

def updated() 
{
	log.debug "Updated with settings: ${settings}"

	initialize()
}

def initialize() 
{
	unsubscribe()
	//state.subscribe = false
        
    addDevice()

    log.debug "Application Initialized"
    log.debug "Selected TV: $selectedTv"
    
    def tv = getChildDevice(tvId())
    
    subscribe(app, appTouch)
    subscribe(muteButton, "switch", switchChange)
    subscribe(tv, "muted", muteChange)
    doPoll()
    backupCheck()
    
    if (!state.accessToken) OAuthToken()
    
    state.devList = ["volume","power","channel","mode","level"]
    state.tv = getChildDevice(tvId()).label
    
}

def OAuthToken(){
	try {
        createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) { log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth." }
}

def switchChange(evt) {
log.debug "switchChange $evt"
	if (evt.value == "off") 
    getChildDevice(tvId())?.setLevel(10) else 
	getChildDevice(tvId())?.setLevel(11)                 
}

def muteChange(evt) {
	log.debug "muteChange $evt"
	if (evt.value == "true") muteButton.on() else
    muteButton.off()
}

def getDevices() {
    if (!state.devices) { state.devices = [:] }
    log.debug("There are ${state.devices.size()} devices at this time")
    state.devices
}

def addDevice()
{ 
//  def deviceSettings = selectedTv.split("!")
//  def ipAddressHex = deviceSettings[1
//  def ipAddress = convertHexToIP(ipAddressHex)
    
//  def dni = "${ipAddressHex}:${convertPortToHex(8080)}"
	def dni = tvId()
	def d = getChildDevice(dni)

  if(!d) 
  {
  	log.debug "Hub: " + location.hubs[0].id
    addChildDevice("tv", "Josh's LG Smart TV", dni, deviceSettings[2], 
    	[
        	name: "LG Smart TV2", 
            label: "", 
            completedSetup: true,
            preferences: [
            	pairingKey: "$pairingKey",
            	televisionIp: "$ipAddress",
                //"ssdpUSN": ssdpUSN,
				//"ssdpPath": devices[ssdpUSN].ssdpPath
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

// Returns a list of the found LG TVs from UPNP discovery
def getLGTvs()
{
	state.televisions = state.televisions ?: [:]
}

// Sends out a UPNP request, looking for the LG TV. Results are sent to [deviceLocationHandler]
private findTv() 
{
	log.trace "findTV"
	sendHubCommand(new physicalgraph.device.HubAction("lan discovery urn:schemas-upnp-org:device:MediaRenderer:1", physicalgraph.device.Protocol.LAN))
}

// Parses results from [findTv], looking for the specific UPNP result that clearly identifies the TV we can use
def deviceLocationHandler(evt) 
{
	log.debug "Device Location Event: $evt.description"
	def upnpResult = parseEventMessage(evt.description)
    def parsedEvent = upnpResult
    log.debug "upnp: $upnpResult"
    
    def hub = evt?.hubId
    log.debug "hub: $hub"
    
    if (upnpResult?.ssdpTerm?.contains("schemas-upnp-org:device:MediaRenderer:")) {
        log.debug "Found TV: ${upnpResult}"
        state.televisions << [device:"${upnpResult.mac}!${upnpResult.ip}!${hub}"]

        def devices = getDevices()

        if (!(devices."${parsedEvent.ssdpUSN.toString()}")) { //if it doesn't already exist
            //log.debug('Parsed Event: ' + parsedEvent)
            devices << ["${parsedEvent.ssdpUSN.toString()}":parsedEvent]
        } else { // just update the values
            def d = devices."${parsedEvent.ssdpUSN.toString()}"
            boolean deviceChangedValues = false

            if(d.ip != parsedEvent.ip || d.port != parsedEvent.port) {
                d.ip = parsedEvent.ip
                d.port = parsedEvent.port
                deviceChangedValues = true
            }

            if (deviceChangedValues) {
                def children = getChildDevices()
                children.each {
                    if (it.getDeviceDataByName("ssdpUSN") == parsedEvent.ssdpUSN) {
                        //it.subscribe(parsedEvent.ip, parsedEvent.port)
                    }
                }
            }

        }
}
}

// Display pairing key on TV
private tvRequestPairingKey()
{
	log.debug "Display pairing key"
    
    def deviceSettings = selectedTv.split("!")
    def ipAddressHex = deviceSettings[1]
    def ipAddress = convertHexToIP(ipAddressHex)
    
    def reqKey = "<?xml version=\"1.0\" encoding=\"utf-8\"?><auth><type>AuthKeyReq</type></auth>"
  
    def httpRequest = [
      	method:		"POST",
        path: 		"/roap/api/auth",
        body:		"$reqKey",
        headers:	[
        				HOST:			"$ipAddress:8080",
                        "Content-Type":	"application/atom+xml",
                    ]
	]

	def hubAction = new physicalgraph.device.HubAction(httpRequest)
	sendHubCommand(hubAction)
}

private def parseEventMessage(String description) 
{
	log.debug description
	def event = [:]
	def parts = description.split(',')
	parts.each { part ->
		part = part.trim()
		if (part.startsWith('devicetype:')) {
			def valueString = part.split(":")[1].trim()
			event.devicetype = valueString
		}
		else if (part.startsWith('mac:')) {
			def valueString = part.split(":")[1].trim()
			if (valueString) {
				event.mac = valueString
			}
		}
		else if (part.startsWith('networkAddress:')) {
			def valueString = part.split(":")[1].trim()
			if (valueString) {
				event.ip = valueString
			}
		}
		else if (part.startsWith('ssdpPath:')) {
			def valueString = part.split(":")[1].trim()
			if (valueString) {
				event.ssdpPath = valueString
			}
		}
		else if (part.startsWith('ssdpUSN:')) {
			part -= "ssdpUSN:"
			def valueString = part.trim()
			if (valueString) {
				event.ssdpUSN = valueString
			}
		}
		else if (part.startsWith('ssdpTerm:')) {
			part -= "ssdpTerm:"
			def valueString = part.trim()
			if (valueString) {
				event.ssdpTerm = valueString
			}
		}
        else if (part.startsWith('ssdpPath:')) {
            def valueString = part.split(":")[1].trim()
            if (valueString) {
                event.ssdpPath = valueString
			}
		}
        
    }    

	event
}

private Integer convertHexToInt(hex) 
{
	Integer.parseInt(hex,16)
}

private String convertHexToIP(hex) 
{
	[convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}

private String convertIPtoHex(ipAddress) 
{ 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    trace("IP address entered is $ipAddress and the converted hex code is $hex")
    return hex
}

private String convertPortToHex(port) 
{
	String hexport = port.toString().format( '%04X', port.toInteger() )
    return hexport
}