import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;
 private GUI guiMock;

 @Before
 public void setup() {
 guiMock = mock(GUI.class);
 menu = new Menu("Test Menu", "", "", "", true, guiMock);
 }

 @Test
 public void testConstructorWithTitleAndTextFilePathAndIsPAL() {
 Menu menuWithTextFilePath = new Menu("Test Menu", "testTextFile.txt", true, guiMock);
 assertNotNull(menuWithTextFilePath);
 }

 @Test
 public void testConstructorWithTitlePicPathAudioPathTextFilePathIsPALAndGUI() {
 Menu menuWithAllParams = new Menu("Test Menu", "testPicPath.jpg", "testAudioPath.mp3", "testTextFile.txt", true, guiMock);
 assertNotNull(menuWithAllParams);
 }

 @Test
 public void testInitReturnsFalseWhenNoErrors() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitReturnsTrueWhenErrorOccurs() {
 menu.m_GUI = mock(GUI.class);
 when(menu.m_GUI.ErrorCheck(anyString())).thenReturn(true);
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenuCreatesMenuOptionsCorrectlyWhenTextFileIsEmpty() {
 menu = new Menu("Test Menu", "", "", "", true, guiMock);
 menu.init();
 List<String> menuOptions = Arrays.asList(menu.dvd_menu.split("\\s+"));
 assertEquals(15, menuOptions.size());
 }

 @Test
 public void testDVDMainMenuCreatesMenuOptionsCorrectlyWhenTextFileIsNotEmpty() {
 String testTextFile = "Title 1\nTitle 2\nTitle 3\nTitle 4\nTitle 5\nBLANK\nDONE";
 menu = new Menu("Test Menu", "testTextFile.txt", true, guiMock);
 menu.init();
 List<String> menuOptions = Arrays.asList(menu.dvd_menu.split("\\s+"));
 assertEquals(11, menuOptions.size());
 for (int i = 0; i < 10; i++) {
 String expectedTitle = "Title " + (i + 1);
 String actualTitle = menuOptions.get(i);
 assertEquals(expectedTitle, actualTitle);
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testDVDMainMenuThrowsFileNotFoundExceptionWhenTextFileIsNotPresent() {
 menu = new Menu("Test Menu", "nonExistentTextFile.txt", true, guiMock);
 menu.init();
 fail("Expected FileNotFoundException, but no exception was thrown");
 }

 @Test(expected = IOException.class)
 public void testDVDMainMenuThrowsIOExceptionWhenTextFileReadingFails() {
 menu = new Menu("Test Menu", "testTextFile.txt", true, guiMock);
 when(menu.m_GUI.txtTextFile.getText()).thenThrow(new IOException());
 menu.init();
 fail("Expected IOException, but no exception was thrown");
 }

 @Test
 public void testCreateXMLCreatesValidVmgmXmlFile() {
 menu = new Menu("Test Menu", "", "", "", true, guiMock);
 menu.init();
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

}