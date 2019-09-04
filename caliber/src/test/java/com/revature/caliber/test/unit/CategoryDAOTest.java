package com.revature.caliber.test.unit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Category;
import com.revature.caliber.data.CategoryDAO;

public class CategoryDAOTest extends CaliberTest {
	private static final Logger log = Logger.getLogger(CategoryDAO.class);

	@Autowired
	private CategoryDAO dao;

	private static final String CATEGORY_COUNT = "select count(category_id) from caliber_category";							//Finds all categories, active and inactive.
	private static final String ACTIVE_CATEGORY = "select count(category_id) from caliber_category WHERE IS_ACTIVE = 1;";	//Only finds categories that are active.
	
	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.CategoryDAO.findAll()
	 */
	@Test
	public void findAll() {
		log.debug("Testing findAll method from CategoryDAO");
		int expected = dao.findAll().size();
		int actual = jdbcTemplate.queryForObject(CATEGORY_COUNT, Integer.class);
		assertEquals(expected, actual);
	}
	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.CategoryDAO.findAllCategories()
	 * distinct from findAll in that it orders by Skill name rather than category ID.
	 */
	@Test
	public void findAllActive() {
		log.debug("Testing findAllActive from CategoryDAO");
		int expected = dao.findAllActive().size();
		int actual = jdbcTemplate.queryForObject(ACTIVE_CATEGORY,Integer.class);
		assertEquals(expected, actual);
		
	}
	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.CategoryDAO.fineOne()
	 */
	@Test
	public void findOne() {
		log.debug("Testing findOne method from CategoryDAO");
		Category myCat = dao.findOne(1);
		assertNotNull(myCat);
		assertTrue(dao.findOne(1) instanceof Category);
		assertEquals(dao.findOne(1).toString(), "Java");
	}
	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.CategoryDAO.update()
	 */
	@Test
	public void update() {
		log.debug("Testing update from CategoryDAO");
		String skillName = "Revature Pro";
		Category myCat = dao.findOne(1);
		myCat.setSkillCategory(skillName);
		dao.update(myCat);
		assertEquals(skillName, myCat.getSkillCategory());
	}
	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.CategoryDAO.save()
	 */
	@Test
	public void save() {
		log.debug("Testing save method from CategoryDAO");
		Category newCat = new Category("Underwater Basket Weaving", false);
		Long before = jdbcTemplate.queryForObject(CATEGORY_COUNT, Long.class);
		dao.save(newCat);
		Long after = jdbcTemplate.queryForObject(CATEGORY_COUNT, Long.class);
		assertEquals(++before, after);
	}
}
