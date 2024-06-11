package saxpath;
import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookupWithValidAxisNumber() {
        String expectedResult = "child";
        int axisNumber = 1;
        String result = Axis.lookup(axisNumber);
        assertEquals(expectedResult, result);
    }

    // Repeat the above test case for the other valid axis numbers
}