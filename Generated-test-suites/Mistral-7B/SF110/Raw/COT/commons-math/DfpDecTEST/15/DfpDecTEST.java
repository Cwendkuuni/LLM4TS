package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldConfigurable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

public class DfpDecTest {

 private DfpFieldConfigurable getTestField() {
 return new Dfp10Field();
 }

 @Test
 public void testNewInstance() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = DfpDec.newInstance(d1);
 assertEquals(d1, d2);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testNewInstanceDifferentRadixDigits() {
 DfpField field1 = getTestField();
 DfpField field2 = new Dfp16Field();
 DfpDec d1 = new DfpDec(field1);
 DfpDec d2 = DfpDec.newInstance(d1, field2);
 fail("Should throw MathIllegalArgumentException");
 }

 @Test
 public void testNewInstanceWithValue() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, 12345);
 DfpDec d2 = DfpDec.newInstance(d1);
 assertEquals(d1, d2);
 }

 @Test
 public void testRound() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, 12345.6789);
 int expected = 2;
 int actual = d1.round(expected);
 assertEquals(expected, actual);
 DfpDec d2 = d1.newInstance();
 assertEquals(d1.round(0), d2.round(expected));
 }

 @Test
 public void testRoundWithNegativeValue() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, -12345.6789);
 int expected = -2;
 int actual = d1.round(expected);
 assertEquals(expected, actual);
 DfpDec d2 = d1.newInstance();
 assertEquals(d1.round(0), d2.round(expected));
 }

 @Test
 public void testRoundWithMaxValue() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, Double.MAX_VALUE);
 int expected = 4;
 int actual = d1.round(expected);
 assertEquals(expected, actual);
 DfpDec d2 = d1.newInstance();
 assertEquals(d1.round(0), d2.round(expected));
 }

 @Test
 public void testRoundWithMinValue() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, Double.MIN_VALUE);
 int expected = 4;
 int actual = d1.round(expected);
 assertEquals(expected, actual);
 DfpDec d2 = d1.newInstance();
 assertEquals(d1.round(0), d2.round(expected));
 }

 @Test
 public void testNextAfter() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, 12345.6789);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec result = DfpDec.nextAfter(d1, d2);
 assertEquals(result, d2.nextAfter(d1));
 }

 @Test
 public void testNextAfterWithZero() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, 0);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec result = DfpDec.nextAfter(d1, d2);
 assertEquals(result, d2.nextAfter(d1));
 assertEquals(result, d1);
 }

 @Test
 public void testNextAfterWithNegativeValue() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, -12345.6789);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec result = DfpDec.nextAfter(d1, d2);
 assertEquals(result, d2.nextAfter(d1));
 }

 @Test
 public void testNextAfterWithMaxValue() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, Double.MAX_VALUE);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec result = DfpDec.nextAfter(d1, d2);
 assertEquals(result, d2.nextAfter(d1));
 }

 @Test
 public void testNextAfterWithMinValue() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, Double.MIN_VALUE);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec result = DfpDec.nextAfter(d1, d2);
 assertEquals(result, d2.nextAfter(d1));
 }

 @Test
 public void testNextAfterWithSubnormalValue() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, -1.4E-45);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec result = DfpDec.nextAfter(d1, d2);
 assertEquals(result, d2.nextAfter(d1));
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, Double.NaN);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec result = DfpDec.nextAfter(d1, d2);
 assertEquals(result, d2.nextAfter(d1));
 assertEquals(result.getClassify(), 3);
 }

 @Test
 public void testNextAfterWithInfinity() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, Double.POSITIVE_INFINITY);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec result = DfpDec.nextAfter(d1, d2);
 assertEquals(result, d2.nextAfter(d1));
 assertEquals(result.getClassify(), 2);
 }

 @Test
 public void testNextAfterWithNegativeInfinity() {
 DfpField field = getTestField();
 DfpDec d1 = new DfpDec(field, Double.NEGATIVE_INFINITY);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec result = DfpDec.nextAfter(d1, d2);
 assertEquals(result, d2.nextAfter(d1));
 assertEquals(result.getClassify(), 2);
 }
}