package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookup_ValidAxisNumbers() {
        int[] axisNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        String[] expectedStrings = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (int i = 0; i < axisNumbers.length; i++) {
            int axisNumber = axisNumbers[i];
            String expectedString = expectedStrings[i];
            String result = Axis.lookup(axisNumber);
            assertEquals(expectedString, result);
        }
    }

    @Test
    public void testLookup_ValidAxisNames() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        int[] expectedAxisNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        for (int i = 0; i < axisNames.length; i++) {
            String axisName = axisNames[i];
            int expectedAxisNumber = expectedAxisNumbers[i];
            int result = Axis.lookup(axisName);
            assertEquals(expectedAxisNumber, result);
        }
    }

    @Test
    public void testLookup_InvalidAxisNumber_ReturnsNull() {
        int[] invalidAxisNumbers = {-1, 14};

        for (int i = 0; i < invalidAxisNumbers.length; i++) {
            int axisNumber = invalidAxisNumbers[i];
            String result = Axis.lookup(axisNumber);
            assertNull(result);
        }
    }

    @Test
    public void testLookup_InvalidAxisName_ReturnsNull() {
        String[] invalidAxisNames = {"invalid", "foo"};

        for (String axisName : invalidAxisNames) {
            String result = Axis.lookup(axisName);
            assertNull(result);
        }
    }
}