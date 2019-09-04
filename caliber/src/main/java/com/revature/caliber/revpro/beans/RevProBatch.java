package com.revature.caliber.revpro.beans;

import java.util.List;

/**
 * Batches from RevPro
 * 
 * @author Patrick Walsh
 *
 */
public class RevProBatch {

	/**
	 * "a190P000009zjakQAA"
	 */
	private String salesforceId;
	
	/**
	 * "TRNG-00000437"
	 */
	private String name;
	
	/**
	 * "2018-07-09 00:00:00 +0000"
	 */
	private String startDate;
	
	/**
	 * "2018-09-14 00:00:00 +0000"
	 */
	private String endDate;
	
	/**
	 * "Full Stack JTA"
	 */
	private String skill;
	
	/**
	 * "TL-0014"
	 */
	private String location;
	
	/**
	 * "Regular"
	 */
	private String type;
	
	private RevProTrainer trainer;
	
	private List<RevProTrainer> coTrainers;
	
	private List<RevProTrainee> batchTrainees;

	public RevProBatch() {
		super();
	}

	public String getSalesforceId() {
		return salesforceId;
	}

	public void setSalesforceId(String salesforceId) {
		this.salesforceId = salesforceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public RevProTrainer getTrainer() {
		return trainer;
	}

	public void setTrainer(RevProTrainer trainer) {
		this.trainer = trainer;
	}

	public List<RevProTrainer> getCoTrainers() {
		return coTrainers;
	}

	public void setCoTrainers(List<RevProTrainer> coTrainers) {
		this.coTrainers = coTrainers;
	}


	public List<RevProTrainee> getBatchTrainees() {
		return batchTrainees;
	}

	public void setBatchTrainees(List<RevProTrainee> batchTrainees) {
		this.batchTrainees = batchTrainees;
	}

	@Override
	public String toString() {
		return "RevProBatch [salesforceId=" + salesforceId + ", name=" + name + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", skill=" + skill + ", location=" + location + ", type=" + type
				+ ", trainer=" + trainer + ", coTrainers=" + coTrainers + "]";
	}
	
}
