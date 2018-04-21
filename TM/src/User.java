import java.util.HashSet;
import java.util.Set;

/**
 * A simple class to represent a user
 * @author Dom Parfitt
 * @version 2016-03-02
 *
 */
public class User {
	private static final int OFFLINE = 0;
	private static final int ONLINE = 1;
	
	private String username;
	private int status;
	private Set<User> contacts;
	//Haven't yet implemented the Message class
//	private Set<Message> set;
//	private Set<Message> received;
	
	public User(String username) {
		this.username = username;
		this.status = ONLINE;
		this.contacts = new HashSet<User>();
	}
	
	
	public static int getOffline() {
		return OFFLINE;
	}


	public static int getOnline() {
		return ONLINE;
	}


	public String getUsername() {
		return username;
	}


	public int getStatus() {
		return status;
	}


	public Set<User> getContacts() {
		return contacts;
	}


	public String toString() {
		String contactsString = "";
		for (User contact : contacts) {
			contactsString += contact;
		}
		return "Username: " + username + " Contacts: " + contactsString;
	}
}
