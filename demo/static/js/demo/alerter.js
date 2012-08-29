// pops up alert box on message
(function() {
	
	storesync.namespace("demo").alerter = {
		onreset : function() {
			// do nothing
		},

		onupdate : function(keyParts, element) {
			alert(element.alertMessage);
		},

		onremove : function(keyParts) {
			// do nothing
		}

	};

	demo.storesync.addElementListenersByType("de.schouten.demo.guibeans.Alerter", demo.alerter);

})();
