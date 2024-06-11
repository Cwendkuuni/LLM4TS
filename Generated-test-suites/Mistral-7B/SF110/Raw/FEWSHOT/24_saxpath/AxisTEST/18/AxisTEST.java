package org.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookup_ValidaxisNumbers() {
        for (int i = 1; i <= 13; i++) {
            int result = Axis.lookup(i);
            String expected = Axis.lookup(Integer.toString(i));
            assertEquals(expected, Axis.lookupName(result));
            assertEquals(i, result);
        }
    }

    @Test
    public void testLookup_ValidaxisNames() {
        String[] axisNames = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
        for (String axisName : axisNames) {
            int result = Axis.lookup(axisName);
            assertEquals(axisName, Axis.lookupName(result));
        }
    }

    @Test
    public void testLookup_InvalidaxisNumbers() {
        for (int i = 14; i <= 100; i++) {
            int result = Axis.lookup(i);
            assertNull(result);
        }
    }

    @Test
    public void testLookup_InvalidaxisNames() {
        String invalidAxisName = "invalidAxis";
        int result = Axis.lookup(invalidAxisName);
        assertNull(result);
    }

    private String AxislookupName(int axis) {
        return Axis.lookupName(axis);
    }
}