package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    @Test
    public void testFractionConstructorWithDouble() {
        Fraction fraction = new Fraction(3.0);
        assertEquals(3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }
}