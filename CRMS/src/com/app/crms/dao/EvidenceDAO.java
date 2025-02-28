package com.app.crms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.app.crms.bean.EvidenceDetails;
import com.app.crms.exception.InvalidNumberFormatException;
import com.app.crms.exception.NumberFormatUtil;

public class EvidenceDAO {

	public int saveEvidenceDetails(Connection connection,EvidenceDetails evidence) {
		int evidenceId = 0;
		String insertQuery = "insert into evidence(crime_id,evidence_type,description,storage_location) "
				+ "values (?,?,?,?)";
		try(PreparedStatement stmt = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)){
			stmt.setInt(1, evidence.getCrimeId());
			stmt.setString(2, evidence.getEvidenceType());
			stmt.setString(3, evidence.getDescription());
			stmt.setString(4, evidence.getLocation());
			stmt.execute();
			ResultSet resultSet = stmt.getGeneratedKeys();
			while(resultSet.next()) {
				evidenceId = resultSet.getInt(1);
			}
			if(evidenceId != 0) {
				System.out.println("Evidence Details stored successfully. Reference Id: "+evidenceId);
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while inserting evidence details "+ e.getMessage());
		}
		return evidenceId;
	}
	
	public List<EvidenceDetails> getAllEvidence(Connection connection){
		List<EvidenceDetails> evidences = new ArrayList<EvidenceDetails>();
		String slectQuery = "select evidence_id,crime_id,evidence_type,description,storage_location from evidence";
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(slectQuery)){
			while(resultSet.next()) {
				EvidenceDetails evidence = new EvidenceDetails();
				evidence.setEvidenceId(resultSet.getInt(1));
				evidence.setCrimeId(resultSet.getInt(2));
				evidence.setEvidenceType(resultSet.getString(3));
				evidence.setDescription(resultSet.getString(4));
				evidence.setLocation(resultSet.getString(5));
				evidences.add(evidence);
			}
			System.out.println(evidences.size() +" evidence data retrieved successfully");
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while selecting evidence details "+ e.getMessage());
		}
		evidences.forEach(data -> System.out.println(data.toString()));
		return evidences;
	}
	
	public String updateEvidence(Connection connection,EvidenceDetails evidence,int evidenceId) {
		String status ="Failed";
		String udateQuery = "update evidence set crime_id=?,evidence_type=?,description=?,storage_location=? where evidence_id=?";
		try(PreparedStatement stmt = connection.prepareStatement(udateQuery)){
			stmt.setInt(1, evidence.getCrimeId());
			stmt.setString(2, evidence.getEvidenceType());
			stmt.setString(3, evidence.getDescription());
			stmt.setString(4, evidence.getLocation());
			stmt.setInt(5, evidenceId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Evidence Id:"+evidenceId+" is updated successfully.");
				status ="Success";
			}else {
				System.out.println("Evidence Id not found");
			}
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while updating evidence details "+ e.getMessage());
		}
		return status;
	}
	public String deleteEvidence(Connection connection,int evidenceId) {
		String status = "not deleted";
		try(PreparedStatement stmt = connection.prepareStatement("delete from evidence where evidence_id=?")){
			stmt.setInt(1,evidenceId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Evidence Id:"+evidenceId+" is deleted successfully.");
				status = "deleted";
			}else {
				System.out.println("Evidence Id:"+evidenceId+" is not found");
				status = "not found";
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while deleting evidence details "+ e.getMessage());
		}
		return status;
	}
	public List<EvidenceDetails> searchEvidence(Connection connection,String evidenceIdOrEvidenceType) {
		List<EvidenceDetails> evidences = new ArrayList<>();
		int evidenceId = 0;
		String evidenceType = null;
		StringBuilder queryBuilder = new StringBuilder();
		try {
			queryBuilder.append("select evidence_id,crime_id,evidence_type,description,storage_location from evidence ");
			evidenceId = NumberFormatUtil.parseInteger(evidenceIdOrEvidenceType);
			System.out.println("We searching by evidence_id: "+ evidenceId);
			queryBuilder.append("where evidence_id="+evidenceId);
		}catch (InvalidNumberFormatException e) {
			System.out.println("Exception occurs "+e.getMessage());
			evidenceType = evidenceIdOrEvidenceType;
			System.out.println("We searching by evidence_type: "+evidenceType);
			queryBuilder.append("where evidence_type like '"+evidenceType+"%'");
		}
		
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(queryBuilder.toString())){
			while(resultSet.next()) {
				EvidenceDetails evidence = new EvidenceDetails();
				evidence.setEvidenceId(resultSet.getInt(1));
				evidence.setCrimeId(resultSet.getInt(2));
				evidence.setEvidenceType(resultSet.getString(3));
				evidence.setDescription(resultSet.getString(4));
				evidence.setLocation(resultSet.getString(5));
				evidences.add(evidence);
			}
			if(evidences.size() > 0) {
				if(evidenceId !=0 ) {
					System.out.println("Evidence id:"+evidenceId+" data retrieved successfully");
				}else {
					System.out.println("Evidence Name:"+evidenceType+" data retrieved successfully");
				}
			}else {
				System.out.println("Evidence not found");
			}
			evidences.forEach(data -> System.out.println(data.toString()));
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while selecting evidence details "+ e.getMessage());
		}
		return evidences;
	}
}
