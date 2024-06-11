package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;

import static org.saxpath.Axis.*;

public class AxisTest {

 @Test
 public void testLookupInt() {
 int axisNum;
 String expected;

 // Test valid axes
 for (axisNum = CHILD; axisNum <= ANCESTOR_OR_SELF; axisNum++) {
 expected = lookup(axisNum);
 assertTrue("Invalid lookup result for axis number: " + axisNum, !isEmptyString(expected));
 }

 // Test invalid axis number
 axisNum = INVALID_AXIS;
 assertNull("Should return null for invalid axis number", lookup(axisNum));
 }

 @Test
 public void testLookupString() {
 String axisName;
 int expected;

 // Test valid axes
 axisName = "child";
 expected = CHILD;
 assertEquals("Invalid lookup result for axis name: child", expected, lookup(axisName));

 axisName = "descendant";
 expected = DESCENDANT;
 assertEquals("Invalid lookup result for axis name: descendant", expected, lookup(axisName));

 axisName = "parent";
 expected = PARENT;
 assertEquals("Invalid lookup result for axis name: parent", expected, lookup(axisName));

 axisName = "ancestor";
 expected = ANCESTOR;
 assertEquals("Invalid lookup result for axis name: ancestor", expected, lookup(axisName));

 axisName = "following-sibling";
 expected = FOLLOWING_SIBLING;
 assertEquals("Invalid lookup result for axis name: following-sibling", expected, lookup(axisName));

 axisName = "preceding-sibling";
 expected = PRECEDING_SIBLING;
 assertEquals("Invalid lookup result for axis name: preceding-sibling", expected, lookup(axisName));

 axisName = "following";
 expected = FOLLOWING;
 assertEquals("Invalid lookup result for axis name: following", expected, lookup(axisName));

 axisName = "preceding";
 expected = PRECEDING;
 assertEquals("Invalid lookup result for axis name: preceding", expected, lookup(axisName));

 axisName = "attribute";
 expected = ATTRIBUTE;
 assertEquals("Invalid lookup result for axis name: attribute", expected, lookup(axisName));

 axisName = "namespace";
 expected = NAMESPACE;
 assertEquals("Invalid lookup result for axis name: namespace", expected, lookup(axisName));

 axisName = "self";
 expected = SELF;
 assertEquals("Invalid lookup result for axis name: self", expected, lookup(axisName));

 axisName = "descendant-or-self";
 expected = DESCENDANT_OR_SELF;
 assertEquals("Invalid lookup result for axis name: descendant-or-self", expected, lookup(axisName));

 axisName = "ancestor-or-self";
 expected = ANCESTOR_OR_SELF;
 assertEquals("Invalid lookup result for axis name: ancestor-or-self", expected, lookup(axisName));

 // Test invalid axis name
 axisName = "invalid";
 assertNull("Should return null for invalid axis name", lookup(axisName));
 }

 private boolean isEmptyString(String str) {
 return str == null || str.isEmpty();
 }
}