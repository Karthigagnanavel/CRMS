package com.app.crms.bean;

import java.util.Date;

public class CrimeDetails {

	private int crimeId;
	private int criminalId;
	private String crimeType;
	private String crimeDescription;
	private String crimeDate;
	private String place;
	private String status;
	public int getCrimeId() {
		return crimeId;
	}
	public void setCrimeId(int crimeId) {
		this.crimeId = crimeId;
	}
	public int getCriminalId() {
		return criminalId;
	}
	public void setCriminalId(int criminalId) {
		this.criminalId = criminalId;
	}
	public String getCrimeType() {
		return crimeType;
	}
	public void setCrimeType(String crimeType) {
		this.crimeType = crimeType;
	}
	public String getCrimeDescription() {
		return crimeDescription;
	}
	public void setCrimeDescription(String crimeDescription) {
		this.crimeDescription = crimeDescription;
	}
	
	public String getCrimeDate() {
		return crimeDate;
	}
	public void setCrimeDate(String crimeDate) {
		this.crimeDate = crimeDate;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "CrimeDetails [crimeId=" + crimeId + ", criminalId=" + criminalId + ", crimeType=" + crimeType
				+ ", crimeDescription=" + crimeDescription + ", crimeDate=" + crimeDate + ", place=" + place
				+ ", status=" + status + "]";
	}

	
}
