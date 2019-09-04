package com.revature.caliber.security.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.exceptions.NotAuthorizedException;
import com.revature.caliber.revpro.rest.models.AuthenticationTokenResponse;
import com.revature.caliber.security.models.RevProUser;

@Controller
@CrossOrigin(origins = "*")
public class BasicBootController {

	private static final Logger log = Logger.getLogger(BasicBootController.class);

	@Value("#{systemEnvironment['CALIBER_PROJECT_URL']}")
	private String redirectUrl;

	@Autowired
	private CaliberUserService caliberUserService;

	@Value("#{systemEnvironment['REVPRO_LOGIN_URL']}")
	private String revProLoginUrl;

	@Value("#{systemEnvironment['CALIBER_DEV_MODE']}")
	private boolean DEV_MODE;

	@Value("#{systemEnvironment['CALIBER_API_USERNAME']}")
	private String DEV_USERNAME;

	@Value("#{systemEnvironment['CALIBER_API_PASSWORD']}")
	private String DEV_PASSWORD;

	/**
	 * Go to the login page by default
	 * 
	 * @return
	 */
	@RequestMapping(value = "/")
	public String home(HttpServletResponse response) {
		if (DEV_MODE) {
			RevProUser user = (RevProUser) caliberUserService.loadUserByUsername(DEV_USERNAME);
			log.debug(user);
			user.getToken().setAccessToken(DEV_USERNAME);
			authorize(user.getCaliberUser(), user, response);
			return "redirect:" + redirectUrl;
		}
		return "login";
	}

	/**
	 * Go to the index page and load the AngularJS application
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/caliber", method = RequestMethod.GET)
	public String devHomePage(HttpServletRequest request, HttpServletResponse response) {
		return "index";
	}

	/**
	 * Authenticate the user with RevaturePro credentials
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/authenticated", method = RequestMethod.POST)
	public String authenticate(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String rawPassword = request.getParameter("pw");

		String token = loginToRevPro(username, rawPassword);
		if (token == null) {
			log.info("authentication failed for " + username);
			return "redirect:/";
		}
		RevProUser user = (RevProUser) caliberUserService.loadUserByUsername(username);
		log.debug(user);
		user.getToken().setAccessToken(token);
		authorize(user.getCaliberUser(), user, response);
		return "redirect:" + redirectUrl;
	}

	/**
	 * Login to the RevaturePro using admin login API
	 * 
	 * @param username
	 * @param password
	 * @return encrypted token
	 */
	private String loginToRevPro(String username, String password) {
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
			log.debug(response.getBody().getData());
			return response.getBody().getData();
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Failed to login " + username + " with cause: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Authorize the user with the Caliber application
	 * 
	 * @param trainer
	 * @param user
	 * @param password
	 * @param servletResponse
	 */
	private void authorize(Trainer trainer, RevProUser user, HttpServletResponse servletResponse) {
		log.info("Logged in user " + user.getEmail() + " now hasRole: " + user.getRole());
		// check if user is active
		if (user.getCaliberUser().getTier().equals(TrainerRole.ROLE_INACTIVE))
			throw new NotAuthorizedException();
		// store custom user Authentication obj in SecurityContext
		Authentication auth = new PreAuthenticatedAuthenticationToken(user, user.getUserId(), user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		servletResponse.addCookie(new Cookie("role", trainer.getTier().toString()));
		servletResponse.addCookie(new Cookie("id", Integer.toString(user.getCaliberUser().getTrainerId())));
	}
}
