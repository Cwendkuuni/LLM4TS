package com.example.menutests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.StringTokenizer;

public class MenuTest {

    private Menu menu;
    private String[] videoFiles;
    private String outputDir = "output";
    private GUI gui;

    @Before
    public void setUp() {
        gui = new GUI();
        menu = new Menu("Test Menu", null, null, null, false, gui);
    }

    @Test
    public void testConstructor() {
        assertNotNull(menu);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertEquals(50, menu.titles.length);
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertFalse(menu.pal_menu);
        assertEquals("Menu Error - ", menu.baseErr);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testConstructorWithTitle() {
        menu = new Menu("Test Menu", null, null, null, false, gui);
        assertEquals("Test Menu", menu.strTitle);
        assertArrayEquals(new String[0], menu.titles);
    }

    @Test
    public void testConstructorWithTitleAndTextFilePath() {
        String textFilePath = "testTextFile.txt";
        menu = new Menu("Test Menu", textFilePath, null, null, false, gui);
        assertEquals("Test Menu", menu.strTitle);
        assertEquals(new String[0], menu.titles);
        assertEquals(textFilePath, menu.strTextFilePath);
    }

    @Test
    public void testConstructorWithTitlePicPathAndAudioPath() {
        String picPath = "testPicPath.jpg";
        String audioPath = "testAudioPath.mp3";
        menu = new Menu("Test Menu", picPath, audioPath, null, false, gui);
        assertEquals("Test Menu", menu.strTitle);
        assertNull(menu.strTextFilePath);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
    }

    @Test
    public void testInit() throws InterruptedException {
        menu.init();
        assertTrue(menu.m_Error || gui.ErrorCheck(outputDir + "/log/dvd-menu.log"));
        assertFalse(menu.m_Thread.isAlive());
    }

    @Test
    public void testDVDMainMenu() throws IOException {
        File outputDir = new File(this.outputDir);
        File listDir = new File(outputDir, "testVobFiles");
        listDir.mkdir();
        File menuVob = new File(listDir, "menu.vob");
        File[] vobFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            vobFiles[i] = new File(listDir, "vobFile" + i + ".vob");
            vobFiles[i].createNewFile();
        }
        menu = new Menu("Test Menu", null, null, null, false, gui);
        menu.strTextFilePath = "testTextFile.txt";
        menu.init();
        menu.DVDMainMenu();
        File xmlFile = new File(outputDir, "vmgm.xml");
        assertTrue(xmlFile.exists());
        xmlFile.delete();
    }

    @Test
    public void testCreateXML() {
        menu = new Menu("Test Menu", null, null, null, false, gui);
        menu.strTextFilePath = "testTextFile.txt";
        menu.init();
        menu.createXML();
        File xmlFile = new File(outputDir, "vmgm.xml");
        assertFalse(xmlFile.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateXMLWithEmptyTextFile() throws IOException {
        menu = new Menu("Test Menu", null, null, null, false, gui);
        menu.strTextFilePath = "testTextFile.txt";
        menu.init();
        menu.titles[0] = "Test Title";
        menu.createXML();
    }

    @Test
    public void testCreateXMLWithTextFile() throws IOException {
        menu = new Menu("Test Menu", null, null, null, false, gui);
        menu.strTextFilePath = "testTextFile.txt";
        menu.init();
        menu.titles[0] = "Test Title 1";
        menu.titles[1] = "Test Title 2";
        menu.createXML();
        File xmlFile = new File(outputDir, "vmgm.xml");
        assertTrue(xmlFile.exists());
        xmlFile.delete();
    }

    @Test(expected = IOException.class)
    public void testCreateXMLWithTextFileIOException() throws IOException {
        menu = new Menu("Test Menu", null, null, null, false, gui);
        menu.strTextFilePath = "testTextFile.txt";
        menu.init();
        menu.titles[0] = "Test Title 1";
        menu.titles[1] = "Test Title 2";
        try (BufferedReader reader = new BufferedReader(new FileReader("nonexistentfile.txt"))) {
            String line = reader.readLine();
        }
        menu.createXML();
    }

    @Test
    public void testAcceptFileNameFilter() {
        File listDir = new File("listDir");
        FilenameFilter filter = new Menu().new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("vob") && !name.equals("menu.vob");
            }
        };
        File[] files = listDir.listFiles(filter);
        assertNotNull(files);
        assertEquals(4, files.length);
    }

    @Test
    public void testAcceptFileNameFilterWithMenuVob() {
        File listDir = new File("listDir");
        FilenameFilter filter = new Menu().new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("vob");
            }
        };
        File[] files = listDir.listFiles(filter);
        assertNotNull(files);
        assertEquals(5, files.length);
    }
}