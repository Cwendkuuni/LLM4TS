package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            int axisNumber = i;
            String axisName = Axis.lookup(axisNumber);
            assertEquals("Unexpected axis name for number: " + axisNumber, Axis.lookup(axisNumber), axisName);
        }
        assertNull("Unexpected non-null value for lookup of invalid axis number", Axis.lookup(-1));
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };

        for (String axisName : axisNames) {
            int axisNumber = Axis.lookup(axisName);
            assertEquals("Unexpected axis number for name: " + axisName, Axis.lookup(axisName), axisNumber);
        }
        assertNull("Unexpected non-null value for lookup of invalid axis name", Axis.lookup("invalidAxisName"));
    }
}