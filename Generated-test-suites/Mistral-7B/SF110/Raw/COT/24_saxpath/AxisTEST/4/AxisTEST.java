package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

    @Test
    public void testLookup_ValidAxisNumbers() {
        for (int i = 1; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testLookup_ValidAxisNames() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor",
                "following-sibling", "preceding-sibling", "following",
                "preceding", "attribute", "namespace", "self",
                "descendant-or-self", "ancestor-or-self"};

        for (String axisName : axisNames) {
            int expected = Axis.lookup(axisName);
            int actual = Axis.lookup(axisName);
            assertEquals(expected, actual);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testLookup_InvalidAxisNumbers() {
        Axis.lookup(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testLookup_InvalidAxisNames() {
        Axis.lookup("invalid");
    }

    @Test
    public void testLookup_NullArg() {
        assertNull(Axis.lookup(null));
    }

}