package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByInt() {
        for (int i = 0; i <= 13; i++) {
            int axis = Axis.lookup(i);
            String expected = Axis.lookup(i + "");
            assertEquals(expected, Integer.toString(axis));
        }
        assertNull(Axis.lookup(-1));
    }

    @Test
    public void testLookupByString() {
        String[] axisNames = new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int axis = Axis.lookup(axisName);
            assertEquals(axisName, Axis.lookup(Integer.toString(axis)));
        }
        assertNull(Axis.lookup(null));
        assertNull(Axis.lookup(""));
        assertNull(Axis.lookup("invalid"));
    }
}