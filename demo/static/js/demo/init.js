// all dom is loaded, now init the frameworks!
(function() {
	var storesyncInstance, readonly;

	readonly = location.hash === "#readonly";

	storesyncInstance = window.storesync.newStoresync().withBasePath("/socket/storesync").withConsoleLogging(false).withReadonly(readonly);

	// give access to the instance
	storesync.namespace("demo").storesync = storesyncInstance;

	// get informed about server side errors
	storesyncInstance.onerror = function(message, trace) {
		alert(message + "\n\n" + trace);
	}
})();
