import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;

public class UniformRealDistributionTest {

    @org.junit.Before
    public void setUp() {
        random = new Well19937c();
    }

    private Well19937c random;

    @org.junit.Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertEquals(1.0 / (1.0 - 0.0), distribution.density(0.5), 0.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorWithInvalidParameters() {
        new UniformRealDistribution(1.0, 0.0);
    }

    @org.junit.Test
    public void testUniformRealDistributionCustomConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.4, 1e-10);
        assertEquals(0.1, distribution.getLowerBound(), 0.0);
        assertEquals(0.4, distribution.getUpperBound(), 0.0);
        assertEquals(1.0 / (0.4 - 0.1), distribution.density(0.25), 0.0);
        assertEquals(0.25, distribution.cumulativeProbability(0.25), 0.0);
    }

    @org.junit.Test
    public void testUniformRealDistributionNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.255, distribution.getNumericalMean(), 0.005);
    }

    @org.junit.Test
    public void testUniformRealDistributionNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.001666666667, distribution.getNumericalVariance(), 0.00001);
    }

    @org.junit.Test
    public void testUniformRealDistributionSupport() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.1, distribution.getSupportLowerBound(), 0.0);
        assertEquals(0.4, distribution.getSupportUpperBound(), 0.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @org.junit.Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.4);
        double sample = distribution.sample();
        assertTrue(sample >= 0.1 && sample <= 0.4);
    }
}