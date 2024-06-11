package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuConstructor1() {
        Menu menu = new Menu();
        assertEquals("dvd-menu", menu.dvd_menu);
        assertArrayEquals(new String[50], menu.titles);
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
    public void testMenuConstructor2() {
        String title = "Test Title";
        Menu menu = new Menu(title, "", "", "", false, null);
        assertEquals(title, menu.strTitle);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertArrayEquals(new String[50], menu.titles);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertEquals("", menu.strTextFilePath);
        assertFalse(menu.pal_menu);
        assertNull(menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuConstructor3() {
        String title = "Test Title";
        String picPath = "Test Pic Path";
        String audioPath = "Test Audio Path";
        String textFilePath = "Test Text File Path";
        boolean isPAL = false;
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, null);
        assertEquals(title, menu.strTitle);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertArrayEquals(new String[50], menu.titles);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertEquals(isPAL, menu.pal_menu);
        assertNull(menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testInitFail() {
        Menu menu = new Menu();
        menu.m_Error = true;
        assertFalse(menu.init());
    }

    @Test
    public void testInitSuccess() throws InterruptedException {
        Menu menu = new Menu();
        assertTrue(menu.init());
    }

    @Test
    public void testDVDMainMenu() {
        String baseOutputDir = "Test Output Dir";
        Menu menu = new Menu();
        menu.m_GUI = new GUI();
        menu.m_GUI.strOutputDir = baseOutputDir;
        Menu menuSpy = new SpyMenu(menu);
        menu.m_GUI = menuSpy;
        menu.DVDMainMenu();

        File listDir = new File(baseOutputDir);
        FileFilter filter = new FilenameFilter() {
            boolean accept(File dir, String name) {
                if (name.endsWith("vob")) {
                    return true;
                }
                return false;
            }
        };
        String[] videoFiles = listDir.listFiles(filter);
        assertEquals(1, videoFiles.length);
        assertEquals("menu.vob", videoFiles[0].getName());

        assertEquals(1, menuSpy.numCalls);
        assertEquals("dvd-menu", menuSpy.lastCallArgs[0]);
        assertEquals("-c", menuSpy.lastCallArgs[1]);
        assertEquals("-D", menuSpy.lastCallArgs[2]);
        assertEquals("-n", menuSpy.lastCallArgs[3]);
        assertEquals(baseOutputDir, menuSpy.lastCallArgs[4]);
        assertNull(menuSpy.lastCallArgs[5]);
        assertNull(menuSpy.lastCallArgs[6]);
        assertNull(menuSpy.lastCallArgs[7]);
    }

    @Test
    public void testCreateXML() {
        String baseOutputDir = "Test Output Dir";
        Menu menu = new Menu();
        menu.m_GUI = new GUI();
        menu.m_GUI.strOutputDir = baseOutputDir;
        menu.createXML();

        File xmlFile = new File(Paths.get(baseOutputDir, "vmgm.xml").toFile());
        assertTrue(xmlFile.exists());
    }
}

class SpyMenu implements GUI {
    Menu menu;
    int numCalls = 0;
    String[] lastCallArgs;

    SpyMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void MessageBox(String message, int type) {
        ++numCalls;
        lastCallArgs = new String[menu.m_Thread.getArgs().length];
        System.arraycopy(menu.m_Thread.getArgs(), 0, lastCallArgs, 0, menu.m_Thread.getArgs().length);
    }
}