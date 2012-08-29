// phone dial functionality
(function() {
	var dial, phone, factory;

	dial = function(digit) {
		demo.storesync.send("dialdigit", {
			digit : digit
		});
	}

	factory = storesync.util.newViewmodelFactory(demo.storesync, "de.schouten.demo.guibeans.PhoneNumberDisplay");
	factory.addObservable("display");
	phone = storesync.namespace("demo").phone = factory.create();

	phone.b0 = function() {
		dial(0);
	};
	phone.b1 = function() {
		dial(1);
	};
	phone.b2 = function() {
		dial(2);
	};
	phone.b3 = function() {
		dial(3);
	};
	phone.b4 = function() {
		dial(4);
	};
	phone.b5 = function() {
		dial(5);
	};
	phone.b6 = function() {
		dial(6);
	};
	phone.b7 = function() {
		dial(7);
	};
	phone.b8 = function() {
		dial(8);
	};
	phone.b9 = function() {
		dial(9);
	};
	phone.bc = function() {
		demo.storesync.send("dialclear", {});
	};
	phone.bdial = function() {
		demo.storesync.send("dial", {});
	};

})();
