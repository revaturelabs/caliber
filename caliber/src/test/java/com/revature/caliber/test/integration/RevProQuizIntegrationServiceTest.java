package com.revature.caliber.test.integration;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.exceptions.RevProIntegrationException;
import com.revature.caliber.services.AssessmentService;
import com.revature.caliber.services.EvaluationService;
import com.revature.caliber.services.RevProQuizIntegrationService;

public class RevProQuizIntegrationServiceTest extends CaliberTest {

	@Autowired
	private RevProQuizIntegrationService revProQuizIntegrationService;
	@Autowired
	private AssessmentService assessmentService;
	@Autowired
	private EvaluationService evaluationService;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
    private String json = "{\r\n" + 
			"	\"quizzes\": [\r\n" + 
			"		{\r\n" + 
			"			\"title\" : \"Core Java Week 1\",\r\n" + 
			"			\"category\": \"Java\",\r\n" + 
			"			\"grades\" : [\r\n" + 
			"				{\r\n" + 
			"					\"trainee\": \"abcdef1234\",\r\n" + 
			"					\"grade\": 75\r\n" + 
			"				},\r\n" + 
			"				{\r\n" + 
			"					\"trainee\": \"ghijkl5678\",\r\n" + 
			"					\"grade\": 87\r\n" + 
			"				}\r\n" + 
			"			]\r\n" + 
			"		},\r\n" + 
			"		{\r\n" + 
			"			\"title\" : \"JUnit\",\r\n" + 
			"			\"category\": \"JUnit\",\r\n" + 
			"			\"grades\" : [\r\n" + 
			"				{\r\n" + 
			"					\"trainee\": \"abcdef1234\",\r\n" + 
			"					\"grade\": 81\r\n" + 
			"				},\r\n" + 
			"				{\r\n" + 
			"					\"trainee\": \"ghijkl5678\",\r\n" + 
			"					\"grade\": 65\r\n" + 
			"				}\r\n" + 
			"			]\r\n" + 
			"		}\r\n" + 
			"	]\r\n" + 
			"}";
    
	@Test
	public void testIntegration() {
		int week = 11;
		int batchId = 2200;
		revProQuizIntegrationService.importGrades(json, week, batchId);
		assertNotNull(assessmentService.findAssessmentByWeek(batchId, week));
		assertNotNull(evaluationService.findGradesByWeek(batchId, week));
	}
	
	@Test
	public void negativeTestIntegration() {
		int week = 11;
		int batchId = 2200;
		String invalidCategory = "{\r\n" + 
				"	\"quizzes\": [\r\n" + 
				"		{\r\n" + 
				"			\"title\" : \"Core Java Week 1\",\r\n" + 
				"			\"category\": \"Dan Pickles\",\r\n" + 
				"			\"grades\" : [\r\n" + 
				"				{\r\n" + 
				"					\"trainee\": \"abcdef1234\",\r\n" + 
				"					\"grade\": 75\r\n" + 
				"				},\r\n" + 
				"				{\r\n" + 
				"					\"trainee\": \"ghijkl5678\",\r\n" + 
				"					\"grade\": 87\r\n" + 
				"				}\r\n" + 
				"			]\r\n" + 
				"		}\r\n" + 
				"	]\r\n" + 
				"}";
		thrown.expect(RevProIntegrationException.class);
		revProQuizIntegrationService.importGrades(invalidCategory, week, batchId);

		String invalidTrainee = "{\r\n" + 
				"	\"quizzes\": [\r\n" + 
				"		{\r\n" + 
				"			\"title\" : \"Core Java Week 1\",\r\n" + 
				"			\"category\": \"Java\",\r\n" + 
				"			\"grades\" : [\r\n" + 
				"				{\r\n" + 
				"					\"trainee\": \"asdjlksadpiuds4123413\",\r\n" + 
				"					\"grade\": 75\r\n" + 
				"				},\r\n" + 
				"				{\r\n" + 
				"					\"trainee\": \"ghijkl5678\",\r\n" + 
				"					\"grade\": 87\r\n" + 
				"				}\r\n" + 
				"			]\r\n" + 
				"		}\r\n" + 
				"	]\r\n" + 
				"}";
		thrown.expect(RevProIntegrationException.class);
		revProQuizIntegrationService.importGrades(invalidTrainee, week, batchId);
		
		int invalidBatch = -111;
		thrown.expect(RevProIntegrationException.class);
		revProQuizIntegrationService.importGrades(json, week, invalidBatch);
		
	}
	
}
