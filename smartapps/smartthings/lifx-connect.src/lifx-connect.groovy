/**
 *  LIFX
 *
 *  Copyright 2015 LIFX
 *
 */
definition(
		name: "LIFX (Connect)",
		namespace: "smartthings",
		author: "LIFX",
		description: "Allows you to use LIFX smart light bulbs with SmartThings.",
		category: "Convenience",
		iconUrl: "https://cloud.lifx.com/images/lifx.png",
		iconX2Url: "https://cloud.lifx.com/images/lifx.png",
		iconX3Url: "https://cloud.lifx.com/images/lifx.png",
		oauth: true,
		singleInstance: true) {
	appSetting "clientId"
	appSetting "clientSecret"
}


preferences {
	page(name: "Credentials", title: "LIFX", content: "authPage", install: true)
}

mappings {
	path("/receivedToken") { action: [ POST: "oauthReceivedToken", GET: "oauthReceivedToken"] }
	path("/receiveToken") { action: [ POST: "oauthReceiveToken", GET: "oauthReceiveToken"] }
	path("/hookCallback") { action: [ POST: "hookEventHandler", GET: "hookEventHandler"] }
	path("/oauth/callback") { action: [ GET: "oauthCallback" ] }
	path("/oauth/initialize") { action: [ GET: "oauthInit"] }
	path("/test") { action: [ GET: "oauthSuccess" ] }
}

def getServerUrl()               { return "https://graph.api.smartthings.com" }
def getCallbackUrl()             { return "https://graph.api.smartthings.com/oauth/callback"}
def apiURL(path = '/') 			 { return "https://api.lifx.com/v1${path}" }
def getSecretKey()               { return appSettings.secretKey }
def getClientId()                { return appSettings.clientId }

def authPage() {
	state.accessToken = true
    state.lifxAccessToken = "c48dc236e76961d5069dd77e1363455da4e28f6b599ae15f95c6d0e6de572ca8"
	log.debug "authPage testING"
	if (!state.lifxAccessToken) {
		log.debug "no LIFX access token"
		// This is the SmartThings access token
		if (!state.accessToken) {
			log.debug "no access token, create access token"
			state.accessToken = createAccessToken() // predefined method
		}
		def description = "Tap to enter LIFX credentials"
		def redirectUrl = "${serverUrl}/oauth/initialize?appId=${app.id}&access_token=${state.accessToken}&apiServerUrl=${apiServerUrl}" // this triggers oauthInit() below
		// def redirectUrl = "${apiServerUrl}"
		// log.debug "app id: ${app.id}"
		// log.debug "redirect url: ${redirectUrl}"s
		return dynamicPage(name: "Credentials", title: "Connect to LIFX", nextPage: null, uninstall: true, install:true) {
			section {
				href(url:redirectUrl, required:true, title:"Connect to LIFX", description:"Tap here to connect your LIFX account")
			}
		}
	} else {
		log.debug "have LIFX access token"

		def options = locationOptions() ?: []
		def count = options.size()

		return dynamicPage(name:"Credentials", title:"", nextPage:"", install:true, uninstall: true) {
			section("Select your location") {
				input "selectedLocationId", "enum", required:true, title:"Select location (${count} found)", multiple:false, options:options, submitOnChange: true
                input "breaker", "capability.switch", title: "Breaker", multiple: false, required: false
			}
		}
	}
}

// OAuth

def oauthInit() {
	def oauthParams = [client_id: "${appSettings.clientId}", scope: "remote_control:all", response_type: "code" ]
	log.info("Redirecting user to OAuth setup")
	redirect(location: "https://cloud.lifx.com/oauth/authorize?${toQueryString(oauthParams)}")
}

def oauthCallback() {
	def redirectUrl = null
	if (params.authQueryString) {
		redirectUrl = URLDecoder.decode(params.authQueryString.replaceAll(".+&redirect_url=", ""))
	} else {
		log.warn "No authQueryString"
	}

	if (state.lifxAccessToken) {
		log.debug "Access token already exists"
		success()
	} else {
		def code = params.code
		if (code) {
			if (code.size() > 6) {
				// LIFX code
				log.debug "Exchanging code for access token"
				oauthReceiveToken(redirectUrl)
			} else {
				// Initiate the LIFX OAuth flow.
				oauthInit()
			}
		} else {
			log.debug "This code should be unreachable"
			success()
		}
	}
}

def oauthReceiveToken(redirectUrl = null) {
	// Not sure what redirectUrl is for
	log.debug "receiveToken - params: ${params}"
	def oauthParams = [ client_id: "${appSettings.clientId}", client_secret: "${appSettings.clientSecret}", grant_type: "authorization_code", code: params.code, scope: params.scope ] // how is params.code valid here?
	def params = [
			uri: "https://cloud.lifx.com/oauth/token",
			body: oauthParams,
			headers: [
					"User-Agent": "SmartThings Integration"
			]
	]
	httpPost(params) { response ->
		state.lifxAccessToken = response.data.access_token
	}

	if (state.lifxAccessToken) {
		oauthSuccess()
	} else {
		oauthFailure()
	}
}

