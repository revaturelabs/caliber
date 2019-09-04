package com.revature.caliber.test.integration;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.data.PanelDAO;
import com.revature.caliber.services.PanelService;

public class PanelServiceTest extends CaliberTest {
	
	private static final Logger log = Logger.getLogger(PanelServiceTest.class);
	PanelDAO dao;
	
	@Autowired
	PanelService panelService;
	
	@Test
	public void findRepanl() {
		log.debug("Testing finding trainees who need repanel");
		List<Panel> panelList = panelService.findAllRecentRepanel();
		for (Panel p : panelList)
			if (p.getStatus() != PanelStatus.Repanel)
				fail();
	}

}
