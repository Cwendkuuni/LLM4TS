package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.math3.fraction.*;

@SuppressWarnings("static-access")
public class FractionTest {

 @Test
 public void testFractionConstructorWithDouble() {
 Fraction fraction = new Fraction(1.5);
 assertEquals(1, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testFractionConstructorWithDoubleAndEpsilon() {
 Fraction fraction = new Fraction(1.5, 0.001, 100);
 assertEquals(1, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testFractionConstructorWithDoubleAndMaxDenominator() {
 Fraction fraction = new Fraction(1.5, 10);
 assertEquals(3, fraction.getNumerator());
 assertEquals(5, fraction.getDenominator());
 }

 @Test
 public void testFractionConstructorWithInt() {
 Fraction fraction = new Fraction(3);
 assertEquals(3, fraction.getNumerator());
 assertEquals(1, fraction.getDenominator());
 }

 @Test
 public void testFractionConstructorWithIntAndInt() {
 Fraction fraction = new Fraction(3, 4);
 assertEquals(3, fraction.getNumerator());
 assertEquals(4, fraction.getDenominator());
 }

 @Test
 public void testFractionAbs() {
 Fraction fraction = new Fraction(-3, 4);
 Fraction expected = new Fraction(3, 4);
 assertEquals(expected, fraction.abs());
 }

 @Test
 public void testFractionCompareTo() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(2, 3);
 int comparisonResult = fraction1.compareTo(fraction2);
 assertEquals(-1, comparisonResult);
 }

 @Test
 public void testFractionDoubleValue() {
 Fraction fraction = new Fraction(2, 3);
 double expected = 0.6666666666666666;
 assertEquals(expected, fraction.doubleValue());
 }

 @Test
 public void testFractionEquals() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 2);
 Fraction fraction3 = new Fraction(1, 3);
 assertTrue(fraction1.equals(fraction1));
 assertTrue(fraction1.equals(fraction2));
 assertFalse(fraction1.equals(fraction3));
 }

 @Test
 public void testFractionFloatValue() {
 Fraction fraction = new Fraction(2, 3);
 float expected = 0.666666F;
 assertEquals(expected, fraction.floatValue());
 }

 @Test
 public void testFractionGetDenominator() {
 Fraction fraction = new Fraction(3, 4);
 assertEquals(4, fraction.getDenominator());
 }

 @Test
 public void testFractionGetNumerator() {
 Fraction fraction = new Fraction(3, 4);
 assertEquals(3, fraction.getNumerator());
 }

 @Test
 public void testFractionHashCode() {
 Fraction fraction = new Fraction(3, 4);
 int expectedHashCode = 1817325323;
 assertEquals(expectedHashCode, fraction.hashCode());
 }

 @Test
 public void testFractionIntValue() {
 Fraction fraction = new Fraction(2, 3);
 int expected = 2;
 assertEquals(expected, fraction.intValue());
 }

 @Test
 public void testFractionLongValue() {
 Fraction fraction = new Fraction(2, 3);
 long expected = 2;
 assertEquals(expected, fraction.longValue());
 }

 @Test
 public void testFractionNegate() {
 Fraction fraction = new Fraction(-3, 4);
 Fraction expected = new Fraction(3, 4);
 assertEquals(expected, fraction.negate());
 }

 @Test
 public void testFractionReciprocal() {
 Fraction fraction = new Fraction(3, 4);
 Fraction expected = new Fraction(2, 3);
 assertEquals(expected, fraction.reciprocal());
 }

 @Test
 public void testFractionAdd() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction expected = new Fraction(3, 6);
 Fraction result = fraction1.add(fraction2);
 assertEquals(expected, result);
 }

 @Test
 public void testFractionAddInt() {
 Fraction fraction = new Fraction(2, 3);
 Fraction expected = new Fraction(8, 3);
 Fraction result = fraction.add(2);
 assertEquals(expected, result);
 }

 @Test
 public void testFractionSubtract() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction expected = new Fraction(2, 6);
 Fraction result = fraction1.subtract(fraction2);
 assertEquals(expected, result);
 }

 @Test
 public void testFractionSubtractInt() {
 Fraction fraction = new Fraction(2, 3);
 Fraction expected = new Fraction(4, 3);
 Fraction result = fraction.subtract(1);
 assertEquals(expected, result);
 }

 @Test
 public void testFractionAddSub() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction expectedAdd = new Fraction(3, 6);
 Fraction expectedSub = new Fraction(-2, 6);
 Fraction resultAdd = fraction1.addSub(fraction2, true);
 Fraction resultSub = fraction1.addSub(fraction2, false);
 assertEquals(expectedAdd, resultAdd);
 assertEquals(expectedSub, resultSub);
 }

 @Test
 public void testFractionMultiply() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction expected = new Fraction(1, 6);
 Fraction result = fraction1.multiply(fraction2);
 assertEquals(expected, result);
 }

 @Test
 public void testFractionMultiplyInt() {
 Fraction fraction = new Fraction(2, 3);
 Fraction expected = new Fraction(6, 3);
 Fraction result = fraction.multiply(2);
 assertEquals(expected, result);
 }

 @Test
 public void testFractionDivide() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction expected = new Fraction(3, 2);
 Fraction result = fraction1.divide(fraction2);
 assertEquals(expected, result);
 }

 @Test
 public void testFractionDivideInt() {
 Fraction fraction = new Fraction(2, 3);
 Fraction expected = new Fraction(2, 1);
 Fraction result = fraction.divide(1);
 assertEquals(expected, result);
 }

 @Test
 public void testFractionPercentageValue() {
 Fraction fraction = new Fraction(3, 4);
 double expected = 75.0;
 assertEquals(expected, fraction.percentageValue());
 }

 @Test
 public void testFractionGetReducedFraction() {
 int numerator = 13;
 int denominator = 24;
 Fraction expected = new Fraction(1, 2);
 Fraction result = Fraction.getReducedFraction(numerator, denominator);
 assertEquals(expected, result);
 }

 @Test
 public void testFractionToString() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1/2";
 assertEquals(expected, fraction.toString());
 }
}