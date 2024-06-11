package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldFactory;
import org.junit.Test;

public class DfpDecTest {

 private DfpFieldFactory fieldFactory = new Dfp32Field();
 private DfpField getField() {
 return fieldFactory.getFieldInstance();
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(new DfpDec(getField()));
 assertEquals(dfpDec.getClass(), DfpDec.class);
 }

 @Test
 public void testNewInstanceInt() {
 DfpDec dfpDec = new DfpDec(getField(), 123);
 assertEquals(dfpDec.intValueExact(), 123);
 }

 @Test
 public void testNewInstanceLong() {
 DfpDec dfpDec = new DfpDec(getField(), 123L);
 assertEquals(dfpDec.longValueExact(), 123L);
 }

 @Test
 public void testNewInstanceDouble() {
 DfpDec dfpDec = new DfpDec(getField(), 123.45);
 assertEquals(dfpDec.doubleValueExact(), 123.45, 0.0001);
 }

 @Test
 public void testNewInstanceDfp() {
 DfpDec dfpDec = new DfpDec(getField(), new DfpDec(getField(), 123));
 assertEquals(dfpDec.doubleValueExact(), 123, 0.0);
 }

 @Test
 public void testNewInstanceString() {
 DfpDec dfpDec = new DfpDec(getField(), "123.4567");
 assertEquals(dfpDec.doubleValueExact(), 123.4567, 0.0001);
 }

 @Test
 public void testNewInstanceByte() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 123);
 assertEquals(dfpDec.intValueExact(), 123);
 }

 @Test
 public void testNewInstanceByteSign() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1, (byte) 2);
 assertEquals(dfpDec.intValueExact(), -2147483649);
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.789);
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(decimalDigits, 15);
 }

 @Test
 public void testRound() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.789);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 }

 @Test
 public void testRoundWithRounding() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.789);
 int roundResult = dfpDec.round(1);
 assertEquals(roundResult, 16);
 }

 @Test
 public void testRoundWithRoundingUp() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.785);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 int roundResultUp = dfpDec.round(1);
 assertEquals(roundResultUp, 15);
 }

 @Test
 public void testRoundWithRoundingDown() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.786);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 int roundResultDown = dfpDec.round(1);
 assertEquals(roundResultDown, 14);
 }

 @Test
 public void testRoundWithRoundingHalfUp() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.785);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 int roundResultHalfUp = dfpDec.round(2);
 assertEquals(roundResultHalfUp, 15);
 }

 @Test
 public void testRoundWithRoundingHalfDown() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.786);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 int roundResultHalfDown = dfpDec.round(2);
 assertEquals(roundResultHalfDown, 14);
 }

 @Test
 public void testRoundWithRoundingHalfEven() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.785);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 int roundResultHalfEven = dfpDec.round(3);
 assertEquals(roundResultHalfEven, 15);
 }

 @Test
 public void testRoundWithRoundingHalfOdd() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.785);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 int roundResultHalfOdd = dfpDec.round(3);
 assertEquals(roundResultHalfOdd, 14);
 }

 @Test
 public void testRoundWithRoundingCeil() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.784);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 int roundResultCeil = dfpDec.round(4);
 assertEquals(roundResultCeil, 16);
 }

 @Test
 public void testRoundWithRoundingFloor() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.786);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 int roundResultFloor = dfpDec.round(4);
 assertEquals(roundResultFloor, 14);
 }

 @Test
 public void testNextAfter() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.789);
 Dfp nextAfter = dfpDec.nextAfter(new DfpDec(getField(), 123456.79));
 assertEquals(nextAfter.doubleValueExact(), 123456.79, 0.0001);
 }

 @Test
 public void testNextAfterWithSameValue() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.789);
 Dfp nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(nextAfter.doubleValueExact(), 123456.789, 0.0001);
 }

 @Test
 public void testNextAfterWithSmallerValue() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.789);
 Dfp nextAfter = dfpDec.nextAfter(new DfpDec(getField(), 123456.788));
 assertEquals(nextAfter.doubleValueExact(), 123456.789, 0.0001);
 }

 @Test
 public void testNextAfterWithLargerValue() {
 DfpDec dfpDec = new DfpDec(getField(), 123456.789);
 Dfp nextAfter = dfpDec.nextAfter(new DfpDec(getField(), 123456.791));
 assertEquals(nextAfter.doubleValueExact(), 123456.79, 0.0001);
 }

 @Test
 public void testNextAfterWithNegativeValue() {
 DfpDec dfpDec = new DfpDec(getField(), -123456.789);
 Dfp nextAfter = dfpDec.nextAfter(new DfpDec(getField(), -123456.788));
 assertEquals(nextAfter.doubleValueExact(), -123456.789, 0.0001);
 }

 @Test
 public void testNextAfterWithNegativeValueAndSameSign() {
 DfpDec dfpDec = new DfpDec(getField(), -123456.789);
 Dfp nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(nextAfter.doubleValueExact(), -123456.789, 0.0001);
 }

 @Test
 public void testNextAfterWithNegativeValueAndDifferentSign() {
 DfpDec dfpDec = new DfpDec(getField(), -123456.789);
 Dfp nextAfter = dfpDec.nextAfter(new DfpDec(getField(), 123456.788));
 assertEquals(nextAfter.doubleValueExact(), -123456.788, 0.0001);
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec dfpDecPos = new DfpDec(getField(), 0);
 Dfp nextAfterPos = dfpDecPos.nextAfter(new DfpDec(getField(), 1));
 assertEquals(nextAfterPos.doubleValueExact(), 1, 0.0);

 DfpDec dfpDecNeg = new DfpDec(getField(), 0);
 Dfp nextAfterNeg = dfpDecNeg.nextAfter(new DfpDec(getField(), -1));
 assertEquals(nextAfterNeg.doubleValueExact(), -1, 0.0);
 }

 @Test
 public void testNextAfterWithSubnormalNumber() {
 DfpDec dfpDec = new DfpDec(getField(), 0.0001F);
 Dfp nextAfter = dfpDec.nextAfter(new DfpDec(getField(), 1));
 assertFalse(nextAfter.isNaN());
 assertEquals(nextAfter.doubleValueExact(), 0.00011102230246251565F, 0.00001);
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpDec dfpDec1 = new DfpDec(getField(), Double.NaN);
 DfpDec dfpDec2 = new DfpDec(getField(), Double.NaN);
 Dfp nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertTrue(nextAfter.isNaN());
 }

 @Test
 public void testNextAfterWithInfinity() {
 DfpDec dfpDec1 = new DfpDec(getField(), Double.POSITIVE_INFINITY);
 DfpDec dfpDec2 = new DfpDec(getField(), Double.POSITIVE_INFINITY);
 Dfp nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertTrue(nextAfter.isInfinity());
 }

 @Test
 public void testNextAfterWithNegativeInfinity() {
 DfpDec dfpDec1 = new DfpDec(getField(), Double.NEGATIVE_INFINITY);
 DfpDec dfpDec2 = new DfpDec(getField(), Double.NEGATIVE_INFINITY);
 Dfp nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertTrue(nextAfter.isNegativeInfinity());
 }
}