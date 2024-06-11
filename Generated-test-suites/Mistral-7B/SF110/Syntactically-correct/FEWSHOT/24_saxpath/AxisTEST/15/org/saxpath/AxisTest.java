package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookup() {
        // Test valid axis numbers
        for (int i = 1; i <= 13; i++) {
            int axisNum = i;
            String expected = Axis.lookup(axisNum);
            String actual = Axis.lookup(Axis.lookup(axisNum));
            assertEquals("Expected axis name for axis number " + axisNum, expected, actual);
        }

        // Test valid axis names
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int axisNum = Axis.lookup(axisName);
            String expected = axisName;
            String actual = Axis.lookup(axisNum);
            assertEquals("Expected axis number for axis name " + axisName, axisNum, Axis.lookup(expected));
            assertEquals("Expected axis name for axis number " + axisNum, expected, actual);
        }

        // Test invalid axis number and name
        assertNull("Expected null for invalid axis number", Axis.lookup(0));
        assertNull("Expected null for invalid axis name", Axis.lookup("invalid"));
    }
}