package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpRoundingMode;

import java.math.BigDecimal;
import java.util.Arrays;

public class DfpDecTest {

 private DfpField field;
 private DfpDec dfpDec;

 @Before
 public void setup() {
 field = new Dfp128Field();
 dfpDec = new DfpDec(field);
 }

 @After
 public void teardown() {
 dfpDec = null;
 field = null;
 }

 @Test
 public void testConstructor_Dfp() {
 Dfp dfp = new DfpDec(field, new DfpDec(field, 3.14));
 dfpDec = new DfpDec(dfp);
 assertEquals(dfp, dfpDec);
 }

 @Test
 public void testConstructor_String() {
 dfpDec = new DfpDec(field, "3.14");
 assertEquals(new BigDecimal("3.14"), dfpDec, 0.00001);
 }

 @Test
 public void testNewInstance_Null() {
 try {
 dfpDec.newInstance(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testNewInstance_Dfp() {
 Dfp dfp = new DfpDec(field, 3.14);
 DfpDec result = dfpDec.newInstance(dfp);
 assertEquals(dfp, result);
 }

 @Test
 public void testNewInstance_Byte() {
 byte x = 3;
 DfpDec result = dfpDec.newInstance(x);
 assertEquals((double) x, result.doubleValueExact(), 0.0);
 }

 @Test
 public void testNewInstance_Int() {
 int x = 3;
 DfpDec result = dfpDec.newInstance(x);
 assertEquals((double) x / 1000.0, result.doubleValueExact(), 0.0001);
 }

 @Test
 public void testNewInstance_Long() {
 long x = 31415;
 DfpDec result = dfpDec.newInstance(x);
 assertEquals(new BigDecimal(Long.toString(x)), result, 0.0);
 }

 @Test
 public void testRound_Zero() {
 int in = 0;
 int result = dfpDec.round(in);
 assertEquals(0, result);
 }

 @Test
 public void testRound_NegativeNumber() {
 int in = Integer.MIN_VALUE;
 int result = dfpDec.round(in);
 assertEquals(0, result);
 }

 @Test
 public void testRound_PositiveNumberLessThanMsb() {
 int in = 1;
 int result = dfpDec.round(in);
 assertEquals(0, result);
 }

 @Test
 public void testRound_PositiveNumberGreaterThanMsb() {
 int in = 1000;
 int result = dfpDec.round(in);
 assertEquals(1, result);
 }

 @Test
 public void testRound_PositiveNumberMsb() {
 int in = 10000;
 int result = dfpDec.round(in);
 assertEquals(1, result);
 }

 @Test
 public void testRound_NegativeNumberWithDiscardedBitSet() {
 int in = Integer.MIN_VALUE | 1;
 int result = dfpDec.round(in);
 assertEquals(0, result);
 }

 @Test
 public void testRound_PositiveNumberWithDiscardedBitSet() {
 int in = 10000 | 1;
 int result = dfpDec.round(in);
 assertEquals(1, result);
 }

 @Test
 public void testRound_DifferentRoundingModes() {
 DfpDec dfpDec1 = new DfpDec(field, 3.5);
 DfpDec dfpDec2 = new DfpDec(field, 3.6);
 DfpDec result1 = dfpDec1.round(DfpRoundingMode.DOWN.getValue());
 DfpDec result2 = dfpDec2.round(DfpRoundingMode.UP.getValue());
 assertNotEquals(result1, result2);
 }

 @Test(expected = AssertionError.class)
 public void testRound_InvalidRoundingMode() {
 DfpDec dfpDec = new DfpDec(field, 3.14);
 dfpDec.round(-1);
 }

 @Test
 public void testNextAfter_SameValue() {
 DfpDec x = new DfpDec(field, 3.14);
 DfpDec result = dfpDec.nextAfter(x);
 assertEquals(x, result);
 }

 @Test
 public void testNextAfter_NegativeZero() {
 DfpDec x = new DfpDec(field, new DfpDec(field, 0.0).negate());
 DfpDec result = dfpDec.nextAfter(x);
 assertEquals(new DfpDec(field, 0.0), result);
 }

 @Test
 public void testNextAfter_Zero() {
 DfpDec x = new DfpDec(field, 0.0);
 DfpDec result = dfpDec.nextAfter(x);
 assertEquals(new DfpDec(field, 0.0), result);
 }

 @Test
 public void testNextAfter_SmallPositiveNumber() {
 DfpDec x = new DfpDec(field, 0.0001);
 DfpDec result = dfpDec.nextAfter(x);
 assertEquals(new DfpDec(field, 0.0001), result);
 }

 @Test
 public void testNextAfter_SmallNegativeNumber() {
 DfpDec x = new DfpDec(field, -0.0001);
 DfpDec result = dfpDec.nextAfter(x);
 assertEquals(new DfpDec(field, -0.0001), result);
 }

 @Test
 public void testNextAfter_EqualValues() {
 DfpDec x1 = new DfpDec(field, 3.141592653589793238462643383279);
 DfpDec x2 = new DfpDec(field, 3.141592653589793238462643383279);
 DfpDec result = dfpDec.nextAfter(x1);
 assertEquals(x2, result);
 }

 @Test
 public void testNextAfter_InequalValuesWithSameSign() {
 DfpDec x1 = new DfpDec(field, 3.1415);
 DfpDec x2 = new DfpDec(field, 3.1416);
 DfpDec result = dfpDec.nextAfter(x1);
 assertTrue(x2.compareTo(result) > 0);
 }

 @Test
 public void testNextAfter_InequalValuesWithOppositeSigns() {
 DfpDec x1 = new DfpDec(field, -3.1415);
 DfpDec x2 = new DfpDec(field, -3.1414);
 DfpDec result = dfpDec.nextAfter(x1);
 assertTrue(x2.compareTo(result) < 0);
 }

 @Test
 public void testNextAfter_MaximumValue() {
 DfpDec x = new DfpDec(field, new DfpDec(field, "1.0E308").pow(2));
 DfpDec result = dfpDec.nextAfter(x);
 assertTrue(result.compareTo(x) > 0);
 }

 @Test
 public void testNextAfter_MinimumValue() {
 DfpDec x = new DfpDec(field, new DfpDec(field, "1.0E-308").pow(-1));
 DfpDec result = dfpDec.nextAfter(x);
 assertTrue(result.compareTo(x) < 0);
 }

 @Test
 public void testNextAfter_NaN() {
 DfpDec x = new DfpDec(field, Dfp.NaN);
 DfpDec result = dfpDec.nextAfter(x);
 assertFalse(result.isNaN());
 }

 @Test
 public void testNextAfter_Infinity() {
 DfpDec x = new DfpDec(field, new DfpDec(field, Double.POSITIVE_INFINITY).negate());
 DfpDec result = dfpDec.nextAfter(x);
 assertTrue(result.isInfinite());
 }
}