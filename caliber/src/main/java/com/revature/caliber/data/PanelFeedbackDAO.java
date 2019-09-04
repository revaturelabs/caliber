package com.revature.caliber.data;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelFeedback;
import com.revature.caliber.beans.PanelStatus;

/**
 * @author Connor Monson
 * @author Matt 'Spring Data' Prass
 */

@Repository
public class PanelFeedbackDAO {

	private static final Logger log = Logger.getLogger(PanelFeedbackDAO.class);
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Find all panels. Useful for listing available panels
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<PanelFeedback> findAll() {
		log.debug("Finding all panel feedback");
		return sessionFactory.getCurrentSession().createCriteria(PanelFeedback.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	/**
	 * Find all panel feedbacks for a panel.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<PanelFeedback> findAllForPanel(int panelId) {
		log.debug("Finding all panel feedback for panel: " + panelId);
		return sessionFactory.getCurrentSession().createCriteria(PanelFeedback.class)
				.add(Restrictions.eq("panel.id", panelId))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Find all panel feedbacks for one panel
	 * @author emmabownes
	 * @return List of panel feedbacks for a given panel
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<PanelFeedback> findFailedFeedbackByPanel(Panel panel) {
		log.debug("Finding failed panel feedback for panel "+ panel);
		int panelId = (panel==null)? -1 : panel.getId();
		return sessionFactory.getCurrentSession().createCriteria(PanelFeedback.class)
				.add(Restrictions.eq("panel.id", panelId))
				.add(Restrictions.eq("status", PanelStatus.Repanel))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param panelFeedback
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(PanelFeedback panelFeedback) {
		log.debug("Save panel feedback " + panelFeedback);
		long id = (long) sessionFactory.getCurrentSession().save(panelFeedback);
		log.debug("New Feedback ID: " +id);
	}

	/**
	 * Find panel by the given identifier
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public PanelFeedback findOne(long panelFeedbackId) {
		log.debug("Find panel feedback by id: " + panelFeedbackId);
		return (PanelFeedback) sessionFactory.getCurrentSession().createCriteria(PanelFeedback.class)
				.add(Restrictions.eq("id", panelFeedbackId)).uniqueResult();
	}

	/**
	 * Updates a panel in the database.
	 * 
	 * @param panel
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(PanelFeedback panelFeedback) {
		log.debug("Update panel " + panelFeedback);
		sessionFactory.getCurrentSession().saveOrUpdate(panelFeedback);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteById(long panelId) {
		log.debug("Delete panel feedback " + panelId);
		PanelFeedback panelFeedback = findOne(panelId);
		if (panelFeedback != null) {
			sessionFactory.getCurrentSession().delete(panelFeedback);
		}
	}
}
