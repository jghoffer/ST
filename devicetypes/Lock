
/**
 * 	Z-Wave Lock
 *
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 ; you may not use this file except
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
	definition  {
		capability "Actuator"
		capability "Lock"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
		capability "Lock Codes"
		capability "Battery"
		capability "Health Check"
		capability "Configuration"

		// Generic
		fingerprint inClusters: "0x62, 0x63", deviceJoinName: "Door Lock"
		fingerprint deviceId: "0x4003", inClusters: "0x98", deviceJoinName: "Door Lock"
		fingerprint deviceId: "0x4004", inClusters: "0x98", deviceJoinName: "Door Lock"
		// KwikSet
		fingerprint mfr:"0090", prod:"0001", model:"0236", deviceJoinName: "KwikSet Door Lock" //KwikSet SmartCode 910 Deadbolt Door Lock
		fingerprint mfr:"0090", prod:"0003", model:"0238", deviceJoinName: "KwikSet Door Lock" //KwikSet SmartCode 910 Deadbolt Door Lock
		fingerprint mfr:"0090", prod:"0001", model:"0001", deviceJoinName: "KwikSet Door Lock" //KwikSet SmartCode 910 Contemporary Deadbolt Door Lock
		fingerprint mfr:"0090", prod:"0003", model:"0339", deviceJoinName: "KwikSet Door Lock" //KwikSet SmartCode 912 Lever Door Lock
		fingerprint mfr:"0090", prod:"0003", model:"4006", deviceJoinName: "KwikSet Door Lock" //backlit version //KwikSet SmartCode 914 Deadbolt Door Lock
		fingerprint mfr:"0090", prod:"0003", model:"0440", deviceJoinName: "KwikSet Door Lock" //KwikSet SmartCode 914 Deadbolt Door Lock
		fingerprint mfr:"0090", prod:"0001", model:"0642", deviceJoinName: "KwikSet Door Lock" //KwikSet SmartCode 916 Touchscreen Deadbolt Door Lock
		fingerprint mfr:"0090", prod:"0003", model:"0642", deviceJoinName: "KwikSet Door Lock" //KwikSet SmartCode 916 Touchscreen Deadbolt Door Lock
		//zw:Fs type:4003 mfr:0090 prod:0003 model:0541 ver:4.79 zwv:4.34 lib:03 cc:5E,72,5A,98,73,7A sec:86,80,62,63,85,59,71,70,5D role:07 ff:8300 ui:8300
		fingerprint mfr:"0090", prod:"0003", model:"0541", deviceJoinName: "KwikSet Door Lock" //KwikSet SmartCode 888 Touchpad Deadbolt Door Lock
		//zw:Fs type:4003 mfr:0090 prod:0003 model:0742 ver:4.10 zwv:4.34 lib:03 cc:5E,72,5A,98,73,7A sec:86,80,62,63,85,59,71,70,4E,8B,4C,5D role:07 ff:8300 ui:8300
		fingerprint mfr:"0090", prod:"0003", model:"0742", deviceJoinName: "Kwikset Door Lock" //Kwikset Obsidian Lock
		// Schlage
		fingerprint mfr:"003B", prod:"6349", model:"5044", deviceJoinName: "Schlage Door Lock" //Schlage Touchscreen Deadbolt Door Lock
		fingerprint mfr:"003B", prod:"6341", model:"5044", deviceJoinName: "Schlage Door Lock" //Schlage Touchscreen Deadbolt Door Lock
		fingerprint mfr:"003B", prod:"634B", model:"504C", deviceJoinName: "Schlage Door Lock" //Schlage Connected Keypad Lever Door Lock
		fingerprint mfr:"003B", prod:"0001", model:"0468", deviceJoinName: "Schlage Door Lock" //BE468ZP //Schlage Connect Smart Deadbolt Door Lock
		fingerprint mfr:"003B", prod:"0001", model:"0469", deviceJoinName: "Schlage Door Lock" //BE469ZP //Schlage Connect Smart Deadbolt Door Lock
		fingerprint mfr:"003B", prod:"0004", model:"2109", deviceJoinName: "Schlage Door Lock" //Schlage Keypad Deadbolt JBE109
		fingerprint mfr:"003B", prod:"0004", model:"6109", deviceJoinName: "Schlage Door Lock" //Schlage Keypad Lever JFE109
		// Yale
		fingerprint mfr:"0129", prod:"0002", model:"0800", deviceJoinName: "Yale Door Lock" // YRD120 //Yale Touchscreen Deadbolt Door Lock
		fingerprint mfr:"0129", prod:"0002", model:"0000", deviceJoinName: "Yale Door Lock" // YRD220, YRD240 //Yale Touchscreen Deadbolt Door Lock
		fingerprint mfr:"0129", prod:"0002", model:"FFFF", deviceJoinName: "Yale Door Lock" // YRD220 //Yale Touchscreen Lever Door Lock
		fingerprint mfr:"0129", prod:"0004", model:"0800", deviceJoinName: "Yale Door Lock" // YRD110 //Yale Push Button Deadbolt Door Lock
		fingerprint mfr:"0129", prod:"0004", model:"0000", deviceJoinName: "Yale Door Lock" // YRD210 //Yale Push Button Deadbolt Door Lock
		fingerprint mfr:"0129", prod:"0001", model:"0000", deviceJoinName: "Yale Door Lock" // YRD210 //Yale Push Button Lever Door Lock
		fingerprint mfr:"0129", prod:"8002", model:"0600", deviceJoinName: "Yale Door Lock" //YRD416, YRD426, YRD446 //Yale Assure Lock
		fingerprint mfr:"0129", prod:"0007", model:"0001", deviceJoinName: "Yale Door Lock" //Yale Keyless Connected Smart Door Lock
		fingerprint mfr:"0129", prod:"8004", model:"0600", deviceJoinName: "Yale Door Lock" //YRD216 //Yale Assure Lock Push Button Deadbolt
		fingerprint mfr:"0129", prod:"6600", model:"0002", deviceJoinName: "Yale Door Lock" //Yale Conexis Lock
		fingerprint mfr:"0129", prod:"0001", model:"0409", deviceJoinName: "Yale Door Lock" // YRL-220-ZW-605 //Yale Touchscreen Lever Door Lock
		fingerprint mfr:"0129", prod:"800B", model:"0F00", deviceJoinName: "Yale Door Lock" // YRL216-ZW2. YRL236 //Yale Assure Keypad Lever Door Lock
		fingerprint mfr:"0129", prod:"800C", model:"0F00", deviceJoinName: "Yale Door Lock" // YRL226-ZW2 //Yale Assure Touchscreen Lever Door Lock
		fingerprint mfr:"0129", prod:"8002", model:"1000", deviceJoinName: "Yale Door Lock" //YRD-ZWM-1 //Yale Assure Lock
		fingerprint mfr:"0129", prod:"803A", model:"0508", deviceJoinName: "Yale Door Lock" //YRD156 //Yale Touchscreen Deadbolt with Integrated ZWave Plus
		// Samsung
		fingerprint mfr:"022E", prod:"0001", model:"0001", deviceJoinName: "Samsung Door Lock", mnmn: "SmartThings", vid: "SmartThings-smartthings-Samsung_Smart_Doorlock" // SHP-DS705, SHP-DHP728, SHP-DHP525 //Samsung Digital Lock
		// KeyWe
		fingerprint mfr:"037B", prod:"0002", model:"0001", deviceJoinName: "KeyWe Door Lock" // GKW-2000D //KeyWe Lock
		fingerprint mfr:"037B", prod:"0003", model:"0001", deviceJoinName: "KeyWe Door Lock" // GKW-1000Z //KeyWe Smart Rim Lock
		// Philia
		fingerprint mfr:"0366", prod:"0001", model:"0001", deviceJoinName: "Philia Door Lock" // PDS-100 //Philia Smart Door Lock
	}

	simulator {
		status "locked": "command: 9881, payload: 00 62 03 FF 00 00 FE FE"
		status "unlocked": "command: 9881, payload: 00 62 03 00 00 00 FE FE"

		reply "9881006201FF,delay 4200,9881006202": "command: 9881, payload: 00 62 03 FF 00 00 FE FE"
		reply "988100620100,delay 4200,9881006202": "command: 9881, payload: 00 62 03 00 00 00 FE FE"
	}

	tiles {
		multiAttributeTile{
			tileAttribute  {
				attributeState "locked", label:'locked', action:"lock.unlock", icon:"st.locks.lock.locked", backgroundColor:"#00A0DC", nextState:"unlocking"
				attributeState "unlocked", label:'unlocked', action:"lock.lock", icon:"st.locks.lock.unlocked", backgroundColor:"#ffffff", nextState:"locking"
				attributeState "unlocked with timeout", label:'unlocked', action:"lock.lock", icon:"st.locks.lock.unlocked", backgroundColor:"#ffffff", nextState:"locking"
				attributeState "unknown", label:"unknown", action:"lock.lock", icon:"st.locks.lock.unknown", backgroundColor:"#ffffff", nextState:"locking"
				attributeState "locking", label:'locking', icon:"st.locks.lock.locked", backgroundColor:"#00A0DC"
				attributeState "unlocking", label:'unlocking', icon:"st.locks.lock.unlocked", backgroundColor:"#ffffff"
			}
		}
		standardTile {
			state "default", label:'lock', action:"lock.lock", icon:"st.locks.lock.locked", nextState:"locking"
		}
		standardTile {
			state "default", label:'unlock', action:"lock.unlock", icon:"st.locks.lock.unlocked", nextState:"unlocking"
		}
		valueTile {
			state "battery", label:'${currentValue}% battery', unit:""
		}
		standardTile {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}

		main "toggle"
		details
	}
}

import physicalgraph.zwave.commands.doorlockv1.*
import physicalgraph.zwave.commands.usercodev1.*

/**
 * Called on app installed
 */
