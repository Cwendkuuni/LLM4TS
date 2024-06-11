import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setUp() {
 gui = new GUI();
 menu = new Menu("Test Menu", "", "", "", true, gui);
 }

 @After
 public void tearDown() {
 menu = null;
 gui = null;
 }

 @Test
 public void testConstructor1() {
 menu = new Menu();
 assertFalse(menu.isError());
 }

 @Test
 public void testConstructor2() {
 menu = new Menu("Test Title", "", "", "", true, gui);
 String expectedTitle = "Test Title";
 assertEquals(expectedTitle, menu.getTitle());
 }

 @Test
 public void testConstructor3() {
 menu = new Menu("Test Title", "test.pic", "test.audio", "", true, gui);
 String expectedPicPath = "test.pic";
 String expectedAudioPath = "test.audio";
 assertEquals(expectedPicPath, menu.getPicPath());
 assertEquals(expectedAudioPath, menu.getAudioPath());
 }

 @Test
 public void testInitSuccess() throws InterruptedException {
 menu.init();
 assertFalse(menu.isError());
 }

 @Test(expected = InterruptedException.class)
 public void testInitInterruptedException() throws InterruptedException {
 menu.init();
 Thread.currentThread().interrupt();
 menu.init();
 }

 @Test
 public void testInitError() throws InterruptedException {
 menu = new Menu("Test Menu", "", "", "", true, gui);
 menu.init();
 menu.m_GUI.setErrorCheckResult(true);
 menu.init();
 assertTrue(menu.isError());
 }

 @Test
 public void testDVDMainMenu() {
 menu = new Menu("Test Menu", "", "", "", true, gui);
 menu.init();
 menu.DVDMainMenu();
 assertFalse(menu.isError());
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Menu", "", "", "", true, gui);
 menu.init();
 menu.createXML();
 assertFalse(menu.isError());
 }
}