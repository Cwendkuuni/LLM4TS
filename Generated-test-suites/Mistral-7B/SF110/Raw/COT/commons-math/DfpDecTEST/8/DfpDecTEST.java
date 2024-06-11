package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFactory2;
import org.junit.Test;

public class DfpDecTest {

 private DfpField getField() {
 return DfpFactory2.getInstance();
 }

 @Test
 public void testDfpDecConstructor() {
 DfpDec d1 = new DfpDec(getField());
 assertEquals("Check instance creation", 0, d1.intValueExact());
 }

 @Test
 public void testDfpDecConstructorWithValue() {
 DfpDec d1 = new DfpDec(getField(), 1234);
 assertEquals("Check instance creation with value", 1234, d1.intValueExact());
 }

 @Test
 public void testDfpDecConstructorWithString() {
 DfpDec d1 = new DfpDec(getField(), "1234.5678");
 assertEquals("Check instance creation with string", 12345678, d1.longValueExact());
 }

 @Test
 public void testDfpDecNewInstance() {
 DfpDec d1 = new DfpDec(getField());
 DfpDec d2 = d1.newInstance();
 assertEquals("Check newInstance returns same instance", d1, d2);
 }

 @Test
 public void testDfpDecNewInstanceWithValue() {
 DfpDec d1 = new DfpDec(getField(), 1234);
 DfpDec d2 = d1.newInstance(5678);
 assertEquals("Check newInstance with value returns new instance", 5678 + 1234, d2.longValueExact());
 }

 @Test
 public void testDfpDecNewInstanceWithDfp() {
 DfpDec d1 = new DfpDec(getField());
 DfpDec d2 = new DfpDec(getField(), 1234);
 DfpDec d3 = d1.newInstance(d2);
 assertEquals("Check newInstance with Dfp returns new instance with correct value", 1234, d3.intValueExact());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDfpDecNewInstanceWithInvalidRadixDigits() {
 DfpDec d1 = new DfpDec(getField());
 DfpDec d2 = new DfpDec(getField(), 123);
 DfpDec d3 = d1.newInstance(d2);
 }

 @Test
 public void testDfpDecGetDecimalDigits() {
 DfpDec d1 = new DfpDec(getField(), 12345678.1234);
 int decDigits = d1.getDecimalDigits();
 assertEquals("Check decimalDigits", 28, decDigits);
 }

 @Test
 public void testDfpDecRound() {
 DfpDec d1 = new DfpDec(getField(), 12345678.1234);
 int roundResult = d1.round(1);
 assertEquals("Check rounding with roundingMode = ROUND_DOWN", 12345678, d1.longValueExact());
 assertEquals("Check rounding result", 12345678, (long) (d1.roundedValueExact()[0] & 0xFFFFFFFFL));
 assertEquals("Check rounding flags", 0, d1.getFlags());
 assertEquals("Check rounding result code", 0, roundResult);

 d1 = new DfpDec(getField(), 12345678.1235);
 roundResult = d1.round(1);
 assertEquals("Check rounding with roundingMode = ROUND_DOWN", 12345678, d1.longValueExact());
 assertEquals("Check rounding result", 12345679, (long) (d1.roundedValueExact()[0] & 0xFFFFFFFFL));
 assertEquals("Check rounding flags", 1, d1.getFlags());
 assertEquals("Check rounding result code", 1, roundResult);

 d1 = new DfpDec(getField(), 12345678.1235);
 roundResult = d1.round(0);
 assertEquals("Check rounding with roundingMode = ROUND_UP", 12345678, d1.longValueExact());
 assertEquals("Check rounding result", 12345679, (long) (d1.roundedValueExact()[0] & 0xFFFFFFFFL));
 assertEquals("Check rounding flags", 0, d1.getFlags());
 assertEquals("Check rounding result code", 0, roundResult);

 d1 = new DfpDec(getField(), 12345678.1235);
 roundResult = d1.round(2);
 assertEquals("Check rounding with roundingMode = ROUND_HALF_UP", 12345678, d1.longValueExact());
 assertEquals("Check rounding result", 12345678, (long) (d1.roundedValueExact()[0] & 0xFFFFFFFFL));
 assertEquals("Check rounding flags", 0, d1.getFlags());
 assertEquals("Check rounding result code", 0, roundResult);

 d1 = new DfpDec(getField(), 12345678.1235);
 roundResult = d1.round(5);
 assertEquals("Check rounding with roundingMode = ROUND_HALF_DOWN", 12345678, d1.longValueExact());
 assertEquals("Check rounding result", 12345678, (long) (d1.roundedValueExact()[0] & 0xFFFFFFFFL));
 assertEquals("Check rounding flags", 0, d1.getFlags());
 assertEquals("Check rounding result code", 0, roundResult);

 d1 = new DfpDec(getField(), 12345678.1235);
 roundResult = d1.round(6);
 assertEquals("Check rounding with roundingMode = ROUND_HALF_ODD", 12345678, d1.longValueExact());
 assertEquals("Check rounding result", 12345679, (long) (d1.roundedValueExact()[0] & 0xFFFFFFFFL));
 assertEquals("Check rounding flags", 0, d1.getFlags());
 assertEquals("Check rounding result code", 0, roundResult);

 d1 = new DfpDec(getField(), 12345678.1235);
 roundResult = d1.round(-1);
 assertEquals("Check rounding with roundingMode = ROUND_CEIL", 12345679, d1.longValueExact());
 assertEquals("Check rounding result", 12345679, (long) (d1.roundedValueExact()[0] & 0xFFFFFFFFL));
 assertEquals("Check rounding flags", 0, d1.getFlags());
 assertEquals("Check rounding result code", 0, roundResult);

 d1 = new DfpDec(getField(), 12345678.1235);
 roundResult = d1.round(-2);
 assertEquals("Check rounding with roundingMode = ROUND_FLOOR", 12345678, d1.longValueExact());
 assertEquals("Check rounding result", 12345678, (long) (d1.roundedValueExact()[0] & 0xFFFFFFFFL));
 assertEquals("Check rounding flags", 0, d1.getFlags());
 assertEquals("Check rounding result code", 0, roundResult);
 }

 @Test
 public void testDfpDecNextAfter() {
 DfpDec d1 = new DfpDec(getField(), 12345678.1234);
 DfpDec d2 = new DfpDec(getField(), 12345678.1235);
 DfpDec d3 = d1.nextAfter(d2);
 assertEquals("Check nextAfter with up = false", 12345679, d3.longValueExact());

 d1 = new DfpDec(getField(), -12345678.1234);
 d2 = new DfpDec(getField(), -12345678.1235);
 d3 = d1.nextAfter(d2);
 assertEquals("Check nextAfter with up = true", -12345677, d3.longValueExact());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDfpDecNextAfterWithInvalidRadixDigits() {
 DfpDec d1 = new DfpDec(getField(), 123);
 DfpDec d2 = new DfpDec(getField(), 123);
 DfpDec d3 = d1.nextAfter(d2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDfpDecNextAfterWithInvalidValues() {
 DfpDec d1 = new DfpDec(getField(), 12345678.1234);
 DfpDec d2 = new DfpDec(getField(), Double.POSITIVE_INFINITY);
 DfpDec d3 = d1.nextAfter(d2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDfpDecNextAfterWithNaN() {
 DfpDec d1 = new DfpDec(getField());
 DfpDec d2 = new DfpDec(getField(), Double.NaN);
 DfpDec d3 = d1.nextAfter(d2);
 }
}