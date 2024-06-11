import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", null, null, null, false, null);
 }

 @Test
 public void testConstructor_NoArgs() {
 menu = new Menu();
 assertTrue(menu.dvd_menu.startsWith("dvd-menu"));
 assertNull(menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructor_TitleOnly() {
 menu = new Menu("Test Menu", null, null, null, false, null);
 assertTrue(menu.dvd_menu.startsWith("dvd-menu"));
 assertTrue(menu.strTitle.equals("Test Menu"));
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructor_TitlePicAudioTextFilePath() {
 String title = "Test Menu";
 String picPath = "test.png";
 String audioPath = "test.wav";
 String textFilePath = "test.txt";
 menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
 assertTrue(menu.dvd_menu.startsWith("dvd-menu"));
 assertTrue(menu.strTitle.equals(title));
 assertTrue(menu.strPicPath.equals(picPath));
 assertTrue(menu.strAudioPath.equals(audioPath));
 assertTrue(menu.strTextFilePath.equals(textFilePath));
 assertNull(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_NoErrors() {
 menu = new Menu("Test Menu", null, null, null, false, null);
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_Error() {
 menu = new Menu("Test Menu", null, null, null, false, null);
 menu.m_GUI = new GUI() {
 @Override
 public String strOutputDir() {
 return "error";
 }
 };
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testInit_ThreadInterruptedException() {
 menu = new Menu("Test Menu", null, null, null, false, null);
 Thread thread = new Thread(() -> {
 try {
 Thread.sleep(5000);
 }
 catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 });
 menu.m_Thread = thread;
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testCreateXML_NoErrors() {
 menu = new Menu("Test Menu", null, null, null, false, null);
 menu.init();
 assertFalse(menu.m_Error);
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXML_IOException() {
 menu = new Menu("Test Menu", null, null, null, false, null);
 menu.init();
 menu.m_GUI = new GUI() {
 @Override
 public String strOutputDir() {
 throw new IOException();
 }
 };
 assertFalse(menu.createXML());
 assertTrue(menu.m_Error);
 }

 @After
 public void tearDown() {
 menu = null;
 }

}