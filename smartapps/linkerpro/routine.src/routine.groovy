definition(
    name: "Routine",
    namespace: "linkerPro",
    author: "Your Name",
    description: "A simple app to control basic lighting automations. This is a child app.",
    category: "My Apps",

    // the parent option allows you to specify the parent app in the form <namespace>/<app name>
    parent: "linkerPro:Manager",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    page(name: "mainPage")
    page(name: "timePage")
    page(name: "configurePage")
    page(name: "effectPage")
    page(name: "limitPage")
}


def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unschedule()
    initialize()
}

def initialize() {
    if (!customTitle) app.updateLabel("[$parent.label] ${defaultLabel()}")
    else app.updateLabel("[$parent.label] $customTitle")

    subscriber()
}

def subscriber() {
	unsubscribe()
	if (physical) {
    	subscribe(physical, "switch", "handler")
       	if (syncDim) subscribe(physical, "level", "handler") 
        if (colorSlave) subscribe (physical, "color", "handler")      
    }    
    if (movement) subscribe(movement, "motion", "handler")  
	if (contact) subscribe(contact, "contact", "handler")
    if (presence) subscribe(presence, "presence", "handler")
    if (vibration) subscribe(vibration, "acceleration", "handler")
    if (modeChange) subscribe(location, "handler")  
    if (specialSwitch) subscribe(specialSwitch, "status", "handler") 
 	if (meter) subscribe(meter, "power", "handler")
    if (dimRange) subscribe(dimRange, "level", "handler")
    if (onColor) subscribe(onColor, "color", "handler")
    if (temp) subscribe(temp, "temperature", "handler")
    if (time) schedule(runTime,timeHandler, [data:[time:time]])
    
    if (slave && offCallback) { slave.each() { subscribe(it, "switch.off", "callbackHandler")} }
}

def mainPage() {
    dynamicPage(name: "mainPage", install: true, uninstall: true)  {
        section ("Assignments") {
    			href(name:"toNextPage", page:"configurePage", description: configureFormat(),title: "Cause")
                href(name:"toNextPage", page:"effectPage", description: eventFormat(),title: "Effects")
                href(name:"toNextPage", page:"limitPage", description: limitFormat(),title: "Limits") 
        }
        section ("App Name") {        
			input("customTitle","text",title:"",description:"custom title",required:false, submitOnChange: true)
		}
    }
}

