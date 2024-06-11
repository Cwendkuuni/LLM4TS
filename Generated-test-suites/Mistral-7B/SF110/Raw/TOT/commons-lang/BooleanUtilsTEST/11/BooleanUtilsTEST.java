package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean original = Boolean.TRUE;
 Boolean expected = Boolean.FALSE;
 Boolean result = BooleanUtils.negate(original);
 assertEquals(expected, result);

 original = null;
 result = BooleanUtils.negate(original);
 assertNull(result);
 }

 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isTrue(bool));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isTrue(bool));

 bool = null;
 assertFalse(BooleanUtils.isTrue(bool));
 }

 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotTrue(bool));

 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotTrue(bool));

 bool = null;
 assertFalse(BooleanUtils.isNotTrue(bool));
 }

 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(bool));

 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(bool));

 bool = null;
 assertFalse(BooleanUtils.isFalse(bool));
 }

 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotFalse(bool));

 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotFalse(bool));

 bool = null;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 @Test
 public void testToBoolean() {
 int value = 1;
 assertTrue(BooleanUtils.toBoolean(value));

 value = 0;
 assertFalse(BooleanUtils.toBoolean(value));

 value = -1;
 assertFalse(BooleanUtils.toBoolean(value));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, true));
 }

 @Test
 public void testToBooleanWithNullableBoolean() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBoolean(bool));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBoolean(bool));

 bool = null;
 assertFalse(BooleanUtils.toBoolean(bool));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanThrowsException() {
 BooleanUtils.toBoolean((byte) 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanThrowsExceptionWithInt() {
 BooleanUtils.toBoolean(-1, 1, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanThrowsExceptionWithNullBoolean() {
 BooleanUtils.toBoolean(null, Boolean.TRUE, Boolean.FALSE);
 }

 @Test
 public void testToBooleanObject() {
 int value = 1;
 assertTrue(BooleanUtils.toBooleanObject(value).booleanValue());

 value = 0;
 assertFalse(BooleanUtils.toBooleanObject(value).booleanValue());

 value = -1;
 assertFalse(BooleanUtils.toBooleanObject(value) instanceof Boolean);
 }

 @Test
 public void testToBooleanObjectWithNullableInt() {
 int value = 1;
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(value));

 value = 0;
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(value));

 value = -1;
 assertNull(BooleanUtils.toBooleanObject(value));
 }

 @Test
 public void testToBooleanWithNullableInt() {
 int value = 1;
 assertTrue(BooleanUtils.toBoolean(value));

 value = 0;
 assertFalse(BooleanUtils.toBoolean(value));

 value = -1;
 assertFalse(BooleanUtils.toBoolean(value));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanThrowsExceptionWithIntArray() {
 BooleanUtils.toBoolean(new int[] { 1, 2 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanThrowsExceptionWithNullableIntArray() {
 Integer[] values = { 1, null };
 assertFalse(BooleanUtils.toBoolean(values));
 assertArrayEquals(new Boolean[] { Boolean.FALSE, null }, BooleanUtils.toBooleanObject(values));
 }

 @Test
 public void testToBooleanObjectWithInteger() {
 Integer value = Integer.valueOf(1);
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(value));

 value = Integer.valueOf(0);
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(value));

 value = null;
 assertNull(BooleanUtils.toBooleanObject(value));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanThrowsExceptionWithIntegerArray() {
 BooleanUtils.toBoolean(new Integer[] { 1, 2 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanThrowsExceptionWithNullableIntegerArray() {
 Integer[] values = { 1, null };
 assertFalse(BooleanUtils.toBoolean(values));
 assertArrayEquals(new Boolean[] { Boolean.FALSE, null }, BooleanUtils.toBooleanObject(values));
 }

 @Test
 public void testToBooleanWithBoolean() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBoolean(bool, Boolean.TRUE, Boolean.FALSE));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBoolean(bool, Boolean.TRUE, Boolean.FALSE));
 }

 @Test
 public void testToBooleanWithNullBoolean() {
 Boolean bool = null;
 assertFalse(BooleanUtils.toBoolean(bool, Boolean.TRUE, Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanThrowsExceptionWithNullBooleanArray() {
 Boolean[] values = { null, Boolean.TRUE };
 assertFalse(BooleanUtils.toBoolean(values));
 assertArrayEquals(new Boolean[] { Boolean.FALSE, Boolean.TRUE }, BooleanUtils.toBooleanObject(values));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanThrowsExceptionWithInvalidBooleanArrayLength() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, Boolean.FALSE, null };
 assertFalse(BooleanUtils.toBoolean(values));
 assertArrayEquals(new Boolean[] { Boolean.FALSE, Boolean.FALSE, null }, BooleanUtils.toBooleanObject(values));
 }

 @Test
 public void testToStringTrueFalse() {
 Boolean bool = Boolean.TRUE;
 assertEquals("true", BooleanUtils.toStringTrueFalse(bool));

 bool = Boolean.FALSE;
 assertEquals("false", BooleanUtils.toStringTrueFalse(bool));
 }

 @Test
 public void testToStringOnOff() {
 Boolean bool = Boolean.TRUE;
 assertEquals("on", BooleanUtils.toStringOnOff(bool));

 bool = Boolean.FALSE;
 assertEquals("off", BooleanUtils.toStringOnOff(bool));
 }

 @Test
 public void testToStringYesNo() {
 Boolean bool = Boolean.TRUE;
 assertEquals("yes", BooleanUtils.toStringYesNo(bool));

 bool = Boolean.FALSE;
 assertEquals("no", BooleanUtils.toStringYesNo(bool));
 }

 @Test
 public void testToStringTrueFalseWithBoolean() {
 Boolean bool = Boolean.TRUE;
 assertEquals("true", BooleanUtils.toStringTrueFalse(bool));

 bool = Boolean.FALSE;
 assertEquals("false", BooleanUtils.toStringTrueFalse(bool));
 }

 @Test
 public void testToStringTrueFalseWithString() {
 Boolean bool = Boolean.TRUE;
 assertEquals("true", BooleanUtils.toStringTrueFalse(bool, "true", "false"));

 bool = Boolean.FALSE;
 assertEquals("false", BooleanUtils.toStringTrueFalse(bool, "true", "false"));
 }

 @Test
 public void testToStringOnOffWithBoolean() {
 Boolean bool = Boolean.TRUE;
 assertEquals("on", BooleanUtils.toStringOnOff(bool));

 bool = Boolean.FALSE;
 assertEquals("off", BooleanUtils.toStringOnOff(bool));
 }

 @Test
 public void testToStringOnOffWithString() {
 Boolean bool = Boolean.TRUE;
 assertEquals("on", BooleanUtils.toStringOnOff(bool, "on", "off"));

 bool = Boolean.FALSE;
 assertEquals("off", BooleanUtils.toStringOnOff(bool, "on", "off"));
 }

 @Test
 public void testToStringYesNoWithBoolean() {
 Boolean bool = Boolean.TRUE;
 assertEquals("yes", BooleanUtils.toStringYesNo(bool));

 bool = Boolean.FALSE;
 assertEquals("no", BooleanUtils.toStringYesNo(bool));
 }

 @Test
 public void testToStringYesNoWithString() {
 Boolean bool = Boolean.TRUE;
 assertEquals("yes", BooleanUtils.toStringYesNo(bool, "yes", "no"));

 bool = Boolean.FALSE;
 assertEquals("no", BooleanUtils.toStringYesNo(bool, "yes", "no"));
 }

 @Test
 public void testAnd() {
 boolean[] values = { true, true, true };
 assertTrue(BooleanUtils.and(values));

 values = new boolean[] { true, false, true };
 assertFalse(BooleanUtils.and(values));

 values = new boolean[] { false, false, false };
 assertFalse(BooleanUtils.and(values));
 }

 @Test
 public void testAndWithBoolean() {
 Boolean bool1 = Boolean.TRUE;
 Boolean bool2 = Boolean.TRUE;
 assertTrue(BooleanUtils.and(bool1, bool2));

 bool1 = Boolean.TRUE;
 bool2 = Boolean.FALSE;
 assertFalse(BooleanUtils.and(bool1, bool2));
 }

 @Test
 public void testAndWithNullBoolean() {
 Boolean bool1 = null;
 Boolean bool2 = Boolean.TRUE;
 assertFalse(BooleanUtils.and(bool1, bool2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndThrowsException() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, null };
 assertFalse(BooleanUtils.and(values));
 }

 @Test
 public void testAndWithBooleanArray() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 assertTrue(BooleanUtils.and(values));

 values = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE };
 assertFalse(BooleanUtils.and(values));

 values = new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
 assertFalse(BooleanUtils.and(values));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndThrowsExceptionWithNullBooleanArray() {
 Boolean[] values = new Boolean[] { null, Boolean.TRUE, Boolean.TRUE };
 assertFalse(BooleanUtils.and(values));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndThrowsExceptionWithBooleanArrayLength() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null };
 assertFalse(BooleanUtils.and(values));
 }

 @Test
 public void testOr() {
 boolean[] values = { true, true, true };
 assertTrue(BooleanUtils.or(values));

 values = new boolean[] { true, false, true };
 assertTrue(BooleanUtils.or(values));

 values = new boolean[] { false, false, false };
 assertFalse(BooleanUtils.or(values));
 }

 @Test
 public void testOrWithBoolean() {
 Boolean bool1 = Boolean.TRUE;
 Boolean bool2 = Boolean.TRUE;
 assertTrue(BooleanUtils.or(bool1, bool2));

 bool1 = Boolean.TRUE;
 bool2 = Boolean.FALSE;
 assertTrue(BooleanUtils.or(bool1, bool2));
 }

 @Test
 public void testOrWithNullBoolean() {
 Boolean bool1 = null;
 Boolean bool2 = Boolean.TRUE;
 assertTrue(BooleanUtils.or(bool1, bool2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrThrowsException() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, null };
 assertFalse(BooleanUtils.or(values));
 }

 @Test
 public void testOrWithBooleanArray() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 assertTrue(BooleanUtils.or(values));

 values = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE };
 assertTrue(BooleanUtils.or(values));

 values = new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
 assertFalse(BooleanUtils.or(values));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrThrowsExceptionWithNullBooleanArray() {
 Boolean[] values = new Boolean[] { null, Boolean.TRUE, Boolean.TRUE };
 assertFalse(BooleanUtils.or(values));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrThrowsExceptionWithBooleanArrayLength() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null };
 assertFalse(BooleanUtils.or(values));
 }

 @Test
 public void testXor() {
 boolean[] values = { true, true, true };
 assertEquals(false, BooleanUtils.xor(values));

 values = new boolean[] { true, false, true };
 assertEquals(true, BooleanUtils.xor(values));

 values = new boolean[] { false, false, false };
 assertEquals(false, BooleanUtils.xor(values));
 }

 @Test
 public void testXorWithBoolean() {
 Boolean bool1 = Boolean.TRUE;
 Boolean bool2 = Boolean.TRUE;
 assertEquals(false, BooleanUtils.xor(bool1, bool2));

 bool1 = Boolean.TRUE;
 bool2 = Boolean.FALSE;
 assertEquals(true, BooleanUtils.xor(bool1, bool2));
 }

 @Test
 public void testXorWithNullBoolean() {
 Boolean bool1 = null;
 Boolean bool2 = Boolean.TRUE;
 assertEquals(false, BooleanUtils.xor(bool1, bool2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorThrowsException() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, null };
 assertFalse(BooleanUtils.xor(values));
 }

 @Test
 public void testXorWithBooleanArray() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 assertEquals(false, BooleanUtils.xor(values));

 values = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE };
 assertEquals(true, BooleanUtils.xor(values));

 values = new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
 assertEquals(false, BooleanUtils.xor(values));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorThrowsExceptionWithNullBooleanArray() {
 Boolean[] values = new Boolean[] { null, Boolean.TRUE, Boolean.TRUE };
 assertFalse(BooleanUtils.xor(values));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorThrowsExceptionWithBooleanArrayLength() {
 Boolean[] values = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null };
 assertFalse(BooleanUtils.xor(values));
 }
}