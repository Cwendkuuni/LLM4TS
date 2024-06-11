package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CaptureTest {

    @Mock
    private GUI m_GUI;

    private Capture capture;

    @Before
    public void setUp() {
        capture = new Capture();
        when(m_GUI.MessageBox(anyString(), eq(0))).thenReturn(0);
        when(m_GUI.MessageBox(anyString(), eq(1))).thenReturn(1);
        when(m_GUI.MenuIsChecked(anyString())).thenReturn(false);
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(Paths.get("test_output_dir"))) {
            Files.deleteRecursively(Paths.get("test_output_dir"));
        }
    }

    @Test
    public void testInitWithNoThreadCheck() {
        assertFalse(capture.init());
        verify(m_GUI, times(0)).ErrorCheck(anyString());
    }

    @Test
    public void testInitWithThreadCheck() {
        when(m_GUI.MenuIsChecked("menuChkThread")).thenReturn(true);
        assertFalse(capture.init());
        verify(m_GUI, times(1)).ErrorCheck(anyString());
    }

    @Test
    public void testDV_CaptureWithOutputDirAlreadyExistsAsDirectory() {
        File testOutputDir = new File("test_output_dir");
        testOutputDir.mkdir();

        capture = new Capture("", "", m_GUI);
        capture.m_GUI = m_GUI;
        capture.m_GUI.strOutputDir = "test_output_dir";

        capture.DV_Capture();

        assertTrue(testOutputDir.exists());
        assertTrue(testOutputDir.isDirectory());
    }

    @Test
    public void testDV_CaptureWithOutputDirAlreadyExistsAsFile() {
        File testOutputDir = new File("test_output_dir");
        testOutputDir.mkdir();
        File outputFile = new File(testOutputDir, "test_output_file");
        outputFile.createNewFile();

        capture = new Capture("", "", m_GUI);
        capture.m_GUI = m_GUI;
        capture.m_GUI.strOutputDir = "test_output_dir";

        capture.DV_Capture();

        assertTrue(testOutputDir.exists());
        assertTrue(testOutputDir.isDirectory());
        assertFalse(outputFile.exists());
    }

    @Test
    public void testDV_CaptureWithOutputDirDoesNotExist() {
        File testOutputDir = new File("test_output_dir");

        capture = new Capture("", "", m_GUI);
        capture.m_GUI = m_GUITest.m_GUI;
        capture.m_GUI.strOutputDir = "test_output_dir";

        capture.DV_Capture();

        assertTrue(testOutputDir.mkdirs());
    }

    @Test(expected = IOException.class)
    public void testDV_CaptureWithIOException() throws IOException {
        File testOutputDir = new File("test_output_dir");
        testOutputDir.mkdirs();

        capture = new Capture("", "", m_GUI);
        capture.m_GUI = m_GUI;
        capture.m_GUI.strOutputDir = "test_output_dir";

        doThrow(IOException.class).when(m_GUI).MessageBox(anyString(), eq(0));

        capture.DV_Capture();
    }

    @Test(expected = InterruptedException.class)
    public void testDV_CaptureWithInterruptedException() throws IOException, InterruptedException {
        File testOutputDir = new File("test_output_dir");
        testOutputDir.mkdirs();

        capture = new Capture("", "", m_GUI);
        capture.m_GUI = m_GUI;
        capture.m_GUI.strOutputDir = "test_output_dir";

        doThrow(InterruptedException.class).when(m_GUI).MessageBox(anyString(), eq(0));

        capture.DV_Capture();
    }

    @Test
    public void testDV_CaptureWithNoCameraFound() throws IOException {
        File testOutputDir = new File("test_output_dir");
        testOutputDir.mkdirs();

        capture = new Capture("", "", m_GUI);
        capture.m_GUI = m_GUI;
        capture.m_GUI.strOutputDir = "test_output_dir";

        doThrow(IOException.class).when(m_GUI).MessageBox(eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), eq(0));

        capture.DV_Capture();

        assertTrue(testOutputDir.exists());
        assertTrue(testOutputDir.isDirectory());
        assertTrue(Files.isEmpty(Paths.get(testOutputDir.getPath(), "log", "dvgrab.log")));
        assertTrue(Files.isEmpty(Paths.get(testOutputDir.getPath(), "log", "dvgrab.err")));
    }

    @Test
    public void testDV_CaptureWithErrorDuringExecution() throws IOException, InterruptedException {
        File testOutputDir = new File("test_output_dir");
        testOutputDir.mkdirs();

        capture = new Capture("", "", m_GUI);
        capture.m_GUI = m_GUI;
        capture.m_GUI.strOutputDir = "test_output_dir";

        doThrow(IOException.class).when(m_GUI).MessageBox(anyString(), eq(0));

        capture.DV_Capture();

        assertTrue(testOutputDir.exists());
        assertTrue(testOutputDir.isDirectory());
        assertTrue(Files.isEmpty(Paths.get(testOutputDir.getPath(), "log", "dvgrab.log")));
        assertTrue(Files.isEmpty(Paths.get(testOutputDir.getPath(), "log", "dvgrab.err")));
    }

}