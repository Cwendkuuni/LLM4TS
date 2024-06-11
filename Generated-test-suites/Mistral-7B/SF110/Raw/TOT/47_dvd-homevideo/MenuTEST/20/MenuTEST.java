import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 }

 @Test
 public void testInit() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitWithError() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_Thread = new Thread(()-> {throw new RuntimeException("Thread Exception");});
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenuWithNoFiles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.DVDMainMenu();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenuWithValidFiles() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.strTextFilePath = "testTextFile.txt";
 File testFile = new File("testTextFile.txt");
 testFile.setContentText("Title 1\nTitle 2\nBLANK\nTitle 3\nDONE");
 menu.m_GUI = new GUI();
 menu.DVDMainMenu();
 String expectedOutput = "dvd-menu: creating authoring project\ndvd-menu: creating vmgm file\ndvd-menu: creating title set\ndvd-menu: creating title 1\ndvd-menu: creating button for title 1\ndvd-menu: creating title 2\ndvd-menu: creating button for title 2\ndvd-menu: creating title 3\ndvd-menu: creating button for title 3\ndvd-menu: creating title 4\ndvd-menu: creating button for title 4\n";
 assertEquals(expectedOutput, menu.m_GUI.txtAreaOutput.getText());
 }

 @Test
 public void testDVDMainMenuWithInvalidTextFile() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.strTextFilePath = "testTextFile.txt";
 menu.m_GUI = new GUI();
 menu.DVDMainMenu();
 String expectedOutput = "Menu Error - IO Error\n";
 assertEquals(expectedOutput.substring(0, expectedOutput.length() - 1), menu.m_GUI.txtAreaOutput.getText().substring(0, expectedOutput.length() - 1));
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Menu", "", "", "", false, null);
 menu.m_GUI = new GUI();
 menu.init();
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 String expectedXML = "<dvdauthor dest=\"" + menu.m_GUI.strOutputDir + "/DVD/\"" +
 " jumppad=\"0\">\n" +
 "\t<vmgm>\n" +
 "\t\t<menus>\n" +
 "\t\t\t<pgc entry=\"title\" >\n" +
 "\t\t\t\t<vob file=\"" + menu.m_GUI.strOutputDir + "/menu.vob\" pause=\"inf\" />\n" +
 "\t\t\t\t<post> jump vmgm menu 1; </post>\n" +
 "\t\t\t</pgc>\n" +
 "\t\t\t<titles>\n" +
 "\t\t\t\t<pgc>\n" +
 "\t\t\t\t\t<vob file=\"" + menu.m_GUI.strOutputDir + "/test1.vob\" />\n" +
 "\t\t\t\t\t<post>jump title 2;</post>\n" +
 "\t\t\t\t</pgc>\n" +
 "\t\t\t\t<pgc>\n" +
 "\t\t\t\t\t<vob file=\"" + menu.m_GUI.strOutputDir + "/test2.vob\" />\n" +
 "\t\t\t\t\t<post>call vmgm menu 1;</post>\n" +
 "\t\t\t\t</pgc>\n" +
 "\t\t\t</titles>\n" +
 "\t</vmgm>\n" +
 "\t<titleset>\n" +
 "\t<titles>\n" +
 "\t\t<pgc>\n" +
 "\t\t\t<vob file=\"" + menu.m_GUI.strOutputDir + "/test1.vob\" />\n" +
 "\t\t\t<post>jump title 1;</post>\n" +
 "\t\t</pgc>\n" +
 "\t\t<pgc>\n" +
 "\t\t\t<vob file=\"" + menu.m_GUI.strOutputDir + "/test2.vob\" />\n" +
 "\t\t\t<post>call vmgm menu 1;</post>\n" +
 "\t\t</pgc>\n" +
 "\t</titles>\n" +
 "\t</titleset>\n" +
 "</dvdauthor>\n";
 try {
 File expectedFile = new File(xmlFile.getAbsolutePath());
 String actualOutput = FileUtils.readFileToString(expectedFile, "UTF-8");
 assertEquals(expectedXML, actualOutput);
 }
 catch (IOException e) {
 fail("Failed to read expected XML file.");
 }
 }

}