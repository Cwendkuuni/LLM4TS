import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

public class MenuTest {

 private Menu menu;
 private String title = "Test Menu";
 private String textFilePath = "testTextFile.txt";
 private boolean isPAL = false;
 private GUI gui;

 @Before
 public void setUp() {
 menu = new Menu(title, textFilePath, isPAL, gui);
 }

 @Test
 public void testConstructor() {
 Menu emptyMenu = new Menu();
 Menu oneArgMenu = new Menu(title);
 Menu twoArgMenu = new Menu(title, textFilePath);
 Menu threeArgMenu = new Menu(title, textFilePath, isPAL);

 assertEquals("dvd-menu", menu.dvd_menu);
 assertEquals(50, menu.titles.length);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertEquals("Menu Error - ", menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);

 assertEquals("dvd-menu", emptyMenu.dvd_menu);
 assertEquals(50, emptyMenu.titles.length);
 assertNull(emptyMenu.strTitle);
 assertNull(emptyMenu.strTextFilePath);
 assertFalse(emptyMenu.pal_menu);
 assertEquals("Menu Error - ", emptyMenu.baseErr);
 assertNull(emptyMenu.video_files);
 assertNull(emptyMenu.m_GUI);
 assertNull(emptyMenu.m_Thread);
 assertFalse(emptyMenu.m_Error);

 assertEquals("dvd-menu", oneArgMenu.dvd_menu);
 assertEquals(50, oneArgMenu.titles.length);
 assertEquals(title, oneArgMenu.strTitle);
 assertNull(oneArgMenu.strPicPath);
 assertNull(oneArgMenu.strTextFilePath);
 assertFalse(oneArgMenu.pal_menu);
 assertEquals("Menu Error - ", oneArgMenu.baseErr);
 assertNull(oneArgMenu.video_files);
 assertNull(oneArgMenu.m_GUI);
 assertNull(oneArgMenu.m_Thread);
 assertFalse(oneArgMenu.m_Error);

 assertEquals("dvd-menu", twoArgMenu.dvd_menu);
 assertEquals(50, twoArgMenu.titles.length);
 assertEquals(title, twoArgMenu.strTitle);
 assertNull(twoArgMenu.strPicPath);
 assertEquals(textFilePath, twoArgMenu.strTextFilePath);
 assertFalse(twoArgMenu.pal_menu);
 assertEquals("Menu Error - ", twoArgMenu.baseErr);
 assertNull(twoArgMenu.video_files);
 assertNull(twoArgMenu.m_GUI);
 assertNull(twoArgMenu.m_Thread);
 assertFalse(twoArgMenu.m_Error);

 assertEquals("dvd-menu", threeArgMenu.dvd_menu);
 assertEquals(50, threeArgMenu.titles.length);
 assertEquals(title, threeArgMenu.strTitle);
 assertNull(threeArgMenu.strPicPath);
 assertNull(threeArgMenu.strTextFilePath);
 assertFalse(threeArgMenu.pal_menu);
 assertEquals("Menu Error - ", threeArgMenu.baseErr);
 assertNull(threeArgMenu.video_files);
 assertNull(threeArgMenu.m_GUI);
 assertNull(threeArgMenu.m_Thread);
 assertFalse(threeArgMenu.m_Error);
 }

 @Test
 public void testInit() {
 menu.init();
 assertTrue(menu.m_Error || menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log"));
 }

 @Test
 public void testDVDMainMenu() throws IOException {
 menu = new Menu(title, textFilePath, isPAL, gui);
 menu.init();
 File ListDir = new File(menu.m_GUI.strOutputDir);
 FilenameFilter filter = new FilenameFilter() {
 boolean ret_val = true;

 @Override
 public boolean accept(final File dir, final String name) {
 if (name.endsWith("vob")) {
 if (!name.equals("menu.vob")) {
 this.ret_val = true;
 }
 else {
 this.ret_val = false;
 }
 }
 else {
 this.ret_val = false;
 }
 return this.ret_val;
 }
 };
 menu.video_files = ListDir.listFiles(filter);

 assertNotNull(menu.video_files);
 assertFalse(menu.m_Error);
 assertFalse(menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log"));
 }

 @Test(expected = IOException.class)
 public void testDVDMainMenuWithIOException() throws IOException {
 menu = new Menu(title, textFilePath, isPAL, gui);
 menu.DVDMainMenu();
 fail("Expected IOException was not thrown.");
 }

 @Test
 public void testCreateXML() {
 menu = new Menu(title, textFilePath, isPAL, gui);
 menu.init();
 assertTrue(menu.createXML());
 }

 @Test(expected = IOException.class)
 public void testCreateXMLWithIOException() {
 menu = new Menu(title, textFilePath, isPAL, gui);
 menu.createXML();
 fail("Expected IOException was not thrown.");
 }
}