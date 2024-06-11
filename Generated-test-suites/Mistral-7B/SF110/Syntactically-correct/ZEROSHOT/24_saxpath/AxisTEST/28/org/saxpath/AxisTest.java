package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupInt() {
 int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 for (int i = 0; i < expected.length; i++) {
 int result = Axis.lookup(i);
 assertEquals("Unexpected result for axis number " + i, expected[i], result);
 }
 }

 @Test
 public void testLookupStringChild() {
 String result = Axis.lookup(Axis.CHILD);
 assertEquals("Unexpected result for 'child' axis", "child", result);
 }

 @Test
 public void testLookupStringDescendant() {
 String result = Axis.lookup(Axis.DESCENDANT);
 assertEquals("Unexpected result for 'descendant' axis", "descendant", result);
 }

 @Test
 public void testLookupStringParent() {
 String result = Axis.lookup(Axis.PARENT);
 assertEquals("Unexpected result for 'parent' axis", "parent", result);
 }

 @Test
 public void testLookupStringAncestor() {
 String result = Axis.lookup(Axis.ANCESTOR);
 assertEquals("Unexpected result for 'ancestor' axis", "ancestor", result);
 }

 @Test
 public void testLookupStringFollowingSibling() {
 String result = Axis.lookup(Axis.FOLLOWING_SIBLING);
 assertEquals("Unexpected result for 'following-sibling' axis", "following-sibling", result);
 }

 @Test
 public void testLookupStringPrecedingSibling() {
 String result = Axis.lookup(Axis.PRECEDING_SIBLING);
 assertEquals("Unexpected result for 'preceding-sibling' axis", "preceding-sibling", result);
 }

 @Test
 public void testLookupStringFollowing() {
 String result = Axis.lookup(Axis.FOLLOWING);
 assertEquals("Unexpected result for 'following' axis", "following", result);
 }

 @Test
 public void testLookupStringPreceding2() {
 String result = Axis.lookup(Axis.PRECEDING);
 assertEquals("Unexpected result for 'preceding' axis", "preceding", result);
 }

 @Test
 public void testLookupStringAttribute() {
 String result = Axis.lookup(Axis.ATTRIBUTE);
 assertEquals("Unexpected result for 'attribute' axis", "attribute", result);
 }

 @Test
 public void testLookupStringNamespace() {
 String result = Axis.lookup(Axis.NAMESPACE);
 assertEquals("Unexpected result for 'namespace' axis", "namespace", result);
 }

 @Test
 public void testLookupStringSelf() {
 String result = Axis.lookup(Axis.SELF);
 assertEquals("Unexpected result for 'self' axis", "self", result);
 }

 @Test
 public void testLookupStringDescendantOrSelf() {
 String result = Axis.lookup(Axis.DESCENDANT_OR_SELF);
 assertEquals("Unexpected result for 'descendant-or-self' axis", "descendant-or-self", result);
 }

 @Test
 public void testLookupStringAncestorOrSelf() {
 String result = Axis.lookup(Axis.ANCESTOR_OR_SELF);
 assertEquals("Unexpected result for 'ancestor-or-self' axis", "ancestor-or-self", result);
 }

 @Test(expected = NullPointerException.class)
 public void testLookupInvalidAxis() {
 String result = Axis.lookup(-1);
 fail("Did not throw NullPointerException for invalid axis number");
 }
}