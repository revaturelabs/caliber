/**
 * API for making vp related AJAX calls to the backend
 * 
 * @param $log
 * @param $http
 * @returns {{}}
 */
angular
		.module("api")
		.factory(
				"vpFactory",
				function($log, $http) {
					$log.debug("Booted VP API Factory");
					var vp = {};

					// Get all batches
					vp.getAllBatches = function() {
						return $http({
							url : "/vp/batch/all/",
							method : "GET"
						})
								.then(
										function(response) {
											$log
													.debug("Batches successfully retrieved.");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
										});
					};
					// Get all current batches
					vp.getAllCurrentBatches = function() {
						return $http({
							url : "/vp/batch/all/current",
							method : "GET"
						})
								.then(
										function(response) {
											$log
													.debug("Batches successfully retrieved");
											$log.debug(response);
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
										});
					};
				
					// Get all Categories
					vp.getAllCategories = function() {
						return $http({
							url : "/vp/category",
							method : "GET"
						})
								.then(
										function(response) {
											$log
													.debug("Categories successfully retrived");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log
													.error("There was an error in vpFactory -> getAllCategories"
															+ response.status);
										});
					};
					// Update a category
					vp.updateCategory = function(category) {
						return $http({
							url : "/vp/category/update",
							method : "PUT",
							data : category
						}).then(
								function(response) {
									$log.debug(category + " Has been Updated");
									$log.debug(response);
								},
								function(response) {
									$log.error(
											"There was an error in vpFactory -> updateCategory "
													+ response, status);
								});
					};
					// Save new category
					vp.saveCategory = function(category) {
						return $http({
							url : "/vp/category",
							method : "POST",
							data : category
						}).then(
								function(response) {
									$log.debug(category + " Has been Created");
									$log.debug(response);
								},
								function(response) {
									$log.error(
											"There was an error in vpFactory -> saveCategory "
													+ response, status);
								});

					};

					// Save new location
					vp.saveLocation = function(location) {
						return $http({
							url : "/vp/location/create",
							method : "POST",
							data : location
						}).then(
								function(response) {
									$log.debug(location + " Has been saved");
									$log.debug(response);
								},
								function(response) {
									$log.error(
											"There was an error in vpFactory -> saveLocation "
													+ response, status);
								});

					};

					/*
					// Save new task
					vp.saveOrUpdateTask = function(task) {
						return $http({
							url : "/vp/task",
							method : "POST",
							data : task
						}).then(
								function(response) {
									$log.debug(task + " Has been Created");
									$log.debug(response);
								},
								function(response) {
									$log.error(
											"There was an error in vpFactory -> saveTask "
													+ response, status);
								});

					};
					
					// Save new task
					vp.saveTaskCompletion = function(taskCompletion) {
						return $http({
							url : "/vp/task/completed",
							method : "POST",
							data : taskCompletion
						}).then(
								function(response) {
									$log.debug(taskCompletion + " has been saved");
									$log.debug(response);
								},
								function(response) {
									$log.error(
											"There was an error in vpFactory -> saveTaskCompletion "
													+ response, status);
								});

					};*/
					

					// deactivate trainer needed to force content type to be
					// JSON else 415
					vp.deactivateTrainer = function(trainerObj) {
						return $http({
							url : "/vp/trainer/delete",
							method : "DELETE",
							data : trainerObj,
							headers : {
								"Content-Type" : "application/json"
							}
						}).then(
								function(response) {
									$log.debug("Trainer deleted");
									$log.debug(response);
								},
								function(response) {
									$log.error("There was an error: "
											+ response.status);
								});
					};

					/** ********************LOCATIONS************************ */

					// Deactivate location
					vp.deactivateLocation = function(locationObj) {
						return $http({
							url : "/vp/location/delete",
							method : "DELETE",
							data : locationObj,
							headers : {
								"Content-Type" : "application/json"
							}
						}).then(
								function(response) {
									$log.debug("Location deleted");
									$log.debug(response);
								},
								function(response) {
									$log.error("There was an error: "
											+ response.status);
								});
					};

					// Update selected location
					vp.updateLocation = function(locationObj) {
						return $http({
							url : "/vp/location/update",
							method : "PUT",
							data : locationObj,
							headers : {
								"Content-Type" : "application/json"
							}
						})
								.then(
										function(response) {
											$log
													.debug("Location successfully updated.");
											$log.debug(response);
										},
										function(response) {
											$log.error("There was an error updating the location: "
													+ response.status);
											return false;
										});
					};
					
					// Update selected location active status
					vp.reactivateLocation = function(locationObj) {
						return $http({
							url : "/vp/location/reactivate",
							method : "PUT",
							data : locationObj,
							headers : {
								"Content-Type" : "application/json"
							}
						})
								.then(
										function(response) {
											$log
													.debug("Location successfully updated.");
											$log.debug(response);
										},
										function(response) {
											$log.error("There was an error updating the location: "
													+ response.status);
											return false;
										});
					};

					// Create location
					vp.createLocation = function(locationObj) {
						$log.debug(locationObj);
						return $http({
							url : "/vp/location/create",
							method : "POST",
							data : locationObj
						})
								.then(
										function(response) {
											$log

													.debug("Location successfully created.");

											$log.debug(response);
											return response;
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
											return response.data;
										});
					};

					// Get all locations
					vp.getAllLocations = function() {
						return $http({
							url : "/all/location/all/",
							method : "GET"
						})
								.then(
										function(response) {
											$log
													.debug("Locations successfully retrieved");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
										});
					};
					
					/*******TRAINERS************/

					// Update selected trainer
					vp.updateTrainer = function(trainerObj) {
						return $http({
							url : "/vp/trainer/update",
							method : "PUT",
							data : trainerObj
						})
								.then(
										function(response) {
											$log
													.debug("Trainer successfully updated.");
											$log.debug(response);
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
											return false;
										});
					};

					/**
					 * returns all titles
					 * 
					 * @returns {*}
					 */
					vp.getAllTrainersTitle = function() {
						return $http({
							url : "/vp/trainer/titles/",
							method : "GET"
						})
								.then(
										function(response) {
											$log
													.debug("Trainers Titles successfully retrieved");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
										});
					};

					/**
					 * 
					 * @param trainerObj
					 * @returns {*}
					 */
					vp.createTrainer = function(trainerObj) {
						$log.debug(trainerObj);
						return $http({
							url : "/vp/trainer/create",
							method : "POST",
							data : trainerObj
						}).then(
								function(response) {
									$log.debug("Trainer successfully created.");
									$log.debug(response);
									return response;
								},
								function(response) {
									$log.error("There was an error: "
											+ response.status);
									return response.data;
								});
					};

					vp.getTrainerEmail = function(trainerEmail) {
						return $http(
								{
									url : "/training/trainer/byemail/"
											+ trainerEmail + "/",
									method : "GET",
								}).then(
								function(response) {
									$log.log(trainerEmail);
									$log.log(response);
									return response;
								},
								function(response) {
									$log.log(trainerEmail);
									$log.error("There was an error: "
											+ response.status);
									return response;
								});
					};
					
					return vp;
				});