def installed {
	// Device-Watch pings if no device events received for 1 hour 
	sendEvent

	if ) { // Samsung locks won't allow you to enter the pairing menu when locked, so it must be unlocked
		sendEvent
	}

	scheduleInstalledCheck
}

/**
 * Verify that we have actually received the lock's initial states.
 * If not, verify that we have at least requested them or request them,
 * and check again.
 */
def scheduleInstalledCheck {
	runIn
}

def installedCheck {
	if  && device.currentState) {
		unschedule
	} else {
		// We might have called updated or configure at some point but not have received a reply, so don't flood the network
		if ) {
			def actions = updated

			if  {
				sendHubCommand)
			}
		}

		scheduleInstalledCheck
	}
}

/**
 * Called on app uninstalled
 */
def uninstalled {
	def deviceName = device.displayName
	log.trace "[DTH] Executing 'uninstalled' for device $deviceName"
	sendEvent
}

/**
 * Executed when the user taps on the 'Done' button on the device settings screen. Sends the values to lock.
 *
 * @return hubAction: The commands to be executed
 */
def updated {
	// Device-Watch pings if no device events received for 1 hour 
	sendEvent

	def hubAction = null
	try {
		def cmds = []
		if  || !device.currentState || !state.configured) {
			log.debug "Returning commands for lock operation get and battery get"
			if  {
				cmds << doConfigure
			}
			cmds << refresh
			cmds << reloadAllCodes
			if  {
				cmds << zwave.manufacturerSpecificV1.manufacturerSpecificGet.format
			}
			if  {
				cmds << zwave.versionV1.versionGet.format
			}
			hubAction = response)
		}
	} catch  {
		log.warn "updated threw $e"
	}
	hubAction
}

/**
 * Configures the device to settings needed by SmarthThings at device discovery time
 *
 */
def configure {
	log.trace "[DTH] Executing 'configure' for device ${device.displayName}"
	def cmds = doConfigure
	log.debug "Configure returning with commands := $cmds"
	cmds
}

/**
 * Returns the list of commands to be executed when the device is being configured/paired
 *
 */
def doConfigure {
	log.trace "[DTH] Executing 'doConfigure' for device ${device.displayName}"
	state.configured = true
	def cmds = []
	cmds << secure)
	cmds << secure)
	if ) {
		cmds << secure.codeLength.number))
	}
	cmds = delayBetween

	state.lastLockDetailsQuery = now

	log.debug "Do configure returning with commands := $cmds"
	cmds
}

/**
 * Responsible for parsing incoming device messages to generate events
 *
 * @param description: The incoming description from the device
 *
 * @return result: The list of events to be sent out
 *
 */
def parse {
	log.trace "[DTH] Executing 'parse' for device ${device.displayName} with description = $description"

	def result = null
	if ) {
		if  {
			result = createEvent
		} else {
			result = createEvent(
					descriptionText: "This lock failed to complete the network security key exchange. If you are unable to control it via SmartThings, you must remove it from your network and add it again.",
					eventType: "ALERT",
					name: "secureInclusion",
					value: "failed",
					displayed: true,
			)
		}
	} else {
		def cmd = zwave.parse
		if  {
			result = zwaveEvent
		}
	}
	log.info "[DTH] parse - returning result=$result"
	result
}

