package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            int axisNumber = Axis.lookup(i);
            String axisName = Axis.lookup(axisNumber);
            assertEquals("Unexpected axis name for axis number: " + i, Axis.lookup(axisNumber), axisName);
        }
        assertNull("Null should be returned for invalid axis number", Axis.lookup(-1));
    }

    @Test
    public void testLookupAxisName() {
        int[] axisNumbers = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
        String[] axisNames = new String[] {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (int i = 0; i < axisNumbers.length; i++) {
            int axisNumber = axisNumbers[i];
            String axisName = Axis.lookup(axisName);
            assertEquals("Unexpected axis number for axis name: " + axisName, axisNumber, Axis.lookup(axisName));
        }

        assertNull("Null should be returned for invalid axis name", Axis.lookup("invalid"));
    }
}