package saxpath;
import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupByInt() {
        String expected = "child";
        int axisNum = 1;
        String result = Axis.lookup(axisNum);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupByInvalidInt() {
        int axisNum = 0;
        String result = Axis.lookup(axisNum);
        assertNull(result);
    }
}