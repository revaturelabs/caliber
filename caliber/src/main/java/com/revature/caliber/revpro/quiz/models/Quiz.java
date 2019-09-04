package com.revature.caliber.revpro.quiz.models;

import java.util.List;

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
public class Quiz {

	/**
	 * RevaturePro-specific title.
	 */
	private String title;

	/**
	 * The category of the quiz, such as Java or SQL.
	 */
	private String category;

	/**
	 * Array of all the grades for that quiz. One for each associate
	 */
	private List<QuizResult> grades;

	public Quiz() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<QuizResult> getGrades() {
		return grades;
	}

	public void setGrades(List<QuizResult> grades) {
		this.grades = grades;
	}

	@Override
	public String toString() {
		return "Quiz [title=" + title + ", category=" + category + ", grades=" + grades + "]";
	}

}
