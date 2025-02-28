package com.app.crms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.app.crms.bean.CriminalDetails;
import com.app.crms.exception.InvalidNumberFormatException;
import com.app.crms.exception.NumberFormatUtil;
import com.app.crms.utils.DatabaseConnection;

public class CriminalDAO {

	public int saveCriminalDetails(Connection connection,CriminalDetails criminal) {
		int criminalId = 0;
		String insertQuery = "insert into criminals(first_name,last_name,fatherName,mostwanted,age,gender,date_of_birth,nationality,maritalStatus,address,occupation,phoneNumer,created_at) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try(PreparedStatement stmt = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)){
			stmt.setString(1, criminal.getFirstName());
			stmt.setString(2, criminal.getLastName());
			stmt.setString(3, criminal.getFatherName());
			stmt.setString(4, String.valueOf(criminal.isMostWanted()));
			stmt.setInt(5, criminal.getAge());
			stmt.setString(6, criminal.getGender());
			stmt.setString(7, criminal.getDateOfBirth());
			stmt.setString(8, criminal.getNationality());
			stmt.setString(9, criminal.getMaritalStatus());
			stmt.setString(10, criminal.getAddress());
			stmt.setString(11, criminal.getOccupation());
			stmt.setLong(12, criminal.getPhoneNumer());
			stmt.setTimestamp(13,new Timestamp(System.currentTimeMillis()));
			stmt.execute();
			ResultSet resultSet = stmt.getGeneratedKeys();
			while(resultSet.next()) {
				criminalId = resultSet.getInt(1);
			}
			
			if(criminalId != 0) {
				System.out.println("Criminal Details stored successfully. Reference Id: "+criminalId);
			}
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while inserting criminal details "+ e.getMessage());
		}
		return criminalId;
	}
	
	public List<CriminalDetails> getAllCriminals(Connection connection){
		List<CriminalDetails> criminals = new ArrayList<CriminalDetails>();
		String slectQuery = "select criminal_id,first_name,last_name,fatherName,mostwanted,age,gender,date_of_birth,nationality,maritalStatus,address,occupation,phoneNumer,created_at from criminals";
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(slectQuery)){
			while(resultSet.next()) {
				CriminalDetails criminal = new CriminalDetails();
				criminal.setCriminalId(resultSet.getInt(1));
				criminal.setFirstName(resultSet.getString(2));
				criminal.setLastName(resultSet.getString(3));
				criminal.setFatherName(resultSet.getString(4));
				criminal.setMostWanted(resultSet.getBoolean(5));
				criminal.setAge(resultSet.getInt(6));
				criminal.setGender(resultSet.getString(7));
				criminal.setDateOfBirth(resultSet.getString(8));
				criminal.setNationality(resultSet.getString(9));
				criminal.setMaritalStatus(resultSet.getString(10));
				criminal.setAddress(resultSet.getString(11));
				criminal.setOccupation(resultSet.getString(12));
				criminal.setPhoneNumer(resultSet.getLong(13));
				criminal.setCreatedDateTime(resultSet.getTimestamp(14));
				criminals.add(criminal);
			}
			System.out.println(criminals.size() +" Crimals data retrieved successfully");
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while inserting criminal details "+ e.getMessage());
		}
		
		criminals.forEach(data -> System.out.println(data.toString()));
		
		return criminals;
	}
	
	public String updateCriminal(Connection connection,CriminalDetails criminal,int criminalId) {
		
		String status ="failed";
		String udateQuery = "update criminals set first_name=?,last_name=?,fatherName=?,mostwanted=?,age=?,gender=?,date_of_birth=?,nationality=?,maritalStatus=?,address=?,occupation=?,phoneNumer=? where criminal_id=?";
		try(PreparedStatement stmt = connection.prepareStatement(udateQuery)){
			stmt.setString(1, criminal.getFirstName());
			stmt.setString(2, criminal.getLastName());
			stmt.setString(3, criminal.getFatherName());
			stmt.setString(4, String.valueOf(criminal.isMostWanted()));
			stmt.setInt(5, criminal.getAge());
			stmt.setString(6, criminal.getGender());
			stmt.setString(7, criminal.getDateOfBirth());
			stmt.setString(8, criminal.getNationality());
			stmt.setString(9, criminal.getMaritalStatus());
			stmt.setString(10, criminal.getAddress());
			stmt.setString(11, criminal.getOccupation());
			stmt.setLong(12, criminal.getPhoneNumer());
			stmt.setInt(13, criminalId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Criminal Id:"+criminalId+" is updated successfully.");
				status = "success";
			}
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while inserting criminal details "+ e.getMessage());
		}
		return status;
	}
	public String deleteCrimal(Connection connection,int criminalId) {
		String status = "not deleted";
		try(PreparedStatement stmt = connection.prepareStatement("delete from criminals where criminal_id=?")){
			stmt.setInt(1,criminalId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Criminal Id:"+criminalId+" is deleted successfully.");
				status ="deleted";
			}else {
				System.out.println("Criminal Id:"+criminalId+" is not found");
				status ="not found";
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while inserting criminal details "+ e.getMessage());
		}
		return status;
	}
	public List<CriminalDetails> searchCriminal(Connection connection,String criminalIdOrName) {
		List<CriminalDetails> criminals = new ArrayList<>();
		int criminalId = 0;
		String criminalName = null;
		StringBuilder queryBuilder = new StringBuilder();
		try {
			queryBuilder.append("select criminal_id,first_name,last_name,fatherName,mostwanted,age,gender,date_of_birth,nationality,maritalStatus,address,occupation,phoneNumer,created_at from criminals ");
			criminalId = NumberFormatUtil.parseInteger(criminalIdOrName);
			System.out.println("We searching by criminal_Id: "+ criminalId);
			queryBuilder.append("where criminal_id="+criminalId);
		}catch (InvalidNumberFormatException e) {
			System.out.println("Exception occurs "+e.getMessage());
			criminalName = criminalIdOrName;
			System.out.println("We searching by criminal_name: "+criminalName);
			queryBuilder.append("where first_name like '"+criminalName+"%'");
		}
		
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(queryBuilder.toString())){
			while(resultSet.next()) {
				CriminalDetails criminal = new CriminalDetails();
				criminal.setCriminalId(resultSet.getInt(1));
				criminal.setFirstName(resultSet.getString(2));
				criminal.setLastName(resultSet.getString(3));
				criminal.setFatherName(resultSet.getString(4));
				criminal.setMostWanted(resultSet.getBoolean(5));
				criminal.setAge(resultSet.getInt(6));
				criminal.setGender(resultSet.getString(7));
				criminal.setDateOfBirth(resultSet.getString(8));
				criminal.setNationality(resultSet.getString(9));
				criminal.setMaritalStatus(resultSet.getString(10));
				criminal.setAddress(resultSet.getString(11));
				criminal.setOccupation(resultSet.getString(12));
				criminal.setPhoneNumer(resultSet.getLong(13));
				criminal.setCreatedDateTime(resultSet.getTimestamp(14));
				criminals.add(criminal);
			}
			
			if(criminals.size() > 0) {
				if(criminalId !=0) {
					System.out.println("Crimal id:"+criminalId+" data retrieved successfully");
				}else {
					System.out.println("Crimal name:"+criminalName+" data retrieved successfully");
				}
			}else {
				System.out.println("Criminal not fund");
			}
			criminals.forEach(data -> System.out.println(data.toString()));
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while selecting criminal details "+ e.getMessage());
		}
		
		return criminals;
	}
	
	public String[] getCrimalIds(Connection connection) {
		List<String> criminalIds = new ArrayList<String>();
		criminalIds.add("");
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery("select criminal_id from criminals")) {
			while(resultSet.next()) {
				criminalIds.add(resultSet.getString(1));
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while getting criminal details "+ e.getMessage());
		}
		return criminalIds.toArray(new String[criminalIds.size()]);
	}
}
