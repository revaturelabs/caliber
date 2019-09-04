/** modules * */
angular.module(
		"app",
		[ "ui.router", "ngCookies", "auth", "chart.js", "vp", "qc", "trainer", "panel",
				"api", "delegate", "charts", "reportApi", "ngAnimate",
				"moment-picker", "angular.filter" ]).run(function($rootScope, $timeout) {
	$rootScope.$on('test', function() {
		$timeout(function() {
			$rootScope.$broadcast('trainerasses');
			$rootScope.$broadcast('qcBatchOverall')
		}, 100);
	});
})
// add modules here
angular.module("vp", []);
angular.module("qc", []);
angular.module("trainer", []);
angular.module("delegate", []);
angular.module("api", []);
angular.module("charts", []);
angular.module("auth", []);
angular.module("reportApi", []);
angular.module("panel", []);

angular.module("app").directive("sticky", function($window) {
	return function(scope, element) {
		var elOriginalPos = element[0].getClientRects()[0].top;
		var elYPos = element[0].getClientRects()[0].top + 100;
		angular.element($window).bind("scroll", function() {
			elYPos;
			if (this.pageYOffset > elYPos) {
				element.addClass("fixed-style-table-header");
			} else if (this.pageYOffset <= elOriginalPos) {
				element.removeClass("fixed-style-table-header");
			}
			scope.$apply();
		});
	}
});
