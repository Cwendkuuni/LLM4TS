package saxpath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        int axisNumber = 1;
        String expectedAxisName = "child";
        String actualAxisName = Axis.lookup(axisNumber);
        assertEquals(expectedAxisName, actualAxisName);
    }
}