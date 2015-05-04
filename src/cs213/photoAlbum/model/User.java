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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * 
 * @author Brian Yoo
 *
 */
public class User implements Serializable{
	/**
	 * Default number given to classes that implements Serializable. Prevents invalid class exceptions.
	 */
	private static final long serialVersionUID = 1796872472004819270L;
	/**
	 * An ID unique to each user used to login and enter interactive mode.
	 */
	public String userID;
	/**
	 * The user's full name.
	 */
	public String userName;
	/**
	 * The user's album collection.
	 */
	public ArrayList<Album> albumCollection;
	Backend backend = new Backend();
	/**
	 * Sets up a person's profile with the ID used to log in to the system, their name, and their albums.
	 * @param userID ID used to specify each user from each other
	 * @param userName full name of user
	 * @param albumCollection the user's albums
	 */
	public User(String userID, String userName, ArrayList<Album> albumCollection) {
		this.userID = userID;
		this.userName = userName;
		this.albumCollection = albumCollection;
	}
	
	/**
	 * 
	 * Creates a new album.
	 * @param albumName the name of the album to be created
	 * @return returns a new Album object with the album name set as the given parameter.
	 */
	public Album createAlbum(String albumName) {
		Album album = new Album(albumName);
		backend.listMod.addElement(albumName);
		return album;
	}
	
	/**
	 * Deletes an existing album.
	 * @param albumName the specified album to be deleted
	 */
	public void deleteAlbum(String albumName) {
		Album album = getAlbum(albumName);
		albumCollection.remove(album);
		backend.listMod.removeElement(albumName);
	}
	
	/**
	 * Lists all albums with their number of photos and the range of dates that the photos were taken.
	 */
	public void listAlbums() {
		String albumName;
		Album currAlbum;
		Date startime, endtime;
		// Check if album collection is empty
		if (albumCollection.size() == 0) {
			System.out.println("No albums exist for user: " + this.userID);
		}
		else {
			//sort alphabetically first
			Collections.sort(albumCollection, new Comparator<Album>() {
		        public int compare(Album a1, Album a2) {
		       		return a1.albumName.compareTo(a2.albumName);
		        }
		    });
			
			System.out.println("Albums for user: " + this.userID);
			for(int i = 0; i < albumCollection.size(); i++) {

				// Check if individual albums have photos
				// if not, just print number of photos and no date
				if(albumCollection.get(i).photoct == 0){
					System.out.println(albumCollection.get(i).albumName + " number of photos: " + albumCollection.get(i).photoct);
				} else {
					currAlbum = albumCollection.get(i);
					albumName = currAlbum.albumName;
					startime = currAlbum.photoAlbum.get(0).cal.getTime();
					endtime = currAlbum.photoAlbum.get(albumCollection.get(i).photoct - 1).cal.getTime();
					
					System.out.println(albumName + " number of photos: " + currAlbum.photoct + ", " + startime.toString() + " - " + endtime.toString());
				}
			}
		}
	}
	
	/**
	 * Gives an existing album a new name.
	 * @param album specified album to be renamed
	 * @param newname the desired name
	 */
	public void renameAlbum(String album, String newname) {
		Album a = getAlbum(album);
		a.setAlbumName(newname);
		backend.listMod.addElement(newname);
		backend.listMod.removeElement(album);
	}
	/**
	 * Prints all details of the specified photo: its file name, which albums it belongs to, the date it was last edited, its caption, and all the tags belonging to it.
	 * @param fileName the path to the desired photo
	 */
	public void listPhotoInfo(String fileName) {
		Album currAlbum;
		Photo targetPhoto = null;
		boolean isValid = false;
		ArrayList<String> temp = new ArrayList<String>();
		
		// Validates if we can print the output. Also determines which albums has the given fileName
		for(int i = 0; i < albumCollection.size(); i++) {
			currAlbum = albumCollection.get(i);				// Gets an album
			if (currAlbum.hasPhoto(fileName)) {				// if the album has the given photo
				isValid = true;
				temp.add(currAlbum.albumName);
				targetPhoto = currAlbum.getPhoto(fileName);
			}
		}
		
		// Prints the output if isValid is true.
		if (isValid) {
			System.out.println("Photo file name: " + fileName);
			System.out.print("Album: " + temp.get(0));
			for(int i = 1; i < temp.size(); i++) {
				System.out.print(", " + temp.get(i));
			} System.out.println("");
			
			System.out.println("Date: " + targetPhoto.cal.getTime().toString());
			System.out.println("Caption: " + targetPhoto.caption);
			System.out.println("Tags: ");
			targetPhoto.printTagList();
		}
	}
	
