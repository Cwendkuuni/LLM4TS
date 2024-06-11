package org.apache.commons.cli.MenuTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.Menu;
import org.apache.commons.cli.Menu.FilenameFilter;
import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuConstructor() {
        Menu menu = new Menu();
        assertNotNull(menu);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertNull(menu.dvd_menu);
        assertNull(menu.titles);
        assertNull(menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuTitleConstructor() {
        String title = "Test Menu";
        Menu menu = new Menu(title, "", "", "", false, null);
        assertNotNull(menu);
        assertEquals(title, menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertNull(menu.titles);
        assertNull(menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuTitlePicPathAudioPathTextFilePathConstructor() {
        String title = "Test Menu";
        String picPath = "test.jpg";
        String audioPath = "test.wav";
        String textFilePath = "test.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        assertNotNull(menu);
        assertEquals(title, menu.strTitle);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertNull(menu.titles);
        assertNull(menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuInit() {
        String title = "Test Menu";
        String picPath = "test.jpg";
        String audioPath = "test.wav";
        String textFilePath = "test.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        menu.init();
        assertTrue(menu.m_Thread.isAlive());
        try {
            menu.m_Thread.join();
        }
        catch (InterruptedException e) {
            fail("InterruptedException was thrown during joining thread");
        }
        assertFalse(menu.m_Error || menu.m_GUI.ErrorCheck(menu.m_GUI.strOutputDir + "/log/dvd-menu.log"));
    }

    @Test
    public void testInitWithError() {
        String title = "Test Menu";
        String picPath = "test.jpg";
        String audioPath = "test.wav";
        String textFilePath = "test.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        // Simulate an error in init method
        menu.m_Error = true;
        menu.init();
        try {
            menu.m_Thread.join();
        }
        catch (InterruptedException e) {
            fail("InterruptedException was thrown during joining thread");
        }
        assertTrue(menu.m_Error);
    }

    @Test
    public void testDVDMainMenu() {
        String title = "Test Menu";
        String picPath = "test.jpg";
        String audioPath = "test.wav";
        String textFilePath = "test.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        menu.init();
        try {
            menu.m_Thread.join();
        }
        catch (InterruptedException e) {
            fail("InterruptedException was thrown during joining thread");
        }

        FilenameFilter filter = new Menu().new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("vob");
            }
        };

        File ListDir = new File(menu.m_GUI.strOutputDir);
        String[] videoFiles = ListDir.list(filter);
        assertEquals(2, videoFiles.length);
        String menuOptions = "";

        if (menu.strPicPath == null && menu.strAudioPath == null) {
            menuOptions = "-c -D -n \"Test Menu\" -o " + menu.m_GUI.strOutputDir;
        } else if (menu.strPicPath.equals("") && menu.strAudioPath != null) {
            menuOptions = "-c -D -n \"Test Menu\" -o " + menu.m_GUI.strOutputDir + " -a " + menu.strAudioPath;
        } else if (menu.strPicPath != null && menu.strAudioPath.equals("")) {
            menuOptions = "-c -D -n \"Test Menu\" -o " + menu.m_GUI.strOutputDir + " -b " + menu.strPicPath;
        } else {
            menuOptions = "-c -D -n \"Test Menu\" -o " + menu.m_GUI.strOutputDir + " -a " + menu.strAudioPath + " -b " + menu.strPicPath;
        }

        assertEquals(menuOptions, menu.dvd_menu.substring(0, menu.dvd_menu.indexOf(" 2>&1")));
    }

    @Test
    public void testCreateXML() {
        String title = "Test Menu";
        String picPath = "test.jpg";
        String audioPath = "test.wav";
        String textFilePath = "test.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        menu.init();
        try {
            menu.m_Thread.join();
        }
        catch (InterruptedException e) {
            fail("InterruptedException was thrown during joining thread");
        }
        File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
        assertTrue(xmlFile.exists());

        try {
            BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
            String line;
            StringBuilder xmlContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                xmlContent.append(line).append("\n");
            }
            reader.close();

            String expectedXmlContent = "<dvdauthor dest=\"D:/testOutput/DVD/\" jumppad=\"0\">\n"
                    + "\t<vmgm>\n"
                    + "\t\t<menus>\n"
                    + "\t\t\t<pgc entry=\"title\" >\n"
                    + "\t\t\t\t<vob file=\"D:/testOutput/menu.vob\" pause=\"inf\" />\n"
                    + "\t\t\t</pgc>\n"
                    + "\t\t</menus>\n"
                    + "\t</vmgm>\n"
                    + "\t<titleset>\n"
                    + "\t\t<titles>\n"
                    + "\t\t\t<pgc>\n"
                    + "\t\t\t\t<vob file=\"D:/testOutput/test1.vob\" />\n"
                    + "\t\t\t\t<post>jump title 2;</post>\n"
                    + "\t\t\t</pgc>\n"
                    + "\t\t\t<pgc>\n"
                    + "\t\t\t\t<vob file=\"D:/testOutput/test2.vob\" />\n"
                    + "\t\t\t\t<post>call vmgm menu 1;</post>\n"
                    + "\t\t\t</pgc>\n"
                    + "\t\t</titles>\n"
                    + "\t</titleset>\n"
                    + "</dvdauthor>";

            assertEquals(expectedXmlContent.toString(), xmlContent.toString());
        } catch (IOException e) {
            fail("IOException was thrown during reading xml file");
        }
    }
}