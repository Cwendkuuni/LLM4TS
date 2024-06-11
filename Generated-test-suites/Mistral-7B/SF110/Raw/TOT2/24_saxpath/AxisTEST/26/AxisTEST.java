import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookupInt() {
        int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        for (int axisNum : expectedValues) {
            String actualValue = Axis.lookup(axisNum);
            String expectedValue = Axis.lookupName(axisNum);
            assertEquals(expectedValue, actualValue);
        }
    }

    // Function to get expected name from given axis number
    private String lookupName(int axisNum) {
        // Implement this method based on the class definition
    }
}