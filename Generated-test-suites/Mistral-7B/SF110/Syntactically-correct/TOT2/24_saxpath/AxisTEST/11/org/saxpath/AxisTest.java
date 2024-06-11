import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        int axisNum = 1;
        String expectedResult = "child";
        String result = Axis.lookup(axisNum);
        assertEquals(expectedResult, result);
    }
}