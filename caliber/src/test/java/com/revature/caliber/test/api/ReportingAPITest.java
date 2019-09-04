package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.util.concurrent.ThreadLocalRandom;

import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.services.ReportingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ReportingAPITest extends AbstractAPITest{
	
	private static final Logger log = Logger.getLogger(ReportingAPITest.class);
	ReportingService service;
	BatchDAO dao;
	
	
	//int BatchId, int TraineeId, int Week
	private static int[] traineeValue = {2200, 5503, 1};

	private static String traineeReports = "all/reports/trainee/{traineeId}/radar-trainee-overall";
	private static String batchReports= "all/reports/batch/{batchId}/overall/radar-batch-overall";
	private static String batchAverage = "all/assessments/average/{batchId}/{week}";
	private static String batchAssessmentCategories = "all/assessments/categories/batch/{batchId}/week/{week}";
	private static String batchOverallLine = "all/reports/batch/{batchId}/overall/line-batch-overall";
	private static String currBatchLines = "all/reports/dashboard";
	
	@Autowired
	public void setDao(BatchDAO dao) {
		this.dao = dao;
	}
	@Autowired
	public void setService(ReportingService service) {
		this.service = service;
	}
	
	/**
	* This test takes a skill All, a training All, and the date. This date can be any date before this instance of time, and 1970.
	* If the date doesn't match up i.e changing 2015 to 2016 the day and weekday would be off giving 83 ish.But because the Web app
	* always calls that second it is valid and returns the Avg number.
	*/
	@Test
	public void testGetBatchComparisonAvg(){
		String date = "Wed Sep 21 15:48:45 EDT 2016";
		log.debug("Here it is testGetBatchComparisonAvg Test");
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		.when().get(baseUrl+"all/reports/compare/skill/(All)/training/(All)/date/"+date).then()
		.assertThat().statusCode(200);
	}
	
	
	@Test
	public void testGetBatchWeekPieChart() throws JsonProcessingException {
		log.debug("TESTING getBatchWeekPieChart");		
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
			.when().get(baseUrl + "all/reports/batch/2200/week/1/pie")
			.then().assertThat().statusCode(200);
	}
	
	/**
	 * 
	 * Tested Method:
	 * 		com.revature.caliber.controllers.ReportingController.getPieChartCurrentWeekQCStatus(Integer batchId)
	 * Check that a batch with no notes returns a 404
	 * Since there are no current batches to test for a 200 OK, we don't test for it . . . yet
	 * 
	 * */
	@Test
	public void testGetPieChartCurrentWeekQCStatus() {
		log.debug("TESTING getPieChartCurrentWeekQCStatus");
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
			.when().get(baseUrl + "all/reports/batch/2200/pie")
			.then().assertThat().statusCode(404);
	}
	
	/**
	 *
	 *Tested Method:
	 * 		com.revature.caliber.controllers.ReportingController.getAllBatchesCurrentWeekQCStackedBarChart()
	 *Tests to make sure response returns a 200
	 *
	 **/
	@Test
	public void testGetAllBatchesCurrentWeekQCStackedBarChart() {
		log.debug("TESTING getAllBatchesCurrentWeekQCStackedBarChart");
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
			.when().get(baseUrl + "all/reports/batch/week/stacked-bar-current-week")
			.then().assertThat().statusCode(200);
	}
	
	/**
	 * 
	 * Tested Method:
	 * 		com.revature.caliber.controllers.ReportingController.getBatchWeekAvgBarChart(int batchId, int week)
	 * Test to see a valid request returns a 200
	 * Test to see an invalid batch returns an empty json object
	 * 
	 * */
	@Test
	public void testGetBatchWeekAvgBarChart() {
		log.debug("TESTING getBatchWeekAvgBarChart");
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
			.when().get(baseUrl + "all/reports/batch/2200/week/1/bar-batch-week-avg")
			.then().assertThat().statusCode(200);
		//Bad Batch number in the uri (223300) should return empty JSON object
		String actual = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
			.when().get(baseUrl + "all/reports/batch/223300/week/1/bar-batch-week-avg")
			.thenReturn().body().asString();
		String expected = "{}";
		assertEquals(expected, actual);
	}
	
	/**
	 *
	 * Tested Method:
	 * 		com.revature.caliber.controllers.ReportingController.getBatchWeekSortedBarChart(int batchId, int week)
	 * Test to see a valid request returns a 200
	 * Test to see an invalid batch returns an empty json object
	 *
	 **/
	@Test
	public void testGetBatchWeekSortedBarChart() {
		log.debug("TESTING getBatchWeekSortedBarChart");
		log.debug("TESTING getBatchWeekAvgBarChart");
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
			.when().get(baseUrl + "all/reports/batch/2200/week/1/bar-batch-weekly-sorted")
			.then().assertThat().statusCode(200);
		//Bad Batch number in the uri (223300) should return empty JSON object
		String actual = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
			.when().get(baseUrl + "all/reports/batch/223300/week/1/bar-batch-weekly-sorted")
			.thenReturn().body().asString();
		String expected = "{}";
		assertEquals(expected, actual);
	}
	
	/**
	 *  Validates the existence of the trainee's overall radar chart,
	 *  given the trainee's ID. 
	 * @throws Exception
	 */
	@Test
	public void getTraineeOverallRadarChart() throws Exception{
		log.debug("Validate trainee's overall radar chart");
		Map<String, Double> expected = new HashMap<>();
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(baseUrl + traineeReports, 5465).
		then().
			assertThat().statusCode(200).
		and().
			body(matchesJsonSchema(new ObjectMapper().writeValueAsString(expected)));
	}

	/**
	  * Test method:
	  * 	com.revature.caliber.controllers.ReportingController
	  * 		.getBatchOverallTraineeBarChart()(Integer batchId, Integer traineeId)
	  * 
	  * Test that getBatchOverallTraineeBarChart returns status code 200 for a non-null object.
	  * 	status code 404 will return for a empty object
	  */
	@Test
	public void testGetBatchOverallTraineeBarChart() throws Exception{
		log.debug("GetBatchOverallTraineeBarChart Test");
		
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		.when().get(baseUrl + "all/reports/batch/{batchId}/overall/trainee/{traineeId}/bar-batch-overall-trainee", 
				traineeValue[0], traineeValue[1])
		.then().assertThat().statusCode(200);
	}
	
	/**
	  * Tests method:
	  * 	com.revature.caliber.controllers.ReportingController
	  * 		.getBatchOverallBarChart()(Integer batchId)
	  * 
	  * Test that getBatchOverallBarChart returns status code 200 for a non-null object.
	  * 	status code 404 will return for a empty object
	  */ 
	@Test
	public void testGetBatchOverallBarChart() throws Exception{
		log.debug("GetBatchOverallBarChart Test");
		
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		.when().get(baseUrl + "all/reports/batch/{batchId}/overall/bar-batch-overall", traineeValue[0])
		.then().assertThat().statusCode(200);
	}
	
	/**
	 *  Validates the existence of the batch overall radar chart,
	 *  given the batch ID. 
	 * @throws Exception
	 */
	@Test
	public void testGetBatchOverallRadarChart() throws Exception{
		log.debug("Validate batch's overall radar chart");
		Map<String, Double> expected = new HashMap<>();
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(baseUrl + batchReports, 2150).
		then().
			assertThat().statusCode(200).
		and().
			body(matchesJsonSchema(new ObjectMapper().writeValueAsString(expected)));
	}
	
	/**
	 *  Validates the existence of the radar chart for all trainees
	 *  in a batch, given the batch ID. 
	 * @throws Exception
	 */
	@Test
	public void testGetBatchAllTraineesRadarChart() throws Exception{
		log.debug("Validate batch's overall radar chart");
		Map<String, Map<String, Double>> expected = new HashMap<>();
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(baseUrl + batchReports, 2150).
		then().
			assertThat().statusCode(200).
		and().
			body(matchesJsonSchema(new ObjectMapper().writeValueAsString(expected)));
	}
	
	/**
	 *  Validates the value of the batch average for the week,
	 *  given the batch ID and the week number
	 *  @throws Exception
	 */
	@Test
	public void testGetBatchWeekAverageValue() throws Exception{
		log.debug("Validate retrieval of batch's overall average in a week");
		
		Double expected = new Double(80.26d);
		Double actual = 
			given().
				spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
			when().
				get(baseUrl + batchAverage, 2150, 1).
			then().
				assertThat().statusCode(200).
			and().
				extract().as(Double.class);
		
		assertEquals(expected, actual, 0.01d);
	}
	
	/**
	 *  Validates the value(s) of the technologies the batch learned
	 *  for the week, given the batch ID and the week number
	 *  @throws Exception
	 */
	@Test
	public void testGetTechnologiesForTheWeek() throws Exception{
		log.debug("Validate retrieval of batch's technologies learned in a week");
		
		Set<String> expected = new HashSet<>();
		expected.add("AWS");
		expected.add("Hibernate");
		
		Set<String> actual = new ObjectMapper().readValue(
			given().
				spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
			when().
				get(baseUrl + batchAssessmentCategories, 2201, 5).
			then().
				contentType(ContentType.JSON).assertThat().statusCode(200).
			and().
				extract().response().asString(), new TypeReference<Set<String>>() {});
		
		assertEquals(expected, actual);
	}
	
	/**
	  * Tests method:
	  * 	com.revature.caliber.controllers.ReportingController
	  * 	.getBatchWeekTraineeBarChart()(Integer batchId,Integer weekId,  Integer traineeId)
	  * 
	  * Test that getBatchWeekTraineeBarChart returns status code 200 for a non-null object.
	  * 	status code 404 will return for a empty object
	  */ 
	@Test
	public void testGetBatchWeekTraineeBarChart() throws Exception {
		log.debug("GetBatchWeekTraineeBarChart Test");
		
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		.when().get(baseUrl + "all/reports/batch/{batchId}/overall/trainee/{traineeId}/bar-batch-overall-trainee", 
				traineeValue[0], traineeValue[1])
		.then().assertThat().statusCode(200);
	}

	//Tests if the returned JSON matches the expected values returned from a Map
	@Test
	public void testgetTraineeOverallLineChart(){
		log.debug("testgetTraineeOverallLineChart Test");
		//The arrays are set up so the nth elemnt in batchId matches the nth element in traineeId
		Integer[] batchId = new Integer[]{2200,2050,2150};
		Integer[] traineeId = new Integer[]{5503,5350,5467};
		int random = ThreadLocalRandom.current().nextInt(0, 3);
		//Gets the maps from the service
		Map<Integer, Double[]> theMap = service.getTraineeOverallLineChart(batchId[random],traineeId[random]);
		//Maps the double array values to an ArrayList
		ArrayList<Double[]> targetList = new ArrayList<>(theMap.values());
		//Stores the array body as a string 
		Response response = given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		.when().get(baseUrl+"all/reports/batch/{batchId}/overall/trainee/{traineeId}/line-trainee-overall",batchId[random],traineeId[random]);
		JSONObject responseJson = new JSONObject(response.getBody().asString());;
		//For the length of the body finds the double array and compares the 1st and second values of the double array
		for(int i =1; i <= responseJson.length(); i++){
			JSONArray values = responseJson.getJSONArray(Integer.toString(i));
			for(int j = 0; j < values.length(); j++) {
				double value = values.getDouble(j);
				assertEquals(Math.round(value*1000d)/1000d, Math.round(targetList.get(i-1)[j]*1000d)/1000d,0.0001);
			}
		}
	}

	/**
	  * Test method:
	  * 	com.revature.caliber.controllers.ReportingController
	  * 	.getTraineeUpToWeekLineChart()(int batchId, int Week, int traineeId)
	  * 
	  * Test that getTraineeUpToWeekLineChart returns status code 200 for a non-null object.
	  * 	status code 404 will return for a empty object
	  */  
	@Test
	public void testGetTraineeUpToWeekLineChart() throws Exception {
		log.debug("GetTraineeUpToWeekLineChart Test");
		
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON)
		.when().get(baseUrl + "all/reports/batch/{batchId}/week"
				+ "/{week}/trainee/{traineeId}/line-trainee-up-to-week", 
				traineeValue[0], traineeValue[2], traineeValue[1])

		.then().assertThat().statusCode(200);
	}

	@Test
	public void testGetBatchOverallLineChart() throws Exception {
		log.debug("Get Overall Line Chart for batch 2200");
		Double[] overall = new Double[]{75.7, 89.2, 77.9, 80.6, 77.8, 83.5, 83.5, 87.4, 77.9};
		Map<Integer, Double> expected = new HashMap<>();
		for(int i = 0; i < overall.length; i++){
			expected.put(i+1, overall[i]);
		}
		Map<Integer, Double> actual = new ObjectMapper().readValue(
				given().
					spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
				when().
					get(baseUrl + batchOverallLine, 2200).
				then().
					contentType(ContentType.JSON).assertThat().statusCode(200).
				and().
					extract().response().asString(), new TypeReference<Map<Integer, Double>>() {});
		for(Map.Entry<Integer, Double> entry : actual.entrySet()){
			assertEquals(entry.getValue(), expected.get(entry.getKey()), 0.1d);
		}
	}

	@Test
	public void testGetCurrentBatchesLineChart() throws Exception {
		log.debug("Getting All Current Batches Line charts");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(baseUrl + currBatchLines).
		then().
			assertThat().statusCode(200);
	}

}
