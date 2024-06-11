import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {

    @Test
    public void testInit() {
        Capture capture = new Capture();
        assertFalse(capture.init());
    }
}