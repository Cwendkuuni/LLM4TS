import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.NullPointerException;

public class CaptureTest {

    @Test
    public void testCaptureConstructor() {
        Capture capture = new Capture();
        assertNull(capture.m_GUI);
        assertNull(capture.m_Thread);
        assertFalse(capture.m_Error);
        assertNull(capture.m_BaseErr);
        assertNull(capture.m_dvgrab);
    }

    @Test
    public void testCaptureConstructorWithArguments() {
        int min = 10;
        int sec = 20;
        GUI gui = new GUI();
        Capture capture = new Capture(min, sec, gui);
        assertEquals(min, capture.m_Minutes);
        assertEquals(sec, capture.m_Seconds);
        assertNotNull(capture.m_GUI);
        assertNull(capture.m_Thread);
        assertFalse(capture.m_Error);
        assertNull(capture.m_BaseErr);
        assertNull(capture.m_dvgrab);
    }

    @Test
    public void testInit_OutputDirAlreadyExists_DeleteDirectory() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.init();
        File outputDir = new File("/temp");
        assertTrue(outputDir.exists() && outputDir.isDirectory());
        File[] files = outputDir.listFiles();
        assertEquals(0, files.length);
    }

    @Test
    public void testInit_OutputDirAlreadyExists_CancelDeleteDirectory() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.init();
        File outputDir = new File("/temp");
        assertFalse(outputDir.exists() || outputDir.isDirectory());
    }

    @Test
    public void testInit_InvalidOutputDir() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp/invalid";
        capture.init();
        File outputDir = new File("/temp/invalid");
        assertFalse(outputDir.exists() || outputDir.isDirectory());
        capture.m_GUI.MessageBox(capture.m_BaseErr + "Please choose a different name for your title or move the directory to another location.", 2);
        assertTrue(capture.m_Error);
    }

    @Test
    public void testInit_DvgrabNotFound() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.init();
        File outputDir = new File("/temp");
        assertTrue(outputDir.exists() && outputDir.isDirectory());
        String errMessage = capture.m_GUI.MessageBox("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0).toString();
        assertTrue(errMessage.contains("Could not locate dvgrab in your path"));
        assertTrue(capture.m_Error);
    }

    @Test(expected = NullPointerException.class)
    public void testInit_NullGUI() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = null;
        capture.init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInit_NegativeMinutes() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.m_Minutes = -1;
        capture.init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInit_NegativeSeconds() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.m_Seconds = -1;
        capture.init();
    }

    @Test
    public void testRun() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.init();
        capture.run();
        assertTrue(capture.m_Thread.isAlive());
    }

    @Test
    public void testRun_Interrupted() throws InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.init();
        capture.run();
        capture.m_Thread.interrupt();
        assertTrue(capture.m_Thread.isInterrupted());
    }

    @Test
    public void testDV_Capture_OutputDirDoesNotExist() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.init();
        capture.DV_Capture();
        File outputDir = new File("/temp");
        assertTrue(outputDir.exists() && outputDir.isDirectory());
        File[] files = outputDir.listFiles();
        assertEquals(0, files.length);
    }

    @Test
    public void testDV_Capture_OutputDirExistsButIsNotADirectory() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp/file.txt";
        capture.init();
        capture.DV_Capture();
        File outputDir = new File("/temp/file.txt");
        assertFalse(outputDir.exists() || outputDir.isDirectory());
    }

    @Test
    public void testDV_Capture_CreateOutputDirectory() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.init();
        capture.DV_Capture();
        File outputDir = new File("/temp");
        assertTrue(outputDir.exists() && outputDir.isDirectory());
    }

    @Test
    public void testDV_Capture_CreateSubDirectories() throws IOException, InterruptedException {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.init();
        capture.DV_Capture();
        File outputDir = new File("/temp");
        File dvDir = new File(outputDir, "dv");
        File logDir = new File(outputDir, "log");
        assertTrue(outputDir.exists() && outputDir.isDirectory());
        assertTrue(dvDir.exists() && dvDir.isDirectory());
        assertTrue(logDir.exists() && logDir.isDirectory());
    }

    @Test
    public void testDV_Capture_ExecuteDvgrabCommand() throws IOException, InterruptedException, NullPointerException, IllegalArgumentException, IllegalStateException, Exception {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "/temp";
        capture.init();
        capture.DV_Capture();
        String dvgrabCmd = capture.m_dvgrab;
        String[] dvgrabCmdArr = dvgrabCmd.split(" ");
        String command = dvgrabCmdArr[0];
        assertTrue(command.equals("/bin/sh"));
    }
}