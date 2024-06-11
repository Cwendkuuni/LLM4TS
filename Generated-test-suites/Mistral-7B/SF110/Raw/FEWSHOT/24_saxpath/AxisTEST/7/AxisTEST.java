package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            int axisNumber = i;
            String expected = Axis.lookup(axisNumber);
            String actual = "";
            if (axisNumber > 0) {
                actual = "axis" + axisNumber;
            }
            assertEquals(expected, actual);
        }
        assertNull(Axis.lookup(-1));
        assertNull(Axis.lookup(14));
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int expected = Axis.lookup(axisName);
            int actual = -1;
            if (expected != INVALID_AXIS) {
                actual = expected;
            }
            assertEquals(expected, actual);
        }
        assertNull(Axis.lookup(null));
        assertNull(Axis.lookup(""));
        assertNull(Axis.lookup("invalidAxisName"));
    }
}