// displays current server time
(function() {
	
	var factory = storesync.util.newViewmodelFactory(demo.storesync, "de.schouten.demo.guibeans.Time");
	factory.addObservable("value");
	storesync.namespace("demo").time = factory.create();

})();