def configureFormat() {
            	def working = "["
            	["physical", "n",
                 "dimRange","dimAbove","dimBelow","nn",
                 "onColor","nn",
                 "movement","contact","presence","vibration","meter","temp","nn",
                 "modeChange","nn","buttonSwitch","buttonValue","n",
                 "specialSwitch","specialValue","n"].each{
                	def inp = settings[it]
                	if (inp) working = working +"\n$it: $inp"
                    if (it == "nn") working = working +"\n\n"
                }
                working = working+"]"
                int index = 0
  				while (index != -1) {
                	working = working.replaceAll('\n\n\n\n',"\n\n")
                    working = working.replaceAll('\n\n\n',"\n\n")
                    working = working.replaceAll('\\[\n',"[")
                    working = working.replaceAll('\n\\]',"]")
    				index = working.indexOf('\n\n\n', index+1);          
    				if (index == -1) index = working.indexOf("[\n", 0);  
                    if (index == -1) index = working.indexOf("\n]", 0);
  				}
                
                working = working.replaceAll("physical:", "Switch:")
				return (working.size() > 0) ? working[1..-1].replaceAll('\\[',"").replaceAll('\\]',"") : "(none)"
}      
def eventFormat() {
            	def working = "["
            	["slave", "colorSlave","nn",
                "On",
                "virtualOn","n","flipOn","n",
                "mimicOn", "masterOn","n",
                "dimOn","dimValueOn","dimMasterOn","n",
                "colorOn","colorValueOn","colorMasterOn", "n",
                "setModeOn","n",
                "statusOn", "statusValueOn","n",  
                "phraseOn","msgOn","phoneOn", "messageOn","nn",
                "Off",
                "virtualOff","n","flipOff","n",
                "mimicOff", "masterOff","n",
                "dimOff","dimValueOff","dimMasterOff","n",
                 "colorOff","colorValueOff","colorMasterOff", "n",
                 "setModeOff","n",
                 "statusOff", "statusValueOff","n", 
                 "phraseOff","msgOff","phoneOff","messageOff"].each{
                	def inp
                    if (it == "nn") working = working +"|n|" else
                    if (it == "n") working = working +"\n" else
                    if (it == "On"){
                    	working = working +"|On|\n"
                    } else
                    if (it == "Off") {                    	
                    	working = working +"|Off|\n" 
                    } else {
                    	inp = settings[it]
                        if (inp) working = working +"$it: $inp  "
                    }
                }
                log.debug working
                working = working+"]"
                working = working.replaceAll('slave:',"Sync").replaceAll('virtualOn',"Turn on").replaceAll('virtualOff',"Turn off").replaceAll('flipOn',"Turn off").replaceAll('flipOff',"Turn on")
                
                log.trace working
                int index = 0
  				while (index != -1) {
                	working = working.replaceAll('\n\n',"\n")
    				index = working.indexOf('\n\n', index+1);
                    //log.trace "first $index"
  				}
                working = working.replaceAll('\\|n\\|',"\n\n")
                if (waitOnSecs) working = working +"\n(wait: $waitSecs seconds)" 
                else if (waitOn) working = working +"\n(wait: $waitOn minutes)"
                log.debug working
                
                index = 0
                while (index != -1) {
                	working = working.replaceAll('\\[\n',"[")
                    working = working.replaceAll('\n\\]',"]")
                    working = working.replaceAll('\n\n\n\\|O',"\n\n\\|O")
                    working = working.replaceAll('\\|On\\|\n\\|Off\\|',"|Off|")
                    working = working.replaceAll('\\|On\\|\n\n\\|Off',"|Off")
                    working = working.replaceAll('\\|On\\|\n\n',"\\|On\\|\n")
                    working = working.replaceAll('\\|Off\\|\n\n',"\\|Off\\|\n")
               		working = working.replaceAll('\\|Off\\|\n\\]',"]")
                    working = working.replaceAll('\\|Off\\|\\]',"]")
                    working = working.replaceAll('Off\\|\\]',"]")
                    working = working.replaceAll('Off\\|\n\\]',"]")
    				index = working.indexOf("[\n", 0)  
                    if (index == -1) index = working.indexOf("\n]", 0);  
                   // log.trace "second $index"
				}
                
                log.trace working
                working = working.replaceAll('On:',":").replaceAll('Off:',":")
                working = working.replaceAll('master:', "-> ").replaceAll('dimMaster:', "-> ").replaceAll('colorMaster:', "-> ").replaceAll('statusValue:', "-> ")
				return (working.size() > 0) ? working[1..-1].replaceAll('\\[',"").replaceAll('\\]',"") : "(none)"
}
def limitFormat() {
	def working = ""
    
    if (onGate) working = working +"\n\nIf any on: $onGate"
    if (offGate) working = working +"\n\nIf all off: $offGate"
	if (openContact) working = working +"\n\nIf open: $openContact"
	if (closedContact) working = working +"\n\nIf closed: $closedContact"
	if (activeMotion) working = working +"\n\nIf motion: $activeMotion"
	if (inactiveMotion) working = working +"\n\nIf no motion: $inactiveMotion"
	if (presencePresent) working = working +"\n\nIf present: $presencePresent"
	if (presenceNotPresent) working = working +"\n\nIf not present: $presenceNotPresent"
    if (modes) working = working +"\n\nIf mode is: $modes"
    if (fromTime && toTime) working = working +"\n\nIf time is between: ${timeFormat()}"
             	
    return (working.size() > 0) ? working[2..-1].replaceAll('\\[',"").replaceAll('\\]',"") : "(none)"
}

