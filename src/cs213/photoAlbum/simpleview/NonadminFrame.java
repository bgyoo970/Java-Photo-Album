package cs213.photoAlbum.simpleview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.control.DataControl;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.User;
/**
 * 
 * @author Brian Yoo
 *
 */
public class NonadminFrame extends JFrame{
	
	JScrollPane left;
	JList list = new JList();
	
	// Northeast quadrant
	JLabel albumName = new JLabel("Album Name: ");
	JLabel numPhotos = new JLabel("Number of Photos: ");
	JLabel earliestDate = new JLabel("Earliest Date: ");
	JLabel latestDate = new JLabel("Latest Date: ");
	JTextField albumField = new JTextField("");
	JLabel photosField = new JLabel("");
	JLabel earliestField = new JLabel("");
	JLabel latestField = new JLabel("");
	JButton create = new JButton("Create");
	JButton delete = new JButton("Delete");
	JButton rename = new JButton("Rename");
	JButton open = new JButton("Open");
	JButton confirm = new JButton("Confirm");
	JButton cancel = new JButton("Cancel");	
	
	// South
	JLabel searchDate = new JLabel("Search by Date");
	JLabel searchTag = new JLabel("Search by Tags");
	JLabel startDate = new JLabel("Start Date: ");
	JLabel endDate = new JLabel("End Date: ");
	JLabel tags = new JLabel("Tags: ");
	JTextField startField = new JTextField("");
	JTextField endField = new JTextField("");
	JTextField tagsField = new JTextField("");
	JButton go1 = new JButton("Go!");
	JButton go2 = new JButton("Go!");
	JButton logout = new JButton("Logout");
	
	private static String userID;
	private User logged_in_user;
	static Backend backend = new Backend();
	static DataControl control = new DataControl();
	
	private int deleteIndex;
	private int renameIndex;
	private boolean creating;
	private boolean deleting;
	private boolean renaming;
	private boolean canSbDate;
	private boolean canSbTags;
	private boolean isFilledStartD = false;
	private boolean isFilledEndD = false;
	
