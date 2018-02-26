/**
*  Total Comfort API
*   
*  Based on Code by Eric Thomas & Bob Jase
*/

preferences {
    input("username", "text", title: "Username", description: "Your Total Comfort User Name", required: true)
    input("password", "password", title: "Password", description: "Your Total Comfort password",required: true)
    input("honeywelldevice", "text", title: "Device ID", description: "Your Device ID", required: true)
    input ("enableOutdoorTemps", "enum", title: "Do you have the optional outdoor temperature sensor and want to enable it?", options: ["Yes", "No"], required: false, defaultValue: "No")
    input ("tempScale", "enum", title: "Fahrenheit or Celsius?", options: ["F", "C"], required: true)
    
    input("tzOffset", "text", title: "Time zone offset +/-xx?", required: false, defaultValue: "-4", description: "Time Zone Offset ie -5.")  
  	input("scLow", "number", title: "Low Shortcut:", required: false, defaultValue: 65, description: "")  
  	input("scMid", "number", title: "Mid Shortcut:", required: false, defaultValue: 70, description: "")  
  	input("scHigh", "number", title: "High Shortcut:", required: false, defaultValue: 73, description: "")  
}

metadata {
    definition (name: "Total Comfort", namespace: 
                "Thermostat", author: "Josh") {
        capability "Polling"
        capability "Thermostat"
        capability "Refresh"
        capability "Temperature Measurement"
        capability "Sensor"
        capability "Relative Humidity Measurement" 
        command "temperatureUp"
		command "temperatureDown" 
        command "heatLevelUp"
        command "heatLevelDown"
        command "coolLevelUp"
        command "coolLevelDown"
        command "setFollowSchedule"
    command "getStatus"
    command "fanOn"
    command "fanAuto"
    command "fanCirculate"
    command "goLow"
    command "goMid"
    command "goHigh"
    command "toggle"
    command "toggleHome"
    attribute "disp", "string"
    attribute "scLow", "number"
    attribute "scMid", "number"
    attribute "scHigh", "number"
        attribute "outdoorHumidity", "number"
        attribute "outdoorTemperature", "number"
        attribute "lastUpdate", "string"
        attribute "followSchedule", "string"
    }

    simulator {
        // TODO: define status and reply messages here
    }

   tiles (scale: 2) {
   	multiAttributeTile(name:"thermostatMulti", type:"thermostat", width:6, height:4) {
  		tileAttribute("disp", key: "PRIMARY_CONTROL") {
   			//attributeState("load", label: '...', backgroundColor: "#44b621", icon:"polling.poll", nextState:"default")
                		
            attributeState("default", label:'${currentValue}', action: "toggle", icon: "",nextState:"0")
			attributeState("0", label:'', action: "toggle", icon: "st.Outdoor.outdoor19" ,nextState:"0b")
            attributeState("0b", label:'', icon: "st.secondary.refresh")
            
            attributeState("c0", label: 'Cool', icon:"st.Weather.weather7", action: "toggle", nextState:"h0b")
            attributeState("c0b", label: 'Cool', icon:"st.secondary.refresh-icon") 
            attributeState("h0", label: 'Heat', icon:"st.Weather.weather14", action: "toggle", nextState:"0b")
            attributeState("h0b", label: 'Heat', icon:"st.secondary.refresh-icon") 

            
            attributeState("c1", label: 'Cool to Low Preset', icon:"st.thermostat.ac.air-conditioning", action: "toggle", nextState:"c2b")
            attributeState("c1b", label: 'Cool to Low Preset', icon:"st.secondary.refresh-icon") 
            attributeState("c2", label: 'Cool Mid', icon:"st.Weather.weather2", action: "toggle", nextState:"3b")
            attributeState("c2b", label: 'Cool Mid', icon:"st.secondary.refresh-icon")
            
            attributeState("h1", label: 'Heat to High Preset', icon:"st.thermostat.heating", action: "toggle", nextState:"h2b")
            attributeState("h1b", label: 'Heat to High Preset', icon:"st.secondary.refresh-icon") 
            attributeState("h2", label: 'Heat Mid', icon:"st.Weather.weather2", action: "toggle", nextState:"3b")
            attributeState("h2b", label: 'Heat Mid', icon:"st.secondary.refresh-icon")
            
            attributeState("3", label: 'System Off', icon:"st.vents.vent", action: "toggle", nextState:"0b")
            attributeState("3b", label: 'System Off', icon:"st.secondary.refresh-icon") //st.samsung.da.oven_ic_send
    		//attributeState("default", label:'${currentValue}', action: "getStatus", backgroundColor: "#44b621", unit:"dF",icon: " ",nextState:"load")
  		}
  		tileAttribute("device.temperature", key: "VALUE_CONTROL") {
    		//attributeState("default", action: "setTemperature")
            attributeState("VALUE_UP", label:'${currentValue}°', action: "temperatureUp")
   			attributeState("VALUE_DOWN", label:'${currentValue}°', action: "temperatureDown") 
 		 }
  		tileAttribute("device.humidity", key: "SECONDARY_CONTROL") {
    		attributeState("default", label:'${currentValue}%', icon:"st.alarm.water.wet")
  		}
  		tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
    		attributeState("Idle",  backgroundColor:"#ffffff")
    		attributeState("Heating", backgroundColor:"#E14902") //E14902 //ce4302
            attributeState("heating", backgroundColor:"#fe8f5b")
            attributeState("Waiting for Heater", backgroundColor:"#ffb999")
            
            attributeState("cooling", backgroundColor:"#74c2e6") //64bae3
    		attributeState("Cooling", backgroundColor:"#2082b1") //238fc1
            attributeState("Waiting for A/C", backgroundColor:"#b5def2")
  		}
  		tileAttribute("device.thermostatMode", key: "THERMOSTAT_MODE") {
    		attributeState("off", label:'${name}')
    		attributeState("heat", label:'Heater')
  		    attributeState("cool", label:'A/C')
   		   attributeState("auto", label:'${name}')
  		}
  		tileAttribute("device.heatingSetpoint", key: "HEATING_SETPOINT") {
    		attributeState("default", label:'${currentValue}', unit:"dF")
  		}
  		tileAttribute("device.coolingSetpoint", key: "COOLING_SETPOINT") {
    		attributeState("default", label:'${currentValue}', unit:"dF")
  		}
  	}
  
        valueTile("temperature", "device.temperature", width: 2, height: 2, canChangeIcon: true) {
            state("temperature", label: '${currentValue}°', 
             icon: "http://cdn.device-icons.smartthings.com/Weather/weather2-icn@2x.png",
             unit:"F", backgroundColors: [            
             		[value: -14, color: "#1e9cbb"],
         	        [value: -10, color: "#90d2a7"],
               		[value: -5, color: "#44b621"],
             	    [value: -2, color: "#f1d801"],
             	    [value: 0, color: "#153591"],
               	    [value: 7, color: "#1e9cbb"],
     			    [value: 15, color: "#90d2a7"],           
              		[value: 23, color: "#44b621"],
            	    [value: 29, color: "#f1d801"],
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        
        valueTile("thermostatMode", "device.thermostatMode", width: 1, height: 1) {
   			state "Default", label:'Set to ${currentValue}'
   			//state "off", label:'${name}', action:"thermostat.cool"//, icon: "st.Outdoor.outdoor19"
            //state "cool", label:'${name}', action:"thermostat.heat"//, icon: "st.Weather.weather7", backgroundColor: '#1e9cbb'
            //state "heat", label:'${name}', action:"thermostat.auto"//, icon: "st.Weather.weather14", backgroundColor: '#E14902'  
            //state "auto", label:'${name}', action:"thermostat.off"//, icon: "st.Weather.weather3", backgroundColor: '#44b621'
        }
        standardTile("thermostatFanMode", "device.thermostatFanMode", width: 2, height: 2, inactiveLabel: false) {
            state "auto", label:'${name}', action:"faOn", icon: "st.Appliances.appliances11", backgroundColor: '#44b621'
            state "circulate", label:'${name}', action:"fanAuto", icon: "st.Appliances.appliances11", backgroundColor: '#44b621'
            state "on", label:'${name}', action:"fanAuto", icon: "st.Appliances.appliances11", backgroundColor: '#44b621'
        }
        
       standardTile("coolMode", "device.thermostatMode", inactiveLabel: false, height: 2, width: 2) {
        	state "off", label:'Cool', action:"thermostat.cool", icon: "st.Outdoor.outdoor19"
            state "cool", label:'Cool', action:"thermostat.off", icon: "st.Weather.weather7", backgroundColor: '#003CEC'
            state "auto", label:'Cool', action:"thermostat.cool", icon: "st.Weather.weather3", backgroundColor: '#44b621'            
        }
        
        standardTile("heatMode", "device.thermostatMode", inactiveLabel: false, height: 2, width: 2) {
        	state "off", label:'heat', action:"thermostat.heat", icon: "st.Outdoor.outdoor19"
            state "heat", label:'heat', action:"thermostat.off", icon: "st.Weather.weather14", backgroundColor: '#E14902'
            state "auto", label:'Heat', action:"thermostat.heat", icon: "st.Weather.weather3", backgroundColor: '#44b621'
            
        }

        controlTile("coolSliderControl", "device.coolingSetpoint", "slider", height: 3, width: 1, inactiveLabel: false) {
            state "setCoolingSetpoint", label:'Set temperarure to', action:"thermostat.setCoolingSetpoint", 
            backgroundColors:[
             	    [value: 0, color: "#153591"],
               	    [value: 7, color: "#1e9cbb"],
     			    [value: 15, color: "#90d2a7"],           
              		[value: 23, color: "#44b621"],
            	    [value: 29, color: "#f1d801"],
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
            ]               
        }
       valueTile("coolingSetpoint", "device.coolingSetpoint",width: 2, height: 2, inactiveLabel: false) 
    	  {
      state "default", label:'${currentValue}°', unit:"F", inactiveLabel: false, decoration: "flat", backgroundColor: "#1450ff"
  
        }
   		valueTile("heatingSetpoint", "device.heatingSetpoint",width: 2, height: 2,  inactiveLabel: false) 
    	{
    	  state "default", label:'${currentValue}°', unit:"F",  backgroundColor: "#e03c3f"
    	}
    
        
        //tile added for operating state - Create the tiles for each possible state, look at other examples if you wish to change the icons here. 
        
         standardTile("thermostatOperatingState", "device.thermostatOperatingState",width: 2, height: 1,  inactiveLabel: false,decoration: "flat", ) {
			state "Default", label:'"${currentValue}"'
			state "Heating", label:'${name}', icon: "st.Weather.weather14"
            state "Cooling", label:'${name}', icon: "st.Weather.weather7"
            state "Idle", label:'${name}', icon: ""
            state "unknown", label:'${name}', icon: ""
        }
        
        
        
           valueTile("fanOperatingState", "device.fanOperatingState",width: 2, height: 1,  inactiveLabel: false) {
           			state "Default", label:'Fan: ${currentValue}'
            state "On", label:'${name}'//,icon: "st.Appliances.appliances11", backgroundColor : '#53a7c0'
            state "Idle", label:'${name}'//,icon: "st.Appliances.appliances11"
            state "Unknown", label:'${name}'//,icon: "st.Appliances.appliances11", backgroundColor : '#cc0000'
        }

        
        standardTile("heatLevelUp", "device.heatingSetpoint",width: 2, height: 2,  canChangeIcon: false, inactiveLabel: false) {
                        state "heatLevelUp", label:'  ', action:"heatLevelUp", icon:"st.thermostat.thermostat-up"
        }
        standardTile("heatLevelDown", "device.heatingSetpoint",width: 2, height: 2,  canChangeIcon: false, inactiveLabel: false) {
                        state "heatLevelDown", label:'  ', action:"heatLevelDown", icon:"st.thermostat.thermostat-down"
        }
        standardTile("coolLevelUp", "device.heatingSetpoint",width: 2, height: 2,  canChangeIcon: false, inactiveLabel: false) {
                        state "coolLevelUp", label:'  ', action:"coolLevelUp", icon:"st.thermostat.thermostat-up"
        }
        standardTile("coolLevelDown", "device.heatingSetpoint",width: 2, height: 2,  canChangeIcon: false, inactiveLabel: false) {
                        state "coolLevelDown", label:'  ', action:"coolLevelDown", icon:"st.thermostat.thermostat-down"
        }
        
        valueTile("relativeHumidity", "device.relativeHumidity",width: 2, height: 2, inactiveLabel: false)
        {
        	state "default", label:'Humidity\n${currentValue}%',
             icon: "http://cdn.device-icons.smartthings.com/Weather/weather12-icn@2x.png",
             unit:"%", backgroundColors : [
                    [value: 01, color: "#724529"],
                    [value: 11, color: "#724529"],
                    [value: 21, color: "#724529"],
                    [value: 35, color: "#44b621"],
                    [value: 49, color: "#44b621"],
                    [value: 50, color: "#1e9cbb"]
                ]
        }

        
        /* lgk new tiles for outside temp and hummidity */
          valueTile("outdoorTemperature", "device.outdoorTemperature", width: 1, height: 1, canChangeIcon: true) {
            state("temperature", label: 'Outdoor\n ${currentValue}°',
            icon: "http://cdn.device-icons.smartthings.com/Weather/weather2-icn@2x.png",
            unit:"F", backgroundColors: [
                    [value: -31, color: "#003591"],
                    [value: -10, color: "#90d2a7"],
               		[value: -5, color: "#44b621"],
             	    [value: -2, color: "#f1d801"],
             	    [value: 0, color: "#153591"],
               	    [value: 7, color: "#1e9cbb"],
                    [value: 00, color: "#cccccc"],
                    [value: 31, color: "#153500"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        
         valueTile("outdoorHumidity", "device.outdoorHumidity", inactiveLabel: false){
        	state "default", label:'Outdoor\n ${currentValue}%', 
            icon: "http://cdn.device-icons.smartthings.com/Weather/weather12-icn@2x.png",
            unit:"%", backgroundColors : [
                    [value: 01, color: "#724529"],
                    [value: 11, color: "#724529"],
                    [value: 21, color: "#724529"],
                    [value: 35, color: "#44b621"],
                    [value: 49, color: "#44b621"],
                    [value: 70, color: "#449c00"],
                    [value: 90, color: "#009cbb"]
                  
                ]
        }
        
                
        valueTile("status", "device.lastUpdate",width: 2, height: 1,  inactiveLabel: false, decoration: "flat") {
            state "default", label: '${currentValue}\n', action:"polling.poll"
        }

        valueTile("scLow", "device.scLow",width: 2, height: 1,  inactiveLabel: false, decoration: "flat") {
            state "default", label: '${currentValue}', action:"goLow"
        }
        
        valueTile("scMid", "device.scMid",width: 2, height: 1,  inactiveLabel: false, decoration: "flat") {
            state "default", label: '${currentValue}\n\n', action:"goMid"
        }       
        valueTile("scHigh", "device.scHigh",width: 2, height: 1,  inactiveLabel: false, decoration: "flat") {
            state "default", label: '${currentValue}', action:"goHigh"
        }         

        main "thermostatMulti"
        details([ "thermostatMulti", 
                  "scLow","scMid","scHigh",
                 "coolMode", "thermostatFanMode", "heatMode", 
                "thermostatOperatingState","fanOperatingState","status",     

        "coolLevelUp","coolingSetpoint", "coolLevelDown",       

        "heatLevelUp", "heatingSetpoint" , "heatLevelDown" 
  
       
       ])
       
    }
}

def toggle() {
	def disp = device.currentValue("disp")
    log.debug "!!! !!! $thermostatMode"
    def operMode = device.currentValue("thermostatMode")
    def newDisp
    log.debug "DISP IS $disp"
    unschedule("toggleHome")

	switch (disp) {
		case "0":	if (operMode == 'cool') disp = "c1"
        			else 
                    if (operMode == 'heat') disp = "h1"
                    else disp = "c0"
                    break
    	case "h1":	disp = "h2"; break
        case "c1":	disp = "c2"; break
    	case ["c2","h2"]:	disp = "3"; break
        default: disp="0"; break
   }
   
  
  sendEvent(name:"disp", value:disp)
   
   runIn(5, "toggleHome")
}

def toggleHome() {
	def disp = device.currentValue("disp")
    switch(disp) {
    	case "h1": goHigh(); break
        case "c1": goLow(); break
        case ["c2","h2"]: goMid(); break
        case "c0": cool(); break
        case "h0": heat(); break
        case "3": off(); break      
    }
	sendEvent(name:"disp", value:"Executing...")
    delay(1500)
    sendEvent(name:"disp", value:"0")
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


def temperatureUp()
{
	def operMode = device.currentValue("thermostatMode")
//    if (operMode == "cool") coolLevelUp()   
//    if (operMode == "heat") heatLevelUp()
	level(1, operMode)

}

def temperatureDown()
{	
	def operMode = device.currentValue("thermostatMode")
    //if (operMode == "cool") coolLevelDown()
    //if (operMode == "heat") heatLevelDown()
    level(-1, operMode)
}

def level(Integer inc, String mode) {
	log.trace "$data.setStatus"
	if (data.setStatus != 0) {
    	data.setStatus = -1
    	runIn(5, "applySetpoint")
        //runIn(5, "set${mode}ingSetpoint")
		data["${mode}Setpoint"]=data["${mode}Setpoint"]+inc
        def newval = data["${mode}Setpoint"] as Double
        log.debug "why isn't ${mode}ingSetpoint actually giving me $newval?"
    	sendEvent(name: "${mode}ingSetpoint", value: newval)
    }    
}

def setCoolingSetpoint(temp) {
	data.coolSetpoint = temp as Double
    log.debug "setting temp to $temp"
    applySetpoint()
}

def setHeatingSetpoint(temp) {
	data.heatSetpoint = temp as Double
    log.debug "setting temp to $temp"
	applySetpoint()
}

def coolLevelUp() { 	level(1, "cool")	}
def coolLevelDown() {	level(-1, "cool")	}
def heatLevelUp() {		level(1, "heat")	}
def heatLevelDown() {	level(-1, "heat")	}

def applySetpoint() {
	log.debug "applying setpoint routine"
	setStatus()   
}

def goLow() { setTargetTemp(settings.scLow) }
def goMid() { setTargetTemp(settings.scMid) }
def goHigh() { setTargetTemp(settings.scHigh) }

// parse events into attributes
def parse(String description) {

}

// handle commands


def setFollowSchedule() {
    log.debug "in set follow schedule"
    data.SystemSwitch = 'null' 
    data.heatSetpoint = 'null'
    data.coolSetpoint = 'null'
    data.HeatNextPeriod = 'null'
    data.CoolNextPeriod = 'null'
    data.StatusHeat='0'
    data.StatusCool='0'
    data.FanMode = 'null'
    setStatus()

    if(data.SetStatus==1)
    {
        log.debug "Successfully sent follow schedule.!"
        runIn(60,"getStatus")
    }

}

def setTargetTemp(temp) {
	setTargetTemp(temp as Double)
}


def setTargetTemp(double temp) {
    data.SystemSwitch = 'null' 
    data.heatSetpoint = temp
    data.coolSetpoint = temp
    data.HeatNextPeriod = 'null'
    data.CoolNextPeriod = 'null'
    data.StatusHeat='1'
    data.StatusCool='1'
    data.FanMode = 'null'
    setStatus()
}


def off() {
    setThermostatMode(2)
}

def auto() {
    setThermostatMode(4)
}

def heat() {
    setThermostatMode(1)
}

def emergencyHeat() {

}

def cool() {
    setThermostatMode(3)
}

def setThermostatMode(mode) {
    data.SystemSwitch = mode 
    data.heatSetpoint = 'null'
    data.coolSetpoint = 'null'
    data.HeatNextPeriod = 'null'
    data.CoolNextPeriod = 'null'
    data.StatusHeat=1
    data.StatusCool=1
    data.FanMode = 'null'

    setStatus()

    def switchPos

    if(mode==1)
    switchPos = 'heat'
    if(mode==2)
    switchPos = 'off'
    if(mode==3)
    switchPos = 'cool'
    if(mode==4 || swithPos == 5) switchPos = 'auto'

    if(data.setStatus==1) {
        sendEvent(name: 'thermostatMode', value: switchPos)
    }

}

def fanOn() {
    setThermostatFanMode(1)
}

def fanAuto() {
    setThermostatFanMode(0)
}

def fanCirculate() {
    setThermostatFanMode(2)
}


def setThermostatFanMode(mode) {    

    data.SystemSwitch = 'null' 
    data.heatSetpoint = 'null'
    data.coolSetpoint = 'null'
    data.HeatNextPeriod = 'null'
    data.CoolNextPeriod = 'null'
    data.StatusHeat='null'
    data.StatusCool='null'
    data.FanMode = mode

    setStatus()

    def fanMode

    if(mode==0)
    fanMode = 'auto'
    if(mode==1)
    fanMode = 'on'
    if(mode==2)
    fanMode = 'circulate'

    if(data.setStatus==1)
    {
        sendEvent(name: 'thermostatFanMode', value: fanMode)    
    }

}


def poll() {
	log.debug "poll! $data.setStatus"
    refresh()
    runIn(60, poll) // refresh status every 60 seconds
}


def refresh() {
    log.debug "Executing 'refresh' "//& logged in: ${isLoggedIn()}"
    def unit = getTemperatureScale()
    log.debug "units = $unit"

    //getHumidifierStatus()
	if (data.setStatus == 1) {
       // if (!isLoggedIn()) 
        login()
 		getStatus()
    }    
}


def setStatus() {

    data.setStatus = 0
    log.debug "Executing 'setStatus'"
	//if (!isLoggedIn()) 
    login()
    log.debug "Processing 'setStatus'"
    def today= new Date()
   // log.debug "https://www.mytotalconnectcomfort.com/portal/Device/SubmitControlScreenChanges"
    log.debug "setting heat setpoint to $data.heatSetpoint"
    log.debug "setting cool setpoint to $data.coolSetpoint"
    
    data.StatusHeat='2'
    data.StatusCool='2'

    def params = [
        uri: "https://www.mytotalconnectcomfort.com/portal/Device/SubmitControlScreenChanges",
        headers: [
            'Accept': 'application/json, text/javascript, */*; q=0.01',
            'DNT': '1',
            'Accept-Encoding': 'gzip,deflate,sdch',
            'Cache-Control': 'max-age=0',
            'Accept-Language': 'en-US,en,q=0.8',
            'Connection': 'keep-alive',
            'Host': 'mytotalconnectcomfort.com',
            'Referer': "https://www.mytotalconnectcomfort.com/portal/Device/Control/${settings.honeywelldevice}",
            'X-Requested-With': 'XMLHttpRequest',
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36',
            'Cookie': data.cookiess        ],
        body: [ DeviceID: "${settings.honeywelldevice}", SystemSwitch : data.SystemSwitch ,HeatSetpoint : 
               data.heatSetpoint, CoolSetpoint: data.coolSetpoint, HeatNextPeriod: 
               data.HeatNextPeriod,CoolNextPeriod:data.CoolNextPeriod,StatusHeat:data.StatusHeat,
               StatusCool:data.StatusCool,FanMode:data.FanMode,ThermostatUnits: settings.tempScale,
               VacationHold:'1', IsInVacationHoldMode:'true', CurrentSetpointStatus:'2']

    ]

    log.debug "params = $params"
    httpPost(params) { response ->
        log.debug "Request was successful, $response.status"

    }

    log.debug "SetStatus is 1 now"
    //data.setStatus = 1
    
    delay(1000)
    getStatus()

}

def getStatus() {
	data.setStatus = 0
    log.debug "Executing getStatus"
   // log.debug "enable outside temps = $enableOutdoorTemps"
    def today= new Date()
    //log.debug "https://www.mytotalconnectcomfort.com/portal/Device/CheckDataSession/${settings.honeywelldevice}?_=$today.time"

    def params = [
        uri: "https://www.mytotalconnectcomfort.com/portal/Device/CheckDataSession/${settings.honeywelldevice}",
        headers: [
            'Accept': '*/*',
            'DNT': '1',
            'Cache' : 'false',
            'dataType': 'json',
            'Accept-Encoding': 'plain',
            'Cache-Control': 'max-age=0',
            'Accept-Language': 'en-US,en,q=0.8',
            'Connection': 'keep-alive',
            'Referer': 'https://www.mytotalconnectcomfort.com/portal',
            'X-Requested-With': 'XMLHttpRequest',
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36',
            'Cookie': data.cookiess        ],
    ]

    //log.debug "doing request"

    httpGet(params) { response ->
        log.debug "Request was successful, $response.status"
        //log.debug "data = $response.data"
        log.debug "id = $response.data.latestData"

        def curTemp = response.data.latestData.uiData.DispTemperature
        def fanMode = response.data.latestData.fanData.fanMode
        def switchPos = response.data.latestData.uiData.SystemSwitchPosition
        def coolSetPoint = response.data.latestData.uiData.CoolSetpoint
        def heatSetPoint = response.data.latestData.uiData.HeatSetpoint
        def statusCool = response.data.latestData.uiData.StatusCool
        def statusHeat = response.data.latestData.uiData.StatusHeat
        def curHumidity = response.data.latestData.uiData.IndoorHumidity
        def Boolean hasOutdoorHumid = response.data.latestData.uiData.OutdoorHumidityAvailable
        def Boolean hasOutdoorTemp = response.data.latestData.uiData.OutdoorTemperatureAvailable
        def curOutdoorHumidity = response.data.latestData.uiData.OutdoorHumidity
        def curOutdoorTemp = response.data.latestData.uiData.OutdoorTemperature
        def displayUnits = response.data.latestData.uiData.DisplayUnits
        def fanIsRunning = response.data.latestData.fanData.fanIsRunning
        def equipmentStatus = response.data.latestData.uiData.EquipmentOutputStatus
        
       // log.debug "coolSetPoint is $coolSetPoint | heatSetPoint is $heatSetPoint | curTemp is $curTemp"
        def formattedCoolSetPoint = coolSetPoint//String.format("%5.1f", coolSetPoint)
        def formattedHeatSetPoint = heatSetPoint//String.format("%5.1f", heatSetPoint)
        def formattedTemp = curTemp//String.format("%5.1f", curTemp)

        def hold = response.data.latestData.uiData.CurrentSetpointStatus 
 
	switch(hold) {
    	case 0:		sendEvent(name: 'followSchedule', value: "FollowingSchedule")
        			break
    	case 1:		sendEvent(name: 'followSchedule', value: "TemporaryHold")
        			break
    	case 2:		sendEvent(name: 'followSchedule', value: "VacationHold")
        			break         
     }         
    // sendEvent(name: 'followSchedule', value: "no")

        //  log.debug "displayUnits = $displayUnits"
        state.DisplayUnits = $displayUnits      

        // set fan state
        def fanState = "Unknown"
        if (fanIsRunning == true)
        fanState = "On"
        else fanState = "Idle" 
        //log.trace("Set Fan operating State to: ${fanState}")        

        //fanmode 0=auto, 2=circ, 1=on
        if(fanMode==0)
        fanMode = 'auto'
        if(fanMode==1)
        fanMode = 'on'
        if(fanMode==2)
        fanMode = 'circulate'

        if(switchPos==1)
        switchPos = 'heat'
        if(switchPos==2)
        switchPos = 'off'
        if(switchPos==3)
        switchPos = 'cool'
        if(switchPos==4 || switchPos==5)
        switchPos = 'auto'
               
        def operatingState = "Unknown"
        if (equipmentStatus == 1) {
            operatingState = "Heating"
        } else if (equipmentStatus == 2) {
            operatingState = "Cooling"
        } else if (equipmentStatus == 0) {
            operatingState = "Idle"
        } 
        //log.trace("Set operating State to: ${operatingState}")  
        
        def finalCoolSetPoint = formattedCoolSetPoint as BigDecimal
        def finalHeatSetPoint = formattedHeatSetPoint as BigDecimal
        def finalTemp = formattedTemp as BigDecimal
        
        
		//log.trace "(switchPos == cool : ${(switchPos == 'cool')} && operatingState == Idle : ${(operatingState == "Idle")} && finalCoolSetPoint < finalTemp : ${(finalCoolSetPoint < finalTemp)})" 
		if ((switchPos == 'cool') && (operatingState == "Idle")) {
        	if (finalCoolSetPoint < finalTemp) operatingState = "Waiting for A/C" 
    		else operatingState = "cooling" 
		}
		if ((switchPos == 'heat') && (operatingState == "Idle")) {
        	if (finalHeatSetPoint > finalTemp) operatingState = "Waiting for Heater" 
    		else operatingState = "heating" 
		}
        
        //Send events 
        sendEvent(name: 'thermostatOperatingState', value: operatingState)
        sendEvent(name: 'fanOperatingState', value: fanState)
        sendEvent(name: 'thermostatFanMode', value: fanMode)
        sendEvent(name: 'thermostatMode', value: switchPos)
        sendEvent(name: 'coolingSetpoint', value: finalCoolSetPoint )
        data.coolSetpoint = finalCoolSetPoint
        sendEvent(name: 'heatingSetpoint', value: finalHeatSetPoint )
        data.heatSetpoint = finalHeatSetPoint
        sendEvent(name: 'temperature', value: finalTemp, isStateChange: true)
        //sendEvent(name: 'relativeHumidity', value: curHumidity as Integer)
        sendEvent(name: 'humidity', value: curHumidity as Integer)


        //log.debug "location = $location.name tz = $location.timeZone"
        def now = new Date().format('MM/dd/yyyy h:mm a',location.timeZone)

        //def now = new Date()
        //def tf = new java.text.SimpleDateFormat("MM/dd/yyyy h:mm a")
        //tf.setTimeZone(TimeZone.getTimeZone("GMT${settings.tzOffset}"))
        //def newtime = "${tf.format(now)}" as String   
        // sendEvent(name: "lastUpdate", value: newtime, descriptionText: "Last Update: $newtime")
        sendEvent(name: "lastUpdate", value: now, descriptionText: "Last Update: $now")


        if (enableOutdoorTemps == "Yes")
        {

            if (hasOutdoorHumid)
            {
                sendEvent(name: 'outdoorHumidity', value: curOutdoorHumidity as Integer)
            }

            if (hasOutdoorTemp)
            {
                sendEvent(name: 'outdoorTemperature', value: curOutdoorTemp as Integer)
            }
        }




    }
    data.setStatus = 1
}


def getHumidifierStatus()
{
    /*
$.ajax({
url: humUrl,
type: 'POST',
cache: false,
dataType: "json",
success: function(data) {
/portal/Device/Menu/GetHumData/454832';
*/
    def params = [
        uri: "https://www.mytotalconnectcomfort.com/portal/Device/Menu/GetHumData/${settings.honeywelldevice}",
        headers: [
            'Accept': '*/*',
            'DNT': '1',
            'dataType': 'json',
            'cache': 'false',
            'Accept-Encoding': 'plain',
            'Cache-Control': 'max-age=0',
            'Accept-Language': 'en-US,en,q=0.8',
            'Connection': 'keep-alive',
            'Host': 'rs.alarmnet.com',
            'Referer': 'https://www.mytotalconnectcomfort.com/portal/Menu/${settings.honeywelldevice}',
            'X-Requested-With': 'XMLHttpRequest',
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36',
            'Cookie': data.cookiess        ],
    ]
    httpGet(params) { response ->
        log.debug "GetHumidity Request was successful, $response.status"
        log.debug "response = $response.data"

        //  log.debug "ld = $response.data.latestData"
        //  log.debug "humdata = $response.data.latestData.humData"

        log.trace("lowerLimit: ${response.data.latestData.humData.lowerLimit}")        
        log.trace("upperLimit: ${response.data.humData.upperLimit}")        
        log.trace("SetPoint: ${response.data.humData.Setpoint}")        
        log.trace("DeviceId: ${response.data.humData.DeviceId}")        
        log.trace("IndoorHumidity: ${response.data.humData.IndoorHumidity}")        

    }
}

def api(method, args = [], success = {}) {

}

// Need to be logged in before this is called. So don't call this. Call api.
def doRequest(uri, args, type, success) {

}


def login() {  
    log.debug "Executing 'login'"

    def params = [
        uri: 'https://www.mytotalconnectcomfort.com/portal',
        headers: [
            'Content-Type': 'application/x-www-form-urlencoded',
            'Accept': 'application/json, text/javascript, */*; q=0.01',
            'Accept-Encoding': 'sdch',
            'Host': 'www.mytotalconnectcomfort.com',
            'DNT': '1',
            'Origin': 'www.mytotalconnectcomfort.com/portal/',
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36'
        ],
        body: [timeOffset: '240', UserName: "${settings.username}", Password: "${settings.password}", RememberMe: 'false']
    ]
    
    //log.debug "data.cookiess $data.cookiess"
    data.cookiess = ''


    httpPost(params) { response ->
        log.debug "Request was successful, $response.status"
//        log.debug response.getHeaders()
        String allCookies = ""

   //     response.getHeaders('Set-Cookie').each {
   //                   log.debug "@ !!! Set-Cookie: ${it.value}"
   //           }

        response.getHeaders('Set-Cookie').each {
//log.trace "IT: $it"
            String cookie = it.value.split(';|,')[0]
            Boolean skipCookie = false
            def expireParts = it.value.split('expires=')
//log.debug "cookie: $cookie"
            try {
                def cookieSegments = it.value.split(';')
                for (int i = 0; i < cookieSegments.length; i++) {
                    def cookieSegment = cookieSegments[i]
//log.trace "segment = ${cookieSegments[i]}"
                    String cookieSegmentName = cookieSegment.split('=')[0]
//log.trace "cookieSegmentName ${cookieSegmentName}"                    
//log.trace "cookieSegmentName.trim() ${cookieSegmentName.trim()}"
                    if (cookieSegmentName.trim() == "expires") {
                        String expiration = cookieSegment.split('=')[1] 
//log.trace "expiration $expiration"
                        Date expires = new Date(expiration)
                        Date newDate = new Date() // right now
//log.trace "expires: $expires  newDate: $newDate"
                        if (expires < newDate ) {
                            skipCookie=true
                           //log.debug "-skip cookie: $it.value"
                           if (it.value.contains(".ASPXAUTH_TRUEHOME")) data.auth = expiration as String
                        } else {
                            //log.debug "+not skipping cookie: expires=$expires. now=$newDate. cookie: $it.value"
                        }

                    } 
                }
            }
            catch (e) {
                log.debug "!error when checking expiration date: $e ($expiration) [$expireParts.length] {$it.value}"
            }

            allCookies = allCookies + it.value + ';'
            log.debug "skipCookie = $skipCookie"
//log.debug "allcookie: $allCookies" 
//log.debug "cookie: $cookie"
//log.debug "data.cookiess $data.cookiess"
//log.trace "compare = ${(cookie != ".ASPXAUTH_TRUEHOME=")} and ${it.value.split('=')[1].trim()}"
            //if(cookie != ".ASPXAUTH_TH_A=") {
            if(cookie != ".ASPXAUTH_TRUEHOME") {
                if (it.value.split('=')[1].trim() != "") {
                    if (!skipCookie) {
//                        log.debug "Adding cookie to collection: $cookie"
                        data.cookiess = data.cookiess+cookie+';'
//                        log.debug "added cookie to $data.cookiess"
                    }
                }
            }
        log.debug "getting awesome?"    
        //log.debug "data: ${data.cookiess}"
        }

        log.debug "AWESOME cookies" 
        //log.debug "data: ${data.cookiess}"
    }
}

def isLoggedIn() {
	log.debug "auth $data.auth" 
    if(!data.auth) {
        log.debug "No data.auth"
        return false
    }

    def now = new Date()
    def expires = new Date(data.auth)
    return expires < now
}


def updated()
{
    log.debug "in updated"
    state.DisplayUnits = settings.tempScale
	sendEvent(name: 'scLow', value: settings.scLow) 
    sendEvent(name: 'scMid', value: settings.scMid) 
    sendEvent(name: 'scHigh', value: settings.scHigh) 
    log.debug "display units now = $state.DisplayUnits"
    data.setStatus = 1
    data.coolSetpoint = data.CoolSetpoint
    data.heatSetpoint = data.heatSetpoint

}

def installed() {
   // state.DisplayUnits = settings.tempScale
updated()
   // log.debug "display units now = $state.DisplayUnits"
}