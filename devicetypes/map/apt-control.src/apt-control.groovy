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
 */
metadata {
	
    definition (name: "Apt Control", namespace: "Map", author: "Josh") {
		capability "Switch"
        capability "Relay Switch"
        
        attribute "status", "STRING"
        attribute "TableMotion", "STRING"
        attribute "FloorMotion", "STRING"        
        attribute "LivingRoomCouchMotion", "STRING"
        attribute "LivingRoomDoorMotion", "STRING"
        attribute "LivingRoomTVMotion", "STRING"
        attribute "HallMotion", "STRING"
        attribute "EntryMotion", "STRING"
        attribute "BedroomHallMotion", "STRING"        
        attribute "KitchenFrontMotion", "STRING"
        attribute "KitchenStoveMotion", "STRING"
        attribute "BedroomMotion", "STRING"       
        attribute "FrontContact", "STRING"
        attribute "BedroomContact", "STRING" 
        attribute "EntryClosetContact", "STRING" 
        attribute "DenContact", "STRING"
        attribute "HallClosetContact", "STRING"
        attribute "BedroomClosetContact", "STRING"
        attribute "LaundryContact", "STRING"
        
        attribute "DenClosetContact", "STRING"
        attribute "DenContact", "STRING"
        attribute "DenDeskMotion", "STRING"
        attribute "DenDiplomaMotion", "STRING"
        attribute "GuestBathContact", "STRING"
        attribute "GuestBathMotion", "STRING"
        attribute "GuestBathShowerMotion", "STRING"
        attribute "BedBathContact", "STRING"
        attribute "HVACContact", "STRING"
        attribute "BedBathMotion", "STRING"        
        attribute "BedBathShowerMotion", "STRING" 


        attribute "BedTemperature", "STRING"
        attribute "BedBathShowerTemperature", "STRING"
        attribute "GuestBathShowerTemperature", "STRING"
        attribute "DenDiplomaTemperature", "STRING"
        attribute "KitchenStoveTemperature", "STRING" 
        attribute "LivingRoomCouchTemperature", "STRING"
        attribute "DryerTemperature", "STRING"
        attribute "emperatureTemperature", "STRING"
        
        attribute "BedMotion", "STRING"  

        command "activity", ["STRING"]
        
	}

	tiles(scale: 2) {
		standardTile("status", "device.status", width: 6, height: 6, canChangeIcon: false) {
			state "status", label: '', backgroundColor: "#1e50bb" 
         
        }
        standardTile("DenMotion", "device.DenDeskMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/FFFCCC.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}
        standardTile("DenMotion2", "device.DenDiplomaMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/FFFCCC.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
        }
        standardTile("GuestBathMotion", "device.GuestBathMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/DCBADB.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}
        standardTile("LivingRoomC", "device.LivingRoomCouchMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/FFFCCC.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}
        standardTile("LivingRoomD", "device.LivingRoomDoorMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/FFFCCC.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}
        standardTile("LivingRoomT", "device.LivingRoomTVMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/FFFCCC.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}        
        
        standardTile("HallwayMotion", "device.HallMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/FFFFFF.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}  
        standardTile("EntryMotion", "device.EntryMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/FFFFFF.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}          
        standardTile("BedHall", "device.BedroomHallMotion", width: 1, height: 1) {        
			state "off", label: '', icon: "http://html-color.org/E2BFAC.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}          
        standardTile("KitchenFront", "device.KitchenFrontMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/B8DEC0.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/green-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/green-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/green-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/green-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
        }
        standardTile("KitchenBack", "device.KitchenStoveMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/B8DEC0.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/green-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/green-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/green-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/green-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}
        standardTile("BedBathMotion", "device.BedBathMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/DCBADB.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}
        standardTile("BedShower", "device.BedBathShowerMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/DCBADB.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}
        standardTile("GuestShower", "device.GuestBathShowerMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/DCBADB.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
		}
        standardTile("BedroomMotion", "device.BedroomMotion", width: 1, height: 1) {        
			state "off", label: '', icon: "http://html-color.org/E2BFAC.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
        }
        
        standardTile("BedroomSpecial", "device.BedMotion", width: 1, height: 1) {
			state "off", label: '', icon: "http://html-color.org/E2BFAC.jpg"
            state "0", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-0.png"
            state "2", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-2.png"
            state "7", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-7.png"
            state "10", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-10.png"
			state "on", label: '', icon: "http://html-color.org/99B8FF.jpg"
        }        
        
        standardTile("FrontDoor", "device.FrontContact", inactiveLabel: true, width: 1, height: 1, decoration: "flat") {
		  state "closed", label:'${currentValue}', action:"${name}", icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-closed-green.png"
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-open-red.png"
		}       
        standardTile("HVACContact", "device.HVACContact", width: 1, height: 1) {
		  state "closed", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-closed-dim.png"
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-open-red.png"
		}
        standardTile("GuestBathContact", "device.GuestBathContact", width: 1, height: 1) {
		  state "closed", label:'${currentValue}', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-closed-red.png"  
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-open.png"     
		}
        standardTile("BedContact", "device.BedroomContact", inactiveLabel: false, width: 1, height: 1) {
		  state "closed", label:'${currentValue}', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-closed-red.png"
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-open.png"
		}
        standardTile("BedBathContact", "device.BedBathContact", inactiveLabel: false, width: 1, height: 1) {
		  state "closed", label:'${currentValue}', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-closed-red.png"
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-open.png"
		}
        standardTile("DenCloset", "device.DenClosetContact", inactiveLabel: false, width: 1, height: 1) {
		  state "closed", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-closed.png"
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-open-red.png"
		}
        standardTile("DenContact", "device.DenContact", inactiveLabel: false, width: 1, height: 1) {
		  state "closed", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-closed-red.png"
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-open.png"
		}    
                
        standardTile("BedCloset", "device.BedroomClosetContact", inactiveLabel: false, width: 1, height: 1) {
		  state "closed", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-closed.png"
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-open-red.png"
		}        
        standardTile("Laundry", "device.LaundryContact", inactiveLabel: false, width: 1, height: 1) {
		  state "closed", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-closed-dim.png"
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-open-red.png"
		}           
        standardTile("EntryCloset", "device.HallClosetContact", inactiveLabel: false, width: 1, height: 1) {
		  state "closed", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-closed-dim.png"
          state "open", label:'', icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/white-open-red.png"
        }       
        
        standardTile("dummy", "dummy", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "default", label:"",icon:"st.secondary.secondary"//, backgroundColor:"#000000"
        }       
        standardTile("filler", "filler", height: 1, width: 1, inactiveLabel:false, decoration:"flat") {
            state "default", label:"", icon:" "//"st.secondary.secondary"//, backgroundColor:"#000000"
        }       
          
        valueTile("LivingTemp", "device.LivingRoomCouchTemperature", inactiveLabel: false, width: 1, height: 1) {
			state "temperature", label:'${currentValue}\n',icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-hole.png",
				backgroundColors:[
					[value: 50, color: "#153591"],
					[value: 60, color: "#1e9cbb"],
					[value: 64, color: "#47c2b1"],
					[value: 65, color: "#41af93"],
					[value: 66, color: "#44b67b"],
					[value: 67, color: "#44b65a"],
					[value: 68, color: "#44b621"],
					[value: 69, color: "#1ec81e"],
					[value: 70, color: "#8cc801"],
					[value: 71, color: "#bfd801"],
					[value: 72, color: "#f1d801"],					
					[value: 73, color: "#d4aa19"],
					[value: 74, color: "#d48519"],                    
					[value: 75, color: "#d04e00"]
				]
		}
        
        valueTile("BedTemp", "device.BedTemperature", inactiveLabel: false, width: 1, height: 1) {
			state "temperature", label:'${currentValue}\n',icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/brown-hole.png",
				backgroundColors:[
					[value: 50, color: "#153591"],
					[value: 60, color: "#1e9cbb"],
					[value: 64, color: "#47c2b1"],
					[value: 65, color: "#41af93"],
					[value: 66, color: "#44b67b"],
					[value: 67, color: "#44b65a"],
					[value: 68, color: "#44b621"],
					[value: 69, color: "#1ec81e"],
					[value: 70, color: "#8cc801"],
					[value: 71, color: "#bfd801"],
					[value: 72, color: "#f1d801"],					
					[value: 73, color: "#d4aa19"],
					[value: 74, color: "#d48519"],                    
					[value: 75, color: "#d04e00"]
				]
		}
        
        valueTile("BedBathTemp", "device.BedBathShowerTemperature", inactiveLabel: false, width: 1, height: 1) {
			state "temperature", label:'${currentValue}\n',icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-hole.png",
				backgroundColors:[
					[value: 50, color: "#153591"],
					[value: 60, color: "#1e9cbb"],
					[value: 64, color: "#47c2b1"],
					[value: 65, color: "#41af93"],
					[value: 66, color: "#44b67b"],
					[value: 67, color: "#44b65a"],
					[value: 68, color: "#44b621"],
					[value: 69, color: "#1ec81e"],
					[value: 70, color: "#8cc801"],
					[value: 71, color: "#bfd801"],
					[value: 72, color: "#f1d801"],					
					[value: 73, color: "#d4aa19"],
					[value: 74, color: "#d48519"],                    
					[value: 75, color: "#d04e00"]
				]
		}
        
        valueTile("GuestBathTemp", "device.GuestBathShowerTemperature", inactiveLabel: false, width: 1, height: 1) {
			state "temperature", label:'${currentValue}\n',icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/pink-hole.png",
				backgroundColors:[
					[value: 50, color: "#153591"],
					[value: 60, color: "#1e9cbb"],
					[value: 64, color: "#47c2b1"],
					[value: 65, color: "#41af93"],
					[value: 66, color: "#44b67b"],
					[value: 67, color: "#44b65a"],
					[value: 68, color: "#44b621"],
					[value: 69, color: "#1ec81e"],
					[value: 70, color: "#8cc801"],
					[value: 71, color: "#bfd801"],
					[value: 72, color: "#f1d801"],					
					[value: 73, color: "#d4aa19"],
					[value: 74, color: "#d48519"],                    
					[value: 75, color: "#d04e00"]
				]
		}
        valueTile("DenTemp", "device.DenDiplomaTemperature", inactiveLabel: false, width: 1, height: 1) {
			state "temperature", label:'${currentValue}\n',icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/yellow-hole.png",
				backgroundColors:[
					[value: 50, color: "#153591"],
					[value: 60, color: "#1e9cbb"],
					[value: 64, color: "#47c2b1"],
					[value: 65, color: "#41af93"],
					[value: 66, color: "#44b67b"],
					[value: 67, color: "#44b65a"],
					[value: 68, color: "#44b621"],
					[value: 69, color: "#1ec81e"],
					[value: 70, color: "#8cc801"],
					[value: 71, color: "#bfd801"],
					[value: 72, color: "#f1d801"],					
					[value: 73, color: "#d4aa19"],
					[value: 74, color: "#d48519"],                    
					[value: 75, color: "#d04e00"]
				]
		}
        
        valueTile("KitchenTemp", "device.KitchenStoveTemperature", inactiveLabel: false, width: 1, height: 1) {
			state "temperature", label:'${currentValue}\n.',icon:"https://raw.githubusercontent.com/jghoffer/STicons/master/green-hole.png",//wordWrap: true,
				backgroundColors:[
					[value: 50, color: "#153591"],
					[value: 60, color: "#1e9cbb"],
					[value: 64, color: "#47c2b1"],
					[value: 65, color: "#41af93"],
					[value: 66, color: "#44b67b"],
					[value: 67, color: "#44b65a"],
					[value: 68, color: "#44b621"],
					[value: 69, color: "#1ec81e"],
					[value: 70, color: "#8cc801"],
					[value: 71, color: "#bfd801"],
					[value: 72, color: "#f1d801"],					
					[value: 73, color: "#d4aa19"],
					[value: 74, color: "#d48519"],                    
					[value: 75, color: "#d04e00"]
				]
		}        
        valueTile("LaundryTemp", "device.DryerTemperature", inactiveLabel: false, width: 1, height: 1) {
			state "temperature", label:'${currentValue}\n.',icon:" ",
				backgroundColors:[
					[value: 50, color: "#153591"],
					[value: 60, color: "#1e9cbb"],
					[value: 64, color: "#47c2b1"],
					[value: 65, color: "#41af93"],
					[value: 66, color: "#44b67b"],
					[value: 67, color: "#44b65a"],
					[value: 68, color: "#44b621"],
					[value: 69, color: "#1ec81e"],
					[value: 70, color: "#8cc801"],
					[value: 71, color: "#bfd801"],
					[value: 72, color: "#f1d801"],					
					[value: 73, color: "#d4aa19"],
					[value: 74, color: "#d48519"],                    
					[value: 75, color: "#d04e00"]
				]
		}                
        valueTile("Temp", "device.emperatureTemperature", inactiveLabel: false, width: 1, height: 1) {
			state "temperature", label:'${currentValue}\n', backgroundColor:"#000000"
		}  
        
        
        main "status"
		details([
  "DenMotion2",		"DenMotion",	 	"Temp",	"FrontDoor",		"dummy",				"dummy",
  "DenMotion2", 	"DenMotion",	 	"DenContact",	"EntryMotion", 		"GuestBathContact",	"GuestBathMotion",
  "DenTemp",		"DenCloset",		"HVACContact",	"HallwayMotion",	"GuestBathTemp",	"GuestShower",
  "BedShower",		"BedBathTemp", 		"Laundry",		"HallwayMotion",	"EntryCloset",		"KitchenTemp",
  "BedBathMotion",	"BedBathContact",	"BedContact",	"HallwayMotion",	"KitchenFront",		"KitchenBack",
  "BedCloset",		"BedroomMotion",	"BedHall",		"LivingRoomT",		"LivingRoomT",		"LivingRoomC",
  "BedroomSpecial",	"BedroomMotion",	"BedHall",		"LivingRoomD",		"LivingRoomT",		"LivingRoomC",
  "BedroomMotion",	"BedroomMotion",	"BedTemp",		"LivingRoomD",		"LivingRoomD",		"LivingTemp"
              
            ])
	}
}

def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

def activity(val) {
	def vals = val.split(":")
	log.debug "${vals[0]} ${vals[1]} ${vals[2]}"    
    def att = vals[0] + vals[1]
    def attVal = vals[2]   
	if ((attVal == "off") || (attVal == "closed")) {
    	//attVal = new Date().format('H:mm',location.timeZone) //= new Date() 
        attVal = 0
    	state["${att}"] = new Date().format("yyy-MM-dd'T'HH:mm:ssZ",location.timeZone) 
    }
    sendEvent(name: "$att", value: "$attVal", canBeCurrentState:true, displayed: true)
    
    
   	state.each{k, last ->  		
        def cur = device.currentValue("${k}")
        if ((cur != "on") && (cur != "open")) 
        if ((k.contains("Motion")) || (k.contains("Contact")))   {
        	def s = since(last, "yyy-MM-dd'T'HH:mm:ssZ") 
            def v
            if (k.contains("Contact")) v = s
        	if (k.contains("Motion")) {      
            	if (s < 2) v = "0" else
            	if (s < 7) v = "2" else
            	if (s < 10) v = "7" else
            	if (s < 15) v = "10" else
         	   	v = "off"
        	}    
            log.trace "$k is $s"          
            sendEvent(name: "$k", value: "$v", canBeCurrentState:false, displayed: false, isStateChange: true)  
        }
    }
}

def since(String s, String form){
	//form = "yyy-MM-dd'T'HH:mm:ssZ"
    long timeDiff
    def now = new Date()
    def end = Date.parse("$form","${s}".replace("+00:00","+0000"))
    long unxNow = now.getTime()
    long unxEnd = end.getTime()
    
    unxNow = unxNow/1000
    unxEnd = unxEnd/1000
    
    timeDiff = Math.abs(unxNow-unxEnd)
    timeDiff = Math.round(timeDiff/60)
    return timeDiff
}


