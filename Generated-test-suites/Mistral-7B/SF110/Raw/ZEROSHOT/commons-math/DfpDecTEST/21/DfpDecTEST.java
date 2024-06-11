package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldConfigurable;
import org.apache.commons.math3.dfp.DfpRuntimeException;
import org.junit.Test;

public class DfpDecTest {

 private DfpFieldConfigurable field = new org.apache.commons.math3.dfp.Dfp128Field();

 @Test
 public void testNewInstance() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance();
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithValue() {
 DfpDec d1 = new DfpDec(field, 12345.0);
 DfpDec d2 = d1.newInstance();
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec d1 = new DfpDec(field, new DfpDec(field, 12345.0));
 DfpDec d2 = d1.newInstance(d1);
 assertEquals(d1, d2);
 }

 @Test
 public void testRounding() {
 DfpDec d1 = new DfpDec(field, 12345.6789);
 DfpDec d2 = d1.newInstance();
 int roundingMode = DfpField.ROUND_HALF_UP;
 int roundingResult = d1.round(roundingMode);
 d1.round(roundingMode);
 assertEquals(d2, d1); // no change because rounding is done after test
 assertEquals(roundingResult, d1.round(roundingMode));
 }

 @Test
 public void testRoundingWithSignChange() {
 DfpDec d1 = new DfpDec(field, -12345.6789);
 DfpDec d2 = d1.newInstance();
 int roundingMode = DfpField.ROUND_HALF_UP;
 int roundingResult = d1.round(roundingMode);
 d1.round(roundingMode);
 assertEquals(d2, d1); // no change because rounding is done after test
 assertEquals(roundingResult, d1.round(roundingMode));
 }

 @Test
 public void testRoundingWithZero() {
 DfpDec d1 = new DfpDec(field, 0.0);
 DfpDec d2 = d1.newInstance();
 int roundingMode = DfpField.ROUND_HALF_UP;
 int roundingResult = d1.round(roundingMode);
 d1.round(roundingMode);
 assertEquals(d2, d1); // no change because rounding is done after test
 assertEquals(0, d1.round(roundingMode));
 }

 @Test
 public void testRoundingWithNaN() {
 DfpDec d1 = new DfpDec(field, Double.NaN);
 DfpDec d2 = d1.newInstance();
 int roundingMode = DfpField.ROUND_HALF_UP;
 assertThrows(DfpRuntimeException.class, () -> d1.round(roundingMode));
 }

 @Test
 public void testNewInstanceWithString() {
 DfpDec d1 = new DfpDec(field, "12345.6789");
 DfpDec d2 = d1.newInstance();
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithInvalidString() {
 DfpDec d1 = new DfpDec(field);
 assertThrows(NumberFormatException.class, () -> d1.newInstance("abcdefg"));
 }

 @Test
 public void testNewInstanceWithNegativeString() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance("-12345.6789");
 assertEquals(d2, d1.newNegate());
 }

 @Test
 public void testNewInstanceWithSign() {
 DfpDec d1 = new DfpDec(field, -1);
 DfpDec d2 = d1.newInstance((byte) -1);
 assertEquals(d2, d1.negate());
 }

 @Test
 public void testNewInstanceWithNan() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance((byte) 3);
 assertFalse(d2.isNaN());
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec d1 = new DfpDec(field, 12345.6789);
 int decimalDigits = d1.getDecimalDigits();
 assertEquals(53, decimalDigits);
 }

 @Test
 public void testNextAfter() {
 DfpDec d1 = new DfpDec(field, 12345.6789);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, 12346.0));
 assertEquals(d2, new DfpDec(field, 12345.6790));
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec d1 = new DfpDec(field, 0.0);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, 0.0));
 assertEquals(d2, d1);
 }

 @Test
 public void testNextAfterWithSameValue() {
 DfpDec d1 = new DfpDec(field, 12345.6789);
 DfpDec d2 = d1.nextAfter(d1);
 assertEquals(d2, d1);
 }

 @Test
 public void testNextAfterWithNegativeValue() {
 DfpDec d1 = new DfpDec(field, -12345.6789);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, -12346.0));
 assertEquals(d2, new DfpDec(field, -12345.6788));
 }

 @Test
 public void testNextAfterWithNegativeValueAndZero() {
 DfpDec d1 = new DfpDec(field, -12345.6789);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, 0.0));
 assertEquals(d2, d1);
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpDec d1 = new DfpDec(field, Double.NaN);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, 12345.6789));
 assertNotEquals(d2, d1);
 assertFalse(d2.isNaN());
 }

 @Test
 public void testNextAfterWithInf() {
 DfpDec d1 = new DfpDec(field, Double.POSITIVE_INFINITY);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, Double.POSITIVE_INFINITY));
 assertEquals(d2, d1);
 }

 @Test
 public void testNextAfterWithNegativeInf() {
 DfpDec d1 = new DfpDec(field, Double.NEGATIVE_INFINITY);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, Double.NEGATIVE_INFINITY));
 assertEquals(d2, d1);
 }

 @Test
 public void testNextAfterWithInfAndNaN() {
 DfpDec d1 = new DfpDec(field, Double.POSITIVE_INFINITY);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, Double.NaN));
 assertNotEquals(d2, d1);
 assertFalse(d2.isNaN());
 }

 @Test
 public void testNextAfterWithNaNAndInf() {
 DfpDec d1 = new DfpDec(field, Double.NaN);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, Double.POSITIVE_INFINITY));
 assertNotEquals(d2, d1);
 assertFalse(d2.isNaN());
 }

 @Test
 public void testNextAfterWithNaNAndZero() {
 DfpDec d1 = new DfpDec(field, Double.NaN);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, 0.0));
 assertEquals(d2, d1);
 }

 @Test
 public void testNextAfterWithNaNAndNaN() {
 DfpDec d1 = new DfpDec(field, Double.NaN);
 DfpDec d2 = d1.nextAfter(new DfpDec(field, Double.NaN));
 assertEquals(d2, d1);
 }

}