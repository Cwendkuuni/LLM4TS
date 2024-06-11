package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

    @Test
    public void testLookupByInteger() {
        for (int i = 0; i <= 13; i++) {
            String result = Axis.lookup(i);
            switch (i) {
                case 1:
                    assertTrue("Expected 'child'", result.equals("child"));
                    break;
                case 2:
                    assertTrue("Expected 'descendant'", result.equals("descendant"));
                    break;
                case 3:
                    assertTrue("Expected 'parent'", result.equals("parent"));
                    break;
                case 4:
                    assertTrue("Expected 'ancestor'", result.equals("ancestor"));
                    break;
                case 5:
                    assertTrue("Expected 'following-sibling'", result.equals("following-sibling"));
                    break;
                case 6:
                    assertTrue("Expected 'preceding-sibling'", result.equals("preceding-sibling"));
                    break;
                case 7:
                    assertTrue("Expected 'following'", result.equals("following"));
                    break;
                case 8:
                    assertTrue("Expected 'preceding'", result.equals("preceding"));
                    break;
                case 9:
                    assertTrue("Expected 'attribute'", result.equals("attribute"));
                    break;
                case 10:
                    assertTrue("Expected 'namespace'", result.equals("namespace"));
                    break;
                case 11:
                    assertTrue("Expected 'self'", result.equals("self"));
                    break;
                case 12:
                    assertTrue("Expected 'descendant-or-self'", result.equals("descendant-or-self"));
                    break;
                case 13:
                    assertTrue("Expected 'ancestor-or-self'", result.equals("ancestor-or-self"));
                    break;
                default:
                    assertTrue("Expected null", result == null);
                    break;
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByInvalidInteger() {
        Axis.lookup(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByIntegerOutOfRange() {
        Axis.lookup(14);
    }

    @Test
    public void testLookupByString() {
        String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (String axisName : axisNames) {
            int result = Axis.lookup(axisName);
            String expectedAxisName = null;

            switch (result) {
                case 1:
                    expectedAxisName = "child";
                    break;
                case 2:
                    expectedAxisName = "descendant";
                    break;
                case 3:
                    expectedAxisName = "parent";
                    break;
                case 4:
                    expectedAxisName = "ancestor";
                    break;
                case 5:
                    expectedAxisName = "following-sibling";
                    break;
                case 6:
                    expectedAxisName = "preceding-sibling";
                    break;
                case 7:
                    expectedAxisName = "following";
                    break;
                case 8:
                    expectedAxisName = "preceding";
                    break;
                case 9:
                    expectedAxisName = "attribute";
                    break;
                case 10:
                    expectedAxisName = "namespace";
                    break;
                case 11:
                    expectedAxisName = "self";
                    break;
                case 12:
                    expectedAxisName = "descendant-or-self";
                    break;
                case 13:
                    expectedAxisName = "ancestor-or-self";
                    break;
            }

            if (expectedAxisName != null) {
                assertEquals("Expected the correct axis name", expectedAxisName, Axis.lookup(axisName));
            } else {
                assertTrue("Expected null", Axis.lookup(axisName) == null);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByInvalidString() {
        Axis.lookup("invalidAxis");
    }

    @Test
    public void testLookupByStringNull() {
        assertNull("Expected null when null string is passed", Axis.lookup(null));
    }
}