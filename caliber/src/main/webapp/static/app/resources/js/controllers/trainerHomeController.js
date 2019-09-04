angular
		.module("trainer")
		.controller(
				"trainerHomeController",
				function($rootScope, $scope, $state, $log, caliberDelegate,
						chartsDelegate, allBatches) {
					$log.debug("Booted trainer home controller.");
					
					const
					OVERALL = "(All)";
					
					// What you see when you open Reports
					var startingDate = new Date();
					startingDate.setFullYear(startingDate.getFullYear() - 1);
					$scope.startDate = startingDate;
					$scope.selectedTrainingType = OVERALL;
					$scope.selectedSkill = OVERALL;

					/**
					 * ********************************* On Start
					 * ****************************
					 */
					$scope.currentBatch = allBatches[0];
					if($scope.currentBatch === undefined || $scope.currentBatch === null) 
						return;

					(function() {
						// Finishes any left over AJAX animation
						NProgress.done();

						createDefaultCharts();

					})();

					function createDefaultCharts() {
					    NProgress.start();
						createAverageTraineeScoresOverall();
						createTechnicalSkillsBatchOverall();
						createCurrentQCStatus();
					}

					/**
					 * ********************************************* UI
					 * **************************************************
					 */

					// *******************************************************************************
					// *** Bar Charts
					// *******************************************************************************
					function createAverageTraineeScoresOverall() {
						chartsDelegate.bar
						.getBatchComparisonLineData($scope.selectedSkill, 
								$scope.selectedTrainingType, 
								$scope.startDate)
								.then(
										function(comparison) {
											chartsDelegate.bar.data
											.getAverageTraineeScoresOverallData(
											$scope.currentBatch.batchId)
											// confirm if batch or trainee
											.then(
													function(data) {
														NProgress.done();
														var barChartObject = chartsDelegate.bar
														.getAverageTraineeScoresOverall(data, comparison, $scope.currentBatch.borderlineGradeThreshold, $scope.currentBatch.goodGradeThreshold);
														$scope.batchOverAllLabels = barChartObject.labels;
														$scope.batchOverAllData = barChartObject.data;
														$scope.batchOverAllOptions = barChartObject.options;
														$scope.batchOverAllColors = barChartObject.colors;
														$scope.batchOverAllDsOverride = barChartObject.datasetOverride;
													}, function() {
														NProgress.done();
													});
										});
					}

					// *******************************************************************************
					// *** Radar Charts
					// *******************************************************************************

					function createTechnicalSkillsBatchOverall() {
						$log.debug("createTechnicalSkillsBatchOverall");
						chartsDelegate.radar.data
								.getTechnicalSkillsBatchOverallData(
										$scope.currentBatch.batchId)
								// batchId
								.then(
										function(data) {
											NProgress.done();
											var radarBatchOverallChartObject = chartsDelegate.radar
													.getTechnicalSkillsBatchOverall(
															data,
															$scope.currentBatch.trainingName);
											$scope.radarBatchOverallData = radarBatchOverallChartObject.data;
											$scope.radarBatchOverallOptions = radarBatchOverallChartObject.options;
											$scope.radarBatchOverallLabels = radarBatchOverallChartObject.labels;
											$scope.radarBatchOverallSeries = radarBatchOverallChartObject.series;
											$scope.radarBatchOverallColors = radarBatchOverallChartObject.colors;

											$scope.radarBatchOverallTable = chartsDelegate.utility
													.dataToTable(radarBatchOverallChartObject);
										});

					}

					// *******************************************************************************
					// *** Doughnut Charts
					// *******************************************************************************

					function createCurrentQCStatus() {
						$log.debug($scope.currentBatch.batchId);
						chartsDelegate.doughnut.data
								.getCurrentQCStatsData($scope.currentBatch.batchId)
								.then(
										function(data) {
											$log.debug(data);
											NProgress.done();
											var doughnutChartObject = chartsDelegate.doughnut
													.getCurrentQCStats(data);
											$scope.currentQCStatsLabels = doughnutChartObject.labels;
											$scope.currentQCStatsData = doughnutChartObject.data;
											$scope.currentQCStatsOptions = doughnutChartObject.options;
											$scope.currentQCStatsColors = doughnutChartObject.colors;
										});

					}
					
					
					/* ************************************************************************************
					 *  alertPopup() Method
					 *  AUTHOR: Leibniz H. Berihuete
					 *  PURPOSE: to update location of existing batches that have unspecified locations.
					 *  NOTE: already-ended batches (old batches) do not need to be updated
					 *  INPUT: none
					 *  PROCESS:
					 *  	1. Get all batches from trainer
					 *  	2. Get current date
					 *  	3. Iterate through all batches
					 *  	4. In each iteration: obtain address object and obtain endDate
					 *  	5. In each iteration: if address== null AND if currentDate < endDate, showAlert: true
					 *  	6. If showAlert is true, showAlert to trainer, indicating which batches to update.
					 * OUTPUT: alert message.  
					 * ************************************************************************************/
					$scope.alertPopup = function() {
						
						
						caliberDelegate.trainer.getAllBatches()
						.then(
								function(batches) {
									var batchesToUpdate = [];
									
									// Holds the address of the a batch
									var address;
									
									// Holds the end-date of a batch
									var endDate;
									
									// get current Date
									var cd = new Date();
									
									// Format the date (in order to do comparison
									var currentDate =  cd.getFullYear() + "-" + cd.getMonth() + "-" +cd.getDate();
									
									// holds the batches that needs to be updated
									var batchesToAlert = [];
									
									
									var showAlert = false;
									
									
									
									// Iterate through each batch
									for(var i = 0; i < batches.length; i++) {
										address = batches[i].address;
										endDate = batches[i].endDate;
										
										// check if the batch needs to be updated
										if(address === null && currentDate < endDate) {
											
											showAlert = true;
											batchesToAlert.push(batches[i].trainingName);
											batchesToUpdate.push(batches[i]);
										}
									}
									
									if(showAlert) {
										// SHOW POP PUP WINDOW:
										
										$scope.batchesToUpdate = batchesToUpdate;
										caliberDelegate.vp.getAllLocations()
										.then(
												function() {
													$scope.alertMessage = "Please update the location on the following batches:";
												    $scope.batchesToAlert = batchesToAlert;
												    
													(function(){
														angular.element('#alertModal').modal('show');
													})();
												}
										);
										
									}
								});
					}

				});
