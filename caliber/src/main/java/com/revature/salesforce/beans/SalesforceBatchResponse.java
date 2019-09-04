package com.revature.salesforce.beans;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@Deprecated
public class SalesforceBatchResponse extends SalesforceResponse{

	@JsonProperty("records")
	@JsonSerialize(as=SalesforceBatch[].class)
	private SalesforceBatch[] records;
	
	public SalesforceBatchResponse() {
		super();
	}

	public SalesforceBatch[] getRecords() {
		return records;
	}

	public void setRecords(SalesforceBatch[] records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "SalesforceBatchResponse [records=" + Arrays.toString(records) + "]";
	}
	
}
