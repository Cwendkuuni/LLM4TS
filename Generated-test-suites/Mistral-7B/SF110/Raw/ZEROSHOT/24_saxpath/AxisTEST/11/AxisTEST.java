package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;

import static org.saxpath.Axis.CHILD;
import static org.saxpath.Axis.DESCENDANT;
import static org.saxpath.Axis.DESCENDANT_OR_SELF;
import static org.saxpath.Axis.FOLLOWING;
import static org.saxpath.Axis.FOLLOWING_SIBlob;
import static org.saxpath.Axis.INVALID_AXIS;
import static org.saxpath.Axis.ANCESTOR;
import static org.saxpath.Axis.ANCESTOR_OR_SELF;
import static org.saxpath.Axis.NAMESPACE;
import static org.saxpath.Axis.PARENT;
import static org.saxpath.Axis.PRECEDING;
import static org.saxpath.Axis.PRECEDING_SIBlob;
import static org.saxpath.Axis.SELF;

public class AxisTest {

 @Test
 public void testLookupByInt() {
 String[] expectedValues = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
 int[] axisNums = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
 for (int i = 0; i < axisNums.length; i++) {
 String actualValue = Axis.lookup(axisNums[i]);
 assertEquals(expectedValues[i], actualValue);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByIntInvalidAxis() {
 String actualValue = Axis.lookup(INVALID_AXIS);
 assertNull(actualValue);
 }

 @Test
 public void testLookupByString() {
 String[] expectedValues = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
 String[] axisNames = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
 for (int i = 0; i < axisNames.length; i++) {
 int axisNum = Axis.lookup(axisNames[i]);
 assertEquals(expectedValues[i], Axis.lookup(axisNum));
 assertEquals(axisNum, Axis.lookup(expectedValues[i]));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupByStringInvalidAxis() {
 String actualValue = Axis.lookup("invalidAxis");
 assertNull(actualValue);
 }
}