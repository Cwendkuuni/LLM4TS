package org.apache.commons.math3.dfp.test;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 @Test
 public void testConstructorWithInt() {
 DfpDec dfpDec = new DfpDec(new DfpField(4), 123);
 assertEquals(123.0, dfpDec.doubleValue(), 0.0001);
 }

 @Test
 public void testConstructorWithDouble() {
 DfpDec dfpDec = new DfpDec(new DfpField(4), 123.45);
 assertEquals(123.45, dfpDec.doubleValue(), 0.0001);
 }

 @Test
 public void testConstructorWithString() {
 DfpDec dfpDec = new DfpDec(new DfpField(4), "123.45");
 assertEquals(123.45, dfpDec.doubleValue(), 0.0001);
 }

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec1 = new DfpDec(new DfpField(4), new DfpDec(new DfpField(4), 123.45));
 DfpDec dfpDec2 = new DfpDec(new DfpDec(dfpDec1));
 assertEquals(dfpDec1.doubleValue(), dfpDec2.doubleValue(), 0.0001);
 }

 @Test
 public void testRoundingWithNegativeNumber() {
 DfpDec dfpDec = new DfpDec(new DfpField(4), -123.45);
 int roundResult = dfpDec.round(DfpField.ROUND_DOWN);
 assertEquals(-123.45, dfpDec.doubleValue(), 0.0001);
 assertEquals(-123, roundResult);
 }

 @Test
 public void testRoundingWithZero() {
 DfpDec dfpDec = new DfpDec(new DfpField(4), 0.5);
 int roundResult = dfpDec.round(DfpField.ROUND_DOWN);
 assertEquals(0.0, dfpDec.doubleValue(), 0.0001);
 assertEquals(0, roundResult);
 }

 @Test
 public void testRoundingWithPositiveNumber() {
 DfpDec dfpDec = new DfpDec(new DfpField(4), 123.45);
 int roundResult = dfpDec.round(DfpField.ROUND_UP);
 assertEquals(124.0, dfpDec.doubleValue(), 0.0001);
 assertEquals(124, roundResult);
 }

 @Test
 public void testNextAfterWithNegativeNumber() {
 DfpDec dfpDec1 = new DfpDec(new DfpField(4), -123.45);
 DfpDec dfpDec2 = dfpDec1.nextAfter(new DfpDec(new DfpField(4), -123.5));
 assertEquals(-123.44999999999999555910572189432629516552734375, dfpDec1.doubleValue(), 0.0000000000000001);
 assertEquals(-123.5, dfpDec2.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec dfpDec1 = new DfpDec(new DfpField(4), 0.0);
 DfpDec dfpDec2 = dfpDec1.nextAfter(new DfpDec(new DfpField(4), 0.0));
 assertEquals(0.0, dfpDec1.doubleValue(), 0.0001);
 assertEquals(0.0, dfpDec2.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterWithPositiveNumber() {
 DfpDec dfpDec1 = new DfpDec(new DfpField(4), 123.45);
 DfpDec dfpDec2 = dfpDec1.nextAfter(new DfpDec(new DfpField(4), 123.46));
 assertEquals(123.459999999999996662157313284452292257080078125, dfpDec1.doubleValue(), 0.0000000000000001);
 assertEquals(123.46, dfpDec2.doubleValue(), 0.0001);
 }

}