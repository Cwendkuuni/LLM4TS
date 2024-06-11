import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setUp() {
 gui = new GUI();
 menu = new Menu("Test Menu", "", "", "", false, gui);
 }

 @After
 public void tearDown() {
 menu = null;
 gui = null;
 }

 @Test
 public void testConstructor() {
 Menu menu1 = new Menu();
 Menu menu2 = new Menu("Test Title", "", "", "", true, gui);
 Menu menu3 = new Menu("Test Title", "testPicPath", "testAudioPath", "testTextFilePath", true, gui);

 assertEquals("dvd-menu", menu1.dvd_menu);
 assertEquals("dvd-menu", menu2.dvd_menu);
 assertEquals("dvd-menu", menu3.dvd_menu);

 assertEquals(0, menu1.titles.length);
 assertEquals(50, menu2.titles.length);
 assertEquals(false, menu1.pal_menu);
 assertEquals(true, menu2.pal_menu);
 }

 @Test
 public void testInit() {
 boolean expectedResult = false;
 assertTrue(menu.init());
 assertEquals(expectedResult, menu.m_Error);
 }

 @Test(expected = InterruptedException.class)
 public void testInitInterrupted() throws InterruptedException {
 menu = new Menu("Test Menu", "", "", "", false, gui);
 Thread.sleep(100);
 menu.m_Thread.interrupt();
 menu.init();
 }

 @Test
 public void testCreateXML() {
 File xmlFile = new File("test.xml");
 boolean expectedResult = false;
 assertTrue(menu.createXML());
 assertEquals(expectedResult, xmlFile.exists());
 xmlFile.delete();
 }

 @Test(expected = IOException.class)
 public void testCreateXMLIOException() throws IOException {
 menu = new Menu("Test Menu", "", "", "", false, gui);
 menu.createXML();
 fail("createXML should throw an IOException");
 }
}