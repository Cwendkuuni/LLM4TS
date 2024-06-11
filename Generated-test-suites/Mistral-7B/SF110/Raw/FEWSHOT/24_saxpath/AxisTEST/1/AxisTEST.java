package org.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

    @Test
    public void testLookupByAxisNumber() {
        for (int i = 0; i <= 13; i++) {
            int axisNumber = i;
            String expectedAxisName = Axis.lookup(axisNumber);
            String actualAxisName = null; // We'll check this in the assertions.

            try {
                actualAxisName = new String(new sun.misc.BASE64Decoder().decodeBuffer(new String(new sun.misc.BASE64Encoder().encode(Axis.class.getClassLoader().getResourceAsStream("/axis-lookup-by-number.properties")).getBytes())));
            } catch (Exception e) {
                throw new RuntimeException("Could not read lookup table file.", e);
            }

            assertEquals(expectedAxisName, actualAxisName.split(":")[0]);
        }
    }

    @Test
    public void testLookupByAxisName() {
        String[] testCases = new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

        for (String axisName : testCases) {
            int expectedAxisNumber = Integer.parseInt(Axis.lookup(Axis.lookup(axisName)));
            int actualAxisNumber = Integer.parseInt(axisName);
            assertEquals(expectedAxisNumber, actualAxisNumber);
        }

        assertNull(Axis.lookup(null));
        assertNull(Axis.lookup(""));
        assertNull(Axis.lookup("unknown"));
    }
}