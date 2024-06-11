package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CaptureTest {

    private Capture capture;
    private GUI mockGUI;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        capture = new Capture();
        mockGUI = mock(GUI.class);
    }

    @Test
    public void testInitWhenThreadDisabled() throws InterruptedException, IOException {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(false);
        assertFalse(capture.init());
        verify(mockGUI, times(1)).MessageBox(anyString(), anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testInitWhenThreadEnabled() throws InterruptedException, IOException {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(true);
        Thread thread = capture.new Thread(capture);
        thread.start();
        verify(mockGUI, times(1)).MessageBox(anyString(), anyInt());
        verify(mockGUI, times(1)).ErrorCheck(anyString());
        verify(mockGUI, times(1)).MessageBox(anyString(), anyInt());
        verifyZeroInteractions(mockGUI.strOutputDir, Runtime.getRuntime(), File.class, ProcessBuilder.class, Process.class, BufferedReader.class, BufferedWriter.class, InputStreamReader.class, FileWriter.class);
        thread.join();
    }

    @Test(expected = NullPointerException.class)
    public void testInitWhenGUIIsNull() {
        capture = new Capture(1, 2, null);
        capture.init();
    }

    @Test
    public void testDV_CaptureWhenOutputDirExistsAsDirectory() throws IOException, InterruptedException {
        when(mockGUI.strOutputDir).thenReturn(new File("/existing/output/dir").getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            int response = (Integer) args[1];
            if (response == 0) {
                return true;
            }
            throw new AssertionError("Unexpected message box response: " + response);
        });
        capture = new Capture(1, 2, mockGUI);
        assertTrue(capture.init());
        verify(mockGUI, times(2)).MessageBox(anyString(), anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testDV_CaptureWhenOutputDirExistsAsFile() throws IOException, InterruptedException {
        when(mockGUI.strOutputDir).thenReturn(new File("/existing/output/file").getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            int response = (Integer) args[1];
            if (response == 0) {
                return true;
            }
            throw new AssertionError("Unexpected message box response: " + response);
        });
        capture = new Capture(1, 2, mockGUI);
        assertTrue(capture.init());
        verify(mockGUI, times(2)).MessageBox(anyString(), anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testDV_CaptureWhenErrorOccurs() throws IOException, InterruptedException {
        when(mockGUI.strOutputDir).thenReturn(new File("/output/dir").getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            int response = (Integer) args[1];
            if (response != 0) {
                throw new AssertionError("Unexpected message box response: " + response);
            }
            return true;
        });
        capture = new Capture(1, 2, mockGUI);
        assertFalse(capture.init());
        verify(mockGUI, times(4)).MessageBox(anyString(), anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testDV_CaptureWhenDvgrabNotFound() throws IOException, InterruptedException {
        when(mockGUI.strOutputDir).thenReturn(new File("/output/dir").getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            int response = (Integer) args[1];
            if (response != 0) {
                throw new AssertionError("Unexpected message box response: " + response);
            }
            return true;
        });
        String dvgrabCmd = capture.m_dvgrab;
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", dvgrabCmd);
        pb.directory(new File("/output/dir"));
        try {
            Process process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            assertEquals("/bin/sh: dvgrab: command not found", line);
            br.close();
            isr.close();
            process.destroy();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException", e);
        }
        assertFalse(capture.init());
        verify(mockGUI, times(4)).MessageBox(anyString(), anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testDV_CaptureWhenCameraNotConnected() throws IOException, InterruptedException {
        when(mockGUI.strOutputDir).thenReturn(new File("/output/dir").getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            int response = (Integer) args[1];
            if (response != 0) {
                throw new AssertionError("Unexpected message box response: " + response);
            }
            return true;
        });
        String dvgrabCmd = capture.m_dvgrab;
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", dvgrabCmd);
        pb.directory(new File("/output/dir"));
        Process process = pb.start();
        BufferedReader errIn = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String errorLine = errIn.readLine();
        assertEquals("Error: no camera exists", errorLine);
        errIn.close();
        process.destroy();
        assertFalse(capture.init());
        verify(mockGUI, times(4)).MessageBox(anyString(), anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

}