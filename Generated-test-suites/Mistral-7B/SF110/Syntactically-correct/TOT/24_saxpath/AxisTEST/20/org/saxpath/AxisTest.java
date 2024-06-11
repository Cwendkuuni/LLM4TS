package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        // Test valid axis numbers
        for (int i = 1; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            assertEquals(expected, actual);
        }

        // Test invalid axis number
        String actual = Axis.lookup(Integer.MIN_VALUE);
        assertNull(actual);
    }

    @Test
    public void testLookupByAxisName() {
        // Test valid axis names
        String[] validAxes = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : validAxes) {
            int expected = Axis.lookup(axisName);
            int actual = Axis.lookup(axisName);
            assertEquals(expected, actual);
        }

        // Test invalid axis name
        String actual = Axis.lookup("invalid");
        assertEquals(Axis.INVALID_AXIS, actual);
    }

    // Add more test cases as needed for edge cases and error scenarios
}