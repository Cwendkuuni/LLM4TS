import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CaptureTest {

    @Mock
    private GUI m_GUI;

    private Capture capture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        capture = new Capture();
        capture.m_GUI = m_GUI;
    }

    @Test
    public void testCaptureConstructor() {
        Capture capture = new Capture();
        assertNotNull(capture);
    }

    @Test
    public void testCaptureConstructorWithArguments() {
        int min = 10;
        int sec = 20;
        GUI DVD_GUI = mock(GUI.class);
        Capture capture = new Capture(min, sec, DVD_GUI);
        assertNotNull(capture);
        assertEquals(min, capture.m_Minutes);
        assertEquals(sec, capture.m_Seconds);
        assertEquals(DVD_GUI, capture.m_GUI);
    }

    @Test
    public void testInit_NoThread() {
        doReturn(false).when(m_GUI).menuChkThread_isSelected();
        boolean result = capture.init();
        assertTrue(result);
    }

    @Test
    public void testInit_Thread() {
        doReturn(true).when(m_GUI).menuChkThread_isSelected();
        capture.init();
        verify(m_GUI, times(1)).MessageBox(eq("The Output Directory already exists!"), anyInt());
    }

    @Test(expected = NullPointerException.class)
    public void testDV_Capture_NullGUI() {
        capture = new Capture();
        capture.m_GUI = null;
        capture.DV_Capture();
    }

    @Test
    public void testDV_Capture_OutputExistsAsDirectory() {
        doReturn(new File("output_dir")).when(m_GUI).strOutputDir;
        doReturn(new File("output_dir")).when(m_GUI).strOutputDir;
        doReturn(true).when(m_GUI).MessageBox(anyString(), anyInt());
        capture.init();
        verify(m_GUI, times(1)).MessageBox(eq("The Output Directory already exists!"), anyInt());
    }

    @Test
    public void testDV_Capture_OutputExistsAsFile() {
        doReturn(new File("output_dir")).when(m_GUI).strOutputDir;
        doReturn(new File("output_dir/file.txt")).when(m_GUI).strOutputDir;
        doReturn(true).when(m_GUI).MessageBox(anyString(), anyInt());
        capture.init();
        verify(m_GUI, times(1)).MessageBox(eq("Please choose a different name for your title or move the file to another location."), anyInt());
    }

    @Test
    public void testDV_Capture_DvgrabNotFound() {
        doReturn("output_dir").when(m_GUI).strOutputDir;
        doReturn("/bin/sh: dvgrab: command not found").when(m_GUI).MessageBox(anyString(), anyInt());
        capture.init();
        verify(m_GUI, times(1)).MessageBox(eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), anyInt());
    }

    @Test(expected = IOException.class)
    public void testDV_Capture_IOException() throws IOException {
        doReturn("output_dir").when(m_GUI).strOutputDir;
        String rm = "rm -fr output_dir";
        doThrow(IOException.class).when(m_GUI).MessageBox(eq(rm), anyInt());
        capture.init();
    }

    @Test(expected = InterruptedException.class)
    public void testDV_Capture_InterruptedException() throws InterruptedException {
        doReturn("output_dir").when(m_GUI).strOutputDir;
        String rm = "rm -fr output_dir";
        doThrow(InterruptedException.class).when(m_GUI).MessageBox(eq(rm), anyInt());
        capture.init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDV_Capture_IllegalArgumentException() {
        doReturn("output_dir").when(m_GUI).strOutputDir;
        doReturn(null).when(m_GUI).MessageBox(anyString(), anyInt());
        capture.init();
        verify(m_GUI, times(1)).MessageBox(anyString(), anyInt());
        verify(m_GUI, times(1)).MessageBox(eq("Please choose a different name for your title or move the directory to another location."), anyInt());
    }

    @Test
    public void testDV_Capture_Success() {
        doReturn("output_dir").when(m_GUI).strOutputDir;
        doReturn(true).when(m_GUI).menuChkThread_isSelected();
        capture.init();
        verify(m_GUI, times(1)).MessageBox(eq("The Output Directory already exists!"), anyInt());
        verifyZeroInteractions(m_GUI);
    }
}