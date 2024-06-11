package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;

import static org.saxpath.Axis.*;

public class AxisTest {

 @Test
 public void testLookupIntValidAxis() {
 for (int i = 1; i <= 13; i++) {
 String expected = lookup(i);
 int actual = lookup(i);
 assertEquals("Expected valid axis name for integer value: " + i, expected, lookupName(actual));
 }
 }

 @Test
 public void testLookupIntInvalidAxis() {
 int actual = lookup(100);
 assertNull("Expected null for invalid axis integer value", actual);
 }

 @Test
 public void testLookupStringValidAxis() {
 String[] validAxes = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 for (String axis : validAxes) {
 int expected = lookup(axis);
 int actual = lookup(axis);
 assertEquals("Expected same integer value for valid axis names: " + axis, expected, actual);
 }
 }

 @Test
 public void testLookupStringInvalidAxis() {
 String invalidAxis = "invalidAxis";
 int actual = lookup(invalidAxis);
 assertEquals("Expected 0 for invalid axis name: " + invalidAxis, 0, actual);
 }

 @Test
 public void testLookupNameValidAxis() {
 for (int i = 1; i <= 13; i++) {
 String expected = lookup(i);
 String actual = lookupName(i);
 assertEquals("Expected same axis name for valid integer and string values: " + i, expected, actual);
 }
 }

 @Test
 public void testLookupNameInvalidAxis() {
 int invalidAxis = 100;
 String actual = lookupName(invalidAxis);
 assertNull("Expected null for invalid integer value", actual);
 }

 private String lookupName(int axis) {
 return Axis.lookup(axis);
 }

}