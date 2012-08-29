// displays current server time
(function() {

	storesync.namespace("demo").svgclock = {
		onreset : function() {
			demo.svgclock.transformSec("");
			demo.svgclock.transformMin("");
			demo.svgclock.transformHour("");
		},

		onupdate : function(keyParts, element) {
			demo.svgclock.transformSec("rotate(" + (360 / 60 * element.seconds) + ",61,61)");
			demo.svgclock.transformMin("rotate(" + (360 / 60 * element.minutes) + ",61,61)");
			demo.svgclock.transformHour("rotate(" + (360 / 12 * (element.hours + element.minutes / 60)) + ",61,61)");
		},

		onremove : function(keyParts) {
			demo.svgclock.transformSec("");
			demo.svgclock.transformMin("");
			demo.svgclock.transformHour("");
		},

		transformSec : ko.observable(""),
		transformMin : ko.observable(""),
		transformHour : ko.observable("")
	};

	demo.storesync.addElementListenersByType("de.schouten.demo.guibeans.Time", demo.svgclock);
})();
