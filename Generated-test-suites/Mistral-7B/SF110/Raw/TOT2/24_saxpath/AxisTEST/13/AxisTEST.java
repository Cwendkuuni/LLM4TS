import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {

    @Test
    public void testLookupMethodWithValidAxisNumber() {
        int expectedAxis = 1;
        String actualAxis = Axis.lookup(expectedAxis);
        assertEquals("child", actualAxis);
    }
}