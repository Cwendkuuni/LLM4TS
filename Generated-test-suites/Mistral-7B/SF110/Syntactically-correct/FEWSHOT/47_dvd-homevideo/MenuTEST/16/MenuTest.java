package org.apache.commons.cli.menu;

import static org.junit.Assert.*;

import java.io.File;
import java.util.FilenameFilter;

import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuConstructors() {
        Menu menu = new Menu();
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertNull(menu.dvd_menu);
        assertEquals(false, menu.m_Error);

        Menu menu1 = new Menu("Title", "", "", "", false, null);
        assertEquals("Title", menu1.strTitle);
        assertNull(menu1.strPicPath);
        assertNull(menu1.strAudioPath);
        assertNull(menu1.strTextFilePath);
        assertNull(menu1.dvd_menu);
        assertEquals(false, menu1.m_Error);

        Menu menu2 = new Menu("Title", "pic.jpg", "audio.mp3", "text.txt", false, null);
        assertEquals("Title", menu2.strTitle);
        assertEquals("pic.jpg", menu2.strPicPath);
        assertEquals("audio.mp3", menu2.strAudioPath);
        assertEquals("text.txt", menu2.strTextFilePath);
        assertNull(menu2.dvd_menu);
        assertEquals(false, menu2.m_Error);
    }

    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertFalse(menu.init());
        assertTrue(menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log").length() > 0);

        Menu menu1 = new Menu("Title", "", "", "", false, null);
        assertTrue(menu1.init());
        assertFalse(menu1.m_GUI.ErrorCheck(menu1.m_GUI.strOutputDir + "/log/dvd-menu.log").length() > 0);

        Menu menu2 = new Menu("Title", "pic.jpg", "audio.mp3", "text.txt", false, null);
        assertTrue(menu2.init());
        assertFalse(menu2.m_GUI.ErrorCheck(menu2.m_GUI.strOutputDir + "/log/dvd-menu.log").length() > 0);
    }

    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());

        Menu menu1 = new Menu("Title", "", "", "", false, null);
        assertFalse(menu1.createXML());

        Menu menu2 = new Menu("Title", "pic.jpg", "audio.mp3", "text.txt", false, null);
        File xmlFile = new File(menu2.m_GUI.strOutputDir + "/vmgm.xml");
        assertTrue(menu2.createXML());
        assertTrue(xmlFile.exists());
    }

    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        assertNull(menu.dvd_menu);

        Menu menu1 = new Menu("Title", "", "", "", false, null);
        String dvdMenuOptions = "--title=\"Title\" ";
        File ListDir = new File(".");
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("vob")) {
                    if (!name.equals("menu.vob")) {
                        dvdMenuOptions += "-f " + dir.getAbsolutePath() + "/" + name.substring(0, 11) + ".vob ";
                    }
                }
                return true;
            }
        };
        File[] files = ListDir.listFiles(filter);
        if (files != null && files.length > 0) {
            dvdMenuOptions += "-t \"Title_1\" ";
            for (int i = 1; i < files.length; ++i) {
                dvdMenuOptions += "-f " + files[i].getAbsolutePath().substring(0, files[i].getName().lastIndexOf(".vob")) + "/" + files[i].getName().substring(0, 11) + ".vob ";
                if (i < files.length - 1) {
                    dvdMenuOptions += "-t \"Title_" + (i + 1) + "\" ";
                }
            }
        }
        menu1.DVDMainMenu();
        assertEquals(dvdMenuOptions, menu1.dvd_menu);

        Menu menu2 = new Menu("Title", "pic.jpg", "audio.mp3", "text.txt", false, null);
        dvdMenuOptions = "--title=\"Title\" -a audio.mp3 ";
        if (new File("pic.jpg").exists()) {
            dvdMenuOptions += "-b pic.jpg ";
        }
        ListDir = new File(menu2.m_GUI.strOutputDir);
        filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("vob")) {
                    if (!name.equals("menu.vob")) {
                        dvdMenuOptions += "-f " + dir.getAbsolutePath() + "/" + name.substring(0, 11) + ".vob ";
                    }
                }
                return true;
            }
        };
        files = ListDir.listFiles(filter);
        if (files != null && files.length > 0) {
            dvdMenuOptions += "-t \"Title_1\" ";
            for (int i = 1; i < files.length; ++i) {
                dvdMenuOptions += "-t \"Title_" + (i + 1) + "\" -f " + files[i].getAbsolutePath().substring(0, files[i].getName().lastIndexOf(".vob")) + "/" + files[i].getName().substring(0, 11) + ".vob";
            }
        }
        menu2.DVDMainMenu();
        assertEquals(dvdMenuOptions, menu2.dvd_menu);
    }
}