angular.module("auth").factory("authFactory",
		function($log, $http, $cookies, $state, $location, $window) {
			$log.debug("Booted Authentication Factory");

			var auth = {};

			// Roles
			var vpRole = "ROLE_VP";
			var panelRole = "ROLE_PANEL";
			var qcRole = "ROLE_QC";
			var trainerRole = "ROLE_TRAINER";
			var stagingRole = "ROLE_STAGING";

			// home states
			var vpState = "vp.home";
			var panelState = "panel.home";
			var qcState = "qc.home";
			var trainerState = "trainer.home";
			var stagingState = "staging.home";

			// home
			var vpHome = "/vp/home";
			var panelHome = "/panel/home";
			var qcHome = "/qc/home";
			var trainerHome = "/trainer/home";

			var vpManage = "vp.manage";
			var qcManage = "qc.manage";
			var stagingManage = "staging.manage";
			var trainerManage = "/trainer/home";
			var panelManage = "panel.manage";

			var vpAudit = "vp.audit";
			var qcAudit = "qc.audit";
			var panelAudit = "panel.audit";

			var vpReports = "vp.reports";
			var qcReports = "qc.reports";
			var trainerReports = "trainer.reports";
			var stagingReports = "staging.reports";
			var panelReports = "panel.reports";

			var trainerImport = "trainer.import";
			var panelImport = "panel.import";

			var vpAssess = "vp.assess";
			var trainerAssess = "trainer.assess";
			var panelAssess = "panel.assess";

			var vpCategory = "vp.category";

			var vpTrainers = "vp.trainers";
		

			/**
			 * Retrieves role from cookie
			 * 
			 * @returns A cookie that contains the role
			 */

			function getCookie() {
				$log.debug($cookies.get("role"));
				return $cookies.get("role");
			}
			
			function getIdCookie() {
				return $cookies.get("id");
			}

			//

			/**
			 * Moves user to home page when entering root
			 */
			auth.auth = function() {
				var role = getCookie();

				switch (role) {
				case trainerRole:
					$log.debug("Changing state to: " + trainerState);
					$state.go(trainerState);
					break;
				case qcRole:
					$log.debug("Changing state to: " + qcState);
					$state.go(qcState);
					break;
				case panelRole:
					$log.debug("Changing state to: " + panelState);
					$state.go(panelState);
					break;
				case vpRole:
					$log.debug("Changing state to: " + vpState);
					$state.go(vpState);
					break;
				case stagingRole:
					$log.debug("Changing state to: " + stagingState);
					$state.go(stagingState);
					break;
				default:
					$log.debug("Auth Default");
					$window.location.replace("/");
					break;
				}
			};

			/**
			 * Moves user to home page if user is not of role qc
			 */
			auth.authQC = function() {
				var role = getCookie();
				if (role === qcRole)
					$log.debug("Authenticated user as QC");
				else if (role === trainerRole)
					$location.path(trainerHome);
				else if (role === vpRole)
					$location.path(vpHome);
				else if (role === panelRole)
					$location.path(panelHome);
				else if (role === undefined){
					$log.debug("authQC redirect");
					$window.location.replace("/")
					throw Error;
				}
			};

			auth.authPanel = function () {
				var role = getCookie();
				if (role === panelRole) {
					$log.debug("Authenticated user as Panel");
					$location.path(panelHome);
				}
				else if (role === trainerRole) {
					$location.path(trainerHome);
				}
				else if (role === qcRole) {
					$location.path(qcHome);
				}
				else if (role === vpRole) {
					$location.path(vpHome);
				}
				else if (role === undefined){
					$log.debug("authPanel redirect");
					$window.location.replace("/");
					throw Error;
				}
			};

			/**
			 * moves user to home page if user is not of role vp
			 */
			auth.authVP = function() {
				var role = getCookie();
				if (role === vpRole)
					$log.debug("Authenticate user as VP");
				else if (role === trainerRole)
					$location.path(trainerHome);
				else if (role === qcRole)
					$location.path(qcHome);
				else if (role === panelRole)
					$location.path(panelHome);
				else if (role === undefined){
					$log.debug("authVP redirect")
					$window.location.replace("/");
					throw new Error();
				}
			};

			/**
			 * Moves user to home page if user is not of role trainer
			 */
			auth.authTrainer = function() {
				var role = getCookie();
				if (role === trainerRole)
					$log.debug("Authenticated user as Trainer");
				else if (role === qcRole)
					$location.path(qcHome);
				else if (role === vpRole)
					$location.path(vpHome);
				else if (role === panelRole)
					$location.path(panelHome);
				else if (role === undefined){
					$log.debug("authTrainer redirect")
					$window.location.replace("/");
					throw Error;
				}
			};

			auth.authStaging = function() {
				var role = getCookie();

				$log.debug("Authorizing staging role");

				if (role === trainerRole)
					$location.path(trainerHome);
				else if (role === qcRole)
					$location.path(qcHome);
				else if (role === vpRole)
					$location.path(vpHome);
				else if (role === panelRole)
					$location.path(panelHome);
				else if (role === undefined){
					$log.debug("authStaging redirect")
					$window.location.replace("/");
					throw Error;
				}
			};

			/*
			 * if user has one of the specified roles, continue on. Otherwise,
			 * take the user to his/her homepage.
			 */
			auth.authManage = function() {
				var role = getCookie();

				switch (role) {
				case vpRole:
					$log.debug("Changing state to: " + vpManage);
					break;
				case qcRole:
					$log.debug("Changing state to: " + qcManage);
					break;
				case stagingRole:
					$log.debug("Changing state to: " + stagingManage);
					break;
				case trainerRole:
					$log.debug("Changing state to: " + trainerManage);
					break;
				case panelRole:
					$log.debug("Changing state to: " + panelManage);
					break;	
				default:
					auth.auth();
				}
			};

			/*
			 * if user has one of the specified roles, continue on. Otherwise,
			 * take the user to his/her homepage.
			 */
			auth.authAudit = function() {
				var role = getCookie();

				switch (role) {
				case vpRole:
					$log.debug("Changing state to: " + vpAudit);
					break;
				case qcRole:
					$log.debug("Changing state to: " + qcAudit);
					break;
				case panelRole:
					$log.debug("Changing state to: " + panelAudit);
					break;
				default:
					auth.auth();
				}
			};

			/*
			 * if user has one of the specified roles, continue on. Otherwise,
			 * take the user to his/her homepage.
			 */
			auth.authReports = function() {
				var role = getCookie();

				switch (role) {
				case vpRole:
					$log.debug("Changing state to: " + vpReports);
					break;
				case qcRole:
					$log.debug("Changing state to: " + qcReports);
					break;
				case trainerRole:
					$log.debug("Changing state to: " + trainerReports);
					break;
				case stagingRole:
					$log.debug("Changing state to: " + stagingReports);
					break;
				case panelRole:
					$log.debug("Changing state to: " + panelReports);
					break;
				default:
					auth.auth();
				}
			};

			/*
			 * if user has one of the specified roles, continue on. Otherwise,
			 * take the user to his/her homepage.
			 */
			auth.authImport = function() {
				var role = getCookie();

				switch (role) {
				case trainerRole:
					$log.debug("Changing state to: " + trainerImport);
					break;
				case panelRole:
					$log.debug("Changing state to: " + panelImport);
					break;
				default:
					auth.auth();
				}
			};

			/*
			 * if user has one of the specified roles, continue on. Otherwise,
			 * take the user to his/her homepage.
			 */
			auth.authAssess = function() {
				var role = getCookie();

				switch (role) {
				case vpRole:
					$log.debug("Changing state to: " + vpAssess);
					break;
				case trainerRole:
					$log.debug("Changing state to: " + trainerAssess);
					break;
				case panelRole:
					$log.debug("Changing state to: " + panelAssess);
					break;
				default:
					auth.auth();
				}
			};

			/*
			 * if user has one of the specified roles, continue on. Otherwise,
			 * take the user to his/her homepage.
			 */
			auth.authCategory = function() {

				var role = getCookie();
				if(role === vpRole){
					$log.debug("Changing state to: " + vpCategory);
				} else {
					auth.auth();
				}
			};

			/*
			 * if user has one of the specified roles, continue on. Otherwise,
			 * take the user to his/her homepage.
			 */
			auth.authTrainers = function() {
				var role = getCookie();

				switch (role) {
				case vpRole:
					$log.debug("Changing state to: " + vpTrainers);
					break;
				default:
					auth.auth();
				}
			};

			return auth;
		});