	/**
	 * Constructor for the NonadminFrame
	 * @param title The name for the current window
	 * @param id the user identification to distinguish the current logged in user
	 */
	public NonadminFrame(String title, String id){
		super(title);
		setLayout(new GridLayout(2, 1));
		
		// Load the userList.
		try {backend.userList = backend.loadUserList();}
		catch (ClassNotFoundException e1) {e1.printStackTrace();}
		catch (IOException e1) {e1.printStackTrace();}
		
		userID = id;
		if (backend.userList != null) {
			logged_in_user = backend.getUser(userID);
		}
		
		
		// Top panel
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1,2));
		left = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		left.setBorder(new EmptyBorder(3,3,3,3));
		
		// Lay out the albums here ##############################################################################
		// Get the user, get his albums and list them.
		if (logged_in_user != null) {
			for (int i = 0; i < logged_in_user.albumCollection.size(); i++) {
				backend.listMod.addElement(logged_in_user.albumCollection.get(i).getAlbumName());
			}
			//logged_in_user.listAlbums();
		}
		// Adds the physical JScrollPane into the Panel
		list = backend.list;
		left.setViewportView(list);
		
		JPanel right = new JPanel();
		right.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// All the JLabels in Northeast quadrant
		// Resize labels
		albumName.setFont(new Font(null, Font.PLAIN, 10));
		numPhotos.setFont(new Font(null, Font.PLAIN, 10));
		earliestDate.setFont(new Font(null, Font.PLAIN, 9));
		latestDate.setFont(new Font(null, Font.PLAIN, 10));
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5,5,0,0);
		right.add(albumName, gbc);
		gbc.gridx = 0; gbc.gridy = 1;
		right.add(numPhotos, gbc);
		gbc.gridx = 0; gbc.gridy = 2;
		right.add(earliestDate, gbc);
		gbc.gridx = 0; gbc.gridy = 3;
		right.add(latestDate, gbc);
		
		// All text fields in Northeast quadrant
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.insets = new Insets(0,0,0,3);
		gbc.anchor = GridBagConstraints.CENTER; //reset
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = .3;
		right.add(albumField, gbc);
		gbc.gridx = 1; gbc.gridy = 1;
		right.add(photosField, gbc);
		gbc.gridx = 1; gbc.gridy = 2;
		right.add(earliestField, gbc);
		gbc.gridx = 1; gbc.gridy = 3;
		right.add(latestField, gbc);
		
		// All Buttons in Northeast quadrant
		// Resize font
		create.setFont(new Font(null, Font.PLAIN, 12));
		delete.setFont(new Font(null, Font.PLAIN, 12));
		rename.setFont(new Font(null, Font.PLAIN, 12));
		open.setFont(new Font(null, Font.PLAIN, 12));
		cancel.setFont(new Font(null, Font.PLAIN, 12));
		confirm.setFont(new Font(null, Font.PLAIN, 12));
		// Set preferred size
		create.setPreferredSize(new Dimension(85,25));
		delete.setPreferredSize(new Dimension(85,25));
		rename.setPreferredSize(new Dimension(85,25));
		open.setPreferredSize(new Dimension(85,25));
		cancel.setPreferredSize(new Dimension(85,25));
		
		gbc.gridx = 3; gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE; //reset
		gbc.insets = new Insets(2,0,2,5);
		right.add(create, gbc);
		gbc.gridx = 3; gbc.gridy = 1;
		right.add(delete, gbc);
		gbc.gridx = 3; gbc.gridy = 2;
		right.add(rename, gbc);
		gbc.gridx = 3; gbc.gridy = 3;
		right.add(open, gbc);
		gbc.gridx = 3; gbc.gridy = 4;
		right.add(cancel, gbc);
		gbc.gridx = 2; gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		right.add(confirm, gbc);
		
		top.add(left);
		top.add(right);
		
		// Bottom panel
		JPanel search = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		search.setBorder(BorderFactory.createTitledBorder("Search"));
		
		// "Search by Date: "
		c.insets = new Insets(10,20,0,0); 
		c.anchor = GridBagConstraints.WEST;
		search.add(searchDate, c);		
		
		// "Search by Tag: " 
		c.gridx = 0; c.gridy = 2;
		c.insets = new Insets(15,20,0,0); 
		search.add(searchTag, c);

		// "Start Date: "
		c.gridx = 0; c.gridy = 1;
		c.insets = new Insets(0,0,0,0); //reset
		c.anchor = GridBagConstraints.EAST;
		search.add(startDate, c);

		// "End Date: "
		c.gridx = 3; c.gridy = 1;
		c.insets = new Insets(0,5,0,0);
		search.add(endDate, c);
		
		// "Tags: "
		c.gridx = 0; c.gridy = 3;
		c.anchor = GridBagConstraints.EAST;
		search.add(tags, c);
		
		// text field after Start Date:
		c.gridx = 1; c.gridy = 1;
		c.insets = new Insets(0,0,0,0); //reset
		c.anchor = GridBagConstraints.CENTER;//reset
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = .5;
		search.add(startField,c);
		
		// text field after End Date:
		c.gridx = 4; c.gridy = 1;
		search.add(endField,c);
		
		// text field after Tags:
		c.gridx = 1; c.gridy = 3;
		c.insets = new Insets(0,0,0,0); //reset
		c.anchor = GridBagConstraints.CENTER;//reset
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = .5;
		search.add(tagsField,c);

		// Go buttons
		c.gridx = 6; c.gridy = 1;
		c.gridwidth = GridBagConstraints.NONE; //reset
		c.fill = GridBagConstraints.NONE; //reset
		c.weightx = 0.0; //reset
		c.insets = new Insets(0,5,0,10);
		search.add(go1, c);
		c.gridx = 6; c.gridy = 3;
		search.add(go2,c);
		
		// Logout Button
		c.gridx = 0; c.gridy = 4;
		c.insets = new Insets(10,0,10,0);
		c.anchor = GridBagConstraints.SOUTH;
		search.add(logout, c);
		
		add(top);
		add(search);
		
		// Default setEnables.
		create.setEnabled(true);
		confirm.setEnabled(false);
		cancel.setEnabled(false);
		logout.setEnabled(true);
		go1.setEnabled(false);
		go2.setEnabled(false);
		
		albumField.setEnabled(false);
		photosField.setEnabled(false);
		earliestField.setEnabled(false);
		latestField.setEnabled(false);
		
		startField.setEnabled(true);
		endField.setEnabled(true);
		tagsField.setEnabled(true);
		
		albumName.setForeground(Color.gray);
		numPhotos.setForeground(Color.gray);
		earliestDate.setForeground(Color.gray);
		latestDate.setForeground(Color.gray);
		searchDate.setForeground(Color.black);
		searchTag.setForeground(Color.black);
		startDate.setForeground(Color.black);
		endDate.setForeground(Color.black);
		tags.setForeground(Color.black);
		
		
		// Check if the list is not empty. If so, you can delete.
		if (backend.listMod.isEmpty()) {
			delete.setEnabled(false);
			rename.setEnabled(false);
			open.setEnabled(false);
		}
		// Initially, the list will not have a selection. Do not add for deletion if nothing is selected.
		if (backend.list.isSelectionEmpty()){
			delete.setEnabled(false);
			rename.setEnabled(false);
			open.setEnabled(false);
		}else{
			delete.setEnabled(true);
			rename.setEnabled(true);
			open.setEnabled(true);
		}
		
		// Create Listener
		create.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == create) {
					// setEnables
					create.setEnabled(false);
					delete.setEnabled(false);
					rename.setEnabled(false);
					open.setEnabled(false);
					confirm.setEnabled(true);
					cancel.setEnabled(true);
					logout.setEnabled(false);
					go1.setEnabled(false);
					go2.setEnabled(false);
					list.setEnabled(false);
					albumField.setEnabled(true);
					photosField.setEnabled(false);
					earliestField.setEnabled(false);
					latestField.setEnabled(false);
					
					startField.setEnabled(false);
					endField.setEnabled(false);
					tagsField.setEnabled(false);
					
					albumName.setForeground(Color.black);
					numPhotos.setForeground(Color.gray);
					earliestDate.setForeground(Color.gray);
					latestDate.setForeground(Color.gray);
					searchDate.setForeground(Color.gray);
					searchTag.setForeground(Color.gray);
					startDate.setForeground(Color.gray);
					endDate.setForeground(Color.gray);
					tags.setForeground(Color.gray);
					
					albumField.setText("");
					photosField.setText("");
					earliestField.setText("");
					latestField.setText("");
					startField.setText("");
					endField.setText("");
					tagsField.setText("");
					
					creating = true;
				}
			}
		});
		
		// Delete Listener
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// setEnables
				create.setEnabled(false);
				delete.setEnabled(false);
				rename.setEnabled(false);
				open.setEnabled(false);
				confirm.setEnabled(true);
				cancel.setEnabled(true);
				logout.setEnabled(false);
				go1.setEnabled(false);
				go2.setEnabled(false);
				list.setEnabled(false);
				albumField.setEnabled(false);
				photosField.setEnabled(false);
				earliestField.setEnabled(false);
				latestField.setEnabled(false);
				
				startField.setEnabled(false);
				endField.setEnabled(false);
				tagsField.setEnabled(false);
				
				albumName.setForeground(Color.gray);
				numPhotos.setForeground(Color.gray);
				earliestDate.setForeground(Color.gray);
				latestDate.setForeground(Color.gray);
				searchDate.setForeground(Color.gray);
				searchTag.setForeground(Color.gray);
				startDate.setForeground(Color.gray);
				endDate.setForeground(Color.gray);
				tags.setForeground(Color.gray);
				
				albumField.setText("");
				photosField.setText("");
				earliestField.setText("");
				latestField.setText("");
				startField.setText("");
				endField.setText("");
				tagsField.setText("");
				
				// Obtain the index number for use later.
				deleteIndex = backend.list.getSelectedIndex();
				
				deleting = true;
			}
		});
		
		// Rename Listener
		rename.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// setEnables
				create.setEnabled(false);
				delete.setEnabled(false);
				rename.setEnabled(false);
				open.setEnabled(false);
				confirm.setEnabled(true);
				cancel.setEnabled(true);
				logout.setEnabled(false);
				go1.setEnabled(false);
				go2.setEnabled(false);
				list.setEnabled(false);
				albumField.setEnabled(true);
				photosField.setEnabled(false);
				earliestField.setEnabled(false);
				latestField.setEnabled(false);
				
				startField.setEnabled(false);
				endField.setEnabled(false);
				tagsField.setEnabled(false);
				
				albumName.setText("New Album Name: ");
				albumName.setForeground(Color.black);
				numPhotos.setForeground(Color.gray);
				earliestDate.setForeground(Color.gray);
				latestDate.setForeground(Color.gray);
				searchDate.setForeground(Color.gray);
				searchTag.setForeground(Color.gray);
				startDate.setForeground(Color.gray);
				endDate.setForeground(Color.gray);
				tags.setForeground(Color.gray);
				
				albumField.setText("");
				photosField.setText("");
				earliestField.setText("");
				latestField.setText("");
				startField.setText("");
				endField.setText("");
				tagsField.setText("");
				
				// Obtain the index number for use later.
				renameIndex = backend.list.getSelectedIndex();
				renaming = true;
			}
		});
		
		// Open Listener
		open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try { backend.saveUserList(backend.userList); } 
				catch (FileNotFoundException e1) {e1.printStackTrace();}
				dispose();
				
				String albumName = backend.list.getSelectedValue();
				AlbumFrame.album(logged_in_user, albumName);
			}
		});
		
		// Confirm Listener
		confirm.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (creating) {
	        		String name = albumField.getText();
	        		if (name.equals(""))
	        			albumField.setText("Invalid name");
	        		
	        		if (!name.equals(""))
	        			control.createAlbum(logged_in_user, name);
	        		creating = false;
	        	}
	        	if (deleting) {
	        		String name = backend.listMod.get(deleteIndex);
	        		backend.listMod.remove(deleteIndex);
	        		control.deleteAlbum(logged_in_user, name);
	        		
	        		// While there's still entries in the album list. Reset the selection.
	        		if (backend.listMod.size() != 0) {
		        		backend.list.setSelectedIndex(0);
	        		}
	        		
	        		deleting = false;
	        	}
	        	
	        	if (renaming) {
	        		String newName = albumField.getText();
	        		String oldName = backend.listMod.get(renameIndex);
	        		control.renameAlbum(logged_in_user, oldName, newName);
	        		
	        		// While there's still entries in the album list. Reset the selection.
	        		if (backend.listMod.size() != 0) {
		        		backend.list.setSelectedIndex(0);
	        		}
	        		
	        		renaming = false;
	        	}
	        	
	        	
	        	if (e.getSource() == confirm) {
	        		// Default setEnables.
	        		create.setEnabled(true);
	        		confirm.setEnabled(false);
	        		cancel.setEnabled(false);
	        		logout.setEnabled(true);
	        		go1.setEnabled(false);
	        		go2.setEnabled(false);
	        		list.setEnabled(true);
	        		albumField.setEnabled(false);
	        		photosField.setEnabled(false);
	        		earliestField.setEnabled(false);
	        		latestField.setEnabled(false);
	        		
	        		startField.setEnabled(true);
	        		endField.setEnabled(true);
	        		tagsField.setEnabled(true);
	        		
	        		albumName.setText("Album Name: ");
	        		albumName.setForeground(Color.gray);
					numPhotos.setForeground(Color.gray);
					earliestDate.setForeground(Color.gray);
					latestDate.setForeground(Color.gray);
					searchDate.setForeground(Color.black);
					searchTag.setForeground(Color.black);
					startDate.setForeground(Color.black);
					endDate.setForeground(Color.black);
					tags.setForeground(Color.black);
	        		
	        		// Check if the list is not empty. If so, you can delete.
	        		if (backend.listMod.isEmpty()) {
	        			delete.setEnabled(false);
	        			rename.setEnabled(false);
	        			open.setEnabled(false);
	        		}
	        		// Initially, the list will not have a selection. Do not add for deletion if nothing is selected.
	        		if (backend.list.isSelectionEmpty()){
	        			delete.setEnabled(false);
	        			rename.setEnabled(false);
	        			open.setEnabled(false);
	        		}else{
	        			delete.setEnabled(true);
	        			rename.setEnabled(true);
	        			open.setEnabled(true);
	        		}
	        		
	        		try { backend.saveUserList(backend.userList); } 
					catch (FileNotFoundException e1) {e1.printStackTrace();}
	        	}
	        }
		});
		
		// Cancel Listener
		cancel.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (e.getSource() == cancel) {
	        		// Default setEnables.
	        		create.setEnabled(true);
	        		confirm.setEnabled(false);
	        		cancel.setEnabled(false);
	        		logout.setEnabled(true);
	        		go1.setEnabled(false);
	        		go2.setEnabled(false);
	        		list.setEnabled(true);
	        		albumField.setEnabled(false);
	        		photosField.setEnabled(false);
	        		earliestField.setEnabled(false);
	        		latestField.setEnabled(false);
	        		
	        		startField.setEnabled(true);
	        		endField.setEnabled(true);
	        		tagsField.setEnabled(true);
	        		
	        		albumName.setText("Album Name: ");
	        		albumName.setForeground(Color.gray);
	        		numPhotos.setForeground(Color.gray);
	        		earliestDate.setForeground(Color.gray);
	        		latestDate.setForeground(Color.gray);
	        		searchDate.setForeground(Color.black);
	        		searchTag.setForeground(Color.black);
	        		startDate.setForeground(Color.black);
	        		endDate.setForeground(Color.black);
	        		tags.setForeground(Color.black);
	        		
	        		// Check if the list is not empty. If so, you can delete.
	        		if (backend.listMod.isEmpty()) {
	        			delete.setEnabled(false);
	        			rename.setEnabled(false);
	        			open.setEnabled(false);
	        		}
	        		// Initially, the list will not have a selection. Do not add for deletion if nothing is selected.
	        		if (backend.list.isSelectionEmpty()){
	        			delete.setEnabled(false);
	        			rename.setEnabled(false);
	        			open.setEnabled(false);
	        		}else{
	        			delete.setEnabled(true);
	        			rename.setEnabled(true);
	        			open.setEnabled(true);
	        		}
	        		
	        		creating = false;
		    		deleting = false;
		    		renaming = false;
		    		
		    		try { backend.saveUserList(backend.userList); } 
					catch (FileNotFoundException e1) {e1.printStackTrace();}
	        	}
	        }
		});
		
		// StartField Listener
		startField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				isFilledStartD = true;
				
				// See if Go1 is valid to be enabled.
				enableGo1(isFilledStartD, isFilledEndD);
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				String str = startField.getText();
				if (str.isEmpty())
					isFilledStartD = false;
				else 
					isFilledStartD = true;
				
				// See if Go1 is valid to be enabled.
				enableGo1(isFilledStartD, isFilledEndD);
			}
		});
		
		// endField Listener
		endField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				isFilledEndD = true;
				
				// See if Go1 is valid to be enabled.
				enableGo1(isFilledStartD, isFilledEndD);
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				String str = endField.getText();
				if (str.isEmpty())
					isFilledEndD = false;
				else 
					isFilledEndD = true;
				
				// See if Go1 is valid to be enabled.
				enableGo1(isFilledStartD, isFilledEndD);
			}
		});
		// tagsField Listener
		tagsField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				go2.setEnabled(true);
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				String str = tagsField.getText();
				if (str.isEmpty())
					go2.setEnabled(false);
				else 
					go2.setEnabled(true);
			}
		});
		
		// Focus Listeners
		startField.addFocusListener(new FocusAdapter(){
			// if mouse clicks in it
			public void focusGained(FocusEvent e){
            	startField.setText("");
            }
        });
		endField.addFocusListener(new FocusAdapter(){
			// if mouse clicks in it
			public void focusGained(FocusEvent e){
            	endField.setText("");
            }
        });
		tagsField.addFocusListener(new FocusAdapter(){
			// if mouse clicks in it
			public void focusGained(FocusEvent e){
            	tagsField.setText("");
            }
        });
		
		// Go1 Listener
		go1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try { backend.saveUserList(backend.userList); } 
				catch (FileNotFoundException e1) {e1.printStackTrace();}
				String startD = startField.getText();
				String endD = endField.getText();
				// If either dates are invalid, present an error.
				if (logged_in_user.checkValidDate(startD) == false) {
					startField.setText("Error, correct date format: MM/dd/yyyy-kk:mm:ss");
				}
				if (logged_in_user.checkValidDate(endD) == false) {
					endField.setText("Error, correct date format: MM/dd/yyyy-kk:mm:ss");
				}
				if (logged_in_user.checkValidDate(startD) && logged_in_user.checkValidDate(endD)){
					dispose();
					SearchResultsFrame.searchresults("SearchByDate", logged_in_user, startD, endD);
				}
				
				// Save here
				try { backend.saveUserList(backend.userList); } 
				catch (FileNotFoundException e1) {e1.printStackTrace();}
			}
		});
		
		// Go2 Listener
		go2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try { backend.saveUserList(backend.userList); } 
				catch (FileNotFoundException e1) {e1.printStackTrace();}
				String stringStream = tagsField.getText();
				if (logged_in_user.processStr(stringStream) == null) {
					tagsField.setText("Error, correct input format: [<tagType:]\"<tagValue>\"[,[<tagType:]\"<tagValue>\"]...");
				} else {
					dispose();
					SearchResultsFrame.searchresults("SearchByTags", logged_in_user, stringStream);
				}
				
				// Save here
				try { backend.saveUserList(backend.userList); } 
				catch (FileNotFoundException e1) {e1.printStackTrace();}
			}
		});
		
		// Mouse Listener
		backend.list.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent arg0) {
				if (!creating && !deleting && !renaming) {
					delete.setEnabled(true);
					rename.setEnabled(true);
	    			open.setEnabled(true);
				}
				if (backend.list.isSelectionEmpty()){
        			delete.setEnabled(false);
        			rename.setEnabled(false);
        			open.setEnabled(false);
        		}
			}
		});
		
		backend.list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (! e.getValueIsAdjusting() && !creating && !deleting && !renaming) {
					// For some reason, the for loop prevents a selection bug error.
					// Find out why later.
					for (int i = 0; i < backend.listMod.size(); i++) {
						if (backend.list.isSelectedIndex(i)) {	// at the right album
							int userIndex = backend.getUserIndex(userID);
							Album curralbum = backend.userList.get(userIndex).getAlbum(backend.list.getSelectedValue());
							int curralbumsize = 0;
							if (curralbum != null) {
								curralbumsize = curralbum.getPhotoAlbum().size();
							}
							int firstphotoindex = 0; int lastphotoindex = curralbumsize;
							numPhotos.setEnabled(true);
							earliestDate.setEnabled(true);
							latestDate.setEnabled(true);
							
							albumField.setText(curralbum.getAlbumName());
							photosField.setText(Integer.toString(curralbum.getPhotoct()));
							if (curralbum.getPhotoAlbum().size() > 0) {
								earliestField.setText(curralbum.getPhotoAlbum().get(firstphotoindex).getCal());
								latestField.setText(curralbum.getPhotoAlbum().get(lastphotoindex - 1).getCal());
							}
							else {
								earliestField.setText("N/A");
								latestField.setText("N/A");
							}
						}
					}
					
				}
			}
		});
		
		// Logout Listener
		logout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//backend.listMod.removeAllElements();
				backend.listMod.clear();
				dispose();
				// These persistence methods aren't working by just calling control.save()
				//control.save();
				// Must manually save the list.
				try { backend.saveUserList(backend.userList); } 
				catch (FileNotFoundException e1) {e1.printStackTrace();}
				LoginFrame.login();
			}
		});
	}
	
	/**
	 * Establishes the NonadminFrame in a JFrame
	 * @param id the user identification to distinguish the current logged in user
	 */
	public static void nonadmin(String id){
		JFrame frame = new NonadminFrame("Non-Admin", id);
		frame.setSize(750,375);
		frame.setMinimumSize(new Dimension(500,375));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		userID = id;
	}
	
	/**
	 * A helper method to see if the Go button in the NonadminFrame should be enabled. The button will be enabled if the given parameters are true.
	 * @param isFilledStartD variable that is used to determine if the start date field is filled
	 * @param isFilledEndD variable that is used to determine if the end date field is filled
	 */
	public void enableGo1(boolean isFilledStartD, boolean isFilledEndD){
		// Check if both textFields are filled. If so, we can enable the Go! Button.
		if (isFilledStartD && isFilledEndD) {
			canSbDate = true;
		} else {
			canSbDate = false;
		}
		if (canSbDate)
			go1.setEnabled(true);
		else
			go1.setEnabled(false);
	}

}
