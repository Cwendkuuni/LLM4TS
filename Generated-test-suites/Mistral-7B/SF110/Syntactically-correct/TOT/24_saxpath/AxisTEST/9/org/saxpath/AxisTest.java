import org.junit.Test;
import org.saxpath.Axis;
import static org.junit.Assert.assertEquals;

public class AxisTest {
    @Test
    public void testLookupInt() {
        // Test valid axis numbers
        for (int i = 0; i <= Axis.ANCESTOR_OR_SELF; i++) {
            String expected = Axis.lookup(i);
            int axisNum = i;
            String actual = Axis.lookup(axisNum);
            assertEquals(expected, actual);
        }

        // Test invalid axis number
        int invalidAxisNum = 14;
        String actual = Axis.lookup(invalidAxisNum);
        assertNull(actual);
    }
}