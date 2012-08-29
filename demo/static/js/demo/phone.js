// phone dial functionality
(function() {
	var dial;

	dial = function(digit) {
		demo.storesync.send("dialdigit", {
			digit : digit
		});
	}

	storesync.namespace("demo").phone = {
		onreset : function() {
			demo.phone.display("");
		},

		onupdate : function(keyParts, element) {
			demo.phone.display(element.display);
		},

		onremove : function(keyParts) {
			demo.phone.display("");
		},

		display : ko.observable(""),

		b0 : function() {
			dial(0);
		},
		b1 : function() {
			dial(1);
		},
		b2 : function() {
			dial(2);
		},
		b3 : function() {
			dial(3);
		},
		b4 : function() {
			dial(4);
		},
		b5 : function() {
			dial(5);
		},
		b6 : function() {
			dial(6);
		},
		b7 : function() {
			dial(7);
		},
		b8 : function() {
			dial(8);
		},
		b9 : function() {
			dial(9);
		},
		bc : function() {
			demo.storesync.send("dialclear", {});
		},
		bdial : function() {
			demo.storesync.send("dial", {});
		}
	};

	demo.storesync.addElementListenersByType("de.schouten.demo.guibeans.PhoneNumberDisplay", demo.phone);
})();
