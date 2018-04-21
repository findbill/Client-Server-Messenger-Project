package gui;

import java.util.Observable;

/**
 * A class for passing messages from one user to another via the GUI
 * @author Dom Parfitt
 * @version 2016-03-15
 *
 */
public class ChatModel extends Observable {
	
	private String from;
	private String chatText;
	private String[] chatHistory;
	
	public ChatModel() {
		chatText = "";
	}
	
	public String getChatText() {
		return chatText;
	}
	
	public void setChatText(String chatText) {
		this.chatText = chatText;
		setChanged();
		notifyObservers();
	}
	
	public String[] getChatHistory() {
		return chatHistory;
	}
	
	public void setChatHistory(String[] chatHistory) {
		this.chatHistory = chatHistory;
		setChanged();
		notifyObservers();
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setChatMessage(String from, String chatText) {
		this.from = from;
		this.chatText = chatText;
		setChanged();
		notifyObservers();
	}
	
	
	

}
