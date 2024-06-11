import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 }

 @Test
 public void testConstructor() {
 Menu menu = new Menu();
 assertNotNull(menu);

 menu = new Menu("Test Menu", "", "", "", false, null);
 assertNotNull(menu.strTitle);
 assertNotNull(menu.titles);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testDVDMainMenu_NoFiles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.DVDMainMenu();
 String output = menu.m_GUI.txtAreaOutput.getText();
 assertTrue(output.contains("dvd-menu: command not found"));
 assertTrue(output.contains("Could not locate dvd-menu in your path"));
 }

 @Test
 public void testDVDMainMenu_ValidFiles() {
 String[] videoFiles = {"file1.vob", "file2.vob"};
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.video_files = videoFiles;
 menu.DVDMainMenu();
 String output = menu.m_GUI.txtAreaOutput.getText();
 assertFalse(output.contains("dvd-menu: command not found"));
 assertFalse(output.contains("Could not locate dvd-menu in your path"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDVDMainMenu_InvalidTitleLength() {
 String[] videoFiles = {"file1.vob", "file2.vob"};
 menu = new Menu("Test Menu".repeat(91), "", "", "", false, null);
 menu.video_files = videoFiles;
 menu.DVDMainMenu();
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 assertTrue(menu.createXML());
 }

 @Test
 public void testCreateXML_NoFiles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 assertFalse(menu.createXML());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateXML_InvalidTitleLength_XML() {
 menu = new Menu("Test Menu".repeat(91), "", "", "", false, null);
 menu.createXML();
 }
}