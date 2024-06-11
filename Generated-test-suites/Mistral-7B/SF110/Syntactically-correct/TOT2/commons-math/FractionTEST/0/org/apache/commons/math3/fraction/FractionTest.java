import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    @Test
    public void testFractionConstructionWithDoubleValue() {
        Fraction fraction = new Fraction(2.5);
        int expectedNumerator = 5;
        int expectedDenominator = 2;
        assertEquals(expectedNumerator, fraction.getNumerator());
        assertEquals(expectedDenominator, fraction.getDenominator());
    }
}