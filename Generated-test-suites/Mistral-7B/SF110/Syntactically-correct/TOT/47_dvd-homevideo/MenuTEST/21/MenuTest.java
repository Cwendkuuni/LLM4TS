import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.StringJoiner;
import java.util.Arrays;

public class MenuTest {

 private Menu menu;
 private String expectedOutput;

 @Before
 public void setUp() {
 menu = new Menu("Test Menu", "testPicPath", "testAudioPath", "testTextFilePath.txt", false, null);
 }

 @Test
 public void testInit() throws InterruptedException {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullTitle() {
 new Menu(null, "testTextFilePath.txt", false, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullTextFilePath() {
 new Menu("Test Menu", null, false, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullGUI() {
 new Menu("Test Menu", "testTextFilePath.txt", false, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullTextFile() {
 new Menu("Test Menu", "testPicPath", "testAudioPath", null, false, null);
 }

 @Test
 public void testDVDMainMenu() throws IOException {
 menu = new Menu("Test Menu", "testPicPath", "testAudioPath", "testTextFilePath.txt", false, null);
 menu.init();
 StringJoiner joiner = new StringJoiner("\n");
 String menuOutput = menu.dvd_menu;
 String[] expectedLines = {"dvd-menu", "-c", "-D", "-n", "Test Menu", "-o", menu.m_GUI.strOutputDir, "-a", "testAudioPath"};
 Arrays.stream(expectedLines).forEach(line -> joiner.add(line));
 expectedOutput = joiner.toString();
 assertEquals(expectedOutput, menuOutput);
 }

 @Test
 public void testCreateXML() throws IOException {
 menu = new Menu("Test Menu", "testPicPath", "testAudioPath", "testTextFilePath.txt", false, null);
 menu.init();
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 String expectedXML = "<dvdauthor dest=\"testOutputDir/DVD/\" jumppad=\"0\">\n<vmgm>\n<menus>\n<pgc entry=\"title\" >\n<vob file=\"testOutputDir/menu.vob\" pause=\"inf\" />\n<button>jump title 1;</button>\n<button>jump title 2;</button>\n<button>jump title 3;</button>\n<button>jump title 4;</button>\n<button>jump title 5;</button>\n<post> jump vmgm menu 1; </post>\n</pgc>\n</menus>\n</vmgm>\n<titleset>\n<titles>\n<pgc>\n<vob file=\"testOutputDir/test1.vob\" />\n<post>jump title 1;</post>\n</pgc>\n<pgc>\n<vob file=\"testOutputDir/test2.vob\" />\n<post>jump title 2;</post>\n</pgc>\n<pgc>\n<vob file=\"testOutputDir/test3.vob\" />\n<post>jump title 3;</post>\n</pgc>\n<pgc>\n<vob file=\"testOutputDir/test4.vob\" />\n<post>jump title 4;</post>\n</pgc>\n<pgc>\n<vob file=\"testOutputDir/test5.vob\" />\n<post>call vmgm menu 1;</post>\n</pgc>\n</titles>\n</titleset>\n</dvdauthor>\n";
 File expectedXmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertEquals(expectedXML, FileUtils.readFileToString(xmlFile, "UTF-8"));
 }

 @Test(expected = IOException.class)
 public void testCreateXMLWithNullTextFile() throws IOException {
 menu = new Menu("Test Menu", "testPicPath", "testAudioPath", null, false, null);
 menu.init();
 menu.createXML();
 }

 @Test(expected = IOException.class)
 public void testCreateXMLWithEmptyTextFile() throws IOException {
 String emptyTextFilePath = "testTextFilePathEmpty.txt";
 menu = new Menu("Test Menu", "testPicPath", "testAudioPath", emptyTextFilePath, false, null);
 menu.init();
 menu.createXML();
 }

 @Test
 public void testCreateXMLWithInvalidTextFileFormat() throws IOException {
 String invalidTextFilePath = "testTextFilePathInvalid.txt";
 menu = new Menu("Test Menu", "testPicPath", "testAudioPath", invalidTextFilePath, false, null);
 menu.init();
 menu.createXML();
 }
}