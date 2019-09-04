package com.revature.caliber.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelFeedback;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.data.PanelDAO;
import com.revature.caliber.data.TraineeDAO;
import com.revature.caliber.exceptions.MalformedRequestException;

/**
 * Provides logic concerning Panel and PanelFeedback data. Application logic has
 * no business being in a DAO nor in a Controller. This is the ideal place for
 * calculations
 *
 * @author Connor Monson
 * @author Matt 'Spring Data' Prass
 *
 */
@Service
public class PanelService {

	private static final Logger log = Logger.getLogger(PanelService.class);

	private PanelDAO panelDAO;
	private TraineeDAO traineeDAO;

	@Autowired
	public void setPanelDAO(PanelDAO panelDAO) {
		this.panelDAO = panelDAO;
	}

	@Autowired
	public void setTraineeDAO(TraineeDAO traineeDAO) {
		this.traineeDAO = traineeDAO;
	}

	/*
	 *******************************************************
	 * PANEL SERVICES
	 *
	 *******************************************************
	 */
	public void update(Panel panel) {
		log.debug("Update panel: " + panel);
		panelDAO.update(panel);
	}

	public void createPanel(Panel panel) throws MalformedRequestException {
		log.debug("Creating Panel " + panel);
		log.debug("Panel for Trainee: " +panel.getTrainee());
		List<Panel> previousPanels = findByTraineeId(panel.getTrainee().getTraineeId());
		log.debug("Trainee's Previous Panels: " + previousPanels);
		//verifying server side that the panel round field has not been tampered with
		if(previousPanels.size()+1 != panel.getPanelRound()) {
			log.warn("Failed to create panel. Panel round calculation incorrect.");
			throw new MalformedRequestException();
		}
		else {
			panelDAO.save(panel);
		}
	}

	public void deletePanel(int panelId) {
		log.debug("Deleting Panel " + panelId);
		panelDAO.delete(panelId);
	}

	public List<Panel> findAllPanels() {
		log.debug("Finding all panels");
		return panelDAO.findAll();
	}

	public List<Panel> findAllRepanel() {
		log.debug("Finding all panels");
		return panelDAO.findAllRepanel();
	}

	public Panel findById(int panelId) {
		log.debug("Getting Panel with ID " + panelId);
		Panel panel = panelDAO.findOne(panelId);
		log.debug("Got " + panel);
		return panel;
	}

	public List<Panel> findByTraineeId(int traineeId) {
		log.debug("Getting Panels with trainee ID " + traineeId);
		return panelDAO.findAllByTrainee(traineeId);
	}

	/**
	 * Finds all (undropped) trainees for a batch with their panels and returns
	 * a convenient List of Maps of Strings to use as a dto
	 * 
	 * @author emmabownes
	 * @param batchId
	 * @return list of Maps of strings to serve as a paneldto for batch overall
	 */
	public List<Map<String, String>> getBatchPanels(Integer batchId) {
		List<Trainee> trainees = panelDAO.findAllTraineesAndPanels(batchId);
		return utilAllTraineePanels(trainees);
	}

	// Utility methods
	/**
	 * Takes a List of panels for a batch and returns a Map of labels with
	 * information needed for batch overall panel table (Trainee Name, Panel
	 * Status, Repanel Topics)
	 * 
	 * @author emmabownes
	 * @param trainees
	 * @return
	 */
	private List<Map<String, String>> utilAllTraineePanels(List<Trainee> trainees) {
		Map<String, String> panelInfo;
		List<Map<String, String>> batchPanels = new ArrayList<>();
		for (Trainee t : trainees) {
			panelInfo = new HashMap<>();
			panelInfo.put("trainee", t.getName());
			List<Panel> panels = new ArrayList<>(t.getPanelInterviews());
			Panel panel;
			String status;
			if (!panels.isEmpty()) {
				panel = panels.get(0);
				status = panel.getStatus().toString();
				panelInfo.put("status", status);
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a");
				String[] dateTime = df.format(panel.getInterviewDate()).split("at");
				panelInfo.put("date", dateTime[0]);
				panelInfo.put("time", dateTime[1]);
				if(status.equalsIgnoreCase("Repanel")) {
					String topics = utilGetRepanelTopics(panel.getFeedback());
					panelInfo.put("topics", topics);
				}
			}
			batchPanels.add(panelInfo);
		}
		return batchPanels;
	}

	/**
	 * Takes a Set of panel feedbacks and returns a string which is a list of
	 * all categories which must be repaneled
	 * 
	 * @author emmabownes
	 * @author Daniel Fairbanks
	 * @param feedback
	 * @return topics
	 */
	private String utilGetRepanelTopics(Set<PanelFeedback> feedback) {
		String topics = "";
		for(PanelFeedback pf: feedback) {
			if(pf.getStatus().toString().equalsIgnoreCase("Repanel")) {
				if (topics.equals(""))
					topics += pf.getTechnology().getSkillCategory();
				else
					topics += ", " + pf.getTechnology().getSkillCategory();
			}
		}
		return topics;
	}

	/**
	 * FIND ALL PANELS WHERE THE TRAINEE'S LAST PANEL HAD STATUS REPANEL
	 */
	public List<Panel> findAllRecentRepanel() {
		log.debug("Find all trainees whose last panel had status Repanel");
		List<Trainee> trainees = traineeDAO.findAllNotDropped();
		List<Panel> result = new ArrayList<>();
		for (Trainee t : trainees) {
			List<Panel> panels = panelDAO.findAllByTrainee(t.getTraineeId());
			if (panels != null && !panels.isEmpty()) {
				Panel p = mostRecentPanel(panels);
				if (p.getStatus() == PanelStatus.Repanel) {
					result.add(p);
					continue;
				}
			}
		}
		return result;
	}

	private Panel mostRecentPanel(List<Panel> panels) {
		int max = -1;
		Panel result = null;
		for (Panel p : panels)
			if (max < p.getPanelRound()) {
				max = p.getPanelRound();
				result = p;
			}
		return result;
	}
}
