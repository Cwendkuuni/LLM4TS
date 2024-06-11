import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

 private Menu menu;
 private GUI guiMock;

 @Before
 public void setUp() {
 guiMock = new GUIMock();
 menu = new Menu("Test Menu", "", "", "", true, guiMock);
 }

 @After
 public void tearDown() {
 menu = null;
 guiMock = null;
 }

 @Test
 public void testConstructor() {
 Menu noArgsMenu = new Menu();
 Menu titleMenu = new Menu("Title", "", "", "", false, guiMock);
 Menu titlePicAudioTextMenu = new Menu("Title", "picPath", "audioPath", "textFilePath", false, guiMock);
 Menu titlePicAudioTextPALMenu = new Menu("Title", "picPath", "audioPath", "textFilePath", true, guiMock);

 assertEquals("dvd-menu", noArgsMenu.dvd_menu);
 assertEquals(50, noArgsMenu.titles.length);
 assertEquals("Menu Error - ", noArgsMenu.baseErr);
 assertEquals("Title", titleMenu.strTitle);
 assertEquals(false, titleMenu.pal_menu);
 assertNull(titleMenu.strPicPath);
 assertNull(titleMenu.strAudioPath);
 assertNull(titleMenu.strTextFilePath);
 assertEquals("picPath", titlePicAudioTextMenu.strPicPath);
 assertEquals("audioPath", titlePicAudioTextMenu.strAudioPath);
 assertEquals("textFilePath", titlePicAudioTextMenu.strTextFilePath);
 assertEquals("Title", titlePicAudioTextMenu.strTitle);
 assertEquals(false, titlePicAudioTextMenu.pal_menu);
 assertEquals("picPath", titlePicAudioTextPALMenu.strPicPath);
 assertEquals("audioPath", titlePicAudioTextPALMenu.strAudioPath);
 assertEquals("textFilePath", titlePicAudioTextPALMenu.strTextFilePath);
 assertEquals("Title", titlePicAudioTextPALMenu.strTitle);
 assertEquals(true, titlePicAudioTextPALMenu.pal_menu);
 }

 @Test
 public void testInit() {
 Menu menu = new Menu("Test Menu", "", "", "", true, guiMock);
 menu.init();
 assertTrue(menu.m_Error || guiMock.errorCheckCalled);
 }

 @Test
 public void testDVDMainMenu() {
 Menu menu = new Menu("Test Menu", "", "", "", true, guiMock);
 menu.init();
 menu.DVDMainMenu();
 assertTrue(menu.m_GUI.lblAuthor.isEnabled());
 assertTrue(menu.m_GUI.lblAuthorProg.isEnabled());
 assertTrue(menu.m_GUI.prgAuthor.isEnabled());
 }

 @Test
 public void testCreateXML() {
 Menu menu = new Menu("Test Menu", "", "", "", true, guiMock);
 menu.init();
 menu.createXML();
 File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }
}

class GUIMock {
 boolean errorCheckCalled;
 String strOutputDir = "outputDir";
 TextField txtTextFile;
 String strTextFilePath;
 MessageBox messageBox;
 TextArea txtAreaOutput;

 public void errorCheckCalled() {
 this.errorCheckCalled = true;
 }
}