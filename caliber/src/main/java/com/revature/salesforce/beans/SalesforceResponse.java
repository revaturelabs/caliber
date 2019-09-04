package com.revature.salesforce.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Salesforce Data Transfer Object
 * (current as of 7/3/2017) 
 * <br/>
 *	Salesforce returns all responses in an object containing: <br/>
 *		totalSize: number of records returned  <br/>
 *		done: if request is completely processed <br/>
 *		records: the results of the SOQL query  <br/><br/>
 *	Inherit to create a sub-type that deserializes the specific 'records' 
 * @author Patrick Walsh
 *
 */@Deprecated
public abstract class SalesforceResponse {

	@JsonProperty("totalSize")
	private int totalSize;
	
	@JsonProperty("done")
	private boolean done;
	
	public SalesforceResponse() {
		super();
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (done ? 1231 : 1237);
		result = prime * result + totalSize;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalesforceResponse other = (SalesforceResponse) obj;
		if (done != other.done)
			return false;
		if (totalSize != other.totalSize)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SalesforceResponse [totalSize=" + totalSize + ", done=" + done + "]";
	}
	
}
