package cs213.photoAlbum.simpleview;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.control.DataControl;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.User;
/**
 * 
 * @author Michelle Gavino
 *
 */
public class AdminFrame extends JFrame implements ActionListener, ListSelectionListener{
	
	private static final long serialVersionUID = -9215829052688468485L;
	
	// GLOBALS
	JTextField idField = new JTextField("");
	JTextField nameField = new JTextField("");
	
	JButton create = new JButton("Create User");
	JButton delete = new JButton("Delete User");
	JButton confirm = new JButton("Confirm");
	JButton cancel = new JButton("Cancel");
	JButton logout = new JButton("Logout");
	
	JLabel userID = new JLabel("User ID: ");
	JLabel username = new JLabel("Username: ");
	JLabel msg = new JLabel("Deleting selected user.. Are you sure?");
	
	JScrollPane scrollPane;
	JList list = new JList();
	
	Backend backend = new Backend();
	DataControl control = new DataControl();
	
	private boolean creating;
	private boolean deleting;
	private int deleteIndex;
	
	/**
	 * 
	 * @param title
	 */
	public AdminFrame(String title){
		super(title);
		setLayout(new GridLayout(2, 1));
		
		// TOP PANEL
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout());
		listPanel.setBorder(new EmptyBorder(10,10,10,10));
		
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
		// Add the elements from the userList to the listMod. Allows to display onto the JScrollPane. Only do if userList exists
		if (backend.userList != null){
			for (int i = 0; i < backend.userList.size(); i++) {
				backend.listMod.addElement(backend.userList.get(i).userID);
			}
		}
		//alphaSort(backend.listMod);
		// Adds the physical JScrollPane into the Panel
		list = backend.list;
		JScrollPane scrollPane = new JScrollPane(list);
		listPanel.add(scrollPane);
		// END OF TOP PANEL
		
	
		// Start of the bottom panel
		JPanel commands = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
		commands.add(delete);
		commands.add(create);
		
		// "User ID: "
		JPanel text = new JPanel(new GridBagLayout());
		GridBagConstraints ctext = new GridBagConstraints();
		ctext.gridx = 0; ctext.gridy = 1;
		ctext.insets = new Insets(0,20,0,0);
		text.add(userID, ctext);
		
		// "Username: "
		ctext.gridx = 0; ctext.gridy = 2;
		ctext.insets = new Insets(0,20,0,0);
		text.add(username, ctext);
		
		// text field after User ID:
		ctext.gridx = 1; ctext.gridy = 1;
		ctext.gridwidth = GridBagConstraints.REMAINDER; 
		ctext.weightx = 0.5;
		ctext.weighty = 1.0;
		ctext.insets = new Insets(0,0,0,20);
		ctext.fill = GridBagConstraints.HORIZONTAL;
		text.add(idField, ctext);
		
		// text field after Username: 
		ctext.gridx = 2; ctext.gridy = 2;
		ctext.fill = GridBagConstraints.HORIZONTAL;
		text.add(nameField, ctext);
		
		msg.setForeground(getBackground());
		ctext.gridx = 0; ctext.gridy = 0;
		ctext.insets = new Insets(0,15,5,0);
		ctext.gridwidth = 3;
		ctext.weightx = 1.0; 
		text.add(msg,ctext);
		
		
		// Cancel/Confirm buttons
		JPanel conf = new JPanel(new GridBagLayout());
		GridBagConstraints cconf = new GridBagConstraints();
		cconf.gridx = 0; cconf.gridy = 0;
		cconf.weightx = 0.5;
		cconf.fill = GridBagConstraints.HORIZONTAL;
		cconf.anchor = GridBagConstraints.CENTER;
		cconf.insets = new Insets(0,20,0,5);
		conf.add(confirm,cconf);
		cconf.gridx = 1; cconf.gridy = 0;
		cconf.insets = new Insets(0,0,0,20);
		conf.add(cancel,cconf);
		
		JPanel back = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		back.add(logout);
		
		JPanel bottom = new JPanel(new GridLayout(4, 1));
		bottom.add(commands);
		bottom.add(text);
		bottom.add(conf);
		bottom.add(back);
		
		add(listPanel);
		add(bottom);
		
		// Default setEnables.
		create.setEnabled(true);
		confirm.setEnabled(false);
		cancel.setEnabled(false);
		logout.setEnabled(true);
		
		idField.setEnabled(false);
		nameField.setEnabled(false);
		
		userID.setForeground(Color.gray);
		username.setForeground(Color.gray);
		
		// Check if the list is not empty. If so, you can delete.
		if (backend.listMod.isEmpty()) 
			delete.setEnabled(false);
		
