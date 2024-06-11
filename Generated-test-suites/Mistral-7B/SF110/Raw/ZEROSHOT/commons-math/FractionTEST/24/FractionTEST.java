package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.math3.fraction.Fraction;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FractionTest {

 private Fraction fraction1, fraction2, fraction3, result;

 @BeforeAll
 static void initStaticFields() {
 Fraction.ONE = new Fraction(1, 1);
 Fraction.ZERO = new Fraction(0, 1);
 }

 @BeforeEach
 void setUp() {
 fraction1 = new Fraction(1, 2);
 fraction2 = new Fraction(1, 3);
 fraction3 = new Fraction(3, 4);
 }

 @Test
 void testConstructorWithDouble() throws FractionConversionException {
 result = new Fraction(1.5);
 assertEquals(2, result.getNumerator());
 assertEquals(3, result.getDenominator());
 }

 @Test
 void testConstructorWithDoubleAndEpsilon() throws FractionConversionException {
 result = new Fraction(1.5, 0.001, 100);
 assertEquals(2, result.getNumerator());
 assertEquals(3, result.getDenominator());
 }

 @Test
 void testConstructorWithDoubleAndMaxDenominator() throws FractionConversionException {
 result = new Fraction(1.5, 100);
 assertEquals(2, result.getNumerator());
 assertEquals(3, result.getDenominator());
 }

 @Test
 void testConstructorWithInt() {
 result = new Fraction(4);
 assertEquals(4, result.getNumerator());
 assertEquals(1, result.getDenominator());
 }

 @Test
 void testConstructorWithIntAndInt() {
 result = new Fraction(4, 5);
 assertEquals(4, result.getNumerator());
 assertEquals(5, result.getDenominator());
 }

 @Test
 void testGetDenominator() {
 assertEquals(2, fraction1.getDenominator());
 assertEquals(3, fraction2.getDenominator());
 assertEquals(4, fraction3.getDenominator());
 }

 @Test
 void testGetNumerator() {
 assertEquals(1, fraction1.getNumerator());
 assertEquals(1, fraction2.getNumerator());
 assertEquals(3, fraction3.getNumerator());
 }

 @Test
 void testAbs() {
 Fraction absFraction1 = fraction1.abs();
 Fraction absFractionNegativeOne = new Fraction(-1, 1).abs();
 assertEquals(fraction1, absFraction1);
 assertEquals(ONE, absFractionNegativeOne);
 }

 @Test
 void testCompareTo() {
 int comparisonResult12 = fraction1.compareTo(fraction2);
 int comparisonResult13 = fraction1.compareTo(fraction3);
 int comparisonResult21 = fraction2.compareTo(fraction1);
 int comparisonResult31 = fraction3.compareTo(fraction1);

 assertLessThan(0, comparisonResult12);
 assertGreaterThan(0, comparisonResult13);
 assertLessThan(0, comparisonResult21);
 assertGreaterThan(0, comparisonResult31);
 }

 @Test
 void testDoubleValue() {
 assertEquals(0.5, fraction1.doubleValue(), 0.001);
 assertEquals(0.3333333333333333, fraction2.doubleValue(), 0.0000001);
 assertEquals(0.75, fraction3.doubleValue(), 0.001);
 }

 @Test
 void testEquals() {
 Fraction fraction4 = new Fraction(1, 2);
 Fraction fraction5 = new Fraction(1, 2);
 Fraction fraction6 = new Fraction(2, 3);

 assertTrue(fraction1.equals(fraction1));
 assertTrue(fraction1.equals(fraction4));
 assertFalse(fraction1.equals(fraction2));
 assertFalse(fraction1.equals(fraction6));
 }

 @Test
 void testHashCode() {
 int hashCodeFraction1 = fraction1.hashCode();
 int hashCodeFraction2 = fraction2.hashCode();
 assertEquals(hashCodeFraction1, hashCodeFraction1);
 assertNotEquals(hashCodeFraction1, hashCodeFraction2);
 }

 @Test
 void testIntValue() {
 assertEquals(-1, fraction3.intValue());
 }

 @Test
 void testLongValue() {
 assertEquals(-6, fraction3.longValue());
 }

 @Test
 void testNegate() {
 Fraction negateFraction1 = fraction1.negate();
 Fraction negateFractionNegativeOne = new Fraction(-1, 1).negate();
 assertEquals(fraction1, negateFraction1.negate());
 assertEquals(ONE, negateFractionNegativeOne);
 }

 @Test
 void testReciprocal() {
 Fraction reciprocalFraction1 = fraction1.reciprocal();
 Fraction reciprocalFraction1Over2 = new Fraction(2, 1).reciprocal();
 assertEquals(new Fraction(2, 1), reciprocalFraction1);
 assertEquals(fraction1, reciprocalFraction1Over2);
 }

 @Test
 void testAdd() {
 result = fraction1.add(fraction2);
 assertEquals(new Fraction(3, 6), result);
 }

 @Test
 void testAddInt() {
 result = fraction1.add(1);
 assertEquals(new Fraction(2, 2), result);
 }

 @Test
 void testSubtract() {
 result = fraction1.subtract(fraction2);
 assertEquals(new Fraction(1, 6), result);
 }

 @Test
 void testSubtractInt() {
 result = fraction1.subtract(1);
 assertEquals(new Fraction(1, 2), result);
 }

 @Test
 void testAddSub() {
 result = fraction1.addSub(fraction2, true);
 assertEquals(new Fraction(3, 6), result);
 result = fraction1.addSub(fraction2, false);
 assertEquals(new Fraction(-1, 6), result);
 }

 @Test
 void testMultiply() {
 result = fraction1.multiply(fraction2);
 assertEquals(new Fraction(1, 6), result);
 }

 @Test
 void testMultiplyInt() {
 result = fraction1.multiply(2);
 assertEquals(new Fraction(2, 1), result);
 }

 @Test
 void testDivide() {
 result = fraction3.divide(fraction2);
 assertEquals(new Fraction(3, 2), result);
 }

 @Test
 void testDivideInt() {
 result = fraction3.divide(2);
 assertEquals(new Fraction(3, 2), result);
 }

 @Test
 void testPercentageValue() {
 assertEquals(50.0, fraction1.percentageValue(), 0.01);
 }

 @Test
 void testGetField() {
 FractionField field = Fraction.getField();
 assertEquals(FractionField.getInstance(), field);
 }
}