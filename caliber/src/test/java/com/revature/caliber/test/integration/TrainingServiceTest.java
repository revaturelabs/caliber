package com.revature.caliber.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.data.TrainerDAO;
import com.revature.caliber.services.TrainingService;

public class TrainingServiceTest extends CaliberTest {
	private static final Logger log = Logger.getLogger(TrainingServiceTest.class);
	private static final String INACTIVE = "select NUMBER_OF_WEEKS from CALIBER_BATCH where BATCH_ID = 2100";
	TrainingService service;
	TrainerDAO dao;
	
	@Autowired
	public void setDao(TrainerDAO dao) {
		this.dao = dao;
	}
	
	@Autowired
	public void setService(TrainingService service) {
		this.service = service;
	}
	
	@Test
	public void makeInactiveWeek(){
		TrainerRole teirBefore = dao.findOne(1).getTier();
		assertEquals(TrainerRole.ROLE_VP,teirBefore);
		service.makeInactive(dao.findOne(1));
		TrainerRole teirAfter = dao.findOne(1).getTier();
		assertNotEquals(teirBefore, teirAfter );
		assertEquals(TrainerRole.ROLE_INACTIVE, teirAfter);
	}
	@Test
	public void addWeek(){
		log.debug("Testing TrainingService addWeek function");
		int beforeNum = jdbcTemplate.queryForObject(INACTIVE, Integer.class);
		service.addWeek(2100);
		int afterNum = jdbcTemplate.queryForObject(INACTIVE, Integer.class);
		assertEquals(++beforeNum, afterNum);
	}

}
