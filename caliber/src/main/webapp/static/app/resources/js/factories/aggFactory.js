angular.module("api").factory(
		"aggFactory",
		function($http, $log) {
			$log.debug("Booted Aggregate Factory");

			var agg = {};
			/**
			 * 
			 * @param traineeId
			 * @returns {*}
			 */
			agg.techTrainee = function(traineeId) {
				return $http({
					//url : "/all/tech/trainee/" + traineeId,
					url: "/all/reports/trainee/" + traineeId + "/radar-trainee-overall",
					method : "GET"
				}).then(function(response) {
					$log.debug("Agg - Tech - trainee -- success");
					$log.debug(response);
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});

			};
			/**
			 * 
			 * @param traineeId
			 * @returns {*}
			 */
			// RADAR CHART TRAINEE TECHNOLOGY
			agg.weekTrainee = function(traineeId) {
				return $http({
					//url : "/all/agg/week/trainee/" + traineeId,
					url : "/all/reports/trainee/" + traineeId + "/radar-trainee-overall",
					method : "GET"
				}).then(function(response) {
					$log.debug("Agg - Week - trainee -- success");
					$log.debug(response);
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			};

			/**
			 * 
			 * @param batchId
			 * @returns {*}
			 */
			agg.techBatch = function(batchId) {
				return $http({
					//url : "/all/agg/tech/batch/" + batchId,
					url: "/all/reports/batch/" + batchId + "/overall/radar-batch-overall",
					method : "GET"
				}).then(function(response) {
					$log.debug("Agg - Tech - Batch -- success");
					$log.debug(response);
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			};

			/**
			 * 
			 * @param batchId
			 * @returns {*}
			 */
			// Assessments ?
			agg.weekBatch = function(batchId) {
				return $http({
					url : "/all/agg/week/batch/" + batchId,
					method : "GET"
				}).then(function(response) {
					$log.debug("Agg - Week - Batch -- success");
					$log.debug(response);
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			};

			/**
			 * 
			 * @returns {*}
			 */
			agg.techAllBatch = function() {
				return $http({
					url : "/vp/agg/tech/batch/all",
					method : "GET"
				}).then(function(response) {
					$log.debug("Agg - Tech - Batch - All -- success");
					$log.debug(response);
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			};

			/**
			 * 
			 * @param trainerId
			 * @returns {*}
			 */
			agg.batchTrainer = function(trainerId) {
				return $http({
					url : "/all/agg/batch/trainer/" + trainerId,
					method : "GET"
				}).then(function(response) {
					$log.debug("Agg - Batch - Trainer -- success");
					$log.debug(response);
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			};
			/**
			 * 
			 * @param trainerId
			 * @returns {*}
			 */
			agg.batchTrainer = function(trainerId) {
				return $http({
					url : "/all/agg/batch/trainer/" + trainerId,
					method : "GET"
				}).then(function(response) {
					$log.debug("Agg - Batch - Trainer -- success");
					$log.debug(response);
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			};

			/**
			 * 
			 */
			agg.reportLineChart = function(week, traineeId) {
				return $http(
						{
							url : "/all/reports/batch/week/" + week
									+ "/trainee/" + traineeId,
							method : "GET"
						}).then(function(response) {
					$log.debug("Agg - Batch - week-- trainee -- success");
					$log.debug(response);
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			};
			/**
			 * 
			 */
			agg.reporAssesmentChart = function(batchId, week) {
				return $http(
						{
							url : "/all/reports/batch/" + batchId + "/week/"
									+ week + "/barAssesment",
							method : "GET"
						}).then(function(response) {
					$log.debug("Agg - Batch - batchId, Week -- success");
					$log.debug(response);
					return response.data;
				}, function(response) {
					$log.error("There was an error: " + response.status);
				});
			};

			return agg;
		});
