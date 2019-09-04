/**
 * API for making trainer related AJAX calls caliber endpoints
 * 
 * @param $log
 * @param $http
 * @returns {{}}
 */
angular.module("api").factory("trainerFactory", function($log, $http) {
	$log.debug("Booted Trainer API");
	var trainer = {};

	/** ************************* Batch *********************** */
	// grab all batches
	trainer.getAllBatches = function() {
		return $http({
			url : "/trainer/batch/all/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Batches successfully retrieved");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};
	/** ******************** Import Batch ******************** */
	// import all batches
	trainer.importAllBatches = function() {
		return $http({
			url : "/trainer/batch/all/importget",
			method : "GET"
		}).then(function(response) {
			$log.debug("Imported Batches successfully retrieved");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	/** ************************* Week *********************** */
	// create a new week
	trainer.createWeek = function(batchId) {
		return $http({
			url : "/trainer/week/new/" + batchId + "/",
			method : "POST",
		}).then(function(response) {
			$log.debug("Week successfully created.");
			$log.debug(response);
			// return id
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	/** ************************* Grade *********************** */
	// add a new grade
	trainer.addGrade = function(gradeObj) {
		return $http({
			url : "/trainer/grade/create/",
			method : "POST",
			data : gradeObj
		}).then(function(response) {
			$log.debug("Grade successfully created.");
			$log.debug(response);
			return response;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	// update trainer grade
	trainer.updateGrade = function(gradeObj) {
		return $http({
			url : "/trainer/grade/update/",
			method : "PUT",
			data : gradeObj
		}).then(function(response) {
			$log.debug("Grade successfully updated");
			$log.debug(response);
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	// import grades using JSON
	trainer.importGrade = function(gradeJson, week, batchId) {
		return $http({
			url : "/trainer/grade/import?week=" + week + "&batchId=" + batchId,
			method : "POST",
			data : gradeJson
		});
	};

	/** *********************** Assessment ********************** */
	// create assessment
	trainer.createAssessment = function(assessmentObj) {
		return $http({
			url : "/trainer/assessment/create/",
			method : "POST",
			data : assessmentObj,
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function(response) {
			$log.debug("Assessment successfully created.");
			$log.debug(response);
			// return id
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	// get all assessments
	trainer.getAllAssessmentsForWeek = function(batchId, week) {
		return $http({
			url : "/trainer/assessment/" + batchId + "/" + week + "/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Assessments successfully retrieved");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	// update assessment
	trainer.updateAssessment = function(assessmentObj) {
		return $http({
			url : "/trainer/assessment/update",
			method : "PUT",
			data : assessmentObj
		}).then(function(response) {
			$log.debug("Assessments successfully updated");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	// delete assessment
	trainer.deleteAssessment = function(assessmentId) {
		return $http({
			url : "/trainer/assessment/delete/" + assessmentId + "/",
			method : "DELETE"
		}).then(function(response) {
			$log.debug("Assessment successfully deleted");
			$log.debug(response);
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	/** ************************ Notes ************************ */
	// Call EvaluationController's findTrainerBatchNotes method
	trainer.getTrainerBatchNote = function(batchId, week) {
		return $http({
			url : "/trainer/note/batch/" + batchId + "/" + week + "/",
			method : "GET"
		}).then(function(response) {
			$log.log("Trainer Batch Note retrieved successfully");
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	trainer.getTraineeBatchNotesForWeek = function(batchId, week) {
		return $http({
			url : "/trainer/note/trainee/" + batchId + "/" + week + "/",
			method : "GET"
		}).then(function(response) {
			if (response.data) {
				return response.data;
			} else {
				return response;
			}
		}, function(response) {
			$log.error("Error retrieving " + response.status);
		});
	};

	trainer.getTraineeNote = function(traineeId, week) {
		return $http({
			url : "/trainer/note/trainee/" + traineeId + "/for/" + week + "/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Notes successfully fetched");
			$log.debug(response);
			if (response.data) {
				return response.data;
			} else {
				return response;
			}
		}, function(response) {
			$log.error("Error retrieving " + response.status);
		})
	};

	trainer.createNote = function(noteObj) {
		return $http({
			url : "/note/create/",
			method : "POST",
			data : noteObj
		}).then(function(response) {
			$log.debug("Notes successfully created");
			$log.debug(response);
			// return id
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	trainer.updateNote = function(noteObj) {
		return $http({
			url : "/note/update/",
			method : "POST",
			data : noteObj
		}).then(function(response) {
			$log.debug("Assessments successfully updated");
			$log.debug(response);
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	trainer.saveOrUpdateNote = function(noteObj) {
		return $http({
			url : "/note/update/",
			method : "POST",
			data : noteObj
		}).then(function(response) {
			$log.debug("note saved");
			$log.debug(response);
			return response;
		}, function(response) {
			$log.error("there was an error " + response.status);
		});
	};
	return trainer;
});