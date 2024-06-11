import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.StringJoiner;

public class MenuTest {

 private Menu menu;
 private String title;
 private String textFilePath;
 private boolean isPAL;
 private GUI gui;

 @Before
 public void setUp() {
 title = "Test Menu";
 textFilePath = "testTextFile.txt";
 isPAL = false;
 gui = new GUI();
 menu = new Menu(title, textFilePath, isPAL, gui);
 }

 @After
 public void tearDown() {
 menu = null;
 title = null;
 textFilePath = null;
 isPAL = false;
 gui = null;
 }

 @Test
 public void testInit() {
 assertTrue(menu.init());
 }

 @Test
 public void testInitWithInvalidVobFile() {
 menu = new Menu(title, textFilePath, isPAL, gui, new String[]{"invalidFile.vob"});
 assertFalse(menu.init());
 }

 @Test
 public void testDVDMainMenuWithEmptyTextFile() {
 menu = new Menu(title, textFilePath, isPAL, gui);
 menu.DVDMainMenu();
 StringJoiner joiner = new StringJoiner("", "\n", "");
 String expectedOutput = "dvd-menu " + // other command line arguments
 " -f dvd-menu.vob 1 -t Title 1 -f dvd-menu.vob 2 -t Title 2 ...";
 String actualOutput = menu.m_GUI.txtAreaOutput.getText();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDVDMainMenuWithInvalidTextFile() {
 menu = new Menu(title, "invalidTextFile.txt", isPAL, gui);
 menu.DVDMainMenu();
 StringJoiner joiner = new StringJoiner("", "\n", "");
 String expectedOutput = "dvd-menu " + // other command line arguments
 " -f dvd-menu.vob 1 -t Title 1 -f dvd-menu.vob 2 -t Title 2 ...";
 String actualOutput = menu.m_GUI.txtAreaOutput.getText();
 assertNotEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCreateXMLWithEmptyTextFile() {
 menu = new Menu(title, textFilePath, isPAL, gui);
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXMLWithInvalidTextFile() {
 menu = new Menu(title, "invalidTextFile.txt", isPAL, gui);
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertFalse(xmlFile.exists());
 }
}