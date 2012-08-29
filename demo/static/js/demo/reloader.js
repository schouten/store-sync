// reloads the browser on message
(function() {

	storesync.namespace("demo").reloader = {
		onreset : function() {
			// do nothing
		},

		onupdate : function(keyParts, element) {
			location.reload();
		},

		onremove : function(keyParts) {
			// do nothing
		}

	};

	demo.storesync.addElementListenersByType("de.schouten.demo.guibeans.Reload", demo.reloader);
})();
