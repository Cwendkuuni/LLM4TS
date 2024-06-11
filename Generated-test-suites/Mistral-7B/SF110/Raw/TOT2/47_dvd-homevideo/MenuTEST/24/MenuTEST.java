import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.File;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MenuTest {

 private Menu menu;
 private String title;
 private String textFilePath;
 private boolean isPAL;
 private GUI dvdGUI;

 @Before
 public void setup() {
 title = "sampleTitle";
 textFilePath = "sampleTextFilePath";
 isPAL = true;
 dvdGUI = mock(GUI.class);
 menu = new Menu(title, textFilePath, isPAL, dvdGUI);
 }

 @Test
 public void testConstructorWithTitleAndTextFilePath() {
 Menu menu = new Menu(title, textFilePath, false, dvdGUI);
 assertNotNull(menu.strTitle);
 assertNotNull(menu.strTextFilePath);
 }

 @Test
 public void testConstructorWithAllParameters() {
 String picPath = "samplePicPath";
 String audioPath = "sampleAudioPath";
 Menu menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, dvdGUI);
 assertNotNull(menu.strTitle);
 assertNotNull(menu.strPicPath);
 assertNotNull(menu.strAudioPath);
 assertNotNull(menu.strTextFilePath);
 }

 @Test
 public void testInitSuccess() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test(expected = InterruptedException.class)
 public void testInitInterruptedException() {
 menu = new Menu();
 menu.init();
 throw new InterruptedException();
 }

 @Test
 public void testInitErrorCheckLogFile() {
 when(dvdGUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log")).thenReturn(true);
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenuNoVobFiles() {
 File dir = new File(menu.m_GUI.strOutputDir);
 FilenameFilter filter = new FilenameFilter() {
 boolean accept(File dir, String name) {
 return name.endsWith("vob");
 }
 };
 File[] files = dir.listFiles(filter);
 assertEquals(0, files.length);
 menu.DVDMainMenu();
 }

 @Test
 public void testDVDMainMenuWithVobFiles() {
 File dir = new File(menu.m_GUI.strOutputDir);
 FilenameFilter filter = new FilenameFilter() {
 boolean accept(File dir, String name) {
 return name.endsWith("vob") && !name.equals("menu.vob");
 }
 };
 File[] files = dir.listFiles(filter);
 assertEquals(1, files.length);
 menu.DVDMainMenu();
 }

 @Test(expected = FileNotFoundException.class)
 public void testReadTextFileNotFound() {
 when(dvdGUI.txtTextFile.getText()).thenThrow(new FileNotFoundException());
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testCreateXML() {
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

}