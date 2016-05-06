/**
 *  x10 interface
 *
 *  Copyright 2014 john west
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
 
 preferences 
{
    input("housecode", "text", title: "House Code", description: "A-P")
    input("devicecode", "text", title: "Device Code", description: "1-15")
}

metadata {
	definition (name: "X10 Interface", namespace: "johnwest80", author: "john west") {
		capability "switch"

        command "processX10Command"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
standardTile("switch", "device.switch", width: 1, height: 1, canChangeIcon: true, canChangeBackground: true) {
			state "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#707000"
			state "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#007000"  
		}
        main "switch"
		details(["switch"])

	}
}

// parse events into attributes
def parse(String description) {
	def value = zigbee.parse(description)?.text
    
    value = value + "/" + new Date().time //makes sure the event gets sent every time, even if it's a duplicate

	log.debug "x10 interface parse value = $value"

    log.debug "sendevent commandFromX10 off"
    
	def result = createEvent(name: "commandFromX10", value: "$value")
    log.debug "Event $result.name created with value $result.value"
	return result
}

// handle commands
def on() {
	log.debug "Executing 'on' $settings.housecode$settings.devicecode"
	zigbee.smartShield(text: "X10-$settings.housecode-$settings.devicecode-On").format()
}

def off() {
	log.debug "Executing 'off' $settings.housecode$settings.devicecode"
	zigbee.smartShield(text: "X10-$settings.housecode-$settings.devicecode-Off").format()
}

def processX10Command(String device, String command) {
	log.debug "Processing command $command for $device"
	zigbee.smartShield(text: "X10-$device-$command").format()
}
