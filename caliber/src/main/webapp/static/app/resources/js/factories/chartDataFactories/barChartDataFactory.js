/**
 * Team !Uncharted
 * 
 * @author Pier Yos
 * @author Hossain Yahya
 * @author Yanilda Peralta
 * @author Igor Gluskin
 * @author Ateeb Khawaja
 * 
 */
angular
		.module("reportApi")
		.factory(
				"barChartDataFactory",
				function($http, $log) {
					$log.debug("Booted Bar Chart Data Factory");

					var report = {};

					report.getBatchWeekAvgBarChart = function(batchId, week) {
						return $http(
								{
									url : "/all/reports/batch/" + batchId
											+ "/week/" + week
											+ "/bar-batch-week-avg",
									method : "GET"
								})
								.then(
										function(response) {
											$log.debug("Agg - Batch - batchId, Week -- success");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: " + response.status);
										});
					}

					report.batchWeekTraineeAssessBar = function(batchId,
							weekNum, traineeId) {
						return $http(
								{
									url : "/all/reports/batch/" + batchId
											+ "/week/" + weekNum + "/trainee/"
											+ traineeId
											+ "/bar-batch-week-trainee",
									method : "GET"
								})
								.then(
										function(response) {
											$log.debug("Reports - batchWeekTraineeAssessBar -- success");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: " + response.status);
										});
					}

					report.batchOverallTraineeAssessBar = function(batchId,
							traineeId) {
						return $http(
								{
									url : "/all/reports/batch/" + batchId
											+ "/overall/trainee/" + traineeId
											+ "/bar-batch-overall-trainee",
									method : "GET"
								})
								.then(
										function(response) {
											$log.debug("Reports - batchWeekTraineeAssessBar -- success");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: " + response.status);
										});
					}

					report.getBarChartBatchWeekAvg = function(batchId, week) {
						return $http(
								{
									url : "/all/reports/batch/" + batchId
											+ "/week/" + week + "/bar",
									method : "GET"
								})
								.then(
										function(response) {
											$log.debug("Batch - Week - batch avg Bar Chart");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: " + response.status);
										}); // end then
					}

					report.getBatchWeekSortedBarChartData = function(batchId,
							week) {
						return $http(
								{
									url : "/all/reports/batch/" + batchId
											+ "/week/" + week
											+ "/bar-batch-weekly-sorted",
									method : "GET"
								})
								.then(
										function(response) {
											$log.debug("Batch -> Week -> getBatchWeekSortedBarChartData")
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error in barChartDataFactory -> getBatchWeekSortedBarChartData "
															+ response.status);
										});
					}

					report.getBatchOverallBarChart = function(batchId) {
						return $http(
								{
									url : "/all/reports/batch/" + batchId
											+ "/overall/bar-batch-overall",
									method : "GET"
								})
								.then(
										function(response) {
											$log.debug("Batch -> overall -> score")
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error in barChartDataFactory -> getBatchOverallBarChart "
															+ response.status);
										});
					}

					report.getDummyBarChartData = function() {
						return {
							"poor" : [ 1000, 1200, 1300, 1400, 1060, 2030,
								2070, 4000, 4100, 4020, 4030, 4050 ],
							"average" : [ 21000, 22000, 26000, 35000, 55000,
								55000, 56000, 59000, 60000, 61000, 60100,
								62000 ],
							"good" : [ 21000, 22000, 26000, 35000, 55000,
								55000, 56000, 59000, 60000, 61000, 60100,
								62000 ],
							"superstar" : [ 1000, 1200, 1300, 1400, 1060, 2030,
									2070, 4000, 4100, 4020, 4030, 4050 ],
							"batches" : [ "Batch 1", "Batch 2", "Batch 3",
									"Batch 4", "Batch 5", "Batch 6", "Batch 7",
									"Batch 8", "Batch 9", "Batch 10",
									"Batch 11", "Batch 12" ]
						};

					}

					report.getAllBatchesCurrentWeekQCStats = function() {
						return $http(
								{
									url : "/all/reports/batch/week/stacked-bar-current-week",
									method : "GET"
								})
								.then(
										function(response) {
											$log.debug("getAllBatchesCurrentWeekQCStats")
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error in barChartDataFactory -> getAllBatchesCurrentWeekQCStats:"
															+ response.status);
										});
					}

					report.getBatchComparisonLine = function(skill, training,
							startDate) {
						return $http(
								{
									url : "/all/reports/compare/skill/" + skill
											+ "/training/" + training
											+ "/date/" + startDate,
									method : "GET"
								})
								.then(
										function(response) {
											$log.debug("getBatchComparisonLine")
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error in barChartDataFactory -> getBatchComparisonLine:"
															+ response.status);
										});
					}

					return report;
				});