def oauthSuccess() {
	def message = """
		<p>Your LIFX Account is now connected to SmartThings!</p>
		<p>Click 'Done' to finish setup.</p>
	"""
	oauthConnectionStatus(message)
}

def oauthFailure() {
	def message = """
		<p>The connection could not be established!</p>
		<p>Click 'Done' to return to the menu.</p>
	"""
	oauthConnectionStatus(message)
}

def oauthReceivedToken() {
	def message = """
		<p>Your LIFX Account is already connected to SmartThings!</p>
		<p>Click 'Done' to finish setup.</p>
	"""
	oauthConnectionStatus(message)
}

def oauthConnectionStatus(message, redirectUrl = null) {
	def redirectHtml = ""
	if (redirectUrl) {
		redirectHtml = """
			<meta http-equiv="refresh" content="3; url=${redirectUrl}" />
		"""
	}

	def html = """
		<!DOCTYPE html>
		<html>
		<head>
		<meta name="viewport" content="width=device-width">
		<title>SmartThings Connection</title>
		<style type="text/css">
			@font-face {
				font-family: 'Swiss 721 W01 Thin';
				src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot');
				src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot?#iefix') format('embedded-opentype'),
					 url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.woff') format('woff'),
					 url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.ttf') format('truetype'),
					 url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.svg#swis721_th_btthin') format('svg');
				font-weight: normal;
				font-style: normal;
			}
			@font-face {
				font-family: 'Swiss 721 W01 Light';
				src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot');
				src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot?#iefix') format('embedded-opentype'),
					 url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.woff') format('woff'),
					 url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.ttf') format('truetype'),
					 url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.svg#swis721_lt_btlight') format('svg');
				font-weight: normal;
				font-style: normal;
			}
			.container {
				width: 280;
				padding: 20px;
				text-align: center;
			}
			img {
				vertical-align: middle;
			}
			img:nth-child(2) {
				margin: 0 15px;
			}
			p {
				font-size: 1.2em;
				font-family: 'Swiss 721 W01 Thin';
				text-align: center;
				color: #666666;
				padding: 0 20px;
				margin-bottom: 0;
			}
			span {
				font-family: 'Swiss 721 W01 Light';
			}
		</style>
		${redirectHtml}
		</head>
		<body>
			<div class="container">
				<img src='https://cloud.lifx.com/images/lifx.png' alt='LIFX icon' width='100'/>
				<img src='https://s3.amazonaws.com/smartapp-icons/Partner/support/connected-device-icn%402x.png' alt='connected device icon' width="40"/>
				<img src='https://s3.amazonaws.com/smartapp-icons/Partner/support/st-logo%402x.png' alt='SmartThings logo' width="100"/>
				<p>
					${message}
				</p>
			</div>
		</body>
		</html>
	"""
	render contentType: 'text/html', data: html
}

String toQueryString(Map m) {
	return m.collect { k, v -> "${k}=${URLEncoder.encode(v.toString())}" }.sort().join("&")
}

// App lifecycle hooks

def installed() {
	if (!state.accessToken) {
		
        createAccessToken()
	} else {
		initialize()
	}
}

// called after settings are changed
def updated() {
	if (!state.accessToken) {
		createAccessToken()
	} else {
		initialize()
	}
}

def uninstalled() {
	log.info("Uninstalling, removing child devices...")
	unschedule('updateDevices')
	removeChildDevices(getChildDevices())
}

private removeChildDevices(devices) {
	devices.each {
		deleteChildDevice(it.deviceNetworkId) // 'it' is default
	}
}

def appTouch(evt) {
	updateDevices()
}

// called after Done is hit after selecting a Location
def initialize() {
	log.debug "initialize"
    subscribe(app, appTouch)
	updateDevices()
	// Check for new devices and remove old ones every 3 hours
	runEvery5Minutes('updateDevices')
	setupDeviceWatch()
}

// Misc
private setupDeviceWatch() {
	def hub = location.hubs[0]
	// Make sure that all child devices are enrolled in device watch
	getChildDevices().each {
		it.sendEvent(name: "DeviceWatch-Enroll", value: "{\"protocol\": \"LAN\", \"scheme\":\"untracked\", \"hubHardwareId\": \"${hub?.hub?.hardwareID}\"}")
	}
}

Map apiRequestHeaders() {
	return ["Authorization": "Bearer ${state.lifxAccessToken}",
			"Accept": "application/json",
			"Content-Type": "application/json",
			"User-Agent": "SmartThings Integration"
	]
}

// Requests

def logResponse(response) {
	log.info("Status: ${response.status}")
	log.info("Body: ${response.data}")
}

