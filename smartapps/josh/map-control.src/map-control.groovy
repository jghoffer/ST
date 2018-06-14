/**
 *  Smart Timer
 *  Loosely based on "Light Follows Me"
 *
 *  This prevent them from turning off when the timer expires, if they were already turned on
 *
 *  If the switch is already on, if won't be affected by the timer  (Must be turned of manually)
 *  If the switch is toggled while in timeout-mode, it will remain on and ignore the timer (Must be turned of manually)
 *
 *  The timeout perid begins when the contact is closed, or motion stops, so leaving a door open won't start the timer until it's closed.
 *
 *  Author: andersheie@gmail.com
 *  Date: 2014-08-31
 */

definition(
    name: "Map Control",
    namespace: "Josh",
    author: "listpope@cox.net",
    description: " . ",
    category: "Convenience",
    iconUrl: "http://upload.wikimedia.org/wikipedia/commons/6/6a/Light_bulb_icon_tips.svg",
    iconX2Url: "http://upload.wikimedia.org/wikipedia/commons/6/6a/Light_bulb_icon_tips.svg")

preferences {
    page(name: "firstPage")
	page(name: "secondPage")
}

def firstPage() {
	dynamicPage(name: "firstPage",uninstall: true, install:true,nextPage: null) {
    	section("Map") {
        	input "control", "capability.switch", multiple: false, title: "Controller", submitOnChange: true	
        }

    	section("Motion") {
        	input "denMotion", "capability.motionSensor", multiple: false, title: "Den", submitOnChange: true
            input "denMotion2", "capability.motionSensor", multiple: false, title: "Den", submitOnChange: true
        	input "entryMotion", "capability.motionSensor", multiple: false, title: "Entry", submitOnChange: true
        
            input "livingMotion", "capability.motionSensor", multiple: false, title: "Living Room", submitOnChange: true
            input "livingMotion2", "capability.motionSensor", multiple: false, title: "Living Room", submitOnChange: true
            input "livingMotion3", "capability.motionSensor", multiple: false, title: "Living Room", submitOnChange: true
            
            input "kitchenMotionFront", "capability.motionSensor", multiple: false, title: "Kitchen", submitOnChange: true
            input "kitchenMotionBack", "capability.motionSensor", multiple: false, title: "Kitchen", submitOnChange: true
            input "hallMotion", "capability.motionSensor", multiple: false, title: "Hall", submitOnChange: true
            input "bedroomMotion", "capability.motionSensor", multiple: false, title: "Bedroom", submitOnChange: true
            input "bedroomExtraMotion", "capability.motionSensor", multiple: false, title: "Bedroom", submitOnChange: true
            input "bedHallMotion", "capability.motionSensor", multiple: false, title: "Bedroom Hall", submitOnChange: true
            
            input "bedBathMotion", "capability.motionSensor", multiple: false, title: "Bed Bathroom", submitOnChange: true
            input "bedBathShowerMotion", "capability.motionSensor", multiple: false, title: "Bed Bathroom S", submitOnChange: true
            input "guestBathMotion", "capability.motionSensor", multiple: false, title: "guest Bathroom", submitOnChange: true
            input "guestBathShowerMotion", "capability.motionSensor", multiple: false, title: "guest Bathroom S", submitOnChange: true            
        }
        
        section("Contacts") {        
			input "denContact", "capability.contactSensor", multiple: false, title: "Den", submitOnChange: true
			
			input "frontContact", "capability.contactSensor", multiple: false, title: "Front Door", submitOnChange: true
			input "closetContact", "capability.contactSensor", multiple: false, title: "Front Closet Door", submitOnChange: true
            input "denClosetContact", "capability.contactSensor", multiple: false, title: "Den Closet Door", submitOnChange: true
            input "hvacContact", "capability.contactSensor", multiple: false, title: "HVAC Door", submitOnChange: true
			input "bedBathContact", "capability.contactSensor", multiple: false, title: "Bed Bathroom", submitOnChange: true
			input "guestBathContact", "capability.contactSensor", multiple: false, title: "Guest Bathroom", submitOnChange: true
			input "bedroomContact", "capability.contactSensor", multiple: false, title: "Bedroom", submitOnChange: true
            
            input "bedClosetContact", "capability.contactSensor", multiple: false, title: "Bedroom Closet", submitOnChange: true
            input "laundryContact", "capability.contactSensor", multiple: false, title: "Laundry", submitOnChange: true            
		}
        
        section("Temperature") {
            input "kitchenTemp", "capability.temperatureMeasurement", multiple: false, title: "Kitchen", submitOnChange: true
            input "bedBathTemp", "capability.temperatureMeasurement", multiple: false, title: "Bed Bathroom", submitOnChange: true
            input "guestBathTemp", "capability.temperatureMeasurement", multiple: false, title: "guest Bathroom", submitOnChange: true
            input "livingTemp", "capability.temperatureMeasurement", multiple: false, title: "Living Room", submitOnChange: true
        	input "denTemp", "capability.temperatureMeasurement", multiple: false, title: "Den", submitOnChange: true    
            input "bedroomTemp", "capability.temperatureMeasurement", multiple: true, title: "Bedroom", submitOnChange: true  
            input "laundryTemp", "capability.temperatureMeasurement", multiple: false, title: "Dryer", submitOnChange: true
            input "empTemp", "capability.temperatureMeasurement", multiple: false, title: "Thermostat", submitOnChange: true             
        }
        
        section("Other") {        
    		input "vibrationL", "capability.accelerationSensor", title: "Bed Vibration Left", multiple: true, required: false, submitOnChange: true  
            input "vibrationR", "capability.accelerationSensor", title: "Bed Vibration Right", multiple: true, required: false, submitOnChange: true 
        }
      
    }
}         
 

