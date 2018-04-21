package gui;

import java.util.ArrayList;
import java.util.Observable;

import chatComponents.Group;
import client.Client;

/**
 * Model class for the GUI
 * @author Dom Parfitt
 * @version 2016-03-10
 *
 */
public class ClientModel extends Observable {
	
	private boolean loggedIn = false;
	private String chatText = "";
	private String errorText = "";
	private boolean validRegister = false;
	private boolean loginView;
	private boolean registerView;
	private boolean contactsView;
	private boolean forgetPasswordView;
//	private ArrayList<String> openChats;
	private ArrayList<Group> openChats;
	private boolean addFriendsView;
	

	private Client client;
	
	/**
	 * Initialises a new instance of the model, which creates a new Client object to use.
	 * This may need to be changed once everything comes together to take in an already
	 * initialised Client object
	 */
	public ClientModel(Client client) {
		this.client = client;
//		openChats = new ArrayList<String>();
		openChats = new ArrayList<Group>();
	}
	
	public String getErrorText() {
		return errorText;
	}
	
	public void setErrorText(String errorText) {
		this.errorText = errorText;
		setChanged();
		notifyObservers();
	}
	
	public Client getClient() {
		return client;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
		setChanged();
		notifyObservers();
	}
	
	public boolean getLoggedIn() {
		return loggedIn;
	}

	public void setChatText(String text) {
		this.chatText = text;
		setChanged();
		notifyObservers();
	}
	
	public String getChatText() {
		return chatText;
	}
	
	public boolean isValidRegister() {
		return validRegister;
	}

	public void setValidRegister(boolean validRegister) {
		this.validRegister = validRegister;
		setChanged();
		notifyObservers();
	}
	
	public boolean isLoginView() {
		return loginView;
	}

	public void setLoginView(boolean loginView) {
		this.loginView = loginView;
		setChanged();
		notifyObservers();
	}

	public boolean isRegisterView() {
		return registerView;
	}

	public void setRegisterView(boolean registerView) {
		this.registerView = registerView;
//		setChanged();
//		notifyObservers();
	}

	public boolean isContactsView() {
		return contactsView;
	}

	public void setContactsView(boolean contactsView) {
		this.contactsView = contactsView;
//		setChanged();
//		notifyObservers();
	}

	public boolean isForgetPasswordView() {
		return forgetPasswordView;
	}

	public void setForgetPasswordView(boolean forgetPasswordView) {
		this.forgetPasswordView = forgetPasswordView;
		setChanged();
		notifyObservers();
	}

	public boolean isAddFriendsView() {
		return addFriendsView;
	}

	public void setAddFriendsView(boolean addFriendsView) {
		this.addFriendsView = addFriendsView;
		setChanged();
		notifyObservers();
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
//	public ArrayList<String> getOpenChats() {
//		return openChats;
//	}
	
	public ArrayList<Group> getOpenChats() {
		return openChats;
	}
	
//	public void addChat(String username) {
//		if (!openChats.contains(username)) {
//			openChats.add(username);
//			setChanged();
//			notifyObservers();
//		}
//	}
	
	public void addChat(Group group) {
		if (!openChats.contains(group)) {
			openChats.add(group);
			setChanged();
			notifyObservers();
		}
	}
	

}
