package com.app.crms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.app.crms.bean.CrimeDetails;
import com.app.crms.exception.InvalidNumberFormatException;
import com.app.crms.exception.NumberFormatUtil;
import com.app.crms.utils.DatabaseConnection;

public class CrimeDAO {

	public int saveCrimeDetails(Connection connection,CrimeDetails crime) {
		int crimeId = 0;
		String insertQuery = "insert into crimes(criminal_id,crime_type,crime_description,crime_date,place,status) "
				+ "values (?,?,?,?,?,?)";
		try(PreparedStatement stmt = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)){
			stmt.setInt(1, crime.getCriminalId());
			stmt.setString(2, crime.getCrimeType());
			stmt.setString(3, crime.getCrimeDescription());
			stmt.setString(4, crime.getCrimeDate());
			stmt.setString(5, crime.getPlace());
			stmt.setString(6, crime.getStatus());
			stmt.execute();
			ResultSet resultSet = stmt.getGeneratedKeys();
			while(resultSet.next()) {
				crimeId = resultSet.getInt(1);
			}
			
			if(crimeId != 0) {
				System.out.println("Crime Details stored successfully. Reference Id: "+crimeId);
			}
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while inserting crime details "+ e.getMessage());
		}
		return crimeId;
	}
	
	public List<CrimeDetails> getAllCrime(Connection connection){
		List<CrimeDetails> crimes = new ArrayList<CrimeDetails>();
		String slectQuery = "select crime_id,criminal_id,crime_type,crime_description,crime_date,place,status from crimes";
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(slectQuery)){
			while(resultSet.next()) {
				CrimeDetails crime = new CrimeDetails();
				crime.setCrimeId(resultSet.getInt(1));
				crime.setCriminalId(resultSet.getInt(2));
				crime.setCrimeType(resultSet.getString(3));
				crime.setCrimeDescription(resultSet.getString(4));
				crime.setCrimeDate(resultSet.getString(5));
				crime.setPlace(resultSet.getString(6));
				crime.setStatus(resultSet.getString(7));
				crimes.add(crime);
			}
			System.out.println(crimes.size() +" Crime data retrieved successfully");
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while selecting crime details "+ e.getMessage());
		}
		
		crimes.forEach(data -> System.out.println(data.toString()));
		
		return crimes;
	}
	
	public String updateCrime(Connection connection,CrimeDetails crime,int crimeId) {
		String status ="failed";
		String udateQuery = "update crimes set criminal_id=?,crime_type=?,crime_description=?,crime_date=?,place=?,status=? where crime_id=?";
		try(PreparedStatement stmt = connection.prepareStatement(udateQuery)){
			stmt.setInt(1, crime.getCriminalId());
			stmt.setString(2, crime.getCrimeType());
			stmt.setString(3, crime.getCrimeDescription());
			stmt.setString(4, crime.getCrimeDate());
			stmt.setString(5, crime.getPlace());
			stmt.setString(6, crime.getStatus());
			stmt.setInt(7, crimeId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Crime Id:"+crimeId+" is updated successfully.");
				status ="Success";
			}else {
				System.out.println("Crime Id not found");
			}
			
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while updating crime details "+ e.getMessage());
		}
		return status;
	}
	public String deleteCrime(Connection connection,int crimeId) {
		String status = "not deleted";
		try(PreparedStatement stmt = connection.prepareStatement("delete from crimes where crime_id=?")){
			stmt.setInt(1,crimeId);
			if(stmt.executeUpdate() > 0) {
				System.out.println("Crime Id:"+crimeId+" is deleted successfully.");
				status ="deleted";
			}else {
				System.out.println("Crime Id:"+crimeId+" is not found");
				status ="not found";
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while deleting crime details "+ e.getMessage());
		}
		return status;
	}
	public List<CrimeDetails> searchCrime(Connection connection,String crimeIdOrCrimeType) {
		List<CrimeDetails> crimes = new ArrayList<>();
		int crimeId = 0;
		String crimeTye = null;
		StringBuilder queryBuilder = new StringBuilder();
		try {
			queryBuilder.append("select crime_id,criminal_id,crime_type,crime_description,crime_date,place,status from crimes ");
			crimeId = NumberFormatUtil.parseInteger(crimeIdOrCrimeType);
			System.out.println("We searching by crime_Id: "+ crimeId);
			queryBuilder.append("where crime_id="+crimeId);
		}catch (InvalidNumberFormatException e) {
			System.out.println("Exception occurs "+e.getMessage());
			crimeTye = crimeIdOrCrimeType;
			System.out.println("We searching by crime_type: "+crimeTye);
			queryBuilder.append("where crime_type like '"+crimeTye+"%'");
		}
		
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery(queryBuilder.toString())){
			while(resultSet.next()) {
				CrimeDetails crime = new CrimeDetails();
				crime.setCrimeId(resultSet.getInt(1));
				crime.setCriminalId(resultSet.getInt(2));
				crime.setCrimeType(resultSet.getString(3));
				crime.setCrimeDescription(resultSet.getString(4));
				crime.setCrimeDate(resultSet.getString(5));
				crime.setPlace(resultSet.getString(6));
				crime.setStatus(resultSet.getString(7));
				crimes.add(crime);
			}
			
			if(crimes.size() > 0) {
				if(crimeId !=0 ) {
					System.out.println("Crime id:"+crimeId+" data retrieved successfully");
				}else {
					System.out.println("Crime Type:"+crimeTye+" data retrieved successfully");
				}
			}else {
				System.out.println("Crime not found");
			}
			crimes.forEach(data -> System.out.println(data.toString()));
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while selecting crime details "+ e.getMessage());
		}
		return crimes;
	}
	
	public String[] getCrimeIds(Connection connection) {
		List<String> crimeIds = new ArrayList<String>();
		crimeIds.add("");
		try(Statement stmt = connection.createStatement();
				ResultSet resultSet = stmt.executeQuery("select crime_id from crimes")) {
			while(resultSet.next()) {
				crimeIds.add(resultSet.getString(1));
			}
		}catch(Exception e) {
			System.out.println("Exception Ocuurs while getting crime details "+ e.getMessage());
		}
		return crimeIds.toArray(new String[crimeIds.size()]);
	}
}
