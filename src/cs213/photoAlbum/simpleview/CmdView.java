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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import cs213.photoAlbum.control.DataControl;

/**
 * 
 * @author Brian Yoo
 *
 */
public class CmdView implements Serializable{
	
	static DataControl control = new DataControl();

	/**
	 * Manages the command view of the program. This is where all of the command line arguments are parse and used.
	 * @param args the command line arguments
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
        String cwd = System.getProperty("user.dir");
        String path = cwd.concat(File.separator + "data");
        File dir = new File(path);
        if(!dir.exists()){
            System.out.println("Directory 'path/' does not exist. Creating directory..");
            dir.mkdir();
        }
		
		control.load();
		LoginFrame.login();

		
		/*
		// Make 'data/' directory if it doesn't exist yet
        String cwd = System.getProperty("user.dir");
        String path = cwd.concat(File.separator + "data");
        File dir = new File(path);
        if(!dir.exists()){
            System.out.println("Directory 'path/' does not exist. Creating directory..");
            dir.mkdir();
        }
		
		if (args.length == 0) {
			System.out.println("Error. No arguments were given");
			return;
		}
		
		control.load();

		
		if (args[0].equalsIgnoreCase("listusers")) {
			if (args.length == 1) {
				control.listusers();
			} else {
				System.out.println("Error. The command 'listusers' must have 1 argument");
				System.out.println("Format of the command 'listusers' must be: listusers");
			}
		}
		
		else if (args[0].equalsIgnoreCase("adduser")) {
			if (args.length == 3) {
				control.adduser(args[1], args[2]);
				control.save();
			} else {
				System.out.println("Error. The command 'adduser' must have 3 arguments");
				System.out.println("Format of the command 'adduser' must be: adduser <user id> \"<user name>\"");
			}
		}
		
		else if (args[0].equalsIgnoreCase("deleteuser")) {
			if (args.length == 2) {
				control.deleteuser(args[1]);
				control.save();
			} else {
				System.out.println("Error. The command 'deleteuser' must have 3 arguments");
				System.out.println("Format of the command 'deleteuser' must be: deleteuser <user id>");
			}
		}
		else if (args[0].equalsIgnoreCase("login")) {
			if (args.length == 2) {
				control.login(args[1]);
			} else {
				System.out.println("Error. The command 'login' must have 2 arguments");
				System.out.println("Format of the command 'login' must be: login <user id>");
			}
		}
		
		else {
			System.out.println("No commands were recognized, please enter a valid command");
		}
	*/
	}
	
	
}