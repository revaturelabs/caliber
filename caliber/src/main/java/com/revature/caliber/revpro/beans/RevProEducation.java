package com.revature.caliber.revpro.beans;

/**
 * Degree info from RevPro
 * 
 * @author Patrick Walsh
 *
 */
public class RevProEducation {

	private String degree;
	private String major;
	private String universityName;

	public RevProEducation() {
		super();
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	@Override
	public String toString() {
		return "RevProEducation [degree=" + degree + ", major=" + major + ", universityName=" + universityName + "]";
	}
}
