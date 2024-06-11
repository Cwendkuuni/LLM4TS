import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.StringJoiner;
import java.io.File;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 }

 @Test
 public void testInitSuccess() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitFailure() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI() {
 @Override
 public String strOutputDir() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void MessageBox(String message, int type) {
 // Do nothing
 }
 };
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testCreateXMLSuccess() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXMLFailure() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI() {
 @Override
 public String strOutputDir() {
 throw new UnsupportedOperationException();
 }
 };
 menu.createXML();
 assertFalse(new File(menu.m_GUI.strOutputDir + "/vmgm.xml").exists());
 }

 @Test
 public void testDVDMainMenuWithNoTextFile() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.init();
 menu.DVDMainMenu();
 StringJoiner joiner = new StringJoiner("\n", "", "\n");
 String output = menu.m_GUI.txtAreaOutput.getText();
 String expected = "/bin/sh: dvd-menu: command not found\n" +
 "DVD_MENU_VERSION=1.4.0\n" +
 "DVD_MENU_SCREEN=1\n" +
 "MENU_TITLE=Test Menu\n" +
 "FRAME#\n";
 assertEquals(expected, joiner.merge(output.split(System.lineSeparator())));
 }

 @Test
 public void testDVDMainMenuWithTextFile() {
 menu = new Menu("Test Menu", "", "", "testTextFile.txt", false, null);
 menu.m_GUI = new GUI() {
 @Override
 public String strOutputDir() {
 return "testOutputDir";
 }

 @Override
 public void MessageBox(String message, int type) {
 // Do nothing
 }
 };
 File textFile = new File("testTextFile.txt");
 try {
 textFile.createNewFile();
 textFile.setWritable(true);
 BufferedWriter writer = new BufferedWriter(new FileWriter(textFile));
 writer.write("BLANK\nBLANK\nBLANK\nTitle 1\nBLANK\nTitle 2\nBLANK\nTitle 3\nBLANK\nDONE");
 writer.close();
 menu.init();
 menu.DVDMainMenu();
 StringJoiner joiner = new StringJoiner("\n", "", "\n");
 String output = menu.m_GUI.txtAreaOutput.getText();
 String expected = "/bin/sh: dvd-menu: command not found\n" +
 "DVD_MENU_VERSION=1.4.0\n" +
 "DVD_MENU_SCREEN=1\n" +
 "MENU_TITLE=Test Menu\n" +
 "FRAME#\n" +
 "Title 1\n" +
 "Title 2\n" +
 "Title 3\n" +
 "0\n" +
 "1\n" +
 "2\n" +
 "3\n";
 assertEquals(expected, joiner.merge(output.split(System.lineSeparator())));
 textFile.delete();
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to write test text file.");
 }
 }

 @Test
 public void testDVDMainMenuWithInvalidTextFileFormat() {
 menu = new Menu("Test Menu", "", "", "testTextFile.txt", false, null);
 menu.m_GUI = new GUI() {
 @Override
 public String strOutputDir() {
 return "testOutputDir";
 }

 @Override
 public void MessageBox(String message, int type) {
 // Do nothing
 }
 };
 File textFile = new File("testTextFile.txt");
 try {
 textFile.createNewFile();
 textFile.setWritable(true);
 BufferedWriter writer = new BufferedWriter(new FileWriter(textFile));
 writer.write("BLANK\nBLANK\nTitle 1\nBLANK\nTitle 1\nBLANK\nTitle 2\nBLANK\nBLANK\nDONE");
 writer.close();
 menu.init();
 menu.DVDMainMenu();
 StringJoiner joiner = new StringJoiner("\n", "", "\n");
 String output = menu.m_GUI.txtAreaOutput.getText();
 String expected = "/bin/sh: dvd-menu: command not found\n" +
 "Your text file is not formatted correctly.\n";
 assertEquals(expected, joiner.merge(output.split(System.lineSeparator())));
 textFile.delete();
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to write test text file.");
 }
 }
}