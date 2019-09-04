package com.revature.caliber.revpro.beans;

/**
 * Trainers from RevPro
 * 
 * @author Patrick Walsh
 *
 */
public class RevProTrainer {

	private String firstName;
	private String lastName;
	private String email;
	
	public RevProTrainer() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "RevProTrainer [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

}