/**
 * Responsible for parsing ConfigurationReport command
 *
 * @param cmd: The ConfigurationReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	if  && cmd.parameterNumber == getSchlageLockParam.codeLength.number) {
		def result = []
		def length = cmd.scaledConfigurationValue
		def deviceName = device.displayName
		log.trace "[DTH] Executing 'ConfigurationReport' for device $deviceName with code length := $length"
		def codeLength = device.currentValue
		if  {
			log.trace "[DTH] Executing 'ConfigurationReport' for device $deviceName - all codes deleted"
			result = allCodesDeletedEvent
			result << createEvent(name: "codeChanged", value: "all deleted", descriptionText: "Deleted all user codes",
					isStateChange: true, data: [notify: true, notificationText: "Deleted all user codes in $deviceName at ${location.name}"])
			result << createEvent, displayed: false, descriptionText: "'lockCodes' attribute updated")
		}
		result << createEvent
		return result
	}
	return null
}

/**
 * Responsible for parsing SecurityMessageEncapsulation command
 *
 * @param cmd: The SecurityMessageEncapsulation command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def encapsulatedCommand = cmd.encapsulatedCommand
	if  {
		zwaveEvent
	}
}

/**
 * Responsible for parsing NetworkKeyVerify command
 *
 * @param cmd: The NetworkKeyVerify command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	createEvent
}

/**
 * Responsible for parsing SecurityCommandsSupportedReport command
 *
 * @param cmd: The SecurityCommandsSupportedReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	state.sec = cmd.commandClassSupport.collect { String.format }.join
	if  {
		state.secCon = cmd.commandClassControl.collect { String.format }.join
	}
	createEvent
}

/**
 * Responsible for parsing DoorLockOperationReport command
 *
 * @param cmd: The DoorLockOperationReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def result = []

	unschedule
	unschedule

	// DoorLockOperationReport is called when trying to read the lock state or when the lock is locked/unlocked from the DTH or the smart app
	def map = [ name: "lock" ]
	if ) {
		map.value = cmd.doorCondition >> 1 ? "unlocked" : "locked"
		map.descriptionText = cmd.doorCondition >> 1 ? "Unlocked" : "Locked"
	} else if  {
		map.value = "locked"
		map.descriptionText = "Locked"
	} else if  {
		map.value = "unknown"
		map.descriptionText = "Unknown state"
	} else if  {
		map.value = "unlocked with timeout"
		map.descriptionText = "Unlocked with timeout"
	}  else {
		map.value = "unlocked"
		map.descriptionText = "Unlocked"
		if  {
			result << response))
			result << response)
			result << response))
		}
	}
	if ) {
		// we're expecting lock events to come after notification events, but for specific yale locks they come out of order
		runIn
		return [:]
	} else {
		return result ? [createEvent, *result] : createEvent
	}
}

def delayLockEvent {
	log.debug "Sending cached lock operation: $data.map"
	sendEvent
}

/**
 * Responsible for parsing AlarmReport command
 *
 * @param cmd: The AlarmReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def result = []

	if  {
		result = handleAccessAlarmReport
	} else if  {
		result = handleBurglarAlarmReport
	} else if {
		result = handleBatteryAlarmReport
	} else {
		result = handleAlarmReportUsingAlarmType
	}

	result = result ?: null
	log.debug "[DTH] zwaveEvent returning with result = $result"
	result
}

/**
 * Responsible for handling Access AlarmReport command
 *
 * @param cmd: The AlarmReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
private def handleAccessAlarmReport {
	log.trace "[DTH] Executing 'handleAccessAlarmReport' with cmd = $cmd"
	def result = []
	def map = null
	def codeID, changeType, lockCodes, codeName
	def deviceName = device.displayName
	lockCodes = loadLockCodes
	if  {
		map = [ name: "lock", value:  ? "locked" : "unlocked" ]
	}
	switch {
		case 1: // Manually locked
			map.descriptionText = "Locked manually"
			map.data = [ method:  ? "keypad" : "manual" ]
			break
		case 2: // Manually unlocked
			map.descriptionText = "Unlocked manually"
			map.data = [ method: "manual" ]
			break
		case 3: // Locked by command
			map.descriptionText = "Locked"
			map.data = [ method: "command" ]
			break
		case 4: // Unlocked by command
			map.descriptionText = "Unlocked"
			map.data = [ method: "command" ]
			break
		case 5: // Locked with keypad
			if  {
				codeID = readCodeSlotId
				codeName = getCodeName
				map.descriptionText = "Locked by \"$codeName\""
				map.data = [ codeId: codeID as String, codeName: codeName, method: "keypad" ]
			} else {
				// locked by pressing the Schlage button
				map.descriptionText = "Locked manually"
				map.data = [ method: "keypad" ]
			}
			break
		case 6: // Unlocked with keypad
			if  {
				codeID = readCodeSlotId
				codeName = getCodeName
				map.descriptionText = "Unlocked by \"$codeName\""
				map.data = [ codeId: codeID as String, codeName: codeName, method: "keypad" ]
			}
			break
		case 7:
			map = [ name: "lock", value: "unknown", descriptionText: "Unknown state" ]
			map.data = [ method: "manual" ]
			break
		case 8:
			map = [ name: "lock", value: "unknown", descriptionText: "Unknown state" ]
			map.data = [ method: "command" ]
			break
		case 9: // Auto locked
			map = [ name: "lock", value: "locked", data: [ method: "auto" ] ]
			map.descriptionText = "Auto locked"
			break
		case 0xA:
			map = [ name: "lock", value: "unknown", descriptionText: "Unknown state" ]
			map.data = [ method: "auto" ]
			break
		case 0xB:
			map = [ name: "lock", value: "unknown", descriptionText: "Unknown state" ]
			break
		case 0xC: // All user codes deleted
			result = allCodesDeletedEvent
			map = [ name: "codeChanged", value: "all deleted", descriptionText: "Deleted all user codes", isStateChange: true ]
			map.data = [notify: true, notificationText: "Deleted all user codes in $deviceName at ${location.name}"]
			result << createEvent, displayed: false, descriptionText: "'lockCodes' attribute updated")
			break
		case 0xD: // User code deleted
			if  {
				codeID = readCodeSlotId
				if ]) {
					codeName = getCodeName
					map = [ name: "codeChanged", value: "$codeID deleted", isStateChange: true ]
					map.descriptionText = "Deleted \"$codeName\""
					map.data = [ codeName: codeName, notify: true, notificationText: "Deleted \"$codeName\" in $deviceName at ${location.name}" ]
					result << codeDeletedEvent
				}
			}
			break
		case 0xE: // Master or user code changed/set
			if  {
				codeID = readCodeSlotId
				if) {
					//Ignoring this AlarmReport as Kwikset reports codeID 0 when all slots are full and user tries to set another lock code manually
					//Kwikset locks don't send AlarmReport when Master code is set
					log.trace "Ignoring this alarm report in case of Kwikset locks"
					break
				}
				codeName = getCodeNameFromState
				changeType = getChangeType
				map = [ name: "codeChanged", value: "$codeID $changeType",  descriptionText: "${getStatusForDescription} \"$codeName\"", isStateChange: true ]
				map.data = [ codeName: codeName, notify: true, notificationText: "${getStatusForDescription} \"$codeName\" in $deviceName at ${location.name}" ]
				if) {
					result << codeSetEvent
				} else {
					map.descriptionText = "${getStatusForDescription} \"$codeName\""
					map.data.notificationText = "${getStatusForDescription} \"$codeName\" in $deviceName at ${location.name}"
				}
			}
			break
		case 0xF: // Duplicate Pin-code error
			if  {
				codeID = readCodeSlotId
				clearStateForSlot
				map = [ name: "codeChanged", value: "$codeID failed", descriptionText: "User code is duplicate and not added",
						isStateChange: true, data: [isCodeDuplicate: true] ]
			}
			break
		case 0x10: // Tamper Alarm
		case 0x13:
			map = [ name: "tamper", value: "detected", descriptionText: "Keypad attempts exceed code entry limit", isStateChange: true, displayed: true ]
			break
		case 0x11: // Keypad busy
			map = [ descriptionText: "Keypad is busy" ]
			break
		case 0x12: // Master code changed
			codeName = getCodeNameFromState
			map = [ name: "codeChanged", value: "0 set", descriptionText: "${getStatusForDescription} \"$codeName\"", isStateChange: true ]
			map.data = [ codeName: codeName, notify: true, notificationText: "${getStatusForDescription} \"$codeName\" in $deviceName at ${location.name}" ]
			break
		case 0x18: // KeyWe manual unlock
			map = [ name: "lock", value: "unlocked", data: [ method: "manual" ] ]
			map.descriptionText = "Unlocked manually"
			break
		case 0x19: // KeyWe manual lock
			map = [ name: "lock", value: "locked", data: [ method: "manual" ] ]
			map.descriptionText = "Locked manually"
			break
		case 0xFE:
			// delegating it to handleAlarmReportUsingAlarmType
			return handleAlarmReportUsingAlarmType
		default:
			// delegating it to handleAlarmReportUsingAlarmType
			return handleAlarmReportUsingAlarmType
	}

	if  {
		result << createEvent
	}
	result = result.flatten
	result
}

/**
 * Responsible for handling Burglar AlarmReport command
 *
 * @param cmd: The AlarmReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
private def handleBurglarAlarmReport {
	log.trace "[DTH] Executing 'handleBurglarAlarmReport' with cmd = $cmd"
	def result = []
	def deviceName = device.displayName

	def map = [ name: "tamper", value: "detected" ]
	switch  {
		case 0:
			map.value = "clear"
			map.descriptionText = "Tamper alert cleared"
			break
		case 1:
		case 2:
			map.descriptionText = "Intrusion attempt detected"
			break
		case 3:
			map.descriptionText = "Covering removed"
			break
		case 4:
			map.descriptionText = "Invalid code"
			break
		default:
			// delegating it to handleAlarmReportUsingAlarmType
			return handleAlarmReportUsingAlarmType
	}

	result << createEvent
	result
}

/**
 * Responsible for handling Battery AlarmReport command
 *
 * @param cmd: The AlarmReport command to be parsed
 *
 * @return The event to be sent out
 */
