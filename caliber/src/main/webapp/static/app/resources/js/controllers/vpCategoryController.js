/*******************************************************************************
 * Team: HKM Team Lead: Humberto Corea Authors: Humberto Corea, s Rex Toothman,
 * Carlos Vallejo
 * 
 * Humberto worked on adding functionality to deactivate an assessment no longer
 * being used in. Reavture Rex worked on adding functionality that allows a vp
 * to add a new assessment. Carlos worked on adding functionality to edit and
 * assessment. Humberto and Carlos are working concurrently in the edit
 * assessments to include the functionality to be able to toggle an assessment
 * from active to inactive.
 * ******************************************************************************
 */

angular
		.module("vp")
		.controller(
				"vpCategoryController",
				function($scope, $log, caliberDelegate) {
					// Needed to keep track of what index in categories is used
					// during Edit
					var editIndex;
					// Get all categories on load up
					caliberDelegate.vp.getAllCategories().then(
							function(categories) {
								$log.debug(categories);
								$scope.categories = categories;
							});
					// Helper function used to load newly created category to
					// categories array
					var loadAllCategories = function() {
						caliberDelegate.vp.getAllCategories().then(
								function(categories) {

									$scope.categories = categories;
								});
					};
					// Update Category
					$scope.updateCategory = function() {
						caliberDelegate.vp
								.updateCategory($scope.thisCategoryForm)
								.then(
										function(response) {
											$log.debug("Category Updated: "
													+ response);
											$scope.categories[editIndex] = $scope.thisCategoryForm;
										});
					};
					// Save Category
					$scope.saveCategory = function(categoryForm) {
						var newCategory = categoryForm;
						createCategoryObject(newCategory);
						caliberDelegate.vp.saveCategory(newCategory)
								.then(
										function(response) {
											loadAllCategories();
											$log.debug("Category Created: "
													+ response);
										});
					};
					// Set variable thisCategoryForm used in
					// edit-category-modals
					$scope.populateCategory = function(index) {
						$log.debug($scope.categories[index]);
						$scope.thisCategoryForm = angular
								.copy($scope.categories[index]);
						editIndex = index;

					}
					// Create category variable used in create category
					function createCategoryObject(category) {
						category = $scope.categoryForm;
						$log.debug(category);
					}
					;
					// Constructor used for saving category
					$scope.categoryForm = {
						skillCategory : null,
						active : true
					};
				});
