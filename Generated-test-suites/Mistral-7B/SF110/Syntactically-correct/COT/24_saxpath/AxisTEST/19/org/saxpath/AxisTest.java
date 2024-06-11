import org.junit.Test;
import org.junit.Assert.*;
import static org.saxpath.Axis.*;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            String expected = lookup(i);
            int actual = lookup((short) i);
            assertEquals(expected, actual);
        }
        // Test for invalid axis number
        short invalidAxis = 14;
        String actual = lookup(invalidAxis);
        assertNull(actual);
    }

    @Test
    public void testLookupByAxisName_Valid() {
        String[] validAxes = {"child", "descendant", "parent", "ancestor",
                "following-sibling", "preceding-sibling", "following",
                "preceding", "attribute", "namespace", "self",
                "descendant-or-self", "ancestor-or-self"};

        for (String axisName : validAxes) {
            int expected = lookup(axisName);
            int actual = lookup(axisName);
            assertEquals(expected, actual);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testLookupByAxisName_Null() {
        String axisName = null;
        int actual = lookup(axisName);
        fail("Expected NullPointerException, but got: " + actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByAxisName_Invalid() {
        String invalidAxisName = "invalidAxis";
        int actual = lookup(invalidAxisName);
        fail("Expected IllegalArgumentException, but got: " + actual);
    }
}