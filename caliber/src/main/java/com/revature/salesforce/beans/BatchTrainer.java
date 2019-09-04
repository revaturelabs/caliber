package com.revature.salesforce.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Salesforce Data Transfer Object
 * (current as of 7/3/2017) 
 * @author Patrick Walsh
 *
 */@Deprecated
public class BatchTrainer extends SalesforceRecord{
	
	@JsonProperty("Email")
	private String email;
	
	public BatchTrainer() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if (!super.equals(obj))
			return false;
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		BatchTrainer other = (BatchTrainer) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "BatchTrainer [attributes=" + attributes + ", name=" + name + ", email=" + email + "]";
	}

}
