package org.saxpath.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class AxisTest {

 @Test
 public void testLookupIntValidAxis() {
 for (int axisNum = 1; axisNum <= 13; axisNum++) {
 String expected = Axis.lookup(axisNum);
 assertEquals(expected, Axis.lookup(axisNum));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupIntInvalidAxis() {
 String result = Axis.lookup(0);
 assertNull(result);
 }

 @Test
 public void testLookupStringValidAxis() {
 for (String axisName : new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"}) {
 int expected = Axis.lookup(axisName);
 int result = Axis.lookup(axisName);
 assertEquals(expected, result);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupStringInvalidAxis() {
 String result = Axis.lookup("invalidAxisName");
 fail("Expected IllegalArgumentException");
 }
}