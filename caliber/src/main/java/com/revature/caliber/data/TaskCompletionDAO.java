package com.revature.caliber.data;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.beans.TrainerTaskCompletion;

@Repository
public class TaskCompletionDAO {

	private static final Logger log = Logger.getLogger(TaskCompletionDAO.class);
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    /*
     * Retrieve all completed tasks
     **/
    @SuppressWarnings("unchecked")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<TrainerTaskCompletion> findAllCompletedTasks() {
    	log.debug("Finding all completed tasks");
        return sessionFactory.getCurrentSession()
        		.createCriteria(TrainerTaskCompletion.class)
        		.createAlias("trainer", "t", JoinType.LEFT_OUTER_JOIN)
        		.createAlias("checkedBy", "c", JoinType.LEFT_OUTER_JOIN)
        		.createAlias("taskCompleted", "tc", JoinType.LEFT_OUTER_JOIN)
        		.list();
    }
    /*
     * Retrieve all completed tasks for certain trainer
     **/
    @SuppressWarnings("unchecked")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<TrainerTaskCompletion> findAllTasksByTrainerId(int id) {
    	log.debug("Finding all completed tasks by trainer with id " + id);
        return sessionFactory.getCurrentSession()
        		.createCriteria(TrainerTaskCompletion.class)
        		.createAlias("trainer", "t", JoinType.LEFT_OUTER_JOIN)
        		.createAlias("checkedBy", "c", JoinType.LEFT_OUTER_JOIN)
        		.createAlias("taskCompleted", "tc", JoinType.LEFT_OUTER_JOIN)
        		.add(Restrictions.eq("t.trainerId", id))
        		.list();
    }
    
    /*
     * Save a new Task Completion
     **/
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void saveTaskCompletion(TrainerTaskCompletion taskCompletion) {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String dateStr = df.format(date);
		Date completionDate;
		try {
			completionDate = new SimpleDateFormat("yyyy/MM/dd").parse(dateStr);
			taskCompletion.setCompletionDate(completionDate);
			sessionFactory.getCurrentSession().save(taskCompletion);
		} catch (ParseException e) {
			log.warn(e);
		}
		
	}
}
