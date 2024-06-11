package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookup_ValidAxisNumbers() {
        int[] axisNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        String[] expectedAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (int i = 0; i < axisNumbers.length; i++) {
            int axisNum = axisNumbers[i];
            String axisName = Axis.lookup(axisNum);
            assertEquals(expectedAxisNames[i], axisName);
        }
    }

    @Test
    public void testLookup_ValidAxisNames() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        int[] expectedAxisNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        for (int i = 0; i < axisNames.length; i++) {
            String axisName = axisNames[i];
            int axisNum = Axis.lookup(axisName);
            assertEquals(expectedAxisNumbers[i], axisNum);
        }
    }

    @Test
    public void testLookup_InvalidAxisNumberOrName() {
        int[] invalidAxisNumbers = {-1, 14};
        String invalidAxisName = "invalid";

        for (int axisNum : invalidAxisNumbers) {
            assertNull(Axis.lookup(axisNum));
        }

        assertNull(Axis.lookup(invalidAxisName));
    }
}