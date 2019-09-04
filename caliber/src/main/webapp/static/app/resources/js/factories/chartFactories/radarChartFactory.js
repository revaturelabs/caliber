/**
 * 
 * @author Ateeb Khawaja
 * @author Pier Yos
 */
angular
		.module("charts")
		.factory(
				"radarChartFactory",
				function($log) {
					$log.debug("Booted radarChartFactory");
					//Enum defining colors and rgb values
					var colorEnum = {
							  BATCH_BLUE: 1,
							  YELLOW: 2,
							  DARK_GREY: 3,
							  ORANGE: 4,
							  GREEN:5,
							  value: {
							    1: {r: 114, g: 164, b: 194},
							    2: {r: 252, g:180, b: 20},
							    3: {r: 71, g: 76, b: 85},
							    4: {r: 252, g:180, b: 20},
							    5: {r: 242, g: 105, b: 37}
							  }
					};

					var radar = {};
					//Array of colors 
					var colors = [];
					//Loop through enum and add colors to our array
					for(var color in colorEnum)
						{
							//Do not add enum value(properties) as a color
							if(color !== "value")
								{
								var enumValue = colorEnum[color];
								//Chart JS wants properties to be a string of 'rgba(number,number,number,number)' so we concat strings with our rgb values from enum 
								var newColor = {
									backgroundColor : 'rgba(' + colorEnum.value[enumValue].r+',' +colorEnum.value[enumValue].g+',' +colorEnum.value[enumValue].b+', .5)',
									pointBackgroundColor : 'rgba(' + colorEnum.value[enumValue].r+',' +colorEnum.value[enumValue].g+',' +colorEnum.value[enumValue].b+', .5)',
									borderColor : 'rgba(' + colorEnum.value[enumValue].r+',' +colorEnum.value[enumValue].g+',' +colorEnum.value[enumValue].b+', 1)',
									pointHoverBackgroundColor : 'rgba(' + colorEnum.value[enumValue].r+',' +colorEnum.value[enumValue].g+',' +colorEnum.value[enumValue].b+', .3)',
									pointHoverBorderColor : 'rgba(' + colorEnum.value[enumValue].r+',' +colorEnum.value[enumValue].g+',' +colorEnum.value[enumValue].b+', .3)',
									pointBorderColor : '#fff',
									fill : false  
								}
								//Push our newly defined color to the array
								colors.push(newColor);
								}
						}
					//set the first color(Batch color) to be filled.
					colors[0].fill = true;
		
					var radarOptions = {
							legend : {
								display : true
							},
							scale : {
								reverse : false,
								ticks : {
									beginAtZero : false,
									fixedStepSize : 10,
									max : 100,
									suggestedMin : 40
								}
							}
						};
					
					var createGenericRadarChartObject = function(dataArray,
							seriesName) {
						var chartData = {};
						chartData.colors = colors;
						chartData.series = [];
						chartData.series.push(seriesName);

						chartData.labels = [];

						chartData.data = [];
						chartData.data.push([]);

						angular.forEach(dataArray, function(value, key) {
							chartData.labels.push(key);
							chartData.data[0].push(value.toFixed(2));
						});

						chartData.options = radarOptions;
						return chartData;
					}

					radar.getTraineeUpToWeekRadarChart = function(dataArray,
							seriesName) {
						return createGenericRadarChartObject(dataArray,
								seriesName);
					};

					radar.getTraineeOverallRadarChart = function(dataArray,
							seriesName) {
						return createGenericRadarChartObject(dataArray,
								seriesName);
					};

					radar.getBatchOverallRadarChart = function(dataArray,
							seriesName) {
						return createGenericRadarChartObject(dataArray,
								seriesName);
					};

					radar.addDataToExistingRadar = function(currentChartData,
							otherDataArray, seriesName) {
						var newData = [];
						var totalTechs = currentChartData.labels.length;
						currentChartData.series.push(seriesName);

						for (var i = 0; i < totalTechs; i++) {
							if (otherDataArray
									.hasOwnProperty(currentChartData.labels[i])) {
								newData
										.push((otherDataArray[currentChartData.labels[i]])
												.toFixed(2));
							}
						}

						currentChartData.data.push(newData);
						return currentChartData;
					}

					radar.createCombineBatchAndAllTrainees = function(dataSet) {
						var chartData = {};

						chartData.colors = colors;

						chartData.series = [];
						chartData.labels = [];
						chartData.data = [];

						angular.forEach(dataSet, function(value, key) {
							chartData.series.push(key);
							var averageTemp = [];
							angular.forEach(value, function(average, assess) {
								if (!chartData.labels.includes(assess)) {
									chartData.labels.push(assess);
								}
								averageTemp.push(average.toFixed(2));
							})
							chartData.data.push(averageTemp);
						});
						chartData.options = radarOptions;
						return chartData;
					}

					radar.createFromTwoDataSets = function(batchDataSet,
							traineeDataSet, batchSeriesName, traineeSeriesName) {
						$log.debug("radarChartFactory: createFromTwoDataSets");
						var chartData = {};
						var batchSeries = [];
						var traineeSeries = [];
						var labels = [];

						angular.forEach(batchDataSet, function(value, key) {
							if (traineeDataSet.hasOwnProperty(key)) {
								traineeSeries.push((traineeDataSet[key])
										.toFixed(2));
								batchSeries
										.push((batchDataSet[key]).toFixed(2));
								labels.push(key);
							}
						});

						chartData.labels = labels;

						chartData.series = [];
						chartData.series.push(traineeSeriesName);
						chartData.series.push(batchSeriesName);

						chartData.data = [];
						chartData.data.push(traineeSeries);
						chartData.data.push(batchSeries);

						chartData.colors = colors;

						chartData.options = radarOptions;
						$log.debug(chartData);
						return chartData;
					}

					return radar;
				});