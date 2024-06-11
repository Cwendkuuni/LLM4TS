package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.Pattern;

public class ValidateTest {

 @Test
 public void testIsTrueWithBooleanExpression() {
 Validate.isTrue(true, "Expected true but got false");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseBooleanExpression() {
 Validate.isTrue(false, "Expected true but got false");
 }

 @Test
 public void testIsTrueWithDoubleValue() {
 Validate.isTrue(1.0 > 0.0, "Expected true but got false");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseDoubleExpression() {
 Validate.isTrue(1.0 < 0.0, "Expected true but got false");
 }

 @Test
 public void testIsTrueWithObjectArray() {
 Object[] arr = new Object[] { 1, "one", true };
 Validate.isTrue(arr.length > 0, "Expected true but got false");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseObjectArray() {
 Object[] arr = new Object[0];
 Validate.isTrue(arr.length > 0, "Expected true but got false");
 }

 @Test
 public void testIsTrueWithoutMessageOrValues() {
 Validate.isTrue(true);
 }

 // --------------------------------------------------------

 @Test
 public void testNotNullWithNullObject() {
 try {
 Validate.notNull(null, "Message", new Object[] { "Index" });
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testNotNullWithNotNullObject() {
 Object obj = new Object();
 Validate.notNull(obj);
 }

 // --------------------------------------------------------

 @Test
 public void testNotEmptyWithEmptyArray() {
 try {
 Validate.notEmpty((Object[]) null, "Message", new Object[] { "Index" });
 fail();
 } catch (NullPointerException e) {
 // expected
 }

 try {
 Validate.notEmpty(new Object[0], "Message", new Object[] { "Index" });
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNotEmptyWithNonEmptyArray() {
 Object[] arr = new Object[] { 1, "one", true };
 Validate.notEmpty(arr);
 }

 // --------------------------------------------------------

 @Test
 public void testNoNullElementsWithNullArray() {
 try {
 Validate.noNullElements((Object[]) null, "Message", new Object[] { "Index" });
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testNoNullElementsWithArrayContainingNull() {
 Object[] arr = new Object[] { 1, null, "one", true };
 try {
 Validate.noNullElements(arr, "Message", new Object[] { "Index" });
 fail();
 } catch (IllegalArgumentException e) {
 String msg = e.getMessage();
 assertEquals("The validated array contains null element at index: 1", msg);
 }
 }

 @Test
 public void testNoNullElementsWithArrayContainingOnlyNulls() {
 Object[] arr = new Object[2];
 arr[0] = null;
 arr[1] = null;
 try {
 Validate.noNullElements(arr, "Message", new Object[] { "Index" });
 fail();
 } catch (IllegalArgumentException e) {
 String msg = e.getMessage();
 assertEquals("The validated array contains null element at index: 0", msg);
 }
 }

 @Test
 public void testNoNullElementsWithArrayContainingNoNulls() {
 Object[] arr = new Object[] { 1, "one", true };
 Validate.noNullElements(arr);
 }

 // --------------------------------------------------------

 @Test
 public void testValidIndexWithInvalidArrayIndex() {
 Object[] arr = new Object[] { 1, "one", true };
 try {
 Validate.validIndex(arr, 3, "Message", new Object[] { "Index" });
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testValidIndexWithValidArrayIndex() {
 Object[] arr = new Object[] { 1, "one", true };
 Validate.validIndex(arr, 0);
 }

 // --------------------------------------------------------

 @Test
 public void testValidStateWithFalseBooleanExpression() {
 try {
 Validate.validState(false);
 fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testValidStateWithTrueBooleanExpression() {
 Validate.validState(true);
 }

 // --------------------------------------------------------

 @Test
 public void testMatchesPatternWithValidString() {
 String input = "abcd1234efgh";
 Validate.matchesPattern(input, "ab[cd]\\d+ef[gh]");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPatternWithInvalidString() {
 String input = "abcd1234efgh";
 Validate.matchesPattern(input, "ab[cd]\\d+ef[gh]x");
 }

 @Test
 public void testMatchesPatternWithMessage() {
 String input = "abcd1234efgh";
 try {
 Validate.matchesPattern(input, "ab[cd]\\d+ef[gh]", "Message", new Object[] { "Input" });
 fail();
 } catch (IllegalArgumentException e) {
 String msg = e.getMessage();
 assertEquals("The string Input does not match the pattern ab[cd]\\d+ef[gh]x", msg);
 }
 }

 // --------------------------------------------------------

 @Test
 public void testInclusiveBetweenWithLongValues() {
 Validate.inclusiveBetween(0L, 10L, 5L);
 Validate.inclusiveBetween(0L, 10L, 15L, "Message", new Object[] { "Value" });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenWithLongValuesOutOfRange() {
 Validate.inclusiveBetween(0L, 10L, 15L);
 }

 @Test
 public void testInclusiveBetweenWithDoubleValues() {
 Validate.inclusiveBetween(0.0, 10.0, 5.5);
 Validate.inclusiveBetween(0.0, 10.0, 15.5, "Message", new Object[] { "Value" });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenWithDoubleValuesOutOfRange() {
 Validate.inclusiveBetween(0.0, 10.0, 15.5);
 }

 // --------------------------------------------------------

 @Test
 public void testExclusiveBetweenWithLongValues() {
 Validate.exclusiveBetween(0L, 10L, 5L);
 Validate.exclusiveBetween(0L, 10L, 4L, "Message", new Object[] { "Value" });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenWithLongValuesOutOfRange() {
 Validate.exclusiveBetween(0L, 10L, 4L);
 }

 @Test
 public void testExclusiveBetweenWithDoubleValues() {
 Validate.exclusiveBetween(0.0, 10.0, 5.5);
 Validate.exclusiveBetween(0.0, 10.0, 4.5, "Message", new Object[] { "Value" });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenWithDoubleValuesOutOfRange() {
 Validate.exclusiveBetween(0.0, 10.0, 4.5);
 }

 // --------------------------------------------------------

 @Test
 public void testIsInstanceOf() {
 Object obj = new String("Hello");
 Validate.isInstanceOf(String.class, obj);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOfWithInvalidType() {
 Object obj = new String("Hello");
 Validate.isInstanceOf(Integer.class, obj);
 }

 @Test
 public void testIsInstanceOfWithMessage() {
 Object obj = new String("Hello");
 try {
 Validate.isInstanceOf(String.class, new Object(), "Message", new Object[] { "ExpectedType" });
 fail();
 } catch (IllegalArgumentException e) {
 String msg = e.getMessage();
 assertEquals("Expected type: ExpectedType, actual: class java.lang.Object", msg);
 }
 }

 // --------------------------------------------------------

 @Test
 public void testIsAssignableFrom() {
 String s = new String("Hello");
 Validate.isAssignableFrom(String.class, StringBuffer.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFromWithInvalidSuperClass() {
 String s = new String("Hello");
 Validate.isAssignableFrom(String.class, Integer.class);
 }

 @Test
 public void testIsAssignableFromWithMessage() {
 String s = new String("Hello");
 try {
 Validate.isAssignableFrom(String.class, Object.class, "Message", new Object[] { "ExpectedSuperClass" });
 fail();
 } catch (IllegalArgumentException e) {
 String msg = e.getMessage();
 assertEquals("Cannot assign a String to a Object", msg);
 }
 }

}