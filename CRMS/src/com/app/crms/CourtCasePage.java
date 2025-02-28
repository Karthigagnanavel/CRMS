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
import com.app.crms.dao.CourtCaseDAO;
import com.app.crms.dao.CrimeDAO;
import com.app.crms.utils.DatabaseConnection;

public class CourtCasePage extends JFrame{
	// Declare components
				private JTextField courtName, judgeName , hearing_date;
				private JComboBox<String> crimeIdComboBox;
				private JComboBox<String> statusComboBox;
				private JButton btnSave, btnView, btnSearch, btnUpdate, btnDelete,btnNext,btnBack;
				private JTable table;
				private DefaultTableModel model;

				// Database connection variables
				private Connection connection;
				
				public void startCase() {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							new CourtCasePage().setVisible(true);
						}
					});
				}
			
			  public CourtCasePage() {
				  		connection = DatabaseConnection.getConnection();
						// JFrame settings
						setTitle("Criminal Record Management System | Court Case Page");
						setSize(1000, 800);
						setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						setLocationRelativeTo(null);
						setLayout(new BorderLayout());
				
						// Initialize components
						JPanel panel = new JPanel();
						panel.setLayout(new GridLayout(8, 2));

						JLabel lblCourtName = new JLabel("Court Name:");
						courtName = new JTextField();
						JLabel lblJudgeName = new JLabel("Judge Name:");
						judgeName = new JTextField();
						JLabel lblHearingDate = new JLabel("Hearing Date (yyyy-MM-dd):");
						hearing_date = new JTextField();
						JLabel lblCrimeId = new JLabel("Crime Id:");
						crimeIdComboBox = new JComboBox<>(new CrimeDAO().getCrimeIds(connection));
						JLabel lblmariedStatus = new JLabel("Status:");
						statusComboBox = new JComboBox<>(new String[] { "", "Ongoing", "Convicted","Acquitted","Closed" });

						btnBack = new JButton("Back");
						btnSave = new JButton("Save");
						btnView = new JButton("View Records");
						btnSearch = new JButton("Search");
						btnUpdate = new JButton("Update");
						btnDelete = new JButton("Delete");
						btnNext = new JButton("Next");

						panel.add(lblCourtName);
						panel.add(courtName);
						panel.add(lblJudgeName);
						panel.add(judgeName);
						panel.add(lblHearingDate);
						panel.add(hearing_date);
						panel.add(lblCrimeId);
						panel.add(crimeIdComboBox);
						panel.add(lblCrimeId);
						panel.add(crimeIdComboBox);
						panel.add(lblmariedStatus);
						panel.add(statusComboBox);
						// Add panel to frame
						add(panel, BorderLayout.NORTH);

						// JTable for displaying Crime records
						model = new DefaultTableModel(new String[] { "Case Id","Crime Id", "Court Name", "Judge Name", "Hearing Date",
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
								saveCasetRecord();
							}
							
						});

						btnView.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								viewCaseRecords();
							}
						});

						btnSearch.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								searchCaseRecord();
							}

						});

						btnDelete.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								deletedCaseRecord();
							}
						});

						btnUpdate.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								updateCaseRecord();
							}
						});
						
						btnNext.addActionListener(new  ActionListener() {
							public void actionPerformed(ActionEvent e) {
								new EvidencePage().startEvidence();
							}
						});
						btnBack.addActionListener(new  ActionListener() {
							public void actionPerformed(ActionEvent e) {
								new ArrestPage().startArrest();
							}
						});
			}
			private void saveCasetRecord() {
				try {
					CourtCaseDAO courtCaseDAO = new CourtCaseDAO();
					CourtCaseDetails courtCase = getValuesFromTextFields();
					int criminalId = courtCaseDAO.saveCourtCaseDetails(connection, courtCase); // insert into db
					List<CourtCaseDetails> courtCases = courtCaseDAO.searchCourtCase(connection, String.valueOf(criminalId));
					printTableData(courtCases);
					JOptionPane.showMessageDialog(this, "Case record saved!");
					clearData();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error saving record.");
				}
				
			}
			private void viewCaseRecords() {
				try {
					CourtCaseDAO courtCaseDAO = new CourtCaseDAO();
					List<CourtCaseDetails> cases = courtCaseDAO.getAllCourtCase(connection);
					printTableData(cases);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error fetching records.");
				}
				
			}
			private void searchCaseRecord() {
				try {
					String searchTerm = JOptionPane.showInputDialog(this, "Enter Case ID or Court Name:");
					CourtCaseDAO courtCaseDAO = new CourtCaseDAO();
					List<CourtCaseDetails> cases = courtCaseDAO.searchCourtCase(connection, searchTerm);
					if (cases.size() == 0) {
						JOptionPane.showMessageDialog(this, "Case Not Found");
					}
					printTableData(cases);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error searching records.");
				}
				
			}
			private void deletedCaseRecord() {
				try {
					String searchTerm = JOptionPane.showInputDialog(this, "Enter Case ID");
					CourtCaseDAO courtCaseDAO = new CourtCaseDAO();
					String status = courtCaseDAO.deleteCourtCase(connection, Integer.parseInt(searchTerm));
					if (status.equalsIgnoreCase("not found")) {
						JOptionPane.showMessageDialog(this, "Case Not Found");
					} else if (status.equalsIgnoreCase("deleted")) {
						JOptionPane.showMessageDialog(this, "Case deleted successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error deleting records.");
				}
				
			}
			private void updateCaseRecord() {
				try {
					String updateTerm = JOptionPane.showInputDialog(this, "Enter Case ID");
					CourtCaseDAO courtCaseDAO = new CourtCaseDAO();
					CourtCaseDetails arrest = getValuesFromTextFields();
					String status = courtCaseDAO.updateCourtCase(connection, arrest, Integer.parseInt(updateTerm));
					if (status.equalsIgnoreCase("failed")) {
						JOptionPane.showMessageDialog(this, "Case Not Found");
					} else if (status.equalsIgnoreCase("success")) {
						JOptionPane.showMessageDialog(this, "Case updated successfully");
					}
					clearData();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error updating records.");
				}
				
			}
			private void printTableData(List<CourtCaseDetails> courtCases) {
				model.setRowCount(0); // Clear previous records
				for (CourtCaseDetails cases : courtCases) {
					Object[] row = { cases.getCaseId(), cases.getCrimeId(),cases.getCourtName(), cases.getJudgeName(), cases.getHearingDate()
						, cases.getStatus()};
					model.addRow(row);
				}
			}
			private void clearData() {
				courtName.setText("");
				judgeName.setText("");
				hearing_date.setText("");
				statusComboBox.setSelectedItem("");
				crimeIdComboBox.setSelectedItem("");
			}

			private CourtCaseDetails getValuesFromTextFields() {
				CourtCaseDetails courtCase = new CourtCaseDetails();
				courtCase.setCrimeId(Integer.parseInt((String)crimeIdComboBox.getSelectedItem()));
				courtCase.setCourtName(courtName.getText());
				courtCase.setJudgeName(judgeName.getText());
				courtCase.setHearingDate(hearing_date.getText());
				courtCase.setStatus((String)statusComboBox.getSelectedItem());
				return courtCase;
			}
}
