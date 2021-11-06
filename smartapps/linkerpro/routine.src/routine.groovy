definition(
    name: "Routine",
    namespace: "linkerPro",
    author: "Josh",
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
    state.killed = "false"
	if (physical) {
    	subscribe(physical, "switch", "handler")
       	if (syncDim) subscribe(physical, "level", "handler") 
        if (syncCol) {
        	subscribe (physical, "color", "handler") 
            subscribe (physical, "colorTemperature", "handler")
        }    
    }    
    if (movement) subscribe(movement, "motion", "handler")  
	if (contact) subscribe(contact, "contact", "handler")
    if (presence) subscribe(presence, "presence", "handler")
    if (vibration) subscribe(vibration, "acceleration", "handler")
    if (modeChange) subscribe(location, "handler")  
    
    if (specialSwitch) subscribe(specialSwitch, settings["specialAttribute"] ?: "status", "handler") 
 	if (meter) subscribe(meter, "power", "handler")
    if (dimRange) subscribe(dimRange, "level", "handler")
    if (onColor) {
    	subscribe (onColor, "color", "handler") 
        subscribe (onColor, "colorTemperature", "handler")
    }    
    if (temp) subscribe(temp, "temperature", "handler")
    if (timer) schedule(timer,timeHandler)
    if (buttonPush) subscribe(buttonPush, "button", "handler")   
      
    if (slave && offCallback) { slave.each() { subscribe(it, "switch.off", "callbackHandler")} }
    
    if (killSwitch) subscribe(killSwitch,"switch","killHandler")
    if (killSensor) ["motion","contact","presence","acceleration","power","temperature"].each { cap -> subscribe(killSensor, cap, "killHandler") }
    //if (killSensor) subscribe(killSensor,"sensor","killHandler")
}

def mainPage() {
    dynamicPage(name: "mainPage", install: true, uninstall: true)  {
        section ("Assignments") {
    			href(name:"toNextPage", page:"configurePage", description: configureFormat(),title: "Cause")
                href(name:"toNextPage", page:"effectPage", description: eventFormat(),title: "Effects")
                if (!hideSection("limits") || !quickEdit) 
                href(name:"toNextPage", page:"limitPage", description: limitFormat(),title: "Limits") 
                inptSimp("quickEdit", "bool", "Quick Edit")
        }
        section ("App Name") {        
			input("customTitle","text",title:"",description:"custom title",required:false, submitOnChange: true)
		}
    }
}

