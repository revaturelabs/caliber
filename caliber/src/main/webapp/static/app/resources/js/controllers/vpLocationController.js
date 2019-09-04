angular
		.module("vp")
		.controller(
				"vpLocationController",
				function($scope, $log, caliberDelegate) {
					
					$log.debug("Booted location manage controller.");
					$log.debug('test locationmanager cntroller -j');
					$scope.allLocations = [];
					$scope.selectedLocation = {};

					/** On page start --> load all locations * */

					(function(){
						caliberDelegate.vp.getAllLocations().then(
								function(locations) {
									$log.debug(locations);
									$scope.allLocations = locations;
								});
					})();

					var loadAllLocations = function(){
						caliberDelegate.vp.getAllLocations().then(
								function(locations) {
									$log.debug(locations);
									$scope.allLocations = locations;
								});
					}

					// creating scope for location form
					$scope.locationForm = {
						addressId : null,
						company : null,
						street : null,
						city : null,
						state : null,
						zipcode : null,
						active : 1
					};

					// create new Address object
					function createAddressObject(location) {
						location = $scope.locationForm;
						$log.debug(location);
					}
					// Create Location
					$scope.createLocation = function(locationForm) {
						var newLocation = locationForm;
						createAddressObject(newLocation);
						caliberDelegate.vp.createLocation(newLocation)
								.then(
										function(response) {
											loadAllLocations();
											$log.debug("Location Created: "
													+ response);
										});
					};
					
					/** Fill update form with locations data */
					$scope.populateLocation = function(index) {
						$log.debug($scope.allLocations[index]);
						$scope.locationForm.addressId = $scope.allLocations[index].addressId;
						$scope.locationForm.company = $scope.allLocations[index].company;
						$scope.locationForm.street = $scope.allLocations[index].street;
						$scope.locationForm.city = $scope.allLocations[index].city;
						$scope.locationForm.state = $scope.allLocations[index].state;
						$scope.locationForm.zipcode = $scope.allLocations[index].zipcode;
						$scope.locationForm.active = 1;
						$scope.Save = "Update";
					};

					// to update location
					$scope.updateLocation = function() {
						var currentLocation = $scope.locationForm;
						caliberDelegate.vp.updateLocation(currentLocation)
								.then(
										function(response) {
											loadAllLocations();
											$log.debug("Location Updated: "
													+ response);
										});
					};
					
					// ** Resets location form* *//*
					$scope.resetLocationForm = function() {
						$scope.locationForm.addressId = ""; 
						$scope.locationForm.company = "";
						$scope.locationForm.street = "";
						$scope.locationForm.city = "";
						$scope.locationForm.state = "";
						$scope.locationForm.zipcode = "";
						$scope.locationForm.active = 1;
						$scope.Save = "Save";
					};
					
	
					// removing location - deactivate
					$scope.removeLocation = function() {
						$scope.selectedLocation.active = 0;
						caliberDelegate.vp.deactivateLocation(
								$scope.selectedLocation).then(
								function(response) {
									loadAllLocations();
									$log.debug("Location removed:" + response);
								})
						angular.element("#deleteLocationModal").modal("hide");
					}

					// get location from input
					$scope.updateSelectedLocation = function(index) {
						$scope.selectedLocation = $scope.allLocations[index];
					}

					// add location - reactivate
					$scope.reactivateLocation = function() {
						$scope.selectedLocation.active = 1;
						caliberDelegate.vp.reactivateLocation(
								$scope.selectedLocation).then(
								function(response) {
									loadAllLocations();
									$log.debug("Location reactivate:" + response);
								})
						angular.element("#addLocationModal").modal("hide");
					}
				});