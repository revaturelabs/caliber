package com.revature.caliber.data;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.beans.Note;
import com.revature.caliber.beans.NoteType;
import com.revature.caliber.beans.TrainingStatus;

/**
 * 
 * @author Patrick Walsh
 * @author Ateeb Khawaja
 *
 */
@Repository
public class NoteDAO {

	private static final Logger log = Logger.getLogger(NoteDAO.class);
	private SessionFactory sessionFactory;
	private static final String BATCH = "batch";
	private static final String B_TRAINEES = "b.trainees";
	private static final String T_TRAINING_STATUS = "t.trainingStatus";
	private static final String B_BATCH_ID = "b.batchId";
	private static final String TRAINEE = "trainee";
	private static final String T_TRAINEE_ID = "t.traineeId";
	private static final String QC_FEEDBACK = "qcFeedback";
	private static final int MONTHS_BACK = -1;
	private static final String BATCH_BATCH_ID = "batch.batchId";


	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Save a new note
	 * 
	 * @param note
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int save(Note note) {
		log.debug("Saving Note " + note);
		return (int) sessionFactory.getCurrentSession().save(note);
	}

	/**
	 * Update note contents
	 * 
	 * @param note
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(Note note) {
		log.debug("Updating Note " + note);
		sessionFactory.getCurrentSession().saveOrUpdate(note);
	}

	/**
	 * Returns all batch-level notes for a given week. Only notes written by
	 * trainers are returned.
	 * 
	 * @param batchId
	 * @param week
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findBatchNotes(Integer batchId, Integer week) {
		log.debug("Finding batch notes for week " + week + BATCH + batchId);
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(B_TRAINEES, "t", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq(B_BATCH_ID, batchId))
				.add(Restrictions.eq("week", week.shortValue()))
				.add(Restrictions.eq(QC_FEEDBACK, false))
				.add(Restrictions.eq("type", NoteType.BATCH))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	/**
	 * Returns all individual notes for a given week. Only notes written by
	 * trainers are returned.
	 * 
	 * @param traineeId
	 * @param week
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findIndividualNotes(Integer batchId, Integer week) {
		log.debug("Finding individual notes for week " + week + " " + BATCH + batchId);
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(TRAINEE, "t", JoinType.LEFT_OUTER_JOIN)
				.createAlias(B_TRAINEES, "bt", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq(B_BATCH_ID, batchId))
				.add(Restrictions.eq("week", week.shortValue()))
				.add(Restrictions.eq("type", NoteType.TRAINEE))
				.add(Restrictions.eq(QC_FEEDBACK, false))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Returns Trainee note for the week
	 * 
	 * @param traineeId
	 * @param week
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Note findTraineeNote(Integer traineeId, Integer week) {
		return (Note) sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(TRAINEE, "t", JoinType.LEFT_OUTER_JOIN)
				.createAlias(B_TRAINEES, "bt", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq(T_TRAINEE_ID, traineeId))
				.add(Restrictions.eq("week", week.shortValue())).add(Restrictions.eq("type", NoteType.TRAINEE))
				.add(Restrictions.eq(QC_FEEDBACK, false))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
	}

	/**
	 * Returns QCTrainee note for the week(Michael)
	 * 
	 * @param traineeId
	 * @param week
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Note findQCTraineeNote(Integer traineeId, Integer week) {
		return (Note) sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(TRAINEE, "t", JoinType.LEFT_OUTER_JOIN)
				.createAlias(B_TRAINEES, "bt", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq(T_TRAINEE_ID, traineeId))
				.add(Restrictions.eq("week", week.shortValue())).add(Restrictions.eq("type", NoteType.QC_TRAINEE))
				.add(Restrictions.eq(QC_FEEDBACK, true)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.uniqueResult();
	}

	/**
	 * Returns batch note for the batch for the week
	 * 
	 * @param batchId
	 * @param week
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Note findQCBatchNotes(Integer batchId, Integer week) {
		log.debug("Finding QC batch notes for week " + week + " " + BATCH + batchId);
		return (Note) sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(B_TRAINEES, "t", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq("type", NoteType.QC_BATCH))
				.add(Restrictions.eq(BATCH_BATCH_ID, batchId)).add(Restrictions.eq("week", week.shortValue()))
				.add(Restrictions.eq(QC_FEEDBACK, true)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.uniqueResult();
	}

	/**
	 * Returns all batch-level notes for a particular batch.
	 * 
	 * @param batchId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findAllBatchQcNotes(Integer batchId) {
		log.debug("Finding All batch notes for batch " + BATCH + batchId);
		Calendar endDateLimit = Calendar.getInstance();	
		endDateLimit.add(Calendar.MONTH, MONTHS_BACK);
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(B_TRAINEES, "t", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq("type", NoteType.QC_BATCH))
				.add(Restrictions.eq(B_BATCH_ID, batchId))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Returns all individual notes written by QC for a given week.
	 * 
	 * @param traineeId
	 * @param week
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findQCIndividualNotes(Integer traineeId, Integer week) {
		log.debug("Finding QC individual notes for week " + week + " for trainee: " + traineeId);
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(TRAINEE, "t", JoinType.LEFT_OUTER_JOIN)
				.createAlias(B_TRAINEES, "bt", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq(T_TRAINEE_ID, traineeId))
				.add(Restrictions.eq("week", week.shortValue()))
				.add(Restrictions.eq("type", NoteType.QC_TRAINEE))
				.add(Restrictions.eq(QC_FEEDBACK, true))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	/**
	 * Returns all individual notes written by QC for a given week.
	 * 
	 * @param traineeId
	 * @param week
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findQCIndividualNotes(Integer traineeId) {
		log.debug("Finding QC individual notes for trainee: " + traineeId);
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(TRAINEE, "t", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq(T_TRAINEE_ID, traineeId))
				.add(Restrictions.eq("type", NoteType.QC_TRAINEE))
				.add(Restrictions.eq(QC_FEEDBACK, true))
				.addOrder(Order.asc("week"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Returns all batch-level notes for a given week.
	 * 
	 * @param batchId
	 * @param week
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findAllBatchNotes(Integer batchId, Integer week) {
		log.debug("Finding All batch notes for week " + week + " " + BATCH + batchId);
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(B_TRAINEES, "t", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq("type", NoteType.BATCH))
				.add(Restrictions.eq(BATCH_BATCH_ID, batchId)).add(Restrictions.eq("week", week.shortValue()))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Returns all individual trainee notes for a particular week.
	 * 
	 * @param traineeId
	 * @param week
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findAllIndividualNotes(Integer traineeId, Integer week) {
		log.debug("Finding All individual notes for week " + week + " for trainee: " + traineeId);
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(TRAINEE, "t", JoinType.LEFT_OUTER_JOIN)
				.createAlias(B_TRAINEES, "bt", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq("type", NoteType.TRAINEE))
				.add(Restrictions.eq(T_TRAINEE_ID, traineeId))
				.add(Restrictions.eq("week", week.shortValue()))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Returns all notes for a particular trainee. Used for cumulative reporting
	 * on a single trainee. Only trainer notes are displayed.
	 * 
	 * @param traineeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findAllPublicIndividualNotes(Integer traineeId) {
		log.debug("Finding All individual notes for trainee: " + traineeId);
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(TRAINEE, "t", JoinType.LEFT_OUTER_JOIN)
				.createAlias(B_TRAINEES, "bt", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq("type", NoteType.TRAINEE))
				.add(Restrictions.eq(T_TRAINEE_ID, traineeId)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.addOrder(Order.asc("week")).list();
	}

	/**
	 * Returns all qc notes for a batch
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findAllQCBatchNotes(Integer batchId) {
		log.debug("Find All QC Batch notes");
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(B_TRAINEES, "t", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq(B_BATCH_ID, batchId)).add(Restrictions.eq(QC_FEEDBACK, true))
				.add(Restrictions.eq("type", NoteType.QC_BATCH))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.asc("week")).list();
	}

	/**
	 * Returns all QC notes for trainee in the batch for the week
	 * 
	 * @return
	 * @param BatchID
	 * @param week Number
	 * @return List of notes
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findAllQCTraineeNotes(Integer batchId, Integer week) {
		log.debug("Find All QC Trainee notes");
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(TRAINEE, "t", JoinType.LEFT_OUTER_JOIN)
				.createAlias(B_TRAINEES, "bt", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq(B_BATCH_ID, batchId)).add(Restrictions.eq("week", week.shortValue()))
				.add(Restrictions.eq(QC_FEEDBACK, true)).add(Restrictions.eq("type", NoteType.QC_TRAINEE))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.asc("week")).list();
	}

	/**
	 * Returns all QC notes for trainee in the batch
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findAllQCTraineeOverallNotes(Integer traineeId) {
		log.debug("Find All QC Trainee notes for that trainee");
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(TRAINEE, "t", JoinType.LEFT_OUTER_JOIN)
				.createAlias(B_TRAINEES, "bt", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq(T_TRAINEE_ID, traineeId)).add(Restrictions.eq(QC_FEEDBACK, true))
				.add(Restrictions.eq("type", NoteType.QC_TRAINEE))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.asc("week")).list();
	}


	/**
	 * Returns all QC notes for trainee in the batch for all weeks
	 * @author Junaid syed - Team-Qcator
	 * @batchId
	 * @return
	 */

	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Note> findAllQCTraineeNotesForAllWeeks(Integer batchId) {
		log.debug("Find All QC Trainee notes");
		return sessionFactory.getCurrentSession().createCriteria(Note.class)
				.createAlias(BATCH, "b")
				.createAlias(TRAINEE, "t")
				.createAlias(B_TRAINEES, "bt", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ne(T_TRAINING_STATUS, TrainingStatus.Dropped))
				.add(Restrictions.eq(B_BATCH_ID, batchId))
				.add(Restrictions.eq(QC_FEEDBACK, true)).add(Restrictions.eq("type", NoteType.QC_TRAINEE))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.asc("trainee")).addOrder(Order.asc("week")).list();

	}

}