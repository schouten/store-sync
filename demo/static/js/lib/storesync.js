// Synchronizes the state stored on the server side in a store to the client.
// The state can be updated asynchronously by external events on the server or
// by sending actions from the client to the server.
(function() {
	var socketBaseUrl;

	// store the module in the namespace "storesync".
	storesync = {};

	socketBaseUrl = (function() {
		var socketBaseUrl;
		if (location.protocol === "https") {
			socketBaseUrl = "wss://";
		} else {
			socketBaseUrl = "ws://";
		}
		socketBaseUrl += location.host;
		return socketBaseUrl;
	})();

	// public method to create a new storesync object
	storesync.newStoresync = function() {
		var path, // path to the web socket url
		readonly = false, // set to true to send only the init action
		storesync = {}, // the object for api users
		elementListenersByType = {}, // all element listeners registered for element changes by element type
		allListeners = [], // contains all listeners to report reset
		protocol = "storesync", // the protocol for the websocket
		retryPause = 1000, // pause 1 second before reconnect
		consoleLogging = false, // switch console logging on and off
		webSocket, // the web socket object from the browser
		initAction, // the first (repeatable) action send to the server
		restart, // restart method
		send, // send method
		changeState, // method to report about state changes
		reportError, // method to report about server errors
		reportReset, // method to report about reset
		reportUpdate, // method to report about store updates
		reportRemove; // method to report about store removes

		// (re) starts a web socket and sends the initAction 
		restart = function() {
			changeState("initializing");
			webSocket = new WebSocket(socketBaseUrl + path, protocol);
			webSocket.onopen = function() {
				send(initAction.name, initAction.data);
			}
			webSocket.onmessage = function(evt) {
				var msg = evt.data, message = JSON.parse(evt.data);
				if (consoleLogging) {
					console.log("received " + msg);
				}
				switch (message.type) {
				case "update":
					reportUpdate(message);
					break;
				case "remove":
					reportRemove(message);
					break;
				case "status":
					changeState(message.clientStatus, message.statusText);
					break;
				case "error":
					reportError(message);
					break;
				default:
					console.log("Unknown message type: " + message.type);
				}
			}
			webSocket.onclose = function(evt) {
				if (consoleLogging) {
					console.log("closed " + evt.code);
				}
				if (evt.code === 4000) { // logout
					changeState("closed");
				} else { // try to start again
					changeState("initializing");
					setTimeout(function() {
						restart();
					}, retryPause);
				}
			}
		}

		// send the action with name and data to the web socket.
		send = function(actionName, actionData) {
			var msg = actionName + " " + JSON.stringify(actionData);
			if (consoleLogging) {
				console.log("send " + msg);
			}
			webSocket.send(msg);
		}

		// changes the state of the storesync.
		changeState = function(newState, newText) {
			if (storesync.onstatechange) {
				var state = {
					state : newState
				};
				if (newText) {
					state.text = newText;
				} else {
					state.text = "";
				}
				storesync.onstatechange(state);
			}
			if (newState === "initializing") {
				reportReset();
			}
		}

		// report an server side error
		reportError = function(message) {
			if (storesync.onerror) {
				storesync.onerror(message.message, message.trace);
			}
		}

		// report about an update of an store element
		reportUpdate = function(message) {
			var listeners, i;
			if (storesync.onupdate) {
				storesync.onupdate(message.domainKeyParts, message.elementType, message.elementKeyParts, message.element);
			}
			listeners = elementListenersByType[message.elementType];
			if (listeners) {
				for (i = 0; i < listeners.length; i++) {
					listeners[i].onupdate(message.elementKeyParts, message.element);
				}
			}
		}

		// report about an remove of an store element
		reportRemove = function(message) {
			var listeners, i;
			if (storesync.onremove) {
				storesync.onremove(message.domainKeyParts, message.elementType, message.elementKeyParts);
			}
			listeners = elementListenersByType[message.elementType];
			if (listeners) {
				for (i = 0; i < listeners.length; i++) {
					listeners[i].onremove(message.elementKeyParts);
				}
			}
		}

		// report about reset for all listeners
		reportReset = function() {
			var i;
			for (i = 0; i < allListeners.length; i++) {
				allListeners[i].onreset();
			}
		}

		// sets the socket path.
		storesync.withBasePath = function(p) {
			path = p;
			return storesync;
		}

		// sets the readonly property
		storesync.withReadonly = function(ro) {
			readonly = ro;
			return storesync;
		}

		// sets the socket protocol.
		storesync.withProtocol = function(proto) {
			protocol = proto;
			return storesync;
		}

		// sets the retry pause.
		storesync.withRetryPause = function(pause) {
			retryPause = pause;
			return storesync;
		}

		// sets the console logging.
		storesync.withConsoleLogging = function(logging) {
			consoleLogging = logging;
			return storesync;
		}

		// sets the init action and starts the web socket.
		storesync.start = function(actionName, actionData) {
			initAction = {
				name : actionName,
				data : actionData
			};
			if (webSocket) { // we already have a websocket - just send the init action
				send(actionName, actionData);
			} else {
				restart();
			}
			return storesync;
		}

		// send the given action with its data.
		storesync.send = function(actionName, actionData) {
			if (!readonly) {
				send(actionName, actionData);
			}
		}

		storesync.addElementListenersByType = function(type, listener) {
			if (!elementListenersByType[type]) {
				elementListenersByType[type] = [];
			}
			elementListenersByType[type].push(listener);
			allListeners.push(listener);
		}

		storesync.toString = function() {
			return "storesync " + path + " " + protocol + " " + retryPause;
		}

		return storesync;
	}

	// creates a new namespace
	storesync.namespace = function() {
		var i, length = arguments.length, current = window;
		for (i = 0; i < length; i++) {
			if (!window[arguments[i]]) {
				window[arguments[i]] = {};
			}
			current = window[arguments[i]];
		}
		return current;
	}

})();
