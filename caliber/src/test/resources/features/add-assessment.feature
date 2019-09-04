# feature
@add-assessment
Feature: Add Assessment
	As a trainer or VP,
	I can add a new assessment for the batch,
	such as LMS exams, mock interviews, and projects,
	so I can define what the batch is being evaluated/graded on Monday.
	
# positive testing
Scenario: Add LMS exam
	Given I am on the Assess Batch page
	And I have selected a batch
	And I have clicked the Week 1 tab
	And I have clicked Create Assessment button
	And I have selected "Java" as the Category
	And I have entered 100 as the Max Points
	And I have selected "Exam" as the Assessment Type
	When I click the Save button
	Then the Java Exam appears on the screen
	
# negative testing, etc..