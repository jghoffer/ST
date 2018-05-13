/**
Room Boxer!
 */

definition(
    name: "Room Boxer",
    namespace: "Josh",
    author: "listpope@cox.net",
    description: "Box a room!",
    category: "Convenience",
    iconUrl: "http://icons.iconarchive.com/icons/aha-soft/large-home/48/New-room-icon.png",
    iconX2Url: "http://icons.iconarchive.com/icons/aha-soft/large-home/48/New-room-icon.png")

preferences {
    page(name: "firstPage")
	page(name: "secondPage")
}

def firstPage() {
	dynamicPage(name: "firstPage",uninstall: true, install:true,nextPage: null) {
		section("Room:"){
			input "switches", "capability.switch", multiple: true, title: "Select Lights", required: true
            input "indicator", "capability.switch", multiple: false, title: "Indicator", required: false
            input "deviceId", "string", title: "Child Device:", required: true
            input "offSwitchTime", "number", title: "Switch off delay (minutes):", defaultValue: "60"
            input "thermostat", "capability.temperatureMeasurement", multiple: false, title: "Thermometer", required: false
		}
    	section("Motion") {
        	input "motions", "capability.motionSensor", multiple: true, title: "Select motion detectors", required: false, submitOnChange: true

			if (motions) { 
				input "onMotion", "bool", title: "Trigger on (active)", required: false,submitOnChange: true
        		input "offMotion", "bool", title: "Trigger off (inactive)", required: false, submitOnChange: true
            	if (offMotion) {
       				input "offMotionTime", "number", title: "Motion inactive(minutes):", defaultValue: "3"
            	}    
			}	
        }
        
        section("Contacts") {
			input "contacts", "capability.contactSensor", multiple: true, title: "Select Contacts", required: false, submitOnChange: true

       		if (contacts) { 
				input "onContact", "bool", title: "Trigger on (open)", required: false, submitOnChange: true
        		input "offContact", "bool", title: "Trigger off (closed)", required: false, submitOnChange: true    
				if (offContact) {
					input "offContactTime", "number", title: "Contact closed delay (minutes):", defaultValue: "3"
            	}
        	}
		}
        
        section("Interactions") {
           input "buffer", "number", title: "Buffer (seconds):",required: true, defaultValue: "20" 

			if (motions && contacts) {
        	
                input "crosscheck", "bool", title: "Crosscheck?", required: false, submitOnChange: true      
//                if (crosscheck) input "crosscheckTime", "number", title: "Crosscheck delay (seconds):", defaultValue: "30"
               	input "lookback", "bool", title: "Lookback?", required: false, submitOnChange: true 
                if (lookback) input "lookTime", "number", title: "Lookback delay (seconds):", defaultValue: "30"
 				//if (crosscheck || lookback) {
            		//input "buffer", "number", title: "Buffer (seconds):",required: true, defaultValue: "20"    
            //    }
			}
        }	
       
        section(""){
        	def working = ""
        	["modes", "offGates","onModes","offModes","onOffGates","offOffGates"].each{
        		def inp = settings[it]

				if (["offGates", "modes"].contains(it)) {it = "General "+it[0..-1]}
                if (["onModes","onOffGates"].contains(it)) {it = it[2..-1]+" for: on"}
                if (["offModes","offOffGates"].contains(it)) {it = it[3..-1]+" for: off"}
                if (it.startsWith("off")) {it = it[3..-1]}
                def outp = it +": $inp"

            	if (inp) working = working +"$outp\n"
        	}        
            working = working.replaceAll('\\[',"").replaceAll('\\]',"")
             
        	href(name:"toNextPage", page:"secondPage", description: "$working", title: "Limits")
        }
       
        
        section(mobileOnly:true,"General") { label title: "Name", required: false }
    }
}         
 
def secondPage() {

	dynamicPage(name: "secondPage", title: "Limits", nextPage: "firstPage", uninstall: false) {
        	section("") {
            	input "specialLimits", "bool", title: "Special?", required: false, submitOnChange: true
            } 
            section("Mode must be:") {
      			input "modes", "mode", title: "", multiple: true, required: false
                if (specialLimits) {
                	input "onModes", "mode", title: "...as to ON", multiple: true, required: false 	
      				input "offModes", "mode", title: "...as to OFF", multiple: true, required: false 
                }
            }
            section("And off gates are:") {
				input "offGates", "capability.switch", title: "", multiple: true, required: false
                if (specialLimits) {
                	input "onOffGates", "capability.switch", title: "...as to ON", multiple: true, required: false 	
      				input "offOffGates", "capability.switch", title: "...as to OFF", multiple: true, required: false 
                }    
			}  
    }
}

