package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFactory;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

public class DfpDecTest {

 private DfpField getField() {
 return DfpFactory.getInstance(64);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(getField());
 DfpDec newInstance = dfpDec.newInstance();
 assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceInt() {
 DfpDec dfpDec = new DfpDec(getField(), 123);
 DfpDec newInstance = dfpDec.newInstance(123);
 assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceLong() {
 DfpDec dfpDec = new DfpDec(getField(), 1234567890L);
 DfpDec newInstance = dfpDec.newInstance(1234567890L);
 assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceDouble() {
 DfpDec dfpDec = new DfpDec(getField(), 123.45);
 DfpDec newInstance = dfpDec.newInstance(123.45);
 assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceDfp() {
 DfpDec dfpDec1 = new DfpDec(getField(), 123.45);
 DfpDec dfpDec2 = new DfpDec(getField(), 123.45);
 DfpDec newInstance = dfpDec1.newInstance(dfpDec2);
 assertEquals(dfpDec1, newInstance);
 }

 @Test
 public void testNewInstanceString() {
 DfpDec dfpDec = new DfpDec(getField(), "123.45");
 DfpDec newInstance = dfpDec.newInstance("123.45");
 assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceByte() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1);
 DfpDec newInstance = dfpDec.newInstance((byte) 1);
 assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceByteNans() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1, (byte) 3);
 DfpDec newInstance = dfpDec.newInstance((byte) 1, (byte) 3);
 assertEquals(dfpDec, newInstance);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testNewInstanceInvalidRadixDigits() {
 DfpDec dfpDec = new DfpDec(getField(), 123);
 DfpDec newInstance = dfpDec.newInstance((byte) 10); // different radix digits
 assertFalse(true);
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(getField(), 123.45);
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(491, decimalDigits);
 }

 @Test
 public void testRoundZero() {
 DfpDec dfpDec = new DfpDec(getField(), 123.45);
 int result = dfpDec.round(0);
 assertEquals(0, result);
 }

 @Test
 public void testRoundNonZero() {
 DfpDec dfpDec = new DfpDec(getField(), 123.45);
 int result = dfpDec.round(1);
 assertTrue(result > 0);
 }

 @Test
 public void testRoundEdgeCases() {
 DfpDec dfpDec = new DfpDec(getField(), 0.5);
 int result = dfpDec.round(-1); // rounding mode not considered
 assertEquals(0, result);

 dfpDec = new DfpDec(getField(), 0.5);
 result = dfpDec.round(0); // rounding towards zero
 assertEquals(0, result);

 dfpDec = new DfpDec(getField(), 0.5);
 result = dfpDec.round(1); // rounding up
 assertEquals(1, result);

 dfpDec = new DfpDec(getField(), 0.5);
 result = dfpDec.round(-123456); // rounding mode not considered
 assertEquals(-123456, result);

 dfpDec = new DfpDec(getField(), 0.5);
 result = dfpDec.round(123456); // rounding mode not considered
 assertEquals(123457, result);
}

 @Test
 public void testNextAfterSameValue() {
 DfpDec dfpDec = new DfpDec(getField(), 123.45);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(dfpDec, nextAfter);
 }

 @Test
 public void testNextAfterLessValue() {
 DfpDec dfpDec1 = new DfpDec(getField(), 123.44);
 DfpDec nextAfter = dfpDec1.nextAfter(new DfpDec(getField(), 123.45));
 assertTrue(nextAfter.compareTo(dfpDec1) > 0);
 }

 @Test
 public void testNextAfterGreaterValue() {
 DfpDec dfpDec1 = new DfpDec(getField(), 123.46);
 DfpDec nextAfter = dfpDec1.nextAfter(new DfpDec(getField(), 123.45));
 assertTrue(nextAfter.compareTo(dfpDec1) < 0);
 }

 @Test
 public void testNextAfterZero() {
 DfpDec dfpDec1 = new DfpDec(getField(), 0);
 DfpDec nextAfter = dfpDec1.nextAfter(new DfpDec(getField(), 1));
 assertTrue(nextAfter.compareTo(dfpDec1) > 0);
 }

 @Test
 public void testNextAfterNaN() {
 DfpDec dfpDec1 = new DfpDec(getField(), Double.NaN);
 DfpDec nextAfter = dfpDec1.nextAfter(new DfpDec(getField(), 1));
 assertFalse(nextAfter.isNaN());
 assertTrue(nextAfter.compareTo(new DfpDec(getField(), 1)) < 0);
 }

 @Test
 public void testNextAfterNegativeValue() {
 DfpDec dfpDec1 = new DfpDec(getField(), -123.45);
 DfpDec nextAfter = dfpDec1.nextAfter(new DfpDec(getField(), -123.44));
 assertTrue(nextAfter.compareTo(dfpDec1) < 0);
 }
}