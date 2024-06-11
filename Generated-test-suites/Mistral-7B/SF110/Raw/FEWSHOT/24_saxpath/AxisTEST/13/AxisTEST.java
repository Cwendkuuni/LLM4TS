package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int axisNum = 0; axisNum <= 13; axisNum++) {
            String expected = Axis.lookup(axisNum);
            assertEquals(expected, Axis.lookupByAxisNumber(axisNum));
        }
        assertEquals(null, Axis.lookupByAxisNumber(Integer.MIN_VALUE));
        assertEquals(null, Axis.lookupByAxisNumber(Integer.MAX_VALUE));
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };

        for (String axisName : axisNames) {
            int expectedAxisNumber = Axis.lookup(axisName.hashCode());
            assertEquals(expectedAxisNumber, Axis.lookupByAxisName(axisName));
        }

        assertEquals(null, Axis.lookupByAxisName(null));
        assertEquals(null, Axis.lookupByAxisName(""));
        assertEquals(null, Axis.lookupByAxisName("invalidName"));
    }
}