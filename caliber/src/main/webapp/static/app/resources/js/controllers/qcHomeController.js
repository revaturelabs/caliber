
angular
		.module("qc")
		.controller(
				"qcHomeController",
				function($log, $scope, $filter, chartsDelegate,
						caliberDelegate, qcFactory, allBatches) {

                    $log.debug("Booted qc home controller.");
                    $scope.averageScoreData = [];
                    $scope.auditData = [];
                    $scope.selectedStateFromLineChar = "";
                    $scope.selectedStateFromBarChar = "";
                    /*
                     * *Moved over code from qcAssessController for modal
                     * use
                     */
                    $scope.batches = allBatches;
                    $scope.bnote = null;
                    $scope.faces = [];
                    $scope.weeks = [];
                    $scope.batchesByYear = [];
                    $scope.categories = [];

                    $scope.qcOverallNotes = [];

                    // call function to get batch overall feedback
                    for (var b in $scope.batches){
                        if ($scope.batches.hasOwnProperty(b) )
                            getOverallBatchFeedback($scope.batches[b].batchId, $scope.batches[b].weeks);
                    }
                    (function() {
                        // Finishes any left over ajax animation from another
                        // page
                        NProgress.done();
                        createDefaultCharts();


                    })();

                    // function to get overall feedback for all batches for current week
                    function getOverallBatchFeedback(batchId, currentWeek) {
                        caliberDelegate.qc
                            .batchNote(batchId, currentWeek)
                            .then(function (notes) {
                                $scope.qcOverallNotes.push(notes.qcStatus);
                            })
                    }

                    // function to grab latest qc information from click
                    // event
                    $scope.onClick = function(points) {
                        if (points[0]) {

                            var j = 0;
                            // define var that grabs batch id from scope
                            var batchId = $scope.stackedBarIds[points[0]._index];
                            // set current batch to j
                            $scope.currentBatch = $scope.batches[j];

                            // starting scope vars
                            $log.debug($scope.currentBatch);

                            // While loop to check if current batch id
                            // matches defined
                            // variable
                            while (batchId !== $scope.currentBatch.batchId) {
                                j += 1;
                                $scope.currentBatch = $scope.batches[j];
                                if (batchId === $scope.currentBatch.batchId) {
                                    $scope.currentBatch = $scope.batches[j];
                                    break;
                                }
                            }

                            // create an array of numbers for number of
                            // weeks in the
                            // batch selected
                            if ($scope.currentBatch) {
                                for (var i = 1; i <= $scope.currentBatch.weeks; i++) {
                                    $scope.weeks.push(i);
                                }
                            }

                            start();
                            getNotes();
                            categories();
                            wipeFaces();

                            // opens modal view
                            $('#viewLastAudit').modal('toggle');
                        }
                    };

                    // default -- view assessments table
                    $scope.currentView = true;

                    // function to get notes
                    function getNotes() {
                        // Check if there are no weeks
                        if ($scope.displayWeek !== undefined
                            && $scope.currentBatch !== undefined
                            && $scope.currentBatch !== null) {
                            // Get qc batch notes for selected batch
                            caliberDelegate.qc
                                .batchNote($scope.currentBatch.batchId,
                                    $scope.displayWeek)
                                .then(
                                    function(notes) {
                                        // If no batch note found
                                        // create
                                        // empty note object to be
                                        // used
                                        if (notes === "") {
                                            $log.debug("EMPTY!");
                                            $scope.bnote = new Note(
                                                null,
                                                null,
                                                null,
                                                $scope.currentWeek,
                                                $scope.currentBatch,
                                                null,
                                                "ROLE_QC",
                                                "QC_BATCH",
                                                true);
                                        }
                                        // If note found set the
                                        // note
                                        // object to note content
                                        // and
                                        // face
                                        else {
                                            $scope.bnote = notes;
                                            $scope.qcBatchAssess = notes.qcStatus;
                                        }
                                    });
                            // Get qc notes for trainees in selected batch
                            // for
                            // the week
                            caliberDelegate.qc
                                .traineeNote(
                                    $scope.currentBatch.batchId,
                                    $scope.displayWeek)
                                .then(
                                    function(notes) {
                                        $log.debug(notes);
                                        // Iterate through trainees
                                        for (var i = 0; i < $scope.currentBatch.trainees.length; i++) {
                                            var content = null;
                                            var status = null;
                                            var id = null;
                                            // Set note content,
                                            // status
                                            // and
                                            // id to note in
                                            // database if
                                            // found
                                            for (var j = 0; j < notes.length; j++) {
                                                if ($scope.currentBatch.trainees[i].name === notes[j].trainee.name) {
                                                    content = notes[j].content;
                                                    status = notes[j].qcStatus;
                                                    id = notes[j].noteId;
                                                    break;
                                                }
                                            }
                                            // Push note object into
                                            // array, batch set to
                                            // null
                                            // for trainee note
                                            // always
                                            $scope.faces
                                                .push(new Note(
                                                    id,
                                                    content,
                                                    status,
                                                    $scope.displayWeek,
                                                    null,
                                                    $scope.currentBatch.trainees[i],
                                                    "ROLE_QC",
                                                    "QC_TRAINEE",
                                                    true));
                                        }
                                    });
                            // If there are no weeks
                        } else if ($scope.currentBatch !== undefined
                            && $scope.currentBatch !== null) {
                            $scope.bnote = null;
                            for (var i = 0; i < $scope.currentBatch.trainees.length; i++) {
                                $scope.faces.push(new Note(null, null,
                                    null, $scope.currentWeek,
                                    $scope.currentBatch,
                                    $scope.currentBatch.trainees[i],
                                    "ROLE_QC", "QC_TRAINEE", true));
                            }
                        }
                    }


                    // Note object
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

                    // Used to sort trainees in batch
                    function compare(a, b) {
                        if (a.name < b.name)
                            return -1;
                        if (a.name > b.name)
                            return 1;
                        return 0;
                    }

                    function createDefaultCharts(){
                        NProgress.start();
                        getCurrentBatchesAuditData();
                        getCurrentBatchesAvergeScoreData();
                    }


                    // Start function for reports to use and assess
                    function start() {
                        if ($scope.batches[0]) {
                            $scope.trainingNameDate = $scope.batches[0].trainer.name
                                + " - "
                                + $filter('date')(
                                    $scope.batches[0].startDate,
                                    'shortDate');
                        }

                        // Sort trainees alphabetically
                        if ($scope.currentBatch) {
                            $scope.currentBatch.trainees.sort(compare);
                        }

                        //set display week for modal header
                        $scope.displayWeek = $scope.displayWeeksMap[$scope.currentBatch.batchId];
                                                
                        // Set current week to first week
                        // If reports week is selected
                        if ($scope.reportCurrentWeek !== undefined
                            && $scope.reportCurrentWeek !== "(All)") {
                            $log.debug("Got report week");
                            // Set current week to week selected in report
                            $scope.currentWeek = $scope.reportCurrentWeek;
                        } else {
                            $log.debug("No report week");
                            // Set week to first week in batch
                            $scope.currentWeek = $scope.weeks[$scope.weeks.length - 1];
                        }

                        // get status types
                        $scope.qcStatusTypes = [];
                        caliberDelegate.all.enumQCStatus().then(
                            function(types) {
                                $log.debug(types);
                                $scope.qcStatusTypes = types;
                            });

                        // load note types
                        caliberDelegate.all.enumNoteType().then(
                            function(noteTypes) {
                                $log.debug(noteTypes);
                                // do something with note type
                            });
                    }

                    // Get categories for the week
                    function categories() {
                        if ($scope.currentBatch) {
                            caliberDelegate.qc.getAllAssessmentCategories(
                                $scope.currentBatch.batchId,
                                $scope.currentWeek).then(
                                function(response) {
                                    $scope.categories = response;
                                });
                        }
                    }

                    // wipe faces and selections
                    function wipeFaces() {
                        $scope.faces = [];
                        $scope.qcBatchAssess = null;
                        $scope.finalQCBatchNote = null;
                    }

                    // restructured graph functions

                    function createAllBatchesCurrentWeekQCStats(data) {
                        var barChartObj = chartsDelegate.bar
                            .getAllBatchesCurrentWeekQCStats(data);
                        $scope.stackedBarData = barChartObj.data;
                        $scope.stackedBarLabels = barChartObj.labels;
                        $scope.stackedBarSeries = barChartObj.series;
                        $scope.stackedBarOptions = barChartObj.options;
                        $scope.stackedBarColors = barChartObj.colors;
                        $scope.stackedBarIds = barChartObj.id; // define scope for batch ids
                        $scope.qcOverall = barChartObj.qcOverall;
						$scope.displayWeeksMap = barChartObj.displayWeeksMap;
                    }

                    function createCurrentBatchesAverageScoreChart(data) {
                        var lineChartObj = chartsDelegate.line
                            .getCurrentBatchesAverageScoreChart(data);
                        $scope.currentBatchesLineData = lineChartObj.data;
                        $scope.currentBatchesLineLabels = lineChartObj.labels;
                        $scope.currentBatchesLineSeries = lineChartObj.series;
                        $scope.currentBatchesLineOptions = lineChartObj.options;
                        $scope.currentBatchesLineColors = lineChartObj.colors;
                        $scope.currentBatchesDsOverride = lineChartObj.datasetOverride;
                    }

                    function getCurrentBatchesAvergeScoreData() {
                        chartsDelegate.line.data
                            .getCurrentBatchesAverageScoreChartData()
                            .then(
                                function(data) {
                                    $scope.averageScoreData = data;
                                    createCurrentBatchesAverageScoreChart(data);
                                    NProgress.done();
                                }, function() {
                                    NProgress.done();
                                });
                    }

                    function getCurrentBatchesAuditData() {
                        chartsDelegate.bar.data
                            .getAllBatchesCurrentWeekQCStatsData()
                            .then(function(data) {
                                NProgress.done();
                                $scope.auditData = data;
                                createAllBatchesCurrentWeekQCStats(data);
                            }, function() {
                                NProgress.done();
                            });
                    }

                    var filterLineChartByCity = function(city){
                        if(city){
                            var filteredData = $scope.averageScoreData.filter(function(batch){
                                if(batch.address){
                                    return batch.address.city===city;
                                }
                            });
                            createCurrentBatchesAverageScoreChart(filteredData);
                        } else {
                            filterLineChartByState($scope.selectedStateFromLineChar);
                        }
                    };

                    var filterLineChartByState = function(state){
                        if(state){
                            var filteredData = $scope.averageScoreData.filter(function(batch){
                                if(batch.address)
                                    return batch.address.state===state;
                            });
                            createCurrentBatchesAverageScoreChart(filteredData);
                        } else {
                            createCurrentBatchesAverageScoreChart($scope.averageScoreData);
                        }
                    };

                    var filterBarChartByState = function(state){
                        if(state){
                            var filteredData = $scope.auditData.filter(function(batch){
                                if(batch.address)
                                    return batch.address.state===state;
                            });
                            createAllBatchesCurrentWeekQCStats(filteredData);
                        } else {
                            createAllBatchesCurrentWeekQCStats($scope.auditData);
                        }
                    };

                    var filterBarChartByCity = function (city) {
                        if (city) {
                            var filteredData = $scope.auditData.filter(function (batch) {
                                if (batch.address) {
                                    return batch.address.city === city;
                                }
                            });
                            createAllBatchesCurrentWeekQCStats(filteredData);
                        } else {
                            filterBarChartByState($scope.selectedStateFromBarChar);
                        }
                    };

                    $scope.onLineCharAddressStateChange = function(state){
                        if(state!=="undefined"){
                            $scope.selectedStateFromLineChar = state;
                            filterLineChartByState(state);
                        }
                    };

                    $scope.onLineCharAddressCityChange = function(city) {
                        filterLineChartByCity(city);
                    };

                    $scope.onBarCharAddressStateChange = function(state){
                        if(state!=="undefined"){
                            $scope.selectedStateFromBarChar = state;
                            filterBarChartByState(state);
                        }
                    };

                    $scope.onBarCharAddressCityChange = function(city) {
                        filterBarChartByCity(city);
                    }
			
				});
