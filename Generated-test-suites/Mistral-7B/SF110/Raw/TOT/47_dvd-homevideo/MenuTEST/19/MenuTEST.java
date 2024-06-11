import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.StringJoiner;

public class MenuTest {

 private Menu menu;
 private String[] videoFiles = new String[]{"video1.vob", "video2.vob", "menu.vob"};
 private String textFilePath = "textFile.txt";
 private boolean isPAL = false;
 private GUI gui;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", textFilePath, isPAL, gui);
 }

 @Test
 public void testInit_Success() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_ErrorLogFile() {
 menu = new Menu("Test Menu", null, isPAL, gui);
 menu.init();
 assertTrue(menu.m_Error);
 File logFile = new File(menu.m_GUI.strOutputDir + "/log/dvd-menu.log");
 assertTrue(logFile.exists());
 }

 @Test
 public void testInit_ErrorThreadInterrupted() {
 menu = new Menu("Test Menu", textFilePath, isPAL, gui);
 Thread thread = new Thread(() -> {
 try {
 Thread.sleep(Long.MAX_VALUE);
 }
 catch (InterruptedException e) {
 Thread.currentThread().interrupt();
 }
 });
 menu.init();
 thread.join();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenu_NoTextFile() {
 menu = new Menu("Test Menu", null, isPAL, gui);
 menu.init();
 menu.DVDMainMenu();
 assertEquals("", menu.titles[0]);
 StringJoiner joiner = new StringJoiner("\n", "", "\n");
 for (String file : videoFiles) {
 joiner.add("File: " + file);
 }
 assertEquals(joiner.toString(), menu.dvd_menu);
 }

 @Test
 public void testDVDMainMenu_TextFile_2Titles() {
 menu = new Menu("Test Menu", textFilePath, isPAL, gui);
 menu.init();
 menu.DVDMainMenu();
 assertEquals("Title 1", menu.titles[0]);
 assertEquals("Title 2", menu.titles[1]);
 StringJoiner joiner = new StringJoiner("\n", "", "\n");
 for (String file : videoFiles) {
 joiner.add("File: " + file);
 }
 assertEquals(joiner.toString(), menu.dvd_menu);
 }

 @Test(expected = FileNotFoundException.class)
 public void testDVDMainMenu_TextFileNotFound() {
 menu = new Menu("Test Menu", "notFound.txt", isPAL, gui);
 menu.init();
 menu.DVDMainMenu();
 }

 @Test(expected = IOException.class)
 public void testDVDMainMenu_TextFileIOError() {
 menu = new Menu("Test Menu", textFilePath, isPAL, gui);
 try {
 File textFile = new File(textFilePath);
 FileInputStream inputStream = new FileInputStream(textFile);
 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
 String line;
 StringJoiner joiner = new StringJoiner("\n", "", "\n");
 while ((line = reader.readLine()) != null) {
 joiner.add(line);
 }
 inputStream.close();
 reader.close();
 assertEquals(joiner.toString(), menu.titlesString);
 }
 catch (FileNotFoundException e) {
 throw new IOException();
 }
 menu.init();
 menu.DVDMainMenu();
 }

 @Test
 public void testCreateXML() {
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

}