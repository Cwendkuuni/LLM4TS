package org.apache.commons.cli.MenuTest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MenuTest {

    private Menu menu;
    private GUI dummyGUI;

    @Before
    public void setUp() {
        this.dummyGUI = new GUI() {};
        this.menu = new Menu("Test Menu", "", false, this.dummyGUI);
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.menu);
        assertEquals("Test Menu", this.menu.getStrTitle());
        assertNull(this.menu.getStrPicPath());
        assertNull(this.menu.getStrAudioPath());
        assertNull(this.menu.getStrTextFilePath());
        assertFalse(this.menu.isPalMenu());
        assertNull(this.menu.getBaseErr());
        assertNull(this.menu.getVideoFiles());
        assertNull(this.menu.getM_GUI());
        assertFalse(this.menu.isError());
        assertNull(this.menu.getM_Thread());
    }

    @Test
    public void testConstructorWithTitle() {
        this.menu = new Menu("Title", "", "", "", false, this.dummyGUI);
        assertEquals("Title", this.menu.getStrTitle());
        assertNull(this.menu.getStrPicPath());
        assertNull(this.menu.getStrAudioPath());
        assertNull(this.menu.getStrTextFilePath());
        assertFalse(this.menu.isPalMenu());
        assertNull(this.menu.getBaseErr());
        assertNull(this.menu.getVideoFiles());
        assertNull(this.menu.getM_GUI());
        assertFalse(this.menu.isError());
        assertNull(this.menu.getM_Thread());
    }

    @Test
    public void testConstructorWithTitlePicPathAudioPathTextFilePath() {
        this.menu = new Menu("Title", "picPath", "audioPath", "textFilePath", false, this.dummyGUI);
        assertEquals("Title", this.menu.getStrTitle());
        assertEquals("picPath", this.menu.getStrPicPath());
        assertEquals("audioPath", this.menu.getStrAudioPath());
        assertEquals("textFilePath", this.menu.getStrTextFilePath());
        assertFalse(this.menu.isPalMenu());
        assertNull(this.menu.getBaseErr());
        assertNull(this.menu.getVideoFiles());
        assertNull(this.menu.getM_GUI());
        assertFalse(this.menu.isError());
        assertNull(this.menu.getM_Thread());
    }

    @Test
    public void testInit() throws InterruptedException {
        this.menu.init();
        assertTrue(this.menu.isError());
    }

    @Test
    public void testRun() {
        this.menu.run();
    }

    @Test
    public void testDVDMainMenu() {
        this.menu.DVDMainMenu();
    }

    @Test
    public void testCreateXML() throws IOException {
        this.menu.createXML();
        File xmlFile = new File(this.menu.getM_GUI().strOutputDir + "/vmgm.xml");
        assertTrue(xmlFile.exists());
    }

    @Test
    public void testGetVideoFiles() {
        File ListDir = new File("src/test/resources/menuTestFiles");
        FilenameFilter filter = new FilenameFilter() {
            boolean ret_val = true;

            @Override
            public boolean accept(File dir, String name) {
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
        this.menu = new Menu("Test Menu", "", "", "", false, this.dummyGUI);
        this.menu.strOutputDir = ListDir.getAbsolutePath();
        this.menu.video_files = ListDir.listFiles(filter);
        assertNotNull(this.menu.video_files);
        assertEquals(3, this.menu.video_files.length);
        String[] expectedVideoFiles = {"test1.vob", "test2.vob", "test3.vob"};
        assertArrayEquals(expectedVideoFiles, this.menu.video_files);
    }

    @Test
    public void testGetError() {
        this.menu.setError(true);
        assertTrue(this.menu.isError());
        this.menu.setError(false);
        assertFalse(this.menu.isError());
    }

    @Test
    public void testGetMThread() {
        assertNull(this.menu.getM_Thread());
    }
}