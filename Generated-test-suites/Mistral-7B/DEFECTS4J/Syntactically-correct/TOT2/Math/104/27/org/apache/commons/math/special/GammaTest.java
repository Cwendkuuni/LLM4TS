import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Positive values
        double result = Gamma.logGamma(1.0);
        double expected = Math.log(1.0) + Math.E; // Approximate value of gamma(1)
        assertEquals(expected, result, 1e-6);

        result = Gamma.logGamma(2.0);
        expected = Math.log(Math.sqrt(Math.PI)) + (1.0 / 2.0) * Math.log(2.0);
        assertEquals(expected, result, 1e-6);

        // Negative values and NaN
        result = Gamma.logGamma(-1.0);
        assertEquals(Double.NaN, result);

        result = Gamma.logGamma(0.0);
        assertEquals(Double.NaN, result);

        // Special values
        result = Gamma.logGamma(0.5);
        expected = Math.log(Math.sqrt(Math.PI)) / 2.0;
        assertEquals(expected, result, 1e-6);

        result = Gamma.logGamma(1.0 / 2.0);
        expected = -Math.log(Math.sqrt(Math.PI)) / 2.0;
        assertEquals(expected, result, 1e-6);
    }
}