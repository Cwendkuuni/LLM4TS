package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.math3.fraction.Fraction;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FractionTest {

 private Fraction fraction;

 @BeforeEach
 public void setup() {
 fraction = new Fraction(1, 2);
 }

 @Test
 public void testConstructorWithDouble() {
 Fraction fraction = new Fraction(1.5);
 assertEquals(new Fraction(3, 2), fraction);
 }

 @Test
 public void testConstructorWithDouble_epsilon() {
 Fraction fraction = new Fraction(1.5, 0.01, 100);
 assertEquals(new Fraction(3, 2), fraction);
 }

 @Test
 public void testConstructorWithDouble_maxDenominator() {
 Fraction fraction = new Fraction(1.5, 10);
 assertEquals(new Fraction(3, 1), fraction);
 }

 @Test
 public void testConstructorWithInt() {
 Fraction fraction = new Fraction(2);
 assertEquals(new Fraction(2, 1), fraction);
 }

 @Test
 public void testConstructorWithIntAndInt() {
 Fraction fraction = new Fraction(2, 3);
 assertEquals(new Fraction(2, 3), fraction);
 }

 @Test
 public void testGetDenominator() {
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testGetNumerator() {
 assertEquals(1, fraction.getNumerator());
 }

 @Test
 public void testAbs() {
 Fraction abs = fraction.abs();
 assertEquals(new Fraction(1, 2), abs);
 }

 @Test
 public void testCompareTo() {
 Fraction anotherFraction = new Fraction(3, 4);
 int comparisonResult = fraction.compareTo(anotherFraction);
 assertEquals(-1, comparisonResult);
 }

 @Test
 public void testDoubleValue() {
 double value = fraction.doubleValue();
 assertEquals(0.5, value);
 }

 @Test
 public void testEquals() {
 Fraction anotherFraction = new Fraction(1, 2);
 assertTrue(fraction.equals(anotherFraction));
 }

 @Test
 public void testFloatValue() {
 float value = fraction.floatValue();
 assertEquals(0.5F, value);
 }

 @Test
 public void testHashCode() {
 int hashCode = fraction.hashCode();
 assertEquals(73, hashCode);
 }

 @Test
 public void testIntValue() {
 int value = fraction.intValue();
 assertEquals(0, value);
 }

 @Test
 public void testLongValue() {
 long value = fraction.longValue();
 assertEquals(0L, value);
 }

 @Test
 public void testNegate() {
 Fraction negatedFraction = fraction.negate();
 assertEquals(new Fraction(-1, 2), negatedFraction);
 }

 @Test
 public void testReciprocal() {
 Fraction reciprocal = fraction.reciprocal();
 assertEquals(new Fraction(2, 1), reciprocal);
 }

 @Test
 public void testAdd() {
 Fraction sum = fraction.add(new Fraction(1, 3));
 assertEquals(new Fraction(5, 6), sum);
 }

 @Test
 public void testAddInt() {
 Fraction sum = fraction.add(2);
 assertEquals(new Fraction(3, 2), sum);
 }

 @Test
 public void testSubtract() {
 Fraction difference = fraction.subtract(new Fraction(1, 3));
 assertEquals(new Fraction(1, 6), difference);
 }

 @Test
 public void testSubtractInt() {
 Fraction difference = fraction.subtract(2);
 assertEquals(new Fraction(-1, 2), difference);
 }

 @Test
 public void testAddSub() {
 Fraction sumOrDifference = fraction.addSub(new Fraction(1, 3), true);
 assertEquals(new Fraction(5, 6), sumOrDifference);
 Fraction sumOrDifferenceNegative = fraction.addSub(new Fraction(1, 3), false);
 assertEquals(new Fraction(1, 6), sumOrDifferenceNegative);
 }

 @Test
 public void testMultiply() {
 Fraction product = fraction.multiply(new Fraction(2, 3));
 assertEquals(new Fraction(1, 3), product);
 }

 @Test
 public void testMultiplyInt() {
 Fraction product = fraction.multiply(2);
 assertEquals(new Fraction(2, 1), product);
 }

 @Test
 public void testDivide() {
 Fraction quotient = fraction.divide(new Fraction(2, 3));
 assertEquals(new Fraction(3, 2), quotient);
 }

 @Test
 public void testDivideInt() {
 Fraction quotient = fraction.divide(2);
 assertEquals(new Fraction(1, 2), quotient);
 }

 @Test
 public void testPercentageValue() {
 double percentageValue = fraction.percentageValue();
 assertEquals(50.0, percentageValue);
 }

 @Test
 public void testToString() {
 String stringRepresentation = fraction.toString();
 assertEquals("1/2", stringRepresentation);
 }

 @Test
 public void testGetField() {
 FractionField field = fraction.getField();
 assertNotNull(field);
 }

}