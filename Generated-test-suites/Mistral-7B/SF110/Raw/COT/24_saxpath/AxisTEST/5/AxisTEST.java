package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int axisNum = 0; axisNum <= 13; axisNum++) {
            String expected = Axis.lookup(axisNum);
            String actual = Axis.lookup(axisNum);
            assertEquals(expected, actual);
        }
        String actual = Axis.lookup(-1);
        assertNull(actual);
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int expected = Axis.lookup(axisName);
            int actual = Axis.lookup(axisName);
            assertEquals(expected, actual);
        }
        int actual = Axis.lookup("unknown");
        assertEquals(Axis.INVALID_AXIS, actual);
    }
}