import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Menu Title", "", "", "", false, null);
 }

 @Test
 public void testInit_NoFiles() {
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testInit_ValidFiles() {
 menu = new Menu("Menu Title", "", "", "", false, null);
 menu.strTextFilePath = "testTextFile.txt";
 File testDir = new File("testOutputDir");
 testDir.mkdir();
 File[] testFiles = new File[5];
 for (int i = 0; i < testFiles.length; i++) {
 testFiles[i] = new File(testDir.getAbsolutePath() + "/testFile" + i + ".vob");
 testFiles[i].createNewFile();
 }
 menu.video_files = testFiles;
 menu.m_GUI = new GUI();
 assertTrue(menu.init());
 assertFalse(menu.m_GUI.ErrorCheck(testDir.getAbsolutePath() + "/log/dvd-menu.log"));
 }

 @Test
 public void testInit_InvalidFiles() {
 menu = new Menu("Menu Title", "", "", "", false, null);
 menu.strTextFilePath = "testTextFile.txt";
 File testDir = new File("testOutputDir");
 testDir.mkdir();
 File[] testFiles = new File[5];
 for (int i = 0; i < testFiles.length; i++) {
 testFiles[i] = new File(testDir.getAbsolutePath() + "/testFile" + i + ".vob");
 testFiles[i].createNewFile();
 testFiles[1] = new File(testDir.getAbsolutePath() + "/invalidFile.txt");
 }
 menu.video_files = testFiles;
 menu.m_GUI = new GUI();
 assertFalse(menu.init());
 }

 @Test
 public void testInit_InvalidTextFile() {
 menu = new Menu("Menu Title", "", "", "", false, null);
 menu.strTextFilePath = "testTextFile.txt";
 File testDir = new File("testOutputDir");
 testDir.mkdir();
 File[] testFiles = new File[5];
 for (int i = 0; i < testFiles.length; i++) {
 testFiles[i] = new File(testDir.getAbsolutePath() + "/testFile" + i + ".vob");
 testFiles[i].createNewFile();
 }
 menu.video_files = testFiles;
 menu.m_GUI = new GUI();
 menu.titles = new String[5];
 menu.titles[0] = "BLANK";
 menu.titles[1] = "DONE";
 menu.titles[2] = "Invalid Title";
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testCreateXML_ValidFiles() {
 menu = new Menu("Menu Title", "", "", "", false, null);
 menu.strTextFilePath = "testTextFile.xml";
 File testDir = new File("testOutputDir");
 testDir.mkdir();
 File[] testFiles = new File[5];
 for (int i = 0; i < testFiles.length; i++) {
 testFiles[i] = new File(testDir.getAbsolutePath() + "/testFile" + i + ".vob");
 testFiles[i].createNewFile();
 }
 menu.video_files = testFiles;
 menu.m_GUI = new GUI();
 menu.init();
 menu.createXML();
 File xmlFile = new File(testDir.getAbsolutePath() + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXML_InvalidFiles() {
 menu = new Menu("Menu Title", "", "", "", false, null);
 menu.strTextFilePath = "testTextFile.xml";
 File testDir = new File("testOutputDir");
 testDir.mkdir();
 File[] testFiles = new File[5];
 for (int i = 0; i < testFiles.length; i++) {
 testFiles[i] = new File(testDir.getAbsolutePath() + "/testFile" + i + ".vob");
 testFiles[i].createNewFile();
 }
 File invalidFile = new File(testDir.getAbsolutePath() + "/invalidFile.txt");
 menu.video_files = testFiles;
 menu.m_GUI = new GUI();
 menu.init();
 menu.createXML();
 assertFalse(new File(testDir.getAbsolutePath() + "/vmgm.xml").exists());
 }

 @After
 public void tearDown() {
 if (menu != null) {
 menu = null;
 }
 File testDir = new File("testOutputDir");
 testDir.deleteRecursively();
 }
}