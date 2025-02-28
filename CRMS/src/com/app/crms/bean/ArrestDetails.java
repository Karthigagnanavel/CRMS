package com.app.crms.bean;

import java.util.Date;

public class ArrestDetails {

	private int arrestId;
	private int criminalId;
	private int crimeId;
	private String arrestDate;
	private String arrestOfficerName;
	private String policeStationName;
	private String status;
	
	public int getArrestId() {
		return arrestId;
	}
	public void setArrestId(int arrestId) {
		this.arrestId = arrestId;
	}
	public int getCriminalId() {
		return criminalId;
	}
	public void setCriminalId(int criminalId) {
		this.criminalId = criminalId;
	}
	public int getCrimeId() {
		return crimeId;
	}
	public void setCrimeId(int crimeId) {
		this.crimeId = crimeId;
	}
	
	public String getArrestDate() {
		return arrestDate;
	}
	public void setArrestDate(String arrestDate) {
		this.arrestDate = arrestDate;
	}
	public String getArrestOfficerName() {
		return arrestOfficerName;
	}
	public void setArrestOfficerName(String arrestOfficerName) {
		this.arrestOfficerName = arrestOfficerName;
	}
	public String getPoliceStationName() {
		return policeStationName;
	}
	public void setPoliceStationName(String policeStationName) {
		this.policeStationName = policeStationName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ArrestDetails [arrestId=" + arrestId + ", criminalId=" + criminalId + ", crimeId=" + crimeId
				+ ", arrestDate=" + arrestDate + ", arrestOfficerName=" + arrestOfficerName + ", policeStationName="
				+ policeStationName + ", status=" + status + "]";
	}
	
}
