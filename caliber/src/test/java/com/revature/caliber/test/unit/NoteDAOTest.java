package com.revature.caliber.test.unit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Note;
import com.revature.caliber.beans.NoteType;
import com.revature.caliber.beans.QCStatus;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.data.NoteDAO;
import com.revature.caliber.data.TraineeDAO;

public class NoteDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(NoteDAOTest.class);

	private static final int TEST_TRAINEE_ID = 5529;
	private static final int TEST_BATCH_ID = 2100;
	private static final int TEST_QCTRAINEE_ID = 5532;
	private static final int TEST_QCBATCH_ID = 2201;
	
	@Autowired
	private NoteDAO noteDao;
	@Autowired
	private BatchDAO batchDao;
	@Autowired
	private TraineeDAO traineeDao;
	
	/**
	 * Test methods:
	 * Positive tests for finding individual notes for trainee
	 * @see com.revature.caliber.data.GradeDAO#save(Grade)
	 */
	@Test
	public void findQCIndividualNote(){
		log.trace("GETTING individual notes by Trainee");
		//get trainee comparison
		Trainee trainee = traineeDao.findOne(TEST_TRAINEE_ID);
		
		// get notes
		List<Note> notes = noteDao.findQCIndividualNotes(trainee.getTraineeId(), 2);
		
		// compare with expected
		assertEquals(notes.size(),1);
		assertEquals(notes.get(0).getContent(),"technically weak on SQL.");
		
		
	}
	
	/**
	 * Test methods:
	 * Positive tests for finding individual notes for trainee
	 */
	@Test
	public void findAllQCIndividualNotes(){
		log.trace("GETTING individual notes by Trainee");
		//get trainee comparison
		Trainee trainee = traineeDao.findOne(TEST_TRAINEE_ID);
		
		// get notes
		List<Note> notes = noteDao.findQCIndividualNotes(trainee.getTraineeId());
		
		// compare with expected
		assertEquals(notes.size(), 7);	
		log.debug(notes);
	}
	
	/**
	 * Test methods:
	 * Positive tests for finding individual notes for a bacth in a given week
	 * @see com.revature.caliber.data.NoteDAO#findAllBatchNotes(batch,week)
	 */
	@Test
	public void findAllBatchNotes(){
		log.trace("GETTING individual notes by Batch");
		//get batch 
		Batch batch = batchDao.findOne(TEST_BATCH_ID);
		
		// get batch notes
		List<Note> notes = noteDao.findAllBatchNotes(batch.getBatchId(), 2);
		
		// compare with expected
		//assertEquals(1,1);
		//assertEquals(notes.get(0).getNoteId(),5133);
		
	}
	
	/**
	 * Test methods:
	 * Positive tests for finding individual notes for a trainee in a given week
	 * @see com.revature.caliber.data.NoteDAO#findAllIndividualNotes(trainee,week)
	 */
	@Test
	public void findAllIndividualNotes(){
		log.trace("GETTING individual notes by Batch");
		//get batch 
		Trainee trainee = traineeDao.findOne(TEST_TRAINEE_ID);
		
		// get individual notes
		List<Note> notes = noteDao.findAllIndividualNotes(trainee.getTraineeId(), 2);
		
		// compare with expected
		assertEquals(notes.size(),1);
		assertEquals(notes.get(0).getContent(),"Good communication. Not as confident and less use of technical terms");
		
		
	}
	
	/**
	 * Test methods:
	 * Positive tests for finding all individual notes for a trainee
	 * @see com.revature.caliber.data.NoteDAO#findAllPublicIndividualNotes(trainee)
	 */
	@Test
	public void findAllPublicIndividualNotes(){
		log.trace("GETTING all public individual notes by Trainee ID");
		//get batch 
		Trainee trainee = traineeDao.findOne(TEST_TRAINEE_ID);
		
		// get public individual notes
		List<Note> notes = noteDao.findAllPublicIndividualNotes(trainee.getTraineeId());
		
		// compare with expected
		assertTrue(notes.size() == 7);
		for(Note note : notes){
			assertEquals(trainee,note.getTrainee());
		}
		
	}

	
	/**
	 * Positive testing for finding a trainee note
	 * @see com.revature.caliber.data.NoteDAO#findTraineeNote(Integer, Integer)
	 */
	@Test
	public void testFindTraineeNote(){
		log.trace("Testing findTraineeNote");
		final int weekNum = 2;
		final String content = "Good communication. Not as confident and less use of technical terms";
		
		// find trainee note
		Note actual = noteDao.findTraineeNote(TEST_TRAINEE_ID, weekNum);

		// compare with expected
		assertFalse(actual.isQcFeedback());
		assertEquals(content, actual.getContent());
		assertEquals(weekNum, actual.getWeek());
		assertEquals(TEST_TRAINEE_ID, actual.getTrainee().getTraineeId());
	}
	
	/**
	 * Positive testing for finding a QC trainee note
	 * @see com.revature.caliber.data.NoteDAO#findQCTraineeNote(Integer, Integer)
	 */
	@Test
	public void testFindQCTraineeNote(){
		log.trace("Testing findQCTraineeNote");
		final int weekNum = 3;
		final QCStatus status = QCStatus.Good;
		
		// find QC trainee note
		Note actual = noteDao.findQCTraineeNote(TEST_QCTRAINEE_ID, weekNum);

		// compare with expected
		assertTrue(actual.isQcFeedback());
		assertEquals(status, actual.getQcStatus());
		assertEquals(weekNum, actual.getWeek());
		assertEquals(TEST_QCTRAINEE_ID, actual.getTrainee().getTraineeId());
	}
	
	/**
	 * Positive testing for finding QC batch notes for a week
	 * @see com.revature.caliber.data.NoteDAO#findQCBatchNotes(Integer, Integer)
	 */
	@Test
	public void testFindQCBatchNotes(){
		log.trace("Testing findQCBatchNotes");
		final int week = 5;
		
		// find QC batch notes
		Note actual = noteDao.findQCBatchNotes(TEST_QCBATCH_ID, week);
		
		// compare with expected
		assertEquals(TEST_QCBATCH_ID, actual.getBatch().getBatchId());
		assertEquals(week, actual.getWeek());
	}
	
	/**
	 * Positive testing for finding all batch level notes
	 * @see com.revature.caliber.data.NoteDAO#findAllBatchQcNotes(Integer)
	 */
	@Test
	public void testFindAllBatchQcNotes(){
		log.trace("Testing findAllBatchQcNotes");
		
		// get all QC batch notes
		List<Note> actual = noteDao.findAllBatchQcNotes(TEST_QCBATCH_ID);
				
		// compare with expected size
		assertEquals(7, actual.size());
	}
	
	/**
	 * Positive testing for finding all QC notes for a batch
	 * @see com.revature.caliber.data.NoteDAO#findAllQCBatchNotes(Integer)
	 */
	@Test	
	public void findAllQCBatchNotes(){
		log.trace("GETTING ALL QC BATCH NOTES");
		
		// find all QC batch notes
		List<Note> notes = noteDao.findAllQCBatchNotes(TEST_QCBATCH_ID);
		
		// compare with expected
		List<Integer> expected = new ArrayList<>();
		Collections.addAll(expected, 6369,6390,6391,6420,6438,6457,6470);
		assertEquals(expected.size(), notes.size()); // same size
		for(int j = 0; j < expected.size(); j++){
			assertTrue(expected.contains(notes.get(j).getNoteId())); // same values
		}
	}
	
	/**
	 * Positive testing for finding all QC notes for a trainee for a week
	 * @see com.revature.caliber.data.NoteDAO#findAllQCTraineeNotes(Integer, Integer)
	 */
	@Test	
	public void findAllQCTraineeNotes(){
		log.trace("GETTING ALL QC TRAINEE NOTES");
		
		// find all QC trainee notes for week 7
		List<Note> notes = noteDao.findAllQCTraineeNotes(TEST_QCBATCH_ID, 7);
		
		// compare with expected
		List<Integer> expected = new ArrayList<>();
		Collections.addAll(expected, 6459,6460,6461,6462,6463,6464,6465,6466,6467,6468,6469,6471,6472,6473,6474,6475);
		assertEquals(expected.size(), notes.size()); // same size
		for(int j = 0; j < expected.size(); j++){
			assertTrue(expected.contains(notes.get(j).getNoteId())); // same values
		}
	}
	
	
	/**
	 * Positive testing for finding all QC notes for a trainee
	 * @see com.revature.caliber.data.NoteDAO#findAllQCTraineeOverallNotes(Integer)
	 */
	@Test	
	public void findAllQCTraineeOverallNotes(){
		log.trace("GETTING ALL QC TRAINEE OVERALL NOTES");

		// get all QC trainee overall notes
		List<Note> notes = noteDao.findAllQCTraineeOverallNotes(TEST_QCTRAINEE_ID);
		
		// compare with expected
		List<Integer> expected = new ArrayList<>();
		Collections.addAll(expected, 6353,6392,6421,6385,6416,6417,6471,6476);
		assertEquals(expected.size(), notes.size()); // same size
		for(int j = 0; j < expected.size(); j++){
			assertTrue(expected.contains(notes.get(j).getNoteId())); // same values
		}
	}

	/**
	 * Testing saving a note
	 * @see com.revature.caliber.data.NoteDAO#save(Note)
	 */
	@Test
	public void testSaveNote() {
		log.trace("Tesing Save Note");

		// find batch and trainee to associate with note
		final Batch batch = batchDao.findOne(TEST_QCBATCH_ID);
		final Trainee trainee = traineeDao.findAll().get(0);

		// create a new note
		Note note = new Note();
		note.setNoteId(1);
		note.setContent("Note Test");
		note.setBatch(batch);
		note.setQcFeedback(false);
		note.setWeek((short) 1);
		note.setTrainee(trainee);
		note.setQcStatus(null);
		note.setMaxVisibility(null);
		note.setType(NoteType.TRAINEE);

		// save note
		noteDao.save(note);
	}

	/**
	 * Tests updating a note's content
	 * @see com.revature.caliber.data.NoteDAO#update(Note)
	 */
	@Test
	public void testUpdateNote() {
		log.trace("Testing updating note");

		// get the list of individual notes for week 2
		List<Note> notes = noteDao.findIndividualNotes(TEST_QCBATCH_ID, 2);

		assertTrue(!notes.isEmpty());

		Note note = notes.get(1);

		// Old Content = Superstar. Great communication skill and good solid knowledge.
		String oldContent = note.getContent();

		note.setContent("Hello");

		// update note
		noteDao.update(note);

		// Old Content should not equal new Content if updated
		notes = noteDao.findIndividualNotes(TEST_QCBATCH_ID, 2);
		String newContentSet = notes.get(1).getContent();
		assertNotEquals(oldContent, newContentSet);
	}

	/**
	 * Tests finding batch notes
	 * @see com.revature.caliber.data.NoteDAO#findBatchNotes(Integer, Integer)
	 */
	@Test
	public void testfindBatchNotes() {
		log.trace("Finding Batch Notes");

		// get the list of batch notes for week 2
		List<Note> batchNotes = noteDao.findBatchNotes(TEST_BATCH_ID, 2);

		// has batch note
		assertTrue(!batchNotes.isEmpty());
	}

	/**
	 * Tests finding individual notes
	 * @see com.revature.caliber.data.NoteDAO#findIndividualNotes(Integer, Integer)
	 */
	@Test
	public void testfindIndividualNotes() {
		log.trace("Finding Individual Notes");

		// get the list of individual notes for week 2
		List<Note> notes = noteDao.findIndividualNotes(TEST_QCBATCH_ID, 2);

		// check if the individual notes size is equal to 16
		assertEquals(16, notes.size());
	}
	/**
	 * Tests finding individual notes all weeks
	 * @see com.revature.caliber.data.NoteDAO#findAllQCTraineeNotesForAllWeeks(Integer)
	 */
	@Test 
	public void testFindIndividualNotesAllWeeks() {
		log.trace("Finding individual notes, all weeks");
		List<Note> notes = noteDao.findAllQCTraineeNotesForAllWeeks(2201);
		assertEquals(117, notes.size());
	}
	
}
