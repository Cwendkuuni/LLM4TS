package org.apache.commons.math3.dfp.test;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 private DfpField field;

 @BeforeClass
 public static void setUpBeforeClass() {
 field = new DfpField(64, 32, 23, 52, 1023, 52); // Radix digits: 64, Significand digits: 32, Exponent digits: 23, Denormalized number bits: 52, Maximum exponent: 1023
 }

 @Before
 public void setUp() {
 this.field.setRoundingMode(DfpField.ROUND_HALF_UP);
 }

 @Test
 public void testConstructorWithDouble() {
 DfpDec d1 = new DfpDec(field, 3.14159265358979323846);
 DfpDec d2 = new DfpDec(field, 3.14159265358979323846);
 assertEquals("Test constructing DfpDec from double", d1, d2);
 }

 @Test
 public void testConstructorWithString() {
 DfpDec d1 = new DfpDec(field, "1.23456E+07");
 DfpDec d2 = new DfpDec(field, "1.23456E+07");
 assertEquals("Test constructing DfpDec from string", d1, d2);
 }

 @Test
 public void testNewInstanceMethod() {
 DfpDec d1 = new DfpDec(field, 123456789012345678901234567890.0);
 DfpDec d2 = d1.newInstance();
 assertEquals("Test newInstance method", d1, d2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithInvalidRadiusDigits() {
 DfpDec d1 = new DfpDec(field, 123456789012345678901234567890.0);
 DfpDec d2 = new DfpDec(new DfpField(32, 32, 23, 52, 1023, 52), d1);
 }

 @Test
 public void testRoundMethod() {
 DfpDec d1 = new DfpDec(field, 123456789012345678901234567890.5);
 int roundingMode = DfpField.ROUND_HALF_UP;
 int rounding = d1.round(roundingMode);
 DfpDec d2 = new DfpDec(field, 123456789012345678901234567891.0);
 DfpDec expected = new DfpDec(field, d1.add(Dfp.ONE));
 assertEquals("Test round method with rounding mode: " + roundingMode, expected, d1.round(roundingMode));
 }

 @Test
 public void testRoundMethodWithInvalidRoundingMode() {
 DfpDec d1 = new DfpDec(field, 123456789012345678901234567890.5);
 int roundingMode = 123;
 int rounding = d1.round(roundingMode);
 fail("Test round method with invalid rounding mode: " + roundingMode);
 }

 @Test
 public void testNextAfterMethod() {
 DfpDec x = new DfpDec(field, 1.0);
 DfpDec y = new DfpDec(field, 0.0);
 DfpDec z = x.nextAfter(y);
 assertFalse("Test nextAfter method: x.nextAfter(y) returns false when x > y", x.compareTo(y) > 0);
 assertNotNull("Test nextAfter method: x.nextAfter(y) returns non-null result", z);
 }

 @Test
 public void testNextAfterMethodWithSelf() {
 DfpDec x = new DfpDec(field, 1.0);
 DfpDec z = x.nextAfter(x);
 assertFalse("Test nextAfter method: x.nextAfter(x) returns false", x.equals(z));
 assertNotNull("Test nextAfter method: x.nextAfter(x) returns non-null result", z);
 }

 @Test
 public void testNextAfterMethodWithNegativeNumber() {
 DfpDec x = new DfpDec(field, -1.0);
 DfpDec z = x.nextAfter(x);
 assertFalse("Test nextAfter method: x.nextAfter(x) returns false", x.equals(z));
 assertNotNull("Test nextAfter method: x.nextAfter(x) returns non-null result", z);
 assertTrue("Test nextAfter method: x.nextAfter(x) has the same sign as x", x.getSign() == z.getSign());
 }

 @Test
 public void testNextAfterMethodWithZero() {
 DfpDec x = new DfpDec(field, 0.0);
 DfpDec y = new DfpDec(field, 0.0);
 DfpDec z = x.nextAfter(y);
 assertFalse("Test nextAfter method: x.nextAfter(y) returns false when x = y", x.equals(y));
 assertNull("Test nextAfter method: x.nextAfter(y) returns null when x = y", z);
 }

 @Test
 public void testNextAfterMethodWithInfinite() {
 DfpDec x = new DfpDec(field, Double.POSITIVE_INFINITY);
 DfpDec y = new DfpDec(field, Double.POSITIVE_INFINITY);
 DfpDec z = x.nextAfter(y);
 assertTrue("Test nextAfter method: x.nextAfter(y) returns true when x = y", x.equals(y));
 assertNull("Test nextAfter method: x.nextAfter(y) returns null when x = y", z);
 }

 @Test
 public void testNextAfterMethodWithNaN() {
 DfpDec x = new DfpDec(field, Double.NaN);
 DfpDec y = new DfpDec(field, Double.NaN);
 DfpDec z = x.nextAfter(y);
 assertTrue("Test nextAfter method: x.nextAfter(y) returns true when x = y", x.equals(y));
 assertNull("Test nextAfter method: x.nextAfter(y) returns null when x = y", z);
 }

 @Test
 public void testNextAfterMethodWithPositiveFiniteNumber() {
 DfpDec x = new DfpDec(field, 1.23456789012345678901234567890);
 DfpDec y = new DfpDec(field, 1.23456789012345678901234567891);
 DfpDec z = x.nextAfter(y);
 assertFalse("Test nextAfter method: x.nextAfter(y) returns false when x < y", x.compareTo(y) < 0);
 assertNotNull("Test nextAfter method: x.nextAfter(y) returns non-null result", z);
 }

 @Test
 public void testNewInstanceMethodWithNaN() {
 DfpDec d1 = new DfpDec(field, Double.NaN);
 DfpDec d2 = d1.newInstance();
 assertEquals("Test newInstance method with NaN", d1, d2);
 }

 @Test
 public void testNewInstanceMethodWithNegativeNumber() {
 DfpDec d1 = new DfpDec(field, -123456789012345678901234567890.0);
 DfpDec d2 = d1.newInstance();
 assertEquals("Test newInstance method with negative number", d1, d2);
 }

 @Test
 public void testNewInstanceMethodWithZero() {
 DfpDec d1 = new DfpDec(field, 0.0);
 DfpDec d2 = d1.newInstance();
 assertEquals("Test newInstance method with zero", d1, d2);
 }

 @Test
 public void testNewInstanceMethodWithInfinite() {
 DfpDec d1 = new DfpDec(field, Double.POSITIVE_INFINITY);
 DfpDec d2 = d1.newInstance();
 assertEquals("Test newInstance method with infinity", d1, d2);
 }

 @After
 public void tearDown() {
 this.field.setRoundingMode(DfpField.ROUND_DOWN);
 }

}