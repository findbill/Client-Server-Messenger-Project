package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import chatComponents.Group;
import chatComponents.User;

public class StartGroupChatView extends JFrame {
	
	private ClientModel model;
	private JPanel contactsPanel, buttonPanel;
	private JButton confirmButton, cancelButton;
	private JScrollPane scroll;
	private ArrayList<JCheckBox> contactsBoxes;
	private ContactsView parent;
	
	public StartGroupChatView(ClientModel model, ContactsView parent) {
		this.model = model;
		this.parent = parent;
		
		contactsBoxes = new ArrayList<JCheckBox>();
		
		//Set up frame
		setTitle("Start Group Chat");
//		setResizable(false);
		setSize(150,300);
		setLayout(new BorderLayout());
		
		
		//Set up contacts panel
		contactsPanel = new JPanel();
//		contactsPanel.setLayout(new GridLayout(model.getClient().getUser().getContacts().size(), 1));
		contactsPanel.setLayout(new BoxLayout(contactsPanel,BoxLayout.Y_AXIS));
		scroll = new JScrollPane(contactsPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Set up check boxes
		for (User contact : model.getClient().getUser().getContacts()) {
			JCheckBox c = new JCheckBox(contact.getUsername());
			c.setSelected(false);
			contactsBoxes.add(c);
			contactsPanel.add(c);
		}
		
		//Set up buttons
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		confirmButton = new JButton("Confirm");
		//Add actionlistener
		confirmButton.addActionListener(new ConfirmListener());
		cancelButton = new JButton("Cancel");
		//Add actionlistener
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		
		//Add components to frame
		add(contactsPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);
		
		//Make the frame visible
		setVisible(true);
	}
	
	private class ConfirmListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Group group = new Group();
			for (JCheckBox contact : contactsBoxes) {
				if (contact.isSelected()) {
					group.addMember(contact.getText());
					System.out.println("Contact: " +contact.getText());
				}
			}
			System.out.println("Group: " + group);
			model.getOpenChats().add(group);
			parent.getOpenChats().put(group, new ChatView(model, group, parent));
//			ChatView chat = new ChatView(model, group, parent);
			dispose();
			
		}
		
	}
}
