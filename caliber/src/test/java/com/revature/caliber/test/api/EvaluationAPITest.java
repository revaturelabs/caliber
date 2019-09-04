package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.caliber.beans.Assessment;
import com.revature.caliber.beans.AssessmentType;
import com.revature.caliber.beans.Category;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Note;
import com.revature.caliber.beans.NoteType;
import com.revature.caliber.beans.QCStatus;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.data.AssessmentDAO;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.data.CategoryDAO;
import com.revature.caliber.data.GradeDAO;
import com.revature.caliber.data.NoteDAO;
import com.revature.caliber.data.TraineeDAO;
import com.revature.caliber.data.TrainerDAO;

import io.restassured.http.ContentType;

public class EvaluationAPITest extends AbstractAPITest {

	@Autowired
	GradeDAO gradeDAO;
	@Autowired
	TraineeDAO traineeDAO;
	@Autowired
	BatchDAO batchDAO;
	@Autowired
	TrainerDAO trainerDAO;
	@Autowired
	CategoryDAO categoryDAO;
	@Autowired
	AssessmentDAO assessmentDAO;
	@Autowired
	NoteDAO noteDAO;
	private static final Logger log = Logger.getLogger(EvaluationAPITest.class);

	private static final int TEST_TRAINEE_ID = 5537;
	private static final int TEST_WEEK = 1;

	private static final String FIND_TRAINEE_NOTE = "trainer/note/trainee/";
	private static final String FIND_QCTRAINEE_NOTE = "qc/note/trainee/";
	private static final String CREATE_GRADE = "trainer/grade/create";
	private static final String UPDATE_GRADE = "trainer/grade/update";
	private static final String FIND_QCBATCH_NOTES = "qc/note/batch/2201/5";
	private static final String GET_ALL_QCTRAINEE_NOTES = "qc/note/trainee/2201/5";
	private static final String FIND_ALL_BATCH_NOTES = "vp/note/batch/2100/2";
	private static final String GET_ALL_QCTRAINEE_OVERALL_NOTES = "qc/note/trainee/5529";
	private static final String FIND_ALL_TRAINEE_NOTES = "all/notes/trainee/5529";
	private static final String FIND_BATCH_NOTES = "trainer/note/batch/2100/2";

	/**
	 * Testing createGrade - create a new grade and check for HTTP 200 success
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#createGrade(Grade)
	 */
	@Test
	@Ignore
	public void createGrade() throws JsonProcessingException {
		log.trace("API Testing createGrade");

		// make an assessment to store the grade in
		Category category = categoryDAO.findAllActive().get(0);
		Trainee trainee = traineeDAO.findOne(TEST_TRAINEE_ID);
		Assessment assessment = new Assessment("Testing Test", trainee.getBatch(), 200, AssessmentType.Exam, TEST_WEEK,
				category);
		assessmentDAO.save(assessment);

		// setting the expected value as a grade
		Grade expected = new Grade(assessment, trainee, new Date(), 99.99);

		given().header(AUTH, accessToken).spec(requestSpec).contentType(ContentType.JSON)
				.body(new ObjectMapper().writeValueAsString(expected)).when().post(baseUrl + CREATE_GRADE).then()
				.assertThat().statusCode(201);

		assertTrue(gradeDAO.findByAssessment(assessment.getAssessmentId()).size() > 0);
	}

	/**
	 * testing updateGrade - create a new grade and check for HTTP 204
	 * no_content
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#updateGrade(Grade)
	 */
	@Test
	@Ignore
	public void updateGrade() throws Exception {
		log.trace("API Testing updateGrade");

		// get expected value as a grade
		Grade expected = gradeDAO.findByTrainee(TEST_TRAINEE_ID).get(0);

		// change grade
		expected.setScore(55.55);

		given().header(AUTH, accessToken).spec(requestSpec).contentType(ContentType.JSON)
				.body(new ObjectMapper().writeValueAsString(expected)).when().post(baseUrl + UPDATE_GRADE).then()
				.assertThat().statusCode(204);

		assertEquals(expected, gradeDAO.findByTrainee(TEST_TRAINEE_ID).get(0));
	}