private def handleBatteryAlarmReport {
	log.trace "[DTH] Executing 'handleBatteryAlarmReport' with cmd = $cmd"
	def result = []
	def deviceName = device.displayName
	def map = null
	switch {
		case 0x01: //power has been applied, check if the battery level updated
			log.debug "Batteries replaced. Queueing a battery get."
			runIn
			state.batteryQueries = 0
			result << response))
			break;
		case 0x0A:
			map = [ name: "battery", value: 1, descriptionText: "Battery level critical", displayed: true]
			break
		case 0x0B:
			map = [ name: "battery", value: 0, descriptionText: "Battery too low to operate lock", isStateChange: true, displayed: true]
			break
		default:
			// delegating it to handleAlarmReportUsingAlarmType
			return handleAlarmReportUsingAlarmType
	}
	result << createEvent
	result
}

/**
 * Responsible for handling AlarmReport commands which are ignored by Access & Burglar handlers
 *
 * @param cmd: The AlarmReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
private def handleAlarmReportUsingAlarmType {
	log.trace "[DTH] Executing 'handleAlarmReportUsingAlarmType' with cmd = $cmd"
	def result = []
	def map = null
	def codeID, lockCodes, codeName
	def deviceName = device.displayName
	lockCodes = loadLockCodes
	switch {
		case 9:
		case 17:
			map = [ name: "lock", value: "unknown", descriptionText: "Unknown state" ]
			break
		case 16: // Note: for levers this means it's unlocked, for non-motorized deadbolt, it's just unsecured and might not get unlocked
		case 19: // Unlocked with keypad
			map = [ name: "lock", value: "unlocked" ]
			if  {
				codeID = readCodeSlotId
				codeName = getCodeName
				map.isStateChange = true // Non motorized locks, mark state changed since it can be unlocked multiple times
				map.descriptionText = "Unlocked by \"$codeName\""
				map.data = [ codeId: codeID as String, codeName: codeName, method: "keypad" ]
			}
			break
		case 18: // Locked with keypad
			codeID = readCodeSlotId
			map = [ name: "lock", value: "locked" ]
			// Kwikset lock reporting code id as 0 when locked using the lock keypad button
			if  && codeID == 0) {
				map.descriptionText = "Locked manually"
				map.data = [ method: "manual" ]
			} else {
				codeName = getCodeName
				map.descriptionText = "Locked by \"$codeName\""
				map.data = [ codeId: codeID as String, codeName: codeName, method: "keypad" ]
			}
			break
		case 21: // Manually locked
			map = [ name: "lock", value: "locked", data: [ method:  ? "keypad" : "manual" ] ]
			map.descriptionText = "Locked manually"
			break
		case 22: // Manually unlocked
			map = [ name: "lock", value: "unlocked", data: [ method: "manual" ] ]
			map.descriptionText = "Unlocked manually"
			break
		case 23:
			map = [ name: "lock", value: "unknown", descriptionText: "Unknown state" ]
			map.data = [ method: "command" ]
			break
		case 24: // Locked by command
			map = [ name: "lock", value: "locked", data: [ method: "command" ] ]
			map.descriptionText = "Locked"
			break
		case 25: // Unlocked by command
			map = [ name: "lock", value: "unlocked", data: [ method: "command" ] ]
			map.descriptionText = "Unlocked"
			break
		case 26:
			map = [ name: "lock", value: "unknown", descriptionText: "Unknown state" ]
			map.data = [ method: "auto" ]
			break
		case 27: // Auto locked
			map = [ name: "lock", value: "locked", data: [ method: "auto" ] ]
			map.descriptionText = "Auto locked"
			break
		case 32: // All user codes deleted
			result = allCodesDeletedEvent
			map = [ name: "codeChanged", value: "all deleted", descriptionText: "Deleted all user codes", isStateChange: true ]
			map.data = [notify: true, notificationText: "Deleted all user codes in $deviceName at ${location.name}"]
			result << createEvent, displayed: false, descriptionText: "'lockCodes' attribute updated")
			break
		case 33: // User code deleted
			codeID = readCodeSlotId
			if ]) {
				codeName = getCodeName
				map = [ name: "codeChanged", value: "$codeID deleted", isStateChange: true ]
				map.descriptionText = "Deleted \"$codeName\""
				map.data = [ codeName: codeName, notify: true, notificationText: "Deleted \"$codeName\" in $deviceName at ${location.name}" ]
				result << codeDeletedEvent
			}
			break
		case 38: // Non Access
			map = [ descriptionText: "A Non Access Code was entered at the lock", isStateChange: true ]
			break
		case 13:
		case 112: // Master or user code changed/set
			codeID = readCodeSlotId
			if) {
				//Ignoring this AlarmReport as Kwikset reports codeID 0 when all slots are full and user tries to set another lock code manually
				//Kwikset locks don't send AlarmReport when Master code is set
				log.trace "Ignoring this alarm report in case of Kwikset locks"
				break
			}
			codeName = getCodeNameFromState
			def changeType = getChangeType
			map = [ name: "codeChanged", value: "$codeID $changeType", descriptionText:
					"${getStatusForDescription} \"$codeName\"", isStateChange: true ]
			map.data = [ codeName: codeName, notify: true, notificationText: "${getStatusForDescription} \"$codeName\" in $deviceName at ${location.name}" ]
			if) {
				result << codeSetEvent
			} else {
				map.descriptionText = "${getStatusForDescription} \"$codeName\""
				map.data.notificationText = "${getStatusForDescription} \"$codeName\" in $deviceName at ${location.name}"
			}
			break
		case 34:
		case 113: // Duplicate Pin-code error
			codeID = readCodeSlotId
			clearStateForSlot
			map = [ name: "codeChanged", value: "$codeID failed", descriptionText: "User code is duplicate and not added",
					isStateChange: true, data: [isCodeDuplicate: true] ]
			break
		case 130:  // Batteries replaced
			map = [ descriptionText: "Batteries replaced", isStateChange: true ]
			log.debug "Batteries replaced. Queueing a battery check."
			runIn
			state.batteryQueries = 0
			result << response))
			break
		case 131: // Disabled user entered at keypad
			map = [ descriptionText: "Code ${cmd.alarmLevel} is disabled", isStateChange: false ]
			break
		case 161: // Tamper Alarm
			if  {
				map = [ name: "tamper", value: "detected", descriptionText: "Front escutcheon removed", isStateChange: true ]
			} else {
				map = [ name: "tamper", value: "detected", descriptionText: "Keypad attempts exceed code entry limit", isStateChange: true, displayed: true ]
			}
			break
		case 167: // Low Battery Alarm
			if  - state.lastbatt > 12*60*60*1000) {
				map = [ descriptionText: "Battery low", isStateChange: true ]
				result << response))
			} else {
				map = [ name: "battery", value: device.currentValue, descriptionText: "Battery low", isStateChange: true ]
			}
			break
		case 168: // Critical Battery Alarms
			map = [ name: "battery", value: 1, descriptionText: "Battery level critical", displayed: true ]
			break
		case 169: // Battery too low to operate
			map = [ name: "battery", value: 0, descriptionText: "Battery too low to operate lock", isStateChange: true, displayed: true ]
			break
		default:
			map = [ displayed: false, descriptionText: "Alarm event ${cmd.alarmType} level ${cmd.alarmLevel}" ]
			break
	}

	if  {
		result << createEvent
	}
	result = result.flatten
	result
}

/**
 * Responsible for parsing UserCodeReport command
 *
 * @param cmd: The UserCodeReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with userIdentifier: ${cmd.userIdentifier} and status: ${cmd.userIdStatus}"
	def result = []
	// cmd.userIdentifier seems to be an int primitive type
	def codeID = cmd.userIdentifier.toString
	def lockCodes = loadLockCodes
	def map = [ name: "codeChanged", isStateChange: true ]
	def deviceName = device.displayName
	def userIdStatus = cmd.userIdStatus

	if (userIdStatus == UserCodeReport.USER_ID_STATUS_OCCUPIED ||
			) {

		def codeName

		// Schlage locks sends a blank/empty code during code creation/updation where as it sends "**********" during scanning
		// Some Schlage locks send "**********" during code creation also. The state check will work for them
		if  && isSchlageLock) {
			// this will be executed when the user tries to create/update a user code through the
			// smart app or manually on the lock. This is specific to Schlage locks.
			log.trace "[DTH] User code creation successful for Schlage lock"
			codeName = getCodeNameFromState
			def changeType = getChangeType

			map.value = "$codeID $changeType"
			map.isStateChange = true
			map.descriptionText = "${getStatusForDescription} \"$codeName\""
			map.data = [ codeName: codeName, notify: true, notificationText: "${getStatusForDescription} \"$codeName\" in $deviceName at ${location.name}" ]
			if) {
				result << codeSetEvent
			} else {
				map.descriptionText = "${getStatusForDescription} \"$codeName\""
				map.data.notificationText = "${getStatusForDescription} \"$codeName\" in $deviceName at ${location.name}"
			}
		} else {
			// We'll land here during scanning of codes
			codeName = getCodeName
			def changeType = getChangeType
			if  {
				result << codeSetEvent
			} else {
				map.displayed = false
			}
			map.value = "$codeID $changeType"
			map.descriptionText = "${getStatusForDescription} \"$codeName\""
			map.data = [ codeName: codeName ]
		}
	} else if) {
		// This is code creation/updation error for Schlage locks.
		// It should be OK to mark this as duplicate pin code error since in case the batteries are down, or lock is not in range,
		// or wireless interference is there, the UserCodeReport will anyway not be received.
		map = [ name: "codeChanged", value: "$codeID failed", descriptionText: "User code is not added", isStateChange: true,
				data: [ isCodeDuplicate: true] ]
	} else {
		// We are using userIdStatus here because codeID = 0 is reported when user tries to set programming code as the user code
		if ) {
			// all codes deleted for Schlage locks
			log.trace "[DTH] All user codes deleted for Schlage lock"
			result << allCodesDeletedEvent
			map = [ name: "codeChanged", value: "all deleted", descriptionText: "Deleted all user codes", isStateChange: true,
					data: [ notify: true,
							notificationText: "Deleted all user codes in $deviceName at ${location.name}"] ]
			lockCodes = [:]
			result << lockCodesEvent
		} else {
			// code is not set
			if  {
				def codeName = getCodeName
				map.value = "$codeID deleted"
				map.descriptionText = "Deleted \"$codeName\""
				map.data = [ codeName: codeName, notify: true, notificationText: "Deleted \"$codeName\" in $deviceName at ${location.name}" ]
				result << codeDeletedEvent
			} else {
				map.value = "$codeID unset"
				map.displayed = false
			}
		}
	}

	clearStateForSlot
	result << createEvent

	if  == state.checkCode) {  // reloadAllCodes was called, keep requesting the codes in order
		if  {
			state.remove  // done
			state["checkCode"] = null
			sendEvent
		} else {
			state.checkCode = state.checkCode + 1  // get next
			result << response)
		}
	}
	if  == state.pollCode) {
		if  {
			state.remove  // done
			state["pollCode"] = null
		} else {
			state.pollCode = state.pollCode + 1
		}
	}

	result = result.flatten
	result
}

/**
 * Responsible for parsing UsersNumberReport command
 *
 * @param cmd: The UsersNumberReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def result = [createEvent]
	state.codes = cmd.supportedUsers
	if  {
		if  {
			result << response)
		} else {
			state.remove
			state["checkCode"] = null
		}
	}
	result
}

/**
 * Responsible for parsing AssociationReport command
 *
 * @param cmd: The AssociationReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def result = []
	if  {
		state.remove
		state["associationQuery"] = null
		result << createEvent
		state.assoc = zwaveHubNodeId
		if  {
			result << response)
		}
	} else if  {
		result << response))
	} else if  {
		result << response)
	}
	result
}

/**
 * Responsible for parsing TimeGet command
 *
 * @param cmd: The TimeGet command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def result = []
	def now = new Date.toCalendar
	if now.timeZone = location.timeZone
	result << createEvent
	result << response(secure(zwave.timeV1.timeReport(
			hourLocalTime: now.get,
			minuteLocalTime: now.get,
			secondLocalTime: now.get))
	)
	result
}

/**
 * Responsible for parsing BasicSet command
 *
 * @param cmd: The BasicSet command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	// The old Schlage locks use group 1 for basic control - we don't want that, so unsubscribe from group 1
	def result = [ createEvent ]
	def cmds = [
			zwave.associationV1.associationRemove.format,
			"delay 1200",
			zwave.associationV1.associationGet.format
	]
	[result, response]
}

/**
 * Responsible for parsing BatteryReport command
 *
 * @param cmd: The BatteryReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def map = [ name: "battery", unit: "%" ]
	if  {
		map.value = 1
		map.descriptionText = "Has a low battery"
	} else {
		map.value = cmd.batteryLevel
		map.descriptionText = "Battery is at ${cmd.batteryLevel}%"
	}
	state.lastbatt = now
	unschedule
	createEvent
}

/**
 * Responsible for parsing ManufacturerSpecificReport command
 *
 * @param cmd: The ManufacturerSpecificReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def result = []
	def msr = String.format
	updateDataValue
	result << createEvent
	result
}

/**
 * Responsible for parsing VersionReport command
 *
 * @param cmd: The VersionReport command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def fw = "${cmd.applicationVersion}.${cmd.applicationSubVersion}"
	updateDataValue
	if  == "003B-6341-5044") {
		updateDataValue
	}
	def text = "${device.displayName}: firmware version: $fw, Z-Wave version: ${cmd.zWaveProtocolVersion}.${cmd.zWaveProtocolSubVersion}"
	createEvent
}

/**
 * Responsible for parsing ApplicationBusy command
 *
 * @param cmd: The ApplicationBusy command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	def msg = cmd.status == 0 ? "try again later" :
			cmd.status == 1 ? "try again in ${cmd.waitTime} seconds" :
					cmd.status == 2 ? "request queued" : "sorry"
	createEvent
}

/**
 * Responsible for parsing ApplicationRejectedRequest command
 *
 * @param cmd: The ApplicationRejectedRequest command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	createEvent
}

/**
 * Responsible for parsing zwave command
 *
 * @param cmd: The zwave command to be parsed
 *
 * @return The event to be sent out
 *
 */
