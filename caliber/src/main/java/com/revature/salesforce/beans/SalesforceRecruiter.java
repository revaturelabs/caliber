package com.revature.salesforce.beans;

/**
 * Salesforce Data Transfer Object
 * (current as of 7/3/2017) 
 * @author Patrick Walsh
 *
 */@Deprecated
public class SalesforceRecruiter extends SalesforceRecord{
	
	public SalesforceRecruiter() {
		super();
	}

	@Override
	public String toString() {
		return "SalesforceRecruiter [attributes=" + attributes + ", name=" + name + "]";
	}
	
}
