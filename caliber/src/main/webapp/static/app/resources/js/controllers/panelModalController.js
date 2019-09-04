/**
 * Team Tyranoformus Rex
 * 
 * @author Lauren Wallace
 * @author Katie Bixby
 * @author Nathan Koszuta
 * @author Matt "Spring Data" Prass
 * @author Connor Monson
 */

angular
		.module("panel")
		.controller(
				"panelModalController",
				function($rootScope, $scope, $state, $log, $cookies, $timeout, 
						caliberDelegate, allBatches) {

					$log.debug("in panel controller");
					$log.debug("Booted panel controller.");

					$scope.errorMessage = 'Failed to create panel for trainee ';
					$scope.showMessage = false;

					$scope.newPanel = {};
					$scope.techFeedback = [];
					$scope.feedbacksToReturn = [];
					$scope.counter = 0;
					$scope.techId = 0;

					// Objects or list of objects that need to be created for
					// this form
					$scope.traineePanels = [];
					$scope.employedTrainees = [];
					$scope.batchSkillType = {};
					$scope.isFeedbackAllPassing = false;

					// Create the form models & their options
					$scope.trainee = {
						name : null
					};

					$scope.recordingConsent = {
						model : null,
						options : [ {
							exp : 'Yes',
							value : 'Yes'
						}, {
							exp : 'No',
							value : 'No'
						}, {
							exp : 'Did Not Ask',
							value : 'Did Not Ask'
						} ]
					};

					$scope.panelStatus = {
						model : null,
						options : []
					};

					// Stores overall panelStatus
					$scope.repanel = {
						model : null,
						options : []
					};

					$scope.interviewMode = {
						model : null,
						options : []
					};

					$scope.interviewConnectivity = {
						model : null,
						options : [ 'Stable', 'Unstable' ]
					};

					$scope.panelResult = {
						model : null,
						options : [ {
							exp : '0 - Very Poor',
							value : 0
						}, {
							exp : '1 - Moderately Poor',
							value : 1
						}, {
							exp : '2 - Fairly Poor',
							value : 2
						}, {
							exp : '3 - Poor',
							value : 3
						}, {
							exp : '4 - Below Average',
							value : 4
						}, {
							exp : '5 - Average',
							value : 5
						}, {
							exp : '6 - Above Average',
							value : 6
						}, {
							exp : '7 - Good',
							value : 7
						}, {
							exp : '8 - Very Good',
							value : 8
						}, {
							exp : '9 - Great',
							value : 9
						}, {
							exp : '10 - Excellent',
							value : 10
						} ]
					};

					$scope.technologies = {
						model : null,
						options : []
					};

					$scope.overallStatus = {
						model : null,
						options : []
					};

					$scope.associateIntro = {
						model : null
					};

					$scope.p1Expl = {
						model : null
					};

					$scope.p2Expl = {
						model : null
					};

					$scope.p3Expl = {
						model : null
					};

					$scope.communicationSkills = {
						model : null
					};

					$scope.interviewDuration = {
						model : null
					};

					$scope.panelRound = {
						model : null
					};

					$scope.overallPanel = {
						model : null
					};

					$scope.recordingLink = {
						model : null
					};

					$scope.interviewDuration = {
						model : null
					};

					$scope.interviewDate = {
						model : null
					};

					$scope.interviewTime = {
						model : null
					};

					$scope.techComment = {
						model : null
					};

					// Get all panel status on load up
					caliberDelegate.all.enumPanelStatus().then(
							function(response) {
								$log.debug(response);
								$scope.panelStatus.options = response;
								$scope.repanel.options = response;
								$scope.overallStatus.options = response;
							});

					// Get all interview modes
					caliberDelegate.all.enumInterviewFormat().then(
							function(response) {
								$log.debug(response);
								$scope.interviewMode.options = response;
							});

					// Get all categories on load up
					caliberDelegate.all.getAllCategories().then(
							function(response) {
								$log.debug(response);
								$scope.technologies.options = response;
							});

					// *******************************************************************************
					// *** Tables
					// *******************************************************************************

					$scope.selectChosenTrainee = function() {
						$log.debug('selected trainee= ' + $scope.chosenTrainee);
						var traineeName = $scope.chosenTrainee;

						$scope.employedTrainees.forEach(function(trainee) {
							if (trainee.name === traineeName) {
								traineeId = trainee.traineeId;
							}
						});
						caliberDelegate.panel
								.reportTraineePanels(traineeId)
								.then(
										function(response) {
											$scope.traineePanels = response;
											$scope.trainee.name = traineeName;
											$scope.panelRound.model = $scope.traineePanels.length + 1;
											$log.debug('trainee name= '
													+ $scope.trainee.name);
											$log.debug($scope.trainee.name);
											$scope.currentBatch();
											$log.debug($scope.traineePanels);
											$timeout(
													function() {
														$scope.newPanel = {};
														$('.highlight').attr(
																'class',
																'no-highlight');
														$timeout(
																function() {
																	$(
																			'.no-highlight')
																			.removeClass(
																					'no-highlight');
																}, 2000);
													}, 1000);
										});

						$log.debug($scope.trainee);
					};

					// Retrieved trainees upon user input, not before
					$scope.populateTraineeList = function() {
						if($scope.chosenTrainee.indexOf('@') === -1){
							return;
						}
						// ask backend for trainees
						caliberDelegate.all.searchTrainee($scope.chosenTrainee).then(function(response){
							$scope.employedTrainees = response.data;
						});
						$log.debug($scope.employedTrainees);
					}

					function getTrainees() {
						// for each batch, add the trainees to an overall list
						// of trainees
						allBatches.forEach(function(batch) {
							batch.trainees.forEach(function(trainee) {
								$scope.employedTrainees.push(trainee);
							});
						});
					}
					function createTechFeedback(id, tech, result, repanel,
							comment) {
						return {
							"id" : id,
							"technology" : tech,
							"result" : result,
							"status" : repanel,
							"comment" : comment
						};
					}

					function isRepanel(arg) {
						return arg.status === 'Pass';
					}

					$scope.addRow = function() {
						var theFeedback = createTechFeedback($scope.techId,
								$scope.technologies.model,
								$scope.panelResult.model.exp,
								$scope.repanel.model, $scope.techComment.model);
						$scope.techFeedback.push(theFeedback);
						$scope.feedbacksToReturn
								.push(createTechFeedback($scope.techId,
										$scope.technologies.model,
										$scope.panelResult.model.value,
										$scope.repanel.model,
										$scope.techComment.model));
						$log.debug($scope.feedbacksToReturn);

						$scope.isFeedbackAllPassing = $scope.feedbacksToReturn
								.every(isRepanel);
						$log.debug($scope.isFeedbackAllPassing);
						if ($scope.isFeedbackAllPassing) {
							$scope.overallStatus.model = 'Pass';
						} else {
							$scope.overallStatus.model = 'Repanel';
						}

						if ($scope.feedbacksToReturn.length === 0) {
							$scope.overallStatus.model = '';
						}

						$scope.technologies.model = null;
						$scope.panelResult.model = null;
						$scope.repanel.model = null;
						$scope.techComment.model = null;

					}

					$scope.deleteRow = function(loc) {
						$scope.techFeedback.splice(loc, 1);
						$scope.feedbacksToReturn.splice(loc, 1);
						$log.debug($scope.feedbacksToReturn);

						$scope.isFeedbackAllPassing = $scope.feedbacksToReturn
								.every(isRepanel);
						$log.debug($scope.isFeedbackAllPassing);
						if ($scope.isFeedbackAllPassing) {
							$scope.overallStatus.model = 'Pass';
						} else {
							$scope.overallStatus.model = 'Repanel';
						}

						if ($scope.feedbacksToReturn.length === 0) {
							$scope.overallStatus.model = '';
						}
					}

					// Sets the Training Type
					$scope.currentBatch = function() {
						$log.debug("In skillType funtion");
						allBatches.forEach(function(batch) {
							batch.trainees.forEach(function(trainee) {
								if (trainee.name === $scope.trainee.name) {
									$log.debug($scope.trainee);
									$log.debug(batch);
									for ( const prop in trainee) {
										$scope.trainee[prop] = trainee[prop];
									}
									$scope.batchSkillType = batch.skillType;
								}
							});
						});
						$log.debug($scope.batchSkillType);
					};

					$scope.savePanel = function() {

						var panel = {
							trainee : $scope.trainee,
							panelist : {},
							interviewDate : $scope.interviewDate.model,
							duration : $scope.interviewDuration.model,
							format : $scope.interviewMode.model,
							internet : $scope.interviewConnectivity.model,
							panelRound : $scope.panelRound.model,
							recordingConsent : $scope.recordingConsent.model ? $scope.recordingConsent.model.value
									: null,
							recordingLink : $scope.recordingLink.model,
							status : $scope.overallStatus.model,
							feedback : $scope.feedbacksToReturn,
							associateIntro : $scope.associateIntro.model,
							projectOneDescription : $scope.p1Expl.model,
							projectTwoDescription : $scope.p2Expl.model,
							projectThreeDescription : $scope.p3Expl.model,
							communicationSkills : $scope.communicationSkills.model,
							overall : $scope.overallPanel.model
						};
						$log.debug(panel);

						caliberDelegate.panel
								.createPanel(panel)
								.then(
										function(createdPanel) {
											if (createdPanel) {
												$log.debug('got good panel');
												$scope.resetPanelForm();
												if (!$scope.newPanel) {
													$scope.newPanel = {};
												}
												for ( const prop in createdPanel) {
													$scope.newPanel[prop] = createdPanel[prop];
												}
												$scope
														.selectChosenTrainee($scope.trainee.name);
											} else {
												showErrorMessage();
											}
										});
					};

					function showErrorMessage() {
						$log
								.debug('showing error message after failed panel creation');
						$scope.showMessage = true;
						$timeout(function() {
							$scope.showMessage = false;
						}, 7000);
					}
					;

					// Resets the Panel Feedback Form
					$scope.resetPanelForm = function() {
						$scope.techFeedback = [];
						$scope.feedbacksToReturn = [];
						$scope.panelRound.model = null;
						$scope.recordingConsent.model = null;
						$scope.interviewDate.model = null;
						$scope.interviewTime.model = null;
						$scope.interviewMode.model = null;
						$scope.interviewConnectivity.model = null;
						$scope.associateIntro.model = null;
						$scope.p1Expl.model = null;
						$scope.p2Expl.model = null;
						$scope.p3Expl.model = null;
						$scope.communicationSkills.model = null;
						$scope.technologies.model = null;
						$scope.panelResult.model = null;
						$scope.repanel.model = null;
						$scope.techComment.model = null;
						$scope.overallPanel.model = null;
						$scope.overallStatus.model = null;
						$scope.recordingLink.model = null;
						$scope.interviewDuration.model = null;
					};
				});