def zwaveEvent {
	log.trace "[DTH] Executing 'zwaveEvent' with cmd = $cmd"
	createEvent
}

/**
 * Executes lock and then check command with a delay on a lock
 */
def lockAndCheck {
	secureSequence([
			zwave.doorLockV1.doorLockOperationSet,
			zwave.doorLockV1.doorLockOperationGet
	], 4200)
}

/**
 * Executes lock command on a lock
 */
def lock {
	log.trace "[DTH] Executing lock for device ${device.displayName}"
	lockAndCheck
}

/**
 * Executes unlock command on a lock
 */
def unlock {
	log.trace "[DTH] Executing unlock for device ${device.displayName}"
	lockAndCheck
}

/**
 * Executes unlock with timeout command on a lock
 */
def unlockWithTimeout {
	log.trace "[DTH] Executing unlockWithTimeout for device ${device.displayName}"
	lockAndCheck
}

/**
 * PING is used by Device-Watch in attempt to reach the Device
 */
def ping {
	log.trace "[DTH] Executing ping for device ${device.displayName}"
	runIn
	secure)
}

/**
 * Checks the door lock state. Also, schedules checking of door lock state every one hour.
 */
def followupStateCheck {
	runEvery1Hour
	stateCheck
}

/**
 * Checks the door lock state
 */
