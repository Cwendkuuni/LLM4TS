import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.File;
import java.io.FileNotFoundException;
import static org.mockito.Mockito.*;

public class MenuTest {

 private Menu menu;
 private GUI guiMock;

 @Before
 public void setUp() {
 guiMock = mock(GUI.class);
 menu = new Menu("Title", "", "", "", false, guiMock);
 }

 @After
 public void tearDown() throws Exception {
 menu = null;
 guiMock = null;
 }

 @Test
 public void testInit_Success() throws InterruptedException {
 when(guiMock.ErrorCheck(anyString())).thenReturn(false);
 menu.init();
 verify(guiMock).ErrorCheck(eq(guiMock.strOutputDir + "/log/dvd-menu.log"));
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit_Error() throws InterruptedException {
 when(guiMock.ErrorCheck(anyString())).thenReturn(true);
 menu.init();
 verify(guiMock).ErrorCheck(eq(guiMock.strOutputDir + "/log/dvd-menu.log"));
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenu_Success() {
 menu = new Menu("Title", "", "", "", false, guiMock);
 menu.DVDMainMenu();
 assertNotNull(menu.video_files);
 assertTrue(menu.video_files.length > 0);
 }

 @Test(expected = FileNotFoundException.class)
 public void testDVDMainMenu_TextFileNotFound() {
 menu = new Menu("Title", "", "", "", false, guiMock);
 doThrow(FileNotFoundException.class).when(guiMock).txtTextFile;
 menu.DVDMainMenu();
 }

 @Test
 public void testCreateXML_Success() {
 menu = new Menu("Title", "", "", "", false, guiMock);
 menu.init();
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }
}