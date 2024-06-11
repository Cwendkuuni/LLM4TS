import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 1; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            assertEquals(expected, actual);
        }
        int axisNum = 0;
        String actual = Axis.lookup(axisNum);
        assertNull(actual);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupByInvalidAxisNumber() {
        int axisNum = -1;
        Axis.lookup(axisNum);
    }

    @Test
    public void testLookupByInvalidAxisName() {
        String axisName = "invalid";
        int actual = Axis.lookup(axisName);
        assertEquals(0, actual);
    }

    @Test
    public void testLookupByName() {
        for (String axisName : new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"}) {
            int expected = Axis.lookup(axisName.toString());
            int actual = Axis.lookup(axisName);
            assertEquals(expected, actual);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testLookupByNullAxisName() {
        String axisName = null;
        Axis.lookup(axisName);
    }

    @Test
    public void testLookupCaseInsensitive() {
        String axisName = "FOLLOWING";
        int expected = 7;
        int actual = Axis.lookup(axisName);
        assertEquals(expected, actual);
    }
}