// API Requests
// logObject is because log doesn't work if this method is being called from a Device
def logErrors(options = [errorReturn: null, logObject: log], Closure c) {
	try {
		return c()
	} catch (groovyx.net.http.HttpResponseException e) {
		options.logObject.error("got error: ${e}, body: ${e.getResponse().getData()}")
		if (e.statusCode == 401) { // token is expired
			state.remove("lifxAccessToken")
			options.logObject.warn "Access token is not valid"
		}
		return options.errorReturn
	} catch (java.net.SocketTimeoutException e) {
		options.logObject.warn "Connection timed out, not much we can do here"
		return options.errorReturn
	}
}

def apiGET(path) {
	try {
		httpGet(uri: apiURL(path), headers: apiRequestHeaders()) {response ->
			logResponse(response)
			return response
		}
	} catch (groovyx.net.http.HttpResponseException e) {
		logResponse(e.response)
		return e.response
	}
}

def apiPUT(path, body = [:]) {
	try {
		log.debug("Beginning API PUT: ${path}, ${body}")
		httpPutJson(uri: apiURL(path), body: new groovy.json.JsonBuilder(body).toString(), headers: apiRequestHeaders(), ) {response ->
			logResponse(response)
			return response
		}
	} catch (groovyx.net.http.HttpResponseException e) {
		logResponse(e.response)
		return e.response
	}}

def devicesList(selector = '') {
	logErrors([]) {
		def resp = apiGET("/lights/${selector}")
		if (resp.status == 200) {
			return resp.data
		} else {
			log.error("Non-200 from device list call. ${resp.status} ${resp.data}")
			return []
		}
	}
}

Map locationOptions() {
	def options = [:]
	def devices = devicesList()
	devices.each { device ->
		options[device.location.id] = device.location.name
	}
	log.debug("Locations: ${options}")
	return options
}

def devicesInLocation() {
	return devicesList("location_id:${settings.selectedLocationId}")
}

// ensures the devices list is up to date
def updateDevices() {
	if (!state.devices) {
		state.devices = [:]
	}
	def devices = devicesInLocation()
	def selectors = []

	log.debug("All selectors *yay: ${selectors}")

	devices.each { device ->
		def childDevice = getChildDevice(device.id)
		selectors.add("${device.id}")
		if (!childDevice) {
			// log.info("Adding device ${device.id}: ${device.product}")
			if (device.product.capabilities.has_color) {
				childDevice = addChildDevice(app.namespace, "Custom LIFX Color Bulb", device.id, null, ["label": device.label, "completedSetup": true])
			} else {
				childDevice = addChildDevice(app.namespace, "LIFX White Bulb", device.id, null, ["label": device.label, "completedSetup": true])
			}
		}

		if (device.product.capabilities.has_color) {
			childDevice.sendEvent(name: "color", value: colorUtil.hslToHex((device.color.hue / 3.6) as int, (device.color.saturation * 100) as int))
			childDevice.sendEvent(name: "hue", value: device.color.hue / 3.6)
			childDevice.sendEvent(name: "saturation", value: device.color.saturation * 100)
		}
		childDevice.sendEvent(name: "label", value: device.label)
		childDevice.sendEvent(name: "level", value: Math.round((device.brightness ?: 1) * 100))
		childDevice.sendEvent(name: "switch.setLevel", value: Math.round((device.brightness ?: 1) * 100))
		childDevice.sendEvent(name: "status", value: device.connected ? device.power : "unreachable")//device.power)
		childDevice.sendEvent(name: "colorTemperature", value: device.color.kelvin)
		childDevice.sendEvent(name: "model", value: device.product.name)

		if (state.devices[device.id] == null) {
			// State missing, add it and set it to opposite status as current status to provoke event below
			state.devices[device.id] = [online: !device.connected]
		}
        
        if (!device.connected && breaker) {
        	breaker.off()
            log.debug "breaker: ${breaker.currentValue("switch")}"
            breaker.on()
     	}
      //  log.debug "Devices: ${state.devices} || Device(device.id): ${state.devices[device.id]}"
      //  log.debug "GO: ${childDevice.currentValue('switch')}"

		if (!state.devices[device.id]?.online && device.connected) {
			// Device came online after being offline
			childDevice?.sendEvent(name: "DeviceWatch-DeviceStatus", value: "online", displayed: false)
			log.debug "$device is back Online"
		} else if (state.devices[device.id]?.online && !device.connected) {
			// Device went offline after being online
			childDevice?.sendEvent(name: "DeviceWatch-DeviceStatus", value: "offline", displayed: false)
			log.debug "$device went Offline"
		}
		state.devices[device.id] = [online: device.connected]
	}
	getChildDevices().findAll { !selectors.contains("${it.deviceNetworkId}") }.each {
		log.info("Deleting ${it.deviceNetworkId}")
		state.devices[it.deviceNetworkId] = null
		deleteChildDevice(it.deviceNetworkId)
	}
}