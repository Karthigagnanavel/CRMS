package com.app.crms.bean;

public class EvidenceDetails {

	private int evidenceId;
	private int crimeId;
	private String evidenceType;
	private String description;
	private String location;
	public int getEvidenceId() {
		return evidenceId;
	}
	public void setEvidenceId(int evidenceId) {
		this.evidenceId = evidenceId;
	}
	public int getCrimeId() {
		return crimeId;
	}
	public void setCrimeId(int crimeId) {
		this.crimeId = crimeId;
	}
	public String getEvidenceType() {
		return evidenceType;
	}
	public void setEvidenceType(String evidenceType) {
		this.evidenceType = evidenceType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
