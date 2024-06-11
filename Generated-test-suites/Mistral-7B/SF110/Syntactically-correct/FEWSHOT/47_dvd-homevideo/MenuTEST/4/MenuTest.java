package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {

 Menu menu = new Menu();
 @Test
 public void testMenuConstruction() {
 Menu m = new Menu();
 assertNull(m.getStrTitle());
 assertNull(m.getStrPicPath());
 assertNull(m.getStrAudioPath());
 assertNull(m.getStrTextFilePath());
 assertNull(m.getDvd_menu());
 assertNull(m.getTitles());
 assertFalse(m.isPal_menu());
 assertNull(m.getBaseErr());
 assertNull(m.getVideo_files());
 assertNull(m.getM_GUI());
 assertNull(m.getM_Thread());
 assertFalse(m.isM_Error());
 }

 @Test
 public void testMenuConstructionWithTitle() {
 Menu m = new Menu("Test Title");
 assertEquals("Test Title", m.getStrTitle());
 assertNull(m.getStrPicPath());
 assertNull(m.getStrAudioPath());
 assertNull(m.getStrTextFilePath());
 assertNull(m.getDvd_menu());
 assertNull(m.getTitles());
 assertFalse(m.isPal_menu());
 assertNull(m.getBaseErr());
 assertNull(m.getVideo_files());
 assertNull(m.getM_GUI());
 assertNull(m.getM_Thread());
 assertFalse(m.isM_Error());
 }

 @Test
 public void testMenuConstructionWithTitlePicPath() {
 Menu m = new Menu("Test Title", "Test Pic Path");
 assertEquals("Test Title", m.getStrTitle());
 assertEquals("Test Pic Path", m.getStrPicPath());
 assertNull(m.getStrAudioPath());
 assertNull(m.getStrTextFilePath());
 assertNull(m.getDvd_menu());
 assertNull(m.getTitles());
 assertFalse(m.isPal_menu());
 assertNull(m.getBaseErr());
 assertNull(m.getVideo_files());
 assertNull(m.getM_GUI());
 assertNull(m.getM_Thread());
 assertFalse(m.isM_Error());
 }

 @Test
 public void testMenuConstructionWithTitlePicPathAudioPath() {
 Menu m = new Menu("Test Title", "Test Pic Path", "Test Audio Path");
 assertEquals("Test Title", m.getStrTitle());
 assertEquals("Test Pic Path", m.getStrPicPath());
 assertEquals("Test Audio Path", m.getStrAudioPath());
 assertNull(m.getStrTextFilePath());
 assertNull(m.getDvd_menu());
 assertNull(m.getTitles());
 assertFalse(m.isPal_menu());
 assertNull(m.getBaseErr());
 assertNull(m.getVideo_files());
 assertNull(m.getM_GUI());
 assertNull(m.getM_Thread());
 assertFalse(m.isM_Error());
 }

 @Test
 public void testMenuConstructionWithTitleTextFilePath() {
 Menu m = new Menu("Test Title", "Test Text File Path");
 assertEquals("Test Title", m.getStrTitle());
 assertNull(m.getStrPicPath());
 assertNull(m.getStrAudioPath());
 assertEquals("Test Text File Path", m.getStrTextFilePath());
 assertNull(m.getDvd_menu());
 assertNull(m.getTitles());
 assertFalse(m.isPal_menu());
 assertNull(m.getBaseErr());
 assertNull(m.getVideo_files());
 assertNull(m.getM_GUI());
 assertNull(m.getM_Thread());
 assertFalse(m.isM_Error());
 }

 @Test
 public void testMenuConstructionWithTitlePicPathAudioPathTextFilePath() {
 Menu m = new Menu("Test Title", "Test Pic Path", "Test Audio Path", "Test Text File Path");
 assertEquals("Test Title", m.getStrTitle());
 assertEquals("Test Pic Path", m.getStrPicPath());
 assertEquals("Test Audio Path", m.getStrAudioPath());
 assertEquals("Test Text File Path", m.getStrTextFilePath());
 assertNull(m.getDvd_menu());
 assertNull(m.getTitles());
 assertFalse(m.isPal_menu());
 assertNull(m.getBaseErr());
 assertNull(m.getVideo_files());
 assertNull(m.getM_GUI());
 assertNull(m.getM_Thread());
 assertFalse(m.isM_Error());
 }

 @Test
 public void testInit() {
 Menu m = new Menu();
 assertFalse(m.init());
 assertFalse(m.getM_GUI().ErrorCheck(m.getM_GUI().strOutputDir + "/log/dvd-menu.log"));
 assertTrue(m.isM_Error());
 }

 @Test
 public void testInitSuccess() {
 Menu m = new Menu("Test Title", "Test Text File Path");
 assertTrue(m.init());
 assertFalse(m.getM_GUI().ErrorCheck(m.getM_GUI().strOutputDir + "/log/dvd-menu.log"));
 assertFalse(m.isM_Error());
 }

 @Test
 public void testRun() {
 Menu m = new Menu();
 m.run();
 assertNull(m.getDvd_menu());
 }

 @Test
 public void testDVDMainMenu() {
 Menu m = new Menu();
 m.DVDMainMenu();
 assertNull(m.getDvd_menu());
 }

 @Test
 public void testCreateXML() {
 Menu m = new Menu();
 assertTrue(m.createXML());
 }
}