package com.app.crms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.app.crms.utils.DatabaseConnection;

public class LoginPage extends JFrame {

			// Declare components
			private JTextField userName;
			private JPasswordField password;
			private JButton btnLogin;
			private Connection connection = null;
			public void startLogin() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new LoginPage().setVisible(true);
					}
				});
			}
		
		  public LoginPage() {
			  		connection = DatabaseConnection.getConnection();
					// JFrame settings
					setTitle("Criminal Record Management System | Login Page");
					setSize(600, 400);
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					setLocationRelativeTo(null);
					setLayout(new BorderLayout());
			
					// Initialize components
					JPanel panel = new JPanel();
					//panel.setLayout(new GridLayout(6, 2));
					panel.setLayout(new FlowLayout());

					JLabel lblUserName = new JLabel("User Name:");
					lblUserName.setPreferredSize(new Dimension(300, 60));
					userName = new JTextField();
					userName.setPreferredSize(new Dimension(300, 40));
					JLabel lblPassword = new JLabel("Password:");
					lblPassword.setPreferredSize(new Dimension(300, 60));
					password = new JPasswordField();
					password.setPreferredSize(new Dimension(300, 40));

					btnLogin = new JButton("Login");
					btnLogin.setPreferredSize(new Dimension(300, 40));
					
					panel.add(lblUserName);
					panel.add(userName);
					panel.add(lblPassword);
					panel.add(password);
					// Add panel to frame
					add(panel, BorderLayout.CENTER);
					// Add buttons
					JPanel buttonPanel = new JPanel();
					buttonPanel.add(btnLogin);
					
					add(buttonPanel, BorderLayout.SOUTH);
					
					setResizable(false);
					
					// Add event listeners
					btnLogin.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String status = verifyUser();
							if(status.equalsIgnoreCase("Success"))
							new CriminalPage().startCriminal();
						}
					});

		}
		 private String verifyUser() {
			 String status = "failed";
				try {
					String user = userName.getText();
					String pass = password.getText();
					
					if(user.equalsIgnoreCase(CRMSApplication.userName) && pass.equals(CRMSApplication.password)) {
						JOptionPane.showMessageDialog(this, "Login Successfully");
						status ="Success";
					}else {
						JOptionPane.showMessageDialog(this, "Invalid UserName/Password");
					}
				}catch (Exception e) {
					System.out.println("Exception occurs "+e);
				}
				return status;
			}
}
