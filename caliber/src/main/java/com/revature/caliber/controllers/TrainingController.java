package com.revature.caliber.controllers;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.beans.Address;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerTask;
import com.revature.caliber.beans.TrainerTaskCompletion;
import com.revature.caliber.security.models.RevProUser;
import com.revature.caliber.services.TrainingService;

/**
 * Services requests for Trainer, Trainee, and Batch information
 *
 * @author Patrick Walsh
 *
 */
@RestController
@PreAuthorize("isAuthenticated()")
@CrossOrigin(origins = "*")
public class TrainingController {

	private static final Logger log = Logger.getLogger(TrainingController.class);
	private TrainingService trainingService;

	@Autowired
	public void setTrainingService(TrainingService trainingService) {
		this.trainingService = trainingService;
	}

	/*
	 *******************************************************
	 * LOCATION SERVICES
	 *
	 *******************************************************
	 */
	/**
	 * Create location
	 *
	 * @param location
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/vp/location/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Address> createLocation(@Valid @RequestBody Address location) {
		log.debug("Saving location: " + location);
		trainingService.createLocation(location);
		return new ResponseEntity<>(location, HttpStatus.CREATED);
	}

	/**
	 * Update location
	 *
	 * @param location
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/vp/location/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Void> updateLocation(@Valid @RequestBody Address location) {
		log.debug("Updating location: " + location);
		trainingService.update(location);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Returns all locations from the database `
	 *
	 * @return
	 */
	@RequestMapping(value = "/all/location/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Address>> getAllLocations() {
		log.debug("Fetching all locations");
		List<Address> locations = trainingService.findAllLocations();
		return new ResponseEntity<>(locations, HttpStatus.OK);
	}

