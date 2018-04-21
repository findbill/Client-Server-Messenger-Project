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

import org.postgresql.util.PSQLException;

public class Database implements DatabaseInterface
{
	
	private Connection connection;
	private PreparedStatement preparedStatement;
	private Statement statement;
	
	public Database(){
		open();
	}
		
	private int getCount(String tableName)
	{
		int count=0;
		Statement statement;
		String query = "SELECT COUNT(*) FROM " + tableName;
		System.out.println("executing: " + query);
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			while (rs.next())
			{
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("getCount method returns " + count + " as the current count");
		return count;
	}
	
	public void insertRowMessages(int messageFrom, int messageTo, String message)
	{
		System.out.println("executing insertRowMessages query");
		preparedStatement = null;
		String query = "INSERT INTO chatsystem.messages VALUES (?, ?, ?, ?, ?, ?)";
		
		try {
			System.out.println("try block entered");
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, getCount("chatsystem.messages") + 1);
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
	
	private static java.sql.Timestamp getCurrentTimeStamp() 
	{
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Timestamp(today.getTime());
	}
	
	public void insertRowUserContacts(int uid, int contactid)
	{
		preparedStatement = null;
		String query = "INSERT INTO chatsystem.usercontacts VALUES (?, ?);";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, uid);
			preparedStatement.setInt(2, contactid);
			// execute insert SQL stetement
			preparedStatement.executeUpdate();
			System.out.println("Record is inserted into usercontacts table!");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public String getLastname(String username) 
	{
		String lastname = null;
		preparedStatement = null;
		String query = "SELECT lastname FROM chatsystem.user WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				lastname = rs.getString("lastname");				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 

		return lastname;
	}
	
	@Override
	public String getFirstname(String username) 
	{
		String firstname = null;
		preparedStatement = null;
		String query = "SELECT firstname FROM chatsystem.user WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				firstname = rs.getString("firstname");				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 

		return firstname;
	}
	
	@Override
	public String getPassword(String username) 
	{
		String password = null;
		preparedStatement = null;
		String query = "SELECT password FROM chatsystem.user WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				password = rs.getString("password");				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 

		return password;
	}
	
	@Override
	public String getStatus(String username) 
	{
		String status = null;
		preparedStatement = null;
		String query = "SELECT status FROM chatsystem.user WHERE (username = ?)";

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
	
	@Override
	public String getEmail(String username) 
	{
		String email = null;
		preparedStatement = null;
		String query = "SELECT email FROM chatsystem.user WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				email = rs.getString("email");				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return email;
	}
	@Override
	public boolean getIsOnline(String username)
	{
		boolean isOnline = false;
		preparedStatement = null;
		String query = "SELECT isonline FROM chatsystem.user WHERE (username = ?)";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				isOnline = rs.getBoolean("isOnline");				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return isOnline;
	}
	
	@Override
	public void setOnline(String username, boolean onlineStatus) 
	{
		preparedStatement = null;
		String query = "UPDATE chatsystem.user SET isOnline = ? WHERE username = ?;";
		
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
	
	@Override
	public void setStatus(String status) {
		// TODO Auto-generated method stub
		
	}
	// change status to display name (or add in display name)
	
	/**
	 * Method to insert a new user into the database.
	 * @return 0,1 or 2. Returns 0 if both username and email are valid, returns 1 if username is invalid and 2 if email is invalid.   
	 **/
	public int insertRowUser(String firstname, String lastname, String username, String password, String status, String email)
	{
		preparedStatement = null;
		
		try {
			String query = "INSERT INTO chatsystem.user (uid, firstname, lastname, username, password, status, email, isonline) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, getCount("chatsystem.user") + 1);

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
			
			if (emailIsValid(email))
			{
				preparedStatement.setString(7, email);
			}
			else
			{
				return 2;
			}
			
			preparedStatement.setBoolean(8, false);
			preparedStatement.executeUpdate();
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		

	}
	
	public ArrayList<String> selectChatHistory(int userFrom, int userTo)
	{
		String message = null;
		ArrayList<String> chatHistory = new ArrayList<String>();
		preparedStatement = null;
		String query = "SELECT message FROM chatsystem.messages WHERE (userFrom = ? AND userTo = ?) ORDER BY datetime DESC LIMIT 50;";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userFrom);
			preparedStatement.setInt(2,  userTo);
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				message = rs.getString("message");
				chatHistory.add(message);
				System.out.println("message: " + message);
			}
		}
		
				
	}
	
	public void selectUsernameAndPassword() throws SQLException 
	{
		statement = null;
		String query = "SELECT username, password from chatsystem.user";
		
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			while (rs.next()) 
			{
				String username = rs.getString("username");
				String password = rs.getString("password");

				System.out.println("username : " + username);
				System.out.println("password : " + password);
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());

		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	
	
	public boolean usernameIsValid(String username)
	{
		String query = "SELECT username FROM chatsystem.user";
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
		} catch (PSQLException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
		System.out.println("username is valid");
        return usernameIsValid;
	}
	
	public boolean emailIsValid(String username)
	{
		String query = "SELECT email FROM chatsystem.user";
		String dbEmail;
		Statement statement = null;
		boolean emailIsValid = true;
		
		try {
			statement = connection.createStatement();
			statement.executeQuery(query);
			ResultSet rs = statement.getResultSet();
			
			while (rs.next())
			{
				dbEmail = rs.getString("email");
				
				if (dbEmail.equals(username))
				{
					System.out.println("username already exists");
					emailIsValid = false;
				}
			}
		} catch (PSQLException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
		System.out.println("email is valid");
        return emailIsValid;
	}
	

	
	public boolean loginCheck(String username, String password)
	{
        String query = "SELECT username, password FROM chatsystem.user";
        String dbUsername, dbPassword;
        Statement statement = null;
        boolean login = false;

        try {
            statement = connection.createStatement();
            statement.executeQuery(query);
            ResultSet rs = statement.getResultSet();

            while(rs.next()){
                dbUsername = rs.getString("username");
                dbPassword = rs.getString("password");

                if(dbUsername.equals(username) && dbPassword.equals(password))
                {
                    System.out.println("OK");
                    login = true;
                }
            }
        } 
        catch (PSQLException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return login;
    }
	
	public void open() {

		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");

		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;

		}

		System.out.println("PostgreSQL JDBC Driver Registered!");

		this.connection = null;

		try {

			this.connection = DriverManager.getConnection(
					"jdbc:postgresql://dbteach2:5432/nar779", "nar779",
					"Deltasql89");

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (this.connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}
	
	public void close()
	{
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (preparedStatement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		Database database = new Database();
		//System.out.println(database.loginCheck("john", "password2"));
		//database.insertRowUser("paul", "scholes", "pauls", "paulpw", "hii", "paul_scholes@gmail.com");
		//database.usernameIsValid("gazzafggfgdf");
		//database.insertRowMessages(2, 1, "Not bad thanks, how are you?");
		//System.out.println(database.getCount("chatsystem.messages"));
		System.out.println(database.getLastname("nick"));
		System.out.println(database.getFirstname("nick"));
		System.out.println(database.getStatus("nick"));
		System.out.println(database.getEmail("nick"));
		System.out.println(database.getPassword("nick"));
		System.out.println(database.getIsOnline("nick"));
		database.setOnline("nick", true);
	}

}