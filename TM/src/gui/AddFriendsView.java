package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *  AddFriendView for that online chat program.
 * @author Bingyao Tian
 * @version 16/03/2016
 */
public class AddFriendsView extends JFrame implements Observer{

	private JTextField ID = new JTextField();
	private JLabel label1 = new JLabel("ID");
	private JButton button = new JButton("Add");
	private JPanel panel = new JPanel();
	private ClientModel model;
	
	public AddFriendsView(ClientModel model) {
		this.model = model;
		
		add(panel);
	
		panel.setSize(getMaximumSize());
		panel.setLayout(null);	

		panel.add(button);
		button.setBounds(280, 100, 80, 30);
		panel.add(ID);
		ID.setBounds(100, 50, 230, 30);
		panel.add(label1);
		label1.setBounds(20, 40, 50, 50);
			
		button.addActionListener(new ButtonListener());
		
		setTitle("Add contact");
		setResizable(false);
		setSize(400,160);
		setLocation(500, 300);
		setVisible(true);	
	}
	public class ButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				String username = ID.getText().trim();
				model.getClient().addContactRequest(username);
				dispose();
			}
	}
	@Override
	public void update(Observable o, Object arg) {
		
	}
}

