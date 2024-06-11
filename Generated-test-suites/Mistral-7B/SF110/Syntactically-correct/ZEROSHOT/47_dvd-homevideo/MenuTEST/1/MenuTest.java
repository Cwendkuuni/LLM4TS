import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MenuTest {

    private Menu menu;
    private String title;
    private String textFilePath;
    private boolean isPAL;
    private GUI gui;

    @Before
    public void setUp() {
        title = "Test Menu";
        textFilePath = "test.txt";
        isPAL = false;
        gui = new GUI();
        menu = new Menu(title, textFilePath, isPAL, gui);
    }

    @After
    public void tearDown() {
        if (menu != null) {
            menu.init();
        }
    }

    @Test
    public void testConstructor() {
        assertFalse(menu.m_Error);
    }

    @Test
    public void testInitWithNoArgs() {
        menu = new Menu();
        menu.init();
        assertTrue(menu.m_Error || gui.ErrorCheck(gui.strOutputDir + "/log/dvd-menu.log"));
    }

    @Test
    public void testInitWithTitleAndTextFilePath() {
        menu = new Menu(title, textFilePath, isPAL, gui);
        menu.init();
        assertTrue(menu.m_Error || gui.ErrorCheck(gui.strOutputDir + "/log/dvd-menu.log"));
    }

    @Test
    public void testInitWithTitlePicPathAudioPathTextFilePathIsPALAndGUI() {
        String picPath = "test.jpg";
        String audioPath = "test.mp3";
        menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, gui);
        menu.init();
        assertTrue(menu.m_Error || gui.ErrorCheck(gui.strOutputDir + "/log/dvd-menu.log"));
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullGUI() {
        menu = new Menu(title, textFilePath, isPAL, null);
        menu.init();
    }

    @Test
    public void testDVDMainMenu() throws IOException, InterruptedException {
        final File ListDir = new File(gui.strOutputDir);
        final FilenameFilter filter = new FilenameFilter() {
            boolean ret_val = true;

            @Override
            public boolean accept(final File dir, final String name) {
                if (name.endsWith("vob")) {
                    if (!name.equals("menu.vob")) {
                        ret_val = true;
                    } else {
                        ret_val = false;
                    }
                } else {
                    ret_val = false;
                }
                return ret_val;
            }
        };
        StringJoiner joiner = new StringJoiner(" ", " ", "\n");
        menu.DVDMainMenu();
        menu.createXML();
        this.video_files = ListDir.listFiles(filter);
        String menuOptions = menu.dvd_menu;
        String expectedMenuOptions = joiner.join(this.video_files);
        assertTrue(menuOptions.contains(expectedMenuOptions));
    }

    @Test
    public void testCreateXML() throws IOException {
        menu.createXML();
        File xmlFile = new File(gui.strOutputDir + "/vmgm.xml");
        assertTrue(xmlFile.exists());
    }
}