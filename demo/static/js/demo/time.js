// displays current server time
(function() {

	storesync.namespace("demo").time = {
		onreset : function() {
			demo.time.value("");
		},

		onupdate : function(keyParts, element) {
			demo.time.value(element.value);
		},

		onremove : function(keyParts) {
			demo.time.value("");
		},

		value : ko.observable("")
	};

	demo.storesync.addElementListenersByType("de.schouten.demo.guibeans.Time", demo.time);
})();
