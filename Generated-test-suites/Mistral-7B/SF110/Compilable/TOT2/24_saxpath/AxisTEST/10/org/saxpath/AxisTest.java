package saxpath;
import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        String expected = "child";
        int axisNum = 1;
        String actual = Axis.lookup(axisNum);
        assertEquals(expected, actual);
    }
}