package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldFactory;
import org.junit.Test;

public class DfpDecTest {

 private DfpFieldFactory fieldFactory = new DfpField(10, 1);

 @Test
 public void testDfpDecConstructor() {
 DfpDec dfpDec = new DfpDec(fieldFactory);
 assertEquals(0, dfpDec.doubleValueExact(), 0);
 }

 @Test
 public void testDfpDecConstructorWithDouble() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.45);
 assertEquals(123.45, dfpDec.doubleValueExact(), 0.001);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(fieldFactory, new DfpDec(fieldFactory, 123.45));
 assertEquals(123.45, dfpDec.doubleValueExact(), 0.001);
 }

 @Test
 public void testNewInstanceWithByte() {
 DfpDec dfpDec = new DfpDec(fieldFactory, (byte) 123);
 assertEquals(123, dfpDec.doubleValueExact(), 0);
 }

 @Test
 public void testNewInstanceWithInt() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123);
 assertEquals(123, dfpDec.doubleValueExact(), 0);
 }

 @Test
 public void testNewInstanceWithLong() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123L);
 assertEquals(123, dfpDec.doubleValueExact(), 0);
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec dfpDec = new DfpDec(fieldFactory, new DfpDec(fieldFactory, 123.45).negate());
 assertEquals(-123.45, dfpDec.doubleValueExact(), 0.001);
 }

 @Test
 public void testNewInstanceWithString() {
 DfpDec dfpDec = new DfpDec(fieldFactory, "123.45");
 assertEquals(123.45, dfpDec.doubleValueExact(), 0.001);
 }

 @Test
 public void testNewInstanceWithByteAndSign() {
 DfpDec dfpDec = new DfpDec(fieldFactory, (byte) 123, (byte) 1);
 assertEquals(123, dfpDec.doubleValueExact(), 0);
 dfpDec = new DfpDec(fieldFactory, (byte) 123, (byte) -1);
 assertEquals(-123, dfpDec.doubleValueExact(), 0);
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.4567);
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(21, decimalDigits);
 }

 @Test
 public void testRoundZero() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.456);
 int roundResult = dfpDec.round(0);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundPositive() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.5);
 int roundResult = dfpDec.round(0);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundPositiveWithCarry() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.55);
 int roundResult = dfpDec.round(0);
 assertEquals(1, roundResult);
 }

 @Test
 public void testRoundNegative() {
 DfpDec dfpDec = new DfpDec(fieldFactory, -123.5);
 int roundResult = dfpDec.round(0);
 assertEquals(-1, roundResult);
 }

 @Test
 public void testRoundNegativeWithCarry() {
 DfpDec dfpDec = new DfpDec(fieldFactory, -123.55);
 int roundResult = dfpDec.round(0);
 assertEquals(-2, roundResult);
 }

 @Test
 public void testRoundDown() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.545);
 int roundResult = dfpDec.round(-1);
 assertEquals(-2, roundResult);
 }

 @Test
 public void testRoundUp() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.545);
 int roundResult = dfpDec.round(1);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundHalfUp() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.545);
 int roundResult = dfpDec.round(0);
 assertEquals(0, roundResult);
 DfpDec dfpDec2 = new DfpDec(fieldFactory, 123.55);
 int roundResult2 = dfpDec2.round(0);
 assertEquals(0, roundResult2);
 DfpDec dfpDec3 = new DfpDec(fieldFactory, 123.56);
 int roundResult3 = dfpDec3.round(0);
 assertEquals(1, roundResult3);
 }

 @Test
 public void testRoundHalfEven() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.545);
 int roundResult = dfpDec.round(-1);
 assertEquals(-2, roundResult);
 DfpDec dfpDec2 = new DfpDec(fieldFactory, 123.55);
 int roundResult2 = dfpDec2.round(0);
 assertEquals(0, roundResult2);
 DfpDec dfpDec3 = new DfpDec(fieldFactory, 123.56);
 int roundResult3 = dfpDec3.round(0);
 assertEquals(1, roundResult3);
 }

 @Test
 public void testRoundHalfOdd() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.545);
 int roundResult = dfpDec.round(-1);
 assertEquals(-2, roundResult);
 DfpDec dfpDec2 = new DfpDec(fieldFactory, 123.55);
 int roundResult2 = dfpDec2.round(0);
 assertEquals(0, roundResult2);
 DfpDec dfpDec3 = new DfpDec(fieldFactory, 123.56);
 int roundResult3 = dfpDec3.round(0);
 assertEquals(1, roundResult3);
 }

 @Test
 public void testRoundCeil() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.499);
 int roundResult = dfpDec.round(1);
 assertEquals(1, roundResult);
 }

 @Test
 public void testRoundFloor() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.501);
 int roundResult = dfpDec.round(-1);
 assertEquals(-1, roundResult);
 }

 @Test
 public void testRoundTiesToEven() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.55);
 int roundResult = dfpDec.round(0);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundTiesToOdd() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.55);
 int roundResult = dfpDec.round(1);
 assertEquals(1, roundResult);
 }

 @Test
 public void testNextAfterSameValue() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.45);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(dfpDec, nextAfter);
 }

 @Test
 public void testNextAfterLargerValue() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.45);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(fieldFactory, 123.46));
 assertNotEquals(dfpDec, nextAfter);
 assertEquals(new DfpDec(fieldFactory, 123.46), nextAfter);
 }

 @Test
 public void testNextAfterSmallerValue() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.46);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(fieldFactory, 123.45));
 assertNotEquals(dfpDec, nextAfter);
 assertEquals(new DfpDec(fieldFactory, 123.45), nextAfter);
 }

 @Test
 public void testNextAfterNegativeValue() {
 DfpDec dfpDec = new DfpDec(fieldFactory, -123.45);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(fieldFactory, -123.44));
 assertEquals(new DfpDec(fieldFactory, -123.44), nextAfter);
 }

 @Test
 public void testNextAfterZeroValue() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 0);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(new DfpDec(fieldFactory, 0), nextAfter);
 }

 @Test
 public void testNextAfterMinusZeroValue() {
 DfpDec dfpDec = new DfpDec(fieldFactory, -0);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(new DfpDec(fieldFactory, -0), nextAfter);
 }

 @Test
 public void testNewInstanceWithInvalidRadixDigits() {
 DfpDec dfpDec = new DfpDec(fieldFactory, 123.45);
 DfpDec dfpDec2 = new DfpDec(fieldFactory, new DfpDec(fieldFactory, 123.45), 2);
 assertTrue(dfpDec2.getField().getRadixDigits() == 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithNegativeRadixDigits() {
 new DfpDec(fieldFactory, new DfpDec(fieldFactory, 123.45), -2);
 }

 @Test
 public void testNewInstanceWithNullDfp() {
 DfpDec dfpDec = new DfpDec(fieldFactory);
 assertFalse(dfpDec.newInstance(null).isValid());
 }
}