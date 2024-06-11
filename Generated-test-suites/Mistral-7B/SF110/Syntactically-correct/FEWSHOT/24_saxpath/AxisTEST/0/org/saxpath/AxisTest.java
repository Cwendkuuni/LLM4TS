package org.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookup() {
        int axis = Axis.lookup(1);
        assertEquals(Axis.CHILD, axis);

        axis = Axis.lookup("child");
        assertEquals(Axis.CHILD, axis);

        axis = Axis.lookup(2);
        assertEquals(Axis.DESCENDANT, axis);

        axis = Axis.lookup("descendant");
        assertEquals(Axis.DESCENDANT, axis);

        axis = Axis.lookup(3);
        assertEquals(Axis.PARENT, axis);

        axis = Axis.lookup("parent");
        assertEquals(Axis.PARENT, axis);

        axis = Axis.lookup(4);
        assertEquals(Axis.ANCESTOR, axis);

        axis = Axis.lookup("ancestor");
        assertEquals(Axis.ANCESTOR, axis);

        axis = Axis.lookup(5);
        assertEquals(Axis.FOLLOWING_SIBLING, axis);

        axis = Axis.lookup("following-sibling");
        assertEquals(Axis.FOLLOWING_SIBLING, axis);

        axis = Axis.lookup(6);
        assertEquals(Axis.PRECEDING_SIBLING, axis);

        axis = Axis.lookup("preceding-sibling");
        assertEquals(Axis.PRECEDING_SIBLING, axis);

        axis = Axis.lookup(7);
        assertEquals(Axis.FOLLOWING, axis);

        axis = Axis.lookup("following");
        assertEquals(Axis.FOLLOWING, axis);

        axis = Axis.lookup(8);
        assertEquals(Axis.PRECEDING, axis);

        axis = Axis.lookup("preceding");
        assertEquals(Axis.PRECEDING, axis);

        axis = Axis.lookup(9);
        assertEquals(Axis.ATTRIBUTE, axis);

        axis = Axis.lookup("attribute");
        assertEquals(Axis.ATTRIBUTE, axis);

        axis = Axis.lookup(10);
        assertEquals(Axis.NAMESPACE, axis);

        axis = Axis.lookup("namespace");
        assertEquals(Axis.NAMESPACE, axis);

        axis = Axis.lookup(11);
        assertEquals(Axis.SELF, axis);

        axis = Axis.lookup("self");
        assertEquals(Axis.SELF, axis);

        axis = Axis.lookup(12);
        assertEquals(Axis.DESCENDANT_OR_SELF, axis);

        axis = Axis.lookup("descendant-or-self");
        assertEquals(Axis.DESCENDANT_OR_SELF, axis);

        axis = Axis.lookup(13);
        assertEquals(Axis.ANCESTOR_OR_SELF, axis);

        axis = Axis.lookup("ancestor-or-self");
        assertEquals(Axis.ANCESTOR_OR_SELF, axis);

        axis = Axis.lookup(-1);
        assertNull(axis);

        axis = Axis.lookup("invalid");
        assertNull(axis);

        axis = Axis.lookup(14);
        assertNull(axis);
    }
}