private hideSection(lead) {
  def res = null
  switch(lead) { 
  	  case "switch":	res = physical ||specialSwitch || dimRange || onColor 
      						break
      case "sensors": 	res=movement || contact || meter || 
      						presence || vibration ||
      						meterValue || temp
                        	break  
                            //
      case "event": 	res=modeChange || time     						 
							break  
      case "slave": 	res=slave || colorSlave 
                        	break
      case "on": 		res=virtualOn || flipOn || 
      						mimicOn || masterOn ||
     					    dimOn || dimValueOn || dimMasterOn ||
                            colorOn || colorValueOn || colorMasterOn ||
                            statusOn || statusValueOn ||  
                            phraseOn || msgOn || messageOn
      						break                            
      case "specOn":    res=indicatorOn ||
      						setModeOn || flashOn || 
      						
                            indicatorOn || buttonDisplayOn || goDisplayOn ||
                            goButtonOn || ledOn
                        	break 
                            
      case "off": 		res=virtualOff || flipOff ||  
      						mimicOff || masterOff ||
      						dimOff || dimValueOff || dimMasterOff ||
                            colorOff || colorValueOff || colorMasterOff ||
                            statusOff || statusValueOff ||
                            phraseOff|| msgOff || messageOff
                            break                                                     
      case "specOff":    res=indicatorOff ||
      						setModeOff || flashOff || 
      						
                            indicatorOff || buttonDisplayOff || goDisplayOff ||
                            goButtonOff || ledOff 
                        	break  
                            
	  case "limits": 	res=onGate || offGate ||
     						openContact || closedContact || 
                            activeMotion || inactiveMotion ||
                            presencePresent || presenceNotPresent ||
  							modes || fromTime || toTime
                        	break
  }                      
  
  return res ? false : true
}

def configurePage() {
	dynamicPage(name: "configurePage", title: "Causes:", nextPage: "mainPage", uninstall: false) {
		section("switch", hideable:true, hidden: hideSection("switch")) {
		    input "physical", "capability.switch", title: "", description: "Switch", multiple: true, required: false, submitOnChange: true
    		if (physical) {
 				input "hardOn", "bool", title: "(hard push?)", required: false
			}
    		input "dimRange", "capability.switch", title: "Dimmer", multiple: true, required: false, submitOnChange: true
    		if (dimRange) {    
    			input "dimAbove", "number",  title: "", description: "Above", required: false
        		input "dimBelow", "number",  title: "", description: "Below", required: false
 			}
            input "onColor", "capability.colorControl", title: "Color Change", multiple: true, required: false, submitOnChange: true
            input "specialSwitch", "capability.switch", title: "Special switch", multiple: false, required: false, submitOnChange: true         
			if (specialSwitch) input "specialValue", "text", title: "", description:"Answer", required: true
        }    
 
		section(title: "Sensor", hideable:true, hidden: hideSection("sensors")) {
        	input "movement", "capability.motionSensor", title: "Movement sensor", multiple: true, required: false
			input "contact", "capability.contactSensor", title: "Contact sensor", multiple: true, required: false
    		input "presence", "capability.presenceSensor", title: "Presence sensor", multiple: true, required: false
    		input "vibration", "capability.accelerationSensor", title: "Vibration sensor", multiple: true, required: false       
	   		input "meter", "capability.powerMeter", title: "Power", multiple: true, required: false, submitOnChange: true 
			if (meter) {
				input "meterValue", "number",  title: "", description: "Threshold", required: true
			}
    		input "temp", "capability.temperatureMeasurement", title:  "Temperature Sensor", multiple: true, required:false, submitOnChange: true 
   			if (temp) {
    			input "tempValue", "number", title: "", description: "Target", required: false
    		}
        }
		section(title: "Events", hideable:true, hidden: hideSection("events")) {       
            input "modeChange", "mode", title: "Mode change", multiple: false, required: false 
            
            input "time", "time", title: "Time", required: false
        }    

	}        
}


