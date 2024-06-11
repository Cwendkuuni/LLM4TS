import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.StringTokenizer;

public class MenuTest {

 private Menu menu;
 private String[] videoFiles;
 private String baseOutputDir;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 baseOutputDir = "output_dir";
 }

 @Test
 public void testMenuConstruction() {
 Menu menu1 = new Menu();
 Menu menu2 = new Menu("Title", "TextFilePath", false, null);
 Menu menu3 = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", false, null);
 assertNotNull(menu1);
 assertNotNull(menu2);
 assertNotNull(menu3);
 }

 @Test
 public void testInit() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitWithError() {
 File errorLogFile = new File(baseOutputDir + "/log/dvd-menu.log");
 errorLogFile.createNewFile();
 Menu menuWithError = new Menu("Test Menu", "", "", "", false, null);
 menuWithError.init();
 assertTrue(menuWithError.m_Error);
 }

 @Test
 public void testDVDMainMenu() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.DVDMainMenu();
 File ListDir = new File(baseOutputDir);
 FilenameFilter filter = new FilenameFilter() {
 boolean accept(File dir, String name) {
 if (name.endsWith("vob")) {
 return true;
 }
 return false;
 }
 };
 videoFiles = ListDir.listFilenames(filter, null);
 assertNotNull(videoFiles);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testDVDMainMenuNoFiles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.DVDMainMenu();
 File ListDir = new File(baseOutputDir);
 FilenameFilter filter = new FilenameFilter() {
 boolean accept(File dir, String name) {
 if (name.endsWith("vob")) {
 return false;
 }
 return true;
 }
 };
 videoFiles = ListDir.listFilenames(filter, null);
 assertTrue(videoFiles.length == 0);
 assertTrue(menu.m_Error);
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.createXML();
 File xmlFile = new File(baseOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXMLWithError() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_Error = true;
 menu.createXML();
 File xmlFile = new File(baseOutputDir + "/vmgm.xml");
 assertFalse(xmlFile.exists());
 }

 @Test
 public void testTitleArrayParse() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.DVDMainMenu();
 String textFileContent = "Title1\nTitle2\nTitle3\nBLANK\nTitle4\nBLANK\nTitle5\nDONE";
 menu.setTxtTextFile(textFileContent);
 String[] expectedTitles = new String[]{"Title1", "Title2", "Title3", "Title4", "Title5"};
 assertArrayEquals(expectedTitles, menu.titles);
 }

 @Test
 public void testTitleArrayParseWithInvalidFormat() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.DVDMainMenu();
 String textFileContent = "Title1\nTitle2\nTitle3\nBLANK\nTitle4\nTitle5\nTitle6\nTitle7";
 menu.setTxtTextFile(textFileContent);
 String[] expectedTitles = new String[]{"Title1", "Title2", "Title3", "Title4", "Title5", "Title6"};
 assertArrayEquals(expectedTitles, menu.titles);
 assertTrue(menu.m_Error);
 }

 @Test
 public void testTitleArrayParseWithInvalidContent() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.DVDMainMenu();
 String textFileContent = "Title1\nTitle2\nTitle3\nInvalidTitle\nTitle4\nTitle5\nTitle6\nTitle7";
 menu.setTxtTextFile(textFileContent);
 String[] expectedTitles = new String[]{"Title1", "Title2", "Title3", "Title4", "Title5", "Title6"};
 assertArrayEquals(expectedTitles, menu.titles);
 assertTrue(menu.m_Error);
 }
}