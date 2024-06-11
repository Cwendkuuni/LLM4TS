package org.apache.commons.cli.MenuTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.FilenameFilter;

import org.apache.commons.cli.Menu;
import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuInstance() {
        Menu menu = new Menu();
        assertNull(menu.getTitle());
        assertNull(menu.getTextFilePath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        assertTrue(menu.init());
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuWithTitle() {
        String title = "Test Menu";
        Menu menu = new Menu(title, null, false, null);
        assertEquals(title, menu.getTitle());
        assertNull(menu.getTextFilePath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        assertTrue(menu.init());
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuWithTitleTextFilePath() {
        String title = "Test Menu";
        String textFilePath = "test.txt";
        Menu menu = new Menu(title, textFilePath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            assertFalse(menu.m_Error);
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }

    @Test
    public void testMenuWithTitleTextFilePathAudioPath() {
        String title = "Test Menu";
        String textFilePath = "test.txt";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, audioPath, null, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(audioPath, menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            assertFalse(menu.m_Error);
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }

    @Test
    public void testMenuWithTitleTextFilePathPicPath() {
        String title = "Test Menu";
        String textFilePath = "test.txt";
        String picPath = "image.jpg";
        Menu menu = new Menu(title, textFilePath, null, picPath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertNull(menu.getAudioPath());
        assertEquals(picPath, menu.getPicPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            assertFalse(menu.m_Error);
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }

    @Test
    public void testMenuWithTitleTextFilePathPicPathAudioPath() {
        String title = "Test Menu";
        String textFilePath = "test.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(picPath, menu.getPicPath());
        assertEquals(audioPath, menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            assertFalse(menu.m_Error);
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }

    @Test
    public void testMenuInitialization() {
        String title = "Test Menu";
        String textFilePath = "test.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(picPath, menu.getPicPath());
        assertEquals(audioPath, menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            assertFalse(menu.m_Error);
            String[] videoFiles = menu.getVideoFiles();
            assertEquals(1, videoFiles.length);
            assertEquals("menu.vob", videoFiles[0]);
            String[] titles = menu.getTitles();
            assertEquals(0, titles.length);
            File xmlFile = new File(menu.getOutputDir(), "vmgm.xml");
            File file = new File(menu.getOutputDir(), "dvd_fs");
            assertFalse(xmlFile.exists());
            assertFalse(file.exists());
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }

    @Test
    public void testMenuInitializationWithError() throws Exception {
        String title = "Test Menu";
        String textFilePath = "test.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(picPath, menu.getPicPath());
        assertEquals(audioPath, menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        File xmlFile = new File(menu.getOutputDir(), "vmgm.xml");
        File file = new File(menu.getOutputDir(), "dvd_fs");
        try {
            menu.init();
            fail("Expected initialization to fail");
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
        assertFalse(xmlFile.exists());
        assertFalse(file.exists());
    }

    @Test
    public void testMenuWithTextFile() throws IOException {
        String title = "Test Menu";
        String textFilePath = "test.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(picPath, menu.getPicPath());
        assertEquals(audioPath, menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            assertFalse(menu.m_Error);
            String[] videoFiles = menu.getVideoFiles();
            assertEquals(2, videoFiles.length);
            assertEquals("menu.vob", videoFiles[0]);
            assertEquals("some_video.vob", videoFiles[1]);
            String[] titles = menu.getTitles();
            assertEquals(2, titles.length);
            assertEquals("Title 1", titles[0]);
            assertEquals("Title 2", titles[1]);
            File xmlFile = new File(menu.getOutputDir(), "vmgm.xml");
            File file = new File(menu.getOutputDir(), "dvd_fs");
            assertTrue(xmlFile.exists());
            assertTrue(file.exists());
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }

    @Test
    public void testMenuWithEmptyTextFile() throws IOException {
        String title = "Test Menu";
        String textFilePath = "empty.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(picPath, menu.getPicPath());
        assertEquals(audioPath, menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            assertFalse(menu.m_Error);
            String[] videoFiles = menu.getVideoFiles();
            assertEquals(1, videoFiles.length);
            assertEquals("menu.vob", videoFiles[0]);
            String[] titles = menu.getTitles();
            assertEquals(0, titles.length);
            File xmlFile = new File(menu.getOutputDir(), "vmgm.xml");
            File file = new File(menu.getOutputDir(), "dvd_fs");
            assertFalse(xmlFile.exists());
            assertFalse(file.exists());
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }

    @Test
    public void testMenuWithInvalidTextFile() throws IOException {
        String title = "Test Menu";
        String textFilePath = "invalid.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(picPath, menu.getPicPath());
        assertEquals(audioPath, menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            fail("Expected initialization to fail");
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }

    @Test
    public void testMenuWithLongTextFile() throws IOException {
        String title = "Test Menu";
        String textFilePath = "long_text.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(picPath, menu.getPicPath());
        assertEquals(audioPath, menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            fail("Expected initialization to fail");
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
        File file = new File(menu.getOutputDir(), "log/dvd-menu.log");
        assertTrue(file.exists());
    }

    @Test
    public void testMenuWithMissingTextFile() throws IOException {
        String title = "Test Menu";
        String textFilePath = "missing.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, false, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(picPath, menu.getPicPath());
        assertEquals(audioPath, menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            fail("Expected initialization to fail");
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
        File file = new File(menu.getOutputDir(), "log/dvd-menu.log");
        assertTrue(file.exists());
    }

    @Test
    public void testMenuWithPAL() throws Exception {
        String title = "Test Menu";
        String textFilePath = "test.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, true, null);
        assertEquals(title, menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertEquals(picPath, menu.getPicPath());
        assertEquals(audioPath, menu.getAudioPath());
        assertTrue(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            assertFalse(menu.m_Error);
            String[] videoFiles = menu.getVideoFiles();
            assertEquals(2, videoFiles.length);
            assertEquals("menu.vob", videoFiles[0]);
            assertEquals("some_video.vob", videoFiles[1]);
            String[] titles = menu.getTitles();
            assertEquals(2, titles.length);
            assertEquals("Title 1", titles[0]);
            assertEquals("Title 2", titles[1]);
            File xmlFile = new File(menu.getOutputDir(), "vmgm.xml");
            File file = new File(menu.getOutputDir(), "dvd_fs");
            assertTrue(xmlFile.exists());
            assertTrue(file.exists());
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }

    @Test
    public void testMenuWithInvalidTitle() {
        String title = null;
        String textFilePath = "test.txt";
        String picPath = "image.jpg";
        String audioPath = "audio.mp3";
        Menu menu = new Menu(title, textFilePath, picPath, audioPath, false, null);
        assertNull(menu.getTitle());
        assertEquals(textFilePath, menu.getTextFilePath());
        assertNull(menu.getPicPath());
        assertNull(menu.getAudioPath());
        assertFalse(menu.isPAL());
        assertEquals(0, menu.getTitles().length);
        try {
            menu.init();
            fail("Expected initialization to fail");
        } catch (Exception e) {
            assertTrue(menu.m_Error);
        }
    }
}