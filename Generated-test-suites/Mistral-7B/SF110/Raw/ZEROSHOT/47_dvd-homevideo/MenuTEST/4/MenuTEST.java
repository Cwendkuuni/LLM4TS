import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MenuTest {

 private Menu menu;
 private String title;
 private String textFilePath;
 private boolean isPAL;
 private GUI gui;

 @Before
 public void setUp() {
 title = "Test Menu Title";
 textFilePath = "test/textFile.txt";
 isPAL = false;
 gui = mock(GUI.class);
 menu = new Menu(title, null, null, textFilePath, isPAL, gui);
 }

 @Test
 public void testConstructor1() {
 Menu menu1 = new Menu();
 Assert.assertEquals("dvd-menu", menu1.dvd_menu);
 Assert.assertNull(menu1.strTitle);
 Assert.assertNull(menu1.strPicPath);
 Assert.assertNull(menu1.strAudioPath);
 Assert.assertNull(menu1.strTextFilePath);
 Assert.assertNull(menu1.titles);
 Assert.assertFalse(menu1.pal_menu);
 Assert.assertEquals("Menu Error - ", menu1.baseErr);
 Assert.assertNull(menu1.video_files);
 Assert.assertNull(menu1.m_GUI);
 Assert.assertNull(menu1.m_Thread);
 Assert.assertFalse(menu1.m_Error);
 }

 @Test
 public void testConstructor2() {
 menu = new Menu(title, textFilePath, isPAL, gui);
 Assert.assertEquals(title, menu.strTitle);
 Assert.assertNull(menu.strPicPath);
 Assert.assertNull(menu.strAudioPath);
 Assert.assertEquals(textFilePath, menu.strTextFilePath);
 Assert.assertEquals(isPAL, menu.pal_menu);
 Assert.assertEquals("Menu Error - ", menu.baseErr);
 Assert.assertNull(menu.video_files);
 Assert.assertNotNull(menu.m_GUI);
 Assert.assertNull(menu.m_Thread);
 Assert.assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructor3() {
 title = "Test Menu Title with PicPath and AudioPath";
 String picPath = "test/picPath.jpg";
 String audioPath = "test/audioPath.mp3";
 menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, gui);
 Assert.assertEquals(title, menu.strTitle);
 Assert.assertEquals(picPath, menu.strPicPath);
 Assert.assertEquals(audioPath, menu.strAudioPath);
 Assert.assertEquals(textFilePath, menu.strTextFilePath);
 Assert.assertEquals(isPAL, menu.pal_menu);
 Assert.assertEquals("Menu Error - ", menu.baseErr);
 Assert.assertNull(menu.video_files);
 Assert.assertNotNull(menu.m_GUI);
 Assert.assertNull(menu.m_Thread);
 Assert.assertFalse(menu.m_Error);
 }

 @Test
 public void testInit() throws InterruptedException {
 when(gui.ErrorCheck(eq(gui.strOutputDir + "/log/dvd-menu.log"))).thenReturn(false);
 menu.init();
 Assert.assertFalse(menu.m_Error);
 }

 @Test
 public void testInitWithError() throws InterruptedException {
 when(gui.ErrorCheck(eq(gui.strOutputDir + "/log/dvd-menu.log"))).thenReturn(true);
 menu.init();
 Assert.assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenu() {
 //TODO: Implement this test
 }

 @Test
 public void testCreateXML() throws IOException {
 File xmlFile = new File("test/output/vmgm.xml");
 when(gui.strOutputDir).thenReturn("test/output");
 File ListDir = mock(File.class);
 FilenameFilter filter = mock(FilenameFilter.class);
 when(ListDir.listFiles(filter)).thenReturn(new File[]{"menu.vob", "test1.vob", "test2.vob"});
 when(ListDir.getName()).thenReturn("output");
 menu.m_GUI = gui;
 menu.m_GUI.strOutputDir = ListDir;
 menu.dvd_menu = "dvd-menu";
 menu.titles = new String[3];
 menu.titles[0] = "Title 1";
 menu.titles[1] = "Title 2";
 menu.titles[2] = null;
 menu.createXML();
 BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
 String line;
 String expectedXml = "<dvdauthor dest=\"output/DVD/\" jumppad=\"0\">\n" +
         "\t<vmgm>\n" +
         "\t<menus>\n" +
         "\t\t<pgc entry=\"title\" >\n" +
         "\t\t\t<vob file=\"output/menu.vob\" pause=\"inf\" />\n" +
         "\t\t\t<post> jump title 1; </post>\n" +
         "\t\t\t<post> jump title 2; </post>\n" +
         "\t\t\t<post> jump title 3; </post>\n" +
         "\t\t\t<post> call vmgm menu 1;</post>\n" +
         "\t\t</pgc>\n" +
         "\t</menus>\n" +
         "\t<titleset>\n" +
         "\t<titles>\n" +
         "\t\t<pgc>\n" +
         "\t\t\t<vob file=\"output/test1.vob\" />\n" +
         "\t\t\t<post>jump title 1;</post>\n" +
         "\t\t</pgc>\n" +
         "\t\t<pgc>\n" +
         "\t\t\t<vob file=\"output/test2.vob\" />\n" +
         "\t\t\t<post>jump title 2;</post>\n" +
         "\t\t</pgc>\n" +
         "\t\t<pgc>\n" +
         "\t\t\t<vob file=\"output/menu.vob\" />\n" +
         "\t\t\t<post>call vmgm menu 1;</post>\n" +
         "\t\t</pgc>\n" +
         "\t</titles>\n" +
         "\t</titleset>\n" +
         "</dvdauthor>";
 while ((line = reader.readLine()) != null) {
 Assert.assertEquals(expectedXml, line);
 }
 reader.close();
 xmlFile.delete();
 }
}