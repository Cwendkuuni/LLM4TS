package org.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupValidAxisNum() {
 int axisNum;
 String expectedAxisName;
 String actualAxisName;

 for (axisNum = CHILD; axisNum <= ANCESTOR_OR_SELF; axisNum++) {
 expectedAxisName = Axis.lookup(axisNum);
 actualAxisName = Axis.lookup(axisNum);
 assertEquals(expectedAxisName, actualAxisName);
 }
 }

 @Test
 public void testLookupValidAxisName() {
 String axisName;
 int expectedAxisNum;
 int actualAxisNum;

 for (axisName = "child"; axisName <= "ancestor-or-self"; axisName++) {
 expectedAxisNum = Axis.lookup(axisName.hashCode());
 actualAxisNum = Axis.lookup(axisName);
 assertEquals(expectedAxisNum, actualAxisNum);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupInvalidAxisNumOrName() {
 int invalidAxisNum = 14;
 String invalidAxisName = "invalid";
 String axisName;
 int axisNum;

 for (axisName = "child"; axisName <= "ancestor-or-self"; axisName++) {
 axisNum = Axis.lookup(axisName.hashCode());
 if (axisNum == 14) {
 continue;
 }
 String expectedAxisName = Axis.lookup(axisNum);
 assertNotEquals(expectedAxisName, null);
 }

 String nullAxisName = null;
 int lookupResult = Axis.lookup(nullAxisName);
 assertNull(lookupResult);

 axisNum = INVALID_AXIS;
 expectedAxisName = Axis.lookup(axisNum);
 assertNull(expectedAxisName);
 }
}