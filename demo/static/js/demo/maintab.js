// switches tabs
(function() {
	var currenttab, switchTab;

	currenttab = ko.observable("phone");

	switchTab = function(tab) {
		demo.storesync.send("switchtab", {
			tab : tab
		});
	}

	storesync.namespace("demo").maintab = {
		onreset : function() {
			currenttab("phone");
		},

		onupdate : function(keyParts, element) {
			currenttab(element.tab);
		},

		onremove : function(keyParts) {
			currenttab("phone");
		},

		displayPhoneTab : ko.computed(function() {
			return currenttab() === "phone" ? "" : "none";
		}),

		displayPhoneTabColor : ko.computed(function() {
			return currenttab() === "phone" ? "white" : "";
		}),

		displayClockTab : ko.computed(function() {
			return currenttab() === "clock" ? "" : "none";
		}),

		displayClockTabColor : ko.computed(function() {
			return currenttab() === "clock" ? "white" : "";
		}),

		displayMessageTab : ko.computed(function() {
			return currenttab() === "message" ? "" : "none";
		}),

		displayMessageTabColor : ko.computed(function() {
			return currenttab() === "message" ? "white" : "";
		}),

		switchToPhone : function() {
			switchTab("phone");
		},

		switchToClock : function() {
			switchTab("clock");
		},

		switchToMessage : function() {
			switchTab("message");
		}
	};

	demo.storesync.addElementListenersByType("de.schouten.demo.guibeans.MainTab", demo.maintab);
})();
