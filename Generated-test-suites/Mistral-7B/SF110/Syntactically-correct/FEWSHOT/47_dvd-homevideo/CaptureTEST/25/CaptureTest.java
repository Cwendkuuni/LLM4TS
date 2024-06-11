package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CaptureTest {

    private Capture capture;
    private GUI gui;

    @Before
    public void setup() {
        gui = mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = gui;
    }

    @Test
    public void testInitEmpty() {
        assertFalse(capture.init());
    }

    @Test
    public void testInitWithArguments() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        capture = new Capture(1, 2, gui);
        assertFalse(capture.init());

        File outputDir = new File("output");
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        // Verify that the output directory is created if it doesn't exist
        assertFalse(outputDir.exists());
        assertFalse(capture.init());
        assertFalse(outputDir.exists());

        // Verify that the output directory is not created if it already exists
        outputDir.mkdir();
        when(gui.MessageBox((String) anyString())).thenReturn(0);
        assertFalse(capture.init());
        assertFalse(outputDir.exists());

        // Verify that the output directory is deleted if it already exists and the user confirms
        when(gui.MessageBox((String) anyString(), 0)).thenReturn(0);
        when(gui.MessageBox((String) anyString(), 2)).thenReturn(0);
        outputDir.mkdir();
        capture = new Capture();
        capture.m_GUI = gui;
        capture.m_Thread = mock(Thread.class);
        when(gui.MessageBox((String) anyString())).thenReturn(0);
        when(gui.ErrorCheck((String) anyString())).thenReturn(false);
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(m_Thread.isAlive()).thenReturn(true);
        assertFalse(capture.init());

        // Verify that the output directory is not deleted if the user cancels
        when(gui.MessageBox((String) anyString())).thenReturn(1);
        assertFalse(capture.init());
        assertFalse(outputDir.exists());
    }

    @Test
    public void testRun() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        capture = new Capture(1, 2, gui);

        Thread thread = mock(Thread.class);
        when(thread.isAlive()).thenReturn(true);
        capture.m_Thread = thread;

        when(gui.MessageBox((String) anyString())).thenReturn(0);
        when(gui.ErrorCheck((String) anyString())).thenReturn(false);
        when(gui.strOutputDir).thenReturn("output");

        capture.run();

        // Verify that the output directory is created
        File outputDir = new File("output");
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        // Verify that the output directory and its subdirectories are deleted after the capture is finished
        when(gui.MessageBox((String) anyString())).thenReturn(0);
        capture.m_Error = true;
        capture.run();
        assertTrue(outputDir.deleteRecursively());
    }

    @Test
    public void testDV_Capture() throws IOException, InterruptedException {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        capture = new Capture(1, 2, gui);
        when(gui.MessageBox((String) anyString())).thenReturn(0);
        when(gui.ErrorCheck((String) anyString())).thenReturn(false);
        when(gui.strOutputDir).thenReturn("output");

        // Verify that the output directory is created if it doesn't exist
        File outputDir = new File("output");
        assertFalse(outputDir.exists());
        capture.DV_Capture();
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        // Verify that the output directory is not created if it already exists
        outputDir.mkdir();
        capture.DV_Capture();
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        // Verify that the output directory is deleted if it already exists and the user confirms
        when(gui.MessageBox((String) anyString(), 2)).thenReturn(0);
        outputDir.mkdir();
        capture.DV_Capture();
        assertFalse(outputDir.exists());

        // Verify that the output directory is not deleted if the user cancels
        when(gui.MessageBox((String) anyString(), 2)).thenReturn(1);
        capture.DV_Capture();
        assertTrue(outputDir.exists());

        // Verify that the dvgrab command is executed and the input and error streams are read
        StringWriter inputStreamWriter = new StringWriter();
        StringWriter errorStreamWriter = new StringWriter();
        when(gui.strOutputDir).thenReturn("output");
        when(gui.MessageBox((String) anyString())).thenAnswer(invocation -> 0);
        when(gui.ErrorCheck((String) anyString())).thenAnswer(invocation -> false);
        Process process = mock(Process.class);
        when(Runtime.getRuntime().exec((String[]) anyArray())).thenReturn(process);
        when(process.getInputStream()).thenReturn(new ByteArrayInputStream("dvgrab output".getBytes()));
        when(process.getErrorStream()).thenReturn(new ByteArrayInputStream("dvgrab error".getBytes()));
        when(process.waitFor()).thenReturn(true);
        when(process.destroy()).thenReturn(true);
        capture.DV_Capture();
        assertEquals("dvgrab output\n", capture.m_GUI.txtAreaOutput.getText());
        assertEquals("dvgrab error\n", capture.m_GUI.txtAreaOutput.getText());
    }

    @Test
    public void testInitWithDvgrabNotFound() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        capture = new Capture(1, 2, gui);
        when(gui.MessageBox((String) anyString())).thenReturn(0);
        when(gui.strOutputDir).thenReturn("output");

        // Verify that an error is shown if dvgrab is not found
        when(gui.ErrorCheck((String) anyString())).thenReturn(false);
        when(Runtime.getRuntime().exec((String[]) anyArray())).thenThrow(new IOException("dvgrab not found"));
        capture.init();
        assertTrue(capture.m_Error);
        assertEquals(capture.m_BaseErr + "IO Error\n dvgrab not found\n", capture.m_GUI.txtAreaOutput.getText());
    }

    @Test
    public void testInitWithCameraNotFound() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        capture = new Capture(1, 2, gui);
        when(gui.MessageBox((String) anyString())).thenReturn(0);
        when(gui.strOutputDir).thenReturn("output");
        when(gui.ErrorCheck((String) anyString())).thenReturn(true);

        // Verify that an error is shown if the camera is not found
        when(gui.MessageBox((String) anyString())).thenReturn(0);
        when(Runtime.getRuntime().exec((String[]) anyArray())).thenReturn(mock(Process.class));
        capture.init();
        assertTrue(capture.m_Error);
        assertEquals(capture.m_BaseErr + "Error executing Runtime.getRuntime().exec()\n", capture.m_GUI.txtAreaOutput.getText());
    }

    @After
    public void teardown() {
        capture = null;
        gui = null;
    }

}