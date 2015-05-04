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

package cs213.photoAlbum.control;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.simpleview.AlbumFrame;

/**
 * 
 * @author Michelle Gavino
 * @author Brian Yoo
 *
 */
public class DataControl {
	/**
	 * Backend object used to save and manipulate the data of all of the users.
	 * This object will hold the list of all of the users.
	 */
	public static Backend backendmodel = new Backend();
	
	/**
	 * The constructor. This will establish which user is currently being processed and will issue all data
	 * manipulation according to this specified user.
	 */
	public DataControl() {
	}
	
	/**
	 * Loads the list of existing users from memory. (persistence)
	 */
	public void load(){
        // Load userList
        String path =  System.getProperty("user.dir");
        File f = new File(path + File.separator + "data" + File.separator + "userList.txt");
        if(f.exists()) {
            try {
                backendmodel.userList = backendmodel.loadUserList();
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
	
	/**
	 * Saves the list of existing users to memory. (persistence)
	 */
	public void save(){
		try {
			backendmodel.saveUserList(backendmodel.userList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Adds a tag to the specified photo by the tagType and the tagValue.
	 * @param user the user whose photo albums are being accessed
	 * @param fileName the file path name for the photo object
	 * @param typeandval the specific type and value of the tag to be added
	 */
	public void addTag(User user, String fileName, String typeandval){
		// Error check if the entire 3rd argument was entirely in quotes.
		String firstQ = typeandval.substring(0,1);
		String lastQ = typeandval.substring(typeandval.length() - 1);
		if (firstQ.equals("\"") && lastQ.equals("\"")) {
			System.out.println("Error: Please specify a tag type and a tag value in the format <tagType>:\"<tagValue>\"");
			return;
		}
		
		// Error Check to see if there is a tag type and a tag value.
		String arr[] = typeandval.split(":(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		if (arr.length != 2 || arr[0].equals("")) {
			System.out.println("Error: Please specify a tag type and a tag value in the format <tagType>:\"<tagValue>\"");
			return;
		}
		// Error Check, see if the tag value argument was in fact in quotes
		firstQ = arr[1].substring(0,1);
		lastQ = arr[1].substring(arr[1].length() - 1);
		if (!(firstQ.equals("\"") && lastQ.equals("\""))) {
			System.out.println("Error: Please specify a tag type and a tag value in the format <tagType>:\"<tagValue>\"");
			return;
		}
		// Strip Quotes
		else {
			arr[1] = arr[1].substring(1,arr[1].length() - 1);
		}
		fileName = fileName.replace("\"", "");
		
		// Error Check. If the tagValue is null, do not add
		if (arr[1].equals("")) {
			System.out.println("Error: <tagValue> has no value. Please input a value for <tagValue>");
			return;
		}
		// END OF ERROR CHECKS
		
		// Add Tag.
		boolean hasPhoto = true;
		// Iterate through each album to find photo
		for(int i = 0; i < user.albumCollection.size(); i++){
			Album currAlbum = user.albumCollection.get(i);
			
			// check if photo exists first
			if(currAlbum.getPhoto(fileName) == null){
				hasPhoto = false;
				continue;
			} else {
				hasPhoto = true;
			}
			
			//check if tag already exists
			if(currAlbum.getPhoto(fileName).getTag(arr[0], arr[1]) == null){
				currAlbum.getPhoto(fileName).addTag(arr[0], arr[1]);
				System.out.println("Added tag:");
				System.out.println(fileName + " " + arr[0] + ":" + arr[1]);
				break;
			} else {
				System.out.println("Tag already exists for " + fileName + " " + arr[0] + ":" + arr[1]);
				break;
			}
		}
		
		// Iterated through all of the albums. The photo does not exist.
		if(!hasPhoto){
			System.out.println("Photo " + fileName + " does not exist");
		}
		
	}
	
	/**
	 * Deletes a tag of the specified photo with the given tagType and tagValue parameters.
	 * @param user the user whose photo albums are being accessed
	 * @param fileName the file path name for the photo object
	 * @param typeandval the specific type and value of the tag to be deleted
	 */
	public void deleteTag(User user, String fileName, String typeandval){
		// Error Check. if the entire 3rd argument was entirely in quotes.
		String firstQ = typeandval.substring(0,1);
		String lastQ = typeandval.substring(typeandval.length() - 1);
		if (firstQ.equals("\"") && lastQ.equals("\"")) {
			System.out.println("Error: Please specify a tag type and a tag value in the format <tagType>:\"<tagValue>\"");
			return;
		}
		
		// Error Check. to see if there is a tag type and a tag value.
		String arr[] = typeandval.split(":(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		if (arr.length != 2 || arr[0].equals("")) {
			System.out.println("Error: Please specify a tag type and a tag value in the format <tagType>:\"<tagValue>\"");
			return;
		}
		// Error Check. see if the tag value argument was in fact in quotes
		firstQ = arr[1].substring(0,1);
		lastQ = arr[1].substring(arr[1].length() - 1);
		if (!(firstQ.equals("\"") && lastQ.equals("\""))) {
			System.out.println("Error: Please specify a tag type and a tag value in the format <tagType>:\"<tagValue>\"");
			return;
		}
		// Strip Quotes
		else {
			arr[1] = arr[1].substring(1,arr[1].length() - 1);
		}
		fileName = fileName.replace("\"", "");
		
		// Error Check. If the tagValue is null, do not add
		if (arr[1].equals("")) {
			System.out.println("Error: <tagValue> has no value");
			return;
		}
		// END OF ERROR CHECKS
		
		// Delete Tag.
		boolean hasPhoto = true;
		for(int i = 0; i < user.albumCollection.size(); i++){
			Album currAlbum = user.albumCollection.get(i);
			Photo photo = currAlbum.getPhoto(fileName);
			// check if photo exists first
			if(photo == null){
				hasPhoto = false;
				continue;
			}else{
				hasPhoto = true;
			}
			
			// Check if tag exists
			if(currAlbum.getPhoto(fileName).getTag(arr[0], arr[1]) == null){
				System.out.println("Tag does not exist for " + fileName + " " + arr[0] + ":" + arr[1]);
				break;
			}else{
				currAlbum.getPhoto(fileName).deleteTag(arr[0], arr[1]);
				System.out.println("Deleted tag:");
				System.out.println(fileName + " " + arr[0] + ":" + arr[1]);
				break;
			}
		}
		if(!hasPhoto){
			System.out.println("Photo " + fileName + " does not exist");
		}
	}
	
	/**
	 * Outputs all information about the specified fileName. 
	 * Information includes file name, album(s) it belongs to, date it was created, caption, and tags.
	 * Tags are sorted by location first, then people, then others, sorted by tag value within each type.
	 * @param user the user whose photo albums are being accessed
	 * @param fileName the file path name for the photo object
	 */
	public void listPhotoInfo(User user, String fileName) {
		boolean flag = false;
		for (int i = 0; i < user.albumCollection.size(); i++) {
			if (user.albumCollection.get(i).getPhoto(fileName) != null) {
				flag = true;
			}
		}
		
		if (flag) {
			user.listPhotoInfo(fileName);
		} else
			System.out.println("Photo " + fileName + " does not exist");
	}
	
	/**
	 * Retrieves all photos taken within a given range of dates, in chronological order.
	 * @param user the user whose photo albums are being accessed
	 * @param startDate the first date to be checked
	 * @param endDate the last date to be checked
	 * @return ArrayList of Photos within the range of dates
	 */
	public ArrayList<Photo> getPhotosByDate(User user, String startDate, String endDate) {
		return user.getPhotosByDate(startDate, endDate);
	}
	
	/**
	 * Retrieves all photos that contain all the tags listed in stringStream. Listed in alphabetical order.
	 * @param user the user whose photo albums are being accessed
	 * @param stringStream the desired tag(s) to search for
	 */
	public ArrayList<Photo> getPhotosByTag(User user, String stringStream) {
		return user.getPhotosByTag(stringStream);
	}
	
	/**
	 * Adds new photo to specified album. The absolute path of the photo is stored as the file name of the photo. All accesses to the photo MUST be specified with the absolute path.
	 * Note: The caption is a property of the photo, not the album. This means if you add a photo to more than one album, you specify the caption when adding to the first album. Then, when adding to subsequent albums, you specify an empty string for the caption, meaning that the caption is retained from the first add.
	 * @param user the user whose photo albums are being accessed
	 * @param fileName the absolute path for the file of photo
	 * @param caption the desired caption of the new photo
	 * @param albumName the name of the album to be added to 
	 */
	public void addPhoto(User user, String fileName, String caption, String albumName){
		ArrayList<Tag> temp = new ArrayList<Tag>();
		String cap = caption;
		
		File f = null;
		String path = "";
		boolean flag = false;
		boolean exists = false;
		
		f = new File(fileName);
		try {
			path = f.getCanonicalPath();
			flag = f.exists();
			if (flag) {
			} else {
				System.out.println("File " + fileName + " does not exist");
				return;
			}
			
		} catch (IOException e) {
			//AlbumFrame.error1.setForeground(Color.red);
			//AlbumFrame.error1.setText("Invalid input");
			//AlbumFrame.error2.setForeground(Color.red);
			//AlbumFrame.error2.setText("Could not add photo.");
			System.out.println("File " + fileName + " does not exist");
			return;
		}
		
		fileName = path;
		// Error Check
		if(user.getAlbum(albumName) == null){
			System.out.println("Album " + albumName + " does not exist");
		}else{
			Photo photo = new Photo(fileName, caption);
			// First check if photo exists in current album

			// Check if photo already exists
			if(user.getAlbum(albumName).getPhoto(fileName) != null){
				System.out.println("Photo " + fileName + " already exists in album " + albumName);
				//AlbumFrame.error1.setForeground(Color.red);
				//AlbumFrame.error1.setText("Photo " + fileName + " already exists in album " + albumName);
				//AlbumFrame.error2.setForeground(Color.red);
				//AlbumFrame.error2.setText("Could not add photo");
				return;
			} 
			
			// Then search the other albums for that photo
			for(int i = 0; i < user.albumCollection.size(); i++) {
				Album currAlbum = user.albumCollection.get(i);
				if(currAlbum.hasPhoto(fileName)) {
					exists = true;
					if(caption.isEmpty()){
						cap = currAlbum.getPhoto(fileName).getCaption();
						exists = false;
					}else if (!caption.isEmpty()){
						//AlbumFrame.error1.setForeground(Color.red);
						//AlbumFrame.error1.setText("Error. Must specify empty string for caption when adding an existing photo to subsequent albums.");
						//AlbumFrame.error2.setForeground(Color.red);
						//AlbumFrame.error2.setText("Could not add photo");
						System.out.println("Error. Must specify empty string for caption when adding an existing photo to subsequent albums.");
						return;
					}
					temp = currAlbum.getPhoto(fileName).getTagList();
				}
			}
			photo.setTagList(temp);
			
			// After checking if photo does not exist in current album and other albums, finally add
			if(!exists){
				photo.setCaption(cap);
				user.getAlbum(albumName).addPhoto(photo);
				System.out.println("Added photo " + fileName + ":");
				System.out.println(cap + " - Album: " + albumName);
			}
			
		}
	}

	
	/**
	 * Removes photo from specified album.
	 * @param user the user whose photo albums are being accessed
	 * @param fileName the file path name for the photo object
	 * @param albumName the name of the album containing the photo to be deleted
	 */
	public void removePhoto(User user, String fileName, String albumName) {
		// Error Check if the album exists
		if (user.getAlbum(albumName) == null) {
			System.out.println("Album " + albumName + " does not exist.");
		}
		
		// Check if photo exists in album
		if(user.getAlbum(albumName).hasPhoto(fileName)){
			user.getAlbum(albumName).removePhoto(fileName);
			System.out.println("Removed photo:");
			System.out.println(fileName + " - From album " + albumName);
		} else {
			System.out.println("Photo " + fileName + " is not in album " + albumName);
		}
	}
	
	/**
	 * Lists all photos and their details in the specified album.
	 * @param user the user whose photo albums are being accessed
	 * @param albumName the name of the album whose photos are to be printed
	 */
	public void listPhotos(User user, String albumName) {
		// check if album exists
		if(user.getAlbum(albumName) == null){
			System.out.println("Album " + albumName + " does not exist.");
		} else {
			// Check if album has any photos
			if(user.getAlbum(albumName).getPhotoct() == 0){
				System.out.println("No photos exist for " + albumName);
			} else {
				user.getAlbum(albumName).listPhotos();
			}
		}
	}
	
	/**
	 * 
	 * Moves photo from oldAlbum into newAlbum. If the photo already exists in newAlbum, returns without doing anything.
	 * @param user the user whose photo albums are being accessed
	 * @param fileName the path to the specified photo to be moved
	 * @param oldAlbum the name of the album containing the photo to be moved
	 * @param newAlbum the name of the desired album to add the photo to
	 * 
	 */
	public void movePhoto(User user, String fileName, String oldAlbum, String newAlbum) {
		// Error check. See if the albums exist
		if (user.getAlbum(newAlbum) == null) {
			System.out.println("Album " + newAlbum + " does not exist");
			return;
		}
		if (user.getAlbum(oldAlbum) == null) {
			System.out.println("Album " + oldAlbum + " does not exist");
			return;
		}
		
		// Error Check. The photo must exist in the old album.
		if(user.getAlbum(oldAlbum).getPhoto(fileName) == null) {
			System.out.println("Photo " + fileName + " does not exist in " + oldAlbum);
		} 
		// Photo cannot exist in the new album
		else if(user.getAlbum(newAlbum).getPhoto(fileName) != null) {
			System.out.println("Photo " + fileName + " already exists in " + newAlbum);
			//AlbumFrame.error1.setForeground(Color.red);
			//AlbumFrame.error1.setText("Photo " + fileName + " already exists in " + newAlbum);
			//AlbumFrame.error2.setForeground(Color.red);
			//AlbumFrame.error2.setText("Could not move photo");
		}
		// No errors. Move the photo.
		else {
			System.out.println("Moved photo " + fileName + ":");
			System.out.println(fileName + " - From album " + oldAlbum + " to album " + newAlbum);
			user.movePhoto(fileName, oldAlbum, newAlbum);
		}
	}
	
	/**
	 * Creates a new album with the given albumName
	 * @param user the user whose photo albums are being accessed
	 * @param albumName name of the new album
	 */
	public Album createAlbum(User user, String albumName) {
		// Error Check.
		if (albumName.equals("")) {
			System.out.println("Error: album name cannot be null");
			return null;
		}
		
		if(user.getAlbum(albumName) != null){
			System.out.println("album exists for user " + user.userID + ":");
			System.out.println(albumName);
			return null;
		} else {
			Album album = user.createAlbum(albumName);
			user.albumCollection.add(album);
			System.out.println("created album for user " + user.userID + ":");
			System.out.println(album.getAlbumName());
			return album;
		}
	}
	
	/**
	 * Deletes the specified album and all its contents.
	 * @param user the user whose photo albums are being accessed
	 * @param albumName the name of the album to be deleted
	 */
	public void deleteAlbum(User user, String albumName) {
		if(user.getAlbum(albumName) != null) {
			System.out.println("deleted album from user " + user.userID + ":");
			user.deleteAlbum(albumName);
		} else {
			System.out.println("album does not exist for user " + user.userID + ":");
			System.out.println(albumName);
		}
	}
	
	/**
	 * Outputs all albums with their number of photos and the range of dates that the photos were taken.
	 * @param user the user whose photo albums are being accessed
	 */
	public void listAlbums(User user) {
		if(user.albumCollection.size() == 0){
			System.out.println("No albums exist for user " + user.userID);
		} else {
			user.listAlbums();
		}
	}
	
	/**
	 * Renames the specified album.
	 * @param user the user whose photo albums are being accessed
	 * @param oldAlbumName the name of the album to be renamed
	 * @param newAlbumName the desired name of the album
	 */
	public void renameAlbum(User user, String oldAlbumName, String newAlbumName) {
		Album temp = user.getAlbum(oldAlbumName);
		if (temp == null) {
			System.out.println("Album: " + oldAlbumName + " does not exist. Cannot rename album.");
		}
		for (int i = 0; i < user.albumCollection.size(); i++) {
			if(newAlbumName.equals(user.albumCollection.get(i).getAlbumName())) {
				System.out.println("Album: " + newAlbumName + " already exists. Cannot rename album.");
				return;
			}
		}
		System.out.println("Album: " + oldAlbumName + " is renamed to " + newAlbumName);
		user.renameAlbum(oldAlbumName, newAlbumName);
	}
	
	/**
	 * Lists all existing users.
	 */
	public void listusers() {
		
		// Check. See if the userList is empty.
		if (backendmodel.userList == null) {
			System.out.println("no users exist");
		} else {
			for (int i = 0 ; i < backendmodel.userList.size(); i++) {
				System.out.println(backendmodel.userList.get(i).getUserID());
			}
		}
	}
	/**
	 * Adds a new user with the given userID and userName parameters.
	 * @param userID the user's unique ID that is used to log into interactive mode
	 * @param userName the user's full name
	 */
	public void adduser(String userID, String userName) {
		// Error Check. See if user with userID & userName already exist
		// Initialize userList if it is null.
		if (backendmodel.userList == null) {
			backendmodel.userList = new ArrayList<User>();
		}
		for(int i = 0; i < backendmodel.userList.size(); i++) {
			if (backendmodel.getUser(userID) != null) {
				System.out.println("user " + userID + " already exists with name " + backendmodel.getUser(userID).getUserName());
				return;
			}
		}
		
		// Success. Call model methods and add & write the user.
		System.out.println("created user " + userID + " with name " + userName);
		ArrayList<Album> albumCollection;
		albumCollection = new ArrayList<Album>();
		backendmodel.adduser(userID, userName, albumCollection);
		write(userID);
	}
	
	/**
	 * Deletes an existing user, identified by user ID.
	 * @param userID the user's unique ID that is used to log into interactive mode
	 */
	public void deleteuser(String userID) {
		// Check if the user exists first
		if(backendmodel.getUser(userID) == null) {
			System.out.println("user " + userID + " does not exist");
		
		}else{
			backendmodel.deleteuser(userID);
			System.out.println("deleted user " + userID);
		}
		
	}
	/**
	 * Edits the caption of an existing photo
	 * @param user the user whose photo albums are being accessed
	 * @param fileName the file path name for the photo object
	 * @param newCaption the desired caption
	 */
	public void editCaption(User user, String fileName, String newCaption){
		boolean found = false;
		// Search in entire album collection for photo
		for(int i = 0; i < user.albumCollection.size(); i++) {
			Album currAlbum = user.albumCollection.get(i);
			if(currAlbum.hasPhoto(fileName)) {
				currAlbum.getPhoto(fileName).editCaption(currAlbum.getPhoto(fileName), newCaption);
				found = true;
			}
		}
		if(!found){
			System.out.println("Error: photo " + fileName + " does not exist");
		}

	}
	/**
	 * Reads a user in from memory from the specified unique user ID.
	 * @param userID the user's unique ID that is used to log into interactive mode
	 */
	public void read(String userID) {
	}
	/**
	 * Writes a user to memory from the specified unique user ID.
	 * @param userID the user's unique ID that is used to log into interactive mode
	 */
	public void write(String userID) {
		User user = backendmodel.getUser(userID);
		if (user == null){
			System.out.println("Error. The user does not exist with the given userID");
		} else {
			try {
				backendmodel.writeuser(user);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Logs into interactive mode and begins the session.
	 * @param userID the user's unique ID that is used to log into interactive mode
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public User login(String userID) throws IOException, ClassNotFoundException {
		User user = backendmodel.getUser(userID);
		if (user == null) 
			System.out.println("user " + userID + " does not exist");
		else {
			backendmodel.readuser(userID);
			//InteractiveView.interactive(user);
			
		}
		return null;
	}
	/**
	 * Logs out of interactive mode and ends the session.
	 */
	public void logout(){
		try {
			backendmodel.saveUserList(backendmodel.userList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}