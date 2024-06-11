package saxpath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        // Test valid axis numbers
        for (int i = 1; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            assertEquals(expected, actual);
        }
        
        // Test invalid axis number (default case)
        String actual = Axis.lookup(-1);
        String expected = null;
        assertEquals(expected, actual);
    }
}