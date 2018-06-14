/**
 *  Copyright 2015 SmartThings
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
 *  Linker
 */
 
/*

input "time", "time", title: "Time of Day"
schedule(time, changeMode)


*/


definition(
	name: "Device Linker",
	namespace: "Josh",
	author: "Josh",
	description: "_",
	category: "My Apps",
	iconUrl: "http://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/64/Actions-insert-link-icon.png",
	iconX2Url: "http://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/64/Actions-insert-link-icon.png"
    
)



preferences {
    page(name: "firstPage")
    page(name: "editPage")
	page(name: "configurePage")
    page(name: "effectPage")
    page(name: "limitPage")
    page(name: "deletePage")
    
 }
    
def firstPage()     {
	dynamicPage(name: "firstPage",uninstall: true, install:true,nextPage: null) {
        
        def addNew = 0
        def assignments = []
        (1..99).each() { button ->
            def tt = getTitle(button)            
            
            if (tt) assignments << button
            else {
               if (addNew == 0)
               addNew = assignments.size()+1
            }   
        }  
        
        section("Assignments"){
        	def mainItems =
            	["virtual","color",
				 "virtualOn","flipOn","mimicOn", "masterOn","dimOn","dimValueOn","dimMasterOn",
                "colorOn","colorValueOn","colorMasterOn","setModeOn","statusOn","statusValueOn",
                "flashOn","numFlashOn","indicatorOn","msgOn",
                "virtualOff","flipOff","mimicOff", "masterOff","dimOff","dimValueOff","dimMasterOff",
                "colorOff","colorValueOff","colorMasterOff","setModeOff","statusOff","statusValueOff",
                "flashOff","numFlashOff","indicatorOff","msgOff"]        
        	def textItems = ["setModeFOn","phraseOn","msgOn","phoneOn","messageOn","flashOn","numFlashOn","dimValueOn","colorValueOn","statusValueOn",
							 "setModeOff","phraseOff","msgOff","phoneOff","messageOff","flashOff","numFlashOff","dimValueOff","colorValueOff","phraseOff","statusValueOff"]

			def limitItems = ["onGate","offGate","openContact","closedContact","activeMotion","inactiveMotion","presencePresent","presenceNotPresent","modes"]

			def working = ""
			assignments.each { button ->      
            	def eff = []
				mainItems.each{
					def inp = find(it,button)
                    if (inp) {
                    	log.trace "so hey, $it is $inp"
                    	if (textItems.contains(it)) eff << '"'+inp.toString()+'"'
                        else inp.each { e ->
                        	def f = eff.toString().contains(e.toString())
                    		if (!f) eff << e
                        }
                    }
				}
                def lim = []
                limitItems.each{
                	def inp = find(it,button)
                    if (inp) lim << inp.toString()
                }    
                
                working = eff.join(", ")
                if (lim) working = working + "\n" + lim.join(", ").replaceAll('"',"")
        		href(name:"toNextPage", page:"editPage", title: "${getTitle(button)}", description: working, params: [num:button])
        	}
        	href(name:"toNextPage", page:"editPage", title: "Add New...", params: [num:addNew])
      }      
        
        section(mobileOnly:true,"General") { label title: "Name", required: false }
	}
}

