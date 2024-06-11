import org.junit.Test;
import static org.junit.Assert.assertNull;

public class CaptureTest {

    @Test
    public void testCapture() {
        Capture capture = new Capture();
        assertNull(capture.m_GUI);
        assertNull(capture.m_Thread);
        assertNull(capture.m_Error);
        assertNull(capture.m_BaseErr);
        assertNull(capture.m_dvgrab);
    }
}