package com.revature.caliber.test.unit;

import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.email.GradeSubmitMailer;

public class GradesSubmitTest extends CaliberTest{

	private static final Logger log = Logger.getLogger(GradesSubmitTest.class);
	
	@Autowired
	private GradeSubmitMailer gradeSubmitMailer;
	
	@Test
	public void testTrainer() {
		Set<Trainer> trainers = gradeSubmitMailer.getTrainersWhoNeedToSubmitGrades();
		log.info(trainers);
	}
	
}
