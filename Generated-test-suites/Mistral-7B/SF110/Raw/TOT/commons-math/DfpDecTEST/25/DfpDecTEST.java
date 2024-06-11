package org.apache.commons.math3.dfp;

import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 @Test
 public void testConstructionFromFactory() {
 DfpField factory = new DfpField(48, 53); // radix digits, fraction digits
 DfpDec d = new DfpDec(factory);
 assertEquals(d.getClass(), DfpDec.class);
 assertEquals(d.getField(), factory);
 }

 @Test
 public void testConstructionFromFactoryWithValue() {
 DfpField factory = new DfpField(48, 53);
 DfpDec d = new DfpDec(factory, 1234.5678);
 assertEquals(d.getClass(), DfpDec.class);
 assertEquals(d.getField(), factory);
 assertEquals(d.toString(), "1234.5678", 7); // 7 decimal digits of precision
 }

 @Test
 public void testConstructionFromDfp() {
 DfpDec d1 = new DfpDec(new DfpDec(new DfpField(48, 53), 1234.5678));
 DfpDec d2 = new DfpDec(d1);
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceMethods() {
 DfpDec d = new DfpDec(new DfpField(48, 53), 1234.5678);
 DfpDec d1 = d.newInstance();
 DfpDec d2 = d.newInstance(1);
 DfpDec d3 = d.newInstance(1234567890L);
 DfpDec d4 = d.newInstance(0.123456789);
 DfpDec d5 = d.newInstance(new DfpDec(new DfpField(48, 53), 56.78));
 DfpDec d6 = d.newInstance(new DfpDec(new DfpField(48, 53), 1, 2));
 DfpDec d7 = d.newInstance(new DfpDec(new DfpField(48, 53), -1, 3));
 DfpDec d8 = d.newInstance(new DfpDec(new DfpField(48, 53), "0.123456789012345678901234567890"));
 DfpDec d9 = d.newInstance(new DfpDec(new DfpField(48, 53), 1, 2, 3));

 assertEquals(d1.getClass(), DfpDec.class);
 assertEquals(d1.getField(), d.getField());
 assertEquals(d1, d);

 assertEquals(d2.toString(), "1", 1);
 assertEquals(d2.getClass(), DfpDec.class);
 assertEquals(d2.getField(), d.getField());

 assertEquals(d3.toString(), "1234567890", 11);
 assertEquals(d3.getClass(), DfpDec.class);
 assertEquals(d3.getField(), d.getField());

 assertEquals(d4.toString(), "0.1234567890123456789", 13);
 assertEquals(d4.getClass(), DfpDec.class);
 assertEquals(d4.getField(), d.getField());

 assertEquals(d5.toString(), "56.78", 5);
 assertEquals(d5.getClass(), DfpDec.class);
 assertEquals(d5.getField(), d.getField());

 assertEquals(d6.toString(), "1", 1);
 assertEquals(d6.getClass(), DfpDec.class);
 assertEquals(d6.getField(), d.getField());

 assertEquals(d7.toString(), "-0", 1);
 assertEquals(d7.getClass(), DfpDec.class);
 assertEquals(d7.getField(), d.getField());

 assertEquals(d8.toString(), "0.123456789012345678901234567890", 17);
 assertEquals(d8.getClass(), DfpDec.class);
 assertEquals(d8.getField(), d.getField());

 assertEquals(d9.toString(), "1.0001", 4);
 assertEquals(d9.getClass(), DfpDec.class);
 assertEquals(d9.getField(), d.getField());
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec d = new DfpDec(new DfpField(48, 53), 1234.5678);
 assertEquals(d.getDecimalDigits(), 2096);
 }

 @Test
 public void testRounding() {
 DfpDec d = new DfpDec(new DfpField(48, 53), 1234.5678);
 int roundingModeDown = DfpField.ROUND_DOWN;
 int roundingModeUp = DfpField.ROUND_UP;
 int roundingModeHalfUp = DfpField.ROUND_HALF_UP;
 int roundingModeHalfEven = DfpField.ROUND_HALF_EVEN;
 int roundingModeHalfOdd = DfpField.ROUND_HALF_ODD;
 int roundingModeCeil = DfpField.ROUND_CEIL;
 int roundingModeFloor = DfpField.ROUND_FLOOR;

 DfpDec dRoundDown = d.newInstance();
 dRoundDown.round(roundingModeDown);
 assertEquals(dRoundDown.toString(), "1234.5678", 7);

 DfpDec dRoundUp = d.newInstance();
 dRoundUp.round(roundingModeUp);
 assertEquals(dRoundUp.toString(), "1235.0", 1);

 DfpDec dRoundHalfUp = d.newInstance();
 dRoundHalfUp.round(roundingModeHalfUp);
 assertEquals(dRoundHalfUp.toString(), "1234.568", 1);

 DfpDec dRoundHalfEven = d.newInstance();
 dRoundHalfEven.round(roundingModeHalfEven);
 assertEquals(dRoundHalfEven.toString(), "1234.5678", 7);

 DfpDec dRoundHalfOdd = d.newInstance();
 dRoundHalfOdd.round(roundingModeHalfOdd);
 assertEquals(dRoundHalfOdd.toString(), "1234.568", 1);

 DfpDec dRoundCeil = d.newInstance();
 dRoundCeil.round(roundingModeCeil);
 assertEquals(dRoundCeil.toString(), "1235.0", 1);

 DfpDec dRoundFloor = d.newInstance();
 dRoundFloor.round(roundingModeFloor);
 assertEquals(dRoundFloor.toString(), "1234.5678", 7);
 }

 @Test
 public void testNextAfter() {
 DfpDec d1 = new DfpDec(new DfpField(48, 53), 1234.5678);
 DfpDec d2 = new DfpDec(new DfpField(48, 53), 1234.5679);
 DfpDec d3 = new DfpDec(new DfpField(48, 53), -1234.5678);
 DfpDec d4 = new DfpDec(new DfpField(48, 53), -1234.5679);
 DfpDec d5 = new DfpDec(new DfpField(48, 53), 0);

 assertEquals(d1.nextAfter(d2), d3);
 assertEquals(d2.nextAfter(d1), d3);

 assertEquals(d1.nextAfter(d5), d1);
 assertEquals(d5.nextAfter(d1), d1);

 assertEquals(d2.nextAfter(d5), d2);
 assertEquals(d5.nextAfter(d2), d2);

 assertEquals(d3.nextAfter(d4), d1);
 assertEquals(d4.nextAfter(d3), d1);
 }

 @Test
 public void testNegativeValues() {
 DfpDec d1 = new DfpDec(new DfpField(48, 53), 1234.5678);
 DfpDec d2 = new DfpDec(new DfpField(48, 53), -1234.5678);
 DfpDec d3 = new DfpDec(new DfpField(48, 53), -0.0);
 DfpDec d4 = new DfpDec(new DfpField(48, 53), 0.0);

 assertEquals(d1.negate(), d2);
 assertEquals(d2.negate(), d1);

 assertEquals(d3.negate(), d4);
 assertEquals(d4.negate(), d3);

 assertEquals(d1.abs(), d1);
 assertEquals(d1.abs(), d1);

 assertEquals(d2.abs(), d1);
 assertEquals(d2.abs(), d1);

 assertEquals(d3.abs(), d4);
 assertEquals(d3.abs(), d4);

 assertEquals(d4.abs(), d4);
 assertEquals(d4.abs(), d4);
 }

 @Test
 public void testNaNValues() {
 DfpDec d1 = new DfpDec(new DfpField(48, 53), Double.NaN);
 DfpDec d2 = new DfpDec(new DfpField(48, 53), Double.NEGATIVE_INFINITY);
 DfpDec d3 = new DfpDec(new DfpField(48, 53), Double.POSITIVE_INFINITY);

 assertEquals(d1.isNaN(), true);
 assertEquals(d1.isInfinite(), false);
 assertEquals(d1.isNegative(), false);
 assertEquals(d1.compareTo(d1), 0);
 assertEquals(d1.compareTo(d2), 1);
 assertEquals(d1.compareTo(d3), 1);

 assertEquals(d2.isNaN(), false);
 assertEquals(d2.isInfinite(), true);
 assertEquals(d2.isNegative(), true);
 assertEquals(d2.compareTo(d1), -1);
 assertEquals(d2.compareTo(d3), 1);

 assertEquals(d3.isNaN(), false);
 assertEquals(d3.isInfinite(), true);
 assertEquals(d3.isNegative(), false);
 assertEquals(d3.compareTo(d1), 1);
 assertEquals(d3.compareTo(d2), -1);
 }

 @Test
 public void testArithmeticOperations() {
 DfpDec d1 = new DfpDec(new DfpField(48, 53), 1234.5678);
 DfpDec d2 = new DfpDec(new DfpField(48, 53), 56.78);
 DfpDec d3 = new DfpDec(new DfpField(48, 53), -1234.5678);
 DfpDec d4 = new DfpDec(new DfpField(48, 53), -56.78);
 DfpDec d5 = new DfpDec(new DfpField(48, 53), 0);
 DfpDec d6 = new DfpDec(new DfpField(48, 53), 1.0);

 assertEquals(d1.add(d2), new DfpDec(new DfpField(48, 53), 1241.3456, 7));
 assertEquals(d1.subtract(d2), new DfpDec(new DfpField(48, 53), 1182.1822, 7));
 assertEquals(d1.multiply(d2), new DfpDec(new DfpField(48, 53), 726338.083152, 13));
 assertEquals(d1.divide(d2), new DfpDec(new DfpField(48, 53), 21.421873435448, 12));
 assertEquals(d1.divide(d5), d1);
 assertEquals(d1.divide(d6), new DfpDec(new DfpField(48, 53), 1234.5678, 7));
 assertEquals(d1.multiply(d5), new DfpDec(new DfpField(48, 53), 0));

 assertEquals(d1.add(d3), new DfpDec(new DfpField(48, 53), 2469.1430, 7));
 assertEquals(d1.subtract(d3), new DfpDec(new DfpField(48, 53), 2568.1100, 7));
 assertEquals(d1.multiply(d4), new DfpDec(new DfpField(48, 53), -323211.628752, 13));
 assertEquals(d1.divide(d4), new DfpDec(new DfpField(48, 53), -0.0037745233216313, 16));
 assertEquals(d1.divide(d1), new DfpDec(new DfpField(48, 53), 1.0, 1));
 }
}