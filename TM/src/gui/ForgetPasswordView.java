package gui;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.management.modelmbean.ModelMBean;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.omg.CORBA.PUBLIC_MEMBER;
/**
 * ForgetPassword View for this online chat program.
 * @author Bingyao Tian
 *@version 16/03/2016
 */

public class ForgetPasswordView extends JFrame implements Observer {
	private JTextField usernameField = new JTextField();
	private JTextField emailField = new JTextField();
	private JLabel usernameLabel = new JLabel("Username");
	private JLabel emailLabel = new JLabel("Email address");
	private JButton submitButton = new JButton("Submit");
	private JButton cancelButton = new JButton("Cancel");
	
	private ClientModel model;
	
	public ForgetPasswordView (final ClientModel model) {
		this.model = model;
		model.addObserver(this);
		model.setForgetPasswordView(true);
		setLayout(null);
		add(usernameField);
		usernameField.setBounds(100, 100, 230, 30);
		add(emailField);
		emailField.setBounds(100, 180, 230, 30);
		add(usernameLabel);
		usernameLabel.setBounds(10, 100, 70, 30);
		add(emailLabel);
		emailLabel.setBounds(10, 180, 90, 30);
		add(submitButton);
		submitButton.setBounds(180, 300, 80, 30);
		add(cancelButton);
		cancelButton.setBounds(280, 300, 80, 30);
		
		
		setTitle("Forgotten password");
		setResizable(false);
		setSize(400,360);
		setLocation(500, 300);
		setVisible(true);
		
		submitButton.addActionListener(new SubmitListener());
		cancelButton.addActionListener(new CancelListener());
	}
	
	public class CancelListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setForgetPasswordView(false);
				dispose();
			}
			}

		public class SubmitListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				String s1 = usernameField.getText();
				String s2 = emailField.getText();
				if (s1 == "" || s2 == "") {
					JOptionPane.showMessageDialog(null, "Please enter both a username and an email");
				} else {
					model.getClient().forgotPassword(s1, s2);
				}
			}
		}
		
		@Override
		public void update(Observable o, Object arg) {
			if (model.isForgetPasswordView()) {
				}
		}
}

