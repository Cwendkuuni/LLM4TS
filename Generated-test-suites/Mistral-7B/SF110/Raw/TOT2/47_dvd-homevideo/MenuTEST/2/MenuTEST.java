import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 gui = new GUI();
 }

 @Test
 public void testConstructor() {
 Menu menu = new Menu();
 assertEquals("dvd-menu", menu.dvd_menu);
 assertEquals(50, menu.titles.length);
 assertEquals("Menu Error - ", menu.baseErr);
 }

 @Test
 public void testMenuWithNoTextFile() {
 Menu menu = new Menu("Test Menu", "", "", "", false, gui);
 assertTrue(menu.init());
 assertEquals(0, menu.video_files.length);
 }

 @Test
 public void testMenuWithTextFile() throws IOException {
 String textFileContent = "Title 1\nTitle 2\nTitle 3\nBLANK\nDONE";
 File txtTextFile = new File("testTextFile.txt");
 txtTextFile.writeText(textFileContent);
 Menu menu = new Menu("Test Menu", "", "", "", false, gui, txtTextFile);
 assertTrue(menu.init());
 assertEquals(3, menu.video_files.length);
 Arrays.sort(menu.video_files);
 assertEquals("menu.vob", menu.video_files[0]);
 assertEquals("title1.vob", menu.video_files[1]);
 assertEquals("title2.vob", menu.video_files[2]);
 txtTextFile.delete();
 }

 @Test(expected = NullPointerException.class)
 public void testMenuWithNullTextFile() {
 Menu menu = new Menu("Test Menu", "", "", "", false, gui, null);
 menu.init();
 fail("Menu should not have been initialized with a null text file");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMenuWithMoreThanTenTitles() {
 String textFileContent = "Title 1\nTitle 2\nTitle 3\nTitle 4\nTitle 5\nTitle 6\nTitle 7\nTitle 8\nTitle 9\nTitle 10\nTitle 11\nTitle 12";
 File txtTextFile = new File("testTextFile.txt");
 txtTextFile.writeText(textFileContent);
 Menu menu = new Menu("Test Menu", "", "", "", false, gui, txtTextFile);
 menu.init();
 fail("Menu should not have been initialized with more than ten titles in the text file");
 txtTextFile.delete();
 }

 @Test
 public void testMenuWithInvalidVideoFileExtension() {
 Menu menu = new Menu("Test Menu", "", "", "", false, gui);
 File tmpFile = File.createTempFile("menu", ".txt");
 tmpFile.deleteOnExit();
 menu.video_files = new File(gui.strOutputDir).listFiles(new FilenameFilter() {
 @Override
 public boolean accept(File dir, String name) {
 return name.endsWith(".txt");
 }
 });
 menu.init();
 File tempFile = menu.video_files[0];
 tempFile.renameTo(new File(gui.strOutputDir, "menu.vob"));
 assertTrue(menu.init());
 assertFalse(Arrays.binarySearch(menu.video_files, tempFile) >= 0);
 tempFile.delete();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMenuWithMissingTitleInTextFile() throws IOException {
 String textFileContent = "Title 1\nTitle 2\nTitle 3\nBLANK\nDONE";
 File txtTextFile = new File("testTextFile.txt");
 txtTextFile.writeText(textFileContent);
 Menu menu = new Menu("Test Menu", "", "", "", false, gui, txtTextFile);
 menu.init();
 fail("Menu should not have been initialized with a missing title in the text file");
 txtTextFile.delete();
 }

 @Test
 public void testCreateXML() throws IOException {
 Menu menu = new Menu("Test Menu", "", "", "", false, gui);
 assertTrue(menu.init());
 menu.createXML();
 File xmlFile = new File(gui.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 xmlFile.delete();
 }

 @After
 public void tearDown() {
 menu = null;
 gui = null;
 }

}