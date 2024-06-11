package org.saxpath.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        String expected = "child";
        int axisNum = 1;
        String result = Axis.lookup(axisNum);
        assertEquals(expected, result);
    }

    @Test
    public void testLookupByAxisName() {
        int expected = 1;
        String axisName = "child";
        int result = Axis.lookup(axisName);
        assertEquals(expected, result);
    }

    @Test
    public void testLookupByInvalidAxisNumber() {
        int axisNum = 0;
        String result = Axis.lookup(axisNum);
        assertNull(result);
    }

    @Test
    public void testLookupByInvalidAxisName() {
        String axisName = "invalidAxis";
        int result = Axis.lookup(axisName);
        assertEquals(0, result);
    }

    @Test
    public void testLookupByValidAxisNumber() {
        String expected = "child";
        int axisNum = 1;
        String result = Axis.lookup(axisNum);
        assertEquals(expected, result);
    }

    @Test
    public void testLookupByValidAxisName() {
        int expected = 1;
        String axisName = "child";
        int result = Axis.lookup(axisName);
        assertEquals(expected, result);
    }
}