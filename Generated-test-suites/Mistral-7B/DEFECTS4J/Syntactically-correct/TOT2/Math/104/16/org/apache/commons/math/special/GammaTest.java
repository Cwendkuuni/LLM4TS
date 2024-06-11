import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid inputs
        assertEquals(0.0, Gamma.logGamma(0.0), 0.0);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0);
        assertEquals(Math.log(0.5), Gamma.logGamma(0.5), 0.01);
        assertEquals(Math.log(1.2), Gamma.logGamma(1.2), 0.01);
        assertEquals(Math.log(2.0), Gamma.logGamma(2.0), 0.01);

        // Test with negative inputs
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);

        // Test with large inputs
        assertEquals(Math.log(4712.0), Gamma.logGamma(4712.0), 0.01);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid inputs
        assertEquals(0.231619583513, Gamma.regularizedGammaP(0.5, 1.0), 0.001);
        assertEquals(0.877572610975, Gamma.regularizedGammaP(2.0, 2.0), 0.001);
        assertEquals(0.999999999999, Gamma.regularizedGammaP(10.0, 10.0), 0.0001);

        // Test with negative input for a
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 1.0), 0.0);

        // Test with large input for x
        assertEquals(0.362686403291, Gamma.regularizedGammaP(1.5, 5.0), 0.001);

        // Test with small input for x
        assertEquals(1.0 - Math.exp(-1.0 + Math.log(1.0) - Gamma.logGamma(1.0)),
                     Gamma.regularizedGammaP(1.0, 1.0), 0.001);

        // Test with very small epsilon
        assertEquals(0.877572610975, Gamma.regularizedGammaP(2.0, 2.0), 1.0e-16);

        // Test with MaxIterationsExceededException
        try {
            Gamma.regularizedGammaP(0.5, 100000.0, 0.0, 1);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid inputs
        assertEquals(0.768421052517, Gamma.regularizedGammaQ(0.5, 1.0), 0.001);
        assertEquals(0.125968225892, Gamma.regularizedGammaQ(2.0, 2.0), 0.001);
        assertEquals(0.0, Gamma.regularizedGammaQ(10.0, 10.0), 0.001);

        // Test with negative input for a
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 1.0), 0.0);

        // Test with large input for x
        assertEquals(1.0 - Gamma.regularizedGammaP(1.5, 5.0),
                     Gamma.regularizedGammaQ(1.5, 5.0), 0.001);

        // Test with small input for x
        assertEquals(1.0 - Gamma.regularizedGammaP(1.0, 1.0),
                     Gamma.regularizedGammaQ(1.0, 1.0), 0.001);

        // Test with very small epsilon
        assertEquals(0.125968225892, Gamma.regularizedGammaQ(2.0, 2.0), 1.0e-16);

        // Test with MaxIterationsExceededException
        try {
            Gamma.regularizedGammaQ(0.5, 100000.0, 0.0, 1);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}