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

package cs213.photoAlbum.simpleview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cs213.photoAlbum.control.DataControl;
import cs213.photoAlbum.model.*;

/**
 * 
 * @author Michelle Gavino
 *
 */
public class InteractiveView {
    /**
     * DataControl object used as a model to help access and return values.
     * Will be used for data access object methods
     */
    static DataControl control = new DataControl();
   
    /**
     * Manages the interactive view of the program. Accepts the user's commands by use of System.in and parses accordingly.
     * @param user the user currently logged in to interactive mode
     * @throws NullPointerException
     * @throws IOException
     */
    public static void interactive(User user) throws NullPointerException, IOException{
        while(true){
            //parse the command
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String str = br.readLine();
            if(str == null){
            	//TEMP
            	System.out.println("No inputs given. Quitting.");
            	return;
            }
            //splits by spaces except when in quotations
            String[] arr = str.split("\\s(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            for(int i = 0; i < arr.length; i++){
            	arr[i] = arr[i].replace("\"", "");
            }
            
			if(arr[0].equalsIgnoreCase("createalbum")){
				//make sure has enough arr
				if(arr.length != 2){
					System.out.println("Error. The command 'createAlbum' must have 2 arguments");
					System.out.println("Format of the command 'createAlbum' must be: createAlbum \"<albumName>\"");
				}else{
					control.createAlbum(user,arr[1]);
				}
			}
			else if(arr[0].equalsIgnoreCase("deletealbum")){
				if(arr.length != 2){
					System.out.println("Error. The command 'deleteAlbum' must have 2 arguments");
					System.out.println("Format of the command 'deleteAlbum' must be: deleteAlbum \"<albumName>\"");
				}else{
					control.deleteAlbum(user, arr[1]);
				}
			}
			else if(arr[0].equalsIgnoreCase("listalbums")){
				if(arr.length != 1){
					System.out.println("Error. The command 'listAlbums' must have 1 argument");
					System.out.println("Format of the command 'listAlbums' must be: listAlbums");				
				}else{
					control.listAlbums(user);
				}
			}
			else if(arr[0].equalsIgnoreCase("renamealbum")){
				if(arr.length != 3){
					System.out.println("Error. The command 'renameAlbum' must have 3 arguments");
					System.out.println("Format of the command 'renameAlbum' must be: renamealbum \"<oldAlbumName>\" \"<newAlbumName\"");				
				}else{
					control.renameAlbum(user, arr[1], arr[2]);
				}
			}
			else if(arr[0].equalsIgnoreCase("listphotos")){
				if(arr.length != 2){
					System.out.println("Error. The command 'listPhotos' must have 2 arguments");
					System.out.println("Format of the command 'listPhotos' must be: listPhotos \"<albumName>\"");
				}else{
					control.listPhotos(user, arr[1]);
				}
			}
			else if(arr[0].equalsIgnoreCase("addphoto")){
				if(arr.length != 4){
					System.out.println("Error. The command 'addPhoto' must have 4 arguments");
					System.out.println("Format of the command 'addPhoto' must be: addPhoto \"<fileName>\" \"<caption>\" \"<albumName>\"");
				}else{
					control.addPhoto(user, arr[1], arr[2], arr[3]);
				}
			}		
			else if(arr[0].equalsIgnoreCase("movephoto")){
				if(arr.length != 4){
					System.out.println("Error. The command 'movePhoto' must have 4 arguments");
					System.out.println("Format of the command 'movePhoto' must be: movePhoto \"<fileName>\" \"<oldAlbumName>\" \"<newAlbumName>\"");
				} else {
					control.movePhoto(user, arr[1], arr[2], arr[3]);
				}
			}	
			else if(arr[0].equalsIgnoreCase("removephoto")){
				if(arr.length != 3){
					System.out.println("Error. The command 'removePhoto' must have 2 arguments");
					System.out.println("Format of the command 'removePhoto' must be: removePhoto \"<fileName>\" \"<albumName>\"");
				}else{
					control.removePhoto(user, arr[1], arr[2]);
				}
			}
			else if(arr[0].equalsIgnoreCase("editcaption")){
				if(arr.length != 3){
					System.out.println("Error. The command 'editCaption' must have 3 arguments");
					System.out.println("Format of the command 'editCaption' must be: editCaption \"<fileName>\" \"<newCaption>\"");
				}else{
					control.editCaption(user, arr[1], arr[2]);
				}
			}
			else if(arr[0].equalsIgnoreCase("addtag")){
				if(arr.length != 3){
					System.out.println("Error. The command 'addTag' must have 3 arguments");
					System.out.println("Format of the command 'addTag' must be: addTag \"<fileName>\" <tagType>:\"<tagValue>\"");
				}else{
					control.addTag(user, arr[1], arr[2]);
				}
			}
			else if(arr[0].equalsIgnoreCase("deletetag")){
				if(arr.length != 3){
					System.out.println("Error. The command 'deleteTag' must have 3 arguments");
					System.out.println("Format of the command 'deleteTag' must be: deleteTag \"<fileName>\" <tagType>:\"<tagValue>\"");
				}else{
					control.deleteTag(user, arr[1], arr[2]);
				}
			}
			else if(arr[0].equalsIgnoreCase("listphotoinfo")){
				if(arr.length != 2){
					System.out.println("Error. The command 'listPhotoInfo' must have 2 arguments");
					System.out.println("Format of the command 'listPhotoInfo' must be: listPhotoInfo \"<fileName>\"");
				}else{
					control.listPhotoInfo(user, arr[1]);
				}
			}
			else if(arr[0].equalsIgnoreCase("getphotosbydate")){
				if(arr.length != 3){
					System.out.println("Error. The command 'getPhotosByDate' must have 3 arguments");
					System.out.println("Format of the command 'getPhotosByDate' must be: getPhotosByDate <startDate> <endDate>");
				}else{
					control.getPhotosByDate(user, arr[1], arr[2]);
				}
			}
			else if(arr[0].equalsIgnoreCase("getphotosbytag")){
				if(arr.length == 1){
					System.out.println("Error. The command 'getPhotosByTag' must have at least 2 arguments");
					System.out.println("Format of the command 'getPhotosByTag' must be: getPhotosByTag [<tagType>:]\"<tagValue>\" [,[<tagType>:]\"<tagValue>\"]...");
				}else{
					control.getPhotosByTag(user, arr[1]);
				}
			}
			else if(arr[0].equalsIgnoreCase("logout")){
				if(arr.length != 1){
					System.out.println("Error. The command 'logout' must have 1 argument");
					System.out.println("Format of the command 'logout' must be: logout");
				}else{
					control.logout();
					break;
				}
				
			}
			else {
				System.out.println("No commands were recognized, please enter a valid command");
			}
		
        }
    }
   
}