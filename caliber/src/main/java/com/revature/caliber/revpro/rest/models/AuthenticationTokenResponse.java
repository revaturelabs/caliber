package com.revature.caliber.revpro.rest.models;

public class AuthenticationTokenResponse extends BasicHTTPResponseWrapper {

	private String data;

	public AuthenticationTokenResponse() {
		super();
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
