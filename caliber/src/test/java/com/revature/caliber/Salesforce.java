package com.revature.caliber;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.data.SalesforceDAO;

public class Salesforce extends CaliberTest{

	private static final Logger log = Logger.getLogger(Salesforce.class);
	
	@Autowired
	private SalesforceDAO salesforceDAO;
	
	@Test
	public void go(){
		log.debug("Getting batches");
		List<Batch> batches = salesforceDAO.getAllRelevantBatches();
		log.debug(batches);
		log.debug("Getting batch details");
		List<Trainee> trainees = salesforceDAO.getBatchDetails("a0Yi000000F0b7I");
		log.debug(trainees);
	}
	
}
