package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            int axisNum = i;
            String expected = Axis.lookup(axisNum);
            String result = Axis.lookup(Axis.lookup(expected));
            assertEquals("Unexpected result for axis number: " + axisNum, expected, result);
        }
        String unknownAxisNum = "14";
        assertNull("Unexpected non-null result for unknown axis number: " + unknownAxisNum, Axis.lookup(unknownAxisNum));
    }

    @Test
    public void testLookupByAxisName() {
        for (int i = 0; i <= 13; i++) {
            String axisName = Axis.lookup(i);
            int expected = i;
            int result = Axis.lookup(axisName);
            assertEquals("Unexpected result for axis name: " + axisName, expected, result);
        }
        String unknownAxisName = "invalidAxis";
        assertNull("Unexpected non-null result for unknown axis name: " + unknownAxisName, Axis.lookup(unknownAxisName));
    }
}