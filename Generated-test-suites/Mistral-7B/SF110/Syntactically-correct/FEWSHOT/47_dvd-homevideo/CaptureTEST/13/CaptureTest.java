package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CaptureTest {

    private Capture capture;
    private GUI mockedGUI;
    private File mockedCreateOutDir;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockedGUI = mock(GUI.class);
        mockedCreateOutDir = mock(File.class);
        capture = new Capture();
        capture.m_GUI = mockedGUI;
    }

    @After
    public void tearDown() throws Exception {
        capture = null;
        mockedGUI = null;
        mockedCreateOutDir = null;
    }

    @Test
    public void testInit_OutputDirectoryDoesNotExistAndIsDirectory() {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedCreateOutDir.exists()).thenReturn(false);
        when(mockedCreateOutDir.isDirectory()).thenReturn(true);

        boolean result = capture.init();
        assertTrue(result);
    }

    @Test
    public void testInit_OutputDirectoryExistsAndIsDirectory() {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedCreateOutDir.exists()).thenReturn(true);
        when(mockedCreateOutDir.isDirectory()).thenReturn(true);

        boolean result = capture.init();
        assertFalse(result);
    }

    @Test
    public void testInit_OutputDirectoryExistsAndIsFile() {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedCreateOutDir.exists()).thenReturn(true);
        when(mockedCreateOutDir.isDirectory()).thenReturn(false);

        boolean result = capture.init();
        assertFalse(result);
    }

    @Test
    public void testInit_OutputDirectoryDoesNotExistButIsFile() {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedCreateOutDir.exists()).thenReturn(false);
        when(mockedCreateOutDir.isFile()).thenReturn(true);

        boolean result = capture.init();
        assertFalse(result);
    }

    @Test
    public void testInit_OutputDirectoryIsEmpty() {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedCreateOutDir.exists()).thenReturn(true);
        when(mockedCreateOutDir.isDirectory()).thenReturn(true);
        when(mockedCreateOutDir.list().length).thenReturn(0);

        boolean result = capture.init();
        assertTrue(result);
    }

    @Test
    public void testInit_OutputDirectoryHasFiles() {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedCreateOutDir.exists()).thenReturn(true);
        when(mockedCreateOutDir.isDirectory()).thenReturn(true);
        String[] files = new String[]{"file1.txt", "file2.txt"};
        when(mockedCreateOutDir.list()).thenReturn(files);

        boolean result = capture.init();
        assertFalse(result);
    }

    @Test
    public void testInit_OutputDirectoryHasDirectory() {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedCreateOutDir.exists()).thenReturn(true);
        when(mockedCreateOutDir.isDirectory()).thenReturn(true);
        File mockedSubDir = mock(File.class);
        when(mockedCreateOutDir.listFiles()).thenReturn(new File[] { mockedSubDir });

        boolean result = capture.init();
        assertFalse(result);
    }

    @Test
    public void testInit_MenuCheckThreadIsSelected() {
        when(mockedGUI.menuChkThread.isSelected()).thenReturn(true);

        boolean result = capture.init();
        assertFalse(result);
    }

    @Test
    public void testDV_Capture_IOError() throws IOException, InterruptedException {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedGUI.MessageBox(anyString(), anyInt())).thenReturn(JOptionPane.NO_OPTION);

        String rm = "rm -fr outputDirectory";
        Process rmProcess = mock(Process.class);
        when(Runtime.getRuntime().exec(anyStringArray(), (File[]) anyObject(), any(File.class))).thenReturn(rmProcess);
        rmProcess.waitFor(1000, TimeUnit.MILLISECONDS);
        doThrow(IOException.class).when(rmProcess).destroy();

        try {
            capture = new Capture(1, 1, mockedGUI);
            capture.init();
            fail();
        } catch (Exception e) {
            verify(mockedGUI, times(1)).MessageBox(capture.m_BaseErr + "IO Error", 0);
            verify(mockedGUI, times(1)).strOutputDir;
            verify(mockedGUI, times(1)).MessageBox(eq("Could not remove existing output directory.\nPlease choose a different name for your title or move the directory to another location."), 2);
        }
    }

    @Test
    public void testDV_Capture_InterruptedException() throws IOException, InterruptedException {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedGUI.MessageBox(anyString(), anyInt())).thenReturn(JOptionPane.NO_OPTION);

        String rm = "rm -fr outputDirectory";
        Process rmProcess = mock(Process.class);
        when(Runtime.getRuntime().exec(anyStringArray(), (File[]) anyObject(), any(File.class))).thenReturn(rmProcess);
        rmProcess.waitFor(1000, TimeUnit.MILLISECONDS);
        doThrow(InterruptedException.class).when(rmProcess).destroy();

        try {
            capture = new Capture(1, 1, mockedGUI);
            capture.init();
            fail();
        } catch (Exception e) {
            verify(mockedGUI, times(1)).MessageBox(capture.m_BaseErr + "dvd-homevideo thread was interrupted", 0);
        }
    }

    @Test
    public void testDV_Capture_DvgrabCommandNotFound() throws IOException, InterruptedException {
        when(mockedGUI.strOutputDir).thenReturn("outputDirectory");
        when(mockedGUI.MessageBox(anyString(), anyInt())).thenReturn(JOptionPane.NO_OPTION);

        String dvgrabCmd = "/bin/sh -c dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-";
        Process dvgrabProcess = mock(Process.class);
        when(Runtime.getRuntime().exec(eq("/bin/sh"), (String[]) anyArray(), any(File.class))).thenReturn(dvgrabProcess);
        dvgrabProcess.waitFor(1000, TimeUnit.MILLISECONDS);
        doThrow(IOException.class).when(dvgrabProcess).destroy();

        String errLine = "/bin/sh: dvgrab: command not found";
        when(dvgrabProcess.getErrorStream()).thenReturn(new ByteArrayInputStream(errLine.getBytes()));

        try {
            capture = new Capture(1, 1, mockedGUI);
            capture.init();
            fail();
        } catch (Exception e) {
            verify(mockedGUI, times(1)).MessageBox(capture.m_BaseErr + "Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0);
        }
    }
}