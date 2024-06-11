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
 for (axisNumber = 1; axisNumber <= 13; axisNumber++) {
 expectedAxisName = Axis.lookup(axisNumber);
 actualAxisName = Axis.lookup(axisNumber);
 assertEquals(expectedAxisName, actualAxisName);
 }

 // Test invalid axis number
 String invalidAxisName = Axis.lookup(0);
 assertNull(invalidAxisName);
 }

 @Test
 public void testLookupByAxisName() {
 int expectedAxisNumber;
 String axisName;
 int actualAxisNumber;

 // Test valid axis names
 axisName = "child";
 expectedAxisNumber = 1;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "descendant";
 expectedAxisNumber = 2;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "parent";
 expectedAxisNumber = 3;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "ancestor";
 expectedAxisNumber = 4;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "following-sibling";
 expectedAxisNumber = 5;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "preceding-sibling";
 expectedAxisNumber = 6;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "following";
 expectedAxisNumber = 7;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "preceding";
 expectedAxisNumber = 8;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "attribute";
 expectedAxisNumber = 9;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "namespace";
 expectedAxisNumber = 10;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "self";
 expectedAxisNumber = 11;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "descendant-or-self";
 expectedAxisNumber = 12;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "ancestor-or-self";
 expectedAxisNumber = 13;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 // Test invalid axis name
 axisName = "invalid";
 int invalidAxisNumber = Axis.lookup(axisName);
 assertEquals(Axis.INVALID_AXIS, invalidAxisNumber);
 }
}