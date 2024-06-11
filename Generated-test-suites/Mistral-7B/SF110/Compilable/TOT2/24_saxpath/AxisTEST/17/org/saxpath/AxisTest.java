package saxpath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        String expectedAxis = null;
        int axisNum = 0;

        String actualAxis = Axis.lookup(axisNum);

        assertEquals(expectedAxis, actualAxis);

        expectedAxis = "child";
        axisNum = 1;

        actualAxis = Axis.lookup(axisNum);

        assertEquals(expectedAxis, actualAxis);

        // Test for invalid axis number
        expectedAxis = null;
        axisNum = 14;

        actualAxis = Axis.lookup(axisNum);

        assertEquals(expectedAxis, actualAxis);
    }
}