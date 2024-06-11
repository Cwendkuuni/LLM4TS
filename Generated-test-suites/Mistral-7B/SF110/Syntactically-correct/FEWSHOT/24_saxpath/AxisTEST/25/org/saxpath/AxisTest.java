package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int axis = 0; axis <= 13; axis++) {
            String axisName = Axis.lookup(axis);
            int expectedAxis = axis;

            if (axis == 0) {
                expectedAxis = Axis.INVALID_AXIS;
            }

            assertEquals(expectedAxis, Axis.lookup(expectedAxis));
            assertEquals(axisName, Axis.lookup(expectedAxis));
        }

        assertNull(Axis.lookup(-1));
        assertNull(Axis.lookup(14));
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = {
                "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following",
                "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"
        };

        for (String axisName : axisNames) {
            int expectedAxis = Integer.parseInt(axisName.substring(0, axisName.indexOf(" ")));

            assertEquals(expectedAxis, Axis.lookup(expectedAxis));
            assertEquals(axisName, Axis.lookup(expectedAxis));
        }

        assertNull(Axis.lookup("invalidAxisName"));
    }
}