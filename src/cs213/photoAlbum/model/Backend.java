/* Project Part 1
 * Photo Album: Design and Implementation I
 * API (javadoc HTML) Due Friday, Feb 20th
 * 
 * Complete Code (plus UML and updated javadoc HTML)
 * Due Fri, Mar 6th
 * 
 * Brian Yoo 140007707
 * Michelle Gavino 138004573
 */

package cs213.photoAlbum.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * 
 * @author Michelle Gavino
 *
 */
public class Backend implements Serializable {

	/**
	 * Default number given to classes that implements Serializable. Prevents invalid class exceptions.
	 */
	private static final long serialVersionUID = 2960040745856236894L;
	/**
	 * A list of all existing users, identified by userIDs.
	 */
	public ArrayList<User> userList;
	/**
	 * The data structure that holds the strings of the user IDs of each user.
	 */
	public static DefaultListModel<String> listMod = new DefaultListModel<String>();
	/**
	 * The data structure that holds a list of user IDs to be displayed in a JFrame
	 */
	public static JList<String> list = new JList<String>(listMod);
	
	/**
	 * Searches through the userList array list to obtain the user object
	 * @param userID the desired user to be found
	 * @return returns the desired user object
	 */
	public User getUser(String userID) {
		if(userList == null || userList.size() == 0){
			System.out.println("No users exist.");
			return null;
		}else{
			for(int i = 0; i < userList.size(); i++) {
				if(userList.get(i).userID.equals(userID)) {
					return userList.get(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks if a user exists in the list of users.
	 * @param userID the ID of the user to be checked
	 * @param userName the name of the user to be checked
	 * @return returns true if user exists, else returns false.
	 */
	public boolean hasUser(String userID, String userName) {
		for(int i = 0; i < userList.size(); i++) {
			if(userList.get(i).userID.equals(userID) && userList.get(i).userName.equals(userName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Saves a user's data to storage from memory. (persistence)
	 * @param user the object to be saved to storage from memory
	 */
	public void writeuser(User user) 
	throws FileNotFoundException {
		try {
			String path =  System.getProperty("user.dir");
			FileOutputStream fos = new FileOutputStream(path + File.separator + "data" + File.separator + user.userID + ".txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);   
			//System.out.println("creating file for user "+ user.userID);
			oos.writeObject(user.albumCollection); //write in the userlist?
			oos.close(); 
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Loads user's data from disk. (persistence)
	 * @param userID the unique ID used to load user's data from disk
	 * @return returns the user's album collection
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Album> readuser(String userID)	
	throws IOException, ClassNotFoundException {
			String path =  System.getProperty("user.dir");
			FileInputStream fis = new FileInputStream(path + File.separator + "data" + File.separator + userID + ".txt");
			//System.out.println("reading textfile for " + userID);
			ObjectInputStream ois = new ObjectInputStream(fis);   
			ArrayList<Album>collection = (ArrayList<Album>)ois.readObject();	//read the objects in the file
			fis.close();
			ois.close(); 
			return collection;
	}
	/**
	 * Saves userList into memory. (persistence)
	 * @param userList the object to be saved to storage from memory
	 */
	public void saveUserList(ArrayList<User> userList) 
		//save User object under textfile with name "<userID>.txt"
	throws FileNotFoundException {
		try {
			String path =  System.getProperty("user.dir");
			FileOutputStream fos = new FileOutputStream(path + File.separator + "data" + File.separator + "userList.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);   
			//System.out.println("creating file for userList");
			oos.writeObject(userList); 
			oos.close(); 
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Loads user list from data. (persistence)
	 * @return returns the list of existing users
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<User> loadUserList()	
	throws IOException, ClassNotFoundException {
			String path =  System.getProperty("user.dir");
			FileInputStream fis = new FileInputStream(path + File.separator + "data" + File.separator + "userList.txt");
			//System.out.println("reading textfile for userList");
			ObjectInputStream ois = new ObjectInputStream(fis);   
			ArrayList<User> userList = (ArrayList<User>)ois.readObject();	//read the objects in the file
			fis.close();
			ois.close(); 
			return userList;
	}
	
	
	/**
	 * Creates an instance of a user with the specified userID and userName.
	 * @param userID the desired ID of the user
	 * @param userName the name of the user
	 * @param albumCollection the user's album collection
	 * 
	 */
	public void adduser(String userID, String userName, ArrayList<Album> albumCollection){
		User user = new User(userID, userName, albumCollection);
		this.userList.add(user);
		listMod.addElement(userID);
		
	}
	
	/**
	 * Deletes an existing user from memory, identified by user ID.
	 * @param userID the user's unique ID that is used to log into interface mode
	 */ 
	public void deleteuser(String userID) {
		this.userList.remove(getUser(userID));
		listMod.removeElement(userID);
	}
	
	/**
	 * Displays a list of existing users, identified by user IDs, from the data directory.
	 */
	public void listusers() {
		
		if(userList.size() == 0){
			System.out.println("no users exist");
		}
		
		for(int i = 0; i < userList.size(); i++) {
			System.out.println(userList.get(i).userID);
		}
	}
	
	/**
	 * Logs into interactive mode as an existing user.
	 * @param userID the user's unique ID that is used to log into interface mode
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	
	public void login(String userID) throws ClassNotFoundException, IOException {
		User u = getUser(userID);
		u.albumCollection = readuser(userID);
		
	}
	
	/**
	 * Obtains the index of the specified userID in the user list
	 * @param userID the user's unique id name
	 * @return the index to the user in the user list. Upon failure, the method will return -1.
	 */
	public int getUserIndex(String userID) {
		
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i) == null || userList.get(i).userID == null) {continue;}
			if (userID.equals(userList.get(i).userID)) {
				return i;
			}
		}
		
		
		return -1;
	}
	
}
