package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class CaptureTest {

    private Capture instance;
    private GUI gui;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        gui = mock(GUI.class);
        instance = new Capture();
    }

    @Test
    public void testInitWhenThreadNotSelected() {
        assertFalse(instance.init());
    }

    @Test
    public void testInitWhenThreadSelected() throws InterruptedException {
        when(gui.MenuBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(true);
        instance.m_GUI = gui;
        instance.m_Thread = Mockito.mock(Thread.class);
        instance.init();
        Mockito.verify(gui).MessageBox(ArgumentMatchers.startsWith(instance.m_BaseErr), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());
        Mockito.verify(instance.m_Thread).setPriority(10);
        Mockito.verify(instance.m_Thread).start();
    }

    @Test(expected = NullPointerException.class)
    public void testInitWhenGuiIsNull() {
        instance.init();
    }

    @Test
    public void testRun() {
        instance.run();
    }

    @Test
    public void testDV_CaptureWhenOutputDirectoryExistsAndIsADirectory() {
        instance.m_GUI = gui;
        when(gui.strOutputDir).thenReturn("output_dir");
        File outputDir = new File("output_dir");
        when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(true);

        try {
            instance.DV_Capture();
        } catch (IOException e) {
            // expected
        } catch (InterruptedException e) {
            // expected
        }

        ArgumentCaptor<String> rmCmdCaptor = ArgumentCaptor.forClass(String[].class);
        Mockito.verify(Runtime.getRuntime()).exec(rmCmdCaptor.capture());
        String[] rmCmd = rmCmdCaptor.getValue();
        assertEquals("rm -fr output_dir", new String(rmCmd[0].getBytes()));
    }

    @Test
    public void testDV_CaptureWhenOutputDirectoryExistsAndIsNotADirectory() {
        instance.m_GUI = gui;
        when(gui.strOutputDir).thenReturn("output_dir/file.txt");
        File outputDir = new File("output_dir/file.txt");
        when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(true);

        try {
            instance.DV_Capture();
        } catch (IOException e) {
            // expected
        } catch (InterruptedException e) {
            // expected
        }

        ArgumentCaptor<String> rmCmdCaptor = ArgumentCaptor.forClass(String[].class);
        Mockito.verify(Runtime.getRuntime()).exec(rmCmdCaptor.capture());
        String[] rmCmd = rmCmdCaptor.getValue();
        assertEquals("rm -fr output_dir", new String(rmCmd[0].getBytes()));
    }

    @Test
    public void testDV_CaptureWhenOutputDirectoryDoesNotExist() {
        instance.m_GUI = gui;
        when(gui.strOutputDir).thenReturn("output_dir");
        File outputDir = new File("output_dir");
        when(gui.MessageBox(Mockito.anyString(), Mockito.eq(2))).thenReturn(0);

        try {
            instance.DV_Capture();
        } catch (IOException e) {
            // expected
        } catch (InterruptedException e) {
            // expected
        }

        Mockito.verify(gui, Mockito.times(0)).MessageBox(ArgumentMatchers.startsWith(instance.m_BaseErr), Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(outputDir).mkdir();
    }

    @Test(expected = IOException.class)
    public void testDV_CaptureWithIOException() throws IOException, InterruptedException {
        instance.m_GUI = gui;
        when(gui.strOutputDir).thenReturn("output_dir");
        File outputDir = new File("output_dir");

        Runtime runtime = Mockito.mock(Runtime.class);
        Process p = Mockito.mock(Process.class);
        when(runtime.exec(ArgumentMatchers.any(String[].class), ArgumentMatchers.eq(null), ArgumentMatchers.eq(outputDir))).thenReturn(p);
        when(p.waitFor()).thenThrow(new IOException("test error"));

        instance.DV_Capture();
    }

    @Test(expected = InterruptedException.class)
    public void testDV_CaptureWithInterruptedException() throws IOException, InterruptedException {
        instance.m_GUI = gui;
        when(gui.strOutputDir).thenReturn("output_dir");
        File outputDir = new File("output_dir");

        Runtime runtime = Mockito.mock(Runtime.class);
        Process p = Mockito.mock(Process.class);
        when(runtime.exec(ArgumentMatchers.any(String[].class), ArgumentMatchers.eq(null), ArgumentMatchers.eq(outputDir))).thenReturn(p);
        when(p.waitFor()).thenThrow(new InterruptedException("test error"));

        instance.DV_Capture();
    }

    @Test(expected = NullPointerException.class)
    public void testDV_CaptureWithNullGui() {
        instance.DV_Capture();
    }

    @Test
    public void testDV_CaptureWithInvalidCommand() throws IOException, InterruptedException {
        instance.m_GUI = gui;
        instance.m_dvgrab = "invalid_command";
        when(gui.strOutputDir).thenReturn("output_dir");

        Runtime runtime = Mockito.mock(Runtime.class);
        Process p = Mockito.mock(Process.class);
        when(runtime.exec(ArgumentMatchers.any(String[].class), ArgumentMatchers.eq(null), ArgumentMatchers.eq(new File("output_dir"))))
                .thenReturn(p);
        when(p.waitFor()).thenReturn(0);

        when(p.getInputStream()).thenReturn(Mockito.mock(BufferedReader.class));
        when(p.getErrorStream()).thenReturn(Mockito.mock(BufferedReader.class));
        when(p.getInputStream()).thenThrow(new IOException("test error"));

        instance.DV_Capture();

        Mockito.verify(gui).MessageBox(ArgumentMatchers.startsWith(instance.m_BaseErr), ArgumentMatchers.eq(0), ArgumentMatchers.anyString());
    }

    @Test
    public void testDV_CaptureWithNoCameraFound() throws IOException, InterruptedException {
        instance.m_GUI = gui;
        when(gui.strOutputDir).thenReturn("output_dir");

        Runtime runtime = Mockito.mock(Runtime.class);
        Process p = Mockito.mock(Process.class);
        when(runtime.exec(ArgumentMatchers.any(String[].class), ArgumentMatchers.eq(null), ArgumentMatchers.eq(new File("output_dir"))))
                .thenReturn(p);
        when(p.waitFor()).thenReturn(0);

        when(p.getInputStream()).thenReturn(Mockito.mock(BufferedReader.class));
        when(p.getErrorStream()).thenReturn(Mockito.mock(BufferedReader.class));
        when(p.getErrorStream()).thenReadLine().thenReturn("/bin/sh: dvgrab: command not found");

        instance.DV_Capture();

        Mockito.verify(gui).MessageBox(ArgumentMatchers.startsWith(instance.m_BaseErr), ArgumentMatchers.eq(0), ArgumentMatchers.anyString());
    }

    @Test
    public void testDV_CaptureWithError() throws IOException, InterruptedException {
        instance.m_GUI = gui;
        when(gui.strOutputDir).thenReturn("output_dir");

        Runtime runtime = Mockito.mock(Runtime.class);
        Process p = Mockito.mock(Process.class);
        when(runtime.exec(ArgumentMatchers.any(String[].class), ArgumentMatchers.eq(null), ArgumentMatchers.eq(new File("output_dir"))))
                .thenReturn(p);
        when(p.waitFor()).thenReturn(0);

        when(p.getInputStream()).thenReturn(Mockito.mock(BufferedReader.class));
        when(p.getErrorStream()).thenReturn(Mockito.mock(BufferedReader.class));
        when(p.getErrorStream()).thenReadLine().thenReturn("Error: no camera exists");

        instance.DV_Capture();

        Mockito.verify(gui).MessageBox(ArgumentMatchers.startsWith(instance.m_BaseErr), ArgumentMatchers.eq(0), ArgumentMatchers.anyString());
    }

    @Test
    public void testCaptureWithInitError() {
        instance.m_GUI = gui;
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        instance.m_Thread = Mockito.mock(Thread.class);

        try {
            instance.init();
        } catch (Exception e) {
            // expected
        }

        Mockito.verify(instance.m_Thread).start();
    }
}