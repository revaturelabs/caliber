package com.revature.caliber.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SkillType implements Serializable{
	@JsonProperty("Full Stack Java/JEE")
	J2EE("Full Stack Java/JEE"),
	@JsonProperty("Full Stack .NET")
	NET("Full Stack .NET"),
	@JsonProperty("SDET")
	SDET("SDET"),
	@JsonProperty("BPM")
	BPM("BPM"),
	@JsonProperty("Appian BPM")
	APPIAN("Appian BPM"),
	@JsonProperty("PEGA BPM")
	PEGA("PEGA BPM"),
	@JsonProperty("Dynamics CRM")
	DYNAMICS("Dynamics CRM"),
	@JsonProperty("Full Stack JTA")
	JTA("Full Stack JTA"),
	@JsonProperty("Microservices")
	MICROSERVICES("Microservices"),
	@JsonProperty("Oracle Fusion Middleware")
	FUSION("Oracle Fusion"),
	@JsonProperty("Salesforce")
	SALESFORCE("Salesforce"),
	@JsonProperty("ServiceNow")
	SERVICENOW("ServiceNow"),
	@JsonProperty("Business Analyst")
	BA("Business Analyst"),
	@JsonProperty("System Admin")
	SYSADMIN("System Admin"),
	@JsonProperty("QA")
	QA("QA"),
	@JsonProperty("Database Administrator")
	DBA("Database Administrator"),
	@JsonProperty("Cloud Admin")
	CLOUD("Cloud Admin"),
	@JsonProperty("Cloud Native")
	NATIVE("Cloud Native"),
	@JsonProperty("Spark")
	SPARK("Spark"),
	@JsonProperty("Big Data/Hadoop")
	BIGDATA("Big Data/Hadoop"),
	@JsonProperty("Other")
	OTHER("Other");
	
	private String type;

	private SkillType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}
	
	public static SkillType fromString(String text) {
	    for (SkillType skill : SkillType.values()) {
	      if (skill.type.equalsIgnoreCase(text)) {
	        return skill;
	      }
	    }
	    return OTHER;
	  }

}
