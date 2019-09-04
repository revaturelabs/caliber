package com.revature.caliber.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.security.models.RevProUser;
import com.revature.caliber.services.SalesforceService;

@Deprecated
//@PreAuthorize("isAuthenticated()")
//@CrossOrigin(origins = "http://ec2-54-163-132-124.compute-1.amazonaws.com")
public class SalesforceController {

	private static final Logger log = Logger.getLogger(SalesforceController.class);

	@Autowired
	private SalesforceService salesforceService;

	public void setSalesforceService(SalesforceService salesforceService) {
		this.salesforceService = salesforceService;
	}

	/**
	 * Delete when we're done with development Used to grab access_token for running
	 * local tests of Salesforce API
   
	 * @return
	 */
	@RequestMapping(value="/salesforce/token", method=RequestMethod.GET)
	public String getSalesforceToken() {
		log.debug("Getting access_token for testing purposes only!");
		return ((RevProUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getToken().getAccessToken();
	}

	/**
	 * Gets all the relevent batches

	 * @return Batches in JSON
	 */
	@RequestMapping(value = "/all/batch/import", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public Iterable<Batch> getAllReleventBatches() {
		return salesforceService.getAllRelevantBatches();
	}

	/**
	 * Gets all trainees for a given batch

	 * @return Batches in JSON
	 */
	@RequestMapping(value = "/all/trainee/import", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public Iterable<Trainee> getAllTraineesFromBatch(@RequestParam String resourceId) {
		return salesforceService.getAllTraineesFromBatch(resourceId);
	}

	/**
	 * Gets all the relevant batches or response String. Testing purpose only.

	 * @return Batches in JSON
	 */
	@RequestMapping(value = "/all/batch/import/log", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String logBatches(@RequestParam(required=false) String resourceId) {
		if(resourceId == null)
			return salesforceService.logBatches();
		else
			return salesforceService.logBatches(resourceId);
	}
	
}
