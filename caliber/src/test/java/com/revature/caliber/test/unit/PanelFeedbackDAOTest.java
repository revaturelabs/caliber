package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Category;
import com.revature.caliber.beans.InterviewFormat;
import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelFeedback;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.data.CategoryDAO;
import com.revature.caliber.data.PanelDAO;
import com.revature.caliber.data.PanelFeedbackDAO;
import com.revature.caliber.data.TraineeDAO;
import com.revature.caliber.data.TrainerDAO;

/**
 * @author Connor Monson
 * @author Matt 'Spring Data' Prass
 * @author Nathan Koszuta
 */

public class PanelFeedbackDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(PanelFeedbackDAOTest.class);
	private static final String PANEL_FEEDBACK_COUNT = "SELECT count(panel_feedback_id) FROM caliber_panel_feedback";
	private static final String PANEL_FEEDBACK_COUNT_ID = PANEL_FEEDBACK_COUNT + " WHERE panel_id = ";

	private PanelFeedbackDAO dao;
	private CategoryDAO categoryDAO;
	private PanelDAO panelDAO;
	private TraineeDAO traineeDAO;
	private TrainerDAO trainerDAO;

	@Autowired
	public void setDao(PanelFeedbackDAO dao) {
		this.dao = dao;
	}

	@Autowired
	public void setCategoryDao(CategoryDAO dao) {
		this.categoryDAO = dao;
	}

	@Autowired
	public void setPanelDao(PanelDAO dao) {
		this.panelDAO = dao;
	}

	@Autowired
	public void setTraineeDao(TraineeDAO dao) {
		this.traineeDAO = dao;
	}

	@Autowired
	public void setTrainerDao(TrainerDAO dao) {
		this.trainerDAO = dao;
	}

	/**
	 * Tests the save method in AddressDAO. Creates a new address and checks if
	 * the address appears, and then checks to make sure the size has increased.
	 */
	@Test
	public void saveFeedbackDAO() {
		log.debug("Saving a new Feedback using PanelFeedbackDAO");
		int before = jdbcTemplate.queryForObject(PANEL_FEEDBACK_COUNT, Integer.class);
		PanelFeedback panelFeedback = new PanelFeedback();
		Panel panel = new Panel();
		panel.setFormat(InterviewFormat.Phone);
		panel.setPanelRound(1);
		panel.setStatus(PanelStatus.Pass);
		panel.setTrainee(traineeDAO.findOne(1));
		panel.setPanelist(trainerDAO.findOne(1));
		Category category = categoryDAO.findOne(2);

		panelFeedback.setComment("test");
		panelFeedback.setResult(5);
		panelFeedback.setTechnology(category);
		panelFeedback.setStatus(PanelStatus.Pass);
		panelFeedback.setPanel(panel);

		dao.save(panelFeedback);
		long panelFeedbackid = panelFeedback.getId();
		log.debug("panelFeedbackId: " + panelFeedbackid);
		int after = jdbcTemplate.queryForObject(PANEL_FEEDBACK_COUNT, Integer.class);
		assertEquals(panelFeedback.toString(), dao.findOne(panelFeedbackid).toString());
		assertEquals(++before, after);
	}

	/**
	 * Tests getting all feedback from the database
	 */
	@Test
	public void findAllFeedbackDAO() {
		log.debug("Getting all feedback using PanelFeedbackDAO getAll function");
		long num = jdbcTemplate.queryForObject(PANEL_FEEDBACK_COUNT, Long.class);
		assertNotNull(dao.findAll());
		assertEquals(dao.findAll().size(), num);
	}

	/**
	 * Tests getting all feedback for a certain panel feedback id
	 */
	@Test
	public void getFeedbackByIdDAO() {
		log.debug("Finding feedback by panel id");
		long panelFId = 140;
		int expected = 70;
		assertEquals(dao.findOne(panelFId).getPanel().getId(), expected);
	}

	/**
	 * Tests to make sure an address not in the database returns null.
	 */
	@Test
	public void nullGetPanelFeedbackByInt() {
		log.debug("Attempting to get a panel that doesn't exist");
		PanelFeedback feedback = dao.findOne(99999999);
		assertNull(feedback);
	}

	/**
	 * Tests update method of FeedbackDAO. Asserts that addresses zipcode was
	 * changed.
	 */
	@Test
	public void updateFeedbackDAO() {
		log.debug("UpdateFeedbackDAO Test");
		String comment = "11111";
		long panelFId = 10;

		PanelFeedback actual = dao.findOne(panelFId);

		actual.setComment(comment);
		dao.update(actual);
		assertEquals(dao.findOne(panelFId).getComment(), actual.getComment());
	}

	/**
	 * Test finding all feedbacks for a panel id
	 */
	@Test
	public void findAllForPanelDAO() {
		int panelId = 40;
		int actual = dao.findAllForPanel(panelId).size();
		int expected = jdbcTemplate.queryForObject(PANEL_FEEDBACK_COUNT_ID + panelId, Integer.class);

		assertEquals(expected, actual);

		panelId = -789;
		actual = dao.findAllForPanel(panelId).size();
		expected = jdbcTemplate.queryForObject(PANEL_FEEDBACK_COUNT_ID + panelId, Integer.class);

		assertEquals(expected, actual);
	}

	/**
	 * 
	 */
	@Test
	public void findFailedFeedbackByPanelDAO() {
		// positive testing
		int panelId = 60;
		int actual = dao.findFailedFeedbackByPanel(panelDAO.findOne(panelId)).size();
		int expected = jdbcTemplate.queryForObject(PANEL_FEEDBACK_COUNT_ID + panelId + " AND panel_status = 'Repanel'",
				Integer.class);

		assertEquals(expected, actual);

		// negative testing
		panelId = -8309;
		actual = dao.findFailedFeedbackByPanel(panelDAO.findOne(panelId)).size();
		expected = jdbcTemplate.queryForObject(PANEL_FEEDBACK_COUNT_ID + panelId + " AND panel_status = 'Repanel'",
				Integer.class);

		assertEquals(expected, actual);
	}

}
