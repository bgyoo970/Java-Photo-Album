package cs213.photoAlbum.simpleview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import cs213.photoAlbum.control.DataControl;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;
/**
 * 
 * @author Michelle Gavino
 *
 */
public class SearchResultsFrame extends JFrame {
	
	JButton create = new JButton("Create Album Using Results");
	JButton back = new JButton("Back");
	JLabel label = new JLabel();
	
	static JList<JLabel> list;
	static DefaultListModel<JLabel> listmodel;
	ArrayList<String> descriptions = new ArrayList<String>();
	
	private static String userID;
	ArrayList<Photo> searchedPhotos = new ArrayList<Photo>();
	private int num = 0;
	/**
	 * Constructor for the SearchResults Frame. How the frame will be constructed is based on the search type and the number of arguments given.
	 * @param title The name for the current window
	 * @param logged_in_user the specified user that is logged in
	 * @param searchType the string used to distinguish if the search was by date or by tag
	 * @param arg1 The first value specified
	 * @param arg2 The second value specified
	 */
	public SearchResultsFrame(String title, User logged_in_user, String searchType, String arg1, String arg2){
		super(title);
		setLayout(new BorderLayout());
		
		// load the photos.
		try {NonadminFrame.backend.userList = NonadminFrame.backend.loadUserList();}
		catch (ClassNotFoundException e1) {e1.printStackTrace();}
		catch (IOException e1) {e1.printStackTrace();}
		
		userID = logged_in_user.userID;
		
		// Upper JPanel for the pictures
		JPanel display = new JPanel();
		JScrollPane scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);				// Scroll pane to show imageicons
		
		// Photo List. Icon Representation
		// Fill this combo box with photos obtained from the SBDate or SBTags
		JComboBox<String> combo = new JComboBox<String>();
		
		// If SearchByDate, use both arg1 (startD) and arg2 (endD)
		if (searchType.equals("SearchByDate")) {
			// Obtain the album of all valid photos.
			this.searchedPhotos = NonadminFrame.control.getPhotosByDate(logged_in_user, arg1, arg2);
			
			// Display the album.
			listmodel = new DefaultListModel<JLabel>();
			listmodel.clear();
			updateList(searchedPhotos);
			list = new JList<JLabel>(listmodel);
			list.setCellRenderer(new LabelRenderer());
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setSelectedIndex(0); 
			
			scroll.setViewportView(list);
			scroll.setBorder(new EmptyBorder(3,3,3,3));
			
			//scroll.add(previews);
			//display.add(scroll);
			
		}
		// If SearchByTags, only use arg1 (stringStream). arg2 == null.
		else if(searchType.equals("SearchByTags")) {
			// Obtain the album of all valid photos.
			this.searchedPhotos = NonadminFrame.control.getPhotosByTag(logged_in_user, arg1);
			
			// Display the album.
			listmodel = new DefaultListModel<JLabel>();
			listmodel.clear();
			updateList(searchedPhotos);
			list = new JList<JLabel>(listmodel);
			list.setCellRenderer(new LabelRenderer());
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setSelectedIndex(0); 
			
			scroll.setViewportView(list);
			scroll.setBorder(new EmptyBorder(3,3,3,3));
		}
		
		// Load the userList.
		try {NonadminFrame.backend.userList = NonadminFrame.backend.loadUserList();}
		catch (ClassNotFoundException e1) {e1.printStackTrace();}
		catch (IOException e1) {e1.printStackTrace();}
		
		// Lower JPanel for the buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 3));
		buttons.setBorder(new EmptyBorder(7,7,7,7));
		buttons.add(create);
		buttons.add(back);
		
