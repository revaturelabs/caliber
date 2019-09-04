package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.TrainerTask;
import com.revature.caliber.data.TaskDAO;

public class TaskDAOTest extends CaliberTest{
	
	/*
	private static final Logger log = Logger.getLogger(TaskDAOTest.class);

	@Autowired
	private TaskDAO dao;
	
	private static final String TASK_COUNT = "select count(task_id) from caliber_task";
	private static final String ACTIVE_TASK_COUNT = "select count(task_id) from caliber_task WHERE IS_ACTIVE = 1";

	@Autowired
	public void setTaskDAO(TaskDAO taskDAO) {
		this.dao = taskDAO;
	}
	
	@Test
	public void testFindAllActive() {
		log.debug("FIND ALL ACTIVE TASKS TEST");
		Long sizeActual = jdbcTemplate.queryForObject(ACTIVE_TASK_COUNT, Long.class);
		List<TrainerTask> tasks = dao.findAllActiveTasks();
		Long sizeExpected = (long) tasks.size();
		assertEquals(sizeExpected, sizeActual);
	}
	
	@Test
	public void save() {
		log.debug("Testing save method from TaskDAO");
		TrainerTask newTask = new TrainerTask("Gets Revature logo inner lip tattoo",2);
		Integer before = jdbcTemplate.queryForObject(TASK_COUNT, Integer.class);
		dao.saveOrUpdateTask(newTask);
		Integer after = jdbcTemplate.queryForObject(TASK_COUNT, Integer.class);
		assertEquals(++before, after);
	}
	*/

}
	