		// Initially, the list will not have a selection. Do not add for deletion if nothing is selected.
		if (backend.list.isSelectionEmpty()){
			delete.setEnabled(false);
		}else{
			delete.setEnabled(true);
		}

		
		// Create Listener
		create.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e) {
	        	if (e.getSource() == create) {
		    		create.setEnabled(false);
		    		delete.setEnabled(false);
		    		confirm.setEnabled(true);
		    		cancel.setEnabled(true);
		    		logout.setEnabled(false);
		    		list.setEnabled(false);
		    		idField.setEnabled(true);
		    		nameField.setEnabled(true);
		    		
		    		userID.setForeground(Color.black);
		    		username.setForeground(Color.black);
		    		msg.setText("");
		    		idField.setText("");
		    		nameField.setText("");
		    		
		    		creating = true;
	        	}
	        }
		});
		
		// Delete Listener
		delete.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e) {
	        	
	        	deleteIndex = backend.list.getSelectedIndex();
	        	
	        	if (e.getSource() == delete) {
	        		msg.setForeground(Color.red);
		    		create.setEnabled(false);
		    		delete.setEnabled(false);
		    		confirm.setEnabled(true);
		    		cancel.setEnabled(true);
		    		logout.setEnabled(false);
		    		list.setEnabled(false);
		    		idField.setEnabled(false);
		    		nameField.setEnabled(false);
		    		msg.setText("Deleting selected user.. Are you sure?");
		    		
		    		userID.setForeground(Color.gray);
		    		username.setForeground(Color.gray);
		    		
		    		deleting = true;
	        	}
	        }
		});
		
		// Confirm Listener
		confirm.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (creating) {
	        		String id = idField.getText();
	        		String name = nameField.getText();
	        		User user = new User(id, name, new ArrayList<Album>());
	        		
	        		if (id.equals("")) {
	        			msg.setForeground(Color.red);
	        			msg.setText("Please input a value for id");
	        		}
	        		if (id.contains(" ")) {
	        			msg.setForeground(Color.red);
	        			msg.setText("Invalid input. userID can only be one string");
	        		}
	        		if (name.equals("")) {
	        			msg.setForeground(Color.red);
	        			msg.setText("Please input a value for name");
	        		}
	        		
	        		if (!id.equals("") && !name.equals("") && !id.contains(" "))
	        			control.adduser(id, name);
	        		
	        		// -- Method: alphasort.
	        		creating = false;
	        	}
	        	
	        	if (deleting) {
	        		String id = backend.listMod.get(deleteIndex);
	        		
	        		backend.listMod.remove(deleteIndex);
	        		control.deleteuser(id);
	        		if (backend.listMod.size() != 0) {
		        		backend.list.setSelectedIndex(0);
	        		}
	        		msg.setText("");
	        		deleting = false;
	        	}
	        	
	        	if (e.getSource() == confirm) {
	        		create.setEnabled(true);
		    		confirm.setEnabled(false);
		    		cancel.setEnabled(false);
		    		logout.setEnabled(true);
		    		list.setEnabled(true);
		    		idField.setEnabled(false);
		    		nameField.setEnabled(false);
		    		userID.setForeground(Color.gray);
		    		username.setForeground(Color.gray);
		    		
		    		
		    		// Check if the list is not empty. If so, you can delete.
		    		if (backend.listMod.isEmpty()) 
		    			delete.setEnabled(false);
		    		// Initially, the list will not have a selection. Do not add for deletion if nothing is selected.
		    		if (backend.list.isSelectionEmpty()){
		    			delete.setEnabled(false);
		    		}else{
		    			delete.setEnabled(true);
		    		}
	        	}
	        }
		});
		
		// Cancel Listener
		cancel.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (e.getSource() == cancel) {
	        		create.setEnabled(true);
		    		confirm.setEnabled(false);
		    		cancel.setEnabled(false);
		    		logout.setEnabled(true);
		    		list.setEnabled(true);
		    		idField.setEnabled(false);
		    		nameField.setEnabled(false);
		    		idField.setText("");
		    		nameField.setText("");
		    		
		    		userID.setForeground(Color.gray);
		    		username.setForeground(Color.gray);
		    		msg.setForeground(getBackground());
		    		msg.setText("");
		    		// Check if the list is not empty. If so, you can delete.
		    		if (backend.listMod.isEmpty()) 
		    			delete.setEnabled(false);
		    		// Initially, the list will not have a selection. Do not add for deletion if nothing is selected.
		    		if (backend.list.isSelectionEmpty()){
		    			delete.setEnabled(false);
		    		}else{
		    			delete.setEnabled(true);
		    		}
		    		
		    		creating = false;
		    		deleting = false;
	        	}
	        }
		});
		
		backend.list.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent arg0) {
				if (!deleting && !creating)
				delete.setEnabled(true);
				if (backend.list.isSelectionEmpty()){
        			delete.setEnabled(false);
        		}
			}
		});
		
		// Logout Listener
		logout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				backend.listMod.clear();
				dispose();
				control.save();
				LoginFrame.login();
			}
		});
	}	
	
	
	/**
	 * Establishes the AdminFrame in a JFrame 
	 */
	public static void admin(){
		JFrame frame = new AdminFrame("Admin");
		frame.setSize(400,510);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Sorts the given ArrayList into alphabetical order
	 * @param listMod the ArrayList of users to be sorted
	 */
	public void alphaSort(DefaultListModel<String> listMod) {
		String temp1, temp2;
		String temp3;
		int index;
		DefaultListModel<String> newUserList = new DefaultListModel<String>();
		// Error check.
		if (listMod == null || listMod.isEmpty()) {
			return;
		}

		
		
		backend.listMod.removeAllElements();
		
		for (int i = 0; i < newUserList.size(); i++) {
			backend.listMod.addElement((newUserList.get(i)));
		}
		//backend.listMod = newUserList;
	}
	
	
	// Default methods that must be included as a part of the implementation.
	// They don't need to be filled.
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
