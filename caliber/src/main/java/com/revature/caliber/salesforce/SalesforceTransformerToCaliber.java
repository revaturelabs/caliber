package com.revature.caliber.salesforce;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.SkillType;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.salesforce.beans.BatchTrainer;
import com.revature.salesforce.beans.SalesforceBatch;
import com.revature.salesforce.beans.SalesforceTrainee;

@Deprecated
public class SalesforceTransformerToCaliber {

	private static final Logger logger = Logger.getLogger(SalesforceTransformerToCaliber.class);
	
	public Batch transformBatch(SalesforceBatch salesforceBatch) {
		Batch batch = new Batch();
		if (salesforceBatch == null) {
			return batch;
		}
		batch.setResourceId(salesforceBatch.getId());
		batch.setTrainingName(salesforceBatch.getName());
		batch.setStartDate(salesforceBatch.getBatchStartDate());
		batch.setTrainer(transformTrainer(salesforceBatch.getTrainer()));
		batch.setCoTrainer(transformCoTrainer(salesforceBatch.getCotrainer()));
		batch.setEndDate(salesforceBatch.getBatchEndDate());
		batch.setResourceId(salesforceBatch.getId());
		batch.setSkillType(transformSkillType(salesforceBatch));
		batch.setLocation(salesforceBatch.getLocation());

		return batch;
	}

	// Tranform batchtrainers into trainers
	public Trainer transformTrainer(BatchTrainer batchTrainer) {
		Trainer trainer = new Trainer();
		if (batchTrainer == null) {
			return null;
		}
		trainer.setName(batchTrainer.getName());
		trainer.setEmail(batchTrainer.getEmail());
		return trainer;
	}
	
	/**
	 * Salesforce v1 stores the Cotrainer with the Trainee record and not the Trainer record... #hackamania
	 * @param batchTrainer
	 * @return
	 */
	public Trainer transformCoTrainer(SalesforceTrainee batchTrainer) {
		Trainer trainer = new Trainer();
		if (batchTrainer == null) {
			return null;
		}
		trainer.setName(batchTrainer.getName());
		// generate email
		String email = guessEmail(batchTrainer.getName());
		if (email != null) {
			trainer.setEmail(email);
			logger.info("Guessed co-trainer email as: " + email);
			return trainer;
		}else {
			return null;
		}
	}
	
	/**
	 * Guess the email as first.last@revature.com
	 * If it cannot be computed, return null and not all to be saved as cotrainer
	 * 
	 * @param name
	 * @return
	 */
	public String guessEmail(String name) {
		if(name == null) {
			return null;
		}
		String[] firstLast = name.split(" ");
		if(firstLast.length < 2) {
			return null; // trainer has only 1 name.. looking at you, Cher
		}
		return new StringBuilder().append(firstLast[0]).append(".").append(firstLast[1]).append("@revature.com").toString().toLowerCase();
	}

	public SkillType transformSkillType(SalesforceBatch salesforceBatch) {
		String stringSkillType = salesforceBatch.getSkillType();
		if (stringSkillType == null) {
			return SkillType.OTHER;
		}
		return transformSkillTypeHelper(stringSkillType);

	}

	private SkillType transformSkillTypeHelper(String skillType) {
		logger.info("Converting skill type as " + skillType);
		if(skillType.contains("Java")) {
			return SkillType.J2EE;
		}
		if(skillType.contains(".NET")) {
			return SkillType.NET;
		}
		if(skillType.contains("JTA")) {
			return SkillType.JTA;
		}
		switch (skillType) {
		case "Full Stack Java/JEE":
			return SkillType.J2EE;
		case "Full Stack .NET":
			return SkillType.NET;
		case "SDET":
			return SkillType.SDET;
		case "BPM":
			return SkillType.BPM;
		case "Appian BPM":
			return SkillType.APPIAN;	
		case "PEGA BPM":
			return SkillType.PEGA;
		case "Dynamics CRM":
			return SkillType.DYNAMICS;
		case "Microservices":
			return SkillType.MICROSERVICES;
		case "Oracle Fusion Middleware":
			return SkillType.FUSION;
		case "Salesforce":
			return SkillType.SALESFORCE;
		case "Business Analyst":
			return SkillType.BA;
		case "System Admin":
			return SkillType.SYSADMIN;
		case "QA":
			return SkillType.QA;
		case "Full Stack JTA":
			return SkillType.JTA;
		default:
			return SkillType.OTHER;
		}
	}

	public Trainee transformTrainee(SalesforceTrainee salesforceTrainee) {

		Trainee trainee = new Trainee();

		if (salesforceTrainee == null) {
			return trainee;
		}

		trainee.setName(salesforceTrainee.getName());
		trainee.setEmail(salesforceTrainee.getEmail());
		trainee.setTrainingStatus(transformStatus(salesforceTrainee));
		trainee.setPhoneNumber(salesforceTrainee.getPhone());
		trainee.setPhoneNumber(salesforceTrainee.getMobilePhone());
		trainee.setResourceId(salesforceTrainee.getId());
		trainee.setCollege(salesforceTrainee.getCollege().getName());
		trainee.setProjectCompletion(salesforceTrainee.getProjectCompletion());
		trainee.setRecruiterName(salesforceTrainee.getRecruiter().getName());
		trainee.setTechScreenerName(salesforceTrainee.getScreener());

		// need to add: trainee setTechScreenFeedback
		String degree = "None";
		if (salesforceTrainee.getAssociates() != null){
			degree = "Associates";
			trainee.setMajor(salesforceTrainee.getAssociates());
		}
		if (salesforceTrainee.getBachelors() != null){
			degree = "Bachelors";
			trainee.setMajor(salesforceTrainee.getBachelors());
		}
		if (salesforceTrainee.getMasters() != null){
			degree = "Masters";
			trainee.setMajor(salesforceTrainee.getMasters());
		}
		trainee.setDegree(degree);
		return trainee;
	}

	public TrainingStatus transformStatus(SalesforceTrainee salesforceTrainee) {

		String stringTrainingStatus = salesforceTrainee.getTrainingStatus();
		String dropped = "Dropped";

		if (stringTrainingStatus == null) {
			stringTrainingStatus = "";
		}
		switch (stringTrainingStatus) {
		case "Declined Offer":
			stringTrainingStatus = dropped;
			return transformStatusHelper(stringTrainingStatus);
		case "Did Not Show":
			stringTrainingStatus = dropped;
			return transformStatusHelper(stringTrainingStatus);
		case "Rejected":
			stringTrainingStatus = dropped;
			return transformStatusHelper(stringTrainingStatus);
		case "Terminated":
			stringTrainingStatus = dropped;
			return transformStatusHelper(stringTrainingStatus);
		default:
			return transformStatusHelper(stringTrainingStatus);
		}
	}

	private TrainingStatus transformStatusHelper(String stringTrainingStatus) {
		try {
			return TrainingStatus.valueOf(stringTrainingStatus);
		} catch (IllegalArgumentException exp) {
			logger.debug("Exp caught in SalesforceTransformer.transformStatusHelper");
			logger.debug(exp);
			return TrainingStatus.Training;
		}
	}
}
