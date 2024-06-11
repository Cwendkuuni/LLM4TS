package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CaptureTest {

    private Capture capture;
    private GUI gui;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.gui = mock(GUI.class);
        this.capture = new Capture(1, 2, this.gui);
    }

    @Test
    public void testInit_OutputDirExistsAndIsDirectory_CreatesNewOutputDir() {
        File outputDir = new File("outputDir");
        outputDir.mkdir();
        when(this.gui.strOutputDir).thenReturn("outputDir");

        boolean result = this.capture.init();
        assertTrue(result);

        File outputDirExpected = new File("outputDir/DVD");
        assertTrue(outputDirExpected.mkdir());
        File outputDirLog = new File("outputDir/log");
        assertTrue(outputDirLog.mkdir());
        File outputDirDv = new File("outputDir/dv");
        assertTrue(outputDirDv.mkdir());
    }

    @Test
    public void testInit_OutputDirExistsAndIsFile_DeletesOutputFile() {
        File outputDir = new File("outputDir");
        outputDir.mkdir();
        File outputFile = new File("outputDir/outputFile.txt");
        outputFile.createNewFile();
        when(this.gui.strOutputDir).thenReturn("outputDir");

        boolean result = this.capture.init();
        assertTrue(result);

        verify(this.gui, times(1)).MessageBox("Please choose a different name for your\ntitle or move the file to another location.", 2);
    }

    @Test
    public void testInit_OutputDirDoesNotExist_CreatesNewOutputDir() {
        when(this.gui.strOutputDir).thenReturn("outputDir");

        boolean result = this.capture.init();
        assertTrue(result);

        File outputDirExpected = new File("outputDir");
        assertTrue(outputDirExpected.mkdir());
        File outputDirDVD = new File("outputDir/DVD");
        assertTrue(outputDirDVD.mkdir());
        File outputDirLog = new File("outputDir/log");
        assertTrue(outputDirLog.mkdir());
        File outputDirDv = new File("outputDir/dv");
        assertTrue(outputDirDv.mkdir());
    }

    @Test
    public void testInit_ThreadEnabled_StartsCaptureThread() {
        when(this.gui.menuChkThread.isSelected()).thenReturn(true);

        boolean result = this.capture.init();
        assertFalse(result);

        verify(this.capture, times(1)).setPriority(10);
        verify(this.capture, times(1)).start();
    }

    @Test
    public void testInit_ThreadDisabled_DoesNotStartCaptureThread() {
        when(this.gui.menuChkThread.isSelected()).thenReturn(false);

        boolean result = this.capture.init();
        this.capture.DV_Capture();

        verify(this.capture, never()).setPriority(10);
        verify(this.capture, never()).start();
    }

    @Test
    public void testDV_Capture_OutputDirExists_RunsCaptureCommand() throws IOException, InterruptedException {
        File outputDir = new File("outputDir");
        outputDir.mkdir();
        when(this.gui.strOutputDir).thenReturn("outputDir");

        this.capture.init();

        String dvgrabCmd = this.capture.m_dvgrab;
        Process process = mock(Process.class);
        BufferedReader inputStreamReader = mock(BufferedReader.class);
        BufferedReader errorStreamReader = mock(BufferedReader.class);
        BufferedWriter outputWriter = mock(BufferedWriter.class);
        BufferedWriter errorOutputWriter = mock(BufferedWriter.class);

        when(Runtime.getRuntime().exec(dvgrabCmd.split(" "))).thenReturn(process);
        when(process.getInputStream()).thenReturn(inputStreamReader);
        when(process.getErrorStream()).thenReturn(errorStreamReader);
        when(process.getInputStream()).thenReturn(inputStreamReader);
        when(process.getErrorStream()).thenReturn(errorStreamReader);
        when(process.getInputStream()).thenReturn(inputStreamReader);
        when(process.getErrorStream()).thenReturn(errorStreamReader);
        when(process.waitFor(1000, TimeUnit.MILLISECONDS)).thenReturn(true);

        this.capture.DV_Capture();

        verify(process, times(1)).destroy();
    }

    @Test
    public void testDV_Capture_OutputDirDoesNotExist_DeletesNonExistentOutputDir() {
        when(this.gui.strOutputDir).thenReturn("outputDir");

        this.capture.init();

        File outputDir = new File("outputDir");
        assertFalse(outputDir.exists());

        this.capture.DV_Capture();

        File outputDirExpected = new File("outputDir");
        assertTrue(outputDirExpected.mkdir());
        File outputDirDVD = new File("outputDir/DVD");
        assertTrue(outputDirDVD.mkdir());
        File outputDirLog = new File("outputDir/log");
        assertTrue(outputDirLog.mkdir());
        File outputDirDv = new File("outputDir/dv");
        assertTrue(outputDirDv.mkdir());
    }

    @Test
    public void testDV_Capture_ErrorDuringOutputDirCreation_ReturnsError() {
        when(this.gui.strOutputDir).thenReturn("outputDir");

        this.capture.init();

        File outputDir = new File("outputDir");
        assertFalse(outputDir.mkdir());

        this.capture.DV_Capture();

        assertTrue(this.capture.m_Error);
    }

    @Test
    public void testDV_Capture_CommandNotFound_ReturnsError() {
        when(this.gui.strOutputDir).thenReturn("outputDir");

        this.capture.init();

        String dvgrabCmd = this.capture.m_dvgrab;
        Process process = mock(Process.class);
        BufferedReader inputStreamReader = mock(BufferedReader.class);
        BufferedReader errorStreamReader = mock(BufferedReader.class);
        BufferedWriter outputWriter = mock(BufferedWriter.class);
        BufferedWriter errorOutputWriter = mock(BufferedWriter.class);

        when(Runtime.getRuntime().exec(dvgrabCmd.split(" "))).thenThrow(new IOException("Command not found"));

        this.capture.DV_Capture();

        assertTrue(this.capture.m_Error);
    }

    @Test
    public void testDV_Capture_CameraNotConnected_ReturnsError() throws IOException, InterruptedException {
        when(this.gui.strOutputDir).thenReturn("outputDir");

        this.capture.init();

        String dvgrabCmd = this.capture.m_dvgrab;
        Process process = mock(Process.class);
        BufferedReader inputStreamReader = mock(BufferedReader.class);
        BufferedReader errorStreamReader = mock(BufferedReader.class);
        BufferedWriter outputWriter = mock(BufferedWriter.class);
        BufferedWriter errorOutputWriter = mock(BufferedWriter.class);

        when(Runtime.getRuntime().exec(dvgrabCmd.split(" "))).thenReturn(process);
        when(process.getInputStream()).thenReturn(inputStreamReader);
        when(process.getErrorStream()).thenReturn(errorStreamReader);
        when(process.getInputStream()).thenReturn(inputStreamReader);
        when(process.getErrorStream()).thenReturn(errorStreamReader);
        when(process.getInputStream()).thenReturn(inputStreamReader);
        when(process.getErrorStream()).thenReturn(errorStreamReader);
        when(process.waitFor(100, TimeUnit.MILLISECONDS)).thenThrow(new IOException("Error: no camera exists"));

        this.capture.DV_Capture();

        assertTrue(this.capture.m_Error);
    }
}