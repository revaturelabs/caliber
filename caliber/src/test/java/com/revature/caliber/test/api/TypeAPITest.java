package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import com.revature.caliber.beans.InterviewFormat;
import com.revature.caliber.beans.PanelStatus;

import io.restassured.http.ContentType;

public class TypeAPITest extends AbstractAPITest {

	private static final Logger log = Logger.getLogger(TypeAPITest.class);

	/*
	 * Type API endpoints
	 */
	
	private static final String ALL_SKILL_TYPES = "types/skill/all";
	private static final String ALL_TRAINING_TYPES = "types/training/all";
	private static final String ALL_TRAINING_STATUS = "types/trainingstatus/all";
	private static final String ALL_TYPE_NOTES = "types/note/all";
	private static final String ALL_QCSTATUS_TYPES = "types/qcstatus/all";
	private static final String ALL_ASSESSMENT_TYPES = "types/assessment/all";
	private static final String ALL_TRAINER_ROLES = "types/trainer/role/all";
	private static final String ALL_PANEL_STATUS = "types/panelstatus/all";
	private static final String ALL_INTERVIEW_FORMATS = "types/interviewformat/all";
	private static final String OTHER = "Other";


	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.controllers.TypeController#allSkillTypes()
	 */
	@Test
	public void allSkillTypes() throws Exception {

		log.debug("API Testing allSkillTypes at baseUrl  " + baseUrl);

		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		//get request for all skills
		.when().get(baseUrl + ALL_SKILL_TYPES)
		//assertions
		.then().assertThat().statusCode(200)
			.body("$", hasItems("J2EE", ".NET", "SDET", "BPM", OTHER))
			.body("$", not(hasItems("Cukes", "Parfait")));
	
	}
	
	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.controllers.TypeController#allTrainingTypes()
	 */
	@Test
	public void allTrainingTypes(){
		
		log.debug("API Testing allTrainingTypes at baseUrl  " + baseUrl);

		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		//get request for all training types
		.when().get(baseUrl + ALL_TRAINING_TYPES)
		//assertions
		.then().assertThat().statusCode(200)
			.body("$", hasItems("Revature", "Corporate", "University", OTHER))
			.body("$", not(hasItems("Gherkins", "Parfait")));
		
		
	}
	
	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.controllers.TypeController#allTrainingStatusTypes()
	 */
	@Test
	public void allTrainingStatusTypes(){
		
		log.debug("API Testing allTrainingStatusTypes at baseUrl  " + baseUrl);

		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		//get request for all training status
		.when().get(baseUrl + ALL_TRAINING_STATUS)
		//assertions
		.then().assertThat().statusCode(200)
			.body("$", hasItems("Signed", "Selected", "Training",
					"Marketing", "Confirmed", "Employed", "Dropped"))
			.body("$", not(hasItems("Gherkins", "Tofu Steak")));
		
		
	}
	
	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.controllers.TypeController#allNoteTypes()
	 */
	@Test
	public void allNoteTypes(){
		
		log.debug("API Testing allNoteTypes at baseUrl  " + baseUrl);

		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		//get request for all note types
		.when().get(baseUrl + ALL_TYPE_NOTES)
		//assertions
		.then().assertThat().statusCode(200)
			.body("$", hasItems("TRAINEE", "BATCH", "QC_TRAINEE","QC_BATCH"))
			.body("$", not(hasItems("Pudding a la mode", "Tofu Steak")));
		
	}
	
	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.controllers.TypeController#allQCStatusTypes()
	 */
	@Test
	public void allQCStatusTypes(){
		
		log.debug("API Testing allQCStatusTypes at baseUrl  " + baseUrl);

		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		//get request for all QCStatusTypes
		.when().get(baseUrl + ALL_QCSTATUS_TYPES)
		//assertions
		.then().assertThat().statusCode(200)
			.body("$", hasItems("Superstar", "Good", "Average","Poor"))
			.body("$", not(hasItems("Pudding a la mode", "Crepe")));
		
		
	}
	
	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.controllers.TypeController#allAssessmentTypes()
	 */
	@Test
	public void allAssessmentTypes(){
		
		log.debug("API Testing allAssessmentTypes at baseUrl  " + baseUrl);

		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		//get request for all assessment types
		.when().get(baseUrl + ALL_ASSESSMENT_TYPES)
		//assertions
		.then().assertThat().statusCode(200)
			.body("$", hasItems("Exam", "Verbal", "Project",OTHER))
			.body("$", not(hasItems("Sundae", "Mousse")));
		
		
	}
	
	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.controllers.TypeController#allTrainerRoles()
	 */
	@Test
	public void allTrainerRoles(){
		
		
		log.debug("API Testing allTrainingRoles at baseUrl  " + baseUrl);

		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		//get request for all trainer roles
		.when().get(baseUrl + ALL_TRAINER_ROLES)
		//assertions
		.then().assertThat().statusCode(200)
			.body("$", hasItems("ROLE_VP", "ROLE_QC", "ROLE_TRAINER","ROLE_STAGING", "ROLE_INACTIVE"))
			.body("$", not(hasItems("Che", "Mousse")));
		
		
	}
	
	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.controllers.TypeController#allPanelStatus()
	 */
	@Test
	public void allPanelStatus() {		
		log.debug("API Testing allPanelStatus at baseUrl  " + baseUrl);

		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(baseUrl + ALL_PANEL_STATUS).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body("size()", is(PanelStatus.values().length)).
			body("$", hasItems("Pass", "Repanel")).
			body("$", not(hasItems("Green Bay", "Packers")));		
	}
	
	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.controllers.TypeController#allInterviewFormat()
	 */
	@Test
	public void allInterviewFormat() {		
		log.debug("API Testing allInterviewFormat at baseUrl  " + baseUrl);

		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(baseUrl + ALL_INTERVIEW_FORMATS).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body("size()", is(InterviewFormat.values().length)).
			body("$", hasItems("Skype", "Phone")).
			body("$", not(hasItems("Green Bay", "Packers")));		
	}
}
