import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() throws Exception {
        Fraction fraction = new Fraction(0.5);
        assertEquals("Check the numerator and denominator after constructor call", 1, fraction.getNumerator());
        assertEquals("Check the denominator after constructor call", 2, fraction.getDenominator());
    }

    @Test
    public void testConstructorWithInt() throws Exception {
        Fraction fraction = new Fraction(1);
        assertEquals("Check the numerator and denominator after constructor call", 1, fraction.getNumerator());
        assertEquals("Check the denominator after constructor call", 1, fraction.getDenominator());
    }

    @Test
    public void testConstructorWithIntAndInt() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("Check the numerator and denominator after constructor call", 1, fraction.getNumerator());
        assertEquals("Check the denominator after constructor call", 2, fraction.getDenominator());
    }

    @Test
    public void testAbsMethod() throws Exception {
        Fraction fraction = new Fraction(-1, 1);
        Fraction result = fraction.abs();
        assertEquals("Check the numerator after abs method call", 1, result.getNumerator());
        assertEquals("Check the denominator after abs method call", 1, result.getDenominator());
    }

    @Test
    public void testCompareToMethod() throws Exception {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(1, 2);
        int result = fraction1.compareTo(fraction2);
        assertEquals("Check the comparison result", -1, result);
    }

    @Test
    public void testDoubleValueMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        double result = fraction.doubleValue();
        assertEquals("Check the result after doubleValue method call", 0.5, result, 0.001);
    }

    @Test
    public void testEqualsMethod() throws Exception {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(1, 1);
        assertTrue("Check if two equal fractions are equal", fraction1.equals(fraction2));
    }

    @Test
    public void testGetDenominatorMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        int result = fraction.getDenominator();
        assertEquals("Check the denominator after getDenominator method call", 2, result);
    }

    @Test
    public void testGetNumeratorMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        int result = fraction.getNumerator();
        assertEquals("Check the numerator after getNumerator method call", 1, result);
    }

    @Test
    public void testHashCodeMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        int result = fraction.hashCode();
        int expectedHashCode = 37 * (629 + 1) + 2;
        assertEquals("Check the hashcode value", expectedHashCode, result);
    }

    @Test
    public void testIntValueMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        int result = fraction.intValue();
        assertEquals("Check the result after intValue method call", 0, result);
    }

    @Test
    public void testLongValueMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        long result = fraction.longValue();
        assertEquals("Check the result after longValue method call", 0L, result);
    }

    @Test
    public void testNegateMethod() throws Exception {
        Fraction fraction = new Fraction(1, 1);
        Fraction result = fraction.negate();
        assertEquals("Check the numerator after negate method call", -1, result.getNumerator());
        assertEquals("Check the denominator after negate method call", 1, result.getDenominator());
    }

    @Test
    public void testReciprocalMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.reciprocal();
        assertEquals("Check the numerator after reciprocal method call", 2, result.getNumerator());
        assertEquals("Check the denominator after reciprocal method call", 1, result.getDenominator());
    }

    @Test
    public void testToStringMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        String result = fraction.toString();
        assertEquals("Check the result after toString method call", "1/2", result);
    }

    @Test
    public void testGetFieldMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        FractionField expectedField = FractionField.getInstance();
        FractionField result = fraction.getField();
        assertEquals("Check the field object after getField method call", expectedField, result);
    }

    @Test
    public void testAddMethod() throws Exception {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.add(fraction2);
        assertEquals("Check the numerator after add method call", 2, result.getNumerator());
        assertEquals("Check the denominator after add method call", 2, result.getDenominator());
    }

    @Test
    public void testAddIntMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(1);
        assertEquals("Check the numerator after addInt method call", 3, result.getNumerator());
        assertEquals("Check the denominator after addInt method call", 2, result.getDenominator());
    }

    @Test
    public void testSubtractMethod() throws Exception {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals("Check the numerator after subtract method call", 0, result.getNumerator());
        assertEquals("Check the denominator after subtract method call", 2, result.getDenominator());
    }

    @Test
    public void testSubtractIntMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.subtract(1);
        assertEquals("Check the numerator after subtractInt method call", -1, result.getNumerator());
        assertEquals("Check the denominator after subtractInt method call", 2, result.getDenominator());
    }

    @Test
    public void testMultipleMethods() throws Exception {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals("Check the numerator after multipleMethods call", 3, result.getNumerator());
        assertEquals("Check the denominator after multipleMethods call", 6, result.getDenominator());
    }

    @Test
    public void testMultipleIntMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(2);
        assertEquals("Check the numerator after multipleIntMethod call", 2, result.getNumerator());
        assertEquals("Check the denominator after multipleIntMethod call", 2, result.getDenominator());
    }

    @Test
    public void testDivideMethod() throws Exception {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals("Check the numerator after divide method call", 3, result.getNumerator());
        assertEquals("Check the denominator after divide method call", 2, result.getDenominator());
    }

    @Test
    public void testDivideIntMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.divide(2);
        assertEquals("Check the numerator after divideIntMethod call", -1, result.getNumerator());
        assertEquals("Check the denominator after divideIntMethod call", 1, result.getDenominator());
    }

    @Test
    public void testPercentageValueMethod() throws Exception {
        Fraction fraction = new Fraction(1, 2);
        double result = fraction.percentageValue();
        assertEquals("Check the percentage value", 50.0, result, 0.001);
    }

    @Test
    public void testGetReducedFractionMethod() throws Exception {
        Fraction fraction = new Fraction(3, 8);
        Fraction result = Fraction.getReducedFraction(3, 8);
        assertEquals("Check the numerator after getReducedFraction method call", 1, result.getNumerator());
        assertEquals("Check the denominator after getReducedFraction method call", 4, result.getDenominator());
    }

}