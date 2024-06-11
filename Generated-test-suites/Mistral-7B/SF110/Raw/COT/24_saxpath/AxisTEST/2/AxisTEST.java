import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            String expected = Axis.lookup(i);
            int axisNumber = i;
            String result = Axis.lookup(axisNumber);
            assertEquals(expected, result);
        }
        int invalidAxisNumber = 14;
        String result = Axis.lookup(invalidAxisNumber);
        assertNull(result);
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int expectedAxisNumber = Axis.lookup(axisName);
            String resultAxisName = Axis.lookup(expectedAxisNumber);
            assertEquals(axisName, resultAxisName);
        }
        String invalidAxisName = "invalidAxisName";
        int resultAxisNumber = Axis.lookup(invalidAxisName);
        assertEquals(0, resultAxisNumber);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForLookupByAxisNumber() {
        int nullAxisNumber = null;
        String result = Axis.lookup(nullAxisNumber);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForLookupByAxisName() {
        String nullAxisName = null;
        int resultAxisNumber = Axis.lookup(nullAxisName);
    }

}