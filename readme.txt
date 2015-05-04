/* Photo Album: Design and Implementation
 * API (javadoc HTML)
 * Complete Code (plus UML and updated javadoc HTML)
 * 
 * Brian Yoo 
 */
 
 *** All use of this code must comply with the Rutgers University Code of Student Conduct.

Upon running the applicaiton, an login page will prompt for a user name.
To create a new user, one must log into the admin account by typing in "admin". This will bring you to the administrator frame
where one can create users or delete existing ones. You may not create users with the same name.

If you type in an existing user to the login prompt, the application will bring you to a Non-Admin page where the user
can create, delete, edit albums. A list of albums belonging to the user will appear along with information about the photos
it contains. One also has to option to search through albums for photos by their date or their tags. Searching through this method
will give you the option to create a new album from the list of photos that were found from the search.

Upon selecting an opening an album, a new window will appear, allowing for one to create, delete, move photos and edit their captions.
In addition, one may present their photo through a slideshow or a simple display button to view the photos. Tags belonging to the
photos can also be added, deleted, or edited.


An Model-View-Control (MVC) architechtural pattern was used to implement this program. Java Swing was utilized to create the
GUI inside the view package. Using serialization to persist objects and data belonging to each specfici user, 
the modifications made by each users were stored into a text file. The repository comes
with a sample user <bgy2> along with some photos to add from the photos file (../PhotoAlbum45/data/photos)
In order to add a photo to the album one may enter (as an example):
C:\Users\JohnDoe\Pictures\apple.jpg

Javadocs and comments are provided throughout the code to give a better understand as to what the code does.
cs213.photoAlbum.simpleview will hold the code that implements all of the Java Swing files for the GUI. 
