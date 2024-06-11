package com.example.menutest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.File;
import java.io.FileFilter;
import java.io.IOException;

public class MenuTest {

    private Menu menu;
    private GUI guiMock;
    private String[] videoFiles;
    private File testDir;

    @Before
    public void setUp() {
        guiMock = new GUIMock();
        menu = new Menu("Test Title", "", "", "", true, guiMock);
        testDir = new File("testOutputDir");
        testDir.mkdirs();
    }

    @Test
    public void testConstructor() {
        Menu menu = new Menu();
        assertNotNull(menu.dvd_menu);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertEquals(0, menu.titles.length);
    }

    @Test
    public void testInit() throws InterruptedException, IOException {
        menu.init();
        assertTrue(menu.m_Error);
        assertEquals("Menu Error - dvd-menu.log is not found", menu.m_GUI.errMsg);

        File dvdMenuLogFile = new File(testDir.getAbsolutePath() + "/log/dvd-menu.log");
        File dvdMenuFile = new File(testDir.getAbsolutePath() + "/dvd_fs");
        assertTrue(dvdMenuLogFile.exists());
        assertTrue(dvdMenuFile.exists() && dvdMenuFile.isDirectory());

        menu = new Menu("Test Title", "", "", "", true, guiMock);
        menu.init();
        assertFalse(menu.m_Error);
    }