def installed()
{	updated()	}

def updated()
{
	unsubscribe()
    unschedule()
    subscribe(entryMotion, "motion", pipeHandler)
    
    subscribe(denMotion, "motion", pipeHandler)
    subscribe(denMotion2, "motion", pipeHandler)
    subscribe(bedHallMotion, "motion", pipeHandler)
    subscribe(livingMotion, "motion", pipeHandler)
    subscribe(livingMotion2, "motion", pipeHandler)
    subscribe(livingMotion3, "motion", pipeHandler) 
    subscribe(kitchenMotionFront, "motion", pipeHandler)    
    subscribe(kitchenMotionBack, "motion", pipeHandler)
    subscribe(hallMotion, "motion", pipeHandler)
    subscribe(bedroomMotion, "motion", pipeHandler)
    subscribe(bedroomExtraMotion, "motion", pipeHandler)
    subscribe(bedBathMotion, "motion", pipeHandler)
    subscribe(bedBathShowerMotion, "motion", pipeHandler)
    subscribe(guestBathMotion, "motion", pipeHandler)
    subscribe(guestBathShowerMotion, "motion", pipeHandler)
  
    subscribe(frontContact, "contact", pipeHandler)
    subscribe(closetContact, "contact", pipeHandler)
    subscribe(denClosetContact, "contact", pipeHandler)
    subscribe(denContact, "contact", pipeHandler) 
    
    subscribe(bedBathContact, "contact", pipeHandler)
    subscribe(guestBathContact, "contact", pipeHandler)
    subscribe(bedroomContact, "contact", pipeHandler) 
    subscribe(bedClosetContact, "contact", pipeHandler) 
    subscribe(laundryContact, "contact", pipeHandler) 
    subscribe(hvacContact, "contact", pipeHandler) 
    
    subscribe(kitchenTemp, "temperature", pipeHandler)    
    subscribe(denTemp, "temperature", pipeHandler)
    subscribe(bedroomTemp, "temperature", pipeHandler) 
    subscribe(livingTemp, "temperature", pipeHandler) 
    subscribe(guestBathTemp, "temperature", pipeHandler)
    subscribe(bedBathTemp, "temperature", pipeHandler)    
    subscribe(laundryTemp, "temperature", pipeHandler)  
    subscribe(empTemp, "temperature",pipeHandler) 
    
    subscribe(vibrationL, "acceleration", pipeHandler)
    subscribe(vibrationR, "acceleration", pipeHandler)
/*    
	settings.each() {
        log.trace "Settings: ${it.value.label}"
    	def evt = it.value?.events(max: 1)
        log.debug "DEV: ${evt}"
        if (evt) if (evt[0]?.name) if (evt[0].displayName[1]) {	pipeHandler(evt[0]) }
	}
    */
}


def pipeHandler(evt) {
    log.debug "| $evt.description && $evt.displayName"
    def dev = evt.displayName[1..-1].replaceAll(' ',"")
    if (evt.name.contains("acceleration")) {
       log.trace "motion yes"
       if (vibrationL.displayName.contains(evt.displayName)) dev = "BedL"
       if (vibrationR.displayName.contains(evt.displayName)) dev = "BedR"
    } else if (evt.name == "temperature") 
       if (bedroomTemp.displayName.contains(evt.displayName)) dev = "Bedroom"
    
    def val = evt.value.replaceAll('inactive',"off").replaceAll('active',"on")
    def cat = evt.name.replaceAll('motion',"Motion").replaceAll('contact',"Contact").replaceAll('temperature',"Temperature").replaceAll('acceleration',"Motion")
    log.debug "dev: $dev, cat: $cat, val: $val"

	control.activity("$dev:$cat:$val")   
}

