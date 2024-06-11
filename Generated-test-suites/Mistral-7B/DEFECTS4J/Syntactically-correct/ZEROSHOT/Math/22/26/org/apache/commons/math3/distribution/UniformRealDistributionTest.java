import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0.0, urd.getLowerBound(), 0.0);
        assertEquals(1.0, urd.getUpperBound(), 0.0);
        assertNotNull(urd.getRandomGenerator());
        assertTrue(urd instanceof UniformRealDistribution);
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() {
        double lowerBound = 1.5;
        double upperBound = 3.5;
        UniformRealDistribution urd = new UniformRealDistribution(lowerBound, upperBound);
        assertEquals(lowerBound, urd.getLowerBound(), 0.0);
        assertEquals(upperBound, urd.getUpperBound(), 0.0);
        assertNotNull(urd.getRandomGenerator());
        assertTrue(urd instanceof UniformRealDistribution);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidConstructor() {
        double lowerBound = 3.5;
        double upperBound = 1.5;
        new UniformRealDistribution(lowerBound, upperBound);
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithRandomGenerator() {
        double lowerBound = 1.5;
        double upperBound = 3.5;
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution urd = new UniformRealDistribution(rng, lowerBound, upperBound);
        assertEquals(lowerBound, urd.getLowerBound(), 0.0);
        assertEquals(upperBound, urd.getUpperBound(), 0.0);
        assertEquals(rng, urd.getRandomGenerator());
        assertTrue(urd instanceof UniformRealDistribution);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution urd = new UniformRealDistribution();
        for (double d = urd.getLowerBound(); d <= urd.getUpperBound(); d++) {
            double density = urd.density(d);
            double eps = 1e-5;
            assertEquals("Incorrect density at " + d, 1.0 / (urd.getUpperBound() - urd.getLowerBound()), density, eps);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution();
        for (double d = urd.getLowerBound(); d <= urd.getUpperBound(); d++) {
            double cumProb = urd.cumulativeProbability(d);
            double eps = 1e-5;
            if (d == urd.getLowerBound()) {
                assertEquals("Incorrect cumulative probability at lower bound", 0.0, cumProb, eps);
            } else {
                double prevCumProb = urd.cumulativeProbability(d - 1e-9);
                double nextCumProb = urd.cumulativeProbability(d + 1e-9);
                assertEquals("Incorrect cumulative probability at " + d, (d - urd.getLowerBound()) / (urd.getUpperBound() - urd.getLowerBound()), cumProb, eps);
                assertTrue("Cumulative probabilities not monotonically increasing: prevCumProb = " + prevCumProb + ", cumProb = " + cumProb + ", nextCumProb = " + nextCumProb, prevCumProb < cumProb && cumProb < nextCumProb);
            }
        }
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution urd = new UniformRealDistribution();
        for (int i = 0; i < 10000; i++) {
            double sample = urd.sample();
            assertTrue("Sample out of bounds", urd.getLowerBound() <= sample && sample < urd.getUpperBound());
        }
    }

    @Test
    public void testUniformRealDistributionNumericalMean() {
        UniformRealDistribution urd = new UniformRealDistribution();
        double numericalMean = urd.getNumericalMean();
        double expectedMean = (urd.getLowerBound() + urd.getUpperBound()) / 2;
        assertEquals("Incorrect numerical mean", expectedMean, numericalMean, 1e-5);
    }

    @Test
    public void testUniformRealDistributionNumericalVariance() {
        UniformRealDistribution urd = new UniformRealDistribution();
        double numericalVariance = urd.getNumericalVariance();
        double expectedVariance = Math.pow((urd.getUpperBound() - urd.getLowerBound()), 2) / 12;
        assertEquals("Incorrect numerical variance", expectedVariance, numericalVariance, 1e-5);
    }

    @Test
    public void testUniformRealDistributionSupport() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals("Incorrect support lower bound", urd.getSupportLowerBound(), urd.getLowerBound(), 0.0);
        assertEquals("Incorrect support upper bound", urd.getSupportUpperBound(), urd.getUpperBound(), 0.0);
        assertTrue("Support lower bound is inclusive", urd.isSupportLowerBoundInclusive());
        assertFalse("Support upper bound is inclusive", urd.isSupportUpperBoundInclusive());
        assertTrue("Support is connected", urd.isSupportConnected());
    }
}