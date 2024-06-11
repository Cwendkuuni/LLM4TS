package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpRuntimeException;
import org.junit.Test;

public class DfpDecTest {

 private static final DfpField FACTORY = new Dfp128Field();

 @Test
 public void testConstructorWithFactory() {
 DfpDec d = new DfpDec(FACTORY);
 }

 @Test
 public void testConstructorWithFactoryAndByte() {
 DfpDec d = new DfpDec(FACTORY, (byte) 1);
 }

 @Test
 public void testConstructorWithFactoryAndInt() {
 DfpDec d = new DfpDec(FACTORY, 1);
 }

 @Test
 public void testConstructorWithFactoryAndLong() {
 DfpDec d = new DfpDec(FACTORY, 1L);
 }

 @Test
 public void testConstructorWithFactoryAndDouble() {
 DfpDec d = new DfpDec(FACTORY, 1.0);
 }

 @Test
 public void testConstructorWithDfp() {
 DfpDec d1 = new DfpDec(FACTORY, Dfp.ONE);
 DfpDec d2 = new DfpDec(FACTORY, d1);
 assertEquals(d1, d2);
 }

 @Test
 public void testConstructorWithString() {
 DfpDec d = new DfpDec(FACTORY, "1.2345");
 }

 @Test
 public void testConstructorWithSignByteAndNansByte() {
 DfpDec d = new DfpDec(FACTORY, (byte) 1, (byte) 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithInvalidNumberOfRadixDigits() {
 DfpDec d = new DfpDec(FACTORY, new DfpField(3));
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec d = new DfpDec(FACTORY, 12345678);
 int decimalDigits = d.getDecimalDigits();
 assertEquals(50, decimalDigits);
 }

 @Test
 public void testRoundWithZero() {
 DfpDec d = new DfpDec(FACTORY, 0.5);
 int roundResult = d.round(0);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundWithNonZeroAndRoundDown() {
 DfpDec d = new DfpDec(FACTORY, 1.5);
 int roundResult = d.round(0);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundWithNonZeroAndRoundUp() {
 DfpDec d = new DfpDec(FACTORY, 1.5);
 int roundResult = d.round(1);
 assertEquals(1, roundResult);
 }

 @Test
 public void testRoundWithNonZeroAndRoundHalfUp() {
 DfpDec d = new DfpDec(FACTORY, 1.5);
 int roundResult = d.round(4);
 assertEquals(1, roundResult);
 }

 @Test
 public void testRoundWithNonZeroAndRoundHalfDown() {
 DfpDec d = new DfpDec(FACTORY, 1.5);
 int roundResult = d.round(5);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundWithNonZeroAndRoundHalfEven() {
 DfpDec d = new DfpDec(FACTORY, 1.5);
 int roundResult = d.round(6);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundWithNonZeroAndRoundHalfOdd() {
 DfpDec d = new DfpDec(FACTORY, 1.5);
 int roundResult = d.round(7);
 assertEquals(1, roundResult);
 }

 @Test
 public void testRoundWithNonZeroAndRoundCeil() {
 DfpDec d = new DfpDec(FACTORY, -1.5);
 int roundResult = d.round(0);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundWithNonZeroAndRoundFloor() {
 DfpDec d = new DfpDec(FACTORY, -1.5);
 int roundResult = d.round(1);
 assertEquals(1, roundResult);
 }

 @Test(expected = DfpRuntimeException.class)
 public void testRoundWithInvalidRoundingMode() {
 DfpDec d = new DfpDec(FACTORY, 1.5);
 d.round(10);
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec d = new DfpDec(FACTORY, 0);
 DfpDec nextAfter = d.nextAfter(Dfp.ONE);
 assertNotEquals(d, nextAfter);
 }

 @Test
 public void testNextAfterWithPositiveNumber() {
 DfpDec d1 = new DfpDec(FACTORY, 1.1);
 DfpDec d2 = d1.nextAfter(Dfp.ONE);
 assertEquals(d2, d1.add(Dfp.ONE));
 }

 @Test
 public void testNextAfterWithNegativeNumber() {
 DfpDec d1 = new DfpDec(FACTORY, -2.5);
 DfpDec d2 = d1.nextAfter(Dfp.ONE);
 assertEquals(d2, d1.subtract(Dfp.ONE));
 }

 @Test
 public void testNextAfterWithNegativeNumberAndLessThanZero() {
 DfpDec d1 = new DfpDec(FACTORY, -3.5);
 DfpDec d2 = d1.nextAfter(Dfp.ONE);
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNextAfterWithMaximumNegativeExponent() {
 DfpDec d1 = new DfpDec(FACTORY, Dfp.NEGATIVE_INFINITY);
 DfpDec d2 = d1.nextAfter(Dfp.ONE);
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNextAfterWithMinusZero() {
 DfpDec d1 = new DfpDec(FACTORY, Dfp.NEGATIVE_ZERO);
 DfpDec d2 = d1.nextAfter(Dfp.ONE);
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNextAfterWithMaxValue() {
 DfpDec d1 = new DfpDec(FACTORY, Dfp.POSITIVE_INFINITY);
 DfpDec d2 = d1.nextAfter(Dfp.ONE);
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNextAfterWithMaxValueAndRoundingMode() {
 DfpDec d1 = new DfpDec(FACTORY, Dfp.POSITIVE_INFINITY);
 DfpDec d2 = d1.nextAfter(Dfp.ROUND_UP);
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNextAfterWithMaxValueAndRoundingModeDown() {
 DfpDec d1 = new DfpDec(FACTORY, Dfp.POSITIVE_INFINITY);
 DfpDec d2 = d1.nextAfter(Dfp.ROUND_DOWN);
 assertEquals(d1, d2);
 }
}