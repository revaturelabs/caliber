package com.revature.caliber.services;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.caliber.beans.Assessment;
import com.revature.caliber.beans.AssessmentType;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Category;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.data.AssessmentDAO;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.data.CategoryDAO;
import com.revature.caliber.data.GradeDAO;
import com.revature.caliber.data.TraineeDAO;
import com.revature.caliber.exceptions.RevProIntegrationException;
import com.revature.caliber.revpro.quiz.models.Quiz;
import com.revature.caliber.revpro.quiz.models.QuizResult;
import com.revature.caliber.revpro.quiz.models.Quizzes;

@Service
public class RevProQuizIntegrationService {

	private static final Logger log = Logger.getLogger(RevProQuizIntegrationService.class);

	@Autowired
	private AssessmentDAO assessmentDAO;
	@Autowired
	private GradeDAO gradeDAO;
	@Autowired
	private BatchDAO batchDAO;
	@Autowired
	private TraineeDAO traineeDAO;
	@Autowired
	private CategoryDAO categoryDAO;

	/**
	 * Parse the JSON data and create a new assessment for each RevaturePro quiz. 
	 * For each assessment, save each grade for each associate. We find associates
	 * based on their Salesforce resourceId.
	 * 
	 * @param json
	 * @param week
	 * @param batchId
	 */
	@Transactional(rollbackFor=Exception.class)
	public void importGrades(String json, int week, int batchId) {
		log.debug("Parsing data for Aron Anderson: " + json);
		try {
			Quizzes quizzes = new ObjectMapper().readValue(json, Quizzes.class);
			log.debug(quizzes);
			for(Quiz quiz : quizzes.getQuizzes()) {
				// load Caliber-data on batch and category
				log.debug(quiz);
				Category category = categoryDAO.findBySkillCategory(quiz.getCategory());
				if(category == null) {
					throw new RevProIntegrationException(quiz.getCategory() + " is not a valid skill category in Caliber. Please create this category before trying again.");
				}
				log.debug("Found category: " + category);
				Batch batch = batchDAO.findOne(batchId);
				if(batch == null) {
					throw new RevProIntegrationException("Batch number " + batchId + " does not exist!");
				}
				log.debug("Found batch: " + batch);
				
				// save a new Assessment in the database of type 'Exam'
				Assessment assessment = new Assessment(quiz.getTitle(), batch, 100, AssessmentType.Exam, week, category);
				assessmentDAO.save(assessment);
				
				// save each grade for the assessment
				for(QuizResult quizResult : quiz.getGrades()) {
					Trainee trainee = traineeDAO.findByResourceId(quizResult.getTrainee());
					if(trainee == null) {
						throw new RevProIntegrationException("Trainee with resourceId " + quizResult.getTrainee() + " does not exist!");
					}
					log.debug("Found trainee: " + trainee);
					Grade grade = new Grade(assessment, trainee, new Date(), quizResult.getGrade());
					gradeDAO.save(grade);
				}
			}
		} catch (IOException e) {
			log.error(e);
			throw new RevProIntegrationException("Invalid JSON format.");
		}
	}
}
