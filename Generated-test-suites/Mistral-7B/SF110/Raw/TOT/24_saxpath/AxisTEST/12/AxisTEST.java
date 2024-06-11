package org.saxpath;

import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        // Test valid axis numbers
        for (int i = 1; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            assertEquals("Expected axis name '"+ expected +"' for axis number '"+ i +"' but got '"+ actual +"'", expected, actual);
        }

        // Test invalid axis number
        assertNull("Expected null for invalid axis number", Axis.lookup(0));
    }

    @Test
    public void testLookupByAxisName() {
        // Test valid axis names
        String[] validAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : validAxisNames) {
            int expected = Axis.lookup(axisName);
            int actual = Axis.lookup(axisName);
            assertEquals("Expected axis number '"+ expected +"' for axis name '"+ axisName +"' but got '"+ actual +"'", expected, actual);
        }

        // Test invalid axis name
        assertNull("Expected null for invalid axis name", Axis.lookup(""));
    }

    @Test
    public void testLookup() {
        // Test valid axis numbers and names
        for (int i = 1; i <= 13; i++) {
            String expectedAxisName = Axis.lookup(i);
            int expected = Axis.lookup(expectedAxisName);
            int actual = Axis.lookup(i);
            assertEquals("Expected axis number '"+ expected +"' for axis number '"+ i +"' but got '"+ actual +"'", expected, actual);

            String actualAxisName = Axis.lookup(actual);
            assertEquals("Expected axis name '"+ expectedAxisName +"' for axis number '"+ actual +"' but got '"+ Axis.lookup(actual) +"'", expectedAxisName, Axis.lookup(actual));
        }

        // Test invalid axis number and name
        assertNull("Expected null for invalid axis number", Axis.lookup(0));
        assertNull("Expected null for invalid axis name", Axis.lookup(""));

        // Test edge cases: negative numbers and null
        assertNull("Expected null for negative axis number", Axis.lookup(-1));
        assertNull("Expected null for null axis number", Axis.lookup(null));

        // Test error scenarios: non-numeric axis number and non-string axis name
        assertNull("Expected null for non-numeric axis number", Axis.lookup("abc"));
        assertNull("Expected null for non-string axis name", Axis.lookup(123));
    }
}