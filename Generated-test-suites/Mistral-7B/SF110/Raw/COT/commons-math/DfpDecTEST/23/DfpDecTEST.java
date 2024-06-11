package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

public class DfpDecTest {

 private DfpField field = new Dfp128Field();

 @Test
 public void testConstructorWithField() {
 DfpDec dfpDec = new DfpDec(field);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFieldAndDouble() {
 DfpDec dfpDec = new DfpDec(field, 3.14);
 assertEquals(3.14, dfpDec.doubleValue(), 0.0001);
 }

 @Test
 public void testConstructorWithFieldAndString() {
 DfpDec dfpDec = new DfpDec(field, "1.2345");
 assertEquals(1.2345, dfpDec.doubleValue(), 0.0001);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(field);
 DfpDec instance = dfpDec.newInstance();
 assertEquals(dfpDec, instance);
 }

 @Test
 public void testNewInstanceWithDouble() {
 DfpDec dfpDec = new DfpDec(field);
 DfpDec instance = dfpDec.newInstance(3.14);
 assertEquals(3.14, instance.doubleValue(), 0.0001);
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec dfpDec1 = new DfpDec(field, 1.23);
 DfpDec dfpDec2 = new DfpDec(field, 4.56);
 DfpDec instance = dfpDec1.newInstance(dfpDec2);
 assertEquals(5.79, instance.doubleValue(), 0.0001);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testNewInstanceWithInvalidDfp() {
 DfpDec dfpDec = new DfpDec(field);
 DfpDec dfpDec2 = new DfpDec(field, 1.23);
 DfpDec instance = dfpDec.newInstance(dfpDec2.newInstance(new byte[] { 1, 1 }));
 }

 @Test
 public void testRoundMethodZero() {
 DfpDec dfpDec = new DfpDec(field, 123456.7891);
 int rounded = dfpDec.round(0);
 assertEquals(0, rounded);
 }

 @Test
 public void testRoundMethodKeepFraction() {
 DfpDec dfpDec = new DfpDec(field, 123456.7891);
 int rounded = dfpDec.round(3);
 assertEquals(123456.789, dfpDec.doubleValue(), 0.0001);
 assertEquals(123456.789, dfpDec.round(3).doubleValue(), 0.0001);
 }

 @Test
 public void testRoundMethodRoundUp() {
 DfpDec dfpDec = new DfpDec(field, 123456.7891);
 int rounded = dfpDec.round(0.5F);
 assertTrue(dfpDec.doubleValue() < rounded.doubleValue());
 }

 @Test
 public void testRoundMethodRoundDown() {
 DfpDec dfpDec = new DfpDec(field, 123456.7891);
 int rounded = dfpDec.round(0.4F);
 assertTrue(rounded.doubleValue() <= dfpDec.doubleValue());
 }

 @Test
 public void testRoundMethodRoundHalfUp() {
 DfpDec dfpDec = new DfpDec(field, 123456.7891);
 int rounded = dfpDec.round(0.5F);
 assertTrue(rounded.doubleValue() > dfpDec.doubleValue());
 }

 @Test
 public void testRoundMethodRoundHalfDown() {
 DfpDec dfpDec = new DfpDec(field, 123456.7891);
 int rounded = dfpDec.round(0.4F);
 assertTrue(rounded.doubleValue() <= dfpDec.doubleValue());
 }

 @Test
 public void testRoundMethodRoundHalfEven() {
 DfpDec dfpDec = new DfpDec(field, 1.5);
 int rounded = dfpDec.round(0.5F);
 assertEquals(2, rounded);
 }

 @Test
 public void testRoundMethodRoundHalfOdd() {
 DfpDec dfpDec = new DfpDec(field, 1.5);
 int rounded = dfpDec.round(0.5F);
 assertEquals(1, rounded);
 }

 @Test
 public void testRoundMethodRoundCeil() {
 DfpDec dfpDec = new DfpDec(field, 1.1);
 int rounded = dfpDec.round(0);
 assertEquals(2, rounded);
 }

 @Test
 public void testRoundMethodRoundFloor() {
 DfpDec dfpDec = new DfpDec(field, 1.1);
 int rounded = dfpDec.round(0);
 assertEquals(1, rounded);
 }

 @Test
 public void testNextAfterMethodWithPositiveNumber() {
 DfpDec dfpDec = new DfpDec(field, 1.23);
 DfpDec nextAfter = dfpDec.nextAfter(DfpDec.ONE);
 assertEquals(2.23, nextAfter.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterMethodWithNegativeNumber() {
 DfpDec dfpDec = new DfpDec(field, -1.23);
 DfpDec nextAfter = dfpDec.nextAfter(DfpDec.ONE);
 assertEquals(-1.22, nextAfter.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterMethodWithZero() {
 DfpDec dfpDec = new DfpDec(field, 0);
 DfpDec nextAfter = dfpDec.nextAfter(DfpDec.ONE);
 assertEquals(0, nextAfter.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterMethodWithEqualNumbers() {
 DfpDec dfpDec1 = new DfpDec(field, 1.23);
 DfpDec dfpDec2 = new DfpDec(field, 1.23);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertEquals(1.23, nextAfter.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterMethodWithLargerNumber() {
 DfpDec dfpDec1 = new DfpDec(field, 1.23);
 DfpDec dfpDec2 = new DfpDec(field, 2.23);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertEquals(2.23, nextAfter.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterMethodWithSmallerNumber() {
 DfpDec dfpDec1 = new DfpDec(field, 1.23);
 DfpDec dfpDec2 = new DfpDec(field, 0.23);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertEquals(1.22, nextAfter.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterMethodWithNegativeAndLargerNumber() {
 DfpDec dfpDec1 = new DfpDec(field, -1.23);
 DfpDec dfpDec2 = new DfpDec(field, -0.23);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertEquals(-1.22, nextAfter.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterMethodWithNegativeAndSmallerNumber() {
 DfpDec dfpDec1 = new DfpDec(field, -1.23);
 DfpDec dfpDec2 = new DfpDec(field, -2.23);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertEquals(-1.24, nextAfter.doubleValue(), 0.0001);
 }

}