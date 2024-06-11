import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {

    @Test
    public void TestCapture() {
        Capture capture = new Capture();
        assertNotNull(capture);
    }

    @Test
    public void TestCaptureInit() {
        Capture capture = new Capture();
        assertFalse(capture.init());
    }

    // Add the following test cases for edge cases and exceptions

    // TestInitTrue()
    // TestInitIOException()
    // TestInitInterruptedException()
    // TestDV_CaptureDirectoryFile()
    // TestDV_CaptureCmdNotFound()
    // TestDV_CaptureIOException()
    // TestDV_CaptureInterruptedException()

    // TestDV_Capture()
    @Test
    public void TestDV_Capture() {
        Capture capture = new Capture(0, 0, null);
        capture.init();
        capture.DV_Capture();

        File outputDir = new File(capture.m_GUI.strOutputDir);
        assertTrue(outputDir.exists() && outputDir.isDirectory());

        for (String dirName : new String[]{"DVD", "log", "dv"}) {
            File dir = new File(outputDir.getAbsolutePath() + "/" + dirName);
            assertTrue(dir.exists() && dir.isDirectory());
        }
    }
}