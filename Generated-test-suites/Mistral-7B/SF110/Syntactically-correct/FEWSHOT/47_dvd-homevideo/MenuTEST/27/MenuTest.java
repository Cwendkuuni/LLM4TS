package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuConstructor1() {
        Menu menu = new Menu();
        assertEquals("dvd-menu", menu.dvd_menu);
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertTrue(Arrays.equals(new String[0], menu.titles));
        assertFalse(menu.pal_menu);
        assertNull(menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuConstructor2() {
        String title = "Test Menu";
        Menu menu = new Menu(title, "", "", "", false, null);
        assertEquals(title, menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertTrue(Arrays.equals(new String[0], menu.titles));
        assertFalse(menu.pal_menu);
        assertNull(menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuConstructor3() {
        String title = "Test Menu";
        String picPath = "pic.jpg";
        String audioPath = "audio.mp3";
        String textFilePath = "text.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        assertEquals(title, menu.strTitle);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertTrue(Arrays.equals(new String[0], menu.titles));
        assertFalse(menu.pal_menu);
        assertNull(menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testInit() throws InterruptedException {
        String title = "Test Menu";
        String picPath = "pic.jpg";
        String audioPath = "audio.mp3";
        String textFilePath = "text.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        menu.init();
        assertTrue(menu.m_Error || menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log"));
    }

    @Test
    public void testRun() throws InterruptedException, IOException {
        String title = "Test Menu";
        String picPath = "pic.jpg";
        String audioPath = "audio.mp3";
        String textFilePath = "text.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        menu.init();
        menu.run();
        fail("Method run should throw an exception.");
    }

    @Test
    public void testDVDMainMenu() throws IOException {
        String title = "Test Menu";
        String picPath = "pic.jpg";
        String audioPath = "audio.mp3";
        String textFilePath = "text.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        menu.init();
        File ListDir = new File(menu.m_GUI.strOutputDir);
        FileFilter filter = new FilenameFilter() {
            boolean ret_val = true;

            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("vob")) {
                    if (!name.equals("menu.vob")) {
                        ret_val = true;
                    } else {
                        ret_val = false;
                    }
                } else {
                    ret_val = false;
                }
                return ret_val;
            }
        };
        String[] video_files = ListDir.listFiles(filter);
        assertEquals(1, video_files.length);
        assertEquals("menu.vob", video_files[0].getName());
    }

    @Test
    public void testCreateXML() throws IOException {
        String title = "Test Menu";
        String picPath = "pic.jpg";
        String audioPath = "audio.mp3";
        String textFilePath = "text.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        menu.init();
        menu.createXML();
        File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
        assertTrue(xmlFile.exists());
    }
}