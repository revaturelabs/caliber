package com.revature.caliber.revpro.rest.models;

/**
 * Wraps the HTTP response by all RevPro requests
 * @author Patrick Walsh
 *
 */
public abstract class BasicHTTPResponseWrapper {

	private int statusCode;
	private String description;
	
	public BasicHTTPResponseWrapper() {
		super();
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "BasicHTTPResponseWrapperBean [statusCode=" + statusCode + ", description=" + description + "]";
	}
	
}
