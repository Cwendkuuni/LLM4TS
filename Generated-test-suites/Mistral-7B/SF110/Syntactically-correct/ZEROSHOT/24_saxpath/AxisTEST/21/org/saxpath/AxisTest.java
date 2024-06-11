package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

 @Test
 public void testLookupAxisNumber() {
 int axisNumber = Axis.CHILD;
 String expected = "child";
 String result = Axis.lookup(axisNumber);
 assertEquals(expected, result);

 axisNumber = Axis.DESCENDANT;
 expected = "descendant";
 result = Axis.lookup(axisNumber);
 assertEquals(expected, result);

 // Testing invalid axis number
 axisNumber = Axis.INVALID_AXIS;
 result = Axis.lookup(axisNumber);
 assertNull(result);
 }

 @Test
 public void testLookupAxisName() {
 String axisName = "child";
 int expected = Axis.CHILD;
 int result = Axis.lookup(axisName);
 assertEquals(expected, result);

 axisName = "descendant";
 expected = Axis.DESCENDANT;
 result = Axis.lookup(axisName);
 assertEquals(expected, result);

 // Testing unknown axis name
 axisName = "unknown";
 result = Axis.lookup(axisName);
 assertEquals(Axis.INVALID_AXIS, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupNegativeAxisNumber() {
 int axisNumber = -1;
 try {
 Axis.lookup(axisNumber);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupNullAxisNumber() {
 int axisNumber = 0;
 try {
 Axis.lookup(axisNumber);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupNullAxisName() {
 String axisName = null;
 try {
 Axis.lookup(axisName);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }
}