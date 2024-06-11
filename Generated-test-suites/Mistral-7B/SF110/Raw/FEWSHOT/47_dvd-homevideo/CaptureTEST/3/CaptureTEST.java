package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class CaptureTest {

    private Capture capture;
    private GUI gui;
    private File outputDir;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        gui = mock(GUI.class);
        capture = new Capture();
        outputDir = tempFolder.newFolder();
        capture.m_GUI = gui;
        capture.m_strOutputDir = outputDir.getAbsolutePath();
    }

    @Test
    public void testInitNoThreadCheck() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        assertFalse(capture.init());
        verify(gui).MessageBox(eq("The Output Directory " + outputDir.getAbsolutePath() + " already exists!"), eq(2));
    }

    @Test
    public void testInitThreadCheck() {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        Thread thread = mock(Thread.class);
        capture.m_Thread = thread;
        when(thread.isAlive()).thenReturn(true);
        assertFalse(capture.init());
    }

    @Test
    public void testInitSuccess() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(gui.MessageBox(eq("The Output Directory " + outputDir.getAbsolutePath() + " already exists!"), eq(0))).thenReturn(JOptionPane.NO_OPTION);
        when(gui.MessageBox(eq("Please choose a different name for your\ntitle or move the directory to another location."), eq(2))).thenReturn(JOptionPane.NO_OPTION);

        File outputDir = new File(capture.m_GUI.strOutputDir);
        when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        assertTrue(capture.init());

        File createOutDir = new File(outputDir, "DVD");
        File createOutDir2 = new File(outputDir, "log");
        File createOutDir3 = new File(outputDir, "dv");

        assertTrue(createOutDir.mkdir());
        assertTrue(createOutDir.isDirectory());
        assertTrue(createOutDir2.mkdir());
        assertTrue(createOutDir2.isDirectory());
        assertTrue(createOutDir3.mkdir());
        assertTrue(createOutDir3.isDirectory());
    }

    @Test
    public void testDV_Capture() {
        // TODO: Implement this test
    }

    @Test
    public void testInitException() throws IOException, InterruptedException {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(gui.MessageBox(eq("The Output Directory " + outputDir.getAbsolutePath() + " already exists!"), eq(0))).thenReturn(JOptionPane.YES_OPTION);

        expectedException.expect(IOException.class);
        expectedException.expectMessage("dvgrab: command not found");

        capture.init();
    }

    @Test
    public void testInitNullPointerException() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(gui.MessageBox(eq("The Output Directory " + outputDir.getAbsolutePath() + " already exists!"), eq(2))).thenReturn(JOptionPane.NO_OPTION);
        when(gui.MessageBox(eq("Please choose a different name for your\ntitle or move the directory to another location."), eq(2))).thenReturn(JOptionPane.NO_OPTION);

        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("gui");

        capture.init();
    }

    @Test
    public void testInitIllegalArgumentException() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(gui.MessageBox(eq("The Output Directory " + outputDir.getAbsolutePath() + " already exists!"), eq(2))).thenReturn(JOptionPane.NO_OPTION);
        when(gui.MessageBox(eq("Please choose a different name for your\ntitle or move the directory to another location."), eq(2))).thenReturn(JOptionPane.NO_OPTION);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("strOutputDir");

        capture.init();
    }

    @Test
    public void testInitUnknownError() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(gui.MessageBox(eq("The Output Directory " + outputDir.getAbsolutePath() + " already exists!"), eq(2))).thenReturn(JOptionPane.NO_OPTION);
        when(gui.MessageBox(eq("Please choose a different name for your\ntitle or move the directory to another location."), eq(2))).thenReturn(JOptionPane.NO_OPTION);

        expectedException.expect(Exception.class);
        expectedException.expectMessage("Unknown Error occurred");

        capture.init();
    }
}