	/**
	 * 
	 * Retrieves all photos and their details taken within the specified range of dates, in chronological order.
	 * @param startDate the first date to be checked
	 * @param endDate the last date to be checked
	 * 
	 */
	public ArrayList<Photo> getPhotosByDate(String startDate, String endDate) {
		System.out.println("Photos for user " + this.userID + " in range " + startDate.toString() + " to " + endDate.toString() + ":");
		if ( checkValidDate(startDate) && checkValidDate(endDate) ) {
			
			Calendar start = parseStringintoDate(startDate);
			Calendar end = parseStringintoDate(endDate);
			
			ArrayList<Photo> temp = new ArrayList<Photo>();
			Date currDate;
			Calendar currCal;
			String currCaption;
			Photo currPhoto;
			
			// Check if the list of albums is empty or not
			if (this.albumCollection != null) {
				// Go through each album
				for (int i = 0; i < this.albumCollection.size(); i++) {
					// Go through each photo in each album
					int photoAlbumSize = this.albumCollection.get(i).photoAlbum.size();
					for (int j = 0; j < photoAlbumSize; j++) {
						currCal = this.albumCollection.get(i).photoAlbum.get(j).cal;
						currCaption = this.albumCollection.get(i).photoAlbum.get(j).caption;
						currPhoto = this.albumCollection.get(i).photoAlbum.get(j);
						currDate = this.albumCollection.get(i).photoAlbum.get(j).cal.getTime();
						// Check if the current photo's date is in between the range of the given parameters
						if (currCal.after(start) == true && currCal.before(end) == true) {
							temp.add(currPhoto);
						}
					}
				}
			}
			// Sort the dates in chronological order
			// temp is a single album.
			temp = sortbyDate(temp);
			// return temp;
			
			// Print the output
			for (int i = 0; i < temp.size(); i++) {
				currCal = temp.get(i).cal;
				currCaption = temp.get(i).caption;
				currPhoto = temp.get(i);
				currDate = temp.get(i).cal.getTime();
				
				System.out.print(currCaption + " - Album: ");
				
				ArrayList<String> tempAlbumList = getAlbumsbyPhotos(currPhoto);
				System.out.print(tempAlbumList.get(0));
				for(int k = 1; k < tempAlbumList.size(); k++) {
					System.out.print(", " + tempAlbumList.get(k));
				}
				
				System.out.println(" - Date: " + currDate.toString());
			}
			return temp;
		}
		return null;
	}
	/**
	 * Sorts the ArrayList in chronological order, from the latest date to the most recent date.
	 * @param list the photo list to be sorted
	 * @return the sorted ArrayList photo object.
	 */
	private ArrayList<Photo> sortbyDate(ArrayList<Photo> list) {
		ArrayList<Photo> temp = new ArrayList<Photo>();
		Photo photemp;
		int ct = 0;
		if (!list.isEmpty()) {
			temp.add(list.get(ct));
			for (int i = 1; i < list.size(); i++) {
				// before
				if(temp.get(ct).cal.getTime().compareTo(list.get(i).cal.getTime()) < 0) {
					temp.add(list.get(i));
					ct++;
				}
				// after
				else if (temp.get(ct).cal.getTime().compareTo(list.get(i).cal.getTime()) > 0) {
					photemp = temp.get(ct);
					temp.set(ct, list.get(i));
					temp.add(photemp);
					ct++;
				}
				else {
					temp.add(list.get(i));
					ct++;
				}
			}
			return temp;
		}
		
		return temp;
	}
	
	/**
	 * Checks if the given value is a valid date.
	 * @param date the value that will be determined as a valid date
	 * @return a boolean value that will determine if the date is valid or not
	 */
	public boolean checkValidDate(String date) {
		if(date == null){
			return false;
		}
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy-kk:mm:ss");
		format.setLenient(false);
		try {
			// if the value is not valid, it will throw an exception.
			@SuppressWarnings("unused")
			Date tempd = format.parse(date);
		}catch (ParseException e){
 
			System.out.println("Error, please enter a valid date. The format of a valid date is: MM/dd/yyyy-kk:mm:ss");
			return false;
		}
		return true;
	}
	
