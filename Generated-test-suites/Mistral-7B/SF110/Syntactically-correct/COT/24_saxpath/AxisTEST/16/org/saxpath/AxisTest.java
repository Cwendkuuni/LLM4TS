package org.saxpath.test;

import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

 private static final String[] VALID_AXIS_NAMES = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };
 private static final int[] VALID_AXIS_NUMBERS = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };

 @Test
 public void testLookupByAxisNumber() {
 for (int validAxisNumber : VALID_AXIS_NUMBERS) {
 String expected = Axis.lookup(validAxisNumber);
 String actual = Axis.lookup(validAxisNumber);
 Assert.assertEquals(expected, actual);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByInvalidAxisNumber() {
 String actual = Axis.lookup(-1);
 Assert.assertNull(actual);
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByNullAxisName() {
 String actual = Axis.lookup(null);
 Assert.assertNull(actual);
 }

 @Test
 public void testLookupByInvalidAxisName() {
 for (String invalidAxisName : new String[] { "", "invalidAxis" }) {
 int actual = Axis.lookup(invalidAxisName);
 int expected = Axis.INVALID_AXIS;
 Assert.assertEquals(expected, actual);
 }
 }

 @Test
 public void testLookupByUnknownAxisNameCaseInsensitive() {
 String actual = Axis.lookup("Descendant");
 int expected = Axis.DESCENDANT;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLookupByUnknownAxisNameWithDash() {
 String actual = Axis.lookup("following-sibling");
 int expected = Axis.FOLLOWING_SIBLING;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLookupByUnknownAxisNameWithHyphenAndCapitalLetter() {
 String actual = Axis.lookup("Following-Sibling");
 int expected = Axis.FOLLOWING_SIBLING;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLookupByUnknownAxisNameWithHyphenAndSpace() {
 String actual = Axis.lookup("Following - sibling");
 int expected = Axis.FOLLOWING_SIBLING;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLookupByZeroLengthString() {
 String actual = Axis.lookup("");
 int expected = Axis.INVALID_AXIS;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLookupByEmptyStringAsAxisNameAndNumber() {
 String actual = Axis.lookup("");
 int expected = Axis.INVALID_AXIS;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLookupByZeroAsAxisNumber() {
 String actual = Axis.lookup(0);
 int expected = Axis.INVALID_AXIS;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLookupByNegativeAxisNumber() {
 String actual = Axis.lookup(-1);
 int expected = Axis.INVALID_AXIS;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLookupByAxisNameWithTrailingSpace() {
 String actual = Axis.lookup("following sibling");
 int expected = Axis.FOLLOWING_SIBLING;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLookupByAxisNameContainingSpecialCharacter() {
 String actual = Axis.lookup("following#sibling");
 int expected = Axis.INVALID_AXIS;
 Assert.assertEquals(expected, actual);
 }
}