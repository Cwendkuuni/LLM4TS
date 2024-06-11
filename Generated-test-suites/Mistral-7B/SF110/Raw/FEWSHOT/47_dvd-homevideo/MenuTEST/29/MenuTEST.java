package org.apache.commons.cli.MenuTest;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Menu;

public class MenuTest {

    @Test
    public void testMenuConstructorNoArgs() {
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
    }

    @Test
    public void testMenuConstructorWithTitleArgs() {
        Menu menu = new Menu("Title", "", "", "", false, null);
        assertEquals("Title", menu.getStrTitle());
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
    }

    @Test
    public void testMenuConstructorWithTitlePicAudioTextArgs() {
        String title = "Title";
        String picPath = "picPath";
        String audioPath = "audioPath";
        String textFilePath = "textFilePath";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        assertEquals(title, menu.getStrTitle());
        assertEquals(picPath, menu.getStrPicPath());
        assertEquals(audioPath, menu.getStrAudioPath());
        assertEquals(textFilePath, menu.getStrTextFilePath());
        assertNull(menu.getDvd_menu());
        assertNull(menu.getTitles());
        assertFalse(menu.isPal_menu());
        assertNull(menu.getBaseErr());
        assertNull(menu.getVideo_files());
        assertNull(menu.getM_GUI());
        assertNull(menu.getM_Thread());
        assertFalse(menu.ism_Error());
    }

    @Test
    public void testInit() {
        String title = "Title";
        Menu menu = new Menu(title, "", "", "", false, null);
        assertFalse(menu.init());
    }

    @Test
    public void testRun() {
        String title = "Title";
        Menu menu = new Menu(title, "", "", "", false, null);
        menu.run();
    }

    @Test
    public void testDVDMainMenu() {
        String title = "Title";
        Menu menu = new Menu(title, "", "", "", false, null);
        menu.DVDMainMenu();
    }

    @Test
    public void testCreateXML() {
        String title = "Title";
        Menu menu = new Menu(title, "", "", "", false, null);
        assertFalse(menu.createXML());
    }
}