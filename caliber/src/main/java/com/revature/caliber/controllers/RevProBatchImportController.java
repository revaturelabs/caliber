package com.revature.caliber.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.services.RevProBatchImportService;

@RestController
@PreAuthorize("isAuthenticated()")
public class RevProBatchImportController {

	private static final Logger log = Logger.getLogger(RevProBatchImportController.class);

	@Autowired
	private RevProBatchImportService service;

	/**
	 * Gets all the relevant batches

	 * @return Batches in JSON
	 */
	@RequestMapping(value = "/all/batch/import", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public Iterable<Batch> getAllReleventBatches() {
		log.debug("Fetching all batches");
		return service.getAllRelevantBatches();
	}

	/**
	 * Gets all trainees for a given batch

	 * @return Batches in JSON
	 */
	@RequestMapping(value = "/all/trainee/import", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public Iterable<Trainee> getAllTraineesFromBatch(@RequestParam String resourceId) {
		log.debug("Fetching trainees from batch: " + resourceId);
		return service.getAllTraineesFromBatch(resourceId);
	}
	
}
