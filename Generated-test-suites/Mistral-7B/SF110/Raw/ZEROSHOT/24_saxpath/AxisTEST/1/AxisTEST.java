package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupAxisNumber() {
 int axisNumber = Axis.CHILD;
 String expectedAxisName = "child";
 String actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.DESCENDANT;
 expectedAxisName = "descendant";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 // Test for invalid axis number
 axisNumber = Axis.INVALID_AXIS;
 assertNull(Axis.lookup(axisNumber));
 }

 @Test
 public void testLookupAxisName() {
 String axisName = "child";
 int expectedAxisNumber = Axis.CHILD;
 int actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "descendant";
 expectedAxisNumber = Axis.DESCENDANT;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 // Test for invalid axis name
 axisName = "invalid";
 assertNull(Axis.lookup(axisName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAxisNumber() {
 int axisNumber = 14;
 Axis.lookup(axisNumber);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAxisName() {
 String axisName = "invalid";
 Axis.lookup(axisName);
 }
}