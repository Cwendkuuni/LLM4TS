package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuConstructor() {
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
        assertFalse(menu.ism_Error());

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
        assertFalse(menu1.ism_Error());

        Menu menu2 = new Menu("Title2", "picPath1", "audioPath1", "TextFilePath1.txt", true, null);
        assertEquals("Title2", menu1.getStrTitle());
        assertEquals("picPath1", menu1.getStrPicPath());
        assertEquals("audioPath1", menu1.getStrAudioPath());
        assertEquals("TextFilePath1.txt", menu1.getStrTextFilePath());
        assertEquals("dvd-menu", menu1.getDvd_menu());
        assertNull(menu1.getTitles());
        assertTrue(menu1.isPal_menu());
        assertNull(menu1.getBaseErr());
        assertNull(menu1.getVideo_files());
        assertNull(menu1.getM_GUI());
        assertNull(menu1.getM_Thread());
        assertFalse(menu1.ism_Error());
    }

    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertFalse(menu.init());
        assertTrue(menu.ism_Error());

        Menu menu1 = new Menu("Title1", "", "", "", true, null);
        assertFalse(menu1.init());
        assertTrue(menu1.ism_Error());

        Menu menu2 = new Menu("Title2", "picPath1", "audioPath1", "TextFilePath1.txt", true, null);
        assertTrue(menu2.init());
        assertFalse(menu2.ism_Error());
    }

    @Test
    public void testDVDMainMenu() {
        Menu menu = spy(new Menu());
        when(menu.getVideo_files()).thenReturn(new String[0]);

        menu.DVDMainMenu();

        verify(menu, times(1)).init();
    }

    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());

        Menu menu1 = new Menu("Title1", "", "", "", true, null);
        assertFalse(menu1.createXML());

        Menu menu2 = new Menu("Title2", "picPath1", "audioPath1", "TextFilePath1.txt", true, null);
        assertFalse(menu2.createXML());
    }
}