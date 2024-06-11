import org.junit.Test;
import static org.junit.Assert.*;
import java.util.StringJoiner;
import java.io.File;
import java.io.FileFilter;

public class MenuTest {

 private Menu menu;

 @Test
 public void testMenuConstructor() {
 menu = new Menu();
 assertNull(menu.getStrTitle());
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getm_GUI());
 assertNull(menu.getm_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructorWithTitle() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 assertEquals("Test Menu", menu.getStrTitle());
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getm_GUI());
 assertNull(menu.getm_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructorWithTitlePicAndAudio() {
 menu = new Menu("Test Menu", "test.jpg", "test.wav", "", false, null);
 assertEquals("Test Menu", menu.getStrTitle());
 assertEquals("test.jpg", menu.getStrPicPath());
 assertEquals("test.wav", menu.getStrAudioPath());
 assertNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getm_GUI());
 assertNull(menu.getm_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructorWithTitleTextFilePath() {
 menu = new Menu("Test Menu", "", "", "test.txt", false, null);
 assertEquals("Test Menu", menu.getStrTitle());
 assertNull(menu.getStrPicPath());
 assertNull(menu.getStrAudioPath());
 assertNotNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getm_GUI());
 assertNull(menu.getm_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testMenuConstructorWithTitlePicAudioAndTextFilePath() {
 menu = new Menu("Test Menu", "test.jpg", "test.wav", "test.txt", false, null);
 assertEquals("Test Menu", menu.getStrTitle());
 assertEquals("test.jpg", menu.getStrPicPath());
 assertEquals("test.wav", menu.getStrAudioPath());
 assertNotNull(menu.getStrTextFilePath());
 assertNull(menu.getDvd_menu());
 assertNull(menu.getTitles());
 assertFalse(menu.isPal_menu());
 assertNull(menu.getBaseErr());
 assertNull(menu.getVideo_files());
 assertNull(menu.getm_GUI());
 assertNull(menu.getm_Thread());
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testInitWithNoErrors() throws InterruptedException {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.init();
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testInitWithErrorsFromGUI() throws InterruptedException {
 menu = new Menu("Test Menu", "", "", "", false, new FakeGUI()) {
 @Override
 public boolean ErrorCheck(String path) {
 throw new RuntimeException("Test error from GUI");
 }
 };
 menu.init();
 assertTrue(menu.ism_Error());
 }

 @Test
 public void testDVDMainMenuWithNoFiles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.DVDMainMenu();
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testDVDMainMenuWithOneFile() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 final String vobFile = "test.vob";
 menu.video_files = new String[] { vobFile };
 menu.DVDMainMenu();
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testDVDMainMenuWithMoreThanOneFile() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 final String[] vobFiles = { "test1.vob", "test2.vob", "test3.vob" };
 menu.video_files = vobFiles;
 menu.DVDMainMenu();
 assertFalse(menu.ism_Error());
 }

 @Test
 public void testCreateXMLWithOneFile() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 final String vobFile = "test.vob";
 menu.video_files = new String[] { vobFile };
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertFalse(xmlFile.exists());
 }

 @Test
 public void testCreateXMLWithMultipleFiles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 final String[] vobFiles = { "test1.vob", "test2.vob", "test3.vob" };
 menu.video_files = vobFiles;
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateXMLWithNonExistentTextFile() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.strTextFilePath = "nonexistent.txt";
 menu.createXML();
 }

 @Test
 public void testCreateXMLWithInvalidNumberOfFiles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.strTextFilePath = "test.txt";
 final String[] vobFiles = { "test1.vob", "test2.vob" };
 menu.video_files = vobFiles;
 menu.createXML();
 final File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 FileFilter filter = new FileFilter() {
 @Override
 public boolean accept(File file) {
 return file.isFile() && file.getName().endsWith(".xml");
 }
 };
 File xml = new File(menu.m_GUI.strOutputDir, "DVD/vmgm.xml");
 assertFalse(xml.exists());
 }

 @Test
 public void testCreateXMLWithInvalidNumberOfTitles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.strTextFilePath = "test.txt";
 final String[] vobFiles = { "test1.vob", "test2.vob", "test3.vob", "test4.vob" };
 menu.video_files = vobFiles;
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 StringJoiner joiner = new StringJoiner("\n");
 joiner.add("<dvdauthor dest=\"");
 joiner.add(menu.m_GUI.strOutputDir + "/DVD/\" jumppad=\"0\">");
 joiner.add("\t<vmgm>");
 joiner.add("\t\t<menus>");
 joiner.add("\t\t\t<pgc entry=\"title\" >");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/menu.vob\" pause=\"inf\" />");
 joiner.add("\t\t\t\t<post> jump title 1; </post>");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t\t<pgc entry=\"title\" >");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/test1.vob\" pause=\"inf\" />");
 joiner.add("\t\t\t\t<post> jump title 2; </post>");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t\t<pgc entry=\"title\" >");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/test2.vob\" pause=\"inf\" />");
 joiner.add("\t\t\t\t<post> jump title 3; </post>");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t\t<pgc entry=\"title\" >");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/test3.vob\" pause=\"inf\" />");
 joiner.add("\t\t\t\t<post> jump title 4; </post>");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t</menus>");
 joiner.add("\t</vmgm>");
 joiner.add("\t<titleset>");
 joiner.add("\t\t<titles>");
 joiner.add("\t\t\t<pgc>");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/test1.vob\" />");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t</titles>");
 joiner.add("\t</titleset>");
 joiner.add("</dvdauthor>");
 String xmlText = joiner.toString();
 String xmlFromFile = new String(new File(xmlFile.getAbsolutePath()).getTextFilesContent("UTF-8"));
 assertEquals(xmlText, xmlFromFile);
 }

 @Test
 public void testCreateXMLWithInvalidNumberOfFilesAndTitles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.strTextFilePath = "test.txt";
 final String[] vobFiles = { "test1.vob", "test2.vob" };
 menu.video_files = vobFiles;
 menu.createXML();
 final File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 StringJoiner joiner = new StringJoiner("\n");
 joiner.add("<dvdauthor dest=\"");
 joiner.add(menu.m_GUI.strOutputDir + "/DVD/\" jumppad=\"0\">");
 joiner.add("\t<vmgm>");
 joiner.add("\t\t<menus>");
 joiner.add("\t\t\t<pgc entry=\"title\" >");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/menu.vob\" pause=\"inf\" />");
 joiner.add("\t\t\t\t<post> jump title 1; </post>");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t\t<pgc entry=\"title\" >");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/test1.vob\" pause=\"inf\" />");
 joiner.add("\t\t\t\t<post> jump title 2; </post>");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t\t<pgc entry=\"title\" >");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/test2.vob\" pause=\"inf\" />");
 joiner.add("\t\t\t\t<post> jump title 3; </post>");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t</menus>");
 joiner.add("\t</vmgm>");
 joiner.add("\t<titleset>");
 joiner.add("\t\t<titles>");
 joiner.add("\t\t\t<pgc>");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/test1.vob\" />");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t\t<pgc>");
 joiner.add("\t\t\t\t<vob file=\"");
 joiner.add(menu.m_GUI.strOutputDir);
 joiner.add("/test2.vob\" />");
 joiner.add("\t\t\t</pgc>");
 joiner.add("\t\t</titles>");
 joiner.add("\t</titleset>");
 joiner.add("</dvdauthor>");
 String xmlText = joiner.toString();
 String xmlFromFile = new String(new File(xmlFile.getAbsolutePath()).getTextFilesContent("UTF-8"));
 assertEquals(xmlText, xmlFromFile);
 }
}

class FakeGUI {
 public boolean ErrorCheck(String path) {
 return true;
 }
}