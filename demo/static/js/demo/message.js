// displays messages
(function() {

	storesync.namespace("demo").message = {
		onreset : function() {
			demo.message.messages.removeAll();
		},

		onupdate : function(keyParts, element) {
			demo.message.messages.push({
				time : element.time,
				dateString : element.dateString,
				message : element.message,
				urgent : element.urgent
			});
		},

		onremove : function(keyParts) {
			var i, time = keyParts[0] * 1;
			for (i = 0; i < demo.message.messages().length; i++) {
				if (demo.message.messages()[i].time === time) {
					demo.message.messages.splice(i, 1);
					return;
				}
			}
		},

		messages : ko.observableArray()
	};

	demo.storesync.addElementListenersByType("de.schouten.demo.guibeans.Message", demo.message);
})();
