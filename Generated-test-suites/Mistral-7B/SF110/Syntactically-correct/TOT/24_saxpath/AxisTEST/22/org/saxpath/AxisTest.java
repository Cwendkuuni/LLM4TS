import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {
    // 
    @Test
    public void testLookupAxisNumber() {
        int expectedAxisNumber = 1;
        String result = Axis.lookup(expectedAxisNumber);

        String expectedAxisName = "child";
        int axisNumber = Axis.lookup(expectedAxisName.charAt(0) - 'a'); // convert 'child' to number

        assertEquals(expectedAxisName, Axis.lookup(axisNumber));
        assertEquals(expectedAxisNumber, axisNumber);
    }
    // 
}