def installed()
{
	//indicatorDev = 0
    unschedule()
	//updated()
    //addDevice()
}

def isOpen() {
	def passed = false
    if (!contacts) {passed = true} else {
    	contacts?.each() { if (it.currentValue('contact') == "open") { passed = true } } }
    return passed
}

def wasOpen(since) {
    def proof = false
     if (!contacts) {passed = true} else {
    	def adj = since + (buffer*1000) 
    	contacts?.each{
    		if (it.eventsSince(new Date(adj)).findAll{it.value == "open"}) {
			proof = true}
     	}
    }    
    return proof
}

def isMotion() {
	def passed = false
    motions?.each() { if (it.currentValue('motion') == "active") { passed = true } }
    return passed
}

def wasMotion(since) {
    def proof = false
    if (since) motions?.each{
        def adj = since + (buffer*1000) 
    	if (it.eventsSince(new Date(adj)).findAll{it.value == "active"}) {
		proof = true}
     }
    return proof
}


def updated()
{
	log.debug "updated!"
	unsubscribe()
    unschedule()
    addDevice()
    
    def indicatorDevice = indicatorDev()
    log.debug "${indicatorDevice.currentValue('boxed')}"
    if (indicatorDevice.currentValue('boxed') != "true") indicatorDevice.unBox("internal")
    
	subscribe(motions, "motion", pipeHandler)
    subscribe(contacts, "contact", pipeHandler)
    subscribe(switches, "switch", switchChange)
    subscribe(indicatorDevice, "switch", indicatorChange)
    subscribe(indicatorDevice, "boxed", indicatorChange)
    subscribe(thermostat, "temperature", pipeHandler)
    
    log.debug "indicator: $indicatorDev"
    
   if (!contacts) {
      state.boxed = false    
      indicatorDevice.activity("noContact") 
      log.trace "limited mode!"
    }
   if (!thermostat) { 
      indicatorDevice.setTemp("noTermp") 
      log.trace "No thermometer..."
    } else {
    	def temp = thermostat.currentValue('temperature')
        log.trace "temperature is $temp"
    	indicatorDev()?.setTemp("$temp")     
    }
    
}

def indicatorDev() {
	return getChildDevice("Indic $deviceId")
}


