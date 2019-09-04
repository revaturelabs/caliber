package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.InterviewFormat;
import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.data.PanelDAO;
import com.revature.caliber.data.TraineeDAO;
import com.revature.caliber.data.TrainerDAO;

/**
 * @author Connor Monson
 * @author Matt 'Spring Data' Prass
 * @author Nathan Koszuta
 */
public class PanelDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(PanelDAOTest.class);
	private static final String PANEL_COUNT = "SELECT count(panel_id) FROM caliber_panel";

	@Autowired
	private PanelDAO panelDAO;

	@Autowired
	private TraineeDAO traineeDao;

	@Autowired
	private TrainerDAO trainerDao;

	public void setPanelDAO(PanelDAO panelDAO) {
		this.panelDAO = panelDAO;
	}

	public void setTraineeDao(TraineeDAO dao) {
		this.traineeDao = dao;
	}

	public void setTrainerDao(TrainerDAO dao) {
		this.trainerDao = dao;
	}

	/**
	 * Tests getting all panels
	 */
	@Test
	public void findAllTest() {
		log.debug("Testing the PanelDAO.findAll()");
		String sql = "SELECT * FROM CALIBER_PANEL";
		int expect = jdbcTemplate.queryForList(sql).size();
		int actual = panelDAO.findAll().size();
		assertEquals(expect, actual);
	}

	/**
	 * Tests getting all panels belonging to a single trainee
	 */
	@Test
	public void findAllByTraineeTest() {
		log.debug("Testing the PanelDAO.findAllByTraineeTest()");
		
		// positive testing
		Integer traineeId = 5500;
		String sql = "SELECT * FROM CALIBER_PANEL WHERE TRAINEE_ID=" + traineeId;
		int expect = jdbcTemplate.queryForList(sql).size();
		List<Panel> panels = panelDAO.findAllByTrainee(traineeId);
		int actual = panels.size();
		assertEquals(expect, actual);

		// negative testings
		traineeId = Integer.MIN_VALUE;
		expect = 0;
		actual = panelDAO.findAllByTrainee(traineeId).size();
		assertEquals(expect, actual);
	}

	/**
	 * Tests getting one panel by id
	 */
	@Test
	public void findOneTest() {
		log.debug("Testing method PanelDAO.findOne(Integer panelId)");
		int expected = 40;
		int actual = panelDAO.findOne(expected).getId();
		assertEquals(expected, actual);
		
		try {
			expected = -234;
			panelDAO.findOne(expected).getId();
			fail();
		} catch (Exception e) {
			log.debug(e);
		}
	}

	/**
	 * Tests updating a single panel
	 */
	@Test
	public void updateTest() {
		log.debug("Testing method PanelDAO.update(Panel panel)");
		Panel testPanel = panelDAO.findOne(1);
		testPanel.setPanelRound(100);
		panelDAO.update(testPanel);
		Panel updatedTestPanel = panelDAO.findOne(1);
		assertEquals(updatedTestPanel.getPanelRound(), 100);
		
		try {
			testPanel.setId(-984);
			panelDAO.update(testPanel);
			fail();
		} catch (Exception e) {
			log.debug(e);
		}
	}

	/**
	 * Tests creating a new panel and saving to the database
	 */
	@Test
	public void saveTest() {
		log.debug("Saving a new Panel using PanelDAO");
		int before = jdbcTemplate.queryForObject(PANEL_COUNT, Integer.class);
		Panel p = getPanel();

		panelDAO.save(p);
		int after = jdbcTemplate.queryForObject(PANEL_COUNT, Integer.class);
		List<Panel> resultSet = panelDAO.findAll();
		boolean success = false;
		for (Panel found : resultSet) {
			if (p.getId() == found.getId()) {
				success = true;
				break;
			}
		}
		assertTrue(success);
		assertEquals(++before, after);
	}

	/**
	 * Tests finding all panels marked with the status 'Repanel'
	 */
	@Test
	public void findAllRepanelTest() {
		log.debug("Testing the PanelDAO.findAllRepanelTest()");

		String sql = "SELECT * FROM CALIBER_PANEL WHERE panel_status = 'Repanel'";
		int expect = jdbcTemplate.queryForList(sql).size();
		List<Panel> paneles = panelDAO.findAllRepanel();
		int actual = paneles.size();
		assertEquals(expect, actual);
	}

	/**
	 * Tests deleting a panel
	 */
	@Test
	public void deletePanelTest() {
		log.debug("DELETE PANEL DAO");
		
		// positive testing
		int beforeTest = jdbcTemplate.queryForObject(PANEL_COUNT, Integer.class);
		Panel p = panelDAO.findOne(1);
		log.debug("panel: " + p);
		panelDAO.delete(p.getId());
		int afterTest = jdbcTemplate.queryForObject(PANEL_COUNT, Integer.class);
		assertEquals(--beforeTest, afterTest);
		
		// negative testing
		panelDAO.delete(-100);
		assertEquals(beforeTest, afterTest);
	}

	/**
	 * Tests that the date format already in the database is the same as after
	 * updating a panel
	 */
	@Test
	public void panelDateFormatTest() {
		Panel expected = panelDAO.findOne(40);
		expected.setDuration("100 hours");
		panelDAO.update(expected);
		Panel actual = panelDAO.findOne(40);

		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getInterviewDate(), actual.getInterviewDate());
		assertEquals(expected, actual);
	}

	/**
	 * Tests the .equals method on the panel
	 */
	@Test
	public void equalsTest() {
		Panel expected = panelDAO.findOne(40);
		Panel actual = panelDAO.findOne(40);

		assertTrue(expected.equals(actual));
	}

	/**
	 * Tests whether the date can be updated while maintaining the same
	 * date format
	 */
	@Test
	public void panelCreateUpdateTest() {
		Panel expected = getPanel();

		panelDAO.save(expected);
		expected.setInterviewDate(new Date(5));
		panelDAO.update(expected);
		Panel actual = panelDAO.findOne(expected.getId());

		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getInterviewDate(), actual.getInterviewDate());
		assertEquals(expected, actual);
	}
	
	/**
	 * Tests the .equals method on the created panel
	 */
	@Test
	public void createSaveTest() {
		Panel expected = getPanel();
		panelDAO.save(expected);
		Panel actual = panelDAO.findOne(expected.getId());

		assertEquals(expected, actual);
	}
	
	/**
	 * Tests the .equals method on the created panel after a trivial update
	 */
	@Test
	public void createSaveUpdateTest() {
		Panel expected = getPanel();
		panelDAO.save(expected);
		panelDAO.update(expected);
		Panel actual = panelDAO.findOne(expected.getId());

		assertEquals(expected, actual);
	}

	public Panel getPanel() {
		Panel p = new Panel();
		p.setFormat(InterviewFormat.Phone);
		p.setPanelRound(1);
		p.setStatus(PanelStatus.Pass);
		p.setTrainee(traineeDao.findOne(1));
		p.setPanelist(trainerDao.findOne(3));
		p.setInterviewDate(new Date());
		return p;
	}

}
