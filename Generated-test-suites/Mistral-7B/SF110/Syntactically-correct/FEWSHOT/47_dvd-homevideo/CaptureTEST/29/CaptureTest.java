package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

public class CaptureTest {

    private Capture capture;
    private GUI guiMock;

    @Before
    public void setUp() {
        guiMock = mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = guiMock;
    }

    @Test
    public void testInit_noThreadCheckboxSelected() {
        when(guiMock.menuChkThread.isSelected()).thenReturn(false);
        assertFalse(capture.init());
    }

    @Test
    public void testInit_threadCheckboxSelected() {
        when(guiMock.menuChkThread.isSelected()).thenReturn(true);
        Thread thread = mock(Thread.class);
        capture.m_Thread = thread;
        when(thread.isAlive()).thenReturn(true);
        assertFalse(capture.init());
    }

    @Test(expected = NullPointerException.class)
    public void testRun_noGUI() {
        capture = new Capture();
        capture.run();
    }

    @Test
    public void testRun_threadAlive() {
        Capture capture = new Capture(1, 1, guiMock);
        Thread thread = mock(Thread.class);
        capture.m_Thread = thread;
        when(thread.isAlive()).thenReturn(true);
        capture.run();
        verify(guiMock).MessageBox("The Output Directory already exists!\nWould you like to delete it?", 0);
    }

    @Test
    public void testRun_threadNotAlive() {
        Capture capture = new Capture(1, 1, guiMock);
        Thread thread = mock(Thread.class);
        capture.m_Thread = thread;
        when(thread.isAlive()).thenReturn(false);
        capture.run();
        verify(guiMock).MessageBox("Please choose a different name for your title or move the directory to another location.", 2);
    }

    @Test
    public void testDV_Capture_outputDirectoryExistsAndIsADirectory() throws IOException, InterruptedException {
        File outputDir = new File("outputDir");
        when(guiMock.strOutputDir).thenReturn("outputDir");
        Capture capture = new Capture(1, 1, guiMock);
        capture.DV_Capture();
        verify(guiMock).MessageBox("The Output Directory already exists!\nWould you like to delete it?", 0);
        outputDir.mkdir();
        capture.DV_Capture();
        verify(guiMock).MessageBox("Please choose a different name for your title or move the directory to another location.", 2);
    }

    @Test
    public void testDV_Capture_outputDirectoryExistsAndIsFile() throws IOException, InterruptedException {
        File outputDir = new File("outputDir/outputFile.txt");
        when(guiMock.strOutputDir).thenReturn("outputDir");
        Capture capture = new Capture(1, 1, guiMock);
        capture.DV_Capture();
        verify(guiMock).MessageBox("There exists a file with the Output directory name at this location!  Would you like to delete it?", 0);
        outputDir.createNewFile();
        capture.DV_Capture();
        verify(guiMock).MessageBox("Please choose a different name for your title or move the file to another location.", 2);
    }

    @Test
    public void testDV_Capture_outputDirectoryDoesNotExist() throws IOException, InterruptedException {
        File outputDir = new File("outputDir");
        when(guiMock.strOutputDir).thenReturn("outputDir");
        Capture capture = new Capture(1, 1, guiMock);
        capture.DV_Capture();
        verify(guiMock, never()).MessageBox("The Output Directory already exists!\nWould you like to delete it?", 0);
        verify(guiMock, never()).MessageBox("There exists a file with the Output directory name at this location!  Would you like to delete it?", 0);
        outputDir.mkdir();
        capture.DV_Capture();
        verify(guiMock).MessageBox("Please choose a different name for your title or move the directory to another location.", 2);
    }

    @Test
    public void testDV_Capture_dvgrabCommandNotFound() throws IOException, InterruptedException {
        when(guiMock.strOutputDir).thenReturn("outputDir");
        Capture capture = new Capture(1, 1, guiMock);
        capture.DV_Capture();
        verify(guiMock).MessageBox("Could not locate dvgrab in your path.\nPlease install all necessary dependencies and rerun dvd-homevideo.", 0);
    }

    @Test
    public void testDV_Capture_cameraNotConnected() throws IOException, InterruptedException {
        when(guiMock.strOutputDir).thenReturn("outputDir");
        BufferedReader errorIn = mock(BufferedReader.class);
        when(errorIn.ready()).thenReturn(true);
        when(errorIn.readLine()).thenReturn("Error: no camera exists");
        InputStream errorStream = new ByteArrayInputStream("Error: no camera exists\n".getBytes());
        BufferedReader input = new BufferedReader(new InputStreamReader(errorStream));
        when(guiMock.strOutputDir + "/log/dvgrab.err").thenReturn(new File("outputDir/log/dvgrab.err"));
        when(guiMock.strOutputDir + "/log/dvgrab.log").thenReturn(new File("outputDir/log/dvgrab.log"));
        Capture capture = new Capture(1, 1, guiMock);
        capture.DV_Capture();
        verify(guiMock).MessageBox("It appears that your camcorder is not connected to your computer!", 0);
        input.close();
        errorIn.close();
    }
}