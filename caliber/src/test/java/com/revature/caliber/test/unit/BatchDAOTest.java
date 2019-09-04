package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.data.BatchDAO;

public class BatchDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(BatchDAOTest.class);

	@Autowired
	private BatchDAO batchDAO;

	public void setBatchDAO(BatchDAO batchDAO) {
		this.batchDAO = batchDAO;
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAllCurrent(trainerId)
	 */
	@Test
	public void findAllCurrentIntTest() {
		log.debug("Testing the BatchDAO.findAllCurrent(trainerId)");
		List<Batch> batches = batchDAO.findAllCurrent(1);
		int expected = 3; //only 3 current batches with trainerId: 1
		int actual = batches.size();
		assertEquals(expected, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAllCurrentWithNotes()
	 */
	@Test
	public void findAllCurrentWithNotesTest() {
		log.debug("Testing the BatchDAO.findAllCurrentWithNotesTest()");
		List<Batch> batches = batchDAO.findAllCurrentWithNotes();
		int expected = 1; // only one current batch has notes
		int actual = batches.size();
		assertEquals(expected, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAllCurrentWithNotesAndTrainees()
	 */
	@Test
	public void findAllCurrentWithNotesAndTraineesTest() {
		log.debug("Testing the BatchDAO.findAllCurrentWithNotesAndTrainees()");
		List<Batch> batches = batchDAO.findAllCurrentWithNotesAndTrainees();
		int expected = 1; // Only one current batch has notes and trainees
		int actual = batches.size();
		assertEquals(expected, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAllCurrentWithTrainees()
	 */
	@Test
	public void findAllCurrentWithTraineesTest() {
		log.debug("Testing the BatchDAO.findAllCurrentWithTrainees()");
		List<Batch> batches = batchDAO.findAllCurrentWithTrainees();
		int expected = 7; // All current batches have trainees
		int actual = batches.size();
		assertEquals(expected, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAll()
	 */
	@Test
	public void findAllTest() {
		log.debug("Testing the BatchDAO.findAll()");
		String sql = "SELECT * FROM CALIBER_BATCH";
		int expect = jdbcTemplate.queryForList(sql).size();
		int actual = batchDAO.findAll().size();
		assertEquals(expect, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAllAfterDate()
	 */
	@Test
	public void findAllAfterDateTest() {
		log.debug("Testing the BatchDAO.findAllAfterDateTest()");
		// positive test
		// find how many after a specific date
		int expect = 9;
		int actual = batchDAO.findAllAfterDate(1, 1, 2017).size();
		log.debug(batchDAO.findAllAfterDate(1, 1, 2017));
		assertEquals(expect, actual);

		// negative test
		String sql = "SELECT START_DATE FROM CALIBER_BATCH WHERE START_DATE >= '2017-01-01'";
		expect = jdbcTemplate.queryForList(sql).size();
		actual = batchDAO.findAllAfterDate(Integer.MAX_VALUE, 1, 2017).size();
		// If SQL statement found at least 1 batch start date they should not
		// equal. Otherwise they both should be equal
		if (expect > 0) {
			assertNotEquals(expect, actual);
		} else {
			assertEquals(expect, actual);
		}
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAllByTrainer() Returns
	 *      batches with specified trainer and co-trainer without dropped
	 *      trainees
	 */
	@Test
	public void findAllByTrainerTest() {
		log.debug("Testing the BatchDAO.findAllByTrainerTest()");
		// positive testing
		String sql = "SELECT TRAINER_ID FROM CALIBER_TRAINER WHERE ROWNUM = 1";
		Integer trainerId = jdbcTemplate.queryForObject(sql, Integer.class);
		sql = "SELECT * FROM CALIBER_BATCH WHERE TRAINER_ID = " + trainerId + " OR CO_TRAINER_ID = " + trainerId;
		int expect = jdbcTemplate.queryForList(sql).size();
		List<Batch> batches = batchDAO.findAllByTrainer(trainerId);
		int actual = batches.size();
		assertEquals(expect, actual);

		// Make sure dropped trainees are not included
		// Testing against batch 2150 with trainer Patrick Walsh
		expect = 13;
		for (int i = 0; i < batches.size(); i++) {
			if (batches.get(i).getBatchId() == 2150) {
				actual = batches.get(i).getTrainees().size();
				break;
			}
		}
		assertEquals(expect, actual);

		// negative testings
		trainerId = Integer.MIN_VALUE;
		expect = 0;
		actual = batchDAO.findAllByTrainer(trainerId).size();
		assertEquals(expect, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAllCurrent() The BatchDAO
	 *      findAllCurrent takes into account 30 days before the current date.
	 *      It also removes dropped trainees from the batches returned
	 */
	@Test
	public void findAllCurrentTest() {
		log.debug("Testing the BatchDAO.findAllCurrentTest()");
		// This allows for 1 month flexibility. This was needed because in
		// BatchDao, the query takes into account 1 month ago as 'current'
		int endDateLimit = 30;
		String sql = "SELECT * FROM CALIBER_BATCH WHERE END_DATE+" + endDateLimit
				+ " >= TO_DATE(SYSDATE,'YYYY/MM/DD') AND START_DATE <= TO_DATE(SYSDATE,'YYYY/MM/DD');";
		int expect = jdbcTemplate.queryForList(sql).size();
		List<Batch> batches = batchDAO.findAllCurrent();
		int actual = batches.size();
		assertEquals(expect, actual);

		// Test to make sure it does not count dropped trainees
		// Testing batch id 2201 because it has 4 dropped from 20
		expect = 16;
		for (int i = 0; i < batches.size(); i++) {
			if (batches.get(i).getBatchId() == 2201) {
				actual = batches.get(i).getTrainees().size();
				break;
			}
		}
		assertEquals(expect, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO.findOne(Integer batchId)
	 * Find a known batch, assert that the IDs are the same.
	 * Try to find a batch that doesn't exist, fail if it does.
	 */
	@Test
	public void findOneTest() {
		log.debug("Testing method BatchDAO.findOne(Integer batchId)");
		int expected = 2050;
		int actual = batchDAO.findOne(expected).getBatchId();
		assertEquals(expected, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO.findOneWithDroppedTrainees(Integer
	 *      batchId) Finds from a batch with known dropped trainees, upon
	 *      finding a single trainee with the TrainingStatus of Dropped, calls
	 *      it good.
	 */
	@Test
	public void findOneWithDroppedTraineesTest() {
		log.debug("Testing method BatchDAO.findOneWithDroppedTrainees(Integer batchId)");
		boolean success = false;
		Set<Trainee> resultSet = batchDAO.findOneWithDroppedTrainees(2150).getTrainees();
		for (Trainee resultSetTrainee : resultSet) {
			if (resultSetTrainee.getTrainingStatus() == TrainingStatus.Dropped) {
				success = true;
				break;
			}
		}
		assertTrue(success);
		try{
			batchDAO.findOneWithDroppedTrainees(-999).getTrainees();
			fail();
		}
		catch(Exception e){
			log.debug(e);
		}
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO.findOneWithTraineesAndGrades(Integer batchId)
	 * Finds from an existing batch, fails if any trainees have a dropped status.
	 * Tries to find from a non-existing batch, fails if no exception gets thrown.
	 */
	@Test
	public void findOneWithTraineesAndGradesTest() {
		log.debug("Testing method BatchDAO.findOneWithTraineesAndGrades(Integer batchId)");
		String expected = "1602 Feb08 Java";
		String actual = batchDAO.findOneWithTraineesAndGrades(2050).getTrainingName();
		assertEquals(expected, actual);
		boolean success = true;
		Set<Trainee> resultSet = batchDAO.findOneWithTraineesAndGrades(2050).getTrainees();
		for (Trainee resultSetTrainee : resultSet) {
			if (resultSetTrainee.getTrainingStatus() == TrainingStatus.Dropped) {
				success = false;
				break;
			}
		}
		assertTrue(success);
		try{
			batchDAO.findOneWithTraineesAndGrades(-999).getTrainees();
			fail();
		}
		catch(Exception e){
			log.debug(e);
		}
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO.update()
	 * This test needs the findOne method to work.
	 * It finds a batch from the database, changes a value, updates the database,
	 * loads the batch from the database again.
	 * Tries to update a non-existing batch.
	 */
	@Test
	public void updateTest() {
		log.debug("Testing method BatchDAO.update(Batch batch)");
		Batch testBatch = batchDAO.findOne(2050);
		testBatch.setLocation("The basement");
		batchDAO.update(testBatch);
		Batch updatedTestBatch = batchDAO.findOne(2050);
		assertEquals(updatedTestBatch.getLocation(),"The basement");
		try{
			testBatch.setBatchId(-984);
			batchDAO.update(testBatch);
			fail();
		}
		catch(Exception e){
			log.debug(e);
		}
	}

	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.data.BatchDAO.save(Batch batch)
	 */
	@Test
	public void saveTest() {
		log.debug("Testing method BatchDAO.save(Batch batch)");
		Trainer testTrainer = new Trainer("Sir. Test", "Tester", "test@test.test", TrainerRole.ROLE_TRAINER);
		testTrainer.setTrainerId(2);
		Batch testBatch = new Batch("Test Name", testTrainer, Date.from(Instant.now()),
				Date.from(Instant.now().plus(Period.ofDays(60))), "Test Location");
		batchDAO.save(testBatch);
		List<Batch> resultSet = batchDAO.findAll();
		boolean success = false;
		for (Batch found : resultSet) {
			if ("Test Location".equals(found.getLocation())) {
				success = true;
				break;
			}
		}
		assertTrue(success);
	}
	
	/**
	 * @see BatchDAO#findAllInProgress()
	 */
	public void testInProgress() {
		log.info("Test all-in-porgress");
		List<Batch> batches = batchDAO.findAllInProgress();
		Calendar today = Calendar.getInstance();
		for(Batch batch : batches) {
			log.info(batch);
			assertTrue(batch.getStartDate().before(today.getTime()) && (batch.getEndDate().after(today.getTime())));
		}
	}
}
