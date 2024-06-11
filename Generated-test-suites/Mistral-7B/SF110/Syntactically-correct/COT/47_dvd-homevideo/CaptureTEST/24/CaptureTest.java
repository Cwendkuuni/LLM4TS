import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class CaptureTest {

    private Capture capture;
    private GUI gui;

    @org.junit.Before
    public void setUp() {
        gui = Mockito.mock(GUI.class);
        capture = new Capture(0, 0, gui);
    }

    @Test
    public void testCaptureConstructor() {
        Assert.assertNotNull(capture);
    }

    @Test
    public void testInit_CreateOutputDirectory_DoesNotExist() {
        Mockito.when(gui.strOutputDir).thenReturn(new File("output").getAbsolutePath());

        boolean result = capture.init();
        Assert.assertFalse(result);

        File outputDir = new File(capture.m_GUI.strOutputDir);
        Assert.assertTrue(outputDir.mkdir());
    }

    @Test
    public void testInit_CreateOutputDirectory_AlreadyExists_DeleteIt() {
        Mockito.when(gui.strOutputDir).thenReturn(new File("output").getAbsolutePath());

        File outputDir = new File(capture.m_GUI.strOutputDir);
        outputDir.mkdir();

        boolean result = capture.init();
        Assert.assertTrue(result);

        Assert.assertFalse(outputDir.exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInit_OutputDirectoryIsNull() {
        Mockito.when(gui.strOutputDir).thenReturn(null);
        capture.init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInit_OutputDirectoryIsFile() {
        Mockito.when(gui.strOutputDir).thenReturn(new File("output").getAbsolutePath() + File.separator + "output.txt").getAbsolutePath();

        capture.init();
    }

    @Test(expected = IOException.class)
    public void testDV_Capture_DvgrabCommandNotFound() {
        Mockito.when(gui.strOutputDir).thenReturn(new File("output").getAbsolutePath());
        Mockito.when(gui.MessageBox(String.valueOf((Object) ("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.")))).thenReturn(0);

        capture.DV_Capture();
    }

    @Test(expected = NullPointerException.class)
    public void testDV_Capture_NullGui() {
        capture = new Capture(0, 0, null);
        capture.DV_Capture();
    }

    @Test
    public void testDV_Capture_Success() {
        Mockito.when(gui.strOutputDir).thenReturn(new File("output").getAbsolutePath());
        Mockito.when(gui.MessageBox((String) org.junit.Assert.anyString())).then(invocationOnMock -> 0);

        capture.DV_Capture();

        File outputDir = new File(capture.m_GUI.strOutputDir);
        File logDir = new File(outputDir, "log");
        File dvDir = new File(outputDir, "dv");

        Assert.assertTrue(outputDir.exists());
        Assert.assertTrue(logDir.exists());
        Assert.assertTrue(dvDir.exists());
    }

    @Test(expected = InterruptedException.class)
    public void testRun_Interrupted() {
        capture.run();
        Thread.currentThread().interrupt();
    }
}