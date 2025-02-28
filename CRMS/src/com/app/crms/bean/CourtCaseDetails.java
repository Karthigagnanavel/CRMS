package com.app.crms.bean;

import java.util.Date;

public class CourtCaseDetails {

	private int caseId;
	private int crimeId;
	private String courtName;
	private String judgeName;
	private String hearingDate;
	private String status;
	public int getCaseId() {
		return caseId;
	}
	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}
	public int getCrimeId() {
		return crimeId;
	}
	public void setCrimeId(int crimeId) {
		this.crimeId = crimeId;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getJudgeName() {
		return judgeName;
	}
	public void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
	}
	
	public String getHearingDate() {
		return hearingDate;
	}
	public void setHearingDate(String hearingDate) {
		this.hearingDate = hearingDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "CourtCaseDetails [caseId=" + caseId + ", crimeId=" + crimeId + ", courtName=" + courtName
				+ ", judgeName=" + judgeName + ", hearingDate=" + hearingDate + ", status=" + status + "]";
	}
}
