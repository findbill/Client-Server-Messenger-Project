package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 * Registration View for this online chat program.
 * @author Bingyao Tian
 * @version 16/03/2016
 */
public class RegistrationView extends JFrame implements Observer {

	private JTextField firstnameField = new JTextField();
	private JTextField lastnameField = new JTextField();
	private JTextField usernameField = new JTextField();
	private JTextField passwordField = new JTextField();
	private JTextField email = new JTextField();
	
	private JLabel firstnameLabel = new JLabel("First name");
	private JLabel lastnameLabel = new JLabel("Last name");
	private JLabel usernameLabel = new JLabel("Username");
	private JLabel passwordLabel = new JLabel("Password");
	private JLabel emailLabel = new JLabel("Email");
	
	private JButton submitButton = new JButton("Submit");
	private JButton cancelButton = new JButton("Cancel");
	
	private ClientModel model;
	
	public RegistrationView(final ClientModel model) {

		this.model = model;
		model.addObserver(this);
		model.setRegisterView(true);
		
		setLayout(null);
		add(firstnameField);
		add(lastnameField);
		add(usernameField);
		add(passwordField);
		add(email);
		
		firstnameField.setBounds(120, 50, 300, 30);
		lastnameField.setBounds(120, 100, 300, 30);
		usernameField.setBounds(120, 150, 300, 30);
		passwordField.setBounds(120, 200, 300, 30);
		email.setBounds(120, 250, 300, 30);
		
		add(firstnameLabel);
		add(lastnameLabel);
		add(usernameLabel);
		add(passwordLabel);
		add(emailLabel);
		
		firstnameLabel.setBounds(20, 50, 100, 30);
		lastnameLabel.setBounds(20, 100, 100, 30);
		usernameLabel.setBounds(20, 150, 100, 30);
		passwordLabel.setBounds(20, 200, 100, 30);
		emailLabel.setBounds(20, 250, 100, 30);
		
		add(submitButton);
		submitButton.setBounds(250, 330, 80, 30);
		add(cancelButton);
		cancelButton.setBounds(370, 330, 80, 30);
		
		setTitle("Registration");
		setLocation(500, 400);
		setSize(500, 400);
		setResizable(false);
		setVisible(true);
		
		submitButton.addActionListener(new SubmitListener());
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.setRegisterView(false);
				dispose();
			}
			});
	}
	
	public class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String firstName = firstnameField.getText().trim();
			String lastName = lastnameField.getText().trim();
			String userName = usernameField.getText().trim();
			String password = passwordField.getText();
			String emailAddress = email.getText().trim();
	
			if (firstName.equals("") || lastName.equals("") || userName.equals("") || password.equals("") ||emailAddress.equals("")) {
				JOptionPane.showMessageDialog(null, "Please fill in all boxes.");
			} else {
					model.getClient().registerRequest(firstName, lastName, userName, password, emailAddress);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (model.isRegisterView()) {
			if (model.isValidRegister()) {
				JOptionPane.showMessageDialog(null, "You have successfully registered");
				model.setRegisterView(false);
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, model.getErrorText());
			}
		}
	}
}
