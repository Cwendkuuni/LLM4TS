package org.apache.commons.lang3;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean expected = BooleanUtils.negate(bool);
 assertFalse(expected.equals(bool));
 expected = BooleanUtils.negate(Boolean.FALSE);
 assertTrue(expected.equals(Boolean.TRUE));
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
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(bool));
 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(bool));
 }

 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotFalse(bool));
 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 @Test
 public void testToBoolean() {
 assertFalse(BooleanUtils.toBoolean((short) -1));
 assertTrue(BooleanUtils.toBoolean(0));
 assertFalse(BooleanUtils.toBoolean((byte) -1));
 assertTrue(BooleanUtils.toBoolean(1));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 bool = null;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 @Test
 public void testToBooleanObject() {
 assertNull(BooleanUtils.toBooleanObject((short) -1));
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(0));
 assertNull(BooleanUtils.toBooleanObject((byte) -1));
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 }

 @Test
 public void testToBooleanObjectWithNull() {
 assertNull(BooleanUtils.toBooleanObject((short) -1, null, null, null));
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(0, null, null, null));
 }

 @Test
 public void testToInteger() {
 assertEquals(1, BooleanUtils.toInteger(true));
 assertEquals(0, BooleanUtils.toInteger(false));
 }

 @Test
 public void testToIntegerObjectWithBoolean() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithInvalidInteger() {
 BooleanUtils.toBoolean(Integer.MIN_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithNullBoolean() {
 try {
 BooleanUtils.toBoolean(null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithInvalidInteger() {
 try {
 BooleanUtils.toBooleanObject(Integer.MIN_VALUE);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 }
 }

 @Test(expected = NullPointerException.class)
 public void testToBooleanObjectWithNullInteger() {
 try {
 BooleanUtils.toBooleanObject(null, null, null, null);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException e) {
 }
 }
}