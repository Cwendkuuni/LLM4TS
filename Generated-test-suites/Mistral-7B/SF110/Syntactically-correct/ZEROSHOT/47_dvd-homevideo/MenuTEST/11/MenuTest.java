package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class MenuTest {

    private Menu menu;
    private GUI gui;

    @Before
    public void setUp() {
        gui = new GUI();
        menu = new Menu("Test Menu", "", "", "", "", gui);
    }

    @Test
    public void testConstructorWithNoArguments() {
        Menu emptyMenu = new Menu();
        assertTrue(emptyMenu.titles.length == 50);
    }

    @Test
    public void testConstructorWithTitleAndTextFilePath() {
        Menu menuWithTextFile = new Menu("Test Menu", "test.txt", false, gui);
        assertTrue(menuWithTextFile.strTitle.equals("Test Menu"));
        assertTrue(menuWithTextFile.strTextFilePath.equals("test.txt"));
    }

    @Test
    public void testConstructorWithTitlePicPathAudioPathTextFilePath() {
        String picPath = "test.png";
        String audioPath = "test.mp3";
        String textFilePath = "test.txt";
        Menu menuWithTitlePicPathAudioPathTextFilePath = new Menu("Test Menu", picPath, audioPath, textFilePath, false, gui);
        assertTrue(menuWithTitlePicPathAudioPathTextFilePath.strTitle.equals("Test Menu"));
        assertTrue(menuWithTitlePicPathAudioPathTextFilePath.strPicPath.equals(picPath));
        assertTrue(menuWithTitlePicPathAudioPathTextFilePath.strAudioPath.equals(audioPath));
        assertTrue(menuWithTitlePicPathAudioPathTextFilePath.strTextFilePath.equals(textFilePath));
    }

    @Test
    public void testInitReturnsFalseWhenNoErrors() {
        menu.init();
        assertFalse(menu.m_Error);
    }

    @Test
    public void testInitReturnsTrueWhenErrorOccurs() {
        menu.m_GUI = new GUI() {
            @Override
            public boolean ErrorCheck(String logFile) {
                return false;
            }
        };
        menu.init();
        assertTrue(menu.m_Error);
    }

    @Test
    public void testDVDMainMenuCreatesExpectedMenuOptions() {
        menu.strTextFilePath = "test.txt";
        menu.DVDMainMenu();

        File listDir = new File(gui.strOutputDir);
        File[] videoFiles = listDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("vob");
            }
        });

        String[] expectedVideoFiles = {"menu.vob", "test1.vob", "test2.vob", "test3.vob", "test4.vob", "test5.vob"};
        Arrays.sort(videoFiles);
        Arrays.sort(expectedVideoFiles);

        for (int i = 0; i < videoFiles.length; i++) {
            assertTrue(videoFiles[i].getName().equals(expectedVideoFiles[i].getName()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDVDMainMenuThrowsExceptionWhenTextFileNotFound() {
        menu.strTextFilePath = "non_existent_file.txt";
        menu.DVDMainMenu();
    }

    @Test
    public void testCreateXMLCreatesExpectedXMLFile() {
        menu.strTextFilePath = "test.txt";
        menu.createXML();

        File xmlFile = new File(gui.strOutputDir + "/vmgm.xml");
        assertTrue(xmlFile.exists());

        String xmlContent = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(xmlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                xmlContent += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedXmlContent = "<dvdauthor dest=\"test_output/DVD/\" jumppad=\"0\">\n"
                + "\t<vmgm>\n"
                + "\t\t<menus>\n"
                + "\t\t\t<pgc entry=\"title\" >\n"
                + "\t\t\t\t<vob file=\"test_output/menu.vob\" pause=\"inf\" />\n"
                + "\t\t\t\t<button>jump title 1;</button>\n"
                + "\t\t\t\t<button>jump title 2;</button>\n"
                + "\t\t\t\t<button>jump title 3;</button>\n"
                + "\t\t\t\t<button>jump title 4;</button>\n"
                + "\t\t\t\t<button>jump title 5;</button>\n"
                + "\t\t\t\t<post> jump vmgm menu 1; </post>\n"
                + "\t\t\t</pgc>\n"
                + "\t\t</menus>\n"
                + "\t</vmgm>\n"
                + "\t<titleset>\n"
                + "\t\t<titles>\n"
                + "\t\t\t<pgc>\n"
                + "\t\t\t\t<vob file=\"test_output/test1.vob\" />\n"
                + "\t\t\t\t<post>jump title 1;</post>\n"
                + "\t\t\t</pgc>\n"
                + "\t\t\t<pgc>\n"
                + "\t\t\t\t<vob file=\"test_output/test2.vob\" />\n"
                + "\t\t\t\t<post>jump title 2;</post>\n"
                + "\t\t\t</pgc>\n"
                + "\t\t\t<pgc>\n"
                + "\t\t\t\t<vob file=\"test_output/test3.vob\" />\n"
                + "\t\t\t\t<post>jump title 3;</post>\n"
                + "\t\t\t</pgc>\n"
                + "\t\t\t<pgc>\n"
                + "\t\t\t\t<vob file=\"test_output/test4.vob\" />\n"
                + "\t\t\t\t<post>jump title 4;</post>\n"
                + "\t\t\t</pgc>\n"
                + "\t\t\t<pgc>\n"
                + "\t\t\t\t<vob file=\"test_output/test5.vob\" />\n"
                + "\t\t\t\t<post>call vmgm menu 1;</post>\n"
                + "\t\t\t</pgc>\n"
                + "\t\t</titles>\n"
                + "\t</titleset>\n"
                + "</dvdauthor>";

        assertTrue(xmlContent.equals(expectedXmlContent));
    }

    @Test
    public void testCreateXMLThrowsExceptionWhenTextFileNotFound() {
        menu.strTextFilePath = "non_existent_file.txt";
        try {
            menu.createXML();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not find test_output/non_existent_file.txt "));
        }
    }
}