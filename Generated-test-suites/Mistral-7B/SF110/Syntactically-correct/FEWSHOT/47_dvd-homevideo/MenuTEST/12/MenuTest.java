package org.apache.commons.cli.MenuTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.FilenameFilter;

import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuConstruction() {
        Menu menu = new Menu();
        assertNull(menu.getStrTitle());
        assertEquals(50, menu.getTitles().length);
        assertTrue(menu.isError());
        assertEquals("Menu Error - ", menu.getBaseErr());
        assertNull(menu.getM_GUI());
        assertNull(menu.getM_Thread());
        assertFalse(menu.isError());

        Menu menuTitle = new Menu("Title");
        assertEquals("Title", menuTitle.getStrTitle());
        assertEquals(50, menuTitle.getTitles().length);
        assertTrue(menuTitle.isError());
        assertEquals("Menu Error - ", menuTitle.getBaseErr());
        assertNull(menuTitle.getM_GUI());
        assertNull(menuTitle.getM_Thread());
        assertFalse(menuTitle.isError());

        Menu menuTitleTextFilePath = new Menu("Title", "TextFilePath");
        assertEquals("Title", menuTitleTextFilePath.getStrTitle());
        assertEquals(50, menuTitleTextFilePath.getTitles().length);
        assertTrue(menuTitleTextFilePath.isError());
        assertEquals("Menu Error - ", menuTitleTextFilePath.getBaseErr());
        assertNull(menuTitleTextFilePath.getM_GUI());
        assertNull(menuTitleTextFilePath.getM_Thread());
        assertFalse(menuTitleTextFilePath.isError());

        Menu menuTitlePicPathAudioPathTextFilePath = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, null);
        assertEquals("Title", menuTitlePicPathAudioPathTextFilePath.getStrTitle());
        assertEquals(50, menuTitlePicPathAudioPathTextFilePath.getTitles().length);
        assertFalse(menuTitlePicPathAudioPathTextFilePath.isError());
        assertNull(menuTitlePicPathAudioPathTextFilePath.getBaseErr());
        assertNull(menuTitlePicPathAudioPathTextFilePath.getM_GUI());
        assertNull(menuTitlePicPathAudioPathTextFilePath.getM_Thread());
    }

    @Test
    public void testMenuInit() {
        Menu menu = new Menu();
        Thread thread = new Thread(menu);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(menu.isError());

        Menu menuTitle = new Menu("Title");
        Menu menuTitleThread = new Thread(menuTitle);
        menuTitleThread.start();
        try {
            menuTitleThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(menuTitle.isError());

        Menu menuTitleTextFilePath = new Menu("Title", "TextFilePath");
        Menu menuTitleTextFilePathThread = new Thread(menuTitleTextFilePath);
        menuTitleTextFilePathThread.start();
        try {
            menuTitleTextFilePathThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(menuTitleTextFilePath.isError());

        Menu menuTitlePicPathAudioPathTextFilePath = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, null);
        Menu menuTitlePicPathAudioPathTextFilePathThread = new Thread(menuTitlePicPathAudioPathTextFilePath);
        menuTitlePicPathAudioPathTextFilePathThread.start();
        try {
            menuTitlePicPathAudioPathTextFilePathThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(menuTitlePicPathAudioPathTextFilePath.isError());
    }

    @Test
    public void testDVDMainMenu() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        Menu menu = new Menu("Title", null, null, "TextFilePath", true, null);
        menu.init();
        assertTrue(menu.isError());

        File outputDir = new File(tempDir.getAbsolutePath() + "/output");
        outputDir.mkdirs();
        Menu menuOutputDir = new Menu("Title", null, null, "TextFilePath", true, null);
        menuOutputDir.setM_GUI(new GUI());
        menuOutputDir.init();
        assertFalse(menuOutputDir.isError());

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".vob");
            }
        };
        File[] files = outputDir.listFiles(filter);
        assertEquals(1, files.length);
        assertEquals("menu.vob", files[0].getName());

        File xmlFile = new File(outputDir, "vmgm.xml");
        assertTrue(xmlFile.exists());
    }

    @Test
    public void testCreateXML() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        Menu menu = new Menu("Title", null, null, "TextFilePath", true, null);
        menu.init();
        assertTrue(menu.isError());

        File outputDir = new File(tempDir.getAbsolutePath() + "/output");
        outputDir.mkdirs();
        Menu menuOutputDir = new Menu("Title", null, null, "TextFilePath", true, null);
        menuOutputDir.setM_GUI(new GUI());
        menuOutputDir.init();
        assertFalse(menuOutputDir.isError());

        menuOutputDir.createXML();
        File xmlFile = new File(outputDir, "vmgm.xml");
        assertTrue(xmlFile.exists());
    }

    @Test
    public void testDVDMainMenuWithTextFile() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        Menu menu = new Menu("Title", null, null, "TextFilePath", true, null);
        menu.setM_GUI(new GUI());
        menu.init();
        assertTrue(menu.isError());

        File outputDir = new File(tempDir.getAbsolutePath() + "/output");
        outputDir.mkdirs();
        Menu menuOutputDir = new Menu("Title", null, null, "TextFilePath", true, null);
        menuOutputDir.setM_GUI(new GUI());
        menuOutputDir.init();
        assertFalse(menuOutputDir.isError());

        File txtTextFile = File.createTempFile("test", ".txt");
        menuOutputDir.setTxtTextFile(txtTextFile);
        menuOutputDir.init();
        assertTrue(menuOutputDir.isError());

        File xmlFile = new File(outputDir, "vmgm.xml");
        assertFalse(xmlFile.exists());

        txtTextFile.delete();
        txtTextFile.createNewFile();
        String[] lines = {"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11"};
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtTextFile))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
        menuOutputDir.init();
        assertFalse(menuOutputDir.isError());

        xmlFile = new File(outputDir, "vmgm.xml");
        assertTrue(xmlFile.exists());

        String xmlContent = readFile(xmlFile);
        String expectedXmlContent = "<dvdauthor dest=\"output/DVD/\">"
                + "<vmgm>"
                + "\t<menus>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/menu.vob\" pause=\"inf\" />"
                + "\t\t\t<post> jump vmgm menu 1; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 1.vob\" />"
                + "\t\t\t<post> jump title 2; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 2.vob\" />"
                + "\t\t\t<post> jump title 3; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 3.vob\" />"
                + "\t\t\t<post> jump title 4; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 4.vob\" />"
                + "\t\t\t<post> jump title 5; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 5.vob\" />"
                + "\t\t\t<post> jump title 6; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 6.vob\" />"
                + "\t\t\t<post> jump title 7; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 7.vob\" />"
                + "\t\t\t<post> jump title 8; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 8.vob\" />"
                + "\t\t\t<post> jump title 9; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 9.vob\" />"
                + "\t\t\t<post> jump title 10; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 10.vob\" />"
                + "\t\t\t<post> jump title 11; </post>"
                + "\t\t</pgc>"
                + "\t\t<pgc entry=\"title\" >"
                + "\t\t\t<vob file=\"output/Title 11.vob\" />"
                + "\t\t\t<post> call vmgm menu 1; </post>"
                + "\t\t</pgc>"
                + "\t</menus>"
                + "</vmgm>"
                + "</dvdauthor>";
        assertEquals(expectedXmlContent, xmlContent);

        xmlFile.delete();
    }
}