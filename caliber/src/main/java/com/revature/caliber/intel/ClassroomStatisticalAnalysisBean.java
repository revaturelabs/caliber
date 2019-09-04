package com.revature.caliber.intel;

import java.util.Map;

import com.revature.caliber.beans.QCStatus;

/**
 * Defines the various algorithms required by the Reporting Service. Handles the
 * complexity of measuring grades and statistical analyses.
 * 
 * All methods must return grades as a percentage (%) of total raw score.
 * 
 * @author Patrick Walsh
 *
 */
public interface ClassroomStatisticalAnalysisBean {

	/*
	 *******************************************************
	 * Doughnut / Pie Charts
	 *******************************************************
	 */
	/**
	 * Pie chart displaying number of trainees that received red, yellow, green,
	 * x-Axis: QC Statuses
	 * y-Axis: Number of Trainees with 
	 * @param batchId
	 * @param weekNumber
	 * @return Map <The QC Status, Number of Trainees>
	 */
	public Map<QCStatus, Integer> getBatchWeekPieChart(Integer batchId, Integer weekNumber);
	
	/*
	 *******************************************************
	 * Bar Charts
	 *******************************************************
	 */

	/**
	 * x-Axis: Assessment Names
	 * y-Axis: Average Batch Scores
	 * @param batchId
	 * @param week
	 * @return
	 */
	public Map<String, Double[]> getBatchWeekAvgBarChart(int batchId, int week);
	
	/**
	 * x-Axis: Trainee Names
	 * y-Axis: Average Trainee Score
	 * @param batchId
	 * @param week
	 * @return Map<Trainee's name, Double Average Score>
	 */
	public Map<String, Double> getBatchWeekSortedBarChart(int batchId, int week);
	
	/**
	 * x-Axis: Assessment Name, (Weight as well)
	 * y-Axis: Average Scores
	 * @param traineeId
	 * @param week
	 * @return Map<AssessmentType Name, [0: Trainee Overall Average, 1: Batch Overall Average, 2: Score Weight]>
	 */
	public Map<String, Double[]> getBatchOverallTraineeBarChart(Integer batchId, Integer traineeId);
	
	/**
	 * x-Axis: Trainee
	 * y-Axis: Average score
	 * @param batchId
	 * @return
	 */
	public Map<String, Double> getBatchOverallBarChart(Integer batchId);
	
	/**
	 * x-Axis: 
	 * y-Axis: 
	 * @param traineeId
	 * @param week
	 * @return map<'assessmentType', {traineeAvg, batchAvg, weight Percentage}
	 */
	public Map<String, Double[]> getBatchWeekTraineeBarChart(Integer batchId, Integer traineeId, Integer week);
	
	/*
	 *******************************************************
	 * Line Charts
	 *******************************************************
	 */

	/**
	 * x-Axis: 
	 * y-Axis: 
	 * @param batchId
	 * @param week
	 * @param traineeId
	 * @return Map<'week', 'avgScore'>
	 */

	public Map<Integer, Double[]> getTraineeUpToWeekLineChart(int week, int traineeId);
	
	/**
	 * x-Axis: trainee avg, batch avg
	 * y-Axis: week
	 * @param batchId,
	 * @param traineeId
	 * @return Map<Total Weeks, Double[0:Trainee Overall Average 1: Batch Overall Average]>
	 * 
	 */
	public Map<Integer, Double[]> getTraineeOverallLineChart(Integer batchId, Integer traineeId);
	
	/**
	 * x-Axis: 
	 * y-Axis: 
	 * Method for Controller to fetch Week number Batch Average Score
	 * @param batchId
	 * @return Map<Week #, Double Average Score>
	 */
	public Map<Integer, Double> getBatchOverallLineChart(int batchId);
	
	/*
	 *******************************************************
	 * Radar Charts
	 *******************************************************
	 */

	/**
	 * label-Axis: 
	 * value-Axis: 
	 * @param traineeId
	 * @param week
	 * @return
	 */
	public Map<String, Double> getTraineeUpToWeekRadarChart(Integer traineeId, Integer week);
	
	/**
	 * label-Axis: 
	 * value-Axis: 
	 * @param traineeId
	 * @return
	 */
	public Map<String, Double> getTraineeOverallRadarChart(Integer traineeId);
	
	/**
	 * label-Axis: 
	 * value-Axis: 
	 * @param batchId
	 * @return
	 */
	public Map<String, Double> getBatchOverallRadarChart(Integer batchId);
	
}