	/**
	 * Create note
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#createNote(@Valid @RequestBody
	 *      Note note)
	 * 
	 */
	@Ignore
	@Test
	public void createNote() throws Exception {
		log.trace("API test createNote");

		// create a new Note object
		String createNote = "note/create";
		Note expected = new Note();
		expected.setContent("This is a test note");
		expected.setWeek((short) 2);
		expected.setBatch(null);
		expected.setType(NoteType.TRAINEE);
		expected.setQcStatus(QCStatus.Poor);
		expected.setMaxVisibility(TrainerRole.ROLE_TRAINER);
		expected.setTrainee(null);

		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
				.body(new ObjectMapper().writeValueAsString(expected)).when().post(baseUrl + createNote).then()
				.assertThat().statusCode(201);

		// check that new note is in database
		assertTrue(noteDAO.findAllQCTraineeNotes(null, 2).contains(expected));
	}

	/**
	 * Test by finding note, setting note content, and then posting new note
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#updateNote(@Valid @RequestBody
	 *      Note note)
	 * 
	 */
	@Test
	@Ignore
	public void updateNote() throws Exception {
		log.trace("API test updateNote");

		// find and change value of note to update
		String updateNote = "note/update";
		Note expected = noteDAO.findQCIndividualNotes(5529, 2).get(0);
		expected.setContent("This is a test notes");

		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
				.body(new ObjectMapper().writeValueAsString(expected)).when().post(baseUrl + updateNote).then()
				.assertThat().statusCode(201);

		// check that note was updated in database
		assertTrue(noteDAO.findQCIndividualNotes(5529, 2).contains(expected));
	}

	/**
	 * Find batch notes for trainee using batchId = 2100 and week 2
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#findBatchNotes(@PathVariable
	 *      Integer batchId, @PathVariable Integer week)
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void findBatchNotes() {
		log.trace("API test findBatchNotes");

		List<Note> notes = new ArrayList<>();
		notes = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + FIND_BATCH_NOTES).then().assertThat().statusCode(200).extract().body()
				.as(notes.getClass()); // extract body of response as a
										// List<Note>

		// check that some notes were returned
		assertTrue(!notes.isEmpty());
	}

	/**
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#findQCBatchNotes(@PathVariable
	 *      Integer batchId, @PathVariable Integer week)
	 */
	@Test
	public void findQCBatchNotes() {
		log.trace("API Testing findQCBatchNotes");

		Note note = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				// get request for QC BatchNotes
				.get(baseUrl + FIND_QCBATCH_NOTES).then().assertThat()
				// assertions
				.statusCode(200).extract().body().as(Note.class); // extract
																	// body of
																	// response
																	// as a Note

		// check that right note was returned
		assertEquals(6438, note.getNoteId());
	}

