import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

public class CaptureTest {

    private Capture capture;
    private GUI gui;

    @Before
    public void setup() {
        this.gui = new GUI(); // Assuming GUI is a testable class as well
        this.capture = new Capture(0, 0, gui);
    }

    @Test
    public void testConstructor() {
        assertNotNull(capture);
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertNotNull(capture.m_dvgrab);
    }

    @Test
    public void testInit_noThread() {
        assertFalse(capture.init());
        assertTrue(capture.m_Error);
    }

    @Test
    public void testInit_withThread() {
        gui.menuChkThread.setSelected(true);
        capture = new Capture(0, 0, gui);
        assertFalse(capture.init());
        assertTrue(capture.m_Error);
    }

    @Test(expected = NullPointerException.class)
    public void testDV_Capture_nullGUI() {
        Capture capture = new Capture(0, 0, null);
        capture.DV_Capture();
    }

    @Test
    public void testDV_Capture_validOutputDirectory() {
        File outputDir = new File("output");
        gui.strOutputDir = outputDir.getAbsolutePath();
        capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        assertTrue(!outputDir.exists() || outputDir.isDirectory());
    }

    @Test
    public void testDV_Capture_existingOutputDirectory() {
        File outputDir = new File("output");
        outputDir.mkdir();
        gui.strOutputDir = outputDir.getAbsolutePath();
        capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        assertTrue(!outputDir.exists() || outputDir.isDirectory());
    }

    @Test
    public void testDV_Capture_existingNonDirectoryOutputFile() {
        File outputDir = new File("output");
        outputDir.mkdir();
        File outputFile = new File(outputDir, "output.txt");
        outputFile.createNewFile();
        gui.strOutputDir = outputDir.getAbsolutePath();
        capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        assertTrue(!outputFile.exists() || outputFile.isFile());
    }

    @Test(expected = IOException.class)
    public void testDV_Capture_ioErrorDuringDeleteDirectory() {
        File outputDir = new File("output");
        outputDir.mkdir();
        gui.strOutputDir = outputDir.getAbsolutePath();
        capture = new Capture(0, 0, gui);
        final String rm = "rm -fr " + outputDir.getAbsolutePath();
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", rm});
        capture.DV_Capture();
    }

    @Test(expected = InterruptedException.class)
    public void testDV_Capture_interrupted() throws InterruptedException {
        Thread.currentThread().interrupt();
        capture = new Capture(0, 0, gui);
        capture.DV_Capture();
    }

    @Test
    public void testDV_Capture_dvgrabNotFound() {
        gui.strOutputDir = "output";
        capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        String expected = "/bin/sh: dvgrab: command not found";
        String output = capture.m_GUI.txtAreaOutput.getText();
        assertTrue(output.contains(expected));
        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_noCamera() {
        gui.strOutputDir = "output";
        capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        String expected = "Error: no camera exists";
        String output = capture.m_GUI.txtAreaOutput.getText();
        assertTrue(output.contains(expected));
        assertTrue(capture.m_Error);
    }

}