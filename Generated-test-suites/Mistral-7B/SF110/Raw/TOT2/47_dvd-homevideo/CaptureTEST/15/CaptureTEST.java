
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

public class CaptureTest {
    private Capture capture;
    private GUI gui;

    @Before
    public void setUp() {
        gui = Mockito.mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = gui;
    }

    @Test
    public void testEmptyConstructor() {
        Capture capture = new Capture();
        assertNotNull(capture);
    }

    @Test
    public void testConstructorWithArguments() {
        int min = 5;
        int sec = 10;
        Capture capture = new Capture(min, sec, gui);
        assertEquals(min, capture.m_Minutes);
        assertEquals(sec, capture.m_Seconds);
        assertNotNull(capture.m_Thread);
    }

    @Test
    public void testInitWhenMenuNotSelected() {
        Capture capture = new Capture();
        assertFalse(capture.init());
    }

    @Test
    public void testInitWhenMenuSelected() {
        int min = 5;
        int sec = 10;
        GUI gui = mock(GUI.class);
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        Capture capture = new Capture(min, sec, gui);
        assertTrue(capture.init());
        verify(capture).DV_Capture();
    }

    @Test
    public void testDV_CaptureWhenOutputDirIsAFile() {
        Capture capture = new Capture();
        File outputDir = new File("output_dir");
        outputDir.setWritable(true);
        outputDir.createNewFile();

        when(capture.m_GUI.strOutputDir).thenReturn("output_dir");
        doThrow(IOException.class).when(capture).DV_Capture();

        capture.init();

        verify(capture).m_GUI.MessageBox("There exists a file with the Output directory name at this location!  Would you like to delete it?", eq(2));
    }

    @Test
    public void testDV_CaptureWhenOutputDirIsADirectory() {
        Capture capture = new Capture();
        File outputDir = new File("output_dir");
        outputDir.mkdirs();

        when(capture.m_GUI.strOutputDir).thenReturn("output_dir");

        capture.init();

        verify(capture).m_GUI.MessageBox("The Output Directory already exists!", eq(0));
    }

    @Test
    public void testDV_CaptureWhenRuntimeExecThrowsException() {
        Capture capture = new Capture();
        when(capture.m_GUI.strOutputDir).thenReturn("output_dir");
        doThrow(IOException.class).when(capture).DV_Capture();

        capture.init();

        verify(capture).m_GUI.MessageBox("Capture Error - IO Error\nIOException", eq(0));
    }
}
