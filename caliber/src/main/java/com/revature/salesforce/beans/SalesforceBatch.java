package com.revature.salesforce.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Salesforce Data Transfer Object
 * (current as of 7/3/2017) 
 * @author Patrick Walsh
 *
 */@Deprecated
public class SalesforceBatch extends SalesforceRecord{

	@JsonProperty("Id")
	private String id;
	
	@JsonProperty("Batch_Start_Date__c")
	private Date batchStartDate;
	
	@JsonProperty("Batch_End_Date__c")
	private Date batchEndDate;
	
	@JsonProperty("Batch_Trainer__r")
	@JsonSerialize(as=BatchTrainer.class)
	private BatchTrainer trainer;
	
	@JsonProperty("Co_Trainer__r")
	@JsonSerialize(as=SalesforceTrainee.class)
	private SalesforceTrainee cotrainer;
	
	@JsonProperty("Skill_Type__c")
	private String skillType;
	
	@JsonProperty("Location__c")
	private String location;
	
	@JsonProperty("Type__c")
	private String type;
	
	public SalesforceBatch() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getBatchStartDate() {
		return batchStartDate;
	}

	public void setBatchStartDate(Date batchStartDate) {
		this.batchStartDate = batchStartDate;
	}

	public Date getBatchEndDate() {
		return batchEndDate;
	}

	public void setBatchEndDate(Date batchEndDate) {
		this.batchEndDate = batchEndDate;
	}

	public BatchTrainer getTrainer() {
		return trainer;
	}

	public void setTrainer(BatchTrainer trainer) {
		this.trainer = trainer;
	}

	public SalesforceTrainee getCotrainer() {
		return cotrainer;
	}

	public void setCotrainer(SalesforceTrainee cotrainer) {
		this.cotrainer = cotrainer;
	}

	public String getSkillType() {
		return skillType;
	}

	public void setSkillType(String skillType) {
		this.skillType = skillType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((batchEndDate == null) ? 0 : batchEndDate.hashCode());
		result = prime * result + ((batchStartDate == null) ? 0 : batchStartDate.hashCode());
		result = prime * result + ((cotrainer == null) ? 0 : cotrainer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((skillType == null) ? 0 : skillType.hashCode());
		result = prime * result + ((trainer == null) ? 0 : trainer.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalesforceBatch other = (SalesforceBatch) obj;
		if (batchEndDate == null) {
			if (other.batchEndDate != null)
				return false;
		} else if (!batchEndDate.equals(other.batchEndDate))
			return false;
		if (batchStartDate == null) {
			if (other.batchStartDate != null)
				return false;
		} else if (!batchStartDate.equals(other.batchStartDate))
			return false;
		if (cotrainer == null) {
			if (other.cotrainer != null)
				return false;
		} else if (!cotrainer.equals(other.cotrainer))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (skillType == null) {
			if (other.skillType != null)
				return false;
		} else if (!skillType.equals(other.skillType))
			return false;
		if (trainer == null) {
			if (other.trainer != null)
				return false;
		} else if (!trainer.equals(other.trainer))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SalesforceBatch [attributes=" + attributes + ", id=" + id + ", name=" + name + ", batchStartDate="
				+ batchStartDate + ", batchEndDate=" + batchEndDate + ", trainer=" + trainer + ", cotrainer="
				+ cotrainer + ", skillType=" + skillType + ", location=" + location + ", type=" + type + "]";
	}
	
}
