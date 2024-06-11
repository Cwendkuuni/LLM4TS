package org.apache.commons.cli.menu;

import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenu() {
        Menu menu = new Menu();
        assertNull(menu.getStrTitle());
        assertNull(menu.getStrPicPath());
        assertNull(menu.getStrAudioPath());
        assertNull(menu.getStrTextFilePath());
        assertNull(menu.getDvd_menu());
        assertNull(menu.getTitles());
        assertFalse(menu.isPal_menu());
        assertNull(menu.getBaseErr());
        assertNull(menu.getVideo_files());
        assertNull(menu.getM_GUI());
        assertNull(menu.getM_Thread());
        assertFalse(menu.isM_Error());

        Menu menu1 = new Menu("Title1", "", "", "", true, null);
        assertEquals("Title1", menu1.getStrTitle());
        assertNull(menu1.getStrPicPath());
        assertNull(menu1.getStrAudioPath());
        assertNull(menu1.getStrTextFilePath());
        assertEquals("dvd-menu", menu1.getDvd_menu());
        assertNull(menu1.getTitles());
        assertTrue(menu1.isPal_menu());
        assertNull(menu1.getBaseErr());
        assertNull(menu1.getVideo_files());
        assertNull(menu1.getM_GUI());
        assertNull(menu1.getM_Thread());
        assertFalse(menu1.isM_Error());

        Menu menu2 = new Menu("Title2", "picPath", "audioPath", "textFilePath", true, null);
        assertEquals("Title2", menu2.getStrTitle());
        assertEquals("picPath", menu2.getStrPicPath());
        assertEquals("audioPath", menu2.getStrAudioPath());
        assertEquals("textFilePath", menu2.getStrTextFilePath());
        assertEquals("dvd-menu", menu2.getDvd_menu());
        assertNull(menu2.getTitles());
        assertTrue(menu2.isPal_menu());
        assertNull(menu2.getBaseErr());
        assertNull(menu2.getVideo_files());
        assertNull(menu2.getM_GUI());
        assertNull(menu2.getM_Thread());
        assertFalse(menu2.isM_Error());
    }

    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertFalse(menu.init());
        Menu menu1 = new Menu("Title1", "", "", "", true, null);
        assertFalse(menu1.init());
        Menu menu2 = new Menu("Title2", "picPath", "audioPath", "textFilePath", true, null);
        assertTrue(menu2.init());
    }

    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        menu.DVDMainMenu();
        Menu menu1 = new Menu("Title1", "", "", "", true, null);
        menu1.DVDMainMenu();
        Menu menu2 = new Menu("Title2", "picPath", "audioPath", "textFilePath", true, null);
        menu2.DVDMainMenu();
    }

    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());
        Menu menu1 = new Menu("Title1", "", "", "", true, null);
        assertFalse(menu1.createXML());
        Menu menu2 = new Menu("Title2", "picPath", "audioPath", "textFilePath", true, null);
        assertTrue(menu2.createXML());
    }
}