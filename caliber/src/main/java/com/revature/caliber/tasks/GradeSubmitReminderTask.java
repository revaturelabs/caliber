package com.revature.caliber.tasks;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.revature.caliber.email.GradeSubmitMailer;

@Service
public class GradeSubmitReminderTask {

	private static final Logger log = Logger.getLogger(FlagEmailTask.class);
	
	@Autowired
	private GradeSubmitMailer gradeSubmitMailer;
	
	// at 15:00:00, on any day of month, all months, on Tuesday
	@Scheduled(cron = "0 0 15 ? * Tue")
	public void run() {
		log.info("Sending reminder emails to trainers currently conducting the batch");
		gradeSubmitMailer.run();
	}
	
	
}