def addDevice()
{ 
	log.debug "adding device... $deviceId"
  //def indicatorDev = getChildDevice(deviceId)

  if (!indicatorDev()) 
  {
  log.debug "...$indicatorDev"  	
    addChildDevice("Room", "Indicator", "Indic $deviceId", null, 
    	[
        	name: deviceId,
            label: deviceId, 
            completedSetup: true,
            preferences: [
            ]
        ])
        
        log.debug "${getChildDevices()}"
    //indicatorDev = getChildDevice(deviceId)
    log.debug "created ${indicatorDev().displayName} with id $deviceId"
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

def indicatorChange(evt) {
	log.debug "IndicatorChange: name: $evt.name | value: $evt.value | type: $evt.type | boxed: $state.boxed"
    if (evt.type != "internal") {
    	if (evt.name == "switch") {
			if (evt.value == "on") {
        		turnOn()
        		setActiveAndSchedule(offSwitchTime)       
        	}    
        	if (evt.value == "off") turnOff() 
        }
        if (evt.name == "boxed") {
        	log.debug "indicator change calling box: $evt.value"
        	if (evt.value == "true") setBox(true)  
            if (evt.value == "false") setBox(false) 
        }    
	}
}

def switchChange(evt) {
	log.debug "SwitchChange: $evt.name: $evt.value $evt.type"
    if (evt.name == "switch" && evt.type != "internal") {
        resetState()
		if (evt.value == "off") { 
        	indicatorDev().off("internal")
        } else {
    		indicatorDev().on("internal")
    		setActiveAndSchedule(offSwitchTime)               
        }
	}
}

def modeCheck(mode) {
	return (!mode || mode.contains(location.mode))
}

def gateCheck(gate) {
	return (!(gate?.any { it.currentValue('switch').contains("on") }))
}

def pipeHandler(evt) {
	log.trace "what is $evt.value and ${gateCheck(onOffGates)} && ${modeCheck(onModes)}"
    log.debug "inactive check: ${(evt.value != "closed")} || ${(!isOpen())} || ${(evt.value != "inactive")} || ${(!isMotion())}"
    if (((evt.value != "inactive") || (!isMotion())) && ((evt.value != "closed")) || (!isOpen()))
	indicatorDev()?.activity(evt.value)

    if (evt.name == "contact") contactHandler(evt)
    if (evt.name == "motion") motionHandler(evt)
    if (evt.name == "temperature") indicatorDev().setTemp(evt.value) 
}

def contactHandler(evt) {
	log.debug "contactHandler: $evt.name: || value: $evt.value || boxed: $state.boxed || basicSet: $state.basicSet"
	resetState()
	if (evt.value == "open") {
       	state.backFrom = now()	  
     	if (state.boxed && lookback) {            
   			log.debug "Waiting to look... in $lookTime"
			runIn(lookTime, looking)
            state.looking = true
            indicatorDev()?.unBox("internal")
            indicatorDev()?.countdown("look")
            log.debug "changed indicator"
        } else {
        	if (onContact) {
            	log.debug "Turning on lights by contact opening"
				//resetState()
                turnOn()
        	}
    	}
        setBox(false)        
	} else if (evt.value == "closed") {
    	state.checkFrom = now()
    	if (offContact) setActiveAndSchedule(offContactTime)
    }
}

def motionHandler(evt) {
	log.debug "motionHandler: $evt.name: || value: $evt.value || boxed: $state.boxed || basicSet: $state.basicSet || looking: $state.looking || crosscheck: $crosscheck || checkFrom: $state.checkFrom || isOpen: ${isOpen()}"

    if (evt.value == "active" && !state.boxed) {
		resetState()
       	if(onMotion) turnOn()
		log.debug "check-box ${!(state.boxed)}"
		log.debug "check ${(crosscheck && !isOpen())}"
        
		log.debug "check ${(!state.boxed && crosscheck && !isOpen())}"

        if (!state.boxed && crosscheck && !isOpen()) {
         	def elapsed = now() - state.checkFrom
            log.trace "$elapsed >= ${(buffer * 1000)}"
            if (elapsed >= (buffer * 1000)) setBox(true)			
        }

    } else if (evt.value == "inactive" && !state.boxed) {
    	if (offMotion && !state.basicSet && !isMotion()) setActiveAndSchedule(offMotionTime) 
    }
}

def setActiveAndSchedule(mins) {
log.trace "I AM $state.boxed" 
if (!state.boxed) {
    log.debug "setting active and scheduling ($mins)"   
    unschedule("scheduledBasic"); unschedule("scheduledBackup")
    state.inactiveAt = now()
    log.debug "Schedule available? ${canSchedule()}"
    runIn(30, "scheduledBasic"); runIn(60, "scheduledBackup")

    if (state.looking) indicatorDev().countdown("look") 
    else indicatorDev().countdown(mins)
    state.basicSet = true
    state.minutes = mins
    log.debug "basic schedule set"
}	}

def scheduledRun()
{
	log.debug "scheduledRun: boxed: $state.boxed || basicSet: $state.basicSet || looking: $state.looking || crosscheck: $crosscheck || checkFrom: $state.checkFrom || isOpen: ${isOpen()}"    
    
	if (state.boxed) {
    	unschedule()
    	return
    }    
    
    if(state.inactiveAt != null) {
    	def elapsed = now() - state.inactiveAt
        log.debug "${(elapsed / 1000)} sec since [trigger] stopped"
	    def threshold = 60000 * state.minutes
	    if ((elapsed >= threshold) && state.basicSet) {
        	log.debug "Basic time's done!"  
            state.basicSet = false
            if (!state.boxed && !isMotion()) // && (isOpen() || !crosscheck || !wasMotion(state.checkFrom)))
				turnOff()
			//unschedule()
	        state.inactiveAt = null
	     } else {
         	def remaining = (threshold - elapsed) / 60000
            log.debug "BasicTimer: ${remaining} (raw) to go!"
            if 	    (remaining <= 0.25) null //state.basicTimer = "seconds" 
            else if (remaining < 0.6) state.basicTimer = "30 secs"  
            else if (remaining < 1) state.basicTimer = "1"  	
			else state.basicTimer = Math.round(remaining)
            log.debug "basicTimer: ${state.basicTimer}"
            if (!state.looking) indicatorDev().countdown(state.basicTimer)

			log.debug "report: ($contact && $crosscheck && ${!isOpen()} && (${isMotion()} || ${wasMotion(state.checkFrom)}))"

            if (contacts && crosscheck && !isOpen() && (isMotion() || wasMotion(state.checkFrom))) 
            {
            	log.trace "setting box"
                setBox(true) 
			} else { 
            	int tickTime = (remaining * 60) - 11
                log.debug "tick time = $tickTime"
            	if ((remaining < 1.2) && (tickTime > 1))  //tickTock([value:10])
                runIn(tickTime, tickTock, [data:[value:11]])  
                //if (remaining < 0.6) { if (tickTime > 1) remaining = tickTime else remaining = 0} 
                if ((remaining < 0.6) && (tickTime > 1)) remaining = tickTime + 13
 				else if (remaining < 1.2) remaining = 12
                else remaining = 30
            	log.trace "scheduling next in $remaining"
             	runIn(remaining, "scheduledBasic"); runIn(60, "scheduledBackup")
                
            }
        }
    }
    //else if (switches.any { it.currentValue('switch').contains("off") }) unschedule()
}

def tickTock(data) {
	log.debug "Tick! Tock! $data"
    def value = data.value
	indicatorDev().countdown("s$value")
    if (value > 1)
	runIn(1, tickTock, [data:[value:value-1]])
}

def scheduledBasic() {	
	log.debug "scheduledBasic"
	unschedule("scheduledBackup")
	scheduledRun() 
}
def scheduledBackup() {	
	log.debug "scheduledBackup"
	scheduledRun() 
}

def looking() {	
	log.debug "looking - backFrom: $state.backFrom | wasMotion: ${wasMotion(state.backFrom)} "
    state.looking = false;
	if (!state.boxed) {
    	if (!wasMotion(state.backFrom) && !isMotion()) {
        	turnOff()
            setBox(false) 
        }    
      	else if (state.basicTimer) indicatorDev().countdown(state.basicTimer)
	}
}

def turnOn() {
	log.debug "turning on lights..."
    if (modeCheck(modes) && gateCheck(offGates) && gateCheck(onOffGates) && modeCheck(onModes))
	{
   		switches.on("internal")//.onInternal()
    	indicatorDev().on("internal")\
    } else log.debug "turn on BLOCKED"
}

def turnOff() {
	log.debug "turning off lights"
    if (modeCheck(modes) && gateCheck(offGates) && gateCheck(offOffGates) && modeCheck(offModes))
	{
		switches.off("internal")//.offInternal()  
    	indicatorDev().off("internal")
    	resetState()
    } else {
    	log.debug "turn off BLOCKED"
    }
}

def setBox(val) {
    log.debug "setBox | val: $val | state.boxed: $state.boxed | boxed != val: ${state.boxed != val}"
	if (state.boxed != val) {
		state.boxed = val
		
		if (val == true) {
    		indicatorDev()?.box("internal")
       		resetState()
    	} else if (val == false) {
    		indicatorDev()?.unBox("internal")
            //if (offContactTime != null)
            if (offContact && (offContactTime > 0)) setActiveAndSchedule(offContactTime) else
        	setActiveAndSchedule(offSwitchTime)
    	}    
    } else log.debug "box unchanged"
}

def resetState() {
	log.debug "Resetting: basicSet, looking, inactiveAt, backFrom, basicTimer"
	unschedule()
    state.basicSet = false
    state.looking = false
    state.inactiveAt = null
    state.backFrom = null
    state.basicTimer
}

def unsch(r= null) {
	log.trace "unsched-ing $r"
	try {
		if (r) unschedule(r)
		else unschedule()
	}
	catch (Throwable t) {
		log.error "Caught: '$t' on $r"
	}
}
