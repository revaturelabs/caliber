package com.revature.caliber.tasks;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.revature.caliber.email.FlagAlertMailer;

@Service
public class FlagEmailTask {

	private static final Logger log = Logger.getLogger(FlagEmailTask.class);
	
	@Autowired
	private FlagAlertMailer flagAlertMailer;
	
	// at 12:00:00, on any day of month, all months, on Monday
	@Scheduled(cron = "0 0 12 ? * Mon")
	public void run() {
		log.info("Sending flag emails to VPs");
		flagAlertMailer.run();
	}
	
}
