import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;

 @Before
 public void setUp() {
 menu = new Menu("Test Title", "", "", "", false, null);
 }

 @Test
 public void testInit_noTitleTextFile_shouldReturnError() {
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testInit_validInput_shouldNotReturnError() {
 menu = new Menu("Test Title", "testTextFile.txt", false, null);
 assertFalse(menu.init());
 }

 @Test
 public void testInit_invalidTitle_shouldReturnError() {
 menu = new Menu("Test Title\0", "testTextFile.txt", false, null);
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testInit_invalidTextFile_shouldReturnError() {
 menu = new Menu("Test Title", "testTextFile", false, null);
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testInit_validTextFile_shouldNotReturnError() {
 menu = new Menu("Test Title", "testTextFile.txt", false, null);
 assertFalse(menu.init());
 }

 @Test
 public void testCreateXML_noVobFiles_shouldReturnError() {
 menu = new Menu("Test Title", "testTextFile.txt", false, null);
 menu.init();
 assertFalse(menu.createXML());
 }

 @Test
 public void testCreateXML_validInput_shouldCreateXMLFile() {
 menu = new Menu("Test Title", "testTextFile.txt", false, null);
 menu.init();
 assertTrue(menu.createXML());
 }

 @Test
 public void testCreateXML_missingMenuVob_shouldReturnError() {
 menu = new Menu("Test Title", "testTextFile.txt", false, null);
 menu.init();
 assertTrue(menu.createXML());
 menu.m_GUI = null;
 assertFalse(menu.createXML());
 }

 @Test
 public void testCreateXML_validInputWithVobFiles_shouldCreateXMLFile() {
 menu = new Menu("Test Title", "testTextFile.txt", false, null);
 menu.init();
 assertTrue(menu.createXML());
 }

 @Test(expected = NullPointerException.class)
 public void testInit_nullGUI_shouldThrowNullPointerException() {
 menu = new Menu("Test Title", "testTextFile.txt", false, null);
 menu.m_GUI = null;
 menu.init();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInit_palMenuAndPicPath_shouldThrowIllegalArgumentException() {
 menu = new Menu("Test Title", "", "testPicPath.jpg", "testTextFile.txt", true, null);
 menu.init();
 }
}