def effectPage() {
	dynamicPage(name: "effectPage", title: "Effects:", nextPage: "mainPage", uninstall: false) {
    
    	section() {
			input "slave", "capability.switch", title: "Sync", multiple: true, required: false, submitOnChange: true
			if (slave) {
    			input "syncDim", "bool", title: "Dim?", required: false, submitOnChange: true  
    			if (syncDim) input "dimOnly", "bool", title: "(dim only?)", required: false 
    			input "offCallback", "bool", title: "Callback (on off())?", required: false, submitOnChange: true  
        		if (offCallback) input "offCallbackAny", "bool", title: "...even just one?", required: false          
     		}
			input "colorSlave", "capability.colorControl", title: "Colors?", multiple: true, required: false, submitOnChange: true 
		}
    
		section(title: "On", hideable:true, hidden: hideSection("on")) {    
			input "virtualOn", "capability.switch", title: "", description: "On --> On()", multiple: true, required: false
   			input "flipOn", "capability.switch", title: "", description:"On --> Off()", multiple: true, required: false
    		input "mimicOn", "capability.switch",  title: "", description:"On --> Mimic", multiple: true, required:false, submitOnChange: true
    		if (mimicOn) input "masterOn", "capability.switch",  title: "", description: "Master switch:", required: true, multiple: false 
   
   			input "dimOn", "capability.switchLevel", title: "Set dimmer", multiple: true, required: false, submitOnChange: true
    		if (dimOn) {
   				input "dimOnlyOn", "bool", title: "(dim only?)", required: false  
				input "dimValueOn", "text",  title: "", description: "Dim value:", required: false
				input "dimMasterOn", "capability.switchLevel",  title: "", description: "Dim master:", required: false         
    		}
			input "colorOn", "capability.colorControl", title: "Set color:", multiple: true, required: false, submitOnChange: true
    		if (colorOn) {
           		input "colorOnlyOn", "bool", title: "(color only?)",  submitOnChange: true, required: false  
                if (colorOnlyOn) input "colorIgnoreOn", "bool", title: "(ignore off?)", submitOnChange: true, required: false  

				input "colorValueOn", "text",  title: "", description: "color value:", required: false
				input "colorMasterOn", "capability.colorControl", title: "Color master:", multiple: false, required: false, submitOnChange: true        
    		}    
    
    		input "statusOn", "capability.switch", title: "Set status", multiple: true, required: false, submitOnChange: true
    		if (statusOn) input "statusValueOn", "text",  title: "", description: "status value:", required: false
            
            input "setModeOn", "mode",  title: "", description: "Change mode to:", multiple: false, required: false, submitOnChange: true
			def phrases = location.helloHome?.getPhrases()*.label
			input "phraseOn", "enum",  title: "", description: "Activate routine:", required: false, options: phrases, submitOnChange: true
    		input "msgOn", "text", title: "Message", description: "Internal", required: false, submitOnChange: true
    		if (msgOn) {	
  				input "msgPushOn", "bool", title: "Alert?", required: false, submitOnChange: true 
  				input "msgFeedOn", "bool", title: "Feed?", required: false, submitOnChange: true      
			}

    		input "messageOn", "text", title: "", description: "SMS", required: false, submitOnChange: true
    		if (messageOn) input "phoneOn","phone" ,title: "", required: false  
            
    	}
        
        section(title: "Off", hideable:true, hidden: hideSection("off")) {    
      
        	input "virtualOff", "capability.switch", title: "", description:"Off --> Off()", multiple: true, required: false 
			input "flipOff", "capability.switch", title: "", description:"Off --> On()", multiple: true, required: false

		    input "mimicOff", "capability.switch",  title: "", description:"Off --> Mimic", multiple: true, required:false, submitOnChange: true    
    		if (mimicOff) input "masterOff", "capability.switch",  title: "", description: "Master switch:", required: true, multiple: false       

    		input "dimOff", "capability.switchLevel", title: "Set dimmer", multiple: true, required: false, submitOnChange: true
    		if (dimOff) {
   				input "dimOnlyOff", "bool", title: "(dim only?)", required: false  
				input "dimValueOff", "text",  title: "", description: "Dim value:", required: false
				input "dimMasterOff", "capability.switchLevel",  title: "", description: "Dim mimic:", required: false        
    		}
			input "colorOff", "capability.colorControl", title: "Set color", multiple: true, required: false, submitOnChange: true
    		if (colorOff) {
                input "colorOnlyOff", "bool", title: "(color only?)",  submitOnChange: true, required: false  
                if (colorOnlyOff) input "colorIgnoreOff", "bool", title: "(ignore off?)", submitOnChange: true, required: false  
                
				input "colorValueOff", "text",  title: "", description: "color value:", required: false
				input "colorMasterOff", "capability.colorControl", title: "Color master:", multiple: false, required: false, submitOnChange: true        
    		}    
            
			input "statusOff", "capability.switch", title: "Set status", multiple: true, required: false, submitOnChange: true
    		if (statusOff) input "statusValueOff", "text",  title: "", description: "status value:", required: false
                
            input "setModeOff", "mode",  title: "", description: "Change mode to:", multiple: false, required: false, submitOffChange: true
			def phrases = location.helloHome?.getPhrases()*.label
			input "phraseOff", "enum",  title: "", description: "Activate routine:", required: false, options: phrases, submitOnChange: true
    		input "msgOff", "text", title: "Message", description: "Internal", required: false, submitOnChange: true
    		if (msgOff) {	
  				input "msgPushOff", "bool", title: "Alert?", required: false, submitOnChange: true 
  				input "msgFeedOff", "bool", title: "Feed?", required: false, submitOnChange: true      
			}

    		input "messageOff", "text", title: "", descriptiOff: "SMS", required: false, submitOnChange: true
    		if (messageOff) input "phoneOff","phOffe" ,title: "", required: false              
    	}
        
        section("Delay") {
    		if (!waitOnSecs) input "waitOn", "number", title: "Minutes:", description: " ", required: false, submitOnChange: true
            input "waitOnSecs", "bool", title: "Seconds:", required: false, submitOnChange: true
            if (waitOnSecs) input "waitSecs", "number", title: "", description: " ", required: false, submitOnChange: true
        }    
    }
}    


