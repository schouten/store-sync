// all dom is loaded, now init the frameworks!
(function() {
	var userName;

	userName = location.search;
	if (userName.length >= 1) {
		userName = userName.substr(1);
	}
	
	// kick of knockout framework
	ko.applyBindings(demo);

	// kick of the synchronization and send the login action
	demo.storesync.start("login", {
		user : userName
	});
})();
