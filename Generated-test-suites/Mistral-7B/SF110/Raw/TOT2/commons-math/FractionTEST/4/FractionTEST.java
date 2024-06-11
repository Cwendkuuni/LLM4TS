import org.junit.Test;
import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @Test
    public void testFractionFromDouble() {
        Fraction fraction = new Fraction(1.5);
        int expectedNumerator = 3;
        int expectedDenominator = 2;
        assertNotNull(fraction);
        assertEquals(expectedNumerator, fraction.getNumerator());
        assertEquals(expectedDenominator, fraction.getDenominator());
    }
}