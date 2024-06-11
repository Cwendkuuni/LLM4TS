package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 // Test with null input
 assertNull(BooleanUtils.negate(null));

 // Test with true input
 assertTrue(BooleanUtils.negate(Boolean.TRUE));
 assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.FALSE));

 // Test with integer input
 assertEquals(Boolean.FALSE, BooleanUtils.negate(Integer.valueOf(1)));
 assertEquals(Boolean.TRUE, BooleanUtils.negate(Integer.valueOf(0)));
 }

 @Test
 public void testIsTrue() {
 // Test with null input
 assertFalse(BooleanUtils.isTrue(null));

 // Test with true input
 assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 assertFalse(BooleanUtils.isTrue(Boolean.FALSE));

 // Test with integer input
 assertTrue(BooleanUtils.isTrue(Integer.valueOf(1)));
 assertFalse(BooleanUtils.isTrue(Integer.valueOf(0)));
 }

 @Test
 public void testIsNotTrue() {
 // Test with null input
 assertFalse(BooleanUtils.isNotTrue(null));

 // Test with true input
 assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
 assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));

 // Test with integer input
 assertFalse(BooleanUtils.isNotTrue(Integer.valueOf(1)));
 assertTrue(BooleanUtils.isNotTrue(Integer.valueOf(0)));
 }

 @Test
 public void testIsFalse() {
 // Test with null input
 assertFalse(BooleanUtils.isFalse(null));

 // Test with true input
 assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 assertTrue(BooleanUtils.isFalse(Boolean.FALSE));

 // Test with integer input
 assertFalse(BooleanUtils.isFalse(Integer.valueOf(1)));
 assertTrue(BooleanUtils.isFalse(Integer.valueOf(0)));
 }

 @Test
 public void testIsNotFalse() {
 // Test with null input
 assertFalse(BooleanUtils.isNotFalse(null));

 // Test with true input
 assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
 assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));

 // Test with integer input
 assertTrue(BooleanUtils.isNotFalse(Integer.valueOf(1)));
 assertFalse(BooleanUtils.isNotFalse(Integer.valueOf(0)));
 }

 @Test
 public void testToBoolean() {
 // Test with null input
 assertFalse(BooleanUtils.toBoolean(null));

 // Test with boolean input
 assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));

 // Test with integer input
 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 // Test with null input and false default value
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));

 // Test with null input and true default value
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));

 // Test with boolean input
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));

 // Test with integer input
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Integer.valueOf(1), false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Integer.valueOf(0), false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Integer.valueOf(0), true));
 }

 @Test
 public void testToBooleanWithInvalidInputs() {
 String[] inputs = new String[]{"abc", "123", null};
 for (String input : inputs) {
 try {
 BooleanUtils.toBoolean(input);
 fail("Expected IllegalArgumentException to be thrown with input: " + input);
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }
 }

 @Test
 public void testToInteger() {
 // Test with null input
 assertFalse(BooleanUtils.toBoolean(null));

 // Test with true input
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));

 // Test with integer input
 assertEquals(1, BooleanUtils.toInteger(Integer.valueOf(1)));
 assertEquals(0, BooleanUtils.toInteger(Integer.valueOf(0)));
 }

 @Test
 public void testToIntegerObject() {
 // Test with null input
 assertNull(BooleanUtils.toBooleanObject(null));

 // Test with true input
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));

 // Test with integer input
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
 }

 @Test
 public void testToIntegerObjectWithNullInputs() {
 Integer[] inputs = new Integer[]{null, null, null};
 for (Integer input : inputs) {
 try {
 BooleanUtils.toBooleanObject(input, 1, 0);
 fail("Expected IllegalArgumentException to be thrown with input: " + input);
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }
 }

 @Test
 public void testToBooleanObjectWithNullInputs() {
 Integer[] inputs = new Integer[]{null, null, null};
 for (Integer input : inputs) {
 try {
 BooleanUtils.toBooleanObject(input, 1, 0, 0);
 fail("Expected IllegalArgumentException to be thrown with input: " + input);
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }
 }

 @Test
 public void testToBooleanObjectWithInvalidInputs() {
 Integer[] inputs = new Integer[]{-1, -1, -1};
 for (Integer input : inputs) {
 try {
 BooleanUtils.toBooleanObject(input, 1, 0, 0);
 fail("Expected IllegalArgumentException to be thrown with input: " + input);
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }
 }

 @Test
 public void testToStringTrueFalse() {
 // Test with true input
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));

 // Test with false input
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 @Test
 public void testToStringOnOff() {
 // Test with true input
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));

 // Test with false input
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 @Test
 public void testToStringYesNo() {
 // Test with true input
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));

 // Test with false input
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 @Test
 public void testToStringWithNullInput() {
 // Test with null input
 assertNull(BooleanUtils.toString(null, "true", "false"));
 }

 @Test
 public void testToStringWithInvalidInputs1() {
 String[] inputs = new String[]{"", "abc", null};
 for (String input : inputs) {
 try {
 BooleanUtils.toString(Boolean.TRUE, input, "false");
 fail("Expected IllegalArgumentException to be thrown with input: " + input);
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }
 }

 @Test
 public void testToStringWithInvalidInputs2() {
 String[] inputs = new String[]{"true", "", "false"};
 for (String input : inputs) {
 try {
 BooleanUtils.toString(Boolean.FALSE, "true", input);
 fail("Expected IllegalArgumentException to be thrown with input: " + input);
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }
 }

 @Test
 public void testToStringWithInvalidInputs3() {
 String[] inputs = new String[]{"true", "false", null};
 for (String input : inputs) {
 try {
 BooleanUtils.toString(null, input, "false");
 fail("Expected IllegalArgumentException to be thrown with input: " + input);
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }
 }

 @Test
 public void testAnd() {
 // Test with empty array
 assertFalse(BooleanUtils.and(new boolean[0]));

 // Test with single boolean input
 assertTrue(BooleanUtils.and(new boolean[]{true}));

 // Test with multiple boolean inputs
 assertTrue(BooleanUtils.and(new boolean[]{true, true, true}));
 assertFalse(BooleanUtils.and(new boolean[]{true, false, false}));
 assertFalse(BooleanUtils.and(new boolean[]{false, true, false}));
 assertFalse(BooleanUtils.and(new boolean[]{false, false, false}));
 }

 @Test
 public void testAndWithBooleanArrays() {
 // Test with empty array
 assertTrue(BooleanUtils.and((Boolean[])new Object[0]));

 // Test with single boolean input
 assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE}));

 // Test with multiple boolean inputs
 assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
 assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
 assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
 assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
 }

 @Test
 public void testOr() {
 // Test with empty array
 assertFalse(BooleanUtils.or(new boolean[0]));

 // Test with single boolean input
 assertTrue(BooleanUtils.or(new boolean[]{true}));

 // Test with multiple boolean inputs
 assertTrue(BooleanUtils.or(new boolean[]{true, true, true}));
 assertFalse(BooleanUtils.or(new boolean[]{true, false, false}));
 assertFalse(BooleanUtils.or(new boolean[]{false, true, false}));
 assertFalse(BooleanUtils.or(new boolean[]{false, false, false}));
 }

 @Test
 public void testOrWithBooleanArrays() {
 // Test with empty array
 assertTrue(BooleanUtils.or((Boolean[])new Object[0]));

 // Test with single boolean input
 assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE}));

 // Test with multiple boolean inputs
 assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
 assertFalse(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
 assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
 assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
 }

 @Test
 public void testXor() {
 // Test with empty array
 assertFalse(BooleanUtils.xor(new boolean[0]));

 // Test with single boolean input
 assertEquals(false, BooleanUtils.xor(new boolean[]{true}));

 // Test with multiple boolean inputs
 assertTrue(BooleanUtils.xor(new boolean[]{true, false, true}));
 assertFalse(BooleanUtils.xor(new boolean[]{true, false, false}));
 assertFalse(BooleanUtils.xor(new boolean[]{false, true, false}));
 assertFalse(BooleanUtils.xor(new boolean[]{false, false, false}));
 }

 @Test
 public void testXorWithBooleanArrays() {
 // Test with empty array
 assertFalse(BooleanUtils.xor((Boolean[])new Object[0]));

 // Test with single boolean input
 assertEquals(false, BooleanUtils.xor(new Boolean[]{Boolean.TRUE}));

 // Test with multiple boolean inputs
 assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
 assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
 assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
 assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
 }

 @Test
 public void testAndWithNullArray() {
 try {
 BooleanUtils.and((boolean[])null);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // expected
 }
 }

 @Test
 public void testOrWithNullArray() {
 try {
 BooleanUtils.or((boolean[])null);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // expected
 }
 }

 @Test
 public void testXorWithNullArray() {
 try {
 BooleanUtils.xor((boolean[])null);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // expected
 }
 }
}