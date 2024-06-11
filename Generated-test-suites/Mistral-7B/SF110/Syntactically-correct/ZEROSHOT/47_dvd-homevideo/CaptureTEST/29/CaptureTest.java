import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CaptureTest {

    private Capture capture;
    private GUI mockGUI;
    private String strOutputDir = "/temp/output";

    @Before
    public void setUp() {
        mockGUI = Mockito.mock(GUI.class);
        capture = new Capture(1, 2, mockGUI);
    }

    @Test
    public void testConstructorWithoutArguments() {
        Capture capture = new Capture();
        assertNull(capture.m_GUI);
        assertNull(capture.m_Thread);
        assertNull(capture.m_Error);
        assertNull(capture.m_BaseErr);
        assertNull(capture.m_dvgrab);
    }

    @Test
    public void testConstructorWithArguments() {
        Capture capture = new Capture(1, 2, mockGUI);
        assertSame(mockGUI, capture.m_GUI);
        assertNull(capture.m_Thread);
        assertFalse(capture.init());
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);
        assertEquals(1, capture.m_Minutes);
        assertEquals(2, capture.m_Seconds);
    }

    @Test
    public void testInitWithoutThread() {
        Mockito.when(mockGUI.menuChkThread.isSelected()).thenReturn(false);
        capture.init();
        Mockito.verify(mockGUI, Mockito.atLeastOnce()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(2));
        assertTrue(capture.m_Error);
    }

    @Test
    public void testInitWithThread() throws InterruptedException, IOException {
        Mockito.when(mockGUI.menuChkThread.isSelected()).thenReturn(true);
        capture.init();
        Thread thread = Mockito.capture(capture.m_Thread);
        Mockito.verify(mockGUI, Mockito.atLeastOnce()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(0));
        assertTrue(thread.isAlive());
    }

    @Test
    public void testRun() throws IOException, InterruptedException {
        File outputDir = new File(strOutputDir);
        if (outputDir.exists() && outputDir.isDirectory()) {
            Mockito.when(mockGUI.MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(0))).thenReturn(JOptionPane.YES_OPTION);
            Runtime runtime = Mockito.mock(Runtime.class);
            capture.run();
            Mockito.verify(runtime, Mockito.atLeastOnce()).exec(ArgumentMatchers.anyArray());
        }
    }

    @Test
    public void testDVCapture() throws IOException {
        File outputDir = new File(strOutputDir);
        if (outputDir.exists() && outputDir.isDirectory()) {
            Mockito.when(mockGUI.MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(0))).thenReturn(JOptionPane.YES_OPTION);
            Capture capture = new Capture(1, 2, mockGUI);
            capture.DV_Capture();
            Mockito.verify(mockGUI, Mockito.atLeastOnce()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
            Mockito.verify(mockGUI, Mockito.atLeastOnce()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(2));
            Mockito.verify(mockGUI, Mockito.atLeastOnce()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
            Mockito.verify(mockGUI, Mockito.atLeastOnce()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
            Mockito.verify(mockGUI, Mockito.atLeastOnce()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
            Mockito.verify(mockGUI, Mockito.atLeastOnce()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
            assertTrue(capture.m_Error);
        }
    }
}