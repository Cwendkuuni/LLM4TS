package com.example.capture;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CaptureTest {

    private Capture capture;
    private GUI mockedGUI;

    @Before
    public void setup() {
        mockedGUI = Mockito.mock(GUI.class);
        capture = new Capture(1, 2, mockedGUI);
    }

    @Test
    public void testInit_OutputDirExistsAndIsADirectory() {
        File outputDir = new File("outputDir");
        outputDir.mkdir();

        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        boolean result = capture.init();
        assertTrue(result);

        verify(mockedGUI, times(0)).MessageBox(anyString(), anyInt());
    }

    @Test
    public void testInit_OutputDirExistsButIsNotADirectory() {
        File outputDir = new File("outputDir");
        outputDir.mkdir();
        outputDir.setExecutable(true);

        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        boolean result = capture.init();
        assertFalse(result);

        verify(mockedGUI, times(1)).MessageBox(eq("Please choose a different name for your title or move the directory to another location."), eq(2));
    }

    @Test
    public void testInit_OutputDirDoesNotExist() {
        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        boolean result = capture.init();
        assertFalse(result);

        verify(mockedGUI, times(1)).MessageBox(eq("The Output Directory outputDir already exists!"), eq(0));
    }

    @Test
    public void testInit_dvgrabCommandNotFound() {
        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        String[] dvgrabCmd = {"/bin/sh", "-c", "dvgrab: command not found"};
        Process process = mock(Process.class);

        doThrow(IOException.class).when(process).waitFor();

        when(Runtime.getRuntime().exec(eq("/bin/sh"), anyArray(), any(File.class))).thenReturn(process);

        boolean result = capture.init();
        assertFalse(result);

        verify(mockedGUI, times(1)).MessageBox(eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), eq(0));
    }

    @Test
    public void testDV_Capture_CameraNotConnected() {
        when(mockedGUI.strOutputDir).thenReturn("outputDir");

        String[] dvgrabCmd = {"/bin/sh", "-c", "Error: no camera exists"};
        Process process = mock(Process.class);

        doThrow(IOException.class).when(process).waitFor();

        when(Runtime.getRuntime().exec(eq("/bin/sh"), anyArray(), any(File.class))).thenReturn(process);

        boolean result = capture.init();
        assertFalse(result);

        verify(mockedGUI, times(1)).MessageBox(eq("It appears that your camcorder is not connected to your computer!"), eq(0));
    }
}