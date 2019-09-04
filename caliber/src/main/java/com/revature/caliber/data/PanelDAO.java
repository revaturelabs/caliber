package com.revature.caliber.data;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TrainingStatus;

/**
 * @author Connor Monson
 * @author Matt 'Spring Data' Prass
 */

@Repository
public class PanelDAO {

	private static final Logger log = Logger.getLogger(PanelDAO.class);
	private static final String INTERVIEW_DATE = "interviewDate";
	private SessionFactory sessionFactory;
	
	@Autowired
	PanelFeedbackDAO panelFeedbackDao;
	
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
	public List<Panel> findAll() {
		log.debug("Finding all panels");
		return sessionFactory.getCurrentSession().createCriteria(Panel.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	/**
	 * Looks for all panels where the user was the trainee
	 * 
	 * @param traineeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Panel> findAllByTrainee(Integer traineeId) {
		log.debug("Fetching all panels for trainee: " + traineeId);
		List<Panel> panels = sessionFactory.getCurrentSession().createCriteria(Panel.class)
				.add(Restrictions.eq("trainee.traineeId", traineeId))
				.addOrder(Order.desc(INTERVIEW_DATE)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		panels.parallelStream();
		return panels;
	}

	
	/**
	 * Looks for all panels with the panel status: Repanel 
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Panel> findAllRepanel() {
		log.debug("Fetching all panels with the status Repanel");
		return sessionFactory.getCurrentSession().createCriteria(Panel.class)
				.add(Restrictions.eq("status", PanelStatus.Repanel))
				.addOrder(Order.desc(INTERVIEW_DATE)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	/**
	 * Looks for all panels within past 14 days (includes today and 13 days before)
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Panel> findBiWeeklyPanels() {
		log.debug("Fetching all panels within last 14 days");
		Query query = sessionFactory.getCurrentSession().createQuery(
				"FROM Panel p WHERE p.interviewDate >= TRUNC(SYSDATE) - 13");
		return query.list();
	}

	/**
	 * 
	 * Convenience method only. Not practical in production since panels must
	 * be registered in the Salesforce with a matching email address.
	 * 
	 * @param panel
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(Panel panel) {
		log.debug("Save panel " + panel);
		sessionFactory.getCurrentSession().save(panel);
	}

	/**
	 * Find panel by the given identifier. Initialize panel feedback
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Panel findOne(Integer panelId) {
		log.debug("Find panel by id: " + panelId);
		return (Panel) sessionFactory.getCurrentSession().get(Panel.class, panelId);
	}

	/**
	 * Updates a panel in the database.
	 * 
	 * @param panel
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(Panel panel) {
		log.debug("Update panel " + panel);
		sessionFactory.getCurrentSession().saveOrUpdate(panel);
	}

	/**
	 * Convenience method only. Deletes a panel from the database. Panel
	 * will still be registered with a Salesforce account.
	 * 
	 * @param panel id
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(int panelId) {
		log.debug("Delete panel " + panelId);
		Panel panel = findOne(panelId);
		if (panel != null) {
			sessionFactory.getCurrentSession().delete(panel);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Trainee> findAllTraineesAndPanels(Integer batchId) {
		log.debug("get trainees and their panelInterviews from " + batchId);
		return (List<Trainee>)sessionFactory.getCurrentSession()
				.createCriteria(Trainee.class)
				.add(Restrictions.eq("batch.batchId", batchId))
				.add(Restrictions.ne("trainingStatus", TrainingStatus.Dropped))
				.createCriteria("panelInterviews", JoinType.LEFT_OUTER_JOIN)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}
	
}
