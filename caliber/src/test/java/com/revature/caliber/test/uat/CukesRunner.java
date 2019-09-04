package com.revature.caliber.test.uat;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Hook class to kick off the E2E tests using Cucumber/Selenium.
 * This test must be ignored in the initial build until the
 * app is deployed into the test environment.
 * 
 * All Cucumber features must end in the word Feature.
 * 
 * @author Patrick Walsh
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(features={"src/test/resources/features"})
public class CukesRunner {}
