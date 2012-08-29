// Support methods for knockoutjs library and other utilities.
// Use in combination of with library from http://www.knockoutjs.com
// Include this after storesync.js
(function() {

	if (!window.storesync.util) {
		window.storesync.util = {};
	}

	// A facotry to create new vievmodels with.
	// storesync the object register the new viewbean for.
	// javaBeanName is the name of the bean in the store to sync with.
	window.storesync.util.newViewmodelFactory = function(storesync, javaBeanName) {
		var factory = {}, // the factory to create the model with.
		defaultValues = {}, // all default values for the model attributes.
		names = [], // all attribute names. 
		viewmodel, // the viewmodel itself.
		updateMethod, // the callback methods for onupdate.
		resetMethod; // the callback methods for onreset and onremove.

		updateMethod = function(keyParts, element) {
			var i;
			// iterate over all names and set the values from the element.
			for (i = 0; i < names.length; i++) {
				viewmodel[names[i]](element[names[i]]);
			}
		}

		resetMethod = function() {
			var i;
			// iterate over all names and set them back to the default values.
			for (i = 0; i < names.length; i++) {
				viewmodel[names[i]](defaultValues[names[i]]);
			}
		}

		// add the callback methods to the viewmodel
		viewmodel = {
			onreset : resetMethod,
			onremove : resetMethod,
			onupdate : updateMethod,
		}

		storesync.addElementListenersByType(javaBeanName, viewmodel);

		// adds a new observable with default a value to the viewmodel.
		factory.addObservable = function(name, defaultValue) {
			names.push(name);
			if (!defaultValue) {
				defaultValue = "";
			}
			defaultValues[name] = defaultValue;
			viewmodel[name] = ko.observable(defaultValue);
			return factory;
		}

		// returns the created viewmodel.
		factory.create = function() {
			return viewmodel;
		}

		return factory;
	}

})();
