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

 @Test
 public void testConstructor() {
 Menu menu1 = new Menu();
 assertNotNull(menu1);
 Menu menu2 = new Menu("Test Menu", "", "", "", false, gui);
 assertNotNull(menu2);
 Menu menu3 = new Menu("Test Menu", "test.pic", "test.audio", "test.text", true, gui);
 assertNotNull(menu3);
 }

 @Test
 public void testInit() {
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenu() {
 menu = new Menu("Test Menu", "", "", "test.txt", false, gui);
 menu.init();
 menu.DVDMainMenu();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testCreateXML() {
 menu = new Menu("Test Menu", "", "", "test.txt", false, gui);
 menu.init();
 menu.createXML();
 File xmlFile = new File("test.xml");
 assertTrue(xmlFile.exists());
 xmlFile.delete();
 }
}