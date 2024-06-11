package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class CaptureTest {

    private Capture capture;
    private GUI mockGUI;

    @Before
    public void setUp() {
        mockGUI = mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = mockGUI;
    }

    @Test
    public void testInitNoThread() {
        assertFalse(capture.init());
    }

    @Test
    public void testInitWithThread() {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(true);
        Thread thread = mock(Thread.class);
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);

        capture = new Capture(1, 2, mockGUI);
        capture.m_Thread = thread;

        assertFalse(capture.init());
        verify(thread).start();
    }

    @Test
    public void testInitWithError() {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(false);
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(2);

        capture = new Capture(1, 2, mockGUI);
        boolean result = capture.init();

        assertFalse(result);
        verifyNoMoreInteractions(mockGUI);
    }

    @Test(expected = NullPointerException.class)
    public void testInitNullGUI() {
        capture = new Capture(1, 2, null);
        capture.init();
    }

    @Test
    public void testDV_CaptureNoError() {
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);
        when(mockGUI.strOutputDir.exists()).thenReturn(true);

        capture = new Capture(1, 2, mockGUI);
        capture.DV_Capture();

        verify(mockGUI, times(1)).MessageBox(eq("The Output Directory " + capture.m_GUI.strOutputDir + " already exists!"), anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testDV_CaptureDeleteFile() {
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);
        when(mockGUI.strOutputDir.exists()).thenReturn(true);
        when(mockGUI.strOutputDir.isDirectory()).thenReturn(false);

        capture = new Capture(1, 2, mockGUI);
        capture.DV_Capture();

        verify(mockGUI, times(1)).MessageBox("There exists a file with the Output directory name at this location!  Would you like to delete it?", anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testDV_CaptureDeleteDirectory() {
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);
        when(mockGUI.strOutputDir.exists()).thenReturn(true);
        when(mockGUI.strOutputDir.isDirectory()).thenReturn(true);

        capture = new Capture(1, 2, mockGUI);
        capture.DV_Capture();

        verify(mockGUI, times(1)).MessageBox("Please choose a different name for your title or move the directory to another location.", anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testDV_Capture() {
        when(mockGUI.MessageBox(anyString(), anyInt())).thenAnswer((Answer<Integer> arg0) -> 0);
        when(mockGUI.strOutputDir.exists()).thenReturn(false);

        capture = new Capture(1, 2, mockGUI);
        capture.DV_Capture();

        verify(mockGUI, times(1)).MessageBox("Please choose a name for your title.", anyInt());
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testRun() {
        when(mockGUI.MessageBox(anyString(), anyInt())).thenAnswer((Answer<Integer> arg0) -> 0);
        when(mockGUI.strOutputDir.exists()).thenReturn(true);

        capture = new Capture(1, 2, mockGUI);
        capture.run();

        verify(mockGUI, times(1)).MessageBox("The Output Directory " + capture.m_GUI.strOutputDir + " already exists!");
        verifyNoMoreInteractions(mockGUI);
    }

    @Test
    public void testRunNoError() {
        when(mockGUI.MessageBox(anyString(), anyInt())).thenAnswer((Answer<Integer> arg0) -> 0);
        when(mockGUI.strOutputDir.exists()).thenReturn(false);

        capture = new Capture(1, 2, mockGUI);
        capture.run();

        verifyNoMoreInteractions(mockGUI);
    }
}