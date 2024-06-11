package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

public class DfpDecTest {

 private DfpField getField() {
 return new Dfp128Field();
 }

 @Test
 public void testConstructorWithFactory() {
 DfpDec dfpDec = new DfpDec(getField());
 assertEquals(0, dfpDec.doubleValue(), 0);
 }

 @Test
 public void testConstructorWithFactoryAndByte() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1);
 assertEquals(1, dfpDec.doubleValue(), 0);
 }

 @Test
 public void testConstructorWithFactoryAndInt() {
 DfpDec dfpDec = new DfpDec(getField(), 1);
 assertEquals(1, dfpDec.doubleValue(), 0);
 }

 @Test
 public void testConstructorWithFactoryAndLong() {
 DfpDec dfpDec = new DfpDec(getField(), 1L);
 assertEquals(1, dfpDec.doubleValue(), 0);
 }

 @Test
 public void testConstructorWithFactoryAndDouble() {
 DfpDec dfpDec = new DfpDec(getField(), 1.0);
 assertEquals(1, dfpDec.doubleValue(), 0);
 }

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec = new DfpDec(new DfpDec(getField(), 1.0));
 assertEquals(1, dfpDec.doubleValue(), 0);
 }

 @Test
 public void testConstructorWithFactoryAndString() {
 DfpDec dfpDec = new DfpDec(getField(), "1.0");
 assertEquals(1, dfpDec.doubleValue(), 0);
 }

 @Test
 public void testConstructorWithFactoryByteSignByteNans() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1, (byte) 3);
 assertEquals(1, dfpDec.doubleValue(), 0);
 assertEquals(3, dfpDec.getNans(), 0);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(getField());
 DfpDec newInstance = dfpDec.newInstance();
 assertEquals(0, newInstance.doubleValue(), 0);
 }

 @Test
 public void testNewInstanceWithByte() {
 DfpDec dfpDec = new DfpDec(getField(), 1);
 DfpDec newInstance = dfpDec.newInstance((byte) 1);
 assertEquals(1, newInstance.doubleValue(), 0);
 }

 @Test
 public void testNewInstanceWithInt() {
 DfpDec dfpDec = new DfpDec(getField(), 1);
 DfpDec newInstance = dfpDec.newInstance(1);
 assertEquals(1, newInstance.doubleValue(), 0);
 }

 @Test
 public void testNewInstanceWithLong() {
 DfpDec dfpDec = new DfpDec(getField(), 1L);
 DfpDec newInstance = dfpDec.newInstance(1L);
 assertEquals(1, newInstance.doubleValue(), 0);
 }

 @Test
 public void testNewInstanceWithDouble() {
 DfpDec dfpDec = new DfpDec(getField(), 1.0);
 DfpDec newInstance = dfpDec.newInstance(1.0);
 assertEquals(1, newInstance.doubleValue(), 0);
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec dfpDec = new DfpDec(getField(), new DfpDec(getField(), 1.0));
 DfpDec newInstance = dfpDec.newInstance(new DfpDec(getField(), 1.0));
 assertEquals(1, newInstance.doubleValue(), 0);
 }

 @Test
 public void testNewInstanceWithString() {
 DfpDec dfpDec = new DfpDec(getField(), "1.0");
 DfpDec newInstance = dfpDec.newInstance("1.0");
 assertEquals(1, newInstance.doubleValue(), 0);
 }

 @Test
 public void testNewInstanceWithByteSignByteNans() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1, (byte) 3);
 DfpDec newInstance = dfpDec.newInstance((byte) 1, (byte) 3);
 assertEquals(1, newInstance.doubleValue(), 0);
 assertEquals(3, newInstance.getNans(), 0);
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(getField(), 1234.5678);
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(18, decimalDigits);
 }

 @Test
 public void testRoundWithZero() {
 DfpDec dfpDec = new DfpDec(getField(), 1234.5678);
 int result = dfpDec.round(0);
 assertEquals(0, result);
 }

 @Test
 public void testRoundWithPositiveNumber() {
 DfpDec dfpDec = new DfpDec(getField(), 1234.5678);
 int result = dfpDec.round(0.5);
 assertEquals(1, result);
 }

 @Test
 public void testRoundWithNegativeNumber() {
 DfpDec dfpDec = new DfpDec(getField(), -1234.5678);
 int result = dfpDec.round(0.5);
 assertEquals(-1, result);
 }

 @Test
 public void testRoundWithOverflow() {
 DfpDec dfpDec = new DfpDec(getField(), Long.MAX_VALUE);
 int result = dfpDec.round(0.5);
 assertEquals(4, result);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testRoundWithInvalidRoundingMode() {
 DfpDec dfpDec = new DfpDec(getField(), 1234.5678);
 int result = dfpDec.round(-1);
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec dfpDec = new DfpDec(getField(), 0);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(getField(), 0));
 assertEquals(0, nextAfter.doubleValue(), 0);
 }

 @Test
 public void testNextAfterWithPositiveNumber() {
 DfpDec dfpDec = new DfpDec(getField(), 1234.5678);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(getField(), 1234.5679));
 assertEquals(1234.5679, nextAfter.doubleValue(), 0);
 }

 @Test
 public void testNextAfterWithNegativeNumber() {
 DfpDec dfpDec = new DfpDec(getField(), -1234.5678);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(getField(), -1234.5677));
 assertEquals(-1234.5677, nextAfter.doubleValue(), 0);
 }

 @Test
 public void testNextAfterWithInfiniteNumber() {
 DfpDec dfpDec = new DfpDec(getField(), Dfp.POSITIVE_INFINITY);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(getField(), Dfp.POSITIVE_INFINITY));
 assertFalse(nextAfter.isNaN());
 assertFalse(nextAfter.isInfinite());
 assertEquals(Dfp.POSITIVE_INFINITY, nextAfter.doubleValue(), 0);
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpDec dfpDec = new DfpDec(getField(), Dfp.NaN);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(getField(), 0));
 assertTrue(nextAfter.isNaN());
 }

 @Test
 public void testNextAfterWithNegativeAndPositiveNumbers() {
 DfpDec dfpDec = new DfpDec(getField(), -1234.5678);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(getField(), 1234.5679));
 assertEquals(-1234.5678, dfpDec.doubleValue(), 0);
 assertEquals(1234.5679, nextAfter.doubleValue(), 0);
 }
}