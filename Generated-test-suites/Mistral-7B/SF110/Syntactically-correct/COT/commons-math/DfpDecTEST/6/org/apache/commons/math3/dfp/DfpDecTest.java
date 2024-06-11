package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldFactory;
import org.junit.Test;

public class DfpDecTest {

 private DfpFieldFactory factory = new Dfp128Field();
 private DfpField field = factory.getField();

 @Test
 public void testConstructorWithFactory() {
 DfpDec dfpDec = new DfpDec(field);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndByte() {
 DfpDec dfpDec = new DfpDec(field, (byte) 1);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndInt() {
 DfpDec dfpDec = new DfpDec(field, 1);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndLong() {
 DfpDec dfpDec = new DfpDec(field, 1L);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndDouble() {
 DfpDec dfpDec = new DfpDec(field, 1.0);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec = new DfpDec(new DfpDec(field, 1.0));
 assertNotNull(dfpDec);
 assertEquals(dfpDec.toString(), "1.00000000000000000000000000000000000000000000000000000000000000000000000000000000000000001D000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001");
 }

 @Test
 public void testConstructorWithString() {
 DfpDec dfpDec = new DfpDec(field, "1.0");
 assertNotNull(dfpDec);
 assertEquals(dfpDec.toString(), "1.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000001D00000000000000000000000000000000000000000000000000000000000000000000000000000000000000001");
 }

 @Test
 public void testConstructorWithSignAndNans() {
 DfpDec dfpDec = new DfpDec(field, (byte) 1, (byte) 3);
 assertNotNull(dfpDec);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(field);
 DfpDec newInstance = dfpDec.newInstance();
 assertNotNull(newInstance);
 assertEquals(newInstance.toString(), dfpDec.toString());
 }

 @Test
 public void testNewInstanceWithByte() {
 DfpDec dfpDec = new DfpDec(field, 1);
 DfpDec newInstance = dfpDec.newInstance((byte) 1);
 assertNotNull(newInstance);
 assertEquals(newInstance.toString(), dfpDec.toString());
 }

 @Test
 public void testNewInstanceWithInt() {
 DfpDec dfpDec = new DfpDec(field, 1);
 DfpDec newInstance = dfpDec.newInstance(1);
 assertNotNull(newInstance);
 assertEquals(newInstance.toString(), dfpDec.toString());
 }

 @Test
 public void testNewInstanceWithLong() {
 DfpDec dfpDec = new DfpDec(field, 1L);
 DfpDec newInstance = dfpDec.newInstance(1L);
 assertNotNull(newInstance);
 assertEquals(newInstance.toString(), dfpDec.toString());
 }

 @Test
 public void testNewInstanceWithDouble() {
 DfpDec dfpDec = new DfpDec(field, 1.0);
 DfpDec newInstance = dfpDec.newInstance(1.0);
 assertNotNull(newInstance);
 assertEquals(newInstance.toString(), dfpDec.toString());
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec dfpDec = new DfpDec(field, new DfpDec(field, 1.0));
 DfpDec newInstance = dfpDec.newInstance(dfpDec);
 assertNotNull(newInstance);
 assertEquals(newInstance.toString(), dfpDec.toString());
 }

 @Test
 public void testNewInstanceWithString() {
 DfpDec dfpDec = new DfpDec(field, "1.0");
 DfpDec newInstance = dfpDec.newInstance("1.0");
 assertNotNull(newInstance);
 assertEquals(newInstance.toString(), dfpDec.toString());
 }

 @Test
 public void testNewInstanceWithSignAndNans() {
 DfpDec dfpDec = new DfpDec(field, (byte) 1, (byte) 3);
 DfpDec newInstance = dfpDec.newInstance((byte) 1, (byte) 3);
 assertNotNull(newInstance);
 assertEquals(newInstance.getSign(), dfpDec.getSign());
 assertEquals(newInstance.getNaN(), dfpDec.getNaN());
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(field, 123456789.123456789);
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(decimalDigits, 37);
 }

 @Test(expected = AssertionError.class)
 public void testRoundWithInvalidInput() {
 DfpDec dfpDec = new DfpDec(field, 1);
 int round = dfpDec.round(-1);
 fail("Unexpected rounding result.");
 }

 @Test
 public void testRoundWithZero() {
 DfpDec dfpDec = new DfpDec(field, 1.5);
 int round = dfpDec.round(0);
 assertEquals(round, 0);
 }

 @Test
 public void testRoundWithRoundingDown() {
 DfpDec dfpDec = new DfpDec(field, 1.5);
 int round = dfpDec.round(DfpField.ROUND_DOWN);
 assertEquals(round, 1);
 }

 @Test
 public void testRoundWithRoundingUp() {
 DfpDec dfpDec = new DfpDec(field, 1.4);
 int round = dfpDec.round(DfpField.ROUND_UP);
 assertEquals(round, 2);
 }

 @Test
 public void testRoundWithRoundingHalfUp() {
 DfpDec dfpDec = new DfpDec(field, 1.5);
 int round = dfpDec.round(DfpField.ROUND_HALF_UP);
 assertEquals(round, 2);
 }

 @Test
 public void testRoundWithRoundingHalfDown() {
 DfpDec dfpDec = new DfpDec(field, 1.5);
 int round = dfpDec.round(DfpField.ROUND_HALF_DOWN);
 assertEquals(round, 1);
 }

 @Test
 public void testRoundWithRoundingHalfEven() {
 DfpDec dfpDec = new DfpDec(field, 1.5);
 int round = dfpDec.round(DfpField.ROUND_HALF_EVEN);
 assertEquals(round, 1);
 }

 @Test
 public void testRoundWithRoundingHalfOdd() {
 DfpDec dfpDec = new DfpDec(field, 1.5);
 int round = dfpDec.round(DfpField.ROUND_HALF_ODD);
 assertEquals(round, 2);
 }

 @Test
 public void testRoundWithRoundingCeil() {
 DfpDec dfpDec = new DfpDec(field, 1.4);
 int round = dfpDec.round(DfpField.ROUND_CEIL);
 assertEquals(round, 2);
 }

 @Test
 public void testRoundWithNegativeNumber() {
 DfpDec dfpDec = new DfpDec(field, -1.5);
 int round = dfpDec.round(0);
 assertEquals(round, -2);
 }

 @Test
 public void testRoundWithNegativeNumberAndRoundingUp() {
 DfpDec dfpDec = new DfpDec(field, -1.5);
 int round = dfpDec.round(DfpField.ROUND_UP);
 assertEquals(round, -1);
 }

 @Test
 public void testRoundWithNegativeNumberAndRoundingDown() {
 DfpDec dfpDec = new DfpDec(field, -1.5);
 int round = dfpDec.round(DfpField.ROUND_DOWN);
 assertEquals(round, -2);
 }

 @Test
 public void testRoundWithNegativeNumberAndRoundingHalfUp() {
 DfpDec dfpDec = new DfpDec(field, -1.5);
 int round = dfpDec.round(DfpField.ROUND_HALF_UP);
 assertEquals(round, -2);
 }

 @Test
 public void testRoundWithNegativeNumberAndRoundingHalfDown() {
 DfpDec dfpDec = new DfpDec(field, -1.5);
 int round = dfpDec.round(DfpField.ROUND_HALF_DOWN);
 assertEquals(round, -2);
 }

 @Test
 public void testRoundWithNegativeNumberAndRoundingHalfEven() {
 DfpDec dfpDec = new DfpDec(field, -1.5);
 int round = dfpDec.round(DfpField.ROUND_HALF_EVEN);
 assertEquals(round, -2);
 }

 @Test
 public void testRoundWithNegativeNumberAndRoundingHalfOdd() {
 DfpDec dfpDec = new DfpDec(field, -1.5);
 int round = dfpDec.round(DfpField.ROUND_HALF_ODD);
 assertEquals(round, -1);
 }

 @Test
 public void testRoundWithNegativeNumberAndRoundingCeil() {
 DfpDec dfpDec = new DfpDec(field, -1.4);
 int round = dfpDec.round(DfpField.ROUND_CEIL);
 assertEquals(round, -1);
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec dfpDec = new DfpDec(field, 0);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(nextAfter.toString(), "0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001");
 }

 @Test
 public void testNextAfterWithPositiveNumber() {
 DfpDec dfpDec = new DfpDec(field, 1.23456789123456789);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(nextAfter.toString(), "1.2345678912345679123456789123456789123456789123456789123456789123456789123456790D00000000000000000000000000000000000000000000000000000000000000000000000000000000000000001");
 }

 @Test
 public void testNextAfterWithNegativeNumber() {
 DfpDec dfpDec = new DfpDec(field, -1.23456789123456789);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(nextAfter.toString(), "-1.234567891234567889999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999992");
 }

 @Test
 public void testNextAfterWithNegativeNumberAndNaN() {
 DfpDec dfpDec = new DfpDec(field, -0.0d / 0.0d, 3);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(nextAfter.getNaN(), DfpField.NaNQuiet);
 }

 @Test
 public void testNextAfterWithNegativeNumberAndInfinity() {
 DfpDec dfpDec = new DfpDec(field, -1 / 0.0d, 3);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(nextAfter.getNaN(), DfpField.NaNSignaling);
 }
}