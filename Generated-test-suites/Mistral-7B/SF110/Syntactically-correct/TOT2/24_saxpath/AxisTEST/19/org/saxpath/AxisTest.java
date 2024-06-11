package org.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

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
        String expected = "descendant";
        String axisName = "descendant";
        int result = Axis.lookup(axisName);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupNegativeAxisNumber() {
        int axisNum = -1;
        Axis.lookup(axisNum);
    }

    @Test
    public void testLookupLargerAxisNumber() {
        int axisNum = 14;
        String result = Axis.lookup(axisNum);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupNullInput() {
        int axisNum = null;
        Axis.lookup(axisNum);
    }

    @Test
    public void testLookupInvalidAxisName() {
        String axisName = "invalid";
        int result = Axis.lookup(axisName);
        assertEquals(0, result); // INVALID_AXIS
    }

    @Test
    public void testLookupEmptyAxisName() {
        String axisName = "";
        int result = Axis.lookup(axisName);
        assertEquals(0, result); // INVALID_AXIS
    }

    @Test
    public void testLookupNullAxisName() {
        String axisName = null;
        Axis.lookup(axisName);
    }

    @Test
    public void testLookupFollowingSibling() {
        String expected = "following-sibling";
        int result = Axis.lookup(expected);
        assertEquals(5, result);
    }

    @Test
    public void testLookupPrecedingSibling() {
        String expected = "preceding-sibling";
        int result = Axis.lookup(expected);
        assertEquals(6, result);
    }

    @Test
    public void testLookupFollowing() {
        String expected = "following";
        int result = Axis.lookup(expected);
        assertEquals(7, result);
    }

    @Test
    public void testLookupPreceding() {
        String expected = "preceding";
        int result = Axis.lookup(expected);
        assertEquals(8, result);
    }

    @Test
    public void testLookupAttribute() {
        String expected = "attribute";
        int result = Axis.lookup(expected);
        assertEquals(9, result);
    }

    @Test
    public void testLookupNamespace() {
        String expected = "namespace";
        int result = Axis.lookup(expected);
        assertEquals(10, result);
    }

    @Test
    public void testLookupSelf() {
        String expected = "self";
        int result = Axis.lookup(expected);
        assertEquals(11, result);
    }

    @Test
    public void testLookupDescendantOrSelf() {
        String expected = "descendant-or-self";
        int result = Axis.lookup(expected);
        assertEquals(12, result);
    }

    @Test
    public void testLookupAncestorOrSelf() {
        String expected = "ancestor-or-self";
        int result = Axis.lookup(expected);
        assertEquals(13, result);
    }
}