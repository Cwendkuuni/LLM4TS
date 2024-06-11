import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.StringTokenizer;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 }

 @Test
 public void testConstructor() {
 Menu menu = new Menu();
 assertNotNull(menu.dvd_menu);
 assertEquals("dvd-menu", menu.dvd_menu);

 menu = new Menu("Test Menu", "", "", "", false, null);
 assertNotNull(menu.strTitle);
 assertEquals("Test Menu", menu.strTitle);

 menu = new Menu("Test Menu", "", "", "test.txt", false, null);
 assertNotNull(menu.strTextFilePath);
 assertEquals("test.txt", menu.strTextFilePath);
 }

 @Test
 public void testInit_NoError() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 assertTrue(menu.init());
 }

 @Test
 public void testInit_Error() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = null;
 assertFalse(menu.init());
 }

 @Test
 public void testDVDMainMenu_NoError() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI();
 menu.init();
 menu.DVDMainMenu();

 File dir = new File(menu.m_GUI.strOutputDir);
 assertTrue(dir.exists());

 String menuOptions = menu.dvd_menu;
 assertFalse(menuOptions.isEmpty());

 String firstVob = menuOptions.split(" ")[1].split("/")[1];
 assertEquals("menu.vob", firstVob);
 }

 @Test
 public void testCreateXML_NoError() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI();
 menu.init();
 menu.createXML();

 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateXML_NullGui() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = null;
 menu.createXML();
 }

 @Test(expected = IOException.class)
 public void testCreateXML_IOException() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI();
 menu.m_GUI.txtTextFile = null;
 menu.createXML();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateXML_IllegalArgumentException() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI();
 menu.titles = new String[0];
 menu.createXML();
 }

 @Test
 public void testGetTitle() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.strTitle = "Test Title";
 assertEquals("Test Title", menu.getTitle());
 }

 @Test
 public void testGetTitle_NullTitle() {
 menu = new Menu();
 assertNull(menu.getTitle());
 }

 private String[] getTestFiles(String dirPath) {
 File dir = new File(dirPath);
 String[] files = dir.list(new FilenameFilter() {
 @Override
 public boolean accept(File dir, String name) {
 return name.endsWith(".vob");
 }
 });
 return files;
 }

 @Test
 public void testDVDMainMenu_InvalidTextFile() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI();
 menu.init();
 menu.strTextFilePath = "non-existent.txt";
 menu.DVDMainMenu();

 String menuOptions = menu.dvd_menu;
 assertTrue(menuOptions.contains("Your text file can only have a maximumof 10 titles!"));
 assertFalse(new File(menu.m_GUI.strOutputDir).exists());
 }

 @Test
 public void testDVDMainMenu_TextFileContainsMoreThanTenTitles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI();
 menu.init();
 String textFileContent = "Title 1\nTitle 2\nTitle 3\nTitle 4\nTitle 5\nTitle 6\nTitle 7\nTitle 8\nTitle 9\nTitle 10\nTitle 11\nTitle 12";
 menu.m_GUI.txtTextFile = new File("test.txt");
 menu.m_GUI.txtTextFile.setContent(textFileContent);
 menu.DVDMainMenu();

 String menuOptions = menu.dvd_menu;
 assertTrue(menuOptions.contains("Your text file can only have a maximumof 10 titles!  Please edit your text file and rerun dvd-homevideo."));
 assertFalse(new File(menu.m_GUI.strOutputDir).exists());
 }

 @Test
 public void testDVDMainMenu_TextFileLinesNotMatchingNumberOfVobs() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI();
 menu.init();
 String textFileContent = "Title 1\nTitle 2\nTitle 3\nTitle 4\nTitle 5\nTitle 6\nTitle 7\nTitle 8\nTitle 9\nTitle 10\nTitle 11\nTitle 12\nTitle 13";
 menu.m_GUI.txtTextFile = new File("test.txt");
 menu.m_GUI.txtTextFile.setContent(textFileContent);
 menu.DVDMainMenu();

 String menuOptions = menu.dvd_menu;
 assertTrue(menuOptions.contains("It appears that your text file is not\nformatted correctly.  You currently have\n12 vob files, \nhowever your text file is showing\n11 lines."));
 assertFalse(new File(menu.m_GUI.strOutputDir).exists());
 }
}