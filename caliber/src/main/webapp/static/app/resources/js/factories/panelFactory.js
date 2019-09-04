/**
 * API that makes panel related AJAX calls to the back-end
 * 
 * @author Nathan Koszuta
 * @author Daniel Fairbanks
 * @author Emma Bownes
 * @author Matt 'Spring Data' Prass
 * @author Lauren Wallace
 * 
 * @param $log
 * @param $http
 * 
 * @returns {{}}
 */

angular.module('api').factory('panelFactory', function($log, $http) {
	$log.debug('Booted Panel API');
	const panel = {};

	/** ******************* Panel ************************** */

	// Gets all panels
	panel.findAllPanels = function () {
		return $http({
			url: '/panel/all',
			method: 'GET'
		}).then(function (response) {
			$log.debug('Panel - AllPanels -- success');
			$log.debug(response);
			return response.data;	
		}, function (error) {
			$log.error('There was an error: ' + error.status);
		});
	};

	// Gets the most recent panels for all trainees in a batch
	panel.reportPanelTable = function (batchId) {
		return $http({
			url: '/all/reports/batch/' + batchId + '/panel-batch-all-trainees',
			method: 'GET'
		}).then(function (response) {
			$log.debug('Panel - Batch - batchId -- success');
			$log.debug(response);
			return response.data;
		}, function (error) {
			$log.error('There was an error: ' + error.status);
		});
	};

	// Get a panel by its ID
	panel.getPanelById = function (panelId) {
		return $http({
			url: '/panel/' + panelId,
			method: 'GET'
		}).then(function (response) {
			$log.debug('Panel - panelId -- success');
			$log.debug(response);
			return response.data;
		}, function (error) {
			$log.error('There was an error: ' + error.status);
		});
	};

	// Get all panels for a single trainee
	panel.reportTraineePanels = function (traineeId) {
		return $http({
			url : '/panel/trainee/' + traineeId,
			method : 'GET'
		}).then(function (response) {
			$log.debug('Retrieve all panels for trainee successful');
			$log.debug(response);
			return response.data;
		}, function (error) {
			$log.error('There was an error: ' + error.status);
		});
	};

	// Get all repanels across all trainees
	panel.reportAllRepanels = function () {
		return $http({
			url : '/panel/repanel/all',
			method : 'GET'
		}).then(function (response) {
			$log.debug('Retrieve all repanels across all trainees successful');
			$log.debug(response);
			return response.data;
		}, function (error) {
			$log.error('There was an error: ' + error.status);
		});
	};

	// Update a panel
	panel.updatePanel = function (panelObj) {
		return $http({
			url : '/panel/update',
			method : 'PUT',
			data : panelObj
		}).then(function (response) {
			$log.debug('Panel updated successfully');
			$log.debug(response);
			return response.data;
		}, function (error) {
			$log.error('There was an error: ' + error.status);
		});
	};

	// Create a panel
	panel.createPanel = function (panelObj) {
		return $http({
			url : '/panel/create',
			method : 'POST',
			data : panelObj
		}).then(function (response) {
			$log.debug('Panel created successfully');
			$log.debug(response);
			return response.data;
		}, function (error) {
			$log.error('There was an error: ' + error.status);
		});
	};

	// Delete a panel by panelId
	panel.deletePanel = function (panelId) {
		return $http({
			url : '/panel/delete/' + panelId,
			method : 'DELETE'
		}).then(function (response) {
			$log.debug('Panel deleted successfully');
			$log.debug(response);
		}, function (error) {
			$log.error('There was an error: ' + error.status);
		});
	};

	// Gets all trainees with last panel status = Repanel
	panel.getRecentRepanels = function () {
		return $http({
			url : '/panel/repanel/recent',
			method : 'GET'
		}).then(function (response) {
			$log.debug('Fetching all trainees whose last panel status was Repanel');
			$log.debug(response);
			return response.data
		}, function (error) {
			$log.error('There was an error: ' + error.status);
		});
	};

	
	return panel;
});
