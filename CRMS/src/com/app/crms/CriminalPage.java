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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.app.crms.bean.CriminalDetails;
import com.app.crms.dao.CriminalDAO;
import com.app.crms.utils.DatabaseConnection;

public class CriminalPage extends JFrame {
	private JTextField txtFirstName, txtLastName, txtFatherName, txtAge, txtDOB, txtNationality, txtOccupatin,
			txtPhoneNumber;
	private JTextArea txtAddress;
	private JComboBox<String> genderComboBox;
	private JComboBox<String> mostWantedComboBox;
	private JComboBox<String> mariedStatusComboBox;
	private JButton btnSave, btnView, btnSearch, btnUpdate, btnDelete,btnNext;
	private JTable table;
	private DefaultTableModel model;

	// Database connection variables
	private Connection connection;
	
	public void startCriminal() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CriminalPage().setVisible(true);
			}
		});
	}

	public CriminalPage() {
		// JFrame settings
		setTitle("Criminal Record Management System | Criminal Page");
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Initialize components
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 2));

		JLabel lblFirstName = new JLabel("First Name:");
		txtFirstName = new JTextField();
		JLabel lblLastName = new JLabel("Last Name:");
		txtLastName = new JTextField();
		JLabel lblfatherName = new JLabel("Father Name:");
		txtFatherName = new JTextField();
		JLabel lblGender = new JLabel("Gender:");
		genderComboBox = new JComboBox<>(new String[] { "", "Male", "Female", "Other" });
		JLabel lblmostWanted = new JLabel("MostWanted:");
		mostWantedComboBox = new JComboBox<>(new String[] { "", "Yes", "No" });
		JLabel lblmariedStatus = new JLabel("Marrital Status:");
		mariedStatusComboBox = new JComboBox<>(new String[] { "", "Married", "Single" });
		JLabel lblAge = new JLabel("Age:");
		txtAge = new JTextField();
		JLabel lblAddress = new JLabel("Address:");
		txtAddress = new JTextArea(3, 20);

		JLabel lblDOBName = new JLabel("DOB (yyyy-MM-dd):");
		txtDOB = new JTextField();
		JLabel lblNationality = new JLabel("Nationality:");
		txtNationality = new JTextField();
		JLabel lblOccuption = new JLabel("Occuption:");
		txtOccupatin = new JTextField();
		JLabel lblphone = new JLabel("Phone Number:");
		txtPhoneNumber = new JTextField();

		JScrollPane addressScroll = new JScrollPane(txtAddress);

		btnSave = new JButton("Save");
		btnView = new JButton("View Records");
		btnSearch = new JButton("Search");
		btnUpdate = new JButton("Update");
		btnDelete = new JButton("Delete");
		btnNext = new JButton("Next");

		panel.add(lblFirstName);
		panel.add(txtFirstName);
		panel.add(lblLastName);
		panel.add(txtLastName);
		panel.add(lblfatherName);
		panel.add(txtFatherName);
		panel.add(lblGender);
		panel.add(genderComboBox);
		panel.add(lblAge);
		panel.add(txtAge);
		panel.add(lblAddress);
		panel.add(addressScroll);
		panel.add(lblDOBName);
		panel.add(txtDOB);
		panel.add(lblNationality);
		panel.add(txtNationality);
		panel.add(lblmostWanted);
		panel.add(mostWantedComboBox);
		panel.add(lblmariedStatus);
		panel.add(mariedStatusComboBox);
		panel.add(lblOccuption);
		panel.add(txtOccupatin);
		panel.add(lblphone);
		panel.add(txtPhoneNumber);

		// Add panel to frame
		add(panel, BorderLayout.NORTH);

		// JTable for displaying criminal records
		model = new DefaultTableModel(new String[] { "Criminal Id", "First Name", "Last Name", "FatherName",
				"Mostwanted", "Age", "DOB", "Gender", "Phone Number", "Nationality", "Address", "CreatedAt" }, 0);
		table = new JTable(model);
		JScrollPane tableScroll = new JScrollPane(table);
		add(tableScroll, BorderLayout.CENTER);

		// Add buttons
		JPanel buttonPanel = new JPanel();
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
				saveCriminalRecord();
			}
		});

		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewCriminalRecords();
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchCriminalRecord();
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
				new CrimePage().startCrime();
			}
		});
		
		connection = DatabaseConnection.getConnection();
	}

	// Save criminal record into the database
	private void saveCriminalRecord() {
		try {
			CriminalDAO crimalDAO = new CriminalDAO();
			CriminalDetails crimal = getValuesFromTextFields();
			int criminalId = crimalDAO.saveCriminalDetails(connection, crimal); // insert into db
			List<CriminalDetails> criminals = crimalDAO.searchCriminal(connection, String.valueOf(criminalId));
			printTableData(criminals);
			JOptionPane.showMessageDialog(this, "Criminal record saved!");
			clearData();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error saving record.");
		}
	}
	// View all criminal records
	private void viewCriminalRecords() {
		try {
			CriminalDAO crimalDAO = new CriminalDAO();
			List<CriminalDetails> criminals = crimalDAO.getAllCriminals(connection);
			printTableData(criminals);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching records.");
		}
	}

	// Search criminal record by ID or name
	private void searchCriminalRecord() {
		try {
			String searchTerm = JOptionPane.showInputDialog(this, "Enter Criminal ID or Name:");
			CriminalDAO crimalDAO = new CriminalDAO();
			List<CriminalDetails> criminals = crimalDAO.searchCriminal(connection, searchTerm);
			if (criminals.size() == 0) {
				JOptionPane.showMessageDialog(this, "Crimal Not Found");
			}
			printTableData(criminals);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error searching records.");
		}
	}

	private void deletedCrimalRecord() {
		try {
			String searchTerm = JOptionPane.showInputDialog(this, "Enter Criminal ID");
			CriminalDAO crimalDAO = new CriminalDAO();
			String status = crimalDAO.deleteCrimal(connection, Integer.parseInt(searchTerm));
			if (status.equalsIgnoreCase("not found")) {
				JOptionPane.showMessageDialog(this, "Crimal Not Found");
			} else if (status.equalsIgnoreCase("deleted")) {
				JOptionPane.showMessageDialog(this, "Crimal deleted successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error searching records.");
		}
	}

	private void updateCrimalRecord() {
		try {
			String updateTerm = JOptionPane.showInputDialog(this, "Enter Criminal ID");
			CriminalDAO crimalDAO = new CriminalDAO();
			CriminalDetails crimal = getValuesFromTextFields();
			String status = crimalDAO.updateCriminal(connection, crimal, Integer.parseInt(updateTerm));
			if (status.equalsIgnoreCase("failed")) {
				JOptionPane.showMessageDialog(this, "Crimal Not Found");
			} else if (status.equalsIgnoreCase("success")) {
				JOptionPane.showMessageDialog(this, "Crimal updated successfully");
			}
			clearData();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error updating records.");
		}
	}

	private void printTableData(List<CriminalDetails> criminals) {
		model.setRowCount(0); // Clear previous records
		for (CriminalDetails criminalData : criminals) {
			Object[] row = { criminalData.getCriminalId(), criminalData.getFirstName(), criminalData.getLastName(),
					criminalData.getFatherName(), criminalData.isMostWanted(), criminalData.getAge(),
					criminalData.getDateOfBirth(), criminalData.getGender(), criminalData.getPhoneNumer(),
					criminalData.getNationality(), criminalData.getAddress(), criminalData.getCreatedDateTime() };
			model.addRow(row);
		}
	}
	private void clearData() {
		txtFirstName.setText("");
		txtLastName.setText("");
		txtFatherName.setText("");
		txtAge.setText("");
		mostWantedComboBox.setSelectedItem("");
		genderComboBox.setSelectedItem("");
		mariedStatusComboBox.setSelectedItem("");
		txtDOB.setText("");
		txtOccupatin.setText("");
		txtAddress.setText("");
		txtPhoneNumber.setText("");
		txtNationality.setText("");
	}

	private CriminalDetails getValuesFromTextFields() {
		CriminalDetails crimal = new CriminalDetails();
		crimal.setFirstName(txtFirstName.getText());
		crimal.setLastName(txtLastName.getText());
		crimal.setFatherName(txtFatherName.getText());
		crimal.setAge(Integer.parseInt(txtAge.getText()));
		crimal.setMostWanted(Boolean.parseBoolean((String) mostWantedComboBox.getSelectedItem()));
		crimal.setGender((String) genderComboBox.getSelectedItem());
		crimal.setDateOfBirth(txtDOB.getText());
		crimal.setNationality(txtNationality.getText());
		crimal.setMaritalStatus((String) mariedStatusComboBox.getSelectedItem());
		crimal.setOccupation(txtOccupatin.getText());
		crimal.setPhoneNumer(Long.parseLong(txtPhoneNumber.getText()));
		crimal.setAddress(txtAddress.getText());

		return crimal;
	}
}
