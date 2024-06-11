import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;
import java.lang.reflect.InvocationTargetException;

public class CaptureTest {

    private Capture capture;
    private File outputDir;
    private GUI gui;

    @Before
    public void setUp() {
        outputDir = new File("outputDir");
        outputDir.mkdir();
        gui = new GUI();
        capture = new Capture(0, 0, gui);
    }

    @After
    public void tearDown() {
        outputDir.delete();
    }

    // Test constructor without arguments
    @Test
    public void testConstructor() {
        capture = new Capture();
        Assert.assertNotNull(capture.m_dvgrab);
    }

    // Test constructor with arguments
    @Test
    public void testConstructorWithArguments() {
        capture = new Capture(1, 2, gui);
        Assert.assertEquals(1, capture.m_Minutes);
        Assert.assertEquals(2, capture.m_Seconds);
        Assert.assertNotNull(capture.m_GUI);
    }

    // Test init method when menuChkThread is not selected
    @Test
    public void testInitWhenThreadNotSelected() {
        capture.init();
        Assert.assertFalse(capture.m_Error);
    }

    // Test init method when menuChkThread is selected and thread starts successfully
    @Test
    public void testInitWhenThreadSelectedAndSuccess() {
        gui.menuChkThread.setSelected(true);
        capture = new Capture(0, 0, gui);
        capture.init();
        Assert.assertFalse(capture.m_Error);
    }

    // Test init method when menuChkThread is selected and thread fails to start
    @Test(expected = NullPointerException.class)
    public void testInitWhenThreadSelectedAndFailure() {
        gui.menuChkThread.setSelected(true);
        capture = new Capture(0, 0, null);
        capture.init();
    }

    // Test init method when output directory already exists as a file
    @Test
    public void testInitWhenOutputDirExistsAsFile() {
        outputDir.delete();
        File outputFile = new File(outputDir, "outputFile.txt");
        outputFile.createNewFile();
        capture = new Capture(0, 0, gui);
        capture.init();
        Assert.assertFalse(capture.m_Error);
    }

    // Test init method when output directory already exists as a directory
    @Test
    public void testInitWhenOutputDirExistsAsDirectory() {
        capture = new Capture(0, 0, gui);
        capture.init();
        Assert.assertTrue(capture.m_Error);
    }

    // Test init method when output directory does not exist and cannot be created
    @Test(expected = IOException.class)
    public void testInitWhenOutputDirCannotBeCreated() {
        File outputDir = new File("outputDir");
        capture = new Capture(0, 0, gui);
        capture.init();
    }

    // Test DV_Capture method when output directory does not exist
    @Test
    public void testDV_CaptureWhenOutputDirDoesNotExist() {
        capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        Assert.assertFalse(capture.m_Error);
    }

    // Test DV_Capture method when output directory exists as a file
    @Test
    public void testDV_CaptureWhenOutputDirExistsAsFile() {
        outputDir.delete();
        File outputFile = new File(outputDir, "outputFile.txt");
        outputFile.createNewFile();
        capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        Assert.assertTrue(capture.m_Error);
    }

    // Test DV_Capture method when output directory exists as a directory
    @Test
    public void testDV_CaptureWhenOutputDirExistsAsDirectoryWithNodvgrab() {
        capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        Assert.assertTrue(capture.m_Error);
    }

    // Test DV_Capture method when output directory exists as a directory and dvgrab is found
    @Test
    public void testDV_CaptureWhenOutputDirExistsAsDirectoryWithDvgrab() {
        capture = new Capture(0, 0, gui);
        capture.m_dvgrab = "/path/to/dvgrab";
        capture.DV_Capture();
        Assert.assertFalse(capture.m_Error);
    }

    // Test DV_Capture method when dvgrab fails to execute
    @Test(expected = IOException.class)
    public void testDV_CaptureWhenDvgrabFailsToExecute() {
        capture = new Capture(0, 0, gui);
        capture.m_dvgrab = "/path/to/nonexistent_command";
        capture.DV_Capture();
    }

    // Test DV_Capture method when dvgrab execution is interrupted
    @Test(expected = InterruptedException.class)
    public void testDV_CaptureWhenDvgrabExecutionIsInterrupted() {
        capture = new Capture(0, 0, gui);
        capture.m_Thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        capture.m_Thread.start();
        capture.DV_Capture();
    }
}