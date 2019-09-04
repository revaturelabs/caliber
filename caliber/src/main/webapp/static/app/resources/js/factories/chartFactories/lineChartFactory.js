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
angular.module("charts").factory("lineChartFactory", function($log) {
	$log.debug("Booted Line Chart Factory");

	Chart.defaults.global.elements.line.fill = false;
	
	var lineChart = {};
	
	var mainColor = {
		backgroundColor : 'rgba(114, 164, 194, .5)',
		pointBackgroundColor : 'rgba(114, 164, 194, .5)',
		borderColor : 'rgba(114, 164, 194, 1)',
		pointHoverBackgroundColor : 'rgba(114, 164, 194, .3)',
		pointHoverBorderColor : 'rgba(114, 164, 194, .3)',
		pointBorderColor : '#fff'
	};

	var secondaryColor = {
		backgroundColor : 'rgba(252, 180, 20, .6)',
		pointBackgroundColor : 'rgba(252, 180, 20, .6)',
		borderColor : 'rgba(252, 180, 20, 1)',
		pointHoverBackgroundColor : 'rgba(252, 180, 20, .3)',
		pointHoverBorderColor : 'rgba(252, 180, 20, .3)',
		pointBorderColor : '#fff'
	};

	lineChart.getBatchOverallLineChart = function(dataArray) {
		var chartData = {};
		chartData.data = [];
		chartData.data.push([]);
		chartData.labels = [];
		chartData.colors = [ mainColor ];
		chartData.options = {
			scales : {
				yAxes : [ {
					scaleLabel : {
						display : true,
						labelString : 'Average'
					},
					ticks : {
						suggestedMin : 40,
						suggestedMax : 100,
						stepSize : 20
					}
				} ]
			}
		};
		chartData.datasetOverride = [ {
			fill : false
		} ];
		// traverse through array of objects and grab labels and data

		angular.forEach(dataArray, function(value, key) {
			chartData.labels.push(key);
			chartData.data[0].push(value.toFixed(2));
		});
		return chartData;
	};

	lineChart.getTraineeUpToWeekLineChart = function(dataArray) {
		var chartData = {};

		chartData.data = [];
		chartData.labels = [];
		chartData.series = [ "Trainee", "Batch" ];
		chartData.colors = [ mainColor, secondaryColor ];
		chartData.options = {
			scales : {
				xAxes : [ {
					scaleLabel : {
						display : true,
						labelString : 'Week'
					}

				} ],
				yAxes : [ {
					scaleLabel : {
						display : true,
						labelString : 'Score'
					},

					ticks : {
						suggestedMin : 40,
						suggestedMax : 100,
						stepSize : 20
					}
				} ]
			},
		};
		
		var series1 = [];
		var series2 = [];

		angular.forEach(dataArray, function(value, key) {
			chartData.labels.push(key);
			series1.push(value[0].toFixed(2));
			series2.push(value[1].toFixed(2));
		});

		chartData.data.push(series1);
		chartData.data.push(series2);
		chartData.datasetOverride = [ {
			fill : false
		}, {
			fill : false
		} ];
		return chartData;
	};

	lineChart.getTraineeOverallLineChart = function(dataArray) {
		var chartData = {};
		chartData.series = [ "Trainee", "Batch" ];
		chartData.colors = [ mainColor, secondaryColor ];
		chartData.data = [];
		chartData.options = {
			scales : {
				xAxes : [ {
					scaleLabel : {
						display : true,
						labelString : 'Week'
					}

				} ],
				yAxes : [ {
					scaleLabel : {
						display : true,
						labelString : 'Score'
					},

					ticks : {
						suggestedMin : 40,
						suggestedMax : 100,
						stepSize : 20
					}
				} ]
			},
		};
		// loop through object array
		var trainee = [];
		var batch = [];
		var week = [];
		angular.forEach(dataArray, function(value, key) {
			trainee.push(value[0].toFixed(2));
			batch.push(value[1].toFixed(2));
			week.push(key);
		});
		chartData.data.push(trainee);
		chartData.data.push(batch);
		chartData.labels = week;
		
		chartData.datasetOverride = [ {
			fill : false
		}, {
			fill : false
		} ];
		
		return chartData;

	};

	lineChart.getCurrentBatchesAverageScoreChart = function(dataArray) {
		var chartData = {};
		chartData.data = [];
		chartData.colors = [ mainColor, secondaryColor ];
		chartData.series = [];
		chartData.labels = [];

		chartData.options = {
				legend : {
					display : true,
					labels : {
						boxWidth : 10
					}
				},	
			scales : {
				xAxes : [ {
					scaleLabel : {
						display : true,
						labelString : 'Week'
					}

				} ],
				yAxes : [ {
					scaleLabel : {
						display : true,
						labelString : 'Score'
					},

					ticks : {
						suggestedMin : 40,
						suggestedMax : 100,
						stepSize : 20
					}
				} ]
			}
		};
	
		var highestWeek = 0;
		angular.forEach(dataArray, function(batch) {
			var currentWeek = 1;
			chartData.series.push(batch.label);
			var temp = [];
			angular.forEach(batch.grades, function(value2, key2) {
				while (currentWeek < key2) {
					temp.push(0);
					currentWeek++;
				}
				temp.push(value2.toFixed(2));
				currentWeek++;
				if (currentWeek > highestWeek) {
					highestWeek = currentWeek;
				}
			});
			chartData.data.push(temp);
		});
		chartData.datasetOverride = [];
		for (var i = 1; i < highestWeek; i++) {
			chartData.labels.push("Week " + i);
			chartData.datasetOverride.push({fill:false});
		}
		return chartData;
	};
	//panel chart
	lineChart.getCurrentPanelsLineChart = function(dataArray) {
		var chartData = {};
		chartData.data = [];
		chartData.colors = [];								
		chartData.series = [];
		chartData.labels = [];
		chartData.datasetOverride = [];
		for (var i = 0; i<=13; i++){
			chartData.datasetOverride.push({fill:false});
		}
		
		
		//format output of day from day of year number
		function dateFromDay(year, day){
			  var date = new Date(year, 0); // initialize a date in `year-01-01`
			  return new Date(date.setDate(day)); // add the number of days
		}
		//need today for year value in date formatting
		var today = new Date();
		
		//max y-axis value
		var max = 0;
		//set data by going through Pass and Repanel results
		Object.entries(dataArray).forEach(([key, value]) => {
			var lineResult = [];
			//k = day of year, v = # of panels for that day
			Object.entries(value).forEach(([k,v]) => {
				lineResult.push(v);
				
				//set new max
				if(v > max)
					max = v;
			});
			
			chartData.data.push(lineResult);
			chartData.series.push(key); // Pass or Repanel
			
			//set colors for correct line
			if(key === "Pass"){
				chartData.colors.push("#18ad18");
			} else{
				chartData.colors.push("#ea2825");
			}
		});
		//set labels for x-axis to readable date 
		Object.entries(dataArray['Pass']).forEach(([key, value]) => {
			//using k as day of year
			var d = dateFromDay(today.getFullYear(), key)
			chartData.labels.push(d.getMonth() + 1 + "/" + d.getDate());
		});
		
		chartData.options = {
				legend : {
					display : true,
					labels : {
						boxWidth : 10
					}
				},	
				scales : {
					xAxes : [ {
						scaleLabel : {
							display : true,
							labelString : 'Day'
						}

					} ],
					yAxes : [ {
						scaleLabel : {
							display : true,
							labelString : '# of Panels'
						},

						ticks : {
							suggestedMin : 0,
							suggestedMax : max + 1,
							stepSize : 1
						}
					} ]
				}
		};

		
		return chartData;
	};
	
	return lineChart;
});
