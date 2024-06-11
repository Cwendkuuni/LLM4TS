package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CaptureTest {

    private Capture capture;
    private GUI gui;

    @Before
    public void setUp() {
        gui = mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = gui;
    }

    @Test
    public void testInit_NoThreadCheckboxSelected() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        assertFalse(capture.init());
    }

    @Test
    public void testInit_ThreadCheckboxSelected() {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        Thread thread = mock(Thread.class);
        capture.m_Thread = thread;
        when(thread.isAlive()).thenReturn(true);
        when(gui.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(true);
        when(gui.MessageBox(String.valueOf(capture.m_BaseErr + "The Output Directory already exists!" + "\nWould you like to delete it?"))).thenReturn(0);
        when(gui.MessageBox("Please choose a different name for your title or move the directory to another location.", 2)).thenReturn(0);
        when(gui.MessageBox("There exists a file with the Output directory name at this location!  Would you like to delete it?", 2)).thenReturn(0);

        assertTrue(capture.init());
    }

    @Test
    public void testInit_ThreadAlreadyRunning() {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        Thread thread = mock(Thread.class);
        capture.m_Thread = thread;
        when(thread.isAlive()).thenReturn(true);

        assertFalse(capture.init());
    }

    @Test
    public void testInit_ErrorCheckReturnsFalse() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(gui.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(false);

        assertFalse(capture.init());
    }

    @Test
    public void testRun() throws IOException, InterruptedException {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        Thread thread = mock(Thread.class);
        capture.m_Thread = thread;
        when(thread.isAlive()).thenReturn(true);
        when(gui.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(true);

        capture.run();

        verify(gui).MessageBox("The Output Directory already exists!", 1);
        verify(gui).MessageBox("Please choose a different name for your title or move the directory to another location.", 2);
        verify(gui).MessageBox("There exists a file with the Output directory name at this location!  Would you like to delete it?", 2);
        verify(gui).MessageBox("Could not locate dvgrab in your path.\nPlease install all necessary dependencies and rerun dvd-homevideo.", 0);
        verify(gui).MessageBox("It appears that your camcorder is not connected to your computer!", 0);
        verify(gui).MessageBox("Error executing Runtime.getRuntime().exec()", 0);
        verify(gui).MessageBox("Illegal argument sent to Runtime.getRuntime().exec()", 0);
        verify(gui).MessageBox("Unknown Error occurred", 0);
    }
}