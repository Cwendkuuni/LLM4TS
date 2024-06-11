package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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
    public void testInit_noThreadCheckboxSelected_doesNotStartThread() {
        when(guiMock.menuChkThread.isSelected()).thenReturn(false);

        assertFalse(capture.init());

        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
    }

    @Test
    public void testInit_threadCheckboxSelected_startsThread() {
        when(guiMock.menuChkThread.isSelected()).thenReturn(true);

        capture.m_Thread = mock(Thread.class);

        assertFalse(capture.init());

        verify(guiMock).MessageBox("The Output Directory already exists!\nWould you like to delete it?", 0);
        verify(guiMock).MessageBox("Please choose a different name for your title or move the directory to another location.", 2);
        verify(guiMock).MessageBox("Could not locate dvgrab in your path!\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0);
        verify(guiMock).MessageBox("It appears that your camcorder is not connected to your computer!", 0);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(guiMock).Thread(runnableCaptor.capture());
        assertEquals(capture, runnableCaptor.getValue());
        verify(capture.m_Thread).start();
    }

    @Test
    public void testRun_dvgrabIsExecuted() throws IOException, InterruptedException {
        String workingDirectory = System.getProperty("user.dir");
        when(guiMock.strOutputDir).thenReturn(workingDirectory);

        Process processMock = mock(Process.class);

        Runtime runtimeMock = mock(Runtime.class);
        when(runtimeMock.exec(anyStringArray(), null, any(File.class))).thenReturn(processMock);

        capture.run();

        verify(runtimeMock).exec(eq("/bin/sh"), eq("-c"), anyStringArray());

        BufferedReader inputMock = mock(BufferedReader.class);
        BufferedReader errorInputMock = mock(BufferedReader.class);
        BufferedWriter outputMock = mock(BufferedWriter.class);
        BufferedWriter errorOutputMock = mock(BufferedWriter.class);

        when(processMock.getInputStream()).thenReturn(new InputStreamReader(inputMock.mockConstConstructor().mock()));
        when(processMock.getErrorStream()).thenReturn(new InputStreamReader(errorInputMock.mockConstConstructor().mock()));

        capture.DV_Capture();

        verify(inputMock).close();
        verify(outputMock).close();
        verify(errorInputMock).close();
        verify(errorOutputMock).close();
    }

    @Test(expected = IOException.class)
    public void testRun_ioErrorThrownDuringDV_Capture() throws IOException, InterruptedException {
        String workingDirectory = System.getProperty("user.dir");
        when(guiMock.strOutputDir).thenReturn(workingDirectory);

        Process processMock = mock(Process.class);
        when(processMock.waitFor(anyLong(), anyLong())).thenThrow(new IOException());

        Runtime runtimeMock = mock(Runtime.class);
        when(runtimeMock.exec(anyStringArray(), null, any(File.class))).thenReturn(processMock);

        capture.run();

        verify(runtimeMock).exec(eq("/bin/sh"), eq("-c"), anyStringArray());
    }

    @Test(expected = InterruptedException.class)
    public void testRun_interruptedExceptionThrownDuringDV_Capture() throws IOException, InterruptedException {
        String workingDirectory = System.getProperty("user.dir");
        when(guiMock.strOutputDir).thenReturn(workingDirectory);

        Process processMock = mock(Process.class);
        when(processMock.waitFor(anyLong(), anyLong())).thenThrow(new InterruptedException());

        Runtime runtimeMock = mock(Runtime.class);
        when(runtimeMock.exec(anyStringArray(), null, any(File.class))).thenReturn(processMock);

        capture.run();

        verify(runtimeMock).exec(eq("/bin/sh"), eq("-c"), anyStringArray());
    }

    @Test
    public void testDV_Capture_outputDirectoryDoesNotExist() throws IOException, InterruptedException {
        String workingDirectory = System.getProperty("user.dir");
        when(guiMock.strOutputDir).thenReturn(workingDirectory);

        Process processMock = mock(Process.class);
        when(processMock.waitFor(anyLong(), anyLong())).thenReturn(0);

        Runtime runtimeMock = mock(Runtime.class);
        when(runtimeMock.exec(anyStringArray(), null, any(File.class))).thenReturn(processMock);

        File outputDirectoryMock = mock(File.class);
        when(outputDirectoryMock.exists()).thenReturn(false);

        capture.DV_Capture();

        verify(guiMock).MessageBox("The Output Directory already exists!\nWould you like to delete it?", 0);
    }

    @Test
    public void testDV_Capture_outputDirectoryIsADirectory() throws IOException, InterruptedException {
        String workingDirectory = System.getProperty("user.dir");
        when(guiMock.strOutputDir).thenReturn(workingDirectory);

        Process processMock = mock(Process.class);
        when(processMock.waitFor(anyLong(), anyLong())).thenReturn(0);

        Runtime runtimeMock = mock(Runtime.class);
        when(runtimeMock.exec(anyStringArray(), null, any(File.class))).thenReturn(processMock);

        File outputDirectoryMock = mock(File.class);
        when(outputDirectoryMock.exists()).thenReturn(true);
        when(outputDirectoryMock.isDirectory()).thenReturn(true);

        capture.DV_Capture();

        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
        verify(guiMock, never()).MessageBox(anyString(), anyInt());
    }

    @Test
    public void testDV_Capture_outputDirectoryIsNotADirectory() throws IOException, InterruptedException {
        String workingDirectory = System.getProperty("user.dir");
        when(guiMock.strOutputDir).thenReturn(workingDirectory);

        Process processMock = mock(Process.class);
        when(processMock.waitFor(anyLong(), anyLong())).thenReturn(0);

        Runtime runtimeMock = mock(Runtime.class);
        when(runtimeMock.exec(anyStringArray(), null, any(File.class))).thenReturn(processMock);

        File outputDirectoryMock = mock(File.class);
        when(outputDirectoryMock.exists()).thenReturn(true);
        when(outputDirectoryMock.isDirectory()).thenReturn(false);

        capture.DV_Capture();

        verify(guiMock).MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?", 0);
    }

    @Test
    public void testDV_Capture_initializesOutputDirectories() throws IOException, InterruptedException {
        String workingDirectory = System.getProperty("user.dir");
        when(guiMock.strOutputDir).thenReturn(workingDirectory);

        Process processMock = mock(Process.class);
        when(processMock.waitFor(anyLong(), anyLong())).thenReturn(0);

        Runtime runtimeMock = mock(Runtime.class);
        when(runtimeMock.exec(anyStringArray(), null, any(File.class))).thenReturn(processMock);

        capture.DV_Capture();

        File outputDirectoryMock = new File(workingDirectory + "/DVD");
        File logOutputDirectoryMock = new File(workingDirectory + "/log");
        File dvOutputDirectoryMock = new File(workingDirectory + "/dv");

        verify(guiMock).MessageBox("Please choose a different name for your title or move the title to another location.", 2);

        assertFalse(outputDirectoryMock.mkdir());
        assertTrue(outputDirectoryMock.mkdir());
        assertTrue(outputDirectoryMock.isDirectory());

        assertFalse(logOutputDirectoryMock.mkdir());
        assertTrue(logOutputDirectoryMock.mkdir());
        assertTrue(logOutputDirectoryMock.isDirectory());

        assertFalse(dvOutputDirectoryMock.mkdir());
        assertTrue(dvOutputDirectoryMock.mkdir());
        assertTrue(dvOutputDirectoryMock.isDirectory());
    }

}