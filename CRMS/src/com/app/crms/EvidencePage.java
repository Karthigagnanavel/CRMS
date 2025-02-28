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

import com.app.crms.bean.CourtCaseDetails;
import com.app.crms.bean.EvidenceDetails;
import com.app.crms.dao.CourtCaseDAO;
import com.app.crms.dao.CrimeDAO;
import com.app.crms.dao.EvidenceDAO;
import com.app.crms.utils.DatabaseConnection;

public class EvidencePage extends JFrame{
	// Declare components
	private JTextField evidenceType, description, location;
	private JComboBox<String> crimeIdComboBox;
	private JButton btnSave, btnView, btnSearch, btnUpdate, btnDelete,btnBack;
	private JTable table;
	private DefaultTableModel model;

	// Database connection variables
	private Connection connection;

	public void startEvidence() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new EvidencePage().setVisible(true);
			}
		});
	}

	public EvidencePage() {
	  		connection = DatabaseConnection.getConnection();
			// JFrame settings
			setTitle("Criminal Record Management System | Evidence Page");
			setSize(1000, 800);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			setLayout(new BorderLayout());
	
			// Initialize components
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(8, 2));

			JLabel lblEvidenceType = new JLabel("Evidence Type:");
			evidenceType = new JTextField();
			JLabel lblDescription = new JLabel("Description:");
			description = new JTextField();
			JLabel lbllocation = new JLabel("Stroge Location:");
			location = new JTextField();
			JLabel lblCrimeId = new JLabel("Crime Id:");
			crimeIdComboBox = new JComboBox<>(new CrimeDAO().getCrimeIds(connection));

			btnBack = new JButton("Back");
			btnSave = new JButton("Save");
			btnView = new JButton("View Records");
			btnSearch = new JButton("Search");
			btnUpdate = new JButton("Update");
			btnDelete = new JButton("Delete");

			panel.add(lblEvidenceType);
			panel.add(evidenceType);
			panel.add(lblDescription);
			panel.add(description);
			panel.add(lbllocation);
			panel.add(location);
			panel.add(crimeIdComboBox);
			panel.add(lblCrimeId);
			panel.add(crimeIdComboBox);
			// Add panel to frame
			add(panel, BorderLayout.NORTH);

			// JTable for displaying Crime records
			model = new DefaultTableModel(new String[] { "Evidence Id","Crime Id", "Evidence Type", "Description", "Location"}, 0);
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
			add(buttonPanel, BorderLayout.SOUTH);
			
			// Add event listeners
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveEvidenceRecord();
				}
				
			});

			btnView.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					viewEvidenceRecords();
				}
			});

			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					searchEvidenceRecord();
				}

			});

			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deletedEvidenceRecord();
				}
			});

			btnUpdate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateEvidenceRecord();
				}
			});
			
			btnBack.addActionListener(new  ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new CourtCasePage().startCase();
				}
			});
}

	private void saveEvidenceRecord() {
		try {
			EvidenceDAO evidenceDAO = new EvidenceDAO();
			EvidenceDetails evidence = getValuesFromTextFields();
			int criminalId = evidenceDAO.saveEvidenceDetails(connection, evidence); // insert into db
			List<EvidenceDetails> evidences = evidenceDAO.searchEvidence(connection, String.valueOf(criminalId));
			printTableData(evidences);
			JOptionPane.showMessageDialog(this, "Evidence record saved!");
			clearData();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error saving record.");
		}

	}

	private void viewEvidenceRecords() {
		try {
			EvidenceDAO evidenceDAO = new EvidenceDAO();
			List<EvidenceDetails> evidences = evidenceDAO.getAllEvidence(connection);
			printTableData(evidences);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching records.");
		}

	}

	private void searchEvidenceRecord() {
		try {
			String searchTerm = JOptionPane.showInputDialog(this, "Enter Evidence ID or Evidence Type:");
			EvidenceDAO evidenceDAO = new EvidenceDAO();
			List<EvidenceDetails> evidences = evidenceDAO.searchEvidence(connection, searchTerm);
			if (evidences.size() == 0) {
				JOptionPane.showMessageDialog(this, "Evidence Not Found");
			}
			printTableData(evidences);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error searching records.");
		}

	}

	private void deletedEvidenceRecord() {
		try {
			String searchTerm = JOptionPane.showInputDialog(this, "Enter Evidence ID");
			EvidenceDAO evidenceDAO = new EvidenceDAO();
			String status = evidenceDAO.deleteEvidence(connection, Integer.parseInt(searchTerm));
			if (status.equalsIgnoreCase("not found")) {
				JOptionPane.showMessageDialog(this, "Evidence Not Found");
			} else if (status.equalsIgnoreCase("deleted")) {
				JOptionPane.showMessageDialog(this, "Evidence deleted successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error deleting records.");
		}

	}

	private void updateEvidenceRecord() {
		try {
			String updateTerm = JOptionPane.showInputDialog(this, "Enter Evidence ID");
			EvidenceDAO evidenceDAO = new EvidenceDAO();
			EvidenceDetails evidence = getValuesFromTextFields();
			String status = evidenceDAO.updateEvidence(connection, evidence, Integer.parseInt(updateTerm));
			if (status.equalsIgnoreCase("failed")) {
				JOptionPane.showMessageDialog(this, "Evidence Not Found");
			} else if (status.equalsIgnoreCase("success")) {
				JOptionPane.showMessageDialog(this, "Evidence updated successfully");
			}
			clearData();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error updating records.");
		}

	}

	private void printTableData(List<EvidenceDetails> evidences) {
		model.setRowCount(0); // Clear previous records
		for (EvidenceDetails evidence : evidences) {
			Object[] row = { evidence.getEvidenceId(), evidence.getCrimeId(), evidence.getEvidenceType(), evidence.getDescription(),
					evidence.getLocation() };
			model.addRow(row);
		}
	}
	private void clearData() {
		evidenceType.setText("");
		description.setText("");
		location.setText("");
		crimeIdComboBox.setSelectedItem("");
	}
	private EvidenceDetails getValuesFromTextFields() {
		EvidenceDetails evidence = new EvidenceDetails();
		evidence.setCrimeId(Integer.parseInt((String) crimeIdComboBox.getSelectedItem()));
		evidence.setEvidenceType(evidenceType.getText());
		evidence.setDescription(description.getText());
		evidence.setLocation(location.getText());
		return evidence;
	}
}
