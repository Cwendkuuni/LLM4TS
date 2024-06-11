import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FractionTest {

    @Test
    public void testFractionFromDouble() {
        Fraction fraction = new Fraction(1.0 / 2.0);
        assertEquals(Fraction.ONE_HALF, fraction);
        fraction = new Fraction(-1.0 / 2.0);
        assertEquals(Fraction.MINUS_ONE_HALF, fraction);
        fraction = new Fraction(Integer.MAX_VALUE);
        try {
            new Fraction(fraction.doubleValue());
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
        fraction = new Fraction(Double.NEGATIVE_INFINITY);
        try {
            new Fraction(fraction.doubleValue());
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }
}