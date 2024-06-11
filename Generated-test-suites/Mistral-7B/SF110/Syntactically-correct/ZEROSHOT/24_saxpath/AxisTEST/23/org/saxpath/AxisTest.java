package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;

import static org.saxpath.Axis.CHILD;
import static org.saxpath.Axis.DESCENDANT;
import static org.saxpath.Axis.DESCENDANT_OR_SELF;
import static org.saxpath.Axis.ANCESTOR;
import static org.saxpath.Axis.ANCESTOR_OR_SELF;
import static org.saxpath.Axis.FOLLOWING_SIBLING;
import static org.saxpath.Axis.PRECEDING_SIBLING;
import static org.saxpath.Axis.FOLLOWING;
import static org.saxpath.Axis.PRECEDING;
import static org.saxpath.Axis.ATTRIBUTE;
import static org.saxpath.Axis.NAMESPACE;
import static org.saxpath.Axis.SELF;

public class AxisTest {

 @Test
 public void testLookupByNumber() {
 int[] axisNumbers = { INVALID_AXIS, CHILD, DESCENDANT, PARENT, ANCESTOR, FOLLOWING_SIBLING, PRECEDING_SIBLING, FOLLOWING, PRECEDING, ATTRIBUTE, NAMESPACE, SELF, DESCENDANT_OR_SELF, ANCESTOR_OR_SELF };
 String[] expectedStrings = { null, "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };

 for (int i = 0; i < axisNumbers.length; i++) {
 String result = Axis.lookup(axisNumbers[i]);
 assertEquals(expectedStrings[i], result);
 }
 }

 @Test
 public void testLookupByString() {
 String[] axisStrings = { null, "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
 int[] expectedNumbers = { INVALID_AXIS, CHILD, DESCENDANT, PARENT, ANCESTOR, FOLLOWING_SIBLING, PRECEDING_SIBLING, FOLLOWING, PRECEDING, ATTRIBUTE, NAMESPACE, SELF, DESCENDANT_OR_SELF, ANCESTOR_OR_SELF };

 for (int i = 0; i < axisStrings.length; i++) {
 int result = Axis.lookup(axisStrings[i]);
 assertEquals(expectedNumbers[i], result);
 }
 }
}