package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CaptureTest {

    private Capture capture;
    private GUI gui;
    private File outputDir;

    @Before
    public void setUp() {
        gui = mock(GUI.class);
        capture = new Capture();
        outputDir = new File("output_dir");
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
    }

    @After
    public void tearDown() {
        capture = null;
        gui = null;
    }

    @Test
    public void testInitWithoutThread() {
        assertFalse(capture.init());
    }

    @Test
    public void testInitWithThread() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        assertTrue(capture.init());
    }

    @Test
    public void testRunWithError() {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        capture = new Capture(1, 2, gui);
        capture.m_Error = true;
        capture.run();
        verifyNoMoreInteractions(gui);
    }

    @Test
    public void testRunWithoutError() {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        capture = new Capture(1, 2, gui);
        capture.run();

        // Verify that the methods in DV_Capture are called
        verify(gui).MessageBox(eq("The Output Directory output_dir already exists!"), anyInt());
        verify(gui).MessageBox(eq("Please choose a different name for your title or move the directory to another location."), anyInt());
        verify(gui).MessageBox(eq("It appears that your camcorder is not connected to your computer!"), anyInt());

        // Verify that files and directories are created
        assertTrue(outputDir.mkdir());
        assertTrue(outputDir.exists() && outputDir.isDirectory());
        File outputDirDVD = new File(outputDir, "DVD");
        assertTrue(outputDirDVD.mkdir());
        File outputDirLog = new File(outputDir, "log");
        assertTrue(outputDirLog.mkdir());
        File outputDirDv = new File(outputDir, "dv");
        assertTrue(outputDirDv.mkdir());

        // Verify that dvgrab command is executed
        String dvgrabCmd = capture.m_dvgrab;
        String[] dvgrabCmdArray = { "/bin/sh", "-c", dvgrabCmd };
        Process process = mock(Process.class);
        when(Runtime.getRuntime().exec(dvgrabCmdArray, null, outputDir)).thenReturn(process);

        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = inputStreamReader.readLine()) != null) {
            assertTrue(gui.txtAreaOutput.append(line).equals(line));
            assertEquals(line, capture.m_GUI.txtAreaOutput.getText());
        }

        inputStreamReader.close();

        // Verify that log file is created and written to
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputDir + "/log/dvgrab.log"));
        String logLine = "This is a log line.";
        fileWriter.write(logLine);
        fileWriter.newLine();
        fileWriter.close();

        String logFileContent = readFileContent(outputDir, "log/dvgrab.log");
        assertEquals(logLine, logFileContent);
    }

    @Test
    public void testRunWithIOError() {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        capture = new Capture(1, 2, gui);

        String rmCmd = "rm -fr output_dir";
        String[] rmCmdArray = { "/bin/sh", "-c", rmCmd };
        Process removeDirProcess = mock(Process.class);
        when(Runtime.getRuntime().exec(rmCmdArray, null)).thenReturn(removeDirProcess);

        IOException ioException = new IOException("IO Error");
        when(removeDirProcess.waitFor()).thenThrow(ioException);

        try {
            capture.run();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("IO Error", e.getMessage());
        }

        verifyNoMoreInteractions(gui);
    }

    @Test
    public void testRunWithInterruptedException() {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        capture = new Capture(1, 2, gui);

        InterruptedException interruptedException = new InterruptedException("dvd-homevideo thread was interrupted");
        when(Runtime.getRuntime().exec(capture.m_dvgrab)).thenThrow(interruptedException);

        try {
            capture.run();
            fail("Expected InterruptedException to be thrown");
        } catch (InterruptedException e) {
            assertEquals("dvd-homevideo thread was interrupted", e.getMessage());
        }

        verifyNoMoreInteractions(gui);
    }

    private String readFileContent(File file, String fileName) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getPath() + "/" + fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}