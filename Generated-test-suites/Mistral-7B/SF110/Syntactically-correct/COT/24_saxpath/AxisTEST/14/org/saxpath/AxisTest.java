package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

    @Test
    public void testLookup_ValidAxisNum_ReturnsCorrectString() {
        for (int i = 1; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            assertEquals("Unexpected string for axis number " + i, expected, actual);
        }
    }

    @Test
    public void testLookup_InvalidAxisNum_ReturnsNull() {
        int invalidAxisNum = 0;
        String actual = Axis.lookup(invalidAxisNum);
        assertNull("Unexpected non-null string for invalid axis number", actual);
    }

    @Test
    public void testLookup_ValidAxisName_ReturnsCorrectAxisNum() {
        String validAxisName;
        for (int i = 1; i <= 13; i++) {
            validAxisName = Axis.lookupName(i);
            int expected = i;
            int actual = Axis.lookup(validAxisName);
            assertEquals("Unexpected axis number for axis name " + validAxisName, expected, actual);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookup_InvalidAxisName_ThrowsIllegalArgumentException() {
        String invalidAxisName = "invalidAxisName";
        Axis.lookup(invalidAxisName);
    }
}