package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import static org.saxpath.Axis.*;

public class AxisTest {

 @Test
 public void testLookupInt() {
 int[] axisValues = {CHILD, DESCENDANT, PARENT, ANCESTOR, FOLLOWING_SIBLING, PRECEDING_SIBLING, FOLLOWING, PRECEDING, ATTRIBUTE, NAMESPACE, SELF, DESCENDANT_OR_SELF, ANCESTOR_OR_SELF};
 for (int i = 0; i < axisValues.length; i++) {
 int axisNum = axisValues[i];
 String axisName = lookup(axisNum);
 assertEquals("Unexpected axis name for axis number: " + axisNum, getExpectedAxisName(axisNum), axisName);
 }
 }

 @Test
 public void testLookupString() {
 String[] axisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 for (String axisName : axisNames) {
 int axisNum = lookup(axisName);
 assertEquals("Unexpected axis number for axis name: " + axisName, getExpectedAxisNumber(axisName), axisNum);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testInvalidLookup() {
 int invalidAxisNum = 0;
 String invalidAxisName = null;
 String result1 = lookup(invalidAxisNum);
 String result2 = lookup(invalidAxisName);
 fail("Expected NullPointerException for invalid lookup");
 }

 private String getExpectedAxisName(int axisNumber) {
 return lookup(axisNumber);
 }

 private int getExpectedAxisNumber(String axisName) {
 return lookup(axisName);
 }
}