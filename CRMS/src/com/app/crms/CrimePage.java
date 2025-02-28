package com.app.crms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.app.crms.bean.CrimeDetails;
import com.app.crms.bean.CriminalDetails;
import com.app.crms.dao.CrimeDAO;
import com.app.crms.dao.CriminalDAO;
import com.app.crms.utils.DatabaseConnection;

public class CrimePage extends JFrame{
		// Declare components
		private JTextField crime_type, crime_description , crime_date , location;
		private JComboBox<String> statusComboBox;
		private JComboBox<String> criminalIdComboBox;
		private JButton btnSave, btnView, btnSearch, btnUpdate, btnDelete,btnNext,btnBack;
		private JTable table;
		private DefaultTableModel model;

		// Database connection variables
		private Connection connection;
		
		public void startCrime() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new CrimePage().setVisible(true);
				}
			});
		}
	
	  public CrimePage() {
		  		connection = DatabaseConnection.getConnection();
				// JFrame settings
				setTitle("Criminal Record Management System | Crime Page");
				setSize(1000, 800);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setLocationRelativeTo(null);
				setLayout(new BorderLayout());
		
				// Initialize components
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(8, 2));

				JLabel lblCrimeType = new JLabel("Crime Type:");
				crime_type = new JTextField();
				JLabel lblDescription = new JLabel("Crime description:");
				crime_description = new JTextField();
				JLabel lblfatherName = new JLabel("Crime Date(yyyy-MM-dd):");
				crime_date = new JTextField();
				JLabel lblCrimalId = new JLabel("Criminal Id:");
				criminalIdComboBox = new JComboBox<>(new CriminalDAO().getCrimalIds(connection));
				JLabel lblmariedStatus = new JLabel("Status:");
				statusComboBox = new JComboBox<>(new String[] { "", "Under Investigation", "Solved","Closed" });
				JLabel lblLocation = new JLabel("Location:");
				location = new JTextField();

				btnBack = new JButton("Back");
				btnSave = new JButton("Save");
				btnView = new JButton("View Records");
				btnSearch = new JButton("Search");
				btnUpdate = new JButton("Update");
				btnDelete = new JButton("Delete");
				btnNext = new JButton("Next");

				panel.add(lblCrimeType);
				panel.add(crime_type);
				panel.add(lblDescription);
				panel.add(crime_description);
				panel.add(lblfatherName);
				panel.add(crime_date);
				panel.add(lblCrimalId);
				panel.add(criminalIdComboBox);
				panel.add(lblmariedStatus);
				panel.add(statusComboBox);
				panel.add(lblLocation);
				panel.add(location);
				// Add panel to frame
				add(panel, BorderLayout.NORTH);

				// JTable for displaying Crime records
				model = new DefaultTableModel(new String[] { "Crime Id", "Criminal Id", "Criminal Type", "Descrption",
						"Crime Date", "Location", "Status"}, 0);
				table = new JTable(model);
				JScrollPane tableScroll = new JScrollPane(table);
				add(tableScroll, BorderLayout.CENTER);

				// Add buttons
				JPanel buttonPanel = new JPanel();
				buttonPanel.add(btnBack);
				buttonPanel.add(btnSave);
				buttonPanel.add(btnView);
				buttonPanel.add(btnSearch);
				buttonPanel.add(btnUpdate);
				buttonPanel.add(btnDelete);
				buttonPanel.add(btnNext);
				add(buttonPanel, BorderLayout.SOUTH);
				
				// Add event listeners
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveCrimeRecord();
					}
					
				});

				btnView.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						viewCrimeRecords();
					}
				});

				btnSearch.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						searchCrimeRecord();
					}

				});

				btnDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						deletedCrimalRecord();
					}
				});

				btnUpdate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						updateCrimalRecord();
					}
				});
				btnNext.addActionListener(new  ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new ArrestPage().startArrest();
					}
				});
				btnBack.addActionListener(new  ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								new CriminalPage().setVisible(true);
							}
						});
					}
				});
	}
	private void saveCrimeRecord() {
		try {
			CrimeDAO crimeDAO = new CrimeDAO();
			CrimeDetails crime = getValuesFromTextFields();
			int criminalId = crimeDAO.saveCrimeDetails(connection, crime); // insert into db
			List<CrimeDetails> criminals = crimeDAO.searchCrime(connection, String.valueOf(criminalId));
			printTableData(criminals);
			JOptionPane.showMessageDialog(this, "Crime record saved!");
			clearData();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error saving record.");
		}
		
	}
	private void viewCrimeRecords() {
		try {
			CrimeDAO crimeDAO = new CrimeDAO();
			List<CrimeDetails> crimes = crimeDAO.getAllCrime(connection);
			printTableData(crimes);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching records.");
		}
		
	}
	private void searchCrimeRecord() {
		try {
			String searchTerm = JOptionPane.showInputDialog(this, "Enter Crime ID or Crime Type:");
			CrimeDAO crimeDAO = new CrimeDAO();
			List<CrimeDetails> crimes = crimeDAO.searchCrime(connection, searchTerm);
			if (crimes.size() == 0) {
				JOptionPane.showMessageDialog(this, "Crime Not Found");
			}
			printTableData(crimes);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error searching records.");
		}
		
	}
	private void deletedCrimalRecord() {
		try {
			String searchTerm = JOptionPane.showInputDialog(this, "Enter Crime ID");
			CrimeDAO crimeDAO = new CrimeDAO();
			String status = crimeDAO.deleteCrime(connection, Integer.parseInt(searchTerm));
			if (status.equalsIgnoreCase("not found")) {
				JOptionPane.showMessageDialog(this, "Crime Not Found");
			} else if (status.equalsIgnoreCase("deleted")) {
				JOptionPane.showMessageDialog(this, "Crime deleted successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error searching records.");
		}
		
	}
	private void updateCrimalRecord() {
		try {
			String updateTerm = JOptionPane.showInputDialog(this, "Enter Crime ID");
			CrimeDAO crimeDAO = new CrimeDAO();
			CrimeDetails crime = getValuesFromTextFields();
			String status = crimeDAO.updateCrime(connection, crime, Integer.parseInt(updateTerm));
			if (status.equalsIgnoreCase("failed")) {
				JOptionPane.showMessageDialog(this, "Crime Not Found");
			} else if (status.equalsIgnoreCase("success")) {
				JOptionPane.showMessageDialog(this, "Crime updated successfully");
			}
			clearData();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error updating records.");
		}
		
	}
	private void printTableData(List<CrimeDetails> crimes) {
		model.setRowCount(0); // Clear previous records
		for (CrimeDetails crime : crimes) {
			Object[] row = { crime.getCrimeId(),crime.getCriminalId(), crime.getCrimeType(), crime.getCrimeDescription(),
					crime.getCrimeDate(), crime.getPlace(), crime.getStatus()};
			model.addRow(row);
		}
	}
	private void clearData() {
		crime_type.setText("");
		crime_description.setText("");
		crime_date.setText("");
		location.setText("");
		statusComboBox.setSelectedItem("");
		criminalIdComboBox.setSelectedItem("");
	}

	private CrimeDetails getValuesFromTextFields() {
		CrimeDetails crime = new CrimeDetails();
		crime.setCriminalId(Integer.parseInt((String)criminalIdComboBox.getSelectedItem()));
		crime.setCrimeType(crime_type.getText());
		crime.setCrimeDescription(crime_description.getText());
		crime.setCrimeDate(crime_date.getText());
		crime.setPlace(location.getText());
		crime.setStatus((String)statusComboBox.getSelectedItem());
		return crime;
	}
}
