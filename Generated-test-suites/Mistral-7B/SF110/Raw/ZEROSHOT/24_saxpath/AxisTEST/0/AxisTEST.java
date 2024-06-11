package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupValidAxisNumbers() {
 for (int i = 1; i <= 13; i++) {
 String name = Axis.lookup(i);
 int expected = i;
 int result = Axis.lookup(name);
 assertEquals("Unexpected result for axis number " + i, expected, result);
 }
 }

 @Test
 public void testLookupValidAxisNames() {
 String[] axisNames = { "child", "descendant", "parent", "ancestor",
 "following-sibling", "preceding-sibling", "following", "preceding",
 "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
 for (String name : axisNames) {
 int expected = Axis.lookup(name);
 int result = Axis.lookup(name);
 assertEquals("Unexpected result for axis name " + name, expected, result);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupInvalidAxisNumberOrName() {
 String invalidAxisName = "invalidName";
 String invalidAxisNumber = "14";
 int expected = 0;
 int result1 = Axis.lookup(Integer.parseInt(invalidAxisNumber));
 int result2 = Axis.lookup(invalidAxisName);
 assertEquals("Unexpected result for invalid axis number " + invalidAxisNumber, expected, result1);
 assertEquals("Unexpected result for invalid axis name " + invalidAxisName, expected, result2);
 }
}