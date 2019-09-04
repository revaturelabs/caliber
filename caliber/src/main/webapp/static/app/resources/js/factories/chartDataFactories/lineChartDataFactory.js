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
				"lineChartDataFactory",
				function($http, $log) {
					$log.debug("Booted Line Chart Data Factory");

					var report = {};

					report.getTraineeUpToWeekLineChart = function(batchId,
							week, traineeId) {
						return $http(
								{
									url : "/all/reports/batch/" + batchId
											+ "/week/" + week + "/trainee/"
											+ traineeId
											+ "/line-trainee-up-to-week",
									method : "GET"
								})
								.then(
										function(response) {
											$log
													.debug("Agg - Batch - week-- trainee -- success");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
										});
					}

					report.getTraineeOverallLineChart = function(batchId,
							traineeId) {

						return $http(
								{
									url : "/all/reports/batch/" + batchId
											+ "/overall/trainee/" + traineeId
											+ "/line-trainee-overall",
									method : "GET"
								})
								.then(
										function(response) {
											$log
													.debug("All-reports-Batch-overall-trainee-overall");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
										});
					}

					report.getBatchOverallLineChartData = function(batchId) {
						return $http(
								{
									url : "/all/reports/batch/" + batchId
											+ " /overall/line-batch-overall",
									method : "GET"
								})
								.then(
										function(response) {
											$log
													.debug("Batch -> Overall -> line chart");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log
													.error("There was an error in lineChartDataFactory -> getBatchOverallLineChart. "
															+ response.status);
										});
					}

					/**
					 * This is for the vpHome LineChart
					 */
					report.getCurrentBatchesAverageScoreChartData = function() {
						return $http({
							url : '/all/reports/dashboard',
							method : "GET"
						})
								.then(
										function(response) {
											$log
													.debug("all -> All Current Batches -> Weekly  Averages");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
										});
					}
					
					/**
					 * This is for the panel LineChart
					 */
					report.getCurrentPanelsLineChartData = function() {
						return $http({
							url : '/all/reports/biweeklyPanelResults',
							method : "GET"
						})
								.then(
										function(response) {
											$log
													.debug("all -> All Current Batches -> Panel results");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
										});
					}

					return report;
				});
