package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            int axisNumber = Axis.lookup(i);
            String expectedAxisName = Axis.lookup(axisNumber);
            assertEquals(expectedAxisName, Axis.lookup(i));
        }
        assertNull(Axis.lookup(-1));
        assertNull(Axis.lookup(14));
    }

    @Test
    public void testLookupAxisName() {
        String[] expectedAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (int i = 0; i < 13; i++) {
            String axisName = Axis.lookupName(i);
            int axisNumber = Axis.lookup(axisName);
            assertEquals(axisName, Axis.lookupName(axisNumber));
            assertEquals(axisNumber, Axis.lookup(axisName));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupInvalidAxisName() {
        Axis.lookup("invalidAxisName");
    }

    private String lookupName(int axisNumber) {
        return Axis.lookup(axisNumber);
    }
}