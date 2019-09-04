package com.revature.caliber.data;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.beans.Category;

@Repository
public class CategoryDAO {

	private static final Logger log = Logger.getLogger(CategoryDAO.class);
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// Is intended to be used to find all ACTIVE categories. Does NOT show
	// categories that are inactive.
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Category> findAllActive() {
		log.debug("Fetching categories");
		return sessionFactory.getCurrentSession().createCriteria(Category.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).add(Restrictions.eq("active", true))
				.addOrder(Order.asc("categoryId")).list();
	}

	// Is intended to be only used by VP to see both ACTIVE and INACTIVE categories
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Category> findAll() {
		log.debug("Fetching categories");
		return sessionFactory.getCurrentSession().createCriteria(Category.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.asc("skillCategory")).list();
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Category findOne(Integer id) {
		return (Category) sessionFactory.getCurrentSession().get(Category.class, id);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void update(Category category) {
		log.debug("updating category");
		sessionFactory.getCurrentSession().update(category);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void save(Category category) {
		sessionFactory.getCurrentSession().save(category);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Category findBySkillCategory(String skillCategory) {
		return (Category) sessionFactory.getCurrentSession().createCriteria(Category.class)
				.add(Restrictions.eq("skillCategory", skillCategory)).uniqueResult();
	}

}
