package org.saxpath.test;

import org.junit.*;
import static org.junit.Assert.*;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        // Test valid axis numbers
        for (int axisNum = 1; axisNum <= 13; axisNum++) {
            String expected = Axis.lookup(axisNum);
            String actual = Axis.lookup(axisNum);
            assertEquals(expected, actual);
        }

        // Test invalid axis number
        String actual = Axis.lookup(-1);
        assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupNegativeAxisNum() {
        Axis.lookup(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupNullOrInvalidAxisName() {
        Axis.lookup(null);
        Axis.lookup("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupInvalidAxisNum() {
        Axis.lookup('a'); // Non-numeric value
        Axis.lookup(-1); // Negative axis number
    }

    @Test
    public void testLookupByAxisName() {
        // Test valid axis names
        for (String axisName : new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"}) {
            int expected = Axis.lookup(axisName.toLowerCase());
            int actual = Axis.lookup(axisName);
            assertEquals(expected, actual);
        }

        // Test invalid axis name
        int actual = Axis.lookup("invalid");
        assertEquals(0, actual);
    }
}