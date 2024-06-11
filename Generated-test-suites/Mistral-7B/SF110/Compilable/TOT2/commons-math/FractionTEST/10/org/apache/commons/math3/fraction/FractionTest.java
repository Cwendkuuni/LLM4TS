package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @Test
    public void testFractionConstructorDouble() {
        Fraction fraction = new Fraction(3.14159265358979323846);
        assertEquals(3, fraction.getNumerator());
        assertEquals(7, fraction.getDenominator());
    }
}