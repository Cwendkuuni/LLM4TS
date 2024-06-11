import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setUp() {
 gui = new GUI();
 menu = new Menu("Test Menu", "", "", "", false, gui);
 }

 @Test
 public void testConstructorEmpty() {
 Menu emptyMenu = new Menu();
 assertEquals("dvd-menu", emptyMenu.dvd_menu);
 assertNull(emptyMenu.strTitle);
 assertNull(emptyMenu.strPicPath);
 assertNull(emptyMenu.strAudioPath);
 assertNull(emptyMenu.strTextFilePath);
 assertNull(emptyMenu.titles);
 assertFalse(emptyMenu.pal_menu);
 assertNull(emptyMenu.baseErr);
 assertNull(emptyMenu.video_files);
 assertNull(emptyMenu.m_GUI);
 assertNull(emptyMenu.m_Thread);
 assertFalse(emptyMenu.m_Error);
 }

 @Test
 public void testConstructorTitle() {
 Menu titleMenu = new Menu("Test Title", "", "", "", false, gui);
 assertEquals("dvd-menu", titleMenu.dvd_menu);
 assertEquals("Test Title", titleMenu.strTitle);
 assertNull(titleMenu.strPicPath);
 assertNull(titleMenu.strAudioPath);
 assertNull(titleMenu.strTextFilePath);
 assertNull(titleMenu.titles);
 assertFalse(titleMenu.pal_menu);
 assertNull(titleMenu.baseErr);
 assertNull(titleMenu.video_files);
 assertNull(titleMenu.m_GUI);
 assertNull(titleMenu.m_Thread);
 assertFalse(titleMenu.m_Error);
 }

 @Test
 public void testConstructorTitlePicAudioText() {
 String picPath = "test.jpg";
 String audioPath = "test.mp3";
 String textFilePath = "test.txt";
 Menu titleMenuWithPicAudioText = new Menu("Test Title", picPath, audioPath, textFilePath, false, gui);
 assertEquals("dvd-menu", titleMenuWithPicAudioText.dvd_menu);
 assertEquals("Test Title", titleMenuWithPicAudioText.strTitle);
 assertEquals(picPath, titleMenuWithPicAudioText.strPicPath);
 assertEquals(audioPath, titleMenuWithPicAudioText.strAudioPath);
 assertEquals(textFilePath, titleMenuWithPicAudioText.strTextFilePath);
 assertNull(titleMenuWithPicAudioText.titles);
 assertFalse(titleMenuWithPicAudioText.pal_menu);
 assertNull(titleMenuWithPicAudioText.baseErr);
 assertNull(titleMenuWithPicAudioText.video_files);
 assertNull(titleMenuWithPicAudioText.m_GUI);
 assertNull(titleMenuWithPicAudioText.m_Thread);
 assertFalse(titleMenuWithPicAudioText.m_Error);
 }

 @Test
 public void testConstructorTitlePicAudioTextPal() {
 String picPath = "test.jpg";
 String audioPath = "test.mp3";
 String textFilePath = "test.txt";
 Menu titleMenuWithPicAudioTextPal = new Menu("Test Title", picPath, audioPath, textFilePath, true, gui);
 assertEquals("dvd-menu", titleMenuWithPicAudioTextPal.dvd_menu);
 assertEquals("Test Title", titleMenuWithPicAudioTextPal.strTitle);
 assertEquals(picPath, titleMenuWithPicAudioTextPal.strPicPath);
 assertEquals(audioPath, titleMenuWithPicAudioTextPal.strAudioPath);
 assertEquals(textFilePath, titleMenuWithPicAudioTextPal.strTextFilePath);
 assertTrue(titleMenuWithPicAudioTextPal.pal_menu);
 assertNull(titleMenuWithPicAudioTextPal.baseErr);
 assertNull(titleMenuWithPicAudioTextPal.video_files);
 assertNull(titleMenuWithPicAudioTextPal.m_GUI);
 assertNull(titleMenuWithPicAudioTextPal.m_Thread);
 assertFalse(titleMenuWithPicAudioTextPal.m_Error);
 }

 @Test
 public void testInit() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitError() {
 menu = new Menu("Test Menu", "", "", "", false, gui);
 menu.m_Error = true;
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testCreateXML() {
 menu.createXML();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testCreateXMLError() {
 menu = new Menu("Test Menu", "", "", "", false, gui);
 menu.m_Error = true;
 menu.createXML();
 assertTrue(menu.m_Error);
 }
}