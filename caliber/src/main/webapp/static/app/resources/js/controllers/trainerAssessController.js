angular
.module("trainer")
.controller(
		"trainerAssessController",
		function($rootScope, $timeout,$log, $window, $scope, $state, chartsDelegate, caliberDelegate,
				allBatches) {
			// Week object
			function Week(weekNumb, assessments) {
				this.weekNumb = weekNumb;
				this.assessments = assessments;
			}
			// this is use to set false on the no trainees for the batch
			// notification.
			$scope.noTrainees = false;					
			$log.debug("Booted Trainer Aesess Controller");

			$scope.trainerBatchNote = null;

			// Note object This is needed to create notes for batch.
			function Note(noteId, content, status, week, batch,
					trainee, maxVisibility, type, qcFeedback) {
				this.noteId = noteId;
				this.content = content;
				this.week = week;
				this.batch = batch;
				this.trainee = trainee;
				this.maxVisibility = maxVisibility;
				this.type = type;
				this.qcFeedback = qcFeedback;
				this.qcStatus = status;
			}
			
			// load categories
			$scope.skill_categories = function() {
				caliberDelegate.all.getAllCategories().then(
						function(categories) {
							$scope.categories = categories;
							$log.debug("all Categories " + categories);
						});
			};

			// Loads all grades for the week with the BatchID and weekID as a
			// param.
			$scope.getAllGradesForWeek=function(batchId,weekId) {							
				caliberDelegate.all
				.getGradesForWeek(batchId,
						weekId).then(
								function(data) {
									if(data === undefined || Object.keys(data).length === 0){
										$scope.grades = false;
									}else{
										$scope.grades = data;
										$scope.doGetAllAssessmentsAvgForWeek($scope.currentBatch.batchId,$scope.currentWeek);
									}
								});
			};
			$scope.assignTraineeScope = function(traineeId){
				if($scope.trainees[traineeId] === undefined){
					$scope.trainees[traineeId] = {};
					$scope.trainees[traineeId].assessments=[];
					$scope.trainees[traineeId].note={};
					$scope.trainees[traineeId].burrito=false;
				}								
				return $scope.trainees[traineeId];							
			};
			$scope.updateTraineeScopeWithCurrentAssessment = function(assessments){
				angular.forEach($scope.trainees,function(key,value){
					for(const a in assessments){
						if($scope.trainees[value].assessments[assessments[a].assessmentId] === undefined){
							$scope.trainees[value].assessments[assessments[a].assessmentId] = {};
							$scope.trainees[value].assessments[assessments[a].assessmentId].score='';
						}
					}
				});
			}

			// get trainee notes and put into $scope.trainees[traineeId].note
			$scope.getTraineeBatchNotesForWeek=function(batchId,weekId){
				caliberDelegate.trainer.getTraineeBatchNotesForWeek(batchId,weekId).then(function(data){
					angular.forEach($scope.trainees,function(key,value){
						$scope.trainees[value].note = {};
					});
					if(data && data.length > 0){
						$scope.notes = data;
						for(const note of $scope.notes){
							if($scope.trainees[note.trainee.traineeId] !== undefined && $scope.trainees[note.trainee.traineeId].note.hasOwnProperty('noteId')){
								$scope.trainees[note.trainee.traineeId].note = {};
							}
							if($scope.trainees[note.trainee.traineeId] !== undefined){
								$scope.trainees[note.trainee.traineeId].note = note;
							}
						}
					}
				});
			}

			// load note types
			caliberDelegate.all.enumNoteType().then(
					function(noteTypes) {
						$log.debug(noteTypes);
					});
			$scope.assessmentType = {
					model : null,
					options : []
			};
			caliberDelegate.all
			.enumAssessmentType()
			.then(
					function(assessmentTypes) {
						$log.debug(assessmentTypes);
						$scope.assessmentType.options = assessmentTypes;
					});
			$log.debug("Batches " + allBatches);
			$log.debug(allBatches);

			(function (allBatches) {

				$scope.grades={};
				$scope.updateAssessmentModel={};

				$scope.batches = allBatches;
				if (!allBatches) 
					return;
				if (allBatches.length > 0) { 
					// if currentBatch is not yet in the scope, run for assess
					// batch..
					// else this controller is being used for reportsController
					if(!$scope.currentBatch){
						$scope.currentBatch = allBatches[0];
					}
					// sort the trainees by name
					$scope.currentBatch.trainees.sort(compare);

					$log.debug("This is the current batch " + $scope.currentBatch);

					if (allBatches[0].weeks > 0) {
						$scope.category = {
								model : null,
								options : []
						};
						caliberDelegate.all
						.getAllCategories()
						.then(
								function(categories) {
									$log.debug(categories);
									$scope.category.options = categories;
								});

						caliberDelegate.all.enumNoteType().then(
								function(noteTypes) {
									$log.debug(noteTypes);
								});

						// loading all assement type for create assement drop
						// down
						$scope.assessmentType = {
								model : null,
								options : []
						};
						caliberDelegate.all
						.enumAssessmentType()
						.then(
								function(assessmentTypes) {
									$log.debug(assessmentTypes);
									$scope.assessmentType.options = assessmentTypes;
								});
						$log.debug("Batches " + allBatches);
						if(!$scope.currentWeek){ 
							// if currentWeek is not yet in the scope, run for
							// assess batch
							var totalWeeks = allBatches[allBatches.length-1].weeks; // the
							// number of weeks for that batch

							$log
							.debug("this is the total week for this batch "
									+ allBatches[allBatches.length-1].trainingName
									+ ": " + totalWeeks);
							batchYears();
							$scope.currentWeek = $scope.currentBatch.weeks;
						}
						getAllAssessmentsForWeek(
								$scope.currentBatch.batchId,
								$scope.currentWeek);
					} else
						$scope.currentWeek = null;
				} 
				$log.debug("Starting Values: currentBatch and currentWeek");
				$log.debug($scope.currentBatch);
				$log.debug($scope.currentWeek);

				$scope.trainees={};						

				if($scope.currentBatch !== undefined)
					for(const trainee of $scope.currentBatch.trainees){
						$scope.assignTraineeScope(trainee.traineeId);
					}
				categories();
			})(allBatches);

			// default -- view assessments table
			$scope.currentView = true;

			// back button
			$scope.back = function() {
				$scope.currentView = true;
			};

			/** ***** Filter batches by year *********** */
			// The year drop down menu is generated here.
			$scope.years = addYears();
			function addYears() {
				var currentYear = new Date().getFullYear();
				$scope.selectedYear = currentYear;

				var data = [];
				// List all years from 2014 --> current year
				for (var y = currentYear + 1; y >= currentYear - 2; y--) {
					data.push(y)
				}
				return data;
			}

			/**
			 * When selecting a year the batch drop down will change to the
			 * latest batch of that year. If there aren't any batch for the year
			 * that is selected then the No Batch will be display check the view
			 * for ng-show author: Kam Lam
			 */
			$scope.selectYear = function(index) {
				$scope.selectedYear = $scope.years[index];
				sortByDate($scope.selectedYear);
				batchYears();
				$scope.noTrainees = false;
				$scope.currentBatch = $scope.batchesByYear[0];
				$log.debug(batchYears());
				$log.debug($scope.currentBatch);
				if ($scope.batchesByYear.length === 0) {
					$scope.noBatches = true;
					$scope.noBatchesMessage = "No Batches were found for this year.";
				} else {
					$scope.noBatches = false;
					$scope.selectedYear = $scope.years[index];
					sortByDate($scope.selectedYear);					

					$scope.trainees={};						
					for(const trainee of $scope.currentBatch.trainees){
						$scope.assignTraineeScope(trainee.traineeId);
						
					}
					if ($scope.currentBatch.weeks > 0) {
						$scope.currentWeek = $scope.currentBatch.weeks;
						getAllAssessmentsForWeek(
								$scope.currentBatch.batchId,
								$scope.currentWeek);
					} else
						$scope.currentWeek = null;

					getAllAssessmentsForWeek($scope.currentBatch.batchId,
							$scope.currentWeek);

				} };

				function sortByDate(currentYear) {
					$scope.selectedBatches = [];
					for (var i = 0; i < $scope.batches.length; i++) {
						var date = new Date($scope.batches[i].startDate);
						if (date.getFullYear() === currentYear) {
							$scope.selectedBatches.push($scope.batches[i]);
						}
					}
				}

				/**
				 * Get batch according to year
				 */
				function batchYears() {
					$scope.batchesByYear = [];

					for (var i = 0; i < $scope.batches.length; i++) {
						$log.debug("Current Year: " + $scope.selectedYear);
						var date = new Date($scope.batches[i].startDate);
						if ($scope.selectedYear === parseInt(date.getFullYear())) {
							$scope.batchesByYear.push($scope.batches[i]);
							$log.debug($scope.batches[i]);
						}
					}
					$log.debug($scope.batches + ", " +$scope.batchesByYear);

				}
				// batch drop down select
				$scope.selectCurrentBatch = function(index) {
					$scope.currentBatch = $scope.batchesByYear[index];
					$log.debug("Selected batch " + index);
					$scope.currentBatch.trainees.sort(compare);

					// create new scope of trainees
					$scope.trainees={};						
					for(const trainee of $scope.currentBatch.trainees){
						$scope.assignTraineeScope(trainee.traineeId);
					}
					if ($scope.currentBatch.weeks > 0) {
						$scope.currentWeek = $scope.currentBatch.weeks;
						getAllAssessmentsForWeek(
								$scope.currentBatch.batchId,
								$scope.currentWeek);
					} else
						$scope.currentWeek = null;

					getAllAssessmentsForWeek($scope.currentBatch.batchId,
							$scope.currentWeek);
					resetFlags();
				};

				// select week
				$scope.selectWeek = function(index) {

					$scope.currentWeek = $scope.currentBatch.arrayWeeks[index];
					$log.debug("[***********This is the week selected*************]:  "+$scope.currentWeek);

					getAllAssessmentsForWeek($scope.currentBatch.batchId,
							$scope.currentWeek);
					$scope.getTBatchNote($scope.currentBatch.batchId, $scope.currentWeek);
				};

				// active week
				$scope.showActiveWeek = function(index) {
					if ($scope.currentWeek === $scope.currentBatch.arrayWeeks[index])
						return "active";
				};

				// create week
				$scope.createWeek = function() {
					if($scope.trainees.length === 0){
						$scope.noTrainees = true;
						$scope.noTraineesMessage ="No trainees were found; weeks could not be created.";
						$timeout(function(){
							$scope.noTrainees = false;
						}, 8000);
						$log.debug("NO Trainees");
						$log.debug($scope.noTraineesMessage);
						$log.debug($scope.noTrainees);
					}else{
						caliberDelegate.trainer.createWeek($scope.currentBatch.batchId).then(
								function() {
									$scope.currentBatch.weeks += 1;
									$scope.currentBatch.arrayWeeks.push($scope.currentBatch.weeks);
									$scope.showActiveWeek($scope.currentBatch.weeks);
									$scope.selectWeek($scope.currentBatch.weeks-1);
									// the new index of the week selected
								});
					} 
				};

				/*
				 * select assessment from list it is basically what it said, is
				 * the select assessment from list function Takes in an index as
				 * passed through the view. The current view is set to false,
				 * see the view as reference author: Kam Lam
				 */
				$scope.selectAssessment = function(index) {
					$scope.currentAssessment = $scope.currentAssessment[index];
					$scope.currentView = false;
				};

				$scope.addAssessment = function() {
					getAllAssessmentsForWeek($scope.currentBatch.batchId,
							$scope.currentWeek);
					var assessment = {
							batch : $scope.currentBatch,
							type : $scope.assessmentType.model,
							category : angular.fromJson($scope.category.model),
							week : $scope.currentWeek,
							rawScore : $scope.rawScore
					};
					$log.debug(assessment);
					caliberDelegate.trainer
					.createAssessment(assessment)
					.then(
							function(response) {
								$log.debug(response);
								getAllAssessmentsForWeek(
										$scope.currentBatch.batchId,
										$scope.currentWeek);
								if ($scope.currentAssessments > 0)
									$scope.currentAssessments
									.unshift(assessment);
								else
									$scope.currentAssessments = assessment;
								angular.element(
										"#createAssessmentModal")
										.modal("hide");
							});
				};

				$scope.selectedCategories = [];

				$scope.toggleSelection = function(category) {
					var index = $scope.selectedCategories.indexOf(category);
					if (index > -1)
						$scope.selectedCategories.splice(index, 1);
					else
						$scope.selectedCategories.push(category);
				};

				/** *******TrainerBatch Notes********** */	

				// This function passes the batchID and week param to get the
				// batch
				// notes for that week Author: Kam Lam
				$scope.getTBatchNote = function (batchId, week){
					caliberDelegate.trainer
					.getTrainerBatchNote(batchId, week)
					.then(
							function(trainerBatchNotes) {
								if (trainerBatchNotes.length === 0) {
									$log.debug("EMPTY!");
									$scope.trainerBatchNote = null;
								}else{																								
									$scope.trainerBatchNote = trainerBatchNotes[0];
									$log.debug(trainerBatchNotes);
								}
							});
				};

				// get all assessments for the batch specific
				function getAllAssessmentsForWeek(batchId, weekNumb) {
					if (!weekNumb)
						return;
					caliberDelegate.qc.getAllAssessmentCategories(
							batchId,
							weekNumb).then(
									function(response) {
										$scope.categories = response;
									});
					caliberDelegate.trainer
					.getAllAssessmentsForWeek(batchId, weekNumb)
					.then(
							function(data) {
								$log.debug(data);
								$scope.currentAssessments = data;
								if(!$scope.grades || $scope.grades === null || $scope.grades === undefined ){
									$scope.grades = [];
								}
								$scope.getAllGradesForWeek($scope.currentBatch.batchId,$scope.currentWeek);
								var week = new Week(
										$scope.currentWeek,
										$scope.currentAssessments);
								$scope.currentBatch.displayWeek = week;
								$scope.currentBatch.arrayWeeks = [];
								// create array of assessments mapped by
								// assessment Id;
								$scope.assessmentsById=[];

								$scope.generateArrAssessmentById(data);
								$scope.updateTraineeScopeWithCurrentAssessment($scope.currentAssessments);
								for(var i = 1; i <= $scope.currentBatch.weeks; i++){
									$scope.currentBatch.arrayWeeks.push(i);
								}
								// Parse the current batch year from date to
								// int. For the selectedYear function.
								 var date = new Date($scope.currentBatch.startDate);
								 $scope.selectedYear = parseInt(date.getFullYear());
								// initializing the batchYears function.
								batchYears();
								// initializing the getTBatchNote function.

								$scope.getTBatchNote($scope.currentBatch.batchId, $scope.currentWeek);
								$scope.allAssessmentsAvgForWeek = false;
								$scope.getTraineeBatchNotesForWeek($scope.currentBatch.batchId, $scope.currentWeek);
							});

				}
				/** Call an all factory method - jack* */
				$scope.doGetAllAssessmentsAvgForWeek = function(batchId, week){
					caliberDelegate.all.getAssessmentsAverageForWeek(batchId, week)
					.then(function(response){
						if(response && response !== "NaN"){
							$scope.allAssessmentsAvgForWeek = response.toFixed(2).toString() + '%';
						}else{
							$scope.allAssessmentsAvgForWeek = false;
						}															
					});
				};

				/** *******Save TrainerBatch Notes********** */	
				// Updating and Creating batch notes - Author: Kam Lam
				$scope.saveTrainerNotes = function(batchNoteId) {
					$log.debug("Saving note: " + $scope.trainerBatchNote);
					// Create note
					if (!$scope.trainerBatchNote) {
						$scope.trainerBatchNote = new Note(
								null,
								$scope.trainerBatchNote.content,
								null,
								$scope.currentWeek,
								$scope.currentBatch,
								null, "ROLE_TRAINER",
								"BATCH", false);
						caliberDelegate.trainer.createNote($scope.trainerBatchNote).then(
								// Set id to created notes id
								function(id) {
									$scope.trainerBatchNote.noteId = id;
								});
					}
					// Update existing note
					else {
						$scope.trainerBatchNote = new Note(batchNoteId, $scope.trainerBatchNote.content,
								null, $scope.currentWeek, $scope.currentBatch, null, "ROLE_TRAINER", "BATCH", false);
						caliberDelegate.trainer.updateNote($scope.trainerBatchNote);
					}
				};
				 // Array of assessments by assessment id used to store raw
					// score and to calculate weight score - jack
				$scope.generateArrAssessmentById = function(assessments){
					var totalRawScore = 0;
					if(assessments !== undefined){
						for(const a of assessments){
							$scope.assessmentsById[a.assessmentId] = {};
							$scope.assessmentsById[a.assessmentId].total = 0;
							$scope.assessmentsById[a.assessmentId].rawScore = a.rawScore; 
							totalRawScore += a.rawScore;
						}						
						for(const a of assessments){
							$scope.assessmentsById[a.assessmentId]
							.weightedScore = $scope.getWeightedScore(
									$scope.assessmentsById[a.assessmentId].rawScore
									,totalRawScore
							).toFixed(0).toString() + '%';
						}
					}
				};
				/* Get weighted score of assessments- jack */
				$scope.getWeightedScore = function(rawScore,totalRawScore){
					return (rawScore/totalRawScore) * 100;
				};
				/**
				 * Updates Grade if exists, else create new Grade, then saves to
				 * $scope
				 * 
				 * @param gradeId
				 * @param traineeId
				 * @param assessment
				 */
				$scope.updateGrade = function(trainee,assessment) {
					var score = $scope.trainees[trainee.traineeId].assessments[assessment.assessmentId].score;
					if(score !== null && score !== undefined && score !=="" && score >= 0){
						// replaces 0 with arbitrary low number for valid
						// average calculations in the back-end - Trevor Lory
						if(score === 0) {
							score = 0.00001;
						}
						if($scope.trainees[trainee.traineeId].assessments[assessment.assessmentId] === undefined){
							$scope.trainees[trainee.traineeId].assessments[assessment.assessmentId] = {};
						}
						// constructs Grade object from the data in table
						var grade = {
								trainee : trainee,
								assessment : assessment,
								dateReceived : new Date(),
								score :score,
						};
						// if assessment object has gradeId, define it in grade
						// object
						if($scope.trainees[trainee.traineeId].assessments[assessment.assessmentId].gradeId){
							grade.gradeId = $scope.trainees[trainee.traineeId].assessments[assessment.assessmentId].gradeId;
						}
						// adds new Grade if not exists, else update, response
						// contains the ID of the created/updated Grade

						caliberDelegate.trainer.addGrade(grade).then(
								function(response) {
									$log.debug("Adding grade to $scope");
									$log.debug(response);
									return response;
								}).then(function(response){
									if(response !== undefined && response.data.trainee !== undefined){
										$scope.trainees[response.data.trainee.traineeId].assessments[response.data.assessment.assessmentId].gradeId = response.data.gradeId;									
										return response;
									}else{
										return;
									}
								}).then(function(response){										
									if(response.data.gradeId && ($scope.grades === undefined || $scope.grades === false)){
										$scope.grades = [];
									}
									if(response.data.gradeId){
										$scope.grades[response.data.trainee.traineeId] = response.data;
									}									
									$scope.allAssessmentsAvgForWeek = false;
									$scope.doGetAllAssessmentsAvgForWeek($scope.currentBatch.batchId,$scope.currentWeek);									
								});
					}
				};
				/* Run when populating input boxes with grades - jack */
				$scope.findGrade = function(traineeId, assessmentId) {
					if(!$scope || !$scope.grades || !traineeId || $scope.grades[traineeId] === undefined){ 
						return false;
					}else if($scope.grades[traineeId]){
						angular.forEach($scope.grades[traineeId],function(grade){
							// create a assessment object that contains gradeId
							// for each $scope.trainees[trainee]

							if(grade.assessment !== undefined && (grade.assessment.assessmentId === assessmentId)){
								if($scope.trainees[traineeId].assessments[grade.assessment.assessmentId] === undefined){
									$scope.trainees[traineeId].assessments[grade.assessment.assessmentId] = {};
								}
								if($scope.trainees[traineeId].assessments[grade.assessment.assessmentId].gradeId === undefined){
									$scope.trainees[traineeId].assessments[grade.assessment.assessmentId].gradeId = grade.gradeId;
									// replaces 0.00001 to 0 for correct display
									// of almost zero - Trevor Lory
									if(grade.score <= 0.00001) {
										$scope.trainees[traineeId].assessments[grade.assessment.assessmentId].score = 0;
									}
									else {
										$scope.trainees[traineeId].assessments[grade.assessment.assessmentId].score = grade.score;	
									}									
								}
								return grade.score;
							}
						});
					}
				};

				// save trainee note - send to "/note/update" By Jack
				$scope.saveOrUpdateTraineeNote=function(traineeId){
					var traineeNote = $scope.trainees[traineeId].note;
					var trainee = $scope.currentBatch.trainees.filter(function(trainee) {
						return trainee.traineeId === traineeId;
					});
					// create noteObj to send to controller - jack
					var noteObj={
							content:traineeNote.content,
							week:$scope.currentWeek,
							trainee:trainee[0],
							type:"TRAINEE",
							batch:$scope.currentBatch
					};
					// if noteId exists, add it to noteObj to get noteObj in db
					// to update
					if($scope.trainees[traineeId].note.noteId !== undefined){
						noteObj.noteId = traineeNote.noteId;
					}
					caliberDelegate.trainer.saveOrUpdateNote(noteObj)
					.then(function(response){
						return response;
					}).then(function(response){
						// set persisted note object into trainee.note
						$log.debug("setting response note obj to trainee scope note obj");
						$scope.trainees[response.data.trainee.traineeId].note = response.data
					});

				};

				$scope.getTotalAssessmentAvgForWeek = function(assessment,trainees){
					// assessmentTotals will assessment objects, each with
					// properties total and count - jack
					if($scope.assessmentTotals === undefined) 
						$scope.assessmentTotals=[];
					if($scope.assessmentTotals[assessment.assessmentId] === undefined) 
						$scope.assessmentTotals[assessment.assessmentId] = {};

					$scope.assessmentTotals[assessment.assessmentId].total = 0;
					$scope.assessmentTotals[assessment.assessmentId].count = 0;
					for(var traineeKey in trainees){
						// checks if trainee has assessment
						if(trainees[traineeKey].assessments[assessment.assessmentId]){
							// Only increment count and add to total if score is
							// greater than or equal to 0 -jack / trevor
							var score = trainees[traineeKey].assessments[assessment.assessmentId].score;
							if(score >= 0){
								// replaces 0 with arbitrary low number for
								// valid average calculations - Trevor Lory
								if(score === 0) {
									score = 0.00001;
								}
								$scope.assessmentTotals[assessment.assessmentId].total+= Number(score);								
								$scope.assessmentTotals[assessment.assessmentId].count +=1;
							}
						}
					}
					return $scope.assessmentTotals[assessment.assessmentId].total / $scope.assessmentTotals[assessment.assessmentId].count ;
				};

				/** *Save Button ** */
				$scope.showSaving = false;
				$scope.showCheck = false;
				$scope.showFloppy = true;

				/**
				 * doBurrito is called when the Save button is clicked The
				 * button does nothing but tricks the user into invoking the
				 * ng-blur on the field they are focused.
				 * 
				 * @author Jack Duong
				 * @author Patrick Walsh (approver)
				 */
				$scope.doBurrito =function(){
					$scope.showFloppy = false;
					$timeout(function(){
						$scope.showSaving=true;								
					},480).then(function(){
						$timeout(function(){
							$scope.showSaving=false;
						}, 1000).then(function(){
							$scope.showCheck = true;
							$timeout(function(){
								$scope.showCheck = false;
							},2000).then(function(){
								$scope.showFloppy = true;
							});
						});
					});
				};

				$scope.stopBurrito = function(traineeId){
					$scope.trainees[traineeId].burrito=false;
				};

				$scope.reloadController = function() {
					$state.reload();
				};

				$rootScope.$on('trainerasses',function(){
					$scope.trainees={};						

					for(const trainee of $scope.currentBatch.trainees){
						$scope.assignTraineeScope(trainee.traineeId);
					}
          
					/***********************************************************
					 * UPDATE ASSESSMENT
					 **********************************************************/

					// sets the update assessment model with the current
					// assessment data
					$scope.setUpdateAssessmentToCurrent = function(index){
						$log.debug(index);
						$scope.updateAssessmentModel.category = $scope.currentAssessments[index].category;
						$scope.updateAssessmentModel.rawScore = $scope.currentAssessments[index].rawScore;
						$scope.updateAssessmentModel.type = $scope.currentAssessments[index].type;
						$log.debug($scope.currentAssessments[index]);
					}
						
					$scope.updateAssessment = function(assessment,event,modalId,index){
						event.stopPropagation();
						if($scope.updateAssessmentModel !==undefined){
							$log.debug(index);
							if($scope.updateAssessmentModel.category){
								assessment.category=$scope.updateAssessmentModel.category;
							}
							if($scope.updateAssessmentModel.type){
								assessment.type=$scope.updateAssessmentModel.type;
							}
							if($scope.updateAssessmentModel.rawScore){
								assessment.rawScore=$scope.updateAssessmentModel.rawScore;
							}
							// call delegate if at least one field was changed
							if($scope.updateAssessmentModel.category || $scope.updateAssessmentModel.type || $scope.updateAssessmentModel.rawScore){
								caliberDelegate.trainer.updateAssessment(assessment)
								.then(function(response){
									$log.debug("the assessment has been updated");
									return response;
								}).then(function(response){
								$('.modal').modal('hide');										
										$scope.currentAssessments[index] = response;
										$log.debug($scope.currentBatch.batchId, $scope.currentWeek);
										getAllAssessmentsForWeek($scope.currentBatch.batchId, $scope.currentWeek);									
								});
							}
						}
						$('.modal').modal('hide');
					};
					
					// delete the assessment clicked
					$scope.deleteAssessment = function(assessment,event,modalId,index){
						$('.modal').modal('hide');
						$('.modal-backdrop').remove();
						event.stopPropagation();
						$log.debug("im deleting an assessment" + $scope.currentAssessments);
						caliberDelegate.trainer.deleteAssessment($scope.currentAssessments[index].assessmentId)
						.then(function(response){																					
							return response;
						}).then(function(){
							$log.debug("im deleting assessment");
							getAllAssessmentsForWeek($scope.currentBatch.batchId, $scope.currentWeek);
						});
					};
					
					 // set children of modal to false on click to prevent
						// modal from fading out - jack
					 
					$scope.preventModalClose = function(){
						$(".editAssessModal .modal-body, .editAssessModal .modal-footer, .editAssessModal form").on("click", function(e){
							e.stopPropagation();
						});
						$('.editAssessModal').on("click",function(e) {
							e.stopPropagation();
						    $(".modal").modal("hide");
						});
					};
					
					$scope.closeModal = function(str){
						$('#'+str).modal('toggle');
					};
				getAllAssessmentsForWeek(
							$scope.currentBatch.batchId,
							$scope.currentWeek);
				});

				$rootScope.$on('GET_TRAINEE_OVERALL_CTRL',function(event,traineeId){
					$log.debug(traineeId);
				});

				// Used to sort trainees in batch
				function compare(a, b) {
					if (a.name < b.name)
						return -1;
					if (a.name > b.name)
						return 1;
					return 0;
				}
				/***************************************************************
				 * UPDATE ASSESSMENT
				 **************************************************************/
				$scope.updateAssessment = function(assessment,event,modalId,index){
					event.stopPropagation();
					if($scope.updateAssessmentModel !==undefined){
						$log.debug(index);
						// $log.debug($scope.currentAssessments[$index]
						// + " ------ " + $index);
						if($scope.updateAssessmentModel.category){
							assessment.category=$scope.updateAssessmentModel.category;
						}
						if($scope.updateAssessmentModel.type){
							assessment.type=$scope.updateAssessmentModel.type;
						}
						if($scope.updateAssessmentModel.rawScore){
							assessment.rawScore=$scope.updateAssessmentModel.rawScore;
						}
						// call delegate if at least one field was
						// changed
						if($scope.updateAssessmentModel.category || $scope.updateAssessmentModel.type || $scope.updateAssessmentModel.rawScore){
							caliberDelegate.trainer.updateAssessment(assessment)
							.then(function(response){
								$log.debug("the assessment has been updated");
								return response;
							}).then(function(response){
								$('.modal').modal('hide');										
								$scope.currentAssessments[index] = response;
								$log.debug($scope.currentBatch.batchId, $scope.currentWeek);
								getAllAssessmentsForWeek($scope.currentBatch.batchId, $scope.currentWeek);									
							});
						}
					}
					$('.modal').modal('hide');
				};

				// delete the assessment clicked
				$scope.deleteAssessment = function(assessment,event,modalId,index){
					$('.modal').modal('hide');
					$('.modal-backdrop').remove();
					event.stopPropagation();
					$log.debug("im deleting an assessment" + $scope.currentAssessments);
					caliberDelegate.trainer.deleteAssessment($scope.currentAssessments[index].assessmentId)
					.then(function(response){																					
						return response;
					}).then(function(){
						$log.debug("im deleting assessment");
						getAllAssessmentsForWeek($scope.currentBatch.batchId, $scope.currentWeek);
					});
				};
				/*
				 * set children of modal to false on click to prevent modal from
				 * fading out when clicking on child elements - jack
				 */
				$scope.preventModalClose = function(){
					$(".editAssessModal .modal-body, .editAssessModal .modal-footer, .editAssessModal form").on("click", function(e){
						e.stopPropagation();
					});
					$('.editAssessModal').on("click",function(e) {
						e.stopPropagation();
						$(".modal").modal("hide");
					});
				};

				$scope.closeModal = function(str){
					$('#'+str).modal('toggle');
				};

				// this method will return the proper string depending if there
				// is an average for the week or not

				$scope.boldBatchAverage = function(){
					if($scope.allAssessmentsAvgForWeek){
						$scope.isThereAvgForWeek = true;
						return "Weekly Batch Avg: ";
					}else{
						$scope.isThereAvgForWeek = false;
						return "Calculating Weekly Batch Avg ";
					}
				};
				/*
				 * if grade is less than 0 or greater than 100 return true; This
				 * will set css class .has-error to grade input box - hack
				 */
				$scope.validateGrade=function(grade){
					if(grade > 0 && grade <=100){
						return false;
					}else if(grade === undefined || grade > 100 ||grade < 0 ){
						return true;
					}
				};
				$scope.returnGradeFormName = function(assessment){
					$log.debug(assessment);
				};

				// Get categories for the week
				function categories() {
					if(!$scope.currentBatch)
						return;
					caliberDelegate.qc
					.getAllAssessmentCategories(
							$scope.currentBatch.batchId,
							$scope.currentWeek).then(
									function(response) {
										$scope.categories = response;
									});

				}
				var status =  null;

				/**
				 * ********************************************************************
				 * Flag Control
				 * ********************************************************************
				 */

				/**
				 * decides to show the form to submit the flagNotes for the
				 * associate
				 */
				$scope.showCommentForm = [];

				/**
				 * decides to show the read-only comment box to view
				 * flagNotes for the associate
				 */
				$scope.showCommentBox = [];

				/**
				 * creates a function triggered by a click on the trainee's
				 * name, that toggles the color of the flag and opens an
				 * input box to comment on the color change
				 */

				// Set flags to color in database
				$scope.initFlags = function(trainee, index) {
					var flag = document
							.getElementsByClassName("glyphicon-flag")[index];
					if (flag == undefined)
						return;
					var flagColor = trainee.flagStatus;
					if (flagColor === 'RED') {
						flag.setAttribute("class",
								"glyphicon glyphicon-flag color-red");
					} else if (flagColor === 'GREEN') {
						flag.setAttribute("class",
								"glyphicon glyphicon-flag color-green");
					} else {
						flag.setAttribute("class",
								"glyphicon glyphicon-flag color-white");
					}
				}

				var status = null;
				$scope.toggleColor = function(trainee, index) {
					var flag = document
							.getElementsByClassName("glyphicon-flag")[index];
					var initialStatus = trainee.flagStatus;
					if (flag.getAttribute("class") === "glyphicon glyphicon-flag color-white") {
						status = "RED";
						flag.setAttribute("class",
								"glyphicon glyphicon-flag color-red");
					} else if (flag.getAttribute("class") === "glyphicon glyphicon-flag color-red") {
						status = "GREEN";
						flag.setAttribute("class",
								"glyphicon glyphicon-flag color-green");
					} else if (flag.getAttribute("class") === "glyphicon glyphicon-flag color-green") {
						status = "NONE";
						flag.setAttribute("class",
								"glyphicon glyphicon-flag color-white");
					}
					if (initialStatus !== status) {
						commentBox(flag, status, initialStatus, index,
								trainee);
					} else {
						$scope.hideNotes(index);
					}
				}

				/**
				 * opens up a comment form box when the flag color changes
				 */
				function commentBox(flag, status, initialStatus, index,
						trainee) {
					$scope.showCommentForm[index] = true;
					$scope.closeComment = function() {
						$scope.showCommentForm[index] = false;
						if (initialStatus === "RED") {
							flag.setAttribute("class",
									"glyphicon glyphicon-flag color-red");
						} else if (initialStatus === "GREEN") {
							flag.setAttribute("class",
									"glyphicon glyphicon-flag color-green");
						} else {
							flag.setAttribute("class",
									"glyphicon glyphicon-flag color-white");
						}
					}
					trainee.batch = {
						batchId : $scope.currentBatch.batchId
					};
				}

				/**
				 * saves changes the flag status in the javascript object
				 * and persists it back to the database upon submission of
				 * the comment form and closes the form
				 */
				$scope.updateFlag = function(trainee, index) {
					trainee.flagStatus = status;
					caliberDelegate.all.updateTrainee(trainee);
					$scope.showCommentForm[index] = false;
				}

				// show flagNotes when hovering over flag
				$scope.showNotes = function(index) {
					if ($scope.currentBatch.trainees[index].flagNotes != null) {
						$scope.showCommentBox[index] = true;
					}
				}

				// hide flagNotes when no there is no flag hover
				$scope.hideNotes = function(index) {
					$scope.showCommentBox[index] = false;
				}

				function resetFlags() {
					$scope.showCommentForm = [];
					$scope.showCommentBox = [];
					angular.forEach($scope.currentBatch.trainees, function(trainee, index) {
						$scope.initFlags(trainee, index);
					});
				}

				
				$scope.quiz = {
						data: null
				}
				$scope.importGrade = function() {
					caliberDelegate.trainer.importGrade($scope.quiz.data, $scope.currentWeek, $scope.currentBatch.batchId)
					.then(function(response) {
						getAllAssessmentsForWeek($scope.currentBatch.batchId, $scope.currentWeek);
					}, function(response) {
						$scope.importGradeError = response.data.message;
						angular.element("#importGradeErrorModal").modal("show");
					});
					$('.modal').modal('hide');
				}
		});