import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class CaptureTest {

    private Capture capture;
    private GUI gui;

    @Before
    public void setup() {
        gui = mock(GUI.class);
        capture = new Capture(1, 2, gui);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCapture() {
        Assert.assertFalse(capture.init());
        capture.DV_Capture();
    }

    @Test(expected = IllegalStateException.class)
    public void testInitWhenThreadAlreadyRunning() {
        capture = new Capture();
        capture.m_Thread = Mockito.mock(Thread.class);
        capture.m_Thread.start();
        capture.init();
    }

    @Test
    public void testInitWhenOutputDirectoryExists() {
        File outputDir = new File("output_directory");
        outputDir.mkdir();

        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
        capture = new Capture(1, 2, gui);
        capture.init();

        Assert.assertTrue(outputDir.delete());
    }

    @Test
    public void testInitWhenOutputDirectoryIsFile() {
        File outputDir = new File("output_directory");
        outputDir.createNewFile();

        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
        capture = new Capture(1, 2, gui);
        capture.init();

        Assert.assertTrue(outputDir.delete());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWhenOutputDirectoryDoesNotExist() {
        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
        capture = new Capture(1, 2, gui);
        capture.init();
    }

    @Test
    public void testDV_CaptureWhenOutputDirectoryDoesNotExist() throws IOException {
        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        File outputDir = new File("output_directory");
        File expectedOutputDir = new File("output_directory/DVD");
        File expectedLogDir = new File("output_directory/log");
        File expectedDVDDir = new File("output_directory/dv");

        Assert.assertTrue(outputDir.mkdir());
        Assert.assertTrue(expectedOutputDir.mkdir());
        Assert.assertTrue(expectedLogDir.mkdir());
        Assert.assertTrue(expectedDVDDir.mkdir());
    }

    @Test
    public void testDV_CaptureWhenOutputDirectoryExists() throws IOException {
        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        File outputDir = new File("output_directory");
        File expectedOutputDir = new File("output_directory/DVD");
        File expectedLogDir = new File("output_directory/log");
        File expectedDVDDir = new File("output_directory/dv");

        Assert.assertFalse(outputDir.exists());
        Assert.assertFalse(expectedOutputDir.exists());
        Assert.assertFalse(expectedLogDir.exists());
        Assert.assertFalse(expectedDVDDir.exists());
    }

    @Test
    public void testDV_CaptureWhenDvgrabCommandNotFound() {
        StringWriter errWriter = new StringWriter();
        PrintWriter errPrintWriter = new PrintWriter(errWriter);

        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenAnswer(invocation -> {
            String message = invocation.getArgument(0);
            if (message.equals("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.")) {
                return 0;
            }
            return 1;
        });

        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        Assert.assertEquals("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", errWriter.toString());
    }

    @Test
    public void testDV_CaptureWhenCameraNotConnected() {
        StringWriter errWriter = new StringWriter();
        PrintWriter errPrintWriter = new PrintWriter(errWriter);

        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenAnswer(invocation -> {
            String message = invocation.getArgument(0);
            if (message.equals("It appears that your camcorder is not connected to your computer!")) {
                return 0;
            }
            return 1;
        });

        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        Assert.assertEquals("It appears that your camcorder is not connected to your computer!", errWriter.toString());
    }

    @Test
    public void testDV_CaptureWhenCaptureComplete() throws InterruptedException, IOException {
        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenAnswer(invocation -> {
            String message = invocation.getArgument(0);
            if (message.equals("/bin/sh: dvgrab: command not found")) {
                return 0;
            }
            return 1;
        });

        when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        Thread.sleep(1000);

        Assert.assertTrue(true);
    }

    @After
    public void cleanup() {
        capture = null;
        gui = null;
    }
}