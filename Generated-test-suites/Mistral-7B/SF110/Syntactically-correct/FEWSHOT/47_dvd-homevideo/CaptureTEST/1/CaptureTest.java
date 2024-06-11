package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class CaptureTest {

    @Test
    public void testInitWithNoThreadCheck() {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture();
        capture.m_GUI = mockedGUI;

        assertFalse(capture.init());
    }

    @Test
    public void testInitWithThreadCheck() {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture();
        capture.m_GUI = mockedGUI;
        capture.m_Thread = mock(Thread.class);

        when(mockedGUI.menuChkThread.isSelected()).thenReturn(false);

        assertFalse(capture.init());
    }

    @Test
    public void testInitWithNoError() {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture(1, 2, mockedGUI);

        when(mockedGUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(true);

        assertTrue(capture.init());
    }

    @Test
    public void testInitWithError() {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture(1, 2, mockedGUI);

        when(mockedGUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(false);

        assertFalse(capture.init());
    }

    @Test
    public void testRunWithNoError() throws IOException, InterruptedException {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture();
        capture.m_GUI = mockedGUI;

        File outputDir = new File("outputDir");
        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        StringWriter out = new StringWriter();
        when(mockedGUI.txtAreaOutput).thenReturn(out);

        when(mockedGUI.MessageBox(String.contains("outputDir"), 0)).thenReturn(0);
        when(mockedGUI.MessageBox(String.contains("outputDir"), 2)).thenReturn(2);

        capture.run();

        assertTrue(outputDir.mkdirs());
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        assertEquals("", out.toString());
    }

    @Test
    public void testRunWithIOException() throws IOException, InterruptedException {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture();
        capture.m_GUI = mockedGUI;

        File outputDir = new File("outputDir");
        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        StringWriter out = new StringWriter();
        when(mockedGUI.txtAreaOutput).thenReturn(out);

        when(mockedGUI.MessageBox(String.contains("outputDir"), 0)).thenReturn(0);
        when(mockedGUI.MessageBox(String.contains("outputDir"), 2)).thenReturn(2);

        try {
            capture.run();
            fail();
        } catch (IOException ex) {
            assertEquals("Capture Error - IO Error\n", ex.getMessage());
            assertEquals("outputDir", ex.getMessage().split(": ")[1]);
        }

        assertFalse(outputDir.mkdirs());
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        assertEquals("Capture Error - IO Error\n", out.toString());
    }

    @Test
    public void testRunWithInterruptedException() throws IOException, InterruptedException {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture();
        capture.m_GUI = mockedGUI;

        File outputDir = new File("outputDir");
        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        StringWriter out = new StringWriter();
        when(mockedGUI.txtAreaOutput).thenReturn(out);

        when(mockedGUI.MessageBox(String.contains("outputDir"), 0)).thenReturn(0);
        when(mockedGUI.MessageBox(String.contains("outputDir"), 2)).thenReturn(2);

        try {
            capture.run();
            fail();
        } catch (InterruptedException ex) {
            assertEquals("Capture Error - dvd-homevideo thread was interrupted\n", ex.getMessage());
        }

        assertFalse(outputDir.mkdirs());
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        assertEquals("Capture Error - dvd-homevideo thread was interrupted\n", out.toString());
    }

    @Test
    public void testInitWithErrorDeleteFile() throws IOException, InterruptedException {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture();
        capture.m_GUI = mockedGUI;

        File outputDir = new File("outputDir");
        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        when(mockedGUI.MessageBox(String.contains("outputDir"), 0)).thenReturn(0);

        try {
            capture.init();
            fail();
        } catch (IOException ex) {
            assertEquals("Capture Error - IO Error\n", ex.getMessage());
            assertNull(outputDir.exists());
        }
    }

    @Test
    public void testInitWithErrorDeleteDirectory() throws IOException, InterruptedException {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture();
        capture.m_GUI = mockedGUI;

        File outputDir = new File("outputDir/outputDir");
        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        when(mockedGUI.MessageBox(String.contains("outputDir"), 0)).thenReturn(0);

        try {
            capture.init();
            fail();
        } catch (IOException ex) {
            assertEquals("Capture Error - IO Error\n", ex.getMessage());
            assertFalse(outputDir.exists());
        }
    }

    @Test
    public void testInitWithErrorNoOutputDir() throws IOException, InterruptedException {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture();
        capture.m_GUI = mockedGUI;

        when(mockedGUI.strOutputDir).thenReturn("");

        try {
            capture.init();
            fail();
        } catch (IOException ex) {
            assertEquals("Capture Error - Please choose a different name for your title or move the directory to another location.\n", ex.getMessage());
        }
    }

    @Test
    public void testInitWithErrorFileExists() throws IOException, InterruptedException {
        GUI mockedGUI = mock(GUI.class);
        Capture capture = new Capture();
        capture.m_GUI = mockedGUI;

        File outputDir = new File("outputDir/outputDir");
        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        when(mockedGUI.MessageBox(String.contains("outputDir"), 0)).thenReturn(0);

        try {
            capture.init();
            fail();
        } catch (IOException ex) {
            assertEquals("Please choose a different name for your title or move the file to another location.\n", ex.getMessage());
        }
    }

}