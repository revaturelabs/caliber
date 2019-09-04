package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.revature.caliber.beans.Category;
import com.revature.caliber.data.CategoryDAO;

import io.restassured.http.ContentType;
public class CategoryAPITest extends AbstractAPITest {
	
	private static final Logger log = Logger.getLogger(CategoryAPITest.class);
	
	private static final String AUTHORIZATION = "Authorization";
	private static final String SKILL = "skillCategory";
	private static final String ACTIVE = "active";
	private static final String LINUX_CATEGORY = "Super Linux2";
	
	private static final String VP_CATEGORY = "vp/category/";
	
	CategoryDAO dao;
	
	@Autowired
	public void setDao(CategoryDAO dao) {
		this.dao = dao;
	}

	//Takes the first category and checks to make sure it is Java, and that it is active.
	@Test
	public void findCategoryByIdTest() throws JsonProcessingException{
		log.debug("Testing findCategoryById function from CategoryController");
		given().spec(requestSpec).header(AUTHORIZATION, accessToken).contentType(ContentType.JSON).when()
		.get(baseUrl + "category/1").then().assertThat()
		.statusCode(200).body(SKILL, equalTo("Java"),ACTIVE, equalTo(true));
	}
	// Returns a JSON object which will only contain Categories that are active. Also checks to make sure there are no inactive categories(false) in the JSON.
	@Test
	public void findAllActiveTest(){
		log.debug("Testing findAllCategories function from CategoryController");
		int size = dao.findAllActive().size();
		given().spec(requestSpec).header(AUTHORIZATION, accessToken).contentType(ContentType.JSON).when()
		.get(baseUrl + "category/all").then().assertThat()
		.statusCode(200).body("body.size",is(size),ACTIVE,not(hasItem(false))); 
	}
	//I create a false object otherwise there is no false object and it fails.
	//Returns a JSON object which will return all Categories even if they are inactive. 
	@Test
	public void findAllTest(){
		log.debug("Testing findAll function from CategoryController");
		Category category2 = new Category(LINUX_CATEGORY,false);
		given().spec(requestSpec).header(AUTHORIZATION, accessToken).contentType(ContentType.JSON).body(category2).when()
		.post(baseUrl + VP_CATEGORY).then().assertThat()
		.statusCode(201);
		given().spec(requestSpec).header(AUTHORIZATION, accessToken).contentType(ContentType.JSON).when()
		.get(baseUrl + VP_CATEGORY).then().assertThat()
		.statusCode(200).body(ACTIVE,hasItem(false));
	}
	// Takes a random String and replaces the Category name from skill number 45.
	@Test
	public void updateCategoryTest() throws Exception{
		log.debug("Testing updateCategory function from CategoryController");
		String[] theList = new String[]{"The Room","Trolls2","Grabage pail kids","Little panda fighter"};
		int randomtheList = ThreadLocalRandom.current().nextInt(0, 3);
		Category category = dao.findOne(45);
		category.setSkillCategory(theList[randomtheList]);
		category.setActive(true);
		given().spec(requestSpec).header(AUTHORIZATION, accessToken).contentType(ContentType.JSON).body(category).when()
		.put(baseUrl + VP_CATEGORY + "update").then().assertThat()
		.statusCode(200).and().body(SKILL,equalTo(category.getSkillCategory()),ACTIVE,equalTo(category.isActive()));
	}
	//Makes a new category and adds it to the category list.
	@Test
	public void saveCategoryTest() throws JsonProcessingException{
		log.debug("Testing saveCategory function from CategoryController");
		Category category2 = new Category(LINUX_CATEGORY,false);
		given().spec(requestSpec).header(AUTHORIZATION, accessToken).contentType(ContentType.JSON).body(category2).when()
		.post(baseUrl + VP_CATEGORY).peek().then().assertThat()
		.statusCode(201).and().body(SKILL,equalTo(LINUX_CATEGORY));
	}
}
