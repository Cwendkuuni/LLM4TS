import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 }

 @Test
 public void testConstructorWithNoArguments() {
 Menu menu = new Menu();
 assertEquals("dvd-menu", menu.dvd_menu);
 assertNull(menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructorWithTitleArgument() {
 Menu menu = new Menu("Test Menu", "", "", "", false, null);
 assertEquals("Test Menu", menu.strTitle);
 assertEquals("dvd-menu", menu.dvd_menu);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructorWithTitlePicPathAudioPathTextFilePathArguments() {
 Menu menu = new Menu("Test Menu", "test.jpg", "test.mp3", "test.txt", false, null);
 assertEquals("Test Menu", menu.strTitle);
 assertEquals("dvd-menu", menu.dvd_menu);
 assertEquals("test.jpg", menu.strPicPath);
 assertEquals("test.mp3", menu.strAudioPath);
 assertEquals("test.txt", menu.strTextFilePath);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitMethod() throws InterruptedException {
 menu.init();
 assertTrue(menu.m_Thread != null);
 assertFalse(menu.m_Error || menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log"));
 }

 @Test(expected = NullPointerException.class)
 public void testInitMethodWithNullGUI() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = null;
 menu.init();
 }

 @Test(expected = IllegalThreadStateException.class)
 public void testRunMethod() throws InterruptedException {
 menu.init();
 menu.run();
 menu.join();
 Thread.sleep(100);
 menu.run();
 }

 @Test
 public void testDVDMainMenuMethod() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 File outputDir = new File("outputDir");
 menu.m_GUI = new GUI();
 menu.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 menu.init();
 menu.DVDMainMenu();
 File listDir = new File(outputDir, "log");
 FilenameFilter filter = new FilenameFilter() {
 boolean accept(File dir, String name) {
 if (name.endsWith("vob")) {
 return true;
 }
 return false;
 }
 };
 String[] videoFiles = listDir.listFiles(filter);
 assertNotNull(videoFiles);
 assertEquals(1, videoFiles.length);
 assertNotNull(menu.video_files[0]);
 assertEquals("menu.vob", menu.video_files[0]);
 }

 @Test
 public void testCreateXMLMethod() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 File xmlFile = new File("vmgm.xml");
 File outputDir = new File("outputDir");
 menu.m_GUI = new GUI();
 menu.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 menu.init();
 menu.createXML();
 assertNotNull(xmlFile);
 assertFalse(xmlFile.exists());
 }

 @Test
 public void testCreateXMLMethodWithError() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 File xmlFile = new File("vmgm.xml");
 File outputDir = new File("outputDir");
 menu.m_GUI = new GUI();
 menu.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 menu.init();
 menu.m_Error = true;
 menu.createXML();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testCreateXMLMethodWithMissingTextFile() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 File xmlFile = new File("vmgm.xml");
 File outputDir = new File("outputDir");
 menu.m_GUI = new GUI();
 menu.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 menu.m_GUI.txtTextFile = new File("nonExistentFile.txt");
 menu.init();
 menu.createXML();
 assertTrue(menu.m_Error);
 }
}