	/**
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#getAllQCTraineeNotes(@PathVariable
	 *      Integer batchId, @PathVariable Integer week)
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getAllQCTraineeNotes() {
		log.debug("API Testing getAllQCTraineeNotes");

		List<Note> notes = new ArrayList<>();
		notes = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				// request for get All QC Trainee Notes
				.get(baseUrl + GET_ALL_QCTRAINEE_NOTES).then().assertThat()
				// assertions to get all Trainee Notes
				.statusCode(200).extract().body().as(notes.getClass()); // extract
																		// body
																		// of
																		// response
																		// as a
																		// List<Note>

		// check that some notes were returned
		assertTrue(!notes.isEmpty());
	}

	/**
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#getAllQCTraineeOverallNotes(@PathVariable
	 *      Integer traineeId)
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getAllQCTraineeOverallNotes() {
		log.trace("API Testing getAllQCTraineeOverallNotes");

		List<Note> notes = new ArrayList<>();
		notes = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				// request for get All QC TraineeOverall Notes
				.get(baseUrl + GET_ALL_QCTRAINEE_OVERALL_NOTES).then().assertThat()
				// assertions
				.statusCode(200).extract().body().as(notes.getClass()); // extract
																		// body
																		// of
																		// response
																		// as a
																		// List<Note>

		// check that some notes were returned
		assertTrue(!notes.isEmpty());
	}

	/**
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#findAllBatchNotes(@PathVariable
	 *      Integer batchId, @PathVariable Integer week)
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void findAllBatchNotes() {
		log.trace("API Testing findAllBatchNotes");

		List<Note> notes = new ArrayList<>();
		notes = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				// request for get find All Batch Notes
				.get(baseUrl + FIND_ALL_BATCH_NOTES).then().assertThat()
				// assertions
				.statusCode(200).extract().body().as(notes.getClass()); // extract
																		// body
																		// of
																		// response
																		// as a
																		// List<Note>

		// check that some notes were returned
		assertTrue(!notes.isEmpty());
	}

	/**
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#findAllTraineeNotes(@PathVariable
	 *      Integer traineeId)
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void findAllTraineeNotes() {
		log.trace("API Testing findAllTraineeNotes");

		List<Note> notes = new ArrayList<>();
		notes = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				// request to find all individual notes
				.get(baseUrl + FIND_ALL_TRAINEE_NOTES).then().assertThat()
				// assertions
				.statusCode(200).statusCode(200).extract().body().as(notes.getClass()); // extract
																						// body
																						// of
																						// response
																						// as
																						// a
																						// List<Note>

		// check that some notes were returned
		assertTrue(!notes.isEmpty());
	}

	/**
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#findIndividualNotes(@PathVariable
	 *      Integer batchId, @PathVariable Integer week)
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void findIndividualNotes() {
		log.trace("API test findIndividualNotes");

		String findIndividualNotes = "trainer/note/trainee/2201/6";
		List<Note> notes = new ArrayList<>();
		notes = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + findIndividualNotes).then().assertThat().statusCode(200).extract().body()
				.as(notes.getClass()); // extract body of response as a
										// List<Note>

		// check that some notes were returned
		assertTrue(!notes.isEmpty());

		findIndividualNotes = "trainer/note/trainee/2200/3";
		notes = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + findIndividualNotes).then().assertThat().statusCode(200).extract().body()
				.as(notes.getClass()); // extract body of response as a Note

		// check that some notes were returned
		assertTrue(!notes.isEmpty());
	}

	/**
	 * Uses RESTAssured to test the findTraineeNote method in Evaluation
	 * Controller
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#findTraineeNote(Integer
	 *      traineeId, Integer week)
	 */
	@Test
	public void findTraineeNote() {
		given().spec(requestSpec).header(AUTH, accessToken) // authorization
				.contentType(ContentType.JSON) // data expected
				.when().get(baseUrl + FIND_TRAINEE_NOTE + TEST_TRAINEE_ID + "/for/" + TEST_WEEK).then().assertThat()
				.statusCode(200) // should return status of OK
				// make sure note recieved is the one expected
				.body("noteId", equalTo(6252))
				.body("content",
						equalTo("Technically good. A little quiet and less aware of appearance. A little disheveled during interview."))
				.body("type", equalTo("TRAINEE"));
	}

	/**
	 * Uses RESTAssured to test findQCTraineeNote in the Evaluation Controller
	 * 
	 * @see com.revature.caliber.controllers.EvaluationController#findQCTraineeNote(Integer
	 *      traineeId, Integer week)
	 */
	@Test
	public void findQCTraineeNote() {
		given().spec(requestSpec).header(AUTH, accessToken) // authorization
				.contentType(ContentType.JSON) // data expected
				.when().get(baseUrl + FIND_QCTRAINEE_NOTE + TEST_TRAINEE_ID + "/for/" + TEST_WEEK).then().assertThat()
				.statusCode(200) // should return status of OK
				// make sure note recieved is the one expected
				.body("noteId", equalTo(6359)).body("content", equalTo("Weak")).body("qcStatus", equalTo("Poor"))
				.body("type", equalTo("QC_TRAINEE"));
	}

}