def configureFormat() {
            	def working = "["
            	["physical", "n",
                 "specialSwitch","specialAttribute","specialValue","nn",
                 "dimRange","dimAbove","dimBelow","n",
                 "onColor","nn",
                 "movement","contact","presence","vibration","meter","temp","nn",
                 "modeChange","nn","buttonPush","buttonValue","n"
                 ].each{
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
            	["slave", "collave","nn",
                "On",
                "virtualOn","n","flipOn","n",
                "mimicOn", "masterOn","n",
                "dimOn","dimValueOn","dimMasterOn","n",
                "colorOn","colorValueOn","colorMasterOn", "n",
                "setModeOn","n",
                "statusOn", "statusValueOn","n",  
                "phraseOn","msgOn","phoneOn", "messageOn","n",
                 "customOn","customCmdOn","nn",
                "Off",
                "virtualOff","n","flipOff","n",
                "mimicOff", "masterOff","n",
                "dimOff","dimValueOff","dimMasterOff","n",
                 "colorOff","colorValueOff","colorMasterOff", "n",
                 "setModeOff","n",
                 "statusOff", "statusValueOff","n", 
                 "phraseOff","msgOff","phoneOff","messageOff", "n",
                 "customOff","customCmdOff"].each{
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
               // log.debug working
                working = working+"]"
                working = working.replaceAll('slave:',"Sync").replaceAll('virtualOn',"Turn on").replaceAll('virtualOff',"Turn off").replaceAll('flipOn',"Turn off").replaceAll('flipOff',"Turn on")
                
                //log.trace working
                int index = 0
  				while (index != -1) {
                	working = working.replaceAll('\n\n',"\n")
    				index = working.indexOf('\n\n', index+1);
                    //log.trace "first $index"
  				}
                working = working.replaceAll('\\|n\\|',"\n\n")
                if (waitOnSecs) working = working +"\n(wait: $waitSecs seconds)" 
                else if (waitOn) working = working +"\n(wait: $waitOn minutes)"
               // log.debug working
                
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
    if ((killSwitch || killSensor) && killVal) working = working +"\n\nKILL if ${(killSwitch) ?: killSensor} is $killVal"
    
    return (working.size() > 0) ? working[2..-1].replaceAll('\\[',"").replaceAll('\\]',"") : "(none)"
}

private hideSection(lead) {
  def res = null
  //if (!quickEdit)
  switch(lead) { 
  	  case "switch":	res = physical ||specialSwitch || dimRange || onColor || buttonPush 
      						break
      case "sensors": 	res=movement || contact || meter || 
      						presence || vibration ||
      						meter || temp
                        	break  
                            //
      case "special":  res=baseDevice
      					break
      case "events": 	res=modeChange || timer   						 
							break  
      case "slave": 	res=slave || colorSlave 
                        	break
      case ["on","On"]: 		res=virtualOn || flipOn || 
      						mimicOn || masterOn ||
     					    dimOn || dimValueOn || dimMasterOn ||
                            colorOn || colorValueOn || colorMasterOn ||
                            statusOn || statusValueOn ||  
                            phraseOn || msgOn || messageOn ||
                            setModeOn || customOptionsOn || CamraOn                       
                        	break 
                            
      case ["off","Off"]: 		res=virtualOff || flipOff ||  
      						mimicOff || masterOff ||
      						dimOff || dimValueOff || dimMasterOff ||
                            colorOff || colorValueOff || colorMasterOff ||
                            statusOff || statusValueOff ||
                            phraseOff|| msgOff || messageOff ||
                            setModeOff || customOptionsOff || CamraOff
                        	break    
                            
      case "delay":			res=waitOn || waitOnSecs
      						break
                            
	  case "limits": 	res=onGate || offGate ||
     						openContact || closedContact || 
                            activeMotion || inactiveMotion ||
                            presencePresent || presenceNotPresent ||
  							modes || fromTime || toTime || killSwitch || killSensor
                        	break 
	  case "limSwitch": 	res=onGate || offGate
     						break   
	  case "limSensor": 	res=openContact || closedContact || 
                            activeMotion || inactiveMotion ||
                            presencePresent || presenceNotPresent
                        	break                             
	  case "limEvent": 		res=modes || fromTime || toTime || killSwitch || killSensor
                        	break                             
                            
  }// else res = true
  
  return res ? false : true
}

def configurePage() {
	dynamicPage(name: "configurePage", title: "Causes:", nextPage: "mainPage", uninstall: false) {
    	if (!hideSection("switch") || !quickEdit) section("switch", hideable:true, hidden: hideSection("switch")) {
            inpt([],"physical", "capability.switch","","Switch"); inpt(["physical"],"hardOn", "bool", "(hard push?)")
    		inptSimp("dimRange", "capability.switch", "Dimmer"); inpt(["dimRange"],"dimAbove","number", "","Above");  inpt(["dimRange"],"dimBelow","number", "","Below") 
            inptSimp("onColor", "capability.colorControl", "Color Change")
            inptSimp("buttonPush", "capability.button", "Button(s)"); inpt(["buttonPush"],"buttonValue","text","","Button number");	inpt(["buttonPush"],"buttonHeld","bool", "Held?") 
        }     
		if (!hideSection("sensor") || !quickEdit) section(title: "Sensor", hideable:true, hidden: hideSection("sensors")) {
        	inptSimp("movement", "capability.motionSensor", "Movement sensor")
			inptSimp("contact", "capability.contactSensor", "Contact sensor")
    		inptSimp("presence", "capability.presenceSensor", "Presence sensor")
    		inptSimp("vibration", "capability.accelerationSensor", "Vibration sensor")     
	   		inptSimp("meter", "capability.powerMeter", "Power"); inpt(["meter"],"meterValue","number","","Threshold")
    		inptSimp("temp","capability.temperatureMeasurement","Temperature Sensor"); inpt(["temp"],"tempValue", "number", "", "Target")
		}
        if (!hideSection("special") || !quickEdit) section(title: "Special", hideable:true, hidden: hideSection("special")) {
       		input("baseDevice", "enum" ,title: type, multiple: false, required: false, options: [["capability.actuator":"Actuator"],["capability.switch":"Switch"],["capability.sensor":"Sensor"]], submitOnChange: true)
			inpt(["baseDevice"],"specialSwitch", baseDevice, "Special switch",""); 
            
            def atts = []
            if (specialSwitch) specialSwitch.each {it.getSupportedAttributes().each { 
            	if (!atts.contains(it.name))
            	if (!["checkInterval", "DeviceWatch-DeviceStatus", "healthStatus", "DeviceWatch-Enroll"].contains(it.name)) atts+=it.name
            }	}
            inptEnum(["specialSwitch"], "specialAttribute",atts, "", "Attribute")
            //inpt(["specialSwitch"],"specialAttribute", "text", "", "Attribute")
            inpt(["specialSwitch"],"specialValue", "text", "", "Value")  
            inpt(["specialSwitch"],"specialNot", "bool", "Not")  
        }
		if (!hideSection("events") || !quickEdit) section(title: "Events", hideable:true, hidden: hideSection("events")) {       
            inptSimp("modeChange", "mode", "Mode change")
            inptSimp("timer", "time", "Time")
        }    
	}        
}    

def inptSimp(String name, String type, String title = "") {	inpt([], name, type, title)	}   
def inptAlt(ArrayList parent=[], Boolean inverted, String name, String type, String title = "", String desc = "", Boolean multiple=true) {
	def parentPassed = false
	parent.each { if (settings["$it"]) parentPassed = true }
	if ((parentPassed && !inverted) || (!parentPassed && inverted)) inpt([], name, type, title, desc, multiple)
}


def inpt(ArrayList parent=[], String name, String type, String title = "", String desc = "", Boolean multiple=true) {
	
	def parentPassed = true
    log.trace "name $name title $title desc $desc parent $parent"
    parent.each { if (!settings["$it"]) parentPassed = false }
    if (type == "bool" || type == "text" || type == "number" || type == "time" || type == "mode" || type == "capability.button") multiple = false
	if ((settings["$name"] || !quickEdit) && (!parent || parentPassed))
	input name, type, title: title, description: desc, required: false, multiple: multiple, submitOnChange: true
}

def inptEnum(ArrayList parent=[], String name, ArrayList opts, String title = "", String desc = "", Boolean required=false) {
    def parentPassed = true
    log.trace "name $name title $title desc $desc parent $parent"
    parent.each { if (!settings["$it"]) parentPassed = false }
	if ((settings["$name"] || !quickEdit) && (!parent || parentPassed))
	input name, "enum", title: title, description: desc, options: opts, required: required, multiple: false, submitOnChange: true
    else log.trace "$parentPassed"
}



def effectPage() {
	dynamicPage(name: "effectPage", title: "Effects:", nextPage: "mainPage", uninstall: false) {       
    	 if (!hideSection("slave") || !quickEdit) section("Sync") {
        	 inpt([], "slave", "capability.switch", "Sync")
             inpt(["slave"], "syncDim", "bool", "Dim?");  inpt(["slave", "syncDim"], "dimOnly", "bool", "(dim only?)") 
             
             inpt(["syncDim"], "syncDimMult", "text", "Multiplier");
             inpt(["slave"], "syncCol", "bool", "Color?");  inpt(["slave", "syncCol"], "colOnly", "bool", "(color only?)") 
             inpt(["slave"], "offCallback", "bool", "Callback (on off())?");  inpt(["slave","offCallback"], "offCallbackAny", "bool", "...even just one?")     		
			 inpt([],"colorSlave", "capability.colorControl", "Colors?")            
		}
        
        if (!hideSection("on") || !quickEdit) section("On", hideable:true, hidden: hideSection("on")) {    
			inpt([], "virtualOn", "capability.switch", "On --> On()")
   			inpt([], "flipOn", "capability.switch", "On --> Off()")
    		inpt([], "mimicOn", "capability.switch", "On --> Mimic");	inpt(["mimicOn"], "masterOn", "capability.switch",  "", "Master switch:")
            
   			inpt([], "dimOn", "capability.switchLevel", "Set dimmer");  inpt(["dimOn"], "dimOnlyOn", "bool", "(dim only?)")
		     inpt(["dimOn"], "dimValueOn", "text", "Dim value:");  inpt(["dimOn"], "dimMasterOn", "capability.switchLevel",  "", "Dim master:")    
             inpt(["dimMasterOn"], "dimMasterMultOn", "text", "Multiplier");
            
			inpt([], "colorOn", "capability.colorControl", "Set color:");	inpt(["colorOn"], "colorOnlyOn", "bool", "(color only?)")  
             inpt(["colorOnlyOn"], "colorIgnoreOn", "bool", "(ignore off?)")  
			 inpt(["colorOn"], "colorValueOn", "text",  "Color value:");	inpt(["colorOn"], "colorMasterOn", "capability.colorControl", "Color master:")        
    		
    		inpt([], "statusOn", "capability.switch", "Set status");	inpt(["statusOn"], "statusValueOn", "text", "Status value:")           
            inpt([], "setModeOn", "mode",  "Change mode to:")
			//def phrases = location.helloHome?.getPhrases()*.label
			inptEnum([], "phraseOn", location.helloHome?.getPhrases()*.label, "Activate routine:")
    		
            inpt([], "msgOn", "text", "Message", "Internal");	inpt(["msgOn"], "msgPushOn", "bool", "Alert?");	inpt(["msgOn"], "msgFeedOn", "bool", "Feed?")      
			inpt([], "messageOn", "text", "SMS");	inpt(["messageOn"], "phoneOn","phone" ,"") 
            
            inpt([], "cameraOn", "capability.imageCapture", "Camera", "Take!")
                        
         	inpt([], "customOptionsOn", "bool", "Custom?");   def typeOn = "capability.actuator"
             inptEnum(["customOptionsOn"],"customTypeOn", [["capability.actuator":"Actuator"],["capability.switch":"Switch"],["capability.sensor":"Sensor"]], "Device", "Type");  if (customTypeOn) typeOn = customTypeOn		           
             inpt(["customOptionsOn"], "customOn", "$typeOn", "Device");	
             //inpt(["customOptionsOn", "customOn"], "customCmdOn", "text", "Command", "none")	             
            def cmds = []
            if (customOn) customOn.each { it.supportedCommands.each {if (!cmds.contains(it.name)) cmds+=it.name}	}
            if (!customOptionsOnText) inptEnum(["customOptionsOn", "customOn"], "customCmdOn",cmds, "Command") 
            inpt(["customOptionsOn", "customOn"], "customOptionsOnText", "bool", "Manual input?");
            inpt(["customOptionsOn", "customOn", "customOptionsOnText"], "customCmdOn", "text", "Command", "none")	
		}
        log.trace "${!hideSection("off")} || ${!quickEdit}"
        if (!hideSection("off") || !quickEdit) section("Off", hideable:true, hidden: hideSection("off")) {    
			inpt([], "virtualOff", "capability.switch", "Off --> Off()")
   			inpt([], "flipOff", "capability.switch", "Off --> On()")
    		inpt([], "mimicOff", "capability.switch", "Off --> Mimic");	inpt(["mimicOff"], "masterOff", "capability.switch", "Master switch:")
            
   			inpt([], "dimOff", "capability.switchLevel", "Set dimmer");  inpt(["dimOff"], "dimOnlyOff", "bool", "(dim only?)")
			 inpt(["dimOff"], "dimValueOff", "text",  "Dim value:");  inpt(["dimOff"], "dimMasterOff", "capability.switchLevel", "Dim master:")    
            inpt(["dimMasterOff"], "dimMasterMultOff", "text", "Multiplier");
            
            
			inpt([], "colorOff", "capability.colorControl", "Set color:");	inpt(["colorOff"], "colorOnlyOff", "bool", "(color only?)")  
             inpt(["colorOnlyOff"], "colorIgnoreOff", "bool", "(ignore off?)")  
			 inpt(["colorOff"], "colorValueOff", "text",  "color value:");	inpt(["colorOff"], "colorMasterOff", "capability.colorControl", "Color master:")        
    		
    		inpt([], "statusOff", "capability.switch", "Set status");	inpt(["statusOff"], "statusValueOff", "text", "Status value:")           
            inpt([], "setModeOff", "mode", "Change mode to:")
			//def phrases = location.helloHome?.getPhrases()*.label
			inptEnum([], "phraseOff", location.helloHome?.getPhrases()*.label, "Activate routine:")
    		
            inpt([], "msgOff", "text", "Message", "Internal");	inpt(["msgOff"], "msgPushOff", "bool", "Alert?");	inpt(["msgOff"], "msgFeedOff", "bool", "Feed?")      
			inpt([], "messageOff", "text", "SMS");	inpt(["messageOff"], "phoneOff","phone" ,"") 
            
            inpt([], "cameraOff", "capability.imageCapture", "Camera", "Take!")
                        
         	inpt([], "customOptionsOff", "bool", "Custom?");   def typeOff = "capability.actuator"
             inptEnum(["customOptionsOff"],"customTypeOff", [["capability.actuator":"Actuator"],["capability.switch":"Switch"],["capability.sensor":"Sensor"]], "Device", "Type");  if (customTypeOff) typeOff = customTypeOff		           
             inpt(["customOptionsOff"], "customOff", "$typeOff", "Device");	
             	
            def cmds = []
            if (customOff) customOff.each { it.supportedCommands.each {if (!cmds.contains(it.name)) cmds+=it.name}}
            if (!customOptionsOffText) inptEnum(["customOptionsOff", "customOff"], "customCmdOff",cmds, "Command")
            inpt(["customOptionsOff", "customOff"], "customOptionsOffText", "bool", "Manual input?");
            inpt(["customOptionsOff", "customOff","customOptionsOffText"], "customCmdOff", "text", "Command", "none")
		}
        
        if (!hideSection("delay") || !quickEdit) section("Delay",hideable:true, hidden: hideSection("delay")) {
			if (waitOnSecs == false) inpt([], "waitOn", "number", "Minutes:", "")
        	inpt([], "waitOnSecs", "bool", "Seconds")
            inpt(["waitOnSecs"], "waitSecs", "number", "", "")
        }    
    }
}    


def limitPage() {
	dynamicPage(name: "limitPage", title: "Limits:", nextPage: "mainPage", uninstall: false) {
		if (!hideSection("limSwitch") || !quickEdit) section("Switch") {
            inpt([], "onGate", "capability.switch", "Only if one of these is ON:");	inpt([], "offGate", "capability.switch", "Only if ALL of these are OFF:")
        }
		if (!hideSection("limSensors") || !quickEdit) section("Sensors") {
            inpt([], "openContact", "capability.contactSensor", "Only if one of these is OPEN:"); inpt([], "closedContact", "capability.contactSensor", "Only if one of these is CLOSED:")  
            inpt([], "activeMotion", "capability.motionSensor", "Only if one of these is ACTIVE:"); inpt([], "inactiveMotion", "capability.motionSensor", "Only if one of these is INACTIVE:") 
            inpt([],  "presencePresent", "capability.presenceSensor", "Only if one of these is PRESENT:"); inpt([], "presenceNotPresent", "capability.presenceSensor", "Only if one of these is NOT PRESENT:") 
		}
       
		if (!hideSection("limState") || !quickEdit) section("State") {     
        	inpt([], "modes", "mode", "Only if in one of these MODES:")
            href(name:"toTimePage", page:"timePage", title: "Only if BETWEEN", description: timeFormat()) 
		}
        if (waitOnSecs || waitOn)
        section("Kill") {
            inptAlt(["killSensor"], true, "killSwitch", "capability.switch", "","Switch")	
            inptAlt(["killSwitch"], true, "killSensor", "capability.sensor", "","Sensor")
            if (settings["killSensor"] || settings["killSwitch"])
            inptEnum([],"killVal", [["On":"On"],["Off":"Off"],["?":"Other"]], "", "Value",true)	           
             
            
           // inptAlt(["killSwitch","killSensor"], false, "killVal", "text", "","Value")
            
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
    ["physical","specialSwitch","buttonPush","dimRange","onColor","movement","contact","presence","vibration","meter","temp"].each{ causeType ->
        if (settings["$causeType"]) causes << settings["$causeType"].join(", ")
    } 
    if (settings["modeChange"]) causes << settings["modeChange"]
    
    ["slave","colorSlave","virtualOn","flipOn","mimicOn","dimOn","colorOn","virtualOff","flipOff","mimicOff","dimOff","colorOff"].each{ effectType ->
    	//log.debug "list $effectType and ${settings["$effectType"]}"
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
    def alive = (state.killed == "false")
    
	def onGatePass=true, offGatePass=true, openGatePass=true, closedGatePass=true, activeGatePass=true, inactiveGatePass=true, presentGatePass=true, notPresentGatePass=true
	if (onGate) { 		onGatePass = onGate?.any { it.currentValue('switch').contains("on") } }
	if (offGate) { 		offGatePass = !(offGate?.any { it.currentValue('switch').contains("on") }) }
	if (openContact) {	openGatePass = openContact?.any { it.currentValue('contact').contains("open") } }    
	if (closedContact) {	closedGatePass = !(closedContact?.any { it.currentValue('contact').contains("open") }) }        
	if (activeMotion) {	activeGatePass = !(activeMotion?.any { it.currentValue('motion').contains("inactive") }) }      
	if (inactiveMotion) {	inactiveGatePass = !(inactiveMotion?.any { (it.currentValue('motion') == "active") }) }    
	if (presencePresent) {	presentGatePass = !(presencePresent?.any { (it.currentValue('presence') == "not present") }) }    
	if (presenceNotPresent) {	notPresentGatePass = !(presenceNotPresent?.any { (it.currentValue('presence') == "present") }) }    
   
   def ret = onGatePass && offGatePass && 
    	openGatePass && closedGatePass && 
        activeGatePass && inactiveGatePass &&
        presentGatePass && notPresentGatePass &&
        isMode && between && alive
    log.trace  "onGatePass $onGatePass && offGatePass $offGatePass && openGatePass $openGatePass && closedGatePass $closedGatePass" +
    			"&& activeGatePass $activeGatePass && inactiveGatePass $inactiveGatePass && presentGatePass $presentGatePass && notPresentGatePass $notPresentGatePass " +
            	"|| isMode $isMode && between $between && alive $alive \n $ret"
    return ret
}

def killHandler(evt) {
	log.trace "Killing: ${(settings["killVal"] == cleanEvt(evt.value))}" 
	if (settings["killVal"] == cleanEvt(evt.value)) state.killed = "true"
}

def handler(evt) {
	log.trace evt.value
	run([value:evt.value, name:evt.name, type:evt.type, displayName:evt.displayName,stringValue:evt.stringValue])
}

def run(evt) {
	log.trace "RUN: $evt.value"
	state.killed = "false"
    if (gateCheck())
    	runIn((waitOnSecs ? waitSecs : (waitOn ? waitOn*60 : 0)), runStuff, [overwrite: true, data:[value:evt.value, name:evt.name, type:evt.type, displayName:evt.displayName,stringValue:evt.stringValue]])
}


def timeHandler() {
	runStuff(createEvent(name: "time", value: "time"))
}

def runStuff(evt) { 
	log.trace "RUN STUFF: $evt.value"
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
        def dimVals = evt.value as Integer
        def dimMasterMult = (settings["syncDimMult"] ?: 1) as Double 
        dimVals = Math.min((dimMasterMult * dimVals), 99) as Integer
        log.debug "making it dim to ${dims}"
      	if (dimOnly) {
            dims?.each() {if (it.currentValue('switch')?.contains('on')) 
            {it.setLevel(dimVals)}}
        } else dims?.setLevel(dimVals) 
      	log.debug "${dims*.currentValue('switch')}"
    }  
    
    if (cat == "color" || cat == "colorTemperature") {
    	def colorSlave = colorSlave; def colorMaster = onColor
        if (syncCol) {
        	colorSlave = slave; colorMaster = physical
        }
        log.trace "slave = $colorSlave"
    
    log.trace "NOW slave = $colorSlave"
        
    if (cat == "color" && colorSlave && colorMaster) {
    	log.debug "going: color sync"
        def hueColor = colorMaster.latestValue("hue")[0]
        def saturation = colorMaster.latestValue("saturation")[0]
        
        colorSlave.each() {      
        	
            if (it.hasCommand("setColor")) 
               	it.setColor([hue:hueColor, saturation:saturation, hex:colorUtil.hslToHex((hueColor) as int, (saturation) as int)])
            if (it.hasCommand("setHue")) {
         		it.setHue(hueColor)                
               	it.setSaturation(saturation)
            }   
            if (saturation < 20) cat = "colorTemperature"
        }
	}
    
    if (cat == "colorTemperature" && colorSlave && colorMaster) {  
   		log.debug "going: colorTemp sync"
        def colorTemp = 3500	
        if (colorMaster.hasAttribute("colorTemperature")) colorTemp = colorMaster.latestValue("colorTemperature")[0] ?: 3500 
 		colorSlave.each() {          	
			if (it.hasCommand("setColorTemperature")) if (it.currentValue("colorTemperature") != colorTemp)
               	it.setColorTemperature(colorTemp) 
        }        
    }
    }
	// 			SYNC BLOCK		//  
    
    def special = settings["specialAttribute"] ?: "status"    
    if (cat == "switch") {cat = (!hardOn || typ == "physical") ? "OnOff" : "ignore"}
    if (cat == special) {
    	if (!specialNot) val = (specialValue == val) ? "On" : "ignore"
        else val = (specialValue == val) ? "Ignore" : "On"
    }
    if (cat == "mode") val = (val==modeChange) ? "On" : "Off"  
    if (cat == "button") {
		//val = getButtonVal(evt.value, evt.data)
        //log.debug "button hit: $val"                      
    	//val = (buttonValue == val && (!buttonHeld ^ evt.value.contains("held")))  ? "On" : "Off"  
        log.debug "button = $evt"
        val = (!buttonHeld ^ evt.value.contains("held"))  ? "On" : "Off"  
    }      
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
    	def valInt = val as Integer //Math.round(val)//evt.numericValue)
        desc = valInt as String
        //if (!tempValue) val = "On" else
        //val = (tempValue > valInt)  ? "Off" : "On"  
        val = (!tempValue) ? "On" : (tempValue > valInt)  ? "Off" : "On"
    }    
    cat = (["motion","contact","presence","acceleration","power",special,"mode", "temperature","button"].contains(cat)) ? "OnOff" : cat 
    val = cleanEvt(val)
    

	log.trace "Formated as: ${cat} is calling ${val}" 
    
    if (cat == "OnOff" && ["On","Off"].contains(val)) {    
   		if (slave && !dimOnly && !colOnly) {if (val=="On") {slave*.on()} else {slave*.off()}}
  
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
            def dimMaster = settings["dimMaster$val"]
            def dimMasterMult = (settings["dimMasterMult$val"] ?: 1) as Double
            log.debug "mult $dimMasterMult"
            log.debug "dimVals = ${dimVals}"
            
            dims?.each() {
            	if ((it.currentValue('switch')?.contains('on')) || (!settings["dimOnly$val"])) {
                	def curDim = (dimMaster) ? dimMaster?.currentValue('level')[0] : it.currentValue('level') as Integer
                    log.trace "curDim $curDim | *= ${dimMasterMult * curDim}"        
                    curDim = Math.min((dimMasterMult * curDim), 99) as Integer
                   
                    if ((dimVals.toString().contains("+")) || (dimVals.toString().contains("-"))) {
                    	def newDim = dimVals as Integer
                        dimVals = Math.max((curDim + newDim),0)                    
                    } else if (dimMaster) dimVals = curDim
                    log.trace "dimVals $dimVals"
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
        		def colorMaster = settings["colorMaster$val"][0]
                if (colorMaster) {
            		hueColor = colorMaster.latestValue("hue")
            		saturation = colorMaster.latestValue("saturation")
                    if (colorMaster.hasAttribute("colorTemperatur"))
                    	colorTemp = colorMaster.latestValue("colorTemperature")
            	}
        	}
            log.trace "colors are ${colors.currentValue('switch')} || $hueColor and $saturation"
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
            if (msg == "stat") msg = "$dev is $desc" 			
        	if (settings["msgPush$val"]) sendPushMessage("$msg")
            if (settings["msgFeed$val"]) sendNotificationEvent("$msg")
        }    
              
        def phoneMsg = settings["message$val"]
        if (phoneMsg) {
        	if (phoneMsg == "stat") phoneMsg = "$dev is $desc" 
			sendSmsMessage(settings["phone$val"] ?: "(202) 679-2566", phoneMsg)
		}
        
        def camera = settings["camera$val"]   
        if (camera) {
        	camera?.take()
        }
        
        
        def custom = settings["custom$val"]
        if (custom && settings["customOptions$val"]) {
        	log.trace "CUSTOM: custom$val:$custom AND customCmd$val"
        	def cmdTxt = settings["customCmd$val"]
            if (!cmdTxt.contains("(")) cmdTxt = cmdTxt.replaceFirst(' ',"(")
            if (cmdTxt[-1] == ")") cmdTxt = cmdTxt[0..-2]
            def cmds = cmdTxt.replaceAll(',',"(").split('\\(')
            
           
           log.trace "CMDS = $cmds"
           if (custom.hasCommand(cmds[0])) switch(cmds.length) {
           	case 1: custom."${cmds[0]}"(); break
            case 2: custom."${cmds[0]}"(cmds[1]); break
            case 3: custom."${cmds[0]}"(cmds[1], cmds[2]); break
            //case 4: custom."${cmds[0]}"(cmds[1], cmds[2], cmds[3]); break
           } 
        }    
    }    
}	}

def callbackHandler(evt) {
    if (physical && slave) {
		def allOff = !(slave?.any { it.currentValue('switch').contains("on") })
		if (allOff || offCallbackAny) physical*.off()
	}
}


private getButtonVal(value, data) {
	log.debug "getButtonVal( $value, $data )"
  	def val = "0"
    def test = value?.find(/\d+/) { match -> return "$match"}
    log.debug "PRESENTING: $test"
    switch(value) {
		case ~/.*10.*/:
			val = "10"
			break
		case ~/.*11.*/:
			val = "11"
			break		
        case ~/.*12.*/:
			val = "12"
			break
		case ~/.*1.*/:
			val = "1"
			break
		case ~/.*2.*/:
			val = "2"
               break
		case ~/.*3.*/:
			val = "3"
			break
		case ~/.*4.*/:
			val = "4"
			break
		case ~/.*5.*/:
			val = "5"
			break   
		case ~/.*6.*/:
			val = "6"
			break                
	}
    if (val == "0") switch(data) {
		case ~/.*1.*/:
			val = "1"
			break
		case ~/.*2.*/:
			val = "2"
			break
		case ~/.*3.*/:
			val = "3"
			break
		case ~/.*4.*/:
			val = "4"
			break
		case ~/.*5.*/:
			val = "5"
			break
		case ~/.*6.*/:
			val = "6"
			break                
	}
    return val
}

private cleanEvt(val) {
    return (["active", "open", "present","time","on"].contains(val)) ? "On" : ((["inactive", "closed", "not present","off"].contains(val)) ? "Off" : val) 
    if (val=="on") val="On"
    if (val=="off") val="Off"     
 }