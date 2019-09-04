package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.caliber.beans.Assessment;
import com.revature.caliber.beans.AssessmentType;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Category;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.data.CategoryDAO;

import io.restassured.http.ContentType;

public class AssessmentAPITest extends AbstractAPITest {

	private static final Logger log = Logger.getLogger(AssessmentAPITest.class);
	private String createAssessment = "trainer/assessment/create";
	private String deleteAssessment = "trainer/assessment/delete/{assessment}";
	private String updateAssessment = "trainer/assessment/update";
	private String findAssessment = "trainer/assessment/{batchId}/{week}";
	private BatchDAO batchDAO;
	private CategoryDAO catDAO;

	@Autowired
	public void setBatchDAO(BatchDAO batchDAO) {
		this.batchDAO = batchDAO;
	}

	@Autowired
	public void setCatDAO(CategoryDAO catDAO) {
		this.catDAO = catDAO;
	}

	/**
	 * Tests the find uri for a batch that does not exist and ensures that the
	 * expected amount of assessments are returned
	 */
	@Test
	public void testFind404() {
		log.debug("Finding two batches that don't exist");
		given().spec(requestSpec).header(AUTH, accessToken).when().get(baseUrl + findAssessment, 1, 1).then()
				.assertThat().statusCode(404);
		given().spec(requestSpec).header(AUTH, accessToken).when().get(baseUrl + findAssessment, 2200, 10).then()
				.assertThat().statusCode(404);
	}

	/**
	 * Tests the find uri for a batch that does exist and ensures that the
	 * expected amount of assessments are returned
	 */
	@Test
	public void testFind200() {
		log.debug("Finding two batches that does exist");
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).when()
				.get(baseUrl + findAssessment, 2200, 1).then().assertThat().statusCode(200).and().body("size()", is(3));
	}

	/**
	 * Tests creation. Asserts that the status code 201 is returned meaning the creation was successful
	 */
	@Test
	public void testCreate201() {
		log.debug("Creating a new Assessment type");
		Assessment assessment = new Assessment("This title doesn't matter", batchDAO.findOne(2200), 100,
				AssessmentType.Verbal, 100, catDAO.findOne(1));
		given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).body(assessment).when()
				.post(baseUrl + createAssessment).then().assertThat().statusCode(201);
	}

	/**
	 * Tests updating an existing assessment by changing the raw score. 
	 * Asserts whats returned is the same as what we sent in the request
	 * @throws Exception
	 * 
	 */
	@Test
	public void testUpdate() throws Exception {
		log.debug("Updating an assessment");
		Batch batch = batchDAO.findOne(2200);
		Category category = catDAO.findOne(1);
		Assessment expected = new Assessment(null, batch, 100, AssessmentType.Exam, 1, category);
		expected.setAssessmentId(2057);
		expected.setRawScore(100000);
		Assessment actual = new ObjectMapper()
				.readValue(
						given().spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).body(expected)
								.when().put(baseUrl + updateAssessment).then().contentType(ContentType.JSON)
								.assertThat().statusCode(200).and().extract().response().asString(),
						new TypeReference<Assessment>() {});
		assertEquals(expected.getRawScore(), actual.getRawScore());
	}

	/**
	 * Tests delete and asserts the appropriate status code is returned (204)
	 */
	@Test
	public void testDelete() {
		log.debug("Deleting an assessment");
		given().spec(requestSpec).header(AUTH, accessToken).when().delete(baseUrl + deleteAssessment, 2050).then()
				.assertThat().statusCode(204);
	}
}
