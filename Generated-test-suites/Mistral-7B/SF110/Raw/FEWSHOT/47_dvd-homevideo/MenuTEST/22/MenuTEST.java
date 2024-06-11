package org.apache.commons.cli.menu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class MenuTest {

    private Menu menu;

    @Before
    public void setUp() {
        menu = new Menu();
    }

    @Test
    public void testMenuConstructorWithNoArguments() {
        menu = new Menu();
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertNull(menu.dvd_menu);
        assertNull(menu.titles);
        assertFalse(menu.pal_menu);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuConstructorWithTitleArgument() {
        menu = new Menu("Test Title");
        assertEquals("Test Title", menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertNull(menu.dvd_menu);
        assertNull(menu.titles);
        assertFalse(menu.pal_menu);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuConstructorWithTitlePicPathAudioPathTextFilePathArguments() {
        String title = "Test Title";
        String picPath = "path/to/pic.jpg";
        String audioPath = "path/to/audio.mp3";
        String textFilePath = "path/to/text.txt";

        menu = new Menu(title, picPath, audioPath, textFilePath, false, null);

        assertEquals(title, menu.strTitle);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertNull(menu.dvd_menu);
        assertNull(menu.titles);
        assertFalse(menu.pal_menu);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuConstructorWithTitleDvdGuiArguments() {
        String title = "Test Title";
        GUI dvdGui = new GUI();

        menu = new Menu(title, "", "", "", false, dvdGui);

        assertEquals(title, menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertNull(menu.dvd_menu);
        assertNull(menu.titles);
        assertFalse(menu.pal_menu);
        assertFalse(menu.m_Error);
        assertNotNull(menu.m_GUI);
        assertEquals(dvdGui, menu.m_GUI);
    }

    @Test
    public void testMenuConstructorWithTitlePicPathAudioPathTextFilePathPalGuiArguments() {
        String title = "Test Title";
        boolean isPAL = true;
        String picPath = "path/to/pic.jpg";
        String audioPath = "path/to/audio.mp3";
        String textFilePath = "path/to/text.txt";
        GUI dvdGui = new GUI();

        menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, dvdGui);

        assertEquals(title, menu.strTitle);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertFalse(menu.pal_menu);
        assertTrue(menu.isPalMenu());
        assertFalse(menu.m_Error);
        assertNotNull(menu.m_GUI);
        assertEquals(dvdGui, menu.m_GUI);
    }

    @Test
    public void testInitWithError() throws InterruptedException {
        menu = new Menu("Test Title", null, null, null, false, null);
        menu.init();
        assertTrue(menu.m_Error);
    }

    @Test
    public void testInitWithoutError() throws InterruptedException {
        menu = new Menu("Test Title", null, null, null, false, null);
        menu.init();
        assertFalse(menu.m_Error);
    }

    @Test
    public void testCreateXML() throws IOException {
        String[] videoFiles = new String[]{"file1.vob", "file2.vob"};
        menu = new Menu("Test Title", null, null, null, false, null);

        Arrays.stream(videoFiles).forEach(filename -> {
            File file = new File("path/to/output_dir/" + filename);
            menu.video_files = new File(menu.m_GUI.strOutputDir).listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".vob");
                }
            });
            assertEquals(filename, menu.video_files[0]);
        });

        menu.createXML();

        File xmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
        assertNotNull(xmlFile);
        assertTrue(xmlFile.length() > 0);
    }

    @Test
    public void testCreateXMLWithError() throws IOException {
        menu = new Menu("Test Title", null, null, null, false, null);

        menu.createXML();

        File vmgmXmlFile = new File(menu.m_GUI.strOutputDir + "/vmgm.xml");
        assertThrows(IOException.class, () -> {
            if (vmgmXmlFile.exists() && vmgmXmlFile.isFile()) {
                vmgmXmlFile.delete();
            }
            if (!vmgmXmlFile.exists()) {
                vmgmXmlFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(vmgmXmlFile));
            writer.write("<dvdauthor dest=\"output_dir/DVD/\">");
            writer.newLine();
            writer.write("\t<vmgm>");
            writer.newLine();
            writer.write("\t\t<menus>");
            writer.newLine();
            writer.close();
        });

        menu.m_Error = true;
        menu.createXML();

        assertFalse(vmgmXmlFile.exists());
    }

    @Test
    public void testDVDMainMenu() {
        // TODO: Write a test case for the DVDMainMenu method
    }
}