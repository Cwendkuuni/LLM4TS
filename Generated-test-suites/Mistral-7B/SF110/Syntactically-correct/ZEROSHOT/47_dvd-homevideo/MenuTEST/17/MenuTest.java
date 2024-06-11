package com.example.menu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setUp() {
 gui = new GUI();
 menu = new Menu("Test Title", "", "", "", false, gui);
 }

 @Test
 public void testConstructorWithNoArguments() {
 Menu menuWithNoArgs = new Menu();
 assertEquals("dvd-menu", menuWithNoArgs.dvd_menu);
 assertNull(menuWithNoArgs.strTitle);
 assertNull(menuWithNoArgs.strPicPath);
 assertNull(menuWithNoArgs.strAudioPath);
 assertNull(menuWithNoArgs.strTextFilePath);
 assertNull(menuWithNoArgs.titles);
 assertFalse(menuWithNoArgs.pal_menu);
 assertNull(menuWithNoArgs.baseErr);
 assertNull(menuWithNoArgs.video_files);
 assertNull(menuWithNoArgs.m_GUI);
 assertNull(menuWithNoArgs.m_Thread);
 assertFalse(menuWithNoArgs.m_Error);
 }

 @Test
 public void testConstructorWithTitleAndTextFilePath() {
 Menu menuWithTitleAndTextFilePath = new Menu("Test Title", "test.txt", false, gui);
 assertEquals("Test Title", menuWithTitleAndTextFilePath.strTitle);
 assertEquals("test.txt", menuWithTitleAndTextFilePath.strTextFilePath);
 assertNull(menuWithTitleAndTextFilePath.strPicPath);
 assertNull(menuWithTitleAndTextFilePath.strAudioPath);
 assertNull(menuWithTitleAndTextFilePath.titles);
 assertFalse(menuWithTitleAndTextFilePath.pal_menu);
 assertNull(menuWithTitleAndTextFilePath.baseErr);
 assertNull(menuWithTitleAndTextFilePath.video_files);
 assertSame(gui, menuWithTitleAndTextFilePath.m_GUI);
 assertNull(menuWithTitleAndTextFilePath.m_Thread);
 assertFalse(menuWithTitleAndTextFilePath.m_Error);
 }

 @Test
 public void testConstructorWithTitlePicPathAudioPathTextFilePathAndPal() {
 Menu menuWithTitlePicPathAudioPathTextFilePathAndPal = new Menu("Test Title", "test.jpg", "test.wav", "test.txt", false, gui);
 assertEquals("Test Title", menuWithTitlePicPathAudioPathTextFilePathAndPal.strTitle);
 assertEquals("test.jpg", menuWithTitlePicPathAudioPathTextFilePathAndPal.strPicPath);
 assertEquals("test.wav", menuWithTitlePicPathAudioPathTextFilePathAndPal.strAudioPath);
 assertEquals("test.txt", menuWithTitlePicPathAudioPathTextFilePathAndPal.strTextFilePath);
 assertNull(menuWithTitlePicPathAudioPathTextFilePathAndPal.titles);
 assertFalse(menuWithTitlePicPathAudioPathTextFilePathAndPal.pal_menu);
 assertNull(menuWithTitlePicPathAudioPathTextFilePathAndPal.baseErr);
 assertNull(menuWithTitlePicPathAudioPathTextFilePathAndPal.video_files);
 assertSame(gui, menuWithTitlePicPathAudioPathTextFilePathAndPal.m_GUI);
 assertNull(menuWithTitlePicPathAudioPathTextFilePathAndPal.m_Thread);
 assertFalse(menuWithTitlePicPathAudioPathTextFilePathAndPal.m_Error);
 }

 @Test
 public void testInitMethod() throws InterruptedException, IOException {
 menu = new Menu("Test Title", "", "", "", false, gui);
 menu.init();
 assertTrue(menu.init());
 File logFile = new File(gui.strOutputDir + "/log/dvd-menu.log");
 assertTrue(logFile.exists());
 logFile.delete();
 }

 @Test(expected = NullPointerException.class)
 public void testInitMethodWithNullGUI() {
 menu = new Menu("Test Title", "", "", "", false, null);
 menu.init();
 }

 @Test
 public void testDVDMainMenuMethod() throws IOException {
 menu = new Menu("Test Title", "", "", "", false, gui);
 menu.DVDMainMenu();
 File ListDir = new File(gui.strOutputDir);
 FilenameFilter filter = new FilenameFilter() {
 boolean accept(File dir, String name) {
 if (name.endsWith("vob")) {return true;}
 return false;
 }
 };
 String[] video_files = ListDir.list(filter);
 assertEquals(1, video_files.length);
 assertEquals("menu.vob", video_files[0]);
 }

 @Test
 public void testCreateXMLMethod() throws IOException {
 menu = new Menu("Test Title", "", "", "", false, gui);
 menu.createXML();
 File xmlFile = new File(gui.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 xmlFile.delete();
 }

 @After
 public void tearDown() {
 gui = null;
 menu = null;
 }

}