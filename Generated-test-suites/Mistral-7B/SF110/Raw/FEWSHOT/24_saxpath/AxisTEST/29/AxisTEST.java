package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            int axis = Axis.lookup(i);
            String expectedName = Axis.lookup(axis);
            assertEquals(Integer.toString(i), expectedName);
        }
        assertNull(Axis.lookup(-1));
        assertNull(Axis.lookup(14));
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int axis = Axis.lookup(axisName);
            String expectedName = Axis.lookup(axis);
            assertEquals(axisName, expectedName);
        }
        assertNull(Axis.lookup(null));
        assertNull(Axis.lookup("unknown"));
    }
}