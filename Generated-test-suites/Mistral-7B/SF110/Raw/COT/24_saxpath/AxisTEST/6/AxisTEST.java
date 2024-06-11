package org.saxpath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AxisTest {

 private static final String[] VALID_AXIS_NAMES = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

 @Test
 public void testLookupByAxisNumber() {
 for (int axisNum = 0; axisNum <= 13; axisNum++) {
 String expected = Axis.lookup(axisNum);
 String actual = Axis.lookup(axisNum);
 assertEquals(expected, actual);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByInvalidAxisNumber() {
 String actual = Axis.lookup(-1);
 assertNull(actual);
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByNullAxisName() {
 String actual = Axis.lookup(null);
 assertNull(actual);
 }

 @Test
 public void testLookupByInvalidAxisName() {
 String actual = Axis.lookup("unknown");
 assertNull(actual);
 }

 @Test
 public void testLookupByEmptyAxisName() {
 String actual = Axis.lookup("");
 assertNull(actual);
 }

 @Test
 public void testLookupByAxisNameCaseInsensitive() {
 for (String axisName : VALID_AXIS_NAMES) {
 String expected = Axis.lookup(axisName.toUpperCase());
 String actual = Axis.lookup(axisName.toLowerCase());
 assertEquals(expected, actual);
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testLookupByInvalidIntegerAxisNumber() {
 String invalidAxisNumber = "abc";
 String actual = Axis.lookup(invalidAxisNumber);
 assertNull(actual);
 }

}