def limitPage() {
	dynamicPage(name: "limitPage", title: "Limits:", nextPage: "mainPage", uninstall: false) {
		section("Switch") {
			input "onGate", "capability.switch", title: "Only if one of these is ON:", multiple: true, required: false
			input "offGate", "capability.switch", title: "Only if ALL of these are OFF:", multiple: true, required: false
        }
		section("Sensors") {
	   		input "openContact", "capability.contactSensor", title: "Only if one of these is OPEN:", multiple: true, required: false
    		input "closedContact", "capability.contactSensor", title: "Only if ALL of these are CLOSED:", multiple: true, required: false
    		input "activeMotion", "capability.motionSensor", title: "Only if one of these is ACTIVE", multiple: true, required: false
    		input "inactiveMotion", "capability.motionSensor", title: "Only if ALL of these are INACTIVE", multiple: true, required: false
    		input "presencePresent", "capability.presenceSensor", title: "Only if ALL of these are PRESENT", multiple: true, required: false
    		input "presenceNotPresent", "capability.presenceSensor", title: "Only if ALL of these are NOT PRESENT", multiple: true, required: false
		}
		section("State") {        
            href(name:"toTimePage", page:"timePage", title: "Only if BETWEEN", description: timeFormat())
            input "modes", "mode", title: "Only if in one of these MODES:", multiple: true, required: false 
		}
    }
}

def timePage() {
	dynamicPage(name: "timePage", title: "Time", install: false, uninstall: false, nextPage: "limitPage") {
	section {
		input "fromTime", "time", title:"", description: "From", required: false//, submitOnChange: true
	    input "toTime", "time", title:"", description: "To", required: false//, submitOnChange: true
    }
}	}

def timeFormat() {
	if (!fromTime || !toTime) return "none" else
	return new Date().parse( "yyyy-MM-dd'T'HH:mm:ss.SSSX", fromTime).format('hh:mm',location.timeZone) + ' and ' +
    new Date().parse( "yyyy-MM-dd'T'HH:mm:ss.SSSX", toTime).format('hh:mm',location.timeZone)
}


