package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class CaptureTest {

    private File outputDir;
    private GUI gui;
    private Capture capture;

    @Before
    public void setUp() {
        outputDir = new File("testOutputDir");
        gui = Mockito.mock(GUI.class);
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(gui.MessageBox(anyString(), anyInt())).thenReturn(0);
        capture = new Capture(0, 0, gui);
    }

    @After
    public void tearDown() {
        if (outputDir.exists() && outputDir.isDirectory()) {
            FileUtils.deleteDirectory(outputDir);
        }
    }

    @Test
    public void testInit_OutputDirExistsAndIsDirectory() {
        File outputDir = new File("testOutputDir");
        outputDir.mkdirs();

        Capture capture = new Capture(0, 0, gui);

        assertTrue(capture.init());

        verify(gui).MessageBox(argThat(s -> s.contains("The Output Directory")), 2);
        verifyZeroInteractions(gui).MessageBox(argThat(s -> s.contains("Please choose a different name for your title or move the directory to another location.")));
    }

    @Test
    public void testInit_OutputDirExistsButIsNotADirectory() {
        File outputDir = new File("testOutputDir");
        outputDir.mkdir();

        Capture capture = new Capture(0, 0, gui);

        assertFalse(capture.init());

        verify(gui).MessageBox(argThat(s -> s.contains("There exists a file with the Output directory name")), 2);
        verifyZeroInteractions(gui).MessageBox(argThat(s -> s.contains("Please choose a different name for your title or move the file to another location.")));
    }

    @Test
    public void testInit_OutputDirIsFile() {
        File outputFile = new File("testOutputDir");

        Capture capture = new Capture(0, 0, gui);

        assertFalse(capture.init());

        verify(gui).MessageBox(argThat(s -> s.contains("There exists a file with the Output directory name")), 2);
        verifyZeroInteractions(gui).MessageBox(argThat(s -> s.contains("Please choose a different name for your title or move the file to another location.")));
    }

    @Test
    public void testDV_Capture_dvgrabCommandNotFound() {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab_not_found";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        doThrow(IOException.class).when(Runtime.getRuntime()).exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull());

        Capture capture = new Capture(0, 0, gui);

        assertFalse(capture.init());
        verify(gui).MessageBox(argThat(s -> s.contains("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo")), 0);
    }

    @Test
    public void testDV_Capture_dvgrabCommandExecutedSuccessfully() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);

        Capture capture = new Capture(0, 0, gui);

        capture.init();

        verify(gui).MessageBox(argThat(s -> s.isEmpty()), 0);
        verifyZeroInteractions(gui).MessageBox(argThat(s -> s.contains("It appears that your camcorder is not connected to your computer!")));
        verify(process).destroy();
    }

    @Test
    public void testDV_Capture_dvgrabCommandExecutionFailed() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);
        when(process.waitFor(ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong())).thenThrow(new InterruptedException());

        Capture capture = new Capture(0, 0, gui);

        capture.init();

        verify(gui).MessageBox(argThat(s -> s.contains("dvd-homevideo thread was interrupted")), 0);
    }

    @Test
    public void testDV_Capture_dvgrabCommandExecutionFailedWithIOError() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);
        when(process.getErrorStream()).thenThrow(new IOException("Test IO Error"));

        Capture capture = new Capture(0, 0, gui);

        capture.init();

        verify(gui).MessageBox(argThat(s -> s.contains("IO Error")), 0);
    }

    @Test
    public void testDV_Capture_dvgrabCommandExecutionFailedWithNullPointerException() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);
        when(process.getInputStream()).thenThrow(new NullPointerException());

        Capture capture = new Capture(0, 0, gui);

        capture.init();

        verify(gui).MessageBox(argThat(s -> s.contains("Error executing Runtime.getRuntime().exec()")), 0);
    }

    @Test
    public void testDV_Capture_dvgrabCommandExecutionFailedWithIllegalArgumentException() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);
        when(process.getInputStream()).thenThrow(new IllegalArgumentException("Test IllegalArgument Exception"));

        Capture capture = new Capture(0, 0, gui);

        capture.init();

        verify(gui).MessageBox(argThat(s -> s.contains("Illegal argument sent to Runtime.getRuntime().exec()")), 0);
    }

    @Test
    public void testDV_Capture_dvgrabCommandExecutionFailedWithUnknownException() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);
        when(process.getInputStream()).thenThrow(new RuntimeException("Test Unknown Exception"));

        Capture capture = new Capture(0, 0, gui);

        capture.init();

        verify(gui).MessageBox(argThat(s -> s.contains("Unknown Error occurred")), 0);
    }

    @Test
    public void testDV_Capture_dvgrabCommandExecutionSuccessfullyWithCaptions() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab --subtitle";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);
        when(process.waitFor(ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong())).thenReturn(0);

        Capture capture = new Capture(0, 0, gui);

        capture.init();

        verifyZeroInteractions(gui).MessageBox(ArgumentMatchers.anyString());
    }

    @Test
    public void testDV_Capture_dvgrabCommandExecutionSuccessfullyWithNoCaptions() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);
        when(process.waitFor(ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong())).thenReturn(0);

        Capture capture = new Capture(0, 0, gui);

        capture.init();

        verifyZeroInteractions(gui).MessageBox(ArgumentMatchers.anyString());
    }

    @Test
    public void testRun_DV_Capture_ExecutedSuccessfully() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);
        when(process.waitFor(ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong())).thenReturn(0);

        Capture capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        capture.run();

        verifyZeroInteractions(gui).MessageBox(ArgumentMatchers.anyString());
    }

    @Test
    public void testRun_DV_Capture_ExecutedWithThreadInterruptedException() throws IOException, InterruptedException {
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        String dvgrabCommand = "dvgrab";
        String[] dvgrabCmd = { "/bin/sh", "-c", dvgrabCommand };
        Process process = mock(Process.class);
        when(Runtime.getRuntime()).thenReturn(mock(Runtime.class));
        when(Runtime.getRuntime().exec(ArgumentMatchers.arrayEq(dvgrabCmd), ArgumentMatchers.isNull(), ArgumentMatchers.isNull())).thenReturn(process);
        when(process.waitFor(ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong())).thenThrow(new InterruptedException());

        Capture capture = new Capture();
        capture.m_Minutes = 1;
        capture.m_Seconds = 2;
        capture.m_GUI = gui;

        capture.run();

        verify(gui).MessageBox(argThat(s -> s.contains("dvd-homevideo thread was interrupted")), 0);
    }
}