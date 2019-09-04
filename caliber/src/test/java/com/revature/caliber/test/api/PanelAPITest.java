package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.revature.caliber.beans.InterviewFormat;
import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.data.PanelDAO;
import com.revature.caliber.data.TraineeDAO;
import com.revature.caliber.data.TrainerDAO;
import com.revature.caliber.services.PanelService;
import com.revature.caliber.validator.ValidPanel;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * @author Nathan Koszuta
 * @author Connor Monson
 * @author Allan Jones
 * @author Matt 'Spring Data' Prass
 */

public class PanelAPITest extends AbstractAPITest {

	private static final Logger log = Logger.getLogger(PanelAPITest.class);

	private static final String GET_ALL_PANELS_URL = baseUrl + "panel/all";
	private static final String GET_PANEL_BY_ID_URL = baseUrl + "panel/{panelId}";
	private static final String GET_TRAINEE_PANELS_URL = baseUrl + "panel/trainee/{traineeId}";
	private static final String GET_ALL_REPANELS_URL = baseUrl + "panel/repanel/all";
	private static final String GET_ALL_RECENT_REPANLS_URL = baseUrl + "panel/repanel/recent";
	private static final String CREATE_PANEL_URL = baseUrl + "panel/create";
	private static final String DELETE_PANEL_URL = baseUrl + "panel/delete/{panelId}";
	private static final String UPDATE_PANEL_URL = baseUrl + "panel/update";
	private static final String GET_PANEL_BY_BATCH_URL = baseUrl + "all/reports/batch/{batchId}/panel-batch-all-trainees";
	private static final String SIZE = "size()";
	private static final String EXPECTED = "expected= ";
	
	@Autowired
	private PanelDAO panelDAO;
	@Autowired
	private BatchDAO batchDAO;
	@Autowired
	private TraineeDAO traineeDAO;
	@Autowired
	private TrainerDAO trainerDAO;
	@Autowired
	private PanelService panelService;
	
	@BeforeClass
	public static void logIfValidationFails() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	/**
	 * Tests creation. Asserts that the status code 201 is returned meaning the
	 * creation was successful
	 */
	@Test
	public void testCreate201() {
		log.debug("Creating a new Panel type");
		Panel panel = new Panel();
		panel.setFormat(InterviewFormat.Phone);
		panel.setPanelRound(1);
		panel.setStatus(PanelStatus.Pass);
		panel.setTrainee(traineeDAO.findOne(1));
		panel.setPanelist(trainerDAO.findOne(1));
	
		
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
			body(panel).
		when()
			.post(CREATE_PANEL_URL).
		then().assertThat().
			statusCode(HttpStatus.CREATED_201);
	}

	/**
	 * Tests updating an existing panel by changing the duration. Asserts what is
	 * returned is the same as what we sent in the request
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testUpdate() throws Exception {
		log.debug("Updating an panel");
		Panel expected = panelDAO.findOne(40);
		expected.setDuration("100 hours");
		
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
			body(expected).
		when().
			put(UPDATE_PANEL_URL).
		then().assertThat().
			body("id", is(expected.getId())).
			body("duration", is(expected.getDuration())).
			statusCode(HttpStatus.OK_200);
	}

	/**
	 * Tests delete and asserts the appropriate status code is returned (204)
	 */
	@Test
	public void testDelete() {
		log.debug("Deleting an panel");
		int panelId = panelDAO.findAll().get(0).getId();
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			delete(DELETE_PANEL_URL, panelId).
		then().assertThat().
			statusCode(HttpStatus.NO_CONTENT_204);
	}
	
	/**
	 * Tests deletion of a panel that doesn't exist.
	 * Asserts a 404 Not Found status is returned.
	 */
	@Test
	public void testDeletePanel404() {
		log.debug("Deleting an panel that doesn't exist");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			delete(DELETE_PANEL_URL, -1).
		then().assertThat().
			statusCode(HttpStatus.NOT_FOUND_404);
	}
	
