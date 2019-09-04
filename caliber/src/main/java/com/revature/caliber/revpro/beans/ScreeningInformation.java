package com.revature.caliber.revpro.beans;

/**
 * Screening feedback from RevPro
 * @author Patrick Walsh
 *
 */
public class ScreeningInformation {
	
	private String screenerName;
	private String screenerFeedback; 
	private double screeningScore;
	
	public ScreeningInformation() {
		super();
	}
	public String getScreenerName() {
		return screenerName;
	}
	public void setScreenerName(String screenerName) {
		this.screenerName = screenerName;
	}
	public String getScreenerFeedback() {
		return screenerFeedback;
	}
	public void setScreenerFeedback(String screenerFeedback) {
		this.screenerFeedback = screenerFeedback;
	}
	public double getScreeningScore() {
		return screeningScore;
	}
	public void setScreeningScore(double screeningScore) {
		this.screeningScore = screeningScore;
	}
	@Override
	public String toString() {
		return "ScreenerInformation [screenerName=" + screenerName + ", screenerFeedback=" + screenerFeedback
				+ ", screeningScore=" + screeningScore + "]";
	}

	
}
