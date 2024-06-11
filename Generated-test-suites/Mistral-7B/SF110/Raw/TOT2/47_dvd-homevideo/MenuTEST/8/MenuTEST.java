import org.junit.Test;
import static org.junit.Assert.*;
import java.util.StringJoiner;
import java.io.File;
import java.io.IOException;

public class MenuTest {

 private Menu menu;

 @Test
 public void testMenuConstructor() {
 Menu menu = new Menu();
 assertNull(menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertNull(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testMenuWithTitleConstructor() {
 String title = "Test Menu";
 Menu menu = new Menu(title, "", "", "", false, null);
 assertEquals(title, menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testMenuWithTitlePicPathAudioPathConstructor() {
 String title = "Test Menu";
 String picPath = "test.jpg";
 String audioPath = "test.mp3";
 Menu menu = new Menu(title, picPath, audioPath, "", false, null);
 assertEquals(title, menu.strTitle);
 assertEquals(picPath, menu.strPicPath);
 assertEquals(audioPath, menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testMenuWithTitleTextFilePathConstructor() {
 String title = "Test Menu";
 String textFilePath = "test.txt";
 Menu menu = new Menu(title, "", "", textFilePath, false, null);
 assertEquals(title, menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertEquals(textFilePath, menu.strTextFilePath);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testMenuInit() throws InterruptedException {
 Menu menu = new Menu();
 menu.init();
 assertTrue(menu.m_Error || menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log"));
 }

 @Test
 public void testMenuDVDMainMenu() throws IOException {
 Menu menu = new Menu("Test Menu", "", "", "", false, null);
 menu.DVDMainMenu();
 assertNotNull(menu.video_files);
 assertTrue(menu.video_files.length > 0);
 }

 @Test(expected = FileNotFoundException.class)
 public void testMenuDVDMainMenuWithInvalidTextFile() {
 Menu menu = new Menu("Test Menu", "", "", "invalidTextFile.txt", false, null);
 menu.DVDMainMenu();
 }

 @Test
 public void testMenuCreateXML() {
 Menu menu = new Menu("Test Menu", "", "", "", false, null);
 menu.init();
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testMenuCreateXMLWithEmptyTitleFile() {
 Menu menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI.txtTextFile.setText("");
 menu.init();
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertFalse(xmlFile.exists());
 }

 @Test
 public void testMenuCreateXMLWithInvalidNumberOfTitles() {
 StringJoiner titlesJoiner = new StringJoiner("\n", "", "\n");
 titlesJoiner.add("Title 1");
 titlesJoiner.add("Title 2");
 titlesJoiner.add("Title 3");
 titlesJoiner.add("Title 4");
 titlesJoiner.add("Title 5");
 titlesJoiner.add("Title 6");
 titlesJoiner.add("Title 7");
 File textFile = new File("test.txt");
 try (PrintWriter writer = new PrintWriter(textFile)) {
 writer.println(titlesJoiner.toString());
 }
 Menu menu = new Menu("Test Menu", "", "", "test.txt", false, null);
 menu.init();
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertFalse(xmlFile.exists());
 textFile.delete();
 }

 @Test
 public void testMenuCreateXMLWithInvalidVobFile() {
 StringJoiner titlesJoiner = new StringJoiner("\n", "", "\n");
 titlesJoiner.add("Title 1");
 titlesJoiner.add("Title 2");
 titlesJoiner.add("Title 3");
 titlesJoiner.add("Title 4");
 titlesJoiner.add("Title 5");
 titlesJoiner.add("Title 6");
 titlesJoiner.add("Title 7");
 File textFile = new File("test.txt");
 try (PrintWriter writer = new PrintWriter(textFile)) {
 writer.println(titlesJoiner.toString());
 }
 Menu menu = new Menu("Test Menu", "", "", "test.txt", false, null);
 menu.init();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertFalse(xmlFile.exists());
 FileListFilter filter = new FileListFilter() {
 boolean accept(File dir, String name) {
 return name.endsWith("vob");
 }
 };
 FileList directory = new File(menu.m_GUI.strOutputDir).listFiles(filter);
 assertNotNull(directory);
 File vobFile = directory[0];
 File vobFileParent = vobFile.getParentFile();
 vobFile.delete();
 menu.createXML();
 assertTrue(xmlFile.exists());
 textFile.delete();
 vobFileParent.deleteRecursively();
 }
}