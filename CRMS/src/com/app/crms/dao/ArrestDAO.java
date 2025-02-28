package com.app.crms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.app.crms.bean.ArrestDetails;
import com.app.crms.exception.InvalidNumberFormatException;
import com.app.crms.exception.NumberFormatUtil;
import com.app.crms.utils.DatabaseConnection;

public class ArrestDAO {

	public int saveArrestDetails(Connection connection,ArrestDetails arrest) {
		int arrestId = 0;
		String insertQuery = "insert into arrests(criminal_id,crime_id,arrest_date,arresting_officer,police_station,status) "
				+ "values (?,?,?,?,?,?)";
		try(PreparedStatement stmt = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)){
			stmt.setInt(1, arrest.getCriminalId());
			stmt.setInt(2, arrest.getCrimeId());
			stmt.setString(3, arrest.getArrestDate());
			stmt.setString(4, arrest.getArrestOfficerName());
			stmt.setString(5, arrest.getPoliceStationName());
			stmt.setString(6, arrest.getStatus());
			stmt.execute();
			ResultSet resultSet = stmt.getGeneratedKeys();
			while(resultSet.next()) {
				arrestId = resultSet.getInt(1);
			}
			if(arrestId != 0) {
				System.out.println("Arrest Details stored successfully. Reference Id: "+arrestId);
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while inserting arrest details "+ e.getMessage());
		}
		return arrestId;
	}
	
	public List<ArrestDetails> getAllArrest(Connection connection){
		List<ArrestDetails> arrests = new ArrayList<ArrestDetails>();
		String slectQuery = "select criminal_id,crime_id,arrest_date,arresting_officer,police_station,status,arrest_id from arrests";
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(slectQuery)){
			while(resultSet.next()) {
				ArrestDetails arrest = new ArrestDetails();
				arrest.setCriminalId(resultSet.getInt(1));
				arrest.setCrimeId(resultSet.getInt(2));
				arrest.setArrestDate(resultSet.getString(3));
				arrest.setArrestOfficerName(resultSet.getString(4));
				arrest.setPoliceStationName(resultSet.getString(5));
				arrest.setStatus(resultSet.getString(6));
				arrest.setArrestId(resultSet.getInt(7));
				arrests.add(arrest);
			}
			System.out.println(arrests.size() +" Arrest data retrieved successfully");
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while selecting arrest details "+ e.getMessage());
		}
		arrests.forEach(data -> System.out.println(data.toString()));
		return arrests;
	}
	
	public String updateArrest(Connection connection,ArrestDetails arrest,int arrestId) {
		String status ="Failed";
		String udateQuery = "update arrests set criminal_id=?,crime_id=?,arrest_date=?,arresting_officer=?,police_station=?,status=? where arrest_id=?";
		try(PreparedStatement stmt = connection.prepareStatement(udateQuery)){
			stmt.setInt(1, arrest.getCriminalId());
			stmt.setInt(2, arrest.getCrimeId());
			stmt.setString(3, arrest.getArrestDate());
			stmt.setString(4, arrest.getArrestOfficerName());
			stmt.setString(5, arrest.getPoliceStationName());
			stmt.setString(6, arrest.getStatus());
			stmt.setInt(7, arrestId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Arrest Id:"+arrestId+" is updated successfully.");
				status ="Success";
			}else {
				System.out.println("Arrest Id not found");
			}
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while updating arrest details "+ e.getMessage());
		}
		return status;
	}
	public String deleteArrest(Connection connection,int arrestId) {
		String status = "not deleted";
		try(PreparedStatement stmt = connection.prepareStatement("delete from arrests where arrest_id=?")){
			stmt.setInt(1,arrestId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Arrest Id:"+arrestId+" is deleted successfully.");
				status ="deleted";
			}else {
				System.out.println("Arrest Id:"+arrestId+" is not found");
				status ="not found";
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while deleting arrest details "+ e.getMessage());
		}
		return status;
	}
	public List<ArrestDetails> searchArrest(Connection connection,String arrestIdOrArrestOfficer) {
		List<ArrestDetails> arrests = new ArrayList<>();
		int arrestId = 0;
		String arrestOfficer = null;
		StringBuilder queryBuilder = new StringBuilder();
		try {
			queryBuilder.append("select criminal_id,crime_id,arrest_date,arresting_officer,police_station,status,arrest_id from arrests ");
			arrestId = NumberFormatUtil.parseInteger(arrestIdOrArrestOfficer);
			System.out.println("We searching by arrest_id: "+ arrestId);
			queryBuilder.append("where arrest_id="+arrestId);
		}catch (InvalidNumberFormatException e) {
			System.out.println("Exception occurs "+e.getMessage());
			arrestOfficer = arrestIdOrArrestOfficer;
			System.out.println("We searching by arresting_officer: "+arrestOfficer);
			queryBuilder.append("where arresting_officer like '"+arrestOfficer+"%'");
		}
		
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(queryBuilder.toString())){
			while(resultSet.next()) {
				ArrestDetails arrest = new ArrestDetails();
				arrest.setCriminalId(resultSet.getInt(1));
				arrest.setCrimeId(resultSet.getInt(2));
				arrest.setArrestDate(resultSet.getString(3));
				arrest.setArrestOfficerName(resultSet.getString(4));
				arrest.setPoliceStationName(resultSet.getString(5));
				arrest.setStatus(resultSet.getString(6));
				arrest.setArrestId(resultSet.getInt(7));
				arrests.add(arrest);
			}
			if(arrests.size() > 0) {
				if(arrestId !=0 ) {
					System.out.println("Arrest id:"+arrestId+" data retrieved successfully");
				}else {
					System.out.println("Arrest Officer:"+arrestOfficer+" data retrieved successfully");
				}
			}else {
				System.out.println("Arrest not found");
			}
			arrests.forEach(data -> System.out.println(data.toString()));
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while selecting arrest details "+ e.getMessage());
		}
		return arrests;
	}
	public static void main(String[] args) {
		Connection c = DatabaseConnection.getConnection();
		ArrestDAO  d = new ArrestDAO();
		ArrestDetails arrest = new ArrestDetails();
		arrest.setCriminalId(1);
		arrest.setCrimeId(2);
		arrest.setArrestDate("2025-02-28");
		arrest.setArrestOfficerName("police");
		arrest.setPoliceStationName("chennai anna nagar");
		arrest.setStatus("Arrested");
		//d.saveArrestDetails(c, arrest);
		//d.getAllArrest(c);
		//d.updateArrest(c, arrest, 1);
		//d.deleteArrest(c, 1);
		//d.searchArrest(c, "te");
	}
}
