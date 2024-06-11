import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CaptureTest {

    private Capture capture;
    private GUI mockGUI;

    @Before
    public void setUp() {
        mockGUI = mock(GUI.class);
        capture = new Capture(0, 0, mockGUI);
    }

    @Test
    public void testCaptureConstructor() {
        assertNotNull(capture);
        assertEquals(0, capture.m_Minutes);
        assertEquals(0, capture.m_Seconds);
        assertNotNull(capture.m_GUI);
        assertNull(capture.m_Thread);
        assertFalse(capture.m_Error);
        assertNull(capture.m_BaseErr);
        assertNull(capture.m_dvgrab);
    }

    @Test
    public void testCaptureInitNoThread() {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(false);
        capture.init();
        verify(mockGUI).MessageBox("Please choose a different name for your title or move the directory to another location.", 2);
        assertTrue(capture.m_Error);
    }

    @Test
    public void testCaptureInitWithThread() {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(true);
        try {
            capture.init();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InterruptedException e) {
            fail("InterruptedException should not be thrown");
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        verify(mockGUI).MessageBox("The Output Directory already exists!", JOptionPane.QUESTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCaptureInitNullGUI() {
        capture = new Capture(0, 0, null);
    }

    @Test
    public void testDV_CaptureEmptyDirectory() {
        File outputDir = new File("/output/dir");
        capture = new Capture(mockGUI, 0, 0) {
            @Override
            public String m_GUI_strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        };
        try {
            capture.DV_Capture();
            fail("Expected exception not thrown");
        } catch (IOException e) {
            // Expected exception
            assertTrue(e.getMessage().contains("IO Error"));
        }
        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_CaptureNonDirectory() {
        File outputFile = new File("/output/file");
        capture = new Capture(mockGUI, 0, 0) {
            @Override
            public String m_GUI_strOutputDir() {
                return outputFile.getAbsolutePath();
            }
        };
        try {
            capture.DV_Capture();
            fail("Expected exception not thrown");
        } catch (IOException e) {
            // Expected exception
            assertTrue(e.getMessage().contains("IO Error"));
        }
        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_CaptureInvalidCommand() {
        when(mockGUI.strOutputDir).thenReturn("/output/dir");
        capture = new Capture(mockGUI, 0, 0) {
            @Override
            public String m_dvgrab() {
                return "invalid_command";
            }
        };
        try {
            capture.DV_Capture();
            fail("Expected exception not thrown");
        } catch (IOException e) {
            // Expected exception
            assertTrue(e.getMessage().contains("/bin/sh: dvgrab: command not found"));
        }
        assertTrue(capture.m_Error);
    }

    @Test
    public void testCaptureRun() {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(true);
        when(mockGUI.MessageBox(String.contains("Output Directory already exists!"), intEq(JOptionPane.QUESTION)))
                .thenReturn(JOptionPane.YES_OPTION);
        when(mockGUI.MessageBox(String.contains("There exists a file with the Output directory name"), intEq(JOptionPane.QUESTION)))
                .thenReturn(JOptionPane.YES_OPTION);

        try {
            capture.init();
            capture.run();
            fail("Expected exception not thrown");
        } catch (IOException e) {
            // Expected exception
        } catch (InterruptedException e) {
            fail("InterruptedException should not be thrown");
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }

        verify(mockGUI, times(2)).MessageBox(anyString(), eq(JOptionPane.QUESTION));
        verify(mockGUI).MessageBox("Please choose a different name for your title or move the directory to another location.", 2);
        verify(mockGUI, times(2)).MessageBox(String.contains("Would you like to delete it?"), eq(JOptionPane.YES_NO_OPTION));
        verify(mockGUI).MessageBox("Could not locate dvgrab in your path!", 0);

        assertTrue(capture.m_Error);
    }

    @Test
    public void testCaptureRunSuccess() {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(true);
        when(mockGUI.MessageBox(String.contains("Output Directory already exists!"), intEq(JOptionPane.QUESTION)))
                .thenReturn(JOptionPane.NO_OPTION);
        when(mockGUI.MessageBox(String.contains("There exists a file with the Output directory name"), intEq(JOptionPane.QUESTION)))
                .thenReturn(JOptionPane.NO_OPTION);

        try {
            capture.init();
            capture.run();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InterruptedException e) {
            fail("InterruptedException should not be thrown");
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }

        verify(mockGUI, times(1)).MessageBox("The Output Directory already exists!", JOptionPane.QUESTION);
        verify(mockGUI, times(1)).MessageBox("Please choose a different name for your title or move the directory to another location.", 2);
        verify(mockGUI, times(1)).MessageBox("Please choose a different name for your title or move the directory to another location.", 2);
        verify(mockGUI, times(1)).MessageBox("Could not locate dvgrab in your path!", 0);

        verify(mockGUI, never()).MessageBox("It appears that your camcorder is not connected to your computer!", 0);

        assertFalse(capture.m_Error);
    }

    @Test(expected = NullPointerException.class)
    public void testCaptureRunNullGUI() {
        capture = new Capture(null, 0, 0);
        capture.init();
        capture.run();
    }
}