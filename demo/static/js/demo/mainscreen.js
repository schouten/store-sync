// switches between loading and mainscreen
(function() {
	var lastState;

	lastState = ko.observable("initializing");

	storesync.namespace("demo").mainscreen = {

		onstatechange : function(state) {
			lastState(state.state);

			if (state.state === "uninitialized") {
				var userName = prompt(state.text, "");
				demo.storesync.start("login", {
					user : userName
				});
			}
		},

		loading : ko.computed(function() {
			if (lastState() === "initializing" || lastState() === "uninitialized") {
				return "";
			} else {
				return "none";
			}
		}),

		main : ko.computed(function() {
			if (lastState() === "initialized") {
				return "";
			} else {
				return "none";
			}
		}),

		closed : ko.computed(function() {
			if (lastState() === "closed") {
				return "";
			} else {
				return "none";
			}
		})
	};

	// get informed about client state changes
	demo.storesync.onstatechange = demo.mainscreen.onstatechange;

})();
