package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TrainerTask;
import com.revature.caliber.beans.TrainerTaskCompletion;
import com.revature.caliber.data.TaskCompletionDAO;
import com.revature.caliber.data.TaskDAO;
import com.revature.caliber.data.TrainerDAO;

public class TaskCompletionDAOTest extends CaliberTest{
	/*
	private static final Logger log = Logger.getLogger(TaskCompletionDAOTest.class);

	@Autowired
	private TaskCompletionDAO dao;
	
	@Autowired
	private TrainerDAO trainerDAO;
	
	@Autowired
	private TaskDAO taskDAO;
	
	private static final String TASK_COMPLETION_COUNT = "select count(task_completion_id) from caliber_task_completion";
	private static final String TASK_BY_TRAINER_ID_COUNT = "select count(task_completion_id) from caliber_task_completion WHERE TRAINER_ID = ";

	@Autowired
	public void setTaskCompletionDAO(TaskCompletionDAO taskCompletionDAO) {
		this.dao = taskCompletionDAO;
	}
	
	@Autowired
	public void setTrainerDAO(TrainerDAO trainerDAO) {
		this.trainerDAO = trainerDAO;
	}
	
	@Autowired
	public void setTaskDAO(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}
	
	@Test
	public void testFindAllCompleted() {
		log.debug("FIND ALL COMPLETED TASKS");
		Long sizeActual = jdbcTemplate.queryForObject(TASK_COMPLETION_COUNT, Long.class);
		List<TrainerTaskCompletion> tasks = dao.findAllCompletedTasks();
		Long sizeExpected = (long) tasks.size();
		assertEquals(sizeExpected, sizeActual);
	}
	
	@Test
	public void testFindCompletedByTrainerId() {
		log.debug("FIND ALL COMPLETED TASKS BY TRAINER ID");
		Long sizeActual = jdbcTemplate.queryForObject(TASK_BY_TRAINER_ID_COUNT + "42", Long.class);
		List<TrainerTaskCompletion> tasks = dao.findAllTasksByTrainerId(42);
		Long sizeExpected = (long) tasks.size();
		assertEquals(sizeExpected, sizeActual);
	}
	
	@Test
	public void testSaveTaskCompletion() {
		log.debug("CREATE TASK COMPLETION");
		Trainer trainer = new Trainer("Donald Duck", "Trainer in Training","mightyduck@gmail.com", TrainerRole.ROLE_TRAINER);
		Trainer checker = new Trainer("Mickey Mouse", "Vice President","mickey@gmail.com", TrainerRole.ROLE_VP);
		Date now = Date.from(Instant.now());
		TrainerTask task = new TrainerTask("Jump up and down while rubbing your belly",3);
		Integer before = jdbcTemplate.queryForObject(TASK_COMPLETION_COUNT, Integer.class);
		trainerDAO.save(trainer);
		trainerDAO.save(checker);
		taskDAO.saveOrUpdateTask(task);
		TrainerTaskCompletion tc = new TrainerTaskCompletion(trainer, checker, now, task);
		dao.saveTaskCompletion(tc);
		Integer after = jdbcTemplate.queryForObject(TASK_COMPLETION_COUNT, Integer.class);
		log.debug("after = "+ after);
		assertEquals(++before, after);
	}
*/
}
	

