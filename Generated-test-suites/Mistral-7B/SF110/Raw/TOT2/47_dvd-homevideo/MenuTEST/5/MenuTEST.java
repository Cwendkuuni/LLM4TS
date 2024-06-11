import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.File;
import java.io.FileFilter;
import java.io.IOException;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "");
 }

 @Test
 public void testConstructorWithNoArguments() {
 Menu testMenu = new Menu();
 assertEquals("dvd-menu", testMenu.dvd_menu);
 assertNull(testMenu.strTitle);
 assertNull(testMenu.strPicPath);
 assertNull(testMenu.strAudioPath);
 assertNull(testMenu.strTextFilePath);
 assertNull(testMenu.pal_menu);
 assertNull(testMenu.baseErr);
 assertNull(testMenu.video_files);
 assertNull(testMenu.m_GUI);
 assertNull(testMenu.m_Thread);
 assertFalse(testMenu.m_Error);
 }

 @Test
 public void testConstructorWithTitleArgument() {
 Menu testMenu = new Menu("Test Title", "");
 assertEquals("dvd-menu", testMenu.dvd_menu);
 assertEquals("Test Title", testMenu.strTitle);
 assertNull(testMenu.strPicPath);
 assertNull(testMenu.strAudioPath);
 assertNull(testMenu.strTextFilePath);
 assertFalse(testMenu.pal_menu);
 assertNull(testMenu.baseErr);
 assertNull(testMenu.video_files);
 assertNull(testMenu.m_GUI);
 assertNull(testMenu.m_Thread);
 assertFalse(testMenu.m_Error);
 }

 @Test
 public void testConstructorWithTitlePicAudioTextFilePathArgument() {
 Menu testMenu = new Menu("Test Title", "test_pic.jpg", "test_audio.mp3", "test_text.txt", false, null);
 assertEquals("dvd-menu", testMenu.dvd_menu);
 assertEquals("Test Title", testMenu.strTitle);
 assertEquals("test_pic.jpg", testMenu.strPicPath);
 assertEquals("test_audio.mp3", testMenu.strAudioPath);
 assertEquals("test_text.txt", testMenu.strTextFilePath);
 assertFalse(testMenu.pal_menu);
 assertNull(testMenu.baseErr);
 assertNull(testMenu.video_files);
 assertNull(testMenu.m_GUI);
 assertNull(testMenu.m_Thread);
 assertFalse(testMenu.m_Error);
 }

 @Test
 public void testInitMethod() {
 menu.init();
 assertTrue(menu.m_Error || menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log"));
 }

 @Test
 public void testDVDMainMenuMethodNormalCase() {
 menu.strTextFilePath = "test_text.txt";
 menu.init();
 assertFalse(menu.m_Error);
 File ListDir = new File(menu.m_GUI.strOutputDir);
 FileFilter filter = new FileNameFilter() {
 boolean accept(File dir, String name) {
 if (name.endsWith("vob")) {
 return true;
 }
 return false;
 }
 };
 String[] video_files = ListDir.listFiles(filter);
 assertEquals(1, video_files.length);
 assertTrue(menu.m_GUI.lblAuthor.isEnabled());
 assertTrue(menu.m_GUI.lblAuthorProg.isEnabled());
 assertTrue(menu.m_GUI.prgAuthor.isEnabled());
 }

 @Test(expected = NullPointerException.class)
 public void testDVDMainMenuMethodNullTextFilePath() {
 menu.strTextFilePath = null;
 menu.init();
 assertFalse(menu.m_Error);
 menu.DVDMainMenu();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDVDMainMenuMethodInvalidTextFilePath() {
 menu.strTextFilePath = "test_text.txt";
 menu.init();
 menu.DVDMainMenu();
 File ListDir = new File(menu.m_GUI.strOutputDir);
 FileFilter filter = new FileNameFilter() {
 boolean accept(File dir, String name) {
 if (name.endsWith("vob")) {
 return true;
 }
 return false;
 }
 };
 String[] video_files = ListDir.listFiles(filter);
 assertFalse(menu.m_Error);
 assertFalse(new File(menu.m_GUI.strOutputDir + "/log/dvd-menu.log").exists());
 }

 @Test(expected = InterruptedException.class)
 public void testInitMethodInterrupted() {
 Thread testThread = new Thread(() -> {
 try {
 Thread.sleep(500);
 }
 catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 });
 menu.m_Thread = testThread;
 testThread.start();
 menu.init();
 }

 @Test public void testCreateXMLMethod() {
 menu.strTextFilePath = "test_text.txt";
 menu.init();
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test(expected = IOException.class)
 public void testCreateXMLMethodIOException() {
 menu.strTextFilePath = "test_text.txt";
 menu.init();
 try {
 FileWriter writer = new FileWriter(new File(menu.m_GUI.strOutputDir + "/vmgm.xml"));
 writer.write("test");
 writer.close();
 }
 catch (IOException e) {
 throw new RuntimeException(e);
 }
 menu.createXML();
 }
}