import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        int axisNumber = 1;
        String expectedAxisName = "child";
        String result = Axis.lookup(axisNumber);
        assertEquals(expectedAxisName, result);
    }

    @Test
    public void testLookupByAxisName() {
        String axisName = "child";
        int expectedAxisNumber = 1;
        int result = Axis.lookup(axisName);
        assertEquals(expectedAxisNumber, result);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupNegativeAxisNumber() {
        int axisNumber = -1;
        Axis.lookup(axisNumber);
    }

    @Test
    public void testLookupNullInputAxisNumber() {
        int axisNumber = 0;
        String result = Axis.lookup(axisNumber);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupNullInputAxisName() {
        String axisName = null;
        Axis.lookup(axisName);
    }

    @Test
    public void testLookupInvalidAxisName() {
        String axisName = "invalid";
        int result = Axis.lookup(axisName);
        assertEquals(0, result);
    }

    @Test
    public void testLookupEmptyAxisName() {
        String axisName = "";
        int result = Axis.lookup(axisName);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupEmptyAxisNumber() {
        int axisNumber = 0;
        Axis.lookup(axisNumber);
    }
}