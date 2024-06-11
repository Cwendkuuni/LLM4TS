package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFactory2;
import org.apache.commons.math3.dfp.DfpUtils;
import org.junit.Test;

public class DfpDecTest {

 private DfpField field = DfpFactory2.getInstance(64);
 private DfpFactory2 dfpFactory = new DfpFactory2(field);

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec = new DfpDec(new DfpDec(dfpFactory, 1.0));
 assertEquals(1.0, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithString() {
 DfpDec dfpDec = new DfpDec(dfpFactory, "1.2345");
 assertEquals(1.2345, dfpDec.doubleValueExact(), 0.0001);
 }

 @Test
 public void testNewInstanceMethod() {
 DfpDec dfpDec = new DfpDec(dfpFactory, 1.2345);
 DfpDec newInstance = dfpDec.newInstance();
 assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testRoundMethodWithZero() {
 DfpDec dfpDec = new DfpDec(dfpFactory, 1.2345);
 assertEquals(0, dfpDec.round(0));
 assertEquals(1.2345, dfpDec.doubleValueExact(), 0.0001);
 }

 @Test
 public void testRoundMethodWithNonZero() {
 DfpDec dfpDec = new DfpDec(dfpFactory, 1.2346);
 assertEquals(1, dfpDec.round(0));
 assertEquals(1.235, dfpDec.doubleValueExact(), 0.0001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRoundMethodWithNegativeInteger() {
 DfpDec dfpDec = new DfpDec(dfpFactory, 1.2345);
 dfpDec.round(-1);
 }

 @Test
 public void testNextAfterMethodWithPositiveNumber() {
 DfpDec dfpDec = new DfpDec(dfpFactory, 1.2345);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(dfpFactory, 1.2346));
 assertEquals(1.2346, nextAfter.doubleValueExact(), 0.0001);
 }

 @Test
 public void testNextAfterMethodWithNegativeNumber() {
 DfpDec dfpDec = new DfpDec(dfpFactory, -1.2345);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(dfpFactory, -1.2344));
 assertEquals(-1.2344, nextAfter.doubleValueExact(), 0.0001);
 }

 @Test
 public void testNextAfterMethodWithZero() {
 DfpDec dfpDec = new DfpDec(dfpFactory, 0.0);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(dfpFactory, 0.0));
 assertEquals(0.0, nextAfter.doubleValueExact(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextAfterMethodWithDifferentRadixDigits() {
 DfpDec dfpDec = new DfpDec(dfpFactory, 1.2345);
 DfpDec x = new DfpDec(dfpFactory, 1.2346, 32);
 dfpDec.nextAfter(x);
 }

 @Test
 public void testNewInstanceWithDifferentRadixDigits() {
 DfpDec dfpDec = new DfpDec(dfpFactory, 1.2345);
 DfpDec newInstance = dfpDec.newInstance(new DfpDec(dfpFactory, 32));
 assertEquals(dfpDec, newInstance);
 }
}