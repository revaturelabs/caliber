angular
		.module("panel")
		.controller(
				"panelHomeController",
				function($log, $scope, $filter, $http, chartsDelegate,
						caliberDelegate, qcFactory, allBatches) {
					$log.debug("Booted panel home controller.");
					
					(function() {
						// Finishes any left over ajax animation from another
						// page
						NProgress.done();
						createDefaultCharts();

						
					})();

   					function createDefaultCharts(){
						NProgress.start();
						getCurrentPanelsData();
					}


					function createCurrentPanelsLineChart(data) {
						var lineChartObj = chartsDelegate.line
								.getCurrentPanelsLineChart(data);
						$scope.currentPanelsLineData = lineChartObj.data;
						$scope.currentPanelsLineLabels = lineChartObj.labels;
						$scope.currentPanelsLineSeries = lineChartObj.series;
						$scope.currentPanelsLineOptions = lineChartObj.options;
						$scope.currentPanelsLineColors = lineChartObj.colors;
						$scope.currentPanelsDsOverride = lineChartObj.datasetOverride;
					}
					
					function getCurrentPanelsData() {
						chartsDelegate.line.data
								.getCurrentPanelsLineChartData()
								.then(
										function(data) {
											$scope.panelData = data;
											//object with 2 keys: Pass and Repanel w/ last 14 days results for each
											createCurrentPanelsLineChart(data[0]);  
											NProgress.done();
										}, function() {
											NProgress.done();
										});
					}

				});