package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        // Test valid axis numbers
        for (int i = 0; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            assertEquals(expected, actual);
        }

        // Test invalid axis number
        String actual = Axis.lookup(14);
        assertNull(actual);
    }

    @Test
    public void testLookupByAxisName() {
        // Test valid axis names
        String[] validAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : validAxisNames) {
            int expected = Axis.lookup(axisName);
            int actual = Axis.lookup(axisName);
            assertEquals(expected, actual);
        }

        // Test invalid axis name
        String actual = Axis.lookup("invalidAxisName");
        assertEquals(0, actual);
    }

    @Test
    public void testInvalidLabel() {
        // Test lookup with invalid label
        String label = "InvalidLabel";
        int expected = Axis.INVALID_AXIS;
        int actual = Axis.lookup(label);
        assertEquals(expected, actual);
    }
}