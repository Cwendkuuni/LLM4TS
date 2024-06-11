package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AxisTest {

    @Test
    public void testLookupInt() {
        for (int i = 0; i <= 13; i++) {
            int axisNum = i;
            String expectedAxisName = Axis.lookup(axisNum);
            int actualAxisNum = Axis.lookup(expectedAxisName);
            assertEquals(axisNum, actualAxisNum);
        }

        int invalidAxisNum = 14;
        assertNull(Axis.lookup(invalidAxisNum));
        assertNull(Axis.lookup(null));
    }

    @Test
    public void testLookupString() {
        String[] axisNames = new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (String axisName : axisNames) {
            int expectedAxisNum = Axis.lookup(axisName);
            assertEquals(expectedAxisNum, Axis.lookup(axisName));
        }

        assertNull(Axis.lookup(null));
        assertNull(Axis.lookup(""));
        assertNull(Axis.lookup("invalid"));
    }
}