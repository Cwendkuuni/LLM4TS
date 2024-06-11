import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    private Convert convert;
    private GUI DVD_GUI;

    @Before
    public void setUp() {
        DVD_GUI = new GUI(); // Assuming you have a mock implementation of GUI class
        convert = new Convert("dvd-ntsc", "1:1", DVD_GUI);
    }

    @After
    public void tearDown() {
        convert = null;
        DVD_GUI = null;
    }

    @Test
    public void testInitWithNoThread() {
        assertFalse(convert.init());
        assertTrue(convert.m_Error);
    }

    @Test
    public void testInitWithThread() {
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
        assertFalse(convert.m_Error);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithInvalidFiles() {
        convert.thread_track = -1;
        convert.Transcode();
    }

    @Test
    public void testTranscodeWithValidFiles() {
        convert.thread_track = 0;
        convert.Transcode();
        // Add assertions here to test output files, log files, etc.
    }
}