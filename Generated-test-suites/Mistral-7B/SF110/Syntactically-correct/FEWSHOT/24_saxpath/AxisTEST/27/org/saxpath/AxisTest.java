package org.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookup_ValidAxisNumbers() {
        for (int i = 1; i <= 13; i++) {
            int axisNum = i;
            String expected = lookupStringForAxisNumber(axisNum);
            int actual = Axis.lookup(axisNum);
            assertEquals(expected, Axis.lookupName(actual));
        }
    }

    @Test
    public void testLookup_ValidAxisNames() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
        for (String axisName : axisNames) {
            int expected = lookupAxisNumberForName(axisName);
            int actual = Axis.lookup(axisName);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testLookup_InvalidAxisNumberAndName() {
        assertNull(Axis.lookup(-1));
        assertNull(Axis.lookup("invalid"));
    }

    private String lookupStringForAxisNumber(int axisNum) {
        switch (axisNum) {
            case 1: {
                return "child";
            }
            case 2: {
                return "descendant";
            }
            case 3: {
                return "parent";
            }
            case 4: {
                return "ancestor";
            }
            case 5: {
                return "following-sibling";
            }
            case 6: {
                return "preceding-sibling";
            }
            case 7: {
                return "following";
            }
            case 8: {
                return "preceding";
            }
            case 9: {
                return "attribute";
            }
            case 10: {
                return "namespace";
            }
            case 11: {
                return "self";
            }
            case 12: {
                return "descendant-or-self";
            }
            case 13: {
                return "ancestor-or-self";
            }
            default: {
                return null;
            }
        }
    }

    private int lookupAxisNumberForName(String axisName) {
        if ("child".equals(axisName)) {
            return 1;
        }
        if ("descendant".equals(axisName)) {
            return 2;
        }
        if ("parent".equals(axisName)) {
            return 3;
        }
        if ("ancestor".equals(axisName)) {
            return 4;
        }
        if ("following-sibling".equals(axisName)) {
            return 5;
        }
        if ("preceding-sibling".equals(axisName)) {
            return 6;
        }
        if ("following".equals(axisName)) {
            return 7;
        }
        if ("preceding".equals(axisName)) {
            return 8;
        }
        if ("attribute".equals(axisName)) {
            return 9;
        }
        if ("namespace".equals(axisName)) {
            return 10;
        }
        if ("self".equals(axisName)) {
            return 11;
        }
        if ("descendant-or-self".equals(axisName)) {
            return 12;
        }
        if ("ancestor-or-self".equals(axisName)) {
            return 13;
        }
        return 0;
    }

    @Test
    public void testLookupName_ValidAxisNumber() {
        for (int i = 1; i <= 13; i++) {
            int axisNum = i;
            String expected = lookupStringForAxisNumber(axisNum);
            String actual = Axis.lookupName(Axis.lookup(axisNum));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testLookupName_InvalidAxisNumber() {
        assertNull(Axis.lookupName(Axis.lookup(-1)));
    }
}