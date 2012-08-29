// handles logout requests
(function() {
	
	storesync.namespace("demo").logout = function() {
		demo.storesync.send("logout", {});
	}

})();
