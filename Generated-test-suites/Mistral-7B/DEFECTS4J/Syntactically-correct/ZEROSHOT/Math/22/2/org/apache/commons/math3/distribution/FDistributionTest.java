import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistribution() {
        // Test constructor with valid arguments
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.0);

        // Test constructor with invalid arguments
        try {
            new FDistribution(-1.0, denominatorDegreesOfFreedom);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new FDistribution(numeratorDegreesOfFreedom, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test density
        double x = 2.5;
        double expectedDensity = 0.262992725522701;
        double density = fDist.density(x);
        assertEquals(expectedDensity, density, 1e-7);

        // Test cumulative probability
        double cumulativeProbability = fDist.cumulativeProbability(x);
        double expectedCumulativeProbability = 0.99832219562863;
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-6);

        // Test mean
        double mean = fDist.getNumericalMean();
        double expectedMean = 3.0 / (3.0 - 2.0);
        assertEquals(expectedMean, mean, 1e-6);

        // Test variance
        double variance = fDist.getNumericalVariance();
        double expectedVariance = 0.12889176850892;
        assertEquals(expectedVariance, variance, 1e-6);

        // Test support bounds
        assertEquals(0.0, fDist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0.0);

        // Test support bounds inclusivity
        assertTrue(fDist.isSupportLowerBoundInclusive());
        assertFalse(fDist.isSupportUpperBoundInclusive());

        // Test support connectedness
        assertTrue(fDist.isSupportConnected());

        // Test inverse cumulative probability
        double inverseCumulativeProbability = fDist.inverseCumulativeProbability(0.95);
        double expectedInverseCumulativeProbability = 3.2438073257452;
        assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbability, 1e-6);

        // Test random sampling
        Well19937c rng = new Well19937c();
        RealDistribution fDistSampled = new FDistribution(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        for (int i = 0; i < 10000; i++) {
            double sample = fDistSampled.sample();
            assertTrue(sample >= 0.0);
        }
    }
}