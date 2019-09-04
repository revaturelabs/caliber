/*******************************************************************************
 * Team: SDAR Team Lead: Roderick Dye Authors: Roderick Dye, Stanley Chouloute,
 * Daniel Zorrilla, Adam Baker
 * 
 * Daniel worked on viewing all trainers, Stanley worked on adding trainers,
 * Roderick worked on editing trainers, Adam worked on delete functionality for
 * trainers.
 * 
 * Commented-out code related to trainer tasks refers to incomplete issue #791
 * ******************************************************************************
 */

angular
		.module("vp")
		.controller(
				"vpTrainerController",
				function($scope, $cookies, $log, caliberDelegate) {
					// $scope.allActiveTasks;
					var currentUserId = $cookies.get("id");
					var currentUser;
					$log.debug("Booted trainer manage controller.");
					$log.debug('test trainermanager controller -j');
					/**
					 * ************************** Batch
					 * ****************************
					 */

					/** On page start --> load all trainers * */
					$scope.loadAllTrainers = function() {
						caliberDelegate.all
								.getAllTrainers()
								.then(
										function(trainers) {
											$log.debug(trainers);
											$scope.allTrainers = trainers;
											for (var i = 0; i < trainers.length; i++) {
												if (currentUserId == trainers[i].trainerId) {
													currentUser = trainers[i];
												}
											}
										});
					};

					var submitTier = function(tier) {
						var pre = "ROLE_"
						return pre.concat(tier);
					};

					/**
					 * *********************************************** Code to
					 * create and update Trainer************
					 */

					// load training tiers
					caliberDelegate.all.enumTrainerTier().then(function(tiers) {
						$log.debug(tiers);
						var filteredTiers = tiers.filter(function(ary) {
							return ary !== 'ROLE_INACTIVE'
						});
						for (var i = 0; i < filteredTiers.length; i++) {
							filteredTiers[i] = filteredTiers[i].substr(5);
						}
						$scope.trainerTiers = filteredTiers;
					});

					// load trainers' titles
					caliberDelegate.vp.trainersTitles().then(function(titles) {
						$log.debug(titles);
						$scope.trainersTitles = titles;
					});

					/** Save email verification modal* */
					$scope.checkTrainerEmail = function(trainerForm) {
						caliberDelegate.vp
								.getTrainerEmail(trainerForm.email)
								.then(
										function(response) {
											$log.debug(response)
											if (response.data === "") {
												$scope.saveTrainer(trainerForm);
											} else {
												$log.debug(response)
												angular
														.element(
																"#trainerEmailVerificationModal")
														.modal("show");
												return false;
											}
										})
					};

					/** Save New Trainer Input * */
					$scope.saveTrainer = function(trainerForm) {
						var newTrainer = trainerForm;
						createTrainerObject(newTrainer);
						newTrainer.tier = submitTier(newTrainer.tier);
						caliberDelegate.vp.createTrainer(newTrainer).then(
								function(response) {
									$log.debug("trainer added: " + response);
									$scope.loadAllTrainers();
								});
						angular.element("#createTrainerModal").modal("hide");
					};

					/** Create new Trainer Object * */
					function createTrainerObject(trainer) {
						trainer = $scope.trainerForm;
						$log.debug(trainer);
					}
					;

					/** Create scopes for trainer form* */
					$scope.trainerForm = {
						trainerId : null,
						name : null,
						email : null,
						title : null,
						tier : null
					};

					/** Resets trainer form* */
					$scope.resetTrainerForm = function() {
						$scope.trainerForm.trainerId = "";
						$scope.trainerForm.name = "";
						$scope.trainerForm.email = "";
						$scope.trainerForm.title = "";
						$scope.trainerForm.tier = "";
						$scope.Save = "Save";
					};

					/** Fill update form with trainer's previous data */
					$scope.populateTrainer = function(index) {
						$log.debug($scope.allTrainers[index]);
						$scope.trainerForm.trainerId = $scope.allTrainers[index].trainerId;
						$scope.trainerForm.name = $scope.allTrainers[index].name;
						$scope.trainerForm.email = $scope.allTrainers[index].email;
						$scope.trainerForm.title = $scope.allTrainers[index].title;
						$scope.trainerForm.tier = $scope.allTrainers[index].tier
								.substr(5);
						$scope.Save = "Update";

					};

					/** Update Trainer Input * */
					$scope.updateTrainer = function() {
						$scope.trainerForm.tier = submitTier($scope.trainerForm.tier);
						caliberDelegate.vp.updateTrainer($scope.trainerForm)
								.then(function(response) {
									$log.debug("trainer updated: " + response);
									$scope.loadAllTrainers();
								});
						angular.element("#editTrainerModal").modal("hide");
					};

					/**
					 * Adam Baker deactivation function
					 */
					$scope.makeInactive = function() {
						$log.debug($scope.trainerForm);
						$scope.trainerForm.tier = "ROLE_INACTIVE";
						caliberDelegate.vp
								.deactivateTrainer($scope.trainerForm).then(
										function() {
											$log.debug("trainer deactivated");
											$scope.loadAllTrainers();
										});
						angular.element("#deleteTrainerModal").modal("hide");
					};
				});
