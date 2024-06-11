package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i < Axis.values().length; i++) {
            Axis axis = Axis.values()[i];
            int result = Axis.lookup(axis.getAxisNumber());
            assertEquals(axis.getAxisNumber(), result);
        }

        assertNull(Axis.lookup(Integer.MIN_VALUE));
        assertNull(Axis.lookup(Integer.MAX_VALUE));
    }

    @Test
    public void testLookupByAxisName() {
        for (Axis axis : Axis.values()) {
            int result = Axis.lookup(axis.name());
            assertEquals(axis.getAxisNumber(), result);
        }

        assertNull(Axis.lookup(null));
        assertNull(Axis.lookup("INVALID"));
    }

    enum Axis {
        CHILD(1, "child"),
        DESCENDANT(2, "descendant"),
        PARENT(3, "parent"),
        ANCESTOR(4, "ancestor"),
        FOLLOWING_SIBLING(5, "following-sibling"),
        PRECEDING_SIBLING(6, "preceding-sibling"),
        FOLLOWING(7, "following"),
        PRECEDING(8, "preceding"),
        ATTRIBUTE(9, "attribute"),
        NAMESPACE(10, "namespace"),
        SELF(11, "self"),
        DESCENDANT_OR_SELF(12, "descendant-or-self"),
        ANCESTOR_OR_SELF(13, "ancestor-or-self"),
        INVALID(0, null);

        private final int axisNumber;
        private final String axisName;

        Axis(int axisNumber, String axisName) {
            this.axisNumber = axisNumber;
            this.axisName = axisName;
        }

        public int getAxisNumber() {
            return axisNumber;
        }

        public String name() {
            return axisName;
        }
    }
}