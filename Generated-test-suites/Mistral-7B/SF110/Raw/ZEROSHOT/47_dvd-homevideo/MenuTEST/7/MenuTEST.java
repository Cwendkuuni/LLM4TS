import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setUp() {
 gui = new GUI();
 menu = new Menu("Test Title", "", "", "", false, gui);
 }

 @Test
 public void testConstructorWithTitle() {
 Menu menu = new Menu("Test Title", "", "", "", false, gui);
 assertNotNull(menu.strTitle);
 assertEquals("Test Title", menu.strTitle);
 }

 @Test
 public void testConstructorWithTitlePicPath() {
 Menu menu = new Menu("Test Title", "test.jpg", "", "", false, gui);
 assertNotNull(menu.strTitle);
 assertEquals("Test Title", menu.strTitle);
 assertNotNull(menu.strPicPath);
 assertEquals("test.jpg", menu.strPicPath);
 }

 @Test
 public void testConstructorWithTitlePicPathAudioPath() {
 Menu menu = new Menu("Test Title", "test.jpg", "test.mp3", "", false, gui);
 assertNotNull(menu.strTitle);
 assertEquals("Test Title", menu.strTitle);
 assertNotNull(menu.strPicPath);
 assertEquals("test.jpg", menu.strPicPath);
 assertNotNull(menu.strAudioPath);
 assertEquals("test.mp3", menu.strAudioPath);
 }

 @Test
 public void testConstructorWithTitleTextFilePath() {
 Menu menu = new Menu("Test Title", "", "", "test.txt", false, gui);
 assertNotNull(menu.strTitle);
 assertEquals("Test Title", menu.strTitle);
 assertNotNull(menu.strTextFilePath);
 assertEquals("test.txt", menu.strTextFilePath);
 }

 @Test
 public void testConstructorWithTitlePicPathAudioPathTextFilePath() {
 Menu menu = new Menu("Test Title", "test.jpg", "test.mp3", "test.txt", false, gui);
 assertNotNull(menu.strTitle);
 assertEquals("Test Title", menu.strTitle);
 assertNotNull(menu.strPicPath);
 assertEquals("test.jpg", menu.strPicPath);
 assertNotNull(menu.strAudioPath);
 assertEquals("test.mp3", menu.strAudioPath);
 assertNotNull(menu.strTextFilePath);
 assertEquals("test.txt", menu.strTextFilePath);
 }

 @Test
 public void testInit() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitWithError() {
 menu = new Menu("Test Title", "", "", "", true, gui);
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenu() {
 menu = new Menu("Test Title", "", "", "", false, gui);
 menu.DVDMainMenu();
 // Add assertions here to test the output of menu_options variable
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Title", "", "", "", false, gui);
 menu.init();
 assertTrue(menu.createXML());
 }
}