def defaultLabel() {
    def causes = []
    def effects = []
    ["physical","specialSwitch","buttonSwitch","dimRange","onColor","movement","contact","presence","vibration","meter","temp"].each{ causeType ->
        if (settings["$causeType"]) causes << settings["$causeType"].join(", ")
    } 
    if (settings["modeChange"]) causes << settings["modeChange"]
    
    ["slave","color","virtualOn","flipOn","mimicOn","dimOn","colorOn","virtualOff","flipOff","mimicOff","dimOff","colorOff"].each{ effectType ->
    	log.debug "list $effectType and ${settings["$effectType"]}"
        if (settings["$effectType"]) effects << settings["$effectType"].join(", ")
    }    
    ["setModeOn","statusValueOn","msgOn","messageOn","setModeOff","statusOff","msgOff","messageOff"].each{ effectType ->
    	if (settings["$effectType"]) effects << '"' + settings["$effectType"] + '"'
    }
    
    def outp = causes.join(", ")  
    //if (causes.size() > 0) outp = outp + "\n" + causes.join(", ")    
    if (effects.size() > 0) outp = outp + "\n" + effects.join(", ")
   
    return outp.replaceAll('\\:',"").replaceAll('\\.',"").replaceAll('\\>',"").replaceAll('\\=',"")
}    

def colorValue(str) {
	switch(str) {
				case "Blue":
					return 65
					break;
				case "Red":
					return 3
					break;
				case "Yellow":
					return 15
					break;
				case "Green":
					return 32
					break;
				case "White":
					return 0
					break;                    
				case "Purple":
					return 90
					break;     
				case "Orange":
					return 11
					break;  
                default:
					return
					break;  
			}	
}


def gateCheck(){
    def isMode = !modes || modes.contains(location.mode)
    
    def between = ((fromTime) && (toTime)) ? timeOfDayIsBetween(fromTime, toTime, new Date(), location.timeZone) : true
    
	def onGatePass = true
	if (onGate) {
  		onGatePass = onGate?.any { it.currentValue('switch').contains("on") } 
    }
	def offGatePass = true
	if (offGate) {
  		offGatePass = !(offGate?.any { it.currentValue('switch').contains("on") })
    }
	def openGatePass = true
	if (openContact) {
  		openGatePass = openContact?.any { it.currentValue('contact').contains("open") } 
    }    
    def closedGatePass = true
	if (closedContact) {
  		closedGatePass = !(closedContact?.any { it.currentValue('contact').contains("open") }) 
    }        
    def activeGatePass = true
	if (activeMotion) {
  		activeGatePass = !(activeMotion?.any { it.currentValue('motion').contains("inactive") } )
    }      
    def inactiveGatePass = true
	if (inactiveMotion) {
  		inactiveGatePass = !(inactiveMotion?.any { (it.currentValue('motion') == "active") } )
    }    
    def presentGatePass = true
	if (presencePresent) {
  		presentGatePass = !(presencePresent?.any { (it.currentValue('presence') == "not present") } )
    }    
    def notPresentGatePass = true
	if (presenceNotPresent) {
  		notPresentGatePass = !(presenceNotPresent?.any { (it.currentValue('presence') == "present") } )
    }    
    
    def report = "onGatePass $onGatePass && offGatePass $offGatePass && openGatePass $openGatePass && closedGatePass $closedGatePass" +
    			"&& activeGatePass $activeGatePass && inactiveGatePass $inactiveGatePass && presentGatePass $presentGatePass && notPresentGatePass $notPresentGatePass " +
            	"|| isMode $isMode && between $between"
    log.trace report
    return onGatePass && offGatePass && 
    	openGatePass && closedGatePass && 
        activeGatePass && inactiveGatePass &&
        presentGatePass && notPresentGatePass &&
        isMode && between
}

def handler(evt) {
	def secs = waitOnSecs ? waitSecs : (waitOn ? waitOn*60 : 0)
	//def secs = waitOn ? waitOn*60 : 0

    if (gateCheck())
    	runIn(secs, runStuff, [data:[value:evt.value, name:evt.name, type:evt.type, displayName:evt.displayName,stringValue:evt.stringValue]])
}


def timeHandler(time) {
	//def timeEvent = 
	runStuff(createEvent(name: "time", value: "time"))
}

