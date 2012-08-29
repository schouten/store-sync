// sends a message
(function() {
	var clear, send, update, sendUpdate = true;

	clear = function() {
		sendUpdate = false;
		demo.sendmessage.message("")
		demo.sendmessage.urgent(false);
		sendUpdate = true;
	}

	send = function(submitType) {
		if (sendUpdate) {
			demo.storesync.send("sendmessage", {
				message : demo.sendmessage.message(),
				urgent : demo.sendmessage.urgent() === true,
				submitType : submitType
			});
		} 
	}

	update = function() {
		send("update");
	}

	reset = function() {
		send("reset");
	}

	submit = function() {
		send("submit");
	}

	storesync.namespace("demo").sendmessage = {
		onreset : function() {
			clear();
		},

		onupdate : function(keyParts, element) {
			sendUpdate = false;
			demo.sendmessage.message(element.message);
			demo.sendmessage.urgent(element.urgent);
			sendUpdate = true;
		},

		onremove : function(keyParts) {
			clear();
		},

		message : ko.observable(),

		urgent : ko.observable(),

		submit : function() {
			submit();
		},
		
		reset : function() {
			reset();
		}

	};

	demo.sendmessage.message.subscribe(update);
	demo.sendmessage.urgent.subscribe(update);

	demo.storesync.addElementListenersByType("de.schouten.demo.web.actions.SendMessageForm", demo.sendmessage);
})();
