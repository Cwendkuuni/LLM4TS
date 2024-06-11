package org.apache.commons.math3.dfp.test;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 @Test
 public void testConstructorWithDouble() {
 DfpDec d1 = new DfpDec(new DfpField(4), 3.14);
 DfpDec d2 = new DfpDec(d1);
 assertEquals("3.14", d1, d2);
 }

 @Test
 public void testConstructorWithInt() {
 DfpDec d1 = new DfpDec(new DfpField(4), 123);
 DfpDec d2 = new DfpDec(d1.getField(), d1.intValueExact());
 assertEquals("123", d1, d2);
 }

 @Test
 public void testConstructorWithLong() {
 DfpDec d1 = new DfpDec(new DfpField(4), 1234567890L);
 DfpDec d2 = new DfpDec(d1.getField(), d1.longValueExact());
 assertEquals("1234567890", d1, d2);
 }

 @Test
 public void testConstructorWithString() {
 DfpDec d1 = new DfpDec(new DfpField(4), "123.456789");
 DfpDec d2 = new DfpDec(d1.getField(), d1.toString());
 assertEquals("123.456789", d1, d2);
 }

 @Test
 public void testNewInstanceWithValidArguments() {
 DfpDec d1 = new DfpDec(new DfpField(4), 123);
 DfpDec d2 = d1.newInstance();
 assertEquals("123", d1, d2);
 }

 @Test
 public void testNewInstanceWithInvalidArguments() {
 DfpDec d1 = new DfpDec(new DfpField(4), 123);
 try {
 DfpDec d2 = d1.newInstance("invalid");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testRound() {
 DfpDec d1 = new DfpDec(new DfpField(4), 123.456789);
 int roundingMode = DfpField.ROUND_HALF_UP;
 int result = d1.round(roundingMode);
 DfpDec expected = new DfpDec(new DfpField(4), 123.4568);
 assertEquals("Rounded correctly", expected.intValueExact(), result);
 }

 @Test
 public void testRoundWithNegativeNumber() {
 DfpDec d1 = new DfpDec(new DfpField(4), -123.456789);
 int roundingMode = DfpField.ROUND_HALF_DOWN;
 int result = d1.round(roundingMode);
 DfpDec expected = new DfpDec(new DfpField(4), -123.4567);
 assertEquals("Rounded correctly", expected.intValueExact(), result);
 }

 @Test
 public void testRoundWithNan() {
 DfpDec d1 = new DfpDec(new DfpField(4), Double.NaN);
 int roundingMode = DfpField.ROUND_DOWN;
 int result = d1.round(roundingMode);
 assertEquals("Should be unchanged", Dfp.NAN, d1);
 }

 @Test
 public void testRoundWithInf() {
 DfpDec d1 = new DfpDec(new DfpField(4), Double.POSITIVE_INFINITY);
 int roundingMode = DfpField.ROUND_DOWN;
 int result = d1.round(roundingMode);
 assertEquals("Should be unchanged", Dfp.POSITIVE_INFINITY, d1);
 }

 @Test
 public void testNextAfter() {
 DfpDec d1 = new DfpDec(new DfpField(4), 0.123456789);
 DfpDec d2 = new DfpDec(new DfpField(4), 0.12345679);
 DfpDec result = d1.nextAfter(d2);
 assertEquals("Next after", d2, result);
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec d1 = new DfpDec(new DfpField(4), 0);
 DfpDec result = d1.nextAfter(d1);
 assertEquals("Next after zero", Dfp.POSITIVE_INFINITY, result);
 }

 @Test
 public void testNextAfterWithNegativeNumber() {
 DfpDec d1 = new DfpDec(new DfpField(4), -0.123456789);
 DfpDec d2 = new DfpDec(new DfpField(4), -0.12345679);
 DfpDec result = d1.nextAfter(d2);
 assertEquals("Next after negative number", d2, result);
 }

 @Test
 public void testNextAfterWithInf() {
 DfpDec d1 = new DfpDec(new DfpField(4), Double.POSITIVE_INFINITY);
 DfpDec result = d1.nextAfter(d1);
 assertEquals("Next after infinity", (DfpDec) Double.POSITIVE_INFINITY, result);
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpDec d1 = new DfpDec(new DfpField(4), Double.NaN);
 DfpDec result = d1.nextAfter(d1);
 assertEquals("Next after NaN", Dfp.POSITIVE_INFINITY, result);
 }

 @Test
 public void testNextAfterWithDifferentRadixDigits() {
 DfpDec d1 = new DfpDec(new DfpField(4), 0.123456789);
 DfpDec d2 = new DfpDec(new DfpField(2), 0.0123456789);
 DfpDec result = d1.nextAfter(d2);
 this.getField().setIEEEFlagsBits(1);
 assertEquals("Next after with different radix digits", Dfp.NAN, result);
 }
}