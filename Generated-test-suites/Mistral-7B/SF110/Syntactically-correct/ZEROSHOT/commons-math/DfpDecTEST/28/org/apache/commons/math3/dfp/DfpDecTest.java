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

 private DfpFieldFactory dfFactory = new Dfp128Field();

 @Test
 public void testNewInstance() {
 DfpDec d1 = new DfpDec(dfFactory);
 DfpDec d2 = d1.newInstance();
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceInt() {
 DfpDec d1 = new DfpDec(dfFactory, 42);
 DfpDec d2 = d1.newInstance(42);
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceDouble() {
 DfpDec d1 = new DfpDec(dfFactory, 3.1415);
 DfpDec d2 = d1.newInstance(3.1415);
 assertEquals(d1, d2, 0.0001);
 }

 @Test
 public void testNewInstanceDfp() {
 DfpDec d1 = new DfpDec(dfFactory, new DfpDec(dfFactory, 2));
 DfpDec d2 = d1.newInstance(d1);
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceString() {
 DfpDec d1 = new DfpDec(dfFactory, "123.456");
 DfpDec d2 = d1.newInstance("123.456");
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceByte() {
 DfpDec d1 = new DfpDec(dfFactory, (byte) 0x80);
 DfpDec d2 = d1.newInstance((byte) 0x80);
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceByteSign() {
 DfpDec d1 = new DfpDec(dfFactory, (byte) 0x80, (byte) 1);
 DfpDec d2 = d1.newInstance((byte) 0x80, (byte) 1);
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceByteNaN() {
 DfpDec d1 = new DfpDec(dfFactory, (byte) 0x7F, (byte) 3);
 DfpDec d2 = d1.newInstance((byte) 0x7F, (byte) 3);
 assertEquals(d1, d2);
 assertFalse(d1.isNaN());
 assertFalse(d2.isNaN());
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec d = new DfpDec(dfFactory, 12345.6789);
 int decDigits = d.getDecimalDigits();
 assertEquals(49, decDigits);
 }

 @Test
 public void testRound() {
 DfpDec d1 = new DfpDec(dfFactory, 12345.6789);
 DfpDec result1 = d1.newInstance(12345.6788);
 DfpDec d2 = new DfpDec(dfFactory, 12345.6789);
 int roundMode = RoundingMode.HALF_UP.getCode();
 int roundResult1 = d1.round(roundMode);
 int roundResult2 = d2.round(roundMode);
 assertEquals(roundResult1, roundResult2);
 assertEquals(result1, d2.newInstance());
 }

 @Test
 public void testRoundWithInfinity() {
 DfpDec d1 = new DfpDec(dfFactory, 1.798E308);
 DfpDec result1 = d1.newInstance(1.798E308);
 DfpDec d2 = new DfpDec(dfFactory, 1.798E308);
 int roundMode = RoundingMode.HALF_UP.getCode();
 int roundResult1 = d1.round(roundMode);
 int roundResult2 = d2.round(roundMode);
 assertEquals(roundResult1, roundResult2);
 assertEquals(result1, d2.newInstance());
 }

 @Test
 public void testRoundWithNaN() {
 DfpDec d1 = new DfpDec(dfFactory, Double.NaN);
 DfpDec result1 = d1.newInstance(Double.NaN);
 DfpDec d2 = new DfpDec(dfFactory, Double.NaN);
 int roundMode = RoundingMode.HALF_UP.getCode();
 int roundResult1 = d1.round(roundMode);
 int roundResult2 = d2.round(roundMode);
 assertEquals(roundResult1, roundResult2);
 assertEquals(result1, d2.newInstance());
 }

 @Test
 public void testNextAfter() {
 DfpDec d1 = new DfpDec(dfFactory, 0.123456789);
 DfpDec d2 = new DfpDec(dfFactory, 0.123456789);
 DfpDec x = new DfpDec(dfFactory, 0.12345679);
 DfpDec y = d1.nextAfter(x);
 DfpDec z = d2.nextAfter(x);
 assertEquals(y, z);
 assertEquals(y, x.add(new DfpDec(dfFactory, "1E-9")));
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec d1 = new DfpDec(dfFactory, 0.0);
 DfpDec d2 = new DfpDec(dfFactory, 0.0);
 DfpDec x = new DfpDec(dfFactory, 1.0);
 DfpDec y = d1.nextAfter(x);
 DfpDec z = d2.nextAfter(x);
 assertEquals(y, z);
 assertEquals(y, x);
 }

 @Test
 public void testNextAfterWithNegativeZero() {
 DfpDec d1 = new DfpDec(dfFactory, 0.0, (byte) 0);
 DfpDec d2 = new DfpDec(dfFactory, 0.0, (byte) 0);
 DfpDec x = new DfpDec(dfFactory, -1.0);
 DfpDec y = d1.nextAfter(x);
 DfpDec z = d2.nextAfter(x);
 assertEquals(y, z);
 assertEquals(y, x);
 }

 @Test
 public void testNextAfterWithMaxValue() {
 DfpDec d1 = new DfpDec(dfFactory, 1.798E308);
 DfpDec d2 = new DfpDec(dfFactory, 1.798E308);
 DfpDec x = new DfpDec(dfFactory, 1.798E308);
 DfpDec y = d1.nextAfter(x);
 DfpDec z = d2.nextAfter(x);
 assertEquals(y, z);
 assertEquals(y, new DfpDec(dfFactory, "1E-308").pow(new DfpDec(dfFactory, "2302")).add(x));
 }

 @Test
 public void testNextAfterWithMinValue() {
 DfpDec d1 = new DfpDec(dfFactory, -1.798E308);
 DfpDec d2 = new DfpDec(dfFactory, -1.798E308);
 DfpDec x = new DfpDec(dfFactory, -1.798E308);
 DfpDec y = d1.nextAfter(x);
 DfpDec z = d2.nextAfter(x);
 assertEquals(y, z);
 assertEquals(y, new DfpDec(dfFactory, "1E-308").pow(new DfpDec(dfFactory, "2303")).subtract(x));
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpDec d1 = new DfpDec(dfFactory, Double.NaN);
 DfpDec d2 = new DfpDec(dfFactory, Double.NaN);
 DfpDec x = new DfpDec(dfFactory, 0.1);
 DfpDec y = d1.nextAfter(x);
 DfpDec z = d2.nextAfter(x);
 assertFalse(y.isNaN());
 assertFalse(z.isNaN());
 assertEquals(y, z);
 assertNotEquals(y, d1.newInstance(x));
 assertNotEquals(y, d2.newInstance(x));
 }

 @Test
 public void testNextAfterWithInfinity() {
 DfpDec d1 = new DfpDec(dfFactory, Double.POSITIVE_INFINITY);
 DfpDec d2 = new DfpDec(dfFactory, Double.POSITIVE_INFINITY);
 DfpDec x = new DfpDec(dfFactory, 0.0);
 DfpDec y = d1.nextAfter(x);
 DfpDec z = d2.nextAfter(x);
 assertEquals(y, z);
 assertNotEquals(y, d1.newInstance(x));
 assertNotEquals(y, d2.newInstance(x));
 }

 @Test
 public void testNextAfterWithNegativeInfinity() {
 DfpDec d1 = new DfpDec(dfFactory, Double.NEGATIVE_INFINITY);
 DfpDec d2 = new DfpDec(dfFactory, Double.NEGATIVE_INFINITY);
 DfpDec x = new DfpDec(dfFactory, 0.0);
 DfpDec y = d1.nextAfter(x);
 DfpDec z = d2.nextAfter(x);
 assertEquals(y, z);
 assertNotEquals(y, d1.newInstance(x));
 assertNotEquals(y, d2.newInstance(x));
 }

 @Test
 public void testNextAfterWithSubnormal() {
 DfpDec d1 = new DfpDec(dfFactory, 1E-324);
 DfpDec d2 = new DfpDec(dfFactory, 1E-324);
 DfpDec x = new DfpDec(dfFactory, 1.0);
 DfpDec y = d1.nextAfter(x);
 DfpDec z = d2.nextAfter(x);
 assertEquals(y, z);
 assertEquals(y, x.add(new DfpDec(dfFactory, "1E-324").pow(new DfpDec(dfFactory, "127"))));
 }

 @Test
 public void testNextAfterWithSubnormalAndRounding() {
 DfpDec d1 = new DfpDec(dfFactory, 1E-324);
 DfpDec d2 = new DfpDec(dfFactory, 1E-324);
 DfpDec x = new DfpDec(dfFactory, 1.0);
 DfpDec y = d1.nextAfter(x, RoundingMode.CEILING);
 DfpDec z = d2.nextAfter(x, RoundingMode.CEILING);
 assertEquals(y, z);
 assertEquals(y, x.add(new DfpDec(dfFactory, "1E-324").pow(new DfpDec(dfFactory, "128"))));
 }

}