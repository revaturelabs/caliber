package com.revature.caliber.tasks;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.data.TrainerDAO;
import com.revature.caliber.revpro.rest.models.AuthenticationTokenResponse;
import com.revature.caliber.security.models.RevProToken;
import com.revature.caliber.security.models.RevProUser;

@Service
public class RevProAuth {

	@Autowired
	TrainerDAO trainerDao;
	
	protected static RevProToken accessTokenJson = new RevProToken();

	@Value("#{systemEnvironment['REVPRO_LOGIN_URL']}")
	private String revProLoginUrl;
	@Value("#{systemEnvironment['CALIBER_API_USERNAME']}")
	private String username;
	@Value("#{systemEnvironment['CALIBER_API_PASSWORD']}")
	private String password;
	
	private static Logger log = Logger.getLogger(RevProAuth.class);
	
	public boolean setUser() {
		
		boolean userSet = false;
		
		try {
			log.info("User: "+username+" Pass: "+password);
			accessTokenJson.setAccessToken(login());
			RevProUser revProUser = new RevProUser();
			revProUser.setUserId(username);
			Trainer trainer = trainerDao.findByEmail(username);
			revProUser.setCaliberUser(trainer);
			revProUser.setRole(trainer.getTier().name());
			log.info("User role: "+revProUser.getRole());
			revProUser.setToken(accessTokenJson);
			Authentication auth = new PreAuthenticatedAuthenticationToken(revProUser, revProUser.getUserId(),
	                revProUser.getAuthorities());
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
		log.info("Clearing context");
		SecurityContextHolder.clearContext();
	}
	
	/**
	 * Logs into salesforce to obtain an accessToken
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 * @author Patrick Walsh
	 */
	private String login() throws JsonMappingException, IOException {
		// construct JSON for login request body
		String request = "{" + "  \"password\" : \"" + password + "\"," + "  \"userName\" : \"" + username + "\"" + "}";
		// prepare HTTP request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(request, headers);
		RestTemplate http = new RestTemplate();

		try {
			ResponseEntity<AuthenticationTokenResponse> response = http.exchange(revProLoginUrl, HttpMethod.POST,
					entity, AuthenticationTokenResponse.class);
			log.info(response.getBody().getData());
			return response.getBody().getData();
		} catch (Exception e) {
			log.info("Failed to login " + username + " with cause: " + e.getMessage());
			return null;
		}
	}
}
