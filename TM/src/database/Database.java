package database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.postgresql.util.PSQLException;

/**
 * Class to represent the database holding the data for our chat system.
 * @author nar779
 *
 */
public class Database implements DatabaseInterface
{
	private Connection connection;
	private PreparedStatement preparedStatement;
	private Statement statement;

	/**
	 * THis constructor opens the connection to the database and sets the schema.
	 * @param schema The schema of the database.
	 */
	public Database(String schema)
	{
		open();
		 
		try 
		{ 
			Statement statement = connection.createStatement();
			statement.execute("set search_path to '" + schema + "'"); 
			statement.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		} 

	}
	
	/**
	 * Method to return the number of rows in a table. Primarily in order to add a new
	 * contact with a valid id number.
	 * @param tableName The table to be queried.
	 * @return The number of rows in the table.
	 */
	private int getCount(String tableName)
	{
		int count=0;
		Statement statement;
		String query = "SELECT COUNT(*) FROM " + tableName;
		System.out.println("executing: " + query);
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			while (rs.next())
			{
				count = rs.getInt("count");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * Method to add a new sent message to the Messages table. 
	 * @param messageFrom The userId of the message sender.
	 * @param messageTo The userId of the message receiver.
	 * @param message The message itself.
	 */
	@Override
	public void insertRowMessages(int messageFrom, int messageTo, String message)
	{
		preparedStatement = null;
		String query = "INSERT INTO messages VALUES (?, ?, ?, ?, ?, ?)";
		
		try 
		{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, getCount("messages") + 1);
			preparedStatement.setTimestamp(2, getCurrentTimeStamp());
			preparedStatement.setInt(3, messageFrom);
			preparedStatement.setInt(4,  messageTo);
			preparedStatement.setString(5, message);
			preparedStatement.setBoolean(6, false);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to get the current time.
	 * @return The current time.
	 */
	public static java.sql.Timestamp getCurrentTimeStamp() 
	{
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Timestamp(today.getTime());
	}
	

	
	/**
	 * Method to add a contact to another users list of contacts, i.e. add new friend.
	 * @param username The username of the first user (maybe the one who sends the send request?).
	 * @param newFriend The username of the second user.
	 */
	@Override
	public void insertRowUserContacts(String username, String newFriend)
	{
		preparedStatement = null;
		String query = "INSERT INTO usercontacts (uid, contactid) VALUES (?, ?), (?,?);";

		int userIdPerson = getUserId(username);
		int userIdNewFriend = getUserId(newFriend);
		
		try 
		{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userIdPerson);
			preparedStatement.setInt(2, userIdNewFriend);
			preparedStatement.setInt(3, userIdNewFriend);
			preparedStatement.setInt(4, userIdPerson);
			
			preparedStatement.executeUpdate();
			System.out.println("Record is inserted into usercontacts table!");

		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Method to get the last name of a user.
	 * @param username The username of the user.
	 * @return The last name of the user.
	 */
	@Override
	public String getLastname(String username) 
	{
		String lastname = null;
		preparedStatement = null;
		String query = "SELECT lastname FROM users WHERE (username = ?)";

		try 
		{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) 
			{
				lastname = rs.getString("lastname");				
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		} 
		return lastname;
	}
	
	/**
	 * Method to get the first name of a user.
	 * @param username The username of the user.
	 * @return The first name of the user.
	 */
	@Override
	public String getFirstname(String username) 
	{
		String firstname = null;
		preparedStatement = null;
		String query = "SELECT firstname FROM users WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) 
			{
				firstname = rs.getString("firstname");				
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		} 
		return firstname;
	}
	
	/**
	 * Method to get the password of the user.
	 * @param username The username of the user.
	 * @return The password of the user.
	 */
	@Override
	public String getPassword(String username) 
	{
		String password = null;
		preparedStatement = null;
		String query = "SELECT password FROM users WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) 
			{
				password = rs.getString("password");				
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		} 
		return password;
	}
	
	/**
	 * Method to get the status of the user.
	 * @param username The username of the user.
	 * @return The status of the user.
	 */
	@Override
	public String getStatus(String username) 
	{
		String status = null;
		preparedStatement = null;
		String query = "SELECT status FROM cusers WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				status = rs.getString("status");				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return status;
	}
	
	/**
	 * Method to get the email address of the user.
	 * @param username The username of the user.
	 * @return The email address of the user.
	 */
	@Override
	public String getEmail(String username) 
	{
		String email = null;
		preparedStatement = null;
		String query = "SELECT email FROM users WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) 
			{
				email = rs.getString("email");				
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
		return email;
	}
	
	/**
	 * Method to determine whether a user is online or not.
	 * @param username The username of the user.
	 * @return A boolean, true if they are online, false otherwise.
	 */
	@Override
	public boolean getIsOnline(String username)
	{
		boolean isOnline = false;
		preparedStatement = null;
		String query = "SELECT isonline FROM users WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) 
			{
				isOnline = rs.getBoolean("isOnline");				
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
		return isOnline;
	}
	
	/**
	 * Method to set the online status of a user.
	 * @param username The user who's online status is being changed.
	 * @param onlineStatus The online status to be set.
	 */
	@Override
	public void setIsOnline(String username, boolean onlineStatus) 
	{
		preparedStatement = null;
		String query = "UPDATE users SET isOnline = ? WHERE username = ?;";
		
		try
		{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setBoolean(1, onlineStatus);
			preparedStatement.setString(2, username);
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("user " + username + " is now online: " + onlineStatus);
	}
	

	/**
	 * Method to set the user's status
	 * @param username The user who's changing their status.
	 * @param the status it is being changed to.
	 */
	@Override
	public void setStatus(String username, String status) 
	{
		preparedStatement = null;
		String query = "UPDATE users SET status = ? WHERE username = ?;";
		
		try
		{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, status);
			preparedStatement.setString(2, username);
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		System.out.println("user " + username + " has changed their status to: " + "'" + status + "'");
		
	}
	
	/**
	 * Method to insert a new user into the database.
	 * @param firstname The users firstname.
	 * @param lastname The users lastname.
	 * @param username The users username.
	 * @param password The users password.
	 * @param status The users status.
	 * @param email The users email address.
	 * @return 0,1 or 2. Returns 0 if both username and email are valid, returns 1 if username is invalid and 2 if email is invalid.   
	 **/
	public int insertRowUser(String firstname, String lastname, String username, String password, String status, String email)
	{
		preparedStatement = null;
		
		try {
			String query = "INSERT INTO users (uid, firstname, lastname, username, password, status, email, isonline) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, getCount("chatsystem.users") + 1);

			if (firstname.equals(""))
			{
				preparedStatement.setString(2, "NULL");
			}
			else
			{
				preparedStatement.setString(2, firstname);
			}
			if (lastname.equals(""))
			{
				preparedStatement.setString(3, "NULL");
			}
			else
			{
				preparedStatement.setString(3, lastname);
			}
			
			if (usernameIsValid(username))
			{
				preparedStatement.setString(4, username);
			}
			else
			{
				return 1;
			}

			preparedStatement.setString(5, password);
			
			if (status.equals(""))
			{
				preparedStatement.setString(6, "NULL");
			}
			else
			{
				preparedStatement.setString(6, status);
			}
			
			if (emailIsValid(email) && emailIsRegEx(email))
			{
				preparedStatement.setString(7, email);
			}
			else
			{
				if (!emailIsValid(email))
				{
					return 2;
				}
				if (!emailIsRegEx(email))
				{
					return 3;
				}
			}
			preparedStatement.setBoolean(8, false);
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return 0; // if no errors and SQL query executes.
	}
	
	/**
	 * Method for getting the userId as is in the database.
	 * @param username The username of the user.
	 * @return The username's userID value.
	 */
	public int getUserId(String username)
	{
		preparedStatement = null;
		int userId = 0;
		try
		{
			String query = "SELECT uid FROM users WHERE username=?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				userId = rs.getInt("uid");				
			}
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return userId;
	}
	
	/**
	 * Method for getting the username from the userId.
	 * @param userId The user's id number.
	 * @return The users username.
	 */
	public String getUsername(int userId)
	{
		preparedStatement = null;
		String username= "";
		
		try
		{
			String query = "SELECT username FROM users WHERE uid= ?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next())
			{
				username = rs.getString("username");
			}
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return username;
	}
	
	/**
	 * Method to return the chat history between two users.
	 * @param usernameFrom The first user.
	 * @param usernameTo The second user.
	 * @return All messages history between the two given users.
	 */
	public ArrayList<String> selectChatHistory(String usernameFrom, String usernameTo)
	{
		String message = null;
		ArrayList<String> chatHistory = new ArrayList<String>();
		preparedStatement = null;
		String query = "SELECT u1.username AS from,u2.username AS to,m.message FROM messages m, users u1, users u2 "
				+ "WHERE (messageFrom = ? OR messageFrom = ?) AND (messageTo = ? OR messageTo = ?) "
				+ "AND u1.uid=m.messagefrom AND u2.uid=m.messageto ORDER BY datetime DESC;";
		
		try 
		{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, getUserId(usernameFrom));
			preparedStatement.setInt(2, getUserId(usernameTo));
			preparedStatement.setInt(3, getUserId(usernameFrom));
			preparedStatement.setInt(4, getUserId(usernameTo));
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) 
			{
				message = rs.getString("message");
				chatHistory.add(message);
				System.out.println("message: " + message);
			}	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return chatHistory;		
	}
	
	/**
	 * Method to check that a given username is not already taken.
	 * @param username The username to be checked.
	 * @return True if it is not in the table, false if it is.
	 */
	public boolean usernameIsValid(String username)
	{
		String query = "SELECT username FROM users";
		String dbUsername;
		Statement statement = null;
		boolean usernameIsValid = true;
		
		try {
			statement = connection.createStatement();
			statement.executeQuery(query);
			ResultSet rs = statement.getResultSet();
			
			while (rs.next())
			{
				dbUsername = rs.getString("username");
				
				if (dbUsername.equals(username))
				{
					System.out.println("username already exists");
					usernameIsValid = false;
				}
			}
		} 
		/*catch (PSQLException e) 
		{
            e.printStackTrace();
        }*/
		catch (SQLException e) 
		{
            e.printStackTrace();
        }
		System.out.println("username is valid");
        return usernameIsValid;
	}
	
	public boolean emailIsRegEx(String email)
	{
		String emailPattern = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$";
		
		Pattern pattern = Pattern.compile(emailPattern);
		
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
		
	}
	
	
	/**
	 * Method to check that a given email address is valid.
	 * @param email The email to be checked.
	 * @return True if the email address is not in the table, false if it is.
	 */
	public boolean emailIsValid(String email)
	{
		String query = "SELECT email FROM users";
		String dbEmail;
		Statement statement = null;
		boolean emailIsValid = true;
		
		// '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'
		/*
		CREATE DOMAIN us_postal_code AS TEXT
		CHECK(
		   VALUE ~ '^\\d{5}$'
		OR VALUE ~ '^\\d{5}-\\d{4}$'
		);

		CREATE TABLE us_snail_addy (
		  address_id SERIAL PRIMARY KEY,
		  street1 TEXT NOT NULL,
		  street2 TEXT,
		  street3 TEXT,
		  city TEXT NOT NULL,
		  postal us_postal_code NOT NULL
		);
		*/
		
		try {
			statement = connection.createStatement();
			statement.executeQuery(query);
			ResultSet rs = statement.getResultSet();
			
			while (rs.next())
			{
				dbEmail = rs.getString("email");
				
				if (dbEmail.equals(email))
				{
					System.out.println("username already exists");
					emailIsValid = false;
				}
			}
		} 
		/*catch (PSQLException e) 
		{
            e.printStackTrace();
        }*/
		catch (SQLException e) 
		{
            e.printStackTrace();
        }
        return emailIsValid;
	}
	
	/**
	 * Method to validate login details.
	 * @param username The username given at sign-in.
	 * @param password The password given at sign-in.
	 * @return True if they match, false otherwise.
	 */
	public boolean loginCheck(String username, String password)
	{
        String query = "SELECT username, password FROM users";
        String dbUsername, dbPassword;
        Statement statement = null;
        boolean login = false;

        try {
            statement = connection.createStatement();
            statement.executeQuery(query);
            ResultSet rs = statement.getResultSet();

            while(rs.next())
            {
                dbUsername = rs.getString("username");
                dbPassword = rs.getString("password");

                if(dbUsername.equals(username) && dbPassword.equals(password))
                {
                    System.out.println("OK");
                    login = true;
                }
            }
        } 
        /*catch (PSQLException e) 
        {
            e.printStackTrace();
        }*/catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return login;
    }
	
	/**
	 * Method to get the friends of a given user.
	 * @param userName The username of the user.
	 * @return An arrayList of usernames which the user is connected to.
	 */
	@Override
	public ArrayList<String> getContacts(String userName) 
	{
		String friendUsername = null;
		preparedStatement = null;
		ArrayList<String> listOfIds = new ArrayList<String>();

		String query = "SELECT u.username FROM usercontacts uc INNER JOIN users u ON (uc.contactid = u.uid) WHERE uc.uid = ?;";
		int userId = getUserId(userName);
		try 
		{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) 
			{	
				friendUsername = rs.getString("username");
				listOfIds.add(friendUsername);
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
		return listOfIds;
	}
	
	/**
	 * Method to open the connection to the database.
	 */
	public void open() 
	{
		System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");
		
		try 
		{
			Class.forName("org.postgresql.Driver");
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;
		}
		 
		System.out.println("PostgreSQL JDBC Driver Registered!");

		this.connection = null;

		try 
		{
			this.connection = DriverManager.getConnection("jdbc:postgresql://dbteach2:5432/nar779", "nar779", "Deltasql89");
		} 
		catch (SQLException e) 
		{
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (this.connection != null) 
		{
			System.out.println("You made it, take control your database now!");
		} 
		else 
		{
			System.out.println("Failed to make connection!");
		}
	}
	
	/**
	 * Method to close the connection to the database.
	 */
	public void close()
	{
		if (statement != null) 
		{
			try 
			{
				statement.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		if (preparedStatement != null) 
		{
			try 
			{
				statement.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		if (connection != null) 
		{
			try 
			{
				connection.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		Database database = new Database("chatsystem");
		//System.out.println(database.loginCheck("john", "password2"));
		//database.insertRowUser("paul", "scholes", "pauls", "paulpw", "hii", "paul_scholes@gmail.com");
		//database.usernameIsValid("gazzafggfgdf");
		//database.insertRowMessages(2, 1, "Not bad thanks, how are you?");
		//System.out.println(database.getCount("chatsystem.messages"));
//		System.out.println(database.getLastname("nick"));
//		System.out.println(database.getFirstname("nick"));
//		System.out.println(database.getStatus("nick"));
//		System.out.println(database.getEmail("nick"));
//		System.out.println(database.getPassword("nick"));
//		System.out.println(database.getIsOnline("nick"));
//		database.setOnline("nick", true);
//		database.insertRowMessages(4, 1, "Hello");
//		database.insertRowMessages(2, 1, "Great");
//		database.insertRowMessages(1, 2, "What's the time?");
//		database.insertRowMessages(2, 1, "I'm not sure, half 9?");
//		database.insertRowMessages(1, 2, "That's a good time");
//		System.out.println(database.getUserId("nick"));
//		System.out.println(database.getUserId("nat"));
//		System.out.println(database.getUserId("john"));
//		database.selectChatHistory("nat", "john");
		
		/*ArrayList<String> usernames = database.getContacts(database.getUsername(1));
		
		for (String name: usernames)
		{
			System.out.println(name);
		}*/
		
		//database.insertRowUserContacts("wayne", "pnev");
		//database.setStatus("nat", "this is my new status");
		
	
		
	}


	

	

}