package org.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupInt() {
        for (int i = 0; i <= 13; i++) {
            int axis = Axis.lookup(i);
            String name = Axis.lookup(axis);
            assertEquals(name, Axis.lookup(i + ""));
        }

        assertNull(Axis.lookup(-1));
        assertNull(Axis.lookup(14));
    }

    @Test
    public void testLookupString() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (String axisName : axisNames) {
            int axis = Axis.lookup(axisName);
            assertEquals(axisName, Axis.lookup(axis + ""));
        }

        assertNull(Axis.lookup(null));
        assertNull(Axis.lookup(""));
        assertNull(Axis.lookup("unknown"));
    }
}