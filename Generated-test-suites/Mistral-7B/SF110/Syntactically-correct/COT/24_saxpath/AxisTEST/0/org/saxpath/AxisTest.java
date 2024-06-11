package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            assertEquals(expected, actual);
        }
        // Test for invalid axis number
        String actual = Axis.lookup(-1);
        assertNull(actual);
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };

        for (String axisName : axisNames) {
            int expected = Axis.lookup(axisName);
            int actual = Axis.lookup(axisName);
            assertEquals(expected, actual);
        }

        // Test for invalid axis name
        int actual = Axis.lookup("invalid");
        assertEquals(Axis.INVALID_AXIS, actual);
    }
}