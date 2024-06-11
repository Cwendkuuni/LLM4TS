import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        // Test valid axis numbers
        for (int i = 1; i <= 13; i++) {
            String expectedAxisName = Axis.lookup(i);
            int actualAxisNumber = Axis.lookup(expectedAxisName);
            assertEquals(i, actualAxisNumber);
        }

        // Test invalid axis number (default case)
        String nullAxisName = Axis.lookup(0);
        int actualInvalidAxisNumber = Axis.lookup(nullAxisName);
        assertEquals(Axis.INVALID_AXIS, actualInvalidAxisNumber);
    }
}