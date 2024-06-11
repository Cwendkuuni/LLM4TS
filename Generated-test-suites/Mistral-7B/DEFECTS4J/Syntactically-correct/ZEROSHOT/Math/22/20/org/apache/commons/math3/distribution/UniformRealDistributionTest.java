import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistribution() throws NumberIsTooLargeException {
        // Test constructor with default parameters
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(1.0e-9, dist.getSolverAbsoluteAccuracy(), 1.0e-11);

        // Test constructor with custom parameters
        UniformRealDistribution distCustom = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.5, distCustom.getLowerBound(), 0.0);
        assertEquals(1.5, distCustom.getUpperBound(), 0.0);
        assertEquals(1.0e-9, distCustom.getSolverAbsoluteAccuracy(), 1.0e-11);

        // Test density method
        UniformRealDistribution distDensity = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distDensity.density(2.0), 1.0e-10);
        assertEquals(1.0 / 6.0, distDensity.density(1.5), 1.0e-10);
        assertEquals(1.0 / 6.0, distDensity.density(0.5), 1.0e-10);

        // Test cumulativeProbability method
        assertEquals(0.0, distDensity.cumulativeProbability(0.0), 1.0e-10);
        assertEquals(0.5, distDensity.cumulativeProbability(0.5), 1.0e-10);
        assertEquals(1.0, distDensity.cumulativeProbability(1.0), 1.0e-10);

        // Test sample method
        UniformRealDistribution distSample = new UniformRealDistribution(new Well19937c(), 0.5, 1.5);
        for (int i = 0; i < 1000; i++) {
            double sampleValue = distSample.sample();
            assertTrue(sampleValue >= 0.5 && sampleValue < 1.5);
        }

        // Test getNumericalMean method
        assertEquals(0.75, distCustom.getNumericalMean(), 1.0e-10);

        // Test getNumericalVariance method
        assertEquals(0.0625, distCustom.getNumericalVariance(), 1.0e-10);

        // Test getSupportLowerBound method
        assertEquals(0.5, distCustom.getSupportLowerBound(), 0.0);

        // Test getSupportUpperBound method
        assertEquals(1.5, distCustom.getSupportUpperBound(), 0.0);

        // Test isSupportLowerBoundInclusive method
        assertTrue(distCustom.isSupportLowerBoundInclusive());

        // Test isSupportUpperBoundInclusive method
        assertFalse(distCustom.isSupportUpperBoundInclusive());

        // Test isSupportConnected method
        assertTrue(distCustom.isSupportConnected());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() throws NumberIsTooLargeException {
        new UniformRealDistribution(1.0, 0.5);
    }
}