def runStuff(evt) { 
	log.debug "$evt.value"
	if (gateCheck()) {

	def val = evt.value
    def cat = evt.name
    def typ = evt.type
    def dev = evt.displayName
    def desc = evt.stringValue
    
    log.trace "${cat} is calling ${val}"
    
    	// 			SYNC BLOCK		//       
    if (cat == "level" && syncDim) {
        def dims = slave
        log.debug "making it dim to ${dims}"
      	if (dimOnly) {
            dims?.each() {if (it.currentValue('switch')?.contains('on')) 
            {it.setLevel(evt.value)}}
        } else dims?.setLevel(evt.value) 
      	log.debug "${dims*.currentValue('switch')}"
    }  
    
    if (cat == "color" && colorSlave) {
        def hue
        def saturation
 		physical.each() {
            hue = 	it.currentValue("hue")
        	saturation = it.currentValue("saturation")
        }          
        log.trace "final hue | saturation is $hue | $saturation --  value is $val"
        Map colorMap =  [hue: hue, saturation: saturation, hex: val]
	 	
        colorSlave.each() {if (it.currentValue('switch').contains('on')) {    	
 		  it.setColor(hex: val)
		//  it.setHue(hue)
       	//  it.setSaturation(saturation)
        }  }
  	}    
	// 			SYNC BLOCK		//  
    
    if (cat == "switch") {cat = (!hardOn || typ == "physical") ? "OnOff" : "ignore"}
    if (cat == "status") val = (specialValue == val) ? "On" : "ignore"

    if (cat == "mode") val = (val==modeChange) ? "On" : "Off"  

    if (cat == "level" && dimRange) {
    	def valInt = val as Integer
        desc = valInt as String
        val = ((valInt >= dimAbove || !dimAbove) && (valInt <= dimBelow || !dimBelow)) ? "On" : "Off" 
        cat = "OnOff"
    }
       
    if (cat == "color" && onColor) {
    	val = "On"
        cat = "OnOff"
    }
    
   
    if (cat == "power") {
    	def valInt = val as Integer
        desc = valInt as String
		val = (meterValue > valInt)  ? "Off" : "On"  
    }

    if (cat == "temperature") {
    	def valInt =  Math.round(evt.numericValue)
        desc = valInt as String
        if (!tempValue) val = "On" else
		val = (tempValue > valInt)  ? "Off" : "On"  
    }
    
    cat = (["motion","contact","presence","acceleration","power","status","mode", "temperature"].contains(cat)) ? "OnOff" : cat
     
    val = (["active", "open", "present","time"].contains(val)) ? "on" : val 
    val = (["inactive", "closed", "not present"].contains(val)) ? "off" : val 
  
    if (val=="on") val="On"
    if (val=="off") val="Off"    

	log.trace "Formated as: ${cat} is calling ${val}" 
    
    if (cat == "OnOff" && ["On","Off"].contains(val)) {    
   		if (slave && !dimOnly) {if (val=="On") {slave*.on()} else {slave*.off()}}
  
	    def switches = settings["virtual$val"]
    	def flips = settings["flip$val"]
    	def mimics = settings["mimic$val"]
    	def master = settings["master$val"]
    	def dims = settings["dim$val"]
    	def colors = settings["color$val"]
    
    
    	if (val == "On" && switches) switches*.on() 
        if (val == "Off" && flips) flips*.on() 
        if (mimics) { if (master*.currentValue('switch').contains("on")) { mimics*.on() } }
        
        if (dims) {
       		def dimVals = settings["dimValue$val"]
        	if (!dimVals) dimVals = settings["dimMaster$val"]?.currentValue('level') 
            
            dims?.each() {
            	if ((it.currentValue('switch')?.contains('on')) || (!settings["dimOnly$val"])) {
                	if ((dimVals.toString().contains("+")) || (dimVals.toString().contains("-"))) {
                    	def curDim = it.currentValue('level') as Integer
                		def newDim = dimVals as Integer
                        if ((curDim + newDim) < 0) newDim = 0
                        log.debug "dimVals $dimVals = newDim $newDim + curDim $curDim and ${dimVals.contains("-")} or ${dimVals.contains("+")}"
						dimVals = curDim + newDim
                    }   
                    it.setLevel(dimVals)                    
                }
			}  
        }
        
       // def color = settings["color$val"]

		if (colors)   {
            def hueColor
        	def saturation
            def colorTemp = 3500
			def colorVal = settings["colorValue$val"]
            log.debug "$colors is $colorVal"
        	if (colorVal) {
            	saturation = 100
                hueColor = colorValue(colorVal)
                if (colorVal == "White") saturation = 10
                log.debug "hue is $hueColor" 
            } else {
        		def colorMaster = settings["colorMaster$val"]
                if (colorMaster) {
            		hueColor = colorMaster.latestValue("hue")
            		saturation = colorMaster.latestValue("saturation")
                    if (colorMaster.hasAttribute("colorTemperatur"))
                    	colorTemp = colorMaster.latestValue("colorTemperature")
            	}
        	}
            log.trace "colors are ${colors.currentValue('switch')} and $saturation"
            log.trace "hex = ${colorUtil.hslToHex((hueColor / 3.6) as int, (saturation) as int)}"
            log.debug "hex = ${colorUtil.hslToHex((hueColor) as int, (saturation) as int)}" 
            
            colors.each() {if ((it.currentValue('switch').contains('on')) || (!settings["colorIgnore$val"])) {
            	//log.debug "the problem is $it"
                if (it.hasCommand("setColorTemperature"))
                	if (it.currentValue("colorTemperature") != colorTemp)
               		it.setColorTemperature(colorTemp)        
                if (it.hasCommand("setColor")) //it.setColor([hue:hueColor, saturation:saturation, hex:colorUtil.hslToHex((hueColor / 3.6) as int, (saturation) as int)])
                	it.setColor([hue:hueColor, saturation:saturation, hex:colorUtil.hslToHex((hueColor) as int, (saturation) as int)])
                if (it.hasCommand("setHue")) {
         			it.setHue(hueColor)                
                	it.setSaturation(saturation)
                }     
	            if (!settings["colorOnly$val"]) it.on()
            } }
        } 
        
        if (mimics) { if (master*.currentValue('switch').contains("off"))   
        	mimics*.off() }

		if (val == "Off" && switches) switches*.off()
        if (val == "On" && flips) flips*.off()
        
        
        // NON-LIGHTING
        
        def status = settings["status$val"]
        if (status) {
        	def statusVal = settings["statusValue$val"]
        	status.each() {
            	if (it.hasCommand("setStatus")) {status.setStatus("$statusVal") }	
            }
        }
        
        def indicator = settings["indicator$val"] 
        if (indicator) { 
        	def indicatorSet = settings["indicatorSet$val"] 
        	indicator.each() {
         		def curVal = it.currentValue('switch')
            		log.trace "indicator current value = $curVal | indicatorSet = $indicatorSet"
            	if (indicatorSet) {
            		if (curVal == "on") it.indicatorWhenOn()
                	if (curVal == "off") it.indicatorWhenOff()
            	} else {
            		if (curVal == "on") it.indicatorWhenOff()
                	if (curVal == "off") it.indicatorWhenOn()
            	}
        	}	
        }
                   
        def setMode = settings["setMode$val"]
        if (setMode) setLocationMode(setMode)

 		def phrase = settings["phrase$val"]
        if (phrase) location.helloHome.execute(phrase)
        
        def msg = settings["msg$val"]
        if (msg) {
        	if (settings["msgPush$val"]) sendPushMessage("$msg")
            if (settings["msgFeed$val"]) sendNotificationEvent("$msg")
        }    
              
        def phoneMsg = settings["message$val"]
        if (phoneMsg) {
        	if (phoneMsg == "Stat") phoneMsg = "$dev is $desc" 
			sendSmsMessage(settings["phone$val"] ?: "(202) 679-2566", phoneMsg)
		}
    }    
}	}

def callbackHandler(evt) {
    if (physical && slave) {
		def allOff = !(slave?.any { it.currentValue('switch').contains("on") })
		if (allOff || offCallbackAny) physical*.off()
	}
}
