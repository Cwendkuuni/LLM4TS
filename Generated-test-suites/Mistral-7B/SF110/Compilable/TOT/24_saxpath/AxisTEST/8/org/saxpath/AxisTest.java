package saxpath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {
    @Test
    public void testLookupWithInt() {
        // Test valid axis numbers
        for (int i = 1; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            assertEquals(expected, actual);
        }

        // Test invalid axis number
        String actual = Axis.lookup(Integer.MIN_VALUE);
        String expected = null;
        assertEquals(expected, actual);

        // Test out of range axis number
        int outOfRangeAxis = 14;
        actual = Axis.lookup(outOfRangeAxis);
        expected = null;
        assertEquals(expected, actual);
    }
}