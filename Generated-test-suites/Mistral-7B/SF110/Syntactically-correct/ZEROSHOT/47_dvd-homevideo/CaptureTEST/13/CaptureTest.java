import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CaptureTest {

    private Capture capture;
    private GUI guiMock;

    @Before
    public void setUp() {
        guiMock = Mockito.mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = guiMock;
    }

    @Test
    public void testConstructor() {
        assertNull(capture.m_Thread);
        assertFalse(capture.m_Error);
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertNull(capture.m_dvgrab);
    }

    @Test
    public void testConstructorWithArgs() {
        int min = 5;
        int sec = 10;
        capture = new Capture(min, sec, guiMock);
        assertFalse(capture.m_Error);
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals(min, capture.m_Minutes);
        assertEquals(sec, capture.m_Seconds);
        assertNull(capture.m_Thread);
    }

    @Test
    public void testInit_menuChkThreadIsSelected() {
        Mockito.when(guiMock.menuChkThread.isSelected()).thenReturn(true);
        capture.init();
        assertFalse(capture.m_Error);
        assertTrue(capture.m_Thread.isAlive());
    }

    @Test
    public void testInit_menuChkThreadIsNotSelected() {
        Mockito.when(guiMock.menuChkThread.isSelected()).thenReturn(false);
        capture.init();
        assertFalse(capture.m_Error);
        assertNull(capture.m_Thread);
    }

    @Test
    public void testDV_Capture_outputDirExistsAndIsDirectory() {
        File outputDir = Mockito.mock(File.class);
        Mockito.when(outputDir.exists()).thenReturn(true);
        Mockito.when(outputDir.isDirectory()).thenReturn(true);

        Mockito.when(guiMock.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        capture.DV_Capture();
        assertFalse(capture.m_Error);
    }

    @Test
    public void testDV_Capture_outputDirExistsButIsNotDirectory() {
        File outputDir = Mockito.mock(File.class);
        Mockito.when(outputDir.exists()).thenReturn(true);
        Mockito.when(outputDir.isDirectory()).thenReturn(false);

        Mockito.when(guiMock.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        capture.DV_Capture();
        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_outputDirDoesNotExist() {
        File outputDir = Mockito.mock(File.class);
        Mockito.when(outputDir.exists()).thenReturn(false);

        Mockito.when(guiMock.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        capture.DV_Capture();
        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_outputDirIsFile() {
        File outputDir = Mockito.mock(File.class);
        Mockito.when(outputDir.exists()).thenReturn(true);
        Mockito.when(outputDir.isDirectory()).thenReturn(false);

        Mockito.when(guiMock.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        capture.DV_Capture();
        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_dvgrabCommandNotFound() {
        Mockito.when(guiMock.strOutputDir).thenReturn("/path/to/output/dir");

        String[] dvgrabCmd = { "/bin/sh", "-c", capture.m_dvgrab };
        Process process = Mockito.mock(Process.class);
        BufferedReader input = Mockito.mock(BufferedReader.class);
        BufferedReader errorInput = Mockito.mock(BufferedReader.class);
        BufferedWriter output = Mockito.mock(BufferedWriter.class);
        BufferedWriter errorOutput = Mockito.mock(BufferedWriter.class);

        Mockito.when(Runtime.getRuntime().exec(dvgrabCmd, (String[]) ArgumentMatchers.any(), (File) ArgumentMatchers.any())).thenReturn(process);
        Mockito.when(process.getInputStream()).thenReturn(input);
        Mockito.when(process.getErrorStream()).thenReturn(errorInput);
        Mockito.when(process.getOutputStream()).thenReturn(output);

        Mockito.when(errorInput.ready()).thenReturn(true);
        Mockito.when(errorInput.readLine()).thenReturn("/bin/sh: dvgrab: command not found");

        capture.DV_Capture();
        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_dvgrabProcessExecutionSuccess() throws IOException, InterruptedException {
        Mockito.when(guiMock.strOutputDir).thenReturn("/path/to/output/dir");

        String[] dvgrabCmd = { "/bin/sh", "-c", capture.m_dvgrab };
        Process process = Mockito.mock(Process.class);
        BufferedReader input = Mockito.mock(BufferedReader.class);
        BufferedReader errorInput = Mockito.mock(BufferedReader.class);
        BufferedWriter output = Mockito.mock(BufferedWriter.class);
        BufferedWriter errorOutput = Mockito.mock(BufferedWriter.class);

        Mockito.when(Runtime.getRuntime().exec(dvgrabCmd, (String[]) ArgumentMatchers.any(), (File) ArgumentMatchers.any())).thenReturn(process);
        Mockito.when(process.getInputStream()).thenReturn(input);
        Mockito.when(process.getErrorStream()).thenReturn(errorInput);
        Mockito.when(process.getOutputStream()).thenReturn(output);

        Mockito.when(errorInput.ready()).thenReturn(false);
        Mockito.when(input.ready()).thenReturn(true);
        Mockito.when(input.readLine()).thenReturn("capture output");
        Mockito.when(output.write(ArgumentMatchers.anyString())).thenAnswer(invocation -> invocation.getArgument(0).length());

        capture.DV_Capture();
        assertFalse(capture.m_Error);

        assertEquals("capture output\n", capture.m_GUI.txtAreaOutput.getText());
    }
}