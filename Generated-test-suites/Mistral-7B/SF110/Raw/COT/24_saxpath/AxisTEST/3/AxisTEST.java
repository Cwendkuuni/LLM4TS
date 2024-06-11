package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            String expectedAxisName = Axis.lookup(i);
            int axisNumber = i;
            String actualAxisName = Axis.lookup(axisNumber);
            assertEquals("Unexpected axis name for given axis number: " + axisNumber, expectedAxisName, actualAxisName);
        }
        int invalidAxisNumber = 14;
        String actualAxisName = Axis.lookup(invalidAxisNumber);
        assertNull("Axis name returned for invalid axis number: " + invalidAxisNumber, actualAxisName);
    }

    @Test
    public void testLookupByAxisName() {
        String[] validAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : validAxisNames) {
            int expectedAxisNumber = Axis.lookup(axisName);
            String actualAxisName = Axis.lookup(expectedAxisNumber);
            assertEquals("Unexpected axis number for given axis name: " + axisName, expectedAxisNumber, Axis.lookup(axisName.toString()));
            assertEquals("Unexpected axis name for given axis number: " + expectedAxisNumber, axisName, Axis.lookup(expectedAxisNumber).toString());
        }

        String invalidAxisName = "invalid";
        int actualAxisNumber = Axis.lookup(invalidAxisName);
        assertEquals("Invalid axis name should return 0: ", 0, actualAxisNumber);
        String actualAxisNameReturnedForInvalidAxisName = Axis.lookup(actualAxisNumber);
        assertNull("Axis name returned for invalid axis name: ", actualAxisNameReturnedForInvalidAxisName);
    }
}