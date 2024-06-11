package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class CaptureTest {

    private Capture capture;
    private GUI gui;
    private void initMocks() {
        gui = mock(GUI.class);
    }

    @Before
    public void setup() {
        initMocks();
        capture = new Capture();
        capture.m_GUI = gui;
    }

    @Test
    public void testCaptureEmptyConstructor() {
        assertTrue(capture.m_Error == false);
        assertEquals(capture.m_dvgrab, "dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-");
        assertNull(capture.m_Thread);
        assertNull(capture.m_GUI);
    }

    @Test
    public void testCaptureWithArgumentsConstructor() {
        int min = 5;
        int sec = 10;
        initMocks();
        capture = new Capture(min, sec, gui);
        assertEquals(min, capture.m_Minutes);
        assertEquals(sec, capture.m_Seconds);
        assertTrue(capture.m_Error == false);
        assertEquals(capture.m_dvgrab, "dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-");
        assertNotNull(capture.m_Thread);
        assertNotNull(capture.m_GUI);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullGUI() {
        capture = new Capture();
        capture.m_GUI = null;
        capture.init();
    }

    @Test
    public void testInitWithThreadSelected() {
        initMocks();
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        capture = new Capture();
        capture.m_GUI = gui;
        assertFalse(capture.init());
        assertNotNull(capture.m_Thread);
    }

    @Test
    public void testInitWithoutThreadSelected() {
        initMocks();
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        capture = new Capture();
        capture.m_GUI = gui;
        assertFalse(capture.init());
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithOutputDirectoryAlreadyExistsAndDeleted() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();

        capture = new Capture();
        capture.m_GUI = gui;
        assertFalse(capture.init());
        assertTrue(outputDirFile.delete());
        assertTrue(capture.init());
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithOutputDirectoryAlreadyExistsAndNotDeleted() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();

        capture = new Capture();
        capture.m_GUI = gui;
        assertFalse(capture.init());
        assertFalse(outputDirFile.delete());
        assertTrue(capture.init());
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithOutputFileAlreadyExistsAndDeleted() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        String fileName = "file.txt";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();
        File file = new File(outputDir + "/" + fileName);
        file.createNewFile();

        capture = new Capture();
        capture.m_GUI = gui;
        assertFalse(capture.init());
        assertTrue(file.delete());
        assertTrue(capture.init());
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithOutputFileAlreadyExistsAndNotDeleted() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        String fileName = "file.txt";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();
        File file = new File(outputDir + "/" + fileName);
        file.createNewFile();

        capture = new Capture();
        capture.m_GUI = gui;
        assertFalse(capture.init());
        assertFalse(file.delete());
        assertTrue(capture.init());
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithOutputDirectoryDoesNotExist() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);

        capture = new Capture();
        capture.m_GUI = gui;
        assertTrue(outputDirFile.mkdir());
        assertTrue(capture.init());
        assertNotNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithRuntimeException() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();

        capture = new Capture();
        capture.m_GUI = gui;
        when(gui.MessageBox(String.format("The Output Directory %s already exists!%nWould you like to delete it?", outputDir))).thenReturn(1);
        try {
            capture.DV_Capture();
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            // expected
        }
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithIOException() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();

        capture = new Capture();
        capture.m_GUI = gui;
        when(gui.MessageBox(String.format("The Output Directory %s already exists!%nWould you like to delete it?", outputDir))).thenReturn(1);
        when(gui.MessageBox("Please choose a different name for your title or move the file to another location.", 2)).thenReturn(1);
        try {
            capture.DV_Capture();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithInterruptedException() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();

        capture = new Capture();
        capture.m_GUI = gui;
        when(gui.MessageBox(String.format("The Output Directory %s already exists!%nWould you like to delete it?", outputDir))).thenReturn(1);
        when(gui.MessageBox("Please choose a different name for your title or move the file to another location.", 2)).thenReturn(1);
        when(gui.MessageBox("It appears that your camcorder is not connected to your computer!", 0)).thenReturn(1);

        try {
            capture.DV_Capture();
            fail("InterruptedException expected");
        } catch (InterruptedException e) {
            // expected
        }
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithRuntimeExceptionAndIOException() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();

        capture = new Capture();
        capture.m_GUI = gui;
        when(gui.MessageBox(String.format("The Output Directory %s already exists!%nWould you like to delete it?", outputDir))).thenReturn(1);
        when(gui.MessageBox("Please choose a different name for your title or move the file to another location.", 2)).thenReturn(1);
        when(gui.MessageBox("It appears that your camcorder is not connected to your computer!", 0)).thenReturn(1);
        when(gui.MessageBox(String.format("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), 0)).thenReturn(1);

        try {
            capture.DV_Capture();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }

        try {
            capture.DV_Capture();
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            // expected
        }
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithIOExceptionAndInterruptedException() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();

        capture = new Capture();
        capture.m_GUI = gui;
        when(gui.MessageBox(String.format("The Output Directory %s already exists!%nWould you like to delete it?", outputDir))).thenReturn(1);
        when(gui.MessageBox("Please choose a different name for your title or move the file to another location.", 2)).thenReturn(1);
        when(gui.MessageBox("It appears that your camcorder is not connected to your computer!", 0)).thenReturn(1);
        when(gui.MessageBox(String.format("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), 0)).thenReturn(1);
        when(gui.MessageBox("Error: no camera exists", 0)).thenReturn(1);

        try {
            capture.DV_Capture();
            fail("InterruptedException expected");
        } catch (InterruptedException e) {
            // expected
        }
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_CaptureWithRuntimeExceptionAndInterruptedException() throws IOException, InterruptedException {
        String outputDir = "outputDir";
        initMocks();
        when(gui.strOutputDir).thenReturn(outputDir);
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdir();

        capture = new Capture();
        capture.m_GUI = gui;
        when(gui.MessageBox(String.format("The Output Directory %s already exists!%nWould you like to delete it?", outputDir))).thenReturn(1);
        when(gui.MessageBox("Please choose a different name for your title or move the file to another location.", 2)).thenReturn(1);
        when(gui.MessageBox("It appears that your camcorder is not connected to your computer!", 0)).thenReturn(1);
        when(gui.MessageBox(String.format("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), 0)).thenReturn(1);
        when(gui.MessageBox("It appears that your camcorder is not connected to your computer!", 0)).thenReturn(1);

        try {
            capture.DV_Capture();
            fail("InterruptedException expected");
        } catch (InterruptedException e) {
            // expected
        }

        try {
            capture.DV_Capture();
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            // expected
        }
        assertNull(capture.m_Thread);
    }
}