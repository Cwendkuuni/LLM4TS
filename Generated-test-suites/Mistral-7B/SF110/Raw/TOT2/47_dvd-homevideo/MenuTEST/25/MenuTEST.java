import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class MenuTest {

 private Menu menu;
 private String title;
 private String textFilePath;
 private boolean isPAL;
 private GUI gui;

 @Before
 public void setUp() {
 title = "Test Menu";
 textFilePath = "test.txt";
 isPAL = false;
 gui = new GUI();
 menu = new Menu(title, textFilePath, isPAL, gui);
 }

 @Test
 public void testConstructorWithNoArgs() {
 Menu emptyMenu = new Menu();
 assertEquals("dvd-menu", emptyMenu.dvd_menu);
 assertNull(emptyMenu.strTitle);
 assertNull(emptyMenu.strPicPath);
 assertNull(emptyMenu.strAudioPath);
 assertNull(emptyMenu.strTextFilePath);
 assertNull(emptyMenu.titles);
 assertNull(emptyMenu.m_GUI);
 assertNull(emptyMenu.m_Thread);
 assertFalse(emptyMenu.m_Error);
 }

 @Test
 public void testConstructorWithTitleAndTextFilePath() {
 Menu menuWithTitleAndTextFilePath = new Menu(title, textFilePath, isPAL, gui);
 assertEquals(title, menuWithTitleAndTextFilePath.strTitle);
 assertEquals(textFilePath, menuWithTitleAndTextFilePath.strTextFilePath);
 assertNull(menuWithTitleAndTextFilePath.strPicPath);
 assertNull(menuWithTitleAndTextFilePath.strAudioPath);
 assertNull(menuWithTitleAndTextFilePath.titles);
 assertNull(menuWithTitleAndTextFilePath.m_GUI);
 assertNull(menuWithTitleAndTextFilePath.m_Thread);
 assertFalse(menuWithTitleAndTextFilePath.m_Error);
 }

 @Test
 public void testConstructorWithTitlePicPathAudioPathTextFilePathAndIsPAL() {
 Menu menuWithAllArgs = new Menu(title, "testPicPath.jpg", "testAudioPath.mp3", textFilePath, isPAL, gui);
 assertEquals(title, menuWithAllArgs.strTitle);
 assertEquals("testPicPath.jpg", menuWithAllArgs.strPicPath);
 assertEquals("testAudioPath.mp3", menuWithAllArgs.strAudioPath);
 assertEquals(textFilePath, menuWithAllArgs.strTextFilePath);
 assertArrayEquals(new boolean[]{isPAL}, menuWithAllArgs.pal_menu);
 assertNull(menuWithAllArgs.titles);
 assertNull(menuWithAllArgs.m_GUI);
 assertNull(menuWithAllArgs.m_Thread);
 assertFalse(menuWithAllArgs.m_Error);
 }

 @Test
 public void testInitWithNoVobFiles() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitWithVobFiles() {
 File testDir = new File("testOutputDir");
 testDir.mkdirs();
 File menuVobFile = new File(testDir.getAbsolutePath() + "/testMenu.vob");
 menuVobFile.createNewFile();
 File[] vobFiles = {new File(testDir.getAbsolutePath() + "/test1.vob"), new File(testDir.getAbsolutePath() + "/test2.vob")};
 menu.video_files = vobFiles;
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test(expected = NoSuchElementException.class)
 public void testInitWithEmptyTextFile() {
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testInitWithTextFileWithMoreThanTenTitles() {
 File testTextFile = new File("testOutputDir/testTextFile.txt");
 try {
 new BufferedWriter(new FileWriter(testTextFile)).write("Title1\nTitle2\nTitle3\nTitle4\nTitle5\nTitle6\nTitle7\nTitle8\nTitle9\nTitle11\nTitle12");
 new BufferedReader(new FileReader(testTextFile)).close();
 menu.init();
 assertTrue(menu.m_Error);
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 testTextFile.delete();
 }
 }

 @Test
 public void testInitWithTextFileWithLessThanTenTitles() {
 File testTextFile = new File("testOutputDir/testTextFile.txt");
 try {
 new BufferedWriter(new FileWriter(testTextFile)).write("Title1\nTitle2\nTitle3\nTitle4\nTitle5");
 new BufferedReader(new FileReader(testTextFile)).close();
 menu.init();
 assertFalse(menu.m_Error);
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 testTextFile.delete();
 }
 }

 @Test
 public void testCreateXMLWithVobFiles() {
 File testDir = new File("testOutputDir");
 testDir.mkdirs();
 File menuVobFile = new File(testDir.getAbsolutePath() + "/menu.vob");
 menuVobFile.createNewFile();
 File[] vobFiles = {new File(testDir.getAbsolutePath() + "/test1.vob"), new File(testDir.getAbsolutePath() + "/test2.vob")};
 menu.video_files = vobFiles;
 menu.createXML();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testCreateXMLWithNoVobFiles() {
 menu.createXML();
 assertTrue(menu.m_Error);
 }

 @Test(expected = IOException.class)
 public void testCreateXMLWithIOException() {
 menu = new Menu();
 menu.createXML();
 assertTrue(menu.m_Error);
 }
}