package com.revature.caliber.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Note;
import com.revature.caliber.beans.NoteType;
import com.revature.caliber.beans.QCStatus;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TraineeFlag;
import com.revature.caliber.data.GradeDAO;
import com.revature.caliber.data.NoteDAO;
import com.revature.caliber.data.TraineeDAO;

/**
 * Used to add grades for assessments and input notes Application logic has no
 * business being in a DAO nor in a Controller. This is the ideal place for
 * calculations
 * 
 * @author Patrick Walsh
 *
 */
@Service
public class EvaluationService {

	private static final Logger log = Logger.getLogger(EvaluationService.class);
	private GradeDAO gradeDAO;
	private NoteDAO noteDAO;
	private TraineeDAO traineeDAO;
	private static final String FINDING_WEEK = "Finding week ";
	private static final String WEEK_STATIC_FACTOR = "0";

	@Autowired
	public void setGradeDAO(GradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}

	@Autowired
	public void setNoteDAO(NoteDAO noteDAO) {
		this.noteDAO = noteDAO;
	}

	@Autowired
	public void setTraineeDAO(TraineeDAO traineeDAO) {
		this.traineeDAO = traineeDAO;
	}

	/*
	 *******************************************************
	 * GRADING SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * SAVE GRADE
	 * 
	 * @param grade
	 */
	public void save(Grade grade) {
		log.debug("Saving grade: " + grade);
		gradeDAO.save(grade);
	}

	/**
	 * UPDATE GRADE
	 * 
	 * @param grade
	 */
	public void update(Grade grade) {
		log.debug("Updating grade: " + grade);
		gradeDAO.update(grade);
	}

