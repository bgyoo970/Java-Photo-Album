package cs213.photoAlbum.simpleview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import cs213.photoAlbum.control.DataControl;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.model.User;

/**
 * 
 * @author Brian Yoo
 *
 */
public class ImageFrame extends JFrame {
	
	JButton exit = new JButton("Exit");
	// filled in for sake of seeing where they are in panel
	JLabel caption = new JLabel("<caption>");
	JLabel date = new JLabel("<date>");
	JLabel tags = new JLabel("<tags>");

	static DataControl control = new DataControl();
	Backend backend = new Backend();
	static User user;
	static String albumName;
	static String photoName;
	
	/**
	 * Constructor for the ImageFrame.
	 * @param title The name for the current window
	 * @param u the specified user that is currently logged in
	 * @param aN the current album to be looked at
	 * @param pN the current photo to be looked at
	 */
	public ImageFrame(String title, User u, String aN, String pN){
		super(title);
		// Load everything.
		try { 
			NonadminFrame.backend.userList = NonadminFrame.backend.loadUserList(); 
			NonadminFrame.backend.readuser(u.userID);
		} catch (ClassNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }
		user = u;
		albumName = aN;
		photoName = pN;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		ImageIcon icon = new ImageIcon(photoName);
		// Have to do caption.setText() etc and put in actual info
		Photo photo = user.getAlbum(aN).getPhoto(pN);
		caption.setText(photo.getCaption());
		date.setText(photo.getCal());
		ArrayList<Tag> tags = photo.getTagList();
		
		c.gridx = 0; c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.7;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JScrollPane display = new JScrollPane(new JLabel(icon),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		display.setViewportView(new JLabel(icon));
		add(display, c);

		c.gridx = 0; c.gridy = 1;
		c.weighty = 0.3;
		// Bottom
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.setBorder(new EmptyBorder(7,7,7,7));
		

		JPanel b1 = new JPanel();
		b1.setLayout(new BoxLayout(b1, BoxLayout.Y_AXIS));
		b1.setBorder(new EmptyBorder(7,7,7,7));
		b1.setAlignmentX(CENTER_ALIGNMENT);
		b1.add(caption); 
		b1.add(date); 
	
		JScrollPane b2 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		b2.setPreferredSize(new Dimension(getWidth(), 50));
		ArrayList<String> tagsString = tagsToString(tags);
		JList<String> tagsList = createList(tagsString);
		b2.setViewportView(tagsList);
		
		JPanel b3 = new JPanel();
		b3.setLayout(new BorderLayout());
		b3.setBorder(new EmptyBorder(7,0,0,0));
		exit.setPreferredSize(new Dimension(100,25));
		b3.add(exit, BorderLayout.LINE_END);
		
		bottom.add(b1, BorderLayout.NORTH);
		bottom.add(b2, BorderLayout.CENTER);
		bottom.add(b3, BorderLayout.SOUTH);
		add(bottom, c);
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
				AlbumFrame.album(user, albumName);
			}
		});
		
		
	}
	
	  /**
	   * Establishes the Image frame in a JFrame 
	   * @param u the user used to distinguish the current logged in user
	   * @param aN the album used to distinguish the current album to be looked at
	   * @param pN the photo use to distinguish the current photo to be looked at
	   */
	public static void image(User u, String aN, String pN){
		//control.load();
		JFrame frame = new ImageFrame("Image Display", u, aN, pN);
		frame.setSize(400,500);
		frame.setMinimumSize(new Dimension(100,100));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.addWindowListener(new WindowAdapter() {
	    	   public void windowClosing(WindowEvent e) {

	    	     AlbumFrame.album(user, albumName);
	    	   }
	   	 });
	}
	
	/**
	 * Create a JList given an ArrayList of strings
	 * @param al an ArrayList of Strings
	 * @return a Jlist full of the entries from the given parameter
	 */
	public JList<String> createList(ArrayList<String> al){
		DefaultListModel<String> listmodel = new DefaultListModel<String>();
		for(int i = 0; i < al.size(); i++) {
			listmodel.add(i,al.get(i));
		}
		JList<String> jl = new JList<String>(listmodel);
		
		return jl;		
	}
	
	/**
	 * Obtains the string values from the Tag ArrayList and stores them into an ArrayList of Strings
	 * @param tags the specified ArrayList of tags to be converted
	 * @return the ArrayList of strings that contain the entries from the given parameter
	 */
	public ArrayList<String> tagsToString(ArrayList<Tag> tags){
		if(tags == null) return null;
		ArrayList<String> list = new ArrayList();
		for(int i = 0; i < tags.size(); i++){
			list.add(i,tags.get(i).getTagType() + ":" + tags.get(i).getTagValue());
		}
		return list;
	}

}
