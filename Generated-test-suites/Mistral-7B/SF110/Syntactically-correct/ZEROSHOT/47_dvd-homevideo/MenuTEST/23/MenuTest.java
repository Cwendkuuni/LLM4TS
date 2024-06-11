import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setUp() {
 gui = new GUI();
 menu = new Menu("Test Title", "", "", "", false, gui);
 }

 @Test
 public void testConstructor1() {
 Menu menu = new Menu();
 assertEquals("dvd-menu", menu.dvd_menu);
 assertNull(menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.titles);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructor2() {
 String title = "Test Title";
 Menu menu = new Menu(title, "", "", "", false, gui);
 assertEquals(title, menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.titles);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertSame(gui, menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testConstructor3() {
 String title = "Test Title";
 String picPath = "test.jpg";
 String audioPath = "test.wav";
 Menu menu = new Menu(title, picPath, audioPath, "", false, gui);
 assertEquals(title, menu.strTitle);
 assertEquals(picPath, menu.strPicPath);
 assertEquals(audioPath, menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertNull(menu.titles);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertSame(gui, menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitFalse() throws InterruptedException {
 menu = new Menu("Test Title", "", "", "", false, gui);
 assertFalse(menu.init());
 }

 @Test
 public void testInitTrue() throws InterruptedException {
 menu = new Menu("Test Title", "", "", "", true, gui);
 assertTrue(menu.init());
 }

 @Test
 public void testDVDMainMenu() {
 menu = new Menu("Test Title", "", "", "", false, gui);
 menu.DVDMainMenu();
 fail("DVDMainMenu should not be called directly");
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Title", "", "", "", false, gui);
 assertTrue(menu.createXML());
 }
}