def editPage(params)     {

	dynamicPage(name: "editPage",uninstall: false, ,nextPage: "firstPage") {
		def working = ""
        log.debug "Params: $params"
        def buttonNumber 
        if (params.num) buttonNumber = params.num
        else buttonNumber = state.buttonNumber
        //buttonNumber = state.buttonNumber//params.num
        state.buttonNumber = buttonNumber
        log.debug "Num set = $params.num"
        
		section("Edit"){
			//if (selector) {
            	working = "["
            	["physical", "n",
                 "dimRange","dimAbove","dimBelow","nn",
                 "movement","contact","presence","vibration","meter","temp","nn",
                 "modeChange","nn","buttonSwitch","buttonValue","n",
                 "specialSwitch","specialValue","n"].each{
                	def inp = find(it,buttonNumber)
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
				working = (working.size() > 0) ? working[1..-1].replaceAll('\\[',"").replaceAll('\\]',"") : "(none)"
    			href(name:"toNextPage", page:"configurePage", description: "$working",
    				title: "Cause")

            	working = "["
            	["virtual", "n","color","nn",
                "On",
                "virtualOn","n","flipOn","n",
                "mimicOn", "masterOn","n",
                "dimOn","dimValueOn","dimMasterOn","n",
                "colorOn","colorValueOn","colorMasterOn", "n",
                "setModeOn","n",
                "statusOn", "statusValueOn","n",  
                "phraseOn","msgOn","phoneOn", "messageOn","n",
                "flashOn","numFlashOn","n",
                 "indicatorOn","buttonDisplayOn","goDisplayOn","goButtonOn","ledOn",
                 "nn",
                "Off",
                "virtualOff","n","flipOff","n",
                "mimicOff", "masterOff","n",
                "dimOff","dimValueOff","dimMasterOff","n",
                 "colorOff","colorValueOff","colorMasterOff", "n",
                 "setModeOff","n",
                 "statusOff", "statusValueOff","n", 
                 "phraseOff","msgOff","phoneOff","messageOff","n",
                 "flashOff","numFlashOff","n",
                 "indicatorOff","buttonDisplayOff","goDisplayOff","goButtonOff","ledOff"].each{
                	def inp
                    if (it == "nn") working = working +"\n\n" else
                    if (it == "n") working = working +"\n" else
                    if (it == "On"){
                    	working = working +"|On"
                        def wait = find("waitOn",buttonNumber)
                        if (wait) working = working +" (wait: $wait)"
                        working = working + "|\n" 
                    } else
                    if (it == "Off") {                    	
                    	working = working +"|Off"
                        def wait = find("waitOff",buttonNumber)
                        if (wait) working = working +" (wait: $wait)"
                        working = working + "|\n" 
                    } else {
                    	inp = find(it,buttonNumber)
                        if (inp) working = working +"$it: $inp  "
                    }
                }
                working = working+"]"
                working = working.replaceAll('virtual:',"Sync").replaceAll('virtualOn',"Turn on").replaceAll('virtualOff',"Turn off").replaceAll('flipOn',"Turn off").replaceAll('flipOff',"Turn on")
                
                log.trace working
                index = 0
  				while (index != -1) {
                	working = working.replaceAll('\n\n\n\n',"\n\n")
                    working = working.replaceAll('\n\n\n',"\n\n")
    				index = working.indexOf('\n\n\n', index+1);
                    log.trace "first $index"
  				}
                index = 0
                while (index != -1) {
                	working = working.replaceAll('\\[\n',"[")
                    working = working.replaceAll('\n\\]',"]")
                    working = working.replaceAll('\\|On\\|\n\\|Off\\|',"|Off|")
                    working = working.replaceAll('\\|On\\|\n\nOff',"Off")
                    working = working.replaceAll('\\|On\\|\n\n',"\\|On\\|\n")
                    working = working.replaceAll('\\|Off\\|\n\n',"\\|Off\\|\n")
               		working = working.replaceAll('\\|Off\\|]',"]")
    				index = working.indexOf("[\n", 0)  
                    if (index == -1) index = working.indexOf("\n]", 0);  
                    log.trace "second $index"
				}
                

                log.trace working
                working = working.replaceAll('On:',":").replaceAll('Off:',":")
                working = working.replaceAll('master:', "-> ").replaceAll('dimMaster:', "-> ").replaceAll('colorMaster:', "-> ").replaceAll('statusValue:', "-> ")
				working = (working.size() > 0) ? working[1..-1].replaceAll('\\[',"").replaceAll('\\]',"") : "(none)"
    			href(name:"toNextPage", page:"effectPage", description: "$working",
    				title: "Effects")

            	working = ""
            	["onGate","offGate","openContact","closedContact","activeMotion","inactiveMotion","presencePresent","presenceNotPresent","modes"].each{
                	def inp = find(it,buttonNumber)
                    def itnew = inp
                    if (inp) {
 
                    	if (it == 'onGate') itnew = "If any on"
                        if (it == 'offGate') itnew = "If all off"
                        if (it == 'openContact') itnew = "If open"
                        if (it == 'closedContact') itnew = "If closed"
                        if (it == 'activeMotion') itnew = "If motion"
                        if (it == 'inactiveMotion') itnew = "If no motion"
                        if (it == 'presencePresent') itnew = "If present"
                        if (it == 'presenceNotPresent') itnew = "If not present"
                        
                        if (it == 'modes') itnew = "If modes is"
                        working = working +"\n\n$itnew: $inp"
                 	}  
                }
                working = (working.size() > 0) ? working[2..-1].replaceAll('\\[',"").replaceAll('\\]',"") : "(none)"
                working = working.replaceAll('onGate:', "If any on:").replaceAll('offGate:', "If all off:").replaceAll('modes:', "If all off:")
    			href(name:"toNextPage", page:"limitPage", description: "$working",
    				title: "Limits")                 
			}
		//}
        
        log.debug "why ${buttonNumber}"
		//if (selector) {
        	section("Admin") {
            	input("customTitle_${buttonNumber}","text",title:"",description:"custom title",required:false)
                href(name:"toNextPage", page:"deletePage",  params: [num:buttonNumber], title: "Delete: ${getTitle(buttonNumber)}")}       
    	//}
               
	}   
}

def configurePage() {
	def buttonNumber = state.buttonNumber
	dynamicPage(name: "configurePage", title: "Syncs:", nextPage: "editPage", uninstall: false) {
        log.trace "$buttonNumber isn't $state.buttonNumber"
		section(title: "Switch", hideable:false, getSwitchSection(buttonNumber)) 
		section(title: "Sensor", hideable:true, 
        		hidden: hideSection("sensors", buttonNumber), getChangeSection(buttonNumber)) 
        section(title: "State", hideable:true, 
        		hidden: hideSection("other", buttonNumber), getOtherSection(buttonNumber)) 
	}
}

def effectPage() {
	def buttonNumber = state.buttonNumber
	dynamicPage(name: "effectPage", title: "Effects", nextPage: "editPage", uninstall: false) {
		section(title: "Sync", hideable: true, 
        		hidden: hideSection("slave", buttonNumber), getSlaveSection(buttonNumber))  
		section(title: "On", hideable: true, 
        		hidden: hideSection("on", buttonNumber), getOnSection(buttonNumber))   
		section(title: "Special On", hideable: true, 
        		hidden: hideSection("specOn", buttonNumber), getSpecOnSection(buttonNumber))                   
		section(title: "Off", hideable: true, 
        		hidden: hideSection("off", buttonNumber), getOffSection(buttonNumber))
		section(title: "Special Off", hideable: true, 
        		hidden: hideSection("specOff", buttonNumber), getSpecOffSection(buttonNumber))                  
	}                
}

def limitPage() {
	def buttonNumber = state.buttonNumber
	dynamicPage(name: "limitPage", title: "Syncs:", nextPage: "editPage", uninstall: false) {
  		section(title: "Limits?", hideable: false, getLimitsSection(buttonNumber))                           
       }    
}

def getSwitchSection(buttonNumber) { return {
    input "physical_${buttonNumber}", "capability.switch", title: "", description: "Switch", multiple: true, required: false, submitOnChange: true
    if (find("physical", buttonNumber)) {
 		input "hardOn_${buttonNumber}", "bool", title: "(hard push?)", required: false
	}
    input "dimRange_${buttonNumber}", "capability.switch", title: "Dimmer", multiple: true, required: false, submitOnChange: true
    if (find("dimRange", buttonNumber)) {    
    	input "dimAbove_${buttonNumber}", "number",  title: "", description: "Above", required: false
        input "dimBelow_${buttonNumber}", "number",  title: "", description: "Below", required: false
 	}
}	} 
 
def getChangeSection(buttonNumber) { return {
	input "movement_${buttonNumber}", "capability.motionSensor", title: "Movement sensor", multiple: true, required: false
	input "contact_${buttonNumber}", "capability.contactSensor", title: "Contact sensor", multiple: true, required: false
    input "presence_${buttonNumber}", "capability.presenceSensor", title: "Presence sensor", multiple: true, required: false
    input "vibration_${buttonNumber}", "capability.accelerationSensor", title: "Vibration sensor", multiple: true, required: false    
   
    input "meter_${buttonNumber}", "capability.powerMeter", title: "Power", multiple: true, required: false, submitOnChange: true 
	if (find("meter", buttonNumber)) {
		input "meterValue_${buttonNumber}", "number",  title: "", description: "Threshold", required: true
	}
    input "temp_${buttonNumber}", "capability.temperatureMeasurement", title:  "Temperature Sensor", multiple: true, required:false, submitOnChange: true 
    if (find("temp", buttonNumber)) {
    	input "tempValue", "number", title: "", description: "Target", required: false
    }
} }

def getOtherSection(buttonNumber) { return {
	input "modeChange_${buttonNumber}", "mode", title: "Mode change", multiple: false, required: false 
	input "specialSwitch_${buttonNumber}", "capability.switch", title: "Special switch", multiple: false, required: false, submitOnChange: true         
	if (find("specialSwitch", buttonNumber)) {
   		input "specialValue_${buttonNumber}", "text", title: "", description:"Answer", required: true
    }
    input "buttonSwitch_${buttonNumber}", "capability.button", title: "Button(s)", multiple: false, required: false,submitOnChange: true 
	if (find("buttonSwitch", buttonNumber)) {
   		input "buttonValue_${buttonNumber}", "text", title: "", description:"Button number", required: true
        input "buttonHeld_${buttonNumber}", "bool", title: "Held?", required: false
    }    
}   }

def getSlaveSection(buttonNumber) { return {
	input "virtual_${buttonNumber}", "capability.switch", title: "Sync", multiple: true, required: false, submitOnChange: true
	if (find("virtual", buttonNumber)) {
    	input "syncDim_${buttonNumber}", "bool", title: "Dim?", required: false, submitOnChange: true  
    	if (find("syncDim", buttonNumber)) {
			input "dimOnly_${buttonNumber}", "bool", title: "(dim only?)", required: false  
    	}
    	input "offCallback_${buttonNumber}", "bool", title: "Callback (on off())?", required: false, submitOnChange: true  
        if (find("offCallback", buttonNumber)) {
			input "offCallbackAny_${buttonNumber}", "bool", title: "...even just one?", required: false  
        }   
     }
	input "color_${buttonNumber}", "capability.colorControl", title: "Colors?", multiple: true, required: false, submitOnChange: true
} }

def getOnSection(buttonNumber) { return {   
	input "virtualOn_${buttonNumber}", "capability.switch", title: "", description: "On --> On()", multiple: true, required: false
    input "flipOn_${buttonNumber}", "capability.switch", title: "", description:"On --> Off()", multiple: true, required: false
    
    input "mimicOn_${buttonNumber}", "capability.switch",  title: "", description:"On --> Mimic", multiple: true, required:false, submitOnChange: true
    if (find("mimicOn", buttonNumber)) {
    	input "masterOn_${buttonNumber}", "capability.switch",  title: "", description: "Master switch:", required: true, multiple: false       
	}
    
    input "dimOn_${buttonNumber}", "capability.switchLevel", title: "Set dimmer", multiple: true, required: false, submitOnChange: true
    if (find("dimOn", buttonNumber)) {
   		input "dimOnlyOn_${buttonNumber}", "bool", title: "(dim only?)", required: false  
		input "dimValueOn_${buttonNumber}", "text",  title: "", description: "Dim value:", required: false
		input "dimMasterOn_${buttonNumber}", "capability.switchLevel",  title: "", description: "Dim master:", required: false         
    }
	input "colorOn_${buttonNumber}", "capability.colorControl", title: "Set color:", multiple: true, required: false, submitOnChange: true
    if (find("colorOn", buttonNumber)) {
		input "colorValueOn_${buttonNumber}", "text",  title: "", description: "color value:", required: false
		input "colorMasterOn_${buttonNumber}", "capability.colorControl", title: "Color master:", multiple: false, required: false, submitOnChange: true        
    }    
    
    input "statusOn_${buttonNumber}", "capability.switch", title: "Set status", multiple: true, required: false, submitOnChange: true
    if (find("statusOn", buttonNumber)) {
		input "statusValueOn_${buttonNumber}", "text",  title: "", description: "status value:", required: false
	}
   
    //input "waitOn_${buttonNumber}", "number", title: "Delay (mins):", required: false
}	}

def getSpecOnSection(buttonNumber) { return {  
	input "flashOn_${buttonNumber}", "capability.switch", title: "Flash:", required: false, multiple: true, submitOnChange: true
    if (find("flashOn", buttonNumber)) {
		input "numFlashOn_${buttonNumber}", "number", title: "This number of times (default 3)", required: false
		input "flashOnOn_${buttonNumber}", "number", title: "On for (default 1000)", required: false
		input "flashOffOn_${buttonNumber}", "number", title: "Off for (default 1000)", required: false
	}
	input "setModeOn_${buttonNumber}", "mode",  title: "", description: "Change mode to:", multiple: false, required: false, submitOnChange: true
		def phrases = location.helloHome?.getPhrases()*.label
	input "phraseOn_${buttonNumber}", "enum",  title: "", description: "Activate routine:", required: false, options: phrases
	input "indicatorOn_${buttonNumber}", "capability.indicator", title: "Indicator", multiple: true, required: false, submitOnChange: true
	if (find("indicatorOn", buttonNumber)) {
    	input "indicatorSetOn_${buttonNumber}", "bool", title: "On?", required: false, submitOnChange: true  
    }      
	input "buttonDisplayOn_${buttonNumber}", "capability.button", title: "Button display", multiple: true, required: false, submitOnChange: true
	if (find("buttonDisplayOn", buttonNumber)) {
       	input "goDisplayOn_${buttonNumber}", "text", title: "", description:"Go display:", required: false
       	input "goButtonOn_${buttonNumber}", "text", title: "", description:"Go button:", required: false     
       	input "ledOnOn_${buttonNumber}", "text", title: "", description:"Turn on:", required: false         
    }   
    
    input "msgOn_${buttonNumber}", "text", title: "Message", description: "Internal", required: false
    if (find("msgOn", buttonNumber)) {	
  		input "msgPushOn_${buttonNumber}", "bool", title: "Alert?", required: false, submitOnChange: true 
  		input "msgFeedOn_${buttonNumber}", "bool", title: "Feed?", required: false, submitOnChange: true      
	}

    input "messageOn_${buttonNumber}", "text", title: "", description: "SMS", required: false, submitOnChange: true
    if (find("messageOn", buttonNumber)) {
		input "phoneOn_${buttonNumber}","phone" ,title: "", required: false
    }    
} }

def getOffSection(buttonNumber) { return {
	input "virtualOff_${buttonNumber}", "capability.switch", title: "", description:"Off --> Off()", multiple: true, required: false 
	input "flipOff_${buttonNumber}", "capability.switch", title: "", description:"Off --> On()", multiple: true, required: false

    input "mimicOff_${buttonNumber}", "capability.switch",  title: "", description:"Off --> Mimic", multiple: true, required:false, submitOnChange: true    
    if (find("mimicOff", buttonNumber)) {
    	input "masterOff_${buttonNumber}", "capability.switch",  title: "", description: "Master switch:", required: true, multiple: false       
	}
    
    input "dimOff_${buttonNumber}", "capability.switchLevel", title: "Set dimmer", multiple: true, required: false, submitOnChange: true
    if (find("dimOff", buttonNumber)) {
   		input "dimOnlyOff_${buttonNumber}", "bool", title: "(dim only?)", required: false  
		input "dimValueOff_${buttonNumber}", "text",  title: "", description: "Dim value:", required: false
		input "dimMasterOff_${buttonNumber}", "capability.switchLevel",  title: "", description: "Dim mimic:", required: false        
    }
	input "colorOff_${buttonNumber}", "capability.colorControl", title: "Set color", multiple: true, required: false, submitOnChange: true
    if (find("colorOff", buttonNumber)) {
		input "colorValueOff_${buttonNumber}", "text",  title: "", description: "color value:", required: false
		input "colorMasterOff_${buttonNumber}", "capability.colorControl", title: "Color master:", multiple: false, required: false, submitOnChange: true        
    }    

	input "statusOff_${buttonNumber}", "capability.switch", title: "Set status", multiple: true, required: false, submitOnChange: true
    if (find("statusOff", buttonNumber)) {
		input "statusValueOff_${buttonNumber}", "text",  title: "", description: "status value:", required: false
	}
    
    input "waitOff_${buttonNumber}", "number", title: "Delay (mins):", required: false
}	}

def getSpecOffSection(buttonNumber) { return {   
    input "flashOff_${buttonNumber}", "capability.switch", title: "Flash:", required: false, multiple: true, submitOnChange: true
    if (find("flashOff", buttonNumber)) {
		input "numFlashOff_${buttonNumber}", "number", title: "This number of times (default 3)", required: false
		input "flashOnOff_${buttonNumber}", "number", title: "On for (default 1000)", required: false
		input "flashOffOff_${buttonNumber}", "number", title: "Off for (default 1000)", required: false
	}
    input "setModeOff_${buttonNumber}", "mode",  title: "", description: "Change mode to:", multiple: false, required: false
		def phrases = location.helloHome?.getPhrases()*.label
	input "phraseOff_${buttonNumber}", "enum",  title: "", description: "Activate routine:", required: false, options: phrases  
	input "indicatorOff_${buttonNumber}", "capability.indicator", title: "Indicator", multiple: true, required: false, submitOnChange: true
	if (find("indicatorOff", buttonNumber)) {
    	input "indicatorSetOff_${buttonNumber}", "bool", title: "On?", required: false, submitOnChange: true  
    }      
	input "buttonDisplayOff_${buttonNumber}", "capability.button", title: "Button display", multiple: true, required: false, submitOnChange: true
	if (find("buttonDisplayOff", buttonNumber)) {
       	input "goDisplayOff_${buttonNumber}", "text", title: "", description:"Go display:", required: false
       	input "goButtonOff_${buttonNumber}", "text", title: "", description:"Go button:", required: false     
       	input "ledOnOff_${buttonNumber}", "text", title: "", description:"Turn on:", required: false         
    }       
    
    input "msgOff_${buttonNumber}", "text", title: "", description: "Post message:", required: false
    if (find("msgOff", buttonNumber)) {	
  		input "msgPushOff_${buttonNumber}", "bool", title: "Alert?", required: false, submitOnChange: true 
  		input "msgFeedOff_${buttonNumber}", "bool", title: "Feed?", required: false, submitOnChange: true      
	}

    input "messageOff_${buttonNumber}", "text", title: "SMS Message:", required: false, submitOnChange: true
    if (find("messageOff", buttonNumber)) {
		input "phoneOff_${buttonNumber}","phone" ,title: "", required: false
    }       
} }

def getLimitsSection(buttonNumber) { return {
	input "onGate_${buttonNumber}", "capability.switch", title: "Only if one of these is ON:", multiple: true, required: false
	input "offGate_${buttonNumber}", "capability.switch", title: "Only if ALL of these are OFF:", multiple: true, required: false
	input "modes_${buttonNumber}", "mode", title: "Only if in one of these MODES:", multiple: true, required: false 
    input "openContact_${buttonNumber}", "capability.contactSensor", title: "Only if one of these is OPEN:", multiple: true, required: false
    input "closedContact_${buttonNumber}", "capability.contactSensor", title: "Only if ALL of these are CLOSED:", multiple: true, required: false
    input "activeMotion_${buttonNumber}", "capability.motionSensor", title: "Only if one of these is ACTIVE", multiple: true, required: false
    input "inactiveMotion_${buttonNumber}", "capability.motionSensor", title: "Only if ALL of these are INACTIVE", multiple: true, required: false
    input "presencePresent_${buttonNumber}", "capability.presenceSensor", title: "Only if ALL of these are PRESENT", multiple: true, required: false
    input "presenceNotPresent_${buttonNumber}", "capability.presenceSensor", title: "Only if ALL of these are NOT PRESENT", multiple: true, required: false

} }


private hideSection(lead, buttonNumber) {
  def res = find(lead, buttonNumber)
  if (!res)
  switch(lead) { 
      case "sensors": 	res=find("movement", buttonNumber) || find("contact", buttonNumber) || find("meter", buttonNumber) || 
      						find("presence", buttonNumber) || find("vibration", buttonNumber) ||
      						find("meterValue", buttonNumber) || find("temp", buttonNumber)
                        	break  
      case "other": 	res=find("modeChange", buttonNumber) || find("specialSwitch", buttonNumber) || 
      						find("dimRange", buttonNumber) || find("buttonSwitch", buttonNumber) 
							break  
      case "slave": 	res=find("virtual", buttonNumber) || find("color", buttonNumber) 
                        	break
      case "on": 		res=find("virtualOn", buttonNumber) || find("flipOn", buttonNumber) || 
      						find("mimicOn", buttonNumber) || find("masterOn", buttonNumber) ||
     					    find("dimOn", buttonNumber) || find("dimValueOn", buttonNumber) || find("dimMasterOn", buttonNumber) ||
                            find("colorOn", buttonNumber) || find("colorValueOn", buttonNumber) || find("colorMasterOn", buttonNumber) ||
                            find("statusOn", buttonNumber) || find("statusValueOn", buttonNumber) ||  
                            find("waitOn", buttonNumber)
      						break                            
      case "specOn":    res=find("indicatorOn", buttonNumber) ||
      						find("setModeOn", buttonNumber) || find("flashOn", buttonNumber) || 
      						find("phraseOn", buttonNumber) || find("msgOn", buttonNumber) || find("messageOn", buttonNumber) ||
                            find("indicatorOn", buttonNumber) || find("buttonDisplayOn", buttonNumber) || find("goDisplayOn", buttonNumber) ||
                            find("goButtonOn", buttonNumber) || find("ledOn", buttonNumber)
                        	break 
                            
      case "off": 		res=find("virtualOff", buttonNumber) || find("flipOff", buttonNumber) ||  
      						find("mimicOff", buttonNumber) || find("masterOff", buttonNumber) ||
      						find("dimOff", buttonNumber) || find("dimValueOff", buttonNumber) || find("dimMasterOff", buttonNumber) ||
                            find("colorOff", buttonNumber) || find("colorValueOff", buttonNumber) || find("colorMasterOff", buttonNumber) ||
                            find("statusOff", buttonNumber) || find("statusValueOff", buttonNumber) ||
                            find("waitOff", buttonNumber)
                            break                                                     
      case "specOff":    res=find("indicatorOff", buttonNumber) ||
      						find("setModeOff", buttonNumber) || find("flashOff", buttonNumber) || 
      						find("phraseOff", buttonNumber)|| find("msgOff", buttonNumber) || find("messageOff", buttonNumber) ||
                            find("indicatorOff", buttonNumber) || find("buttonDisplayOff", buttonNumber) || find("goDisplayOff", buttonNumber) ||
                            find("goButtonOff", buttonNumber) || find("ledOff", buttonNumber) 
                        	break  
                            
	  case "limits": 	res=find("onGate", buttonNumber) || find("offGate", buttonNumber) ||
     						find("openContact", buttonNumber) || find("closedContact", buttonNumber) || 
                            find("activeMotion", buttonNumber) || find("inactiveMotion", buttonNumber) ||
                            find("presencePresent", buttonNumber) || find("presenceNotPresent", buttonNumber) ||
  							find("modes", buttonNumber)
                        	break
  }                      
  
  return res ? false : true
}

def deletePage(params) {
	def buttonNumber = state.buttonNumber
	def inputs = ["physical","hardOn",
    			  "movement","contact","presence","vibration","meter","temp","tempValue","modeChange","specialSwitch","specialValue",
                  "buttonSwitch","buttonValue","buttonHeld",
                  "dimRange", "dimAbove","dimBelow",
    			  "virtual","syncDim","dimOnly","hardOn","color",
                  "offCallback","offCallbackAny",
    			  "virtualOn","flipOn","mimicOn", "masterOn","dimOn","dimValueOn", "dimMasterOn", "dimOnlyOn",
                  "statusOn","statusValueOn","waitOn",
                  "colorOn","colorValueOn","colorMasterOn","phoneOn", "messageOn", "indicatorOn",
                  "setModeOn","phraseOn","msgOn","phoneOn","messageOn","flashOn","numFlashOn",
                  "indicatorOn","buttonDisplayOn","goDisplayOn","goButtonOn","ledOn", 
                  "virtualOff", "flipOff","mimicOff", "masterOff", "dimOff","dimValueOff", "dimMasterOff", "dimOnlyOff",
                  "statusOff","statusValueOff","waitOff",
                  "colorOff","colorValueOff","colorMasterOff","phoneOff", "messageOff", "indicatorOff",
                  "setModeOff","phraseOff","msgOff","phoneOff","messageOff","flashOff","numFlashOff",
                  "indicatorOff","buttonDisplayOff","goDisplayOff","goButtonOff","ledOff",
                  "onGate","offGate","openContact","closedContact","activeMotion","inactiveMotion","presencePresent","presenceNotPresent","modes",
                  "customTitle"]

	dynamicPage(name: "deletePage", title: "Deleting...", nextPage: "firstPage") {
        section("Confirm delete") {
            def working = ""
            inputs.each{
                def inp = find(it,state.buttonNumber)
              	if (inp) working = working +"\n\n$it: $inp"
            }
            working = (working.size() > 0) ? working[2..-1].replaceAll('\\[',"").replaceAll('\\]',"") : "(none)"
            paragraph "$working"            
        }
	section(title: "Dump", hideable:true, hidden:true){ 
    	input(name: "selector", type: "enum", title: "", description: "Assign", options: ["delete"],submitOnChange: true,required:true)
    	inputs.each {input(name: "${it}_${buttonNumber}", type: "enum", title: "Wipe!", required: false, options: [null])}      
    }    

    log.debug "but: $settings"
	}

}

def find(type, buttonNumber) {
	def preferenceName = type + "_" + buttonNumber
	def pref = settings[preferenceName]
	if(pref != null) {
		log.debug "Found: $pref for $preferenceName"
	}

	return pref
}

def setFind(type, buttonNumber, val) {
  def preferenceName = type + "_" + buttonNumber
  settings[preferenceName] = val
  log.trace "$preferenceName should be $val now..." 
}

def getTitle(buttonNumber) {

 	def title = find("customTitle", buttonNumber)
  	if (title) return "$title"
    else return getCauses(buttonNumber)
}

def getCauses(buttonNumber) {
    def causes = []
    ["physical","specialSwitch","buttonSwitch","dimRange","movement","contact","presence","vibration","meter","temp","modeChange"].each{ causeType ->
    	def cause = find("$causeType", buttonNumber)
        if (cause) causes << cause
    } 
    
    if (causes.size() > 0) return causes.join(",").replaceAll('\\[',"").replaceAll('\\]',"")
    else return null
}    

def subscriber() {
for (buttonNumber in 1 .. 99) { 
    def physical = find('physical', buttonNumber)
	def movement = find('movement', buttonNumber)
    def contact = find('contact', buttonNumber)
    def presence = find('contact', buttonNumber)
    def vibration = find('vibration', buttonNumber)
    def modeChange = find('modeChange', buttonNumber)
    def specialSwitch = find('specialSwitch', buttonNumber)
    def buttonSwitch = find('buttonSwitch', buttonNumber)
    
	def meter = find('meter', buttonNumber)
    def temp = find('temp', buttonNumber)
    def colors = find('color',buttonNumber)
    
    def virtual = find('virtual', buttonNumber)
    def syncDim = find('syncDim', buttonNumber)
    def dimOnly = find('dimOnly', buttonNumber)
    def dimRange = find('dimRange', buttonNumber)

    if (physical) {
    	subscribe(physical, "switch", "fwd${buttonNumber}") 
       	if (syncDim)  subscribe(physical, "level", "fwd${buttonNumber}") 
        if (colors) subscribe (physical, "color", "fwd${buttonNumber}")  
    }    
    if (movement) subscribe(movement, "motion", "fwd${buttonNumber}")  
	if (contact) subscribe(contact, "contact", "fwd${buttonNumber}")
    if (presence) subscribe(presence, "presence", "fwd${buttonNumber}")
    if (vibration) subscribe(vibration, "acceleration", "fwd${buttonNumber}")
    if (modeChange) subscribe(location, "fwd${buttonNumber}")    
    if (specialSwitch) subscribe(specialSwitch, "status", "fwd${buttonNumber}") 
    if (buttonSwitch) subscribe(buttonSwitch, "button", "fwd${buttonNumber}")     
 	if (meter) subscribe(meter, "power", "fwd${buttonNumber}")
    if (dimRange) subscribe(dimRange, "level", "fwd${buttonNumber}") 
    if (temp) subscribe(temp, "temperature", "fwd${buttonNumber}")
    
    if (virtual) { 
      	if (find('offCallback', buttonNumber)) virtual.each() {
      		subscribe(it, "switch.off", "bck${buttonNumber}")
      	}
    }
}	}

def updated(){
	log.debug "Update: $settings"
	unsubscribe()
	subscriber()
}

def installed() {
	subscriber()
}

def refreshHandler(sws) {
    sws?.refresh()
    sws?.poll()
}

def checked(input) {
  if (input) {
  
  } else return false
}

def gateCheck(buttonNumber){

    def onGates = find('onGate', buttonNumber)
  	def offGates = find('offGate', buttonNumber)
    def openGates = find('openContact', buttonNumber)
    def closedGates = find('closedContact', buttonNumber) 
    def activeGates = find('activeMotion', buttonNumber)  
    def inactiveGates = find('inactiveMotion', buttonNumber)
    def presentGates = find('presencePresent', buttonNumber)
    def notPresentGates = find('presenceNotPresent', buttonNumber)
    
	def onGatePass = true
	if (onGates) {
  		onGatePass = onGates?.any { it.currentValue('switch').contains("on") } 
    }
	def offGatePass = true
	if (offGates) {
  		offGatePass = !(offGates?.any { it.currentValue('switch').contains("on") })
    }
	def openGatePass = true
	if (openGates) {
  		openGatePass = openGates?.any { it.currentValue('contact').contains("open") } 
    }    
    def closedGatePass = true
	if (closedGates) {
  		closedGatePass = !(closedGates?.any { it.currentValue('contact').contains("open") }) 
    }        
    def activeGatePass = true
	if (activeGates) {
  		activeGatePass = !(activeGates?.any { it.currentValue('motion').contains("inactive") } )
    }      
    def inactiveGatePass = true
	if (inactiveGates) {
  		inactiveGatePass = !(inactiveGates?.any { (it.currentValue('motion') == "active") } )
    }    
    def presentGatePass = true
	if (presentGates) {
  		presentGatePass = !(presentGates?.any { (it.currentValue('presence') == "present") } )
    }    
    def notPresentGatePass = true
	if (notPresentGates) {
  		notPresentGatePass = !(notPresentGates?.any { (it.currentValue('presence') == "not present") } )
    }    
    
    log.trace "$onGatePass && $offGatePass && $openGatePass && $closedGatePass && $activeGatePass && $inactiveGatePass"
    return onGatePass && offGatePass && 
    	openGatePass && closedGatePass && 
        activeGatePass && inactiveGatePass
}

def switchHandler(evt, buttonNumber) {
	def val = evt.value
    def cat = evt.name
    def typ = evt.type
    def dev = evt.displayName
    def desc = evt.stringValue
    def hardOns = find('hardOn', buttonNumber)

    def dimRange =  find('dimRange', buttonNumber)
    def meterValue = find('meterValue', buttonNumber) 
    def tempValue = find('tempValue', buttonNumber) 
    def dimAbove = find('dimAbove', buttonNumber)
    def dimBelow = find('dimBelow', buttonNumber)
    log.trace "${cat} is calling ${val}"
    log.trace "Specifically: ${dev} is ${typ} from ${evt.source} with ${evt.data} and: ${evt.descriptionText}"
    

	// 			SYNC BLOCK		//       
    if (cat == "level" && find('syncDim', buttonNumber)) {
        def dims = find('virtual', buttonNumber)
        log.debug "making it dim to ${dims}"
      	if (find('dimOnly', buttonNumber)) {
            dims?.each() {if (it.currentValue('switch')?.contains('on')) 
            {it.setLevel(evt.value)}}
        } else dims?.setLevel(evt.value) 
      	log.debug "${dims*.currentValue('switch')}"
    }  
    
    if (cat == "color") {
        def hue
        def saturation
        def colors = find('color', buttonNumber)
 		find('physical', buttonNumber).each() {
            hue = 	it.currentValue("hue")
        	saturation = it.currentValue("saturation")
        }          
        log.trace "final hue | saturation is $hue | $saturation --  value is $val"
        Map color =  [hue: hue, saturation: saturation, hex: val]
	 	
        colors.each() {if (it.currentValue('switch').contains('on')) {    	
 		  it.setColor(hex: val)
		  it.setHue(hue)
       	  it.setSaturation(saturation)
        }  }
  	}
    
	// 			SYNC BLOCK		//      
    

	// 			On / Off BLOCK		//    
    if (cat == "switch") {cat = (!hardOns || typ == "physical") ? "OnOff" : "ignore"}

    if (cat == "status") val = (find('specialValue', buttonNumber) == val)  ? "On" : "ignore"
//    if (cat == "status") val = (find('specialValue', buttonNumber)?.contains(val))  ? "On" : "ignore"
    
    if (cat == "button") {

		val = getButtonVal(evt.value, evt.data)
        log.debug "button hit: $val"
            
        def buttonValue = find('buttonValue', buttonNumber)
    	def buttonHeld = find('buttonHeld', buttonNumber)            
    	val = (buttonValue == val && (!buttonHeld ^ evt.value.contains("held")))  ? "On" : "Off"  
    }  
    
    if (cat == "mode") val = (val==find("modeChange", buttonNumber)) ? "On" : "Off"  

    if (cat == "level" && dimRange) {
    	def valInt = val as Integer
        desc = valInt as String
        val = ((valInt >= dimAbove || !dimAbove) && (valInt <= dimBelow || !dimBelow)) ? "On" : "Off" 
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
    
    cat = (["motion","contact","presence","acceleration","power","status","button", "mode", "temperature"].contains(cat)) ? "OnOff" : cat
     
    val = (["active", "open", "present"].contains(val)) ? "on" : val 
    val = (["inactive", "closed", "not present"].contains(val)) ? "off" : val 
  
    if (val=="on") val="On"
    if (val=="off") val="Off"    

	log.trace "Formated as: ${cat} is calling ${val}"    
     
	if (cat == "OnOff" && ["On","Off"].contains(val)) {
    
        def waitTime = find("wait$val", buttonNumber) ?: 0
        waitTime = waitTime * 60//waitTime * 60000
       
		def slave = find('virtual', buttonNumber)
   		if (slave && !find('dimOnly', buttonNumber)) {
    		log.trace "switch syncing: $slave to $val"
			if (val=="On") {slave*.on()} else {slave*.off()}
    	}    
        
		def switches = find("virtual$val", buttonNumber)
        def flips = find("flip$val", buttonNumber) 

		if (val == "On" && switches) {//switches*.on([delay: waitTime])
        	if (waitTime == 0) switches*.on()
            //else delayCmd([subj: "switches_" + buttonNumber, cmd: "on"])//runIn(10, delayCmd, [overwrite: false, data: [subj: "switches_" + buttonNumber, cmd: "on"]])
        }    
        
        if (val == "Off" && flips) {//flips*.on([delay: waitTime])    
        	if (waitTime == 0) flips*.on()
           // else runIn(10, delayCmd, [overwrite: false, data: [subj: "flips_" + buttonNumber, cmd: "on"]])
		}
        
		def mimics = find("mimic$val", buttonNumber)// ?: []
        def master = find("master$val", buttonNumber)
        if (mimics) { if (master*.currentValue('switch').contains("on")) {
            if (waitTime == 0) mimics*.on()
           // else runIn(waitTime, delayCmd, [overwrite: false, data: [subj: mimics, cmd: "on"]])
		 } }
                
        
        def dims = find("dim$val", buttonNumber)
        if (dims) {
        	def dimVals = find("dimValue$val", buttonNumber) as String
        	if (!dimVals) dimVals = find("dimMaster$val", buttonNumber)?.currentValue('level')   
                       	
			dims?.each() {
            	if ((it.currentValue('switch')?.contains('on')) || (!find("dimOnly$val", buttonNumber))) {
                	if ((dimVals.toString().contains("+")) || (dimVals.toString().contains("-"))) {
                    	def curDim = it.currentValue('level') as Integer
                		def newDim = dimVals as Integer
                        if ((curDim + newDim) < 0) newDim = 0
                        log.debug "dimVals $dimVals = newDim $newDim + curDim $curDim and ${dimVals.contains("-")} or ${dimVals.contains("+")}"
						dimVals = curDim + newDim
                     }   
                     //it.setLevel(dimVals, [delay: waitTime])
            		if (waitTime == 0) it.setLevel(dimVals)
           			//else runIn(waitTime, delayCmd, [overwrite: false, data: [subj: it, cmd: "level",value:dimVals]])                     
                 }
             }    	
        }

        def colors = find("color$val", buttonNumber)
		if (colors)   {
            def hueColor
        	def saturation
            def colorTemp = 3500
			def colorVals = find("colorValue$val", buttonNumber)
        	if (colorVals) { 
            	saturation = 100
           		switch(colorVals) {
				case "Blue":
					hueColor = 65
					break;
				case "Red":
					hueColor = 1
					break;
				case "Yellow":
					hueColor = 15
					break;
				case "Green":
					hueColor = 32
					break;
				case "White":
					hueColor = 0
                    saturation = 0
					break;                    
				case "Purple":
					hueColor = 90
                    saturation = 90
					break;     
				case "Orange":
					hueColor = 11
					break;  
                case "Cold":
                	hueColor = 56
                    saturation = 16
                    colorTemp = 8000
                	break;
                default:
					hueColor = 4
					break;  
			}	} else {
        		def colorMaster = find("colorMaster$val", buttonNumber)
                if (colorMaster) {
            		hueColor = colorMaster.latestValue("hue")
            		saturation = colorMaster.latestValue("saturation")
                    if (colorMaster.hasAttribute("colorTemperatur"))
                    	colorTemp = colorMaster.latestValue("colorTemperature")
            	}
        	}    
        	log.trace "color = $hueColor"
            log.trace "colors are ${colors.currentValue('switch')}"
            colors.each() {if (it.currentValue('switch').contains('on')) {
            	//log.debug "the problem is $it"
                if (it.currentValue("colorTemperature") != colorTemp)
                it.setColorTemperature(colorTemp, [delay: waitTime])                
         		it.setHue(hueColor, [delay: waitTime])                
                it.setSaturation(saturation, [delay: waitTime])
                it.refresh()
                if (it.currentValue('colorTemperature') != colorTemp)
                it.setColorTemperature(colorTemp, [delay: waitTime])
                it.setHue(hueColor, [delay: waitTime])                
                it.setSaturation(saturation, [delay: waitTime]) 
            }	}
        }
        
		def flashes = find("flash$val", buttonNumber) 
        if (flashes) {
        	def numFlashes = find("numFlash$val", buttonNumber) 
            def flashOn = find("flashOn$val", buttonNumber)  
            def flashOff = find("flashOn$val", buttonNumber)  
            flashLights(flashes, numFlashes, flashOn, flashOff)
        }        


		if (mimics) { if (master*.currentValue('switch').contains("off")) 
        
        mimics*.off([delay: waitTime]) }

		if (val == "Off" && switches) {//switches*.off([delay: waitTime]) 
            if (waitTime == 0) switches*.off()
           // else runIn(waitTime, delayCmd, [overwrite: false, data: [subj: switches, cmd: "off"]])
        }
        if (val == "On" && flips) {//flips*.off([delay: waitTime])
            if (waitTime == 0) flips*.off()
          //  else runIn(waitTime, delayCmd, [overwrite: false, data: [subj: flips, cmd: "off"]])
        }
       

//	NON-LIGHT RELATED STUFF...


		def status = find ("status$val", buttonNumber)
        if (status) {
        	def statusVal = find ("statusValue$val", buttonNumber)
        	status.each() { 
            	log.debug "$it is ${it.hasCommand("setStatus")}"
            	if (it.hasCommand("setStatus")) {status.setStatus("$statusVal") }	}
        }
        
        def indicator = find("indicator$val", buttonNumber) 
        if (indicator) { 
        	def indicatorSet = find("indicatorSet$val", buttonNumber) 
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
        
        def buttonDisplay = find("buttonDisplay$val", buttonNumber)  
        log.debug "buttonDisplay $buttonDisplay"
        if (buttonDisplay) {
        	def displayNum = find("goDisplay$val", buttonNumber) 
            def buttonNum = find("goButton$val", buttonNumber)
            def ledNum = find("ledOn$val", buttonNumber) 
        	if (displayNum) buttonDisplay.goDisplay(displayNum)
            if (buttonNum) buttonDisplay.goButton(buttonNum)
            if (ledNum) buttonDisplay.turnOn(ledNum)
            log.debug "goDisplay( $displayNum ) | goButton( $buttonNum ) | turnOn( $ledNum )" 
        }
            
            
        def setModes = find("setMode$val", buttonNumber)
        if (setModes) setLocationMode(setModes)

 		def phrases = find("phrase$val", buttonNumber)
        if (phrases) location.helloHome.execute(phrases)
        
        def msg = find("msg$val", buttonNumber)
        if (msg) {
        	if (find("msgPush$val", buttonNumber)) sendPushMessage("$msg")
            if (find("msgFeed$val", buttonNumber)) sendNotificationEvent("$msg")
        	//sendPush("$msg")
        }    
              
        def phoneMsg = find("message$val", buttonNumber)
        if (phoneMsg) {
        	if (phoneMsg == "Stat") phoneMsg = "$dev is $desc" 
			sendSmsMessage(find("phone$val", buttonNumber) ?: "(202) 679-2566", phoneMsg)
		}
    }       
}

def callbackHandler(evt, buttonNumber) {
	def	physical = find('physical', buttonNumber)
    def virtual = find('virtual', buttonNumber)
    if (physical && virtual) {
		def allOff = !(virtual?.any { it.currentValue('switch').contains("on") })
		if (allOff || find('offCallbackAny', buttonNumber)) {
        	physical*.off()
        }
	}
}

def methodMissing(String name, args) {
	log.debug "--- $name"
	if (args && name != "subscribe")  log.debug "--- (${args[0]?.type}), (${args[0]?.name}), (${args[0]?.value}), (${args[0]?.data}) and <${args[0]?.descriptionText}>"

	def buttonNumber = name.find(/\d+/) {m -> return "$m"} 
    
    
   	log.debug "$buttonNumber is ${gateCheck(buttonNumber)}" 
    
	def modes = find('modes', buttonNumber)


  	def isMode = !modes || modes.contains(location.mode)
  	if (isMode && gateCheck(buttonNumber)) {
    
    	if (name.startsWith("fwd")) switchHandler(args[0], buttonNumber)
    	if (name.startsWith("bck")) callbackHandler(args[0], buttonNumber)
    }
}

def delayCmd(data) {
	log.debug "delay Command... $data"
    log.debug "...and $data.cmd for $data.subj with $data.value"
    def pref = "${data.subj}_Count"
    settings[pref] = 10
    log.debug "pref = $pref is ${settings[pref]}"
    
    runIn(10, delayed, [overwrite: false, data: [subj: data.subj, cmd: data.cmd, value: data.value]])

}

def delayed(data) {
	log.debug "DELAYED data = $data"
    def pref = "${data.subj}_Count"
	log.debug "DELAYED pref = $pref is DELAYED ${settings[pref]}"
}

private flashLights(switches, numFlash, OnFlash, offFlash) {
	log.debug "($switches, $numFlash, $OnFlash, $offFlash)"
	def doFlash = true
	def onFor = onFlash ?: 1000
	def offFor = offFlash ?: 1000
	def numFlashes = numFlash ?: 3

	log.debug "LAST ACTIVATED IS: ${state.lastActivated}"
	if (state.lastActivated) {
		def elapsed = now() - state.lastActivated
		def sequenceTime = (numFlashes + 1) * (onFor + offFor)
		doFlash = elapsed > sequenceTime
		log.debug "DO FLASH: $doFlash, ELAPSED: $elapsed, LAST ACTIVATED: ${state.lastActivated}"
	}

	if (doFlash) {
		log.debug "FLASHING $numFlashes times"
		state.lastActivated = now()
		log.debug "LAST ACTIVATED SET TO: ${state.lastActivated}"
		def initialActionOn = switches.collect{it.currentSwitch != "on"}
		def delay = 0L
		numFlashes.times {
			log.trace "Switch on after  $delay msec"
			switches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.on(delay: delay)
				}
				else {
					s.off(delay:delay)
				}
			}
			delay += onFor
			log.trace "Switch off after $delay msec"
			switches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.off(delay: delay)
				}
				else {
					s.on(delay:delay)
				}
			}
			delay += offFor
		}
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