		// Add to the JPanel
		add(scroll, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		
		// Create Listener
		create.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				User usertemp = NonadminFrame.backend.getUser(userID);
				
				int albumCollectionSize = NonadminFrame.backend.getUser(userID).albumCollection.size();
				for (int i = 0; i < albumCollectionSize; i++) {
					if (NonadminFrame.backend.getUser(userID).albumCollection.get(i).getAlbumName().equals("newAlbum" + " " + Integer.toString(num))) {
						num++;
						i = 0;
					}
				}
				Album tempalbum = NonadminFrame.control.createAlbum(usertemp, "newAlbum" + " " + Integer.toString(num));
				searchedPhotos = elimiateDuplicates(searchedPhotos);
				tempalbum.setPhotoAlbum(searchedPhotos);
				int userIndex = NonadminFrame.backend.getUserIndex(userID);
				//NonadminFrame.backend.userList.get(userIndex).albumCollection.add(tempalbum); delete
				
				try { NonadminFrame.backend.saveUserList(NonadminFrame.backend.userList); } 
				catch (FileNotFoundException e1) {e1.printStackTrace();}
				System.out.println(usertemp.userID);
				
				NonadminFrame.backend.listMod.clear();
				dispose();
				NonadminFrame.nonadmin(userID);
			}
		});
		
		// Back Listener
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
				NonadminFrame.backend.listMod.clear();
				try { NonadminFrame.backend.saveUserList(NonadminFrame.backend.userList); } 
				catch (FileNotFoundException e1) {e1.printStackTrace();}
				NonadminFrame.nonadmin(userID);
			}
		});
		
		
	}
	
	/**
	 * Establishes the SearchResults frame in a JFrame 
	 * @param searchType the string used to distinguish if the search was by date or by tag
	 * @param logged_in_user the specified user that is logged in
	 * @param startD the start of the range of dates specified
	 * @param endD the end of the range of dates specified
	 */
	public static void searchresults(String searchType, User logged_in_user, String startD, String endD){
		JFrame frame = new SearchResultsFrame("Slideshow", logged_in_user, searchType, startD, endD);
		frame.setSize(500,400);
		frame.setMinimumSize(new Dimension(500,400));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		userID = logged_in_user.userID;
	}
	/**
	* Establishes the SearchResults frame in a JFrame 
	 * @param searchType the string used to distinguish if the search was by date or by tag
	 * @param logged_in_user the specified user that is logged it
	 * @param stringStream the stream of tag types and tag values used to search for the photos that contain the specified values.
	 */
	public static void searchresults(String searchType, User logged_in_user, String stringStream){
		JFrame frame = new SearchResultsFrame("Slideshow", logged_in_user, searchType, stringStream, null);
		frame.setSize(500,400);
		frame.setMinimumSize(new Dimension(500,400));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		userID = logged_in_user.userID;
	}
	
	/**
	 * Eliminates any dupilcate photos within a photo ArrayList. Meant to filter out adding doubles for SearchByDate/Tag
	 * @param searchedPhotos ArrayList of Photos to be filtered through
	 * @return the ArrayList<Photo> with no duplicates
	 */
	private ArrayList<Photo> elimiateDuplicates(ArrayList<Photo> searchedPhotos) {
		for (int i = 0; i < searchedPhotos.size(); i++) {
			String currPhotoName = searchedPhotos.get(i).getPhotoName();
			for (int j = 0; j < searchedPhotos.size(); j++) {
				if(i != j && currPhotoName.equals(searchedPhotos.get(j).getPhotoName())){
					searchedPhotos.remove(j);
				}
			}
		}
		return searchedPhotos;
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
	   * Enables JLabels to be shown in JList
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
	 * Updates the list model
	 * @param searchedPhotos the list of photos to be added to the list model
	 */
	public void updateList(ArrayList<Photo> searchedPhotos){
		if (searchedPhotos == null) {
			searchedPhotos = new ArrayList<Photo>();
		}
		for (int i = 0; i < searchedPhotos.size(); i++) {
			ImageIcon icon = new ImageIcon(searchedPhotos.get(i).getPhotoName(), searchedPhotos.get(i).getPhotoName());
			ImageIcon resize = resize(icon, 50,50);
			JLabel label = new JLabel(searchedPhotos.get(i).getCaption(), resize, JLabel.CENTER);
			label.setHorizontalTextPosition(JLabel.CENTER);
			label.setVerticalTextPosition(JLabel.BOTTOM);
			descriptions.add(i, icon.getDescription());
			listmodel.add(i,label);
		}
	}
}
