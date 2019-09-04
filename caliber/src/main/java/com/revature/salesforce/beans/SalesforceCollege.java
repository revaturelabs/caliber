package com.revature.salesforce.beans;

/**
 * Salesforce Data Transfer Object
 * (current as of 7/3/2017) 
 * @author Patrick Walsh
 *
 */
@Deprecated
public class SalesforceCollege extends SalesforceRecord{

	public SalesforceCollege() {
		super();
	}

	@Override
	public String toString() {
		return "SalesforceCollege [attributes=" + attributes + ", name=" + name + "]";
	}
	
}
