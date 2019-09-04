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
				"doughnutChartDataFactory",
				function($http, $log) {
					$log.debug("Booted doughnutChartData Factory");

					var report = {};

					report.batchWeekQCPie = function(batchId, weekNum) {
						return $http({
								url : "/all/reports/batch/" + batchId
											+ "/week/" + weekNum + "/pie",
								method : "GET"
						})
								.then(
										function(response) {
											$log.debug("Batch -> Week -> Pie Chart");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error(
												"There was an error in doughnutChartDataFactory -> batchWeekQCPie. "
												+ response.status);
										});
					};

					report.batchCurrentWeekQCPie = function(batchId) {
						return $http({
							url : "/all/reports/batch/" + batchId + "/pie/",
							method : "GET"
						})
								.then(
										function(response) {
											$log.debug("Batch -> Current Week -> Pie Chart");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error(
												"There was an error in doughnutChartDataFactory -> batchCurrentWeekQCPie. "
												+ response.status);
										});
					};
					return report;
				});
