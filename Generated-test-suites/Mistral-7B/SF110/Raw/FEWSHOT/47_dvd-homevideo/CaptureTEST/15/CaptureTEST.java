package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.lang.reflect.Field;

import org.apache.commons.cli.Capture;
import org.apache.commons.cli.GUI;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CaptureTest {

    private Capture capture;
    private GUI gui;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gui = mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = gui;
    }

    @Test
    public void testInitEmptyConstructor() {
        assertFalse(capture.init());
        verify(gui, never()).MessageBox(anyString(), anyInt());
        verify(gui, never()).strOutputDir;
        verify(gui, never()).ErrorCheck(anyString());
    }

    @Test
    public void testInitWithConstructorAndParams() {
        int min = 1;
        int sec = 2;
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(gui.ErrorCheck(anyString())).thenReturn(true);
        when(gui.strOutputDir).thenReturn("outputDir");

        capture = new Capture(min, sec, gui);

        assertFalse(capture.init());
        verify(gui).MessageBox(eq("The Output Directory outputDir already exists!"), eq(2));
    }

    @Test
    public void testInitNoThread() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(gui.ErrorCheck(anyString())).thenReturn(true);
        when(gui.strOutputDir).thenReturn("outputDir");

        capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        assertTrue(capture.init());
        verify(gui, times(2)).MessageBox(anyString(), anyInt());
        verify(gui).ErrorCheck(eq("outputDir/log/dvgrab.log"));
    }

    @Test
    public void testInitWithThread() {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        when(gui.ErrorCheck(anyString())).thenReturn(true);
        when(gui.strOutputDir).thenReturn("outputDir");

        capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        Thread thread = capture.m_Thread;
        assertTrue(thread.isAlive());
        assertTrue(capture.init());
        verify(gui, never()).MessageBox(anyString(), anyInt());
        verify(gui).ErrorCheck(eq("outputDir/log/dvgrab.log"));
        assertEquals(10, thread.getPriority());
    }

    @Test
    public void testDV_Capture() throws IOException, InterruptedException {
        when(gui.ErrorCheck(eq("outputDir/log/dvgrab.log"))).thenReturn(false);
        when(gui.MessageBox(anyString(), anyInt())).thenReturn(0);

        capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        capture.DV_Capture();

        File outputDir = new File("outputDir");
        File outputDirDVD = new File(outputDir, "DVD");
        File outputDirLog = new File(outputDir, "log");
        File outputDirDv = new File(outputDir, "dv");

        assertTrue(outputDir.mkdir());
        assertTrue(outputDirDVD.mkdir());
        assertTrue(outputDirLog.mkdir());
        assertTrue(outputDirDv.mkdir());

        File logFile = new File(outputDirLog, "dvgrab.log");
        File errFile = new File(outputDirLog, "dvgrab.err");

        File expectedDvgrabFile = new File(outputDirDv, "dv_file-" + System.currentTimeMillis() + ".dv");

        File createdOutputDir = new File(capture.m_GUI.strOutputDir);
        File createdOutputDirDVD = new File(createdOutputDir, "DVD");
        File createdOutputDirLog = new File(createdOutputDir, "log");
        File createdOutputDirDv = new File(createdOutputDir, "dv");

        assertEquals(outputDir, createdOutputDir);
        assertEquals(outputDirDVD, createdOutputDirDVD);
        assertEquals(outputDirLog, createdOutputDirLog);
        assertEquals(outputDirDv, createdOutputDirDv);

        File[] files = outputDir.listFiles();
        assertEquals(4, files.length);
        assertArrayEquals(new File[] { logFile, errFile, expectedDvgrabFile }, files);
    }

    @Test
    public void testDV_CaptureWithError() throws IOException, InterruptedException {
        when(gui.ErrorCheck(eq("outputDir/log/dvgrab.log"))).thenReturn(true);

        capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        capture.DV_Capture();

        verify(gui).MessageBox(eq("The Output Directory outputDir already exists!"), eq(2));
    }

    @Test
    public void testDV_CaptureNoDirectory() throws IOException, InterruptedException {
        when(gui.ErrorCheck(eq("outputDir/log/dvgrab.log"))).thenReturn(false);
        when(gui.MessageBox(anyString(), anyInt())).thenReturn(0);

        capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        try {
            capture.DV_Capture();
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("/bin/sh: dvgrab: command not found", e.getMessage());
        }

        verify(gui).MessageBox(eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), eq(0));
    }

    @Test
    public void testDV_CaptureWithCameraError() throws IOException, InterruptedException {
        when(gui.ErrorCheck(eq("outputDir/log/dvgrab.log"))).thenReturn(false);
        when(gui.MessageBox(anyString(), anyInt())).thenReturn(0);

        File logFile = mock(File.class);
        when(logFile.exists()).thenReturn(true);
        when(logFile.isFile()).thenReturn(true);
        when(logFile.getName()).thenReturn("dvgrab.log");
        when(gui.strOutputDir).thenReturn("outputDir");

        capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        when(gui.txtAreaOutput).thenReturn(mock(TextArea.class));

        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(logFile.newBufferedReader()).thenReturn(bufferedReader);
        when(bufferedReader.ready()).thenReturn(true);
        when(bufferedReader.readLine()).thenReturn("Error: no camera exists");

        capture.DV_Capture();

        verify(gui).MessageBox(eq("It appears that your camcorder is not connected to your computer!"), eq(0));
    }

    @Test
    public void testDV_CaptureWithFilenameExists() throws IOException, InterruptedException {
        when(gui.ErrorCheck(eq("outputDir/log/dvgrab.log"))).thenReturn(false);
        when(gui.MessageBox(anyString(), anyInt())).thenReturn(0);

        File outputDir = new File("outputDir");
        File outputFile = new File(outputDir, "file.dv");
        when(gui.strOutputDir).thenReturn("outputDir");

        capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        when(gui.txtAreaOutput).thenReturn(mock(TextArea.class));

        File expectedDvgrabFile = new File(outputDir, "dv_file-" + System.currentTimeMillis() + ".dv");

        capture.DV_Capture();

        assertFalse(outputFile.exists());
        assertTrue(expectedDvgrabFile.exists());
    }

    @Test
    public void testDV_CaptureWithFilenameExistsAndDelete() throws IOException, InterruptedException {
        when(gui.ErrorCheck(eq("outputDir/log/dvgrab.log"))).thenReturn(false);
        when(gui.MessageBox(anyString(), anyInt())).thenReturn(0);

        File outputDir = new File("outputDir");
        File outputFile = new File(outputDir, "file.dv");
        when(gui.strOutputDir).thenReturn("outputDir");

        capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        when(gui.txtAreaOutput).thenReturn(mock(TextArea.class));

        File expectedDvgrabFile = new File(outputDir, "dv_file-" + System.currentTimeMillis() + ".dv");

        capture.DV_Capture();

        assertFalse(outputFile.exists());
        assertTrue(expectedDvgrabFile.exists());

        File[] files = outputDir.listFiles();
        assertEquals(2, files.length);
        assertArrayEquals(new File[] { expectedDvgrabFile, outputFile }, files);

        capture.DV_Capture();

        assertTrue(outputFile.delete());
        assertFalse(outputFile.exists());
        assertTrue(expectedDvgrabFile.exists());
    }
}