package com.revature.caliber.tasks;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.data.TraineeDAO;
import com.revature.caliber.data.TrainerDAO;
import com.revature.caliber.services.RevProBatchImportService;

@Component
public class BatchUpdate {

	private static final Logger log = Logger.getLogger(BatchUpdate.class);

	@Autowired
	private RevProAuth revProAuth;
	@Autowired
	private RevProBatchImportService importService;
	@Autowired
	private BatchDAO batchDao;
	@Autowired
	private TraineeDAO traineeDao;
	@Autowired
	private TrainerDAO trainerDao;

	/**
	 * Used cron to perform midnight execution To update batches
	 */
	//@Scheduled(cron = "0 0/2 * * * ?") // Every 2 minutes
	@Scheduled(cron = "0 0 0 * * *") // Midnight
	public void updateBatchTask() {
		try {
			log.info("Update Batch Task");
			boolean userSet = revProAuth.setUser();
			if (userSet) {
				List<Batch> caliberBatches = batchDao.findAll();
				log.debug("Caliber Batch list size: " + caliberBatches.size());
				List<Batch> salesforceBatches = importService.getAllBatches();

				compareBatches(caliberBatches, salesforceBatches);
			} else {
				log.error("Unable to perform BatchUpdate");
			}

			revProAuth.clearUser();
			log.debug("End of Update Task");
		} catch (Exception e) {
			log.fatal(e);
			e.printStackTrace();
		}
	}

	/**
	 * Grabs all batches from RevPro and all batches from Caliber. For each of the
	 * Caliber batches, it checks against all the RevPro batches.. if the Salesforce
	 * resourceIds match, then the batch details in Caliber need to be updated with
	 * the data in the RevPro. Furthermore, we then need to check each of the
	 * trainees in that batch and update their information from the RevPro as well.
	 */
	public boolean compareBatches(List<Batch> caliberBatches, List<Batch> revProBatches) {
		log.info(revProBatches);
		log.info("Comparing batches...");
		log.info(caliberBatches);
		for (int sIndex = 0; sIndex < revProBatches.size(); sIndex++) {
			if (revProBatches.get(sIndex).getResourceId() == null) {
				continue;
			}
			if (revProBatches.get(sIndex).getTrainer() == null) {
				log.debug(revProBatches.get(sIndex).getResourceId() + " batch trainer is null");
				continue;
			}
			for (int cIndex = 0; cIndex < caliberBatches.size(); cIndex++) {
				// if caliber batch does not have resourceId, it cannot be synced. continue...
				if (caliberBatches.get(cIndex).getResourceId() == null) {
					continue;
				}
				// if resourceIds are same, update all the datas with fresh Salesforce data
				if (caliberBatches.get(cIndex).getResourceId().equals(revProBatches.get(sIndex).getResourceId())) {
					log.info("Found batch match: " + revProBatches.get(sIndex).getResourceId() + " "
							+ revProBatches.get(sIndex).getTrainingName());
					// extract salesforce data and save
					updateBatch(caliberBatches.get(cIndex), revProBatches.get(sIndex));

					// extract trainee information from RevPro and update the trainees in the
					// Caliber batch
					for (Trainee trainee : caliberBatches.get(cIndex).getTrainees()) {
						for (Trainee revProTrainee : importService
								.getBatchDetails(caliberBatches.get(cIndex).getResourceId())) {
							// if caliber trainee does not have resourceId, it cannot be synced. continue...
							if (trainee.getResourceId() == null)
								continue;
							if (trainee.getResourceId().equals(revProTrainee.getResourceId())) {
								log.info("Updating trainee: " + revProTrainee.getResourceId() + " " + trainee);
								// extract salesforce data and save
								updateTrainee(trainee, revProTrainee);
							}
						}
					}
				}
			}
		}
		return true;
	}

	private void updateTrainee(Trainee caliberTrainee, Trainee revProTrainee) {
		log.info("Batch Update: syncing trainee " + revProTrainee.getResourceId());
		try {
			caliberTrainee.setTrainingStatus(revProTrainee.getTrainingStatus());
			caliberTrainee.setCollege(revProTrainee.getCollege());
			caliberTrainee.setDegree(revProTrainee.getDegree());
			caliberTrainee.setEmail(revProTrainee.getEmail());
			caliberTrainee.setMajor(revProTrainee.getMajor());
			caliberTrainee.setName(revProTrainee.getName());
			caliberTrainee.setPhoneNumber(revProTrainee.getPhoneNumber());
			caliberTrainee.setProjectCompletion(revProTrainee.getProjectCompletion());
			caliberTrainee.setRecruiterName(revProTrainee.getRecruiterName());
			caliberTrainee.setTechScreenerName(revProTrainee.getTechScreenerName());
			traineeDao.update(caliberTrainee);
		} catch (Exception e) {
			log.fatal(e);
			e.printStackTrace();
		}
	}

	private void updateBatch(Batch caliberBatch, Batch revProBatch) {
		log.info("Batch Update: syncing batch " + revProBatch.getResourceId());
		try {
			if (revProBatch.getTrainer() != null) {
				caliberBatch.setTrainer(trainerDao.findByEmail(revProBatch.getTrainer().getEmail()));
			} else {
				log.info("Trainer is null for " + revProBatch.getTrainingName());
				caliberBatch.setTrainer(trainerDao.findByEmail(RevProBatchImportService.DEFAULT_TRAINER));
				log.info("Trainer is now " + RevProBatchImportService.DEFAULT_TRAINER + " for "
						+ caliberBatch.getTrainingName());
			}
			if (revProBatch.getCoTrainer() != null) {
				caliberBatch.setCoTrainer(trainerDao.findByEmail(revProBatch.getCoTrainer().getEmail()));
				log.debug("Cotrainer for " + revProBatch.getTrainingName() + " is: " + caliberBatch.getCoTrainer());
			}
			caliberBatch.setEndDate(revProBatch.getEndDate());
			caliberBatch.setSkillType(revProBatch.getSkillType());
			caliberBatch.setStartDate(revProBatch.getStartDate());
			caliberBatch.setTrainingName(revProBatch.getTrainingName());
			caliberBatch.setTrainingType(revProBatch.getTrainingType());
			batchDao.update(caliberBatch);
		} catch (Exception e) {
			log.fatal(e);
			e.printStackTrace();
		}
	}

}
