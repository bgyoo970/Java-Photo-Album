package cs213.photoAlbum.simpleview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import cs213.photoAlbum.control.DataControl;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.model.User;

/**
 * 
 * @author Brian Yoo
 *
 */
public class AlbumFrame extends JFrame{
	
	// Northwest quadrant
	static JList<JLabel> list;
	static DefaultListModel<JLabel> listmodel;
	static ArrayList<Photo> photoAlbum;
	ArrayList<String> descriptions = new ArrayList<String>();
	
	
	// Northeast quadrant
	JLabel error1 = new JLabel("hi");
	JLabel error2 = new JLabel("hello");
	JButton add = new JButton("Add");
	JButton recaption = new JButton("Recaption");
	JButton delete = new JButton("Delete");
	JButton move = new JButton("Move");
	JButton confirm = new JButton("Confirm");
	JButton cancel = new JButton("Cancel");
	JTextField photo = new JTextField("Photo Name");
	JTextField caption = new JTextField("Caption");
	JTextField date = new JTextField("Date");
	JTextField tags = new JTextField("Tags");
	JComboBox<String> combo = new JComboBox<String>();
	
	// South
	JButton slideshow = new JButton("Slideshow");
	JButton display = new JButton("Display/Show Photo Info");
	
	JButton editTags = new JButton("Edit Photo Tags");
	JButton addTag = new JButton("Add");
	JButton deleteTag = new JButton("Delete");
	JButton confirmTag = new JButton("Confirm");
	JButton cancelTag = new JButton("Cancel");
	JButton exitTagEditing = new JButton("Exit Tag Editing");
	JButton back = new JButton("Back");
	JTextField tagTypeAndVal = new JTextField("Please specify a tag type and tag value in the format <tagType>:\"<tagValue>\"");
	JLabel tagMsg = new JLabel("Deleting tag selection.. Are you sure?");
	static JPanel tagPanel;
	static JList<String> tagList;
	static DefaultListModel<String> taglistmodel;
	static ArrayList<Tag> photoTags;
	private int tagIndex;	// used to get where tag is in tagList
	
	
	static User user;
	private static String userID;
	static String albumName;
	private boolean adding = false;
	private boolean deleting = false;
	private boolean recaptioning = false;
	private boolean moving = false;
	private boolean addingTag = false;
	private boolean deletingTag = false;
	private int userIndex;
	
