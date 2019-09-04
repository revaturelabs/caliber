package com.revature.caliber.test.uat;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AddAssessmentFeature {
	@Given("^I am on the Assess Batch page$")
	public void iAmOnTheAssessBatchPage() {
		// Write code here that turns the phrase above into concrete actions
	}

	@Given("^I have selected a batch$")
	public void iHaveSelectedABatch() {
		// Write code here that turns the phrase above into concrete actions
	}

	@Given("^I have clicked the Week (\\d+) tab$")
	public void iHaveClickedTheWeekTab(int week) {
		// Write code here that turns the phrase above into concrete actions
	}

	@Given("^I have clicked Create Assessment button$")
	public void iHaveClickedCreateAssessmentButton() {
		// Write code here that turns the phrase above into concrete actions
	}

	@Given("^I have selected \"([^\"]*)\" as the Category$")
	public void iHaveSelectedAsTheCategory(String category) {
		// Write code here that turns the phrase above into concrete actions
	}

	@Given("^I have entered (\\d+) as the Max Points$")
	public void iHaveEnteredAsTheMaxPoints(int points) {
		// Write code here that turns the phrase above into concrete actions
	}

	@Given("^I have selected \"([^\"]*)\" as the Assessment Type$")
	public void iHaveSelectedAsTheAssessmentType(String type) {
		// Write code here that turns the phrase above into concrete actions
	}

	@When("^I click the Save button$")
	public void iClickTheSaveButton() {
		// Write code here that turns the phrase above into concrete actions
	}

	@Then("^the Java Exam appears on the screen$")
	public void theJavaExamAppearsOnTheScreen() {
		// Write code here that turns the phrase above into concrete actions
	}
}
