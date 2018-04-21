package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chatComponents.Group;

/**
 * ChatView for this online chat program.
 * @author Bingyao Tian
 * @version 16/03/2016
 */
public class ChatView extends JFrame implements Observer {

	private JTextField tfText = new JTextField();
	private JTextArea taContent = new JTextArea();
	private JScrollPane taPane = new JScrollPane(taContent);
	private JButton sButton = new JButton("Send");
	private JScrollPane contactPane = new JScrollPane();
//	private DefaultListModel<Group> model1 = new DefaultListModel<>();
//	private JList<Group> list = new JList<>( model1);
	private DefaultListModel<String> model1 = new DefaultListModel<>();
	private JList<String> list = new JList<>( model1);
	
	private ClientModel model;
	private ChatModel chat;
	private Group username;
	private ContactsView parent;

	public ChatView(ClientModel model, Group username, ContactsView parent) {
		
		//Sets the ContactsView that initialised this chat
		this.parent = parent;
		
		//Sets the username of this chat
		this.username = username;
		
		//Sets up the Client model
		this.model = model;
		model.addObserver(this);
		model.getClient().addChat(username);
		
		//Sets up ChatModel
		chat = model.getClient().getChatModel(username);
		chat.addObserver(this);

//		setLayout(null);
		
		//Set up menu
		setLayout(new BorderLayout());
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem addFriend = new JMenuItem("Add Friend to Chat");
		JMenuItem close = new JMenuItem("Close");
		file.add(addFriend);
		file.add(close);
		menu.add(file);
		JMenu edit = new JMenu("Edit");
		
		menu.add(edit);
		
		//Set up chat pane
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setPreferredSize(new Dimension(600, 500));
		contentPanel.add(taPane);
		contentPanel.add(tfText);
		contentPanel.add(sButton);
		contentPanel.add(contactPane);
		
		taPane.setBounds(1, 1, 500, 470);
		tfText.setBounds(1, 471, 500, 29);
		sButton.setBounds(501, 471, 98, 29);
		contactPane.setBounds(501, 1, 98, 469);
		
		for (String member : getUsername().getMembers()) {
			model1.addElement(member);
		}

		contactPane.add(list);
		contactPane.setViewportView(list);
		
		tfText.addActionListener(new TFListener());
		sButton.addActionListener(new SBListener());
		
		setTitle(username.toString());
//		setLocation(400, 300);
//		this.setSize(617, 542);

		taContent.setLineWrap(true);
		taContent.setEditable(false);
		
		//Add components to frame
		add(menu, BorderLayout.PAGE_START);
		add(contentPanel, BorderLayout.CENTER);
		
		setResizable(false);	
		pack();
		setVisible(true);
	}

	/**
	 * Gets the username of the chat
	 * @return the username of the user who this chat relates to
	 */
	public Group getUsername() {
		return username;
	}

	/**
	 * Closes the chat window and removes and references to it in 
	 * the ContactsView and Client model
	 */
	public void closeChat() {
		// Remove from openChats in ContactsView
		parent.getOpenChats().remove(username);
		// Remove from openChats in ClientModel
		model.getOpenChats().remove(username);
		dispose();
	}

	@Override
	public void update(Observable o, Object arg) {
		/*
		 * Checks where the update came from. If it is from the ClientModel
		 * then it will be related to the ContactsView being closed or the 
		 * user logging out, so this is checked and the ChatView is closed
		 * if necessary. 
		 * 
		 * If it is from the the ChatModel it will be a new message and so
		 * the displayed text is updated with the username of the sent
		 * message and the chat text
		 */
		if (o.equals(model)) {
			if (!model.isContactsView()) {
				closeChat();
			}
		} else if (o.equals(chat)) {
			//This will not work if we implement group chats so may have to be updated
			taContent.append(chat.getFrom() + ": " + chat.getChatText() + "\n");
		}	
	}

	private class TFListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String s = tfText.getText().trim();
			String myUsername = model.getClient().getUser().getUsername();
			if(s.equals("")) {
				JOptionPane.showMessageDialog(null,
					    "You can not send empty messages!",
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			} else{
				model.getClient().chatRequest(username, s);
				taContent.append(myUsername + ": " + s + "\n");
				tfText.setText("");
			}
		}
	}
	
	private class SBListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String s = tfText.getText().trim();
			String myUsername = model.getClient().getUser().getUsername();
			if(s.equals("")) {
				JOptionPane.showMessageDialog(null,
					    "You can not send empty messages!",
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			} else{
				model.getClient().chatRequest(username, s);
				taContent.append(myUsername + ": " + s + "\n");
				tfText.setText("");
			}
		}
	}
}
