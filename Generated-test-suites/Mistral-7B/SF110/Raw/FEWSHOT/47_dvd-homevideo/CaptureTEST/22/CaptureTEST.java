package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class CaptureTest {

    Capture capture;
    GUI mockedGUI;

    @Test
    public void testCapture_noThread() {
        mockedGUI = mock(GUI.class);
        capture = new Capture();
        assertFalse(capture.init());
        verify(mockedGUI, never()).MessageBox(anyString(), anyInt());
        verify(mockedGUI, never()).ErrorCheck(anyString());
        verify(mockedGUI, never()).MenuBox(anyInt(), anyString());
        verify(mockedGUI, never()).MessageBox(anyString(), anyInt());
        verify(mockedGUI, never()).blnBegin = false;
    }

    @Test
    public void testCapture_init_newThread() {
        mockedGUI = mock(GUI.class);
        capture = new Capture(5, 10, mockedGUI);
        assertFalse(capture.init());
        verify(mockedGUI).MessageBox(anyString(), anyInt());
        verify(mockedGUI, never()).ErrorCheck(anyString());
        verify(mockedGUI, never()).MenuBox(anyInt(), anyString());
        verify(mockedGUI, never()).MessageBox(anyString(), anyInt());
        verify(mockedGUI).blnBegin = false;
        verify(capture).m_Thread = notNull();
    }

    @Test
    public void testDV_Capture_deleteOutputDir() {
        mockedGUI = mock(GUI.class);
        capture = new Capture();
        doReturn(true).when(mockedGUI).MessageBox(anyString(), anyInt());

        String outputDir = "outputDir";
        File outputDirFile = new File(outputDir);
        when(mockedGUI.strOutputDir).thenReturn(outputDir);

        capture.DV_Capture();

        assertTrue(outputDirFile.exists());
        assertTrue(outputDirFile.isDirectory());
        verify(mockedGUI).MessageBox("The Output Directory " + outputDir + " already exists!" + "\nWould you like to delete it?", 0);
    }

    @Test
    public void testDV_Capture_deleteOutputFile() {
        mockedGUI = mock(GUI.class);
        capture = new Capture();
        doReturn(true).when(mockedGUI).MessageBox(anyString(), anyInt());

        String outputDir = "outputDir";
        File outputDirFile = new File(outputDir);
        when(mockedGUI.strOutputDir).thenReturn(outputDir);

        String fileName = "file.txt";
        File outputFile = new File(outputDir, fileName);
        when(mockedGUI.strOutputDir + "/" + anyString()).thenReturn(outputFile.getAbsolutePath());

        capture.DV_Capture();

        assertTrue(outputFile.exists());
        verify(mockedGUI).MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?", 0);
    }

    @Test
    public void testDV_Capture_createOutputDir() {
        mockedGUI = mock(GUI.class);
        capture = new Capture();
        doReturn(true).when(mockedGUI).MessageBox(anyString(), anyInt());

        String outputDir = "outputDir";
        File outputDirFile = new File(outputDir);
        when(mockedGUI.strOutputDir).thenReturn(outputDir);

        capture.DV_Capture();

        assertTrue(outputDirFile.exists());
        assertTrue(outputDirFile.isDirectory());
        verify(mockedGUI, never()).MessageBox("The Output Directory " + outputDir + " already exists!", 0);
        verify(mockedGUI).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
    }

    @Test
    public void testDV_Capture_createOutputFile() {
        mockedGUI = mock(GUI.class);
        capture = new Capture();
        doReturn(false).when(mockedGUI).MessageBox(anyString(), anyInt());

        String outputDir = "outputDir";
        File outputDirFile = new File(outputDir);
        when(mockedGUI.strOutputDir).thenReturn(outputDir);

        String fileName = "file.txt";
        File outputFile = new File(outputDir, fileName);
        when(mockedGUI.strOutputDir + "/" + anyString()).thenReturn(outputFile.getAbsolutePath());

        capture.DV_Capture();

        assertFalse(outputFile.exists());
        verify(mockedGUI).MessageBox("Please choose a different name for your\ntitle or move the file to another location.", 2);
    }

    @Test
    public void testCapture_run() {
        mockedGUI = mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = mockedGUI;

        // Run method should not be called when init returns false
        assertFalse(capture.init());
        verify(capture, never()).run();

        // Run method should be called when init returns true
        doReturn(true).when(mockedGUI).init();
        capture.init();
        verify(capture).run();
    }
}