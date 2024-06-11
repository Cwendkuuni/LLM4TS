import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals("Mean should be set correctly", 5.0, distribution.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbabilityForZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        assertEquals("Probability for zero should be e^(-mean)", 0.2706602536068945, distribution.probability(0), 0.0001);
    }

    @Test
    public void testProbabilityForPositiveValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double probability = distribution.probability(2);
        assertEquals("Probability for a positive value should match the Poisson distribution formula", 0.17545896967723828, probability, 0.0001);
    }

    @Test
    public void testProbabilityForLargeValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(100.0);
        double probability = distribution.probability(100);
        assertEquals("Probability for a large value should be close to 1", 0.9999999999713384, probability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityForZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = distribution.cumulativeProbability(0);
        assertEquals("Cumulative probability for zero should be 0.368 (rounded to 4 decimal places)", 0.36792661, cumulativeProbability, 0.000001);
    }

    @Test
    public void testCumulativeProbabilityForMaxValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = distribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals("Cumulative probability for max value should be 1", 1.0, cumulativeProbability, 0.000001);
    }

    @Test
    public void testCumulativeProbabilityForNegativeValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        try {
            distribution.cumulativeProbability(-1);
            fail("Should have thrown MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testNormalApproximateProbabilityForZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double normalApproximateProbability = distribution.normalApproximateProbability(0);
        assertEquals("Normal approximate probability for zero should be 0.368 (rounded to 4 decimal places)", 0.36792661, normalApproximateProbability, 0.000001);
    }

    @Test
    public void testNormalApproximateProbabilityForLargeValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(100.0);
        double normalApproximateProbability = distribution.normalApproximateProbability(100);
        assertEquals("Normal approximate probability for a large value should be close to 1", 0.9999999999713384, normalApproximateProbability, 0.000001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        int sampledValue = distribution.sample();
        assertTrue("Sampled value should be within the range of the Poisson distribution", sampledValue >= 0 && sampledValue <= 1000 * distribution.getMean());
    }
}