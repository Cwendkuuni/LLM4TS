package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

public class DfpDecTest {

 private DfpField getField() {
 return new Dfp128Field();
 }

 @Test
 public void testConstructorWithFactory() {
 DfpDec dfpDec = new DfpDec(getField());
 assertNotNull(dfpDec);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testConstructorWithNegativeNumberOfRadixDigits() {
 try {
 DfpDec dfpDec = new DfpDec(getField(), -1);
 } catch (MathIllegalArgumentException e) {
 fail("Expected MathIllegalArgumentException but got " + e.getClass().getName());
 }
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec();
 Dfp newInstance = dfpDec.newInstance();
 assertSame(dfpDec, newInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithDfpOfDifferentRadixDigits() {
 DfpDec dfpDec = new DfpDec();
 Dfp anotherDfp = new Dfp108Field().newInstance(2.5);
 try {
 Dfp newInstance = dfpDec.newInstance(anotherDfp);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got " + e.getClass().getName());
 }
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec();
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(48, decimalDigits);
 }

 @Test
 public void testRound() {
 DfpDec dfpDec = new DfpDec();
 int in = 123456789;
 int rounded = dfpDec.round(in);
 assertEquals(0, rounded);

 DfpDec dfpDec2 = new DfpDec(getField(), 1.5);
 int rounded2 = dfpDec2.round(in);
 assertNotEquals(0, rounded2);

 DfpDec dfpDec3 = new DfpDec(getField(), 1.5);
 int rounded3 = dfpDec3.round(-in);
 assertNotEquals(0, rounded3);
}

 @Test
 public void testRoundWithRoundingMode() {
 DfpDec dfpDec = new DfpDec();
 int in = 123456789;
 int roundingModeUp = DfpField.ROUND_UP;
 int roundingModeDown = DfpField.ROUND_DOWN;
 int roundingModeHalfUp = DfpField.ROUND_HALF_UP;
 int roundingModeHalfDown = DfpField.ROUND_HALF_DOWN;
 int roundingModeHalfEven = DfpField.ROUND_HALF_EVEN;
 int roundingModeHalfOdd = DfpField.ROUND_HALF_ODD;
 int roundingModeCeil = DfpField.ROUND_CEIL;
 int roundingModeFloor = DfpField.ROUND_FLOOR;

 DfpDec dfpDecUp = new DfpDec(getField(), roundingModeUp);
 int roundedUp = dfpDecUp.round(in);
 assertTrue(roundedUp > 0);

 DfpDec dfpDecDown = new DfpDec(getField(), roundingModeDown);
 int roundedDown = dfpDecDown.round(in);
 assertTrue(roundedDown <= 0);

 DfpDec dfpDecHalfUp = new DfpDec(getField(), roundingModeHalfUp);
 int roundedHalfUp = dfpDecHalfUp.round(in);
 assertTrue(roundedHalfUp >= 0);

 DfpDec dfpDecHalfDown = new DfpDec(getField(), roundingModeHalfDown);
 int roundedHalfDown = dfpDecHalfDown.round(in);
 assertTrue(roundedHalfDown < 0);

 DfpDec dfpDecHalfEven = new DfpDec(getField(), roundingModeHalfEven);
 int roundedHalfEven = dfpDecHalfEven.round(in);
 int expectedHalfEven = 123456790; // 5 is rounded up because it's greater than 5
 assertTrue(roundedHalfEven == expectedHalfEven || roundedHalfEven == expectedHalfEven - 1);

 DfpDec dfpDecHalfOdd = new DfpDec(getField(), roundingModeHalfOdd);
 int roundedHalfOdd = dfpDecHalfOdd.round(in);
 int expectedHalfOdd = 123456790; // 5 is rounded down because it's less than 5
 assertTrue(roundedHalfOdd == expectedHalfOdd || roundedHalfOdd == expectedHalfOdd - 1);

 DfpDec dfpDecCeil = new DfpDec(getField(), roundingModeCeil);
 int roundedCeil = dfpDecCeil.round(in);
 assertTrue(roundedCeil > 0);

 DfpDec dfpDecFloor = new DfpDec(getField(), roundingModeFloor);
 int roundedFloor = dfpDecFloor.round(in);
 assertTrue(roundedFloor <= 0);
}

 @Test
 public void testNextAfter() {
 DfpDec dfpDec = new DfpDec();
 Dfp x1 = dfpDec.newInstance(1.23456789);
 Dfp x2 = dfpDec.newInstance(1.2345679);
 Dfp nextAfterX1 = dfpDec.nextAfter(x1);
 assertNotEquals(x1, nextAfterX1);
 assertNotEquals(x2, nextAfterX1);

 Dfp x3 = dfpDec.newInstance(1.23456789);
 Dfp nextAfterX3 = dfpDec.nextAfter(x3);
 assertEquals(x2, nextAfterX3);

 Dfp x4 = dfpDec.newInstance(0);
 Dfp nextAfterX4 = dfpDec.nextAfter(x4);
 assertEquals(x1, nextAfterX4);
}

 @Test
 public void testNextAfterWithNegativeNumber() {
 DfpDec dfpDec = new DfpDec();
 Dfp x1 = dfpDec.newInstance(-1.23456789);
 Dfp x2 = dfpDec.newInstance(-1.2345679);
 Dfp nextAfterX1 = dfpDec.nextAfter(x1);
 assertNotEquals(x1, nextAfterX1);
 assertNotEquals(x2, nextAfterX1);

 Dfp x3 = dfpDec.newInstance(-1.23456789);
 Dfp nextAfterX3 = dfpDec.nextAfter(x3);
 assertEquals(x2, nextAfterX3);

 Dfp x4 = dfpDec.newInstance(0);
 Dfp nextAfterX4 = dfpDec.nextAfter(x4);
 assertEquals(x1, nextAfterX4);
}

 @Test
 public void testNextAfterWithZero() {
 DfpDec dfpDec = new DfpDec();
 Dfp x = dfpDec.newInstance(0);
 Dfp nextAfterX = dfpDec.nextAfter(x);
 assertEquals(x, nextAfterX);
}
}