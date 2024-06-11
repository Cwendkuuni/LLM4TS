package org.saxpath.test;

import org.junit.*;
import static org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        String result = Axis.lookup(1);
        assertEquals("child", result);
    }

    @Test
    public void testLookupByAxisName() {
        int result = Axis.lookup("child");
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupNegativeAxisNumber() {
        String result = Axis.lookup(-1);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupNullAxisName() {
        int result = Axis.lookup(null);
        assertEquals(0, result); // Or something appropriate for the default case
    }

    @Test
    public void testLookupUnknownAxisName() {
        int result = Axis.lookup("unknown");
        assertEquals(0, result); // Or something appropriate for the default case
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupEmptyAxisName() {
        int result = Axis.lookup("");
        assertEquals(0, result); // Or something appropriate for the default case
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupEmptyStringAsAxisNumber() {
        String result = Axis.lookup("");
        assertNull(result);
    }
}