package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupIntValidAxis() {
 int axisNum = 1;
 String expected = "child";
 String actual = Axis.lookup(axisNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testLookupIntInvalidAxis() {
 int axisNum = 14;
 String expected = null;
 String actual = Axis.lookup(axisNum);
 Assert.assertNull(actual);
 }

 @Test
 public void testLookupStringValidAxis() {
 String axisName = "child";
 int expected = 1;
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);
 }

 @Test
 public void testLookupStringInvalidAxis() {
 String axisName = "invalidAxis";
 int expected = 0;
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);
 }

 @Test
 public void testLookupStringCaseInsensitive() {
 String axisName = "CHILD";
 int expected = 1;
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testNullAxisName() {
 String axisName = null;
 int actual = Axis.lookup(axisName);
 fail("Expected NullPointerException, but got: " + actual);
 }
}