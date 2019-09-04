angular.module("api").factory("allFactory", function($log, $http) {

	$log.debug("Booted all api factory");
	/**
	 * commented-out Trainer Task code is in service of incomplete issue #791
	 */

	var all = {};

	/** ************************* Enum constants *********************** */
	all.enumCommonLocations = function() {
		return $http({
			url : "/all/locations",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumAssessmentType = function() {
		return $http({
			url : "/types/assessment/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumNoteType = function() {
		return $http({
			url : "/types/note/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumQCStatus = function() {
		return $http({
			url : "/types/qcstatus/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumSkillType = function() {
		return $http({
			url : "/types/skill/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumTrainingStatus = function() {
		return $http({
			url : "/types/trainingstatus/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumTrainingType = function() {
		return $http({
			url : "/types/training/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumTrainerTier = function() {
		return $http({
			url : "/types/trainer/role/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};
	
	all.enumPanelStatus = function() {
		return $http({
			url : "/types/panelstatus/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumInterviewFormat = function() {
		return $http({
			url : "/types/interviewformat/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumPanelStatus = function() {
		return $http({
			url : "/types/panelstatus/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.enumInterviewFormat = function() {
		return $http({
			url : "/types/interviewformat/all",
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	/**
	 * @param allcategories
	 * @returns {*}
	 */

	all.getAllCategories = function() {
		return $http({
			url : "/category/all/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Categories successfully retrieved.");
			$log.debug(response.data);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};
	
	/** ************************* Batch *********************** */

	/**
	 * 
	 * 
	 */
	all.importAvailableBatches = function() {
		return $http({
			url : "/all/batch/import",
			method : "GET",
		}).then(function(response) {
			$log.debug("Object successfully imported");
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	/**
	 * 
	 * @param batchObj
	 */
	all.createBatch = function(batchObj) {
		return $http({
			url : "/all/batch/create",
			method : "POST",
			data : batchObj
		}).then(function(response) {
			$log.debug("Object successfully created");
			$log.debug(response);
			// return id
			return response;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	/**
	 * 
	 * @param batchObj
	 * @returns {*}
	 */
	all.updateBatch = function(batchObj) {
		return $http({
			url : "/all/batch/update",
			method : "PUT",
			data : batchObj
		}).then(function(response) {
			$log.debug("Object successfully updated");
			$log.debug(response);
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	/**
	 * 
	 * @param batchId
	 * @returns {*}
	 */
	all.deleteBatch = function(batchId) {
		return $http({
			url : "/all/batch/delete/" + batchId,
			method : "DELETE"
		}).then(function(response) {
			$log.debug("Batch successfully deleted");
			$log.debug(response);
			return response;
		}, function(response) {
			$log.error("There was an error: " + response.status);
			return response;
		});
	};

	/** ************************* Trainee *********************** */

	all.getAllTraineesFromBatch = function(resourceId) {
		return $http({
			url : "/all/trainee/import?resourceId=" + resourceId,
			method : "GET"
		}).then(function(response) {
			$log.debug("Trainees successfully imported");
			$log.debug(response.data);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
			return response.data;
		});
	};

	/**
	 * 
	 * @param batchId
	 * @returns {*}
	 */
	all.getDroppedTrainees = function(batchId) {
		return $http({
			url : "/all/trainee/dropped?batch=" + batchId,
			method : "GET",
		}).then(function(response) {
			$log.debug("Dropped trainees successfully fetched.");
			$log.debug(response.data);
			// return id
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
			return response.data;
		});
	};

	/**
	 * 
	 * @param traineeObj
	 * @returns {*}
	 */
	all.createTrainee = function(traineeObj) {
		return $http({
			url : "/all/trainee/create",
			method : "POST",
			data : traineeObj
		}).then(function(response) {
			$log.debug("Trainee successfully created.");
			$log.debug(response);
			// return id
			return response;
		}, function(response) {
			$log.error("There was an error: " + response.status);
			return response.data;
		});
	};

	/**
	 * 
	 * @param traineeObj
	 * @returns {*}
	 */
	all.updateTrainee = function(traineeObj) {
		return $http({
			url : "/all/trainee/update",
			method : "PUT",
			data : traineeObj
		}).then(function(response) {
			$log.debug("Trainee successfully updated.");
			$log.debug(response);
		}, function(response) {
			$log.error("There was an error: " + response.status);
			return false;
		});
	};
	
	all.switchBatch = function(traineeId, batchId) {
		return $http({
			url : "/all/trainee/switch/trainee/" + traineeId + "/batch/" + batchId, 
			method : "PUT"
		}).then(function(response) {
			$log.debug("Trainee successfully updated.");
			$log.debug(response);
		}, function(response) {
			$log.error("There was an error: " + response.status);
			return false;
		});
	};

	all.getTraineeEmail = function(traineeEmail) {
		return $http({
			url : "/all/trainee/getByEmail/" + traineeEmail + "/",
			method : "GET",
		}).then(function(response) {
			$log.log(traineeEmail);
			$log.log(response);
			return response;
		}, function(response) {
			$log.log(traineeEmail);
			$log.error("There was an error: " + response.status);
			return response;
		});
	};

	/**
	 * 
	 * @param traineeId
	 */
	all.deleteTrainee = function(traineeId) {
		$http({
			url : "/all/trainee/delete/" + traineeId,
			method : "DELETE"
		}).then(function(response) {
			$log.debug("Trainee successfully deleted.");
			$log.debug(response);
			return response;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};
	
	all.searchTrainee = function (searchTerm) {
		return $http({
			url: '/all/trainee/search/' + searchTerm + '/',
			method: 'GET'
		});
	};

	/** ************************ Grades ************************* */

	/**
	 * Gets all grades for a given Assessment
	 * 
	 * @param assessmentId
	 * @returns {*}
	 */
	all.getGrades = function(assessmentId) {
		return $http({
			url : "/all/grades/assessment/" + assessmentId,
			method : "GET"
		}).then(function(response) {
			$log.debug("Grades successfully retrieved.");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};
	all.getGradesForWeek = function(batchId, weekId) {
		return $http({
			url : "/all/grades/batch/" + batchId + "/week/" + weekId,
			method : "GET"
		}).then(function(response) {
			$log.debug("Grades for week successfully retrieved.");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.getAllTraineeNotes = function(traineeId) {
		return $http({
			url : "/all/notes/trainee/" + traineeId,
			method : "GET"
		}).then(function(response) {
			return response.data
		}, function(response) {
			$log.error("There was an error: " + response.status);
		})
	};

	all.getAssessmentsAverageForWeek = function(batchId, weekId) {
		return $http({
			url : "/all/assessments/average/" + batchId + "/" + weekId,
			method : "GET"
		}).then(function(response) {
			return response.data;
		}, function(response) {
			$log.error("There was a error " + response.status);
		});
	};

	/** *********************** Location ********************* */
	all.getAllLocations = function() {
		return $http({
			url : "/all/location/all/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Locations successfully retrieved");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.createLocation = function(locationObj) {
		$log.debug(locationObj);
		return $http({
			url : "/all/location/create",
			method : "POST",
			data : locationObj
		}).then(function(response) {
			$log.debug("Location successfully created.");
			$log.debug(response);
			return response;
		}, function(response) {
			$log.error("There was an error: " + response.status);
			return response.data;
		});
	};

	/** *********************** Location ********************* */
	all.getAllLocations = function() {
		return $http({
			url : "/all/location/all/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Locations successfully retrieved");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	all.createLocation = function(locationObj) {
		$log.debug(locationObj);
		return $http({
			url : "/all/location/create",
			method : "POST",
			data : locationObj
		}).then(function(response) {
			$log.debug("Location successfully created.")
			$log.debug(response);
			return response;
		}, function(response) {
			$log.error("There was an error: " + response.status);
			return response.data;
		});
	};

	/** *********************** Trainer ********************* */

	/**
	 * 
	 * @returns {*}
	 */
	all.getAllTrainers = function() {
		return $http({
			url : "/all/trainer/all/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Trainers successfully retrieved");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	/**
	 * 
	 * @returns {*}
	 */
	all.importAvailableBatches = function() {
		return $http({
			url : "/all/batch/import/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Object successfully imported");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	/**
	 * 
	 * @param trainerObj
	 * @returns {*}
	 */
	all.createTrainer = function(trainerObj) {
		$log.debug(trainerObj);
		return $http({
			url : "/all/trainer/create",
			method : "POST",
			data : trainerObj
		}).then(function(response) {
			$log.debug("Trainer successfully created.");
			$log.debug(response);
			return response;
		}, function(response) {
			$log.error("There was an error: " + response.status);
			return response.data;
		});
	};
	
	/** *********************** Trainer Tasks********************* */
	
	/**
	 * 
	 * @returns {*}
	 */
	/*
	all.getAllActiveTasks = function() {
		return $http({
			url : "/all/tasks/all/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Trainer tasks successfully retrieved");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	}; */
	
	/**
	 * 
	 * @returns {*}
	 */
	/*
	all.getAllCompletedTasks = function() {
		return $http({
			url : "/all/tasks/trainer/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Trainer tasks successfully retrieved");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};
	*/
	/**
	 * 
	 * @returns {*}
	 */
	/*
	all.getAllTasksByTrainerId = function(id) {
		return $http({
			url : "/all/tasks/trainer/" + id + "/",
			method : "GET"
		}).then(function(response) {
			$log.debug("Trainer tasks successfully retrieved");
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};
	*/
	/***************************************************************************
	 * Server generates PDF from HTML Download via response data
	 * 
	 **************************************************************************/
	all.generatePDF = function(title, html) {
		return $http({
			url : "/report/generate?title=" + title,
			method : "POST",
			data : html,
			responseType : "arraybuffer"
		}).then(function(response) {
			$log.debug(response);
			return response.data;
		}, function(response) {
			$log.error("There was an error: " + response.status);
		});
	};

	return all;
	
});