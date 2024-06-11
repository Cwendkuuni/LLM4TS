package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;

import static org.saxpath.Axis.CHILD;
import static org.saxpath.Axis.DESCENDANT;
import static org.saxpath.Axis.DESCENDANT_OR_SELF;
import static org.saxpath.Axis.FOLLOWING;
import static org.saxpath.Axis.FOLLOWING_SIBLING;
import static org.saxpath.Axis.ANCESTOR;
import static org.saxpath.Axis.ANCESTOR_OR_SELF;
import static org.saxpath.Axis.ATTRIBUTE;
import static org.saxpath.Axis.INVALID_AXIS;
import static org.saxpath.Axis.NAMESPACE;
import static org.saxpath.Axis.PARENT;
import static org.saxpath.Axis.PRECEDING;
import static org.saxpath.Axis.PRECEDING_SIBLING;
import static org.saxpath.Axis.SELF;

public class AxisTest {

 @Test
 public void testLookupByInt() {
 String[] expectedValues = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
 for (int axisNum = 0; axisNum < Axis.values().length; axisNum++) {
 String actualValue = Axis.lookup(axisNum);
 assertEquals(expectedValues[axisNum], actualValue);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByInvalidInt() {
 String actualValue = Axis.lookup(Integer.MIN_VALUE);
 fail("Expected a NullPointerException, but got: " + actualValue);
 }

 @Test
 public void testLookupByString() {
 String[] expectedValues = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
 for (String axisName : expectedValues) {
 int actualValue = Axis.lookup(axisName);
 assertEquals(axisNumToInt(axisName), actualValue);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupByInvalidString() {
 String actualValue = Axis.lookup("invalidAxis");
 fail("Expected an IllegalArgumentException, but got: " + actualValue);
 }

 private int axisNumToInt(String axisName) {
 switch (axisName) {
 case "child":
 return CHILD;
 case "descendant":
 return DESCENDANT;
 case "parent":
 return PARENT;
 case "ancestor":
 return ANCESTOR;
 case "following-sibling":
 return FOLLOWING_SIBLING;
 case "preceding-sibling":
 return PRECEDING_SIBLING;
 case "following":
 return FOLLOWING;
 case "preceding":
 return PRECEDING;
 case "attribute":
 return ATTRIBUTE;
 case "namespace":
 return NAMESPACE;
 case "self":
 return SELF;
 case "descendant-or-self":
 return DESCENDANT_OR_SELF;
 case "ancestor-or-self":
 return ANCESTOR_OR_SELF;
 default:
 return INVALID_AXIS;
 }
 }
}