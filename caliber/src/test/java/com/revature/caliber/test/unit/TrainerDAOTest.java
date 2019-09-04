package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.data.TrainerDAO;

public class TrainerDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(TrainerDAOTest.class);
	private static final String PAT_MAIL = "patrick.walsh@revature.com";
	private static final List<String> TRAINER_TITLES = Arrays
			.asList(new String[] { "Senior Trainer", "Senior Technical Manager", "Lead Trainer",
					"Vice President of Technology", "Senior Java Developer", "Technology Manager", "Trainer" }); 
	private TrainerDAO trainerDAO;

	@Autowired
	public void setTrainerDAO(TrainerDAO trainerDAO) {
		this.trainerDAO = trainerDAO;
	}

	@Test
	public void testFindAllTrainerTitles() {
		log.debug("Find all trainer titles");
		assertTrue(trainerDAO.findAllTrainerTitles().containsAll(TRAINER_TITLES));
	}

	@Test
	public void testFindByEmail() {
		log.debug("Find trainer by email");
		Trainer expected = new Trainer("Patrick Walsh", TRAINER_TITLES.get(2), PAT_MAIL, TrainerRole.ROLE_VP);
		assertEquals(expected, trainerDAO.findByEmail(PAT_MAIL));
	}

	@Test
	public void testFindAll() {
		log.debug("Find all trainers");
		assertEquals(30, trainerDAO.findAll().size());
	}

	@Test
	public void testSave() {
		log.debug("Save a new trainer");
		int before = trainerDAO.findAll().size();
		trainerDAO.save(new Trainer("Alex Cobian", TRAINER_TITLES.get(1), "cobian448@yahoo.com", TrainerRole.ROLE_VP));
		int after = trainerDAO.findAll().size();
		assertEquals(before, --after);
	}

	@Test
	public void testFindOne() {
		log.debug("Find trainer by id");
		Trainer expected = new Trainer("Patrick Walsh", TRAINER_TITLES.get(2), PAT_MAIL, TrainerRole.ROLE_VP);
		assertEquals(expected, trainerDAO.findOne(1));
	}

	@Test
	public void testUpdate() {
		log.debug("Update trainer");
		//Test for name update
		Trainer expected = trainerDAO.findByEmail(PAT_MAIL);
		expected.setName("Success Walsh");
		trainerDAO.update(expected);
		Trainer actual = trainerDAO.findByEmail(PAT_MAIL);
		assertEquals(expected, actual);
		//Test for email update
		String newEmail = "NewEmail@gmail.com";
		expected.setEmail(newEmail);
		trainerDAO.update(expected);
		Trainer actualByEmail = trainerDAO.findByEmail(newEmail);
		assertEquals(expected, actualByEmail);		
	}

}
