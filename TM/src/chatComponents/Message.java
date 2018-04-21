package chatComponents;
import java.io.Serializable;
import java.util.Date;

/**
 * A class for messages sent between various components of the system
 * @author Dom Parfitt
 * @version 2016-03-06
 */
public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -739099039721066842L;
	private static final int REGISTER = 0; 
	private static final int LOGIN = 1;
	private static final int FORGOTPASSWORD = 2;
	private static final int CHAT = 3;
	private static final int LOGOUT = 4;
	private static final int ERROR = 5;
	private static final int ADDCONTACT = 6;
	private static final int CHATHISTORY = 7;
	
	private int type;
	private long timeStamp;
//	private String destination;
	private Group destination;
	private String from;
	private String text;
	private User user;
	
	/**
	 * Initialises a new instance of Message with a type, time stamp, destination user and message
	 * @param type the type of Message that is being sent
	 * @param timeStamp the time of the message
	 * @param destination the user that the message is being sent to
	 * @param text the text component of the message
	 */
//	public Message(int type, long timeStamp, String destination, String from, String text) {
	public Message(int type, long timeStamp, Group destination, String from, String text) {
		this.type = type;
		this.timeStamp = timeStamp;
		this.destination = destination;
		this.from = from;
		this.text = text;
	}
	
	/**
	 * Initialises a new instance of Message with a type, time stamp and message
	 * @param type the type of Message that is being sent
	 * @param timeStamp the time of the message
	 * @param text the text component of the message
	 */
	public Message(int type, long timeStamp, String text) {
		this.type = type;
		this.timeStamp = timeStamp;
		this.text = text;
	}
	
	/**
	 * Initialises a new instance of Message with a type, time stamp and message
	 * @param type the type of Message that is being sent
	 * @param timeStamp the time of the message
	 * @param text the text component of the message
	 */
	public Message(int type, long timeStamp, User user, String text) {
		this.type = type;
		this.timeStamp = timeStamp;
		this.text = text;
		this.user = user;
	}
	
	/**
	 * Getter method for the type of the Message
	 * @return the type of the Message
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Getter method for the time of the Message
	 * @return the time stamp on the Message
	 */
	public long getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Method for getting the timeStamp formatted as a Date
	 * @return a formatted version of the time stamp
	 */
	public Date getTimeStampAsDate() {
		return new Date(getTimeStamp());
	}
	
	/**
	 * Method for getting the destination (user) of the message
	 * @return the user the message is intended for
	 */
//	public String getDestination() {
//		return destination;
//	}
	
	public Group getDestination() {
		return destination;
	}
	
	/**
	 * Getter method for the text component of the message
	 * @return the text component of the message
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Getter method for the User held by the message
	 * @return the User object held by the message
	 */
	public User getUser() {
		return user;
	}
	
	public String getFrom() {
		return from;
	}
	
	/**
	 * Method to convert the Message object into a String representation
	 * @return a String representation of the Message
	 */
	public String toString() {
		return "Message type: " + getType() + " || Time Stamp: " + getTimeStampAsDate() + " || Text: " + getText();
	}

}
