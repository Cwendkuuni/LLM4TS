package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() {
        Fraction fraction = new Fraction(2.5);
        assertEquals(2, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }
}