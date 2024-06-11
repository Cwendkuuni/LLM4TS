package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

public class DfpDecTest {

 private static final DfpField FACTORY = new Dfp128Field(); // Assuming Dfp128Field is the implementation of DfpField

 @Test
 public void testConstructor_Dfp() {
 DfpDec dfpDec = new DfpDec(new DfpDec(FACTORY, 1.0));
 assertEquals(dfpDec.doubleValue(), 1.0, 0.0);
 }

 @Test
 public void testConstructor_int() {
 DfpDec dfpDec = new DfpDec(FACTORY, 42);
 assertEquals(dfpDec.doubleValue(), 42.0, 0.0);
 }

 @Test
 public void testConstructor_long() {
 DfpDec dfpDec = new DfpDec(FACTORY, 9223372036854775807L);
 assertEquals(dfpDec.doubleValue(), 9.223372036854775807E18, 0.0);
 }

 @Test
 public void testConstructor_double() {
 DfpDec dfpDec = new DfpDec(FACTORY, Double.POSITIVE_INFINITY);
 assertEquals(dfpDec.doubleValue(), Double.POSITIVE_INFINITY, 0.0);
 }

 @Test
 public void testConstructor_Dfp() {
 DfpDec dfpDec = new DfpDec(FACTORY, new DfpDec(FACTORY, 1.0));
 assertEquals(dfpDec.doubleValue(), 1.0, 0.0);
 }

 @Test
 public void testConstructor_String() {
 DfpDec dfpDec = new DfpDec(FACTORY, "1.2345");
 assertEquals(dfpDec.doubleValue(), 1.2345, 0.0);
 }

 @Test
 public void testConstructor_byte_byte() {
 DfpDec dfpDec = new DfpDec(FACTORY, (byte) 0x1, (byte) 0x3);
 assertEquals(dfpDec.sign(), 1);
 assertEquals(dfpDec.nans(), 3);
 }

 @Test
 public void testNewInstance_Dfp() {
 DfpDec dfpDec = new DfpDec(FACTORY);
 DfpDec newInstance = dfpDec.newInstance();
 assertEquals(newInstance, dfpDec);
 }

 @Test
 public void testNewInstance_byte() {
 DfpDec dfpDec = new DfpDec(FACTORY, (byte) 1);
 DfpDec newInstance = dfpDec.newInstance((byte) 1);
 assertEquals(newInstance, dfpDec);
 }

 @Test
 public void testNewInstance_int() {
 DfpDec dfpDec = new DfpDec(FACTORY, 42);
 DfpDec newInstance = dfpDec.newInstance(42);
 assertEquals(newInstance, dfpDec);
 }

 @Test
 public void testNewInstance_long() {
 DfpDec dfpDec = new DfpDec(FACTORY, 9223372036854775807L);
 DfpDec newInstance = dfpDec.newInstance(9223372036854775807L);
 assertEquals(newInstance, dfpDec);
 }

 @Test
 public void testNewInstance_double() {
 DfpDec dfpDec = new DfpDec(FACTORY, 1.2345);
 DfpDec newInstance = dfpDec.newInstance(1.2345);
 assertEquals(newInstance, dfpDec);
 }

 @Test
 public void testNewInstance_Dfp() {
 DfpDec dfpDec = new DfpDec(FACTORY, new DfpDec(FACTORY, 1.0));
 DfpDec newInstance = dfpDec.newInstance(dfpDec);
 assertEquals(newInstance, dfpDec);
 }

 @Test
 public void testNewInstance_String() {
 DfpDec dfpDec = new DfpDec(FACTORY, "1.2345");
 DfpDec newInstance = dfpDec.newInstance("1.2345");
 assertEquals(newInstance, dfpDec);
 }