def stateCheck {
	sendHubCommand)))
}

/**
 * Called when the user taps on the refresh button
 */
def refresh {
	log.trace "[DTH] Executing refresh for device ${device.displayName}"

	def cmds = secureSequence, zwave.batteryV1.batteryGet])
	if  {
		cmds << "delay 4200"
		cmds << zwave.associationV1.associationGet.format  // old Schlage locks use group 2 and don't secure the Association CC
		cmds << secure)
		state.associationQuery = now
	} else if  - state.associationQuery.toLong > 9000) {
		cmds << "delay 6000"
		cmds << zwave.associationV1.associationSet.format
		cmds << secure)
		cmds << zwave.associationV1.associationGet.format
		cmds << secure)
		state.associationQuery = now
	}
	state.lastLockDetailsQuery = now

	cmds
}

/**
 * Called by the Smart Things platform in case Polling capability is added to the device type
 */
def poll {
	log.trace "[DTH] Executing poll for device ${device.displayName}"
	def cmds = []
	// Only check lock state if it changed recently or we haven't had an update in an hour
	def latest = device.currentState?.date?.time
	if  || secondsPast) {
		cmds << secure)
		state.lastPoll = now
	} else if  - state.lastbatt > 53*60*60*1000) {
		cmds << secure)
		state.lastbatt = now  //inside-214
	}
	if ) {
		cmds << zwave.associationV1.associationSet.format
		cmds << secure)
		cmds << zwave.associationV1.associationGet.format
		cmds << "delay 6000"
		cmds << secure)
		cmds << "delay 6000"
		state.associationQuery = now
	} else {
		// Only check lock state once per hour
		if ) {
			cmds << secure)
			state.lastPoll = now
		} else if  {
			cmds << zwave.manufacturerSpecificV1.manufacturerSpecificGet.format
		} else if  {
			cmds << zwave.versionV1.versionGet.format
		} else if ) {
			state.pollCode = 1
			cmds << secure)
		} else if  {
			cmds << requestCode
		} else if  - state.lastbatt > 53*60*60*1000) {
			cmds << secure)
		}
	}

	if  {
		log.debug "poll is sending ${cmds.inspect}"
		cmds
	} else {
		// workaround to keep polling from stopping due to lack of activity
		sendEvent
		null
	}
}

