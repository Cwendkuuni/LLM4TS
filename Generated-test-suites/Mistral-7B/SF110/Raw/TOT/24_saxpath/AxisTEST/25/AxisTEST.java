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
            assertEquals("Unexpected result for axis number " + i, expected, actual);
        }

        // Test invalid axis number
        assertNull("Unexpected non-null result for invalid axis number", Axis.lookup(0));
    }

    @Test
    public void testLookupByAxisName() {
        // Test valid axis names
        String[] validAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : validAxisNames) {
            int expected = Axis.lookup(axisName);
            int actual = Axis.lookup(axisName);
            assertEquals("Unexpected result for axis name " + axisName, expected, actual);
        }

        // Test invalid axis name
        assertNull("Unexpected non-null result for invalid axis name", Axis.lookup("invalid"));
    }
}