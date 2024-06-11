import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

public class MenuTest {

 private Menu menu;
 private String title;
 private String textFilePath;
 private boolean isPAL;
 private GUI dvdGUI;

 @Before
 public void setUp() {
 title = "Sample Title";
 textFilePath = "sample.txt";
 isPAL = false;
 dvdGUI = new GUI();
 menu = new Menu(title, textFilePath, isPAL, dvdGUI);
 }

 @After
 public void tearDown() {
 menu = null;
 title = null;
 textFilePath = null;
 isPAL = false;
 dvdGUI = null;
 }

 @Test
 public void testMenuConstructor1() {
 Menu menu1 = new Menu();
 assertEquals("dvd-menu", menu1.dvd_menu);
 assertNull(menu1.strTitle);
 assertNull(menu1.strPicPath);
 assertNull(menu1.strAudioPath);
 assertNull(menu1.strTextFilePath);
 assertFalse(menu1.m_Error);
 assertNull(menu1.m_Thread);
 assertNull(menu1.titles);
 assertNull(menu1.baseErr);
 assertNull(menu1.video_files);
 assertNull(menu1.m_GUI);
 }

 @Test
 public void testMenuConstructor2() {
 menu = new Menu(title, textFilePath, isPAL, dvdGUI);
 assertEquals(title, menu.strTitle);
 assertEquals(textFilePath, menu.strTextFilePath);
 assertTrue(menu.pal_menu == isPAL);
 assertSame(dvdGUI, menu.m_GUI);
 assertFalse(menu.m_Error);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertNull(menu.video_files);
 assertNull(menu.m_Thread);
 }

 @Test
 public void testMenuConstructor3() {
 String picPath = "sample.jpg";
 String audioPath = "sample.mp3";
 menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, dvdGUI);
 assertEquals(title, menu.strTitle);
 assertEquals(textFilePath, menu.strTextFilePath);
 assertTrue(menu.pal_menu == isPAL);
 assertSame(dvdGUI, menu.m_GUI);
 assertEquals(picPath, menu.strPicPath);
 assertEquals(audioPath, menu.strAudioPath);
 assertFalse(menu.m_Error);
 assertNull(menu.dvd_menu);
 assertNull(menu.titles);
 assertNull(menu.video_files);
 assertNull(menu.m_Thread);
 }

 @Test
 public void testInit1() {
 menu = new Menu();
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testInit2() {
 menu = new Menu(title, textFilePath, isPAL, dvdGUI);
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit3() {
 String logFile = "log.txt";
 dvdGUI.setStrOutputDir("output");
 menu = new Menu(title, null, isPAL, dvdGUI);
 menu.init();
 File outputDir = new File("output");
 File logFileExpected = new File("output/log/dvd-menu.log");
 assertTrue(outputDir.exists());
 assertTrue(logFileExpected.exists());
 assertFalse(menu.m_Error);
 }

 @Test
 public void testDVDMainMenu1() {
 menu = new Menu();
 menu.init();
 menu.DVDMainMenu();
 assertNull(menu.video_files);
 }

 @Test
 public void testDVDMainMenu2() {
 menu = new Menu(title, textFilePath, isPAL, dvdGUI);
 menu.init();
 File listDir = new File("output");
 FilenameFilter filter = new FilenameFilter() {
 @Override public boolean accept(File dir, String name) {
 return name.endsWith("vob");
 }
 };
 File[] videoFilesExpected = { new File("output/menu.vob"), new File("output/Sample1.vob"), new File("output/Sample2.vob") };
 menu.DVDMainMenu();
 assertArrayEquals(videoFilesExpected, menu.video_files);
 }

 @Test
 public void testDVDMainMenuWithTextFile1() {
 menu = new Menu(title, textFilePath, isPAL, dvdGUI);
 menu.init();
 menu.DVDMainMenu();
 assertNull(menu.titles);
 }

 @Test
 public void testDVDMainMenuWithTextFile2() {
 menu = new Menu(title, textFilePath, isPAL, dvdGUI);
 menu.init();
 String[] titlesExpected = { "Title1", "Title2", "Title3", "Title4", "Title5" };
 try {
 BufferedReader reader = new BufferedReader(new FileReader("sample.txt"));
 String line;
 int i = 0;
 while ((line = reader.readLine()) != null) {
 if (i < titlesExpected.length) {
 titlesExpected[i] = line;
 }
 ++i;
 }
 reader.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 menu.DVDMainMenu();
 assertArrayEquals(titlesExpected, menu.titles);
 }

 @Test
 public void testCreateXML1() {
 menu = new Menu();
 menu.init();
 menu.createXML();
 File xmlFile = new File("output/vmgm.xml");
 assertFalse(xmlFile.exists());
 }

 @Test
 public void testCreateXML2() {
 menu = new Menu(title, textFilePath, isPAL, dvdGUI);
 menu.init();
 menu.createXML();
 File xmlFile = new File("output/vmgm.xml");
 assertTrue(xmlFile.exists());
 File xmlContentExpected = new File("expected_vmgm.xml");
 assertEquals(xmlContentExpected.getText(), xmlFile.getText());
 }
}