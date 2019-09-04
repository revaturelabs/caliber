package com.revature.caliber.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Assessment;
import com.revature.caliber.data.AssessmentDAO;

/**
 * Used for assessment CRUD operations. Includes both Trainer and QC
 * assessments. Application logic has no business being in a DAO nor in a
 * Controller. This is the ideal place for calculations
 * 
 * @author Patrick Walsh
 *
 */
@Service
public class AssessmentService {

	private static final Logger log = Logger.getLogger(AssessmentService.class);
	private AssessmentDAO assessmentDAO;

	@Autowired
	public void setAssessmentDAO(AssessmentDAO assessmentDAO) {
		this.assessmentDAO = assessmentDAO;
	}

	/*
	 *******************************************************
	 *	ASSESSMENT SERVICES
	 *
	 *******************************************************
	 */
	
	/**
	 * SAVE ASSESSMENT
	 * @param assessment
	 */
	public void save(Assessment assessment) {
		log.debug("Saving assessment " + assessment);
		assessmentDAO.save(assessment);
	}

	/**
	 * FIND ASSESSMENT BY ID
	 * @param assessmentId
	 * @return
	 */
	public Assessment findAssessment(long assessmentId) {
		log.debug("Finding one assessment " + assessmentId);
		return assessmentDAO.findOne(assessmentId);
	}

	/**
	 * FIND ALL ASSESSMENTS
	 * @return
	 */
	public List<Assessment> findAllAssessments() {
		log.debug("Find all assessments");
		return assessmentDAO.findAll();
	}

	/**
	 * FIND ASSESSMENT BY WEEK
	 * @param batch
	 * @param week
	 * @return
	 */
	public List<Assessment> findAssessmentByWeek(Integer batchId, Integer week) {
		log.debug("Find assessment by week number " + week + " for batch " + batchId + " ");
		return assessmentDAO.findByWeek(batchId, week);
	}
	
	/**
	 * FIND ASSESSMENT BY BATCHID
	 * @param 
	 * @return
	 */
	public List<Assessment> findAssessmentByBatchId(Integer batchId) {
		log.debug("Find assessment by batchId " + batchId + " ");
		return assessmentDAO.findByBatchId(batchId);
	}

	/**
	 * UPDATE ASSESSMENT
	 * @param assessment
	 */
	public Assessment update(Assessment assessment) {
		log.debug("Updating assessment " + assessment);
		
		assessmentDAO.update(assessment);
		return assessment;
	}

	/**
	 * DELETE ASSESSMENT
	 * @param assessment
	 */
	public void delete(Assessment assessment) {
		log.debug("Deleting assessment " + assessment);
		//load assessment into persistent state
		Assessment record = assessmentDAO.findOne(assessment.getAssessmentId());
		assessmentDAO.delete(record);
	}

}
