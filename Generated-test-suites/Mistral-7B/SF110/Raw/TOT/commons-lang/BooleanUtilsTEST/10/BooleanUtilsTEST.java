package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean result = BooleanUtils.negate(bool);
 assertFalse(result.booleanValue());

 bool = Boolean.FALSE;
 result = BooleanUtils.negate(bool);
 assertTrue(result.booleanValue());

 bool = null;
 result = BooleanUtils.negate(bool);
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
 assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));

 assertFalse(BooleanUtils.toBoolean((byte) 0));
 assertFalse(BooleanUtils.toBoolean((short) 0));
 assertFalse(BooleanUtils.toBoolean((char) 0));
 assertFalse(BooleanUtils.toBoolean((int) 0));
 assertFalse(BooleanUtils.toBoolean((long) 0L));
 assertFalse(BooleanUtils.toBoolean((float) 0.0f));
 assertFalse(BooleanUtils.toBoolean((double) 0.0));

 assertFalse(BooleanUtils.toBoolean(null));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));

 assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 }

 @Test
 public void testToBooleanWithInt() {
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean(0));
 assertFalse(BooleanUtils.toBoolean(-1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithIntThrowsException() {
 BooleanUtils.toBooleanObject(1, 1, 1, 1);
 }

 @Test
 public void testToBooleanObjectWithIntSuccess() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 1, 1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 1, 1));
 }

 @Test
 public void testToBooleanObjectWithNullableInt() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, null, null, null));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, null, null, null));
 assertNull(BooleanUtils.toBooleanObject(null, null, null, null));
 }

 @Test
 public void testToBooleanWithInts() {
 assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 try {
 BooleanUtils.toBoolean(2, 1, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanWithNullablesInts() {
 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), null, null, null));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), null, null, null));
 assertFalse(BooleanUtils.toBoolean(null, null, null, null));
 try {
 BooleanUtils.toBoolean(Integer.valueOf(2), null, null, null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithIntsThrowsException() {
 BooleanUtils.toBooleanObject(1, 1, 1, 1, 1);
 }

 @Test
 public void testToBooleanObjectWithIntsSuccess() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 1, 1, 1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 1, 1, 1));
 }

 @Test
 public void testToBooleanObjectWithNullableInts() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), null, null, null, null));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), null, null, null, null));
 assertNull(BooleanUtils.toBooleanObject(null, null, null, null, null));
 }

 @Test
 public void testToInteger() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToIntegerObjectWithBooleanThrowsException() {
 BooleanUtils.toIntegerObject(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
 }

 @Test
 public void testToIntegerObjectWithBooleanSuccess() {
 assertEquals(1, BooleanUtils.toIntegerObject(Boolean.TRUE).intValue());
 assertEquals(0, BooleanUtils.toIntegerObject(Boolean.FALSE).intValue());
 }

 @Test
 public void testToIntegerObjectWithNullableBoolean() {
 assertEquals(1, BooleanUtils.toIntegerObject(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE).intValue());
 assertEquals(0, BooleanUtils.toIntegerObject(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE).intValue());
 assertEquals(0, BooleanUtils.toIntegerObject(null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE).intValue());
 }

 @Test
 public void testToIntegerWithBoolean() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToIntegerWithBooleansThrowsException() {
 BooleanUtils.toInteger(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
 }

 @Test
 public void testToIntegerWithBooleansSuccess() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, Boolean.TRUE).intValue());
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, Boolean.TRUE).intValue());
 }

 @Test
 public void testToIntegerWithNullableBooleans() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, null, null).intValue());
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, Boolean.TRUE, null).intValue());
 assertEquals(0, BooleanUtils.toInteger(null, null, null).intValue());
 }

 @Test
 public void testToStringTrueFalse() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 assertEquals(null, BooleanUtils.toStringTrueFalse(null));
 }

 @Test
 public void testToStringOnOff() {
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 assertEquals(null, BooleanUtils.toStringOnOff(null));
 }

 @Test
 public void testToStringYesNo() {
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 assertEquals(null, BooleanUtils.toStringYesNo(null));
 }

 @Test
 public void testToStringTrueFalseWithBoolean() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringTrueFalseWithBooleansThrowsException() {
 BooleanUtils.toStringTrueFalse(Boolean.TRUE, Boolean.TRUE);
 }

 @Test
 public void testToStringTrueFalseWithBooleansSuccess() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE, Boolean.FALSE)[0]);
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.TRUE, Boolean.FALSE)[1]);
 }

 @Test
 public void testToStringTrueFalseWithNullables() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE, null, null)[0]);
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.TRUE, Boolean.FALSE, null)[1]);
 }

 @Test
 public void testToStringOnOffWithBoolean() {
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringOnOffWithBooleansThrowsException() {
 BooleanUtils.toStringOnOff(Boolean.TRUE, Boolean.TRUE);
 }

 @Test
 public void testToStringOnOffWithBooleansSuccess() {
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE, Boolean.FALSE)[0]);
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.TRUE, Boolean.FALSE)[1]);
 }

 @Test
 public void testToStringYesNoWithBoolean() {
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringYesNoWithBooleansThrowsException() {
 BooleanUtils.toStringYesNo(Boolean.TRUE, Boolean.TRUE);
 }

 @Test
 public void testToStringYesNoWithBooleansSuccess() {
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE, Boolean.FALSE)[0]);
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.TRUE, Boolean.FALSE)[1]);
 }

 @Test
 public void testAnd() {
 Boolean[] bools = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.and(bools));

 bools = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertFalse(BooleanUtils.and(bools));

 bools = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertFalse(BooleanUtils.and(bools));

 bools = new Boolean[] {Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE};
 assertFalse(BooleanUtils.and(bools));

 bools = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null};
 assertFalse(BooleanUtils.and(bools));

 bools = new Boolean[] {null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertFalse(BooleanUtils.and(bools));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndWithNullArrayThrowsException() {
 BooleanUtils.and((Boolean[]) null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndWithEmptyArrayThrowsException() {
 BooleanUtils.and(new Boolean[0]);
 }

 @Test
 public void testAndWithBooleanArrays() {
 Boolean[] bools1 = {Boolean.TRUE, Boolean.TRUE};
 Boolean[] bools2 = {Boolean.TRUE, Boolean.TRUE};
 Boolean[] result = BooleanUtils.and(bools1, bools2);
 assertArrayEquals(new Boolean[] {Boolean.TRUE, Boolean.TRUE}, result);

 bools1 = new Boolean[] {Boolean.TRUE, Boolean.TRUE};
 bools2 = new Boolean[] {Boolean.FALSE, Boolean.TRUE};
 result = BooleanUtils.and(bools1, bools2);
 assertArrayEquals(new Boolean[] {Boolean.FALSE, Boolean.FALSE}, result);

 bools1 = new Boolean[] {Boolean.TRUE, Boolean.TRUE};
 bools2 = new Boolean[] {null, Boolean.TRUE};
 result = BooleanUtils.and(bools1, bools2);
 assertArrayEquals(new Boolean[] {Boolean.FALSE, Boolean.TRUE}, result);
 }

 @Test(expected = NullPointerException.class)
 public void testAndWithNullBooleanThrowsException() {
 BooleanUtils.and(new Boolean[] {Boolean.TRUE, null});
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
 public void testAndWithBooleanAndBooleans() {
 Boolean bool1 = Boolean.TRUE;
 Boolean bool2 = Boolean.TRUE;
 Boolean[] booleans = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean result = BooleanUtils.and(bool1, booleans);
 assertFalse(result);
 }

 @Test
 public void testAndWithBooleans() {
 Boolean[] booleans1 = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean[] booleans2 = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean result = BooleanUtils.and(booleans1, booleans2);
 assertArrayEquals(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}, result);

 booleans1 = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 booleans2 = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 result = BooleanUtils.and(booleans1, booleans2);
 assertArrayEquals(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}, result);
 }
}