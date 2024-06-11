package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            int axisNumber = i;
            String axisName = Axis.lookup(axisNumber);
            assertEquals("Unexpected value for axis lookup by number", getExpectedAxisName(axisNumber), axisName);
        }
        assertEquals("Unexpected null value for axis lookup by number", null, Axis.lookup(-1));
    }

    @Test
    public void testLookupByAxisName() {
        String[] expectedAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (String axisName : expectedAxisNames) {
            int axisNumber = Axis.lookup(axisName);
            assertEquals("Unexpected value for axis lookup by name", getExpectedAxisNumber(axisName), axisNumber);
        }

        assertNull("Unexpected non-null value for axis lookup by name", Axis.lookup(null));
        assertNull("Unexpected non-null value for axis lookup by name (invalid)", Axis.lookup("invalid"));
    }

    private int getExpectedAxisNumber(String axisName) {
        switch (axisName) {
            case "child":
                return 1;
            case "descendant":
                return 2;
            case "parent":
                return 3;
            case "ancestor":
                return 4;
            case "following-sibling":
                return 5;
            case "preceding-sibling":
                return 6;
            case "following":
                return 7;
            case "preceding":
                return 8;
            case "attribute":
                return 9;
            case "namespace":
                return 10;
            case "self":
                return 11;
            case "descendant-or-self":
                return 12;
            case "ancestor-or-self":
                return 13;
            default:
                return 0;
        }
    }

    private String getExpectedAxisName(int axisNumber) {
        switch (axisNumber) {
            case 1:
                return "child";
            case 2:
                return "descendant";
            case 3:
                return "parent";
            case 4:
                return "ancestor";
            case 5:
                return "following-sibling";
            case 6:
                return "preceding-sibling";
            case 7:
                return "following";
            case 8:
                return "preceding";
            case 9:
                return "attribute";
            case 10:
                return "namespace";
            case 11:
                return "self";
            case 12:
                return "descendant-or-self";
            case 13:
                return "ancestor-or-self";
            default:
                return null;
        }
    }
}