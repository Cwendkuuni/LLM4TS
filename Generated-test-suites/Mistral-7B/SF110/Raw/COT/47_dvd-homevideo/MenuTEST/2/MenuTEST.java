import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class MenuTest {

    private Menu menu;

    @Before
    public void setup() {
        this.menu = new Menu();
    }

    @Test
    public void testConstructor() {
        Menu noArgsMenu = new Menu();
        assertNotNull(noArgsMenu);

        Constructor<Menu> constructor1 = Menu.class.getConstructor(String.class, String.class, boolean.class, GUI.class);
        Menu argsMenu1 = new Menu("Title1", "", "", "", true, null);
        assertEquals(argsMenu1.strTitle, "Title1");

        Constructor<Menu> constructor2 = Menu.class.getConstructor(String.class, String.class, String.class, String.class, boolean.class, GUI.class);
        Menu argsMenu2 = new Menu("Title2", "picPath1", "audioPath1", "textFilePath1", false, null);
        assertEquals(argsMenu2.strTitle, "Title2");
        assertEquals(argsMenu2.strPicPath, "picPath1");
        assertEquals(argsMenu2.strAudioPath, "audioPath1");
        assertEquals(argsMenu2.strTextFilePath, "textFilePath1");
    }

    @Test
    public void testInit() {
        Menu menuWithThreadInterruption = new Menu();
        menuWithThreadInterruption.m_Thread.interrupt();
        assert(menuWithThreadInterruption.m_Error);

        Menu menuWithoutError = new Menu();
        assertFalse(menuWithoutError.m_Error);

        Menu menuWithGUIError = new Menu("Title", null, null, null, false, null);
        menuWithGUIError.m_GUI = new GUI() {
            @Override
            public boolean ErrorCheck(String strOutputDir) {
                return true;
            }
        };
        menuWithGUIError.init();
        assertTrue(menuWithGUIError.m_Error);
    }

    @Test
    public void testDVDMainMenuWithNoTextFile() {
        File outputDir = new File("outputDir");
        File logDir = new File(outputDir, "log");
        File textFile = new File("textFile.txt");

        Menu menu = new Menu("Title", null, null, null, false, null);
        menu.m_GUI = new GUI() {
            @Override
            public String strOutputDir() {
                return outputDir.getAbsolutePath();
            }

            @Override
            public File txtTextFile() {
                return textFile;
            }
        };

        menu.DVDMainMenu();

        File[] filesInOutputDir = outputDir.listFiles();
        File dvdMenuLogFile = new File(logDir, "dvd-menu.log");

        assertNotNull(filesInOutputDir);
        assertTrue(Arrays.stream(filesInOutputDir).anyMatch(f -> f.getName().endsWith("menu.vob")));
        assertFalse(dvdMenuLogFile.exists());
    }

    @Test
    public void testDVDMainMenuWithTextFile() {
        File outputDir = new File("outputDir");
        File logDir = new File(outputDir, "log");
        File textFile = new File("textFile.txt");
        String textFileContent = "Title1\nTitle2\nBLANK\nDONE";

        Menu menu = new Menu("Title", null, null, null, false, null);
        menu.m_GUI = new GUI() {
            @Override
            public String strOutputDir() {
                return outputDir.getAbsolutePath();
            }

            @Override
            public File txtTextFile() {
                return textFile;
            }
        };

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))) {
            writer.write(textFileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        menu.DVDMainMenu();

        File[] filesInOutputDir = outputDir.listFiles();
        File dvdMenuLogFile = new File(logDir, "dvd-menu.log");

        assertNotNull(filesInOutputDir);
        assertTrue(Arrays.stream(filesInOutputDir).anyMatch(f -> f.getName().endsWith("menu.vob")));
        assertTrue(dvdMenuLogFile.exists());
    }

    @Test
    public void testCreateXMLWithNoVobFiles() {
        File outputDir = new File("outputDir");

        Menu menu = new Menu("Title", null, null, null, false, null);
        menu.m_GUI = new GUI() {
            @Override
            public String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        };

        assertFalse(menu.createXML());
    }

    @Test
    public void testCreateXMLWithVobFiles() {
        File outputDir = new File("outputDir");
        File[] vobFiles = {new File("vobFile1.vob"), new File("vobFile2.vob")};

        Menu menu = new Menu("Title", null, null, null, false, null);
        menu.m_GUI = new GUI() {
            @Override
            public String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        };

        File xmlFile = new File(outputDir, "vmgm.xml");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile));
            writer.write("<dvdauthor dest=\"" + outputDir.getAbsolutePath() + "/DVD/\">");
            writer.newLine();
            writer.write("\t<vmgm>");
            writer.newLine();
            writer.write("\t\t<menus>");
            writer.newLine();
            writer.write("\t\t\t<pgc entry=\"title\" >");
            writer.newLine();
            writer.write("\t\t\t\t<vob file=\"" + vobFiles[0].getAbsolutePath() + "\" pause=\"inf\" />");
            writer.newLine();
            writer.write("\t\t\t\t<post> jump vmgm menu 1; </post>");
            writer.newLine();
            writer.write("\t\t\t</pgc>");
            writer.newLine();
            writer.write("\t\t</menus>");
            writer.newLine();
            writer.write("\t</vmgm>");
            writer.newLine();
            writer.write("\t<titleset>");
            writer.newLine();
            writer.write("\t<titles>");
            writer.newLine();
            writer.write("\t\t<pgc>");
            writer.newLine();
            writer.write("\t\t\t<vob file=\"" + vobFiles[0].getAbsolutePath() + "\" />");
            writer.newLine();
            writer.write("\t\t\t<post>jump title 1;</post>");
            writer.newLine();
            writer.write("\t\t</pgc>");
            writer.newLine();
            writer.write("\t</titles>");
            writer.newLine();
            writer.write("\t</titleset>");
            writer.newLine();
            writer.write("</dvdauthor>");
            writer.newLine();
            writer.close();

            assertTrue(menu.createXML());
            File xmlFileLoaded = new File(outputDir, "vmgm.xml");
            assertTrue(xmlFileLoaded.exists());

            // Perform some validation for the XML file here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}