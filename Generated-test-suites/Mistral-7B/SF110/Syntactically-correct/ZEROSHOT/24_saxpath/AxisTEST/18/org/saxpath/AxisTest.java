package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

 @Test
 public void testLookupValidAxisNumbers() {
 int[] axisNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 String[] expectedStrings = {
 "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling",
 "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"
 };
 for (int i = 0; i < axisNumbers.length; i++) {
 String result = Axis.lookup(axisNumbers[i]);
 String expected = expectedStrings[i];
 assertEquals(expected, result);
 }
 }

 @Test
 public void testLookupValidAxisNames() {
 String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling",
 "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 int[] expectedAxisNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 for (int i = 0; i < axisNames.length; i++) {
 int result = Axis.lookup(axisNames[i]);
 int expected = expectedAxisNumbers[i];
 assertEquals(expected, result);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupInvalidAxisNumberOrName() {
 Integer invalidAxisNumber = 14;
 String invalidAxisName = "invalid-axis";
 String result1 = Axis.lookup(invalidAxisNumber);
 String result2 = Axis.lookup(invalidAxisName);
 Assert.assertNull(result1);
 Assert.assertNull(result2);
 }

}