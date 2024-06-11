import org.junit.Test;
import org.junit.Assert.*;
import static org.saxpath.Axis.*;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i < 14; i++) {
            String expected = lookup(i);
            String actual = lookup((short) i);
            assertEquals(expected, actual);
        }
        // test for invalid axis number
        String result = lookup((short) -1);
        assertNull(result);
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int expected = lookup(axisName);
            int actual = lookup(axisName);
            assertEquals(expected, actual);
        }
        // test for unknown axis name
        int result = lookup("unknown");
        assertEquals(INVALID_AXIS, result);
    }
}