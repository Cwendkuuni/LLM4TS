package org.junit. tests;

import static org.apache.commons.lang3.BooleanUtils.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegateTrueShouldReturnFalse() {
 Boolean actual = negate(Boolean.TRUE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testNegateFalseShouldReturnTrue() {
 Boolean actual = negate(Boolean.FALSE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testNegateNullShouldReturnNull() {
 Boolean actual = negate(null);
 assertNull(actual);
 }

 // Test for isTrue method
 @Test
 public void testIsTrueTrueShouldReturnTrue() {
 Boolean actual = isTrue(Boolean.TRUE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsTrueFalseShouldReturnFalse() {
 Boolean actual = isTrue(Boolean.FALSE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsTrueNullShouldReturnFalse() {
 Boolean actual = isTrue(null);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrueTrueShouldReturnFalse() {
 Boolean actual = isNotTrue(Boolean.TRUE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsNotTrueFalseShouldReturnTrue() {
 Boolean actual = isNotTrue(Boolean.FALSE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsNotTrueNullShouldReturnFalse() {
 Boolean actual = isNotTrue(null);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 // Test for isFalse method
 @Test
 public void testIsFalseFalseShouldReturnTrue() {
 Boolean actual = isFalse(Boolean.FALSE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsFalseTrueShouldReturnFalse() {
 Boolean actual = isFalse(Boolean.TRUE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsFalseNullShouldReturnFalse() {
 Boolean actual = isFalse(null);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalseFalseShouldReturnFalse() {
 Boolean actual = isNotFalse(Boolean.FALSE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsNotFalseTrueShouldReturnFalse() {
 Boolean actual = isNotFalse(Boolean.TRUE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsNotFalseNullShouldReturnFalse() {
 Boolean actual = isNotFalse(null);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 // Test for toBoolean method
 @Test
 public void testToBooleanNullShouldReturnFalse() {
 Boolean actual = toBoolean(null);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanTrueShouldReturnTrue() {
 Boolean actual = toBoolean(Boolean.TRUE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanFalseShouldReturnFalse() {
 Boolean actual = toBoolean(Boolean.FALSE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanTrueInteger() {
 Boolean actual = toBoolean(1);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanFalseInteger() {
 Boolean actual = toBoolean(-1);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 // Test for toBooleanDefaultIfNull method
 @Test
 public void testToBooleanDefaultIfNullNullValueFalse() {
 Boolean actual = toBooleanDefaultIfNull(null, false);
 Boolean expected = false;
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanDefaultIfNullNullValueTrue() {
 Boolean actual = toBooleanDefaultIfNull(null, true);
 Boolean expected = true;
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanDefaultIfNullBooleanValueFalse() {
 Boolean actual = toBooleanDefaultIfNull(Boolean.FALSE, false);
 Boolean expected = false;
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanDefaultIfNullBooleanValueTrue() {
 Boolean actual = toBooleanDefaultIfNull(Boolean.TRUE, true);
 Boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test for edge cases and exceptions in toBoolean method
 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanIntegerOutOfRange() {
 BooleanUtils.toBoolean((byte) 128);
 }

 @Test(expected = NullPointerException.class)
 public void testToBooleanNullArray() {
 boolean[] booleanArray = null;
 boolean result = BooleanUtils.and(booleanArray);
 fail("Expected NullPointerException to be thrown");
 }

 // Test for and method
 @Test
 public void testAndTrueAndTrueShouldReturnTrue() {
 boolean actual = and(Boolean.TRUE, Boolean.TRUE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testAndTrueAndFalseShouldReturnFalse() {
 boolean actual = and(Boolean.TRUE, Boolean.FALSE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testAndFalseAndTrueShouldReturnFalse() {
 boolean actual = and(Boolean.FALSE, Boolean.TRUE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testAndFalseAndFalseShouldReturnFalse() {
 boolean actual = and(Boolean.FALSE, Boolean.FALSE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testAndNullArray() {
 try {
 BooleanUtils.and((Boolean[]) null);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 // Test for or method
 @Test
 public void testOrTrueAndTrueShouldReturnTrue() {
 boolean actual = or(Boolean.TRUE, Boolean.TRUE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testOrTrueAndFalseShouldReturnTrue() {
 boolean actual = or(Boolean.TRUE, Boolean.FALSE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testOrFalseAndTrueShouldReturnTrue() {
 boolean actual = or(Boolean.FALSE, Boolean.TRUE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testOrFalseAndFalseShouldReturnFalse() {
 boolean actual = or(Boolean.FALSE, Boolean.FALSE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testOrNullArray() {
 try {
 BooleanUtils.or((Boolean[]) null);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 // Test for xor method
 @Test
 public void testXorTrueAndTrueShouldReturnFalse() {
 boolean actual = xor(Boolean.TRUE, Boolean.TRUE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testXorTrueAndFalseShouldReturnTrue() {
 boolean actual = xor(Boolean.TRUE, Boolean.FALSE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testXorFalseAndTrueShouldReturnTrue() {
 boolean actual = xor(Boolean.FALSE, Boolean.TRUE);
 Boolean expected = Boolean.TRUE;
 assertEquals(expected, actual);
 }

 @Test
 public void testXorFalseAndFalseShouldReturnFalse() {
 boolean actual = xor(Boolean.FALSE, Boolean.FALSE);
 Boolean expected = Boolean.FALSE;
 assertEquals(expected, actual);
 }

 @Test
 public void testXorNullArray() {
 try {
 BooleanUtils.xor((Boolean[]) null);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 // Test for toStringTrueFalse method
 @Test
 public void testToStringTrueFalseTrue() {
 String expected = "true";
 String actual = BooleanUtils.toStringTrueFalse(Boolean.TRUE);
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringTrueFalseFalse() {
 String expected = "false";
 String actual = BooleanUtils.toStringTrueFalse(Boolean.FALSE);
 assertEquals(expected, actual);
 }

 // Test for toStringOnOff method
 @Test
 public void testToStringOnOffTrue() {
 String expected = "on";
 String actual = BooleanUtils.toStringOnOff(Boolean.TRUE);
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringOnOffFalse() {
 String expected = "off";
 String actual = BooleanUtils.toStringOnOff(Boolean.FALSE);
 assertEquals(expected, actual);
 }

 // Test for toStringYesNo method
 @Test
 public void testToStringYesNoTrue() {
 String expected = "yes";
 String actual = BooleanUtils.toStringYesNo(Boolean.TRUE);
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringYesNoFalse() {
 String expected = "no";
 String actual = BooleanUtils.toStringYesNo(Boolean.FALSE);
 assertEquals(expected, actual);
 }
}