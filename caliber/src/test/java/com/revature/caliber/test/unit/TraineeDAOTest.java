package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;


import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;

import com.revature.caliber.data.TraineeDAO;

public class TraineeDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(TraineeDAOTest.class);

	@Autowired
	private TraineeDAO traineeDAO;
	private static final String TRAINEE_COUNT = "select count(trainee_id) from caliber_trainee";

	@Autowired
	public void setTraineeDAO(TraineeDAO traineeDAO) {
		this.traineeDAO = traineeDAO;
	}

	/**
	 * 
	 * TraineeDAO.save(Trainee trainee)
	 * 	Create trainees:
	 * 		1 fully valid trainee
	 * 		1 trainee with invalid name (none)
	 * 		1 trainee with invalid email (none)
	 * 		1 trainee with invalid batch (none)
	 * 		run save method with each trainee
	 * 		test to see total number of trainers only increases by one since only one is fully valid
	 * 
	 * */
	@Test
	public void testSave() {
		log.debug("CREATE TRAINEE");
		Batch batch = new Batch();
		batch.setBatchId(2200);
		String name = "Danny McQuack";
		String email = "test@anotherDomain.com";
		Trainee trainee = new Trainee(name, null, email, batch);
		Long before = jdbcTemplate.queryForObject(TRAINEE_COUNT, Long.class);
		traineeDAO.save(trainee);

		try {
			Trainee nullName = new Trainee(null, null, email, batch);
			traineeDAO.save(nullName);

		} catch (ConstraintViolationException e) {
			log.debug(e);
		}
		try {
			Trainee nullEmail = new Trainee(name, null, null, batch);
			traineeDAO.save(nullEmail);
		} catch (ConstraintViolationException e) {
			log.debug(e);
		}
		try {
			Trainee nullBatch = new Trainee(name, null, email, null);
			traineeDAO.save(nullBatch);
		} catch (ConstraintViolationException e) {
			log.debug(e);
		}
		Long after = jdbcTemplate.queryForObject(TRAINEE_COUNT, Long.class);
		assertEquals(++before, after);
	}

	/**
	 * 
	 * TraineeDAO.findAll()
	 * 	Use JDBCtemplate to save actual size of Trainee table
	 * 	Use findAll() to save expected list of trainees and size of the trainee list
	 * 	compare actual to expected to make sure the same number of trainees are being returned
	 * 
	 * */
	@Test
	public void testFindAll() {
		log.debug("FIND ALL TRAINEES");
		Long sizeActual = jdbcTemplate.queryForObject(TRAINEE_COUNT, Long.class);
		List<Trainee> trainees = traineeDAO.findAll();
		Long sizeExpected = (long) trainees.size();
		assertEquals(sizeExpected, sizeActual);
	}
	
	/**
	 * 
	 * TraineeDAO.findAllNotDropped()
	 * 	Use JDBCtemplate to save the number of trainees without status 'Dropped'
	 * 	Use findAllNotDropped() to save expected list of trainees and size of the trainee list
	 * 	compare actual to expected to make sure the same number of trainees are being returned
	 * 
	 * */
	@Test
	public void testFindAllNotDropped() {
		log.debug("FIND ALL TRAINEES NOT DROPPED");
		Long sizeActual = jdbcTemplate.queryForObject(TRAINEE_COUNT + " WHERE training_status != 'Dropped'", Long.class);
		List<Trainee> trainees = traineeDAO.findAllNotDropped();
		Long sizeExpected = (long) trainees.size();
		assertEquals(sizeExpected, sizeActual);
	}

	/**
	 * 
	 * TraineeDAO.findAllByBatch()
	 * 	(note) method only returns trainees whose training_status value is not 'Dropped'
	 * 	1:
	 * 	Use JDBCtemplate to save actual list of Trainees found by known Batch of trainees
	 * 	Use findALlByBatch() to find expected list of Trainees found by same Batch
	 * 	compare size of actual to size of expected lists
	 * 	2:
	 * 	Use findAllByBatch() to find list of trainees from batch that DOES NOT EXIST
	 * 	Compare size of list to 0 since list should be empty
	 * 
	 * */
	@Test
	public void testFindAllByBatch() {
		log.debug("FIND ALL TRAINEES BY BATCH");
		Batch batch = new Batch();
		batch.setBatchId(2200);
		String traineeCountByBatch = TRAINEE_COUNT + " where batch_id = " + batch.getBatchId()
				+ " and training_status != 'Dropped'";
		List<Trainee> trainees = traineeDAO.findAllByBatch(batch.getBatchId());
		Long actualBatchSize = jdbcTemplate.queryForObject(traineeCountByBatch, Long.class);
		Long expectedBatchSize = (long) trainees.size();
		assertEquals(expectedBatchSize, actualBatchSize);
		int badBatchId = 5; // no batch with this Id exists
		List<Trainee> badBatchCall = traineeDAO.findAllByBatch(badBatchId);
		int badBatchCallSize = badBatchCall.size();
		assertEquals(0, badBatchCallSize);
	}
	
	/**
	 * 
	 * TraineeDAO.findAllDroppedByBatch()
	 * 	(note) this method is intended to only return trainees whose training_status is 'Dropped'
	 * 	1:
	 * 	Use JDBCtemplate to find actual list of DROPPED trainees from known batch
	 * 	Use findAllDroppedByBatch() to find expected list of trainees from known batch
	 * 	compare the size of expected and actual to make sure they match
	 * 	2:
	 * 	Use findAllDroppedByBatch() to find list of trainees from batch that DOES NOT EXIST
	 * 	Compare size of list to 0 since list should be empty
	 * 
	 * */
	@Test
	public void testFindAllDroppedByBatch() {
		log.debug("FIND ALL TRAINEES DROPPED BY BATCH");

		Batch batch = new Batch();
		batch.setBatchId(2200);
		String traineeCountByBatch = TRAINEE_COUNT + " where batch_id = " + batch.getBatchId()
				+ " and training_status = 'Dropped'";
		Long actualBatchDroppedSize = jdbcTemplate.queryForObject(traineeCountByBatch, Long.class);
		List<Trainee> droppedTrainees = traineeDAO.findAllDroppedByBatch(batch.getBatchId());
		Long expectedBatchDroppedSize = (long) droppedTrainees.size();
		assertEquals(expectedBatchDroppedSize, actualBatchDroppedSize);

		int badBatchId = 5; // no batch with this Id exists
		List<Trainee> badBatchCall = traineeDAO.findAllDroppedByBatch(badBatchId);
		int badBatchCallSize = badBatchCall.size();
		assertEquals(0, badBatchCallSize);
	}
	
	/**
	 * 
	 * TraineeDAO.findAllByTrainer()
	 * 	(note) method only returns trainees whose training_status value is not 'Dropped'
	 * 	1:
	 * 	Use JDBCtemplate to find actual list of trainees matched to known trainer
	 * 	Use findAllByTrainer() to find expected list of trainees matched to known trainer
	 * 	compare size of actual list to size of expected list to make sure they match
	 * 	2:
	 * 	Use findAllByTrainer() to find list of trainees from a trainer that DOES NOT EXIST
	 * 	compare size of list to 0 since no trainees should be returned
	 * 
	 * */
	@Test
	public void testFindAllByTrainer() {
		log.debug("FIND ALL TRAINEES BY TRAINER");
		Trainer trainer = new Trainer();
			trainer.setTrainerId(1);
		String traineeCountByTrainer = TRAINEE_COUNT + " WHERE BATCH_ID IN "
				+ "(SELECT BATCH_ID FROM CALIBER_BATCH WHERE TRAINER_ID = " + trainer.getTrainerId() + ") "
				+ " AND TRAINING_STATUS != 'Dropped'";
		Long actualCountSize = jdbcTemplate.queryForObject(traineeCountByTrainer, Long.class);
		List<Trainee> trainees = traineeDAO.findAllByTrainer(trainer.getTrainerId());
		Long expectedCountSize = (long) trainees.size();
		assertEquals(expectedCountSize, actualCountSize);

		int badTrainerId = -1; // no trainer with this Id exists
		List<Trainee> badTrainerCall = traineeDAO.findAllByTrainer(badTrainerId);
		int badTrainerCallSize = badTrainerCall.size();
		assertEquals(0, badTrainerCallSize);
	}

	/**
	 * Validates the findOne function by finding a trainee by
	 * their unique ID
	 */
	@Test
	public void testFindOne() {
		log.debug("Find trainee by Id Test");
		String actual = "osher.y.cohen@gmail.com";
		assertEquals(actual, traineeDAO.findOne(5503).getEmail());
	}

	/**
	 * Validates the findByEmail function by finding a trainee by
	 * their unique email
	 */
	@Test
	public void testFindByEmail() {
		log.debug("Find trainee by email Test");
		Integer id = 5503;
		assertEquals((int) id, (int) traineeDAO.findByEmail("osher").get(0).getTraineeId());
	}
	
	@Test
	public void testFindByName() {
		log.debug("Find trainee by name Test");
		Integer id = 5511;
		assertEquals((int) id, (int) traineeDAO.findByName("Lau").get(0).getTraineeId());
	}
	
	@Test
	public void testFindBySkypeId() {
		log.debug("Find trainee by SkypeId Test");
		Integer id = 5504;
		assertEquals((int) id, (int) traineeDAO.findBySkypeId("kyle.chang").get(0).getTraineeId());
	}

	/**
	 * Validates the update function by updating the trainee's name
	 * and checking against the original in the database
	 */
	@Test
	public void testUpdate() {
		log.debug("Update trainee");
		String updatedName = "Up, Dated";
		Trainee trainee = traineeDAO.findOne(5503);
		trainee.setName(updatedName);
		traineeDAO.update(trainee);
		assertEquals(updatedName, trainee.getName());
	}

	/**
	 * Validates the delete function by comparing the number of rows
	 * in the original database to the number of rows in the altered
	 * database after the delete (there should be one fewer).
	 */
	@Test
	public void testDelete() {
		log.debug("Delete trainee");
		int initialSize = traineeDAO.findAll().size();
		Trainee toDelete = traineeDAO.findOne(5503);
		traineeDAO.delete(toDelete);
		int newSize = traineeDAO.findAll().size();
		assertEquals(--initialSize, newSize);
	}
}
