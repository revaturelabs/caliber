package com.revature.caliber.revpro;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.SkillType;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.revpro.beans.RevProBatch;
import com.revature.caliber.revpro.beans.RevProEducation;
import com.revature.caliber.revpro.beans.RevProTrainee;
import com.revature.caliber.revpro.beans.RevProTrainer;
import com.revature.caliber.revpro.beans.ScreeningInformation;

@Component
public class RevProCaliberTransformer {

	private static final Logger log = Logger.getLogger(RevProCaliberTransformer.class);

	public Batch transformBatch(RevProBatch revProBatch) {
		Batch batch = new Batch();
		if (revProBatch == null) {
			return batch;
		}
		batch.setResourceId(revProBatch.getSalesforceId());
		batch.setSkillType(transformSkillType(revProBatch));
		try {
			// Date will come in as: 2018-07-02 00:00:00 +0000
			DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z");
			batch.setEndDate(dateformat.parse(revProBatch.getEndDate()));
			batch.setStartDate(dateformat.parse(revProBatch.getStartDate()));

			// gimme that old school name like "1807 May05 Java"
			StringBuilder oldSchoolBatchName = new StringBuilder();
			DateFormat dtf = new SimpleDateFormat("yyMM");
			oldSchoolBatchName.append(dtf.format(batch.getStartDate())).append(" ");
			dtf = new SimpleDateFormat("MMMdd");
			oldSchoolBatchName.append(dtf.format(batch.getStartDate())).append(" ")
					.append(batch.getSkillType().toString());
			batch.setTrainingName(oldSchoolBatchName.toString());
		} catch (ParseException e) {
			log.error("Cannot parse date: " + e.getMessage());
		}
		batch.setTrainer(transformTrainer(revProBatch.getTrainer()));
		if (revProBatch.getCoTrainers() != null && revProBatch.getCoTrainers().size() > 0) {
			batch.setCoTrainer(transformCoTrainer(revProBatch.getCoTrainers().get(0)));
		}
		// batch.setLocation(revProBatch.getLocation()); // don't want the TL-00014
		// thingy
		return batch;
	}

	// Tranform batchtrainers into trainers
	public Trainer transformTrainer(RevProTrainer revProTrainer) {
		Trainer trainer = new Trainer();
		if (revProTrainer == null) {
			return null;
		}
		trainer.setName(revProTrainer.getFirstName() + " " + revProTrainer.getLastName());
		trainer.setEmail(revProTrainer.getEmail());
		log.debug("RevProCaliberTransformer created trainer: " + trainer);
		return trainer;
	}

	/**
	 * Salesforce v1 stores the Cotrainer with the Trainee record and not the
	 * Trainer record... #hackamania
	 * 
	 * @param revProTrainer
	 * @return
	 */
	public Trainer transformCoTrainer(RevProTrainer revProTrainer) {
		Trainer trainer = new Trainer();
		if (revProTrainer == null) {
			return null;
		}
		trainer.setName(revProTrainer.getFirstName() + " " + revProTrainer.getLastName());
		// generate email
		String email = guessEmail(revProTrainer.getFirstName() + " " + revProTrainer.getLastName());
		if (email != null) {
			trainer.setEmail(email);
			log.debug("Guessed co-trainer email as: " + email);
			return trainer;
		} else {
			return null;
		}
	}

	/**
	 * Guess the email as first.last@revature.com If it cannot be computed, return
	 * null and not all to be saved as cotrainer
	 * 
	 * @param name
	 * @return
	 */
	public String guessEmail(String name) {
		if (name == null) {
			return null;
		}
		String[] firstLast = name.split(" ");
		if (firstLast.length < 2) {
			return null; // trainer has only 1 name.. looking at you, Cher
		}
		return new StringBuilder().append(firstLast[0]).append(".").append(firstLast[1]).append("@revature.com")
				.toString().toLowerCase();
	}

	public SkillType transformSkillType(RevProBatch revProBatch) {
		String stringSkillType = revProBatch.getSkill();
		if (stringSkillType == null) {
			return SkillType.OTHER;
		}
		return transformSkillTypeHelper(stringSkillType);

	}

	private SkillType transformSkillTypeHelper(String skillType) {
		log.debug("Converting skill type as " + skillType);
		return SkillType.fromString(skillType);
	}

	public Trainee transformTrainee(RevProTrainee revProTrainee) {

		Trainee trainee = new Trainee();

		if (revProTrainee == null) {
			return trainee;
		}

		// required fields
		try {
			trainee.setName(revProTrainee.getFirstName() + " " + revProTrainee.getLastName());
			trainee.setEmail(revProTrainee.getEmail());
			trainee.setTrainingStatus(transformStatus(revProTrainee));
		} catch (Exception e) {
			return new Trainee(); 
		}

		// not required fields. if any exception, just continue with what data we can get
		try {
			trainee.setPhoneNumber(revProTrainee.getPhone());
			trainee.setPhoneNumber(revProTrainee.getMobilePhone());
			trainee.setResourceId(revProTrainee.getSalesforceId());

			trainee.setProjectCompletion(Double.toString(revProTrainee.getCurrentProjectCompletionPercentage()));
			trainee.setRecruiterName(revProTrainee.getRecruiterEmail());

			// find highest tech screen score
			if (revProTrainee.getScreeningInformation() != null) {
				ScreeningInformation highest = revProTrainee.getScreeningInformation().get(0);
				for (ScreeningInformation screen : revProTrainee.getScreeningInformation()) {
					if (screen.getScreeningScore() > highest.getScreeningScore()) {
						highest = screen;
					}
				}
				trainee.setTechScreenerName(highest.getScreenerName());
				trainee.setTechScreenScore(highest.getScreeningScore());
			}

			// need to add: trainee setTechScreenFeedback
			for (RevProEducation edu : revProTrainee.getEducation()) {
				if (edu == null)
					break;
				trainee.setDegree(edu.getDegree());
				trainee.setMajor(edu.getMajor());
				trainee.setCollege(edu.getUniversityName());
			}
		} catch (Exception e) {
			return trainee; 
		}
		return trainee;
	}

	public TrainingStatus transformStatus(RevProTrainee salesforceTrainee) {

		String stringTrainingStatus = salesforceTrainee.getTrainingStatus();
		String dropped = "Dropped";

		if (stringTrainingStatus == null) {
			stringTrainingStatus = "Training";
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
			exp.printStackTrace();
			return TrainingStatus.Training;
		}
	}

}
