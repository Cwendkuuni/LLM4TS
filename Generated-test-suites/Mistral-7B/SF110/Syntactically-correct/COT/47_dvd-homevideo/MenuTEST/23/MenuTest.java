import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.lang.reflect.Method;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setup() {
 menu = new Menu();
 gui = new GUI();
 }

 @Test
 public void testConstructorNoArgs() {
 Menu testMenu = new Menu();
 assertNull(testMenu.getStrTitle());
 assertNull(testMenu.getStrPicPath());
 assertNull(testMenu.getStrAudioPath());
 assertNull(testMenu.getStrTextFilePath());
 assertNull(testMenu.getDvd_menu());
 assertNull(testMenu.getTitles());
 assertFalse(testMenu.isPal_menu());
 assertNull(testMenu.getBaseErr());
 assertNull(testMenu.getVideo_files());
 assertNull(testMenu.getm_GUI());
 assertNull(testMenu.getm_Thread());
 assertFalse(testMenu.ism_Error());
 }

 @Test
 public void testConstructorWithTitleTextFilePathIsPALAndGUI() throws Exception {
 Method constructorMethod = Menu.class.getDeclaredMethod("Menu", new Class[] { String.class, String.class, boolean.class, GUI.class });
 constructorMethod.setAccessible(true);
 Object[] constructorArgs = { "Test Title", "TestTextFilePath.txt", true, gui };
 Menu testMenu = (Menu) constructorMethod.invoke(menu, constructorArgs);
 assertEquals("Test Title", testMenu.getStrTitle());
 assertEquals("TestTextFilePath.txt", testMenu.getStrTextFilePath());
 assertTrue(testMenu.isPal_menu());
 assertNotNull(testMenu.getm_GUI());
 assertNull(testMenu.getStrPicPath());
 assertNull(testMenu.getStrAudioPath());
 assertFalse(testMenu.isError());
 }

 @Test
 public void testConstructorWithTitlePicPathAudioPathTextFilePathIsPALAndGUI() throws Exception {
 Method constructorMethod = Menu.class.getDeclaredMethod("Menu", new Class[] { String.class, String.class, String.class, String.class, boolean.class, GUI.class });
 constructorMethod.setAccessible(true);
 Object[] constructorArgs = { "Test Title", "TestPicPath.jpg", "TestAudioPath.mp3", "TestTextFilePath.txt", true, gui };
 Menu testMenu = (Menu) constructorMethod.invoke(menu, constructorArgs);
 assertEquals("Test Title", testMenu.getStrTitle());
 assertEquals("TestPicPath.jpg", testMenu.getStrPicPath());
 assertEquals("TestAudioPath.mp3", testMenu.getStrAudioPath());
 assertEquals("TestTextFilePath.txt", testMenu.getStrTextFilePath());
 assertTrue(testMenu.isPal_menu());
 assertNotNull(testMenu.getm_GUI());
 assertFalse(testMenu.isError());
 }

 @Test
 public void testInit() throws InterruptedException, Exception {
 menu.setStrTitle("Test Title");
 menu.setStrTextFilePath("TestTextFilePath.txt");
 menu.setPal_menu(true);
 menu.setm_GUI(gui);
 assertTrue(menu.init());
 assertFalse(gui.ErrorCheck(new File("log/dvd-menu.log").getAbsolutePath()));
 }

 @Test(expected = InterruptedException.class)
 public void testInitInterrupted() throws InterruptedException {
 menu.setStrTitle("Test Title");
 menu.setStrTextFilePath("TestTextFilePath.txt");
 menu.setPal_menu(true);
 menu.setm_GUI(gui);
 Thread.currentThread().interrupt();
 menu.init();
 }

 @Test
 public void testDVDMainMenuSuccess() throws IOException {
 menu.setStrTitle("Test Title");
 menu.setStrTextFilePath("TestTextFilePath.txt");
 menu.setPal_menu(true);
 menu.setm_GUI(gui);
 File testDir = new File("TestOutputDir");
 testDir.mkdir();
 String[] testVideoFiles = { "TestVob1.vob", "TestVob2.vob", "TestVob3.vob" };
 File listDir = new File(testDir.getAbsolutePath());
 listDir.mkdirs();
 FilenameFilter filter = new FilenameFilter() {
 boolean accept(File dir, String name) {
 if (name.endsWith("vob")) {
 if (!name.equals("menu.vob")) {
 return true;
 }
 else {
 return false;
 }
 }
 else {
 return false;
 }
 }
 };
 menu.setVideo_files(testVideoFiles);
 menu.setm_GUI(gui);
 menu.DVDMainMenu();
 String menuOptions = menu.getDvd_menu();
 assertEquals("/bin/sh -c " + menuOptions, "dvd-menu -c -D -n Test Title " + testDir.getAbsolutePath() + " -a null -b null 2>&1");
 }

 @Test(expected = IOException.class)
 public void testDVDMainMenuIOError() throws IOException, InterruptedException {
 menu.setStrTitle("Test Title");
 menu.setStrTextFilePath("TestTextFilePath.txt");
 menu.setPal_menu(true);
 menu.setm_GUI(gui);
 menu.DVDMainMenu();
 String menuOptions = menu.getDvd_menu();
 File testDir = new File("TestOutputDir");
 testDir.mkdir();
 testDir.delete();
 menu.setVideo_files(new String[] { "TestVob1.vob" });
 menu.setm_GUI(gui);
 menu.DVDMainMenu();
 fail("IOException expected but not thrown");
 }

 @Test
 public void testCreateXMLSuccess() throws IOException {
 menu.setStrTitle("Test Title");
 menu.setStrTextFilePath("TestTextFilePath.txt");
 menu.setPal_menu(true);
 menu.setm_GUI(gui);
 menu.setVideo_files(new String[] { "TestVob1.vob", "TestVob2.vob" });
 String xmlString = menu.createXML();
 assertTrue(xmlString.contains("<dvdauthor dest=\"TestOutputDir/DVD/\">"));
 assertTrue(xmlString.contains("<pgc entry=\"title\" >"));
 assertTrue(xmlString.contains("<titleset>"));
 assertTrue(xmlString.contains("<titles>"));
 }

 @Test(expected = IOException.class)
 public void testCreateXMLIOError() throws IOException, InterruptedException {
 menu.setStrTitle("Test Title");
 menu.setStrTextFilePath("TestTextFilePath.txt");
 menu.setPal_menu(true);
 menu.setm_GUI(gui);
 menu.createXML();
 fail("IOException expected but not thrown");
 }

}