	/**
	 * FIND GRADES BY WEEK
	 * 
	 * @param batchId
	 * @param week
	 * @return
	 */
	public Map<Integer, List<Grade>> findGradesByWeek(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " grades for batch: " + batchId);
		List<Grade> grades = gradeDAO.findByWeek(batchId, week);
		Map<Integer, List<Grade>> table = new HashMap<>();
		for (Grade grade : grades) {
			Integer key = grade.getTrainee().getTraineeId();
			if (table.containsKey(grade.getTrainee().getTraineeId())) {
				// eliminate nested records first
				grade.getAssessment().setBatch(null);
				// get the trainee's assessments and add the new assessment
				table.get(key).add(grade);
			} else {
				// eliminate nested records first
				grade.getAssessment().setBatch(null);
				// add the first assessment
				List<Grade> assessments = new ArrayList<>();
				assessments.add(grade);
				table.put(key, assessments);
			}
		}
		return table;
	}

	/*
	 *******************************************************
	 * NOTE SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * SAVE NOTE
	 * 
	 * @param note
	 */
	public int save(Note note) {
		log.debug("Saving note: " + note);
		int key = noteDAO.save(note);
		checkIfTraineeShouldBeFlagged(note);
		return key;
	}

	/**
	 * UPDATE NOTE
	 * 
	 * @param note
	 */
	public void update(Note note) {
		log.debug("Updating note: " + note);
		noteDAO.update(note);
		checkIfTraineeShouldBeFlagged(note);
	}

	/**
	 * Trainees can be flagged automatically if they have: 2 Red or 2 consecutive
	 * yellow QCs
	 * 
	 * @param note
	 */
	public void checkIfTraineeShouldBeFlagged(Note note) {
		if (note != null && note.getType().equals(NoteType.QC_TRAINEE)) {
			// trainee should not be flagged on week 1 itself
			if (note.getWeek() < 2)
				return;

			// get a list of all notes in week ASC order
			List<Note> notes = noteDAO.findQCIndividualNotes(note.getTrainee().getTraineeId());

			// loop over the notes to find if they have 2 or more consecutive yellow or red
			// QCs
			boolean flagged = false;
			// current week
			try {
				flagged = (notes.get(note.getWeek() - 1).getQcStatus().equals(QCStatus.Poor)
						|| notes.get(note.getWeek() - 1).getQcStatus().equals(QCStatus.Average))
						// previous week
						&& (notes.get(note.getWeek() - 2).getQcStatus().equals(QCStatus.Poor)
								|| notes.get(note.getWeek() - 2).getQcStatus().equals(QCStatus.Average)) ? true : false;
				if (flagged) {
					// save the flag status in database
					Trainee trainee = traineeDAO.findOne(note.getTrainee().getTraineeId());
					trainee.setFlagStatus(TraineeFlag.RED);
					// concat a generated flag message at the end of the current trainee notes
					if (trainee.getFlagNotes() != null
							&& !trainee.getFlagNotes().contains("Trainee was automatically flagged by Caliber"))
						trainee.setFlagNotes("Trainee was automatically flagged by Caliber. " + trainee.getFlagNotes());
					else
						trainee.setFlagNotes("Trainee was automatically flagged by Caliber. ");

					traineeDAO.update(trainee);
				} else {
					// remove the flag status in database
					Trainee trainee = traineeDAO.findOne(note.getTrainee().getTraineeId());
					trainee.setFlagStatus(TraineeFlag.NONE);
					traineeDAO.update(trainee);
				}
			} catch (Exception e) {
				log.debug("Failed to autoflag associate with note: " + note);
				log.debug(e);
			}
		}
	}

	/**
	 * FIND WEEKLY BATCH NOTES (TRAINER/PUBLIC)
	 * 
	 * @param batch
	 * @param week
	 * @return
	 */
	public List<Note> findBatchNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " batch notes for batch: " + batchId);
		return noteDAO.findBatchNotes(batchId, week);
	}

	/**
	 * FIND WEEKLY INDIVIDUAL NOTES (TRAINER/PUBLIC)
	 * 
	 * @param trainee
	 * @param week
	 * @return
	 */
	public List<Note> findIndividualNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " individual notes for batch: " + batchId);
		return noteDAO.findIndividualNotes(batchId, week);
	}

	/**
	 * FIND TRAINEE NOTE FOR THE WEEK
	 * 
	 * @param trainee
	 * @param week
	 * @return
	 */
	public Note findTraineeNote(Integer traineeId, Integer week) {
		return noteDAO.findTraineeNote(traineeId, week);
	}

	/**
	 * FIND QCTRAINEE NOTE FOR THE WEEK(Michael)
	 * 
	 * @param trainee
	 * @param week
	 * @return
	 */
	public Note findQCTraineeNote(Integer traineeId, Integer week) {
		return noteDAO.findQCTraineeNote(traineeId, week);
	}

	/**
	 * FIND WEEKLY QC BATCH NOTES (NOT FOR TRAINERS)
	 * 
	 * @param batch
	 * @param week
	 * @return
	 */
	public Note findQCBatchNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " QC batch notes for batch: " + batchId);
		return noteDAO.findQCBatchNotes(batchId, week);
	}

	/**
	 * FIND WEEKLY QC INDIVIDUAL NOTES (NOT FOR TRAINERS)
	 * 
	 * @param trainee
	 * @param week
	 * @return
	 */
	public List<Note> findQCIndividualNotes(Integer traineeId, Integer week) {
		log.debug(FINDING_WEEK + week + " QC individual notes for trainee: " + traineeId);
		return noteDAO.findQCIndividualNotes(traineeId, week);
	}

	/**
	 * FIND ALL WEEKLY BATCH NOTES (VP ONLY)
	 * 
	 * @param batch
	 * @param week
	 * @return
	 */
	public List<Note> findAllBatchNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " batch notes for batch: " + batchId);
		return noteDAO.findAllBatchNotes(batchId, week);
	}

	/**
	 * Find all qc trainee notes
	 * 
	 * @return
	 */
	public List<Note> findAllQCTraineeNotes(Integer batchId, Integer week) {
		log.debug("Find All QC Trainee Notes");
		return noteDAO.findAllQCTraineeNotes(batchId, week);
	}

	public List<Note> findAllIndividualNotesOverall(Integer traineeId) {
		log.debug("Find Overall notes for trainee " + traineeId);
		return noteDAO.findAllPublicIndividualNotes(traineeId);
	}

	/**
	 * Find all qc trainee notes
	 * 
	 * @return
	 */
	public List<Note> findAllQCTraineeOverallNotes(Integer traineeId) {
		log.debug("Find All QC Trainee Notes for that trainee");
		return noteDAO.findAllQCTraineeOverallNotes(traineeId);
	}

	public void calculateAverage(Integer weekId, Batch batch) {
		if (batch != null) {
			Note overallNote = noteDAO.findQCBatchNotes(batch.getBatchId(), weekId);
			if (overallNote == null) {
				log.debug("Creating a new overall Note for week " + weekId);
				overallNote = new Note();
				overallNote.setBatch(batch);
				overallNote.setWeek(weekId.shortValue());
				overallNote.setQcStatus(QCStatus.Undefined);
				overallNote.setType(NoteType.QC_BATCH);
				overallNote.setMaxVisibility(TrainerRole.ROLE_PANEL);
				overallNote.setQcFeedback(true);
				noteDAO.save(overallNote);
			}
			log.debug("Calculating Average of note of week");
			double average = 0.0f;
			List<Note> traineeNoteList = noteDAO.findAllQCTraineeNotes(batch.getBatchId(), weekId);
			int denominator = traineeNoteList.size();
			for (Note note : traineeNoteList) {
				switch (note.getQcStatus()) {
				case Superstar:
					average += 4;
					break;
				case Good:
					average += 3;
					break;
				case Average:
					average += 2;
					break;
				case Poor:
					average += 1;
					break;
				default:
					denominator--;
				}
			}
			if (denominator != 0) {
				average /= denominator;
			} else {
				average = 0.0f;
			}
			if (average >= 2.5) {
				overallNote.setQcStatus(QCStatus.Good);
			} else if (average >= 2 && average < 2.5) {
				overallNote.setQcStatus(QCStatus.Average);
			} else if (average > 0 && average < 2) {
				overallNote.setQcStatus(QCStatus.Poor);
			} else {
				overallNote.setQcStatus(QCStatus.Undefined);
			}
			log.debug("The calculated average is: " + overallNote.getQcStatus());
			noteDAO.update(overallNote);
		} else {
			log.warn("The ASSIGN_NOTE_BATCH_ID Stored Procedure hasn't been ran yet to update Note's batch id.");
		}
	}

	/**
	 * Find all qc trainee notes for all weeks
	 * 
	 * @return
	 */
	public List<List<Note>> findAllQCTraineeNotesForAllWeeks(Integer batchId) {
		log.debug("Find All QC Trainee Notes");
		List<Note> notes = noteDAO.findAllQCTraineeNotesForAllWeeks(batchId);
		ArrayList<List<Note>> noteFormatted2d = new ArrayList<>();
		notes = notes.stream().collect(
				Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(note -> {
					String weekAsString = Short.toString(note.getWeek());
					return note.getTrainee().getName()
							+ ((weekAsString.length() == 1) ? WEEK_STATIC_FACTOR + weekAsString : weekAsString)
							+ note.getTrainee().getTraineeId();
				}))), ArrayList::new));
		if (notes == null || notes.size() < 1) {
			return new ArrayList<>();
		}

		Trainee currentTrainee = null;
		List<Note> traineeNotes = new ArrayList<>();
		for (Note note : notes) {
			Trainee aTrainee = note.getTrainee();
			// trainee HAS changed
			if (!aTrainee.equals(currentTrainee) || currentTrainee == null) {
				// if this is not the first iteration
				if (currentTrainee != null) {
					noteFormatted2d.add(traineeNotes);
				}
				currentTrainee = aTrainee;
				traineeNotes = new ArrayList<>(Collections.singletonList(note));
			} else {// trainee HAS NOT changed
				traineeNotes.add(note);
			}
		}
		noteFormatted2d.add(traineeNotes);
		return formatJaggedArray(noteFormatted2d);
	}

	/**
	 * Formats the jagged "2d" ArrayList of notes. Inserts empty Notes into the
	 * missing spots so that the front end batch QC reports table will display
	 * correctly. Will be called by findAllQCTraineeNotesForAllWeeks() method
	 * 
	 * @return the Lists of Lists ("2d" ArrayList) formatted so that number of
	 *         columns and rows are even
	 */
	public List<List<Note>> formatJaggedArray(List<List<Note>> jaggedArray) {
		int maxWeeks = 0;
		for (List<Note> row : jaggedArray) {
			for (Note column : row) {
				short week = 0;
				week = column.getWeek();
				if (week > maxWeeks) {
					maxWeeks = week;
				}
			}
		}

		log.debug("Max weeks: " + maxWeeks);
		for (int row = 0; row < jaggedArray.size(); row++) {
			for (short column = 0; column < maxWeeks; column++) {
				if (jaggedArray.get(row).size() <= column || jaggedArray.get(row).get(column).getWeek() != column + 1) {
					Note emptyNote = new Note();
					emptyNote.setNoteId(row + column); // testing
					emptyNote.setWeek((short) (column + 1));
					jaggedArray.get(row).add(column, emptyNote);
				}
			}
		}
		log.debug("JaggedArray: " + jaggedArray);
		return jaggedArray;
	}

	/**
	 * noteDAO.findAllQCBatchNotes
	 * 
	 * @param Integer
	 *            batchId: the id of the batch
	 * @return A list of QC batch notes, in ascending order by week
	 */
	public List<Note> findAllQCBatchNotes(Integer batchId) {
		log.debug("Find All QC Batch Notes in ascending order by week");
		return noteDAO.findAllQCBatchNotes(batchId);
	}

}
