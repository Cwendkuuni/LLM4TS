import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MenuTest {

 private Menu menu;

 @Test
 public void testConstructor1() {
 menu = new Menu();
 assertNull(menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructor2() {
 menu = new Menu("Title", "", "", "", false, null);
 assertEquals("Title", menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructor3() {
 menu = new Menu("Title", "", "", "TextFilePath", false, null);
 assertEquals("Title", menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertEquals("TextFilePath", menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructor4() {
 menu = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", false, null);
 assertEquals("Title", menu.strTitle);
 assertEquals("PicPath", menu.strPicPath);
 assertEquals("AudioPath", menu.strAudioPath);
 assertEquals("TextFilePath", menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_noError() throws InterruptedException, IOException {
 menu = new Menu();
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_logError() throws InterruptedException, IOException {
 menu = new Menu();
 final String logFilePath = "test_log.txt";
 File logFile = new File(logFilePath);
 FileOutputStream logFileOS = new FileOutputStream(logFile);
 PrintStream logPrinter = new PrintStream(logFileOS);
 System.setOut(logPrinter);
 menu.init();
 assertTrue(menu.m_Error);
 logFileOS.close();
 }

 @Test(expected = InterruptedException.class)
 public void testInit_threadInterrupted() throws IOException, InterruptedException {
 menu = new Menu();
 Thread.currentThread().interrupt();
 menu.init();
 }

 @Test
 public void testDVDMainMenu_noTextFile() {
 menu = new Menu();
 menu.DVDMainMenu();
 assertFalse(menu.m_Error);
 }

 @Test(expected = FileNotFoundException.class)
 public void testDVDMainMenu_textFileNotFound() throws IOException {
 menu = new Menu("", "TextFilePath", false, null);
 menu.DVDMainMenu();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenu_invalidTextFileFormat() throws IOException {
 menu = new Menu("", "TextFilePath", false, null);
 final String textFileContent = "Invalid Format\n";
 final String expectedOutput = "It appears that your text file is not\nformatted correctly.  You currently have\n2 vob files, \nhowever your text file is showing\n3 lines.\n";
 try (FileOutputStream textFileOS = new FileOutputStream("TextFilePath");) {
 textFileOS.write(textFileContent.getBytes());
 textFileOS.close();
 }
 menu.DVDMainMenu();
 assertTrue(menu.m_Error);
 assertEquals(expectedOutput, menu.m_GUI.txtAreaOutput.getText());
 }

 @Test
 public void testDVDMainMenu_videoFilesNotFound() {
 menu = new Menu("Title", "TextFilePath", false, null);
 menu.DVDMainMenu();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Title", "TextFilePath", false, null);
 menu.DVDMainMenu();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testCreateXML_success() throws IOException {
 menu = new Menu("Title", "TextFilePath", false, null);
 menu.DVDMainMenu();
 menu.createXML();
 assertFalse(menu.m_Error);
 }
}