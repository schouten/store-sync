// switches tabs
(function() {
	var switchTab, factory, maintab;
	
	switchTab = function(tab) {
		demo.storesync.send("switchtab", {
			tab : tab
		});
	}

	factory = storesync.util.newViewmodelFactory(demo.storesync, "de.schouten.demo.guibeans.MainTab");
	factory.addObservable("tab", "phone");
	maintab = storesync.namespace("demo").maintab = factory.create();

	maintab.displayPhoneTab = ko.computed(function() {
		return maintab.tab() === "phone" ? "" : "none";
	});

	maintab.displayPhoneTabColor = ko.computed(function() {
		return maintab.tab() === "phone" ? "white" : "";
	});

	maintab.displayClockTab = ko.computed(function() {
		return maintab.tab() === "clock" ? "" : "none";
	});

	maintab.displayClockTabColor = ko.computed(function() {
		return maintab.tab() === "clock" ? "white" : "";
	});

	maintab.displayMessageTab = ko.computed(function() {
		return maintab.tab() === "message" ? "" : "none";
	});

	maintab.displayMessageTabColor = ko.computed(function() {
		return maintab.tab() === "message" ? "white" : "";
	});

	maintab.switchToPhone = function() {
		switchTab("phone");
	};

	maintab.switchToClock = function() {
		switchTab("clock");
	};

	maintab.switchToMessage = function() {
		switchTab("message");
	};
})();