 @Test
 public void testNewInstance_byte_byte() {
 DfpDec dfpDec = new DfpDec(FACTORY, (byte) 1, (byte) 3);
 DfpDec newInstance = dfpDec.newInstance((byte) 1, (byte) 3);
 assertEquals(newInstance, dfpDec);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testNewInstance_Dfp_invalidRadixDigits() {
 DfpDec dfpDec = new DfpDec(FACTORY, new DfpDec(FACTORY, 1.0));
 DfpDec newInstance = dfpDec.newInstance(new DfpDec(FACTORY, 5)); // Different radix digits
 assertNull(newInstance);
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.45);
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(decimalDigits, 5);
 }

 @Test
 public void testRound_int_zero() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.456789);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 0);
 }

 @Test
 public void testRound_int_positive() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.456789);
 int roundResult = dfpDec.round(1);
 assertEquals(roundResult, 123);
 }

 @Test
 public void testRound_int_negative() {
 DfpDec dfpDec = new DfpDec(FACTORY, -123.456789);
 int roundResult = dfpDec.round(1);
 assertEquals(roundResult, -123);
 }

 @Test
 public void testRound_int_up() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.5);
 int roundResult = dfpDec.round(1);
 assertEquals(roundResult, 124);
 }

 @Test
 public void testRound_int_down() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.5);
 int roundResult = dfpDec.round(0);
 assertEquals(roundResult, 123);
 }

 @Test
 public void testRound_int_halfUp() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.5);
 int roundResult = dfpDec.round(0.5f);
 assertEquals(roundResult, 124);
 }

 @Test
 public void testRound_int_halfDown() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.5);
 int roundResult = dfpDec.round(0.5d);
 assertEquals(roundResult, 123);
 }

 @Test
 public void testRound_int_halfEven() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.5);
 int roundResult = dfpDec.round(0.5);
 assertEquals(roundResult, 123);
 }

 @Test
 public void testRound_int_halfOdd() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.5);
 int roundResult = dfpDec.round(0.5f);
 assertEquals(roundResult, 124);
 }

 @Test
 public void testRound_int_ceiling() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.4999999);
 int roundResult = dfpDec.round(Math.ceil(0.01f));
 assertEquals(roundResult, 124);
 }

 @Test
 public void testRound_int_floor() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.5000001);
 int roundResult = dfpDec.round(Math.floor(0.01f));
 assertEquals(roundResult, 123);
 }

 @Test
 public void testNextAfter_Dfp_positive() {
 DfpDec dfpDec = new DfpDec(FACTORY, 123.456789);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(FACTORY, 123.456789 + 0.000001));
 assertEquals(nextAfter, new DfpDec(FACTORY, 123.456789 + 0.000001));
 }

 @Test
 public void testNextAfter_Dfp_negative() {
 DfpDec dfpDec = new DfpDec(FACTORY, -123.456789);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(FACTORY, -123.456789 - 0.000001));
 assertEquals(nextAfter, new DfpDec(FACTORY, -123.456789 - 0.000001));
 }

 @Test
 public void testNextAfter_Dfp_zero() {
 DfpDec dfpDec = new DfpDec(FACTORY, 0.0);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(FACTORY, 0.0));
 assertEquals(nextAfter, new DfpDec(FACTORY, 0.0));
 }

 @Test
 public void testNextAfter_Dfp_NaN() {
 DfpDec dfpDec = new DfpDec(FACTORY, Double.NaN);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(FACTORY, Double.NaN));
 assertNull(nextAfter);
 }

 @Test
 public void testNextAfter_Dfp_Infinity() {
 DfpDec dfpDec = new DfpDec(FACTORY, Double.POSITIVE_INFINITY);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(FACTORY, Double.POSITIVE_INFINITY));
 assertEquals(nextAfter, new DfpDec(FACTORY, Double.POSITIVE_INFINITY));
 }

 @Test
 public void testNextAfter_Dfp_negativeInfinity() {
 DfpDec dfpDec = new DfpDec(FACTORY, Double.NEGATIVE_INFINITY);
 DfpDec nextAfter = dfpDec.nextAfter(new DfpDec(FACTORY, Double.NEGATIVE_INFINITY));
 assertEquals(nextAfter, new DfpDec(FACTORY, Double.NEGATIVE_INFINITY));
 }
}