/**
 * Returns the command for user code get
 *
 * @param codeID: The code slot number
 *
 * @return The command for user code get
 */
def requestCode {
	secure)
}

/**
 * API endpoint for server smart app to populate the attributes. Called only when the attributes are not populated.
 *
 * @return The command fired for reading attributes
 */
def reloadAllCodes {
	log.trace "[DTH] Executing 'reloadAllCodes' by ${device.displayName}"
	sendEvent
	def lockCodes = loadLockCodes
	sendEvent)
	state.checkCode = state.checkCode ?: 1

	def cmds = []
	// Not calling validateAttributes here because userNumberGet command will be added twice
	if && isSchlageLock) {
		cmds << secure.codeLength.number))
	}
	if  {
		// BUG: There might be a bug where Schlage does not return the below number of codes
		cmds << secure)
	} else {
		sendEvent
		cmds << requestCode
	}
	if > 1) {
		cmds = delayBetween
	}
	cmds
}

/**
 * API endpoint for setting the user code length on a lock. This is specific to Schlage locks.
 *
 * @param length: The user code length
 *
 * @returns The command fired for writing the code length attribute
 */
def setCodeLength {
	if ) {
		length = length.toInteger
		if  {
			log.trace "[DTH] Executing 'setCodeLength' by ${device.displayName}"
			def val = []
			val << length
			def param = getSchlageLockParam
			return secure)
		}
	}
	return null
}

/**
 * API endpoint for setting a user code on a lock
 *
 * @param codeID: The code slot number
 *
 * @param code: The code PIN
 *
 * @param codeName: The name of the code
 *
 * @returns cmds: The commands fired for creation and checking of a lock code
 */
def setCode {
	if  {
		log.trace "[DTH] Executing 'nameSlot' by ${this.device.displayName}"
		nameSlot
		return
	}

	log.trace "[DTH] Executing 'setCode' by ${this.device.displayName}"
	def strcode = code
	if  {
		code = code.toList.findResults { if it.toCharacter as Short }
	} else {
		strcode = code.collect{ it as Character }.join
	}

	def strname = 
	state["setname$codeID"] = strname

	def cmds = validateAttributes
	cmds << secure)
	if > 1) {
		cmds = delayBetween
	}
	cmds
}

/**
 * Validates attributes and if attributes are not populated, adds the command maps to list of commands
 * @return List of commands or empty list
 */
def validateAttributes {
	def cmds = []
	if) {
		cmds << secure)
	}
	if && isSchlageLock) {
		cmds << secure.codeLength.number))
	}
	log.trace "validateAttributes returning commands list: " + cmds
	cmds
}

/**
 * API endpoint for setting/deleting multiple user codes on a lock
 *
 * @param codeSettings: The map with code slot numbers and code pins 
 *
 * @returns The commands fired for creation and deletion of lock codes
 */
def updateCodes {
	log.trace "[DTH] Executing updateCodes for device ${device.displayName}"
	if codeSettings = util.parseJson
	def set_cmds = []
	codeSettings.each { name, updated ->
		if ) {
			def n = name[4..-1].toInteger
			if  >= 4 && updated.size <= 8) {
				log.debug "Setting code number $n"
				set_cmds << secure)
			} else if  {
				log.debug "Deleting code number $n"
				set_cmds << deleteCode
			}
		} else log.warn
	}
	if  {
		return response)
	}
	return null
}

/**
 * Renames an existing lock slot
 *
 * @param codeSlot: The code slot number
 *
 * @param codeName The new name of the code
 */
void nameSlot {
	codeSlot = codeSlot.toString
	if ) {
		return
	}
	def deviceName = device.displayName
	log.trace "[DTH] - Executing nameSlot for device $deviceName"
	def lockCodes = loadLockCodes
	def oldCodeName = getCodeName
	def newCodeName = codeName ?: "Code $codeSlot"
	lockCodes[codeSlot] = newCodeName
	sendEvent)
	sendEvent(name: "codeChanged", value: "$codeSlot renamed", data: [ notify: false, notificationText: "Renamed \"$oldCodeName\" to \"$newCodeName\" in $deviceName at ${location.name}" ],
			descriptionText: "Renamed \"$oldCodeName\" to \"$newCodeName\"", displayed: true, isStateChange: true)
}

/**
 * API endpoint for deleting a user code on a lock
 *
 * @param codeID: The code slot number
 *
 * @returns cmds: The command fired for deletion of a lock code
 */
def deleteCode {
	log.trace "[DTH] Executing 'deleteCode' by ${this.device.displayName}"
	// Calling user code get when deleting a code because some Kwikset locks do not generate
	// AlarmReport when a code is deleted manually on the lock
	secureSequence([
			zwave.userCodeV1.userCodeSet,
			zwave.userCodeV1.userCodeGet
	], 4200)
}

/**
 * Encapsulates a command
 *
 * @param cmd: The command to be encapsulated
 *
 * @returns ret: The encapsulated command
 */
private secure {
	zwave.securityV1.securityMessageEncapsulation.encapsulate.format
}

/**
 * Encapsulates list of command and adds a delay
 *
 * @param commands: The list of command to be encapsulated
 *
 * @param delay: The delay between commands
 *
 * @returns The encapsulated commands
 */
private secureSequence {
	delayBetween }, delay)
}

/**
 * Checks if the time elapsed from the provided timestamp is greater than the number of senconds provided
 *
 * @param timestamp: The timestamp
 *
 * @param seconds: The number of seconds
 *
 * @returns true if elapsed time is greater than number of seconds provided, else false
 */
private Boolean secondsPast {
	if ) {
		if  {
			timestamp = timestamp.time
		} else if  && timestamp.isNumber) {
			timestamp = timestamp.toLong
		} else {
			return true
		}
	}
	return  - timestamp) > 
}

/**
 * Reads the code name from the 'lockCodes' map
 *
 * @param lockCodes: map with lock code names
 *
 * @param codeID: The code slot number
 *
 * @returns The code name
 */
private String getCodeName {
	if ) {
		return "Master Code"
	}
	lockCodes[codeID.toString] ?: "Code $codeID"
}

/**
 * Reads the code name from the device state
 *
 * @param lockCodes: map with lock code names
 *
 * @param codeID: The code slot number
 *
 * @returns The code name
 */
