package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.FilenameFilter;

import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuConstructionWithNoArguments() {
        Menu menu = new Menu();
        assertNull(menu.getStrTitle());
        assertNull(menu.getStrPicPath());
        assertNull(menu.getStrAudioPath());
        assertNull(menu.getStrTextFilePath());
        assertNull(menu.getDvd_menu());
        assertNull(menu.getTitles());
        assertFalse(menu.isPal_menu());
        assertNull(menu.getBaseErr());
        assertNull(menu.getVideo_files());
        assertNull(menu.getM_GUI());
        assertNull(menu.getM_Thread());
        assertFalse(menu.ism_Error());
    }

    @Test
    public void testMenuConstructionWithTitleArgument() {
        String title = "Test Menu";
        Menu menu = new Menu(title, null, false, null);
        assertEquals(title, menu.getStrTitle());
        assertNull(menu.getStrPicPath());
        assertNull(menu.getStrAudioPath());
        assertNull(menu.getStrTextFilePath());
        assertNull(menu.getDvd_menu());
        assertNull(menu.getTitles());
        assertFalse(menu.isPal_menu());
        assertNull(menu.getBaseErr());
        assertNull(menu.getVideo_files());
        assertNull(menu.getM_GUI());
        assertNull(menu.getM_Thread());
        assertFalse(menu.ism_Error());
    }

    @Test
    public void testMenuConstructionWithTitlePicPathAudioPathTextFilePathArguments() {
        String title = "Test Menu";
        String picPath = "test-pic.jpg";
        String audioPath = "test-audio.mp3";
        String textFilePath = "test-text.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        assertEquals(title, menu.getStrTitle());
        assertEquals(picPath, menu.getStrPicPath());
        assertEquals(audioPath, menu.getStrAudioPath());
        assertEquals(textFilePath, menu.getStrTextFilePath());
        assertNull(menu.getDvd_menu());
        assertNull(menu.getTitles());
        assertFalse(menu.isPal_menu());
        assertNull(menu.getBaseErr());
        assertNull(menu.getVideo_files());
        assertNull(menu.getM_GUI());
        assertNull(menu.getM_Thread());
        assertFalse(menu.ism_Error());
    }

    @Test
    public void testMenuInitializationWithNoFiles() {
        String title = "Test Menu";
        Menu menu = new Menu(title, null, false, null);
        boolean initResult = menu.init();
        assertTrue(initResult);
        assertFalse(menu.ism_Error());
        assertNull(menu.getM_GUI().ErrorCheck(menu.getM_GUI().strOutputDir + "/log/dvd-menu.log"));
    }

    @Test
    public void testMenuInitializationWithFiles() throws IOException, InterruptedException {
        String title = "Test Menu";
        String picPath = "test-pic.jpg";
        String audioPath = "test-audio.mp3";
        String textFilePath = "test-text.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        boolean initResult = menu.init();
        assertTrue(initResult);
        assertFalse(menu.ism_Error());
        assertNull(menu.getM_GUI().ErrorCheck(menu.getM_GUI().strOutputDir + "/log/dvd-menu.log"));

        File outputDir = new File(menu.getM_GUI().strOutputDir);
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("vob");
            }
        };
        String[] files = outputDir.listFiles(filter);
        assertEquals(2, files.length);
    }

    @Test
    public void testMenuInitializationWithError() throws IOException, InterruptedException {
        String title = "Test Menu";
        String textFilePath = "test-text.txt";
        Menu menu = new Menu(title, null, false, null);
        menu.setStrTextFilePath(textFilePath);
        menu.init();
        assertTrue(menu.ism_Error());
    }

    @Test
    public void testMenuCreationXMLWithNoFiles() {
        String title = "Test Menu";
        Menu menu = new Menu(title, null, false, null);
        boolean createXMLResult = menu.createXML();
        assertFalse(createXMLResult);
    }

    @Test
    public void testMenuCreationXMLWithFiles() throws IOException {
        String title = "Test Menu";
        String picPath = "test-pic.jpg";
        String audioPath = "test-audio.mp3";
        String textFilePath = "test-text.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, null);
        boolean createXMLResult = menu.createXML();
        assertTrue(createXMLResult);

        File xmlFile = new File(menu.getM_GUI().strOutputDir + "/vmgm.xml");
        File xmlFileParentDir = new File(menu.getM_GUI().strOutputDir);
        assertTrue(xmlFile.exists());
        assertTrue(xmlFileParentDir.exists());

        FileListUtil.deleteRecursively(xmlFileParentDir);
    }

    @Test
    public void testMenuCreationXMLWithError() throws IOException {
        String title = "Test Menu";
        String textFilePath = "test-text.txt";
        Menu menu = new Menu(title, null, false, null);
        menu.setStrTextFilePath(textFilePath);
        boolean createXMLResult = menu.createXML();
        assertFalse(createXMLResult);
    }
}