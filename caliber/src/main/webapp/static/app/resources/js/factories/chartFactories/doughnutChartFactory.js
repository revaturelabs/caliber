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
angular.module("charts").factory("doughnutChartFactory", function($log) {
	$log.debug("Booted Doughtnut Chart Factory");

	var doughnutChart = {};

	doughnutChart.batchWeekQCPie = function(dataArray) {
		var chartData = {};
		chartData.data = [];
		chartData.labels = [];
		chartData.colors = [];
		chartData.options = {
			legend : {
				display : true,
				labels : {
					boxWidth : 10
				}
			}
		};

		angular.forEach(dataArray, function(value, key) {
			if (key === "Superstar")
				chartData.colors.push("#393fef");
			else if (key === "Good")
				chartData.colors.push("#18ad18");
			else if (key === "Average")
				chartData.colors.push("#f9e900");
			else if (key === "Poor")
				chartData.colors.push("#ea2825");
			chartData.labels.push(key);
			chartData.data.push(value);
		});

		return chartData;
	};
	return doughnutChart;
});