private String getCodeNameFromState {
	if ) {
		return "Master Code"
	}
	def nameFromLockCodes = lockCodes[codeID.toString]
	def nameFromState = state["setname$codeID"]
	if {
		if {
			//Updated from smart app
			return nameFromState
		} else {
			//Updated from lock
			return nameFromLockCodes
		}
	} else if {
		//Set from smart app
		return nameFromState
	}
	//Set from lock
	return "Code $codeID"
}

/**
 * Check if a user code is present in the 'lockCodes' map
 *
 * @param codeID: The code slot number
 *
 * @returns true if code is present, else false
 */
private Boolean isCodeSet {
	// BUG: Needed to add loadLockCodes to resolve null pointer when using schlage?
	def lockCodes = loadLockCodes
	lockCodes[codeID.toString] ? true : false
}

/**
 * Reads the 'lockCodes' attribute and parses the same
 *
 * @returns Map: The lockCodes map
 */
private Map loadLockCodes {
	parseJson ?: "{}") ?: [:]
}

/**
 * Populates the 'lockCodes' attribute by calling create event
 *
 * @param lockCodes The user codes in a lock
 */
private Map lockCodesEvent {
	createEvent, displayed: false,
			descriptionText: "'lockCodes' attribute updated")
}

/**
 * Utility function to figure out if code id pertains to master code or not
 *
 * @param codeID - The slot number in which code is set
 * @return - true if slot is for master code, false otherwise
 */
private boolean isMasterCode {
	if {
		codeID = codeID.toInteger
	}
	 ? true : false
}

/**
 * Creates the event map for user code creation
 *
 * @param lockCodes: The user codes in a lock
 *
 * @param codeID: The code slot number
 *
 * @param codeName: The name of the user code
 *
 * @return The list of events to be sent out
 */
private def codeSetEvent {
	clearStateForSlot
	// codeID seems to be an int primitive type
	lockCodes[codeID.toString] = 
	def result = []
	result << lockCodesEvent
	def codeReportMap = [ name: "codeReport", value: codeID, data: [ code: "" ], isStateChange: true, displayed: false ]
	codeReportMap.descriptionText = "${device.displayName} code $codeID is set"
	result << createEvent
	result
}

/**
 * Creates the event map for user code deletion
 *
 * @param lockCodes: The user codes in a lock
 *
 * @param codeID: The code slot number
 *
 * @return The list of events to be sent out
 */
private def codeDeletedEvent {
	lockCodes.remove)
	// not sure if the trigger has done this or not
	clearStateForSlot
	def result = []
	result << lockCodesEvent
	def codeReportMap = [ name: "codeReport", value: codeID, data: [ code: "" ], isStateChange: true, displayed: false ]
	codeReportMap.descriptionText = "${device.displayName} code $codeID was deleted"
	result << createEvent
	result
}

/**
 * Creates the event map for all user code deletion
 *
 * @return The List of events to be sent out
 */
private def allCodesDeletedEvent {
	def result = []
	def lockCodes = loadLockCodes
	def deviceName = device.displayName
	lockCodes.each { id, code ->
		result << createEvent(name: "codeReport", value: id, data: [ code: "" ], descriptionText: "code $id was deleted",
				displayed: false, isStateChange: true)

		def codeName = code
		result << createEvent(name: "codeChanged", value: "$id deleted", data: [ codeName: codeName, notify: true, notificationText: "Deleted \"$codeName\" in $deviceName at ${location.name}" ],
				descriptionText: "Deleted \"$codeName\"",
				displayed: true, isStateChange: true)
		clearStateForSlot
	}
	result
}

/**
 * Checks if a change type is set or update
 *
 * @param lockCodes: The user codes in a lock
 *
 * @param codeID The code slot number
 *
 * @return "set" or "update" basis the presence of the code id in the lockCodes map
 */
private def getChangeType {
	def changeType = "set"
	if ]) {
		changeType = "changed"
	}
	changeType
}

/**
 * Method to obtain status for descriptuion based on change type
 * @param changeType: Either "set" or "changed"
 * @return "Added" for "set", "Updated" for "changed", "" otherwise
 */
private def getStatusForDescription {
	if {
		return "Added"
	} else if {
		return "Updated"
	}
	//Don't return null as it cause trouble
	return ""
}

/**
 * Clears the code name and pin from the state basis the code slot number
 *
 * @param codeID: The code slot number
 */
def clearStateForSlot {
	state.remove
	state["setname$codeID"] = null
}

/**
 * Constructs a map of the code length parameter in Schlage lock
 *
 * @return map: The map with key and values for parameter number, and size
 */
def getSchlageLockParam {
	def map = [
			codeLength: [ number: 16, size: 1]
	]
	map
}

/**
 * Utility function to check if the lock manufacturer is Schlage
 *
 * @return true if the lock manufacturer is Schlage, else false
 */
def isSchlageLock {
	if  {
		if) {
			updateDataValue
		}
		return true
	}
	return false
}

/**
 * Utility function to check if the lock manufacturer is Kwikset
 *
 * @return true if the lock manufacturer is Kwikset, else false
 */
def isKwiksetLock {
	if  {
		if) {
			updateDataValue
		}
		return true
	}
	return false
}

/**
 * Utility function to check if the lock manufacturer is Yale
 *
 * @return true if the lock manufacturer is Yale, else false
 */
def isYaleLock {
	if  {
		if) {
			updateDataValue
		}
		return true
	}
	return false
}

/**
 * Utility function to check if the lock manufacturer is Samsung
 *
 * @return true if the lock manufacturer is Samsung, else false
 */
private isSamsungLock {
	if  {
		if ) {
			updateDataValue
		}
		return true
	}
	return false
}

/**
 * Utility function to check if the lock manufacturer is KeyWe
 *
 * @return true if the lock manufacturer is KeyWe, else false
 */
private isKeyweLock {
	if  {
		if ) {
			updateDataValue
		}
		return true
	}
	return false
}

/**
 * Returns true if this lock generates door lock operation report before alarm report, false otherwise
 * @return true if this lock generates door lock operation report before alarm report, false otherwise
 */
def generatesDoorLockOperationReportBeforeAlarmReport {
	//Fix for ICP-2367, ICP-2366
	if &&
			 ||
					 )) {
		//Yale Keyless Connected Smart Door Lock and Conexis
		return true
	}
	return false
}

/**
 * Generic function for reading code Slot ID from AlarmReport command
 * @param cmd: The AlarmReport command
 * @return user code slot id
 */
def readCodeSlotId {
	if {
		return cmd.eventParameter[0]
	} else if {
		return cmd.eventParameter[2]
	}
	return cmd.alarmLevel
}

private queryBattery {
	log.debug "Running queryBattery"
	if  state.batteryQueries = 0
	if  - state.lastbatt > 10*1000) && state.batteryQueries < 5) {
		log.debug "It's been more than 10s since battery was updated after a replacement. Querying battery."
		runIn
		state.batteryQueries = state.batteryQueries + 1
		sendHubCommand))
	}
}
