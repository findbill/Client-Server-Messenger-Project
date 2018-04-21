
public interface DatabaseInterface {
	
	public boolean loginCheck(String username, String password);
	// register a user.  3 return states: 0 = everything fine, table updated. 1 = username taken. 2 = email taken. 1& 2 require user to enter different username and/or email
	public int insertRowUser(String firstname, String lastname, String username, String password, String status, String email);
	// select lastname where username = login username, same getters for all column names
	public String getLastname(String username);
	public String getFirstname(String username);
	public String getStatus(String username);
	public String getEmail(String username);
	public String getPassword(String username);
	public boolean getIsOnline(String username);
	
	// setters for changing passwords, status's (displayname)
	public void setStatus(String status);
	// set to online when user connects
	public void setOnline(String username, boolean onlineStatus); 
	// add message to message table
	//public 
	
	// used in the insertRowUser method to check that the username has not already been taken.
	public boolean usernameIsValid(String username);
	// used in the insertRowUser method to check that the email has not already been taken.
	public boolean emailIsValid(String username);
	// when a user opts to make another user one of their contacts then add them to the UserContacts table to keep track of whos connected to who.
	public void insertRowUserContacts(int uid, int contactid);
	// method to close the database connection after use
	public void close();
	
}
