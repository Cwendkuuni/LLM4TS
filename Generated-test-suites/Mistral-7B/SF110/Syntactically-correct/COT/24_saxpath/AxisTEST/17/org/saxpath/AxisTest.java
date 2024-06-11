package org.saxpath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class AxisTest {

 private final static String[] VALID_AXIS_NAMES = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };

 @Test
 public void testLookup_ValidAxisNumber() {
 for (int axisNum : VALID_AXIS_NUMBERS) {
 String result = Axis.lookup(axisNum);
 int expectedAxisNum = axisNum;
 assertEquals("Incorrect lookup result for valid axis number: " + axisNum, expectedAxisNum, Axis.lookup(axisNum));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookup_InvalidAxisNumber() {
 assertNull("Should return null for invalid axis number", Axis.lookup(-1));
 }

 @Test(expected = NullPointerException.class)
 public void testLookup_NullAxisName() {
 assertNull("Should return null for null axis name", Axis.lookup(null));
 }

 @Test
 public void testLookup_InvalidAxisName() {
 for (String invalidAxisName : INVALID_AXIS_NAMES) {
 String result = Axis.lookup(invalidAxisName);
 assertNull("Should return null for invalid axis name: " + invalidAxisName, result);
 }
 }

 @Test
 public void testLookup_CaseInsensitiveAxisName() {
 for (String axisName : VALID_AXIS_NAMES) {
 String lowerCaseAxisName = axisName.toLowerCase();
 String result = Axis.lookup(lowerCaseAxisName);
 int expectedAxisNum = Axis.lookup(axisName);
 assertEquals("Incorrect lookup result for case insensitive axis name: " + lowerCaseAxisName, expectedAxisNum, result);
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testLookup_AxisNameWithInvalidCharacters() {
 String invalidAxisName = "child#";
 assertNull("Should return null for invalid axis name with special characters: " + invalidAxisName, Axis.lookup(invalidAxisName));
 }

 private final static int[] VALID_AXIS_NUMBERS = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
 private final static List<String> INVALID_AXIS_NAMES = Arrays.asList("invalidAxis", "anotherInvalidAxis");

}