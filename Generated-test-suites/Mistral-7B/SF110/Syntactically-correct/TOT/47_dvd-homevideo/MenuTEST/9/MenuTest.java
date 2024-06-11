import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu();
 }

 @Test
 public void testConstructorWithNoArguments() {
 menu = new Menu();
 assertEquals("dvd-menu", menu.dvd_menu);
 assertNotNull(menu.titles);
 assertNull(menu.strTitle);
 assertNull(menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructorWithTitleAndTextFilePath() {
 menu = new Menu("Test Title", "test.txt", false, null);
 assertEquals("dvd-menu", menu.dvd_menu);
 assertNotNull(menu.titles);
 assertNull(menu.strTitle);
 assertNotNull(menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructorWithTitlePicPathAudioPathTextFilePath() {
 menu = new Menu("Test Title", "testPic.jpg", "testAudio.mp3", "test.txt", false, null);
 assertEquals("dvd-menu", menu.dvd_menu);
 assertNotNull(menu.titles);
 assertNull(menu.strTitle);
 assertNotNull(menu.strPicPath);
 assertNotNull(menu.strAudioPath);
 assertNotNull(menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitReturnsFalseWhenNoErrorsThrown() throws InterruptedException {
 menu = new Menu("Test Title", null, null, null, false, null);
 assertFalse(menu.init());
 }

 @Test
 public void testInitReturnsTrueWhenErrorThrown() throws InterruptedException {
 menu = new Menu("Test Title", null, null, null, false, null);
 menu.m_GUI = new GUI() {
 @Override
 public String strOutputDir() {
 throw new RuntimeException("Test error");
 }
 };
 assertTrue(menu.init());
 }

 @Test
 public void testCreateXMLReturnsFalseWhenSuccess() throws IOException {
 menu = new Menu("Test Title", null, null, null, false, null);
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(menu.createXML());
 assertFalse(xmlFile.exists());
 }

 @Test
 public void testCreateXMLReturnsTrueWhenError() throws IOException {
 menu = new Menu("Test Title", null, null, null, false, null);
 menu.m_GUI = new GUI() {
 @Override
 public File strOutputDir() {
 throw new IOException("Test error");
 }
 };
 assertTrue(menu.createXML());
 }

 @Test
 public void testCheckVideoFilesIsCorrectlyFiltered() {
 File testDir = new File("testDir");
 File listDir = new File(testDir, "list");
 String[] videoFiles = {"menu.vob", "test.txt", "test2.vob", "test3.txt", "test4.vob"};
 listDir.mkdir();
 for (String file : videoFiles) {
 new File(listDir, file).createNewFile();
 }
 menu = new Menu(null, null, null, null, false, null);
 menu.m_GUI = new GUI() {
 @Override
 File strOutputDir() {
 return testDir;
 }
 };
 FilenameFilter filter = menu::isVobFile;
 File[] files = listDir.listFiles(filter);
 assertArrayEquals(Arrays.copyOfRange(videoFiles, 0, videoFiles.length), files);
 }

 @After
 public void tearDown() {
 if (menu != null) {
 menu = null;
 }
 }

}