	/**
	 * Tests get all panels when no panels exist.
	 * Asserts a 204 No Content status is returned.
	 */
	@Test
	public void testGetAllPanels204() {
		log.debug("Get all panels, no content...");
		List<Panel> allPanels = panelDAO.findAll();
		for (Panel p : allPanels) {
			panelDAO.delete(p.getId());
		}
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_PANELS_URL).
		then().assertThat().
			statusCode(HttpStatus.NO_CONTENT_204);
		log.debug("testGetAllPanels204 succeeded!!!");
	}
	
	/**
	 * Tests get all panels.
	 * Asserts correct number of panels returned
	 * and a 200 OK status is returned.
	 */
	@Test
	public void testGetAllPanels200() {
		log.debug("Get all panels, OK...");
		
		int expected = panelDAO.findAll().size();
		
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_PANELS_URL).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body(SIZE, is(expected));
		log.debug("testGetAllPanels200 succeeded!!!");
	}
	
	/**
	 * Tests get panel by id when panel doesn't exist.
	 * Asserts 404 Not Found status is returned.
	 */
	@Test
	public void testGetPanelById404() {
		log.debug("Get panel by id, resource not found...");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_PANEL_BY_ID_URL, -1).
		then().assertThat().
			statusCode(HttpStatus.NOT_FOUND_404);
		log.debug("testGetPanelById404 succeeded!!!");
	}
	
	/**
	 * Tests get panel by id.
	 * Asserts 200 OK status is returned.
	 */
	@Test
	public void testPanelById200() {
		log.debug("Get panel by id, OK...");
		Panel p = panelDAO.findAll().get(0);
		log.debug("panel= " + p);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_PANEL_BY_ID_URL, p.getId()).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body("id", is(p.getId()));
		log.debug("testPanelById200 succeeded!!!");
	}
	
	/**
	 * Tests get panels by trainee when trainee doesn't exist.
	 * Asserts 404 Not Found status is returned.
	 */
	@Test
	public void testGetPanelsByTrainee404() {
		log.debug("Get all trainee panels, no content...");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_TRAINEE_PANELS_URL, -1).
		then().assertThat().
			statusCode(HttpStatus.NOT_FOUND_404);
		log.debug("testGetPanelsByTrainee404 succeeded!!!");
	}
	
	/**
	 * Tests get panels by trainee when trainee doesn't have any.
	 * Asserts 204 No Content status is returned.
	 */
	@Test
	public void testGetPanelsByTrainee204() {
		log.debug("Get all trainee panels, no content...");
		Trainee t = new Trainee("Test", null, "test@test.com", batchDAO.findAll().get(0));
		traineeDAO.save(t);
		int expected = panelDAO.findAllByTrainee(t.getTraineeId()).size();
		log.debug(EXPECTED + expected);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_TRAINEE_PANELS_URL, t.getTraineeId()).
		then().assertThat().
			statusCode(HttpStatus.NO_CONTENT_204);
		log.debug("testGetPanelsByTrainee204 succeeded!!!");
	}
	
	/**
	 * Tests get panels by trainee.
	 * Asserts correct number of panels returned 
	 * and a 200 OK status is returned.
	 */
	@Test
	@Ignore // doesn't work PJW
	public void testGetPanelsByTrainee200() {
		log.debug("Get all trainee panels, OK...");
		List<Panel> panels = panelDAO.findAll();
		int traineeId = -1;
		if (!panels.isEmpty()) {
			traineeId = panels.get(0).getTrainee().getTraineeId();
		}
		int expected = panelDAO.findAllByTrainee(traineeId).size();
		log.debug(EXPECTED + expected + ", for trainee id " + traineeId);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_TRAINEE_PANELS_URL, traineeId).
		then().assertThat().
			body(SIZE, is(expected)).
			statusCode(HttpStatus.OK_200);
		log.debug("testGetPanelsByTrainee200 succeeded!!!");
	}

	/**
	 * Get all repanels when no panels exist.
	 * Assert 204 No Content status is returned.
	 */
	@Test
	public void testGetAllRepanels204() {
		log.debug("Get all repanels, no content...");
		for (Panel p : panelDAO.findAllRepanel()) {
			panelDAO.delete(p.getId());
		}
		int expected = panelDAO.findAllRepanel().size();
		log.debug(EXPECTED + expected);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_REPANELS_URL).
		then().assertThat().
			statusCode(HttpStatus.NO_CONTENT_204);
		log.debug("testGetAllRepanels204 succeeded!!!");
	}

	/**
	 * Get all repanels.
	 * Assert correct number of panels returned
	 * and a 200 OK status is returned.
	 */
	@Test
	public void testGetAllRepanels200() {
		log.debug("Get all repanels, OK...");
		int expected = panelDAO.findAllRepanel().size();
		log.debug(EXPECTED + expected);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_REPANELS_URL).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body(SIZE, is(expected));
		log.debug("testGetAllRepanels200 succeeded!!!");
	}
	
	@Test
	@Ignore // doesn't work PJW
	public void testGetPanelByBatch204() {
		log.debug("Get all trainee panels by batch, OK...");
		int expected = panelService.getBatchPanels(2050).size();
		log.debug("expected= " + expected);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_RECENT_REPANLS_URL).
		then().assertThat().
			statusCode(HttpStatus.NO_CONTENT_204);
		log.debug("testGetAllRepanels204 succeeded!!!");
	}

	/**
	 * Get all recent repanels.
	 * Assert correct number of panels returned
	 * and a 200 OK status is returned.
	 */
	@Test
	@Ignore // doesn't work PJW
	public void testGetAllRecentRepanels200() {
		log.debug("Get all recent repanels, OK...");
		int expected = 21;
		log.debug(EXPECTED + expected);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_RECENT_REPANLS_URL).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body(SIZE, is(expected));
		log.debug("testGetAllRepanels200 succeeded!!!");
	}

	@Test
	@Ignore // doesn't work PJW
	public void testGetPanelByBatch200() {
		log.debug("Get all trainee panels by batch, OK...");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_PANEL_BY_BATCH_URL, 1000).
		then().assertThat().
			statusCode(HttpStatus.OK_200);
		log.debug("testGetPanelByBatch200 succeeded!!!");
	}
}
