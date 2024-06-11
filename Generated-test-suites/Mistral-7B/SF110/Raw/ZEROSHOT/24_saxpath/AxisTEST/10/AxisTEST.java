package org.saxpath.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class AxisTest {

 @Test
 public void testLookupInt() {
 int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 for (int i = 0; i < expected.length; i++) {
 int axisNum = i + 1;
 String axisName = Axis.lookup(axisNum);
 int result = Axis.lookup(axisName);
 assertEquals(axisNum, result);
 }
 }

 @Test
 public void testLookupString() {
 String[] expected = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 for (String axisName : expected) {
 int result = Axis.lookup(axisName);
 assertEquals(Axis.getAxisValue(result), axisName);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testInvalidLookup() {
 int axisNum = Integer.MIN_VALUE;
 String result = Axis.lookup(axisNum);
 fail("Expected NullPointerException, but got: " + result);
 }

 @Test
 public void testGetAxisValue() {
 String[] expected = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 for (int axisNum = 1; axisNum <= 13; axisNum++) {
 String result = Axis.getAxisValue(axisNum);
 assertEquals(expected[axisNum - 1], result);
 }
 }

 private static String getAxisValue(int axisNum) {
 return Axis.lookup(axisNum);
 }

}