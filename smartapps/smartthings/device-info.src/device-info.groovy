

definition(
    name: "Device Info",
    namespace: "smartthings",
    author: "SmartThings",
    description: "none",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
)

preferences {
	page(name: "deviceSelect")
	page(name: "infoPage")    
}


def deviceSelect() {  dynamicPage(name: "deviceSelect", title: "Device", uninstall: false, install: false) {
	section() {
    	def base = "capability.actuator"
		input(
			name			: "baseDevice"
			,title			: "Base Device Type"
			,multiple		: false
			,required		: false
			,type			: "enum"
            ,options		: [["capability.actuator":"Actuator"],["capability.switch":"Switch"],["capability.sensor":"Sensor"]]
			,submitOnChange	: true
		)
        if (baseDevice) base = baseDevice
		
        input(
			name			: "devices"
			,title			: "Test device"
			,multiple		: false
			,required		: false
			,type			: base
			,submitOnChange	: true
		)   	
	}
    if (devices) //def(name:"toNextPage", page:"infoPage", description: "",title: "Info!")
    	section() {	
        	paragraph title:"Commands","${devices.supportedCommands}"
			paragraph title:"Attributes","${devices.getSupportedAttributes()}"
        }    
}	}



def installed()
{

}

def updated()
{

}
