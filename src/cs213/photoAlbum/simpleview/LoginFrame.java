package cs213.photoAlbum.simpleview;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import cs213.photoAlbum.control.DataControl;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.User;
/**
 * 
 * @author Michelle Gavino
 *
 */
public class LoginFrame extends JFrame {
	
	JLabel username = new JLabel("User Name: ");
	JTextField usernameText = new JTextField("");
	JButton login = new JButton("Go!");
	
	static Backend backend = new Backend();
	
	/**
	 * Constructor for the LoginFrame
	 * @param title The name for the current window
	 */
	public LoginFrame(String title) {
		super(title);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// Load the userList.
		try {
			backend.userList = backend.loadUserList();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// "User Name:" 
		c.gridx = 0; c.gridy = 1;
		c.insets = new Insets(0,20,0,0);
		add(username, c);
		
		// text field after User Name:
		c.gridx = 1; c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER; 
		c.weightx = 0.5;
		c.insets = new Insets(0,0,0,20);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(usernameText, c);

		// Go button
		c.gridx = 1; c.gridy = 2; 
		c.fill = GridBagConstraints.NONE; //reset
		c.insets = new Insets(10,0,0,0);
		c.anchor = GridBagConstraints.WEST;
		add(login, c);
		
		/*
		ActionListener mouseClick = new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (e.getSource() == mouseC)
	        }
		}*/
		
		usernameText.addFocusListener(new FocusAdapter(){
			// if mouse clicks in it
			public void focusGained(FocusEvent e){
            	usernameText.setText("");
            }
        });
		// Hit enter to log in
		usernameText.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	// Admin Frame
				if(usernameText.getText().equals("admin")){
					dispose(); //gets rid of login window
					AdminFrame.admin();
				}
				
				// Non-Admin Frame
				else{
					String input = usernameText.getText();
					if (input.contains(" ")) {
						usernameText.setText("Error: Invalid input");
						return;
					}
					User user = backend.getUser(input);
					if (user == null) {
						usernameText.setText("User '" + input + "' does not exist. Try again");
						login.requestFocus();
					}
					// User exists.
					else {
						String id = user.userID;
						try {
							control.login(id);
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						dispose();
						NonadminFrame.nonadmin(id);
					}
				}
			}
		});
		
		login.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				// Admin Frame
				if(usernameText.getText().equals("admin")){
					dispose(); //gets rid of login window
					AdminFrame.admin();
				}
				
				// Non-Admin Frame
				else{
					String input = usernameText.getText();
					if (input.contains(" ")) {
						usernameText.setText("Error: Invalid input");
						return;
					}
					User user = backend.getUser(input);
					if (user == null) {
						usernameText.setText("User '" + input + "' does not exist. Try again");
						login.requestFocus();
					}
					// User exists.
					else {
						String id = user.userID;
						try {
							control.login(id);
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						dispose();
						NonadminFrame.nonadmin(id);
					}
				}
			}
		});
			
	}
	
	
	/**
	 * DataControl object used as a model to help access and return values.
	 * Will be used for data access object methods
	 */
	static DataControl control = new DataControl();
	
	/**
	 * Used to log into a user's profile and manipulate the photo album data.
	 */
	public static void login(){
		
		// Loads data first thing.
		control.load();
		// Load the userList.
		try {
			backend.userList = backend.loadUserList();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JFrame frame = new LoginFrame("Photo Album Login");
		frame.setSize(350,150);
		frame.setMinimumSize(new Dimension(350,150));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.addWindowListener(new WindowAdapter()
	       {
				// Saves the session upon closing
	    	   public void windowClosing(WindowEvent e) 
	    	   {
	    	     control.save();
	    	     System.exit(0);
	    	   }
	   	 });
		
	}

}
