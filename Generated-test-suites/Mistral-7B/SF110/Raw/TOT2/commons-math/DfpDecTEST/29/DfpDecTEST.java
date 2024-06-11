package org.apache.commons.math3.dfp;

import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 @Test
 public void testConstructorWithFactory() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 assertEquals("Initial value should be zero.", new DfpDec(new DfpField(16)).toString(), dfpDec.toString());
 }

 @Test
 public void testNewInstanceWithFactory() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 Dfp dfp = new Dfp(new DfpField(16), 1.0);
 DfpDec dfpDecInstance = dfpDec.newInstance(dfp);
 assertEquals("Instance value should be the same as the input Dfp.", dfp.toString(), dfpDecInstance.toString());
 }

 @Test
 public void testNewInstanceWithInt() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 int intValue = 42;
 DfpDec dfpDecInstance = dfpDec.newInstance(intValue);
 assertEquals("Instance value should be the same as the input int.", Integer.toString(intValue), dfpDecInstance.toString());
 }

 @Test
 public void testNewInstanceWithLong() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 long longValue = 42L;
 DfpDec dfpDecInstance = dfpDec.newInstance(longValue);
 assertEquals("Instance value should be the same as the input long.", Long.toString(longValue), dfpDecInstance.toString());
 }

 @Test
 public void testNewInstanceWithDouble() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 double doubleValue = 42.5;
 DfpDec dfpDecInstance = dfpDec.newInstance(doubleValue);
 assertEquals("Instance value should be the same as the input double.", Double.toString(doubleValue), dfpDecInstance.toString());
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 Dfp dfp = new Dfp(new DfpField(16), 1.0);
 DfpDec dfpDecInstance = dfpDec.newInstance(dfp);
 assertEquals("Instance value should be the same as the input Dfp.", dfp.toString(), dfpDecInstance.toString());
 }

 @Test
 public void testNewInstanceWithString() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 String stringValue = "3.14159265358979323846";
 DfpDec dfpDecInstance = dfpDec.newInstance(stringValue);
 assertEquals("Instance value should be the same as the input String.", stringValue, dfpDecInstance.toString());
 }

 @Test
 public void testNewInstanceWithSignAndNans() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 byte sign = -1;
 byte nans = 0;
 DfpDec dfpDecInstance = dfpDec.newInstance(sign, nans);
 assertEquals("Instance value should have the specified sign and NaN flag.", sign, dfpDecInstance.getSign());
 assertEquals("Instance value should have the specified NaN flag.", nans, dfpDecInstance.getNans());
 assertEquals("Instance value should be NaN.", "NaN", dfpDecInstance.toString());
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals("The number of decimal digits should match the radix digits.", 64, decimalDigits);
 }

 @Test
 public void testRoundWithIn() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 int in = 0;
 int roundResult = dfpDec.round(in);
 assertEquals("Rounding should not change the value.", 0, roundResult);
 }

 @Test
 public void testRoundWithNonZero() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 int in = 42;
 int roundResult = dfpDec.round(in);
 assertEquals("The rounding result should be the same as the input.", 42, roundResult);
 }

 @Test
 public void testRoundWithOverflow() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 int in = Integer.MAX_VALUE + 1;
 int roundResult = dfpDec.round(in);
 assertEquals("Rounding should set the IEEE flags with code 4.", 4, roundResult);
 }

 @Test
 public void testRoundWithUnderflow() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 int in = Integer.MIN_VALUE - 1;
 int roundResult = dfpDec.round(in);
 assertEquals("Rounding should set the IEEE flags with code 8.", 8, roundResult);
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 DfpDec nextAfterZero = dfpDec.nextAfter(new DfpDec(new DfpField(16), 0));
 assertEquals("NextAfter with zero should return the same zero.", "0", nextAfterZero.toString());
 }

 @Test
 public void testNextAfterWithNonZero() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 DfpDec x = new DfpDec(new DfpField(16), 1.5);
 DfpDec nextAfter = dfpDec.nextAfter(x);
 assertEquals("NextAfter should be greater than the input.", x.toString(), "<", nextAfter.toString());
 }

 @Test
 public void testNextAfterWithNegative() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 DfpDec x = new DfpDec(new DfpField(16), -1.5);
 DfpDec nextAfter = dfpDec.nextAfter(x);
 assertEquals("NextAfter should be less than the input.", x.toString(), ">", nextAfter.toString());
 }

 @Test
 public void testNextAfterWithMaxDouble() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 DfpDec x = new DfpDec(new DfpField(16), Double.POSITIVE_INFINITY);
 DfpDec nextAfter = dfpDec.nextAfter(x);
 assertEquals("NextAfter with max double should return positive infinity.", "Inf", nextAfter.toString());
 }

 @Test
 public void testNextAfterWithMinDouble() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 DfpDec x = new DfpDec(new DfpField(16), Double.NEGATIVE_INFINITY);
 DfpDec nextAfter = dfpDec.nextAfter(x);
 assertEquals("NextAfter with min double should return negative infinity.", "NegInf", nextAfter.toString());
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpDec dfpDec = new DfpDec(new DfpField(16));
 DfpDec x = new DfpDec(new DfpField(16), Double.NaN);
 DfpDec nextAfter = dfpDec.nextAfter(x);
 assertEquals("NextAfter with NaN should return the same NaN.", "NaN", nextAfter.toString());
 }
}