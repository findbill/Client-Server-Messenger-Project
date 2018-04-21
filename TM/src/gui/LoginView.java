package gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Client;
/**
 * Login View for this online chat program.
 * @author Bingyao Tian
 *@version 16/03/2016
 */

public class LoginView extends JFrame implements Observer {

	private JTextField usernameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JButton loginButton = new JButton("Log In");
	private JButton signUpButton = new JButton("Sign Up");
	private JButton forgotPasswordButton = new JButton("Forgot password");
	private JLabel usernameLabel = new JLabel("Username");
	private JLabel passwordLabel = new JLabel("Password");
	private JPanel panel = new JPanel();
	private JLabel messageLabel = new JLabel("error messages go here");
	private JLabel icon;
	private ImageIcon imageIcon;
	private ClientModel model;

	public static void main(String[] args) {
		// String host = args[0];
		// int port = Integer.parseInt(args[1]);
		String host = "localhost";
		int port = 52003;
		new LoginView(new Client(host, port));
	}

	public LoginView(Client client) {

		// Set up model
		this.model = client.getModel();
		model.addObserver(this);
		model.setErrorText("");
		model.setLoginView(true);

		// Set up frame
		setTitle("Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 360);
		setLocation(500, 300);
		setVisible(true);

		add(panel);
		panel.setSize(getMaximumSize());
		panel.setLayout(null);

		panel.add(loginButton);
		loginButton.setBounds(300, 300, 80, 30);
		panel.add(signUpButton);
		signUpButton.setBounds(200, 300, 95, 30);
		panel.add(forgotPasswordButton);
		forgotPasswordButton.setBounds(55, 300, 140, 30);
		panel.add(usernameField);
		usernameField.setBounds(100, 185, 230, 30);
		panel.add(usernameLabel);
		usernameLabel.setBounds(10, 180, 80, 50);
		panel.add(passwordField);
		passwordField.setBounds(100, 235, 230, 30);
		panel.add(passwordLabel);
		passwordLabel.setBounds(10, 230, 80, 50);
		panel.add(messageLabel);
		messageLabel.setBounds(125, 50, 390, 140);
		messageLabel.setForeground(Color.RED);
		
		imageIcon = new ImageIcon(getClass().getResource("icon.png"));
		icon = new JLabel(imageIcon);
		icon.setBounds(1, 1,  398, 178);
		panel.add(icon);
		validate();

		loginButton.addActionListener(new LogInListener());
		signUpButton.addActionListener(new SignUpListener());
		forgotPasswordButton.addActionListener(new ForgotPasswordListener());
	}

	@Override
	public void update(Observable o, Object arg) {
		messageLabel.setText(model.getErrorText());
		if (model.getLoggedIn()) {
			model.setLoggedIn(false);
			ContactsView contactsView = new ContactsView(model);
			dispose();
		}
	}

	public class LogInListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		String username = usernameField.getText();
		String password = passwordField.getText();
		model.getClient().loginRequest(username, password);
		model.setErrorText("Logging in...");
		}
	}
	
	public class SignUpListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new RegistrationView(model);
		}
	}
	
	public class ForgotPasswordListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new ForgetPasswordView(model);
		}
	}

}