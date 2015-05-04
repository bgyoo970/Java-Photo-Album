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
import java.util.ArrayList;

/**
 * 
 * @author Brian Yoo
 *
 */
public class Album implements Serializable{
	
	/**
	 * Default number given to classes that implement Serializable. Prevents invalid class exceptions.
	 */
	private static final long serialVersionUID = -391913447643519491L;
	/**
	 *  Used to distinguish albums from each other.
	 *  A single user may not have duplicate album names, but the name may be duplicated across users.
	 */
	protected String albumName;
	/**
	 *  Data structure used to store Photos in a specific album.
	 */
	protected ArrayList<Photo> photoAlbum;
	
	/**
	 * A count used to determine the amount of photos within an album
	 */
	protected int photoct;
	
	/**
	 * Assigns the specified albumName to a photoAlbum.
	 * @param albumName the desired name of the album
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		this.photoAlbum = new ArrayList<Photo>();
		this.photoct = 0;
	}
	/**
	 * 
	 * Adds existing photo to specified album. 
	 * @param photo the specified photo to be added
	 * 
	 */
	public void addPhoto(Photo photo) {
		this.photoAlbum.add(photo);
		this.photoct++;
	}
	
	/**
	 * 
	 * Adds new photo with its desired caption.
	 * @param fileName the path to the photo to be added
	 * @param caption the caption of the specified photo
	 * 
	 */
	public void addPhoto(String fileName, String caption){
		this.photoAlbum.add(new Photo(fileName, caption));
		this.photoct++;
	}
	
	/**
	 * 
	 * Removes a photo.
	 * @param fileName the path to the specified photo to be removed
	 * 
	 */
	public void removePhoto(String fileName) {
		this.photoAlbum.remove(getPhoto(fileName));
		if(photoct != 0)
			this.photoct--;
	}
	
	/**
	 *
	 * Prints out all photos in the specified album and the date they were created.
	 * 
	 */
	public void listPhotos() {
			System.out.println("Photos for album " + this.albumName + ":");
			for(int i = 0; i < photoAlbum.size(); i++) {
				System.out.println(photoAlbum.get(i).photoName + " - " + photoAlbum.get(i).cal.getTime());
			}
	}
	
	/**
	 * Gets the photo object from the given file name.
	 * @param fileName the path to the specified photo 
	 * @return returns the photo object corresponding to the given file name. Returns null if photo does not exist.
	 */
	public Photo getPhoto(String fileName) {
		for(int i = 0; i < photoAlbum.size(); i++) {
			if(photoAlbum.get(i).photoName.equals(fileName)) {
				return photoAlbum.get(i);
			}
		}
		return null;
	}
	/**
	 * Checks if an album contains the specified photo.
	 * @param fileName the path to the specified photo
	 * @return returns true if an album contains the photo, else returns false.
	 */
	// A check for add/remove etc. to work correctly
	public boolean hasPhoto(String fileName) {
		if (getPhoto(fileName) != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Gets the name of an album.
	 * @return the name of the album
	 */
	public String getAlbumName() {
		return this.albumName;
	}
	
	/**
	 * Allows user to rename specified album.
	 * @param albumName the desired album whose name is to be changed
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	
	/**
	 * Gets the photo count of an album.
	 * @return returns the number of photos in an album.
	 */
	public int getPhotoct() {
		int temp = 0;
		for(int i = 0; i < photoAlbum.size(); i++) {
			temp++;
		}
		photoct = temp;
		return this.photoct;
	}
	
	/**
	 * Gets the photoalbum of a user
	 * @return ArrayList photoAlbum
	 */
	public ArrayList<Photo> getPhotoAlbum(){
		return this.photoAlbum;
	}
	
	/**
	 * Sets the photoalbum of a user
	 */
	public void setPhotoAlbum(ArrayList<Photo> album){
		this.photoAlbum = album;
	}
	
}
