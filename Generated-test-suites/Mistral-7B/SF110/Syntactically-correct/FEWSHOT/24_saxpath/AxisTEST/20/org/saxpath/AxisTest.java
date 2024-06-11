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
            assertEquals("Unexpected axis name for given axis number: " + i, axisName, Axis.lookupNames(i));
        }
        assertNull("Invalid axis number should return null", Axis.lookup(14));
    }

    @Test
    public void testLookupAxisName() {
        for (String axisName : Axis.lookupNames()) {
            int axisNumber = Axis.lookup(axisName);
            assertEquals("Unexpected axis number for given axis name: " + axisName, axisNumber, Axis.lookup(Axis.lookupNames().indexOf(axisName)));
        }
        assertNull("Unknown axis name should return null", Axis.lookup("unknown-axis"));
    }

    @Test
    public void testLookupNames() {
        assertEquals("Unexpected number of axis names", 13, Axis.lookupNames().length);
    }
}