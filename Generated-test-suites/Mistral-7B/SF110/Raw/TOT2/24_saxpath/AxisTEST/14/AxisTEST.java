package org.saxpath.test;

import static org.junit.Assert.*;
import org.junit.Test;

public class AxisTestCase {
    @Test
    public void testLookupByAxisNumber() {
        for (int axisNum = 0; axisNum <= 13; axisNum++) {
            String expectedAxisName = Axis.lookupName(axisNum);
            int actualAxisNum = Axis.lookup(axisNum);
            assertNotNull(actualAxisNum);
            assertEquals(expectedAxisName, Axis.lookupName(actualAxisNum));
        }
    }

    @Test
    public void testLookupByAxisName() {
        String[] validAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : validAxisNames) {
            int expectedAxisNum = Axis.lookup(axisName);
            int actualAxisNum = Axis.lookup(axisName);
            assertNotNull(actualAxisNum);
            assertEquals(expectedAxisNum, actualAxisNum);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupNegativeAxisNumber() {
        Axis.lookup(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupInvalidAxisNumber() {
        Axis.lookup(14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupInvalidAxisName() {
        Axis.lookup("invalid");
    }

    @Test(expected = NullPointerException.class)
    public void testLookupWithNullInputInt() {
        Axis.lookup(null);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupWithNullInputString() {
        Axis.lookup(null);
    }
}