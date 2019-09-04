/**
 * Patrick Walsh found this file and marks it for extermination.
 * In case these are duct-taping components together, we must test
 * the removal of each function before removing this file. :(
 */
angular.module("app")
		.controller(
				"testAPIController",
				function($scope, $log, caliberDelegate) {
					$log.debug("Launched REST API");

					$scope.all = {};
					$scope.vp = {};
					$scope.qc = {};
					$scope.trainer = {};

					/** ********************** Test All ********************** */
					$scope.all.allTrainers = caliberDelegate.all
							.getAllTrainers();

					/** *********************** Trainer ************************ */
					// all batches
					$scope.trainer.allBatches = caliberDelegate.trainer
							.getAllBatches();

					/**
					 * ************************** VP
					 * ****************************
					 */
					// all batches
					$scope.vp.allBatches = caliberDelegate.vp.getAllBatches();

					// get all current batches
					$scope.vp.allCurrentBatches = caliberDelegate.vp
							.getAllCurrentBatches();

					/** ************************ Test QC ************************ */
					// get all batches
					$scope.qc.allBatches = caliberDelegate.qc.getAllBatches();
				});