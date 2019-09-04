package com.revature.salesforce.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Salesforce Data Transfer Object (current as of 7/3/2017)
 * 
 * @author Patrick Walsh
 *
 */@Deprecated
public class SalesforceTrainee extends SalesforceRecord {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("Training_Status__c")
	private String trainingStatus;

	@JsonProperty("Phone")
	private String phone;

	@JsonProperty("Email")
	private String email;

	@JsonProperty("MobilePhone")
	private String mobilePhone;

	@JsonProperty("Training_Batch__c")
	private String batchId;

	@JsonProperty("Training_Batch__r")
	@JsonSerialize(as = SalesforceBatch.class)
	private SalesforceBatch batch;

	@JsonProperty("rnm__Recruiter__r")
	@JsonSerialize(as = SalesforceRecruiter.class)
	private SalesforceRecruiter recruiter;

	@JsonProperty("Account")
	@JsonSerialize(as = SalesforceCollege.class)
	private SalesforceCollege college;

	@JsonProperty("eintern_current_project_completion_pct__c")
	private String projectCompletion;

	@JsonProperty("Screener__c")
	private String screener;
	
	@JsonProperty("Screen_Feedback__c")
	private String screenFeedback;
	
	@JsonProperty("Associates_Degree__c")
	private String associates;
	
	@JsonProperty("Bachelors_Degree__c")
	private String bachelors;
	
	@JsonProperty("Masters_Degree__c")
	private String masters;

	public SalesforceTrainee() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTrainingStatus() {
		return trainingStatus;
	}

	public void setTrainingStatus(String trainingStatus) {
		this.trainingStatus = trainingStatus;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public SalesforceBatch getBatch() {
		return batch;
	}

	public void setBatch(SalesforceBatch batch) {
		this.batch = batch;
	}

	public SalesforceRecruiter getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(SalesforceRecruiter recruiter) {
		this.recruiter = recruiter;
	}

	public SalesforceCollege getCollege() {
		return college;
	}

	public void setCollege(SalesforceCollege college) {
		this.college = college;
	}

	public String getProjectCompletion() {
		return projectCompletion;
	}

	public void setProjectCompletion(String projectCompletion) {
		this.projectCompletion = projectCompletion;
	}

	public String getScreener() {
		return screener;
	}

	public void setScreener(String screener) {
		this.screener = screener;
	}

	public String getScreenFeedback() {
		return screenFeedback;
	}

	public void setScreenFeedback(String screenFeedback) {
		this.screenFeedback = screenFeedback;
	}

	public String getAssociates() {
		return associates;
	}

	public void setAssociates(String associates) {
		this.associates = associates;
	}

	public String getBachelors() {
		return bachelors;
	}

	public void setBachelors(String bachelors) {
		this.bachelors = bachelors;
	}

	public String getMasters() {
		return masters;
	}

	public void setMasters(String masters) {
		this.masters = masters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((associates == null) ? 0 : associates.hashCode());
		result = prime * result + ((bachelors == null) ? 0 : bachelors.hashCode());
		result = prime * result + ((batch == null) ? 0 : batch.hashCode());
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result + ((college == null) ? 0 : college.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((masters == null) ? 0 : masters.hashCode());
		result = prime * result + ((mobilePhone == null) ? 0 : mobilePhone.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((projectCompletion == null) ? 0 : projectCompletion.hashCode());
		result = prime * result + ((recruiter == null) ? 0 : recruiter.hashCode());
		result = prime * result + ((screenFeedback == null) ? 0 : screenFeedback.hashCode());
		result = prime * result + ((screener == null) ? 0 : screener.hashCode());
		result = prime * result + ((trainingStatus == null) ? 0 : trainingStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalesforceTrainee other = (SalesforceTrainee) obj;
		if (associates == null) {
			if (other.associates != null)
				return false;
		} else if (!associates.equals(other.associates))
			return false;
		if (bachelors == null) {
			if (other.bachelors != null)
				return false;
		} else if (!bachelors.equals(other.bachelors))
			return false;
		if (batch == null) {
			if (other.batch != null)
				return false;
		} else if (!batch.equals(other.batch))
			return false;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (college == null) {
			if (other.college != null)
				return false;
		} else if (!college.equals(other.college))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (masters == null) {
			if (other.masters != null)
				return false;
		} else if (!masters.equals(other.masters))
			return false;
		if (mobilePhone == null) {
			if (other.mobilePhone != null)
				return false;
		} else if (!mobilePhone.equals(other.mobilePhone))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (projectCompletion == null) {
			if (other.projectCompletion != null)
				return false;
		} else if (!projectCompletion.equals(other.projectCompletion))
			return false;
		if (recruiter == null) {
			if (other.recruiter != null)
				return false;
		} else if (!recruiter.equals(other.recruiter))
			return false;
		if (screenFeedback == null) {
			if (other.screenFeedback != null)
				return false;
		} else if (!screenFeedback.equals(other.screenFeedback))
			return false;
		if (screener == null) {
			if (other.screener != null)
				return false;
		} else if (!screener.equals(other.screener))
			return false;
		if (trainingStatus == null) {
			if (other.trainingStatus != null)
				return false;
		} else if (!trainingStatus.equals(other.trainingStatus))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SalesforceTrainee [id=" + id + ", trainingStatus=" + trainingStatus + ", phone=" + phone + ", email="
				+ email + ", mobilePhone=" + mobilePhone + ", batchId=" + batchId + ", batch=" + batch + ", recruiter="
				+ recruiter + ", college=" + college + ", projectCompletion=" + projectCompletion + ", screener="
				+ screener + ", screenFeedback=" + screenFeedback + ", associates=" + associates + ", bachelors="
				+ bachelors + ", masters=" + masters + "]";
	}

}
