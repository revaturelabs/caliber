package com.revature.caliber.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum InterviewFormat implements Serializable{

	@JsonProperty("Hangouts")
	Hangouts,
	@JsonProperty("Skype")
	Skype,
	@JsonProperty("Phone")
	Phone,
	@JsonProperty("On-Site")
	On_Site
	
}
