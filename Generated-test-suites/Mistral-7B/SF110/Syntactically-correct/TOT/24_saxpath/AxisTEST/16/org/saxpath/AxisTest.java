package org.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
    @Test
    public void testLookupWithValidAxisNumber() {
        String expected = "child";
        int axisNumber = 1;
        String actual = Axis.lookup(axisNumber);
        assertEquals(expected, actual);
    }

    @Test
    public void testLookupWithValidChild() {
        String expected = "child";
        String axisName = "child";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidDescendant() {
        String expected = "descendant";
        String axisName = "descendant";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidParent() {
        String expected = "parent";
        String axisName = "parent";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidAncestor() {
        String expected = "ancestor";
        String axisName = "ancestor";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidFollowingSibling() {
        String expected = "following-sibling";
        String axisName = "following-sibling";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidPrecedingSibling() {
        String expected = "preceding-sibling";
        String axisName = "preceding-sibling";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidFollowing() {
        String expected = "following";
        String axisName = "following";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidPreceding() {
        String expected = "preceding";
        String axisName = "preceding";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidAttribute() {
        String expected = "attribute";
        String axisName = "attribute";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidNamespace() {
        String expected = "namespace";
        String axisName = "namespace";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidSelf() {
        String expected = "self";
        String axisName = "self";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidDescendantOrSelf() {
        String expected = "descendant-or-self";
        String axisName = "descendant-or-self";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithValidAncestorOrSelf() {
        String expected = "ancestor-or-self";
        String axisName = "ancestor-or-self";
        int actual = Axis.lookup(axisName);
        assertEquals(expected, Integer.toString(actual));
    }

    @Test
    public void testLookupWithInvalidAxisNumber() {
        assertNull(Axis.lookup(-1));
    }

    @Test
    public void testLookupWithInvalidAxisName() {
        assertNull(Axis.lookup("invalid"));
    }

    // Add test cases for other valid axis names
}