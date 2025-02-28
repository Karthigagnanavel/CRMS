package com.app.crms.bean;

import java.util.Date;

public class CriminalDetails {

	private int criminalId;
	private String firstName;
	private String lastName;
	private String fatherName;
	private String gender;
	private int age;
	private String dateOfBirth;
	private long phoneNumer;
	private String nationality;
	private String maritalStatus;
	private Date createdDateTime;
	private boolean mostWanted = false;
	private String occupation;
	private String address;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCriminalId() {
		return criminalId;
	}
	public void setCriminalId(int criminalId) {
		this.criminalId = criminalId;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public long getPhoneNumer() {
		return phoneNumer;
	}
	public void setPhoneNumer(long phoneNumer) {
		this.phoneNumer = phoneNumer;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public boolean isMostWanted() {
		return mostWanted;
	}
	public void setMostWanted(boolean mostWanted) {
		this.mostWanted = mostWanted;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	@Override
	public String toString() {
		return "CriminalDetails [criminalId=" + criminalId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", fatherName=" + fatherName + ", gender=" + gender + ", age=" + age + ", dateOfBirth=" + dateOfBirth
				+ ", phoneNumer=" + phoneNumer + ", nationality=" + nationality + ", maritalStatus=" + maritalStatus
				+ ", createdDateTime=" + createdDateTime + ", mostWanted=" + mostWanted + ", occupation=" + occupation
				+ ", address=" + address + "]";
	}
	
}
