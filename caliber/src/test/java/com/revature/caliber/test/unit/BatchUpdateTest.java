package com.revature.caliber.test.unit;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.tasks.BatchUpdate;

public class BatchUpdateTest extends CaliberTest{
	
	private static final Logger log = Logger.getLogger(BatchUpdate.class);
	
	@Autowired
	private BatchUpdate batchUpdate;
	@Autowired
	private BatchDAO batchDao;
	
	private Trainee traineeOne = new Trainee();
	private Trainee traineeTwo = new Trainee();
	private Trainee traineeThree = new Trainee();
	private Trainer salesforceTrainer = new Trainer();
	private Batch caliberBatch = new Batch();
	private Batch salesforceBatch = new Batch();
	private Set<Trainee> caliberTrainees = new HashSet<>();
	private Set<Trainee> salesforceTrainees = new HashSet<>();
	
	/*
	 *	Dummy data for testUpdateTrainees() 
	 */
	@Before
	public void setTraineeInfo() {
		this.traineeOne.setName("Joe Smith");
		this.traineeOne.setEmail("one@gmail.com");
		this.traineeOne.setPhoneNumber("954-798-6005");
		this.traineeOne.setTrainingStatus(TrainingStatus.Training);
		this.traineeOne.setBatch(caliberBatch);
		
		this.traineeTwo.setName("Shakira Jimenez");
		this.traineeTwo.setResourceId("two");
		this.traineeTwo.setEmail("two@gmail.com");
		this.traineeTwo.setPhoneNumber("347-798-6005");
		this.traineeTwo.setTrainingStatus(TrainingStatus.Training);
		this.traineeTwo.setBatch(caliberBatch);
		
		this.traineeThree.setName("Joe Smith");
		this.traineeThree.setResourceId("one");
		this.traineeThree.setEmail("one@gmail.com");
		this.traineeThree.setPhoneNumber("954-798-6005");
		this.traineeThree.setTrainingStatus(TrainingStatus.Dropped);
		
		caliberTrainees.add(traineeOne);
		caliberTrainees.add(traineeThree);
		salesforceTrainees.add(traineeThree);
		salesforceTrainees.add(traineeTwo);
	}
	
	/*
	 * 	Dummy data for testCompareBatches()
	 */
	@Before
	public void setBatchInfo() {
		
		this.caliberBatch = batchDao.findOneWithDroppedTrainees(2201);
		this.caliberBatch.setTrainees(caliberTrainees);
		log.debug("CaliberBatch: "+ caliberBatch.getResourceId());
		
		this.salesforceTrainer.setName("Tom Riddle");
		this.salesforceTrainer.setEmail("voldemort@gmail.com");
		this.salesforceTrainer.setTitle("Trainer");
		this.salesforceTrainer.setTier(TrainerRole.ROLE_TRAINER);
		
		this.salesforceBatch.setResourceId("TWO");
		this.salesforceBatch.setTrainer(salesforceTrainer);
		this.salesforceBatch.setTrainingName(caliberBatch.getTrainingName());
		this.salesforceBatch.setLocation(caliberBatch.getLocation());
		this.salesforceBatch.setWeeks(caliberBatch.getWeeks());
		this.salesforceBatch.setSkillType(caliberBatch.getSkillType());
		this.salesforceBatch.setTrainees(caliberBatch.getTrainees());
		this.salesforceBatch.setAddress(caliberBatch.getAddress());
		this.salesforceBatch.setEndDate(caliberBatch.getEndDate());
		this.salesforceBatch.setStartDate(caliberBatch.getStartDate());
		this.salesforceBatch.setBatchId(caliberBatch.getBatchId());
		this.salesforceBatch.setTrainees(salesforceTrainees);
	}
	
	/*
	 * Test Methods: Verifies cron trigger executions at midnight,
	 * 				 Checks the last time execution was fired and 
	 * 				 Shows the next execution date that trigger will be fired
	 */
	
	@Test
	@Ignore
	public void testScheduler() {
		org.springframework.scheduling.support.CronTrigger trigger = new CronTrigger("0 0 0 * * *");
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		final Date yesterday = today.getTime();
		String lastMessage = (sdf.format(yesterday)) + " : [Yesterday]";
		log.debug(lastMessage);
		Date nextExecution = trigger.nextExecutionTime(
				new TriggerContext() {
					@Override
					public Date lastScheduledExecutionTime() {
						return yesterday;
					}
					@Override
					public Date lastActualExecutionTime() {
						return yesterday;
					}
					@Override
					public Date lastCompletionTime() {
						return yesterday;
					}
				});
		String nextMessage = sdf.format(nextExecution) + " : [Execution] ";
		log.debug(nextMessage);
	}
	
	@Test
	@Ignore
	public void compareBatchTest() {
		List<Batch> caliberBatches = new ArrayList<>();
		List<Batch> salesforceBatches = new ArrayList<>();
		caliberBatches.add(caliberBatch);
		salesforceBatches.add(salesforceBatch);
		boolean updated = batchUpdate.compareBatches(caliberBatches, salesforceBatches);
		log.debug("Batches were updated: "+updated);
		assertTrue(updated);
	}
	
}
