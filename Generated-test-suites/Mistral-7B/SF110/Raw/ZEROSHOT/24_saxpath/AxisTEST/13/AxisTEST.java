package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupInteger() {
 int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 for (int i = 0; i < expectedValues.length; i++) {
 String axisName = Axis.lookup(i);
 int actualValue = Axis.lookup(axisName);
 assertEquals("Test case for lookup integer and string: " + i, expectedValues[i], actualValue);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupInvalidAxis() {
 int axisNum = Integer.MIN_VALUE;
 String axisName = Axis.lookup(axisNum);
 fail("Expected NullPointerException, but got: " + axisName);
 }

 @Test
 public void testLookupString() {
 String[] expectedAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 for (int i = 0; i < expectedAxisNames.length; i++) {
 int actualAxisNum = Axis.lookup(expectedAxisNames[i]);
 assertEquals("Test case for lookup string: " + expectedAxisNames[i], i+1, actualAxisNum);
 }
 }
}