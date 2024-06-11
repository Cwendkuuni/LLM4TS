package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertLineContains;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.cli.stratego.StrategoRuntime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MenuTest {

    private Menu menu;
    private String strOutputDir = "output";
    private GUI gui;

    @Before
    public void setUp() {
        this.gui = new GUI();
        this.menu = new Menu();
        File outputDir = new File(this.strOutputDir);
        if (outputDir.exists() && outputDir.isDirectory()) {
            FileUtils.deleteDirectory(outputDir);
        }
        outputDir.mkdir();
        this.menu.m_GUI = this.gui;
    }

    @After
    public void tearDown() {
        File outputDir = new File(this.strOutputDir);
        if (outputDir.exists() && outputDir.isDirectory()) {
            FileUtils.deleteDirectory(outputDir);
        }
    }

    @Test
    public void testMenuConstructor() {
        Menu menu = new Menu();
        assertNotNull(menu.dvd_menu);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertFalse(menu.pal_menu);
        assertNull(menu.titles);
        assertNull(menu.video_files);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuTitleConstructor() {
        String title = "Test Menu";
        Menu menu = new Menu(title, null, false, this.gui);
        assertEquals(title, menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertFalse(menu.pal_menu);
        assertNull(menu.titles);
        assertNull(menu.video_files);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testMenuTitlePicPathAudioPathTextFilePathConstructor() {
        String title = "Test Menu";
        String picPath = "test.jpg";
        String audioPath = "test.wav";
        String textFilePath = "test.txt";
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, false, this.gui);
        assertEquals(title, menu.strTitle);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertFalse(menu.pal_menu);
        assertNull(menu.titles);
        assertNull(menu.video_files);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);
    }

    @Test
    public void testInitThrowsExceptionIfThreadIsInterrupted() {
        Menu menu = new Menu();
        this.gui.setThreadInterrupted(true);
        assertThrows(InterruptedException.class, () -> menu.init());
    }

    @Test
    public void testInitReturnsFalseIfNoErrors() {
        Menu menu = new Menu();
        assertTrue(menu.init());
        assertFalse(menu.m_Error);
    }

    @Test
    public void testInitReturnsTrueIfErrors() {
        Menu menu = new Menu();
        this.gui.setErrorCheckResult(true);
        assertTrue(menu.init());
        assertTrue(menu.m_Error);
    }

    @Test
    public void testRun() {
        Menu menu = new Menu();
        String dvdMenuOutput = "dvd-menu output";
        this.gui.setDvdMenuOutput(dvdMenuOutput);
        menu.run();
        assertTrue(menu.m_Error || this.gui.ErrorCheck(this.strOutputDir + "/log/dvd-menu.log"));
    }

    @Test
    public void testCreateXMLReturnsFalse() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());
    }

    @Test
    public void testCreateXMLCreatesVmgmXmlFile() throws IOException {
        Menu menu = new Menu();
        String xmlFile = this.strOutputDir + "/vmgm.xml";
        menu.createXML();
        assertTrue(new File(xmlFile).exists() && new File(xmlFile).isFile());
    }

    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        String dvdMenuOutput = "dvd-menu output";
        this.gui.setDvdMenuOutput(dvdMenuOutput);
        menu.DVDMainMenu();
        assertTrue(menu.m_Error || this.gui.ErrorCheck(this.strOutputDir + "/log/dvd-menu.log"));
    }

    @Test
    public void testInitWithTextFile() throws IOException {
        Menu menu = new Menu();
        String textFilePath = "test.txt";
        File txtTextFile = new File(textFilePath);
        String[] lines = {"Title 1", "Title 2", "Title 3", "Title 4"};
        Files.write(txtTextFile.toPath(), String.join("\n", lines).getBytes());
        this.gui.setTxtTextFile(txtTextFile.getAbsolutePath());
        menu.init();
        assertFalse(menu.m_Error);
        String[] titles = menu.titles;
        assertNotNull(titles);
        assertEquals(4, titles.length);
        assertEquals("Title 1", titles[0]);
        assertEquals("Title 2", titles[1]);
        assertEquals("Title 3", titles[2]);
        assertEquals("Title 4", titles[3]);
        String dvdMenuOutput = "dvd-menu output";
        this.gui.setDvdMenuOutput(dvdMenuOutput);
        menu.DVDMainMenu();
        assertTrue(menu.m_Error || this.gui.ErrorCheck(this.strOutputDir + "/log/dvd-menu.log"));
    }

    @Test
    public void testInitWithTextFileEmptyError() throws IOException {
        Menu menu = new Menu();
        String textFilePath = "test.txt";
        File txtTextFile = new File(textFilePath);
        this.gui.setTxtTextFile(txtTextFile.getAbsolutePath());
        menu.init();
        assertTrue(menu.m_Error);
        String errorMessage = this.gui.getErrorMessage();
        assertLineContains("Your text file can only have a maximum of 10 titles!", errorMessage);
    }

    @Test
    public void testInitWithTextFileMismatchedLines() throws IOException {
        Menu menu = new Menu();
        String textFilePath = "test.txt";
        File txtTextFile = new File(textFilePath);
        String[] lines = {"Title 1", "Title 2", "Title 3", "11 Titles"};
        Files.write(txtTextFile.toPath(), String.join("\n", lines).getBytes());
        this.gui.setTxtTextFile(txtTextFile.getAbsolutePath());
        menu.init();
        assertTrue(menu.m_Error);
        String errorMessage = this.gui.getErrorMessage();
        assertLineContains("It appears that your text file is not formatted correctly.", errorMessage);
    }

    @Test
    public void testInitWithTextFileNoVobFiles() throws IOException {
        Menu menu = new Menu();
        String textFilePath = "test.txt";
        File txtTextFile = new File(textFilePath);
        String[] lines = {"Title 1", "Title 2", "Title 3", "Title 4", "Title 5"};
        Files.write(txtTextFile.toPath(), String.join("\n", lines).getBytes());
        this.gui.setTxtTextFile(txtTextFile.getAbsolutePath());
        menu.init();
        assertTrue(menu.m_Error);
        String errorMessage = this.gui.getErrorMessage();
        assertLineContains("It appears that your text file is not formatted correctly.", errorMessage);
    }

    @Test
    public void testInitWithTextFileLessVobFiles() throws IOException {
        Menu menu = new Menu();
        String textFilePath = "test.txt";
        File txtTextFile = new File(textFilePath);
        String[] lines = {"Title 1", "Title 2", "Title 3", "Title 4"};
        Files.write(txtTextFile.toPath(), String.join("\n", lines).getBytes());
        File[] videoFiles = new File(this.strOutputDir).listFiles((dir, name) -> name.endsWith(".vob"));
        assertEquals(5, videoFiles.length);
        menu.init();
        assertTrue(menu.m_Error);
        String errorMessage = this.gui.getErrorMessage();
        assertLineContains("It appears that your text file is not formatted correctly.", errorMessage);
    }

    @Test
    public void testInitWithTextFileLessTitles() throws IOException {
        Menu menu = new Menu();
        String textFilePath = "test.txt";
        File txtTextFile = new File(textFilePath);
        String[] lines = {"Title 1", "Title 2"};
        Files.write(txtTextFile.toPath(), String.join("\n", lines).getBytes());
        File[] videoFiles = new File(this.strOutputDir).listFiles((dir, name) -> name.endsWith(".vob"));
        assertEquals(5, videoFiles.length);
        menu.init();
        assertTrue(menu.m_Error);
        String errorMessage = this.gui.getErrorMessage();
        assertLineContains("Your text file can only have a maximum of 10 titles!", errorMessage);
    }

    @Test
    public void testInitWithTextFileMoreTitles() throws IOException {
        Menu menu = new Menu();
        String textFilePath = "test.txt";
        File txtTextFile = new File(textFilePath);
        String[] lines = {"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11"};
        Files.write(txtTextFile.toPath(), String.join("\n", lines).getBytes());
        File[] videoFiles = new File(this.strOutputDir).listFiles((dir, name) -> name.endsWith(".vob"));
        assertEquals(5, videoFiles.length);
        menu.init();
        assertTrue(menu.m_Error);
        String errorMessage = this.gui.getErrorMessage();
        assertLineContains("Your text file can only have a maximum of 10 titles!", errorMessage);
    }

    @Test
    public void testInitWithTextFileMismatchedTitlesAndVobFiles() throws IOException {
        Menu menu = new Menu();
        String textFilePath = "test.txt";
        File txtTextFile = new File(textFilePath);
        String[] lines = {"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12"};
        Files.write(txtTextFile.toPath(), String.join("\n", lines).getBytes());
        File[] videoFiles = new File(this.strOutputDir).listFiles((dir, name) -> name.endsWith(".vob"));
        assertEquals(5, videoFiles.length);
        menu.init();
        assertTrue(menu.m_Error);
        String errorMessage = this.gui.getErrorMessage();
        assertLineContains("It appears that your text file is not formatted correctly.", errorMessage);
    }

    @Test
    public void testInitWithTextFileAndVobFiles() throws IOException {
        Menu menu = new Menu();
        String textFilePath = "test.txt";
        File txtTextFile = new File(textFilePath);
        String[] lines = {"Title 1", "Title 2", "Title 3", "Title 4", "Title 5"};
        Files.write(txtTextFile.toPath(), String.join("\n", lines).getBytes());
        File[] videoFiles = new File(this.strOutputDir).listFiles((dir, name) -> name.endsWith(".vob"));
        assertEquals(5, videoFiles.length);
        menu.init();
        assertFalse(menu.m_Error);
        String dvdMenuOutput = this.gui.getDvdMenuOutput();
        assertLineContains("Title 1", dvdMenuOutput);
        assertLineContains("Title 2", dvdMenuOutput);
        assertLineContains("Title 3", dvdMenuOutput);
        assertLineContains("Title 4", dvdMenuOutput);
        assertLineContains("Title 5", dvdMenuOutput);
    }
}