	/**
	 * Parses a string typed as a date in the format of MM/DD/YYYY-HH:MM:SS into a Calendar object
	 * @param str the string date to be parsed into a Calendar object
	 * @return the Calendar object that represents the date of the given string parameter
	 */
	private Calendar parseStringintoDate(String str) {
		StringTokenizer stok = new StringTokenizer(str, "-:/");
		
		String month = stok.nextToken();
		String day = stok.nextToken();
		String year = stok.nextToken();
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(stok.nextToken()), 
				Integer.parseInt( stok.nextToken()), Integer.parseInt( stok.nextToken()));
		
		return cal;
	}
	
	/**
	 * Returns a list of albums that contain the specified photo object.
	 * @param photo the specified photo object that may be located in multiple albums
	 * @return the ArrayList<String> that holds all of the album names that contains the given photo parameter
	 */
	private ArrayList<String> getAlbumsbyPhotos(Photo photo) {
		ArrayList<String> albumNameList = new ArrayList<String>();
		for (int i = 0; i < this.albumCollection.size(); i++) {
			int photoAlbumSize = this.albumCollection.get(i).photoAlbum.size();
			for (int j = 0; j < photoAlbumSize; j++) {
				Photo currPhoto = this.albumCollection.get(i).photoAlbum.get(j);
				if (currPhoto.photoName.equals(photo.photoName)) 
					albumNameList.add(this.albumCollection.get(i).albumName);
			}
		}
		return albumNameList;
	}
	
	/**
	 *
	 * Retrieves all photos and their details that have all specified tags, in chronological order. Tags can be specified with or without their types.
	 * @param stringStream the tags (with or without their tag types) to be checked.
	 * 
	 */
	public ArrayList<Photo> getPhotosByTag(String stringStream) {
		ArrayList<Photo> validPhotos = new ArrayList<Photo>();
		boolean isValid = false;
		// Parse the tags from the input
		String[] tagList = processStr(stringStream);
		if (tagList == null) {
			System.out.println("Error: Please enter the correct format when entering the tags.");
			System.out.println("Note. Tags can be specified with or without their types: [<tagType>:]\"<tagValue>\" [,<tagType>:]\"<tagValue>\"]..." );
			return null;
		}
		
		// Strip of all quotes.
		stringStream = stringStream.replace("\"", "");
        for(int i = 0; i < tagList.length; i++){
        	tagList[i] = tagList[i].replace("\"", "");
        }
        
        while ( stringStream.substring(stringStream.length()-1).equals(",")) {
        	stringStream = stringStream.substring(0, stringStream.length()-1);
        }
		
		// Search which photos have the tags in the list.
		for(int i = 0; i < this.albumCollection.size(); i++) {
			Album currAlbum = this.albumCollection.get(i);
			for (int j = 0; j < currAlbum.photoAlbum.size(); j++) {
				Photo currPhoto = currAlbum.photoAlbum.get(j);
				// This duplicate check is to prevent the same photo outputting multiple times. This method will already
				// display all of the albums that the photos exists in
				if (duplicateCheck(validPhotos, currPhoto)) {
					continue;
				}
				if(currPhoto.tagList.isEmpty()) {
					// Weird quirk. When the Photo has an EMPTY tag list, it likes to adapts its values
					// to the ones inside the tagList made from processStr(stringStream)
					// Putting this if statement here to prevent it from messing up hasTags.
					continue;
				} 
				else if(!hasTags(currPhoto.tagList, tagList)){
					continue;
				}
				else if (hasTags(currPhoto.tagList, tagList)) {
					validPhotos.add(currPhoto);
					isValid = true;
				}
			}
		}
		
		
		if (isValid) {
			System.out.println("Photos for user " + this.userID + " with tags " + stringStream + ":");
			
			// Sort the dates in chronological order
			validPhotos = sortbyDate(validPhotos);
			// return validPhotos;
			
			for (int i = 0; i < validPhotos.size(); i++) {
				Photo currPhoto = validPhotos.get(i);
				String currCaption = currPhoto.caption;
				Date currDate = currPhoto.cal.getTime();
				ArrayList<String> tempAlbumList = getAlbumsbyPhotos(currPhoto);
					
				System.out.print(currCaption + " - Album: ");
				System.out.print(tempAlbumList.get(0));
				for(int j = 1; j < tempAlbumList.size(); j++) {
					System.out.print(", " + tempAlbumList.get(j));
				}
				System.out.println(" - Date: " + currDate.toString());
			}
			return validPhotos;
		}
		return null;
	}
	
	/**
	 * Used for getPhotosByTag. Checks if valid photos array has any duplicates.
	 * @param validPhotos the array list to be checked
	 * @param photo the photo that is being checked
	 * @return returns true if validPhotos contains duplicates, else returns false.
	 */
	private boolean duplicateCheck(ArrayList<Photo> validPhotos, Photo photo) {
		for (int i = 0; i < validPhotos.size(); i++) {
			if (validPhotos.get(i).photoName.equals(photo.photoName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Parses the given string stream into tags and stores the tags into a list.
	 * @param stringStream the string input to be parsed into tag values
	 * @return an array filled with tag objects
	 */
	public String[] processStr(String stringStream) {
		String str = stringStream;
		//Split it by commas first to see if multiple tags to check for
		String[] arr = str.split("\\s*,(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		for(int i = 0; i < arr.length; i++){
			if (arr[i].equals(":")) {
				return null;
			}
		}
		
		// Check if the tagType or the tagValue is null in any of the given tags.
		// If so, return null. Which will return an error statement outside of this method.
		for (int j = 0; j < arr.length; j++) {
			String[] check = arr[j].split(":(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			// Only case where there can be one input is if the entry is only the tagValue
			if (check.length == 1 && check[0].substring(0, 1).equals("\"") && check[0].substring(check[0].length()-1).equals("\"")) {
				arr[j] = arr[j].replace("\"", "");
				if (arr[j].equals("")) {
					return null;
				}
				continue;
			}
			
			// Otherwise invalid
			else if (check.length != 2) {
				return null;
			}
			
			// For the inputs with the tagType and tagValue
			for (int k = 0; k < check.length; k++) {
				if (check[k].equals("")) {
					return null;
				}
				if (check[k].substring(0, 1).equals("\"") && check[k].substring(check[k].length()-1).equals("\"") && check[k].length() == 2) {
					return null;
				}
			}
		}
		
		//	 STRIP THE QUOTES AFTER FINISHING THIS.
		return arr;
	}
	/**
	 * Compares two ArrayLists of Tag objects to see if currPhotoTagList encompasses all of the tags in inputTagList
	 * @param currPhotoTagList the list of the Tag objects of the photo to be checked
	 * @param inputTagList the list of tag objects obtained from the string stream
	 * @return a boolean value determining if the currPhotoTagList has all of the given tags.
	 */
	private boolean hasTags(ArrayList<Tag> currPhotoTagList, String[] inputTagList) {
		String tagType, tagValue;
		String[] temp;
		int tagct = inputTagList.length;
		int ct = 0;
		
		//go through both lists to find matches
		for(int i = 0; i < inputTagList.length; i++){
			for(int j = 0; j < currPhotoTagList.size(); j++){
				// Case 1: tagType and tagValue
				if(inputTagList[i].contains(":")){
					temp = inputTagList[i].split(":");
					tagType = temp[0];
					tagValue = temp[1];
					tagValue = tagValue.replace("\"", "");
					
					//match
					if(currPhotoTagList.get(j).tagType.equals(tagType) && currPhotoTagList.get(j).tagValue.equals(tagValue)){
						ct++;
					}
				}
				// Case 2: only tagValue
				else{
					if(currPhotoTagList.get(j).tagValue.equals(inputTagList[i])){
						ct++;
					}
				}
			}
		}	
		if(ct == tagct){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * Moves photo from oldAlbum into newAlbum. If the photo already exists in newAlbum, returns without doing anything.
	 * @param fileName the path to the specified photo to be moved
	 * @param oldAlbum the album containing the photo to be moved
	 * @param newAlbum the desired album to add the photo to
	 * 
	 */
	public void movePhoto(String fileName, String oldAlbum, String newAlbum) {
		Album oldal = getAlbum(oldAlbum);
		Photo p = oldal.getPhoto(fileName);
		oldal.removePhoto(fileName);
		
		Album newal = getAlbum(newAlbum);
		newal.addPhoto(p);
	}
	/**
	 * Gets the album object from the given parameter.
	 * @param albumName the name of the desired album 
	 * @return returns the album corresponding to the given parameter name
	 */
	public Album getAlbum(String albumName) {
		for(int i = 0; i < albumCollection.size(); i++) {
			if(albumCollection.get(i).albumName.equals(albumName)) {
				return albumCollection.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Obtains the user ID of a specific user.
	 * @return the user ID of a specific user
	 */
	public String getUserID() {
		return this.userID;
	}
	
	/**
	 * Obtains the user name of a specific user.
	 * @return the user name of a specific user
	 */
	public String getUserName() {
		return this.userName;
	}
	
}