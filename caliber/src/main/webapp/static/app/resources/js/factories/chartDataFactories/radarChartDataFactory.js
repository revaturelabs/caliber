/**
 * 
 * @author Ateeb Khawaja
 * @author Pier Yos
 * 
 */
angular.module("reportApi").factory(
		"radarChartDataFactory",
		function($http, $log, $q) {
			$log.debug("Booted radarChartDataFactory");

			var report = {};

			report.getTraineeUpToWeekRadarChart = function(week, traineeId) {
				return $http(
						{
							url : "/all/reports/week/" + week + "/trainee/"
									+ traineeId + "/radar-trainee-up-to-week",
							method : "GET"
						}).then(function(response) {
					$log.debug("Completed getTraineeUpToWeekRadarChart");
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				}); // end then
			}

			report.getTraineeOverallRadarChart = function(traineeId) {
				return $http(
						{
							url : "/all/reports/trainee/" + traineeId
									+ "/radar-trainee-overall",
							method : "GET"
						}).then(function(response) {
					$log.debug("Completed getTraineeOverallRadarChart");
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				}); // end then
			}

			report.getBatchOverallRadarChart = function(batchId) {
				return $http(
						{
							url : "/all/reports/batch/" + batchId
									+ "/overall/radar-batch-overall",
							method : "GET"
						}).then(function(response) {
					$log.debug("Completed getBatchOverallRadarChart");
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			}

			report.getTraineAndBatchSkillComparisonChart = function(batchId,
					week, traineeId) {
				var batchPromise = $http({
					method : "GET",
					url : "/all/reports/batch/" + batchId
							+ "/overall/radar-batch-overall"
				});

				var traineePromise = $http({
					method : "GET",
					url : "/all/reports/trainee/" + traineeId
							+ "/radar-trainee-overall"
				});

				if (Number.isInteger(week) || week > 0) {
					traineePromise = $http({
						method : "GET",
						url : "/all/reports/week/" + week + "/trainee/"
								+ traineeId + "/radar-trainee-up-to-week"
					});
				}

				return $q.all([ batchPromise, traineePromise ]).then(
						function(response) {
							var data = {
								"batch" : response[0].data,
								"trainee" : response[1].data
							};
							return data;
						},
						function(response) {
							$log
									.error("There was an error: "
											+ response.status);
						});

			}

			report.getAllTraineesAndBatchRadarChart = function(batchId) {
				return $http(
						{
							url : "/all/reports/batch/" + batchId
									+ "/radar-batch-all-trainees",
							method : "GET"
						}).then(function(response) {
					$log.debug("Completed getAllTraineeInBatchRadarChart");
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			}
			return report;
		});
