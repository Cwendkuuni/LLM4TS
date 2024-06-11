package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.math3.fraction.Fraction;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class FractionTest {

 @Test
 public void testConstructorWithDouble() {
 Fraction fraction = new Fraction(2.5);
 assertEquals(5, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testConstructorWithDoubleAndEpsilon() {
 Fraction fraction = new Fraction(2.5, 0.001, 100);
 assertEquals(5, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testConstructorWithDoubleAndMaxDenominator() {
 Fraction fraction = new Fraction(2.5, 100);
 assertEquals(13, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testConstructorWithInt() {
 Fraction fraction = new Fraction(4);
 assertEquals(4, fraction.getNumerator());
 assertEquals(1, fraction.getDenominator());
 }

 @Test
 public void testConstructorWithIntAndInt() {
 Fraction fraction = new Fraction(4, 2);
 assertEquals(2, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testAbs() {
 Fraction fraction = new Fraction(-3, 2);
 Fraction result = fraction.abs();
 assertEquals(3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testCompareTo() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 2);
 int result = fraction1.compareTo(fraction2);
 assertEquals(-1, result);
 }

 @Test
 public void testDoubleValue() {
 Fraction fraction = new Fraction(3, 2);
 double result = fraction.doubleValue();
 assertEquals(1.5, result);
 }

 @Test
 public void testEquals() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 2);
 boolean result = fraction1.equals(fraction2);
 assertTrue(result);
 }

 @Test
 public void testFloatValue() {
 Fraction fraction = new Fraction(3, 2);
 float result = fraction.floatValue();
 assertEquals(1.5f, result, 0.001f);
 }

 @Test
 public void testGetDenominator() {
 Fraction fraction = new Fraction(3, 2);
 int result = fraction.getDenominator();
 assertEquals(2, result);
 }

 @Test
 public void testGetNumerator() {
 Fraction fraction = new Fraction(3, 2);
 int result = fraction.getNumerator();
 assertEquals(3, result);
 }

 @Test
 public void testHashCode() {
 Fraction fraction = new Fraction(1, 2);
 int result = fraction.hashCode();
 assertEquals(37 * (629 + 1), result);
 }

 @Test
 public void testIntValue() {
 Fraction fraction = new Fraction(3, 2);
 int result = fraction.intValue();
 assertEquals(1, result);
 }

 @Test
 public void testLongValue() {
 Fraction fraction = new Fraction(3, 2);
 long result = fraction.longValue();
 assertEquals(1L, result);
 }

 @Test
 public void testNegate() {
 Fraction fraction = new Fraction(3, 2);
 Fraction result = fraction.negate();
 assertEquals(-3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testReciprocal() {
 Fraction fraction = new Fraction(3, 2);
 Fraction result = fraction.reciprocal();
 assertEquals(2, result.getNumerator());
 assertEquals(3, result.getDenominator());
 }

 @Test
 public void testAdd() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction result = fraction1.add(fraction2);
 assertEquals(7, result.getNumerator());
 assertEquals(4, result.getDenominator());
 }

 @Test
 public void testAddInt() {
 Fraction fraction = new Fraction(3, 2);
 Fraction result = fraction.add(1);
 assertEquals(7, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testSubtract() {
 Fraction fraction1 = new Fraction(3, 2);
 Fraction fraction2 = new Fraction(1, 2);
 Fraction result = fraction1.subtract(fraction2);
 assertEquals(2, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testSubtractInt() {
 Fraction fraction = new Fraction(3, 2);
 Fraction result = fraction.subtract(1);
 assertEquals(2, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testAddSub() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction result = fraction1.addSub(fraction2, true);
 assertEquals(7, result.getNumerator());
 assertEquals(4, result.getDenominator());

 Fraction result2 = fraction1.addSub(fraction2, false);
 assertEquals(-2, result2.getNumerator());
 assertEquals(4, result2.getDenominator());
 }

 @Test
 public void testMultiply() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 2);
 Fraction result = fraction1.multiply(fraction2);
 assertEquals(3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testMultiplyInt() {
 Fraction fraction = new Fraction(3, 2);
 Fraction result = fraction.multiply(2);
 assertEquals(6, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testDivide() {
 Fraction fraction1 = new Fraction(3, 2);
 Fraction fraction2 = new Fraction(2, 3);
 Fraction result = fraction1.divide(fraction2);
 assertEquals(7, result.getNumerator());
 assertEquals(3, result.getDenominator());
 }

 @Test
 public void testDivideInt() {
 Fraction fraction = new Fraction(3, 2);
 Fraction result = fraction.divide(2);
 assertEquals(3, result.getNumerator());
 assertEquals(1, result.getDenominator());
 }

 @Test
 public void testPercentageValue() {
 Fraction fraction = new Fraction(3, 4);
 double result = fraction.percentageValue();
 assertEquals(75.0, result, 0.01);
 }

 @Test
 public void testGetField() {
 Fraction fraction = new Fraction(3, 2);
 FractionField expected = FractionField.getInstance();
 FractionField result = fraction.getField();
 assertEquals(expected, result);
 }
}