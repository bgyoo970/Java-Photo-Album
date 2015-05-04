package cs213.photoAlbum.simpleview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import cs213.photoAlbum.model.User;
/**
 * 
 * @author Michelle Gavino
 *
 */
public class SlideshowFrame extends JFrame {
	
	JButton left = new JButton("Left");
	JButton right = new JButton("Right");
	JButton exit = new JButton("Exit");
	JLabel label = new JLabel();
	
	static User user;
	static String albumName;
	static ArrayList<String> filePaths;
	static int index;
	
	/**
	 * Constructor for the SlideShow Frame.
	 * @param The name for the current window
	 * @param u the specified user that is currently logged in
	 * @param aN the current album to be looked at
	 * @param fP the ArrayList of Strings of filepaths
	 * @param i the current iteration of photos
	 */
	public SlideshowFrame(String title, User u, String aN, ArrayList<String> fP, int i){
		super(title);
		setLayout(new BorderLayout());
		
		user = u;
		albumName = aN;
		filePaths = fP;
		index = i;
		
		if(filePaths.size() == 0){ left.setEnabled(false); right.setEnabled(false); }
		
		if(i == 0) left.setEnabled(false);
		if(i+1 == filePaths.size()) right.setEnabled(false);
		
		// Image View
		String photoName = filePaths.get(i);
		
	
		ImageIcon icon = new ImageIcon(photoName);
		
		JScrollPane display = new JScrollPane(new JLabel(icon),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		display.setViewportView(new JLabel(icon));
		
		
		
		// Navigation
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		buttons.setBorder(new EmptyBorder(7,7,7,7));
		left.setPreferredSize(new Dimension(100,25));
		right.setPreferredSize(new Dimension(100,25));
		exit.setPreferredSize(new Dimension(75,25));
		
		c.gridx = 1; c.gridy = 0;
		c.insets = new Insets(0,125,0,3);
		c.weightx = 1.0; 
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		buttons.add(left,c);
		c.gridx = 2;
		c.insets = new Insets(0,3,0,3);
		buttons.add(right,c);
		c.gridx = 3; 
		c.insets = new Insets(0,50,0,3);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		buttons.add(exit,c);
		

		
		add(display, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
				AlbumFrame.album(user, albumName);
			}
		});
		left.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
				slideshow(user, albumName, filePaths, index-1);
			}
		});
		right.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
				slideshow(user, albumName, filePaths, index+1);
			}
		});
		
	}
	
	/**
	 * Establishes the SlideshowFrame in a JFrame
	 * @param id the user identification to distinguish the current logged in user
	 */
	public static void slideshow(User u, String aN, ArrayList<String> paths, int i){
		JFrame frame = new SlideshowFrame("Slideshow", u, aN, paths, i);
		frame.setSize(500,400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.addWindowListener(new WindowAdapter() {
	    	   public void windowClosing(WindowEvent e) {
	    	     AlbumFrame.album(user, albumName);
	    	   }
	   	 });
	}
}