	/**
	 * The Constructor for the AlbumFrame object
	 * @param title The name for the current window
	 * @param u the specified user that is currently logged in
	 * @param aN the current album to be looked at
	 */
	public AlbumFrame(String title, User u, String aN){
		super(title);
		setLayout(new BorderLayout());
		
		// Load the userList.
		try { 
			NonadminFrame.backend.userList = NonadminFrame.backend.loadUserList(); 
			NonadminFrame.backend.readuser(u.userID);
		} catch (ClassNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }
		
		userID = u.userID;
		userIndex = NonadminFrame.backend.getUserIndex(userID);
		
		user = u;
		albumName = aN;
		// Fill combobox with album names. Meant for Move Photo.
		for(int i = 0; i < NonadminFrame.backend.userList.get(userIndex).albumCollection.size(); i++){
			if(!NonadminFrame.backend.userList.get(userIndex).albumCollection.get(i).getAlbumName().equals(albumName))
				combo.addItem(NonadminFrame.backend.userList.get(userIndex).albumCollection.get(i).getAlbumName());
		}
		
		// Top panel
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1,2));
		listmodel = new DefaultListModel<JLabel>();
		listmodel.clear();
		// Insert ImageIcons here
		photoAlbum = NonadminFrame.backend.userList.get(userIndex).getAlbum(albumName).getPhotoAlbum();
		updateList(photoAlbum);
		list = new JList<JLabel>(listmodel);
		list.setCellRenderer(new LabelRenderer());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0); 
		
		JScrollPane left = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		left.setViewportView(list);
		left.setBorder(new EmptyBorder(3,3,3,3));
		
		// Buttons and text fields
		JPanel right = new JPanel();
		right.setBorder(BorderFactory.createTitledBorder("Photo Editing"));
		right.setLayout(new GridBagLayout());
		GridBagConstraints c0 = new GridBagConstraints();
		
		c0.gridx = 0; c0.gridy = 0;
		c0.gridwidth = 2;
		c0.anchor = GridBagConstraints.CENTER;
		c0.fill = GridBagConstraints.HORIZONTAL;
		c0.insets = new Insets(0,3,0,3);
		error1.setFont(new Font(null, Font.PLAIN, 12));
		error1.setForeground(getBackground());
		error2.setFont(new Font(null, Font.PLAIN, 12));
		error2.setForeground(getBackground());
		right.add(error1, c0);
		c0.gridx = 0; c0.gridy = 1;
		right.add(error2, c0);
		// Buttons in Northeast quadrant
		// Format buttons
		add.setFont(new Font(null, Font.PLAIN, 12));
		recaption.setFont(new Font(null, Font.PLAIN, 12));
		delete.setFont(new Font(null, Font.PLAIN, 12));
		move.setFont(new Font(null, Font.PLAIN, 12));
		confirm.setFont(new Font(null, Font.PLAIN, 12));
		cancel.setFont(new Font(null, Font.PLAIN, 12));
		add.setPreferredSize(new Dimension(85,23));
		recaption.setPreferredSize(new Dimension(85,23));
		delete.setPreferredSize(new Dimension(85,23));
		move.setPreferredSize(new Dimension(85,23));
		confirm.setPreferredSize(new Dimension(85,23));
		cancel.setPreferredSize(new Dimension(85,23));
		
		c0.gridx = 0; c0.gridy = 2;
		c0.gridwidth = 1; // reset
		c0.insets = new Insets(5,3,0,3);
		c0.anchor = GridBagConstraints.WEST;
		c0.weightx = .5;
		right.add(add, c0);
		c0.gridx = 1; c0.gridy = 2;
		right.add(delete, c0);
		c0.gridx = 0; c0.gridy = 5;
		right.add(recaption, c0);;
		c0.gridx = 0; c0.gridy = 6;
		right.add(move, c0);
		c0.gridx = 0; c0.gridy = 7;
		right.add(confirm, c0);
		c0.gridx = 1; c0.gridy = 7;
		right.add(cancel, c0);
		
		// Text fields in Northeast quadrant
		c0.gridx = 0; c0.gridy = 3;
		c0.anchor = GridBagConstraints.CENTER; //reset
		c0.fill = GridBagConstraints.HORIZONTAL;
		c0.gridwidth = 2;
		right.add(photo, c0);
		c0.gridx = 0; c0.gridy = 4;
		right.add(caption, c0);
		c0.gridx = 1; c0.gridy = 6;
		right.add(combo, c0);

		
		top.add(left); top.add(right);
		
		// Bottom panel
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,3,7,3));
		buttons.setBorder(new EmptyBorder(3,3,3,3));
		slideshow.setFont(new Font(null, Font.PLAIN, 12));
		display.setFont(new Font(null, Font.PLAIN, 12));
		editTags.setFont(new Font(null, Font.PLAIN, 12));
		buttons.add(slideshow);
		buttons.add(display);
		buttons.add(editTags);
		
		tagPanel = new JPanel();
		tagPanel.setBorder(BorderFactory.createTitledBorder("Tag Editing"));
		tagPanel.setLayout(new BorderLayout());
		tagMsg.setForeground(getBackground());
		
		JPanel lower = new JPanel();
		lower.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// Buttons
		c.gridx = 0; c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL; //reset
		c.weightx = 0.25;
		c.insets = new Insets(3,3,3,3);
		lower.add(addTag, c);
		c.gridx = 1; c.gridy = 1;
		lower.add(deleteTag,c);
		c.gridx = 0; c.gridy = 3; 
		c.weightx = 0.3; 
		c.anchor = GridBagConstraints.CENTER; //reset
		c.insets = new Insets(3,5,3,3);
		lower.add(confirmTag, c);
		c.gridx = 1; c.gridy = 3;
		c.insets = new Insets(3,3,3,3);
		lower.add(cancelTag,c);
		c.gridx = 3; c.gridy = 3;
		c.weightx = 0.25;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(3,3,3,5);
		c.fill = GridBagConstraints.NONE; //reset
		lower.add(exitTagEditing,c);
		// Text fields & labels
		c.gridx = 0; c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER; //reset
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 4;
		c.weightx = 1.0; 
		lower.add(tagTypeAndVal, c);
		c.gridx = 0; c.gridy = 0;
		lower.add(tagMsg,c);
		// Insert tags here
		taglistmodel = new DefaultListModel<String>();
		taglistmodel.clear();
		tagList = new JList<String>(taglistmodel);
		tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tagList.setSelectedIndex(0); 
		
		JScrollPane upper = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		upper.setViewportView(tagList);
		upper.setBorder(new EmptyBorder(3,3,3,3));
		
		tagPanel.add(upper, BorderLayout.CENTER);
		tagPanel.add(lower, BorderLayout.SOUTH);

		
		JPanel exitPanel = new JPanel();
		exitPanel.setLayout(new GridLayout(1,4));
		exitPanel.setBorder(new EmptyBorder(7,7,7,7));
		back.setFont(new Font(null, Font.PLAIN, 12));
		exitPanel.add(back);
		// Addition of invisible boxes so exit button is in right position
		exitPanel.add(Box.createRigidArea(new Dimension(1,1)));
		exitPanel.add(Box.createRigidArea(new Dimension(1,1)));
		exitPanel.add(Box.createRigidArea(new Dimension(1,1)));
		
		bottom.add(buttons, BorderLayout.NORTH);
		bottom.add(tagPanel, BorderLayout.CENTER); tagPanel.setVisible(false); 
		bottom.add(exitPanel,BorderLayout.SOUTH);
		top.setPreferredSize(new Dimension(600,230));
		add(top, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		
		// Default setEnables
		if(list.isSelectionEmpty()){
			recaption.setEnabled(false);
			delete.setEnabled(false);
			move.setEnabled(false);
			slideshow.setEnabled(false);
			display.setEnabled(false);
			editTags.setEnabled(false);
			
		}
		// Disable buttons,text fields
		photo.setEnabled(false);
		caption.setEnabled(false);
		tags.setEnabled(false);
		date.setEnabled(false);
		combo.setEnabled(false);
		confirm.setEnabled(false);
		cancel.setEnabled(false);
		// Disable all Tag Editing
		tagTypeAndVal.setEnabled(false);
		addTag.setEnabled(false);
		deleteTag.setEnabled(false);
		confirmTag.setEnabled(false);
		cancelTag.setEnabled(false);
		
		// Buttons
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				add.setEnabled(false);
				photo.setEnabled(true);
				caption.setEnabled(true);
				confirm.setEnabled(true);
				cancel.setEnabled(true);
				// Disable other JList and other buttons
				list.setEnabled(false);
				recaption.setEnabled(false);
				move.setEnabled(false);
				delete.setEnabled(false);
				slideshow.setEnabled(false);
				display.setEnabled(false);
				editTags.setEnabled(false);
				back.setEnabled(false);
				
				adding = true;

			}
		});
		recaption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				add.setEnabled(false);
				caption.setEnabled(true);
				confirm.setEnabled(true);
				cancel.setEnabled(true);
				list.setEnabled(false);
				recaption.setEnabled(false);
				move.setEnabled(false);
				delete.setEnabled(false);
				slideshow.setEnabled(false);
				display.setEnabled(false);
				editTags.setEnabled(false);
				back.setEnabled(false);
				recaptioning = true;
			}
		});
		// if nothing selected, disable delete button
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm.setEnabled(true);
				cancel.setEnabled(true);
				list.setEnabled(false);
				add.setEnabled(false);
				delete.setEnabled(false);
				move.setEnabled(false);
				recaption.setEnabled(false);
				slideshow.setEnabled(false);
				display.setEnabled(false);
				editTags.setEnabled(false);
				back.setEnabled(false);
				deleting = true;
				error1.setText("Deleting photo selection.. Are you sure?");
				error1.setForeground(Color.red);
			}
		});
		move.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				confirm.setEnabled(true);
				cancel.setEnabled(true);
				list.setEnabled(false);
				add.setEnabled(false);
				recaption.setEnabled(false);
				delete.setEnabled(false);
				combo.setEnabled(true);
				move.setEnabled(false);
				slideshow.setEnabled(false);
				display.setEnabled(false);
				editTags.setEnabled(false);
				back.setEnabled(false);
				
				moving = true;
			}
		});
		confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int uIndex = NonadminFrame.backend.getUserIndex(userID);
				
				if(adding){
					ArrayList<Tag> tempt = new ArrayList<Tag>();
					File f = null; String path = "";
					boolean flag = false; boolean exists = false;
					String cap = caption.getText();
					f = new File(photo.getText());
					try {
						path = f.getCanonicalPath();
						flag = f.exists();
						if (flag) {
						} else {
							error1.setForeground(Color.red);
							error1.setText("File " + path + " does not exist,");
							error2.setForeground(Color.red);
							error2.setText("Could not add photo.");
							adding = false;
						}
					} catch (IOException ioe) {
						error1.setForeground(Color.red);
						error1.setText("Invalid input");
						error2.setForeground(Color.red);
						error2.setText("Could not add photo.");
					}

					Photo tempp = new Photo(path, caption.getText());
					// Check if photo already exists
					if(NonadminFrame.backend.userList.get(uIndex).getAlbum(albumName).getPhoto(path) != null){
						error1.setForeground(Color.red);
						error1.setText("Photo " + path + " already exists in album " + albumName);
						error2.setForeground(Color.red);
						error2.setText("Could not add photo");
						adding = false;
					} 
					
					// Then search the other albums for that photo
					for(int i = 0; i < user.albumCollection.size(); i++) {
						Album currAlbum = user.albumCollection.get(i);
						if(adding && currAlbum.hasPhoto(path)) {
							exists = true;
							String captext = caption.getText();
							if (!caption.getText().isEmpty() && !captext.equals("Caption")){
									error1.setForeground(Color.red);
									error1.setText("Caption must be empty when adding an existing photo to subsquent albums");
									error2.setForeground(Color.red);
									error2.setText("Could not add photo");
									adding = false;
							}
							else if(caption.getText().isEmpty() || captext.equals("Caption")){
								caption.setText("");
								cap = currAlbum.getPhoto(path).getCaption();
								exists = false;
							}
							
							tempt = currAlbum.getPhoto(path).getTagList();
						}
					}
					tempp.setTagList(tempt);
					
					if(adding && !exists){
						error1.setForeground(Color.blue);
						error1.setText("Add Successful!");
						error2.setText("");
						NonadminFrame.control.addPhoto(NonadminFrame.backend.userList.get(uIndex), path, caption.getText(), albumName);
						updateList(photoAlbum);
					}
					adding = false;
					
				}
				if(deleting){
					NonadminFrame.control.removePhoto(NonadminFrame.backend.userList.get(uIndex), descriptions.get(list.getSelectedIndex()), albumName);
					updateList(photoAlbum);					
					error1.setForeground(Color.blue);
					error1.setText("Delete Successful!");
					error2.setText("");
					deleting = false;
				}
				if(recaptioning){
					if (caption.getText().equals("")) {
						error1.setForeground(Color.red);
						error1.setText("The caption must be filled,");
						error2.setForeground(Color.red);
						error2.setText("Could not recaption photo");
					} else {
						NonadminFrame.control.editCaption(NonadminFrame.backend.userList.get(uIndex), descriptions.get(list.getSelectedIndex()), caption.getText());
						updateList(photoAlbum);
						error1.setForeground(Color.blue);
						error1.setText("Recaptioning Successful!");
						error2.setText("");
					}
					
					//NonadminFrame.control.editCaption(NonadminFrame.backend.userList.get(uIndex), descriptions.get(list.getSelectedIndex()), caption.getText());
					//updateList(photoAlbum);
					recaptioning = false;
				}
				if(moving){
					int currIndex = list.getSelectedIndex(); // for the photo
					// albumName is the current album
					Photo currPhoto = NonadminFrame.backend.userList.get(uIndex).getAlbum(albumName).getPhotoAlbum().get(currIndex);
					String currPhotoName = currPhoto.getPhotoName();
					
					String newAlbum = combo.getSelectedItem().toString();
					boolean hasPhoto = false;

					for(int i = 0; i < NonadminFrame.backend.userList.get(uIndex).getAlbum(newAlbum).getPhotoAlbum().size(); i++) {
						if(NonadminFrame.backend.userList.get(uIndex).getAlbum(newAlbum).getPhotoAlbum().get(i).getPhotoName().equals(currPhotoName)) {
							hasPhoto = true;
						}
					}
					if (newAlbum.equals("")) {
						error1.setForeground(Color.red);
						error1.setText("Please select an album");
						error2.setForeground(Color.red);
						error2.setText("Could not move photo");
					} else if(hasPhoto) {
						error1.setForeground(Color.red);
						error1.setText("Photo " + currPhotoName );
						error2.setForeground(Color.red);
						error2.setText("already exists in " + newAlbum + ". Did not add.");
					} else {
						error1.setForeground(Color.blue);
						error1.setText("Move successful!");
						error2.setText("");
						NonadminFrame.control.movePhoto(NonadminFrame.backend.userList.get(uIndex), descriptions.get(list.getSelectedIndex()), albumName, newAlbum);
						updateList(photoAlbum);
					}
					moving = false;
				}
				
				
				photo.setEnabled(false); photo.setText("Photo Name");
				caption.setEnabled(false); caption.setText("Caption");
				combo.setEnabled(false);
				if(listmodel.isEmpty()){
					add.setEnabled(true);
				} else {
					add.setEnabled(true);
					list.setEnabled(true);
					recaption.setEnabled(true);
					move.setEnabled(true);
					delete.setEnabled(true);
					slideshow.setEnabled(true);
					display.setEnabled(true);
					editTags.setEnabled(true);
				}				
				confirm.setEnabled(false);
				cancel.setEnabled(false);
				back.setEnabled(true);
				
				// While there's still entries in the album list. Reset the selection.
        		if (listmodel.size() != 0) {
        			list.setSelectedIndex(0);
        		}
				
				try { 
					NonadminFrame.backend.saveUserList(NonadminFrame.backend.userList);
					NonadminFrame.backend.writeuser(user);
				} catch (FileNotFoundException e1) {e1.printStackTrace();}
			}
			
		});
		
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				error1.setForeground(getBackground());
				error2.setForeground(getBackground());
				photo.setEnabled(false); photo.setText("Photo Name");
				caption.setEnabled(false); caption.setText("Caption");
				tags.setEnabled(false); tags.setText("Tags");
				date.setEnabled(false); date.setText("Date");
				combo.setEnabled(false);
				adding = false;
				recaptioning = false;
				deleting = false;
				moving = false; 
				if(listmodel.isEmpty()){
					add.setEnabled(true);
				} else {
					add.setEnabled(true);
					list.setEnabled(true);
					recaption.setEnabled(true);
					move.setEnabled(true);
					delete.setEnabled(true);
					slideshow.setEnabled(true);
					display.setEnabled(true);
					editTags.setEnabled(true);
				}
				confirm.setEnabled(false);
				cancel.setEnabled(false);
				back.setEnabled(true);
			}
		});
		editTags.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Album a = NonadminFrame.backend.userList.get(userIndex).getAlbum(albumName);
				String photopath = descriptions.get(list.getSelectedIndex());
				if(a.getPhoto(photopath).getTagList().isEmpty()) {
					deleteTag.setEnabled(false);
				} else {
					deleteTag.setEnabled(true);
				}
				addTag.setEnabled(true);
				confirmTag.setEnabled(false);
				cancelTag.setEnabled(false);
				tagTypeAndVal.setEnabled(false);
				exitTagEditing.setEnabled(true);
				slideshow.setEnabled(false);
				display.setEnabled(false);
				editTags.setEnabled(false);
				// Disable Photo Editing
				list.setEnabled(false);
				add.setEnabled(false);
				recaption.setEnabled(false);
				delete.setEnabled(false);
				move.setEnabled(false);
				back.setEnabled(false);
				if(descriptions.isEmpty()){
					
				}else{
					String path = descriptions.get(list.getSelectedIndex());
					photoTags = NonadminFrame.backend.userList.get(userIndex).getAlbum(albumName).getPhoto(path).getTagList();
					updateTagsList(photoTags);

					tagList.setSelectedIndex(0);
				}
				
				tagPanel.setVisible(true); setSize(new Dimension(600,565));
			}
		});
		addTag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				tagTypeAndVal.setEnabled(true);
				exitTagEditing.setEnabled(false);
				addTag.setEnabled(false);
				deleteTag.setEnabled(false);
				confirmTag.setEnabled(true);
				cancelTag.setEnabled(true);
				addingTag = true;
				tagList.setEnabled(false);
				
				
			}
		});
		deleteTag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				tagList.setEnabled(false);
				tagTypeAndVal.setEnabled(false);
				exitTagEditing.setEnabled(false);
				addTag.setEnabled(false);
				deleteTag.setEnabled(false);
				confirmTag.setEnabled(true);
				cancelTag.setEnabled(true);
				tagMsg.setForeground(Color.red);
				deletingTag = true;
			}
		});
		confirmTag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				if(addingTag){
					// First check if empty
					if(tagTypeAndVal.getText().equals("")){
						tagTypeAndVal.setText("Error: MUST specify a tag type and tag value in the format <tagType>:\"<tagValue>\"");
						//return;
					}
					
					String firstQ = tagTypeAndVal.getText().substring(0,1);
					String lastQ = tagTypeAndVal.getText().substring(tagTypeAndVal.getText().length() - 1);
					if (firstQ.equals("\"") && lastQ.equals("\"")) {
						tagTypeAndVal.setText("Error: MUST specify a tag type and tag value in the format <tagType>:\"<tagValue>\"");
						return;
					}
					
					// Error Check to see if there is a tag type and a tag value.
					String arr[] = tagTypeAndVal.getText().split(":(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					if (arr.length != 2 || arr[0].equals("")) {
						tagTypeAndVal.setText("Error: MUST specify a tag type and tag value in the format <tagType>:\"<tagValue>\"");
						return;
					}
					// Error Check, see if the tag value argument was in fact in quotes
					firstQ = arr[1].substring(0,1);
					lastQ = arr[1].substring(arr[1].length() - 1);
					if (!(firstQ.equals("\"") && lastQ.equals("\""))) {
						tagTypeAndVal.setText("Error: MUST specify a tag type and tag value in the format <tagType>:\"<tagValue>\"");
						return;
					}
					// Strip Quotes
					else {
						arr[1] = arr[1].substring(1,arr[1].length() - 1);
					}
					// Error Check. If the tagValue is null, do not add
					if (arr[1].equals("")) {
						tagTypeAndVal.setText("Error: <tagValue> has no value. Please input a value for <tagValue>");
						return;
					}
					// Add Tag.
					boolean hasPhoto = true;
					// Iterate through each album to find photo
					for(int i = 0; i < NonadminFrame.backend.getUser(userID).albumCollection.size(); i++){
						Album currAlbum = NonadminFrame.backend.getUser(userID).albumCollection.get(i);
						
						// check if photo exists first
						String fileName = descriptions.get(list.getSelectedIndex());
						if(currAlbum.getPhoto(fileName) == null){
							hasPhoto = false;
							continue;
						} else {
							hasPhoto = true;
						}
						
						//check if tag already exists
						if(currAlbum.getPhoto(fileName).getTag(arr[0], arr[1]) == null){
							currAlbum.getPhoto(fileName).addTag(arr[0], arr[1]);
							
							break;
						} else {
							tagTypeAndVal.setText("Tag already exists.");
							System.out.println("tag exists");
							return;
						}
					}
					
					updateTagsList(photoTags);
					String tag = arr[0] + ":" + arr[1];
					int num = taglistmodel.indexOf(tag);
					tagList.setSelectedIndex(num);
					addingTag = false;
				}
				
				if(deletingTag){
					
					String arr[] = tagList.getSelectedValue().split(":(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					// Delete Tag.
					boolean hasPhoto = true;
					for(int i = 0; i < NonadminFrame.backend.getUser(userID).albumCollection.size(); i++){
						Album currAlbum = NonadminFrame.backend.getUser(userID).albumCollection.get(i);
						String fileName = descriptions.get(list.getSelectedIndex());
						// check if photo exists first
						if(currAlbum.getPhoto(fileName) == null){
							hasPhoto = false;
							continue;
						}else{
							hasPhoto = true;
						}
						
						// Check if tag exists
						if(currAlbum.getPhoto(fileName).getTag(arr[0], arr[1]) == null){
							break;
						}else{
							currAlbum.getPhoto(fileName).deleteTag(arr[0], arr[1]);
							break;
						}
					}
					if(listmodel.size() != 0) {
						list.setSelectedIndex(0);
					}
					updateTagsList(photoTags);
					tagList.setSelectedIndex(0);
					deletingTag = false;
				}

				tagMsg.setForeground(getBackground());
				tagTypeAndVal.setEnabled(false);
				tagTypeAndVal.setText("Please specify a tag type and tag value in the format <tagType>:\"<tagValue>\"");
				addTag.setEnabled(true);
				deleteTag.setEnabled(true);
				confirmTag.setEnabled(false);
				cancelTag.setEnabled(false);
				exitTagEditing.setEnabled(true);
				tagList.setEnabled(true);
			}
		});
		cancelTag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				tagMsg.setForeground(getBackground());
				editTags.setEnabled(false);
				tagTypeAndVal.setEnabled(false); 
				tagTypeAndVal.setText("Please specify a tag type and tag value in the format <tagType>:\"<tagValue>\"");
				addTag.setEnabled(true);
				if(taglistmodel.isEmpty()) deleteTag.setEnabled(false);
				else deleteTag.setEnabled(true);
				confirmTag.setEnabled(false);
				cancelTag.setEnabled(false);
				exitTagEditing.setEnabled(true);
				tagList.setEnabled(true);
			}
		});		
		exitTagEditing.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				editTags.setEnabled(true); 
				tagTypeAndVal.setEnabled(false);
				addTag.setEnabled(false);
				deleteTag.setEnabled(false);
				confirmTag.setEnabled(false);
				cancelTag.setEnabled(false);
				slideshow.setEnabled(true);
				display.setEnabled(true);
				list.setEnabled(true);
				add.setEnabled(true);
				recaption.setEnabled(true);
				delete.setEnabled(true);
				move.setEnabled(true);
				back.setEnabled(true);
				tagPanel.setVisible(false); setSize(new Dimension(600,315));
			}
		});
		display.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
				int index = list.getSelectedIndex();
				String photoName = descriptions.get(index);
				ImageFrame.image(user, albumName, photoName);
			}
		});
		slideshow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
				int index = list.getSelectedIndex();
				if(index == -1) index = 0;
				SlideshowFrame.slideshow(user, albumName, descriptions, index);
			}
		});
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try { 
					NonadminFrame.backend.saveUserList(NonadminFrame.backend.userList);
					NonadminFrame.backend.writeuser(user);
				} catch (FileNotFoundException e1) {e1.printStackTrace();}
				NonadminFrame.backend.listMod.clear();
	    	    dispose();
				NonadminFrame.nonadmin(user.userID);
			}
		});
		
		
		// text fields
		photo.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){
            	if(photo.getText().equals("Photo Name"))
            		photo.setText("");
            }
          
        });
		caption.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){
            	if(caption.getText().equals("Caption"))
            		caption.setText("");
            }
         
        });
		tags.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){
            	if(tags.getText().equals("Tags"))
            		tags.setText("");
            }
        });
		date.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){
            	if(date.getText().equals("Date"))
            		date.setText("");
            }
        });
		tagTypeAndVal.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){
            	if(tagTypeAndVal.getText().equals("Please specify a tag type and tag value in the format <tagType>:\"<tagValue>\""))
            		tagTypeAndVal.setText("");
            	if(tagTypeAndVal.getText().equals("Error: MUST specify a tag type and tag value in the format <tagType>:\"<tagValue>\"")
            			|| tagTypeAndVal.getText().equals("Error: <tagValue> has no value. Please input a value for <tagValue>")
            			|| tagTypeAndVal.getText().equals("Tag already exists."))
            		tagTypeAndVal.setText(""); 
            }
        });
	}

	
	/**
	 * Resizes the ImageIcon
	 * @param img image icon to be resized
	 * @param w desired width
	 * @param h desired height
	 * @return Returns the resized ImageIcon
	 */
	  private ImageIcon resize(ImageIcon img, int w, int h){
		  Image srcImg = img.getImage();
		  BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		  Graphics2D g2 = resizedImg.createGraphics();
		  g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		  g2.drawImage(srcImg, 0, 0, w, h, null);
		  g2.dispose();
		  ImageIcon icon = new ImageIcon(resizedImg);
		  return icon;
		}
	  
	  /**
	   * 
	   * Enables JLabels to be shown in JList
	   *
	   */
	  class LabelRenderer implements ListCellRenderer {
	        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	            JLabel renderer = (JLabel) value;
	            renderer.setForeground(isSelected ? Color.blue : list.getForeground());
	            renderer.setBorder(isSelected ? BorderFactory.createLineBorder(Color.blue) : BorderFactory.createEmptyBorder());
	            return renderer;
	        }
	    }
	  
	  /**
	   * Updates the list model to be displayed on the JFrame in real time. 
	   * @param photoAlbum the photo album to be updated.
	   */
	  public void updateList(ArrayList<Photo> photoAlbum){
		  listmodel.clear();
		  for(int i = 0; i < photoAlbum.size(); i++) {
			  ImageIcon icon = new ImageIcon(photoAlbum.get(i).getPhotoName(), photoAlbum.get(i).getPhotoName());
			  ImageIcon resize = resize(icon, 50,50);
			  JLabel label = new JLabel(photoAlbum.get(i).getCaption(), resize, JLabel.CENTER);
			  label.setHorizontalTextPosition(JLabel.CENTER);
			  label.setVerticalTextPosition(JLabel.BOTTOM);
			  descriptions.add(i, icon.getDescription());
			  listmodel.add(i,label);
			  
		  }
	  }
	  /**
	   * Updates the specified tag list of a certain photo
	   * @param photoTag the ArrayList of tags to be updated
	   */
	  public void updateTagsList(ArrayList<Tag> photoTag){
		  taglistmodel.clear();
		  String tag;
		  int ct = 0;
		  for(int i = 0; i < photoTag.size(); i++) {
				if(photoTag.get(i).getTagType().equalsIgnoreCase("location")) {
					tag = String.format("%s:%s",photoTag.get(i).getTagType(), photoTag.get(i).getTagValue());
					taglistmodel.add(ct, tag); ct++;
				}
			}
			// Print person tags next
			for(int i = 0; i < photoTag.size(); i++) {
				if(photoTag.get(i).getTagType().equalsIgnoreCase("person")) {
					tag = String.format("%s:%s",photoTag.get(i).getTagType(), photoTag.get(i).getTagValue());
					taglistmodel.add(ct, tag); ct++;
				}
			}
			// Print any other tags last
			for(int i = 0; i < photoTag.size(); i++) {
				if(photoTag.get(i).getTagType().equalsIgnoreCase("location") || photoTag.get(i).getTagType().equalsIgnoreCase("person"))
					continue;
				else {
					tag = String.format("%s:%s",photoTag.get(i).getTagType(), photoTag.get(i).getTagValue());
					taglistmodel.add(ct, tag); ct++;
				}
			}
			
	  }
	  
		
	  /**
	   * Establishes the Album frame in a JFrame 
	   * @param u the user used to distinguish the current logged in user
	   * @param aN the album used to distinguish the current album to be looked at
	   */
		public static void album(User u, String aN){
			JFrame frame = new AlbumFrame("Album", u, aN);
			frame.setSize(600,345);
			//frame.setMinimumSize(new Dimension(400,300));
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setResizable(true);
			frame.addWindowListener(new WindowAdapter()
		       {
					// Saves the session upon closing
		    	   public void windowClosing(WindowEvent e) 
		    	   {
		    		   try { 
							NonadminFrame.backend.writeuser(user);
							NonadminFrame.backend.saveUserList(NonadminFrame.backend.userList);
						} catch (FileNotFoundException e1) {e1.printStackTrace();}
						
		    	     System.exit(0);
		    	   }
		   	 });
		}
}
