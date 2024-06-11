package org.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class AxisTest {

 @Test
 public void testLookupInt() {
 int axisNum;
 String result;

 // Valid axis numbers
 axisNum = Axis.CHILD;
 result = Axis.lookup(axisNum);
 assertEquals("child", result);

axisNum = Axis.DESCENDANT;
 result = Axis.lookup(axisNum);
 assertEquals("descendant", result);

axisNum = Axis.PARENT;
 result = Axis.lookup(axisNum);
 assertEquals("parent", result);

axisNum = Axis.ANCESTOR;
 result = Axis.lookup(axisNum);
 assertEquals("ancestor", result);

axisNum = Axis.FOLLOWING_SIBLING;
 result = Axis.lookup(axisNum);
 assertEquals("following-sibling", result);

axisNum = Axis.PRECEDING_SIBLING;
 result = Axis.lookup(axisNum);
 assertEquals("preceding-sibling", result);

axisNum = Axis.FOLLOWING;
 result = Axis.lookup(axisNum);
 assertEquals("following", result);

axisNum = Axis.PRECEDING;
 result = Axis.lookup(axisNum);
 assertEquals("preceding", result);

axisNum = Axis.ATTRIBUTE;
 result = Axis.lookup(axisNum);
 assertEquals("attribute", result);

axisNum = Axis.NAMESPACE;
 result = Axis.lookup(axisNum);
 assertEquals("namespace", result);

axisNum = Axis.SELF;
 result = Axis.lookup(axisNum);
 assertEquals("self", result);

axisNum = Axis.DESCENDANT_OR_SELF;
 result = Axis.lookup(axisNum);
 assertEquals("descendant-or-self", result);

axisNum = Axis.ANCESTOR_OR_SELF;
 result = Axis.lookup(axisNum);
 assertEquals("ancestor-or-self", result);

 // Invalid axis number
 axisNum = Axis.INVALID_AXIS;
 result = Axis.lookup(axisNum);
 assertNull(result);
 }

 @Test
 public void testLookupString() {
 String axisName;
 int result;

 // Valid axis names
 axisName = "child";
 result = Axis.lookup(axisName);
 assertEquals(Axis.CHILD, result);

axisName = "descendant";
 result = Axis.lookup(axisName);
 assertEquals(Axis.DESCENDANT, result);

axisName = "parent";
 result = Axis.lookup(axisName);
 assertEquals(Axis.PARENT, result);

axisName = "ancestor";
 result = Axis.lookup(axisName);
 assertEquals(Axis.ANCESTOR, result);

axisName = "following-sibling";
 result = Axis.lookup(axisName);
 assertEquals(Axis.FOLLOWING_SIBLING, result);

axisName = "preceding-sibling";
 result = Axis.lookup(axisName);
 assertEquals(Axis.PRECEDING_SIBLING, result);

axisName = "following";
 result = Axis.lookup(axisName);
 assertEquals(Axis.FOLLOWING, result);

axisName = "preceding";
 result = Axis.lookup(axisName);
 assertEquals(Axis.PRECEDING, result);

axisName = "attribute";
 result = Axis.lookup(axisName);
 assertEquals(Axis.ATTRIBUTE, result);

axisName = "namespace";
 result = Axis.lookup(axisName);
 assertEquals(Axis.NAMESPACE, result);

axisName = "self";
 result = Axis.lookup(axisName);
 assertEquals(Axis.SELF, result);

axisName = "descendant-or-self";
 result = Axis.lookup(axisName);
 assertEquals(Axis.DESCENDANT_OR_SELF, result);

axisName = "ancestor-or-self";
 result = Axis.lookup(axisName);
 assertEquals(Axis.ANCESTOR_OR_SELF, result);

 // Invalid axis names
 axisName = "InvalidAxis";
 result = Axis.lookup(axisName);
 assertEquals(Axis.INVALID_AXIS, result);
 }
}