	/**
	 * Removes the location
	 *
	 * @param location
	 * @return response entity
	 */
	@RequestMapping(value = "/vp/location/delete", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Void> removeLocation(@Valid @RequestBody Address location) {
		log.debug("Deactivating location: " + location);
		trainingService.update(location);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Reactivates the location
	 *
	 * @param location
	 * @return response entity
	 */
	@RequestMapping(value = "/vp/location/reactivate", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Void> reactivateLocation(@Valid @RequestBody Address location) {
		log.debug("Updating location: " + location);
		trainingService.update(location);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/*
	 *******************************************************
	 * TRAINER SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * Create trainer
	 *
	 * @param trainer
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/vp/trainer/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Trainer> createTrainer(@Valid @RequestBody Trainer trainer) {
		log.debug("Saving trainer: " + trainer);
		trainingService.createTrainer(trainer);
		return new ResponseEntity<>(trainer, HttpStatus.CREATED);
	}

	/**
	 * Update trainer
	 *
	 * @param trainer
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/vp/trainer/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Void> updateTrainer(@Valid @RequestBody Trainer trainer) {
		log.debug("Updating trainer: " + trainer);
		trainingService.update(trainer);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Finds a trainer by email. Used for logging in a user with the Salesforce
	 * controller `
	 *
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/training/trainer/byemail/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("permitAll")
	public ResponseEntity<Trainer> findTrainer(@PathVariable String email) {
		log.debug("Find trainer by email " + email);
		Trainer trainer = trainingService.findTrainer(email);
		return new ResponseEntity<>(trainer, HttpStatus.OK);
	}

	/**
	 * Deactivates the trainer
	 *
	 * @param trainer
	 * @return response entity
	 */
	@RequestMapping(value = "/vp/trainer/delete", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Void> makeInactive(@Valid @RequestBody Trainer trainer) {
		log.debug("Updating trainer: " + trainer);
		trainingService.makeInactive(trainer);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Returns all trainers titles from the database `
	 *
	 * @return
	 */
	@RequestMapping(value = "/vp/trainer/titles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'STAGING', 'QC', 'PANEL')")
	public ResponseEntity<List<String>> getAllTrainersTitles() {
		log.debug("Fetching all trainers titles");
		List<String> trainers = trainingService.findAllTrainerTitles();
		return new ResponseEntity<>(trainers, HttpStatus.OK);
	}

	/**
	 * Returns all trainers from the database `
	 *
	 * @return
	 */
	@RequestMapping(value = "/all/trainer/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'STAGING', 'QC', 'PANEL')")
	public ResponseEntity<List<Trainer>> getAllTrainers() {
		log.debug("Fetching all trainers");
		List<Trainer> trainers = trainingService.findAllTrainers();
		return new ResponseEntity<>(trainers, HttpStatus.OK);
	}

	/*
	 *******************************************************
	 * BATCH SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * Find all batches for the currently logged in trainer
	 *
	 * @param auth
	 * @return
	 */
	@RequestMapping(value = "/trainer/batch/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Batch>> findAllBatchesByTrainer(Authentication auth) {
		Trainer userPrincipal = getPrincipal(auth);
		log.debug("Getting all batches for trainer: " + userPrincipal);
		List<Batch> batches = trainingService.findAllBatches(userPrincipal.getTrainerId());
		return new ResponseEntity<>(batches, HttpStatus.OK);
	}

	/**
	 * Create batch
	 *
	 * @param batch
	 *            the batch
	 * @return the response entity
	 */
	@RequestMapping(value = "/all/batch/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'QC')")
	public ResponseEntity<Batch> createBatch(@Valid @RequestBody Batch batch) {
		log.debug("Saving batch: " + batch);
		trainingService.save(batch);
		return new ResponseEntity<>(batch, HttpStatus.CREATED);
	}

	/**
	 * Update batch
	 *
	 * @param batch the batch
	 * @return the response entity
	 */
	@RequestMapping(value = "/all/batch/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'QC')")
	public ResponseEntity<Void> updateBatch(@Valid @RequestBody Batch batch) {
		log.debug("Updating batch: " + batch);
		trainingService.update(batch);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Delete batch
	 *
	 * @param id the id of the batch to delete
	 * @return the response entity
	 */
	@RequestMapping(value = "/all/batch/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Void> deleteBatch(@PathVariable int id) {
		Batch batch = new Batch();
		batch.setBatchId(id);
		log.debug("Deleting batch: " + id);
		trainingService.delete(batch);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Gets all current batches
	 *
	 * @return the all batches
	 */
	@RequestMapping(value = {"/vp/batch/all/current" }, method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Batch>> getAllCurrentBatches() {
		log.debug("Fetching all current batches");
		List<Batch> batches = trainingService.findAllCurrentBatches();
		return new ResponseEntity<>(batches, HttpStatus.OK);
	}

	/**
	 * Gets all batches
	 *
	 * @return the all batches
	 */
	@RequestMapping(value = { "/qc/batch/all", "/vp/batch/all" },
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Batch>> getAllBatches() {
		log.debug("Fetching all batches");
		List<Batch> batches = trainingService.findAllBatches();
		return new ResponseEntity<>(batches, HttpStatus.OK);

	}

	/**
	 * Adds a new week to the batch. Increments counter of total_weeks in database
	 *
	 * @param batchId
	 * @return
	 */
	@RequestMapping(value = "/trainer/week/new/{batchId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC','TRAINER', 'PANEL')")
	public ResponseEntity<Void> createWeek(@PathVariable int batchId) {
		log.debug("Adding week to batch: " + batchId);
		trainingService.addWeek(batchId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/all/locations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'STAGING', 'QC', 'TRAINER', 'PANEL')")
	public ResponseEntity<List<Address>> findCommonLocations() {
		log.debug("Fetching common training locations");
		return new ResponseEntity<>(trainingService.findCommonLocations(), HttpStatus.OK);
	}

	/*
	 *******************************************************
	 * TRAINEE SERVICES
	 *
	 *******************************************************
	 */
	@RequestMapping(value = "/all/trainee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Trainee>> findAllByBatch(@RequestParam(required = true) Integer batch) {
		log.debug("Finding trainees for batch: " + batch);
		List<Trainee> trainees = trainingService.findAllTraineesByBatch(batch);
		return new ResponseEntity<>(trainees, HttpStatus.OK);
	}

	@RequestMapping(value = "/all/trainee/dropped", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Trainee>> findAllDroppedByBatch(@RequestParam(required = true) Integer batch) {
		log.debug("Finding dropped trainees for batch: " + batch);
		List<Trainee> trainees = trainingService.findAllDroppedTraineesByBatch(batch);
		return new ResponseEntity<>(trainees, HttpStatus.OK);
	}

	/**
	 * Create trainee
	 *
	 * @param trainee
	 *            the trainee
	 * @return the response entity
	 */
	@RequestMapping(value = "/all/trainee/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'QC')")
	public ResponseEntity<Trainee> createTrainee(@Valid @RequestBody Trainee trainee) {
		log.debug("Saving trainee: " + trainee);
		trainingService.save(trainee);
		return new ResponseEntity<>(trainee, HttpStatus.CREATED);
	}

	/**
	 * Update trainee
	 *
	 * @param trainee
	 *            the trainee
	 * @return the response entity
	 */
	@RequestMapping(value = "/all/trainee/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'QC')")
	public ResponseEntity<Void> updateTrainee(@Valid @RequestBody Trainee trainee) {
		trainingService.update(trainee);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	
	/**
	 * Delete trainee
	 *
	 * @param id
	 *            the id
	 * @return the response entity
	 */
	@RequestMapping(value = "/all/trainee/delete/{id}", method = RequestMethod.DELETE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Void> deleteTrainee(@PathVariable int id) {
		Trainee trainee = new Trainee();
		trainee.setTraineeId(id);
		log.debug("Deleting trainee: " + id);
		trainingService.delete(trainee);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/all/trainee/switch/trainee/{traineeId}/batch/{batchId}", method = RequestMethod.PUT)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Void> switchBatch(@PathVariable Integer traineeId, @PathVariable Integer batchId) {
		log.debug("Switching trainee " + traineeId + " to batch " + batchId);
		trainingService.switchBatches(traineeId, batchId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/all/trainee/search/{searchTerm}", method = RequestMethod.GET)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER','PANEL')")
	public ResponseEntity<Set<Trainee>> searchTrainee(@PathVariable String searchTerm) {
		Set<Trainee> trainee = trainingService.search(searchTerm);
		if(trainee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(trainee, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/all/trainee/getByEmail/{traineeEmail}", method = RequestMethod.GET)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'PANEL')")
	public ResponseEntity<Trainee> retreiveTraineeByEmail() {
		/* 
		 1. at some point, we will have unique constraint on trainee email.
		 	this method will check the database before adding the new trainee
		 2. before we can enable this functionality, we need to allow
		 	trainees to be assigned to more than one batch, or added with a batch at all
		 	(for tech screening feedback functionality)
		 */
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	/**
	 * Convenience method for accessing the Trainer information from the User
	 * Principal.
	 *
	 * @param auth
	 * @return
	 */
	private Trainer getPrincipal(Authentication auth) {
		return ((RevProUser) auth.getPrincipal()).getCaliberUser();
	}
	
	/*
	 *******************************************************
	 * TASK SERVICES
	 *
	 *******************************************************
	 */
	
	
	/**
	 * Returns all active tasks from the database`
	 *
	 * @return
	 */
	@RequestMapping(value = "/all/tasks/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<List<TrainerTask>> getAllActiveTasks() {
		log.debug("Fetching all active tasks");
		List<TrainerTask> tasks = trainingService.findAllActiveTasks();
		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}
	
	/**
	 * Returns all completed tasks
	 *
	 * @return
	 */
	@RequestMapping(value = "/all/tasks/trainer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<List<TrainerTaskCompletion>> getAllCompletedTasks() {
		log.debug("Fetching all completed tasks");
		List<TrainerTaskCompletion> tasks = trainingService.findAllCompletedTasks();
		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}
	
	/**
	 * Returns all completed tasks by trainerId
	 *
	 * @return
	 */
	@RequestMapping(value = "/all/tasks/trainer/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<List<TrainerTaskCompletion>> getAllTasksByTrainerId(@PathVariable int id) {
		log.debug("Fetching all completed tasks for trainer with id " + id);
		List<TrainerTaskCompletion> tasks = trainingService.findAllTasksByTrainerId(id);
		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}
	
	//Calls a method that creates a new task
	@RequestMapping(value = "/vp/task", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<TrainerTask> saveOrUpdateTask(@Valid @RequestBody TrainerTask task) {
		trainingService.saveOrUpdateTask(task);
		return new ResponseEntity<>(task, HttpStatus.CREATED);
	}
	
	//Saves a completed task
	@RequestMapping(value = "/vp/task/completed", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<TrainerTaskCompletion> saveTaskCompletopn(@Valid @RequestBody TrainerTaskCompletion taskCompletion) {
		trainingService.saveTaskCompletion(taskCompletion);
		return new ResponseEntity<>(taskCompletion, HttpStatus.CREATED);
	}
	
}
