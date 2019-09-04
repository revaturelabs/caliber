/**
 * Delegates API calls to the appropriate API factory
 * 
 * @param $log
 * @param trainerFactory
 * @param vpFactory
 * @param qcFactory
 * @param allFactory
 * @param aggFactory
 * @param panelFactory
 * @returns {{}}
 */
angular.module("delegate")
	.factory(
			"caliberDelegate",
			function($log, trainerFactory, vpFactory, qcFactory,
					allFactory, aggFactory, panelFactory) {
			$log.debug("Booted Delegate Factory");

			var delegate = {};

			delegate.all = {};
			delegate.trainer = {};
			delegate.qc = {};
			delegate.vp = {};
			delegate.agg = {};
			delegate.panel = {};

			/***********************************************************
			 * Generate PDF
			 **********************************************************/
			delegate.all.generatePDF = function(title, html) {
				return allFactory.generatePDF(title, html);
			};

			/**
			 * ************************** All
			 * ****************************
			 */
			delegate.all.createBatch = function(batchObj) {
				return allFactory.createBatch(batchObj);
			};

			delegate.all.updateBatch = function(batchObj) {
				return allFactory.updateBatch(batchObj);
			};

			delegate.all.deleteBatch = function(batchId) {
				return allFactory.deleteBatch(batchId);
			};

			delegate.all.getDroppedTrainees = function(batchId) {
				return allFactory.getDroppedTrainees(batchId);
			};

			delegate.all.createTrainee = function(traineeObj) {
				return allFactory.createTrainee(traineeObj);
			};

			delegate.all.updateTrainee = function(traineeObj) {
				return allFactory.updateTrainee(traineeObj);
			};
			
			delegate.all.switchBatch = function(traineeId, batchId) {
				return allFactory.switchBatch(traineeId, batchId);
			};

			delegate.all.deleteTrainee = function(traineeId) {
				return allFactory.deleteTrainee(traineeId);
			};
			
			delegate.all.searchTrainee = function (searchTerm) {
				return allFactory.searchTrainee(searchTerm);
			};

			delegate.all.getGrades = function(traineeId) {
				return allFactory.getGrades(traineeId);
			};

			delegate.all.getGradesForWeek = function(batchId, weekId) {
				return allFactory.getGradesForWeek(batchId, weekId);
			}

			delegate.all.getAllTrainers = function() {
				return allFactory.getAllTrainers();
			};

			delegate.all.getAssessmentsAverageForWeek = function(
					batchId, weekId) {
				return allFactory.getAssessmentsAverageForWeek(batchId,
						weekId);
			}

			delegate.all.getAllTraineeNotes = function(traineeId) {
				return allFactory.getAllTraineeNotes(traineeId);
			}

			delegate.all.getTraineeByEmail = function(traineeEmail) {
				return allFactory.getTraineeEmail(traineeEmail);
			}

			delegate.all.createTrainer = function(trainerObj) {
				return allFactory.createTrainer(trainerObj);
			};

			delegate.all.importAvailableBatches = function() {
				return allFactory.importAvailableBatches();
			};

			delegate.all.getAllTraineesFromBatch = function(resourceId) {
				return allFactory.getAllTraineesFromBatch(resourceId);
			};
			
			/*delegate.all.getAllActiveTasks = function() {
				return allFactory.getAllActiveTasks();
			};
			
			delegate.all.getAllCompletedTasks = function() {
				return allFactory.getAllCompletedTasks();
			};
			
			delegate.all.getAllTasksByTrainerId = function(id) {
				return allFactory.getAllTasksByTrainerId(id);
			};*/
			
		
			/** *********************** Enum constants *************************** */

			delegate.all.enumCommonLocations = function() {
				return allFactory.enumCommonLocations();
			};
			delegate.all.enumAssessmentType = function() {
				return allFactory.enumAssessmentType();
			};
			delegate.all.enumNoteType = function() {
				return allFactory.enumNoteType();
			};
			delegate.all.enumQCStatus = function() {
				return allFactory.enumQCStatus();
			};
			delegate.all.enumSkillType = function() {
				return allFactory.enumSkillType();
			};
			delegate.all.enumTrainingStatus = function() {
				return allFactory.enumTrainingStatus();
			};
			delegate.all.enumTrainingType = function() {
				return allFactory.enumTrainingType();
			};
			delegate.all.enumPanelStatus = function() {
				return allFactory.enumPanelStatus();
			};
			delegate.all.enumInterviewFormat = function() {
				return allFactory.enumInterviewFormat();
			};
			delegate.all.getAllCategories = function() {
				return allFactory.getAllCategories();
			};

			delegate.all.enumTrainerTier = function() {
				return allFactory.enumTrainerTier();
			}
			delegate.all.enumTrainerTitle = function() {
				return allFactory.enumTrainerTitle();
			}
			delegate.all.getAddressById = function(addressId) {
				return allFactory.getAddressById(addressId);
			}

			/**
			 * *********************** Trainer
			 * ***************************
			 */
			delegate.trainer.getAllBatches = function() {
				return trainerFactory.getAllBatches();
			};

			delegate.trainer.createWeek = function(batchId) {
				return trainerFactory.createWeek(batchId);
			};

			delegate.trainer.addGrade = function(gradeObj) {
				return trainerFactory.addGrade(gradeObj);
			};

			delegate.trainer.updateGrade = function(gradeObj) {
				return trainerFactory.updateGrade(gradeObj);
			};
			
			delegate.trainer.importGrade = function(gradeJson, week, batchId) {
				return trainerFactory.importGrade(gradeJson, week, batchId);
			};

			delegate.trainer.createAssessment = function(assessmentObj) {
				return trainerFactory.createAssessment(assessmentObj);
			};
			delegate.trainer.getTraineeBatchNotesForWeek = function(
					batchId, week) {
				return trainerFactory.getTraineeBatchNotesForWeek(
						batchId, week);
			}

			delegate.trainer.getTraineeNote = function(traineeId, week) {
				return trainerFactory.getTraineeNote(traineeId, week);
			}

			delegate.trainer.getAllAssessmentsForWeek = function(
					batchId, week) {
				return trainerFactory.getAllAssessmentsForWeek(batchId,
						week);
			};

			delegate.trainer.updateAssessment = function(assessmentObj) {
				return trainerFactory.updateAssessment(assessmentObj);
			};

			delegate.trainer.deleteAssessment = function(assessmentId) {
				return trainerFactory.deleteAssessment(assessmentId);
			};

			delegate.trainer.createNote = function(noteObj) {
				return trainerFactory.createNote(noteObj);
			};

			delegate.trainer.updateNote = function(noteObj) {
				return trainerFactory.updateNote(noteObj);
			};

			delegate.trainer.getTrainerBatchNote = function(batchId,
					week) {
				return trainerFactory
				.getTrainerBatchNote(batchId, week);
			};

			delegate.trainer.saveOrUpdateNote = function(noteObj) {
				return trainerFactory.saveOrUpdateNote(noteObj);
			}

			/** ************************* VP ************************** */
			delegate.vp.getAllBatches = function() {
				return vpFactory.getAllBatches();
			};

			delegate.vp.getAllCurrentBatches = function() {
				return vpFactory.getAllCurrentBatches();
			};

			delegate.vp.getAllCategories = function() {
				return vpFactory.getAllCategories();
			};

			delegate.vp.updateCategory = function(category) {
				return vpFactory.updateCategory(category);
			};

			delegate.vp.saveCategory = function(category) {
				return vpFactory.saveCategory(category);
			};
			/*

			delegate.vp.saveOrUpdateTask = function(task) {
				return vpFactory.saveOrUpdateTask(task);
			};
			
			delegate.vp.saveTaskCompletion = function(taskCompletion) {
				return vpFactory.saveTaskCompletion(taskCompletion);
			};
			*/
			
			delegate.vp.deactivateTrainer = function(trainerObj) {
				return vpFactory.deactivateTrainer(trainerObj);
			}
			delegate.vp.updateTrainer = function(trainerObj) {
				return vpFactory.updateTrainer(trainerObj);
			};
			delegate.vp.createTrainer = function(trainerObj) {
				return vpFactory.createTrainer(trainerObj);
			};

			/** Location* */
			delegate.vp.getAllLocations = function() {
				return vpFactory.getAllLocations();
			};

			delegate.vp.deactivateLocation = function(locationObj) {
				return vpFactory.deactivateLocation(locationObj);
			}
			delegate.vp.reactivateLocation = function(locationObj) {
				return vpFactory.reactivateLocation(locationObj);
			}
			delegate.vp.updateLocation = function(locationObj) {
				return vpFactory.updateLocation(locationObj);
			};
			delegate.vp.createLocation = function(locationObj) {
				return vpFactory.createLocation(locationObj);
			};

			delegate.vp.saveLocation = function(location) {
				return vpFactory.saveLocation(location);
			};
			/****/


			delegate.vp.trainersTitles = function() {
				return vpFactory.getAllTrainersTitle();
			};
			delegate.vp.getTrainerEmail = function(trainerEmail) {
				return vpFactory.getTrainerEmail(trainerEmail);
			}

			/** ************************ QC **************************** */
			delegate.qc.getAllQCTraineeNoteForAllWeeks = function(batchId){
                return qcFactory.getAllQCTraineeNoteForAllWeeks(batchId);
			};
			
			delegate.qc.getAllQCBatchNotes = function(batchId){
                return qcFactory.getAllQCBatchNotes(batchId);
            };

			delegate.qc.getAllBatches = function() {
				return qcFactory.getAllBatches();
			};

			delegate.qc.addGrade = function() {
				return qcFactory.addGrade();
			};

			delegate.qc.updateGrade = function(gradeObj) {
				return qcFactory.updateGrade(gradeObj);
			};

			delegate.qc.createAssessment = function(assessmentObj) {
				return qcFactory.createAssessment(assessmentObj);
			};

			delegate.qc.getAllAssessments = function(weekId) {
				return qcFactory.getAllAssessments(weekId);
			};

			delegate.qc.getAllAssessmentCategories = function(batchId,
					weekId) {
				return qcFactory.getAllAssessmentCategories(batchId,
						weekId);
			}

			delegate.qc.deleteAssessment = function(assessmentId) {
				return qcFactory.deleteAssessment(assessmentId);
			};
			// Notes
			delegate.qc.createNote = function(noteObj) {
				return qcFactory.createNote(noteObj);
			};

			delegate.qc.updateNote = function(noteObj) {
				return qcFactory.updateNote(noteObj);
			};

			delegate.qc.batchNote = function(batchId, week) {
				return qcFactory.getQCBatchNote(batchId, week);
			}

			delegate.qc.aTraineeNote = function(traineeId, week) {
				return qcFactory.getAQCTraineeNote(traineeId, week);
			}

			// get QCtrainee note by week - Michael
			delegate.qc.getQCTraineeNote = function(traineeId, week) {
				return qcFactory.getQCTraineeNote(traineeId, week);
			}

			// GET TRAINEE WEEK - SADAT

			delegate.qc.traineeWeekNote = function(traineeId, week) {
				return qcFactory.getTraineeWeek(traineeId, week);
			}

			delegate.qc.traineeNote = function(batchId, week) {
				return qcFactory.getAllQCTraineeNote(batchId, week);
			}

			delegate.qc.traineeOverallNote = function(traineeId) {
				return qcFactory.getTraineeOverallNote(traineeId);
			}

			/**
			 * ************************ Aggregate
			 * ****************************
			 */
			delegate.agg.getAggTechTrainee = function(traineeId) {
				return aggFactory.techTrainee(traineeId);
			};

			delegate.agg.getAggWeekTrainee = function(traineeId) {
				return aggFactory.weekTrainee(traineeId);
			};

			delegate.agg.getAggTechBatch = function(batchId) {
				return aggFactory.techBatch(batchId);
			};

			delegate.agg.getAggWeekBatch = function(batchId) {
				return aggFactory.weekBatch(batchId);
			};

			delegate.agg.getAggTechAllBatch = function() {
				return aggFactory.techAllBatch();
			};

			delegate.agg.getAggBatchAllTrainer = function(trainerId) {
				return aggFactory.batchTrainer(trainerId);
			};

			/** ******************** Panel ***************************
			 * 
			 * @author Emma Bownes
			 * @author Nathan Koszuta
			 */

			delegate.panel.findAllPanels = function () {
				return panelFactory.findAllPanels();
			};

			delegate.panel.getBatchPanelTable = function (batchId) {
				return panelFactory.reportPanelTable(batchId);
			};

			delegate.panel.getPanelById = function (panelId) {
				return panelFactory.getPanelById(panelId);
			};

			delegate.panel.reportTraineePanels = function (traineeId) {
				return panelFactory.reportTraineePanels(traineeId);
			};
			
			delegate.panel.reportAllRepanels = function () {
				return panelFactory.reportAllRepanels();
			};
			
			delegate.panel.updatePanel = function (panelObj) {
				return panelFactory.updatePanel(panelObj);
			};
			
			delegate.panel.createPanel = function (panelObj) {
				return panelFactory.createPanel(panelObj);
			};
			
			delegate.panel.deletePanel = function (panelObj) {
				return panelFactory.deletePanel(panelObj);
			};			

			delegate.panel.getRecentRepanels = function () {
				return panelFactory.getRecentRepanels();
			};

			
			return delegate;
		});
