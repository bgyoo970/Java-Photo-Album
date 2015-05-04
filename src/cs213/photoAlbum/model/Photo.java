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
import java.util.Calendar;

/**
 * 
 * @author Michelle Gavino
 *
 */
public class Photo implements Serializable{
	/**
	 * Default number given to classes that implements Serializable. Prevents invalid class exceptions.
	 */
	private static final long serialVersionUID = 4267375514811775928L;
	/**
	 * A unique pathname assigned to Photo objects for distinguishing photos
	 */
	protected String photoName;
	/**
	 * A short description of the Photo
	 */
	protected String caption;
	/**
	 * The list of Tag objects assigned to the Photo
	 */
	protected ArrayList<Tag> tagList;
	/**
	 * A descriptor that will entail the date and time of the photo taken
	 */
	protected Calendar cal;
	
	// Set up for default constructor
	// Initialize all to null
	/**
	 * Default Constructor. Sets the Photo specifications to null.
	 */ 
	public Photo() {
		this.photoName = "";
		this.caption = "";
		this.tagList = new ArrayList<Tag>();
		this.cal = Calendar.getInstance();
		this.cal.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * Constructor. Sets the Photo specifications to the parameters given.
	 * 
	 * @param fileName the unique pathname assigned to each photo
	 * @param caption a short description of the photo
	 */
	public Photo(String fileName, String caption) {
		this.photoName = fileName;
		this.caption = caption;
		this.tagList = new ArrayList<Tag>();
		this.cal = Calendar.getInstance();
		this.cal.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * Adds a tag to the specified photo by the tagType and the tagValue. 
	 * @param tagType the specific type of tag to be added
	 * @param tagValue the value of the tag
	 */
	public void addTag(String tagType, String tagValue) {
		this.tagList.add(new Tag(tagType, tagValue));
	}
	
	/**
	 * Deletes a tag of the specified photo with the given tagType and tagValue parameters.
	 * @param tagType the specific type of tag to be deleted
	 * @param tagValue the value of the tag
	 */
	public void deleteTag(String tagType, String tagValue) {
		this.tagList.remove(getTag(tagType, tagValue));
	}
	
	// Used to help delete a tag.
	/**
	 * Searches through the Tag array list to search for a specific tag
	 * @param tagType the type of the tag 
	 * @param tagValue the value of the tag
	 * @return returns the tag of the specified parameters.
	 */
	public Tag getTag(String tagType, String tagValue) {
		for(int i = 0; i < tagList.size(); i++) {
			if (tagList.get(i).tagType.equals(tagType) && tagList.get(i).tagValue.equals(tagValue)) {
				return tagList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Returns the tagList of a photo
	 * @return the ArrayList<Tag> tagList
	 */
	public ArrayList<Tag> getTagList() {
		return this.tagList;
	}
	
	/**
	 * Sets the tagList of a photo to the specified parameter
	 * @param temp the ArrayList<Tag> object to be set as the photo's tagList
	 */
	public void setTagList(ArrayList<Tag> temp) {
		this.tagList = temp;
	}
	
	/**
	 * This method prints the tags of a photo
	 */
	public void printTagList() {
		// Print location tag first
		for(int i = 0; i < tagList.size(); i++) {
			if(tagList.get(i).tagType.equalsIgnoreCase("location"))
				System.out.println(tagList.get(i).tagType + ":" + tagList.get(i).tagValue);
		}
		
		// Print person tags next
		for(int i = 0; i < tagList.size(); i++) {
			if(tagList.get(i).tagType.equalsIgnoreCase("person"))
				System.out.println(tagList.get(i).tagType + ":" + tagList.get(i).tagValue);
		}
		
		// Print any other tags last
		for(int i = 0; i < tagList.size(); i++) {
			if(tagList.get(i).tagType.equalsIgnoreCase("location") || tagList.get(i).tagType.equalsIgnoreCase("person"))
				continue;
			else
				System.out.println(tagList.get(i).tagType + ":" + tagList.get(i).tagValue);
		}
		
		// Error. No tags Exist
		if(tagList.size() == 0) {
			System.out.println("No tags exist.");
		}
	}
	
	/**
	 * Checks if the photo has the specified tag.
	 * @param photo the specified photo to be checked
	 * @param tag the specified tag to be checked
	 * @return returns true if the specified photo has the tag, else returns false.
	 */
	public boolean hasTag(Photo photo, Tag tag) {
		// This for loop makes "currTag" the instance and "tagList" the list to be iterated through
		for(Tag currTag : tagList) {
			if (currTag.equals(tag))
				return true;
		}
		return false;
	}
	
	/**
	 * Creates a new photo.
	 * @return Returns the new photo created
	 */
	public Photo createPhoto() {
		Photo newPhoto = new Photo();
		return newPhoto;
	}
	
	/**
	 * Creates a new photo with the specified file name and caption.
	 * @param filename the path of the photo to be created
	 * @param caption the desired caption of the new photo
	 * @return returns the new photo created.
	 */
	public Photo createPhoto(String filename, String caption) {
		Photo newPhoto = new Photo(filename, caption);
		return newPhoto;
	}
	/**
	 * 
	 * Changes the caption of the specified photo.
	 * @param photo the desired photo whose caption is to be changed
	 * @param newCaption the desired new caption
	 * 
	 */
	public void editCaption(Photo photo, String newCaption) {
		System.out.println("Editing " + photoName + " caption from '" + caption + "' to '" + newCaption + "'");
		this.caption = newCaption;
	
	}
	
	/**
	 * Gets the caption of a photo.
	 * @return Returns the caption as a string
	 */
	// These get and sets are for editing the caption. Used for editCaption inside Album.
	public String getCaption() {
		return this.caption;
	}
	
	/**
	 * Sets the caption of a photo.
	 * @param caption the specified caption to be set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * gets the name of the specified photo
	 * @return the name of the photo
	 */
	public String getPhotoName(){
		return this.photoName;
	}
	/**
	 * gets the date and time taken of the specified photo
	 * @return the date and time of the photo
	 */
	public String getCal(){
		return this.cal.getTime().toString();
	}
}
