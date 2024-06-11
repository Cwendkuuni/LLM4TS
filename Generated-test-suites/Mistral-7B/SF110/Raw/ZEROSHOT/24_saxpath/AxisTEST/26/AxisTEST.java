package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookup_ValidAxisNumbers_ReturnsExpectedString() {
 for (int axisNum = 1; axisNum <= 13; axisNum++) {
 String expected = Axis.lookup(axisNum);
 assertEquals(expected, Axis.lookup(axisNum));
 }
 }

 @Test
 public void testLookup_ValidAxisNames_ReturnsExpectedInt() {
 int[] expectedInts = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 String[] expectedStrings = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 for (int i = 0; i < expectedStrings.length; i++) {
 int axisNum = Axis.lookup(expectedStrings[i]);
 int expectedInt = expectedInts[i];
 assertEquals(expectedInt, axisNum);
 assertEquals(expectedStrings[i], Axis.lookup(expectedInt));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookup_InvalidAxisNamesOrNumbers_ReturnsNull() {
 assertNull(Axis.lookup(-1));
 assertNull(Axis.lookup("not_an_axis"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookup_InvalidAxisNumber_ThrowsIllegalArgumentException() {
 assertNull(Axis.lookup(0));
 }
}