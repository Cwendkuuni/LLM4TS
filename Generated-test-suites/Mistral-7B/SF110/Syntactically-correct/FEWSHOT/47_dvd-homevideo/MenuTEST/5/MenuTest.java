package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {

 Menu menu = new Menu(); // default constructor
 Menu menuWithTitleAndTextFilePath = new Menu("Title", "/path/to/textfile.txt", true, null);
 Menu menuWithTitlePicPathAndAudioPath = new Menu("Title", "/path/to/pic.jpg", "/path/to/audio.mp3", "/path/to/textfile.txt", true, null);

 @Test
 public void testMenuInit() {
 Menu menu = new Menu();
 assertFalse(menu.init());
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);

 Menu menuWithTitleAndTextFilePath = new Menu("Title", "/path/to/textfile.txt", true, null);
 assertTrue(menuWithTitleAndTextFilePath.init());
 assertNotNull(menuWithTitleAndTextFilePath.m_GUI);
 assertNotNull(menuWithTitleAndTextFilePath.m_Thread);
 assertFalse(menuWithTitleAndTextFilePath.m_Error);
 }

 @Test
 public void testMenuDVDMainMenu() {
 Menu menu = new Menu();
 menu.DVDMainMenu();
 assertNull(menu.dvd_menu);

 Menu menuWithTitleAndTextFilePath = new Menu("Title", "/path/to/textfile.txt", true, null);
 menuWithTitleAndTextFilePath.DVDMainMenu();
 assertNotNull(menuWithTitleAndTextFilePath.dvd_menu);
 }

 @Test
 public void testMenuCreateXML() {
 Menu menuWithTitlePicPathAndAudioPath = new Menu("Title", "/path/to/pic.jpg", "/path/to/audio.mp3", "/path/to/textfile.txt", true, null);
 assertTrue(menuWithTitlePicPathAndAudioPath.createXML());

 File xmlFile = new File(menuWithTitlePicPathAndAudioPath.m_GUI.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 xmlFile.delete();
 }

 @Test(expected = NullPointerException.class)
 public void testMenuInitNullTextFilePath() {
 Menu menuWithNullTextFilePath = new Menu("Title", null, true, null);
 menuWithNullTextFilePath.init();
 fail("Should throw NullPointerException");
 }
}