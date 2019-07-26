definition(
    name: "Manager",
    namespace: "linkerPro",
    author: "Josh",
    description: "Woo!",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
page(name: "mainPage")
}
    // The parent app preferences are pretty simple: just use the app input for the child app.
    //page(name: "mainPage", title: "Links:", install: true, uninstall: true,submitOnChange: true) {
    
    
def mainPage() {
    dynamicPage(name: "mainPage", title: "Links",  install: true, uninstall: true) {
    	section {
        	input "singleCause", "bool", title:"Single Cause", submitOnChange: true
        }
        if (settings["singleCause"] == true) {
        	section("switch", hideable:true, hidden: true) {
            inpt([],"physical", "capability.switch","","Switch"); 
            inpt(["physical"],"physOn", "bool", "On?"); inpt(["physical"],"physOff", "bool", "Off?")
    		inptSimp("dimRange", "capability.switch", "Dimmer"); inpt(["dimRange"],"dimAbove","number", "","Above");  inpt(["dimRange"],"dimBelow","number", "","Below") 
            inptSimp("onColor", "capability.colorControl", "Color Change")
            inptSimp("specialSwitch", "capability.switch", "Special switch"); inpt(["specialSwitch"],"specialAttribute", "text", "", "Attribute"); inpt(["specialSwitch"],"specialValue", "text", "", "Value")            
            inptSimp("buttonPush", "capability.button", "Button(s)"); inpt(["buttonPush"],"buttonValue","text","","Button number");	inpt(["buttonPush"],"buttonHeld","bool", "Held?") 
       		}     
			section(title: "Sensor", hideable:true, hidden: true) {
        	inptSimp("movement", "capability.motionSensor", "Movement sensor")
			inptSimp("contact", "capability.contactSensor", "Contact sensor")
    		inptSimp("presence", "capability.presenceSensor", "Presence sensor")
    		inptSimp("vibration", "capability.accelerationSensor", "Vibration sensor")     
	   		inptSimp("meter", "capability.powerMeter", "Power"); inpt(["meter"],"meterValue","number","","Threshold")
    		inptSimp("temp","capability.temperatureMeasurement","Temperature Sensor"); inpt(["temp"],"tempValue", "number", "", "Target")
        	
            if (settings["movement"] || settings["contact"] || settings["presence"] || settings["vibration"] || settings["meter"] || settings["meter"])
            { inpt([], "sensOn", "bool", "On?"); inpt([], "sensOff", "bool", "Off?") }
            }
            
			section(title: "Events", hideable:true, hidden: true) {       
            inptSimp("modeChange", "mode", "Mode change")
            inptSimp("timer", "time", "Time")
        	}	 
    	}
        section {
            app(name: "deviceApp", appName: "Routine", namespace: "linkerPro", title: "+ Add New...", multiple: true)

		}
        section(mobileOnly:true,"Admin") { label title: "Name", required: false }
	}
}    

def handler(evt) {
	def val = evt.value
    def cat = evt.name
 
    def special = settings["specialAttribute"] ?: "status"    
    //if (cat == "switch") {cat = (!hardOn || typ == "physical") ? "OnOff" : "ignore"}
    if (cat == special) val = (specialValue == val) ? "On" : "ignore"
    if (cat == "mode") val = (val==modeChange) ? "On" : "Off"  
    if (cat == "button") {
		val = getButtonVal(evt.value, evt.data)
        log.debug "button hit: $val"                      
    	val = (buttonValue == val && (!buttonHeld ^ evt.value.contains("held")))  ? "On" : "Off"  
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
    
    val = cleanEvt(val) 
    def go = (!["motion","contact","presence","acceleration","power"].contains(cat) || (((settings["sensOn"] == true) && (val == "On")) || ((settings["sensOff"] == true) && (val == "Off"))))
	cat = (["switch","motion","contact","presence","acceleration","power",special,"mode", "temperature"].contains(cat)) ? "OnOff" : cat
    
  	log.trace "$go"
  	def newEvt = [value:val, name:cat, type:evt.type, displayName:evt.displayName,stringValue:evt.stringValue]  
    log.debug newEvt
    if (go) childApps.each {child -> child.run(evt) }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}

def initialize() {
	log.debug "there are ${childApps.size()} child smartapps"
    childApps.each {child ->
        log.debug "child app: ${child.label}"
        child.initialize()
    }
    subscriber()
}

def subscriber() {
unsubscribe() 
if (settings["singleCause"] == true) {    
	if (physical) {
    	if (settings["physOn"] == true)		subscribe(physical, "switch.on", "handler")
        if (settings["physOff"] == true)	subscribe(physical, "switch.off", "handler")
       	if (syncDim) subscribe(physical, "level", "handler") 
        if (colorSlave || syncCol) {
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
    if (onColor) subscribe(onColor, "color", "handler")
    if (temp) subscribe(temp, "temperature", "handler")
    if (timer) schedule(timer,timeHandler)
    if (buttonPush) subscribe(buttonPush, "button", "handler") 
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
   // log.trace "name $name title $title desc $desc parent $parent"
    parent.each { if (!settings["$it"]) parentPassed = false }
    if (type == "bool" || type == "text" || type == "number" || type == "time" || type == "mode" || type == "capability.button") multiple = false
	if ((settings["$name"] || !quickEdit) && (!parent || parentPassed))
	input name, type, title: title, description: desc, required: false, multiple: multiple, submitOnChange: true
}

private cleanEvt(val) {
    return (["active", "open", "present","time","on"].contains(val)) ? "On" : ((["inactive", "closed", "not present","off"].contains(val)) ? "Off" : val) 
    if (val=="on") val="On"
    if (val=="off") val="Off"     
 }