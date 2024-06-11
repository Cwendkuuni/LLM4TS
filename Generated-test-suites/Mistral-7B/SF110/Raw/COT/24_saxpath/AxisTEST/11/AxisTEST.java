package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= Axis.ANCESTOR_OR_SELF; i++) {
            String result = Axis.lookup(i);
            switch (i) {
                case 1:
                    assertEquals("child", result);
                    break;
                case 2:
                    assertEquals("descendant", result);
                    break;
                case 3:
                    assertEquals("parent", result);
                    break;
                case 4:
                    assertEquals("ancestor", result);
                    break;
                case 5:
                    assertEquals("following-sibling", result);
                    break;
                case 6:
                    assertEquals("preceding-sibling", result);
                    break;
                case 7:
                    assertEquals("following", result);
                    break;
                case 8:
                    assertEquals("preceding", result);
                    break;
                case 9:
                    assertEquals("attribute", result);
                    break;
                case 10:
                    assertEquals("namespace", result);
                    break;
                case 11:
                    assertEquals("self", result);
                    break;
                case 12:
                    assertEquals("descendant-or-self", result);
                    break;
                case 13:
                    assertEquals("ancestor-or-self", result);
                    break;
                default:
                    assertEquals(null, result);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByInvalidAxisNumber() {
        String result = Axis.lookup(-1);
        assertEquals(null, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByUnknownAxisNumber() {
        String result = Axis.lookup(14);
        assertEquals(null, result);
    }

    @Test
    public void testLookupByAxisName() {
        for (Axis.AxisName axisName : Axis.AxisName.values()) {
            String result = Axis.lookup(axisName.name());
            assertEquals(axisName.getAxisName(), result);
        }

        String result = Axis.lookup("unknownAxis");
        assertEquals(null, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupByInvalidAxisName() {
        String result = Axis.lookup("child-descendant");
        assertEquals(null, result);
    }
}