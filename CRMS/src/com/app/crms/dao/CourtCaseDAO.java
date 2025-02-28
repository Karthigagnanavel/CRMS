package com.app.crms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.app.crms.bean.CourtCaseDetails;
import com.app.crms.exception.InvalidNumberFormatException;
import com.app.crms.exception.NumberFormatUtil;

public class CourtCaseDAO {

	public int saveCourtCaseDetails(Connection connection,CourtCaseDetails courtCase) {
		int caseId = 0;
		String insertQuery = "insert into court_cases(crime_id,court_name,judge_name,hearing_date,case_status) "
				+ "values (?,?,?,?,?)";
		try(PreparedStatement stmt = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)){
			stmt.setInt(1, courtCase.getCrimeId());
			stmt.setString(2, courtCase.getCourtName());
			stmt.setString(3, courtCase.getJudgeName());
			stmt.setString(4, courtCase.getHearingDate());
			stmt.setString(5, courtCase.getStatus());
			stmt.execute();
			ResultSet resultSet = stmt.getGeneratedKeys();
			while(resultSet.next()) {
				caseId = resultSet.getInt(1);
			}
			if(caseId != 0) {
				System.out.println("case Details stored successfully. Reference Id: "+caseId);
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while inserting court case details "+ e.getMessage());
		}
		return caseId;
	}
	
	public List<CourtCaseDetails> getAllCourtCase(Connection connection){
		List<CourtCaseDetails> courtCases = new ArrayList<CourtCaseDetails>();
		String slectQuery = "select case_id,crime_id,court_name,judge_name,hearing_date,case_status from court_cases";
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(slectQuery)){
			while(resultSet.next()) {
				CourtCaseDetails courtCase = new CourtCaseDetails();
				courtCase.setCaseId(resultSet.getInt(1));
				courtCase.setCrimeId(resultSet.getInt(2));
				courtCase.setCourtName(resultSet.getString(3));
				courtCase.setJudgeName(resultSet.getString(4));
				courtCase.setHearingDate(resultSet.getString(5));
				courtCase.setStatus(resultSet.getString(6));
				courtCases.add(courtCase);
			}
			System.out.println(courtCases.size() +" court case data retrieved successfully");
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while selecting court case details "+ e.getMessage());
		}
		courtCases.forEach(data -> System.out.println(data.toString()));
		return courtCases;
	}
	
	public String updateCourtCase(Connection connection,CourtCaseDetails courtCase,int caseId) {
		String status ="Failed";
		String udateQuery = "update court_cases set crime_id=?,court_name=?,judge_name=?,hearing_date=?,case_status=? where case_id=?";
		try(PreparedStatement stmt = connection.prepareStatement(udateQuery)){
			stmt.setInt(1, courtCase.getCrimeId());
			stmt.setString(2, courtCase.getCourtName());
			stmt.setString(3, courtCase.getJudgeName());
			stmt.setString(4, courtCase.getHearingDate());
			stmt.setString(5, courtCase.getStatus());
			stmt.setInt(6, caseId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Case Id:"+caseId+" is updated successfully.");
				status = "Success";
			}else {
				System.out.println("Case Id not found");
			}
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while updating court case details "+ e.getMessage());
		}
		return status;
	}
	public String deleteCourtCase(Connection connection,int caseId) {
		String status ="not deleted";
		try(PreparedStatement stmt = connection.prepareStatement("delete from court_cases where case_id=?")){
			stmt.setInt(1,caseId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Case Id:"+caseId+" is deleted successfully.");
				status ="deleted";
			}else {
				System.out.println("Case Id:"+caseId+" is not found");
				status ="not found";
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while deleting court case details "+ e.getMessage());
		}
		return status;
	}
	public List<CourtCaseDetails> searchCourtCase(Connection connection,String caseIdOrCourtName) {
		List<CourtCaseDetails> courtCases = new ArrayList<>();
		int caseId = 0;
		String courtName = null;
		StringBuilder queryBuilder = new StringBuilder();
		try {
			queryBuilder.append("select case_id,crime_id,court_name,judge_name,hearing_date,case_status from court_cases ");
			caseId = NumberFormatUtil.parseInteger(caseIdOrCourtName);
			System.out.println("We searching by case_id: "+ caseId);
			queryBuilder.append("where case_id="+caseId);
		}catch (InvalidNumberFormatException e) {
			System.out.println("Exception occurs "+e.getMessage());
			courtName = caseIdOrCourtName;
			System.out.println("We searching by court_name: "+courtName);
			queryBuilder.append("where court_name like '"+courtName+"%'");
		}
		
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(queryBuilder.toString())){
			while(resultSet.next()) {
				CourtCaseDetails courtCase = new CourtCaseDetails();
				courtCase.setCaseId(resultSet.getInt(1));
				courtCase.setCrimeId(resultSet.getInt(2));
				courtCase.setCourtName(resultSet.getString(3));
				courtCase.setJudgeName(resultSet.getString(4));
				courtCase.setHearingDate(resultSet.getString(5));
				courtCase.setStatus(resultSet.getString(6));
				courtCases.add(courtCase);
			}
			if(courtCases.size() > 0) {
				if(caseId !=0 ) {
					System.out.println("Case id:"+caseId+" data retrieved successfully");
				}else {
					System.out.println("Court Name:"+courtName+" data retrieved successfully");
				}
			}else {
				System.out.println("Case not found");
			}
			courtCases.forEach(data -> System.out.println(data.toString()));
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while selecting court case details "+ e.getMessage());
		}
		return courtCases;
	}
}
