import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstruction() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(distribution.isSupportUpperBoundExclusive());
        assertFalse(distribution.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionCustomConstruction() {
        Well19937c rng = new Well19937c();
        double lower = 2.5;
        double upper = 4.5;
        UniformRealDistribution distribution = new UniformRealDistribution(rng, lower, upper, 0.0001);
        assertEquals(lower, distribution.getLowerBound(), 0.0);
        assertEquals(upper, distribution.getUpperBound(), 0.0);
        assertEquals(3.0, distribution.getNumericalMean(), 0.0);
        assertEquals(0.259803926406, distribution.getNumericalVariance(), 0.00001);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertFalse(distribution.isSupportConnected());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionIllegalConstruction() {
        Well19937c rng = new Well19937c();
        double lower = 4.0;
        double upper = 4.0;
        new UniformRealDistribution(rng, lower, upper, 0.0);
    }

    @Test
    public void testUniformRealDistributionMethods() {
        Well19937c rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0.0, 1.0);
        assertEquals(0.0, distribution.density(0.0), 1e-8);
        assertEquals(0.0, distribution.density(1.0), 1e-8);
        assertEquals(0.25, distribution.density(0.5), 1e-8);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 1e-8);
        assertEquals(0.0, distribution.cumulativeProbability(0.0), 1e-8);
        assertEquals(1.0, distribution.cumulativeProbability(1.0), 1e-8);
        assertEquals(0.5, distribution.sample(), 1e-8);
        assertEquals(0.5, distribution.sample(), 1e-8);
    }
}