package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.exception.ArithmeticException;
import org.apache.commons.math3.exception.DivisionByZeroException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.junit.Test;

public class DfpDecTest {

 private DfpField getField() {
 return new Dfp128Field();
 }

 @Test
 public void testConstructorWithFactory() {
 DfpDec dfpDec = new DfpDec(getField());
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndByte() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndInt() {
 DfpDec dfpDec = new DfpDec(getField(), 1);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndLong() {
 DfpDec dfpDec = new DfpDec(getField(), 1L);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndDouble() {
 DfpDec dfpDec = new DfpDec(getField(), 1.0);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec = new DfpDec(new DfpDec(getField(), 1.0));
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndString() {
 DfpDec dfpDec = new DfpDec(getField(), "1");
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryByteSignByteNans() {
 DfpDec dfpDec = new DfpDec(getField(), (byte) 1, (byte) 3);
 assertNotNull(dfpDec);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec();
 DfpDec newInstance = dfpDec.newInstance();
 assertNotNull(newInstance);
 }

 @Test
 public void testNewInstanceWithByte() {
 DfpDec dfpDec = new DfpDec();
 DfpDec newInstance = dfpDec.newInstance((byte) 1);
 assertNotNull(newInstance);
 }

 @Test
 public void testNewInstanceWithInt() {
 DfpDec dfpDec = new DfpDec();
 DfpDec newInstance = dfpDec.newInstance(1);
 assertNotNull(newInstance);
 }

 @Test
 public void testNewInstanceWithLong() {
 DfpDec dfpDec = new DfpDec();
 DfpDec newInstance = dfpDec.newInstance(1L);
 assertNotNull(newInstance);
 }

 @Test
 public void testNewInstanceWithDouble() {
 DfpDec dfpDec = new DfpDec();
 DfpDec newInstance = dfpDec.newInstance(1.0);
 assertNotNull(newInstance);
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec dfpDec = new DfpDec();
 DfpDec newInstance = dfpDec.newInstance(new DfpDec(getField(), 1.0));
 assertNotNull(newInstance);
 }

 @Test
 public void testNewInstanceWithString() {
 DfpDec dfpDec = new DfpDec();
 DfpDec newInstance = dfpDec.newInstance("1");
 assertNotNull(newInstance);
 }

 @Test
 public void testNewInstanceWithByteSignByteNans() {
 DfpDec dfpDec = new DfpDec();
 DfpDec newInstance = dfpDec.newInstance((byte) 1, (byte) 3);
 assertNotNull(newInstance);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testNewInstanceWithInvalidField() {
 DfpDec dfpDec = new DfpDec();
 DfpDec newInstance = dfpDec.newInstance(new DfpField() {
 @Override
 public int getRadixDigits() {
 return 0;
 }
 });
 }

 @Test(expected = ArithmeticException.class)
 public void testRoundWithNegativeNumberAndRoundDown() {
 DfpDec dfpDec = new DfpDec(getField(), -1);
 dfpDec.round(-1);
 }

 @Test
 public void testRoundWithZero() {
 DfpDec dfpDec = new DfpDec(getField(), 0);
 int result = dfpDec.round(0);
 assertEquals(0, result);
 }

 @Test
 public void testRoundWithPositiveNumber() {
 DfpDec dfpDec = new DfpDec(getField(), 1);
 int result = dfpDec.round(0);
 assertNotEquals(0, result);
 }

 @Test
 public void testRoundWithNumberGreaterThanMaxValue() {
 DfpDec dfpDec = new DfpDec(getField(), Double.MAX_VALUE);
 int result = dfpDec.round(0);
 assertEquals(4, result);
 }

 @Test
 public void testRoundWithNumberLessThanMinValue() {
 DfpDec dfpDec = new DfpDec(getField(), Double.MIN_VALUE);
 int result = dfpDec.round(0);
 assertEquals(16, result);
 }

 @Test
 public void testNextAfterWithSmallerNumber() {
 DfpDec dfpDec1 = new DfpDec(getField(), 0.5);
 DfpDec dfpDec2 = new DfpDec(getField(), 0.4);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertTrue(dfpDec1.compareTo(nextAfter) > 0);
 }

 @Test
 public void testNextAfterWithEqualNumber() {
 DfpDec dfpDec1 = new DfpDec(getField(), 0.5);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec1);
 assertEquals(dfpDec1, nextAfter);
 }

 @Test
 public void testNextAfterWithGreaterNumber() {
 DfpDec dfpDec1 = new DfpDec(getField(), 0.5);
 DfpDec dfpDec2 = new DfpDec(getField(), 0.6);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertTrue(dfpDec1.compareTo(nextAfter) <= 0);
 }

 @Test(expected = DivisionByZeroException.class)
 public void testNextAfterWithDivisionByZero() {
 DfpDec dfpDec = new DfpDec(getField(), 1);
 DfpDec x = new DfpDec(getField(), 0);
 DfpDec nextAfter = dfpDec.nextAfter(x);
 }

 @Test(expected = MathUnsupportedOperationException.class)
 public void testNextAfterWithNaN() {
 DfpDec dfpDec1 = new DfpDec(getField(), Double.NaN);
 DfpDec dfpDec2 = new DfpDec(getField(), Double.NaN);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 }

 @Test(expected = ArithmeticException.class)
 public void testNextAfterWithDifferentRadixDigits() {
 DfpDec dfpDec1 = new DfpDec(getField(), 1);
 DfpDec dfpDec2 = new DfpDec(getField(), 2);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 }

}