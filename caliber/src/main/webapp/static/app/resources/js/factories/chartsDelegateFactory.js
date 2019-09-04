angular
		.module("delegate")
		.factory(
				"chartsDelegate",
				function($log, doughnutChartFactory, doughnutChartDataFactory,
						barChartFactory, barChartDataFactory,
						radarChartFactory, radarChartDataFactory,
						lineChartFactory, lineChartDataFactory) {
					$log.debug("Booted charts delegate");

					var delegate = {};
					delegate.doughnut = {};
					delegate.doughnut.data = {};
					delegate.bar = {};
					delegate.bar.data = {};
					delegate.radar = {};
					delegate.radar.data = {};
					delegate.line = {};
					delegate.line.data = {};
					delegate.utility = {};

					/**
					 * ********************* Doughnut
					 * *************************************
					 */
					delegate.doughnut.getQCStats = function(dataArray) {
						return doughnutChartFactory.batchWeekQCPie(dataArray);
					};

					delegate.doughnut.data.getQCStatsData = function(batchId,
							weekId) {
						return doughnutChartDataFactory.batchWeekQCPie(batchId,
								weekId);
					};

					delegate.doughnut.getCurrentQCStats = function(dataArray) {
						return doughnutChartFactory.batchWeekQCPie(dataArray);
					};

					delegate.doughnut.data.getCurrentQCStatsData = function(
							batchId) {
						return doughnutChartDataFactory
								.batchCurrentWeekQCPie(batchId);
					};

					/**
					 * ************************* Bar *************************
					 */
					delegate.bar.getBatchComparisonLineData = function(skill,
							training, startDate) {
						return barChartDataFactory.getBatchComparisonLine(
								skill, training, startDate)
					};

					delegate.bar.getAssessmentAveragesBatchWeekly = function(
							dataArray) {
						return barChartFactory
								.getBatchWeekAvgBarChart(dataArray);
					}

					delegate.bar.data.getAssessmentAveragesBatchWeeklyData = function(
							batchId, week) {
						return barChartDataFactory.getBatchWeekAvgBarChart(
								batchId, week);
					}

					delegate.bar.getAverageTraineeScoresWeekly = function(
							dataArray, comparison, bad, good) {
						return barChartFactory.getBatchWeekSortedBarChart(
								dataArray, comparison, bad, good);
					}

					delegate.bar.data.getAverageTraineeScoresWeeklyData = function(
							batchId, week) {
						return barChartDataFactory
								.getBatchWeekSortedBarChartData(batchId, week);
					}

					delegate.bar.getAssessmentAveragesTraineeWeekly = function(
							dataArray) {
						return barChartFactory
								.getTraineeWeeklyAssessAvgs(dataArray);
					}

					delegate.bar.data.getAssessmentAveragesTraineeWeeklyData = function(
							batchId, weekNum, traineeId) {
						return barChartDataFactory.batchWeekTraineeAssessBar(
								batchId, weekNum, traineeId);
					}

					delegate.bar.getAssessmentAveragesTraineeOverall = function(
							dataArray) {
						return barChartFactory
								.getTraineeOverallAssessAvgs(dataArray);
					}

					delegate.bar.data.getAssessmentAveragesTraineeOverallData = function(
							batchId, traineeId) {
						return barChartDataFactory
								.batchOverallTraineeAssessBar(batchId,
										traineeId);
					}

					delegate.bar.getAverageTraineeScoresOverall = function(
							dataArray, comparison, bad, good) {
						return barChartFactory.getBatchOverallBarChart(
								dataArray, comparison, bad, good);
					}

					delegate.bar.data.getAverageTraineeScoresOverallData = function(
							batchId) {
						return barChartDataFactory
								.getBatchOverallBarChart(batchId);
					}

					delegate.bar.getAllBatchesCurrentWeekQCStats = function(
							dataArray) {
						return barChartFactory
								.getAllBatchesCurrentWeekQCStats(dataArray);
					}

					delegate.bar.data.getAllBatchesCurrentWeekQCStatsData = function() {
						return barChartDataFactory
								.getAllBatchesCurrentWeekQCStats();
					}

					/**
					 * ************************ Radar ************************
					 */
					delegate.radar.data.getAllTraineesAndBatchRadarChart = function(
							batchId) {
						return radarChartDataFactory
								.getAllTraineesAndBatchRadarChart(batchId);
					}
					delegate.radar.getTechnicalSkillsTraineeWeekly = function(
							dataArray, seriesName) {
						return radarChartFactory.getTraineeUpToWeekRadarChart(
								dataArray, seriesName);
					}

					delegate.radar.data.getTechnicalSkillsTraineeWeeklyData = function(
							week, traineeId) {
						return radarChartDataFactory
								.getTraineeUpToWeekRadarChart(week, traineeId);
					}

					delegate.radar.getTechnicalSkillsTraineeOverall = function(
							dataArray, seriesName) {
						return radarChartFactory.getTraineeOverallRadarChart(
								dataArray, seriesName);
					}

					delegate.radar.data.getTechnicalSkillsTraineeOverallData = function(
							traineeId) {
						return radarChartDataFactory
								.getTraineeOverallRadarChart(traineeId);
					}

					delegate.radar.getTechnicalSkillsBatchOverall = function(
							dataArray, seriesName) {
						return radarChartFactory.getBatchOverallRadarChart(
								dataArray, seriesName);
					}

					delegate.radar.data.getTechnicalSkillsBatchOverallData = function(
							batchId) {
						return radarChartDataFactory
								.getBatchOverallRadarChart(batchId);
					}

					delegate.radar.addRadarToExistingRadar = function(
							currentChartData, otherDataArray, seriesName) {
						return radarChartFactory.addDataToExistingRadar(
								currentChartData, otherDataArray, seriesName);
					}

					delegate.radar.getCombineBatchAndAllTraineeAssess = function(
							dataSet) {
						return radarChartFactory
								.createCombineBatchAndAllTrainees(dataSet);
					}

					delegate.radar.data.getTraineAndBatchSkillComparisonChart = function(
							batchId, week, traineeId) {
						return radarChartDataFactory
								.getTraineAndBatchSkillComparisonChart(batchId,
										week, traineeId);
					}

					delegate.radar.createFromTwoDataSets = function(
							batchDataSet, traineeDataSet, batchSeriesName,
							trainingSeriesName) {
						return radarChartFactory.createFromTwoDataSets(
								batchDataSet, traineeDataSet, batchSeriesName,
								trainingSeriesName);
					}

					/**
					 * ************************ Line ************************
					 */
					delegate.line.getWeeklyProgressBatchOverall = function(
							dataArray) {
						return lineChartFactory
								.getBatchOverallLineChart(dataArray);
					}

					delegate.line.data.getWeeklyProgressBatchOverallData = function(
							batchId) {
						return lineChartDataFactory
								.getBatchOverallLineChartData(batchId);
					}

					delegate.line.getWeeklyProgressTraineeWeekly = function(
							dataArray) {
						return lineChartFactory
								.getTraineeUpToWeekLineChart(dataArray);
					}

					delegate.line.data.getWeeklyProgressTraineeWeeklyData = function(
							batchId, week, traineeId) {
						return lineChartDataFactory
								.getTraineeUpToWeekLineChart(batchId, week,
										traineeId);
					}

					delegate.line.getCurrentBatchesAverageScoreChart = function(
							dataArray) {
						return lineChartFactory
								.getCurrentBatchesAverageScoreChart(dataArray);
					}

					delegate.line.data.getCurrentBatchesAverageScoreChartData = function() {
						return lineChartDataFactory
								.getCurrentBatchesAverageScoreChartData();
					}

					delegate.line.getWeeklyProgressTraineeOverall = function(
							dataArray) {
						return lineChartFactory
								.getTraineeOverallLineChart(dataArray);
					}

					delegate.line.data.getWeeklyProgressTraineeOverallData = function(
							batchId, traineeId) {
						return lineChartDataFactory.getTraineeOverallLineChart(
								batchId, traineeId);

					}
					
					delegate.line.getCurrentPanelsLineChart = function(
							dataArray) {
						return lineChartFactory
								.getCurrentPanelsLineChart(dataArray);
					}

					delegate.line.data.getCurrentPanelsLineChartData = function() {
						return lineChartDataFactory
								.getCurrentPanelsLineChartData();
					}

					/**
					 * ************************ Utility ************************
					 */
					delegate.utility.dataToTable = function(chartObject) {
						var tableDataSet = [];
						for (var i = 0; i < chartObject.labels.length; i++) {
							var row = {};
							row.label = chartObject.labels[i];
							row.data = [];
							angular.forEach(chartObject.data, function(value) {
								row.data.push(value[i]);
							});
							tableDataSet.push(row);
						}
						$log.debug(tableDataSet);
						return tableDataSet;
					}
					return delegate;
				});
