package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(new Fraction(3, 2), fraction);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleThrowsException() {
        new Fraction(-1.5);
    }
}