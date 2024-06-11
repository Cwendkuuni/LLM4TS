package com.example.menu;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.FilenameFilter;
import java.util.StringTokenizer;

public class MenuTest {

 private Menu menu;
 private GUI gui;

 @Before
 public void setup() {
 gui = new GUI();
 menu = new Menu("Test Menu", "", "", "", "", gui);
 }

 @Test
 public void testConstructor() {
 assertNotNull(menu);
 assertEquals("dvd-menu", menu.dvd_menu);
 assertEquals(50, menu.titles.length);
 assertNull(menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNull(menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testCreateMenuTitleConstructor() {
 menu = new Menu("Test Menu", "textFile.txt", false, gui);
 assertNotNull(menu);
 assertEquals("Test Menu", menu.strTitle);
 assertNull(menu.strPicPath);
 assertNull(menu.strAudioPath);
 assertNotNull(menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testCreateMenuTitlePicAudioTextConstructors() {
 menu = new Menu("Test Menu", "picPath.jpg", "audioPath.mp3", "textFile.txt", false, gui);
 assertNotNull(menu);
 assertEquals("Test Menu", menu.strTitle);
 assertEqual( "picPath.jpg", menu.strPicPath);
 assertEqual( "audioPath.mp3", menu.strAudioPath);
 assertNotNull(menu.strTextFilePath);
 assertFalse(menu.pal_menu);
 assertNull(menu.baseErr);
 assertNull(menu.video_files);
 assertNull(menu.m_GUI);
 assertNull(menu.m_Thread);
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInit() {
 menu.init();
 assertFalse(menu.m_Error);
 }

 @Test
 public void testInitWithError() {
 menu = new Menu("Test Menu", "", "", "", "", gui);
 menu.m_Error = true;
 menu.init();
 assertTrue(menu.m_Error);
 }

 @Test
 public void testDVDMainMenu() {
 menu.DVDMainMenu();
 File listDir = new File(gui.strOutputDir);
 FilenameFilter filter = new FilenameFilter() {
 boolean accept(File dir, String name) {
 return name.endsWith("vob");
 }
 };
 String[] videoFiles = listDir.listFiles(filter);
 assertNotNull(videoFiles);
 assertFalse(Arrays.stream(videoFiles).anyMatch((String vobFile) -> vobFile.equals("menu.vob")));
 }

 @Test
 public void testCreateXML() {
 menu.createXML();
 File xmlFile = new File(gui.strOutputDir + "/vmgm.xml");
 assertTrue(xmlFile.exists());
 }

 @Test
 public void testCreateXMLWithError() {
 menu.m_Error = true;
 menu.createXML();
 File xmlFile = new File(gui.strOutputDir + "/vmgm.xml");
 assertFalse(xmlFile.exists());
 }
}