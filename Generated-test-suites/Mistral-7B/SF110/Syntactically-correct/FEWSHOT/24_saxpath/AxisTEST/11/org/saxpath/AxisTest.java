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
            assertEquals(expectedAxis, Axis.lookup(axis));
            assertEquals(Axis.lookup(axis), axisName);
        }
    }

    @Test
    public void testLookupByAxisName() {
        for (String axisName : new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"}) {
            int expectedAxis = Integer.parseInt(axisName.substring(0, 1).equals(" ") ? axisName.substring(1) : axisName);
            assertEquals(expectedAxis, Axis.lookup(axisName));
            assertEquals(axisName, Axis.lookup(expectedAxis));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByInvalidAxisNumber() {
        Axis.lookup(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByInvalidAxisName() {
        Axis.lookup("invalid");
    }

    @Test
    public void testLookupByNullAxisNumber() {
        assertNull(Axis.lookup(null));
    }

    @Test
    public void testLookupByNullAxisName() {
        assertNull(Axis.lookup(null));
    }
}