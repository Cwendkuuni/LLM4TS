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
            assertEquals("Unexpected axis name for axis number: " + axisNumber, getExpectedAxisName(axisNumber), axisName);
        }
        assertNull("Null should be returned for invalid axis number", Axis.lookup(-1));
    }

    @Test
    public void testLookupByAxisName() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (String axisName : axisNames) {
            int axisNumber = Axis.lookup(axisName);
            assertEquals("Unexpected axis number for axis name: " + axisName, getExpectedAxisNumber(axisName), axisNumber);
        }
        assertNull("Null should be returned for invalid axis name", Axis.lookup("invalidAxisName"));
    }

    private int getExpectedAxisNumber(String axisName) {
        switch (axisName) {
            case "child":
                return CHILD;
            case "descendant":
                return DESCENDANT;
            case "parent":
                return PARENT;
            case "ancestor":
                return ANCESTOR;
            case "following-sibling":
                return FOLLOWING_SIBLING;
            case "preceding-sibling":
                return PRECEDING_SIBLING;
            case "following":
                return FOLLOWING;
            case "preceding":
                return PRECEDING;
            case "attribute":
                return ATTRIBUTE;
            case "namespace":
                return NAMESPACE;
            case "self":
                return SELF;
            case "descendant-or-self":
                return DESCENDANT_OR_SELF;
            case "ancestor-or-self":
                return ANCESTOR_OR_SELF;
            default:
                throw new IllegalArgumentException("Invalid axis name: " + axisName);
        }
    }

    private String getExpectedAxisName(int axisNumber) {
        switch (axisNumber) {
            case CHILD:
                return "child";
            case DESCENDANT:
                return "descendant";
            case PARENT:
                return "parent";
            case ANCESTOR:
                return "ancestor";
            case FOLLOWING_SIBLING:
                return "following-sibling";
            case PRECEDING_SIBLING:
                return "preceding-sibling";
            case FOLLOWING:
                return "following";
            case PRECEDING:
                return "preceding";
            case ATTRIBUTE:
                return "attribute";
            case NAMESPACE:
                return "namespace";
            case SELF:
                return "self";
            case DESCENDANT_OR_SELF:
                return "descendant-or-self";
            case ANCESTOR_OR_SELF:
                return "ancestor-or-self";
            default:
                throw new IllegalArgumentException("Invalid axis number: " + axisNumber);
        }
    }
}