package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.caliber.beans.Address;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.SkillType;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.beans.TrainingType;
import com.revature.caliber.data.TraineeDAO;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * API testing at the RESTful service message layer using REST Assured. All API
 * tests must be ignored in the initial build until the app is deployed into the
 * test environment.
 * 
 * @Author Patrick Walsh
 *
 */
public class TrainingAPITest extends AbstractAPITest {

	private static final Logger log = Logger.getLogger(TrainingAPITest.class);
	private static final String LEAD_TRAINER = "Lead Trainer";
	private static final String SENIOR_TRAINER = "Senior Trainer";
	private static final String NAME = "Patrick Walsh";
	private static final String EMAIL = "patrick.walsh@revature.com";
	private static final String BATCH = "batch";
	
	/*
	 * Training API endpoints
	 */
	private String findByEmail = "training/trainer/byemail/patrick.walsh@revature.com/";
	private String findAllDroppedTrainees = "all/trainee/dropped";
	private String findAllTraineesInBatch = "all/trainee";
	private String getAllBatches = "qc/batch/all";
	private String getAllCurrentBatches = "vp/batch/all/current";
	private String updateBatch = "all/batch/update";
	private String createTrainer = "vp/trainer/create";
	private String updateTrainer = "vp/trainer/update";
	private String makeInactive = "vp/trainer/delete";
	private String getAllTrainersTitles = "vp/trainer/titles";
	private String getAllTrainers= "all/trainer/all";
	private String createLocationTest = "vp/location/create";
	private String updateLocationTest = "vp/location/update";
	private String getAllLocationTest = "all/location/all";
	private String removeLocationTest = "vp/location/delete";
	private String reactivateLocationTest = "vp/location/reactivate";
	private String createBatch = "all/batch/create";
	private String deleteBatch = "all/batch/delete/{id}";
	private String findAllBatchesByTrainer = "trainer/batch/all";
	private String createWeek = "trainer/week/new/{batchId}";
	private String findCommonLocations = "all/locations";
	private String search = "all/trainee/search/{searchTerm}";
	private Address newYorkAddress = new Address(1, "65-30 Kissena Blvd, CEP Hall 2", "Queens", "NY", "11367","Tech Incubator at Queens College", true);

	@Autowired
	private TraineeDAO traineeDAO;
	
	@Autowired
	public void setTraineeDAO(TraineeDAO traineeDAO) {
		this.traineeDAO = traineeDAO;
	}
	
