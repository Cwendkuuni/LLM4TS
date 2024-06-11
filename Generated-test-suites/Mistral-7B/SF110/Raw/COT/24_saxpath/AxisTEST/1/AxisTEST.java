package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i < Axis.values().length; i++) {
            String expected = Axis.lookup(i + 1);
            String actual = Axis.lookup(i);
            assertEquals(expected, actual);
        }
        int invalidAxis = -1;
        assertNull(Axis.lookup(invalidAxis));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByInvalidAxisName() {
        String invalidAxisName = "invalidAxis";
        Axis.lookup(invalidAxisName);
    }

    @Test
    public void testLookupByValidAxisName() {
        for (Axis axis : Axis.values()) {
            int expected = axis.ordinal() + 1;
            int actual = Axis.lookup(axis.name());
            assertEquals(expected, actual);
        }
    }
}