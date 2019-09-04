package com.revature.caliber.tasks;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.data.TrainerDAO;
import com.revature.caliber.security.models.RevProToken;
import com.revature.caliber.security.models.RevProUser;

@Deprecated
public class SalesforceAuth {
	
	@Autowired
	TrainerDAO trainerDao;
	
	protected static RevProToken accessTokenJson = new RevProToken();
	protected static final String AUTH = "Authorization";
	protected static String jsessionid;

	protected static String baseUrl = System.getenv("CALIBER_API_SERVER");
	private static String username = System.getenv("CALIBER_API_USERNAME");
	private static String password = System.getenv("CALIBER_API_PASSWORD");
	private static String salesforceId = System.getenv("SALESFORCE_CLIENT_ID");
	private static String salesforceSecret = System.getenv("SALESFORCE_CLIENT_SECRET");
	private static String salesforceUrl = System.getenv("SALESFORCE_LOGIN_URL");
	private static String accessTokenUri = "services/oauth2/token";
	protected static String authHeader = "Authorization";
	
	private static final String PASS = "password";
	
	private static Logger log = Logger.getLogger(SalesforceAuth.class);
	
	public boolean setUser() {
		
		boolean userSet = false;
		
		try {
			log.debug("Logging into Salesforce "+salesforceId+" "+salesforceSecret);
			log.debug("User: "+username+" Pass: "+password);
			login();
			RevProUser salesforceUser = new RevProUser();
			salesforceUser.setUserId(username);
			Trainer trainer = trainerDao.findByEmail("patrick.walsh@revature.com");
			salesforceUser.setCaliberUser(trainer);
			salesforceUser.setRole(trainer.getTier().name());
			log.debug("SalesforceUser role: "+salesforceUser.getRole());
			salesforceUser.setToken(accessTokenJson);
			Authentication auth = new PreAuthenticatedAuthenticationToken(salesforceUser, salesforceUser.getUserId(),
	                salesforceUser.getAuthorities());
	        SecurityContextHolder.getContext().setAuthentication(auth);
	        
	        if(((RevProUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken() != null) {
	        	userSet = true;
	        }
	        
		} catch (IOException e) {
			log.error("Error accesing token: "+ e);
			return userSet;
		}
		
		return userSet;
	}
	
	public void clearUser() {
		SecurityContextHolder.clearContext();
	}
	
	/**
	 * Logs into salesforce to obtain an accessToken
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 * @author Patrick Walsh
	 */
	private static void login() throws JsonMappingException, IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		log.debug("logging into URL   " + salesforceUrl + accessTokenUri);
		HttpPost post = new HttpPost(salesforceUrl + accessTokenUri);
		List<NameValuePair> parameters = new ArrayList<>();
		parameters.add(new BasicNameValuePair("grant_type", PASS));
		parameters.add(new BasicNameValuePair("client_secret", salesforceSecret));
		parameters.add(new BasicNameValuePair("client_id", salesforceId));
		parameters.add(new BasicNameValuePair("username", username));
		parameters.add(new BasicNameValuePair(PASS, password));
		post.setEntity(new UrlEncodedFormEntity(parameters));
		HttpResponse response = httpClient.execute(post);
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
			accessTokenJson = mapper.readValue(response.getEntity().getContent(),
					RevProToken.class); // actual
			log.debug("Accessing Salesforce API using token:  " + accessTokenJson.getAccessToken());
		}catch(Exception e){
			log.error(e);
			httpClient = HttpClientBuilder.create().build();
			post = new HttpPost(salesforceUrl + accessTokenUri);
			parameters = new ArrayList<>();
			parameters.add(new BasicNameValuePair("grant_type", PASS));
			parameters.add(new BasicNameValuePair("client_secret", salesforceSecret));
			parameters.add(new BasicNameValuePair("client_id", salesforceId));
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
}
