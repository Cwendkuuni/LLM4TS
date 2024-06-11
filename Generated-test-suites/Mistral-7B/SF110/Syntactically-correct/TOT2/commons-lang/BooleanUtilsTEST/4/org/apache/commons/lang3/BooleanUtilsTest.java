package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean negatedBool = BooleanUtils.negate(bool);
 assertFalse(negatedBool.booleanValue());

 bool = Boolean.FALSE;
 negatedBool = BooleanUtils.negate(bool);
 assertTrue(negatedBool.booleanValue());

 bool = null;
 negatedBool = BooleanUtils.negate(bool);
 assertNull(negatedBool);
 }

 // Test for isTrue method
 @Test
 public void testIsTrue() {
 assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
 assertFalse(BooleanUtils.isTrue(null));
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrue() {
 assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
 assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
 assertFalse(BooleanUtils.isNotTrue(null));
 }

 // Test for isFalse method
 @Test
 public void testIsFalse() {
 assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isFalse(null));
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalse() {
 assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
 assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isNotFalse(null));
 }

 // Test for toBoolean method with Boolean
 @Test
 public void testToBooleanWithBoolean() {
 assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 assertFalse(BooleanUtils.toBoolean(null));
 }

 // Test for toBoolean method with int
 @Test
 public void testToBooleanWithInt() {
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean(0));
 assertFalse(BooleanUtils.toBoolean(-1));
 }

 // Test for toBooleanDefaultIfNull method with Boolean
 @Test
 public void testToBooleanDefaultIfNullWithBoolean() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, true));
 }

 // Test for toBooleanDefaultIfNull method with int
 @Test
 public void testToBooleanDefaultIfNullWithInt() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(-1, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(-1, true));
 }

 // Test for and method
 @Test
 public void testAnd() {
 assertTrue(BooleanUtils.and(true, true));
 assertFalse(BooleanUtils.and(true, false));
 assertFalse(BooleanUtils.and(false, true));
 assertFalse(BooleanUtils.and(false, false));
 }

 // Test for and method with Boolean
 @Test
 public void testAndWithBoolean() {
 Boolean[] booleans = {Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.and(booleans));

 booleans = new Boolean[] {Boolean.TRUE, Boolean.FALSE};
 assertFalse(BooleanUtils.and(booleans));

 booleans = new Boolean[] {Boolean.FALSE, Boolean.TRUE};
 assertFalse(BooleanUtils.and(booleans));

 booleans = new Boolean[] {Boolean.FALSE, Boolean.FALSE};
 assertFalse(BooleanUtils.and(booleans));
 }

 // Test for or method
 @Test
 public void testOr() {
 assertTrue(BooleanUtils.or(true, true));
 assertTrue(BooleanUtils.or(true, false));
 assertTrue(BooleanUtils.or(false, true));
 assertFalse(BooleanUtils.or(false, false));
 }

 // Test for or method with Boolean
 @Test
 public void testOrWithBoolean() {
 Boolean[] booleans = {Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(booleans));

 booleans = new Boolean[] {Boolean.TRUE, Boolean.FALSE};
 assertTrue(BooleanUtils.or(booleans));

 booleans = new Boolean[] {Boolean.FALSE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(booleans));

 booleans = new Boolean[] {Boolean.FALSE, Boolean.FALSE};
 assertFalse(BooleanUtils.or(booleans));
 }

 // Test for xor method
 @Test
 public void testXor() {
 assertFalse(BooleanUtils.xor(true, true));
 assertTrue(BooleanUtils.xor(true, false));
 assertTrue(BooleanUtils.xor(false, true));
 assertFalse(BooleanUtils.xor(false, false));
 }

 // Test for xor method with Boolean
 @Test
 public void testXorWithBoolean() {
 Boolean[] booleans = { Boolean.TRUE, Boolean.TRUE };
 assertFalse(BooleanUtils.xor(booleans));

 booleans = new Boolean[] { Boolean.TRUE, Boolean.FALSE };
 assertTrue(BooleanUtils.xor(booleans));

 booleans = new Boolean[] { Boolean.FALSE, Boolean.TRUE };
 assertTrue(BooleanUtils.xor(booleans));

 booleans = new Boolean[] { Boolean.FALSE, Boolean.FALSE };
 assertFalse(BooleanUtils.xor(booleans));
 }

}