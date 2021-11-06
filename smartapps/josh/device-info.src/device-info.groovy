

definition(
    name: "Device Info",
    namespace: "Josh",
    author: "Josh",
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
    def lastStates = []
    if (devices) //def(name:"toNextPage", page:"infoPage", description: "",title: "Info!")
    	section() {	
        	paragraph title:"Commands","${devices.supportedCommands}"
			paragraph title:"Attributes","${devices.getSupportedAttributes()}"
            devices.getSupportedAttributes().each { 
            	def s = "$it = " 
                if (devices.latestState("$it"))
            	 s = s + "${devices.latestState("$it").value}"
                 else s+"null"
                 lastStates << s
            }
            paragraph title:"Values","$lastStates"
            
            
        }    
}	}



def installed()
{

}

def updated()
{

}