    @Test
    public void testDVDMainMenu() throws IOException {
        File testTextFile = new File(testDir.getAbsolutePath(), "testTextFile.txt");
        testTextFile.createNewFile();
        try (PrintWriter writer = new PrintWriter(testTextFile)) {
            writer.println("Title 1");
            writer.println("Title 2");
            writer.println("Title 3");
            writer.println("BLANK");
            writer.println("Title 4");
            writer.println("Title 5");
            writer.println("DONE");
        }
        menu.strTextFilePath = testTextFile.getAbsolutePath();

        menu.DVDMainMenu();

        videoFiles = testDir.listFiles(new FileNameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".vob");
            }
        });

        assertEquals(6, videoFiles.length);
        assertEquals("Title 1", menu.titles[0]);
        assertEquals("Title 2", menu.titles[1]);
        assertEquals("Title 3", menu.titles[2]);
        assertNull(menu.titles[3]);
        assertEquals("Title 4", menu.titles[4]);
        assertEquals("Title 5", menu.titles[5]);

        testTextFile.delete();
    }

    @Test(expected = IOException.class)
    public void testDVDMainMenuWithInvalidTextFile() throws IOException {
        menu.strTextFilePath = "";
        menu.DVDMainMenu();
    }

    @Test
    public void testCreateXML() throws IOException {
        menu.createXML();

        File xmlFile = new File(testDir.getAbsolutePath(), "vmgm.xml");
        assertTrue(xmlFile.exists());

        xmlFile.delete();
    }

    @Test
    public void testXMLFileContent() throws IOException {
        menu.strTextFilePath = "testTextFile.txt";
        menu.DVDMainMenu();

        String xmlContent = readFile(new File(testDir.getAbsolutePath(), "vmgm.xml"));

        assertEquals("<dvdauthor dest=\"testOutputDir/DVD/\" jumppad=\"0\">\n" +
                "\t<vmgm>\n" +
                "\t\t<menus>\n" +
                "\t\t\t<pgc entry=\"title\" >\n" +
                "\t\t\t\t<vob file=\"testOutputDir/menu.vob\" pause=\"inf\" />\n" +
                "\t\t\t\t<post> jump vmgm menu 1; </post>\n" +
                "\t\t\t</pgc>\n" +
                "\t\t\t<button>jump title 1;</button>\n" +
                "\t\t\t<button>jump title 2;</button>\n" +
                "\t\t\t<button>jump title 3;</button>\n" +
                "\t\t\t<button>jump title 4;</button>\n" +
                "\t\t\t<button>jump title 5;</button>\n" +
                "\t\t\t<post> jump vmgm menu 1; </post>\n" +
                "\t\t</menus>\n" +
                "\t</vmgm>\n" +
                "\t<titleset>\n" +
                "\t<titles>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/1.vob\" />\n" +
                "\t\t\t<post>jump title 1;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/2.vob\" />\n" +
                "\t\t\t<post>jump title 2;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/3.vob\" />\n" +
                "\t\t\t<post>jump title 3;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/4.vob\" />\n" +
                "\t\t\t<post>call vmgm menu 1;</post>\n" +
                "\t\t</pgc>\n" +
                "\t</titles>\n" +
                "\t</titleset>\n" +
                "</dvdauthor>\n", xmlContent);
    }

    @Test
    public void testCreateXMLWithInvalidTextFile() throws IOException {
        menu.strTextFilePath = "";
        menu.createXML();

        File xmlFile = new File(testDir.getAbsolutePath(), "vmgm.xml");
        assertFalse(xmlFile.exists());
    }

    @Test
    public void testErrorCheck() {
        guiMock.setErrorMsg("Test Error Message");
        menu = new Menu("Test Title", "", "", "", true, guiMock);
        menu.init();
        assertTrue(menu.m_Error);
        assertEquals("Test Error Message", menu.m_GUI.errMsg);
    }

    @Test
    public void testCreateXMLWithLessTitlesThanVobs() throws IOException {
        menu.strTextFilePath = "testTextFile.txt";
        File testTextFile = new File(testDir.getAbsolutePath(), "testTextFile.txt");
        try (PrintWriter writer = new PrintWriter(testTextFile)) {
            writer.println("Title 1");
            writer.println("Title 2");
            writer.println("Title 3");
            writer.println("Title 4");
            writer.println("DONE");
        }
        menu.DVDMainMenu();

        menu.createXML();

        String xmlContent = readFile(new File(testDir.getAbsolutePath(), "vmgm.xml"));

        assertEquals("<dvdauthor dest=\"testOutputDir/DVD/\" jumppad=\"0\">\n" +
                "\t<vmgm>\n" +
                "\t\t<menus>\n" +
                "\t\t\t<pgc entry=\"title\" >\n" +
                "\t\t\t\t<vob file=\"testOutputDir/menu.vob\" pause=\"inf\" />\n" +
                "\t\t\t\t<post> jump vmgm menu 1; </post>\n" +
                "\t\t\t</pgc>\n" +
                "\t\t\t<button>jump title 1;</button>\n" +
                "\t\t\t<button>jump title 2;</button>\n" +
                "\t\t\t<button>jump title 3;</button>\n" +
                "\t\t\t<button>jump title 4;</button>\n" +
                "\t\t\t<post> jump vmgm menu 1; </post>\n" +
                "\t\t</menus>\n" +
                "\t</vmgm>\n" +
                "\t<titleset>\n" +
                "\t<titles>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/1.vob\" />\n" +
                "\t\t\t<post>jump title 1;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/2.vob\" />\n" +
                "\t\t\t<post>jump title 2;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/3.vob\" />\n" +
                "\t\t\t<post>jump title 3;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/4.vob\" />\n" +
                "\t\t\t<post>call vmgm menu 1;</post>\n" +
                "\t\t</pgc>\n" +
                "\t</titles>\n" +
                "\t<title length=\"1\">\n" +
                "\t\t<vob file=\"testOutputDir/5.vob\" />\n" +
                "\t\t<post></post>\n" +
                "\t</title>\n" +
                "\t</titleset>\n" +
                "</dvdauthor>\n", xmlContent);

        testTextFile.delete();
    }

    @Test
    public void testCreateXMLWithMoreTitlesThanVobs() throws IOException {
        menu.strTextFilePath = "testTextFile.txt";
        File testTextFile = new File(testDir.getAbsolutePath(), "testTextFile.txt");
        try (PrintWriter writer = new PrintWriter(testTextFile)) {
            writer.println("Title 1");
            writer.println("Title 2");
            writer.println("Title 3");
            writer.println("Title 4");
            writer.println("Title 5");
            writer.println("Title 6");
            writer.println("DONE");
        }
        menu.DVDMainMenu();

        menu.createXML();

        String xmlContent = readFile(new File(testDir.getAbsolutePath(), "vmgm.xml"));

        assertEquals("<dvdauthor dest=\"testOutputDir/DVD/\" jumppad=\"0\">\n" +
                "\t<vmgm>\n" +
                "\t\t<menus>\n" +
                "\t\t\t<pgc entry=\"title\" >\n" +
                "\t\t\t\t<vob file=\"testOutputDir/menu.vob\" pause=\"inf\" />\n" +
                "\t\t\t\t<post> jump vmgm menu 1; </post>\n" +
                "\t\t\t</pgc>\n" +
                "\t\t\t<button>jump title 1;</button>\n" +
                "\t\t\t<button>jump title 2;</button>\n" +
                "\t\t\t<button>jump title 3;</button>\n" +
                "\t\t\t<button>jump title 4;</button>\n" +
                "\t\t\t<post> jump vmgm menu 1; </post>\n" +
                "\t\t</menus>\n" +
                "\t</vmgm>\n" +
                "\t<titleset>\n" +
                "\t<titles>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/1.vob\" />\n" +
                "\t\t\t<post>jump title 1;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/2.vob\" />\n" +
                "\t\t\t<post>jump title 2;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/3.vob\" />\n" +
                "\t\t\t<post>jump title 3;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/4.vob\" />\n" +
                "\t\t\t<post>jump title 4;</post>\n" +
                "\t\t</pgc>\n" +
                "\t\t<pgc>\n" +
                "\t\t\t<vob file=\"testOutputDir/5.vob\" />\n" +
                "\t\t\t<post></post>\n" +
                "\t\t</pgc>\n" +
                "\t</titles>\n" +
                "\t</titleset>\n" +
                "</dvdauthor>\n", xmlContent);

        testTextFile.delete();
    }

    private String readFile(File file) throws IOException {
        byte[] bytes = new byte[1024];
        int read = 0;
        String result = "";
        try (FileInputStream fis = new FileInputStream(file)) {
            while ((read = fis.read(bytes, 0, 1024)) != -1) {
                result += new String(bytes, 0, read);
            }
        }
        return result;
    }

    private static class FileNameFilter implements FileFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".vob");
        }
    }

    private static class GUIMock {
        private String errMsg;

        public void setErrorMsg(String errorMsg) {
            errMsg = errorMsg;
        }
    }
}