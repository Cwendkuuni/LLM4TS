import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        String result = Axis.lookup(1);
        assertEquals("child", result);
        // Test other valid axis numbers
    }

    @Test
    public void testLookupByAxisName() {
        String result = Axis.lookup("child");
        assertEquals("child", result);
        // Test other valid axis names
    }
}