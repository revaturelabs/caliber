package com.revature.caliber.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.beans.AssessmentType;
import com.revature.caliber.beans.InterviewFormat;
import com.revature.caliber.beans.NoteType;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.beans.QCStatus;
import com.revature.caliber.beans.SkillType;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.beans.TrainingType;

/**
 * Provides enumerated types to the UI
 * 
 * @author Patrick Walsh
 * @author Stanley Chouloute
 *
 */
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "types", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class TypeController {

	private static final Logger log = Logger.getLogger(TypeController.class);

	/*
	 *******************************************************
	 * TODO TYPE SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * Get Skill types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/skill/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'STAGING','TRAINER','QC','PANEL')")
	public ResponseEntity<List<String>> allSkillTypes() {
		log.debug("Fetching skill types");
		List<String> types = Stream.of(SkillType.values()).map(Enum::toString).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get Training types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/training/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'STAGING', 'QC', 'TRAINER','PANEL')")
	public ResponseEntity<List<String>> allTrainingTypes() {
		log.debug("Fetching training types");
		List<String> types = Stream.of(TrainingType.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get Training Status types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/trainingstatus/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'STAGING', 'QC', 'TRAINER','PANEL')")
	public ResponseEntity<List<String>> allTrainingStatusTypes() {
		log.debug("Fetching training status types");
		List<String> types = Stream.of(TrainingStatus.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get note types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/note/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'QC', 'STAGING','PANEL')")
	public ResponseEntity<List<String>> allNoteTypes() {
		log.debug("Fetching note types");
		List<String> types = Stream.of(NoteType.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get QC Status types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/qcstatus/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'QC', 'STAGING','PANEL')")
	public ResponseEntity<List<String>> allQCStatusTypes() {
		log.debug("Fetching QC status types");
		List<String> types = Stream.of(QCStatus.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get assessment types for dropdown selection on the UI
	 *
	 * @param assessment the assessment
	 * @return the response entity
	 */
	@RequestMapping(value = "/assessment/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'STAGING')")
	public ResponseEntity<List<String>> allAssessmentTypes() {
		log.debug("Fetching assessment types");
		List<String> types = Stream.of(AssessmentType.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get Trainer Tier for dropdown selection on the UI
	 *
	 * @param Role
	 *            the Tier
	 * @return the response entity
	 */
	@RequestMapping(value = "/trainer/role/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'STAGING','PANEL')")
	public ResponseEntity<List<String>> allTrainerRoles() {
		log.debug("Fetching Trainer Roles");
		// Used toString to Display the roles without the underscore
		List<String> types = Stream.of(TrainerRole.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get Panel Status for dropdown selection on the UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/panelstatus/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'PANEL')")
	public ResponseEntity<List<String>> allPanelStatus() {
		log.debug("Fetching Panel Status");
		List<String> types = Stream.of(PanelStatus.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get Interview Format for dropdown selection on the UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/interviewformat/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'PANEL')")
	public ResponseEntity<List<String>> allInterviewFormat() {
		log.debug("Fetching Interview Format");
		List<String> types = Stream.of(InterviewFormat.values()).map(Enum::name).collect(Collectors.toList());
		types = hyphenate(types);
		return new ResponseEntity<>(types, HttpStatus.OK);
	}
	
    public static List<String> hyphenate(List<String> list) {
        List<String> hyphenatedlist = new ArrayList<>();
        for(String s: list) {
        hyphenatedlist.add(s.replace("_", "-"));
        }
		return hyphenatedlist;
    }
}
