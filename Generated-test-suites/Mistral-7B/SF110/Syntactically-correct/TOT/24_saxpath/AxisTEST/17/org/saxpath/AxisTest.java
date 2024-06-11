import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupWithValidAxisNum() {
        String expectedAxis = "child";
        int axisNum = 1;
        String actualAxis = Axis.lookup(axisNum);

        assertEquals(expectedAxis, actualAxis);
    }
}
