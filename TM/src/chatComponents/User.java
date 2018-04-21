package chatComponents;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple class to represent a user
 * @author Dom Parfitt
 * @version 2016-03-08
 *
 */
public class User implements Serializable{
	private static final long serialVersionUID = -8658385818188864367L;
	private String username, firstname, lastname, email;
	private boolean isOnline;
	private ArrayList<User> contacts;
	//Does this need to be changed to be a set of Strings?
//	private Set<Message> sent;
//	private Set<Message> received;
	
	/**
	 * Initialise a new instance of the User class
	 * @param username the username of the User
	 * @param firstname the first name of the User
	 * @param lastname the last name of the User
	 * @param email the email of the User
	 */
	public User(String username, String firstname, String lastname, String email) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.isOnline = true;
	}
	
	/**
	 * Initialise a new instance of the User class
	 * @param username the username of the User
	 * @param firstname the first name of the User
	 * @param lastname the last name of the User
	 * @param email the email of the User
	 * @param contacts an ArrayList holdings the User's that this User has as contacts
	 */
	public User(String username, String firstname, String lastname, String email, ArrayList<User> contacts) {//Needs to implement contacts
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.isOnline = true;
		this.contacts = contacts;
	}
	
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public ArrayList<User> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<User> contacts) {
		this.contacts = contacts;
	}

	public String toString() {
		String contactsString = "";
		for (User contact : contacts) {
			contactsString += contact;
		}
		return "Username: " + username;
	}
}
