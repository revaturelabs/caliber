angular
		.module("app")
		.config(
				function($stateProvider, $locationProvider, $urlRouterProvider,
						ChartJsProvider, $logProvider) {

					// Turn on/off debug messages
					$logProvider.debugEnabled(false);

                    // chart options
					ChartJsProvider.setOptions({

						chartColors : [ '#803690', '#00ADF9', '#ffff66',
								'#FDB45C', '#949FB1', '#4D5360', '#66ff33',
								'#ff5050' ]

					});

					// go to home on startup
					$urlRouterProvider.otherwise('/routing');

					/***********************************************************
					 * App states
					 **********************************************************/
					$stateProvider
							.state(
									"routing",
									{
										url : "/routing",
										templateUrl : "/static/app/partials/routing.html",
										// uncomment when dev is complete
										onEnter : function(authFactory) {
											authFactory.auth();
										}
									})

							/***************************************************
							 * QC
							 **************************************************/
							.state(
									"qc",
									{
										abstract : true,
										url : "/qc",
										templateUrl : "/static/app/partials/abstracts/qc.html",
										resolve : {
											authenticate : function(authFactory) {
												authFactory.authQC();
											},
											allBatches : function(
													caliberDelegate) {
												return caliberDelegate.qc
														.getAllBatches();
											}
										},

									})
							.state(
									"qc.home",
									{ 
										url : "/home",
										/* add modal view to vpHome page */
										views: {
											"" : {
												templateUrl : "/static/app/partials/home/qc-home.html",
												controller : "qcHomeController"
											},
											"qc-quality-audit@qc.home" : {
												templateUrl : "/static/app/partials/home/view-audit-modal.html"
											},
										}
										
									})		
							.state(
									"qc.manage",
									{
										url : "/manage",
										views : {
											"" : {
												templateUrl : "/static/app/partials/manage/manage-batch.html",
												controller : "qcManageController"
											},
											"batch-form@qc.manage" : {
												templateUrl : "/static/app/partials/manage/edit-batch-modal.html"
											},
											"import-batch@qc.manage" : {
												templateUrl : "/static/app/partials/manage/import-batch-modal.html"
											},
											"batch-extra-modals@qc.manage" : {
												templateUrl : "/static/app/partials/manage/batch-axillary-modals.html"
											},
											"view-trainees@qc.manage" : {
												templateUrl : "/static/app/partials/manage/view-trainees-modal.html"
											},
											"trainee-form@qc.manage" : {
												templateUrl : "/static/app/partials/manage/edit-trainee-modal.html"
											},
											"trainee-extra-modals@qc.manage" : {
												templateUrl : "/static/app/partials/manage/trainee-axillary-modals.html"
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authManage();
										}
										
									})
							.state(
									"qc.audit",
									{
										url : "/audit",
										views : {
											"" : {
												templateUrl : "/static/app/partials/assess/qc-assess.html",
												controller : "qcAssessController"
											},
											"confirm-add-weeks-modal@qc.audit" : {
												templateUrl : "/static/app/partials/assess/confirm-add-weeks-modal.html"
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authAudit();
										}
										
										
									})
							.state(
									"qc.reports",
									{
										url : "/reports",
										views : {
											"" : {
												templateUrl : "/static/app/partials/reports.html",
												controller : "allReportController"
											},
											"trainer-display@qc.reports" : {
												templateUrl : "/static/app/partials/trainer-display.html",
												controller : "trainerAssessController"
											},
											"trainee-overall@qc.reports" : {
												templateUrl : "/static/app/partials/trainee-overall.html",
												controller : "allReportController"
											},
											"panel-report-display@qc.reports" : {
												templateUrl : "/static/app/partials/panel-report-display.html",
												controller : "allReportController"
											},
											"trainee-week@qc.reports" : {
												templateUrl : "/static/app/partials/trainee-week.html",
												controller : "trainerAssessController"
											},
											"qc-batchOverall@qc.reports" : {
												templateUrl : "/static/app/partials/qc-display.html",
												controller : "qcAssessController"
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authReports();
										}
									})
							/***************************************************
							 * TRAINER
							 **************************************************/
							.state(
									"trainer",
									{
										abstract : true,
										url : "/trainer",
										templateUrl : "/static/app/partials/abstracts/trainer.html",
										resolve : {
											authenticate : function(authFactory) {
												authFactory.authTrainer();
											},
											allBatches : function(
													caliberDelegate) {
												return caliberDelegate.trainer
														.getAllBatches();
											}
										},
									})
							.state(
									"trainer.import",
									{
										abstract : true,
										url : " /trainer/batch/all/import",
										templateUrl : "/static/app/partials/abstracts/trainer.html",
										resolve : {
											allBatches : function(
													caliberDelegate) {
												return caliberDelegate.trainer
														.importAllBatches();
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authImport();
										}
									})
							.state(
									"trainer.home",
									{
										templateUrl : "/static/app/partials/home/trainer-home.html",
										url : "/home",
										controller : "trainerHomeController",
									})
							.state(
									"trainer.manage",
									{

										url : "/manage",
										views : {
											"" : {
												templateUrl : "/static/app/partials/manage/manage-batch.html",
												controller : "trainerManageController"
											},
											"batch-form@trainer.manage" : {
												templateUrl : "/static/app/partials/manage/edit-batch-modal.html"
											},
											"import-batch@trainer.manage" : {
												templateUrl : "/static/app/partials/manage/import-batch-modal.html"
											},
											"batch-extra-modals@trainer.manage" : {
												templateUrl : "/static/app/partials/manage/batch-axillary-modals.html"
											},
											"view-trainees@trainer.manage" : {
												templateUrl : "/static/app/partials/manage/view-trainees-modal.html"
											},
											"trainee-form@trainer.manage" : {
												templateUrl : "/static/app/partials/manage/edit-trainee-modal.html"
											},
											"trainee-extra-modals@trainer.manage" : {
												templateUrl : "/static/app/partials/manage/trainee-axillary-modals.html"
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authManage();
										}
									})
							.state(
									"trainer.assess",
									{
										url : "/assess",
										views : {
											"" : {
												templateUrl : "/static/app/partials/assess/trainer-assess.html",
												
												controller : "trainerAssessController"
											},
											"trainer-edit-assess@trainer.assess" : {
												templateUrl : "/static/app/partials/assess/trainer-edit-assess.html"
											},
											"confirm-add-weeks-modal@trainer.assess" : {
												templateUrl : "/static/app/partials/assess/confirm-add-weeks-modal.html"

											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authAssess();
										}
										
									})
							.state(
									"trainer.reports",
									{
										url : "/reports",
										views : {
											"" : {
												templateUrl : "/static/app/partials/reports.html",
												controller : "allReportController"
											},
											"trainer-display@trainer.reports" : {
												templateUrl : "/static/app/partials/trainer-display.html",
												controller : "trainerAssessController"
											},
											"trainee-overall@trainer.reports" : {
												templateUrl : "/static/app/partials/trainee-overall.html",
												controller : "allReportController"
											},
											"panel-report-display@trainer.reports" : {
												templateUrl : "/static/app/partials/panel-report-display.html",
												controller : "allReportController"
											},
											"trainee-week@trainer.reports" : {
												templateUrl : "/static/app/partials/trainee-week.html",
												controller : "allReportController"
											},
											"qc-batchOverall@trainer.reports" : {
												templateUrl : "/static/app/partials/qc-display.html",
												controller : "qcAssessController"
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authReports();
										}
										
									})

							/***************************************************
							 * VP
							 **************************************************/
							.state(
									"vp",
									{
										abstract : true,
										url : "/vp",
										templateUrl : "/static/app/partials/abstracts/vp.html",
										resolve : {
											authenticate : function(authFactory) {
												authFactory.authVP();
											},
											allBatches : function(caliberDelegate) {
												return caliberDelegate.vp.getAllBatches();
											},
											allTrainers : function(caliberDelegate) {
												return caliberDelegate.all.getAllTrainers();
											}
										},

									})
							.state(
									"vp.home",
									{ 
										url : "/home",
										/* add modal view to vpHome page */
										views: {
											"" : {
												templateUrl : "/static/app/partials/home/vp-home.html",
												controller : "vpHomeController"
											},
											"qc-quality-audit@vp.home" : {
												templateUrl : "/static/app/partials/home/view-audit-modal.html"
											},
										}
										
									})
							.state(
									"vp.trainers",
									{
										url : "/trainers",
										views : {
											"" : {
												templateUrl : "/static/app/partials/trainers/manage-trainers.html",
												controller : "vpTrainerController"
											},
											"create-trainer-form@vp.trainers" : {
												templateUrl : "/static/app/partials/trainers/create-trainer-modal.html"
											},
											"edit-trainer-form@vp.trainers" : {
												templateUrl : "/static/app/partials/trainers/edit-trainer-modal.html"
											},
											"delete-trainer-form@vp.trainers" : {
												templateUrl : "/static/app/partials/trainers/delete-trainer-modal.html"
											},
											"trainer-extra-modals@vp.trainers":{
												templateUrl : "/static/app/partials/trainers/trainer-auxillary-modals.html"
											},
											"trainer-checklist-form@vp.trainers":{
												templateUrl : "/static/app/partials/trainers/trainer-checklist-modal.html"
											},
											"deactivate-task-modal@vp.trainers":{
												templateUrl : "/static/app/partials/trainers/deactivate-task-modal.html"
											}
										},
										// authorize the users
										onEnter : function(authFactory) {
											authFactory.authTrainers();
										},
										

									})
							.state(
									"vp.locations",
									{
										url : "/locations",
										views : {
											"" : {
												templateUrl : "/static/app/partials/locations/manage-locations.html",
												controller : "vpLocationController"
											},
											"create-location-form@vp.locations" : {
												templateUrl : "/static/app/partials/locations/create-location-modal.html"
											},
											"edit-location-form@vp.locations" : {
												templateUrl : "/static/app/partials/locations/edit-location-modal.html"
											},
											"delete-location-form@vp.locations" : {
												templateUrl : "/static/app/partials/locations/delete-location-modal.html"
											},
											"location-extra-modals@vp.locations":{
												templateUrl : "/static/app/partials/locations/location-auxillary-modals.html"
											},
											"add-location-form@vp.locations":{
												templateUrl : "/static/app/partials/locations/add-location-modal.html"
											}
											
										}

									})
							.state(
									"vp.category",
									{
										url : "/category",
										views:{
											"":{
												templateUrl : "/static/app/partials/category/manage-categories.html",
												controller : "vpCategoryController"
											},
											"create-category-form@vp.category" : {
												templateUrl : "/static/app/partials/category/add-category-modals.html"
											},
											"edit-category-form@vp.category" : {
												templateUrl : "/static/app/partials/category/edit-category-modals.html"
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authCategory();
										}
										
												
									})
							.state(
									"vp.manage",
									{
										url : "/manage",
										views : {
											"" : {
												templateUrl : "/static/app/partials/manage/manage-batch.html",
												controller : "trainerManageController"
											},
											"batch-form@vp.manage" : {
												templateUrl : "/static/app/partials/manage/edit-batch-modal.html"
											},
											"import-batch@vp.manage" : {
												templateUrl : "/static/app/partials/manage/import-batch-modal.html"
											},
											"batch-extra-modals@vp.manage" : {
												templateUrl : "/static/app/partials/manage/batch-axillary-modals.html"
											},
											"view-trainees@vp.manage" : {
												templateUrl : "/static/app/partials/manage/view-trainees-modal.html"
											},
											"trainee-form@vp.manage" : {
												templateUrl : "/static/app/partials/manage/edit-trainee-modal.html"
											},
											"trainee-extra-modals@vp.manage" : {
												templateUrl : "/static/app/partials/manage/trainee-axillary-modals.html"
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authManage();
										}
										
									})
							.state(
									"vp.assess",
									{
										templateUrl : "/static/app/partials/assess/trainer-assess.html",
										url : "/assess",
										controller : "trainerAssessController",
										views : {
											"" : {
												templateUrl : "/static/app/partials/assess/trainer-assess.html",
												controller : "trainerAssessController"
											},
											"trainer-edit-assess@vp.assess" : {
												templateUrl : "/static/app/partials/assess/trainer-edit-assess.html"
											},
											"confirm-add-weeks-modal@vp.assess" : {
												templateUrl : "/static/app/partials/assess/confirm-add-weeks-modal.html"

											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authAssess();
										}
										
									})
							.state(
									"vp.audit",
									{
										templateUrl : "/static/app/partials/assess/qc-assess.html",
										url : "/audit",
										controller : "qcAssessController",
										views : {
											"" : {
												templateUrl : "/static/app/partials/assess/qc-assess.html",
												controller : "qcAssessController"
											},
											"trainer-edit-assess@vp.audit" : {
												templateUrl : "/static/app/partials/assess/trainer-edit-assess.html"
											},
											"confirm-add-weeks-modal@vp.audit" : {
												templateUrl : "/static/app/partials/assess/confirm-add-weeks-modal.html"

											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authAudit();
										}
										
									})
							.state(
									"vp.reports",
									{
										url : "/reports",
										views : {
											"" : {
												templateUrl : "/static/app/partials/reports.html",
												controller : "allReportController"
											},
											"trainer-display@vp.reports" : {
												templateUrl : "/static/app/partials/trainer-display.html",
												controller : "trainerAssessController"
											},
											"trainee-overall@vp.reports" : {
												templateUrl : "/static/app/partials/trainee-overall.html",
												controller : "allReportController"
											},
											"panel-report-display@vp.reports" : {
												templateUrl : "/static/app/partials/panel-report-display.html",
												controller : "allReportController"
											},
											"trainee-week@vp.reports" : {
												templateUrl : "/static/app/partials/trainee-week.html",
												controller : "trainerAssessController"
											},
											"qc-batchOverall@vp.reports" : {
												templateUrl : "/static/app/partials/qc-display.html",
												controller : "qcAssessController"
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authReports();
										}
										
									})
							.state(
									"vp.panel",
									{
										url : "/panels",
										views : {
											"" : {
												templateUrl : "/static/app/partials/panel/panelhome.html",
												controller: "panelModalController"
												// controller :
												// "allReportController"
											},
											"panelmodal@vp.panel" : {
												templateUrl : "/static/app/partials/panel/panelmodal.html",
											}
										},
										resolve : {
											authenticate : function(authFactory) {
												authFactory.authVP();
											}
										}
										
									})
                            /**
							 * Staging role
							 * 
							 * Reusing qc's controllers because staging and qc
							 * are the same except for access to some states
							 */
                        .state("staging",
                            {
                                abstract: true,
                                url: "/staging",
                                templateUrl: "/static/app/partials/abstracts/staging.html",
                                resolve: {
                                	authenticate: function (authFactory) {
                                		authFactory.authStaging();
                                	},
                                    allBatches: function (caliberDelegate) {
                                        return caliberDelegate.qc.getAllBatches();
                                    },
                                    allTrainers: function (caliberDelegate) {
                                        return caliberDelegate.all.getAllTrainers();
                                    }
                                },
                                
                            })

                        .state( "staging.manage",
								{
									url : "/manage",
									views : {
										"" : {
											templateUrl : "/static/app/partials/manage/manage-batch.html",
											controller : "qcManageController"
										},
										"batch-form@staging.manage" : {
											templateUrl : "/static/app/partials/manage/edit-batch-modal.html"
										},
										"import-batch@staging.manage" : {
											templateUrl : "/static/app/partials/manage/import-batch-modal.html"
										},
										"batch-extra-modals@staging.manage" : {
											templateUrl : "/static/app/partials/manage/batch-axillary-modals.html"
										},
										"view-trainees@staging.manage" : {
											templateUrl : "/static/app/partials/manage/view-trainees-modal.html"
										},
										"trainee-form@staging.manage" : {
											templateUrl : "/static/app/partials/manage/edit-trainee-modal.html"
										},
										"trainee-extra-modals@staging.manage" : {
											templateUrl : "/static/app/partials/manage/trainee-axillary-modals.html"
										}
									},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authManage();
										}
										
									})
									.state("staging.home",
                            {
                                templateUrl: "/static/app/partials/home/staging-home.html",
                                url: "/home",
                                controller: "qcHomeController", // because they
																// are similar
																// roles
                            }
                        )

                        .state("staging.reports",
                            {
                                url: "/reports",
                                views: {
                                    "": {
                                        templateUrl: "/static/app/partials/reports.html",
                                        controller: "allReportController"
                                    }
                                },
                                // authorize the user
                                onEnter : function(authFactory) {
									authFactory.authReports();
								}
                            }
                        )
                        // Panel Role
                        .state(
									"panel",
									{
										abstract : true,
										url : "/panel",
										templateUrl : "/static/app/partials/abstracts/panel.html",
										resolve : {
											authenitcate : function(authFactory) {
												authFactory.authPanel();
											},
											allBatches : function(
													caliberDelegate) {
												return caliberDelegate.trainer
														.getAllBatches();
											},
											allTrainers : function(
													caliberDelegate) {
												return caliberDelegate.all
														.getAllTrainers();
											}
										},
									
							})
							.state(
									"panel.import",
									{
										abstract : true,
										url : " /panel/batch/all/import",
										templateUrl : "/static/app/partials/abstracts/panel.html",
										resolve : {
											allBatches : function(
													caliberDelegate) {
												return caliberDelegate.trainer
														.importAllBatches();
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authImport();
										}
									})
						.state(
									"panel.home",
									{ 
										url : "/home",
										/* add modal view to vpHome page */
										views: {
											"" : {
												templateUrl : "/static/app/partials/home/panel-home.html",
												controller : "panelHomeController"
											}
										}
									
										
							})
							.state(
									"panel.manage",
									{

										url : "/manage",
										views : {
											"" : {
												templateUrl : "/static/app/partials/manage/manage-batch.html",
												controller : "trainerManageController"
											},
											"batch-form@panel.manage" : {
												templateUrl : "/static/app/partials/manage/edit-batch-modal.html"
											},
											"import-batch@panel.manage" : {
												templateUrl : "/static/app/partials/manage/import-batch-modal.html"
											},
											"batch-extra-modals@panel.manage" : {
												templateUrl : "/static/app/partials/manage/batch-axillary-modals.html"
											},
											"view-trainees@panel.manage" : {
												templateUrl : "/static/app/partials/manage/view-trainees-modal.html"
											},
											"trainee-form@panel.manage" : {
												templateUrl : "/static/app/partials/manage/edit-trainee-modal.html"
											},
											"trainee-extra-modals@panel.manage" : {
												templateUrl : "/static/app/partials/manage/trainee-axillary-modals.html"
											}
										},
// // authorize the user
										onEnter : function(authFactory) {
											authFactory.authManage();
										}
									})
								.state(
									"panel.audit",
									{
										templateUrl : "/static/app/partials/assess/qc-assess.html",
										url : "/audit",
										controller : "qcAssessController",
										views : {
											"" : {
												templateUrl : "/static/app/partials/assess/qc-assess.html",
												controller : "qcAssessController"
											},
											"trainer-edit-assess@panel.audit" : {
												templateUrl : "/static/app/partials/assess/trainer-edit-assess.html"
											},
											"confirm-add-weeks-modal@panel.audit" : {
												templateUrl : "/static/app/partials/assess/confirm-add-weeks-modal.html"

											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authAudit();
										}
										
									})
						.state(
									"panel.panel",
									{ 
										url : "/panel",
										
										views: {
											"" : {
												templateUrl : "/static/app/partials/panel/panelhome.html",
												controller : "panelModalController"
											},
											"panelmodal@panel.panel" : {
												templateUrl : "/static/app/partials/panel/panelmodal.html"
											}		
										}
									
										
							})
							
						.state(
									"panel.reports",
									{ 
										url : "/reports",
										views : {
											"" : {
												templateUrl : "/static/app/partials/reports.html",
												controller : "allReportController"
											},
											"trainer-display@panel.reports" : {
												templateUrl : "/static/app/partials/trainer-display.html",
												controller : "trainerAssessController"
											},
											"trainee-overall@panel.reports" : {
												templateUrl : "/static/app/partials/trainee-overall.html",
												controller : "allReportController"
											},
											"trainee-week@panel.reports" : {
												templateUrl : "/static/app/partials/trainee-week.html",
												controller : "allReportController"
											},
											"panel-report-display@panel.reports" : {
												templateUrl : "/static/app/partials/panel-report-display.html",
												controller : "allReportController"
											},
											"qc-batchOverall@panel.reports" : {
												templateUrl : "/static/app/partials/qc-display.html",
												controller : "qcAssessController"
											}
										},
										// authorize the user
										onEnter : function(authFactory) {
											authFactory.authReports();
										}
										
							})
				});
