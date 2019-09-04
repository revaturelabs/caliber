package com.revature.salesforce.beans;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@Deprecated
public class SalesforceTraineeResponse extends SalesforceResponse{

	@JsonProperty("records")
	@JsonSerialize(as=SalesforceTrainee[].class)
	private SalesforceTrainee[] records;
	
	public SalesforceTraineeResponse() {
		super();
	}

	public SalesforceTrainee[] getRecords() {
		return records;
	}

	public void setRecords(SalesforceTrainee[] records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "SalesforceTraineeResponse [records=" + Arrays.toString(records) + "]";
	}

}
