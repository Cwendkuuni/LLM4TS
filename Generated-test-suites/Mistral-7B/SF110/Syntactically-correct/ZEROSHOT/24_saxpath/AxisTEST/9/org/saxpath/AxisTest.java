package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupAxisNumber() {
 int axisNumber = Axis.CHILD;
 String expected = "child";
 String actual = Axis.lookup(axisNumber);

 assertTrue("Expected 'child' but got " + actual, expected.equals(actual));

 axisNumber = Axis.DESCENDANT;
 expected = "descendant";
 actual = Axis.lookup(axisNumber);

 assertTrue("Expected 'descendant' but got " + actual, expected.equals(actual));

 // Testing default case
 axisNumber = 14;
 actual = Axis.lookup(axisNumber);

 assertNull("Expected null but got " + actual, actual);
 }

 @Test
 public void testLookupAxisName() {
 String axisName = "child";
 int expected = Axis.CHILD;
 int actual = Axis.lookup(axisName);

 assertEquals("Expected 'CHILD' but got " + actual, expected, actual);

 axisName = "descendant";
 expected = Axis.DESCENDANT;
 actual = Axis.lookup(axisName);

 assertEquals("Expected 'DESCENDANT' but got " + actual, expected, actual);

 // Testing non-existent axis
 axisName = "test";
 actual = Axis.lookup(axisName);

 assertEquals("Expected 'INVALID_AXIS' but got " + actual, Axis.INVALID_AXIS, actual);
 }
}