package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import chatComponents.Group;

/**
 * Contact View for this online chat program.
 * @author Bingyao Tian
 *@version 16/03/2016
 */
public class ContactsView extends JFrame implements Observer{

	private int n = 50;
	private JPanel contactsPanel = new JPanel();
	private JButton[] contactsButtons = new JButton[n];
	private JButton addFriend = new JButton("Add Friend");
	private JButton logOut = new JButton("Log Out");
	private JButton groupChat = new JButton("Start Group Chat");
	private ClientModel model;
//	private ArrayList<Group> openChats;
	private HashMap<Group, ChatView> openChats;
	private ContactsView self = this;
	
	public ContactsView(final ClientModel model){
		//Set up model
		this.model = model;
		model.addObserver(this);
		model.setContactsView(true);
		
		//Intialise list of open chats
//		openChats = new ArrayList<Group>();
		openChats = new HashMap<Group, ChatView>();
		
		//Set up frame
		setTitle("Contacts");
//		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setMinimumSize(getPreferredSize());
		setSize(400,500);
		setLayout(new BorderLayout());
		
		
		//Set up contacts panel
		contactsPanel.setLayout(new GridLayout(20, 1));	
		JScrollPane scrollPane = new JScrollPane(contactsPanel);
		scrollPane.setSize(new Dimension(300, 750));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Add buttons for each of the User's contacts	
		for (int i = 0; i < model.getClient().getUser().getContacts().size(); i++) {
			String username = model.getClient().getUser().getContacts().get(i).getUsername();
			String firstname = model.getClient().getUser().getContacts().get(i).getFirstname();
			String lastname = model.getClient().getUser().getContacts().get(i).getLastname();
			contactsButtons[i] = new JButton("<html>" + username + " - <i>" + firstname + " " + lastname + "</i></html>");
			contactsButtons[i].setActionCommand(username);
			contactsPanel.add(contactsButtons[i]);
			contactsButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Group user = new Group(e.getActionCommand());
					boolean exists = false;
					for (Group existing : openChats.keySet()) {
						if (existing.equals(user)) {
							exists = true;
							openChats.get(existing).setState(NORMAL);
							openChats.get(existing).toFront();
							break;
						}
					}
					if (!exists) {
//						openChats.add(user);
//						ChatView chatview = new ChatView(model, user, self);
						model.getOpenChats().add(user);
						openChats.put(user, new ChatView(model, user, self));
					} else {
//						openChats.get(user).toFront();
					}
//					openChats.add(e.getActionCommand());
//					ChatView chatview = new ChatView(model, e.getActionCommand(), self);
				}
				});
		}
		
		//Set up bottom panel for add friend and log out buttons
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		bottomPanel.add(addFriend);
		bottomPanel.add(groupChat);
		bottomPanel.add(logOut);
		
		//Add panels to frame
		add(scrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		
		//Add action listeners to bottom buttons
		logOut.addActionListener(new LOListener()); 
		addFriend.addActionListener(new AFListener());
		groupChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new StartGroupChatView(model, self);
				}
			});
		
		//Make the frame visible
		setVisible(true);
	}
			
	public class LOListener implements ActionListener {
		@Override
			public void actionPerformed(ActionEvent e) {
			int	answer = JOptionPane.showConfirmDialog(null, "Are you sure?"); 
			if (answer == JOptionPane.YES_OPTION)  {
					model.getClient().logOutRequest();
					}
			}
	}
	
//	public ArrayList<Group> getOpenChats() {
//		return openChats;
//	}
	public HashMap<Group, ChatView> getOpenChats() {
		return openChats;
	}
	
	public class AFListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			new AddFriendsView(model);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (model.isContactsView()) {
			/*
			 * Loops through any all the open chats that are listed in the model and if there is
			 * not a corresponding open chat held by the ContactsView it opens a new chat. This
			 * means that if a user sends a message to another user it will pop open a window if
			 * one is not already open, alerting them to their new message
			 */
			boolean exists = false;
			for (Group username : model.getOpenChats()) {
				for (Group existing : openChats.keySet()) {
					if (existing.equals(username)) {
						exists = true;
						openChats.get(existing).setState(NORMAL);
						openChats.get(existing).toFront();
						break;
					}
				}
//				if (!openChats.containsKey(username)) {
				if (!exists) {
//					openChats.add(username);
//					ChatView chatview = new ChatView(model, username, this);
					openChats.put(username, new ChatView(model, username, self));
					}
			}
		} else {
			/*
			 * If the model is updated so that the ContactsView flag is false then this
			 * displays some text (a log out message), opens the login view and disposes 
			 * of the contacts view. The contacts view is reset to true to prevent this 
			 * from continuously popping up empty dialog boxes and is reset to false by
			 * the log in view
			 */
			
			JOptionPane.showMessageDialog(null, model.getErrorText());
			model.setContactsView(true);
			LoginView login = new LoginView(model.getClient());
			dispose();
		}
	}
}