	@Test
	public void searchTest() throws Exception {
		String searchTerm = "Lau";
		Set<Trainee> result = new HashSet<>();
		List<Trainee> traineeByEmail = traineeDAO.findByEmail(searchTerm);
		result.addAll(traineeByEmail);
		List<Trainee> traineeByName = traineeDAO.findByName(searchTerm);
		result.addAll(traineeByName);
		List<Trainee> traineeBySkypeId = traineeDAO.findBySkypeId(searchTerm);
		result.addAll(traineeBySkypeId);
		log.debug("API Testing serach at baseUrl  " + baseUrl + search);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(baseUrl + search, searchTerm).
		then().assertThat().
			body("size()", is(result.size())).
			statusCode(HttpStatus.OK_200);
		for(Trainee t: result) {
			log.debug(t);
		}
	}
	@Test
	public void searchNoContentTest() throws Exception {
		String searchTerm = "!!!!!!!!";
		Set<Trainee> result = new HashSet<>();
		List<Trainee> traineeByEmail = traineeDAO.findByEmail(searchTerm);
		result.addAll(traineeByEmail);
		List<Trainee> traineeByName = traineeDAO.findByName(searchTerm);
		result.addAll(traineeByName);
		List<Trainee> traineeBySkypeId = traineeDAO.findBySkypeId(searchTerm);
		result.addAll(traineeBySkypeId);
		log.debug("API Testing serach at baseUrl  " + baseUrl + search);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(baseUrl + search, searchTerm).
		then().assertThat().
			statusCode(HttpStatus.NO_CONTENT_204);
	}
	/**
	 * Tests method:
	 * @see com.revature.caliber.services.TrainingService.findByEmail(@RequestParam(required = true) Integer trainerID)
	 * 
	 * finds a trainer by email
	 **/
	@Test
	public void findByEmail() throws Exception {
		Trainer expected = new Trainer(NAME, LEAD_TRAINER, EMAIL,
				TrainerRole.ROLE_VP);
		expected.setTrainerId(1);
		log.debug("API Testing findTrainerByEmail at baseUrl  " + baseUrl + findByEmail);
		given().header(AUTH, accessToken).contentType(ContentType.JSON).when().get(baseUrl + findByEmail)
				.then().assertThat().statusCode(200);
		log.debug("API Testing findTrainerByEmail at " + baseUrl + findByEmail);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + findByEmail).then().assertThat().statusCode(200)
				.body(matchesJsonSchema(new ObjectMapper().writeValueAsString(expected)));
		
	}
	
	/**
	 * Tests method:
	 * @see com.revature.caliber.services.TrainingService.findAllDroppedByBatch(@RequestParam(required = true) Integer batch)
	 * Makes the call, checks all entities in the result set, fails if any do not have TrainingStatus.Dropped.
	 * @throws Exception 
	 */
	@Test
	public void findAllDroppedByBatchTest() throws Exception{
		
		log.debug("API Testing findAllDroppedByBatchTest at baseUrl  " + baseUrl + findAllDroppedTrainees);
		Response actual = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).queryParam(BATCH, "2050").when()
				.get(baseUrl + findAllDroppedTrainees).then().assertThat().statusCode(200).extract().response();
		Trainee[] resultSet = actual.as(Trainee[].class);
		boolean success = true;
		for(Trainee dude : resultSet){
			if(dude.getTrainingStatus() != TrainingStatus.Dropped){
				success = false;
			}
		}
		assertTrue(success);
	}
	
	/**
	 * Tests method:
	 * @see com.revature.caliber.services.TrainingService.findAllTraineesByBatch(@RequestParam(required = true) Integer batch)
	 * Sees if the call pulls all 6 trainees from the batch 2050. Makes sure no trainees are found from a non-existing batch.
	 * @throws Exception 
	 */
	@Test
	public void findAllByBatchTest(){
		log.debug("API Testing findAllByBatchTest at baseUrl  " + baseUrl + findAllTraineesInBatch);
		Response rs = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).queryParam(BATCH, "2050").when()
				.get(baseUrl + findAllTraineesInBatch).then().assertThat().statusCode(200).extract().response();
		Trainee[] resultSet = rs.as(Trainee[].class);
		assertEquals(6,resultSet.length);
		rs = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).queryParam(BATCH, "-245").when()
				.get(baseUrl + findAllTraineesInBatch).then().assertThat().statusCode(200).extract().response();
		resultSet = rs.as(Trainee[].class);
		assertEquals(resultSet.length, 0);
	}
	
	/**
	 * Tests method:
	 * @see com.revature.caliber.services.TrainingService.getAllBatches()
	 * Makes the call, checks to see if it pulled all 6 batches.
	 */
	@Test
	public void getAllBatchesTest(){
		log.debug("API Testing getAllBatchesTest at baseUrl  " + baseUrl + getAllBatches);
		Response actual = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + getAllBatches).then().assertThat().statusCode(200).extract().response();
		Batch[] resultSet = actual.as(Batch[].class);
		assertEquals(13,resultSet.length);
	}
	/**
	 * Tests method:
	 * @see com.revature.caliber.services.TrainingService.getAllCurrentBatches()
	 * Makes the call. Checks if all 3 current batches are found.
	 */
	@Test
	public void getAllCurrentBatchesTest(){
		log.debug("API Testing getAllCurrentBatchesTest at baseUrl  " + baseUrl + getAllCurrentBatches);
		Response actual = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + getAllCurrentBatches).then().assertThat().statusCode(200).extract().response();
		Batch[] resultSet = actual.as(Batch[].class);		
		assertEquals(10, resultSet.length); 
	}
	
	/**
	 * Tests method:
	 * @see com.revature.caliber.services.TrainingService.updateBatch(@Valid @RequestBody Batch batch)
	 * This test will break if getAllBatches doesn't work, or returns 0 results.
	 * Finds a batch. Changes the location. Updates the batch. Pulls all batches again. Checks for a batch
	 * with the updated location. Changes the batch location back to the original value. (For back when we
	 * didn't have the in-memory database working. You can take it out if you want.)
	 */
	@Test
	public void updateBatchTest(){
		//Pull a batch from the database...
		Response resultBatchSet = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + getAllBatches).then().extract().response();
		Batch[] resultSet = resultBatchSet.as(Batch[].class);
		Batch holderBatch = resultSet[0];
		//Save original location...
		String original = holderBatch.getLocation();
		//Change to a test location...
		holderBatch.setLocation("In the testing zone!");
		//Try to update the batch...
		log.debug("API Testing the updateBatch at baseUrl  " + baseUrl + updateBatch);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).body(holderBatch).when()
				.put(baseUrl + updateBatch).then().assertThat().statusCode(200);
		//See if it actually changed in the database...
		resultBatchSet = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + getAllBatches).then().extract().response();
		resultSet = resultBatchSet.as(Batch[].class);
		boolean success = false;
		for(Batch batch : resultSet){
			if("In the testing zone!".equals(batch.getLocation())){
				success = true;
			}
		}
		assertTrue(success);
		//Change it back...
		holderBatch.setLocation(original);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).body(holderBatch).when()
		.put(baseUrl + updateBatch).then().assertThat().statusCode(200);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.controllers.TrainingController.createTrainer(Trainer)
	 * @throws Exception
	 */
	@Test
	public void createTrainer() throws Exception {
		Trainer expected = new Trainer("RolledBack", SENIOR_TRAINER, "don.wels23hy@revature.com",
				TrainerRole.ROLE_TRAINER);
		log.debug("API Testing createTrainer at baseUrl  " + baseUrl + createTrainer);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
				.body(new ObjectMapper().writeValueAsString(expected)).when().post(baseUrl + createTrainer).then()
				.assertThat().statusCode(201).body(matchesJsonSchema(new ObjectMapper().writeValueAsString(expected)));
	}
	
	/**
	 * Tests methods:
	 * @see com.revature.caliber.controllers.TrainingController.updateTrainer(Trainer)
	 * @throws Exception
	 */
	@Test
	public void updateTrainer() throws Exception {
		Trainer expected = new Trainer("Newwer Trainer", SENIOR_TRAINER, "don.welshy123@revature.com",
				TrainerRole.ROLE_TRAINER);
		expected.setTrainerId(3);
		log.debug("API Testing updateTrainer at baseUrl  " + baseUrl + updateTrainer);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
				.body(new ObjectMapper().writeValueAsString(expected)).when().put(baseUrl + updateTrainer).then()
				.assertThat().statusCode(204);
	}
	/**
	 * Tests methods:
	 * @see com.revature.caliber.controllers.TrainingController.makeInactive(Trainer)
	 * @throws Exception
	 */
	@Test
	public void makeInactive() throws Exception {
		Trainer expected = new Trainer("Dan Pickles", LEAD_TRAINER, "pjw6193@hotmail.com", TrainerRole.ROLE_VP);
		expected.setTrainerId(2);
		log.debug("API Testing makeInactiv at baseUrl  " + baseUrl + makeInactive);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
				.body(new ObjectMapper().writeValueAsString(expected)).when().delete(baseUrl + makeInactive).then()
				.assertThat().statusCode(204);
	}
	/**
	 * Tests methods:
	 * @see com.revature.caliber.controllers.TrainingController.getAllTrainersTitles()
	 * @throws Exception
	 */
	@Test
	public void getAllTrainersTitles() throws Exception {
		log.debug("API Testing getAllTrainersTitles at baseUrl  " + baseUrl + getAllTrainersTitles);
		Response titles = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + getAllTrainersTitles).then().assertThat().statusCode(200).extract().response();
		assertTrue("Test titles",
				titles.asString().contains(SENIOR_TRAINER) & titles.asString().contains("Senior Technical Manager")
						& titles.asString().contains(LEAD_TRAINER) & titles.asString().contains("Trainer")
						& titles.asString().contains("Technology Manager"));
	}
	/**
	 * Tests methods:
	 * @see com.revature.caliber.controllers.TrainingController.getAllTrainers()
	 * @throws Exception revisit when we have transient tests to test more specific
	 * trainers.
	 * @throws Exception
	 * revisit when we have transient tests to test more specific trainers.
	 */
	@Test
	public void getAllTrainers() throws Exception {
		Trainer expected = new Trainer(NAME, LEAD_TRAINER, EMAIL,
				TrainerRole.ROLE_VP);
		expected.setTrainerId(1);
		log.debug("API Testing getAllTrainers at baseUrl  " + baseUrl + getAllTrainers);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + getAllTrainers).then().assertThat().statusCode(200)
				.body(containsString(new ObjectMapper().writeValueAsString(expected)));
		log.debug("Get all trainers passed!!");
	}
	
	/**
	 * 
	 * 
	 * Tests the following methods:
	 * 
	 * @see com.revature.caliber.controllers.TrainingController.deleteBatch()
	 * 
	 * Verifies that a batch can be deleted/deactivated by asserting the status code returned is
	 * 200 when the method is called.
	 * 
	 * In this case, the example batch being tested is the one with id of 2201 in the path param.
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteBatch() throws Exception {
		log.debug("API Testing deleteBatch at baseUrl  " + baseUrl + deleteBatch);
		given().spec(requestSpec).header(AUTH, accessToken).pathParam("id", 2003).contentType(ContentType.JSON).when()
				.delete(baseUrl + deleteBatch).then().assertThat().statusCode(204);
		
	}
	
	/**
	 * 
	 * Tests the following methods:
	 * 
	 * @see com.revature.caliber.controllers.TrainingController.createBatch()
	 * 
	 * 
	 * Verifies that a batch can be created when the method is called, by asserting that
	 * the status code returned is 201.
	 * 
	 * @throws Exception
	 */
	@Test
	public void createBatch() throws Exception {
		Trainer expectedTrainer = new Trainer(NAME, LEAD_TRAINER, EMAIL,
				TrainerRole.ROLE_VP);
		expectedTrainer.setTrainerId(1);
		Batch expected = new Batch("Create Controller TrainingAPI Test", expectedTrainer,
				java.sql.Date.valueOf(LocalDate.now().toString()), java.sql.Date.valueOf(LocalDate.now().toString()),
				"Some Location");
		log.debug("API Testing createBatch at " + baseUrl + createBatch);

		expected.setSkillType(SkillType.J2EE);
		expected.setTrainingType(TrainingType.Revature);
		
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
				.body(new ObjectMapper().writeValueAsString(expected)).when()
				// request to create a batch
				.post(baseUrl + createBatch)
				// assertions
				.then().assertThat().statusCode(201);
	}
	
	
	/**
	 * 
	 * Tests the following methods:
	 *  
	 * @see com.revature.caliber.controllers.TrainingController.findAllBatchesByTrainer()
	 *  
	 * Verifies that batches can be found by specific trainer, by returning a status code of 200 after calling
	 * the method in the TrainingController.
	 * 
	 * @throws Exception
	 */
	@Test
	public void findAllBatchesByTrainer() throws Exception {
		log.debug("API Testing findAllBatchesByTrainer at baseUrl  " + baseUrl + findAllBatchesByTrainer);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
				// get request for all batches by a trainer
				.when().get(baseUrl + findAllBatchesByTrainer)
				// assertions
				.then().assertThat().statusCode(200);
	}
	
	/**
	 * 
	 * Tests the following methods:
	 * 
	 * @see com.revature.caliber.controllers.TrainingController.createWeek()
	 * 
	 * Verifies that a week can be created by calling the method, and then returning a status code of 201.
	 * 
	 * @throws Exception
	 */
	@Test
	public void createWeek() throws Exception {
		log.debug("API Testing createWeek at baseUrl  " + baseUrl + createWeek);
		given().spec(requestSpec).header(AUTH, accessToken).pathParam("batchId", 2201).contentType(ContentType.JSON)
				// request to create a week for a specific batch
				.when().post(baseUrl + createWeek)
				// assertions
				.then().assertThat().statusCode(201);
	}
	
	
	/**
	 *  
	 *  
	 *  Tests the following methods:
	 *  
	 * @see com.revature.caliber.controllers.TrainingController.findCommonLocations()
	 *  
	 *  Verifies that batches with common locations can be found by asserting the status code returned
	 *  to be 200.
	 *  
	 * @throws Exception
	 */
	@Test
	public void findCommonLocations() throws Exception {
		log.debug("API Testing findCommonLocations at baseUrl  " + baseUrl + findCommonLocations);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
				// request to get a list of common locations
				.when().get(baseUrl + findCommonLocations)
				// assertions
				.then().assertThat().statusCode(200);
	}

	/**
	 * Tests methods:
	 * 
	 * @see com.revature.controllers.TrainingController#createLocation
	 */
	@Test
	public void createLocationTest() {
		Address location = new Address(1, "299 CherryStreet", "FruityCity", "FL", "55555", "Revature", true);
		location.setAddressId(20);
		log.debug("API Testing createLocation at baseUrl " + baseUrl + createLocationTest);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).body(location).when()
				.post(baseUrl + createLocationTest).then().assertThat().statusCode(201);
	}

	/**
	 * Tests methods:
	 * 
	 * @see com.revature.controllers.TrainingController#updateLocation
	 */
	@Test
	public void updateLocationTest() throws JsonProcessingException {
		Address location = newYorkAddress;
		location.setState("PA");

		log.debug("API Testing updateLocation at baseUrl " + baseUrl + updateLocationTest);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).body(location).when()
				.put(baseUrl + updateLocationTest).then().assertThat().statusCode(204);
		
	}

	/**
	 * Tests methods:
	 * 
	 * @see com.revature.controllers.TrainingController#getAllLocations
	 */
	@Test
	public void getAllLocationsTest() throws JsonProcessingException {
		Address expect1 = newYorkAddress;
		Address expect2 = new Address(2, "11730 Plaza America Drive, 2nd Floor", "Reston", "VA", "20190",
				"Revature LLC", true);
		log.debug("API Testing updateLocation at baseUrl " + baseUrl + getAllLocationTest);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + getAllLocationTest).then().assertThat().statusCode(200)
				.body(matchesJsonSchema(new ObjectMapper().writeValueAsString(expect1)))
				.body(matchesJsonSchema(new ObjectMapper().writeValueAsString(expect2)));
	}	 

	/**
	 * Tests methods:
	 * 
	 * @see com.revature.controllers.TrainingController#removeLocation
	 * @see com.revature.controllers.TrainingController#reactivateLocation
	 */
	@Test
	public void removeAndReactivateLocationTest() {
		Address location = newYorkAddress;
		location.setActive(false);

		log.debug("API Testing removeLocation at baseUrl " + baseUrl + removeLocationTest);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).body(location).when()
				.delete(baseUrl + removeLocationTest).then().assertThat().statusCode(204);
		
		location.setActive(true);
		log.debug("API Testing reactivateLocation at baseUrl " + baseUrl + reactivateLocationTest);
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).body(newYorkAddress)
				.when().put(baseUrl + reactivateLocationTest).then().assertThat().statusCode(204);
	}
}

