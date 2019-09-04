package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.caliber.CaliberTest;
import com.revature.caliber.security.models.RevProToken;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Abstract class used to be extended to API testing classes. Initializes by
 * authenticating with the Salesforce API so developers need only place
 * accessToken variable as Authorization header in their HTTP requests.
 * 
 * Requires appropriate credentials to be stored as environmental variables. The
 * credentials must also match a user in the Caliber database.
 * 
 * @author Patrick Walsh
 *
 */
public abstract class AbstractAPITest extends CaliberTest implements InitializingBean {

	/**
	 * Salesforce access token to be used in Authorization HTTP header
	 */
	protected static String accessToken = "Auth ";
	protected static RevProToken accessTokenJson;
	protected static final String AUTH = "Authorization";
	protected static String jsessionid;
	protected static RequestSpecification requestSpec;

	protected static String baseUrl = System.getenv("CALIBER_API_SERVER");
	private static String username = System.getenv("CALIBER_API_USERNAME");
	private static String password = System.getenv("CALIBER_API_PASSWORD");
	private static String clientId = System.getenv("SALESFORCE_CLIENT_ID");
	private static String clientSecret = System.getenv("SALESFORCE_CLIENT_SECRET");
	private static String accessTokenUrl = "https://test.salesforce.com/services/oauth2/token";
	protected static String authHeader = "Authorization";
	
	private static final String PASS = "password";
	
	private static final Logger log = Logger.getLogger(AbstractAPITest.class);

	/**
	 * Start up the tomcat server once when initializing any APITest class
	 */
	static {
		SpringApplication.run(EmbeddedTomcatAPIServer.class);
	}

	/**
	 * Executed after initializing an AbstractAPI bean, allowing access to
	 * autowired vairables
	 * 
	 * Uses the helper methods populateDatabase() and teardownDatabase() to have
	 * the default user to login with
	 * 
	 * Uses the helper method login() to log into salesforce to obtain an access
	 * token
	 */
	public void afterPropertiesSet() {
		// only login with Salesforce once
		if ("Auth ".equals(accessToken)) {
			try {
				populateDatabase(); // Add the default trainer to the database
									// in order to login
				login();
				log.debug("Logging into Caliber for API testing at: " + baseUrl + "authenticated_token");
				Response response = given().param("salestoken", accessTokenJson).redirects().allowCircular(true)
						.get(baseUrl + "authenticated_token");
				log.debug("Token: " + accessToken);
				String sessionCookie = response.getSessionId();
				String roleCookie = response.getCookie("role");
				log.debug("JSESSIONID: " + sessionCookie + "\nRole: " + roleCookie + "\nStatus: "
						+ response.getStatusCode());
				requestSpec = new RequestSpecBuilder().addCookie("JSESSIONID", sessionCookie)
						.addCookie("role", roleCookie).build();
				tearDownDatabase(); // remove database data to prepare it for
									// the first test
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	/**
	 * Logs into salesforce to obtain an accessToken
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static void login() throws JsonMappingException, IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		log.debug("logging into URL   " + accessTokenUrl);
		HttpPost post = new HttpPost(accessTokenUrl);
		List<NameValuePair> parameters = new ArrayList<>();
		parameters.add(new BasicNameValuePair("grant_type", PASS));
		parameters.add(new BasicNameValuePair("client_secret", clientSecret));
		parameters.add(new BasicNameValuePair("client_id", clientId));
		parameters.add(new BasicNameValuePair("username", username));
		parameters.add(new BasicNameValuePair(PASS, password));
		post.setEntity(new UrlEncodedFormEntity(parameters));
		HttpResponse response = httpClient.execute(post);
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
			accessTokenJson = mapper.readValue(response.getEntity().getContent(),
					RevProToken.class); // actual
			accessToken += accessTokenJson.getAccessToken();
			log.debug("Accessing Salesforce API using token:  " + accessToken);
		}catch(Exception e){
			log.error(e);
			httpClient = HttpClientBuilder.create().build();
			log.debug("logging into URL   " + accessTokenUrl);
			post = new HttpPost(accessTokenUrl);
			parameters = new ArrayList<>();
			parameters.add(new BasicNameValuePair("grant_type", PASS));
			parameters.add(new BasicNameValuePair("client_secret", clientSecret));
			parameters.add(new BasicNameValuePair("client_id", clientId));
			parameters.add(new BasicNameValuePair("username", username));
			parameters.add(new BasicNameValuePair(PASS, password));
			post.setEntity(new UrlEncodedFormEntity(parameters));
			response = httpClient.execute(post);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
			log.error(mapper.readValue(response.getEntity().getContent(),
					JsonNode.class)); 
		}
	}

	/**
	 * runs the setup.sql on the database
	 * 
	 * @throws SQLException
	 */
	private void populateDatabase() throws SQLException {
		log.debug("Populating database with setup.sql");
		Connection con = jdbcTemplate.getDataSource().getConnection();
		EncodedResource resource = new EncodedResource(
				new FileSystemResource(new File("src/test/resources/setup.sql")));

		ScriptUtils.executeSqlScript(con, resource);
		log.debug("Sql script executed");
		con.close();
	}

	/**
	 * runs the teardown.sql on the database
	 * 
	 * @throws SQLException
	 */
	private void tearDownDatabase() throws SQLException {
		log.debug("Tearingdown database with teardown.sql");
		Connection con = jdbcTemplate.getDataSource().getConnection();
		EncodedResource resource = new EncodedResource(
				new FileSystemResource(new File("src/test/resources/teardown.sql")));

		ScriptUtils.executeSqlScript(con, resource);
		log.debug("Sql script executed");
		con.close();
	}
}
