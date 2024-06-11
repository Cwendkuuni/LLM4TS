package org.saxpath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

 @Test
 public void testLookupIntValidAxis() {
 int axisNum;
 String expectedAxisName;

 axisNum = 1;
 expectedAxisName = "child";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 2;
 expectedAxisName = "descendant";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 3;
 expectedAxisName = "parent";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 4;
 expectedAxisName = "ancestor";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 5;
 expectedAxisName = "following-sibling";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 6;
 expectedAxisName = "preceding-sibling";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 7;
 expectedAxisName = "following";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 8;
 expectedAxisName = "preceding";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 9;
 expectedAxisName = "attribute";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 10;
 expectedAxisName = "namespace";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 11;
 expectedAxisName = "self";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 12;
 expectedAxisName = "descendant-or-self";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));

 axisNum = 13;
 expectedAxisName = "ancestor-or-self";
 assertEquals(expectedAxisName, Axis.lookup(axisNum));
 }

 @Test
 public void testLookupIntInvalidAxis() {
 int axisNum = 100;
 assertNull(Axis.lookup(axisNum));
 }

 @Test
 public void testLookupStringValidAxis() {
 String axisName;
 int expectedAxisNum;

 axisName = "child";
 expectedAxisNum = 1;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "descendant";
 expectedAxisNum = 2;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "parent";
 expectedAxisNum = 3;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "ancestor";
 expectedAxisNum = 4;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "following-sibling";
 expectedAxisNum = 5;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "preceding-sibling";
 expectedAxisNum = 6;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "following";
 expectedAxisNum = 7;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "preceding";
 expectedAxisNum = 8;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "attribute";
 expectedAxisNum = 9;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "namespace";
 expectedAxisNum = 10;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "self";
 expectedAxisNum = 11;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "descendant-or-self";
 expectedAxisNum = 12;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));

 axisName = "ancestor-or-self";
 expectedAxisNum = 13;
 assertEquals(expectedAxisNum, Axis.lookup(axisName));
 }

 @Test
 public void testLookupStringInvalidAxis() {
 String axisName = "invalid";
 assertNull(Axis.lookup(axisName));
 }
}