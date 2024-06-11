package org.saxpath.test;

import org.junit.Test;
import org.saxpath.Axis;
import static org.junit.Assert.assertEquals;

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

 axisNumber = 3;
 expected = "parent";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 4;
 expected = "ancestor";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 5;
 expected = "following-sibling";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 6;
 expected = "preceding-sibling";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 7;
 expected = "following";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 8;
 expected = "preceding";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 9;
 expected = "attribute";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 10;
 expected = "namespace";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 11;
 expected = "self";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 12;
 expected = "descendant-or-self";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 13;
 expected = "ancestor-or-self";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 14; // invalid axis number
 actual = Axis.lookup(axisNumber);
 assertEquals(null, actual);
 }

 @Test
 public void testLookupAxisName() {
 String axisName = "child";
 int expected = 1;
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "descendant";
 expected = 2;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "parent";
 expected = 3;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "ancestor";
 expected = 4;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "following-sibling";
 expected = 5;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "preceding-sibling";
 expected = 6;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "following";
 expected = 7;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "preceding";
 expected = 8;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "attribute";
 expected = 9;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "namespace";
 expected = 10;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "self";
 expected = 11;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "descendant-or-self";
 expected = 12;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "ancestor-or-self";
 expected = 13;
 actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "invalid"; // invalid axis name
 actual = Axis.lookup(axisName);
 assertEquals(0, actual);
 }
}