package org.saxpath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

 @Test
 public void testLookupValidAxisNumbers() {
 for (int i = 1; i <= 13; i++) {
 String expected = Axis.lookup(i);
 int actual = Axis.lookup(i);
 assertEquals(expected, Axis.lookup(actual));
 }
 }

 @Test
 public void testLookupValidAxisNames() {
 String[] expectedNames = { "child", "descendant", "parent", "ancestor",
 "following-sibling", "preceding-sibling", "following", "preceding",
 "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
 for (int i = 0; i < expectedNames.length; i++) {
 String expected = expectedNames[i];
 int actual = Axis.lookup(expected);
 assertEquals(expected, Axis.lookup(actual));
 }
 }

 @Test
 public void testLookupInvalidAxisNumbers() {
 for (int i = 14; i <= 100; i++) {
 assertNull(Axis.lookup(i));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupNullAxisName() {
 Axis.lookup(null);
 }

 @Test
 public void testLookupEmptyAxisName() {
 assertNull(Axis.lookup(""));
 }

}