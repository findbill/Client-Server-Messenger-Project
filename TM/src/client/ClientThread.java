package client;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import chatComponents.Message;

/**
 * This is a simple thread class to assist the Client class.
 * @author Krisztina Ildiko Nogradi
 * @version 04-03-2016
 */
public class ClientThread extends Thread {
	
	private Socket socket = null;
	private Client client = null;
	private ObjectInputStream streamIn = null;
	
	public ClientThread(Client client, Socket socket) {
		this.client = client;
		this.socket = socket;
	}
	
	public void open() {
		try {
			streamIn = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException ioe) {
			System.out.println("Error getting input stream: " + ioe);
		}
	}

	public void close() {
		try {
			if (streamIn != null) {
				streamIn.close();
			}

		} catch (IOException ioe) {
			System.out.println("Error closing input stream: " + ioe);
		}
	}
	
	public void run() {
		
		open();
		boolean connected = true;;
		while (connected) {
			try {
				Message inMessage = (Message) streamIn.readObject();
				client.receiveMessage(inMessage);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EOFException e) {
				
			} catch (SocketException e) {
				System.out.println("The server is not currently available");
				connected = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
