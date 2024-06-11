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
 assertFalse(menu.init());
 }

 @Test
 public void testDVDMainMenu_NoTextFile() {
 menu = new Menu("Test Menu", null, null, null, false, gui);
 menu.DVDMainMenu();
 StringJoiner joiner = new StringJoiner(" ", "\n", "");
 String expectedOutput = "dvd-menu: command not found\n";
 String actualOutput = menu.m_GUI.txtAreaOutput.getText();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDVDMainMenu_InvalidTextFile() {
 menu = new Menu("Test Menu", "test/invalidTextFile.txt", false, gui);
 menu.DVDMainMenu();
 StringJoiner joiner = new StringJoiner(" ", "\n", "");
 String expectedOutput = "Menu Error - IO Error\n";
 String actualOutput = menu.m_GUI.txtAreaOutput.getText().substring(0, expectedOutput.length() + 10); // remove "Menu Error - "
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testDVDMainMenu_ValidTextFile() {
 menu = new Menu("Test Menu", "test/validTextFile.txt", false, gui);
 menu.DVDMainMenu();
 StringJoiner joiner = new StringJoiner(" ", "\n", "");
 String expectedOutput = "dvd-menu: command not found\n"; // Assuming the textFile contains only one title and the command is not found
 String actualOutput = menu.m_GUI.txtAreaOutput.getText();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Menu", "test/validTextFile.txt", false, gui);
 assertTrue(menu.createXML());
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 xmlFile.delete();
 }
}