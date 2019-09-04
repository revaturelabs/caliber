package com.revature.caliber.revpro.quiz.models;

/**
 * DTO used to import grades from RevaturePro quizzes. Format will look as
 * below:
 * 
 * { "batch": "AGSHHASLH19127", "quizzes": [ { "title" : "Core Java Week 1", "category": "Java", "grades" :
 * [ { "trainee": "salesforceResourceId", "grade": 75 }, { "trainee":
 * "jau1j18wjs9aj198", "grade": 87 } ] }, { "title" : "JUnit", "category":
 * "JUnit", "grades" : [ { "trainee": "salesforceResourceId", "grade": 81 }, {
 * "trainee": "jau1j18wjs9aj198", "grade": 65 } ] } ] }
 * 
 * @author Patrick Walsh
 *
 */
public class QuizResult {

	/**
	 * The Salesforce resourceId for the associate that completed the quiz
	 */
	private String trainee;
	
	/**
	 * The final grade percentage of the quiz (out of 100)
	 */
	private double grade;

	public QuizResult() {
		super();
	}

	public String getTrainee() {
		return trainee;
	}

	public void setTrainee(String trainee) {
		this.trainee = trainee;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "QuizResult [trainee=" + trainee + ", grade=" + grade + "]";
	}
	
}
