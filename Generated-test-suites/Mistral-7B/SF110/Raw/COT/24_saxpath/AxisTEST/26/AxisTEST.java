package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int axisNum = 0; axisNum <= 13; axisNum++) {
            String expected = Axis.lookup(axisNum);
            String result = Axis.lookup(axisNum);
            assertEquals("Unexpected lookup result for axis number: " + axisNum, expected, result);
        }
        // Test for invalid axis number
        String result = Axis.lookup(-1);
        assertNull("Unexpected non-null result for invalid axis number", result);
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int expected = Axis.lookup(axisName);
            int result = Axis.lookup(axisName);
            assertEquals("Unexpected lookup result for axis name: " + axisName, expected, result);
        }
        // Test for invalid axis name
        int result = Axis.lookup("invalidAxisName");
        assertEquals("Unexpected non-zero result for invalid axis name", 0, result);
    }
}