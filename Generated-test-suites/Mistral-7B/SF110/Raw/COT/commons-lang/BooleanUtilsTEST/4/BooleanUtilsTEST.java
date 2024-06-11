package org.junit.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.BooleanUtils;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean negatedBool = BooleanUtils.negate(bool);
 assertFalse(negatedBool.equals(bool));
 negatedBool = BooleanUtils.negate(Boolean.FALSE);
 assertTrue(negatedBool.equals(Boolean.TRUE));
 }

 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isTrue(bool));
 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isTrue(bool));
 }

 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotTrue(bool));
 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotTrue(bool));
 }

 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(bool));
 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(bool));
 }

 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isNotFalse(bool));
 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 @Test
 public void testToBoolean() {
 boolean bool = true;
 assertTrue(BooleanUtils.toBoolean(bool));
 bool = false;
 assertFalse(BooleanUtils.toBoolean(bool));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 }

 @Test
 public void testToBooleanWithInt() {
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test
 public void testToBooleanObjectWithInt() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 }

 @Test
 public void testToBooleanWithNullableInt() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanWithInts() {
 try {
 BooleanUtils.toBoolean(1, 2, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }

 try {
 BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(0));
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanObjectWithInts() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, -1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, -1));
 assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, -1));
 try {
 BooleanUtils.toBooleanObject(2, 1, 0, -1);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testToInteger() {
 assertTrue(BooleanUtils.toInteger(Boolean.TRUE) == 1);
 assertFalse(BooleanUtils.toInteger(Boolean.FALSE) == 1);
 assertFalse(BooleanUtils.toInteger(Boolean.TRUE) == 0);
 assertFalse(BooleanUtils.toInteger(Boolean.FALSE) == 0);
 }

 @Test
 public void testToStringTrueFalse() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 @Test
 public void testToStringOnOff() {
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 @Test
 public void testToStringYesNo() {
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 @Test
 public void testToString() {
 assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
 assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
 }

 @Test
 public void testAnd() {
 boolean[] booleans = {true, true, true};
 assertTrue(BooleanUtils.and(booleans));

 booleans = new boolean[] {false, true, true};
 assertFalse(BooleanUtils.and(booleans));

 booleans = new boolean[] {true, false, true};
 assertFalse(BooleanUtils.and(booleans));

 booleans = new boolean[] {true, true, false};
 assertFalse(BooleanUtils.and(booleans));

 booleans = new boolean[] {true, true, null};
 assertFalse(BooleanUtils.and(booleans));
 }

 @Test
 public void testAndWithBooleans() {
 Boolean[] booleans = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertEquals(Boolean.TRUE, BooleanUtils.and(booleans));

 booleans = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE};
 assertFalse(BooleanUtils.and(booleans));

 booleans = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertFalse(BooleanUtils.and(booleans));

 booleans = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
 assertFalse(BooleanUtils.and(booleans));

 booleans = new Boolean[] {Boolean.TRUE, Boolean.TRUE, null};
 assertFalse(BooleanUtils.and(booleans));
 }

 @Test
 public void testOr() {
 boolean[] booleans = {false, false, false};
 assertFalse(BooleanUtils.or(booleans));

 booleans = new boolean[] {true, false, false};
 assertTrue(BooleanUtils.or(booleans));

 booleans = new boolean[] {false, true, false};
 assertTrue(BooleanUtils.or(booleans));

 booleans = new boolean[] {false, false, true};
 assertTrue(BooleanUtils.or(booleans));

 booleans = new boolean[] {true, true, null};
 assertFalse(BooleanUtils.and(booleans));
 }

 @Test
 public void testOrWithBooleans() {
 Boolean[] booleans = {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
 assertFalse(BooleanUtils.or(booleans));

 booleans = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE};
 assertTrue(BooleanUtils.or(booleans));

 booleans = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE};
 assertTrue(BooleanUtils.or(booleans));

 booleans = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(booleans));

 booleans = new Boolean[] {Boolean.TRUE, Boolean.TRUE, null};
 assertFalse(BooleanUtils.and(booleans));
 }

 @Test
 public void testXor() {
 boolean[] booleans = {true, false, true};
 assertTrue(BooleanUtils.xor(booleans) == true);

 booleans = new boolean[] {false, true, false};
 assertTrue(BooleanUtils.xor(booleans) == true);

 booleans = new boolean[] {true, true, true};
 assertFalse(BooleanUtils.xor(booleans) == true);

 booleans = new boolean[] {true, true, null};
 assertNull(BooleanUtils.xor(booleans));
 }

 @Test
 public void testXorWithBooleans() {
 Boolean[] booleans = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertTrue(BooleanUtils.xor(booleans).equals(Boolean.TRUE));

 booleans = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE};
 assertTrue(BooleanUtils.xor(booleans).equals(Boolean.TRUE));

 booleans = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertFalse(BooleanUtils.xor(booleans).equals(Boolean.TRUE));

 booleans = new Boolean[] {Boolean.TRUE, Boolean.TRUE, null};
 assertNull(BooleanUtils.xor(booleans));
 }
}