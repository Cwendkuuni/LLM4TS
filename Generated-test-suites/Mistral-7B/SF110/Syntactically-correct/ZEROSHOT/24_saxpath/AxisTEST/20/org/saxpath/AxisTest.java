package org.saxpath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 String expectedAxisName;
 int axisNumber;
 String actualAxisName;

 // Test valid axis numbers
 for (int i = 1; i <= 13; i++) {
 axisNumber = i;
 expectedAxisName = Axis.lookup(axisNumber);
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);
 }

 // Test invalid axis number
 axisNumber = 0;
 actualAxisName = Axis.lookup(axisNumber);
 assertNull(actualAxisName);
 }

 @Test
 public void testLookupByAxisName() {
 int expectedAxisNumber;
 String axisName;
 int actualAxisNumber;

 // Test valid axis names
 axisName = "child";
 expectedAxisNumber = Axis.CHILD;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "descendant";
 expectedAxisNumber = Axis.DESCENDANT;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "parent";
 expectedAxisNumber = Axis.PARENT;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "ancestor";
 expectedAxisNumber = Axis.ANCESTOR;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "following-sibling";
 expectedAxisNumber = Axis.FOLLOWING_SIBLING;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "preceding-sibling";
 expectedAxisNumber = Axis.PRECEDING_SIBLING;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "following";
 expectedAxisNumber = Axis.FOLLOWING;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "preceding";
 expectedAxisNumber = Axis.PRECEDING;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "attribute";
 expectedAxisNumber = Axis.ATTRIBUTE;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "namespace";
 expectedAxisNumber = Axis.NAMESPACE;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "self";
 expectedAxisNumber = Axis.SELF;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "descendant-or-self";
 expectedAxisNumber = Axis.DESCENDANT_OR_SELF;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "ancestor-or-self";
 expectedAxisNumber = Axis.ANCESTOR_OR_SELF;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 // Test invalid axis names
 axisName = null;
 assertNull(Axis.lookup(axisName));

 axisName = "";
 assertNull(Axis.lookup(axisName));

 axisName = "invalidAxisName";
 assertNull(Axis.lookup(axisName));
 }
}