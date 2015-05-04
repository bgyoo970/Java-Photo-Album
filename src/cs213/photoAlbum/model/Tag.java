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

/**
 * 
 * @author Brian Yoo
 *
 */
public class Tag implements Serializable{
	/**
	 * Default number given to classes that implements Serializable. Prevents invalid class exceptions.
	 */
	private static final long serialVersionUID = -932871094387394564L;
	/**
	 * Used to distinguish tags from one another.
	 */
	protected String tagType;
	/**
	 * Used to give tags a specified value.
	 */
	protected String tagValue;
	
	/**
	 * Default constructor. Sets the tag values to null.
	 */
	public Tag() {
		tagType = "";
		tagValue = "";
	}
	/**
	 * Constructor. Sets the tag values to the parameters given.
	 * @param tagType the specific type of tag to be added
	 * @param tagValue the value of the tag
	 */
	public Tag(String tagType, String tagValue) {
		this.tagType = tagType;
		this.tagValue = tagValue;
	}
	/**
	 * Obtains the tagType of the specified tag
	 * @return tagType
	 */
	public String getTagType(){
		return this.tagType;
	}
	/**
	 * Obtains the tagValue of the specified tag
	 * @return tagValue
	 */
	public String getTagValue(){
		return this.tagValue;
	}

}
