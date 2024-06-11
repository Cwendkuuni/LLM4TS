package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupInt() {
        for (int i = 0; i <= 13; i++) {
            int axis = Axis.lookup(i);
            String lookupName = Axis.lookup(axis);
            assertEquals(String.valueOf(i), lookupName);
        }
        assertNull(Axis.lookup(Integer.MIN_VALUE));
        assertNull(Axis.lookup(Integer.MAX_VALUE));
        assertNull(Axis.lookup(-1));
    }

    @Test
    public void testLookupString() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int axis = Axis.lookup(axisName);
            assertEquals(axisName, Axis.lookup(axis));
        }
        assertNull(Axis.lookup(null));
        assertNull(Axis.lookup(""));
    }
}