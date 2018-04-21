package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import chatComponents.Group;
import chatComponents.Message;
import chatComponents.User;
import gui.ChatModel;
import gui.ClientModel;

/**
 * This is a basic client class to start with. What about threads? What about
 * serializable?
 * 
 * @author Krisztina Ildiko Nogradi
 * @version 04-03-2016
 */
public class Client {

	private static final long serialVersionUID = 1L;
	private Socket socket;
	private ObjectOutputStream outToServer;
	private ClientThread clientThread = null;
	private int port = 0;
	private String host = null;
	private ClientModel model = null;
//	private HashMap<String, ChatModel> chats;
	private HashMap<Group, ChatModel> chats;
	private User user;
	private boolean connectedToServer;
	
	/**
	 * Initialises a new instance of the Client
	 * @param host the hostname of the server
	 * @param port the port which the Server is listening on
	 */
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
//		chats = new HashMap<String, ChatModel>();
		chats = new HashMap<Group, ChatModel>();
		model = new ClientModel(this);
		
		start();
		
	}
	
//	public ChatModel getChatModel(String username) {
//		return chats.get(username);
//	}
	
	public ChatModel getChatModel(Group username) {
		return chats.get(username);
	}
	
//	public void addChat(String username) {
//		if (!chats.containsKey(username)) {
//			chats.put(username, new ChatModel());
//			model.addChat(username);
//		}
//	}
	
	public void addChat(Group group) {
		boolean exists = false;
		for (Group existing : chats.keySet()) {
			if (existing.equals(group)) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			chats.put(group, new ChatModel());
			model.addChat(group);
		}
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public ClientModel getModel() {
		return model;
	}

	/**
	 * What should this method do? How far do the responsibilities of the client
	 * class extend?
	 * 
	 * @param message
	 */
	public void receiveMessage(Message message) {

		int type = message.getType();


	    if (type == 0) {
	    	model.setValidRegister(true);
		} else if (type == 1) {
			setUser(message.getUser());
			model.setLoggedIn(true);
			model.setErrorText(message.getText()); //I don't think this will ever get displayed but not sure
			
		} else if (type == 2) {
			model.setLoggedIn(true);
		} else if (type == 3) {		
			String text = message.getText();
//			System.out.println("Received chat message from: " + message.getFrom());
			Group group = new Group();
			group.addMember(message.getFrom());
			for (String member : message.getDestination().getMembers()) {
				if (!member.equals(user.getUsername())) {
					group.addMember(member);
				}
			}
//			addChat(message.getFrom());
			addChat(group);
//			chats.get(message.getFrom()).setChatMessage(message.getFrom(), text);
			for (Group existing : chats.keySet()) {
				if (existing.equals(group)) {
					chats.get(existing).setChatMessage(message.getFrom(), text);
				}
			}
//			chats.get(group).setChatMessage(message.getFrom(), text);
		} else if (type == 4) {
			model.setContactsView(false);
			model.setErrorText(message.getText());
			model.setLoggedIn(false);	
			stop();
		} else if (type == 5) {
			model.setErrorText(message.getText());
		} else if (type == 7) {
			// chat history
		}
	}

	/**
	 * A method to send a user registration request to the server	
	 * @param firstname the first name of the user
	 * @param lastname the last name of the user
	 * @param username the requested username for the user
	 * @param password the password for the user
	 * @param email the user's email address
	 */
	public void registerRequest(String firstname, String lastname, String username, String password, String email) {
		if (!connectedToServer) {
			start();
		}
		int type = 0;
		java.util.Date date = new java.util.Date();
		long currentTime = date.getTime();
		String encryptedPassword = passwordEncryption(password);
		String userPass = firstname + "," + lastname + "," + username + "," + encryptedPassword + "," + email;

		Message message = new Message(type, currentTime, userPass);
		try {
			outToServer.writeObject(message);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This is a method to request login from the server.
	 * @param username is the name received from the user
	 * @param password is the password received from the user
	 */
	public void loginRequest(String username, String password) {
		if (!connectedToServer) {
			start();
		}
		int type = 1;
		java.util.Date date = new java.util.Date();
		long currentTime = date.getTime();
		String encryptedPassword = passwordEncryption(password);
		String userPass = username + "," + encryptedPassword;

		Message message = new Message(type, currentTime, userPass);
		try {
			outToServer.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param username
	 * @param emailAddress
	 */
	public void forgotPassword(String username, String emailAddress) {
		if (!connectedToServer) {
			start();
		}
		int type = 2;
		java.util.Date date = new java.util.Date();
		long currentTime = date.getTime();

		String userAddress = username + "," + emailAddress;

		Message message = new Message(type, currentTime, userAddress);
		try {
			outToServer.writeObject(message);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * A method to send a chat message from the user to the server
	 * @param username the username of the user that the message is intended for
	 * @param text the text of the message being sent
	 */
//	public void chatRequest(String username, String text) {
	public void chatRequest(Group username, String text) {

		int type = 3;
		java.util.Date date = new java.util.Date();
		long currentTime = date.getTime();
//		String destination = username;
		Group destination = username;
		System.out.println("Sending chat request: " + user.getUsername());
		Message message = new Message(type, currentTime, destination, user.getUsername(), text);
		try {
			outToServer.writeObject(message);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * A method to process a log out request from the user
	 */
	public void logOutRequest() {

		int type = 4;
		java.util.Date date = new java.util.Date();
		long currentTime = date.getTime();
		String text = ",";

		Message message = new Message(type, currentTime, text);
		try {
			outToServer.writeObject(message);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void addContactRequest(String username) {
		
		int type = 6;
		java.util.Date date = new java.util.Date();
		long currentTime = date.getTime();
		
		Message message = new Message(type, currentTime, username);
		try {
			outToServer.writeObject(message);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
//	public void chatHistoryRequest(String username) {
	public void chatHistoryRequest(Group username) {
		
		int type = 7;
		java.util.Date date = new java.util.Date();
		long currentTime = date.getTime();
//		String destination = username;
		Group destination = username;
		String text = ",";

		Message message = new Message(type, currentTime, destination, user.getUsername(), text);
		try {
			outToServer.writeObject(message);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void start() {
		try {
			socket = new Socket(this.host, this.port);
			outToServer = new ObjectOutputStream(socket.getOutputStream());
			clientThread = new ClientThread(this, socket);
			clientThread.start();
			connectedToServer = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			socket.close();
			connectedToServer = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String passwordEncryption(String password) {
		String encryptedPassword = "";
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		messageDigest.update(password.getBytes());

		byte[] byteData = messageDigest.digest();
		java.math.BigInteger numberRep = new java.math.BigInteger(1, byteData);
		encryptedPassword = String.valueOf(numberRep);

		return encryptedPassword;
	}

}
