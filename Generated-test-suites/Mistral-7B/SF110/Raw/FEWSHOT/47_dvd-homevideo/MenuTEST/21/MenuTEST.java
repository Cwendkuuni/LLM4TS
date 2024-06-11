package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class MenuTest {

    private Menu menu;
    private String title;
    private String textFilePath;
    private boolean isPAL;
    private GUI gui;

    @Before
    public void setUp() {
        title = "Example Title";
        textFilePath = "example.txt";
        isPAL = true;
        gui = new GUI();
        menu = new Menu();
    }

    @Test
    public void testMenu() {
        assertEquals("dvd-menu", menu.dvd_menu);
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertEquals(0, menu.titles.length);
        assertFalse(menu.pal_menu);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuWithTitle() {
        menu = new Menu(title, textFilePath, isPAL, gui);
        assertEquals(title, menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertEquals(isPAL, menu.pal_menu);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuWithTitlePicAndAudio() {
        String picPath = "example.jpg";
        String audioPath = "example.mp3";
        menu = new Menu(title, textFilePath, isPAL, picPath, audioPath, gui);
        assertEquals(title, menu.strTitle);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertEquals(isPAL, menu.pal_menu);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testInit() {
        menu = new Menu(title, textFilePath, isPAL, gui);
        assertFalse(menu.init());
        assertTrue(gui.ErrorCheck(gui.strOutputDir + "/log/dvd-menu.log").isDisplayed());
        assertTrue(menu.m_Error);
    }

    @Test
    public void testDVDMainMenu() {
        menu = new Menu(title, textFilePath, isPAL, gui);
        menu.DVDMainMenu();
        assertNull(menu.video_files);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testDVDMainMenuWithVOBFiles() {
        File dir = new File("testVOBFiles");
        File vobFile1 = new File(dir, "file1.vob");
        File vobFile2 = new File(dir, "file2.vob");
        File[] vobFiles = {vobFile1, vobFile2};
        menu = new Menu(title, textFilePath, isPAL, gui);
        menu.m_GUI = new GUI() {{
            strOutputDir = dir.getAbsolutePath();
        }};
        menu.DVDMainMenu();
        assertArrayEquals(vobFiles, menu.video_files);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testCreateXML() {
        File xmlFile = new File("example.xml");
        menu = new Menu(title, textFilePath, isPAL, gui);
        menu.createXML();
        assertTrue(xmlFile.exists());
        xmlFile.delete();
        assertFalse(menu.m_Error);
    }

    @After
    public void tearDown() {
        if (menu != null) {
            menu = null;
        }
        if (gui != null) {
            gui = null;
        }
    }

}