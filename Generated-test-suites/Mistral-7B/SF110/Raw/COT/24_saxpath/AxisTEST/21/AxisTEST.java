package org.saxpath.test;

import org.junit.Assert;
import org.junit.Test;
import org.saxpath.Axis;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            String expected = Axis.lookup(i);
            String actual = Axis.lookup(i);
            Assert.assertEquals(expected, actual);
        }
        String actual = Axis.lookup(-1);
        Assert.assertNull(actual);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupByAxisNumberNull() {
        Axis.lookup(null);
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int expected = Axis.lookup(axisName);
            int actual = Axis.lookup(axisName);
            Assert.assertEquals(expected, actual);
        }
        String invalidAxisName = "invalidAxisName";
        int actual = Axis.lookup(invalidAxisName);
        Assert.assertEquals(Axis.INVALID_AXIS, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByInvalidAxisName() {
        String invalidAxisName = "invalidAxisName";
        Axis.lookup(invalidAxisName);
    }

    @Test
    public void testLookupByEmptyString() {
        String emptyString = "";
        int actual = Axis.lookup(emptyString);
        Assert.assertEquals(Axis.INVALID_AXIS, actual);
    }

    @Test
    public void testLookupByNullString() {
        String nullString = null;
        int actual = Axis.lookup(nullString);
        Assert.assertEquals(Axis.INVALID_AXIS, actual);
    }

    @Test
    public void testLookupByWhiteSpaceString() {
        String whiteSpaceString = "   ";
        int actual = Axis.lookup(whiteSpaceString.trim());
        Assert.assertEquals(Axis.INVALID_AXIS, actual);
    }
}