package com.app.crms;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.app.crms.bean.ArrestDetails;
import com.app.crms.bean.CrimeDetails;
import com.app.crms.dao.ArrestDAO;
import com.app.crms.dao.CrimeDAO;
import com.app.crms.dao.CriminalDAO;
import com.app.crms.utils.DatabaseConnection;

public class ArrestPage extends JFrame{
	// Declare components
			private JTextField arrest_date, arresting_officer , police_station;
			private JComboBox<String> crimeIdComboBox;
			private JComboBox<String> criminalIdComboBox;
			private JComboBox<String> statusComboBox;
			private JButton btnSave, btnView, btnSearch, btnUpdate, btnDelete,btnNext,btnBack;
			private JTable table;
			private DefaultTableModel model;

			// Database connection variables
			private Connection connection;
			
			public void startArrest() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new ArrestPage().setVisible(true);
					}
				});
			}
		
		  public ArrestPage() {
			  		connection = DatabaseConnection.getConnection();
					// JFrame settings
					setTitle("Criminal Record Management System | Arrest Page");
					setSize(1000, 800);
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					setLocationRelativeTo(null);
					setLayout(new BorderLayout());
			
					// Initialize components
					JPanel panel = new JPanel();
					panel.setLayout(new GridLayout(8, 2));

					JLabel lblArrestDate = new JLabel("Arrest Date (yyyy-MM-dd):");
					arrest_date = new JTextField();
					JLabel lblArrestOfficer = new JLabel("Arresting Officer:");
					arresting_officer = new JTextField();
					JLabel lblPliceStation = new JLabel("Police Station:");
					police_station = new JTextField();
					JLabel lblCrimalId = new JLabel("Criminal Id:");
					criminalIdComboBox = new JComboBox<>(new CriminalDAO().getCrimalIds(connection));
					JLabel lblCrimeId = new JLabel("Crime Id:");
					crimeIdComboBox = new JComboBox<>(new CrimeDAO().getCrimeIds(connection));
					JLabel lblmariedStatus = new JLabel("Status:");
					statusComboBox = new JComboBox<>(new String[] { "", "Arrested", "Relesed","Pending" });

					btnBack = new JButton("Back");
					btnSave = new JButton("Save");
					btnView = new JButton("View Records");
					btnSearch = new JButton("Search");
					btnUpdate = new JButton("Update");
					btnDelete = new JButton("Delete");
					btnNext = new JButton("Next");

					panel.add(lblArrestDate);
					panel.add(arrest_date);
					panel.add(lblArrestOfficer);
					panel.add(arresting_officer);
					panel.add(lblPliceStation);
					panel.add(police_station);
					panel.add(lblCrimalId);
					panel.add(criminalIdComboBox);
					panel.add(lblCrimeId);
					panel.add(crimeIdComboBox);
					panel.add(lblmariedStatus);
					panel.add(statusComboBox);
					// Add panel to frame
					add(panel, BorderLayout.NORTH);

					// JTable for displaying Crime records
					model = new DefaultTableModel(new String[] { "Arrest Id","Crime Id", "Criminal Id", "Arrest Date", "Arresting Officer","Police_station",
							"Status"}, 0);
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
							saveArrestRecord();
						}
						
					});

					btnView.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							viewArrestRecords();
						}
					});

					btnSearch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							searchArrestRecord();
						}

					});

					btnDelete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							deletedArrestRecord();
						}
					});

					btnUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							updateArrestRecord();
						}
					});
					btnNext.addActionListener(new  ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new CourtCasePage().startCase();
						}
					});
					btnBack.addActionListener(new  ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new CrimePage().startCrime();
						}
					});
		}
		private void saveArrestRecord() {
			try {
				ArrestDAO arrestDAO = new ArrestDAO();
				ArrestDetails arrest = getValuesFromTextFields();
				int criminalId = arrestDAO.saveArrestDetails(connection, arrest); // insert into db
				List<ArrestDetails> arrests = arrestDAO.searchArrest(connection, String.valueOf(criminalId));
				printTableData(arrests);
				JOptionPane.showMessageDialog(this, "Arrest record saved!");
				clearData();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error saving record.");
			}
			
		}
		private void viewArrestRecords() {
			try {
				ArrestDAO arrestDAO = new ArrestDAO();
				List<ArrestDetails> arrests = arrestDAO.getAllArrest(connection);
				printTableData(arrests);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error fetching records.");
			}
			
		}
		private void searchArrestRecord() {
			try {
				String searchTerm = JOptionPane.showInputDialog(this, "Enter Arrest ID or Officer Name:");
				ArrestDAO arrestDAO = new ArrestDAO();
				List<ArrestDetails> arrests = arrestDAO.searchArrest(connection, searchTerm);
				if (arrests.size() == 0) {
					JOptionPane.showMessageDialog(this, "Arrest Not Found");
				}
				printTableData(arrests);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error searching records.");
			}
			
		}
		private void deletedArrestRecord() {
			try {
				String searchTerm = JOptionPane.showInputDialog(this, "Enter Arrest ID");
				ArrestDAO arrestDAO = new ArrestDAO();
				String status = arrestDAO.deleteArrest(connection, Integer.parseInt(searchTerm));
				if (status.equalsIgnoreCase("not found")) {
					JOptionPane.showMessageDialog(this, "Arrest Not Found");
				} else if (status.equalsIgnoreCase("deleted")) {
					JOptionPane.showMessageDialog(this, "Arrest deleted successfully");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error deleting records.");
			}
			
		}
		private void updateArrestRecord() {
			try {
				String updateTerm = JOptionPane.showInputDialog(this, "Enter Arrest ID");
				ArrestDAO arrestDAO = new ArrestDAO();
				ArrestDetails arrest = getValuesFromTextFields();
				String status = arrestDAO.updateArrest(connection, arrest, Integer.parseInt(updateTerm));
				if (status.equalsIgnoreCase("failed")) {
					JOptionPane.showMessageDialog(this, "Arrest Not Found");
				} else if (status.equalsIgnoreCase("success")) {
					JOptionPane.showMessageDialog(this, "Arrest updated successfully");
				}
				clearData();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error updating records.");
			}
			
		}
		private void printTableData(List<ArrestDetails> arrests) {
			model.setRowCount(0); // Clear previous records
			for (ArrestDetails arrest : arrests) {
				Object[] row = { arrest.getArrestId(), arrest.getCrimeId(),arrest.getCriminalId(), arrest.getArrestDate(), arrest.getArrestOfficerName(),
						arrest.getPoliceStationName(), arrest.getStatus()};
				model.addRow(row);
			}
		}
		private void clearData() {
			arrest_date.setText("");
			arresting_officer.setText("");
			police_station.setText("");
			statusComboBox.setSelectedItem("");
			criminalIdComboBox.setSelectedItem("");
			crimeIdComboBox.setSelectedItem("");
		}

		private ArrestDetails getValuesFromTextFields() {
			ArrestDetails arrest = new ArrestDetails();
			arrest.setCriminalId(Integer.parseInt((String)criminalIdComboBox.getSelectedItem()));
			arrest.setCrimeId(Integer.parseInt((String)crimeIdComboBox.getSelectedItem()));
			arrest.setArrestDate(arrest_date.getText());
			arrest.setArrestOfficerName(arresting_officer.getText());
			arrest.setPoliceStationName(police_station.getText());
			arrest.setStatus((String)statusComboBox.getSelectedItem());
			return arrest;
		}
}
