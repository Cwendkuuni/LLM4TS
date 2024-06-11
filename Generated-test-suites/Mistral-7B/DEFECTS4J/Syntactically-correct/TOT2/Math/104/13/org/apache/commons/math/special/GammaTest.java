import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid input
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-9);
        assertEquals(0.4054651083677812, Gamma.logGamma(1.0), 1e-9);
        assertEquals(1.1630562858588014, Gamma.logGamma(2.0), 1e-9);
        assertEquals(1.3214685510933967, Gamma.logGamma(3.0), 1e-9);

        // Test with edge cases
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(Double.POSITIVE_INFINITY), 0.0);
        assertEquals(-Infinity.doubleValue(), Gamma.logGamma(Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);

        // Test with invalid input
        try {
            Gamma.logGamma(-2.0);
            fail("Expected MathException.");
        } catch (MathException e) {
            // Expected
        }
    }
}