import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {
    @Test
    public void testCaptureConstructor() {
        Capture capture = new Capture();
        assertNotNull(capture);
    }
}