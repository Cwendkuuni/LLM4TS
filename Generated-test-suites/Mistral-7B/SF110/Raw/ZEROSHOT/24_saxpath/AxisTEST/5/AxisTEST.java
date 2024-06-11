package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupAxisNumber() {
 int axisNumber = 1;
 String expected = "child";
 String actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 2;
 expected = "descendant";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 13;
 expected = "ancestor-or-self";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 0;
 assertNull(Axis.lookup(axisNumber));
 }

 @Test
 public void testLookupAxisName() {
 String axisName = "child";
 int expected = 1;
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "descendant-or-self";
 expected = 12;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "ancestor-or-self";
 expected = 13;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "invalidAxis";
 assertNull(Axis.lookup(axisName));
 }
}